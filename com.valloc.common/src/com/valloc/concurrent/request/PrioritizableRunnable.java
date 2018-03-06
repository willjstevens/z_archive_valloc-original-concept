/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.concurrent.request;

/**
 *
 *
 * @author wstevens
 */
public interface PrioritizableRunnable extends Runnable, PrioritizableRequestElement, Comparable<PrioritizableRunnable>
{
}