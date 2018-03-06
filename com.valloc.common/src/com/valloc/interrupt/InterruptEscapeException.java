/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.interrupt;

/**
 *
 *
 * @author wstevens
 */
public final class InterruptEscapeException extends RuntimeException
{	private static final long serialVersionUID = -8539601706912832371L;

	public InterruptEscapeException() {}

	public InterruptEscapeException(final String message) {
		super(message);
	}

	public InterruptEscapeException(final Throwable cause) {
		super(cause);
	}

	public InterruptEscapeException(final String message, final Throwable cause) {
		super(message, cause);
	}
}