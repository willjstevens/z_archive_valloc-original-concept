/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.localization;

import java.util.PropertyResourceBundle;

import com.valloc.file.AbstractFileInterpreterFactory;
import com.valloc.file.FileComponentFactory;
import com.valloc.file.FileConstants;

/**
 *
 *
 * @author wstevens
 */
public class LocalizationFileInterpreterFactory extends AbstractFileInterpreterFactory<PropertyResourceBundle, LocalizationFileInterpreter>
{

	/*
	 * (non-Javadoc)
	 * @see com.valloc.Nameable#name()
	 */
	@Override
	public String name() {
		return FileConstants.FILE_INTERPRETER_RESOURCE_BUNDLE;
	}

	/* (non-Javadoc)
	 * @see com.valloc.util.Factory#newInstance()
	 */
	@Override
	public LocalizationFileInterpreter newInstance() {
		final LocalizationFileInterpreter fileInterpreter = new LocalizationFileInterpreter();

		final FileComponentFactory fileComponentFactory = getFileComponentFactory();
		fileInterpreter.setFileAccessor(fileComponentFactory.newFileAccessor());
		setBasicReferences(fileInterpreter);

		return fileInterpreter;
	}

}
