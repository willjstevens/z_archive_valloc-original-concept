/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import java.util.HashMap;
import java.util.Map;


/**
 *
 *
 * @author wstevens
 */
public class PathOrientedVertex<T, E> extends Vertex<T>
{
	private final Map<PathOrientedVertex<T, E>, E> edgeObjects = new HashMap<PathOrientedVertex<T, E>, E>();
	

	public PathOrientedVertex(final T item)
	{
		super(item);
	}

	public void addDirectedEdgeToVertex(final PathOrientedVertex<T, E> vertex) 
	{
		throw new IllegalStateException("You must configure Graph to use edges from beginning and use the appropriate method.");
	}

	public void removeDirectedEdgeFromVertex(final PathOrientedVertex<T, E> vertex) 
	{
		throw new IllegalStateException("You must configure Graph to use edges from beginning and use the appropriate method.");
	}
	
	public void addDirectedEdgeToVertex(final PathOrientedVertex<T, E> vertex, final E edge) 
	{
		getDirectedEdges().add(vertex);
		edgeObjects.put(vertex, edge);
	}

	public E getDirectedEdge(final PathOrientedVertex<T, E> directedEdge)
	{
		return edgeObjects.get(directedEdge);
	}
	
	public void removeDirectedEdgeFromVertex(final PathOrientedVertex<T, E> vertex, final E edge) 
	{
		getDirectedEdges().remove(vertex);
		edgeObjects.remove(vertex);
	}
	
	@Override
	void clearAllEdges()
	{
		super.clearAllEdges();
		edgeObjects.clear();
	}
}
