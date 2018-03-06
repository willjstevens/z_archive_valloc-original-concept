/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain;

import static com.valloc.Constants.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.valloc.CategoryType;
import com.valloc.Constants;
import com.valloc.log.LogManager;
import com.valloc.log.Logger;

/**
 * An <i>XStream&copy;</i> implementation to the <code>Serializer</code> interface.
 * This implementation makes use of the <a href='http://xstream.codehaus.org/'>XStream</a>
 * open source Java object-to-XML serialization library.
 *
 * @author wstevens
 */
public final class XStreamSerializer implements StringSerializer
{
	private static final Logger logger = LogManager.manager().getLogger(XStreamSerializer.class, CategoryType.UTILITY);
	
	/*
	 * The main instance for processing.
	 */
	private static XStream xstream;

	/**
	 * Initializes the <code>xstream</code> variable.
	 */
	XStreamSerializer()
	{
		xstream = new XStream(new DomDriver());
	}

	/**
	 * Uses the <i>XStream&copy;</i> library to accomplish an object-to-XML based serialization.
	 *
	 * @see Serializer#serialize(Object)
	 */
	public <T> byte[] serialize(final T command) {
		byte[] xmlBytes = null;		
		ByteArrayOutputStream baos = null;
		OutputStreamWriter osw = null;
		Writer writer = null;	
		
		try {
			baos = new ByteArrayOutputStream();
			osw = new OutputStreamWriter(baos, Constants.DEFAULT_CHARSET);
			writer = new BufferedWriter(osw);						
			
			xstream.toXML(command, writer);
			xmlBytes = baos.toByteArray();			
			if (logger.isFinest()) {
				final String xmlStr = new String(xmlBytes, Constants.DEFAULT_CHARSET);
				final StringBuilder sb = new StringBuilder();
				sb.append("XML after object is transformed: ").append(LINE_SEPARATOR).append(DELIMITED_QUOTE);
				sb.append(xmlStr).append(DELIMITED_QUOTE);
				logger.finest(sb.toString());
			}
		} finally {
			try {
				writer.close();
			} catch (final IOException e) {
				logger.error("Serialize could not safely close Writer: " + e.toString(), e);
			}
			try {
				osw.close();
			} catch (final IOException e) {
				logger.error("Serialize could not safely close OutputStreamWriter: " + e.toString(), e);
			}
		}
		
		return xmlBytes;
	}

	/**
	 * Uses the <i>XStream&copy;</i> library to accomplish an XML-to-object deserialization.
	 *
	 * @see Serializer#deserialize(byte[])
	 */
    @SuppressWarnings(value={Constants.UNCHECKED})
	public <T> T deserialize(final byte[] xmlBytes) {
    	T object = null;
    	ByteArrayInputStream bais = null;
    	InputStreamReader isr = null;
    	Reader reader = null;
    	if (logger.isFinest()) {
    		final String xmlStr = new String(xmlBytes, Constants.DEFAULT_CHARSET);
    		final StringBuilder sb = new StringBuilder();
    		sb.append("XML before transformed to object: ").append(LINE_SEPARATOR).append(DELIMITED_QUOTE);
    		sb.append(xmlStr).append(DELIMITED_QUOTE);
    		// TODO: Log this or not on deserialize?
//    		logger.finest(sb.toString());
    	}
    	
		try {
			bais = new ByteArrayInputStream(xmlBytes);
			isr = new InputStreamReader(bais, Constants.DEFAULT_CHARSET);
			reader = new BufferedReader(isr);
			object = (T) xstream.fromXML(reader);
		} finally {
			try {
				reader.close();
			} catch (final IOException e) {
				logger.error("Deserialize could not safely close Reader: " + e.toString(), e);
			}
			try {
				isr.close();
			} catch (final IOException e) {
				logger.error("Deserialize could not safely close InputStreamReader: " + e.toString(), e);
			}
		}
		
    	return object;
	}

	/**
	 * Applies the class alias to XML text.
	 *
	 * @see StringSerializer#setClassAlias(String, Class)
	 */
	public void setClassAlias(final String alias, final Class<?> clazz)
	{
		xstream.alias(alias, clazz);
	}

	/**
	 * Applies the field alias to XML text.
	 *
	 * @see StringSerializer#setFieldAlias(String, Class, String)
	 */
	public void setFieldAlias(final String alias, final Class<?> clazz, final String fieldName)
	{
		xstream.aliasField(alias, clazz, fieldName);
	}
}