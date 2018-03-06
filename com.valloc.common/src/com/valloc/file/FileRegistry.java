/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.valloc.lifecycle.Initializable;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
public class FileRegistry implements Initializable
{

	private static final PlatformFileId[] PLATFORM_FILES = {
		PlatformFileId.VALLOC_XML,
		PlatformFileId.PRODUCT_KEYSTORE
	};
	private final static Map<PlatformFileId, PlatformFileInfo> platformFiles = new HashMap<PlatformFileId, PlatformFileInfo>();
	private final static Map<String, RuntimeFileInfo> runtimeFiles = new HashMap<String, RuntimeFileInfo>();
	private DirectoryRegistry directoryRegistry;

	/*
	 * (non-Javadoc)
	 * @see com.valloc.lifecycle.Initializable#initialize()
	 */
	@Override
	public void initialize() {
		for (final PlatformFileId platformFileId : PLATFORM_FILES) {
			final PlatformDirectoryInfo directoryInfo = directoryRegistry.getPlatformDirectoryInfo(platformFileId.directory());
			final File resolvedDirectory = directoryInfo.getResolvedDirectory();
			final String rawFileName = platformFileId.rawFileName();
			final FileInterpreter<?> fileInterpreter = Util.wrappedClassNewInstance(platformFileId.fileInterpreterClass());
			final File resolvedFile = new File(resolvedDirectory, rawFileName);
			final PlatformFileInfo platformFileInfo = new PlatformFileInfo(platformFileId, resolvedFile, fileInterpreter, directoryInfo);
			platformFiles.put(platformFileId, platformFileInfo);
		}
	}

	public void addRuntimeFile(final RuntimeFileInfo runtimeFileInfo) {
		runtimeFiles.put(runtimeFileInfo.getRuntimeFileId(), runtimeFileInfo);
	}

	public PlatformFileInfo getPlatformFileInfo(final PlatformFileId platformFileId) {
		return platformFiles.get(platformFileId);
	}

	public RuntimeFileInfo getRuntimeFileInfo(final String runtimeFileId) {
		return runtimeFiles.get(runtimeFileId);
	}

	public void setDirectoryRegistry(final DirectoryRegistry directoryRegistry) {
		this.directoryRegistry = directoryRegistry;
	}
}
