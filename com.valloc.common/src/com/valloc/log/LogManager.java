/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.log;

import static com.valloc.Constants.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;

import com.valloc.CategoryType;
import com.valloc.lifecycle.Destroyable;
import com.valloc.lifecycle.Initializable;
import com.valloc.util.IoUtil;
import com.valloc.util.Util;

/**
 *
 *
 *
 *
 *
 * Note on handler encoding: By needing to explicitly set the encoding to be UTF-16BE is a deficiency in the logging setEncoding method. This method's
 * parent (as of Java 1.6) sets the encoding on an encapuslated OutputStreamWriter object using the STRING constructor and NOT the more appropriate
 * Charset constructor. Having done so would have appropriately handled particular bytes in a physical Big Endian platform without having to explicity
 * indicate BE. This can be revisited if Sun provides a fix on the logging API. Otherwise complying with an explicit Big Endian UTF-16 encoding to
 * default to Java's default UTF-16 encoding.
 *
 * @author wstevens
 */
public final class LogManager implements Initializable, Destroyable
{
	final static String TIMESTAMP_FORMAT = "yyyyMMdd HH:mm:ss:SSS z";
	private final static String LOG_DIRECTORY = "log";
	private final static String LOG_FILE_PREFIX = VALLOC.toLowerCase();
	private final static String LOG_FILE_PATTERN = "%g";
	private final static String LOG_FILE_SUFFIX = DOT + "log";

	private final static LogManager logManager = new LogManager();
	private final static Map<String, Logger> loggers = new HashMap<String, Logger>();
	private final static EnumMap<CategoryType, Category> categoryMap = new EnumMap<CategoryType, Category>(CategoryType.class);
	private final static EnumMap<CustomHandlerType, CustomHandler<?>> customHandlerMap =
		new EnumMap<CustomHandlerType, CustomHandler<?>>(CustomHandlerType.class);
	private final static Set<Category> activeCategories = new HashSet<Category>();

	// Defaults to all for troubleshooting in case not able to be overriden on startup.
	private LogLevel logLevel;
	private LogConfiguration logConfiguration;
	private FileHandler fileHandler;
	private ConsoleHandler consoleHandler;
	private boolean isInitialized;

	private LogManager() {
		logConfiguration = new LogConfiguration();
		logLevel = logConfiguration.getLogLevel();
	}

	public static LogManager manager() {
		return logManager;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.lifecycle.Initializable#initialize()
	 */
	@Override
	public void initialize() {
		if (logConfiguration.isConfigSet()) {
			try {
				setLogFileHandlers();
			} catch (final IllegalStateException e) {
				final String msg = "Problem occurred while attempting to set logger file handlers; falling back to console handlers due to error: " + e
						.getMessage();
				Util.printMsgToStdStreams(msg);
				Util.printThrowableToStdStreams(e);

				setFallbackHandler();
			}
		} else {
			setFallbackHandler();
		}

		isInitialized = true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.valloc.lifecycle.Destroyable#destroy()
	 */
	@Override
	public void destroy() {
		removeHandlerFromCategories(fileHandler);
		removeHandlerFromCategories(consoleHandler);
		activeCategories.clear();
		loggers.clear();
	}

	public void prepareTransferFromConsoleToFileHandler() {
		// makes assumption new log config object is set
		removeHandlerFromCategories(consoleHandler);
	}

	private void removeHandlerFromCategories(final Handler handler) {
		if (handler != null) {
			try {
				handler.flush();
				handler.close();
			} catch (final SecurityException e) {
				Util.printThrowableToStdStreams(e);
			}

			for (final Category category : categoryMap.values()) {
				category.removeHandler(handler);
			}
		}
	}

	private void setLogFileHandlers() {
		final URL parentDirectoryUrl = logConfiguration.getParentDirectoryUrl();
		final String parentDirectoryPath = IoUtil.convertUrlToFileString(parentDirectoryUrl);
		String logDirPath = IoUtil.padEndingFileSeparator(parentDirectoryPath) + LOG_DIRECTORY;
		logDirPath = IoUtil.padEndingFileSeparator(logDirPath);

		try {
			IoUtil.createDirectory(new File(logDirPath));
		} catch (final IOException e) {
			final Map<String, Object> params = Util.newParameterizedStringEntryMap();
			params.put("log-dir", logDirPath);
			final String msg = "Problem occurred when attempting to create log directory: " + Util.createParameterizedStringEntry(params);
			Util.printMsgToStdStreams(msg);
			throw new IllegalStateException(msg, e);
		}

		final StringBuilder builder = new StringBuilder();
		builder.append(logDirPath);
		builder.append(LOG_FILE_PREFIX);
		builder.append(DASH);
		builder.append(LOG_FILE_PATTERN);
		builder.append(LOG_FILE_SUFFIX);
		final String logFileName = builder.toString();
		final int fileSize = logConfiguration.getFileSize();
		final int fileGenerationCount = logConfiguration.getFileGenerationCount();
		final boolean appendPolicy = logConfiguration.doAppend();
		try {
			fileHandler = new FileHandler(logFileName, fileSize, fileGenerationCount, appendPolicy);
		} catch (final Exception e) {
			final Map<String, Object> params = Util.newParameterizedStringEntryMap();
			params.put("log-file", logFileName);
			params.put("file-size", fileSize);
			params.put("file-generation-count", fileGenerationCount);
			params.put("append-policy", appendPolicy);
			final String msg = "Problem occurred when attempting to establish logger file handler: " + Util.createParameterizedStringEntry(params);
			Util.printMsgToStdStreams(msg);
			throw new IllegalStateException(msg, e);
		}

		fileHandler.setErrorManager(new ErrorManager(fileHandler));
		initializeLineHandler(fileHandler);
		setHandlerEncoding(fileHandler);

		for (final Category category : categoryMap.values()) {
			category.addHandler(fileHandler);
		}
	}

	public void addCustomHandlerToCategory(final CategoryType categoryType, final CustomHandlerType customHandlerType) {
		CustomHandler<?> customHandler = null;
		try {
			customHandler = customHandlerType.getHandlerClass().newInstance();

			final Category category = getCategory(categoryType);
			category.addHandler(customHandler);
		} catch (final Exception e) {
			final String msg = String.format("Cannot custom handler of type %s; so only original handlers are in use.", customHandlerType
					.getHandlerClass().getSimpleName());
			Util.printMsgToStdStreams(msg);
			Util.printThrowableToStdStreams(e);
		}
	}

	public void removeCustomHandlerFromCategory(final CategoryType categoryType, final CustomHandlerType customHandlerType) {
		final Category category = getCategory(categoryType);
		final CustomHandler<?> customHandler = customHandlerMap.get(customHandlerType);
		category.removeHandler(customHandler);
	}

	private void setFallbackHandler() {
		consoleHandler = new ConsoleHandler();
		consoleHandler.setErrorManager(new ErrorManager(consoleHandler));
		initializeLineHandler(consoleHandler);
		setHandlerEncoding(consoleHandler);

		for (final Category category : categoryMap.values()) {
			category.addHandler(consoleHandler);
		}
	}

	private void setHandlerEncoding(final Handler handler) {
		try {
			handler.setEncoding(logConfiguration.getConsoleEncoding());
		} catch (final Exception e) {
			// print to stderr and eat it - just use default
			final String fallbackEncoding = handler.getEncoding();
			final String msg = String.format("Problem setting character encoding to %s on logger handler \"%s\"; using default encoding of %s.",
					logConfiguration.getConsoleEncoding(), handler.getClass().getSimpleName(), fallbackEncoding);
			Util.printMsgToStdStreams(msg);
		}
	}

	private void initializeLineHandler(final Handler handler) {
		handler.setFormatter(new LineFormatter(logConfiguration.getConsoleEncoding()));
		handler.setLevel(logLevel.level());
	}

	public Logger getLogger(final Class<?> clazz) {
		Logger retval = null;

		if (!isInitialized) {
			initialize();
		}

		final String loggerName = classToName(clazz);
		if (!loggers.containsKey(loggerName)) {
			final Logger logger = new LoggerImpl(clazz);
			loggers.put(loggerName, logger);

			final Category allCategory = getCategory(CategoryType.ALL);
			allCategory.addLogger(logger);
		}
		retval = loggers.get(loggerName);

		return retval;
	}

	public Logger getLogger(final Class<?> clazz, final CategoryType categoryType) {
		return getLogger(clazz, new CategoryType[] { categoryType });
	}

	public Logger getLogger(final Class<?> clazz, final CategoryType... categoryTypes) {
		final Logger retval = getLogger(clazz);

		for (final CategoryType categoryType : categoryTypes) {
			final Category category = getCategory(categoryType);
			category.addLogger(retval);
		}

		return retval;
	}

	public static String classToName(final Class<?> clazz) {
		// return clazz.getName();
		return clazz.getSimpleName();
	}

	public void addLoggerToCategory(final Logger logger, final CategoryType categoryType) {
		final Category category = getCategory(categoryType);
		category.addLogger(logger);
	}

	private Category getCategory(final CategoryType categoryType) {
		Category retval = categoryMap.get(categoryType);
		if (retval == null) {
			retval = new Category(categoryType);
			categoryMap.put(categoryType, retval);
			// here we set standard memory handler if set, otherwise the fallback console handler
			retval.addHandler(fileHandler != null ? fileHandler : consoleHandler);
		}

		return retval;
	}

	LogLevel getLogLevel() {
		return logLevel;
	}

	void setLogLevel(final LogLevel logLevel) {
		this.logLevel = logLevel;
	}

	public void setCategoryLogLevelToFine(final List<CategoryType> categoryTypes) {
		setCategoryLogLevel(categoryTypes, LogLevel.FINE);
	}

	public void setCategoryLogLevelToFine(final CategoryType categoryType) {
		setCategoryLogLevel(categoryType, LogLevel.FINE);
	}

	public void setCategoryLogLevelToFiner(final List<CategoryType> categoryTypes) {
		setCategoryLogLevel(categoryTypes, LogLevel.FINER);
	}

	public void setCategoryLogLevelToFiner(final CategoryType categoryType) {
		setCategoryLogLevel(categoryType, LogLevel.FINER);
	}

	public void setCategoryLogLevelToFinest(final List<CategoryType> categoryTypes) {
		setCategoryLogLevel(categoryTypes, LogLevel.FINEST);
	}

	public void setCategoryLogLevelToFinest(final CategoryType categoryType) {
		setCategoryLogLevel(categoryType, LogLevel.FINEST);
	}

	public void setCategoryLogLevelToAll(final List<CategoryType> categoryTypes) {
		setCategoryLogLevel(categoryTypes, LogLevel.ALL);
	}

	public void setCategoryLogLevelToAll(final CategoryType categoryType) {
		setCategoryLogLevel(categoryType, LogLevel.ALL);
	}

	private void setCategoryLogLevel(final List<CategoryType> categoryTypes, final LogLevel logLevel) {
		for (final CategoryType categoryType : categoryTypes) {
			setCategoryLogLevel(categoryType, logLevel);
		}
	}

	private void setCategoryLogLevel(final CategoryType categoryType, final LogLevel logLevel) {
		final Category category = getCategory(categoryType);
		category.setActive(logLevel);
		activeCategories.add(category);
	}

	public void removeActiveCategory(final List<CategoryType> categoryTypes) {
		for (final CategoryType categoryType : categoryTypes) {
			removeActiveCategory(categoryType);
		}
	}

	public void removeActiveCategory(final CategoryType categoryType) {
		final Category category = getCategory(categoryType);
		category.setInactive();
		activeCategories.remove(categoryType);
	}

	/**
	 * @param logConfiguration
	 *            the logConfiguration to set
	 */
	public void setLogConfiguration(final LogConfiguration logConfiguration) {
		this.logConfiguration = logConfiguration;
		this.logLevel = logConfiguration.getLogLevel();
		isInitialized = false;
	}

}
