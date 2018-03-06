/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

/**
 *
 *
 * @author wstevens
 */
public interface ControllerParticipator<C extends Controller>
{
	public C getController();
	public void setController(C controller);
}
