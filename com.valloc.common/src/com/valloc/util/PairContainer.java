/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author wstevens
 */
public class PairContainer<K, V>
{
	private final List<K> keyList = new ArrayList<K>();
	private final List<V> valueList = new ArrayList<V>();
	
	public void addPair(final K key, final V value)
	{
		keyList.add(key);
		valueList.add(value);
	}
	
	public boolean removePair(final K key)
	{
		boolean wasRemoved = false;
		
		final int idx = keyList.indexOf(key);
		if (idx >= 0) {
			keyList.remove(idx);
			valueList.remove(idx);
			wasRemoved = true;
		}
		
		return wasRemoved;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		
		builder.append('{');
		for (int i = 0, size = keyList.size(); i < size; i++) {
			final K key = keyList.get(i);
			final V val = valueList.get(i);
			Util.addKeyValPair(builder, key, val);
		}
		Util.chompLastChar(builder);
		builder.append('}');
		
		return builder.toString();
	}
}
