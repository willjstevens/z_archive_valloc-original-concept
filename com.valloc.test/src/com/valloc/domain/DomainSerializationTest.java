/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.valloc.AbstractTest;
import com.valloc.domain.system.Agent;
import com.valloc.domain.system.Desktop;
import com.valloc.domain.system.HostConfig;
import com.valloc.domain.system.ProxyHostConfig;
import com.valloc.domain.system.Server;
import com.valloc.domain.system.ServerHostConfig;

/**
 * 
 *
 * @author wstevens
 */
public final class DomainSerializationTest extends AbstractTest
{	
	private MockDomainObjectFactory mockDomainObjectFactory;
	private StringSerializer serializer;
	
	@Before
	public void setUp() throws Exception
	{
		mockDomainObjectFactory = new MockDomainObjectFactory();
		serializer = SerializerFactory.getSerializerFactory().getStringSerializer();
	}

	@Test
	public void basicSerializeDeserialize()
	{
		// HostConfig test:
		final HostConfig hostConfigExpected = mockDomainObjectFactory.newHostConfig();
		final byte[] hostConfigBytes = serializer.serialize(hostConfigExpected);		
		final HostConfig hostConfigActual = serializer.deserialize(hostConfigBytes);		
		Assert.assertEquals(hostConfigExpected.getInetSocketAddress(), hostConfigActual.getInetSocketAddress());
		Assert.assertArrayEquals(hostConfigExpected.getMacAddress(), hostConfigActual.getMacAddress());

		// ServerHostConfig test:
		final ServerHostConfig serverHostConfigExpected = mockDomainObjectFactory.newServerHostConfig();
		final byte[] serverHostConfigBytes = serializer.serialize(serverHostConfigExpected);		
		final ServerHostConfig serverHostConfigActual = serializer.deserialize(serverHostConfigBytes);		
		Assert.assertEquals(serverHostConfigExpected.getInetSocketAddress(), serverHostConfigActual.getInetSocketAddress());
		Assert.assertEquals(serverHostConfigExpected.getPort(), serverHostConfigActual.getPort());
		Assert.assertArrayEquals(serverHostConfigExpected.getMacAddress(), serverHostConfigActual.getMacAddress());

		// ProxyHostConfig test:
		final ProxyHostConfig proxyHostConfigExpected = mockDomainObjectFactory.newProxyHostConfig();
		final byte[] proxyHostConfigBytes = serializer.serialize(proxyHostConfigExpected);		
		final ProxyHostConfig proxyHostConfigActual = serializer.deserialize(proxyHostConfigBytes);		
		Assert.assertEquals(proxyHostConfigExpected.getInetSocketAddress(), proxyHostConfigActual.getInetSocketAddress());
		Assert.assertEquals(proxyHostConfigExpected.getPort(), proxyHostConfigActual.getPort());
		Assert.assertEquals(proxyHostConfigExpected.getUsername(), proxyHostConfigActual.getUsername());
		Assert.assertArrayEquals(proxyHostConfigExpected.getPassword(), proxyHostConfigActual.getPassword());
		Assert.assertArrayEquals(proxyHostConfigExpected.getMacAddress(), proxyHostConfigActual.getMacAddress());
		
		// Desktop test:
		final Desktop desktopExpected = mockDomainObjectFactory.newDesktop();
		final byte[] desktopBytes = serializer.serialize(desktopExpected);		
		final Desktop desktopActual = serializer.deserialize(desktopBytes);		
		Assert.assertEquals(desktopExpected.getName(), desktopActual.getName());
		Assert.assertEquals(desktopExpected.getNodeType(), desktopActual.getNodeType());
		
		// Server test:
		final Server serverExpected = mockDomainObjectFactory.newServer();
		final byte[] serverBytes = serializer.serialize(serverExpected);		
		final Server serverActual = serializer.deserialize(serverBytes);		
		Assert.assertEquals(serverExpected.getName(), serverActual.getName());
		Assert.assertEquals(serverExpected.getNodeType(), serverActual.getNodeType());
		Assert.assertEquals(serverExpected.getServerHostConfig().getPort(), serverActual.getServerHostConfig().getPort());

		// Agent test:
		final Agent agentExpected = mockDomainObjectFactory.newAgent();
		final byte[] agentBytes = serializer.serialize(agentExpected);		
		final Agent agentActual = serializer.deserialize(agentBytes);		
		Assert.assertEquals(agentExpected.getName(), agentActual.getName());
		Assert.assertEquals(agentExpected.getNodeType(), agentActual.getNodeType());
		Assert.assertArrayEquals(agentExpected.getHostConfig().getMacAddress(), agentActual.getHostConfig().getMacAddress());

		
	}
}