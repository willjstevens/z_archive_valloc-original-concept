/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.model;

import java.util.Queue;

import com.valloc.model.dto.ModelDto;
import com.valloc.service.Service;

/**
 *
 *
 * @author wstevens
 */
public interface ModelService extends Service
{
	public void publishModelDtos(Queue<ModelDto> modelDtos);
}
