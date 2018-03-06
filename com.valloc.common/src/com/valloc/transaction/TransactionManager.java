/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transaction;

import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

/**
 * 
 * 
 * @author wstevens
 */
public interface TransactionManager extends XAResource
{
	public Transaction getTransaction(final Xid txId);

	public void doneWithTransaction(final Transaction tx);

	void addTransaction(Transaction transaction);
}
