/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

import java.util.HashMap;
import java.util.Map;

import com.valloc.AbstractComponentFactory;
import com.valloc.Constants;
import com.valloc.core.PlatformEnvironment;
import com.valloc.localization.LocalizationFileInterpreter;
import com.valloc.util.Util;

/**
 *
 *
 * @author wstevens
 */
public final class FileComponentFactory extends AbstractComponentFactory
{
	@SuppressWarnings(Constants.RAWTYPES)
	private static final Class[] cannedInterpreterFactories = {
		LocalizationFileInterpreter.class
	};

	private PlatformEnvironment platformEnvironment;
	private DirectoryRegistry directoryRegistry;
	private FileRegistry fileRegistry;

	private final Map<String, FileInterpreterFactory<?, ? extends FileInterpreter<?>>>
		fileInterpreterFactories = new HashMap<String, FileInterpreterFactory<?, ? extends FileInterpreter<?>>>();

	/* (non-Javadoc)
	 * @see com.valloc.lifecycle.Initializable#initialize()
	 */
	@Override
	public void initialize() {
		directoryRegistry = new DirectoryRegistry();
		directoryRegistry.setEnvironment(platformEnvironment);
		fileRegistry = new FileRegistry();
		fileRegistry.setDirectoryRegistry(directoryRegistry);

		directoryRegistry.initialize();
		fileRegistry.initialize();

		for (final Class<FileInterpreterFactory<?, ? extends FileInterpreter<?>>> fileInterpreterFactoryClass : cannedInterpreterFactories) {
			final FileInterpreterFactory<?, ? extends FileInterpreter<?>> factory = Util.wrappedClassNewInstance(fileInterpreterFactoryClass);
			addFileInterpreterFactory(factory);
		}

		setInitialized(true);
	}

	public <T> FileInterpreter<T> getPlatformFileInterpreter(final PlatformFileId platformFileId) {
		final PlatformFileInfo platformFileInfo = fileRegistry.getPlatformFileInfo(platformFileId);
		@SuppressWarnings(Constants.UNCHECKED)
		final FileInterpreter<T> fileInterpreter = (FileInterpreter<T>) platformFileInfo.getFileInterpreter();
		return fileInterpreter;
	}

	public <T> FileInterpreter<T> getRuntimeFileInterpreter(final String runtimeFileId) {
		final RuntimeFileInfo runtimeFileInfo = fileRegistry.getRuntimeFileInfo(runtimeFileId);
		@SuppressWarnings(Constants.UNCHECKED)
		final FileInterpreter<T> fileInterpreter = (FileInterpreter<T>) runtimeFileInfo.getFileInterpreter();
		return fileInterpreter;
	}

	public <T, FI extends FileInterpreter<T>> void addFileInterpreterFactory(final FileInterpreterFactory<T, FI> factory) {
		final String name = factory.name();
		fileInterpreterFactories.put(name, factory);
	}

	public <FI extends FileInterpreter<?>> FI newFileInterpreter(final String factoryId) {
			FI fileInterpreter = null;

			final Object factoryAsObject = fileInterpreterFactories.get(factoryId);
			@SuppressWarnings(Constants.UNCHECKED)
			final FileInterpreterFactory<?, FI> factory = FileInterpreterFactory.class.cast(factoryAsObject);
			factory.setFileComponentFactory(this);
			fileInterpreter = factory.newInstance();

			return fileInterpreter;
	}

	public FileAccessor newFileAccessor() {
		return new BaseFileAccessor();
	}

	public void setPlatformEnvironment(final PlatformEnvironment platformEnvironment) {
		this.platformEnvironment = platformEnvironment;
	}

	public PlatformEnvironment getPlatformEnvironment() {
		return platformEnvironment;
	}

	public DirectoryRegistry getDirectoryRegistry() {
		return directoryRegistry;
	}

	public FileRegistry getFileRegistry() {
		return fileRegistry;
	}
}
