/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import java.util.Map;

import com.valloc.Executor;

/**
 * A top-level grouping object for encapsulating, logically related group items, intended to have
 * relation to one another. Each item may have dependies on others, or dependees upon itself. The group
 * and all included within, are as of now, single threaded. Each item will execute with respect to after
 * its dependencies have been executed, then itself, then it will notify all dependees. With regards to 
 * two items with the same dependencies which have also completed the same time, the next item to be 
 * executed will be up to that of the {@link Map} implementation and position in looping upon choosing  
 * the next item to execute.
 * <br /><br />
 * <b>Caution:</b> At this time, there is no mechanism to detect and prevent circular dependencies, as 
 * this could cause usage to lockup, if not proactively careful.
 * 
 * @see RelationshipItem
 * @see RelationshipFactory
 * 
 * @author wstevens
 */
public interface RelationshipGroup extends Executor 
{
	/**
	 * Adds a {@link RelationshipItem} to <code>this</code> group.
	 *  
	 * @param item The item to be contributed to the group.
	 */
	public void addItem(RelationshipItem item);
	
	/**
	 * Removes a {@link RelationshipItem} from <code>this</code> group.
	 * 
	 * @param item The item to be removed from the group.
	 */
	public void removeItem(RelationshipItem item);
}