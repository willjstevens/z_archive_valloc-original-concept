/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transaction;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

/**
 *
 *
 * @author wstevens
 */
abstract class AbstractTransactionManager implements TransactionManager
{
	private final Map<Xid, Transaction> processingTransactions = new HashMap<Xid, Transaction>();

	@Override
	public void addTransaction(final Transaction transaction) {
		final Xid xid = transaction.id();
		processingTransactions.put(xid, transaction);
	}

	@Override
	public Transaction getTransaction(final Xid txId) {
		return processingTransactions.get(txId);
	}

	@Override
	public void doneWithTransaction(final Transaction tx) {
		processingTransactions.remove(tx.id());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.transaction.xa.XAResource#commit(javax.transaction.xa.Xid, boolean)
	 */
	@Override
	public void commit(final Xid txId, final boolean arg1) throws XAException {
		final Transaction tx = processingTransactions.get(txId);
//		tx.commit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.transaction.xa.XAResource#rollback(javax.transaction.xa.Xid)
	 */
	@Override
	public void rollback(final Xid txId) throws XAException {
		final Transaction tx = processingTransactions.get(txId);
//		tx.rollback();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.transaction.xa.XAResource#end(javax.transaction.xa.Xid, int)
	 */
	@Override
	public void end(final Xid arg0, final int arg1) throws XAException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.transaction.xa.XAResource#forget(javax.transaction.xa.Xid)
	 */
	@Override
	public void forget(final Xid arg0) throws XAException {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.transaction.xa.XAResource#getTransactionTimeout()
	 */
	@Override
	public int getTransactionTimeout() throws XAException {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.transaction.xa.XAResource#isSameRM(javax.transaction.xa.XAResource)
	 */
	@Override
	public boolean isSameRM(final XAResource arg0) throws XAException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.transaction.xa.XAResource#prepare(javax.transaction.xa.Xid)
	 */
	@Override
	public int prepare(final Xid arg0) throws XAException {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.transaction.xa.XAResource#recover(int)
	 */
	@Override
	public Xid[] recover(final int arg0) throws XAException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.transaction.xa.XAResource#setTransactionTimeout(int)
	 */
	@Override
	public boolean setTransactionTimeout(final int arg0) throws XAException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.transaction.xa.XAResource#start(javax.transaction.xa.Xid, int)
	 */
	@Override
	public void start(final Xid arg0, final int arg1) throws XAException {
	}

}
