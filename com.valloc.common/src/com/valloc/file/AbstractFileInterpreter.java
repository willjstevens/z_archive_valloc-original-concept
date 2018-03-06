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
public abstract class AbstractFileInterpreter<T> implements FileInterpreter<T>
{
	private FileAccessor fileAccessor;
	private DirectoryRegistry directoryRegistry;
	private FileRegistry fileRegistry;
	private PlatformEnvironment platformEnvironment;

	/*
	 * (non-Javadoc)
	 * @see com.valloc.file.FileInterpreter#readFileIntoObject()
	 */
	@Override
	public T readFileIntoObject() {
		{
			/*
			 * Placed here to be overridden if necessary; more often, client code will be concerned with
			 * reading the file into an object such as in the case of static, never-changing files like
			 * property files.  So FileInterpreter can choose to override the write if necessary for a
			 * particular file type.
			 */
			throw new IllegalAccessError("Child class must override this read method if intended.");
		}
	}

	/* (non-Javadoc)
	 * @see com.valloc.file.FileInterpreter#writeObjectToFile(java.lang.Object)
	 */
	@Override
	public void writeObjectToFile(final T object) {
		/*
		 * Placed here to be overridden if necessary; more often, client code will be concerned with
		 * reading the file into an object such as in the case of static, never-changing files like
		 * property files.  So FileInterpreter can choose to override the write if necessary for a
		 * particular file type.
		 */
		throw new IllegalAccessError("Child class must override this write method if intended.");
	}

	protected DirectoryRegistry getDirectoryRegistry() {
		return directoryRegistry;
	}

	protected FileRegistry getFileRegistry() {
		return fileRegistry;
	}

	protected PlatformEnvironment getPlatformEnvironment() {
		return platformEnvironment;
	}

	@Override
	public FileAccessor getFileAccessor() {
		return fileAccessor;
	}

	@Override
	public void setDirectoryRegistry(final DirectoryRegistry directoryRegistry) {
		this.directoryRegistry = directoryRegistry;
	}

	@Override
	public void setFileAccessor(final FileAccessor fileAccessor) {
		this.fileAccessor = fileAccessor;
	}

	@Override
	public void setFileRegistry(final FileRegistry fileRegistry) {
		this.fileRegistry = fileRegistry;
	}

	@Override
	public void setPlatformEnvironment(final PlatformEnvironment platformEnvironment) {
		this.platformEnvironment = platformEnvironment;
	}


}
