/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.crypto;

import java.security.InvalidKeyException;
import java.security.Provider;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.valloc.ApplicationException;
import com.valloc.CategoryType;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;

/**
 * 
 *
 * @author wstevens
 */
class HMacEngine extends AbstractEngine implements MacEngine
{
	private static final Logger logger = LogManager.manager().getLogger(HMacEngine.class, CategoryType.SECURITY_CRYPTO);
	
	/*
	 * http://www.bouncycastle.org/specifications.html
	 */
	static final String HMAC_SHA_256 = "HMacSHA256";
	
	private Mac mac;
	private SecretKey hMacKey;
	private final String transformation;
	
	/**
	 * 
	 */
	HMacEngine(final String transformation, final Provider provider)
	{
		super(provider);
		
		this.transformation = transformation;
		
		try {
			mac = Mac.getInstance(transformation, provider);
		} catch (final Exception e) { 
			final String msg = String.format("Algorithm %s is not available in provider %s: %s", transformation, provider.getName(), e.getMessage());
			logger.error(msg, e);
			throw new ApplicationException(msg, e);	
		}
	}
	
	/* 
	 * @see com.valloc.security.crypto.MacEngine#init(javax.crypto.SecretKey)
	 */
	@Override
	public void init(final SecretKey key)
	{
		// get passed symmetric key encoded bytes and create an hmac key  
		hMacKey = new SecretKeySpec(key.getEncoded(), transformation);
		
		try {			
			mac.init(hMacKey);
		} catch (final InvalidKeyException e) {
			final String msg = "Invalid key exception when initializing secret key: " + e.getMessage();
			logger.error(msg, e);
			throw new ApplicationException(msg, e);	
		}
	}

	@Override
	public byte[] hash(final byte[] input)
	{
		byte[] retval = null;
		
		try {
			retval = mac.doFinal(input);
		} catch (final IllegalStateException e) {
			final String msg = "Invalid state exception when calling doFinal on MAC: " + e.getMessage();
			logger.error(msg, e);
			throw new ApplicationException(msg, e);	
		}
		
		return retval;
	}

	/* 
	 * @see com.valloc.security.crypto.MacEngine#hash(java.lang.String)
	 */
	@Override
	public byte[] hash(final String input)
	{
		final byte[] inputBytes = stringToBytes(input);
		return hash(inputBytes);
	}
}