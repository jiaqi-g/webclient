package edu.ucla.boost.math;

import edu.ucla.boost.common.Log;

public class Quantile {
	//5 means 5%
	private int quantile;
	
	public Quantile(String inputVal) {
		try {
			String s = inputVal.trim().replace("%", "");
			quantile = Integer.parseInt(s);
			
			if (quantile < 0 || quantile > 100) {
				throw new NumberFormatException();
			}
		}
		catch (NumberFormatException e) {
			quantile = 5;
			Log.log("Quantile format wrong: " + inputVal + " , using 5% quantile instead");
		}
	}
	
	public int getQuantile() {
		return quantile;
	}
	
}