/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.state;

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

final class MockComponentStateMachine extends AbstractStateMachine
{
	private StateFactory stateFactory;
	private final MockComponent mockComponent;
	
	/**
	 * @param mockComponent
	 */
	MockComponentStateMachine(final MockComponent mockComponent)
	{
		this.mockComponent = mockComponent;
		
		// define all state vertices
		final PathOrientedGraph<LifecycleState, LifecycleChange> graph = getLifecycleMappingGraph();
		final PathOrientedVertex<LifecycleState, LifecycleChange> unassigned = graph.addPathOrientedVertexByItem(LifecycleState.UNASSIGNED);
		final PathOrientedVertex<LifecycleState, LifecycleChange> bootstrapped = graph.addPathOrientedVertexByItem(LifecycleState.BOOTSTRAPPED);
		final PathOrientedVertex<LifecycleState, LifecycleChange> initialized = graph.addPathOrientedVertexByItem(LifecycleState.INITIALIZED);
		final PathOrientedVertex<LifecycleState, LifecycleChange> active = graph.addPathOrientedVertexByItem(LifecycleState.ACTIVE);
		final PathOrientedVertex<LifecycleState, LifecycleChange> suspended = graph.addPathOrientedVertexByItem(LifecycleState.SUSPENDED);
		final PathOrientedVertex<LifecycleState, LifecycleChange> shutdown = graph.addPathOrientedVertexByItem(LifecycleState.SHUTDOWN);
		final PathOrientedVertex<LifecycleState, LifecycleChange> destroyed = graph.addPathOrientedVertexByItem(LifecycleState.DESTROYED);
		final PathOrientedVertex<LifecycleState, LifecycleChange> killed = graph.addPathOrientedVertexByItem(LifecycleState.KILLED);
		// make directed connection from one vertex to another, were relevant in thread pool container model
		unassigned.addDirectedEdgeToVertex(bootstrapped, LifecycleChange.BOOTSTRAP);
		bootstrapped.addDirectedEdgeToVertex(initialized, LifecycleChange.INITIALIZE);
		bootstrapped.addDirectedEdgeToVertex(shutdown, LifecycleChange.SHUTDOWN);
		bootstrapped.addDirectedEdgeToVertex(killed, LifecycleChange.KILL);
		initialized.addDirectedEdgeToVertex(active, LifecycleChange.START);
		initialized.addDirectedEdgeToVertex(shutdown, LifecycleChange.SHUTDOWN);
		initialized.addDirectedEdgeToVertex(killed, LifecycleChange.KILL);
		active.addDirectedEdgeToVertex(suspended, LifecycleChange.SUSPEND);
		active.addDirectedEdgeToVertex(shutdown, LifecycleChange.SHUTDOWN);
		active.addDirectedEdgeToVertex(killed, LifecycleChange.KILL);
		suspended.addDirectedEdgeToVertex(active, LifecycleChange.RESUME);
		suspended.addDirectedEdgeToVertex(shutdown, LifecycleChange.SHUTDOWN);
		suspended.addDirectedEdgeToVertex(killed, LifecycleChange.KILL);
		shutdown.addDirectedEdgeToVertex(destroyed, LifecycleChange.DESTROY);
		shutdown.addDirectedEdgeToVertex(initialized, LifecycleChange.INITIALIZE);
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
			stateFactory = new MockComponentStateFactory();
		}
		
		return stateFactory;
	}

	class MockComponentStateFactory extends AbstractStateFactory
	{
		@Override
		public State getUnassignedState()	{ return new UnassignedState(); }
		@Override
		public State getBootstrappedState()	{ return new BootstrapState(); }
		@Override
		public State getInitializedState() 	{ return new InitializedState(); }
		@Override
		public State getActiveState() 		{ return new ActiveState(); }
		@Override
		public State getSuspendedState()	{ return new SuspendState(); }
		@Override
		public State getShutdownState() 	{ return new ShutdownState(); }
		@Override
		public State getDestroyedState() 	{ return new DestroyedState(); }
		@Override
		public State getKilledState() 		{ return new KilledState(); }
	}
	
	private final class UnassignedState extends AbstractState
	{
		public UnassignedState() { super(LifecycleState.UNASSIGNED); }

		@Override
		public void bootstrap()
		{
			final MessageSummary summary = mockComponent.bootstrap();
			getStateMachine().setStateChangeMessageSummary(summary);
		}
	}
	
	private final class BootstrapState extends AbstractState
	{
		public BootstrapState() { super(LifecycleState.BOOTSTRAPPED); }
		
		@Override
		public void initialize()
		{
			final MessageSummary summary = mockComponent.initialize();
			getStateMachine().setStateChangeMessageSummary(summary);
		}

		@Override
		public void shutdown()
		{
			final MessageSummary summary = mockComponent.shutdown();
			getStateMachine().setStateChangeMessageSummary(summary);
		}

		@Override
		public void kill()
		{
			final MessageSummary summary = mockComponent.kill();
			getStateMachine().setStateChangeMessageSummary(summary);
		}
	}
	
	
	private final class InitializedState extends AbstractState
	{
		public InitializedState() { super(LifecycleState.INITIALIZED); }
		
		@Override
		public void start()
		{
			final MessageSummary summary = mockComponent.start();
			getStateMachine().setStateChangeMessageSummary(summary);
		}

		@Override
		public void shutdown()
		{
			final MessageSummary summary = mockComponent.shutdown();
			getStateMachine().setStateChangeMessageSummary(summary);
		}

		@Override
		public void kill()
		{
			final MessageSummary summary = mockComponent.kill();
			getStateMachine().setStateChangeMessageSummary(summary);
		}
	}
	
	private final class ActiveState extends AbstractState
	{
		public ActiveState() { super(LifecycleState.ACTIVE); }
		
		@Override
		public void suspend()
		{
			final MessageSummary summary = mockComponent.suspend();
			getStateMachine().setStateChangeMessageSummary(summary);
		}
		
		@Override
		public void shutdown()
		{
			final MessageSummary summary = mockComponent.shutdown();
			getStateMachine().setStateChangeMessageSummary(summary);
		}

		@Override
		public void kill()
		{
			final MessageSummary summary = mockComponent.kill();
			getStateMachine().setStateChangeMessageSummary(summary);
		}
	}

	private final class SuspendState extends AbstractState
	{
		public SuspendState() { super(LifecycleState.SUSPENDED); }
		
		@Override
		public void resume()
		{
			final MessageSummary summary = mockComponent.resume();
			getStateMachine().setStateChangeMessageSummary(summary);
		}

		@Override
		public void shutdown()
		{
			final MessageSummary summary = mockComponent.shutdown();
			getStateMachine().setStateChangeMessageSummary(summary);
		}

		@Override
		public void kill()
		{
			final MessageSummary summary = mockComponent.kill();
			getStateMachine().setStateChangeMessageSummary(summary);
		}

	}
	
	private final class ShutdownState extends AbstractState
	{
		public ShutdownState() { super(LifecycleState.SHUTDOWN); }
		
		@Override
		public void initialize()
		{
			final MessageSummary summary = mockComponent.initialize();
			getStateMachine().setStateChangeMessageSummary(summary);
		}

		@Override
		public void destroy()
		{
			final MessageSummary summary = mockComponent.destroy();
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