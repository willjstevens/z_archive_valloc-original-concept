/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.service;

import com.valloc.Nameable;
import com.valloc.framework.ReferenceKit;



/**
 *
 *
 * @author wstevens
 */
public abstract interface ServiceFactory extends Nameable
{
	//	public abstract Service buildService(ReferenceKit referenceKit);
	public abstract <S extends Service> S buildService(ReferenceKit referenceKit);

}
