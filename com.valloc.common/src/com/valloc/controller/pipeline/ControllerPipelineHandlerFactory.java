/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.Nameable;

/**
 *
 *
 * @author wstevens
 */
public interface ControllerPipelineHandlerFactory extends Nameable
{
	public ControllerPipelineHandler newControllerPipelineHandler();
}
