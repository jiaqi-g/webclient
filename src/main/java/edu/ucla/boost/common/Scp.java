package edu.ucla.boost.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import edu.ucla.boost.web.Asset;

public class Scp {
	
	public static String execute(String remotePath, String path) {
		try {
			//sudo -u victor ssh hadoop-user@131.179.64.66
			Process p = new ProcessBuilder("sudo", "-u", Conf.user, "scp", remotePath, path).start();
			p.waitFor();
//			p = new ProcessBuilder("cat", Conf.planFile).start();
//			p.waitFor();
						
			BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(Conf.planFile)));
			StringBuilder sb = new StringBuilder();
			
			while(read.ready()) {
				sb.append(read.readLine());
			}
			
			return sb.toString();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void main(String[] args) {
		execute(Conf.remotePlanFile, Conf.planFile);
	}
}