/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author wstevens
 */
public class DependencyGroup<I, S>
{
	private final Map<I, DependencyItem<I, S>> items = new HashMap<I, DependencyItem<I, S>>();

	public void addItemToTemplate(final DependencyItem<I, S> item) {
		items.put(item.id(), item);
	}

	public DependencyItem<I, S> getDependencyItem(final I id) {
		return items.get(id);
	}

	public Collection<DependencyItem<I, S>> getAllItems() {
		return items.values();
	}

	public void insertAccordingToTemplate(final DependencyItem<I, S> item, final List<DependencyItem<I, S>> target) {
		final DependencyItem<I, S> prereq = item.getPrerequisite();
		if (prereq != null && !target.contains(prereq)) {
			insertAccordingToTemplate(prereq, target);
		}
		if (target.isEmpty()) {
			target.add(item);
		} else {
			int insertIdx = 0;
			for (int i = 0, size = target.size(); i < size; i++) {
				final DependencyItem<I, S> nextItem = target.get(i);
				if (item.hasDependency(nextItem)) {
					insertIdx = i + 1; // just after the next item
				}
			}
			target.add(insertIdx, item);
		}
	}

	public List<I> toIdentifierList(final List<DependencyItem<I, S>> source) {
		final List<I> idList = new ArrayList<I>();
		for (final DependencyItem<I, S> item : source) {
			idList.add(item.id());
		}
		return idList;
	}

	public List<S> toSubjectList(final List<DependencyItem<I, S>> source) {
		final List<S> idList = new ArrayList<S>();
		for (final DependencyItem<I, S> item : source) {
			idList.add(item.getSubject());
		}
		return idList;
	}
}
