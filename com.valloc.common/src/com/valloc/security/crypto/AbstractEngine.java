/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.crypto;

import java.security.Provider;
import java.security.SecureRandom;

import com.valloc.util.Util;

/**
 * 
 *
 * @author wstevens
 */
abstract class AbstractEngine
{
	private final Provider provider;
	private SecureRandom secureRandom;
	
	/**
	 * 
	 */
	AbstractEngine(Provider provider)
	{
		this.provider = provider;
	}
	
	/**
	 * 
	 */
	AbstractEngine(SecureRandom secureRandom, Provider provider)
	{
		this(provider);
		
		this.secureRandom = secureRandom;
	}

	/**
	 * @return Provider Returns the provider.
	 */
	Provider getProvider()
	{
		return provider;
	}
		
	/**
	 * @return SecureRandom Returns the secureRandom.
	 */
	SecureRandom getSecureRandom()
	{
		return secureRandom;
	}

	byte[] stringToBytes(String string)
	{
		return Util.toByteArray(string);
	}
	
	String bytesToString(byte[] bytes)
	{
		return Util.toString(bytes);
	}	
}