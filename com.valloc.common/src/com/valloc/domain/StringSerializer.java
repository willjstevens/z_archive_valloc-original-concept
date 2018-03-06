/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain;

/**
 * Provides additional methods for techniques of serializing objects into <code>String</code> form.
 *
 * @author wstevens
 */
public interface StringSerializer extends Serializer
{
	/**
	 * Provides additional methods for providing aliases to classes names to help decrease the
	 * amount of serialized data when representing fully qualified class names.
	 *
	 * @param alias The <code>String</code> alias to replace the fully qualified class name.
	 * @param clazz The class from which the alias will be associated.
	 */
	public void setClassAlias(String alias, Class<?> clazz);

	/**
	 * Provides additional methods for providing aliases to fields to help decrease amount of serialized
	 * data and/or better name the fields in text-based file format.
	 *
	 * @param alias The <code>String</code> alias to replace the fully qualified class name.
	 * @param clazz The class from which the alias will be associated.
	 * @param fieldName The field name from which the field alias will be associated.
	 */
	public void setFieldAlias(String alias, Class<?> clazz, String fieldName);
}