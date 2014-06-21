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
import edu.ucla.boost.common.Time;
import edu.ucla.boost.common.TimeUtil;
import edu.ucla.boost.http.ParamUtil;
import edu.ucla.boost.jdbc.JdbcClient;
import edu.ucla.boost.web.Asset;
import edu.ucla.boost.web.Type;
import edu.ucla.boost.web.VanillaBootstrapRunner;

public class Server extends NanoHTTPD {

	protected Thread t = null;
	protected JdbcClient client = new JdbcClient();
	protected double execTime = 0;

	public Server() {
		super(Conf.port);
	}
	
	protected ResultSet execABM(List<String> sqls) {
		ResultSet rs = null;
		execTime = 0;
		
		try {
			
			// TODO
			// set select, set N
			
			for(String sql:sqls) {
				if(sql.startsWith("--")) {
		        client.executeSQL(sql.replace("--", "").trim());
				} else {
					TimeUtil.start();
					rs = client.executeSQL(sql);
					execTime += TimeUtil.getPassedSeconds();
				}
			} 
		 } catch (SQLException e) {
       e.printStackTrace();
     }
		
		return rs;
	}

	@Override
	public Response serve(IHTTPSession session) {
		String uri = session.getUri();
		ParamUtil params = new ParamUtil(session.getParms());
		InputStream mbuffer = null;
		try {
			if (uri != null) {

				System.out.println(uri);
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
				} else if (uri.contains(".htm") || uri.contains(".html")) {
					mbuffer = Asset.open(uri);
					return new Response(Status.OK, Type.MIME_HTML, mbuffer);
				} else if (uri.equals("/search")) {
					List<String> sqls = params.getQueryList();
					ResultSet rs = execABM(sqls);
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
					//only execute "select" here, potential bugs for not executing "set"
					boolean isAbmEligible = true;
					boolean isCloseEligible = true;
					boolean isBootstrapEligible = true;
					
					//String exception = "";
					for (String sql: sqls) {
						if (sql.toLowerCase().startsWith("select")) {
							if (sql.toLowerCase().contains("min") || sql.toLowerCase().contains("max")) {
								isBootstrapEligible = false;
								isAbmEligible = false;
								isCloseEligible = false;
							} else {
								try {
									client.executeSQL("explain " + sql);
								}
								catch (SQLException e) {
									isAbmEligible = false;
									isCloseEligible = false;
									//String[] arrs = e.getMessage().split(":");
									//exception = arrs[arrs.length - 1].trim();
									e.printStackTrace();
								}
							}
						}
					}
					
					mbuffer = Asset.getPlan(isAbmEligible, isCloseEligible, isBootstrapEligible);
					return new Response(Status.OK, Type.MIME_PLAINTEXT, mbuffer);
				} else if (uri.contains(".hive")) {
					mbuffer = Asset.open(uri);
					return new Response(Status.OK, Type.MIME_PLAINTEXT, mbuffer);
				} else if (uri.contains("vanilla")) {
					//support batch execution
					List<String> sqls = params.getQueryList();
					JdbcClient client = new JdbcClient();
					
					String selectSQL = null;
					for (String sql: sqls) {
						if (sql.startsWith("select")) {
							selectSQL = sql;
							break;
						}
					}
					
					client.openABM();
					TimeUtil.start();
					client.executeSQL(selectSQL);
					double abmTime = TimeUtil.getPassedSeconds();

					if (selectSQL != null) {
						VanillaBootstrapRunner runner = new VanillaBootstrapRunner(100, selectSQL, Conf.websitePath + "/", abmTime);
						t = new Thread(runner);
						t.start();
						mbuffer = Asset.open(uri);
						return new Response(Status.OK, Type.MIME_HTML, mbuffer);
					}
					else {
						return null;
					}
				} else if (uri.contains("stopInterval")) {
					if(t != null) {
						t.interrupt();
					}
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
