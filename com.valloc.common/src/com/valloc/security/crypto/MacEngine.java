/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.crypto;

import javax.crypto.SecretKey;

/**
 * 
 *
 * @author wstevens
 */
interface MacEngine
{	
	void init(SecretKey key);
	
	byte[] hash(byte[] input);
	byte[] hash(String input);	
}