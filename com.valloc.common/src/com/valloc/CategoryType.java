/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc;

/**
 *
 *
 * @author wstevens
 */
public enum CategoryType
{
	ALL								("all"),
	CONCURRENT						("concurrent"),
	CONCURRENT_CONTAINER			("concurrent.container"),
	CONCURRENT_CONTAINER_REQUEST	("concurrent.container.request"),
	CONCURRENT_CONTAINER_UTILITY	("concurrent.container.utility"),
	CONFIG							("config"),
	CONTROLLER						("controller"),
	CORE							("core"),
	DATABASE						("database"),
	FILE							("file"),
	FRAMEWORK						("framework"),
	INITIALIZATION					("initialization"),
	LOCALIZATION					("localization"),
	LOG								("log"),
	SECURITY						("security"),
	SECURITY_CRYPTO					("security.crypto"),
	SECURITY_TRANSPORT				("security.transport"),
	SERVICE							("service"),
	SESSION 						("session"),
	STATE_TRANSITION				("state-transition"),
	TRANSPORT		 				("transport"),
	TRANSPORT_CONNECTOR 			("transport.connector"),
	TRANSPORT_SERVER 				("transport.server"),
	UTILITY							("utility");

	private String id;

	private CategoryType(final String id) {
		this.id = id;
	}

	public String id() {
		return id;
	}
}
