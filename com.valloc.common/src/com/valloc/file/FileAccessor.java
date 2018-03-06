/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

/**
 *
 *
 * @author wstevens
 */
public interface FileAccessor
{
	public byte[] readFileIntoBytes(File file) throws IOException;
	public String readFileIntoString(File file) throws IOException;
	public String readFileIntoString(File file, Charset charset) throws IOException;
	public InputStream readFileIntoDetachedInputStream(File file) throws IOException;

	public void writeBytesToFile(File file, byte[] contents) throws IOException;
	public void writeStringToFile(File file, String contents) throws IOException;
	public void writeStringToFile(File file, String contents, Charset charset) throws IOException;

	public boolean isFileType(String fileName, String extention);

	/* New Generation of read/write? */
	public byte[] readUrlStreamIntoDetachedBytes(URL url) throws IOException;
	public InputStream readUrlIntoDetachedInputStream(URL url) throws IOException;
	public File urlToFile(URL directoryOrFileUrl);

}
