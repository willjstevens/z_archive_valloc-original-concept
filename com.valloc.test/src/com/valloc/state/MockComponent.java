/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.state;

import com.valloc.MessageSummary;

/**
 *
 *
 * @author wstevens
 */
class MockComponent
{
	MessageSummary specialSummary;
	
	void setStateMachine(final MockComponentStateMachine stateMachine)
	{
	}
	
	private MessageSummary getMessageSummary()
	{
		return specialSummary != null ? specialSummary : MessageSummary.SUCCESS;
	}
	
	MessageSummary bootstrap() 
	{
		return getMessageSummary();
	}
	
	MessageSummary initialize() 
	{
		return getMessageSummary();
	}
	
	MessageSummary start() 
	{
		return getMessageSummary();
	}
	
	MessageSummary suspend() 
	{
		
		return getMessageSummary();
	}
	
	MessageSummary resume() 
	{
		return getMessageSummary();
	}
	
	MessageSummary shutdown() 
	{
		return getMessageSummary();
	}
	
	MessageSummary destroy() 
	{
		return getMessageSummary();
	}
	
	MessageSummary kill() 
	{
		return getMessageSummary();
	}
	
}
