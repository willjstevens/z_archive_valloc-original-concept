/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.Executor;
import com.valloc.Identifiable;
import com.valloc.Prioritizable;
import com.valloc.interrupt.InterruptParticipator;
import com.valloc.util.UniqueId;


/**
 *
 *
 * @author wstevens
 */
//public interface FrameworkExecutor extends Executor, Identifiable<UniqueId>, Prioritizable, Interruptible
public interface FrameworkExecutor extends Executor, Identifiable<UniqueId>, Prioritizable, InterruptParticipator
{

}
