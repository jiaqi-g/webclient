package edu.ucla.boost.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Asset {
	
	/**
	 * notice: we should put absolute address of website in the root index.html file
	 */
	public static final String rootPath = "/home/victor/boost_website";
	public static final String defaultPage = "/index.html";
	
	//the place relative to server
	public static final String planFile = "/home/victor/shark/shark/json_plan.txt";
	
	public static InputStream open(String uri) {
		InputStream is = null;
		try {
			is = new FileInputStream(rootPath + uri);
			//is.close(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}
	
	public static InputStream openDefault() {
		return open(defaultPage);
	}
	
	public static InputStream getPlan() {
		InputStream is = null;
		try {
			is = new FileInputStream(planFile);
			//is.close(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}
}