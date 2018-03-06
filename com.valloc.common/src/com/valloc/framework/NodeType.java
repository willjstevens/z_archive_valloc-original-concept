/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.Constants;

/**
 * 
 *
 * @author wstevens
 */
public enum NodeType
{
	LOCAL,
	DESKTOP,
	SERVER,
	AGENT,
	VALLOC_COM;

	public String asKeyName() {
		String keyName = name();
		keyName = keyName.toLowerCase();
		keyName = keyName.replace(Constants.UNDERSCORE, Constants.DASH);
		return keyName;
	}
	
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
