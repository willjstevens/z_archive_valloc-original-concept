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
public interface CryptoService
{
	public void setPreferredProvider(Provider provider);
	public void setSecureRandom(SecureRandom secureRandom);
		
	public CipherEngine newPreferredSymmetricEngine(SecretKey key);	
	public CipherEngine newAesCbcNonceEngine(SecretKey key);
	public CipherEngine newPbeSha3KeyTripleDesCbcEngine(char[] password, byte[] salt, int iterationCount);
	
	public MacEngine newPreferredMacEngine();
	public MacEngine newHMacSha256();
	
	public SignatureEngine newPreferredSignatureEngine(KeyPair keyPair);
	public SignatureEngine newSignatureDsaEngine(KeyPair keyPair);
}
