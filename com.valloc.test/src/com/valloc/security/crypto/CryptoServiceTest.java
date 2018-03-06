/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security.crypto;

import static com.valloc.security.SecurityConstants.*;
import static com.valloc.security.crypto.CryptoConstants.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Signature;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.valloc.AbstractTest;
import com.valloc.util.Util;

/**
 * 
 *
 * @author wstevens
 */
public final class CryptoServiceTest extends AbstractTest
{		
	
	@Before
	public void setUp() throws Exception
	{
		
	}
	
	@Test
	public void testProviderCryptoServiceAvailability()
	{		
		// test ciphers
		testCipherAlgorithm(AesCbcNonceEngine.TRANSFORMATION, PREFERRED_PROVIDER); // tests "AES/CBC/PKCS7Padding"		
		// test mac 
		testMacAlgorithm(HMacEngine.HMAC_SHA_256, PREFERRED_PROVIDER);		
		// test signature 
		testSignatureFactory(DSA, PREFERRED_PROVIDER);				
		// test key generators
		testKeyGenerator(AES, PREFERRED_PROVIDER); // tests "AES"		
		// test secret key factories
		testSecretKeyFactory(PbeEngine.PREFERRED_PBE_SHA_KEY_TRIPLE_DES_CBC, PREFERRED_PROVIDER); // tests PBEWithSHAAnd3-KeyTripleDES-CBC
	}
	
	private void testCipherAlgorithm(final String algorithm, final Provider provider)
	{
		try {
			Cipher.getInstance(algorithm, provider);
		} catch (final NoSuchAlgorithmException e) {
			Assert.fail("Cipher algorithm \"" + algorithm + "\" is not available with provider " + provider.getName());
		} catch (final NoSuchPaddingException e) {
			Assert.fail("Cipher algorithm \"" + algorithm + "\" does not have its padding method available with provider " + provider.getName());
		}
	}
		
	private void testSignatureFactory(final String algorithm, final Provider provider)
	{
		try {
			Signature.getInstance(algorithm, provider);
		} catch (final NoSuchAlgorithmException e) {
			Assert.fail("Signature algorithm \"" + algorithm + "\" is not available with provider " + provider.getName());
		}
	}
	
	private void testMacAlgorithm(final String algorithm, final Provider provider)
	{
		try {
			Mac.getInstance(algorithm, provider);
		} catch (final NoSuchAlgorithmException e) {
			Assert.fail("MacEngine algorithm \"" + algorithm + "\" is not available with provider " + provider.getName());
		}
	}
	
	private void testKeyGenerator(final String algorithm, final Provider provider)
	{
		try {
			KeyGenerator.getInstance(algorithm, provider);
		} catch (final NoSuchAlgorithmException e) {
			Assert.fail("KeyGenerator algorithm \"" + algorithm + "\" is not available with provider " + provider.getName());
		}
	}

	private void testSecretKeyFactory(final String algorithm, final Provider provider)
	{
		try {
			SecretKeyFactory.getInstance(algorithm, provider);
		} catch (final NoSuchAlgorithmException e) {
			Assert.fail("SecretKeyFactory algorithm \"" + algorithm + "\" is not available with provider " + provider.getName());
		}
	}
	
	@Test
	public void testHMacSha256Engine()
	{
		SecretKey key = generateSecretKey(256);		
		final String msg = "Hello, there everybody! This is plaintext to be encrypted. Cheers, Rascal!";
		final byte[] plaintext = Util.toByteArray(msg);
		
		// compare hashes with use of same key and different engine objects
		MacEngine macEngine1 = new HMacEngine(HMacEngine.HMAC_SHA_256, PREFERRED_PROVIDER);
		macEngine1.init(key);
        byte[] ciphertext1 = macEngine1.hash(plaintext);		
        MacEngine macEngine2 = new HMacEngine(HMacEngine.HMAC_SHA_256, PREFERRED_PROVIDER);
        macEngine2.init(key);		
        byte[] ciphertext2 = macEngine2.hash(plaintext);
	    Assert.assertArrayEquals("Both HMAC digests did not match.", ciphertext1, ciphertext2); 

		// verify hashes are different with use of different key and same engine objects
		macEngine1 = new HMacEngine(HMacEngine.HMAC_SHA_256, PREFERRED_PROVIDER);
		macEngine1.init(key);
        ciphertext1 = macEngine1.hash(plaintext);		
        macEngine2 = new HMacEngine(HMacEngine.HMAC_SHA_256, PREFERRED_PROVIDER);
        key = generateSecretKey(256);
        macEngine2.init(key);		
        ciphertext2 = macEngine2.hash(plaintext);
	    Assert.assertNotSame("Both HMAC digests objects with different keys initialized were not different.", ciphertext1, ciphertext2); 
	}
	
	@Test
	public void testPbeSha3KeyTripleDesCbcEngine()
	{
		final char[] password = "Rascalicious!".toCharArray();
		final byte[] salt = {0xf, 0x1, 0xe, 0x2, 0xd, 0x3, 0xc, 0x4, 0xb, 0x5, 0xa, 0x6, 0x9, 0x7, 0x8, 0x0};
		final int iterationCount = 2048;
		
		CipherEngine engine = // create key with password 
			new PbeEngine(password, salt, iterationCount, PbeEngine.PREFERRED_PBE_SHA_KEY_TRIPLE_DES_CBC, PREFERRED_PROVIDER);
				
		final String msg = "Hello, there everybody! This is plaintext to be encrypted. Cheers, Rascal!";
		final byte[] expectedPlaintext = msg.getBytes();
		
		final byte[] ciphertext = engine.encrypt(expectedPlaintext);
		Assert.assertNotSame("Original plaintext wrongfully matched ciphertext.", expectedPlaintext, ciphertext);
		
		engine = // recreate key with password again
			new PbeEngine(password, salt, iterationCount, PbeEngine.PREFERRED_PBE_SHA_KEY_TRIPLE_DES_CBC, PREFERRED_PROVIDER);
					
        final byte[] actualPlaintext = engine.decrypt(ciphertext);        
        Assert.assertArrayEquals(expectedPlaintext, actualPlaintext);
        
        final String reconstructedMsg = new String(expectedPlaintext);
        Assert.assertEquals("Original and reconstructed message did not match.", msg, reconstructedMsg);		
	}
	
	@Test
	public void testDsaBasicEngine()
	{	
        final KeyPair keyPair = generateKeyPair(DSA, 512);
		
		final String msg = "Hello, there everybody! This is plaintext to be encrypted. Cheers, Rascal!";
		final byte[] plaintext = msg.getBytes();
		
		final SignatureEngine engine = new DigitalSignatureEngine(keyPair, CryptoConstants.DSA, new SecureRandom(), PREFERRED_PROVIDER);
		
		final byte[] ciphertext = engine.sign(plaintext);
		final boolean isVerified = engine.verify(plaintext, ciphertext);
		Assert.assertTrue("Signature value did not equal a re-sign of plaintext.", isVerified);		
	}
	
	@Test
	public void testAesCbcNonceEngine()
	{	
        final SecretKey key = generateSecretKey(128);
		
		final String msg = "Hello, there everybody! This is plaintext to be encrypted. Cheers, Rascal!";
		final byte[] expectedPlaintext = msg.getBytes();
		
		final CipherEngine engine = new AesCbcNonceEngine(key, new SecureRandom(), PREFERRED_PROVIDER);
		
		byte[] ciphertext = engine.encrypt(expectedPlaintext);
		Assert.assertNotSame("Original plaintext wrongfully matched ciphertext.", expectedPlaintext, ciphertext);
		
        final byte[] actualPlaintext = engine.decrypt(ciphertext);        
        Assert.assertArrayEquals(expectedPlaintext, actualPlaintext);
        
        String reconstructedMsg = new String(expectedPlaintext);
        Assert.assertEquals("Original and reconstructed message did not match.", msg, reconstructedMsg);
        
        ciphertext = engine.encryptString(msg);
        reconstructedMsg = engine.decryptString(ciphertext);
        Assert.assertEquals("Encrypting string to and from cipher text does not produce original message.", msg, reconstructedMsg);
	}
	
	@Test
	public void testCipherWithMuchContent()
	{ 	     
        final SecretKey key = generateSecretKey(128);
        
//        byte[] expectedPlaintext = TestUtil.getFileBytes(LARGE_FILE);
        final byte[] expectedPlaintext = getTestTextFileContents().getBytes();
        
        final CipherEngine engine = new AesCbcNonceEngine(key, new SecureRandom(), new BouncyCastleProvider());
		
//        long start = System.currentTimeMillis();
        
		final byte[] ciphertext = engine.encrypt(expectedPlaintext);
		Assert.assertNotSame("Original plaintext wrongfully matched ciphertext.", expectedPlaintext, ciphertext);
		
        final byte[] actualPlaintext = engine.decrypt(ciphertext);        
        Assert.assertArrayEquals(expectedPlaintext, actualPlaintext);
        
        // this time is only useful when Asserts are not being executed since each Assert KILLS performance
//        double totalTime = (System.currentTimeMillis() - start) / 1000D;
//        System.out.println(totalTime);
	}
	
	private SecretKey generateSecretKey(final int keySize)
	{
		KeyGenerator generator = null;
		try {
			generator = KeyGenerator.getInstance(AES, PREFERRED_PROVIDER);
		} catch (final Exception e) {
			e.printStackTrace();
		}		
		generator.init(keySize);	        
		return generator.generateKey();
	}
	
	private KeyPair generateKeyPair(final String algorithm, final int keySize)
	{	
		KeyPairGenerator generator = null;
		try {
			generator = KeyPairGenerator.getInstance(algorithm, PREFERRED_PROVIDER);
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		}    
		generator.initialize(keySize);
		return generator.generateKeyPair();
	}
}