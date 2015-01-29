package edu.ucla.wis.config;

import edu.ucla.wis.common.Log;
import edu.ucla.wis.web.Asset;
import edu.ucla.wis.web.Type;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class Util {
	static BasicMapping custom = new CustomMapping();
	static BasicMapping mime = new MIMEMapping();
	static ResponseAction defaultAction = new ResponseAction() {
		@Override
		public Response perform(String uri) {
			Log.log("Can not find MIME type for " + uri + ", open default page.");
			return new Response(Status.OK, Type.MIME_HTML.name(), Asset.openDefaultPage());
		}
	};

	public Util() {
	}

	/**
	 * Generate HTTP Response based on uri-action binding
	 * @param uri
	 * @param useDefault: open default page or throw an exception if no action found for this uri
	 * @return
	 */
	public static Response generateResponse(String uri, boolean useDefault) {
		//custom settings first
		ResponseAction action = custom.getResponseAction(uri);
		
		if (action == null) {
			action = mime.getResponseAction(uri);
		}

		if (action == null) {
			if (useDefault) {
				return defaultAction.perform(uri);
			} else {
				throw new RuntimeException("No corresponding action for uri: " + uri);
			}
		}

		return action.perform(uri);
	}
}