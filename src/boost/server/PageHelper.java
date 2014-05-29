package boost.server;

public class PageHelper {
	private static String makePage(String title, String body) {
		StringBuilder res = new StringBuilder();
		
		body = body.replaceAll("(\r\n|\n)", "<br />");
		
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
	
	public static String makeOutlinePage(String res) {
		//HtmlCleaner cleaner = new HtmlCleaner();
		//String s = cleaner.getInnerHtml(cleaner.clean(res.toString()));
		//System.out.println(s);
		//return s;
		return makePage("Outline", res);
	}
}
