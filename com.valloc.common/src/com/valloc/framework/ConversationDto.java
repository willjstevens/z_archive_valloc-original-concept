/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import java.util.ArrayList;
import java.util.List;

import com.valloc.AttributeChange;
import com.valloc.MessageSummary;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class ConversationDto
{
	private final UniqueId uniqueId;
	private final String nextAction;
	private final List<AttributeChange> attributeChangeList = new ArrayList<AttributeChange>();
	private final MessageSummary messageSummary;
	
	/**
	 * @param uniqueId
	 * @param nextAction
	 * @param messageSummary
	 */
	public ConversationDto(final UniqueId id, final String nextAction, final MessageSummary messageSummary)
	{
		this.uniqueId = id;
		this.nextAction = nextAction;
		this.messageSummary = messageSummary;
	}

	/**
	 * @return the uniqueId
	 */
	public UniqueId getId()
	{
		return uniqueId;
	}

	/**
	 * @return the nextAction
	 */
	public String getNextAction()
	{
		return nextAction;
	}

	/**
	 * @return the attributeChangeList
	 */
	public List<AttributeChange> getAttributeChangeList()
	{
		return attributeChangeList;
	}

	/**
	 * @return the messageSummary
	 */
	public MessageSummary getMessageSummary()
	{
		return messageSummary;
	}

	
}
