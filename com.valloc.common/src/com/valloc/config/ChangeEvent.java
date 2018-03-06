/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.config;

import com.valloc.CategoryType;
import com.valloc.MessageSummary;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public final class ChangeEvent<T extends Configuration>
{
	private final UniqueId uniqueId;
	private final CategoryType categoryType;
	private final T configuration;
	private final MessageSummary messageSummary;

	/**
	 * @param uniqueId
	 * @param configuration
	 * @param messageSummary
	 */
	public ChangeEvent(final UniqueId id, final CategoryType categoryType, final T configuration, final MessageSummary messageSummary)
	{
		this.uniqueId = id;
		this.categoryType = categoryType;
		this.configuration = configuration;
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
	 * @return the categoryType
	 */
	public CategoryType getCategoryType()
	{
		return categoryType;
	}

	/**
	 * @return the configuration
	 */
	public T getConfiguration()
	{
		return configuration;
	}

	/**
	 * @return the messageSummary
	 */
	public MessageSummary getMessageSummary()
	{
		return messageSummary;
	}
}
