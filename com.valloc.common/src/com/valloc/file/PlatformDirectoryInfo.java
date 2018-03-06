/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

import java.io.File;


/**
 *
 *
 * @author wstevens
 */
public class PlatformDirectoryInfo extends DirectoryInfo
{
	private final PlatformDirectoryId platformDirectoryId;
	
	/**
	 * @param rawName
	 * @param categoryType
	 */
	public PlatformDirectoryInfo(final PlatformDirectoryId platformDirectoryId, final File resolvedDirectory) {
		super(resolvedDirectory, platformDirectoryId.categoryType());
		this.platformDirectoryId = platformDirectoryId;
	}

	public PlatformDirectoryId getPlatformDirectoryId() {
		return platformDirectoryId;
	}
}
