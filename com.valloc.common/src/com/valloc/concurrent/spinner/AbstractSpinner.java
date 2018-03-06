/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.spinner;

import com.valloc.ApplicationException;
import com.valloc.CategoryType;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;

/**
 * 
 * 
 * @author wstevens
 */
abstract class AbstractSpinner implements Spinner
{
	private static final Logger logger = LogManager.manager().getLogger(AbstractSpinner.class, CategoryType.UTILITY);
	private final SpinHandler spinHandler;
	private boolean isSpinning;

	/**
	 * 
	 * 
	 * @param spinHandler
	 */
	AbstractSpinner(final SpinHandler spinHandler) {
		this.spinHandler = spinHandler;
	}

	abstract void block();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.spinner.Spinner#spinAndWait()
	 */
	@Override
	public void spinAndWait() throws ApplicationException {
		isSpinning = true;
		do {
			try {
				block();
				spinHandler.handleSpinIteration(this);
			} catch (final Exception e) {
				ApplicationException throwVal = null;
				if (e instanceof ApplicationException) {
					throwVal = (ApplicationException) e;
				} else {
					throwVal = new ApplicationException(e);
				}
				logger.error("Caught and will rethrow the following exception from the spinner: ", e);
				isSpinning = false;
				throw throwVal;
			}
		} while (isSpinning);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.valloc.concurrent.spinner.Spinner#stop()
	 */
	@Override
	public void stop() {
		isSpinning = false;
	}
}