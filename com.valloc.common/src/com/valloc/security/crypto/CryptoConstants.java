/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.crypto;

/**
 * 
 *
 * @author wstevens
 */
final class CryptoConstants
{
	private CryptoConstants() {}
	
	/*
	 * "SunJCE" Provider: http://java.sun.com/javase/6/docs/technotes/guides/security/SunProviders.html#SunJCEProvider
	 * "BC" Provider: http://www.bouncycastle.org/specifications.html 
	 */
	static final String AES = "AES";
		
	/*
	 * "SunJCE" Provider: http://java.sun.com/javase/6/docs/technotes/guides/security/SunProviders.html#SunJCEProvider
	 * "BC" Provider: http://www.bouncycastle.org/specifications.html 
	 */
	static final String RSA = "RSA";
		
	/*
	 * "SunJCE" Provider: http://java.sun.com/javase/6/docs/technotes/guides/security/SunProviders.html#SunJCEProvider
	 * "BC" Provider: http://www.bouncycastle.org/specifications.html 
	 */
	static final String DSA = "DSA";
}
