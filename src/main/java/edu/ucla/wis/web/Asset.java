package edu.ucla.wis.web;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import edu.ucla.wis.common.Conf;
import edu.ucla.wis.common.FileSystem;
import edu.ucla.wis.common.Log;
import edu.ucla.wis.common.Scp;

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
	
	public static InputStream openDefaultPage() {
		return open(defaultPage);
	}
	
	public static InputStream getException(Exception e) {
		String[] arr = e.getMessage().split(":");
		String exceptionInfo = "Exception: " + arr[arr.length-1];
		
		return new ByteArrayInputStream(exceptionInfo.getBytes());
	}
	
	public static InputStream getNone() {
		return new ByteArrayInputStream("none".getBytes());
	}
}