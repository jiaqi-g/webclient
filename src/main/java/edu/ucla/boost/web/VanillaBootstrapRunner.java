package edu.ucla.boost.web;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.ucla.boost.common.TimeUtil;
import edu.ucla.boost.jdbc.JdbcClient;

public class VanillaBootstrapRunner implements Runnable {

	int limit;
	int current;
	double abmTime;
	double vanillaTime;
	List<String> sqls;
	String path;
	JdbcClient client;

	public VanillaBootstrapRunner(int limit, List<String>  sqls, String path, double abmTime) {
		this.limit = limit;
		this.current = 0;
		this.sqls = sqls;
		this.path = path;
		this.client = new JdbcClient();
		this.abmTime = abmTime;
	}
	
	protected ResultSet execBootstrap() {
		ResultSet rs = null;
		
		try {
			client.executeSQL("set hive.abm = false");
			client.executeSQL("set mapred.reduce.tasks= 112");
			
			for(String sql:sqls) {
				if(sql.contains("--") || sql.startsWith("set"))
					continue;
				else {
					TimeUtil.start();
					rs = client.executeSQL(sql);
					vanillaTime += TimeUtil.getPassedSeconds();
				}
			}
		} catch (SQLException e) {
      e.printStackTrace();
    }
		
		return rs;
	}

	@Override
	public void run() {
		
		try {
			while(current <= limit && !Thread.interrupted()) {
				if(current % 2 == 0) {
					PrintWriter writer = new PrintWriter(path + "tmp.txt");
					writer.write(current + "," + vanillaTime + "," + abmTime);
					writer.close();
				}
				current++;
				execBootstrap();
			}
			client.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
}
