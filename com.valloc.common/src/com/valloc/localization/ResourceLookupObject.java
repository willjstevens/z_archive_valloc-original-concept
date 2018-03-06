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
final class ResourceLookupObject
{
	private final String key;
	private final BundleItemInfo bundleItemInfo;
//	private T cachedSubject;

	ResourceLookupObject(final BundleItemInfo bundleItemInfo, final String key) {
		this.key = key;
		this.bundleItemInfo = bundleItemInfo;
	}

	String getKey() {
		return key;
	}

	BundleItemInfo getBundleItemInfo() {
		return bundleItemInfo;
	}
}
