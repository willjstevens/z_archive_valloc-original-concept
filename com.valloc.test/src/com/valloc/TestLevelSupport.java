/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc;

/**
 *
 * Taken from http://martinfowler.com/articles/mocksArentStubs.html
 *
 * Meszaros uses the term Test Double as the generic term for any kind of pretend object used in place of a real object 
 * for testing purposes. The name comes from the notion of a Stunt Double in movies. (One of his aims was to avoid using any name that was already widely used.) Meszaros then defined four particular kinds of double:
 *
 *	Dummy - objects are passed around but never actually used. Usually they are just used to fill parameter lists.
 *	Fake - objects actually have working implementations, but usually take some shortcut which makes them not suitable for production (an in memory database is a good example).
 *	Stubs - provide canned answers to calls made during the test, usually not responding at all to anything outside what's programmed in for the test. Stubs may also record information about calls, such as an email gateway stub that remembers the messages it 'sent', or maybe only how many messages it 'sent'.
 *	Mocks - are what we are talking about here: objects pre-programmed with expectations which form a specification of the calls they are expected to receive.
 *
 * @author wstevens
 */
public enum TestLevelSupport
{
	FULL, 	// Full functioning production functionality support.
	DUMMY, 	// Mostly hollow implementations to avoid NPEs and also a 'dead end' side for the real subject code being tested.
	FAKE,	// Contains some interworking functionality - enough to respond back to subject code.
	STUB,	// Can provide full responses back and substituting a more complex environment component.
	CUSTOM	// An implementation passed in which might inherit/override from an existing to provide desired behavior for test.
}
