/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.core;

/**
 *
 *
 * @author wstevens
 */
public class NodeManager
{
	private final static NodeManager nodeManager = new NodeManager();
	private Node node;

	private NodeManager() {
	}

	public static NodeManager getComponentManager() {
		return nodeManager;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(final Node node) {
		this.node = node;
	}


}
