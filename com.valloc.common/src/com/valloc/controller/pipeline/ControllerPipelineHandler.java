/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller.pipeline;

import com.valloc.Nameable;
import com.valloc.interrupt.InterruptParticipator;
import com.valloc.lifecycle.Destroyable;
import com.valloc.lifecycle.Initializable;
import com.valloc.session.SessionComponentFactory;

/**
 *
 *
 * @author wstevens
 */
public interface ControllerPipelineHandler extends Initializable, Destroyable, Nameable, InterruptParticipator
{
	public void execute(PipelineState pipelineState);
	public void handleException(Exception e);

	public SessionComponentFactory getSessionComponentFactory();
	void setSessionComponentFactory(SessionComponentFactory sessionComponentFactory);
}
