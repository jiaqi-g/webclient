package edu.ucla.boost.server;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.markdown4j.Markdown4jProcessor;

import edu.ucla.boost.common.Conf;
import edu.ucla.boost.common.Log;
import edu.ucla.boost.common.Param;
import edu.ucla.boost.common.Time;
import edu.ucla.boost.http.ParamUtil;
import edu.ucla.boost.math.Confidence;
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

	public static String makeTable(ResultSet rs, ParamUtil params, Time time) throws SQLException {
		if (rs == null) {
			return "";
		}

		//construct head
		int columnCount = rs.getMetaData().getColumnCount();
		Log.log("column count: " + columnCount);

		List<Object> head = new ArrayList<Object>();
		for (int i=1; i<=columnCount; i++) {
			head.add(rs.getMetaData().getColumnName(i));
		}

		boolean doVariance = params.doVariance();
		boolean doExist = params.doExist();
		boolean doQuantile = params.doQuantile();
		boolean doConfidence = params.doConfidence();

		if (doVariance) {
			head.add(Param.VARIANCE_COLUMN_NAME);
		}
		if (doExist) {
			head.add(Param.EXIST_COLUMN_NAME);
		}
		if (doQuantile) {
			Quantile quantile = params.getQuantile();
			head.add(Param.QUANTILE_COLUMN_NAME + "_" + quantile.getQuantile() + "%");
		}
		if (doConfidence) {
			Confidence confidence = params.getConfidence();
			head.add(Param.CONFIDENCE_COLUMN_NAME + "_(" + confidence.getConfidenceFrom() + "%, " +  confidence.getConfidenceTo() + "%)");
		}

		//construct body
		
		//		int rowCount = 0;
		//		if (rs.last()) {
		//			rowCount = rs.getRow();
		//			Log.log("ResultSetSize: " + rowCount);
		//			rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
		//		}

		//if (rowCount < Conf.resultSetSizeLimit) {
		
		List<List<Object>> body = new ArrayList<List<Object>>();
		while (rs.next()) {
			List<Object> row = new ArrayList<Object>();
			//column count starts from 1
			for (int i=1; i<=columnCount; i++) {
				Object obj = rs.getObject(i);

				if (obj != null) {
					String s = obj.toString().trim();
					row.add(s);
				}
				else {
					row.add("null");
				}
			}

			//TODO: do not need this in really
			if (doVariance) {
				row.add("");
			}
			if (doExist) {
				row.add("");
			}
			if (doQuantile) {
				row.add("");
			}
			if (doConfidence) {
				row.add("");
			}

			body.add(row);
		}
		//		} else {
		//			List<Object> row = new ArrayList<Object>();
		//			row.add("ResultSetSize: " + rowCount + " exceeds limit " + Conf.resultSetSizeLimit);
		//			body.add(row);
		//		}

		//Log.log("result length: " + body.size());
		return new TableHelper(head, body, time).makeHtmlTable();
	}
	
	public static String makeOutlinePage(String res) {
		return makePage("Outline", res);
	}

	public static void main(String[] args) {
		//System.out.println(convertMarkdownToHtml("``` java \n if (a > 3) {\n     moveShip(5 * gravity, DOWN); \n } \n```"));
	}
	
}
