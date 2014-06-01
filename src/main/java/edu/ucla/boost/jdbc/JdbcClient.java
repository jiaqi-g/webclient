package edu.ucla.boost.jdbc;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;

public class JdbcClient {

	String s = "load data local inpath '/home/victor/emp_data.txt' into table test";
	private static String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
	private Connection con = null;
	private Statement stmt = null;

	static {
		try {
			Class.forName(driverName);
			DriverManager.getConnection("jdbc:hive://localhost:" + Conf.getPort() + "/default", "" , "");
			//only support db called "default"
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void load() {
	}

	public JdbcClient() {
		try {
			con = DriverManager.getConnection("jdbc:hive://localhost:" + Conf.getPort() + "/default", "" , "");
			stmt = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public ResultSet executeSQL(String sql) throws SQLException {
		sql = sql.toLowerCase().trim();
		//if (sql.startsWith("select")) {
		return stmt.executeQuery(prepareSQL(sql));
		//}
		//return null;
	}

	private String getPrettyResult(ResultSet rs) throws SQLException {
		StringBuilder res = new StringBuilder();
		int columnCount = rs.getMetaData().getColumnCount();
		Log.log("column count: " + columnCount);
		res.append("**Result:**\n");

		int index = 0;
		while (rs.next()) {
			//column count starts from 1
			for (int i=1; i<=columnCount; i++) {
				Object obj = rs.getObject(i);
				if (obj != null) {
					res.append(obj.toString().trim());
				}
				else {
					res.append("null");
				}
				res.append(", ");
			}
			res.append("\n");
			index += 1;
		}
		Log.log("result length: " + index);
		return res.toString();
	}

	private static String prepareSQL(String sql) {
		Log.log(sql);
		return sql;
	}

	public static void main(String[] args) throws SQLException {
		JdbcClient.load();

		JdbcClient client = new JdbcClient();
		
		String tableName = "test";
		String sql = null;
		ResultSet res = null;
		
		sql = "drop table " + tableName;
		client.executeSQL(sql);

		sql = "create table " + tableName+ " (empid int, name string) ROW FORMAT DELIMITED FIELDS TERMINATED BY " +   "\",\"";
		client.executeSQL(sql);

		sql = "show tables '" + tableName + "'";
		res = client.executeSQL(sql);
		//System.out.println(getPrettyResult(res));
		
		// describe table   
		sql = "describe " + tableName;
		res = client.executeSQL(sql);
		//System.out.println(getPrettyResult(res));

		// load data into table  
		// NOTE: filepath has to be local to the hive server
		String filepath = "/home/victor/emp_data.txt";
		sql = "load data local inpath '" + filepath + "' into table " + tableName;
		res = client.executeSQL(sql);
		//System.out.println(getPrettyResult(res));

		// select * query
		sql = "select * from " + tableName;
		res = client.executeSQL(sql);
		//System.out.println(getPrettyResult(res));

		// regular hive query  
		sql = "select count(1) from " + tableName;
		res = client.executeSQL(sql);
		//System.out.println(getPrettyResult(res));

		//		String q1="CREATE TABLE one AS SELECT 1 AS one FROM " +  tableName  + " LIMIT 1";
		//		int rows=0;
		//		String c1="";
		//		String c2="";
		//		//insert into table emp_tab1 SELECT stack(3 , 1 , "row1" , 2 , "row2", 3 , "row3") AS (empid, name)FROM one;
		//		System.out.println("Inserting records..... " );
		//		String q2 = "insert into table "  +tableName +  " SELECT stack(3 , 1 ,\"row1\", 2 , \"row2\",3 , \"row3\") AS (empid, name) FROM one";
		//		res = stmt.executeQuery(q2);
		//		System.out.println("Successfully inserted.......... " );
	}
}
