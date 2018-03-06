/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import com.valloc.controller.pipeline.ControllerPipelineProcessor;
import com.valloc.interrupt.InterruptHandler;
import com.valloc.transaction.TransactionParticipator;

/**
 *
 *
 * @author wstevens
 */
public abstract interface Controller extends InterruptHandler, TransactionParticipator
{
	ControllerPipelineProcessor getControllerPipelineProcessor();
	void setControllerPipelineProcessor(ControllerPipelineProcessor pipelineProcessor);

}