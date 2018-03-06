/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

/**
 *
 *
 * @author wstevens
 */
public class DirectoryNotFoundException extends IllegalStateException
{	private static final long serialVersionUID = -7469708161109300394L;

	/**
	 * 
	 */
	public DirectoryNotFoundException() {
	}

	/**
	 * @param s
	 */
	public DirectoryNotFoundException(final String s) {
		super(s);
	}

	/**
	 * @param cause
	 */
	public DirectoryNotFoundException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DirectoryNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
