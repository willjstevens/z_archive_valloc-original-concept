/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author wstevens
 */
public class ConversationManager
{
	private final Map<ConversationId, Conversation> processingConversations = new HashMap<ConversationId, Conversation>();
	
	public Conversation establishNewConversation()
	{
		final ConversationId id = null; // TODO: implement me
		final Conversation conversation = new Conversation(id);
		
		processingConversations.put(id, conversation);
		
		return conversation;
	}
	
	public Conversation getConversation(final ConversationId id)
	{
		return processingConversations.get(id);
	}
	
	public void terminateConversation(final Conversation conversation)
	{
		processingConversations.remove(conversation.id());
	}
	
}
