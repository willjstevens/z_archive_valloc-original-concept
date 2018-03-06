/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import java.util.Date;
import java.util.UUID;

/**
 *
 *
 * @author wstevens
 */
public class UniqueId
{
	private final UUID uuid;
	private final Date inceptionTimestamp;
	
	public UniqueId() {
		this(UUID.randomUUID());
	}

	public UniqueId(final UUID uuid) {
		this.uuid = uuid;
		inceptionTimestamp = Util.nowTimestamp();
	}

	public Date getInceptionTimestamp() {
		return inceptionTimestamp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof UniqueId)) {
			return false;
		}
		final UniqueId other = (UniqueId) obj;
		if (uuid == null) {
			if (other.uuid != null) {
				return false;
			}
		} else if (!uuid.equals(other.uuid)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return uuid.toString();
	}

	public UUID getUuid() {
		return uuid;
	}
}
