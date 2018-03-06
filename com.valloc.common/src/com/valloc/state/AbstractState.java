/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.state;

import static com.valloc.lifecycle.LifecycleState.*;

import com.valloc.MessageSummary;
import com.valloc.lifecycle.LifecycleState;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractState implements State 
{
	private LifecycleState lifecycleState;
	private StateMachine stateMachine;
		
	/**
	 * 
	 */
	public AbstractState()
	{
		this(UNASSIGNED);
	}
	
	/**
	 * @param lifecycleState
	 */
	public AbstractState(final LifecycleState lifecycleState)
	{
		this.lifecycleState = lifecycleState;
	}
	
	/* (non-Javadoc)
	 * @see com.valloc.state.State#setStateMachine(com.valloc.state.StateMachine)
	 */
	@Override
	public void setStateMachine(final StateMachine stateMachine)
	{
		this.stateMachine = stateMachine;
	}

	/**
	 * @return the stateMachine
	 */
	public StateMachine getStateMachine()
	{
		return stateMachine;
	}

	protected void setStateChangeMessageSummary(final MessageSummary stateChangeMessageSummary)
	{
		stateMachine.setStateChangeMessageSummary(stateChangeMessageSummary);
	}
	
	public LifecycleState getLifecycleState()
	{
		return lifecycleState;
	}
	
	/**
	 * @param lifecycleState the lifecycleState to set
	 */
	public void setLifecycleState(final LifecycleState lifecycleState)
	{
		this.lifecycleState = lifecycleState;
	}

	/* (non-Javadoc)
	 * @see com.valloc.lifecycle.Bootstrapable#bootstrap()
	 */
	@Override
	public void bootstrap()
	{
	}
	
	/* (non-Javadoc)
	 * @see com.valloc.lifecycle.Initializable#initialize()
	 */
	@Override
	public void initialize()
	{
	}
	
	/* (non-Javadoc)
	 * @see com.valloc.lifecycle.Startable#start()
	 */
	@Override
	public void start()
	{
	}

	/* (non-Javadoc)
	 * @see com.valloc.lifecycle.Suspendable#suspend()
	 */
	@Override
	public void suspend()
	{
	}
	
	/* (non-Javadoc)
	 * @see com.valloc.lifecycle.Resumable#resume()
	 */
	@Override
	public void resume()
	{
	}
	
	/* (non-Javadoc)
	 * @see com.valloc.lifecycle.Stoppable#stop()
	 */
	@Override
	public void stop()
	{
	}
	
	/* (non-Javadoc)
	 * @see com.valloc.lifecycle.Shutdownable#shutdown()
	 */
	@Override
	public void shutdown()
	{
	}
	
	/* (non-Javadoc)
	 * @see com.valloc.lifecycle.Destroyable#destroy()
	 */
	@Override
	public void destroy()
	{
	}
	
	/* (non-Javadoc)
	 * @see com.valloc.lifecycle.Killable#kill()
	 */
	@Override
	public void kill()
	{
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return lifecycleState.toString();
	}
	
}
