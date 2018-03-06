/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.valloc.CategoryType;
import com.valloc.core.PlatformEnvironment;
import com.valloc.lifecycle.Initializable;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;

/**
 *
 *
 * @author wstevens
 */
public class DirectoryRegistry implements Initializable
{
	private final static Logger logger = LogManager.manager().getLogger(DirectoryRegistry.class, CategoryType.FILE);
	private final static PlatformDirectoryId[] PLATFORM_DIRECTORIES = {
		PlatformDirectoryId.PLATFORM_ROOT,
		PlatformDirectoryId.PLATFORM_CONFIG,
		PlatformDirectoryId.PLATFORM_LOG,
		PlatformDirectoryId.BUNDLE_NL
	};
	private final static Map<PlatformDirectoryId, PlatformDirectoryInfo> platformDirectories = new HashMap<PlatformDirectoryId, PlatformDirectoryInfo>();
	private final static Map<String, RuntimeDirectoryInfo> runtimeDirectories = new HashMap<String, RuntimeDirectoryInfo>();
	private PlatformEnvironment platformEnvironment;

	/*
	 * (non-Javadoc)
	 * @see com.valloc.lifecycle.Initializable#initialize()
	 */
	@Override
	public void initialize() {
		final List<PlatformDirectoryId> uninitiatedDirIds = Arrays.asList(PLATFORM_DIRECTORIES);
		for (int i = 0; i < uninitiatedDirIds.size(); i++) {
			final PlatformDirectoryId candidatePlatDirId = uninitiatedDirIds.get(i);
			final PlatformDirectoryId parentDirId = candidatePlatDirId.parent();
			if (parentDirId != null) { // potentially might/not have a parent
				if (platformDirectories.containsKey(parentDirId)) {
					final PlatformDirectoryInfo parentDirInfo = platformDirectories.get(parentDirId);
					final File parentDir = parentDirInfo.getResolvedDirectory(); // this should be set now
					final DirectoryBuilder dirBuilder = DirectoryBuilder.newDirectoryBuilder(parentDir);
					dirBuilder.addRawDirectoryName(candidatePlatDirId.rawDirectoryName());
					final File subjectDir = dirBuilder.build();
					final PlatformDirectoryInfo subjectDirInfo = new PlatformDirectoryInfo(candidatePlatDirId, subjectDir);
					platformDirectories.put(candidatePlatDirId, subjectDirInfo);
					uninitiatedDirIds.remove(i);
					i = 0;
					if (logger.isFiner()) {
						logger.fine(String.format("Added platform directory \"%s\" to the directory registry.", subjectDir.getAbsolutePath()));
					}
				}
			} else {
				// because no parent directory was associated with this platform directory, there is something
				//		'root' about it; so we determine what it is and load that..
				final File resolvedDir = determineAndLoadPlatformParentDirectory(candidatePlatDirId);
				final PlatformDirectoryInfo subjectDirInfo = new PlatformDirectoryInfo(candidatePlatDirId, resolvedDir);
				platformDirectories.put(candidatePlatDirId, subjectDirInfo);
				uninitiatedDirIds.remove(i);
				i = 0;
				if (logger.isFiner()) {
					logger.fine(String.format("Added platform directory \"%s\" to the directory registry.", resolvedDir.getAbsolutePath()));
				}
			}
		}
	}

	public void addRuntimeDirectory(final RuntimeDirectoryInfo runtimeDirectoryInfo) {
		runtimeDirectories.put(runtimeDirectoryInfo.getRuntimeDirectoryId(), runtimeDirectoryInfo);
	}

	public PlatformDirectoryInfo getPlatformDirectoryInfo(final PlatformDirectoryId platformDirectoryId) {
		return platformDirectories.get(platformDirectoryId);
	}

	public RuntimeDirectoryInfo getRuntimeDirectoryInfo(final String runtimeDirectoryId) {
		return runtimeDirectories.get(runtimeDirectoryId);
	}

	private File determineAndLoadPlatformParentDirectory(final PlatformDirectoryId platformDirectoryId) {
		File parentDir = null;

		switch (platformDirectoryId) {
		case PLATFORM_ROOT: parentDir = platformEnvironment.getInstallDirectoryAsFile();
		}

		return parentDir;
	}

	public void setEnvironment(final PlatformEnvironment platformEnvironment) {
		this.platformEnvironment = platformEnvironment;
	}
}
