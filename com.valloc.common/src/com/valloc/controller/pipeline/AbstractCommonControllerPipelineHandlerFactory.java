/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.CommonResources;
import com.valloc.CommonResourcesParticipator;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractCommonControllerPipelineHandlerFactory implements ControllerPipelineHandlerFactory, CommonResourcesParticipator
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
}
