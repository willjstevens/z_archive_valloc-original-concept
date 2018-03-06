/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.spinner;

import com.valloc.ApplicationException;

/**
 *
 *
 * @author wstevens
 */
public interface Spinner
{
	public void spinAndWait() throws ApplicationException;
	public void stop();
}