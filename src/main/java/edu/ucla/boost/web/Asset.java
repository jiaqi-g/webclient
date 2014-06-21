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
	
	public static InputStream getPlan(boolean isAbmEligible, boolean isCloseEligible, boolean isBootstrapEligible) throws IOException {
		String plan = "none";
		if (isAbmEligible) {
			//copy plan file from remote machine
			Scp.execute(Conf.remotePlanFile, Conf.planFile);
			plan = FileSystem.readFileAsString(Conf.planFile);
			Log.log("JsonPlan From Remote: " + plan);
		}
		
		String notice = "";
		if (isAbmEligible) {
			notice += "Safe for abm;\n";
		} else {
			notice += "Unsafe for abm;\n";
		}
		
		if (isCloseEligible) {
			notice += "Safe for closed form;\n";
		} else {
			notice += "Unsafe for closed form;\n";
		}
		
		if (isBootstrapEligible) {
			notice += "Safe for vanilla bootstrap;\n";
		} else {
			notice += "Unsafe for vanilla bootstrap;\n";
		}
		
		String rs = notice + plan;
		return new ByteArrayInputStream(rs.getBytes());
	}
}