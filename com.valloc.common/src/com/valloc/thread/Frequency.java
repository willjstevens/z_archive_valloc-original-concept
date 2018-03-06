/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.thread;

/**
 *
 *
 * @author wstevens
 */
public enum Frequency
{
	SECONDS_03 	(03),
	SECONDS_05 	(05),
	SECONDS_10 	(10),
	SECONDS_15 	(15),
	SECONDS_30 	(30),
	MINUTE		(60),
	MINUTES_05	(60*5),
	MINUTES_10	(60*10),
	MINUTES_15	(60*15),
	MINUTES_30	(60*30),
	MINUTES_45	(60*45),
	HOUR		(60*60);
		
	private int frequencyInSeconds;
	
	private Frequency(final int seconds) {
		this.frequencyInSeconds = seconds;
	}
	
	public int frequencyInSeconds() {
		return frequencyInSeconds;
	}
}
