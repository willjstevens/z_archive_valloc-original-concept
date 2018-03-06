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
abstract class FileInfo
{
	private final CategoryType categoryType;
	private final File resolvedFile;
	private final FileInterpreter<?> fileInterpreter;

	/**
	 *
	 * @param categoryType
	 * @param subject
	 */
	FileInfo(final File resolvedFile, final CategoryType categoryType, final FileInterpreter<?> fileInterpreter) {
		this.resolvedFile = resolvedFile;
		this.categoryType = categoryType;
		this.fileInterpreter = fileInterpreter;
	}

	public File getResolvedFile() {
		return resolvedFile;
	}

	public CategoryType getCategoryType() {
		return categoryType;
	}

	public FileInterpreter<?> getFileInterpreter() {
		return fileInterpreter;
	}
}
