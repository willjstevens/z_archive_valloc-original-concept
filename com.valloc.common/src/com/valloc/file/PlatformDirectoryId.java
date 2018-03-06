/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

import static com.valloc.CategoryType.*;
import static com.valloc.file.FileConstants.*;

import com.valloc.CategoryType;
import com.valloc.core.NodeManager;
import com.valloc.framework.NodeType;

/**
 *
 *
 * @author wstevens
 */
public enum PlatformDirectoryId
{
	PLATFORM_ROOT		(determinePlatformRootDirectoryName(), 	CORE, 			null),
	PLATFORM_CONFIG		("configuration",						CORE, 			PLATFORM_ROOT),
	PLATFORM_LOG		("logs",								CORE, 			PLATFORM_ROOT),
	BUNDLE_NL			("nl", 									LOCALIZATION, 	null);

	private String rawDirectoryName;
	private CategoryType categoryType;
	private PlatformDirectoryId parent;

	private enum ComponentRootName {
		DESKTOP (ROOT_DIR_DESKTOP),
		SERVER	(ROOT_DIR_SERVER),
		AGENT	(ROOT_DIR_AGENT);

		private String rawRootName;

		private ComponentRootName(final String rawRootName) {
			this.rawRootName = rawRootName;
		}

		private static ComponentRootName toComponentRootName(final NodeType nodeType) {
			ComponentRootName retval = null;

			switch (nodeType) {
			case DESKTOP: 	retval = ComponentRootName.DESKTOP;		break;
			case SERVER:	retval = ComponentRootName.SERVER; 		break;
			case AGENT:		retval = ComponentRootName.AGENT;
			}

			if (retval == null) {
				throw new IllegalStateException("Participant type is not recognized as valid component.");
			}

			return retval;
		}
	}

	/**
	 * @param rawDirectoryName
	 * @param categoryType
	 */
	private PlatformDirectoryId(final String rawDirectoryName, final CategoryType categoryType, final PlatformDirectoryId parent) {
		this.rawDirectoryName = rawDirectoryName;
		this.categoryType = categoryType;
		this.parent = parent;
	}

	public String rawDirectoryName() {
		return rawDirectoryName;
	}

	public CategoryType categoryType() {
		return categoryType;
	}

	public PlatformDirectoryId parent() {
		return parent;
	}

	private static String determinePlatformRootDirectoryName() {
		String platformRootDirectoryName = null;

		final NodeType platformParticipantType = NodeManager.getComponentManager().getNode().getNodeType();
		final ComponentRootName componentRootNameMapping = ComponentRootName.toComponentRootName(platformParticipantType);
		platformRootDirectoryName = componentRootNameMapping.rawRootName;

		return platformRootDirectoryName;
	}
}
