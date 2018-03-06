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
interface SignatureEngine
{	
	public byte[] sign(byte[] plaintext);
	public boolean verify(byte[] originalPlaintext, byte[] signatureCiphertext);
	
	public byte[] sign(String plaintext);
	public boolean verify(String originalPlaintext, String signatureCiphertext);
}