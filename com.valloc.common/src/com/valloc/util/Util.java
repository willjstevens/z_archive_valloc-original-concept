/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import static com.valloc.Constants.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.valloc.CategoryType;
import com.valloc.Identifiable;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;

/**
 * Basic utils class with miscellaneous methods.
 *
 * @author wstevens
 */
public final class Util
{
	private static final Logger logger = LogManager.manager().getLogger(Util.class, CategoryType.UTILITY);
	private static final String HEX_DIGITS = "0123456789abcdef";

	private Util() {}

	/**
	 * Returns the system's current time as a <code>long</code>.
	 *
	 * @return long The time in milliseconds since the epoch.
	 * @see System#currentTimeMillis()
	 */
	public static long now() {
		return System.currentTimeMillis();
	}

	/**
	 * Returns the system's current time as a <code>long</code>.
	 *
	 * @return long The time in milliseconds since the epoch.
	 * @see System#currentTimeMillis()
	 */
	public static Date nowTimestamp() {
		return new Date(now());
	}

	/**
	 * Converts milliseconds to seconds.
	 *
	 * @param int The time in milliseconds.
	 * @return long The seconds.
	 */
	public static int inSeconds(final long millis) {
		return (int) millis / 1000;
	}

	public static String toHex(final byte[] data, final int length) {
		final StringBuilder retval = new StringBuilder();

		/*
		 * Example: Convert 0x2b to String represantation:
		 *
		 * 0x2b --> 0010 1011 --> = v 0x2 --> (v >> 4) --> 0010 = array index postion 2 = "2" 0xb --> & 0xf = 0xb = array index position 11 = "b" =
		 * "2b"
		 */
		for (int i = 0; i < length; i++) {
			final int v = data[i] & 0xff;
			retval.append(HEX_DIGITS.charAt(v >> 4));
			retval.append(HEX_DIGITS.charAt(v & 0xf));
		}

		return retval.toString();
	}

	public static String toHex(final byte[] data) {
		return toHex(data, data.length);
	}

	/**
	 * Convert the passed in String to a byte array by taking the bottom 8 bits of each character it contains.
	 *
	 * @param string
	 *            the string to be converted
	 * @return a byte array representation
	 */
	public static byte[] toByteArray(final String string) {
		final byte[] bytes = new byte[string.length()];
		final char[] chars = string.toCharArray();

		for (int i = 0; i != chars.length; i++) {
			bytes[i] = (byte) chars[i];
		}

		return bytes;
	}

	/**
	 * Convert a byte array of 8 bit characters into a String.
	 *
	 * @param bytes
	 *            the array containing the characters
	 * @param length
	 *            the number of bytes to process
	 * @return a String representation of bytes
	 */
	public static String toString(final byte[] bytes) {
		final char[] chars = new char[bytes.length];

		for (int i = 0; i != chars.length; i++) {
			chars[i] = (char) (bytes[i] & 0xff);
		}

		return new String(chars);
	}

	public static String tidyString(final String rawData) {
		String retval = rawData; // could be null
		if (retval != null) {
			retval = retval.trim();
		}
		return retval;
	}

	public static byte[] toByteArray(final char[] chars) {
		final int charSize = chars.length;
		final byte[] retval = new byte[charSize << 1];

		for (int i = 0; i < charSize; i++) {
			final char c = chars[i];
			int idx = i << 1;
			retval[idx] = (byte) (c >> 8 & 0xFF);
			retval[++idx] = (byte) (c & 0xFF);
		}

		return retval;
	}

	public static <T> T wrappedClassNewInstance(final Class<T> clazz) {
		T retval = null;
		try {
			retval = clazz.newInstance();
		} catch (final InstantiationException e) {
			final String msg = String.format("Could not instantiate a new instance for type %s; verify no-args public constructor.", clazz.getSimpleName());
			logger.error(msg, e);
		} catch (final IllegalAccessException e) {
			final String msg = String.format("No class/method visibility access to target no-args public constructor for type %s.", clazz.getSimpleName());
			logger.error(msg, e);
		}
		return retval;
	}

	public static void quietSleep(final long millis) {
		try {
			Thread.sleep(millis);
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static void quietSecondsSleep(final int seconds) {
		try {
			Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static <I> I toId(final Identifiable<I> identifiable) {
		I id = null;
		if (identifiable != null) {
			id = identifiable.id();
		}
		return id;
	}
	
	public static void chompLastChar(final StringBuilder builder) {
		final int len = builder.length();
		builder.delete(len - 1, len);
	}

	public static String createParameterizedStringEntry(final Map<String, Object> params) {
		final StringBuilder builder = new StringBuilder();

		builder.append('{');
		for (final String key : params.keySet()) {
			final Object val = params.get(key);
			addKeyValPair(builder, key, val);
		}
		chompLastChar(builder);
		builder.append('}');

		return builder.toString();
	}

	public static Map<String, Object> newParameterizedStringEntryMap() {
		return new HashMap<String, Object>();
	}

	public static void addKeyValPair(final StringBuilder builder, final Object key, final Object val) {
		final String valueString = val != null ? val.toString() : null;
		builder.append(key).append(EQUALS);
		if (valueString != null) {
			builder.append(QUOTE).append(valueString).append(QUOTE).append(COMMA);
		} else {
			builder.append(valueString).append(COMMA);
		}
	}

	public static void printMsgToStdOut(final String message) {
		System.out.println(message);
	}

	public static void printMsgToStdErr(final String message) {
		System.err.println(message);
	}

	public static void printMsgToStdStreams(final String message) {
		printMsgToStdOut(message);
		printMsgToStdErr(message);
	}

	public static void printThrowableToStdStreams(final Throwable throwable) {
		// print to both stnd err/out; probably no log due to failure
		printMsgToStdErr(throwable.toString());
		throwable.printStackTrace();
		System.out.println(throwable.toString());
		throwable.printStackTrace(System.out);
	}

	public static String buildString(final Object... toStringObjects) {
		final StringBuilder builder = new StringBuilder();

		for (final Object toStringObject : toStringObjects) {
			builder.append(toStringObject);
		}

		return builder.toString();
	}
}
