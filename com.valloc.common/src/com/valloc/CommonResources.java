/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc;

import com.valloc.controller.ControllerComponentFactory;
import com.valloc.framework.FrameworkComponentFactory;
import com.valloc.service.ServiceComponentFactory;
import com.valloc.session.SessionComponentFactory;

/**
 *
 *
 *
 * @author wstevens
 */
public interface CommonResources
{
	public FrameworkComponentFactory getFrameworkComponentFactory();
	public SessionComponentFactory getSessionComponentFactory();
	public ControllerComponentFactory getControllerComponentFactory();
	public ServiceComponentFactory getServiceComponentFactory();
}
