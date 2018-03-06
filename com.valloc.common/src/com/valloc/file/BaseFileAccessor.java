/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

import com.valloc.ApplicationException;
import com.valloc.CategoryType;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;

/**
 *
 *
 * @author wstevens
 */
public class BaseFileAccessor implements FileAccessor
{
	private static final Logger logger = LogManager.manager().getLogger(BaseFileAccessor.class, CategoryType.FILE);

	/* (non-Javadoc)
	 * @see com.valloc.file.FileAccessor#readFileIntoBytes(java.io.File)
	 */
	@Override
	public byte[] readFileIntoBytes(final File file) throws IOException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.valloc.file.FileAccessor#readFileIntoDetachedInputStream(java.io.File)
	 */
	@Override
	public InputStream readFileIntoDetachedInputStream(final File file) throws IOException {
		return null;
	}

	@Override
	public byte[] readUrlStreamIntoDetachedBytes(final URL url) throws IOException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.valloc.file.FileAccessor#readUrlIntoDetachedInputStream(java.net.URL)
	 */
	@Override
	public InputStream readUrlIntoDetachedInputStream(final URL url) throws IOException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.valloc.file.FileAccessor#readFileIntoString(java.io.File)
	 */
	@Override
	public String readFileIntoString(final File file) throws IOException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.valloc.file.FileAccessor#readFileIntoString(java.io.File, java.nio.charset.Charset)
	 */
	@Override
	public String readFileIntoString(final File file, final Charset charset) throws IOException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.valloc.file.FileAccessor#writeBytesToFile(java.io.File, byte[])
	 */
	@Override
	public void writeBytesToFile(final File file, final byte[] contents) throws IOException {
	}

	/* (non-Javadoc)
	 * @see com.valloc.file.FileAccessor#writeStringToFile(java.io.File, java.lang.String)
	 */
	@Override
	public void writeStringToFile(final File file, final String contents) throws IOException {
	}

	/* (non-Javadoc)
	 * @see com.valloc.file.FileAccessor#writeStringToFile(java.io.File, java.lang.String, java.nio.charset.Charset)
	 */
	@Override
	public void writeStringToFile(final File file, final String contents, final Charset charset) throws IOException {
	}

	/* (non-Javadoc)
	 * @see com.valloc.file.FileAccessor#urlToFile(java.net.URL)
	 */
	@Override
	public File urlToFile(final URL directoryOrFileUrl) {
		File directoryOrFile = null;

		try {
			final URI fileUri = directoryOrFileUrl.toURI();
			if (logger.isFine()) {
				logger.fine("Directory or file URI \"%s\" has a scheme of \"%s\".", directoryOrFileUrl, fileUri.getScheme());
			}
			directoryOrFile = new File(fileUri);
		} catch (final URISyntaxException e) {
			final String msg = String.format("Problem converting URL object \"%s\" into File object.", directoryOrFileUrl.toExternalForm());
			logger.error(msg, e);
			throw new ApplicationException(msg, e);
		}

		return directoryOrFile;
	}

	/*
	 * (non-Javadoc)
	 * @see com.valloc.file.FileAccessor#isFileType(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isFileType(final String fileName, final String extention) {
		return fileName.endsWith(extention);
	}
}
