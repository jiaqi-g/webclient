package edu.ucla.boost.web;

import java.io.PrintWriter;
import edu.ucla.boost.common.TimeUtil;
import edu.ucla.boost.jdbc.JdbcClient;

public class VanillaBootstrapRunner implements Runnable {

	int limit;
	int current;
	double abmTime;
	String sql;
	String path;
	JdbcClient client;
	
	public VanillaBootstrapRunner(int limit, String sql, String path, double abmTime) {
		this.limit = limit;
		this.current = 0;
		this.sql = sql;
		this.path = path;
		this.client = new JdbcClient();
		this.abmTime = abmTime;
	}
	
	@Override
  public void run() {
		
		double vanillaTime = 0;
		try {
	   
			while(current < limit) {
				current ++;
				
		    client.closeABM();
		    TimeUtil.start();
		    client.executeSQL(sql);
				vanillaTime += TimeUtil.getPassedSeconds();
				
				if(current%5 == 0) {
					// write to disk
					PrintWriter writer = new PrintWriter(path + "tmp.txt");
					// System.out.println(current + "," + vanillaTime);
					writer.write(current + "," + vanillaTime + "," + abmTime);
					writer.close();
				}
			}
			client.close();
			
			
		}
		catch (Exception e) {
      e.printStackTrace();
    }
	  
  }

	
	
	
}
