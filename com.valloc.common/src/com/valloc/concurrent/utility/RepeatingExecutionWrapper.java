/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.utility;

import java.util.Date;

import com.valloc.CategoryType;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;
import com.valloc.thread.Frequency;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
class RepeatingExecutionWrapper extends BaseExecutorWrapper implements Runnable
{
	private static final Logger logger = LogManager.manager().getLogger(RepeatingExecutionWrapper.class, CategoryType.CONCURRENT_CONTAINER_UTILITY);
	private final long FIXED_NEXT_SCHEDULING_PAD_MILLIS = 2000;  
	private final UtilityExecutor utilityExecutor;
	private final Frequency frequency;
	private final UtilityCompletionListener completionListener;
	private final UtilityContainer utilityContainer;
	private boolean markedForRemoval;
	private Date lastCompletionTimestamp;
	
	/**
	 * 
	 * @param executor
	 * @param completionListener
	 */
	RepeatingExecutionWrapper(final UtilityExecutor utilityExecutor, final Frequency frequency, final UtilityType utilityType, final Date inceptionTimestamp, final UtilityCompletionListener completionListener, final UtilityContainer utilityContainer)
	{
		super(utilityType, inceptionTimestamp);
		this.utilityExecutor = utilityExecutor;
		this.frequency = frequency;
		this.completionListener = completionListener;
		this.utilityContainer = utilityContainer;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		execute();
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
			final ContainerExecutionResult result = 
							new ContainerExecutionResult(getInceptionTimestamp(), 
														lastCompletionTimestamp, 
														getPassthroughCancelType(), 
														getCaughtThrowable());
			if (completionListener != null) {
				completionListener.utilityComplete(result);
			}
			final Date now = Util.nowTimestamp();
			if (!markedForRemoval && !isSubjectInterrupted() && !hasCaughtThrowable()) {
				reschedule(now);
			}
			lastCompletionTimestamp = now;
		}
	}

	private void reschedule(final Date now)
	{
		int secondsDelay = frequency.frequencyInSeconds();
		final UtilityType type = getUtilityType();
		// Here is safety check to not too agressively reschedule a fixed period's next scheduling  
		//		to avoid overlapping a currently running execution with a reschedule. So calculate  
		//		 what would be next time (iterating if need be) to figure next schedule:
		if (type == UtilityType.REPEATING_FIXED_SCHEDULED) {	
			final int freqDiffInMillis = secondsDelay * 1000;
			long lastComplTstmp = -1L;
			if (lastCompletionTimestamp == null) { // first time through
				// default here to now plus future pad for next scheduling
				lastComplTstmp = now.getTime() + FIXED_NEXT_SCHEDULING_PAD_MILLIS;
			} else { // subsequent times
				lastComplTstmp = lastCompletionTimestamp.getTime();
			}
			for (int i = 1; ; i++) {
				final int elapsedScheduleRoundsMillis = freqDiffInMillis * i;
				final long candidateTimeMillis = lastComplTstmp + elapsedScheduleRoundsMillis;
				final Date candidateNextExecution = new Date(candidateTimeMillis);
				final boolean aheadOfSchedule = now.compareTo(candidateNextExecution) < 0;
				if (aheadOfSchedule) {
					secondsDelay = (int) (candidateTimeMillis - lastComplTstmp) / 1000;
					break;
				}
			}			
		}
		
		utilityContainer.schedule(this, secondsDelay);
	}
	
	/**
	 * @return the markedForRemoval
	 */
	boolean isMarkedForRemoval()
	{
		return markedForRemoval;
	}

	/**
	 * @param markedForRemoval the markedForRemoval to set
	 */
	void setMarkedForRemoval(final boolean markedForRemoval)
	{
		this.markedForRemoval = markedForRemoval;
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
