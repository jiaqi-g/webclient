package edu.ucla.wis.config;

import java.io.File;
import java.util.Map;

import edu.ucla.wis.plugins.WebServerPlugin;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

public abstract class ResponseAction implements WebServerPlugin {
	
	public abstract Response perform(String uri);

	@Override
	public void initialize(Map<String, String> commandLineOptions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canServeUri(String uri, File rootDir) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Response serveFile(String uri, Map<String, String> headers,
			IHTTPSession session, File file, String mimeType) {
		// TODO Auto-generated method stub
		return null;
	}
}