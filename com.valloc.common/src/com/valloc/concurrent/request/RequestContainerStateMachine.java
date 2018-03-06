/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;

import com.valloc.MessageSummary;
import com.valloc.lifecycle.LifecycleChange;
import com.valloc.lifecycle.LifecycleState;
import com.valloc.state.AbstractState;
import com.valloc.state.AbstractStateFactory;
import com.valloc.state.AbstractStateMachine;
import com.valloc.state.State;
import com.valloc.state.StateChangeRequest;
import com.valloc.state.StateFactory;
import com.valloc.util.PathOrientedGraph;
import com.valloc.util.PathOrientedVertex;

public class RequestContainerStateMachine extends AbstractStateMachine
{
	private StateFactory stateFactory = new ThreadContainerStateFactory();
	private final RequestContainer requestContainer;
	
	/**
	 * @param requestContainer
	 */
	RequestContainerStateMachine(final RequestContainer requestContainer)
	{
		this.requestContainer = requestContainer;
		addStateChangeListener(requestContainer); // implied and necessary
		
		// define all state vertices
		final PathOrientedGraph<LifecycleState, LifecycleChange> graph = getLifecycleMappingGraph();
		final PathOrientedVertex<LifecycleState, LifecycleChange> unassigned = graph.addPathOrientedVertexByItem(LifecycleState.UNASSIGNED);
		final PathOrientedVertex<LifecycleState, LifecycleChange> active = graph.addPathOrientedVertexByItem(LifecycleState.ACTIVE);
		final PathOrientedVertex<LifecycleState, LifecycleChange> destroyed = graph.addPathOrientedVertexByItem(LifecycleState.DESTROYED);
		final PathOrientedVertex<LifecycleState, LifecycleChange> killed = graph.addPathOrientedVertexByItem(LifecycleState.KILLED);
		// make directed connection from one vertex to another, were relevant in thread pool container model
		unassigned.addDirectedEdgeToVertex(active, LifecycleChange.START);
		active.addDirectedEdgeToVertex(destroyed, LifecycleChange.DESTROY);
		active.addDirectedEdgeToVertex(killed, LifecycleChange.KILL);
	}

	/* (non-Javadoc)
	 * @see com.valloc.state.AbstractStateMachine#requestStateChange(com.valloc.state.StateChangeRequest)
	 */
	@Override
	public void requestStateChange(final StateChangeRequest request)
	{
		super.requestStateChange(request);
	}

	/* (non-Javadoc)
	 * @see com.valloc.state.StateMachine#getStateFactory()
	 */
	@Override
	public StateFactory getStateFactory()
	{
		if (stateFactory == null) {
			stateFactory = new ThreadContainerStateFactory();
		}
		
		return stateFactory;
	}

	private class ThreadContainerStateFactory extends AbstractStateFactory
	{
		@Override
		public State getUnassignedState()	{ return new UnassignedState(); }
		@Override
		public State getActiveState() 		{ return new ActiveState(); }
		@Override
		public State getDestroyedState() 	{ return new DestroyedState(); }
		@Override
		public State getKilledState() 		{ return new KilledState(); }
	}

	private final class UnassignedState extends AbstractState
	{
		public UnassignedState() { super(LifecycleState.UNASSIGNED); }

		@Override
		public void start()
		{
			final MessageSummary summary = requestContainer.start();
			getStateMachine().setStateChangeMessageSummary(summary);
		}
	}
	
	private final class ActiveState extends AbstractState
	{
		public ActiveState() { super(LifecycleState.ACTIVE); }
		
		@Override
		public void destroy()
		{
			final MessageSummary summary = requestContainer.destroy();
			getStateMachine().setStateChangeMessageSummary(summary);
		}

		@Override
		public void kill()
		{
			final MessageSummary summary = requestContainer.kill();
			getStateMachine().setStateChangeMessageSummary(summary);
		}
	}

	private final class DestroyedState extends AbstractState
	{
		public DestroyedState() { super(LifecycleState.DESTROYED); }
	}

	private final class KilledState extends AbstractState
	{
		public KilledState() { super(LifecycleState.KILLED); }
	}
}