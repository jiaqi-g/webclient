package edu.ucla.boost.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import edu.ucla.boost.web.Asset;

public class Scp {
	
	public static String execute(String remotePath, String path) {
		try {
			Process p = new ProcessBuilder("scp", remotePath, path).start();
			p.waitFor();
//			p = new ProcessBuilder("cat", Asset.planFile).start();
//			p.waitFor();
						
			BufferedReader read = new BufferedReader(new InputStreamReader(p.getInputStream()));
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