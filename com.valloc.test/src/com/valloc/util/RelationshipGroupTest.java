/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.valloc.AbstractTest;
import com.valloc.ApplicationException;
import com.valloc.Executor;
import com.valloc.ApplicationException.ApplicationExceptionBuilder;

/**
 * Tests for the relationship group component.
 *
 * @author wstevens
 */
public final class RelationshipGroupTest extends AbstractTest
{
	private final Executor executor = new Executor() { public void execute(){} };
	private RelationshipItem item1, item2, item3, item4;
	private RelationshipGroup group;
	
	public RelationshipGroupTest()
	{
		super(); 
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		item1 = RelationshipFactory.newRelationshipItem("item-1", executor);
		item2 = RelationshipFactory.newRelationshipItem("item-2", executor);
		item3 = RelationshipFactory.newRelationshipItem("item-3", executor);
		item4 = RelationshipFactory.newRelationshipItem("item-4", executor);
		// create group and add items
		group = RelationshipFactory.newRelationshipGroup();
		group.addItem(item1);
		group.addItem(item2);
		group.addItem(item3);
		group.addItem(item4);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	/**
	 * Executes items in order: <i>1, 2, 3, 4</i>.
	 */
	@Test
	public void relationshipGroupItemTest1Ordered()
	{
		item1.addDependee(item2);
		item1.addDependee(item3);
		item1.addDependee(item4);
		
		item2.addDependency(item1);
		item2.addDependee(item3);
		item2.addDependee(item4);		

		item3.addDependency(item1);
		item3.addDependency(item2);
		item3.addDependee(item4);
		
		item4.addDependency(item1);
		item4.addDependency(item2);
		item4.addDependency(item3);
		
		group.execute();
		Assert.assertEquals(item1.getExecutionOrderNumber(), 1);
		Assert.assertEquals(item2.getExecutionOrderNumber(), 2);
		Assert.assertEquals(item3.getExecutionOrderNumber(), 3);
		Assert.assertEquals(item4.getExecutionOrderNumber(), 4);
	}

	/**
	 * Executes items in order: <i>1, 2, 3, 4</i>.
	 */
	@Test
	public void relationshipGroupItemTest1UnOrdered()
	{
		item2.addDependency(item1);
		item2.addDependee(item3);
		item4.addDependency(item2);
		item1.addDependee(item2);
		item4.addDependency(item3);
		item1.addDependee(item4);		
		item3.addDependency(item2);
		item2.addDependee(item4);	
		item3.addDependency(item1);	
		item4.addDependency(item1);
		item3.addDependee(item4);
		item1.addDependee(item3);		
		group.execute();
		Assert.assertEquals(item1.getExecutionOrderNumber(), 1);
		Assert.assertEquals(item2.getExecutionOrderNumber(), 2);
		Assert.assertEquals(item3.getExecutionOrderNumber(), 3);
		Assert.assertEquals(item4.getExecutionOrderNumber(), 4);
	}
	
	/**
	 * Executes items in order: <i>2, 3, 1, 4</i>.
	 */
	@Test
	public void relationshipGroupItemTest2Ordered()
	{
		item2.addDependee(item3);
		item2.addDependee(item1);
		item2.addDependee(item4);
		
		item3.addDependency(item2);
		item3.addDependee(item1);
		item3.addDependee(item4);
		
		item1.addDependency(item2);
		item1.addDependency(item3);
		item1.addDependee(item4);
		
		item4.addDependency(item2);
		item4.addDependency(item3);
		item4.addDependency(item1);
		
		group.execute();
		Assert.assertEquals(item2.getExecutionOrderNumber(), 1);
		Assert.assertEquals(item3.getExecutionOrderNumber(), 2);
		Assert.assertEquals(item1.getExecutionOrderNumber(), 3);
		Assert.assertEquals(item4.getExecutionOrderNumber(), 4);
	}
	
	/**
	 * Executes items in order: <i>2, 3, 1, 4</i>.
	 */
	@Test
	public void relationshipGroupItemTest2UnOrdered()
	{	
		item3.addDependency(item2);	
		item4.addDependency(item2);
		item3.addDependee(item1);
		item4.addDependency(item3);
		item3.addDependee(item4);		
		item1.addDependency(item2);
		item2.addDependee(item3);	
		item4.addDependency(item1);
		item1.addDependency(item3);
		item2.addDependee(item1);
		item2.addDependee(item4);
		item1.addDependee(item4);			
		group.execute();
		Assert.assertEquals(item2.getExecutionOrderNumber(), 1);
		Assert.assertEquals(item3.getExecutionOrderNumber(), 2);
		Assert.assertEquals(item1.getExecutionOrderNumber(), 3);
		Assert.assertEquals(item4.getExecutionOrderNumber(), 4);
	}
	
	/**
	 * Executes items in order: <i>4, 3, 2, 1</i>.
	 */
	@Test
	public void relationshipGroupItemTest3Ordered()
	{
		item4.addDependee(item3);
		item4.addDependee(item2);
		item4.addDependee(item1);
		
		item3.addDependency(item4);
		item3.addDependee(item2);
		item3.addDependee(item1);
		
		item2.addDependency(item4);
		item2.addDependency(item3);
		item2.addDependee(item1);
		
		item1.addDependency(item4);
		item1.addDependency(item3);
		item1.addDependency(item2);
		
		group.execute();		
		Assert.assertEquals(item4.getExecutionOrderNumber(), 1);
		Assert.assertEquals(item3.getExecutionOrderNumber(), 2);
		Assert.assertEquals(item2.getExecutionOrderNumber(), 3);
		Assert.assertEquals(item1.getExecutionOrderNumber(), 4);
	}
	
	/**
	 * Executes items in order: <i>4, 3, 2, 1</i>.
	 */
	@Test
	public void relationshipGroupItemTest3UnOrdered()
	{
		item2.addDependency(item4);
		item4.addDependee(item3);
		item1.addDependency(item2);
		item4.addDependee(item1);
		item2.addDependency(item3);	
		item3.addDependency(item4);
		item2.addDependee(item1);	
		item3.addDependee(item2);
		item1.addDependency(item3);
		item3.addDependee(item1);
		item4.addDependee(item2);				
		item1.addDependency(item4);
		
		group.execute();		
		Assert.assertEquals(item4.getExecutionOrderNumber(), 1);
		Assert.assertEquals(item3.getExecutionOrderNumber(), 2);
		Assert.assertEquals(item2.getExecutionOrderNumber(), 3);
		Assert.assertEquals(item1.getExecutionOrderNumber(), 4);
	}	
	
	/**
	 * Executes items in order: <i>1, 2, 3, 4</i> and with an item throwing an <code>ApplicationException</code>
	 * but which is also recoverable.
	 */
	@Test
	public void relationshipGroupItemTestExceptionRecoverable()
	{
		item1.addDependee(item2);
		item1.addDependee(item3);
		item1.addDependee(item4);
		
		item2.addDependency(item1);
		item2.addDependee(item3);
		item2.addDependee(item4);		

		item3.addDependency(item1);
		item3.addDependency(item2);
		item3.addDependee(item4);
		
		item4.addDependency(item1);
		item4.addDependency(item2);
		item4.addDependency(item3);
		
		Executor badExecutable = new Executor() { 
			public void execute() {
				ApplicationException e = 
					new ApplicationExceptionBuilder("Purposely thrown exception").setIsRecoverable(true).build();
				throw e;
			} 
		};	
		item2 = RelationshipFactory.newRelationshipItem("item-2", badExecutable);
		group.addItem(item2);
		
		group.execute();		
		Assert.assertEquals(item1.isExecuted(), true);
		Assert.assertEquals(item2.isExecuted(), true);
		Assert.assertEquals(item3.isExecuted(), true);
		Assert.assertEquals(item4.isExecuted(), true);
	}

	/**
	 * Executes items in order: <i>1, 2, 3, 4</i> and with an item throwing an <code>ApplicationException</code>
	 * but which is not recoverable.
	 */
	@Test
	public void relationshipGroupItemTestExceptionNotRecoverable()
	{		
		// rebuild item2 object and reference
		Executor badExecutable = new Executor() { 
			public void execute() {
				ApplicationException e = 
					new ApplicationExceptionBuilder("Purposely thrown exception").setIsRecoverable(false).build();
				throw e;
			} 
		};	
		item2 = RelationshipFactory.newRelationshipItem("item-2", badExecutable);
		item2.addDependency(item1);
		item2.addDependee(item3);
		item2.addDependee(item4);
		group.addItem(item2);
				
		item1.addDependee(item2);
		item1.addDependee(item3);
		item1.addDependee(item4);
		
		item2.addDependency(item1);
		item2.addDependee(item3);
		item2.addDependee(item4);		

		item3.addDependency(item1);
		item3.addDependency(item2);
		item3.addDependee(item4);
		
		item4.addDependency(item1);
		item4.addDependency(item2);
		item4.addDependency(item3);
		
		try {
			group.execute();
		} catch (ApplicationException e) {
			// will be thrown, catch to move on
		}		
		Assert.assertEquals(item1.isExecuted(), true);
		Assert.assertEquals(item2.isExecuted(), false);
		Assert.assertEquals(item3.isExecuted(), false);
		Assert.assertEquals(item4.isExecuted(), false);
	}
}