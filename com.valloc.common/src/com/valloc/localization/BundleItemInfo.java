/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

/**
 *
 *
 * @author wstevens
 */
class BundleItemInfo
{
	private final String bundleItemId;
	private final String bundleSymbolicName;
	private final String fileName;

	BundleItemInfo(final String bundleItemId, final String bundleSymbolicName, final String fileName) {
		this.bundleItemId = bundleItemId;
		this.bundleSymbolicName = bundleSymbolicName;
		this.fileName = fileName;
	}

	String getBundleItemId() {
		return bundleItemId;
	}

	String getBundleSymbolicName() {
		return bundleSymbolicName;
	}

	String getFileName() {
		return fileName;
	}
}
