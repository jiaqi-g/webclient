package edu.ucla.boost.web;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import fi.iki.elonen.ServerRunner;

import java.io.InputStream;

import edu.ucla.boost.jdbc.JdbcClient;
import edu.ucla.boost.jdbc.Log;
import edu.ucla.boost.server.PageHelper;

public class SimpleServer extends NanoHTTPD {
	//private static String debugQuery = "select * from lineitem limit 10";
	
	public SimpleServer() {
		super(8080);
	}
	
	@Override
	public Response serve(IHTTPSession session) {
		String uri = session.getUri();
		//Method method = session.getMethod();
//		Map<String, String> header = session.getHeaders();
		
		//Log.d(TAG,"SERVE ::  URI "+uri);
//		final StringBuilder buf = new StringBuilder();
//		for (Entry<String, String> kv : header.entrySet())
//			buf.append(kv.getKey() + " : " + kv.getValue() + "\n");
		
		InputStream mbuffer = null;
		try {
			if (uri != null) {
				if (uri.contains("favicon")) return null;
				
				if (uri.contains(".js")) {
					mbuffer = Asset.open(uri);
					return new Response(Status.OK, Type.MIME_JS, mbuffer);
				} else if (uri.contains(".css")) {
					mbuffer = Asset.open(uri);
					return new Response(Status.OK, Type.MIME_CSS, mbuffer);
				} else if (uri.contains(".png")) {
					mbuffer = Asset.open(uri);
					return new Response(Status.OK, Type.MIME_PNG, mbuffer); 
				} else if (uri.contains(".txt")) {
					mbuffer = Asset.open(uri);
					return new Response(Status.OK, Type.MIME_PLAINTEXT, mbuffer);
				} else if (uri.contains(".htm") || uri.contains(".html")) {
					mbuffer = Asset.open(uri);
					return new Response(Status.OK, Type.MIME_HTML, mbuffer);
				} else if (uri.equals("/search")) {
					String query = session.getParms().get("query").trim();
					// TODO: query should contain limit in order to be executed now
					if (!query.contains("limit")) {
						Log.log("should contain limit else won't be executed");
						return null;
					}
					return new Response(Status.OK, Type.MIME_HTML, PageHelper.makeTable(new JdbcClient().executeSQL(query)));
				} else if (uri.equals("/plan")) {
					String query = session.getParms().get("query").trim();
					// TODO: query should start with select
					if (!query.startsWith("select")) {
						return null;
					}
					return new Response(Status.OK, Type.MIME_HTML, PageHelper.makePlan(new JdbcClient().executeSQL("explain " + query)));
				} else {
					//Log.log("Opening file "+ uri.substring(1));
					Log.log("Can not find MIME type for " + uri + ", open default page.");
					
					mbuffer = Asset.openDefault();
					return new Response(Status.OK, Type.MIME_HTML, mbuffer);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static void main(String[] args) {
		JdbcClient.load();
		ServerRunner.run(SimpleServer.class);
	}
}