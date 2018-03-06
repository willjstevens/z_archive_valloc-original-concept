/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.controller;

import com.valloc.domain.system.ClientNode;


/**
 *
 *
 * @author wstevens
 */
public interface AsynchronousMultiClientController<C extends ClientNode> extends AsynchronousClient, MultiClientController<C>
{


}
