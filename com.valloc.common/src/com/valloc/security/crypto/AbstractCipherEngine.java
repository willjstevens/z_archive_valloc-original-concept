/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.crypto;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;

import com.valloc.ApplicationException;
import com.valloc.CategoryType;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;

/**
 * 
 *
 * @author wstevens
 */
abstract class AbstractCipherEngine extends AbstractEngine implements CipherEngine
{
	private static final Logger logger = LogManager.manager().getLogger(AbstractCipherEngine.class, CategoryType.SECURITY_CRYPTO);
	private Cipher cipher;
	private AlgorithmParameterSpec params;
	
	/**
	 * 
	 */
	AbstractCipherEngine(final String transformation, final SecureRandom secureRandom, final Provider provider)
	{
		super(secureRandom, provider);
		
		try {
			cipher = Cipher.getInstance(transformation, provider);
		} catch (final Exception e) { 	
			final String msg = String.format("General exception trying to instantiate cipher engine with algorithm %s with provider %s: %s", transformation, provider.getName(), e.getMessage());
			logger.error(msg, e);
			throw new ApplicationException(msg, e);
		}
	}
		
	@Override	
	public <K extends Key> void encryptInit(final K key)
	{
		init(Cipher.ENCRYPT_MODE, key);
	}
	
	@Override
	public <K extends Key> void decryptInit(final K key)
	{
		init(Cipher.DECRYPT_MODE, key);
	}

	private <K extends Key> void init(final int opmode, final K key)
	{
		try {
			cipher.init(opmode, key, params, getSecureRandom());
		} catch (final GeneralSecurityException e) {
			final String msg = String.format("General security exception trying to initialize cipher: %s", e.getMessage());
			logger.error(msg, e);
			throw new ApplicationException(msg, e);
		}	
	}
	
	/* 
	 * @see com.valloc.security.crypto.CipherEngine#encrypt(byte[])
	 */
	@Override
	public byte[] encrypt(final byte[] plaintext)
	{
		return updateAndDoFinal(plaintext);
	}

	/* 
	 * @see com.valloc.security.crypto.CipherEngine#encrypt(java.lang.String)
	 */
	@Override
	public byte[] encryptString(final String plaintext)
	{
		final byte[] inputBytes = stringToBytes(plaintext);
		return encrypt(inputBytes);
	}
		
	/* 
	 * @see com.valloc.security.crypto.CipherEngine#decrypt(byte[])
	 */
	@Override
	public byte[] decrypt(final byte[] input)
	{
		return updateAndDoFinal(input);
	}

	/* 
	 * @see com.valloc.security.crypto.CipherEngine#decrypt(java.lang.String)
	 */
	@Override
	public String decryptString(final byte[] ciphertext)
	{
		final byte[] plaintext = decrypt(ciphertext); 
		return bytesToString(plaintext);
	}

	byte[] updateAndDoFinal(final byte[] input) 
	{ 
		final int originalSize = input.length;
		final int outputSize = cipher.getOutputSize(originalSize);
		byte[] output = new byte[outputSize];
		
		try {
			int updateLength = cipher.update(input, 0, input.length, output, 0);			
			updateLength += cipher.doFinal(output, updateLength);
			
			// trim here usually occurs when decrypting and with padding set
			if (updateLength < outputSize) { 
				final byte[] tmpArray = new byte[updateLength];
				System.arraycopy(output, 0, tmpArray, 0, updateLength);
				output = tmpArray;
			}
		} catch (final Exception e) {
			e.printStackTrace();
			output = new byte[0];
		}    
		
		return output;
	}

	/* 
	 * @see com.valloc.security.crypto.CipherEngine#setParams(java.security.spec.AlgorithmParameterSpec)
	 */
	@Override
	public void setParams(final AlgorithmParameterSpec params)
	{
		this.params = params;
	}
}