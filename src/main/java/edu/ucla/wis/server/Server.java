package edu.ucla.wis.server;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import edu.ucla.wis.common.Conf;
import edu.ucla.wis.common.ConfUtil;
import edu.ucla.wis.common.Log;
import edu.ucla.wis.config.Util;
import edu.ucla.wis.http.ParamUtil;
import edu.ucla.wis.jdbc.JdbcClient;
import edu.ucla.wis.web.Asset;
import edu.ucla.wis.web.Type;

public class Server extends NanoHTTPD {
	protected JdbcClient client;

	public Server() {
		super(Conf.websitePort);
		if (Conf.connectDB) {
			client = new JdbcClient();
		}
	}
	
	@Override
	public Response serve(IHTTPSession session) {
		String uri = session.getUri();
		ParamUtil params = new ParamUtil(session.getParms());

		try {
			if (uri != null) {
				//record uri request
				Log.log(uri);

				Map<String,String> paras = session.getParms();
				for (Map.Entry<String, String> entry: paras.entrySet()) {
					System.out.println(entry.getKey() + "@@" + entry.getValue());
				}

				// TODO: fix this bug
				if (uri.contains("favicon") || uri.contains("http")) {
					return null;
				}
				
				return Util.generateResponse(uri, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(Status.OK, Type.MIME_HTML.name(), Asset.getException(e));
		}

		return null;
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			Log.warn("Missing config file path. Use conf in Programs.");
		}
		else {
			Path path = Paths.get(args[0]);
			Log.warn("Config file " + path + " loaded ...");
			Log.warn("[Config]");	
			ConfUtil.loadConf(path);
		}

		ConfUtil.printArgs();
		if (Conf.connectDB) {
			JdbcClient.load();
		}
		ServerRunner.run(Server.class);
	}
}
