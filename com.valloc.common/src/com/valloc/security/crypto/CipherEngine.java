/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.crypto;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * 
 *
 * @author wstevens
 */
interface CipherEngine
{	
	public byte[] encrypt(byte[] plaintext);
	public byte[] decrypt(byte[] ciphertext);
	
	public byte[] encryptString(String plaintext);
	public String decryptString(byte[] ciphertext);
	
	abstract <K extends Key> void encryptInit(K key);
	abstract <K extends Key> void decryptInit(K key);	
	void setParams(AlgorithmParameterSpec params);
}