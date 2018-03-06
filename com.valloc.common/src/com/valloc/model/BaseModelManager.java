/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author wstevens
 */
public class BaseModelManager implements ModelManager
{
	private final Map<String, ParticipantModelRepository> participantModels = new HashMap<String, ParticipantModelRepository>();
	private ParticipantModelRepository selfRepository;
	private ModelComponentFactory modelComponentFactory;

	/* (non-Javadoc)
	 * @see com.valloc.lifecycle.Initializable#initialize()
	 */
	@Override
	public void initialize() {
	}

	/*
	 * (non-Javadoc)
	 * @see com.valloc.model.ModelManager#setModelComponentFactory(com.valloc.model.ModelComponentFactory)
	 */
	@Override
	public void setModelComponentFactory(final ModelComponentFactory modelComponentFactory) {
		this.modelComponentFactory = modelComponentFactory;
	}

	/* (non-Javadoc)
	 * @see com.valloc.model.ModelManager#addParticipantModelRepository(com.valloc.model.ParticipantModelRepository)
	 */
	@Override
	public void addParticipantModelRepository(final ParticipantModelRepository participantModelRepository) {
		participantModels.put(participantModelRepository.name(), participantModelRepository);
	}

	/* (non-Javadoc)
	 * @see com.valloc.model.ModelManager#getParticipantModelRepository(com.valloc.model.DistributedModelObjectId)
	 */
	@Override
	public ParticipantModelRepository getParticipantModelRepository(final DistributedModelObjectId distributedId) {
		final String repositoryId = ParticipantModelRepository.createId(distributedId.getParticipantType(), distributedId.getParticipantId());
		return participantModels.get(repositoryId);
	}

	/* (non-Javadoc)
	 * @see com.valloc.model.ModelManager#removeParticipantModelRepository(com.valloc.model.ParticipantModelRepository)
	 */
	@Override
	public void removeParticipantModelRepository(final ParticipantModelRepository participantModelRepository) {
		participantModels.remove(participantModelRepository.name());
	}

}
