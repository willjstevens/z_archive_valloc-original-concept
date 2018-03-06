/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import java.util.HashSet;
import java.util.Set;

import com.valloc.Constants;
import com.valloc.Identifiable;

/**
 *
 *
 * @author wstevens
 */
public class DependencyItem<I, S> implements Identifiable<I>
{
	private final I id;
	private final S subject;
	private final Set<DependencyItem<I, S>> dependees = new HashSet<DependencyItem<I, S>>();
	private final Set<DependencyItem<I, S>> dependencies = new HashSet<DependencyItem<I, S>>();
	private DependencyItem<I, S> prerequisite;

	public DependencyItem(final I id, final S subject) {
		this.id = id;
		this.subject = subject;
	}

	@Override
	public I id() {
		return id;
	}

	public S getSubject() {
		return subject;
	}

	public DependencyItem<I, S> getPrerequisite() {
		return prerequisite;
	}

	public void setPrerequisite(final DependencyItem<I, S> prereq) {
		this.prerequisite = prereq;
	}

	public void addDependency(final DependencyItem<I, S> dependency) {
		dependencies.add(dependency);
		dependency.dependees.add(this);
	}

	public void addDependee(final DependencyItem<I, S> dependee) {
		dependees.add(dependee);
		dependee.dependencies.add(this);
	}

	public boolean hasDependency(final DependencyItem<I, S> item) {
		return dependencies.contains(item);
	}

	public boolean hasDependee(final DependencyItem<I, S> item) {
		return dependees.contains(item);
	}
	
	public void removeDependency(final DependencyItem<I, S> dependency) {
		dependencies.remove(dependency);
		dependency.dependees.remove(this);
	}

	public void removeDependee(final DependencyItem<I, S> dependee) {
		dependees.remove(dependee);
		dependee.dependencies.remove(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@SuppressWarnings(Constants.UNCHECKED)
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DependencyItem)) {
			return false;
		}
		final DependencyItem<I, S> other = (DependencyItem<I, S>) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "DependencyItem [id=" + id + ", subject=" + subject + "]";
	}
}
