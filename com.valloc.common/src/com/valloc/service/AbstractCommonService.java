/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.CommonResources;
import com.valloc.controller.ControllerComponentFactory;
import com.valloc.framework.FrameworkManager;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractCommonService extends AbstractService implements CommonService
{
	private CommonResources commonResources;

	@Override
	public CommonResources getCommonResources() {
		return commonResources;
	}

	@Override
	public void setCommonResources(final CommonResources commonResources) {
		this.commonResources = commonResources;
	}

	protected ControllerComponentFactory controllerFactory() {
		return commonResources.getControllerComponentFactory();
	}

	protected FrameworkManager frameworkManager() {
		return commonResources.getFrameworkComponentFactory().getFrameworkManager();
	}
}
