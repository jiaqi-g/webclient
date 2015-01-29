package edu.ucla.wis.config;

import fi.iki.elonen.NanoHTTPD.Response;

public interface ResponseAction {
	public Response perform(String uri);
}