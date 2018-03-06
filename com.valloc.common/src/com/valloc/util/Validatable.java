/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

/*
 * USAGE:
 *
	final Result result = new Validatable<Result>() {
		@Override
		public Result validate() {
			return null;
		}
	}.validate();

	final ReturnObjectResult<String> result2 = new Validatable<ReturnObjectResult<String>>() {
		@Override public ReturnObjectResult<String> validate() {
			return null;
		}
	}.validate();
*/

/**
 *
 *
 * @author wstevens
 */
public interface Validatable<R extends Result>
{
	public R validate();
}
