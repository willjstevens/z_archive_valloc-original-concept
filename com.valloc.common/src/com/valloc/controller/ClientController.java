/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import com.valloc.service.Service;

/**
 *
 *
 * @author wstevens
 */
public abstract interface ClientController extends Controller
{
	public void setService(Service service);
}
