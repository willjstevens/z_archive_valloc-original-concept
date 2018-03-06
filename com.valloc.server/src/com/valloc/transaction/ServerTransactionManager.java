/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transaction;

/**
 * 
 * 
 * @author wstevens
 */
public interface ServerTransactionManager extends TransactionManager
{
	public Transaction createAndRegisterNewTransaction();
}
