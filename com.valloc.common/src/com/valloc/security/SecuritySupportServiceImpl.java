/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security;

import static com.valloc.security.SecurityConstants.*;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.valloc.CategoryType;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;

/**
 * 
 * 
 * @author wstevens
 */
public class SecuritySupportServiceImpl implements SecuritySupportService
{
	private static final Logger logger = LogManager.manager()
			.getLogger(SecuritySupportServiceImpl.class, CategoryType.SECURITY, CategoryType.SECURITY_TRANSPORT);

	private SecureRandom secureRandom;
	private String rngAlgorithm;
	private char[] passphrase;
	private File keyStore;

	public SecuritySupportServiceImpl() {
		Security.addProvider(SecurityConstants.PREFERRED_PROVIDER);
	}

	@Override
	public void setNewSecureRandom() {
		final String algorithm = rngAlgorithm != null ? rngAlgorithm : DEFAULT_RNG;
		try {
			secureRandom = SecureRandom.getInstance(algorithm);
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
			secureRandom = new SecureRandom();
		}

		reseedSecureRandom();
	}

	@Override
	public void reseedSecureRandom() {
		final byte[] entropy1 = secureRandom.generateSeed(64);
		final byte[] entropy2 = { 'c', 'h', 'r', 'i', 's', 't', 'i', 's', 'r', 'i', 's', 'e', 'n' };
		byte[] entropy3 = new byte[0];
		try {
			entropy3 = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
		} catch (final Exception e) {
		}

		final int seedLength = entropy1.length + entropy2.length + entropy3.length;
		final ByteBuffer tmpBuf = ByteBuffer.allocate(seedLength);
		tmpBuf.put(entropy2).put(entropy1).put(entropy3);
		tmpBuf.flip();

		final byte[] seedSupplement = new byte[seedLength];
		byte previousBite = seedSupplement[0] = tmpBuf.get();
		for (int idx = 1; idx < seedLength; idx++) {
			final byte bite = tmpBuf.get();
			seedSupplement[idx] = (byte) ~(previousBite ^ bite);
			previousBite = bite;
		}

		secureRandom.setSeed(seedSupplement);
	}

	@Override
	public SSLContext newSSLContext() {
		SSLContext retval = null;

		// TODO: switch any of these to bouncy castle??

		try {
			final KeyStore ks = KeyStore.getInstance(DEFAULT_KEY_STORE_TYPE);
			final KeyManagerFactory kmf = KeyManagerFactory.getInstance(DEFAULT_CERT_TYPE);
			ks.load(new FileInputStream(keyStore), passphrase);
			kmf.init(ks, passphrase);

			final KeyStore ts = KeyStore.getInstance(DEFAULT_KEY_STORE_TYPE);
			final TrustManagerFactory tmf = TrustManagerFactory.getInstance(DEFAULT_CERT_TYPE);
			ts.load(new FileInputStream(keyStore), passphrase);
			tmf.init(ts);

			retval = SSLContext.getInstance(DEFAULT_SECURE_PROTOCAL);
			retval.init(kmf.getKeyManagers(), tmf.getTrustManagers(), secureRandom);
		} catch (final Exception e) {
			final String msg = "Problem creating SSLContext: " + toString();
			logger.error(msg, e);
			throw new IllegalStateException(msg);
		}

		return retval;
	}

	@Override
	public void setRngAlgorithm(final String rngAlgorithm) {
		this.rngAlgorithm = rngAlgorithm;
	}

	@Override
	public void nextBytes(final byte[] bytes) {
		secureRandom.nextBytes(bytes);
	}

	@Override
	public SecureRandom getSecureRandom() {
		return secureRandom;
	}

	@Override
	public void setPassphrase(final char[] passphrase) {
		this.passphrase = passphrase;
	}

	@Override
	public void setKeyStore(final File keyStore) {
		this.keyStore = keyStore;
	}
}
