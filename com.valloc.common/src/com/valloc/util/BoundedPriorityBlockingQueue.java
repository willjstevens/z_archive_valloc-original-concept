/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.valloc.CategoryType;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;

/**
 * 
 * 
 * @author wstevens
 */
public class BoundedPriorityBlockingQueue<E extends Comparable<E>> implements BlockingQueue<E>
{
	private static final Logger logger = LogManager.manager().getLogger(BoundedPriorityBlockingQueue.class, CategoryType.UTILITY);
	private final LinkedList<E> list;
	private int capacity;
	private BoundedQueueOverflowHandler<E> overflowHandler;
	private final ReentrantLock lock = new ReentrantLock(true);
	private final Condition waiter = lock.newCondition();

	/**
	 * 
	 */
	public BoundedPriorityBlockingQueue(final int capacity) {
		this.capacity = capacity;
		// don't wrap in Collections.synchronizedList since we will always wrap in lock on any access
		this.list = new LinkedList<E>();
	}

	/**
	 * @param capacity
	 * @param overflowHandler
	 */
	public BoundedPriorityBlockingQueue(final int capacity, final BoundedQueueOverflowHandler<E> overflowHandler) {
		this(capacity);
		this.overflowHandler = overflowHandler;
	}

	/*
	 * 
	 * 
	 * @param newEl
	 * 
	 * @return
	 */
	protected boolean insert(final E subject) {
		boolean elementInserted = false;
		if (subject == null) {
			throw new NullPointerException("Cannot add a null element to bounded priority blocking queue.");
		}

		try {
			lock.lock();
			if (list.isEmpty()) {
				list.add(subject);
				elementInserted = true;
			} else {
				// First disregard capacity, just jam element in at correct prioritized position...
				final Iterator<E> iter = list.iterator();
				for (int i = 0; iter.hasNext(); i++) {
					final E resident = iter.next();
					final int compareComputation = subject.compareTo(resident);
					// Note that if compare outcome is same (==0) then it should iterate again to either
					// find when it becomes higher in priority compared to subsequent element or
					// it hits the end, in which case it will be inserted last, below here.
					// Therefore here we just look for only its concern of being higher priority.
					final boolean higherPriority = compareComputation < 0;
					if (higherPriority) {
						list.add(i, subject);
						elementInserted = true;
						break;
					}
				}
				if (!elementInserted) {
					list.addLast(subject);
					elementInserted = true;
				}
			}

			// Now handle issue if we are over capacity (conceptually, not physically):
			final boolean overCapacity = list.size() > capacity; // could only be 1 over, possibly from previous add
			if (overCapacity) {
				final E reject = list.pollLast();
				if (logger.isFine()) {
					logger.fine("Found reject after insert: %s.", reject);
				}
				if (overflowHandler != null) { // optionally set, notify then..
					if (logger.isFine()) {
						logger.fine("Notifying overflow handler of rejected element: %s.", reject);
					}
					overflowHandler.handleOverflowElement(reject);
				}
				if (reject == subject) {
					elementInserted = false;
				}
			}

			if (elementInserted) { // if found, alert anything blocking..
				waiter.signal();
			}
		} finally {
			lock.unlock();
		}

		return elementInserted;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractQueue#add(java.lang.Object)
	 */
	@Override
	public boolean add(final E e) {
		return insert(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.BlockingQueue#drainTo(java.util.Collection, int)
	 */
	@Override
	public int drainTo(final Collection<? super E> c, final int maxElements) {
		int retval = 0;

		try {
			lock.lock();

			for (; retval < maxElements || retval < list.size(); retval++) {
				final E subject = list.poll();
				c.add(subject);
			}
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.BlockingQueue#drainTo(java.util.Collection)
	 */
	@Override
	public int drainTo(final Collection<? super E> c) {
		int retval = 0;

		try {
			lock.lock();

			final Iterator<E> iter = list.iterator();
			for (; iter.hasNext(); retval++) {
				final E subject = iter.next();
				c.add(subject);
			}
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.BlockingQueue#offer(java.lang.Object, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public boolean offer(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
		return offer(e); // in this implementation disregard timeout
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.BlockingQueue#offer(java.lang.Object)
	 */
	@Override
	public boolean offer(final E e) {
		return insert(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.BlockingQueue#poll(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public E poll(final long timeout, final TimeUnit unit) throws InterruptedException {
		E retval = null;

		try {
			lock.lock();
			retval = list.poll();
			if (retval == null) {
				waiter.await(timeout, unit);
				retval = list.poll(); // try once more before leaving
			}
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.BlockingQueue#put(java.lang.Object)
	 */
	@Override
	public void put(final E e) throws InterruptedException {
		insert(e); // should never wait
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.BlockingQueue#remainingCapacity()
	 */
	@Override
	public int remainingCapacity() {
		int retval = 0;
		try {
			lock.lock();
			retval = capacity - list.size();
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.BlockingQueue#take()
	 */
	@Override
	public E take() throws InterruptedException {
		E retval = null;

		try {
			lock.lock();
			do {
				retval = list.poll();
				if (retval != null) {
					break;
				}
				waiter.await();
			} while (retval == null);
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Queue#peek()
	 */
	@Override
	public E peek() {
		E retval = null;

		try {
			lock.lock();
			retval = list.peek();
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Queue#poll()
	 */
	@Override
	public E poll() {
		E retval = null;

		try {
			lock.lock();
			retval = list.poll();
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(final Object o) {
		boolean retval = false;

		try {
			lock.lock();
			retval = list.contains(o);
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		boolean retval = false;

		try {
			lock.lock();
			retval = list.isEmpty();
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<E> iterator() {
		Iterator<E> retval = null;

		try {
			lock.lock();
			retval = list.iterator();
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(final Object o) {
		boolean retval = false;

		try {
			lock.lock();
			retval = list.remove(o);
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#size()
	 */
	@Override
	public int size() {
		int retval = 0;

		try {
			lock.lock();
			retval = list.size();
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#toArray()
	 */
	@Override
	public Object[] toArray() {
		Object[] retval = null;

		try {
			lock.lock();
			retval = list.toArray();
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#toArray(T[])
	 */
	@Override
	public <T> T[] toArray(final T[] a) {
		T[] retval = null;

		try {
			lock.lock();
			retval = list.toArray(a);
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#toString()
	 */
	@Override
	public String toString() {
		return super.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Queue#element()
	 */
	@Override
	public E element() {
		E retval = null;

		try {
			lock.lock();
			retval = list.element();
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Queue#remove()
	 */
	@Override
	public E remove() {
		E retval = null;

		try {
			lock.lock();
			retval = list.remove();
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(final Collection<? extends E> c) {
		boolean retval = false;

		try {
			lock.lock();
			retval = list.addAll(c);
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear() {
		try {
			lock.lock();
			list.clear();
		} finally {
			lock.unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(final Collection<?> c) {
		boolean retval = false;

		try {
			lock.lock();
			retval = list.containsAll(c);
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(final Collection<?> c) {
		boolean retval = false;

		try {
			lock.lock();
			retval = list.removeAll(c);
		} finally {
			lock.unlock();
		}

		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(final Collection<?> c) {
		boolean retval = false;

		try {
			lock.lock();
			retval = list.retainAll(c);
		} finally {
			lock.unlock();
		}

		return retval;
	}

	public void setCapacity(final int capacity) {
		try {
			lock.lock();
			this.capacity = capacity;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * @param overflowHandler
	 *            the overflowHandler to set
	 */
	public void setOverflowHandler(final BoundedQueueOverflowHandler<E> overflowHandler) {
		try {
			lock.lock();
			this.overflowHandler = overflowHandler;
		} finally {
			lock.unlock();
		}
	}
}
