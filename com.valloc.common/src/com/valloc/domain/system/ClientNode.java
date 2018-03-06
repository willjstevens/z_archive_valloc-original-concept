/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain.system;

import com.valloc.framework.NodeType;

/**
 *
 *
 * @author wstevens
 */
public abstract class ClientNode extends Participant
{
	private final HostConfig hostConfig;

	public ClientNode(final String name, final NodeType nodeType, final HostConfig hostConfig) {
		super(name, nodeType);
		this.hostConfig = hostConfig;
	}

	/**
	 * @return the hostConfig
	 */
	public final HostConfig getHostConfig() {
		return hostConfig;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hostConfig == null) ? 0 : hostConfig.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ClientNode other = (ClientNode) obj;
		if (hostConfig == null) {
			if (other.hostConfig != null) {
				return false;
			}
		} else if (!hostConfig.equals(other.hostConfig)) {
			return false;
		}
		return true;
	}


}
