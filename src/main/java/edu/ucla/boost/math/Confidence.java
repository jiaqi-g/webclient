package edu.ucla.boost.math;

import edu.ucla.boost.common.Log;

public class Confidence {
	//5 means 5%
	private int confidenceFrom;
	private int confidenceTo;
	
	public Confidence(String inputFrom, String inputTo) {
		try {
			String fromS = inputFrom.trim().replace("%", "");
			String toS = inputTo.trim().replace("%", "");
			
			confidenceFrom = Integer.parseInt(fromS);
			confidenceTo = Integer.parseInt(toS);
			
			if (confidenceFrom > confidenceTo) {
				confidenceTo = confidenceFrom + 1;
			}
			
			if (confidenceFrom < 0 || confidenceFrom > 100) {
				throw new NumberFormatException();
			}
			if (confidenceTo < 0 || confidenceTo > 100) {
				throw new NumberFormatException();
			}
		}
		catch (NumberFormatException e) {
			confidenceFrom = 5;
			confidenceTo = 95;
			Log.log("Confidence format wrong: " + inputFrom + " " + inputTo + " , using (5, 95) instead");
		}
	}
	
	public int getConfidenceFrom() {
		return confidenceFrom;
	}
	
	public int getConfidenceTo() {
		return confidenceTo;
	}
}