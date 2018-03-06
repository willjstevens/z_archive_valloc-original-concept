/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.valloc.CategoryType;

/**
 *
 *
 * @author wstevens
 */
public class RuntimeDirectoryInfo extends DirectoryInfo
{
	private final String runtimeDirectoryId;
	private final Set<RuntimeFileInfo> directoryFiles = new HashSet<RuntimeFileInfo>();
	
	public RuntimeDirectoryInfo(final String runtimeDirectoryId, final File resolvedDirectory, final CategoryType categoryType) {
		super(resolvedDirectory, categoryType);
		this.runtimeDirectoryId = runtimeDirectoryId;
	}

	public String getRuntimeDirectoryId() {
		return runtimeDirectoryId;
	}
	
	public void addDirectoryFile(final RuntimeFileInfo directoryFile) {
		directoryFiles.add(directoryFile);
	}

	public boolean removeDirectoryFile(final RuntimeFileInfo directoryFile) {
		return directoryFiles.remove(directoryFile);
	}
	
	public Iterator<RuntimeFileInfo> getDirectoryFiles() {
		return directoryFiles.iterator();
	}
}
