/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

/**
 * An immutable event object which contains the subject (of type <code>T</code>) object. 
 *
 * @author wstevens
 */
public class Event<T>
{
	/*
	 * The encapsulated <code>T</code> subject object.
	 */
	private T source;

	/**
	 * Default constructor accepting an <code>T</code>.
	 * 
	 * @param T The relevant <code>T</code> object for the which the event was fired.
	 */
	public Event(T source)
	{
		this.source = source;
	}

	/**
	 * Retrieves the subject <code>T</code> object.
	 * 
	 * @return T Returns the <code>T</code> type subject.
	 */
	public T getSource()
	{
		return source;
	}
}