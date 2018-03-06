/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.transaction;

import java.util.UUID;

import javax.transaction.xa.Xid;

/**
 * 
 * 
 * @author wstevens
 */
public class UuidTxid implements Xid
{
	private final UUID id;

	public UuidTxid(final UUID id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.transaction.xa.Xid#getBranchQualifier()
	 */
	@Override
	public byte[] getBranchQualifier() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.transaction.xa.Xid#getFormatId()
	 */
	@Override
	public int getFormatId() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.transaction.xa.Xid#getGlobalTransactionId()
	 */
	@Override
	public byte[] getGlobalTransactionId() {
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (!(obj instanceof UuidTxid)) {
			return false;
		}
		final UuidTxid other = (UuidTxid) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
}
