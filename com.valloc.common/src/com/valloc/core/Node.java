/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.core;

import com.valloc.framework.NodeType;

/**
 *
 *
 * @author wstevens
 */
public interface Node
{
	public NodeType getNodeType();

	public Class<? extends BaseVallocConfigXmlFileInterpreter<? extends BaseVallocXmlConfiguration>> getVallocXmlFileInterpreter();
	
	public PlatformEnvironment getEnvironment();
	void setEnvironment(PlatformEnvironment platformEnvironment);
	
}
