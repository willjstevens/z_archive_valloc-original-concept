/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;

import com.valloc.Executor;
import com.valloc.Identifiable;
import com.valloc.framework.Interruptible;
import com.valloc.util.UniqueId;


/**
 *
 *
 * @author wstevens
 */
public interface UtilityExecutor extends Executor, Identifiable<UniqueId>, Interruptible
{

}
