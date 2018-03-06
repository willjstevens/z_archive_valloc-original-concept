/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

import junit.framework.Assert;

import com.valloc.log.LogConfiguration;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractTest
{
	private static boolean isLoaded;
	private static final String TEST_BOOTSTRAP_PROPS = "test-bootstrap.properties";
	private static final String TEST_PROPS_FILE_NAME = "test.properties";
	public static final String FILE_SEP = System.getProperty("file.separator");
	public static final String LINE_FEED = System.getProperty("line.separator");

	private static String miscDirStr;
	private static String workDirStr;
	private static String serverRootStr;
	private static String agentRootStr;
	private static String desktopRootStr;
	private static String testKeysFileNameStr;
	private static char[] testKeysFilePassphraseStr;

	private static File workDir;
	// private static File miscDir;
	// private static File testKeysFile;

	protected boolean doneWithTest;

	{
		load();
	}

	public void load() {
		if (isLoaded) {
			return;
		}

		final PrintStream origOut = System.out;
		final PrintStream origErr = System.err;
		// Here we set type from log configuration which is what the fallback console handler uses when code
		// invokes a non-initialized logger with no file handler. This is expected in unit testing.
		final String charset = LogConfiguration.DEFAULT_HANDLER_ENCODING;
		try {
			final PrintStream newOut = new PrintStream(origOut, true, charset);
			System.setOut(newOut); // if here then success
		} catch (final UnsupportedEncodingException e) {
			// report and print to original stderr; and maintain original...
			final String msg = String
					.format("Could not set desired character set (\"%s\") for standard OUT on JUnit console test execution. Keeping original JVM charset.", charset);
			System.err.println(msg);
			e.printStackTrace();
		}
		try {
			final PrintStream newErr = new PrintStream(origErr, true, charset);
			System.setErr(newErr); // if here then success
		} catch (final UnsupportedEncodingException e) {
			// report and print to original stderr; and maintain original...
			final String msg = String
					.format("Could not set desired character set (\"%s\") for standard ERR on JUnit console test execution. Keeping original JVM charset.", charset);
			System.err.println(msg);
			e.printStackTrace();
		}

		final Properties properties = System.getProperties();

		InputStream is = null;
		try { // first load platform dependent properties file
			final String testBootstrapFile = properties.getProperty("user.home") + FILE_SEP + TEST_BOOTSTRAP_PROPS;
			is = new FileInputStream(testBootstrapFile);
			properties.load(is);
		} catch (final Exception e) {
			printThrowableAndFail(e);
			System.exit(1);
		} finally {
			try {
				is.close();
			} catch (final IOException e) {
				printThrowableAndFail(e);
			}
		}

		try { // now load primary/base test properties file
			is = new FileInputStream(TEST_PROPS_FILE_NAME);
			properties.load(is);
		} catch (final Exception e) {
			printThrowableAndFail(e);
			System.exit(1);
		} finally {
			try {
				is.close();
			} catch (final IOException e) {
				printThrowableAndFail(e);
			}
		}

		miscDirStr = properties.getProperty("misc-dir");
		workDirStr = properties.getProperty("test-work");
		serverRootStr = workDirStr + FILE_SEP + properties.getProperty("mock.root.server");
		agentRootStr = workDirStr + FILE_SEP + properties.getProperty("mock.root.agent");
		desktopRootStr = workDirStr + FILE_SEP + properties.getProperty("mock.root.desktop");
		testKeysFileNameStr = miscDirStr + FILE_SEP + properties.getProperty("test-keys-file-name");
		testKeysFilePassphraseStr = properties.getProperty("test-keys-file-passphrase").toCharArray();

		isLoaded = true;
	}

	protected File buildTestKeysFile() {
		return new File(testKeysFileNameStr);
	}

	protected char[] getTestKeysFilePassphrase() {
		return testKeysFilePassphraseStr;
	}

	protected void setupWorkDir() {
		workDir = strPathToFile(workDirStr);
		if (workDir.exists()) {
			recursiveRemove(workDir);
		}
		createDir(workDir);
	}

	protected void cleanWorkDir() {
		if (workDir.exists()) {
			recursiveRemove(workDir);
		}
	}

	protected File strPathToFile(final String path) {
		File retval = null;

		try {
			retval = new File(path);
		} catch (final NullPointerException e) {
			printThrowableAndFail(e);
		}

		return retval;
	}

	protected File initStrPathToFile(final String path) {
		final File retval = strPathToFile(path);
		if (retval != null) {
			createDir(retval);
		}

		return retval;
	}

	public boolean createDir(final File directory) {
		boolean wasCreated = false;

		try {
			if (!directory.exists()) {
				wasCreated = directory.mkdir();
				if (!wasCreated) {
					wasCreated = directory.mkdirs();
				}
			}
		} catch (final SecurityException e) {
			printThrowableAndFail(e);
		}

		return wasCreated;
	}

	protected void cleanDir(final File directory) {
		try {
			final File[] files = directory.listFiles();
			for (final File file : files) {
				file.delete();
			}
			directory.delete();
		} catch (final SecurityException e) {
			printThrowableAndFail(e);
		}
	}

	protected void recursiveRemove(final File directory) {
		try {
			final File[] files = directory.listFiles();
			if (files != null) {
				for (final File file : files) {
					if (file.isDirectory()) {
						recursiveRemove(file);
					}
					final boolean deleted = file.delete();
					if (!deleted) {
						file.deleteOnExit();
					}
				}
			}
			final boolean deleted = directory.delete();
			if (!deleted) {
				directory.deleteOnExit();
			}
		} catch (final SecurityException e) {
			printThrowableAndFail(e);
		}
	}

	protected byte[] getFileBytes(final String absoluteFile) {
		byte[] retval = null;

		FileInputStream fis = null;
		try {
			final File file = new File(absoluteFile);
			fis = new FileInputStream(file);
			final int size = (int) file.length();
			retval = new byte[size];
			final int amtRead = fis.read(retval);
			if (size != amtRead) {
				final String msg = String.format("Amount read from file %s was not fully read.", absoluteFile);
				throw new IllegalArgumentException(msg);
			}
		} catch (final IOException e) {
			printThrowableAndFail(e);
		} finally {
			try {
				fis.close();
			} catch (final Exception e) {
				printThrowableAndFail(e);
			}
		}

		return retval;
	}

	protected InputStream getFileAsStream(final String name) {
		InputStream retval = null;
		try {
			retval = new FileInputStream(name);
		} catch (final FileNotFoundException e) {
			printThrowableAndFail(e);
		}
		return retval;
	}

	protected URL getResource(final String name) {
		return getClass().getResource(name);
	}

	protected File getResourceAsFile(final String name) throws Exception {
		final URL url = getResource(name);
		final URI uri = url.toURI();
		return new File(uri);
	}

	protected byte[] getFileAsByteArray(final String name) throws Exception {
		byte[] retval = null;

		FileInputStream fis = null;
		try {
			final File file = getResourceAsFile(name);
			final long size = file.length();
			retval = new byte[((int) size)];
			fis = new FileInputStream(file);
			int read = 0;
			int idx = 0;
			while ((read = fis.read()) != -1) {
				retval[idx++] = (byte) read;
			}
			if (idx != size) {
				Assert.fail(String.format("File %s of byte size %d was not fully read; only read %d bytes.", name, size, idx));
			}
		} catch (final Exception e) {
			printThrowableAndFail(e);
		} finally {
			fis.close();
		}

		return retval;
	}

	protected String getFileAsString(final String name) throws Exception {
		return getFileAsEncodedString(name, Constants.DEFAULT_CHARSET);
	}

	protected String getFileAsEncodedString(final String name, final Charset charset) throws Exception {
		final byte[] contents = getFileAsByteArray(name);
		return new String(contents, charset);
	}

	protected ApplicationException getTestException(final String message) {
		return new ApplicationException(message);
	}

	protected ApplicationException getTestException() {
		return getTestException("INTENTIONAL_EXCEPTION_FOR_TEST");
	}

	protected void throwIntentionalException() {
		throw getTestException();
	}

	protected void println(final String msg) {
		System.out.println(msg);
	}

	protected void printThrowable(final Throwable e) {
		System.err.println(e);
		e.printStackTrace();
	}

	protected void printThrowableAndFail(final Throwable e) {
		printThrowable(e);
		Assert.fail(e.toString());
	}

	protected String getTestTextFileContents() {
		final StringBuilder sb = new StringBuilder(100);
		sb.append("Hello,").append(LINE_FEED);
		sb.append("\thow").append(LINE_FEED);
		sb.append("\t\tare").append(LINE_FEED);
		sb.append("\t\t\tyou?").append(LINE_FEED).append(LINE_FEED);
		sb.append("bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla").append(LINE_FEED);
		sb.append("\tbla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla").append(LINE_FEED);
		sb.append("\t\tbla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla").append(LINE_FEED);
		sb.append("\t\t\tbla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla").append(LINE_FEED);
		sb.append("\t\t\t\tbla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla bla").append(LINE_FEED).append(LINE_FEED);
		sb.append("Goodbye.").append(LINE_FEED);
		sb.append("EOM");
		return sb.toString();
	}

	protected String getWorkDirStr() {
		return workDirStr;
	}

	protected String getServerRootStr() {
		return serverRootStr;
	}

	protected String getAgentRootStr() {
		return agentRootStr;
	}

	protected String getDesktopRootStr() {
		return desktopRootStr;
	}

	protected String getMiscDirStr() {
		return miscDirStr;
	}

	protected String getTestKeysFileStr() {
		return testKeysFileNameStr;
	}

}
