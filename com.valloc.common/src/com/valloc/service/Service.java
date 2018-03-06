/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.Identifiable;
import com.valloc.MessageSummary;
import com.valloc.Nameable;
import com.valloc.interrupt.InterruptParticipator;
import com.valloc.transaction.TransactionParticipator;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public interface Service extends Nameable, Identifiable<UniqueId>, InterruptParticipator, TransactionParticipator
{
	public void setId(UniqueId id);
	public boolean isMessageSummaryInitialized();
	public MessageSummary getMessageSummary();

}
