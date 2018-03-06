/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author wstevens
 */
public class Attributes
{
	/**
	 * The structure encapsulating all subject keys and values.
	 */
	private final Map<String, Object> attributes = new HashMap<String, Object>();

	public Set<String> getAllKeys() {
		return attributes.keySet();
	}
	
	public void add(final String key, final Object value) {
		attributes.put(key, value);
	}

	public Object get(final String key) {
		return attributes.get(key);
	}

	public String getString(final String key) {
		return (String) get(key);
	}

	public void addString(final String key, final String value) {
		add(key, value);
	}

	public int getInt(final String key) {
		return (Integer) get(key);
	}

	public void addInt(final String key, final int value) {
		add(key, value);
	}

	public boolean getBoolean(final String key) {
		return (Boolean) get(key);
	}

	public void addBoolean(final String key, final boolean value) {
		add(key, value);
	}

	public Map<String, Object> getAttributes() {
		return Collections.unmodifiableMap(attributes);
	}

	public void addAttributes(final Map<String, Object> attributes) {
		attributes.putAll(attributes);
	}

	public void clear() {
		attributes.clear();
	}

	public void remove(final String key) {
		attributes.remove(key);
	}

	public boolean hasAttributes() {
		return !attributes.isEmpty();
	}
}