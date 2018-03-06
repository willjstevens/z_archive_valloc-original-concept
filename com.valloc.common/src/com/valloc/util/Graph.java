/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.valloc.Constants;

/**
 * 
 * 
 * @author wstevens
 */
public class Graph<T>
{
	private final Set<Vertex<T>> vertices = new HashSet<Vertex<T>>();

	public Vertex<T> addVertexByItem(final T item) {
		final Vertex<T> vertex = new Vertex<T>(item);
		addVertex(vertex);

		return vertex;
	}

	void addVertex(final Vertex<T> vertex) {
		final boolean wasAdded = vertices.add(vertex);
		if (!wasAdded) {
			throw new IllegalArgumentException(String.format("Element %s already present in graph.", vertex.getItem()));
		}
	}

	Vertex<T> getVertexByItem(final T item) {
		Vertex<T> vertex = null;

		for (final Vertex<T> vertexCandidate : vertices) {
			final T itemCandidate = vertexCandidate.getItem();
			if (item.equals(itemCandidate)) {
				vertex = vertexCandidate;
				break;
			}
		}

		return vertex;
	}

	public Collection<T> getShortestPathToDestination(final T origin, final T destination) {
		final Collection<T> orderedItems = new ArrayList<T>();

		final Collection<Vertex<T>> vertices = getShortestPathOfVerticesToDestination(origin, destination);
		for (final Vertex<T> vertex : vertices) {
			orderedItems.add(vertex.getItem());
		}

		return orderedItems;
	}

	synchronized Collection<Vertex<T>> getShortestPathOfVerticesToDestination(final T origin, final T destination) {
		final Deque<Vertex<T>> retval = new ArrayDeque<Vertex<T>>();

		try {
			final Vertex<T> originVertex = getVertexByItem(origin);
			if (originVertex == null) {
				throw new IllegalStateException("No origin element found in graph.");
			}

			final Queue<Vertex<T>> subjectVertices = new LinkedList<Vertex<T>>();
			// Perform primary recursive search here for target
			final Vertex<T> target = originVertex.bfs(destination, subjectVertices);
			if (target == null) {
				throw new IllegalStateException("No destination element found in graph.");
			}

			Vertex<T> el = target;
			do { // Now we rebuild path from the origin to the realized target
				final Vertex<T> breadcrumb = el.getBreadcrumb();
				if (breadcrumb != null) {
					retval.addFirst(el);
				}
				el = breadcrumb;
			} while (el != null);
			if (retval.isEmpty()) {
				final String msg = String.format("Found no path from origin %s to destination %s. Please reconfigure data structure.", origin,
						destination);
				throw new IllegalStateException(msg);
			}
		} finally {
			// Always reset all state for breadcrumb references and marked visited links for next user of graph.
			for (final Vertex<T> vertex : vertices) {
				vertex.reset();
			}
		}

		return retval;
	}

	void clearAllVertexEdges() {
		for (final Vertex<T> vertex : vertices) {
			vertex.clearAllEdges();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Graph vertices: {");
		for (final Vertex<T> vertex : vertices) {
			sb.append(vertex).append(Constants.COMMA);
		}
		Util.chompLastChar(sb);
		sb.append('}');

		return sb.toString();
	}
}
