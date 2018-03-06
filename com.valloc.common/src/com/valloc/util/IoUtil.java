/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import static com.valloc.Constants.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.valloc.file.FileConstants;

/**
 * Utility methods for basic IO-related read and write operations. All methods throw any IO related <code>Exception</code>'s so client may handle
 * appropriately.
 * 
 * @author wstevens
 */
@Deprecated
public final class IoUtil
{
	/*
	 * The maximum acceptable file size for files being read.
	 */
	private final static int MAX_ACCEPTABLE_FILE_SIZE = 1000000;

	private IoUtil() {
	}

	public static URI buildFileUri(final String rootPath, final String fileName) {
		final int builderLength = rootPath.length() + fileName.length() + 5;
		final String formalPath = convertDoubleSlashPathDelimeters(padEndingFileSeparator(rootPath));
		final StringBuilder sb = new StringBuilder(builderLength);
		sb.append(FILE_SCHEME);
		sb.append(formalPath);
		sb.append(fileName);
		return URI.create(sb.toString());
	}

	public static String padEndingFileSeparator(final String originalFilePath) {
		final StringBuilder retval = new StringBuilder(originalFilePath);
		final char lastChar = originalFilePath.charAt(originalFilePath.length() - 1);
		if (lastChar != FORWARD_SLASH && lastChar != FileConstants.FILE_SEPARATOR) {
			// TODO: should this be converted to sys-dependent file-sep, or always forward slash?
			retval.append(FORWARD_SLASH);
		}
		return retval.toString();
	}

	public static String convertDoubleSlashPathDelimeters(final String originalFilePath) {
		return originalFilePath.replace("\\", Character.toString(FORWARD_SLASH));
	}

	public static String stripScheme(final String uri, final String scheme) {
		String retval = uri;
		if (uri.contains(scheme)) {
			retval = uri.substring(scheme.length());
		}
		return retval;
	}

	public static File convertUrlToFile(final URL subject) {
		File retval = null;

		final StringBuilder builder = new StringBuilder();
		final String authority = subject.getAuthority();
		if (authority != null) {
			builder.append(authority);
		}
		builder.append(subject.getFile());
		final String fileStr = builder.toString();
		retval = new File(fileStr);
		if (!retval.exists()) {
			final String msg = "Cannot convert URL to file since it does not exist: " + subject.toExternalForm();
			throw new IllegalArgumentException(msg);
		}

		return retval;
	}

	public static String convertUrlToFileString(final URL subject) {
		return convertUrlToFile(subject).getAbsolutePath();
	}

	public static void createDirectory(final File directory) throws IOException {
		try {
			if (!directory.exists()) {
				directory.mkdir();
			}
		} catch (final SecurityException e) {
			String msg = "Failed when attempting to create the following directory, as it failed a checkWrite security operation: ";
			msg += directory.getAbsolutePath();
			throw new IOException(msg);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static String readFileIntoString(final File absoluteFilePath) throws FileNotFoundException, IOException {
		return new String(readFileIntoBytes(absoluteFilePath), DEFAULT_CHARSET);
	}

	/**
	 * Reads a file given a fully qualified location to the file.
	 * 
	 * @param absoluteFilePath
	 *            Includes the full path and the file name.
	 * @return byte[] The raw bytes from the corresponding file.
	 * @throws FileNotFoundException
	 *             If file is not found.
	 * @throws IOException
	 *             Any other general or unanticipated IO problem occurs.
	 */
	public static byte[] readFileIntoBytes(final File absoluteFilePath) throws FileNotFoundException, IOException {
		FileChannel rbc = new FileInputStream(absoluteFilePath).getChannel();
		final long originalSize = rbc.size();
		rbc = rbc.truncate(MAX_ACCEPTABLE_FILE_SIZE);
		final long newSize = rbc.size();
		if (originalSize != newSize) {
			final String msg = "File " + absoluteFilePath.getAbsolutePath() + " is too big and has been truncated.";
			throw new IllegalStateException(msg);
		}
		final byte[] retval = new byte[((int) newSize)]; // safe to downcast to int because of truncate
		int totalRead = 0, amountRead = 0;
		final ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		while ((amountRead = rbc.read(readBuffer)) > -1) {
			readBuffer.flip();
			readBuffer.get(retval, totalRead, amountRead);
			totalRead += amountRead;
		}
		rbc.close();
		return retval;
	}

	public static void writeStringToFile(final File absoluteFilePath, final String contents) throws FileNotFoundException, IOException {
		writeBytesToFile(absoluteFilePath, contents.getBytes(DEFAULT_CHARSET));
	}

	/**
	 * Writes a file given a fully qualified location to the file and its contents.
	 * 
	 * @param absoluteFilePath
	 *            Includes the full path and the file name.
	 * @return byte[] The raw bytes to be written to the corresponding file.
	 * @throws FileNotFoundException
	 *             If file is not found.
	 * @throws IOException
	 *             Any other general or unanticipated IO problem occurs.
	 */
	public static void writeBytesToFile(final File absoluteFilePath, final byte[] contents) throws FileNotFoundException, IOException {
		final FileChannel rbc = new FileOutputStream(absoluteFilePath).getChannel();
		final ByteBuffer writeBuffer = ByteBuffer.wrap(contents);
		int totalWritten = 0, amountWritten = 0;
		final int contentLength = contents.length;
		while ((amountWritten = rbc.write(writeBuffer)) > -1) {
			totalWritten += amountWritten;
			if (totalWritten == contentLength) {
				break;
			}
		}
		rbc.close();
	}
}