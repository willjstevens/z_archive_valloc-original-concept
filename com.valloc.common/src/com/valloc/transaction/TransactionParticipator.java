/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transaction;

/**
 * Represents an object that participates in a transaction and needs access
 * to the transacton's resources.
 * 
 * @author wstevens
 */
public interface TransactionParticipator
{
	public boolean isTransact();
	public Transaction getTransaction();
	public void setTransaction(Transaction transaction);
}
