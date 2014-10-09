package edu.ucla.boost.server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formatter;

import org.markdown4j.Markdown4jProcessor;

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
	
	private static String format(double value) {
		Formatter fmt = new Formatter();
	    String rs = fmt.format("%." + pointsAfterDots +"e", value).toString();
	    fmt.close();
	    
	    return rs;
	}
	
	private static String formatConfidence(String quantile) {
		String[] tokens = quantile.substring(1, quantile.length() - 1).split(",");
		
		Formatter fmt1 = new Formatter();
		Formatter fmt2 = new Formatter();
	    String rs = "[" + fmt1.format("%." + pointsAfterDots +"e", Double.parseDouble(tokens[0])).toString() + ", " 
	    				+ fmt2.format("%." + pointsAfterDots +"e", Double.parseDouble(tokens[1])).toString() + "]";
	    fmt1.close();
	    fmt2.close();
	    
	    return rs;
	}
	
	public static String makeOutlinePage(String res) {
		return makePage("Outline", res);
	}

	public static void main(String[] args) {
		//System.out.println(convertMarkdownToHtml("``` java \n if (a > 3) {\n     moveShip(5 * gravity, DOWN); \n } \n```"));
	}
}