/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transaction;

import javax.transaction.xa.Xid;

/**
 * 
 * 
 * @author wstevens
 */
final class ServerTransactionManagerBase extends AbstractTransactionManager implements ServerTransactionManager
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.transaction.ServerTransactionManager#createAndRegisterNewTransaction()
	 */
	@Override
	public Transaction createAndRegisterNewTransaction() {
		final Xid xid = null; // TODO: implement me
		final Transaction transaction = new Transaction(xid);
		addTransaction(transaction);
		return transaction;
	}
}
