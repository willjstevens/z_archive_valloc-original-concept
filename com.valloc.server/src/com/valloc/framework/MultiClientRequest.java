/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import java.util.HashMap;
import java.util.Map;

import com.valloc.Identifiable;
import com.valloc.domain.system.ClientNode;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public final class MultiClientRequest<C extends ClientNode> implements Identifiable<UniqueId>
{

	private final UniqueId requestId;
	private final Map<C, ClientTracker> clients = new HashMap<C, MultiClientRequest<C>.ClientTracker>();

	private final class ClientTracker {
		FrameworkRequest frameworkRequest;
		FrameworkResponse frameworkResponse;
		private ClientTracker(final FrameworkRequest frameworkRequest, final FrameworkResponse frameworkResponse) {
			this.frameworkRequest = frameworkRequest;
			this.frameworkResponse = frameworkResponse;
		}
	}

	public MultiClientRequest(final UniqueId requestId) {
		this.requestId = requestId;
	}

	@Override
	public UniqueId id() {
		return requestId;
	}

	public void addClient(final C client, final FrameworkRequest frameworkRequest, final FrameworkResponse frameworkResponse) {
		clients.put(client, new ClientTracker(frameworkRequest, frameworkResponse));
	}

	public void removeClient(final C client) {
		clients.remove(client);
	}

	public FrameworkRequest getClientFrameworkRequest(final C client) {
		return clients.get(client).frameworkRequest;
	}

	public FrameworkResponse getClientFrameworkResponse(final C client) {
		return clients.get(client).frameworkResponse;
	}
}
