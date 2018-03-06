/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.core;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 *
 *
 * @author wstevens
 */
public interface PlatformEnvironment
{
	public URL getInstallDirectoryAsUrl();
	public File getInstallDirectoryAsFile();

	public List<URL> getBundleEntries(String bundleSymbolicName, String startingDirectory, String filePattern, boolean recurse);
	public List<URL> getBundleDirectories(String bundleSymbolicName, String startingDirectory, boolean recurse);
	public List<URL> getBundleDirectoryEntries(String bundleSymbolicName, String directory);
	public URL getBundleEntry(String bundleSymbolicName, String entryPath);
	public URL getBundleEntry(String bundleSymbolicName, URL entryPath);

	public URL bundleToFileUrl(URL bundleUrl);
}
