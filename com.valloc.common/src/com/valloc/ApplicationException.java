/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc;

import com.valloc.util.Builder;


/**
 * A unchecked exception with the intent to report an application unanticipated problem, which could  
 * be recoverable or unrecoverable. 
 * <br /><br />
 * This exception should be appropriately used in with respect to the following understanding:
 * <ul>
 * 	<li>An application component should throw a <code>ApplicationException</code> if it encounters 
 * 		an abnormal situation in which it should catch the root exception, embed pass it into 
 * 		<code>this</code> exception and rethrow.
 * 	</li> 
 * 	<li>The above is partially done such that not every application and/or business method sigature
 * 		needs to declare a <code>throws</code> clause of an number of miscellaneous exceptions.
 * 	</li>
 * 	<li>The application component which then catches the root or embedded exception cause may then also
 * 		indicate within this <code>ApplicationException</code> whether or not the overall operation may
 * 		recover and continue to process, via the <b><code>isRecoverable</code></b> method. Defaults to 
 * 		false.
 * 	</li>
 *  <li>If the embedded exception is something which should be presented to the end user (which most often
 *  	the case, it should not), then it may be indicated with the <code>doPresentToUser</code> flag. If 
 *  	the exception was to be presented to the end user, then the message included within the 
 *  	<code>getMessage</code> method is what will be presented - so be cautious as to what is in there!
 *  	Defaults to false.
 *  </li>
 *  <li>An application controller component within the framework may then catch any 'bad' exception which 
 *  	<i>should always</i> be encapsulated within a <code>ApplicationException</code>, log it and decide
 *  	which direction to go or action to take, given both the <code>doPresentToUser</code> and the 
 *  	<code>isRecoverable</code> method.
 *  </li>      
 * <ul>
 * 
 * @author wstevens
 */
public final class ApplicationException extends RuntimeException
{
	private static final long serialVersionUID = -2835894710631933945L;
	
	/*
	 * Indicates whether/not to present the parent <code>message</code> to the end user. 
	 */
	private boolean presentToUser;
	
	/*
	 * Indicates whether/not the operation may continue to process, or abort operation.
	 */
	private boolean isRecoverable;
	
	/**
	 * Exposes construction to parent with just a message provided.
	 * 
	 * @param message The message to be embedded in the parent.
	 */
	public ApplicationException(final String message)
	{
		super(message);
	}

	/**
	 * Exposes construction to the parent with both a message and root <code>Throwable</code> cause.
	 * 
	 * @param cause The root cause of the problem.
	 */
	public ApplicationException(final Throwable cause)
	{
		super(cause);
	}
	
	/**
	 * Exposes construction to the parent with both a message and root <code>Throwable</code> cause.
	 * 
	 * @param message The message to be embedded in the parent.
	 * @param cause The root cause of the problem.
	 */
	public ApplicationException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Provides access to building and assembling a new <code>ApplicationException</code>.
	 */
	public static final class ApplicationExceptionBuilder implements Builder<ApplicationException>
	{
		private final String message;
		private Throwable throwable;
		private boolean presentToUser;
		private boolean isRecoverable;
		
		/**
		 * Default constructor taking a mandatory message.
		 * 
		 * @param message The message to be passed to the parent.
		 */
		public ApplicationExceptionBuilder(final String message) 
		{
			this.message = message;
		}
		
		/**
		 * @see Builder#build()
		 */
		@Override
		public ApplicationException build()
		{			
			ApplicationException retval = null;
			if (throwable == null) {
				retval = new ApplicationException(message);
			} else {
				retval = new ApplicationException(message, throwable);
			}
			retval.presentToUser = this.presentToUser;
			retval.isRecoverable = this.isRecoverable;
			return retval;
		}

		/**
		 * @param throwable Sets the throwable.
		 */
		public ApplicationExceptionBuilder setThrowable(final Throwable throwable)
		{
			this.throwable = throwable;
			return this;
		}

		/**
		 * @param presentToUser Sets the presentToUser.
		 */
		public ApplicationExceptionBuilder setDoPresentToUser(final boolean presentToUser)
		{
			this.presentToUser = presentToUser;
			return this;
		}
		
		/**
		 * @param isRecoverable Sets the isRecoverable.
		 */
		public ApplicationExceptionBuilder setIsRecoverable(final boolean isRecoverable)
		{
			this.isRecoverable = isRecoverable;
			return this;
		}
	}
				 
	/**
	 * If the embedded exception is something which should be presented to the end user (which most often
	 * the case, it should not), then it may be indicated with the <code>doPresentToUser</code> flag. If 
	 * the exception was to be presented to the end user, then the message included within the 
	 * <code>getMessage</code> method is what will be presented - so be cautious as to what is in there!
	 * Defaults to false.
	 * 
	 * @return boolean Returns the presentToUser.
	 */
	public boolean doPresentToUser()
	{
		return presentToUser;
	}

	/**
	 * The application component which then catches the root or embedded exception cause may then also
	 * indicate within this <code>ApplicationException</code> whether or not the overall operation may
	 * recover and continue to process, via the <b><code>isRecoverable</code></b> method, or abort.
	 * Defaults to false.
	 * 
	 * @return boolean Indicates whether/not this exception is recoverable.
	 */
	public boolean isRecoverable()
	{
		return isRecoverable;
	}
	
	/**
	 * Returns the message of the embedded <code>Exception</code>. Most often this is used to display
	 * to the end user.
	 * 
	 * @return String The message of the embedded <code>Exception</code>.
	 */
	@Override
	public String getMessage()
	{
		return super.getMessage();
	}
}