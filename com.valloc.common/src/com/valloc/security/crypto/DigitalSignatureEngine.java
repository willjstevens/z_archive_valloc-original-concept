/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.crypto;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Signature;

import com.valloc.ApplicationException;
import com.valloc.CategoryType;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;

/**
 * 
 *
 * @author wstevens
 */
class DigitalSignatureEngine extends AbstractEngine implements SignatureEngine
{		
	private static final Logger logger = LogManager.manager().getLogger(DigitalSignatureEngine.class, CategoryType.SECURITY_CRYPTO);
	
	private Signature signature;
	private final KeyPair keyPair;
	
	/**
	 * 
	 */
	DigitalSignatureEngine(final KeyPair keyPair, final String transformation, final SecureRandom secureRandom, final Provider provider)
	{
		super(secureRandom, provider);
		this.keyPair = keyPair;
		
		try {
			signature = Signature.getInstance(transformation, getProvider());
		} catch (final NoSuchAlgorithmException e) {	
			final String msg = String.format("Algorithm %s is not available in provider %s: %s", transformation, provider.getName(), e.getMessage());
			logger.error(msg, e);
			throw new ApplicationException(msg, e);
		}	
	}

	@Override
	public byte[] sign(final byte[] plaintext)
	{
		byte[] retval = null;
		
		try {
			signature.initSign(keyPair.getPrivate(), getSecureRandom());
			signature.update(plaintext);
			retval = signature.sign();
		} catch (final GeneralSecurityException e) {
			final String msg = "General security exception signing plaintext:" + e.getMessage();
			logger.error(msg, e);
			throw new ApplicationException(msg, e);	
		}
		
		return retval;
	}


	@Override
	public boolean verify(final byte[] originalPlaintext, final byte[] signatureCiphertext)
	{
		boolean retval = false;
		
		try {
			signature.initVerify(keyPair.getPublic());
			signature.update(originalPlaintext);
			retval = signature.verify(signatureCiphertext);
		} catch (final GeneralSecurityException e) {
			final String msg = "General security exception verifying plaintext against signature ciphertext: " + e.getMessage();
			logger.error(msg, e);
			throw new ApplicationException(msg, e);	
		}

		return retval;
	}

	@Override
	public boolean verify(final String originalPlaintext, final String signatureCiphertext)
	{
		final byte[] arg1Bytes = stringToBytes(originalPlaintext);
		final byte[] arg2Bytes = stringToBytes(signatureCiphertext);
		return verify(arg1Bytes, arg2Bytes);
	}
	
	@Override
	public byte[] sign(final String plaintext)
	{
		final byte[] plaintextBytes = stringToBytes(plaintext);
		return sign(plaintextBytes);
	}
}