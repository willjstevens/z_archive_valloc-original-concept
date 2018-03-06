/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.model;

/**
 *
 *
 * @author wstevens
 */
public abstract class AbstractModelDao implements ModelDao
{
	private ModelManager modelManager;
	private ParticipantModelRepository participantModelRepository;
	
	/* (non-Javadoc)
	 * @see com.valloc.model.ModelDao#getParticipantModelRepository()
	 */
	@Override
	public ParticipantModelRepository getParticipantModelRepository() {
		return participantModelRepository;
	}

	/* (non-Javadoc)
	 * @see com.valloc.model.ModelDao#setParticipantModelRepository(com.valloc.model.ParticipantModelRepository)
	 */
	@Override
	public void setParticipantModelRepository(final ParticipantModelRepository participantModelRepository) {
		this.participantModelRepository = participantModelRepository;
	}

	@Override
	public ModelManager getModelManager() {
		return modelManager;
	}

	@Override
	public void setModelManager(final ModelManager modelManager) {
		this.modelManager = modelManager;
	}
}
