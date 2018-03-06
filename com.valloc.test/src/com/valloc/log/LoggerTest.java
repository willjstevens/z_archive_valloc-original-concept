/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.log;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Test;

import com.valloc.AbstractTest;
import com.valloc.CategoryType;

/**
 * 
 *
 * @author wstevens
 */
public final class LoggerTest extends AbstractTest
{	
	@Test
	public void basicFunctionalityOnFallbackLogger()
	{
		final LogManager manager = LogManager.manager();
		final Logger c1Logger = Class1.logger;
		final Logger c2Logger = Class2.logger;
						
		manager.setCategoryLogLevelToFine(CategoryType.ALL);
		Assert.assertTrue(c1Logger.isFine());
		Assert.assertTrue(c2Logger.isFine());
		Assert.assertFalse(c1Logger.isFiner());
		Assert.assertFalse(c2Logger.isFiner());
		Assert.assertFalse(c1Logger.isFinest());
		Assert.assertFalse(c2Logger.isFinest());
		
		manager.setCategoryLogLevelToFiner(CategoryType.ALL);
		Assert.assertTrue(c1Logger.isFine());
		Assert.assertTrue(c2Logger.isFine());
		Assert.assertTrue(c1Logger.isFiner());
		Assert.assertTrue(c2Logger.isFiner());
		Assert.assertFalse(c1Logger.isFinest());
		Assert.assertFalse(c2Logger.isFinest());

		manager.setCategoryLogLevelToFinest(CategoryType.ALL);
		Assert.assertTrue(c1Logger.isFine());
		Assert.assertTrue(c2Logger.isFine());
		Assert.assertTrue(c1Logger.isFiner());
		Assert.assertTrue(c2Logger.isFiner());
		Assert.assertTrue(c1Logger.isFinest());
		Assert.assertTrue(c2Logger.isFinest());

		manager.setCategoryLogLevelToAll(CategoryType.ALL);
		Assert.assertTrue(c1Logger.isFine());
		Assert.assertTrue(c2Logger.isFine());
		Assert.assertTrue(c1Logger.isFiner());
		Assert.assertTrue(c2Logger.isFiner());
		Assert.assertTrue(c1Logger.isFinest());
		Assert.assertTrue(c2Logger.isFinest());

		manager.setCategoryLogLevelToFine(CategoryType.ALL);
		Assert.assertTrue(c1Logger.isFine());
		Assert.assertTrue(c2Logger.isFine());
		Assert.assertFalse(c1Logger.isFiner());
		Assert.assertFalse(c2Logger.isFiner());
		Assert.assertFalse(c1Logger.isFinest());
		Assert.assertFalse(c2Logger.isFinest());
		
		manager.setCategoryLogLevelToFinest(CategoryType.ALL);
		manager.removeActiveCategory(CategoryType.ALL);
		Assert.assertFalse(c1Logger.isFine());
		Assert.assertFalse(c2Logger.isFine());
		Assert.assertFalse(c1Logger.isFiner());
		Assert.assertFalse(c2Logger.isFiner());
		Assert.assertFalse(c1Logger.isFinest());
		Assert.assertFalse(c2Logger.isFinest());
	}
	
	@Test
	public void categoryFunctionalityOnFallbackLogger()
	{
		final LogManager manager = LogManager.manager();
		final Logger initializeLogger = Initializer.logger;
		final Logger transportServerLogger = TransportServer.logger;
		final Logger transportServerInitializerLogger = TransportServerInitializer.logger;
		final Logger allOnlyLogger = AllOnly.logger;		
		manager.removeActiveCategory(CategoryType.ALL); // remove default fallback level
		
		manager.setCategoryLogLevelToFine(CategoryType.INITIALIZATION);
		Assert.assertTrue(initializeLogger.isFine());
		Assert.assertFalse(allOnlyLogger.isFine());
		Assert.assertFalse(transportServerLogger.isFine());
		Assert.assertTrue(transportServerInitializerLogger.isFine());
		
		manager.setCategoryLogLevelToFiner(CategoryType.TRANSPORT_SERVER);
		Assert.assertTrue(initializeLogger.isFine());
		Assert.assertFalse(initializeLogger.isFiner());		
		Assert.assertTrue(transportServerLogger.isFine());
		Assert.assertTrue(transportServerLogger.isFiner());		
		Assert.assertTrue(transportServerInitializerLogger.isFine());
		Assert.assertTrue(transportServerInitializerLogger.isFiner());
		Assert.assertFalse(transportServerInitializerLogger.isFinest());				
		Assert.assertFalse(allOnlyLogger.isFine());
				
		manager.removeActiveCategory(CategoryType.INITIALIZATION);
		Assert.assertFalse(initializeLogger.isFine());
		Assert.assertTrue(transportServerLogger.isFine());
		Assert.assertTrue(transportServerInitializerLogger.isFine());
		Assert.assertFalse(allOnlyLogger.isFine());
	}

	@Test
	public void basicFallbackLogPrinting()
	{
		final LogManager manager = LogManager.manager();
		final Logger logger = Class1.logger;

		final String msg = "INTENTIONAL LOGGING MESSAGE";
		logger.error(msg, getTestException(msg));
		logger.warn(msg);
		logger.info(msg);
		logger.fine(msg); // should not render
		logger.finer(msg); // should not render
		logger.finest(msg); // should not render
		
		manager.setCategoryLogLevelToFine(CategoryType.ALL);
		logger.error(msg, getTestException(msg));
		logger.warn(msg);
		logger.info(msg);
		logger.fine(msg); 
		logger.finer(msg); // should not render
		logger.finest(msg); // should not render

		manager.setCategoryLogLevelToFiner(CategoryType.ALL);
		logger.error(msg, getTestException(msg));
		logger.warn(msg);
		logger.info(msg);
		logger.fine(msg); 
		logger.finer(msg);
		logger.finest(msg); // should not render

		manager.setCategoryLogLevelToFinest(CategoryType.ALL);
		logger.error(msg, getTestException(msg));
		logger.warn(msg);
		logger.info(msg);
		logger.fine(msg); 
		logger.finer(msg);
		logger.finest(msg);

		manager.removeActiveCategory(CategoryType.ALL);
		logger.error(msg, getTestException(msg));
		logger.warn(msg);
		logger.info(msg);
		logger.fine(msg); // should not render
		logger.finer(msg); // should not render
		logger.finest(msg); // should not render
	}
	
	@Test
	public void basicLogFilePrinting()
	{
		setupWorkDir();
		
		final LogManager manager = LogManager.manager();
		final Logger logger = Class1.logger;
		URL parentDirUrl = null;
		try {
			final File mockInstallLoc = initStrPathToFile(getServerRootStr());
			parentDirUrl = mockInstallLoc.toURI().toURL();
		} catch (final MalformedURLException e) {
			printThrowableAndFail(e);
		}
		final LogConfiguration logConfiguration = new LogConfiguration();
		logConfiguration.setParentDirectoryUrl(parentDirUrl);
		logConfiguration.setIsConfigSet(true);
		manager.setLogConfiguration(logConfiguration);
		manager.initialize();
		
		final String msg = "INTENTIONAL LOGGING MESSAGE";
		logger.error(msg, getTestException(msg));
		logger.warn(msg);
		logger.info(msg);
		logger.fine(msg); // should not render
		logger.finer(msg); // should not render
		logger.finest(msg); // should not render
		
		manager.setCategoryLogLevelToFine(CategoryType.ALL);
		logger.error(msg, getTestException(msg));
		logger.warn(msg);
		logger.info(msg);
		logger.fine(msg); 
		logger.finer(msg); // should not render
		logger.finest(msg); // should not render

		manager.setCategoryLogLevelToFiner(CategoryType.ALL);
		logger.error(msg, getTestException(msg));
		logger.warn(msg);
		logger.info(msg);
		logger.fine(msg); 
		logger.finer(msg);
		logger.finest(msg); // should not render

		manager.setCategoryLogLevelToFinest(CategoryType.ALL);
		logger.error(msg, getTestException(msg));
		logger.warn(msg);
		logger.info(msg);
		logger.fine(msg); 
		logger.finer(msg);
		logger.finest(msg);

		manager.removeActiveCategory(CategoryType.ALL);
		logger.error(msg, getTestException(msg));
		logger.warn(msg);
		logger.info(msg);
		logger.fine(msg); // should not render
		logger.finer(msg); // should not render
		logger.finest(msg); // should not render
		
		cleanWorkDir();
	}
	
	private static class Class1 {
		static final Logger logger = LogManager.manager().getLogger(Class1.class);
	}
	
	private static class Class2 {
		private static final Logger logger = LogManager.manager().getLogger(Class2.class);
	}

	private static class Initializer {
		private static final Logger logger = LogManager.manager().getLogger(Initializer.class);
		static {
			LogManager.manager().addLoggerToCategory(logger, CategoryType.INITIALIZATION);
		}
	}
	
	private static class TransportServer {
		private static final Logger logger = LogManager.manager().getLogger(TransportServer.class);
		static {
			LogManager.manager().addLoggerToCategory(logger, CategoryType.TRANSPORT_SERVER);
		}
	}
	
	private static class TransportServerInitializer {
		private static final Logger logger = LogManager.manager().getLogger(TransportServerInitializer.class);
		static {
			LogManager.manager().addLoggerToCategory(logger, CategoryType.INITIALIZATION);
			LogManager.manager().addLoggerToCategory(logger, CategoryType.TRANSPORT_SERVER);
		}
	}
	
	private static class AllOnly {
		private static final Logger logger = LogManager.manager().getLogger(AllOnly.class);
	}
}