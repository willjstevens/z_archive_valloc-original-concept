/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

import com.valloc.Nameable;
import com.valloc.util.Factory;

/**
 *
 *
 * @author wstevens
 */
//public interface FileInterpreterFactory<T, FI extends FileInterpreter<T>> extends Factory<FI>, Identifiable<String>
public interface FileInterpreterFactory<T, FI extends FileInterpreter<T>> extends Factory<FI>, Nameable
{

	public void setFileComponentFactory(FileComponentFactory fileComponentFactory);
}
