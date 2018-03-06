/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc;

import java.nio.charset.Charset;
import java.util.Locale;


/**
 * General miscellaneous constants, shared between the different application modules.
 *
 * @author wstevens
 */
public class Constants
{

	/**
	 * Static constant <code>char</code> for <b>.</b>.
	 */
	public final static char DOT 					= '.';

	/**
	 * Unique key defining the application where necessary.
	 */
	public final static String VALLOC 						= "Valloc";
	public final static String VALLOC_DOMAIN 				= "com" + DOT + VALLOC.toLowerCase();

	final static String BUNDLE_ID_BASE						= VALLOC_DOMAIN + DOT;
	public static final String BUNDLE_ID_COMMON				= BUNDLE_ID_BASE + "common";

	final static String FRAG_NL_SUFFIX						= DOT + "nl" + DOT;
	// these are language locales (less specific), not country locales (more specific)
	final static String FRAG_NL_SUFFIX_ENGLISH				= FRAG_NL_SUFFIX + Locale.ENGLISH.getLanguage();
	final static String FRAG_NL_SUFFIX_GERMAN				= FRAG_NL_SUFFIX + Locale.GERMAN.getLanguage();
	final static String FRAG_NL_SUFFIX_FRENCH				= FRAG_NL_SUFFIX + Locale.FRENCH.getLanguage();

	public static final String BUNDLE_ID_COMMON_NL_ENGLISH 	= BUNDLE_ID_COMMON + FRAG_NL_SUFFIX_ENGLISH;
	public static final String BUNDLE_ID_COMMON_NL_GERMAN 	= BUNDLE_ID_COMMON + FRAG_NL_SUFFIX_GERMAN;
	public static final String BUNDLE_ID_COMMON_NL_FRENCH 	= BUNDLE_ID_COMMON + FRAG_NL_SUFFIX_FRENCH;

	/**
	 * Constant used to suppress unchecked warnings, usually via an annotation at the method level.
	 */
	public final static String UNCHECKED			= "unchecked";
	public final static String RAWTYPES				= "rawtypes";

	/**
	 * Static constant <code>char</code> for <b>/</b>.
	 */
	public final static char FORWARD_SLASH			= '/';
	public final static String FORWARD_SLASH_STR		= Character.toString('/');

	/**
	 * Represents the platform's line termination character.
	 */
	public static final String LINE_SEPARATOR 		= System.getProperty("line.separator");

	/**
	 * The user's home directory. This is mostly used as the parent directory of the
	 * <code>com.valloc.Constants.PRODUCT_DIR</code> variable.
	 *
	 */
	public static final String USER_HOME			= System.getProperty("user.home");

//	public static final String HOME_DIRECTORY		= System.getProperty(OSGI_INSTALL_AREA_KEY);

	public final static String EMPTY_STRING			= "";

	public final static String DELIMITED_QUOTE		= "\"";

	public final static char TAB 					= '\t';

	/**
	 * Static constant <code>char</code> for <b>,</b>.
	 */
	public final static char COMMA 					= ',';

	/**
	 * Static constant <code>char</code> for <b>&</b>.
	 */
	public final static char AMPERSAND 				= '&';

	/**
	 * Static constant <code>char</code> for <b>=</b>.
	 */
	public final static char EQUALS 				= '=';

	/**
	 * Static constant <code>char</code> for <b>:</b>.
	 */
	public final static char COLON					= ':';

	/**
	 * Static constant <code>char</code> for <b>"</b>.
	 */
	public final static char QUOTE					= '"';

	/**
	 * Static constant <code>char</code> for a space.
	 */
	public final static char SPACE					= ' ';

	public final static char UNDERSCORE				= '_';

	public final static char DASH					= '-';
	public final static char ASTERISK				= '*';
	public static final String ASTERISK_STR			= String.valueOf(ASTERISK);

	/**
	 * A file URI scheme prefix; example: <i>file:/</i>.
	 *
	 * @see java.net.URI
	 */
	public static final String FILE_SCHEME	= "file" + COLON + FORWARD_SLASH;

	/**
	 * HTTP URL prefix.
	 */
	public static final String HTTP_SCHEME	= "http" + COLON + FORWARD_SLASH + FORWARD_SLASH;

	/**
	 * Secured HTTP(S) prefix.
	 */
	public static final String HTTPS_SCHEME	= "https" + COLON + FORWARD_SLASH + FORWARD_SLASH;

	/**
	 * Minimal accetable port number which is acceptable.
	 * Default set to a non-privledged ports.
	 */
	public static final int MINIMAL_ACCEPTABLE_PORT		= 1025;

	/**
	 * Maximum port value allowed.
	 */
	public static final int MAXIMUM_ALLOWABLE_PORT		= 65536;


	public static final String UTF_8				= "UTF-8";
	public static final String UTF_16				= "UTF-16";
	public static final String UTF_16BE				= UTF_16 + "BE";
	public static final Charset UTF_8_CHARSET		= Charset.forName(UTF_8);
	public static final Charset UTF_16_CHARSET		= Charset.forName(UTF_16);
	public static final Charset UTF_16BE_CHARSET	= Charset.forName(UTF_16BE);

	public static final String DEFAULT_ENCODING		= UTF_16;
	public static final Charset DEFAULT_CHARSET		= Charset.forName(DEFAULT_ENCODING);


	public static final String XML_VERSION_1_1			= "1.1";
	public static final String DEFAULT_XML_VERSION 		= XML_VERSION_1_1;


	public static final String OSGI_CROSS_PLATFORM_PATH_SEPARATOR	= FORWARD_SLASH_STR;
	public static final String BUNDLE_ENV_ROOT						= OSGI_CROSS_PLATFORM_PATH_SEPARATOR;
	// For uses like at: http://www.osgi.org/javadoc/r4v42/org/osgi/framework/Bundle.html#findEntries(java.lang.String,%20java.lang.String,%20boolean)

}