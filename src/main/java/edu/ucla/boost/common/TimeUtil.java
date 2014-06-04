package edu.ucla.boost.common;

public class TimeUtil {
	static long recordTime;
	
	public static void reset() {
		recordTime = System.currentTimeMillis();
	}
	
	public static void start() {
		reset();
	}
	
	public static double getPassedSeconds() {
		return (System.currentTimeMillis() - recordTime) / 1000.0;
	}
	
	public static void printPassed() {
		System.out.println("Time: " + getPassedSeconds() + "s");
	}
	
	public static void main(String[] args) throws Exception {
		TimeUtil.start();
		Thread.sleep(1000);
		for (int i = 0; i < 100; i++) {
			System.out.println(123);
		}
		System.out.println(TimeUtil.getPassedSeconds());
	}
}
