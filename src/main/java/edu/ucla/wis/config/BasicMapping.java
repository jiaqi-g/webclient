package edu.ucla.wis.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.ucla.wis.web.Type;

public abstract class BasicMapping {
	private Map<String, ResponseAction> suffixToAction = new HashMap<String, ResponseAction>();
	
	public BasicMapping() {
	}
	
	public ResponseAction getResponseAction(String uri) {
		String[] words = uri.split(".");
		return suffixToAction.get(words[words.length-1]);
	}
	
	public void bindAction(String suffix, ResponseAction action) {
		suffixToAction.put(suffix, action);
	}
	
	/*
	public void bindTypeAction(String suffix, Type type, ResponseAction action) {
		suffixToTypeAction.put(suffix, new TypeAction(type, action));
	}*/
}
