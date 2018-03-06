/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import com.valloc.controller.pipeline.ControllerPipelineProcessor;
import com.valloc.interrupt.InterruptType;
import com.valloc.util.Result;


/**
 *
 *
 * @author wstevens
 */
class SynchronousClientControllerBase extends AbstractSingleClientController implements SynchronousClientController
{


	@Override
	public void submitSync() {
		final ControllerPipelineProcessor pipelineProcessor = getControllerPipelineProcessor();
		// first execute sending out and receiving request
		pipelineProcessor.execute();
		// now immediately close off attached resources
		pipelineProcessor.destroy();
	}



	@Override
	public void handleInterrupt(final InterruptType interruptType, final Result result) {
	}

}
