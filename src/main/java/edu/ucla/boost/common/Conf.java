package edu.ucla.boost.common;

public class Conf {
	
	public static Boolean debug = false;
	
	public static String dropboxPath = "/home/victor/Dropbox";
	public static String websitePath = "/home/victor/boost_website";
	public static String sharkPath = "/home/victor/shark/shark";
	public static String serverName = "localhost"; //"wise-u10.cs.ucla.edu";
	
	
	public static String autoTestResultFolder = "test/auto";
	public static String simpleTestResultFolder = "test/simple";
	public static String tpchQueryFolder = dropboxPath + "/hive/TPC-H_on_Hive/tpch/eligible_queries/corrected";
	
	public static Integer port = 8080;
	public static Integer sharkPort = 10000;
	public static Integer hivePort = 10000;
	
	public static String getConnectionAddress() {
		boolean sharkMode = true;
		if (sharkMode) {
			return "jdbc:hive://" + serverName + ":" + sharkPort + "/default";
		}
		else {
			return "jdbc:hive://" + serverName + ":" + hivePort + "/default";
		}
	}
}