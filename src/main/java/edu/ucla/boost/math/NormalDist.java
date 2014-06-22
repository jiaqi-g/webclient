package edu.ucla.boost.math;

import org.apache.commons.math3.distribution.NormalDistribution;

public class NormalDist {
	double mean;
	double variance;
	double[] x = new double[7];
	double[] y = new double[7];
	
	public NormalDist(double mean, double variance) {
		this.mean = mean;
		this.variance = variance;
		
		double std = Math.sqrt(variance);
		if(std > 0) {
			NormalDistribution dis = new NormalDistribution(mean, variance);
			int idx = 0;
		
			for(int i = -3; i < 4; i ++) {
				 x[idx] = mean + i * std;
			 	 y[idx] = dis.density(x[idx]);
			 	 idx ++;
			}
		} else {
			for(int i = 0; i < 7; i ++) {
				x[i] = 0;
				y[i] = 0;
			}
		}
	}
	
	@Override
	public String toString() {
		String s = "";
		for (int i=0; i<7; i++) {
			s += x[i] + "," + y[i] + ",";
		}
		s = s.substring(0, s.length()-1);
		return s;
	}
	
	public static void main(String[] args) {
		System.out.println(new NormalDist(0,1));
	}
}
