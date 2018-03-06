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
public class Agent extends ClientNode
{
	private boolean isLicensed;

	public Agent(final String name, final HostConfig hostConfig) {
		super(name, NodeType.AGENT, hostConfig);
	}

	public Agent(final String name, final HostConfig hostConfig, final boolean isLicensed) {
		this(name, hostConfig);
		this.isLicensed = isLicensed;
	}

	public boolean isLicensed() {
		return isLicensed;
	}

	public void setLicensed(final boolean isLicensed) {
		this.isLicensed = isLicensed;
	}
}
