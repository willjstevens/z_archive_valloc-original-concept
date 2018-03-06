/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.crypto;

import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.SecretKey;

/**
 * 
 *
 * @author wstevens
 */
class SymmetricEngine extends AbstractCipherEngine
{
	private SecretKey key;

	/**
	 * 
	 */
	SymmetricEngine(String transformation, Provider provider)
	{
		this(null, transformation, null, provider);
	}
	
	/**
	 * 
	 */
	SymmetricEngine(SecretKey key, String transformation, Provider provider)
	{
		this(key, transformation, null, provider);
	}
	
	/**
	 * 
	 */
	SymmetricEngine(SecretKey key, String transformation, SecureRandom secureRandom, Provider provider)
	{
		super(transformation, secureRandom, provider);
		this.key = key; // could be null
	}
		
	void encryptInit()
	{
		encryptInit(key);
	}
		
	/* 
	 * @see com.valloc.security.crypto.AbstractCipherEngine#encrypt(byte[])
	 */
	@Override
	public byte[] encrypt(byte[] plaintext)
	{
		encryptInit(key);
		return super.encrypt(plaintext);
	}

	/* 
	 * @see com.valloc.security.crypto.AbstractCipherEngine#decrypt(byte[])
	 */
	@Override
	public byte[] decrypt(byte[] ciphertext)
	{
		decryptInit(key);		
		return super.decrypt(ciphertext);
	}

	/**
	 * @param key Sets the key.
	 */
	void setKey(SecretKey key)
	{
		this.key = key;
	}
}