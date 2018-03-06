/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

/**
 * 
 * 
 * @author wstevens
 */
interface NettyClientConnector extends NettyConnector
{
	void setPipelineFactory(NettyPipelineFactory pipelineFactory);
}
