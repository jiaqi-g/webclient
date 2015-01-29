package edu.ucla.wis.web;

public enum Type {
	/**
	 * Common mime types for dynamic content
	 */
	MIME_PLAINTEXT ("text/plain"),
	MIME_HTML ("text/html"),
	MIME_JS ("application/javascript"),
	MIME_CSS ("text/css"),
	MIME_PNG ("image/png"),
	MIME_GIF ("image/gif"),
	MIME_DEFAULT_BINARY ("application/octet-stream"),
	MIME_XML ("text/xml");
	
	private final String name;       

    private Type(String s) {
        name = s;
    }

    public boolean equalsName(String otherName){
        return (otherName == null)? false:name.equals(otherName);
    }

    public String toString(){
       return name;
    }
}