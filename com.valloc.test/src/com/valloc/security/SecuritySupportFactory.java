/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.security;

import java.io.File;

/**
 *
 *
 * @author wstevens
 */
public final class SecuritySupportFactory
{
	private final File testKeysFile;
	private final char[] testKeysPassphrase;

	public SecuritySupportFactory(final File testKeysFile, final char[] testKeysPassphrase) {
		this.testKeysFile = testKeysFile;
		this.testKeysPassphrase = testKeysPassphrase;
	}

	public SecuritySupportService newSecuritySupportService() {
		final SecuritySupportService securityService = new SecuritySupportServiceImpl();
		securityService.setKeyStore(testKeysFile);
		securityService.setPassphrase(testKeysPassphrase);
		return securityService;
	}

}
