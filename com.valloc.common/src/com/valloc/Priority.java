/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc;

/**
 * 
 *
 * @author wstevens
 */
public enum Priority
{
	ADMIN_HIGH 			(10),
	ADMIN_STANDARD		(8),
	SYSTEM_HIGH			(6),
	SYSTEM_STANDARD		(5),
	USER_HIGH			(3),
	USER_STANDARD		(2),
	UNASSIGNED			(1);
	
	private final int level;
	private Priority(final int level) {
		this.level = level;
	}
	
	public boolean equals(final Priority priority) {
		return this.level == priority.level;
	}
	
	public boolean isHigher(final Priority priority) {
		return this.level > priority.level;
	}

	public boolean isLower(final Priority priority) {
		return this.level < priority.level;
	}
	
	public int level() {
		return level;
	}
	
	public static Priority toPriority(final int level)
	{
		Priority retval = UNASSIGNED;
		
		switch(level) {
		case 10:	retval = ADMIN_HIGH;		break;
		case 8:		retval = ADMIN_STANDARD;	break;
		case 6:		retval = SYSTEM_HIGH;		break;
		case 5:		retval = SYSTEM_STANDARD;	break;
		case 3:		retval = USER_HIGH; 		break;
		case 2:		retval = USER_STANDARD;		break;
		}
		
		return retval;
	}
}