/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;

import java.util.Date;

import com.valloc.Prioritizable;

/**
 *
 *
 * @author wstevens
 */
public interface PrioritizableRequestElement extends Prioritizable
{
	public Date getTimestamp();
	public void setTimestamp(Date timestamp);
	
	long getContainerId();
}
