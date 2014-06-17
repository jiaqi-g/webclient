package edu.ucla.boost.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.ucla.boost.common.FileSystem;

class Res {
	Double lower;
	Double upper;
	Double mu;
	Double va;
	
	public Res(Double lower, Double upper, Double mu, Double va) {
		this.lower = lower;
		this.upper = upper;
		this.mu = mu;
		this.va = va;
	}
	
	public List<Double> getResult() {
		return Arrays.asList(new Double[]{mu, va});
	}
	
	@Override
	public String toString() {
		return "[" + lower + "," + upper + "," + mu + "," + va + "]"; 
		//return  "[" + mu + "," + va + "]"; 
	}
}

/**
 * Warning: not incremental maintain
 *
 */
public class Srv {
	
	public static final int sampleSize = 100;
	
	List<Double> lst;
	Double valsum = 0.0;
	Double squaresum = 0.0;
	int cnt = 0;
	
	@Deprecated
	public Srv(List<Double> lst) {
		this.lst = lst;
		for (Double d: lst) {
			valsum += d;
			squaresum += d * d;
		}
		this.cnt = lst.size();
	}
	
	public Res sum() {
		Double mu = valsum;
		Double va = squaresum - valsum * valsum / sampleSize;
		Double nva = Math.sqrt(va);
		
		Double lower = mu - 3 * nva;
		Double upper = mu + 3 * nva;
		
		return new Res(lower, upper, mu, va);
	}
	
	public Res count() {
		Double mu = cnt + 0.0;
		Double va = cnt - cnt * cnt * 1.0 / sampleSize;
		Double nva = Math.sqrt(va);
		
		Double lower = mu - 3 * nva;
		Double upper = mu + 3 * nva;
		
		return new Res(lower, upper, mu, va);
	}

	public Res avg() {
		Double mu = valsum / cnt;
		Double va = (squaresum /cnt - mu * mu)/cnt;
		Double nva = Math.sqrt(va);

		Double lower = mu - 3 * nva;
		Double upper = mu + 3 * nva;
		
		return new Res(lower, upper, mu, va);
	}
	
	private static List<Double> toDoubleList(String[] in) {
		List<Double> res = new ArrayList<Double>();
		for (String s: in) {
			res.add(Double.parseDouble(s.trim()));
		}
		return res;
	}
	
	
	public static List<Double> computeSum(List<String> groups) {
		Iterator<String> iter= groups.iterator();
		String baseLine = iter.next();
		List<Double> baseLst = toDoubleList(baseLine.split(","));
		if (baseLst.size() == 1 && (baseLst.get(0) - 0) < 0.001) {
			baseLst.clear();
		}
		
		Double lowest = Double.MAX_VALUE;
		Double highest = Double.MIN_VALUE;
		
		List<Double> res = new ArrayList<Double>();
		while (iter.hasNext()) {
			List<Double> lst = toDoubleList(iter.next().split(","));
			lst.addAll(baseLst);
			
			Srv srv = new Srv(lst);
			Res rs = srv.sum();
			res.addAll(rs.getResult());
			
			if (lowest > rs.lower) {
				lowest = rs.lower;
			}
			if (highest < rs.upper) {
				highest = rs.upper;
			}
		}
		
		Srv srv = new Srv(baseLst);
		Res rs = srv.sum();
		res.addAll(rs.getResult());
		if (lowest > rs.lower) {
			lowest = rs.lower;
		}
		if (highest < rs.upper) {
			highest = rs.upper;
		}
		
		res.add(0, highest);
		res.add(0, lowest);
		return res;
	}
	
	public static List<Double> computeCount(List<String> groups) {
		Iterator<String> iter= groups.iterator();
		String baseLine = iter.next();
		List<Double> baseLst = toDoubleList(baseLine.split(","));
		if (baseLst.size() == 1 && (baseLst.get(0) - 0) < 0.001) {
			baseLst.clear();
		}
		
		Double lowest = Double.MAX_VALUE;
		Double highest = Double.MIN_VALUE;
		
		List<Double> res = new ArrayList<Double>();
		while (iter.hasNext()) {
			List<Double> lst = toDoubleList(iter.next().split(","));
			lst.addAll(baseLst);
			
			Srv srv = new Srv(lst);
			Res rs = srv.count();
			res.addAll(rs.getResult());
			
			if (lowest > rs.lower) {
				lowest = rs.lower;
			}
			if (highest < rs.upper) {
				highest = rs.upper;
			}
		}
		
		Srv srv = new Srv(baseLst);
		Res rs = srv.count();
		res.addAll(rs.getResult());
		if (lowest > rs.lower) {
			lowest = rs.lower;
		}
		if (highest < rs.upper) {
			highest = rs.upper;
		}
		
		res.add(0, highest);
		res.add(0, lowest);
		return res;
	}

	public static List<Double> computeAvg(List<String> groups) {
		Iterator<String> iter= groups.iterator();
		String baseLine = iter.next();
		List<Double> baseLst = toDoubleList(baseLine.split(","));
		if (baseLst.size() == 1 && (baseLst.get(0) - 0) < 0.001) {
			baseLst.clear();
		}
		
		Double lowest = Double.MAX_VALUE;
		Double highest = Double.MIN_VALUE;
		
		List<Double> res = new ArrayList<Double>();
		while (iter.hasNext()) {
			List<Double> lst = toDoubleList(iter.next().split(","));
			lst.addAll(baseLst);
			
			Srv srv = new Srv(lst);
			Res rs = srv.avg();
			res.addAll(rs.getResult());
			
			if (lowest > rs.lower) {
				lowest = rs.lower;
			}
			if (highest < rs.upper) {
				highest = rs.upper;
			}
		}
		
		Srv srv = new Srv(baseLst);
		Res rs = srv.avg();
		res.addAll(rs.getResult());
		if (lowest > rs.lower) {
			lowest = rs.lower;
		}
		if (highest < rs.upper) {
			highest = rs.upper;
		}
		
		res.add(0, highest);
		res.add(0, lowest);
		return res;
	}
	
	public static void main(String[] args) throws Exception {
		
		//Base Group should be put in the first line
		
		//Double[] l = new Double[]{1.0, 3.0, 8.0};
		//l = new Double[]{5.0, 8.0, 15.0, 20.0, 8.0};
		
		//Srv srv = new Srv(Arrays.asList(l));
		//System.out.println(srv.sum());
		//System.out.println(srv.count());
		//System.out.println(srv.avg());
	}
}