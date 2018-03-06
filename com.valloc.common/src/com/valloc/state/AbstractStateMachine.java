/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.state;

import static com.valloc.lifecycle.LifecycleState.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.valloc.CategoryType;
import com.valloc.MessageSummary;
import com.valloc.lifecycle.LifecycleChange;
import com.valloc.lifecycle.LifecycleState;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;
import com.valloc.util.PathOrientedGraph;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractStateMachine implements StateMachine
{
	private static final Logger logger = LogManager.manager().getLogger(AbstractStateMachine.class, CategoryType.STATE_TRANSITION);
	private final PathOrientedGraph<LifecycleState, LifecycleChange> graph = new PathOrientedGraph<LifecycleState, LifecycleChange>();
	private State currentState = getStateFactory().getUnassignedState();
	private MessageSummary stateChangeMessageSummary;
	private final Set<StateChangeListener> changeListeners = new HashSet<StateChangeListener>();

	public AbstractStateMachine() {
		super();
		currentState.setStateMachine(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.state.StateMachine#requestStateChange(com.valloc.state.StateChangeRequest)
	 */
	@Override
	public synchronized void requestStateChange(final StateChangeRequest request) {
		final LifecycleState originalLifecycleState = currentState.getLifecycleState();
		final LifecycleState requestedLifecycleState = request.getRequestedChange().targetState();

		if (requestedLifecycleState != originalLifecycleState) { // protect against current state the same state as requested
			Collection<LifecycleChange> path = Collections.emptyList();
			try {
				path = graph.getShortestPathOfEdgesToDestination(originalLifecycleState, requestedLifecycleState);
			} catch (final IllegalStateException e) {
				logger.warn("Couldn't find path from state %s to %s due to: %s.", originalLifecycleState, requestedLifecycleState, e.getMessage());
			}
			for (final LifecycleChange requestedChange : path) {
				final LifecycleState previousLifecycleState = currentState.getLifecycleState();
				switch (requestedChange) {
				case BOOTSTRAP:
					currentState.setLifecycleState(BOOTSTRAPPING);
					currentState.bootstrap();
					currentState.setLifecycleState(stateChangeMessageSummary.isSuccess() ? BOOTSTRAPPED : previousLifecycleState);
					break;
				case INITIALIZE:
					currentState.setLifecycleState(INITIALIZING);
					currentState.initialize();
					currentState.setLifecycleState(stateChangeMessageSummary.isSuccess() ? INITIALIZED : previousLifecycleState);
					break;
				case START:
					currentState.setLifecycleState(STARTING);
					currentState.start();
					currentState.setLifecycleState(stateChangeMessageSummary.isSuccess() ? ACTIVE : previousLifecycleState);
					break;
				case SUSPEND:
					currentState.setLifecycleState(SUSPENDING);
					currentState.suspend();
					currentState.setLifecycleState(stateChangeMessageSummary.isSuccess() ? SUSPENDED : previousLifecycleState);
					break;
				case RESUME:
					currentState.setLifecycleState(RESUMING);
					currentState.resume();
					currentState.setLifecycleState(stateChangeMessageSummary.isSuccess() ? ACTIVE : previousLifecycleState);
					break;
				case STOP:
					currentState.setLifecycleState(STOPPING);
					currentState.stop();
					currentState.setLifecycleState(stateChangeMessageSummary.isSuccess() ? STOPPED : previousLifecycleState);
					break;
				case SHUTDOWN:
					currentState.setLifecycleState(SHUTTING_DOWN);
					currentState.shutdown();
					currentState.setLifecycleState(stateChangeMessageSummary.isSuccess() ? SHUTDOWN : previousLifecycleState);
					break;
				case DESTROY:
					currentState.setLifecycleState(DESTROYING);
					currentState.destroy();
					currentState.setLifecycleState(stateChangeMessageSummary.isSuccess() ? DESTROYED : previousLifecycleState);
					break;
				case KILL:
					currentState.setLifecycleState(KILLING);
					currentState.kill();
					currentState.setLifecycleState(stateChangeMessageSummary.isSuccess() ? KILLED : previousLifecycleState);
				}

				// now prepare state for next iteration or exit
				final LifecycleState currentLifecycleState = currentState.getLifecycleState();
				if (currentLifecycleState != previousLifecycleState) { // reassign new implementation for next state
					currentState = getStateFactory().getStateObject(currentLifecycleState, this);
				} else { // for some reason the change was never made; probably due to error
					if (logger.isFine()) {
						final String msg = "Requested state change from %s to %s did not fully occur and ceased transition at %s.";
						logger.fine(msg, originalLifecycleState, requestedLifecycleState, currentState.getLifecycleState());
					}
					break; // error probably occurred somewhere in change method
				}
			}
		}

		// capture states here and report for logging
		LifecycleState currentLifecycleState = currentState.getLifecycleState();
		final boolean stateDidChange = currentLifecycleState != originalLifecycleState;
		if (currentLifecycleState == requestedLifecycleState && stateDidChange) {
			if (logger.isFine()) {
				logger.fine("Successfully transitioned state from %s to %s.", originalLifecycleState, requestedLifecycleState);
			}
		} else if (!stateDidChange) {
			if (logger.isFine()) {
				logger.fine("Request to state %s did not change.", requestedLifecycleState);
			}
		}

		// resolve target termination states to official termination
		if (currentLifecycleState == DESTROYED || currentLifecycleState == KILLED) {
			if (logger.isFine()) {
				final String msg = "Requested state sucessfully changed to %s; now moving state component to %s.";
				logger.fine(msg, currentState, TERMINATED);
			}
			currentLifecycleState = TERMINATED;
			currentState.setLifecycleState(currentLifecycleState);
		}

		// build response and notify requester
		final UniqueId id = request.getId();
		final MessageSummary changeSummary = currentLifecycleState == requestedLifecycleState ? MessageSummary.SUCCESS : stateChangeMessageSummary;
		final StateChangeResponse response = new StateChangeResponse(id, currentLifecycleState, changeSummary, stateDidChange);
		request.getRequester().requestCompleted(response);
		if (logger.isFine()) {
			logger.fine("State change requester notified of ending result on state change request.");
		}

		// finally notify listeners, of total change (not intermediate changes)
		if (stateDidChange) {
			for (final StateChangeListener listener : changeListeners) {
				listener.stateChanged(currentLifecycleState);
				if (logger.isFine()) {
					logger.fine("State change listener %s notified of ending result on state change request.", listener);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.state.StateMachine#getActiveState()
	 */
	@Override
	public LifecycleState getActiveState() {
		return currentState.getLifecycleState();
	}

	/**
	 * @param stateChangeMessageSummary
	 *            the stateChangeMessageSummary to set
	 */
	@Override
	public void setStateChangeMessageSummary(final MessageSummary stateChangeMessageSummary) {
		this.stateChangeMessageSummary = stateChangeMessageSummary;
	}

	/**
	 * @return the graph
	 */
	protected PathOrientedGraph<LifecycleState, LifecycleChange> getLifecycleMappingGraph() {
		return graph;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.state.StateMachine#addStateChangeListener(com.valloc.state.StateChangeListener)
	 */
	@Override
	public void addStateChangeListener(final StateChangeListener listener) {
		changeListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.state.StateMachine#removeStateChangeListener(com.valloc.state.StateChangeListener)
	 */
	@Override
	public void removeStateChangeListener(final StateChangeListener listener) {
		changeListeners.remove(listener);
	}
}
