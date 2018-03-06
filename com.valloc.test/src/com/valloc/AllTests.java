/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.valloc.concurrent.queue.QueueMessageTest;
import com.valloc.concurrent.request.RequestContainerTest;
import com.valloc.concurrent.spinner.SpinnerTest;
import com.valloc.concurrent.utility.UtilityContainerTest;
import com.valloc.domain.DomainSerializationTest;
import com.valloc.log.LoggerTest;
import com.valloc.security.SecurityTests;
import com.valloc.state.StateMachineTest;
import com.valloc.thread.ThreadManagerTest;
import com.valloc.transport.TransportTest;
import com.valloc.util.DataStructureTest;
import com.valloc.util.RelationshipGroupTest;

/**
 *
 *
 * @author wstevens
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	LoggerTest.class,
	ThreadManagerTest.class,
	SpinnerTest.class,
	QueueMessageTest.class,
	RequestContainerTest.class,
	UtilityContainerTest.class,
	RelationshipGroupTest.class,
	DataStructureTest.class,
	StateMachineTest.class,
	SecurityTests.class,
	TransportTest.class,
	DomainSerializationTest.class
})
public final class AllTests {}