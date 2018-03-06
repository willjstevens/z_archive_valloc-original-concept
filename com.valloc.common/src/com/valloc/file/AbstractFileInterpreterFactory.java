/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractFileInterpreterFactory<T, FI extends FileInterpreter<T>> implements FileInterpreterFactory<T, FI>
{
	private FileComponentFactory fileComponentFactory;
		
	protected void setBasicReferences(final FileInterpreter<T> fileInterpreter) {
		fileInterpreter.setPlatformEnvironment(fileComponentFactory.getPlatformEnvironment());
		fileInterpreter.setDirectoryRegistry(fileComponentFactory.getDirectoryRegistry());
		fileInterpreter.setFileRegistry(fileComponentFactory.getFileRegistry());
	}

	@Override
	public void setFileComponentFactory(final FileComponentFactory fileComponentFactory) {
		this.fileComponentFactory = fileComponentFactory;
	}

	protected FileComponentFactory getFileComponentFactory() {
		return fileComponentFactory;
	}
}
