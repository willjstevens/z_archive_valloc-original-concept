/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.Identifiable;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public abstract class Message implements Identifiable<UniqueId>
{
	private final UniqueId requestId;

	public Message(final UniqueId requestId) {
		this.requestId = requestId;
	}

	@Override
	public UniqueId id() {
		return requestId;
	}

}
