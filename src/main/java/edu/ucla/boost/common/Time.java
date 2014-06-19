package edu.ucla.boost.common;

public class Time {
	public Long abmTime;
	public Long closeFormTime;
	public Long vanillaTime;
	
	public Time(Long abmTime, Long closeFormTime, Long vanillaTime) {
		super();
		this.abmTime = abmTime;
		this.closeFormTime = closeFormTime;
		this.vanillaTime = vanillaTime;
	}
}