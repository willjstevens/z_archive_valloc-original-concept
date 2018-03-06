/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;

import java.util.Date;
import java.util.concurrent.Callable;

import com.valloc.CategoryType;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;

/**
 *
 *
 * @author wstevens
 */
class SingleExecutionWrapper<R> extends BaseExecutorWrapper implements Callable<R>, ResultUtilityExecutor<R>
{
	private static final Logger logger = LogManager.manager().getLogger(SingleExecutionWrapper.class, CategoryType.CONCURRENT_CONTAINER_UTILITY);
	private final ResultUtilityExecutor<R> utilityExecutor;
	
	/**
	 * 
	 * @param executor
	 */
	SingleExecutionWrapper(final ResultUtilityExecutor<R> utilityExecutor, final UtilityType utilityType, final Date inceptionTimestamp)
	{
		super(utilityType, inceptionTimestamp);
		this.utilityExecutor = utilityExecutor;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public R call()
	{
		execute();
		
		return getResult();
	}

	/* (non-Javadoc)
	 * @see com.valloc.Executor#execute()
	 */
	@Override
	public void execute()
	{
		try {
			
			utilityExecutor.execute();
			
		} catch (final Throwable throwable) {
			setCaughtThrowable(throwable);
			logger.error("Throwable caught while executing a utility executor: %s.", throwable, throwable.toString());
		} finally {
			// nothing to in single execution (yet...)
		}
	}


	/* (non-Javadoc)
	 * @see com.valloc.framework.UtilityExecutor#getResult()
	 */
	@Override
	public R getResult()
	{
		return utilityExecutor.getResult();
	}

	/* (non-Javadoc)
	 * @see com.valloc.concurrent.utility.BaseExecutorWrapper#getUtilityExecutor()
	 */
	@Override
	protected UtilityExecutor getUtilityExecutor()
	{
		return utilityExecutor;
	}
}
