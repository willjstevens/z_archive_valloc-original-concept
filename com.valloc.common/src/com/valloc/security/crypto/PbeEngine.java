/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.crypto;

import java.security.Provider;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.valloc.ApplicationException;
import com.valloc.CategoryType;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;

/**
 * 
 *
 * @author wstevens
 */
class PbeEngine extends SymmetricEngine
{
	private static final Logger logger = LogManager.manager().getLogger(PbeEngine.class, CategoryType.SECURITY_CRYPTO);
	
	/*
	 * http://www.bouncycastle.org/specifications.html
	 */
	/* This one is fine as the key size is satisfied. */
	static final String PBE_SHA_2KEY_TRIPLE_DES_CBC = "PBEWithSHAAnd2-KeyTripleDES-CBC";
	/* This returns an error saying Illegal Key Size: 192 */
	static final String PBE_SHA_3KEY_TRIPLE_DES_CBC = "PBEWithSHAAnd3-KeyTripleDES-CBC";
	static final String PREFERRED_PBE_SHA_KEY_TRIPLE_DES_CBC = PBE_SHA_2KEY_TRIPLE_DES_CBC;
	
	/**
	 * 
	 */
	PbeEngine(final char[] password, final byte[] salt, final int iterationCount, final String transformation, final Provider provider)
	{
		super(null, transformation, provider);
		
		try {
			final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(transformation, provider);
			
			final PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, iterationCount);	        
			final SecretKey secretKey = keyFactory.generateSecret(pbeKeySpec);
	        setKey(secretKey);
	        
			pbeKeySpec.clearPassword();			
		} catch (final Exception e) {
			final String msg = String.format("Problem creating password based encryption engine with algorithm %s and provider %s: %s", transformation, provider.getName(), e.getMessage());
			logger.error(msg, e);
			throw new ApplicationException(msg, e);	
		}
	}
}