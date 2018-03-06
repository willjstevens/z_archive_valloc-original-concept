/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.model;

import com.valloc.ChangeType;
import com.valloc.model.object.ModelObject;

/**
 *
 *
 * @author wstevens
 */
public interface ModelObjectChangeListener<T>
{
	
	public void modelObjectChanged(ModelObject<T> modelObject, ChangeType changeType);
}
