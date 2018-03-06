/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import com.valloc.service.Service;


/**
 *
 *
 * @author wstevens
 */
abstract class AbstractClientController extends AbstractController implements ClientController
{
	private Service service;

	@Override
	public void setService(final Service service) {
		this.service = service;
	}
//	public <S extends Service<? extends FrameworkManager, ? extends ControllerComponentFactory>> void setService(S service);
}
