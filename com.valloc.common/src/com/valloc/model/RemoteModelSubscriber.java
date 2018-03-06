/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.model;

import com.valloc.model.dto.ModelDto;

/**
 *
 *
 * @author wstevens
 */
public interface RemoteModelSubscriber
{
	public void publishModelDto(ModelDto modelDto);
}
