/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security;

import java.io.File;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;

/**
 * 
 *
 * @author wstevens
 */
public interface SecuritySupportService
{
	public void setNewSecureRandom();
	public void setRngAlgorithm(String rngAlgorithm);
	public void reseedSecureRandom();
	public void nextBytes(byte[] bytes);
	public SecureRandom getSecureRandom();
	public SSLContext newSSLContext();
	public void setPassphrase(char[] passphrase);
	public void setKeyStore(File keyStore);
}
