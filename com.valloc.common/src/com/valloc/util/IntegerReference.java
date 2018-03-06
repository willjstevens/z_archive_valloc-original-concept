/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;


/**
 * A plain object for encapulating a single <code>int</code> primitive, such that the object reference may
 * be passed, set and maintained to provide access to the encapsulated subject value.
 *
 * @author wstevens
 */
//public final class IntegerReference implements FutureReferenceable<Integer>
public final class IntegerReference
{	
	/*
	 * The subject primitive. 
	 */
	private int value;
	
	public IntegerReference() {}
	
	public IntegerReference(final int value)
	{
		this.value = value;
	}
	
	public int asInt()
	{
		return value;
	}
	
	public void setValue(final int value)
	{
		this.value = value;
	}

	/**
	 * Incraments the value before returning the value.
	 * 
	 * @return int The value after being incremented by one.
	 */
	public int increment() 
	{
		return ++value;
	}

	/**
	 * Incraments the value before returning the value.
	 * 
	 * @return int The value after being incremented by one.
	 */
	public int decrement() 
	{
		return --value;
	}
	
	public int add(final int addend)
	{
		return value += addend;
	}
}