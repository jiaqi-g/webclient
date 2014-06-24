package edu.ucla.boost.test;

import java.text.DecimalFormat;
import java.util.Formatter;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Formatter fmt = new Formatter();
	    // Format to 2 decimal places in a 16 character field.
	    fmt = new Formatter();
	    
	    fmt.format("%.6e", 123.1234567);
	    String s = fmt.toString();
	    fmt.close();
	    System.out.println(s);
	    
	    //fmt.format("%.6e", 7.428791220865309E9);
	    //System.out.println(fmt.toString());
	}

}
