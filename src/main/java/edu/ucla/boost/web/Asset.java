package edu.ucla.boost.web;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import edu.ucla.boost.common.Conf;
import edu.ucla.boost.common.FileSystem;
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
	
	public static InputStream getException(Exception e) {
		String[] arr = e.getMessage().split(":");
		String exceptionInfo = "Exception: " + arr[arr.length-1];
		
		return new ByteArrayInputStream(exceptionInfo.getBytes());
	}
	
	public static InputStream getPlan(boolean isAbmEligible, String exceptionInfo) throws IOException {
		boolean isCloseEligible = true;
		boolean isBootstrapEligible = true;
		
		String plan = "none";
		if (isAbmEligible) {
			//copy plan file from remote machine
			Scp.execute(Conf.remotePlanFile, Conf.planFile);
			String raw = FileSystem.readFileAsString(Conf.planFile);
			Log.log(raw);
			
			String[] arrs = raw.split("\\|");
			plan = arrs[0];
			isCloseEligible = Boolean.parseBoolean(arrs[1]);
			//plan = FileSystem.readFileAsString(Conf.planFile);
			Log.log("JsonPlan From Remote: " + plan);
			Log.log("isCloseEligible: " + isCloseEligible);
		}
		else {
			isCloseEligible = false;
		}
		
		
		String notice = "";
		if (isAbmEligible) {
			notice += "1|\n";
		} else {
			notice += "0|\n";
		}
		
		if (isCloseEligible) {
			notice += "1|\n";
		} else {
			notice += "0|\n";
		}
		
		if (isBootstrapEligible) {
			notice += "1|\n";
		} else {
			notice += "0|\n";
		}
		
		//exceptionInfo for abm
		notice += exceptionInfo + "|\n";
		
		String rs = notice + plan;
		return new ByteArrayInputStream(rs.getBytes());
	}
}