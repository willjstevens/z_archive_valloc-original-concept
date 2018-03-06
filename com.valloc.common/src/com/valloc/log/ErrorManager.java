/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.log;

import static com.valloc.Constants.*;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;

import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
class ErrorManager extends java.util.logging.ErrorManager
{
	private final StringBuffer buffer = new StringBuffer(250);
	private final Format formatter = new SimpleDateFormat(LogManager.TIMESTAMP_FORMAT);
	private final Date date = new Date();
	private final Handler handlerOwner;
	
	/**
	 * 
	 */
	ErrorManager(final Handler handlerOwner)
	{
		this.handlerOwner = handlerOwner;
	}
	
	/* (non-Javadoc)
	 * @see java.util.logging.ErrorManager#error(java.lang.String, java.lang.Exception, int)
	 */
	@Override
	public synchronized void error(final String msg, final Exception ex, final int code)
	{
		buffer.delete(0, buffer.length());
		date.setTime(Util.now());
		
		buffer.append(formatter.format(date)).append(SPACE);
		buffer.append("Handler Owner of type \"").append(handlerOwner.getClass().getSimpleName()).append("\" ");
		buffer.append(" experienced error on handling log record: ");
		
		switch (code) {
			case CLOSE_FAILURE:		buffer.append("CLOSE_FAILURE"); 	break;
			case FLUSH_FAILURE:		buffer.append("FLUSH_FAILURE"); 	break;
			case FORMAT_FAILURE:	buffer.append("FORMAT_FAILURE"); 	break;
			case GENERIC_FAILURE:	buffer.append("GENERIC_FAILURE");	break;
			case OPEN_FAILURE:		buffer.append("OPEN_FAILURE"); 		break;
			case WRITE_FAILURE:		buffer.append("WRITE_FAILURE"); 				 	 
		}
		buffer.append(SPACE).append(msg).append(DOT);
		
		if (ex != null) {
			buffer.append(LINE_SEPARATOR).append(ex);
		}
		
		final String result = buffer.toString();
		Util.printMsgToStdStreams(result);
	}
}
