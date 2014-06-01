package edu.ucla.boost.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.Map;

import edu.ucla.boost.jdbc.JdbcClient;

import com.sun.net.httpserver.*;

public class Server {

	public static void main(String[] args) throws Exception {
		JdbcClient.load();
		
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
		//server.createContext("/stackoverflow", new MyHandler());
		HttpContext context = server.createContext("/search", new SearchHandler());

		server.createContext("/", new MyHandler());
		server.setExecutor(null); // creates a default executor

		context.getFilters().add(new ParameterFilter());

		System.out.println("Server starts ... ");
		server.start();
	}
	
	static class SearchHandler implements HttpHandler {
		@SuppressWarnings("unchecked")
		public void handle(HttpExchange t) throws IOException {
			Map<String, Object> params = (Map<String, Object>) t.getAttribute("parameters");
			String query = (String) params.get("content");
			String res = null;
			try {
				res = PageHelper.makeTable(new JdbcClient().executeSQL(query));
			}
			catch (SQLException e) {
				res = "SQL execution error! \n" + e.getMessage();
				e.printStackTrace();
			}
			
			//encode to utf-8 to avoid errors
			ByteBuffer rs = Charset.forName("UTF-8").encode(PageHelper.makeOutlinePage(res));
			//String response = rs.toString();
			//System.out.println(response);
			
			t.sendResponseHeaders(200, rs.capacity());
			OutputStream os = t.getResponseBody();
			os.write(rs.array());
			os.close();
		}
	}
	
	static class DefaultHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String response = PageHelper.getDefaultPage("Default page");
			//response = new ucla.stackoverflow.Main().test();

			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

	static class MyHandler implements HttpHandler {
		public void handle(HttpExchange t) throws IOException {
			String content = "<form name=\"input\" action=\"search\" method=\"get\">"
					+ "Content: <input type=\"text\" name=\"content\">"
					+ "<input type=\"submit\" value=\"Submit\" />"
					+ "</form></body></html>";
			String response = PageHelper.getDefaultPage(content);

			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

}