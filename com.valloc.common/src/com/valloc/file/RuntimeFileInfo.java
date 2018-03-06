/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

import java.io.File;

import com.valloc.CategoryType;

/**
 *
 *
 * @author wstevens
 */
public class RuntimeFileInfo extends FileInfo
{
	private final String runtimeFileId;
	private final RuntimeDirectoryInfo runtimeDirectoryInfo;

	/**
	 * @param categoryType
	 * @param id
	 * @param subject
	 */
	public RuntimeFileInfo(final String runtimeFileId, final File resolvedFile, final RuntimeDirectoryInfo runtimeDirectoryInfo, final CategoryType categoryType, final FileInterpreter<?> fileInterpreter) {
		super(resolvedFile, categoryType, fileInterpreter);
		this.runtimeDirectoryInfo = runtimeDirectoryInfo;
		this.runtimeFileId = runtimeFileId;
	}

	public String getRuntimeFileId() {
		return runtimeFileId;
	}

	public RuntimeDirectoryInfo getRuntimeDirectoryInfo() {
		return runtimeDirectoryInfo;
	}
}
