package edu.ucla.boost.server;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import fi.iki.elonen.ServerRunner;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.List;

import edu.ucla.boost.common.Conf;
import edu.ucla.boost.common.ConfUtil;
import edu.ucla.boost.common.Log;
import edu.ucla.boost.common.Time;
import edu.ucla.boost.common.TimeUtil;
import edu.ucla.boost.http.ParamUtil;
import edu.ucla.boost.jdbc.JdbcClient;
import edu.ucla.boost.web.Asset;
import edu.ucla.boost.web.Type;
import edu.ucla.boost.web.VanillaBootstrapRunner;

public class Server extends NanoHTTPD {

	public Server() {
		super(Conf.port);
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
				
				// System.out.println(uri);
				
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
					//support batch execution
					List<String> sqls = params.getQueryList();
					JdbcClient client = new JdbcClient();
					ResultSet rs = null;
					for (String sql: sqls) {
						rs = client.executeSQL(sql);
					}
					return new Response(Status.OK, Type.MIME_HTML,
							PageHelper.makeTable(rs, params));
				} else if (uri.equals("/compare")) {
					//support batch execution
					List<String> sqls = params.getQueryList();
					JdbcClient client = new JdbcClient();
					ResultSet rs = null;
					
					String selectSQL = null;
					for (String sql: sqls) {
						if (sql.startsWith("select")) {
							selectSQL = sql;
							continue;
						}
						rs = client.executeSQL(sql);
					}
					
					double abmTime = 0.0;
					double closeFormTime = 0.0;
					double vanillaTime = 0.0;
					if (selectSQL != null) {
						Log.log("run vanilla bootstrap ...");
						client.closeABM();
						TimeUtil.start();
						client.executeSQL(selectSQL);
						vanillaTime = TimeUtil.getPassedSeconds();
						
						Log.log("run abm ...");
						client.openABM();
						TimeUtil.start();
						client.executeSQL(selectSQL);
						abmTime = TimeUtil.getPassedSeconds();
						
						Log.log("run closed form ...");
						client.openABM();
						TimeUtil.start();
						rs = client.executeSQL(selectSQL);
						closeFormTime = TimeUtil.getPassedSeconds();
					} else {
						return null;
					}
					
					return new Response(Status.OK, Type.MIME_HTML,
							PageHelper.makeAll(rs, params, new Time(abmTime, closeFormTime, vanillaTime)));
				} else if (uri.equals("/plan")) {
					//Log.log("require query plan");
					List<String> sqls = params.getQueryList();
					JdbcClient client = new JdbcClient();
					for (String sql: sqls) {
						if (sql.toLowerCase().startsWith("select")) {
							client.executeSQL("explain " + sql);
						}
					}
					mbuffer = Asset.getPlan();
					return new Response(Status.OK, Type.MIME_PLAINTEXT, mbuffer);
				} else if (uri.contains(".hive")) {
					mbuffer = Asset.open(uri);
					return new Response(Status.OK, Type.MIME_PLAINTEXT, mbuffer);
				} else if (uri.contains("/vallina/")) {
					System.out.println("We are here!");
					List<String> sqls = params.getQueryList();
					JdbcClient client = new JdbcClient();
					String selectSQL = null;
					for (String sql: sqls) {
						if (sql.startsWith("select")) {
							selectSQL = sql;
							continue;
						}
						client.executeSQL(sql);
					}
					if (selectSQL != null) {
						VanillaBootstrapRunner runner = new VanillaBootstrapRunner(20,selectSQL);
						new Thread(runner).start();
					}
					mbuffer = Asset.openDefault();
					return new Response(Status.OK, Type.MIME_HTML, mbuffer);
				}
				else {
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