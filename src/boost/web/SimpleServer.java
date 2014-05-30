package boost.web;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import fi.iki.elonen.ServerRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;

import boost.jdbc.Log;

public class SimpleServer extends NanoHTTPD {
	
	public SimpleServer() {
		super(8080);
	}
	
	@Override
	public Response serve(IHTTPSession session) {
		String uri = session.getUri();
		//Method method = session.getMethod();
		Map<String, String> header = session.getHeaders();
		
		//Log.d(TAG,"SERVE ::  URI "+uri);
		final StringBuilder buf = new StringBuilder();
		for (Entry<String, String> kv : header.entrySet())
			buf.append(kv.getKey() + " : " + kv.getValue() + "\n");
		
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
				} else {
					Log.log("Opening file "+ uri.substring(1));
					File request = new File(uri.substring(1));
					mbuffer = new FileInputStream(request);
					FileNameMap fileNameMap = URLConnection.getFileNameMap();
					String mimeType = fileNameMap.getContentTypeFor(uri);

					Response streamResponse = new Response(Status.OK, mimeType, mbuffer);
					//Random rnd = new Random();
					//String etag = Integer.toHexString(rnd.nextInt());
					//streamResponse.addHeader("ETag", etag);
					streamResponse.addHeader("Connection", "Keep-alive");
					return streamResponse;
				}
			}
		} catch (IOException e) {
			//Log.d(TAG,"Error opening file"+uri.substring(1));
			Log.log("Error opening file "+ uri.substring(1));
			e.printStackTrace();
		}
		
		return null;
	}

	public static void main(String[] args) {
		ServerRunner.run(SimpleServer.class);
	}
}