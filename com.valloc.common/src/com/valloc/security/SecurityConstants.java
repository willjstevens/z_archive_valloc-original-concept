/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security;

import java.security.Provider;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * 
 *
 * @author wstevens
 */
public final class SecurityConstants
{
	private SecurityConstants()	{}

	public static final String RNG = "RNG";
	
	/*
	 * http://java.sun.com/javase/6/docs/technotes/guides/security/StandardNames.html#SecureRandom
	 */
	public static final String DEFAULT_RNG = "SHA1PRNG";
	
	public final static Provider PREFERRED_PROVIDER 	= new BouncyCastleProvider();
	public final static String TLS_PROTOCAL 			= "TLS";
	public final static String DEFAULT_SECURE_PROTOCAL 	= TLS_PROTOCAL;
	public final static String JKS_KEY_STORE_TYPE 		= "JKS";
	public final static String DEFAULT_KEY_STORE_TYPE 	= JKS_KEY_STORE_TYPE;
	public final static String SUN_X509_CERT_TYPE 		= "SunX509";
	public final static String DEFAULT_CERT_TYPE 		= SUN_X509_CERT_TYPE;
	
}
