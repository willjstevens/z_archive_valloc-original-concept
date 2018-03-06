/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.session;


/**
 *
 *
 * @author wstevens
 */
abstract class AbstractClientSessionComponentFactory extends SessionComponentFactory
{
	SessionId newSessionId(final String username) {
		return new SessionId(username);
	}

}
