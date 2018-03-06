/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.crypto;

import java.security.KeyPair;
import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.SecretKey;

/**
 * 
 *
 * @author wstevens
 */
final class CryptoServiceImpl implements CryptoService
{
	private SecureRandom secureRandom;
	private Provider provider;
	
	private CryptoServiceImpl()	{}

	@Override
	public void setPreferredProvider(Provider provider)
	{
		this.provider = provider;
	}

	@Override
	public void setSecureRandom(SecureRandom secureRandom)
	{
		this.secureRandom = secureRandom;
	}

	@Override
	public CipherEngine newPreferredSymmetricEngine(SecretKey key)
	{
		return newAesCbcNonceEngine(key);
	}
	
	@Override
	public CipherEngine newAesCbcNonceEngine(SecretKey key)
	{
		return new AesCbcNonceEngine(key, secureRandom, provider);
	}

	@Override
	public CipherEngine newPbeSha3KeyTripleDesCbcEngine(char[] password, byte[] salt, int iterationCount)
	{
		return new PbeEngine(password, salt, iterationCount, PbeEngine.PREFERRED_PBE_SHA_KEY_TRIPLE_DES_CBC, provider);
	}

	/* 
	 * @see com.valloc.security.crypto.CryptoService#newPreferredMacEngine(javax.crypto.SecretKey)
	 */
	@Override
	public MacEngine newPreferredMacEngine()
	{
		return newHMacSha256();
	}
		
	/* 
	 * @see com.valloc.security.crypto.CryptoService#newHMacSha256(javax.crypto.SecretKey)
	 */
	@Override
	public MacEngine newHMacSha256()
	{
		return new HMacEngine(HMacEngine.HMAC_SHA_256, provider);
	}

	/* 
	 * @see com.valloc.security.crypto.CryptoService#newPreferredSignatureEngine(java.security.KeyPair)
	 */
	@Override
	public SignatureEngine newPreferredSignatureEngine(KeyPair keyPair)
	{
		return newSignatureDsaEngine(keyPair);
	}

	/* 
	 * @see com.valloc.security.crypto.CryptoService#newSignatureDsaEngine(java.security.KeyPair)
	 */
	@Override
	public SignatureEngine newSignatureDsaEngine(KeyPair keyPair)
	{
		return new DigitalSignatureEngine(keyPair, CryptoConstants.DSA, secureRandom, provider);
	}	
	
	
}