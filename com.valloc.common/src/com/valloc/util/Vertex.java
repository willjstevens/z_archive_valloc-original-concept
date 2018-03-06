/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * 
 * 
 * @author wstevens
 */
public class Vertex<T>
{
	private final T item;
	private final Set<Vertex<T>> directedEdges = new HashSet<Vertex<T>>();
	private Vertex<T> breadcrumb;
	private boolean visited;

	public Vertex(final T item) {
		this.item = item;
	}

	public void addDirectedEdgeToVertex(final Vertex<T> vertex) {
		directedEdges.add(vertex);
	}

	public void removeDirectedEdgeFromVertex(final Vertex<T> vertex) {
		directedEdges.remove(vertex);
	}

	void clearAllEdges() {
		directedEdges.clear();
	}

	/**
	 * 
	 * @param destinationItem
	 * @param subjectVertices
	 * @return
	 */
	Vertex<T> bfs(final T destinationItem, final Queue<Vertex<T>> subjectVertices) {
		Vertex<T> target = null;
		visited = true;

		for (final Vertex<T> receivingVertex : directedEdges) {
			final boolean isReceivingVertexVisited = receivingVertex.isVisited();
			final boolean isBreadcrumbSet = receivingVertex.breadcrumb != null;
			// Only set breadcrumb if null - or first analyst of this condition; remember first 'analyst' of
			// this means condition means an earlier vertex on the breadth first search, which implies
			// shorter edge path.
			if (!isReceivingVertexVisited && !isBreadcrumbSet) {
				receivingVertex.breadcrumb = this;
			}
			final T receivingItem = receivingVertex.getItem();
			final boolean targetFound = receivingItem.equals(destinationItem);
			if (targetFound) {
				target = receivingVertex;
				break;
			} else {
				final boolean vertexHasEdges = receivingVertex.hasEdges();
				final boolean vertexAlreadyQueued = subjectVertices.contains(receivingVertex);
				// We only queue for later analysis if these 3 conditions are met:
				// 1) If itself, it has neighbor vertex to explore.
				// 2) If it has not already been visited to avoid confliction in state and other issues.
				// 3) We should never double add the element if already present as first queuing of
				// the vertex should be the one with the higher precedence in the BFS.
				if (vertexHasEdges && !isReceivingVertexVisited && !vertexAlreadyQueued) {
					subjectVertices.offer(receivingVertex);
				}
			}
		}

		// Here we continue to explore if other vertices are queued and we still have not found target.
		while (!subjectVertices.isEmpty() && target == null) {
			final Vertex<T> destinationVertex = subjectVertices.poll();
			target = destinationVertex.bfs(destinationItem, subjectVertices);
		}

		return target;
	}

	/**
	 * @return the directedEdges
	 */
	Set<Vertex<T>> getDirectedEdges() {
		return directedEdges;
	}

	boolean hasEdges() {
		return !directedEdges.isEmpty();
	}

	T getItem() {
		return item;
	}

	Vertex<T> getBreadcrumb() {
		return breadcrumb;
	}

	void reset() {
		breadcrumb = null;
		visited = false;
	}

	/**
	 * @return the visited
	 */
	public boolean isVisited() {
		return visited;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return item.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Vertex<?>)) {
			return false;
		}
		final Vertex<?> other = (Vertex<?>) obj;
		if (item == null) {
			if (other.item != null) {
				return false;
			}
		} else if (!item.equals(other.item)) {
			return false;
		}
		return true;
	}
}
