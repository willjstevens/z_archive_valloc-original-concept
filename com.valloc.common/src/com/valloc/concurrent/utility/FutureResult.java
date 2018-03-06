/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;

import com.valloc.interrupt.InterruptType;

/**
 *
 *
 * @author wstevens
 */
public final class FutureResult<R>
{
	private final ManagedRunnableScheduledFuture<R> futureCallable;
	private R result;
	private InterruptType interruptType;
	private Throwable throwable;
	
	/**
	 * 
	 */
	FutureResult(final ManagedRunnableScheduledFuture<R> futureCallable)
	{
		this.futureCallable = futureCallable;
	}
	
	public void waitForCompletion()
	{
		result = futureCallable.get();	
	}

	public boolean isDone()
	{
		return futureCallable.isDone();
	}
	
	public boolean wasSuccess()
	{
		return interruptType == null && throwable == null;
	}
	
	public boolean wasCancelled()
	{
		return interruptType != null;
	}
	
	public boolean wasError()
	{
		return throwable != null;
	}
	
	/**
	 * @param result the result to set
	 */
	void setResult(final R subject)
	{
		this.result = subject;
	}

	/**
	 * @param interruptType the interruptType to set
	 */
	void setCancelType(final InterruptType interruptType)
	{
		this.interruptType = interruptType;
	}

	/**
	 * @param throwable the throwable to set
	 */
	void setThrowable(final Throwable throwable)
	{
		this.throwable = throwable;
	}

	/**
	 * @return the result
	 */
	public R getResult()
	{
		return result;
	}

	/**
	 * @return the interruptType
	 */
	public InterruptType getCancelType()
	{
		return interruptType;
	}

	/**
	 * @return the throwable
	 */
	public Throwable getThrowable()
	{
		return throwable;
	}
}
