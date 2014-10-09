package edu.ucla.boost.common;

public class Conf {
	public static Boolean debug = true;
	public static Boolean connectDB = true;
	public static String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
	
	/**
	 * Make sure user running boost_client can access hadoop-user@131.179.64.66 by ssh passwordless
	 */
	public static String user = "victor";
	public static String websitePath = "/home/victor/boost_website";
	public static String serverName = "131.179.64.66";
	public static String protocolName = "hive";
	
	//public static String serverName = "localhost";
	
	public static Integer port = 8080;
	public static Integer dbPort = 5000;
	
	public static String getConnectionAddress() {
		return "jdbc:" + protocolName + "://" + serverName + ":" + dbPort + "/default";
	}
}