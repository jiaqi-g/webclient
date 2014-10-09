package edu.ucla.boost.server;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;
import fi.iki.elonen.ServerRunner;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import edu.ucla.boost.common.Conf;
import edu.ucla.boost.common.ConfUtil;
import edu.ucla.boost.common.Log;
import edu.ucla.boost.common.TimeUtil;
import edu.ucla.boost.http.ParamUtil;
import edu.ucla.boost.jdbc.JdbcClient;
import edu.ucla.boost.web.Asset;
import edu.ucla.boost.web.Type;

public class Server extends NanoHTTPD {

	protected Thread t = null;
	protected JdbcClient client = new JdbcClient();
	protected double execTime = 0;

	public Server() {
		super(Conf.port);
	}

	protected String setTotalTupleNumber(String line) {
		int sampleSize = 1;
		String[] tokens = line.split("=");
		String[] words = tokens[1].trim().split("_");
		if(words.length != 3) {
			Log.log(line);
			Log.log("Error in Server.java: incorrect sample table name!");
		} else {
			String tbl = words[0].toLowerCase();
			if(tbl.equals("lineitem")||tbl.equals("tmp17")||tbl.equals("tmp18")) {
				sampleSize = 6000379;
			} else if (tbl.equals("partsupp") || tbl.equals("tmp11")) {
				sampleSize = 800000;
			} else if (tbl.equals("customer")) {
				sampleSize = 150000;
			} else {
				Log.log("Error in Server.java: unknown sample table name!");
			}
			int pct = Integer.parseInt(words[1]);
			sampleSize = sampleSize * pct;
		}

		return "set hive.abm.sample.size = " + sampleSize + "";
	}

	protected ResultSet execABM(List<String> sqls) throws SQLException {
		ResultSet rs = null;
		execTime = 0;

		for(String sql:sqls) {
			if(sql.contains("hive.abm.sampled.table")) {
				client.executeSQL(setTotalTupleNumber(sql));
			}

			if(sql.startsWith("--")||sql.startsWith("set")) {
				client.executeSQL(sql.replace("--", "").trim());
			} else if (!sql.toLowerCase().contains("drop")){
				TimeUtil.start();
				rs = client.executeSQL(sql);
				execTime += TimeUtil.getPassedSeconds();
			} else {
				client.executeSQL(sql);
			}
		}

		Log.log("ABM Execution time: " + execTime);
		return rs;
	}

	protected ResultSet execBootstrap(List<String> sqls) throws SQLException {
		ResultSet rs = null;
		execTime = 0;

		client.executeSQL("set hive.abm = false");
		client.executeSQL("set mapred.reduce.tasks= 112");

		for(String sql:sqls) {
			if(sql.contains("--") || sql.startsWith("set"))
				continue;
			else if(!sql.toLowerCase().contains("drop")) {
				TimeUtil.start();
				rs = client.executeSQL(sql);
				execTime += TimeUtil.getPassedSeconds();
			}
			else {
				client.equals(sql);
			}
		}

		Log.log("Bootstrap Execution time: " + execTime);
		return rs;
	}

	@Override
	public Response serve(IHTTPSession session) {
		String uri = session.getUri();
		ParamUtil params = new ParamUtil(session.getParms());
		InputStream mbuffer = null;

		if(client == null) {
			client = new JdbcClient();
		}

		try {
			if (uri != null) {
				//record uri request
				Log.log(uri);

				Map<String,String> paras = session.getParms();
				for(Map.Entry<String, String> entry:paras.entrySet()) {
					System.out.println(entry.getKey() + "@@" + entry.getValue());
				}

				if (uri.contains("favicon") || uri.contains("http")) {
					return null;
				}

				if (uri.contains(".js")) {
					mbuffer = Asset.open(uri);
					return new Response(Status.OK, Type.MIME_JS, mbuffer);
				} else if (uri.contains(".css")) {
					mbuffer = Asset.open(uri);
					return new Response(Status.OK, Type.MIME_CSS, mbuffer);
				} else if (uri.contains(".png")) {
					mbuffer = Asset.open(uri);
					return new Response(Status.OK, Type.MIME_PNG, mbuffer); 
				} else if (uri.contains(".gif")) {
					mbuffer = Asset.open(uri);
					return new Response(Status.OK, Type.MIME_PNG, mbuffer); 
				} else if (uri.contains(".htm") || uri.contains(".html")) {
					mbuffer = Asset.open(uri);
					return new Response(Status.OK, Type.MIME_HTML, mbuffer);
				} else if (uri.contains(".hive")) {
					mbuffer = Asset.open(uri);
					return new Response(Status.OK, Type.MIME_PLAINTEXT, mbuffer);
				} else if (uri.contains("stopInterval")) {
					if(t != null) {
						t.interrupt();
					}
					mbuffer = Asset.getNone();
					return new Response(Status.OK, Type.MIME_PLAINTEXT, mbuffer);
				} else if (uri.contains(".txt")) {
					mbuffer = Asset.open(uri);
					return new Response(Status.OK, Type.MIME_PLAINTEXT, mbuffer);
				} else {
					Log.log("Can not find MIME type for " + uri + ", open default page.");
					mbuffer = Asset.openDefault();
					return new Response(Status.OK, Type.MIME_HTML, mbuffer);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mbuffer = Asset.getException(e);
			return new Response(Status.OK, Type.MIME_HTML, mbuffer);
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
