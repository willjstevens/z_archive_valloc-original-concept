/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.interrupt;

/**
 * 
 * 
 * @author wstevens
 */
public enum InterruptType
{
	TIMEOUT,
	USER_CANCELLED,
	NORMAL_COMPLETION,
	ROLLBACK,
	SYSTEM,
	SHUTDOWN,
	KILL;
}
