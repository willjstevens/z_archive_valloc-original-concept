/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import java.util.ArrayList;
import java.util.Collection;

import com.valloc.Constants;

/**
 *
 *
 * @author wstevens
 */
public class PathOrientedGraph<T, E> extends Graph<T>
{

	public PathOrientedVertex<T, E> addPathOrientedVertexByItem(final T item) 
	{
		final PathOrientedVertex<T, E> vertex = new PathOrientedVertex<T, E>(item);			
		super.addVertex(vertex);
		return vertex;
	}

	@SuppressWarnings(Constants.UNCHECKED) // Grrr...  Me no likey.
	public Collection<E> getShortestPathOfEdgesToDestination(final T origin, final T destination)
	{
		final Collection<E> orderedEdges = new ArrayList<E>();
		
		final Collection<Vertex<T>> orderedVertices = getShortestPathOfVerticesToDestination(origin, destination);		
		PathOrientedVertex<T, E> fromVertex = (PathOrientedVertex<T, E>) getVertexByItem(origin);
		for (final Vertex<T> orderedVertex : orderedVertices) {
			final PathOrientedVertex<T, E> toVertex = (PathOrientedVertex<T, E>) orderedVertex;
			orderedEdges.add(fromVertex.getDirectedEdge(toVertex));
			fromVertex = toVertex;
		}
		
		return orderedEdges;
	}
}
