/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.state;

import com.valloc.lifecycle.Bootstrapable;
import com.valloc.lifecycle.Destroyable;
import com.valloc.lifecycle.Initializable;
import com.valloc.lifecycle.Killable;
import com.valloc.lifecycle.LifecycleState;
import com.valloc.lifecycle.Resumable;
import com.valloc.lifecycle.Shutdownable;
import com.valloc.lifecycle.Startable;
import com.valloc.lifecycle.Stoppable;
import com.valloc.lifecycle.Suspendable;

/**
 *
 *
 * @author wstevens
 */
public interface State extends Bootstrapable, Initializable, Startable, Suspendable, Resumable, Stoppable, Shutdownable, Destroyable, Killable
{
	public LifecycleState getLifecycleState();
	public void setStateMachine(StateMachine stateMachine);
	public void setLifecycleState(LifecycleState lifecycleState);
}
