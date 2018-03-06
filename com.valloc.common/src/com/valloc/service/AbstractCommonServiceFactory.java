/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.CommonResources;
import com.valloc.framework.ReferenceKit;


/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractCommonServiceFactory extends AbstractServiceFactory implements CommonServiceFactory
{
	private CommonResources commonResources;

	@Override
	protected void initialize(final Service service, final ReferenceKit referenceKit) {
		super.initialize(service, referenceKit);
		final CommonService commonService = (CommonService) service;
		commonService.setCommonResources(commonResources);
	}

	@Override
	public CommonResources getCommonResources() {
		return commonResources;
	}

	@Override
	public void setCommonResources(final CommonResources commonResources) {
		this.commonResources = commonResources;
	}
}
