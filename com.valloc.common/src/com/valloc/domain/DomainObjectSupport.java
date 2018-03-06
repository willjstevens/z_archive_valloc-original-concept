/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain;

/**
 *
 *
 * @author wstevens
 */
public interface DomainObjectSupport<T>
{
	public Class<T> getType();
	public T toDto();
	
	void registerStringAliases(StringSerializer serializer);
}
