package edu.ucla.wis.config;

import edu.ucla.wis.web.Asset;
import edu.ucla.wis.web.Type;

import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.NanoHTTPD.Response.Status;

/**
 * Customized Mapping: You are free to modify this file to add your own custom mapping
 * @author victor
 *
 */
public class CustomMapping extends BasicMapping {
	public CustomMapping() {
		super();
		
		bindAction("stopInterval", new ResponseAction() {
			@Override
			public Response perform(String uri) {
				// TODO: is it possible to use thread control here, eg. like maintain a global variable,
				// change response based on the state of this variable
				return new Response(Status.OK, Type.MIME_PLAINTEXT.name(), Asset.getNone());
			}
		});
	}
}