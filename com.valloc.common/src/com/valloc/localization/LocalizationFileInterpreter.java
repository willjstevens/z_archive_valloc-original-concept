/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.valloc.CategoryType;
import com.valloc.Constants;
import com.valloc.file.AbstractFileInterpreter;
import com.valloc.file.DirectoryNotFoundException;
import com.valloc.file.FileConstants;
import com.valloc.file.PlatformDirectoryId;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;



/**
 *
 *
 * @author wstevens
 */
public class LocalizationFileInterpreter extends AbstractFileInterpreter<PropertyResourceBundle>
{
	private static final Logger logger = LogManager.manager().getLogger(LocalizationFileInterpreter.class, CategoryType.LOCALIZATION);
	private static final String NL_DIRECTORY_ROOT = Constants.BUNDLE_ENV_ROOT + PlatformDirectoryId.BUNDLE_NL.rawDirectoryName();

	public ResourceBundle loadResourceBundle(final URL bundleItemUrl) {
		ResourceBundle resourceBundle = null;
		InputStream detachedInputStream = null;
		Reader propertyFileReader = null;

		try {
			detachedInputStream = getFileAccessor().readUrlIntoDetachedInputStream(bundleItemUrl);
			propertyFileReader = new InputStreamReader(detachedInputStream, Constants.DEFAULT_CHARSET);
			resourceBundle = new PropertyResourceBundle(propertyFileReader);
		} catch (final IOException e) {
			logger.error("Problem while attempting to read URL \"%s\" into detached input stream.", e, bundleItemUrl);
		} finally {
			// even though this is a detached resource stream - close the I/O in good form in case there are other
			//		non-stream resources to cleanup
			try {
				propertyFileReader.close();
			} catch (final IOException shrugOff) {
				logger.warn("Unanticipated problem while closing detached reader object: %s.", shrugOff);
			}
			try {
				detachedInputStream.close();
			} catch (final IOException shrugOff) {
				logger.warn("Unanticipated problem while closing detached input stream object: %s.", shrugOff);
			}
		}

		return resourceBundle;
	}

	public byte[] loadResourceItem(final URL bundleItemUrl) {
		byte[] itemBytes = null;

		try {
			itemBytes = getFileAccessor().readUrlStreamIntoDetachedBytes(bundleItemUrl);
		} catch (final IOException e) {
			logger.error("Problem while attempting to read URL \"%s\" into detached byte array.", e, bundleItemUrl);
		}

		return itemBytes;
	}

	public BundlePackage loadBundlePackage(final String bundleSymbolicName) {
		final BundlePackage bundlePackage = new BundlePackage(bundleSymbolicName);
		final String startingDirectory = Constants.BUNDLE_ENV_ROOT + PlatformDirectoryId.BUNDLE_NL.rawDirectoryName();

		final List<URL> bundleAndFragmentEntries =
			getPlatformEnvironment().getBundleEntries(bundleSymbolicName, startingDirectory, Constants.ASTERISK_STR, true);
		for (final URL entryUrl : bundleAndFragmentEntries) {
			final String entryPath = entryUrl.getPath();
			// always retrieve this entry's parent path (could be a directory or file)
			final Locale locale = translatePathToLocale(entryPath);
			final File itemFileObj = bundleItemUrlToFile(entryUrl);
			if (itemFileObj.isFile()) {
				final String fileName = itemFileObj.getName();
				ProvidedLocale providedLocale = null;
				// now that we know we have a resource file, this is indicative we are in an officially offered
				//		locale directory; so we need to register a Locale object if not already done
				if (!bundlePackage.hasLocale(locale)) {
					providedLocale = new ProvidedLocale(locale, entryUrl, bundlePackage);
					bundlePackage.addProvidedLocale(providedLocale);
				}

				// now determine file entry type and register with parent provided locale
				final boolean isPropertiesFile = getFileAccessor().isFileType(fileName, FileConstants.EXTENSION_PROPERTIES);
				final boolean isOtherFileType = !isPropertiesFile;
				final String bundleItemId = extractBundleItemId(fileName);
				AbstractBundleItem bundleItem = null;
				if (isPropertiesFile) {
					bundleItem = new PropertyFileBundleItem(bundleItemId, entryUrl, providedLocale);
				} else if (isOtherFileType) {
					bundleItem = new ObjectBundleItem<Object>(bundleItemId, entryUrl, providedLocale);
				}
				providedLocale.addBundleItem(bundleItem);
			} if (itemFileObj.isDirectory()) {
				if (logger.isFiner()) {
					logger.finer("Found directory \"%s\" when parsing bundle (and fragments) \"%s\".", itemFileObj.getPath(), bundleSymbolicName);
				}
			} else {
				// this is weird and shouldn't happen (either a directory or file), but I don't have much faith
				// 		in java.io.File on different platforms, especially with a "bundleentry://" scheme...
				final String msg = String.format("Could not detect bundle entry to be nor an NL subdirectory or file, but found entry \"%s\".", entryPath);
				final IllegalStateException e = new IllegalStateException(msg);
				logger.error(msg, e);
				throw e;
			}
		}

		return bundlePackage;
	}

	private String extractBundleItemId(final String fileName) {
		String bundleItemId = null;
		// here there could be file names like "common.properties" or "ui.admin.properties", so lastIndexOf is used
		final int extensionSeparatorIdx = fileName.lastIndexOf(FileConstants.EXTENSION_SEPERATOR);
		bundleItemId = fileName.substring(0, extensionSeparatorIdx);
		return bundleItemId;
	}

	private Locale translatePathToLocale(final String path) {
		Locale locale = null;
		final StringTokenizer pathTokenizer = new StringTokenizer(path, Constants.OSGI_CROSS_PLATFORM_PATH_SEPARATOR);

		// first we check for "/nl" directory and skip over once confirmed
		final String nlDirectory = pathTokenizer.nextToken();
		if (!NL_DIRECTORY_ROOT.equals(nlDirectory)) {
			final String msg = String.format("Expected NL directory prefix \"%s\" but found directory \"%s\".", NL_DIRECTORY_ROOT, nlDirectory);
			final DirectoryNotFoundException e = new DirectoryNotFoundException(msg);
			logger.error(msg, e);
			throw e;
		}

		// get first level directory for language
		String languageCodeDir = null;
		try {
			languageCodeDir = pathTokenizer.nextToken();
			if (languageCodeDir.length() != LocalizationConstants.ISO_639_1_LANGUAGE_CODE_LEN) {
				String msg = "Expected a %d length language code directory but instead found directory \"%s\".";
				msg = String.format(msg, LocalizationConstants.ISO_639_1_LANGUAGE_CODE_LEN, languageCodeDir);
				final DirectoryNotFoundException e = new DirectoryNotFoundException(msg);
				logger.error(msg, e);
				throw e;
			}
		} catch (final NoSuchElementException noSuchElementException) {
			final String msg = "Expected some language directory but found nothing.";
			final DirectoryNotFoundException e = new DirectoryNotFoundException(msg, noSuchElementException);
			logger.error(msg, e);
			throw e;
		}


		// get a *potential* second level directory for country
		String countryCodeDir = null;
		try {
			countryCodeDir = pathTokenizer.nextToken();
			if (countryCodeDir.length() != LocalizationConstants.ISO_3166_1_COUNTRY_CODE_LEN) {
				String msg = "Expected a %d length language code directory but instead found directory \"%s\".";
				msg = String.format(msg, LocalizationConstants.ISO_3166_1_COUNTRY_CODE_LEN, countryCodeDir);
				final DirectoryNotFoundException e = new DirectoryNotFoundException(msg);
				logger.error(msg, e);
				throw e;
			}
		} catch (final NoSuchElementException anticipated) {
			// this could be if in a directory like "/nl/en" and not "/nl/en/US", potentially this could
			//		be base language directory with property files, etc - regarless info
			String msg = "Although this could be okay, there was not a child country directory for parent/language directory \"%s\".";
			msg = String.format(msg, countryCodeDir);
			logger.fine(msg);
		}

		// if this far then assemble the Locale object
		final String languageCode = languageCodeDir; // reset name for formality
		final String countryCode = countryCodeDir; // reset name for formality
		final boolean isLanguageOnlyLocale = countryCode == null; // we didn't get as locale-specific as a country subdirectory
		final boolean isLanguageAndCountryLocale = countryCode != null; // we didn't get as locale-specific as a country subdirectory
		if (isLanguageOnlyLocale) {
			locale = new Locale(languageCode);
		} else if (isLanguageAndCountryLocale) {
			locale = new Locale(languageCode, countryCode);
		}

		return locale;
	}

	public File bundleItemUrlToFile(final URL bundleItemUrl) {
		File file = null;
		// first we convert here from a "bundleentry://" scheme into a standard "file://" scheme
		final URL fileSchemeUrl = getPlatformEnvironment().bundleToFileUrl(bundleItemUrl);
		// now it's safe to convert to file object
		file = getFileAccessor().urlToFile(fileSchemeUrl);
		return file;
	}

//	private String extractFileName(final String entryItemPath) {
//		String fileName = entryItemPath;
//		final int lastPathSeparator = entryItemPath.lastIndexOf(Constants.OSGI_CROSS_PLATFORM_PATH_SEPARATOR);
//		final int fileNameIdx = lastPathSeparator + 1;
//		fileName = entryItemPath.substring(fileNameIdx);
//		return fileName;
//	}
//
//	private boolean isFile(final String path) {
//		boolean isFile = false;
//
//		if (path != null && !isDirectory(path)) {
//			final int fileNameIdx = path.lastIndexOf(Constants.OSGI_CROSS_PLATFORM_PATH_SEPARATOR);
//			if (fileNameIdx != -1) {
//				final String fileName = path.substring(fileNameIdx);
//				if (fileName.contains(FileConstants.EXTENSION_SEPERATOR_STR)) {
//					isFile = true; // Yuk.
//				}
//			}
//		}
//
//		return isFile;
//	}
//
//	private boolean isDirectory(final String path) {
//		boolean isPath = false;
//		if (path != null && path.endsWith(Constants.OSGI_CROSS_PLATFORM_PATH_SEPARATOR)) {
//			isPath = true;
//		}
//		return isPath;
//	}

//	public List<Locale> determineAvailableBundleLocales(final String bundleSymbolicName) {
//		final List<Locale> availableLocales = new ArrayList<Locale>();
//
//		final List<URL> bundleAndFragmentDirectories =
//			getPlatformEnvironment().getBundleDirectories(bundleSymbolicName, Constants.BUNDLE_ENV_ROOT, true);
//
//		final String nlDirName = PlatformDirectoryId.BUNDLE_NL.rawDirectoryName();
//		for (final URL directoryUrl : bundleAndFragmentDirectories) {
//			final File directory = getFileAccessor().urlToFile(directoryUrl);
//			if (directory.isDirectory()) {
//				final File[] propertyFiles = directory.listFiles(fileNameFilter);
//				if (propertyFiles.length >= 1) {
//					final String parentDirectoryName = directory.getParent();
//					final String directoryName = directory.getName();
//					final boolean isLanguageBroadLocaleResources = parentDirectoryName.equals(nlDirName);
//					final boolean isCountrySpecificLocaleResources = !parentDirectoryName.equals(nlDirName);
//
//					Locale locale = null;
//					if (isLanguageBroadLocaleResources) {
//						// here our directory name signifies the abbreviation of the language we are dealing with
//						final String languageName = directoryName;
//						locale = new Locale(languageName);
//					} else if (isCountrySpecificLocaleResources) {
//						// here we are down into a country directory (dir name), with a language directory above
//						//		(parent directory name)
//						final String languageName = parentDirectoryName;
//						final String countryName = directoryName;
//						locale = new Locale(languageName, countryName);
//					} else {
//						String msg = "Unanticipated directory when determining locale. ";
//						msg += "Current directory name: \"%s\", parent directory name: \"%s\".";
//						msg = String.format(msg, directoryName, parentDirectoryName);
//						final IllegalStateException e = new IllegalStateException(msg);
//						logger.error(msg, e);
//						throw e;
//					}
//					availableLocales.add(locale);
//				}
//			}
//		}
//
//		return availableLocales;
//	}
}
