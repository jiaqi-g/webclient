package edu.ucla.boost.server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.markdown4j.Markdown4jProcessor;

import edu.ucla.boost.common.Log;
import edu.ucla.boost.common.Time;
import edu.ucla.boost.http.ParamUtil;
import edu.ucla.boost.math.Confidence;
import edu.ucla.boost.math.NormalDist;
import edu.ucla.boost.math.Quantile;

public class PageHelper {

	static Markdown4jProcessor processor = new Markdown4jProcessor();

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

	public static String makeTable(ResultSet rs, ParamUtil params)  throws SQLException {
		EvaluationResultHelper eval = new EvaluationResultHelper();
		setTable(rs, params, eval);
		return eval.make();
	}

	private static void setTime(Time time, EvaluationResultHelper eval) throws SQLException {
		eval.setTime(time);
	}

		
	private static void setTable(ResultSet rs, ParamUtil params, EvaluationResultHelper eval) throws SQLException {
		if (rs == null) {
			return;
		}
		
		boolean doVariance = params.doVariance();
		boolean doQuantile = params.doQuantile();
		boolean doConfidence = params.doConfidence();
		String quan = null, conf = null;
		
		if (doQuantile) {
				Quantile quantile = params.getQuantile();
				quan = "_" + quantile.getQuantile() + "%";
		}
		if (doConfidence) {
				Confidence confidence = params.getConfidence();
				conf = "_[" + confidence.getConfidenceFrom() + "%, " +  confidence.getConfidenceTo() + "%]";
		}
		
		System.out.println(doVariance + "\t" + doQuantile + "\t" + doConfidence);

		//construct head
		int columnCount = rs.getMetaData().getColumnCount();
		Log.log("column count: " + columnCount);
		
		/*
		 * read first row to construct head
		 */
		List<Boolean> headFlags = new ArrayList<Boolean>();
		List<Object> firstRow = new ArrayList<Object>();
		
		List<Object> head = new ArrayList<Object>();
		List<NormalDist> dists = new ArrayList<NormalDist>();
		List<List<Object>> body = new ArrayList<List<Object>>();
		
		
		double fmean = 0;
		double fvariance = 1;
		if(rs.next()) {
			for (int i=1; i <= columnCount; i++) {
				Object obj = rs.getObject(i);
				boolean flag = false;
				
				if (obj != null) {
					String str = obj.toString().trim();
					if(str.startsWith("[") && str.endsWith("]")) {
						flag = true;						
						str = str.substring(1, str.length() - 1);
						String[] tokens = str.split(",");

						String val = null;
						if(tokens.length == 3) {
							val = tokens[0];
							fmean = Double.parseDouble(tokens[1]);
							fvariance = Double.parseDouble(tokens[2]);
						} else if (tokens.length == 4){
							val = "[" + tokens[0] + "," + tokens[1] + "]";
							fmean = Double.parseDouble(tokens[2]);
							fvariance = Double.parseDouble(tokens[3]);
						} else {
							System.out.println("Unknown Results");
						}
						
						
						firstRow.add(fmean);
						if(doVariance) {
							firstRow.add(fvariance);
						} else if(doQuantile || doConfidence) {
							firstRow.add(val);
						}
						
						
					} else {
						firstRow.add(str);
					}
			} else {
				firstRow.add("null");
			} 
				
			headFlags.add(flag);
			}
		}
		
		
		/*
		 * fill data into table
		 */
		
		body.add(firstRow);
		dists.add(new NormalDist(fmean,fvariance));
		boolean colTag = (headFlags.size() == columnCount);
		
		for(int i = 1; i <= columnCount; i ++) {
			
			String colName = rs.getMetaData().getColumnName(i);
			if(colName.startsWith("_existence")) {
				colName = "Existence_Probability";
			}
			head.add(colName);
			
			if(colTag) {
				if(headFlags.get(i - 1)) {
					if(doVariance) {
						head.add(colName + "_Variance");
					} else if(doQuantile) {
						head.add(colName + quan);
					} else if(doConfidence) {
						head.add(colName + conf);
					}
				}
			}
			
		}
		
		// for debugging
		for(Object headName:head) {
			System.out.print(headName + "\t");
		}
		System.out.println();

		while (rs.next()) {
			List<Object> row = new ArrayList<Object>();

			// TODO current, we still suppose only google chart per line
			double mean = 0;
			double variance = 1;
			
			for (int i=1; i <= columnCount; i++) {
				
				Object obj = rs.getObject(i);

				if (obj != null) {
					String str = obj.toString().trim();
					if(str.startsWith("[") && str.endsWith("]")) {
						
						str = str.substring(1, str.length() - 1);
						String[] tokens = str.split(",");
						
						String val = null;
						if(tokens.length == 3) {
							val = tokens[0];
							mean = Double.parseDouble(tokens[1]);
							variance = Double.parseDouble(tokens[2]);
						} else if (tokens.length == 4){
							val = "(" + tokens[0] + "," + tokens[1] + ")";
							mean = Double.parseDouble(tokens[2]);
							variance = Double.parseDouble(tokens[3]);
						} else {
							System.out.println("Unknown Results");
						}
						
						
						row.add(fmean);
						if(doVariance) {
							row.add(fvariance);
						} else if(doQuantile || doConfidence) {
							row.add(val);
						}
						
					} else {
						row.add(str);
					}
				}
				else {
					row.add("null");
				}
			}

			body.add(row);
			dists.add(new NormalDist(mean,variance));
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
