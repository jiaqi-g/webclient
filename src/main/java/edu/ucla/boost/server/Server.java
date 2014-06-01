package edu.ucla.boost.server;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import fi.iki.elonen.ServerRunner;

import java.io.InputStream;

import edu.ucla.boost.Log;
import edu.ucla.boost.http.ParamUtil;
import edu.ucla.boost.jdbc.JdbcClient;
import edu.ucla.boost.web.Asset;
import edu.ucla.boost.web.Type;

public class Server extends NanoHTTPD {
	//private static String debugQuery = "select * from lineitem limit 10";
	
	public Server() {
		super(8080);
	}
	
	@Override
	public Response serve(IHTTPSession session) {
		
		String uri = session.getUri();
		ParamUtil params = new ParamUtil(session.getParms());
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
					String query = params.getSelectQueryString();
					if (query == null) {
						return null;
					}
					return new Response(Status.OK, Type.MIME_HTML, PageHelper.makeTable(new JdbcClient().executeSQL(query), params));
				} else if (uri.equals("/plan")) {
					String query = params.getExplainQueryString();
					if (query == null) {
						return null;
					}
					return new Response(Status.OK, Type.MIME_HTML, PageHelper.makePlan(new JdbcClient().executeSQL(query)));
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
		ServerRunner.run(Server.class);
	}
}