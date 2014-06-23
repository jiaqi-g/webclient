package edu.ucla.boost.math;

import java.text.DecimalFormat;

import org.apache.commons.math3.distribution.NormalDistribution;

public class NormalDist {
	double mean;
	double variance;
	double[] x = new double[21];
	double[] y = new double[21];
	static final DecimalFormat df = new DecimalFormat("#.###");
	
	public NormalDist(double mean, double variance) {
		this.mean = mean;
		this.variance = variance;
		
		double std = Math.sqrt(variance);
		if(std > 0) {
			NormalDistribution dis = new NormalDistribution(mean, std);
			int idx = 0;
	
			for(int i = -10; i <= 10; i ++) {
				 x[idx] = mean +  0.3 * i * std;
			 	 y[idx] = dis.density(x[idx]);
			 	 idx ++;
			}
		} else {
			for(int i = 0; i < x.length; i ++) {
				x[i] = 0;
				y[i] = 0;
			}
		}
	}
	
	@Override
	public String toString() {
		String s = "";
		for (int i=0; i< x.length; i++) {
			s += df.format(x[i]) + "," + df.format(y[i]) + ",";
		}
		s = s.substring(0, s.length()-1);
		return s;
	}
	
	public static void main(String[] args) {
		System.out.println(new NormalDist(0,1));
	}
}
