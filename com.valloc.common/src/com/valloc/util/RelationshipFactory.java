/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import java.util.HashMap;
import java.util.Map;

import com.valloc.ApplicationException;
import com.valloc.Executor;

/**
 * Provides implementation for both {@link RelationshipGroup} and {@link RelationshipItem}.
 *
 * @author wstevens
 */
public final class RelationshipFactory
{
	/**
	 * Returns a new {@link RelationshipGroup}, which may contain related {@link RelationshipItem}s.
	 *
	 * @return RelationshipGroup A new group.
	 */
	public static RelationshipGroup newRelationshipGroup() {
		return new RelationshipGroupImpl();
	}

	/**
	 * Returns a new {@link RelationshipItem}, which may be added to a {@link RelationshipGroup}.
	 *
	 * @param id
	 *            Labels <code>this</code> item; <i>must be unique within the group</i>.
	 * @param executor
	 *            The target object to be executed, when appropriate in the group.
	 * @return RelationshipItem A new group item.
	 */
	public static RelationshipItem newRelationshipItem(final String id, final Executor executor) {
		return new RelationshipItemImpl(id, executor);
	}

	/*
	 * Provides implementation for {@link RelationshipGroup}.
	 */
	private static class RelationshipGroupImpl implements RelationshipGroup
	{
		/*
		 * Maps an {@link Identifiable} string to a {@link RelationshipItem}.
		 */
		private final Map<String, RelationshipItem> items = new HashMap<String, RelationshipItem>();

		/*
		 * Object passed by reference so change may occur from within items.
		 */
		private final IntegerReference itemExecutionOrderIndex = new IntegerReference();

		/*
		 * @see RelationshipGroup#addItem(RelationshipItem)
		 */
		@Override
		public void addItem(final RelationshipItem item) {
			items.put(item.id(), item);
			((RelationshipItemImpl) item).itemExecutionOrderIndexReference = itemExecutionOrderIndex;
		}

		/*
		 * @see RelationshipGroup#removeItem(RelationshipItem)
		 */
		@Override
		public void removeItem(final RelationshipItem item) {
			items.remove(item.id());
		}

		/*
		 * Sequentially loops through all <code>items</code> and <code>execute</code>s them.
		 *
		 * @see Executor
		 */
		@Override
		public void execute() {
			for (final RelationshipItem item : items.values()) {
				if (!item.isExecuted()) {
					item.execute();
				}
			}
		}
	}

	/*
	 * Provides implementation for {@link RelationshipItem}.
	 */
	private static class RelationshipItemImpl implements RelationshipItem
	{
		/*
		 * The id or label for this item.
		 */
		private final String id;

		/*
		 * The object to be executed.
		 */
		private final Executor executor;

		/*
		 * Indicates what order number <code>this</code> item was executed.
		 */
		private int executionOrderNumber;

		/*
		 * Provides access to a group-level count for maintaining the execution order; this is done to for structure ordering and execution, and only
		 * incremented after the contained <code>executor</code> object has been successfully completed.
		 */
		private IntegerReference itemExecutionOrderIndexReference;

		/*
		 * A list of all item objects <code>this</code> object is dependent upon.
		 */
		private final Map<String, RelationshipItem> dependencies = new HashMap<String, RelationshipItem>();

		/*
		 * A list of all objects which are dependees upon <code>this</code> object.
		 */
		private final Map<String, RelationshipItem> dependees = new HashMap<String, RelationshipItem>();

		/*
		 * Sets necessary object elements.
		 *
		 * @param id The name to refer to the item with.
		 *
		 * @param executor The encapsulated target object to be executed.
		 */
		private RelationshipItemImpl(final String id, final Executor executor) {
			this.id = id;
			this.executor = executor;
		}

		/*
		 * @see RelationshipItem#addDependency(RelationshipItem)
		 */
		@Override
		public void addDependency(final RelationshipItem dependency) {
			dependencies.put(dependency.id(), dependency);
		}

		/*
		 * @see RelationshipItem#removeDependency(RelationshipItem)
		 */
		@Override
		public void removeDependency(final RelationshipItem dependency) {
			dependencies.remove(dependency.id());
		}

		/*
		 * @see RelationshipItem#addDependee(RelationshipItem)
		 */
		@Override
		public void addDependee(final RelationshipItem dependee) {
			dependees.put(dependee.id(), dependee);
		}

		/*
		 * @see RelationshipItem#removeDependee(RelationshipItem)
		 */
		@Override
		public void removeDependee(final RelationshipItem dependee) {
			dependees.remove(dependee.id());
		}

		/*
		 * First verifies that all dependency objects have been executed; then executes the included <code>executor</code>; finally upon successful
		 * execution, it will notify all dependee objects of its successful completion and signal to proceed with their execution. If upon first check
		 * of <i>all</i> dependencies not having been completed, this method will return and later be revisited.
		 */
		@Override
		public void execute() {
			// first determine if any dependencies have not been executed, otherwise return
			for (final RelationshipItem dependency : dependencies.values()) {
				if (!dependency.isExecuted()) {
					return;
				}
			}
			try { // perform actual execute on target object
				executor.execute();
			} catch (final ApplicationException e) {
				if (!e.isRecoverable()) {
					throw e;
				}
			}
			executionOrderNumber = itemExecutionOrderIndexReference.increment();
			// alert all dependees of completion
			final Event<RelationshipItem> event = new Event<RelationshipItem>(this);
			for (final RelationshipItem dependee : dependees.values()) {
				if (!dependee.isExecuted()) {
					dependee.onEvent(event);
				}
			}
		}

		/*
		 * Received notification to execute <code>this</code> object.
		 */
		@Override
		public void onEvent(final Event<RelationshipItem> event) {
			execute();
		}

		/*
		 * @see RelationshipItem#getExecutionOrderNumber()
		 */
		@Override
		public int getExecutionOrderNumber() {
			return executionOrderNumber;
		}

		/*
		 * @see RelationshipItem#isExecuted()
		 */
		@Override
		public boolean isExecuted() {
			return executionOrderNumber > 0 ? true : false;
		}

		/*
		 * @see Identifiable#getId()
		 */
		@Override
		public String id() {
			return id;
		}
	}
}