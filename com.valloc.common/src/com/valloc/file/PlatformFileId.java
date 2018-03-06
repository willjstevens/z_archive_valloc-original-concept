/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

import static com.valloc.CategoryType.*;
import static com.valloc.file.FileConstants.*;
import static com.valloc.file.PlatformDirectoryId.*;

import com.valloc.CategoryType;
import com.valloc.core.NodeManager;

/**
 *
 *
 * @author wstevens
 */
public enum PlatformFileId
{
	PRODUCT_KEYSTORE 	(KEY_STORE_FILE_NAME, 	PLATFORM_CONFIG,	SECURITY, 	null),
	VALLOC_XML			(CONFIG_FILE_NAME, 		PLATFORM_CONFIG,	CONFIG, 	determineVallocXmlFileInterpreter()),
	VALLOC_LOG			(LOG_FILE_NAME, 		PLATFORM_LOG,		LOG,		null);

	private String rawFileName;
	private PlatformDirectoryId directory;
	private CategoryType categoryType;
	final Class<? extends FileInterpreter<?>> fileInterpreterClass;

	private PlatformFileId(final String rawFileName, final PlatformDirectoryId directory, final CategoryType categoryType, final Class<? extends FileInterpreter<?>> fileInterpreterClass) {
		this.rawFileName = rawFileName;
		this.directory = directory;
		this.categoryType = categoryType;
		this.fileInterpreterClass = fileInterpreterClass;
	}

	private static Class<? extends FileInterpreter<?>> determineVallocXmlFileInterpreter() {
		final Class<? extends FileInterpreter<?>> fileInterpreterClass = NodeManager.getComponentManager().getNode().getVallocXmlFileInterpreter();
		return fileInterpreterClass;
	}

	public String rawFileName() {
		return rawFileName;
	}

	public PlatformDirectoryId directory() {
		return directory;
	}

	public CategoryType categoryType() {
		return categoryType;
	}

	public Class<? extends FileInterpreter<?>> fileInterpreterClass() {
		return fileInterpreterClass;
	}
}
