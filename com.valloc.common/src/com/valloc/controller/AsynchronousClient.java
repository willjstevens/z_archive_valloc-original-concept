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
public abstract interface AsynchronousClient
{

	public void submitAsync();
	public void blockForCompletion();
	public boolean isBlocking();
	public void signalCompletion();
}
