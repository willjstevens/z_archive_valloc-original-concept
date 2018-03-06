/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transport;

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

public class TransportServerStateMachine extends AbstractStateMachine
{
	private StateFactory stateFactory = new TransportServerStateFactory();
	private ServerTransportComponentFactory factory;

	/**
	 * @param transportServer
	 */
	TransportServerStateMachine() {
		// addStateChangeListener(transportServer); // implied and necessary

		// define all state vertices
		final PathOrientedGraph<LifecycleState, LifecycleChange> graph = getLifecycleMappingGraph();
		final PathOrientedVertex<LifecycleState, LifecycleChange> unassigned = graph.addPathOrientedVertexByItem(LifecycleState.UNASSIGNED);
		final PathOrientedVertex<LifecycleState, LifecycleChange> active = graph.addPathOrientedVertexByItem(LifecycleState.ACTIVE);
		final PathOrientedVertex<LifecycleState, LifecycleChange> destroyed = graph.addPathOrientedVertexByItem(LifecycleState.DESTROYED);
		// make directed connection from one vertex to another, were relevant in thread pool container model
		unassigned.addDirectedEdgeToVertex(active, LifecycleChange.START);
		active.addDirectedEdgeToVertex(destroyed, LifecycleChange.DESTROY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.state.AbstractStateMachine#requestStateChange(com.valloc.state.StateChangeRequest)
	 */
	@Override
	public void requestStateChange(final StateChangeRequest request) {
		super.requestStateChange(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.state.StateMachine#getStateFactory()
	 */
	@Override
	public StateFactory getStateFactory() {
		if (stateFactory == null) {
			stateFactory = new TransportServerStateFactory();
		}

		return stateFactory;
	}

	private class TransportServerStateFactory extends AbstractStateFactory
	{
		@Override
		public State getUnassignedState() {
			return new UnassignedState();
		}

		@Override
		public State getActiveState() {
			return new ActiveState();
		}

		@Override
		public State getDestroyedState() {
			return new DestroyedState();
		}
	}

	private final class UnassignedState extends AbstractState
	{
		public UnassignedState() {
			super(LifecycleState.UNASSIGNED);
		}

		@Override
		public void start() {
			final TransportServer transportServer = factory.getTransportServer();
			final MessageSummary summary = transportServer.start();
			getStateMachine().setStateChangeMessageSummary(summary);
		}
	}

	private final class ActiveState extends AbstractState
	{
		public ActiveState() {
			super(LifecycleState.ACTIVE);
		}

		@Override
		public void destroy() {
			final TransportServer transportServer = factory.getTransportServer();
			final MessageSummary summary = transportServer.destroy();
			getStateMachine().setStateChangeMessageSummary(summary);
		}
	}

	private final class DestroyedState extends AbstractState
	{
		public DestroyedState() {
			super(LifecycleState.DESTROYED);
		}
	}

	public void setServerTransportComponentFactory(final ServerTransportComponentFactory factory) {
		this.factory = factory;
	}
}