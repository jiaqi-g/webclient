package boost.jdbc;

public class Conf {
	static boolean debug = true;
	static boolean sharkMode = false;
	
	static int sharkPort = 4588;
	static int hivePort = 10000;
	
	public static int getPort() {
		if (sharkMode) {
			return sharkPort;
		}
		else {
			return hivePort;
		}
	}
}