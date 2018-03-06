/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.concurrent.utility.ResultUtilityExecutor;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public class ResultUtilityExecutorAdapter<R> extends UtilityExecutorAdapter implements ResultUtilityExecutor<R>
{
	private final R result;

	/**
	 * @param id
	 */
	public ResultUtilityExecutorAdapter(final UniqueId id, final R result)
	{
		super(id);
		this.result = result;
	}

	/* (non-Javadoc)
	 * @see com.valloc.framework.UtilityExecutor#getResult()
	 */
	@Override
	public R getResult()
	{
		return result;
	}
}
