/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Assert;
import org.junit.Test;

import com.valloc.AbstractTest;
import com.valloc.concurrent.spinner.ConditionTriggerSpinner;
import com.valloc.concurrent.spinner.SpinHandler;
import com.valloc.concurrent.spinner.Spinner;
import com.valloc.concurrent.spinner.TriggerSpinner;
import com.valloc.lifecycle.LifecycleChange;
import com.valloc.lifecycle.LifecycleState;

/**
 * 
 * 
 * @author wstevens
 */
public final class DataStructureTest extends AbstractTest
{

	/**
	 * Test basic functionality and usage of the directed graph.
	 */
	@Test
	public void graph_basic() {
		// common for all tests
		final Graph<String> graph = new Graph<String>();
		final Vertex<String> vertex_zero = graph.addVertexByItem("vertex_zero");
		final Vertex<String> vertex_one_one = graph.addVertexByItem("vertex_one_one");
		final Vertex<String> vertex_one_two = graph.addVertexByItem("vertex_one_two");
		final Vertex<String> vertex_one_three = graph.addVertexByItem("vertex_one_three");
		final Vertex<String> vertex_two_one = graph.addVertexByItem("vertex_two_one");
		final Vertex<String> vertex_two_two = graph.addVertexByItem("vertex_two_two");
		final Vertex<String> vertex_three = graph.addVertexByItem("vertex_three");

		// tests just tests a simple path from vertex_zero down to vertex_three
		vertex_zero.addDirectedEdgeToVertex(vertex_one_one);
		vertex_zero.addDirectedEdgeToVertex(vertex_one_two);
		vertex_zero.addDirectedEdgeToVertex(vertex_one_three);
		vertex_one_two.addDirectedEdgeToVertex(vertex_two_one);
		vertex_one_two.addDirectedEdgeToVertex(vertex_two_two);
		vertex_two_one.addDirectedEdgeToVertex(vertex_three);
		vertex_two_two.addDirectedEdgeToVertex(vertex_one_three);
		Collection<String> path = graph.getShortestPathToDestination("vertex_zero", "vertex_three");
		Assert.assertEquals(3, path.size());
		Iterator<String> iter = path.iterator();
		Assert.assertEquals("vertex_one_two", iter.next());
		Assert.assertEquals("vertex_two_one", iter.next());
		Assert.assertEquals("vertex_three", iter.next());

		try { // tests no destination or nonexistent element
			path = graph.getShortestPathToDestination("vertex_zero", "nonexistent");
			Assert.fail("Graph found nonexistent item.");
		} catch (final Exception expected) {
		}

		// test simple path
		path = graph.getShortestPathToDestination("vertex_zero", "vertex_one_one");
		Assert.assertEquals(1, path.size());

		// test path starting from non-standard spot
		path = graph.getShortestPathToDestination("vertex_one_two", "vertex_one_three");
		Assert.assertEquals(2, path.size());
		iter = path.iterator();
		Assert.assertEquals("vertex_two_two", iter.next());
		Assert.assertEquals("vertex_one_three", iter.next());

		// test path further down graph
		path = graph.getShortestPathToDestination("vertex_two_one", "vertex_three");
		Assert.assertEquals(1, path.size());
		iter = path.iterator();
		Assert.assertEquals("vertex_three", iter.next());

		// test path for different first tier vertex
		path = graph.getShortestPathToDestination("vertex_zero", "vertex_one_three");
		Assert.assertEquals(1, path.size());
		iter = path.iterator();
		Assert.assertEquals("vertex_one_three", iter.next());

		// add a direct edge from tier 1 vertex to target in tier 3 vertex, testing a shorter path
		vertex_one_three.addDirectedEdgeToVertex(vertex_three);
		path = graph.getShortestPathToDestination("vertex_zero", "vertex_three");
		Assert.assertEquals(2, path.size());
		iter = path.iterator();
		Assert.assertEquals("vertex_one_three", iter.next());
		Assert.assertEquals("vertex_three", iter.next());

		// going further add another direct edge from base tier 0 to tier 3, testing a path with 1 edge
		vertex_zero.addDirectedEdgeToVertex(vertex_three);
		path = graph.getShortestPathToDestination("vertex_zero", "vertex_three");
		Assert.assertEquals(1, path.size());
		iter = path.iterator();
		Assert.assertEquals("vertex_three", iter.next());

		// remove/reset 2 previous new edges
		vertex_zero.removeDirectedEdgeFromVertex(vertex_three);
		vertex_one_three.removeDirectedEdgeFromVertex(vertex_three);

		// retest state has been correctly stored as originally done
		path = graph.getShortestPathToDestination("vertex_zero", "vertex_three");
		Assert.assertEquals(3, path.size());
		iter = path.iterator();
		Assert.assertEquals("vertex_one_two", iter.next());
		Assert.assertEquals("vertex_two_one", iter.next());
		Assert.assertEquals("vertex_three", iter.next());
	}

	/**
	 * Test basic functionality and usage of the directed graph, with a path oriented characteristic. This demonstrates that the edge in the graph can
	 * have an associated object attachment of its own type. Here I use LifecycleState objects to represent the vertices or state, which the
	 * LifecycleChange object represents the edge.
	 */
	@Test
	public void graph_pathOriented() {
		// common for all tests
		final PathOrientedGraph<LifecycleState, LifecycleChange> graph = new PathOrientedGraph<LifecycleState, LifecycleChange>();
		// define all vertices
		final PathOrientedVertex<LifecycleState, LifecycleChange> bootstrapped = graph.addPathOrientedVertexByItem(LifecycleState.BOOTSTRAPPED);
		final PathOrientedVertex<LifecycleState, LifecycleChange> initialized = graph.addPathOrientedVertexByItem(LifecycleState.INITIALIZED);
		final PathOrientedVertex<LifecycleState, LifecycleChange> active = graph.addPathOrientedVertexByItem(LifecycleState.ACTIVE);
		final PathOrientedVertex<LifecycleState, LifecycleChange> suspended = graph.addPathOrientedVertexByItem(LifecycleState.SUSPENDED);
		final PathOrientedVertex<LifecycleState, LifecycleChange> shutdown = graph.addPathOrientedVertexByItem(LifecycleState.SHUTDOWN);
		final PathOrientedVertex<LifecycleState, LifecycleChange> destroyed = graph.addPathOrientedVertexByItem(LifecycleState.DESTROYED);
		final PathOrientedVertex<LifecycleState, LifecycleChange> killed = graph.addPathOrientedVertexByItem(LifecycleState.KILLED);
		// Make directed connection from one vertex to another, were relevant in model.
		// Note these mappings are dictated from the request container state diagram as of February 2010
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
		// no directed edges for destroyed and killed

		// simple two hop state change, with edges
		Collection<LifecycleState> path = graph.getShortestPathToDestination(LifecycleState.BOOTSTRAPPED, LifecycleState.ACTIVE);
		Assert.assertEquals(2, path.size());
		Iterator<LifecycleState> iter = path.iterator();
		Assert.assertEquals(LifecycleState.INITIALIZED, iter.next());
		Assert.assertEquals(LifecycleState.ACTIVE, iter.next());
		Collection<LifecycleChange> edgePath = graph.getShortestPathOfEdgesToDestination(LifecycleState.BOOTSTRAPPED, LifecycleState.ACTIVE);
		Assert.assertEquals(2, path.size());
		Iterator<LifecycleChange> changeIter = edgePath.iterator();
		Assert.assertEquals(LifecycleChange.INITIALIZE, changeIter.next());
		Assert.assertEquals(LifecycleChange.START, changeIter.next());

		// simple one hop state change
		path = graph.getShortestPathToDestination(LifecycleState.BOOTSTRAPPED, LifecycleState.INITIALIZED);
		Assert.assertEquals(1, path.size());
		iter = path.iterator();
		Assert.assertEquals(LifecycleState.INITIALIZED, iter.next());
		edgePath = graph.getShortestPathOfEdgesToDestination(LifecycleState.BOOTSTRAPPED, LifecycleState.INITIALIZED);
		Assert.assertEquals(1, path.size());
		changeIter = edgePath.iterator();
		Assert.assertEquals(LifecycleChange.INITIALIZE, changeIter.next());

		// two hop change between states with edges going each direction
		path = graph.getShortestPathToDestination(LifecycleState.INITIALIZED, LifecycleState.SUSPENDED);
		Assert.assertEquals(2, path.size());
		iter = path.iterator();
		Assert.assertEquals(LifecycleState.ACTIVE, iter.next());
		Assert.assertEquals(LifecycleState.SUSPENDED, iter.next());
		edgePath = graph.getShortestPathOfEdgesToDestination(LifecycleState.INITIALIZED, LifecycleState.SUSPENDED);
		Assert.assertEquals(2, path.size());
		changeIter = edgePath.iterator();
		Assert.assertEquals(LifecycleChange.START, changeIter.next());
		Assert.assertEquals(LifecycleChange.SUSPEND, changeIter.next());

		// simple one hop state change going into a state with multiple directed edges pointing to it
		path = graph.getShortestPathToDestination(LifecycleState.ACTIVE, LifecycleState.SHUTDOWN);
		Assert.assertEquals(1, path.size());
		iter = path.iterator();
		Assert.assertEquals(LifecycleState.SHUTDOWN, iter.next());
		edgePath = graph.getShortestPathOfEdgesToDestination(LifecycleState.ACTIVE, LifecycleState.SHUTDOWN);
		Assert.assertEquals(1, path.size());
		changeIter = edgePath.iterator();
		Assert.assertEquals(LifecycleChange.SHUTDOWN, changeIter.next());

		// more complex state change testing graph traversal, from bootstrapped into suspended
		path = graph.getShortestPathToDestination(LifecycleState.BOOTSTRAPPED, LifecycleState.SUSPENDED);
		Assert.assertEquals(3, path.size());
		iter = path.iterator();
		Assert.assertEquals(LifecycleState.INITIALIZED, iter.next());
		Assert.assertEquals(LifecycleState.ACTIVE, iter.next());
		Assert.assertEquals(LifecycleState.SUSPENDED, iter.next());
		edgePath = graph.getShortestPathOfEdgesToDestination(LifecycleState.BOOTSTRAPPED, LifecycleState.SUSPENDED);
		Assert.assertEquals(3, path.size());
		changeIter = edgePath.iterator();
		Assert.assertEquals(LifecycleChange.INITIALIZE, changeIter.next());
		Assert.assertEquals(LifecycleChange.START, changeIter.next());
		Assert.assertEquals(LifecycleChange.SUSPEND, changeIter.next());

		// more complex state change testing graph traversal, from bootstrapped into destroyed, but bypassing active, etc.
		path = graph.getShortestPathToDestination(LifecycleState.BOOTSTRAPPED, LifecycleState.DESTROYED);
		Assert.assertEquals(2, path.size());
		iter = path.iterator();
		Assert.assertEquals(LifecycleState.SHUTDOWN, iter.next());
		Assert.assertEquals(LifecycleState.DESTROYED, iter.next());
		edgePath = graph.getShortestPathOfEdgesToDestination(LifecycleState.BOOTSTRAPPED, LifecycleState.DESTROYED);
		Assert.assertEquals(2, path.size());
		changeIter = edgePath.iterator();
		Assert.assertEquals(LifecycleChange.SHUTDOWN, changeIter.next());
		Assert.assertEquals(LifecycleChange.DESTROY, changeIter.next());
	}

	/**
	 * A basic test on the dependency item and group constructs. 
	 */
	@Test
	public void dependencyGroup_basic() {
		// define items
		final DependencyItem<String, String> resourceInit = new DependencyItem<String, String>("controller.resource-initializer", "Resource Initializer");
		final DependencyItem<String, String> conversation = new DependencyItem<String, String>("controller.conversation", "Conversation");
		final DependencyItem<String, String> transaction = new DependencyItem<String, String>("controller.transaction", "Transaction");
		final DependencyItem<String, String> someHandler = new DependencyItem<String, String>("controller.handler", "Sample Handler");
		final DependencyItem<String, String> timingControl = new DependencyItem<String, String>("controller.timing", "Timing Controller");
		// define group and add items to group
		final DependencyGroup<String, String> group = new DependencyGroup<String, String>();
		group.addItemToTemplate(resourceInit);
		group.addItemToTemplate(conversation);
		group.addItemToTemplate(transaction);
		group.addItemToTemplate(someHandler);
		group.addItemToTemplate(timingControl);
		// associate items to one another
		timingControl.addDependency(resourceInit);
		conversation.addDependency(resourceInit);
		conversation.addDependency(someHandler);
		transaction.addDependency(resourceInit);
		transaction.addDependency(conversation);
		someHandler.addDependency(resourceInit);
		// add in single prereqs
		transaction.setPrerequisite(conversation);

		final LinkedList<DependencyItem<String, String>> chain = new LinkedList<DependencyItem<String,String>>();
		// test a simple optional handler in the middle
		chain.add(resourceInit);
		chain.add(timingControl);		
		group.insertAccordingToTemplate(someHandler, chain);		
		List<String> idChain = group.toIdentifierList(chain);
		Assert.assertEquals(idChain.get(0), "controller.resource-initializer");
		Assert.assertEquals(idChain.get(1), "controller.handler");
		Assert.assertEquals(idChain.get(2), "controller.timing");
		chain.clear();
		// test handler and coversation items in middle with varying dependencies
		chain.add(resourceInit);
		chain.add(timingControl);		
		group.insertAccordingToTemplate(someHandler, chain);		
		group.insertAccordingToTemplate(conversation, chain);
		idChain = group.toIdentifierList(chain);
		Assert.assertEquals(idChain.get(0), "controller.resource-initializer");
		Assert.assertEquals(idChain.get(1), "controller.handler");
		Assert.assertEquals(idChain.get(2), "controller.conversation");
		Assert.assertEquals(idChain.get(3), "controller.timing");
		chain.clear();
		// do the same again but flip insertion order to show dependencies are maintained
		chain.add(resourceInit);
		chain.add(timingControl);		
		group.insertAccordingToTemplate(conversation, chain);
		group.insertAccordingToTemplate(someHandler, chain);		
		idChain = group.toIdentifierList(chain);
		Assert.assertEquals(idChain.get(0), "controller.resource-initializer");
		Assert.assertEquals(idChain.get(1), "controller.handler");
		Assert.assertEquals(idChain.get(2), "controller.conversation");
		Assert.assertEquals(idChain.get(3), "controller.timing");
		chain.clear();
		// now the same but remove dependency of conversation on someHandler
		conversation.removeDependency(someHandler);
		chain.add(resourceInit);
		chain.add(timingControl);		
		// this gets inserted first, so initially it's closer to resourceInit
		group.insertAccordingToTemplate(conversation, chain);
		// but handler gets inserted last so it bumps itself up closer to shared dependency: resourceInit
		group.insertAccordingToTemplate(someHandler, chain); 	 	
		idChain = group.toIdentifierList(chain);
		Assert.assertEquals(idChain.get(0), "controller.resource-initializer");
		Assert.assertEquals(idChain.get(1), "controller.handler");
		Assert.assertEquals(idChain.get(2), "controller.conversation");
		Assert.assertEquals(idChain.get(3), "controller.timing");
		chain.clear();
		// now same as above but flip-flop to show on-add order matters here		
		chain.add(resourceInit);
		chain.add(timingControl);		
		group.insertAccordingToTemplate(someHandler, chain); 	 	
		group.insertAccordingToTemplate(conversation, chain);
		idChain = group.toIdentifierList(chain);
		Assert.assertEquals(idChain.get(0), "controller.resource-initializer");
		Assert.assertEquals(idChain.get(1), "controller.conversation");
		Assert.assertEquals(idChain.get(2), "controller.handler");
		Assert.assertEquals(idChain.get(3), "controller.timing");
		chain.clear();
		// finally restore dependency for further tests
		conversation.addDependency(someHandler);
		// test a transaction add first (prereq of conversation) then a misc handler add next
		chain.add(resourceInit);
		chain.add(timingControl);		
		group.insertAccordingToTemplate(transaction, chain);
		group.insertAccordingToTemplate(someHandler, chain);		
		idChain = group.toIdentifierList(chain);
		Assert.assertEquals(idChain.get(0), "controller.resource-initializer");
		Assert.assertEquals(idChain.get(1), "controller.handler");
		Assert.assertEquals(idChain.get(2), "controller.conversation");
		Assert.assertEquals(idChain.get(3), "controller.transaction");
		Assert.assertEquals(idChain.get(4), "controller.timing");
		chain.clear();
		// test a misc handler first and a transaction next (prereq of conversation); order should be same as above
		chain.add(resourceInit);
		chain.add(timingControl);		
		group.insertAccordingToTemplate(someHandler, chain);		
		group.insertAccordingToTemplate(transaction, chain);
		idChain = group.toIdentifierList(chain);
		Assert.assertEquals(idChain.get(0), "controller.resource-initializer");
		Assert.assertEquals(idChain.get(1), "controller.handler");
		Assert.assertEquals(idChain.get(2), "controller.conversation");
		Assert.assertEquals(idChain.get(3), "controller.transaction");
		Assert.assertEquals(idChain.get(4), "controller.timing");
		chain.clear();
	}

	
	/**
	 * Tests basic usage and functioning of the structure; particulary characteristic of prioritization and bound/capacity.
	 */
	@Test
	public void boundPriorityBlockingQueue_basic() {
		// basic queue offering and polling
		final BoundedPriorityBlockingQueue<String> queue = new BoundedPriorityBlockingQueue<String>(2);
		queue.offer("B");
		queue.offer("A");
		Assert.assertEquals(2, queue.size());
		queue.offer("C"); // this should quietly be rejected
		Assert.assertEquals(2, queue.size()); // should still be at 2
		Assert.assertEquals("A", queue.poll());
		Assert.assertEquals("B", queue.poll());
		Assert.assertEquals(0, queue.size());

		// intermediate offering in different ordering to test priority and bound characteristics
		queue.setCapacity(5);
		// now add 6 elements
		queue.add("C");
		queue.add("E");
		queue.add("B");
		queue.add("F");
		queue.add("A"); // this will be promoted to beginning
		queue.add("D"); // this 6th element will push/reject "F" off the queue
		Assert.assertEquals(5, queue.size());
		Assert.assertEquals("A", queue.poll());
		Assert.assertEquals("B", queue.poll());
		Assert.assertEquals("C", queue.poll());
		Assert.assertEquals("D", queue.poll());
		Assert.assertEquals("E", queue.poll());
		Assert.assertTrue(queue.isEmpty());

		// test of overflow handler getting expected element
		final BoundedQueueOverflowHandlerAdapter overflowHandler = new BoundedQueueOverflowHandlerAdapter();
		queue.setOverflowHandler(overflowHandler);
		queue.setCapacity(1);
		queue.offer("M");
		queue.offer("E");
		Assert.assertEquals("M", overflowHandler.reject);
		queue.offer("K");
		Assert.assertEquals("K", overflowHandler.reject);
		queue.offer("B");
		Assert.assertEquals("E", overflowHandler.reject);
	}

	/**
	 * Load testing on single queue, with multiple producers and consumers pounding the single queue with offers and takes.
	 */
	@Test
	public void boundPriorityBlockingQueue_load() {
		final LoadSpinHandler spinHandler = new LoadSpinHandler();
		final TriggerSpinner spinner = new ConditionTriggerSpinner(spinHandler);
		int loadCount = 5000;
		final AtomicInteger counter = new AtomicInteger();
		final BoundedQueueOverflowHandlerLoadAdapter overflowHandler = new BoundedQueueOverflowHandlerLoadAdapter(counter, loadCount, spinner);
		final BoundedPriorityBlockingQueue<String> queue = new BoundedPriorityBlockingQueue<String>(2, overflowHandler);
		spinHandler.queue = queue;
		spinHandler.consumerCount = 1;

		// simple load test with only 1 producer and 1 consumer; reasonably small queue and medium sized load; and no sleeping
		final Producer producer = new Producer(0, queue, loadCount);
		final Consumer consumer = new Consumer(0, queue, counter, loadCount, spinner);
		new Thread(consumer, "consumer").start();
		new Thread(producer, "producer").start();
		spinner.spinAndWait();
		Assert.assertEquals(loadCount, counter.get());

		// load test with multiple producers and consumers, higher load count, slightly larger queue capacity
		counter.set(0);
		loadCount = 10000;
		overflowHandler.loadCount = loadCount;
		spinHandler.consumerCount = 3;
		queue.setCapacity(10); // this will be nice and strenuous
		final Producer producer1 = new Producer(0, queue, 9500); // portion of load count with little sleeping
		final Producer producer2 = new Producer(10, queue, 300); // portion of load count
		final Producer producer3 = new Producer(25, queue, 190); // portion of load count
		final Producer producer4 = new Producer(100, queue, 10); // portion of load count with .1 second of sleeping
		final Consumer consumer1 = new Consumer(25, queue, counter, loadCount, spinner); // consumes the least due to prolonged sleep interval after
																							// each take()
		final Consumer consumer2 = new Consumer(10, queue, counter, loadCount, spinner); // consumes moderate
		final Consumer consumer3 = new Consumer(0, queue, counter, loadCount, spinner); // consumes the most since never sleeps
		// fire consumers first since they just block on take() and sleep, iterate inbetween, then producers to fire away
		new Thread(consumer1, "consumer1").start();
		new Thread(consumer2, "consumer2").start();
		new Thread(consumer3, "consumer3").start();
		new Thread(producer1, "producer1").start();
		new Thread(producer2, "producer2").start();
		new Thread(producer3, "producer3").start();
		new Thread(producer4, "producer4").start();
		spinner.spinAndWait();
		Assert.assertEquals(loadCount, counter.get());
	}

	private class BoundedQueueOverflowHandlerAdapter implements BoundedQueueOverflowHandler<String>
	{
		private String reject;

		@Override
		public void handleOverflowElement(final String element) {
			this.reject = element;
		}
	}

	private class Sleeper implements Runnable
	{
		final long millis;

		private Sleeper(final long millis) {
			this.millis = millis;
		}

		@Override
		public void run() {
			try {
				if (millis > 0) {
					Thread.sleep(millis);
				}
			} catch (final InterruptedException e) {
				println("Caught InterruptedException in Sleeper: " + e.getMessage());
			}
		}
	}

	private class Producer extends Sleeper
	{
		private final BoundedPriorityBlockingQueue<String> queue;
		private final int loadCount;

		private Producer(final long millis, final BoundedPriorityBlockingQueue<String> queue, final int loadCount) {
			super(millis);
			this.queue = queue;
			this.loadCount = loadCount;
		}

		@Override
		public void run() {
			for (int i = 0; i < loadCount; i++) {
				queue.offer("Rascal says Hi!");
				super.run();
			}
		}
	}

	private final static Lock counterLock = new ReentrantLock(true);

	private class Consumer extends Sleeper
	{
		private final BoundedPriorityBlockingQueue<String> queue;
		private final AtomicInteger counter;
		private final int loadCount;
		private final TriggerSpinner spinner;

		private Consumer(final long millis, final BoundedPriorityBlockingQueue<String> queue, final AtomicInteger counter, final int loadCount, final TriggerSpinner spinner) {
			super(millis);
			this.queue = queue;
			this.counter = counter;
			this.loadCount = loadCount;
			this.spinner = spinner;
		}

		@Override
		public void run() {
			for (;;) {
				String el = null;
				try {
					el = queue.take();
				} catch (final InterruptedException e) {
					printThrowableAndFail(e);
				}

				if (!el.equals(LoadSpinHandler.STOP_SIG)) {
					try {
						counterLock.lock();
						int count = counter.get();
						if (count < loadCount) {
							;
						}
						{
							count = counter.incrementAndGet();
						}
						if (count == loadCount) {
							spinner.pullTrigger();
							break;
						}
					} finally {
						counterLock.unlock();
					}
				} else { // is a stop signal
					break;
				}
				super.run();
			}
		}
	}

	private class BoundedQueueOverflowHandlerLoadAdapter implements BoundedQueueOverflowHandler<String>
	{
		private final AtomicInteger counter;
		private final TriggerSpinner spinner;
		private int loadCount;

		public BoundedQueueOverflowHandlerLoadAdapter(final AtomicInteger counter, final int loadCount, final TriggerSpinner spinner) {
			this.counter = counter;
			this.loadCount = loadCount;
			this.spinner = spinner;
		}

		@Override
		public void handleOverflowElement(final String element) {
			try {
				counterLock.lock();
				int count = counter.get();
				if (count < loadCount) {
					;
				}
				{
					count = counter.incrementAndGet();
				}
				if (count == loadCount) {
					spinner.pullTrigger();
				}
			} finally {
				counterLock.unlock();
			}
		}
	}

	private class LoadSpinHandler implements SpinHandler
	{
		private final static String STOP_SIG = "STOP";
		BoundedPriorityBlockingQueue<String> queue;
		int consumerCount;

		@Override
		public void handleSpinIteration(final Spinner spinner) {
			// do this so all non termination-invoking consumers (just blocking on take()) each receive a STOP_SIG from the queue
			for (int i = 0; i < consumerCount - 1; i++) {
				queue.offer(STOP_SIG);
			}
			spinner.stop();
		}
	}
}