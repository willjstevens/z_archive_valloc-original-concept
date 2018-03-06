/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.core;

import com.valloc.ServerConstants;
import com.valloc.framework.NodeType;

/**
 *
 *
 * @author wstevens
 */
public class ServerNode implements Node
{
	private PlatformEnvironment platformEnvironment;
	
	/*
	 * (non-Javadoc)
	 * @see com.valloc.core.Node#getParticipantType()
	 */
	@Override
	public NodeType getNodeType() {
		return ServerConstants.SERVER_NODE_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * @see com.valloc.core.Node#getVallocXmlFileInterpreter()
	 */
	@Override
	public Class<? extends BaseVallocConfigXmlFileInterpreter<? extends BaseVallocXmlConfiguration>> getVallocXmlFileInterpreter() {
		return ServerVallocConfigXmlFileInterpreter.class;
	}

	@Override
	public PlatformEnvironment getEnvironment() {
		return platformEnvironment;
	}

	@Override
	public void setEnvironment(final PlatformEnvironment platformEnvironment) {
		this.platformEnvironment = platformEnvironment;
	}
	
	
}
