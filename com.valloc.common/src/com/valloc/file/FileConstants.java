/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

import static com.valloc.Constants.*;

import java.io.File;


/**
 *
 *
 * @author wstevens
 */
public final class FileConstants
{

	public static final char PATH_SEPARATOR		= File.pathSeparatorChar;
	public static final char FILE_SEPARATOR 	= File.separatorChar;

	public static final char EXTENSION_SEPERATOR		= DOT;
	public static final String EXTENSION_SEPERATOR_STR	= Character.toString(EXTENSION_SEPERATOR);
	public static final String EXTENSION_XML			= EXTENSION_SEPERATOR + "xml";
	public static final String EXTENSION_TEXT			= EXTENSION_SEPERATOR + "txt";
	public static final String EXTENSION_KEYSTORE		= EXTENSION_SEPERATOR + "ks";
	public static final String EXTENSION_LOG			= EXTENSION_SEPERATOR + "log";
	public static final String EXTENSION_TEMP			= EXTENSION_SEPERATOR + "tmp";
	public static final String EXTENSION_BACKUP			= EXTENSION_SEPERATOR + "bak";
	public static final String EXTENSION_PROPERTIES		= EXTENSION_SEPERATOR + "properties";
	public static final String EXTENSION_BMP			= EXTENSION_SEPERATOR + "bmp";

	public static final String KEY_STORE_FILE_NAME	= VALLOC + EXTENSION_KEYSTORE;
	public static final String CONFIG_FILE_NAME		= VALLOC + EXTENSION_XML;
	public static final String LOG_FILE_NAME		= VALLOC + EXTENSION_LOG;

	private final static String ROOT_PREFIX 		= VALLOC.toLowerCase() + DASH;
	public final static String ROOT_DIR_DESKTOP		= ROOT_PREFIX + "desktop";
	public final static String ROOT_DIR_SERVER		= ROOT_PREFIX + "server";
	public final static String ROOT_DIR_AGENT		= ROOT_PREFIX + "agent";

	final static String FILE_INTERPRETER_PREFIX 					= "file-interpreter" + DOT;
	public final static String FILE_INTERPRETER_BASIC 				= FILE_INTERPRETER_PREFIX + "basic";
	public final static String FILE_INTERPRETER_RESOURCE_BUNDLE		= FILE_INTERPRETER_PREFIX + "resource-bundle";
}
