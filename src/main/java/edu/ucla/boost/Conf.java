package edu.ucla.boost;

public class Conf {
	static boolean debug = true;
	static boolean sharkMode = true;
	public static boolean record = true;
	public static String testFileName = "test.txt";
	
	public static final String autoTestResultFolder = "test/auto";
	public static final String simpleTestResultFolder = "test/simple";
	
	//static String serverName = "wise-u10.cs.ucla.edu";
	static String serverName = "localhost";
	
	static int sharkPort = 10000;
	static int hivePort = 10000;
	
	public static String getConnectionAddress() {
		if (sharkMode) {
			return "jdbc:hive://" + serverName + ":" + sharkPort + "/default";
		}
		else {
			return "jdbc:hive://" + serverName + ":" + hivePort + "/default";
		}
	}
}