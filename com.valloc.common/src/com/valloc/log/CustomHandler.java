/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.log;

import java.util.logging.Handler;

import com.valloc.util.Factory;

/**
 *
 *
 * @author wstevens
 */
interface CustomHandler<T extends Handler & Factory<T>>
{
	void setLogConfiguration(LogConfiguration logConfiguration);
	Handler toHandler();
}
