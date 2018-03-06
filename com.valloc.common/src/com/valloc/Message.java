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
public final class Message
{
	private final String id;
	private final MessageType type;
	private final String message;
	private final boolean displayToUser;

	Message(final String id, final MessageType type, final String message) {
		this(id, type, message, true);
	}

	Message(final String id, final MessageType type, final String message, final boolean displayToUser) {
		this.id = id;
		this.type = type;
		this.message = message;
		this.displayToUser = displayToUser;
	}

	public String getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public boolean doDisplayToUser() {
		return displayToUser;
	}

	public boolean isSuccess() {
		return type == MessageType.SUCCESS;
	}

	public boolean isUserInformation() {
		return type == MessageType.USER_INFORMATION;
	}

	public boolean isUserWarning() {
		return type == MessageType.USER_WARNING;
	}

	public boolean isUserError() {
		return type == MessageType.USER_ERROR;
	}

	public boolean isSystemInformation() {
		return type == MessageType.SYSTEM_INFORMATION;
	}

	public boolean isSystemWarning() {
		return type == MessageType.SYSTEM_WARNING;
	}

	public boolean isSystemError() {
		return type == MessageType.SYSTEM_ERROR;
	}
}
