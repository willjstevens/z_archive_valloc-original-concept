/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain;

/**
 * Defines methods for serializing an <code>Object</code> into a stream of bytes (and back again)
 * for any general type of transmission whether it be to the network or file. The <code>byte</code>
 * array can be XML based, binary, etc.; as this is up to the choice implementation.
 *
 * @author wstevens
 */
public interface Serializer
{
	/**
	 * Converts an <code>Object</code> into a stream of raw bytes.
	 *
	 * @param object The object to be serialized.
	 * @return byte[] The end result after object serialization.
	 */
	public <T> byte[] serialize(T object);

	/**
	 * Transforms a <code>byte[]</code> into a given <code>Object</code>.
	 *
	 * @param bytes The bytes to be transformed back into an <code>Object</code>.
	 * @return T The reassembled object.
	 */
	public <T> T deserialize(byte[] bytes);
}