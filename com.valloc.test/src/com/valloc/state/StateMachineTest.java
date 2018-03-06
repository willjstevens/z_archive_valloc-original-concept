/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.state;

import junit.framework.Assert;

import org.junit.Test;

import com.valloc.AbstractTest;
import com.valloc.MessageSummary;
import com.valloc.lifecycle.LifecycleChange;
import com.valloc.lifecycle.LifecycleState;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public final class StateMachineTest extends AbstractTest
{
	/**
	 * Test basic functionality and usage of the state machine.
	 */
	@Test
	public void stateMachine_basic()
	{
		final MockComponent mockComponent = new MockComponent();
		final MockComponentStateMachine stateMachine = new MockComponentStateMachine(mockComponent);
		final UniqueId id = new UniqueId();
		final MockStateChangeRequester requester = new MockStateChangeRequester();

		// basic change to go from unassigned to active state
		StateChangeRequest request = new StateChangeRequest(id, LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);
		StateChangeResponse response = requester.getResponse();
		Assert.assertEquals(LifecycleState.ACTIVE, response.getResultingState());
		Assert.assertTrue(response.didStateChange());

		// test trying to change state machine to a state it is already in
		request = new StateChangeRequest(id, LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);
		response = requester.getResponse();
		Assert.assertEquals(LifecycleState.ACTIVE, response.getResultingState());
		Assert.assertFalse(response.didStateChange());

		// tests if a MessageSummary.ERROR is returned from a component's lifecycle method
		mockComponent.specialSummary = MessageSummary.ERROR;
		request = new StateChangeRequest(id, LifecycleChange.SUSPEND, requester);
		stateMachine.requestStateChange(request);
		response = requester.getResponse();
		Assert.assertEquals(LifecycleState.ACTIVE, response.getResultingState());
		Assert.assertFalse(response.didStateChange());
		mockComponent.specialSummary = null;

		// this is a non-intuitive change going (backwards) from active to initialized; by first going through shutdown
		request = new StateChangeRequest(id, LifecycleChange.INITIALIZE, requester);
		stateMachine.requestStateChange(request);
		response = requester.getResponse();
		Assert.assertEquals(LifecycleState.INITIALIZED, response.getResultingState());
		Assert.assertTrue(response.didStateChange());

		// test request that it cannot transition to because there is no link in the graph
		request = new StateChangeRequest(id, LifecycleChange.BOOTSTRAP, requester);
		stateMachine.requestStateChange(request);
		response = requester.getResponse();
		Assert.assertEquals(LifecycleState.INITIALIZED, response.getResultingState());
		Assert.assertFalse(response.didStateChange());

		// test transition to termination is successful
		request = new StateChangeRequest(id, LifecycleChange.DESTROY, requester);
		stateMachine.requestStateChange(request);
		response = requester.getResponse();
		Assert.assertEquals(LifecycleState.TERMINATED, response.getResultingState());
		Assert.assertTrue(response.didStateChange());

		// test resurrecting from termination to active is not successful
		request = new StateChangeRequest(id, LifecycleChange.START, requester);
		stateMachine.requestStateChange(request);
		response = requester.getResponse();
		Assert.assertEquals(LifecycleState.TERMINATED, response.getResultingState());
		Assert.assertFalse(response.didStateChange());
	}
}