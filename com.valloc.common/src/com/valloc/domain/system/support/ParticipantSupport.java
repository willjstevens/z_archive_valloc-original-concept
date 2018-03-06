/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.domain.system.support;

import com.valloc.domain.DomainObjectSupport;
import com.valloc.domain.StringSerializer;
import com.valloc.domain.system.Participant;

/**
 *
 *
 * @author wstevens
 */
public class ParticipantSupport implements DomainObjectSupport<Participant>
{

	@Override
	public Class<Participant> getType() {
		return Participant.class;
	}

	@Override
	public void registerStringAliases(final StringSerializer serializer) {
		serializer.setClassAlias("participant", getType());
		
		serializer.setFieldAlias("name", getType(), "name");
		serializer.setFieldAlias("participant-type", getType(), "nodeType");
	}

	@Override
	public Participant toDto() {
		return null;
	}

}
