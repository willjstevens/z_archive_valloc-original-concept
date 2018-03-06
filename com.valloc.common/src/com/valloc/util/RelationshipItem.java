/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import com.valloc.Executor;
import com.valloc.Identifiable;

/**
 * A single item within a {@link RelationshipGroup} which encapsulates an {@link Executor} instance
 * to be executed, with respect to this item's dependencies. This item will execute when its 
 * {@link ResponseListener#onEvent(Event)} method is called.
 *
 * @see RelationshipGroup
 * @see RelationshipFactory
 * 
 * @author wstevens
 */
public interface RelationshipItem extends Executor, Listener<RelationshipItem>, Identifiable<String>
{
	/**
	 * Indicates the sequential execution number of the item, with respect to its encapsulating group.
	 *  
	 * @return int The number <code>this</code> item executed with comparison to other items in the group.
	 */
	public int getExecutionOrderNumber();
	
	/**
	 * Indicates that this item has been already executed; used mostly by the {@link RelationshipGroup} 
	 * object to know when to move onto to other items in the group, or stop to activate <code>this</code>
	 * item object.
	 * 
	 * @return boolean Indicates if <code>this</code> item has been fully executed.
	 */
	public boolean isExecuted();
	
	/**
	 * Adds a {@link RelationshipItem} in which <code>this</code> object is dependent upon; before
	 * <code>this</code> item may execute.
	 * 
	 * @param dependency The item <code>this</code> object is dependent upon.
	 */
	public void addDependency(RelationshipItem dependency);
	
	/**
	 * Removes the subject dependency item from the dependency list.
	 *   
	 * @param dependency The item to be removed.
	 */
	public void removeDependency(RelationshipItem dependency);

	/**
	 * Adds a {@link RelationshipItem} in which is dependent upon <code>this</code> object. Once 
	 * <code>this</code> object has executed, it will notify the dependee object via 
	 * {@link ResponseListener#notify()}.
	 * 
	 * @param dependee A dependent item <code>this</code> object.
	 */
	public void addDependee(RelationshipItem dependee);	
	
	/**
	 * Removes the dependee from the list.
	 *   
	 * @param dependee The item to be removed.
	 */
	public void removeDependee(RelationshipItem dependee);
}