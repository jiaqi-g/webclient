package edu.ucla.wis.config;

import edu.ucla.wis.web.Type;

public class TypeAction {
	Type type;
	ResponseAction action;
	
	public TypeAction(Type type, ResponseAction action) {
		this.type = type;
		this.action = action;
	}
	
	public TypeAction(Type type) {
		this(type, null);
	}
	
	public TypeAction(ResponseAction action) {
		this(null, action);
	}
	
	public Type getType() {
		return type;
	}
	
	public ResponseAction getAction() {
		return action;
	}
	
	public void setResponseAction(ResponseAction action) {
		this.action = action;
	}
}
