/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.crypto;

import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * 
 *
 * @author wstevens
 */
final class AesCbcNonceEngine extends SymmetricEngine
{
	static final String TRANSFORMATION = "AES/CBC/PKCS7Padding";
	
	private final byte[] msgNumber = new byte[] {0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0};
		
	/**
	 * @param transformation
	 * @param provider
	 */
	AesCbcNonceEngine(SecretKey key, SecureRandom secureRandom, Provider provider)
	{
		super(key, TRANSFORMATION, secureRandom, provider);
	}
	
	private void initParams()
	{
		// establish and set random iv for nonce
		IvParameterSpec zeroIv = new IvParameterSpec(new byte[16]);
		setParams(zeroIv);
		encryptInit();		
		byte[] input = updateAndDoFinal(msgNumber);
		IvParameterSpec iv = new IvParameterSpec(input, 0, 16);		
		setParams(iv);
	}

	/* 
	 * @see com.valloc.security.crypto.SymmetricEngine#encrypt(byte[])
	 */
	@Override
	public byte[] encrypt(byte[] plaintext)
	{
		initParams();
		return super.encrypt(plaintext);
	}

	/* 
	 * @see com.valloc.security.crypto.SymmetricEngine#decrypt(byte[])
	 */
	@Override
	public byte[] decrypt(byte[] ciphertext)
	{
		initParams();
		return super.decrypt(ciphertext);
	}
}
