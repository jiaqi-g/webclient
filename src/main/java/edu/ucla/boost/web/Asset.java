package edu.ucla.boost.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import edu.ucla.boost.common.Conf;
import edu.ucla.boost.common.Log;
import edu.ucla.boost.common.Scp;

public class Asset {
	
	public static final String defaultPage = "/index.html";
	
	public static InputStream open(String uri) {
		InputStream is = null;
		try {
			
			if(uri.contains(".txt")) {
				// replace the file name to upper case
				String[] tokens = uri.split("/");
				String upperURI = "";
				for(String token:tokens) {
					upperURI += "/" + token;
				}
				uri = upperURI;
			}

			is = new FileInputStream(Conf.websitePath + uri);
			//is.close(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return is;
	}
	
	public static InputStream openDefault() {
		return open(defaultPage);
	}
	
	public static InputStream getPlan() {
		//copy plan file from remote machine
		String s = Scp.execute(Conf.remotePlanFile, Conf.planFile);
		Log.log(s);
		
		InputStream is = null;
		try {
			is = new FileInputStream(Conf.planFile);
			//is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return is;
	}
}