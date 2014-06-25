package edu.ucla.boost.server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import org.markdown4j.Markdown4jProcessor;

import edu.ucla.boost.common.Log;
import edu.ucla.boost.common.Time;
import edu.ucla.boost.http.ParamUtil;
import edu.ucla.boost.math.NormalDist;

public class PageHelper {

	static Markdown4jProcessor processor = new Markdown4jProcessor();
	public static final int pointsAfterDots = 6;
	
	private static String makePage(String title, String body) {
		StringBuilder res = new StringBuilder();
		//body = body.replaceAll("(\r\n|\n)", "<br />");

		res.append("<html>");
		res.append("<head>");
		res.append("<title>");
		res.append(title);
		res.append("</title>");
		res.append("</head>");
		res.append("<body bgcolor=\"white\">");
		res.append(body);
		res.append("</body>");
		res.append("</html>");

		return res.toString();
	}

	public static String getDefaultPage(String content) {
		return makePage("Default Page", content);
	}

	public static String getOutlinePart(String outline, String content) {
		StringBuilder res = new StringBuilder();
		res.append("<h2>" + outline + "</h2>");
		res.append(content);

		return res.toString();
	}

	public static String convertMarkdownToHtml(String s) {
		StringBuilder sb = new StringBuilder();
		String html = "";
		try {
			sb.append("``` java \n");
			sb.append(s);
			sb.append("\n ```");
			html = processor.process(sb.toString());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return html;
	}

	public static String makePlan(ResultSet rs) throws SQLException {
		StringBuilder res = new StringBuilder();
		while (rs.next()) {
			res.append(rs.getObject(1));
			res.append("<br />");
		}
		return res.toString();
	}

	public static String makeAll(ResultSet rs, ParamUtil params, Time time)  throws SQLException {
		EvaluationResultHelper eval = new EvaluationResultHelper();
		setTable(rs, params, eval);
		setTime(time, eval);
		return eval.make();
	}

	/*
	public static String makeTable(ResultSet rs, ParamUtil params)  throws SQLException {
		EvaluationResultHelper eval = new EvaluationResultHelper();
		setTable(rs, params, eval);
		return eval.make();
	}*/

	private static void setTime(Time time, EvaluationResultHelper eval) throws SQLException {
		eval.setTime(time);
	}
	
	/*
	private static String format(double value) {
		Formatter fmt = new Formatter();
	    String rs = fmt.format("%." + pointsAfterDots +"e", value).toString();
	    fmt.close();
	    
	    return rs;
	}*/
	
	/*
	private static String formatConfidence(String quantile) {
		String[] tokens = quantile.substring(1, quantile.length() - 1).split(",");
		
		Formatter fmt1 = new Formatter();
		Formatter fmt2 = new Formatter();
	    String rs = "[" + fmt1.format("%." + pointsAfterDots +"e", Double.parseDouble(tokens[0])).toString() + ", " 
	    				+ fmt2.format("%." + pointsAfterDots +"e", Double.parseDouble(tokens[1])).toString() + "]";
	    fmt1.close();
	    fmt2.close();
	    
	    return rs;
	}*/

	private static void setTable(ResultSet rs, ParamUtil params, EvaluationResultHelper eval) throws SQLException {
		if (rs == null) {
			return;
		}

		boolean doVariance = params.doVariance();
		boolean doQuantile = params.doQuantile();
		boolean doConfidence = params.doConfidence();
		
		Log.log(doVariance + "\t" + doQuantile + "\t" + doConfidence);
		
		int columnCount = rs.getMetaData().getColumnCount();
		Log.log("column count: " + columnCount);

		List<Object> head = new ArrayList<Object>();
		List<List<Object>> body = new ArrayList<List<Object>>();
		List<List<NormalDist>> dists = new ArrayList<List<NormalDist>>();
		
		int index = 0;
		/*
		 * construct body
		 */
		while (rs.next()) {
			List<Object> row = new ArrayList<Object>();
			
			List<NormalDist> distLst = new ArrayList<NormalDist>();
			for (int i=1; i <= columnCount; i++) {
				double mean = 0;
				double variance = 0;

				Object obj = rs.getObject(i);
				if (obj != null) {
					String str = obj.toString().trim();
					if (str.startsWith("[") && str.endsWith("]")) {
						str = str.substring(1, str.length() - 1);
						String[] tokens = str.split(",");

						String val = null;
						if (tokens.length == 3) {
							val = tokens[0];
							mean = Double.parseDouble(tokens[1]);
							variance = Double.parseDouble(tokens[2]);
						} else if (tokens.length == 4){
							val = "[" + tokens[0] + "," + tokens[1] + "]";
							mean = Double.parseDouble(tokens[2]);
							variance = Double.parseDouble(tokens[3]);
						} else {
							System.out.println("Unknown Results");
						}
						
						String s = "" + mean + " ";
						if(doVariance) {
							s += "(" + variance + ")";
						} else if (doQuantile) {
							s += "(" + val + ")";
						} else if (doConfidence) {
							s += "(" + val + ")";
						}
						
						row.add(s);
						distLst.add(new NormalDist(mean,variance));
					} else {
						row.add(str);
						distLst.add(null);
					}
				}
				else {
					row.add("null");
					distLst.add(null);
				}
			}
			body.add(row);
			Log.log("Dist in row " + index + " " + distLst);
			index++;
			dists.add(distLst);
		}
		
		/*
		 * construct head
		 */
		for(int i = 1; i <= columnCount; i ++) {
			String colName = rs.getMetaData().getColumnName(i);
			
			if (colName.startsWith("_existence")) {
				colName = "Existence_Probability";
			}
			
			if (dists.get(0).get(i-1) != null) {
				if (doVariance) {
					colName += " (variance)";
				}
				if (doQuantile) {
					colName += " (" + params.getQuantile().getQuantile() + "%)";
				}
				if (doConfidence) {
					colName += " ([" + params.getConfidence().getConfidenceFrom() + "%, " +  params.getConfidence().getConfidenceTo() + "%])";
				}
			}
			
			head.add(colName);
		}

		eval.setTable(head, body, dists);
	}
	
	public static String makeOutlinePage(String res) {
		return makePage("Outline", res);
	}

	public static void main(String[] args) {
		//System.out.println(convertMarkdownToHtml("``` java \n if (a > 3) {\n     moveShip(5 * gravity, DOWN); \n } \n```"));
	}
}