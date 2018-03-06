/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.log;


/**
 *
 *
 * @author wstevens
 */
public enum CustomHandlerType
{
	VALLOC_COM (VallocComHandler.class);
//  EXPORTABLE ??
	
	
	private Class<? extends CustomHandler<?>> handler;
	
	private CustomHandlerType(final Class<? extends CustomHandler<?>> handler) 
	{
		this.handler = handler;
	}
	
	Class<? extends CustomHandler<?>> getHandlerClass()
	{
		return handler;
	}
}
