/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain;

/**
 * 
 * 
 * @author wstevens
 */
public final class SerializerFactory
{
	private final static SerializerFactory factory = new SerializerFactory();
	private static StringSerializer serializer;

	private SerializerFactory() {
		synchronized (this) {
			serializer = new XStreamSerializer();
			DomainObjectSupportFactory.getDomainObjectSupportFactory().initializeDomainSupport(serializer);
		}
	}

	public static SerializerFactory getSerializerFactory() {
		return factory;
	}

	public Serializer getSerializer() {
		return serializer;
	}

	public StringSerializer getStringSerializer() {
		return serializer;
	}

}
