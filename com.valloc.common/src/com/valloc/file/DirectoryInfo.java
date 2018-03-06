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
abstract class DirectoryInfo
{
	private final CategoryType categoryType;
	private final File resolvedDirectory;
	
	/**
	 * @param categoryType
	 * @param rawName
	 */
	DirectoryInfo(final File resolvedDirectory, final CategoryType categoryType) {
		this.categoryType = categoryType;
		this.resolvedDirectory = resolvedDirectory;
	}

	public File getResolvedDirectory() {
		return resolvedDirectory;
	}

	public CategoryType getCategoryType() {
		return categoryType;
	}
}
