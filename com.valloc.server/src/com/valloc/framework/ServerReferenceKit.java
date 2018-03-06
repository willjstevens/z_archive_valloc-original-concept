/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.controller.ControllerComponentFactory;
import com.valloc.controller.ServerControllerComponentFactory;


/**
 *
 *
 * @author wstevens
 */
public class ServerReferenceKit extends ReferenceKit
{
	public ServerControllerComponentFactory controllerComponentFactory;
	public ServerFrameworkComponentFactory frameworkComponentFactory;

	@Override
	ControllerComponentFactory getControllerComponentFactory() {
		return controllerComponentFactory;
	}

	@Override
	FrameworkComponentFactory getFrameworkComponentFactory() {
		return frameworkComponentFactory;
	}
}
