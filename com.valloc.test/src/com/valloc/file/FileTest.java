/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.file;

import org.junit.Before;
import org.junit.Test;

import com.valloc.AbstractTest;
import com.valloc.core.NodeManager;
import com.valloc.core.ServerNode;
import com.valloc.core.ServerVallocXmlConfiguration;

/**
 *
 *
 * @author wstevens
 */
public final class FileTest extends AbstractTest
{
	private NodeManager nodeManager;
	private FileComponentFactory fileComponentFactory;

	@Before
	public void setUp() throws Exception {
		nodeManager = NodeManager.getComponentManager();
		nodeManager.setNode(new ServerNode());
		fileComponentFactory = new FileComponentFactory();
	}

	@Test
	public void basicUsage()
	{
		ServerVallocXmlConfiguration platformFileObject = new ServerVallocXmlConfiguration();
		final FileInterpreter<ServerVallocXmlConfiguration> fileInterpreter = fileComponentFactory.getPlatformFileInterpreter(PlatformFileId.VALLOC_XML);
		fileInterpreter.writeObjectToFile(platformFileObject);
		platformFileObject = fileInterpreter.readFileIntoObject();

		String runtimeFileObject = "This is the body of the text document.";
		final FileInterpreter<String> runtimeFileInterpreter = fileComponentFactory.getRuntimeFileInterpreter("file:///my-env-file.txt");
		runtimeFileInterpreter.writeObjectToFile(runtimeFileObject);
		runtimeFileObject = runtimeFileInterpreter.readFileIntoObject();
	}
}