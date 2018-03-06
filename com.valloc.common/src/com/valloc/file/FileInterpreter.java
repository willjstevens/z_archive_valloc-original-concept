/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

import com.valloc.core.PlatformEnvironment;

/**
 *
 *
 * @author wstevens
 */
public interface FileInterpreter<T>
{

	public T readFileIntoObject();
	public void writeObjectToFile(T object);
	
	public FileAccessor getFileAccessor();
	
	void setFileAccessor(FileAccessor fileAccessor);
	void setDirectoryRegistry(DirectoryRegistry directoryRegistry);
	void setFileRegistry(FileRegistry fileRegistry);
	void setPlatformEnvironment(PlatformEnvironment platformEnvironment);
}
