package edu.ucla.boost.web;

import java.sql.SQLException;
import java.util.List;

import edu.ucla.boost.common.TimeUtil;
import edu.ucla.boost.jdbc.JdbcClient;

public class VanillaBootstrapRunner implements Runnable {

	int limit;
	int current;
	String sql;
	JdbcClient client;
	
	public VanillaBootstrapRunner(int limit, String sql) {
		this.limit = limit;
		this.current = 0;
		this.sql = sql;
		this.client = new JdbcClient();
	}
	
	@Override
  public void run() {
		
		double vanillaTime;
	  
		while(current < limit) {
			current ++;
			
			try {
	      client.closeABM();
	      TimeUtil.start();
				client.executeSQL(sql);
				
      } catch (SQLException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
      }
			vanillaTime = TimeUtil.getPassedSeconds();
			
			if(current%5 == 0) {
				// write to disk
				System.out.println(current + "," + limit);
			}
			
		}
	  
  }

	
	
	
}
