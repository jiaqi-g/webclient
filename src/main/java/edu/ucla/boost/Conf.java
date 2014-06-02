package edu.ucla.boost;

public class Conf {
	static boolean debug = true;
	static boolean sharkMode = true;
	
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