package boost.web;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import fi.iki.elonen.ServerRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;

import boost.jdbc.JdbcClient;
import boost.jdbc.Log;
import boost.server.PageHelper;

public class SimpleServer extends NanoHTTPD {
	
	/**
	 * notice: we should put absolute address of website in the root index.html file
	 */
	private static String defaultURI = "/index.html";
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
					mbuffer = Asset.open(uri.substring(1));
					return new Response(Status.OK, Type.MIME_JS, mbuffer);
				} else if (uri.contains(".css")) {
					mbuffer = Asset.open(uri.substring(1));
					return new Response(Status.OK, Type.MIME_CSS, mbuffer);
				} else if (uri.contains(".png")) {
					mbuffer = Asset.open(uri.substring(1));
					return new Response(Status.OK, Type.MIME_PNG, mbuffer); 
				} else if (uri.contains(".txt")) {
					mbuffer = Asset.open(uri.substring(1));
					return new Response(Status.OK, Type.MIME_PLAINTEXT, mbuffer);
				} else if (uri.contains(".htm") || uri.contains(".html")) {
					mbuffer = Asset.open(uri.substring(1));
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
					Log.log("Can not find MIME type for " + uri.substring(1) + " open default page");
					
					uri = defaultURI;
					File request = new File(uri.substring(1));
					mbuffer = new FileInputStream(request);
					FileNameMap fileNameMap = URLConnection.getFileNameMap();
					String mimeType = fileNameMap.getContentTypeFor(uri.substring(1));

					Response streamResponse = new Response(Status.OK, mimeType, mbuffer);
					//Random rnd = new Random();
					//String etag = Integer.toHexString(rnd.nextInt());
					//streamResponse.addHeader("ETag", etag);
					streamResponse.addHeader("Connection", "Keep-alive");
					return streamResponse;
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