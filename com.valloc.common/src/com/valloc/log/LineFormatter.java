/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.log;

import static com.valloc.Constants.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
class LineFormatter extends Formatter
{
	private final StringBuffer buffer = new StringBuffer(250);
	private final Format dateFormatter = new SimpleDateFormat(LogManager.TIMESTAMP_FORMAT);
	private final Date date = new Date();
	private final String encoding; 
	
	LineFormatter(final String encoding)
	{
		this.encoding = encoding;
	}

	/* (non-Javadoc)
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(final LogRecord record)
	{
		date.setTime(record.getMillis());				
		buffer.delete(0, buffer.length());
		
		buffer.append(dateFormatter.format(date)).append(SPACE);
		buffer.append(record.getLevel()).append(SPACE);
		buffer.append(record.getLoggerName()).append(COLON);
		buffer.append(SPACE).append(SPACE);
		buffer.append(record.getMessage());
		
		final Throwable thrown = record.getThrown();
		if (thrown != null) {
			buffer.append(LINE_SEPARATOR);
			try {
				final StringWriter sw = new StringWriter();
				final PrintWriter pw = new PrintWriter(sw);
				thrown.printStackTrace(pw);
				buffer.append(sw);
				pw.close();
				sw.close();
			} catch (final Exception e) {
				Util.printThrowableToStdStreams(e);
			}
		} else {
			buffer.append(LINE_SEPARATOR);
		}
		
		String retval = null;
		try {
			retval = new String(buffer.toString().getBytes(), encoding);
		} catch (final UnsupportedEncodingException e) {
			Util.printThrowableToStdStreams(e);
			retval = buffer.toString(); // fallback
		}
		return retval;
	}
}
