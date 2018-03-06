/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.log;

import java.util.logging.Level;

/**
 * Defines various log levels assocated for the logger.
 *
 * The standard for using different log levels is as follows:
 * <ul>
 *  <li>error: Indicates something severe has occurred and application task cannot continue normally.</li>
 *  <li>warning: Indicates something unanticpated has occurred, but application can recover.</li>
 *  <li>info: Basic information such as beginning a task and successful/error completion of a task.</li>
 *  <li>fine: Detail statements indicating basic troubleshooting information and info dumps on operations.</li>
 *  <li>finer: Essentially tracing statements, mostly in/throughout complex operations.</li>
 * 	<li>finest: Detail of displaying entire data contents, no matter how big. This might come in the form of a
 * 		file's/object's entire contents.</li>
 * </ul>
 *
 * @author wstevens
 */
public enum LogLevel
{
	OFF		(Level.OFF, 		Level.OFF.getName()),
	ERROR 	(Level.SEVERE, 		"ERROR"),
	WARN	(Level.WARNING,		Level.WARNING.getName()),
	INFO	(Level.INFO, 		Level.INFO.getName()),
	FINE	(Level.FINE, 		Level.FINE.getName()),
	FINER	(Level.FINER, 		Level.FINER.getName()),
	FINEST	(Level.FINEST, 		Level.FINEST.getName()),
	ALL		(Level.ALL, 		Level.ALL.getName());

	private Level level;
	private String label;
	
	private LogLevel(final Level level, final String label) {
		this.level = level;
		this.label = label;
	}
	
	Level level() { 
		return level;
	}
	
	int intLevel() { 
		return level.intValue();
	}
	
	String label() {
		return label;
	}
	
	public static LogLevel toLogLevel(final String logLevelStr)
	{
		LogLevel retval = ALL; // if unparseable, report as ALL for troubleshooting
		
		if (logLevelStr.equals(OFF)) {
			retval = OFF;
		} else if (logLevelStr.equals(ERROR)) {
			retval = ERROR;
		} else if (logLevelStr.equals(WARN)) {
			retval = WARN;
		} else if (logLevelStr.equals(INFO)) {
			retval = INFO;
		} else if (logLevelStr.equals(FINE)) {
			retval = FINE;
		} else if (logLevelStr.equals(FINER)) {
			retval = FINER;
		} else if (logLevelStr.equals(FINEST)) {
			retval = FINEST;
		} else if (logLevelStr.equals(ALL)) {
			retval = ALL;
		}
		
		return retval;
	}
}
