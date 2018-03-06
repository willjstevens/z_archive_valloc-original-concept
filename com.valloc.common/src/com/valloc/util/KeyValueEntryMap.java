/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import static com.valloc.Constants.*;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author wstevens
 */
public final class KeyValueEntryMap
{
	private final Map<Integer, KeyValuePair> params = new HashMap<Integer, KeyValuePair>();
	private int orderCount;
	
	public void add(final String key, final Object value)
	{
		final KeyValuePair keyValuePair = new KeyValuePair(key, value);
		params.put(orderCount++, keyValuePair);
	}
	
	private static final class KeyValuePair 
	{
		private final String key;
		private final Object value;
		private KeyValuePair(final String key, final Object value) {
			this.key = key;
			this.value = value;
		}
	}
	
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		
		builder.append('{');		
		for (final Integer orderKey : params.keySet()) {
			final KeyValuePair pair = params.get(orderKey);
			final String elementKey = pair.key;
			final Object elementValue = pair.value;
			final String valueString = elementValue != null ? elementValue.toString() : null;
			builder.append(elementKey).append(EQUALS);
			builder.append(QUOTE).append(valueString).append(QUOTE).append(COMMA); 
		}
		builder.delete(builder.length()-1, builder.length()); // chomp trailing comma
		builder.append('}');
		
		return builder.toString();
	}
}
