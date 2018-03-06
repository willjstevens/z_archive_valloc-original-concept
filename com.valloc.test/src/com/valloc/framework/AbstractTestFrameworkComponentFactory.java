/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.framework;

import com.valloc.Priority;
import com.valloc.domain.MockDomainObjectFactory;
import com.valloc.lifecycle.Bootstrapable;
import com.valloc.lifecycle.Initializable;
import com.valloc.util.UniqueId;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractTestFrameworkComponentFactory implements Bootstrapable, Initializable
{
	private MockDomainObjectFactory mockDomainObjectFactory;

	public FrameworkRequest newCannedWhateverFrameworkRequest() {
		final UniqueId requestId = new UniqueId();
		final FrameworkRequest request =
			new FrameworkRequest(
					requestId,
					NodeType.DESKTOP,
					"bogus-serviceName",
					"bogus-commandName",
					CallType.REMOTE_SYNCHRONOUS,
					Priority.USER_STANDARD,
					requestId.getInceptionTimestamp());
		return request;
	}

	public FrameworkResponse newCannedWhateverFrameworkResponse(final FrameworkRequest request) {
		return new FrameworkResponse(request.id());
	}

	public MockDomainObjectFactory getMockDomainObjectFactory() {
		return mockDomainObjectFactory;
	}

	public void setDomainObjectFactory(final MockDomainObjectFactory mockDomainObjectFactory) {
		this.mockDomainObjectFactory = mockDomainObjectFactory;
	}


}
