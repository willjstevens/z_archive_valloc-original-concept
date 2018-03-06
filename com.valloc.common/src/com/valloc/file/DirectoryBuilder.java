/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.valloc.CategoryType;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;
import com.valloc.util.Builder;

/**
 *
 *
 * @author wstevens
 */
public class DirectoryBuilder implements Builder<File>
{
	private static final Logger logger = LogManager.manager().getLogger(DirectoryBuilder.class, CategoryType.FILE);
	private final List<String> rawDirectoryNamePath = new ArrayList<String>();
	private File baseDirectory;
	
	private DirectoryBuilder() {}
	 
	public static DirectoryBuilder newDirectoryBuilder(final File baseDirectory) {
		if (!baseDirectory.isDirectory()) {
			final String msg = String.format("File %s is not a directory as expected. A directory must be specified for the base directory.", baseDirectory.getName());
			logger.warn(msg);
			throw new IllegalArgumentException(msg);
		}
		final DirectoryBuilder directoryBuilder = new DirectoryBuilder();
		directoryBuilder.baseDirectory = baseDirectory;
		
		return directoryBuilder;
	}

	public static DirectoryBuilder newDirectoryBuilder(final URL baseDirectory) {
		URI dirUri = null;
		try {
			dirUri = baseDirectory.toURI();
		} catch (final URISyntaxException e) {
			final String msg = String.format("Could not convert base directory URL \"%s\" into URI object.", baseDirectory.toString());
			logger.error(msg, e);
			throw new IllegalArgumentException(msg, e);
		}
		final File baseDirectoryFile = new File(dirUri);
		
		return newDirectoryBuilder(baseDirectoryFile);
	}
	
	public DirectoryBuilder addRawDirectoryName(final String rawDirectoryName) {
		rawDirectoryNamePath.add(rawDirectoryName);
		return this;
	}

	@Override
	public File build() throws DirectoryNotFoundException {
		File directory = baseDirectory; 
		
		for (final String rawDirectoryName : rawDirectoryNamePath) {
			final File candidateDir = new File(directory, rawDirectoryName);			
			if (!candidateDir.exists()) {
				String msg = "Failed to reconcile child directory \"%s\" as an existing directory, in relation to parent absolute directory \"%s\".";
				msg = String.format(msg, rawDirectoryName, directory.getAbsolutePath());
				logger.warn(msg);
				throw new DirectoryNotFoundException(msg);
			}
			directory = candidateDir;
		}
		
		return directory;
	}
}
