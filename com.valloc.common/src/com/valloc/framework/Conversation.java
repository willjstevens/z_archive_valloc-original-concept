/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import java.util.ArrayList;
import java.util.List;

import com.valloc.Identifiable;
import com.valloc.util.Attributes;

/**
 *
 *
 * @author wstevens
 */
public class Conversation implements Identifiable<ConversationId>
{
	private final ConversationId conversationId;
	private String currentAction;
	private final List<String> previousActions = new ArrayList<String>();
	private Attributes attributes = new Attributes();

	/**
	 *
	 */
	public Conversation(final ConversationId id) {
		this.conversationId = id;
	}

	@Override
	public ConversationId id() {
		return conversationId;
	}

	/**
	 * @return the currentAction
	 */
	public String getCurrentAction() {
		return currentAction;
	}

	/**
	 * @param currentAction
	 *            the currentAction to set
	 */
	public void setCurrentAction(final String currentAction) {
		previousActions.add(this.currentAction);
		this.currentAction = currentAction;
	}

	/**
	 * @return the attributes
	 */
	public Attributes getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(final Attributes attributes) {
		this.attributes = attributes;
	}
}
