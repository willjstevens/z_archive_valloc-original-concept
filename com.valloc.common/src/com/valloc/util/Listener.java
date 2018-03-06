/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import java.util.EventListener;

/**
 * A listener definition listening for <code>Event</code>s.
 *
 * @author wstevens
 */
public interface Listener<T> extends EventListener
{
	/**
	 * Accepts the <code>Event</code> object once an event has been fired.
	 */
	public void onEvent(Event<T> event);
}