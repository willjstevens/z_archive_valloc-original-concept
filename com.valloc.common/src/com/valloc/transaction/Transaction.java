/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transaction;

import javax.transaction.xa.Xid;

import com.valloc.Identifiable;
import com.valloc.util.Attributes;

/**
 * 
 * 
 * @author wstevens
 */
public class Transaction implements Identifiable<Xid>
{
	private final Xid txId;
	private String serviceName;
	private String commandName;
	private final Attributes rollbackData = new Attributes();
	
	/**
	 * 
	 */
	// TODO: This needs to have both original service and command name passed into constructor  
	public Transaction(final Xid txId) {
		this.txId = txId;
	}

	@Override
	public Xid id() {
		return txId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((txId == null) ? 0 : txId.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Transaction)) {
			return false;
		}
		final Transaction other = (Transaction) obj;
		if (txId == null) {
			if (other.txId != null) {
				return false;
			}
		} else if (!txId.equals(other.txId)) {
			return false;
		}
		return true;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getCommandName() {
		return commandName;
	}

	public Attributes getRollbackData() {
		return rollbackData;
	}
	
	
}
