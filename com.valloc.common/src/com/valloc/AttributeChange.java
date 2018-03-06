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
public class AttributeChange
{
	private final ChangeType changeType;
	private final String key;
	private final Object value;
	
	public AttributeChange(final ChangeType changeType, final String key, final Object value) {
		this.changeType = changeType;
		this.key = key;
		this.value = value;
	}
	
	public ChangeType getChangeType() {
		return changeType;
	}
	
	public String getKey() {
		return key;
	}
	
	public Object getValue() {
		return value;
	}
}
