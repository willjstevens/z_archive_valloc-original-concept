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
public class PlatformFileInfo extends FileInfo
{
	private final PlatformFileId platformFileId;
	private final PlatformDirectoryInfo platformDirectoryInfo;

	/**
	 * @param categoryType
	 * @param id
	 * @param subject
	 */
	public PlatformFileInfo(final PlatformFileId platformFileId, final File resolvedFile, final FileInterpreter<?> fileInterpreter, final PlatformDirectoryInfo directoryInfo) {
		super(resolvedFile, platformFileId.categoryType(), fileInterpreter);
		this.platformDirectoryInfo = directoryInfo;
		this.platformFileId = platformFileId;
	}

	public PlatformFileId getPlatformFileId() {
		return platformFileId;
	}

	public PlatformDirectoryInfo getPlatformDirectoryInfo() {
		return platformDirectoryInfo;
	}
}
