/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.session;

import com.valloc.Constants;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class SessionId extends UniqueId
{
	private final String username;

	SessionId(final String username) {
		super();
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		final int usernameHashcode = username.hashCode();
		final int idHashcode = getUuid().hashCode();

		// final int result = prime * (usernameHashcode + idHashcode + transportId);
		final int result = prime * (usernameHashcode + idHashcode);

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		// check basics first
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof SessionId)) {
			return false;
		}

		// check deeper levels
		final SessionId other = (SessionId) obj;
		// if (transportId != other.transportId) {
		// return false;
		// }
		if (!username.equals(other.username)) {
			return false;
		}
		if (!getUuid().equals(other.getUuid())) {
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(username).append(Constants.DASH);
		// sb.append(getId()).append(Constants.DASH);
		// sb.append(transportId);
		sb.append(getUuid());
		return sb.toString();
	}
}
