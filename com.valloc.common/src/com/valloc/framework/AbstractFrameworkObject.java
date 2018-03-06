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
abstract class AbstractFrameworkObject implements Identifiable<UniqueId>
{
	private Message message;

	@Override
	public UniqueId id() {
		return message.id();
	}

	void setSource(final Message message) {
		this.message = message;
	}
}
