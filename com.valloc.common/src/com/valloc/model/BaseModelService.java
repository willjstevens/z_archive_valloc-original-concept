/*
 * Property of Will Stevens
 * All rights reserved.
 */
package com.valloc.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Queue;

import com.valloc.Command;
import com.valloc.model.dto.ModelDto;
import com.valloc.service.AbstractCommonService;
import com.valloc.service.ServiceDirectory;

/**
 *
 *
 * @author wstevens
 */
public class BaseModelService extends AbstractCommonService implements ModelService
{
	private ModelManager modelManager;
	private ModelComponentFactory modelComponentFactory;

	@Override
	public String name() {
		return ServiceDirectory.MODEL;
	}

	/* (non-Javadoc)
	 * @see com.valloc.model.ModelService#publicModelDtos(java.util.Queue)
	 */
	@Override
	public void publishModelDtos(final Queue<ModelDto> modelDtos) {
		// For each ModelDto object, do the following steps
		for (final ModelDto modelDto : modelDtos) {
			final String daoId = modelDto.getDaoId();
			final String commandId = modelDto.getCommandId();
			final ModelDao modelDao = modelComponentFactory.newModelDao(daoId);
			Method targetDaoMethod = null;
			// 1) Determine handling DAO to invoke:
			search: for (final Method daoMethod : modelDao.getClass().getMethods()) {
				for (final Annotation methodAnnotation : daoMethod.getAnnotations()) {
					final Class<? extends Annotation> annoClazz = methodAnnotation.annotationType();
					if (annoClazz.equals(Command.class)) {
						final Command candidateAnnotation = (Command) methodAnnotation;
						final String candidateAnnoName = candidateAnnotation.name();
						if (commandId.equals(candidateAnnoName)) {
							targetDaoMethod = daoMethod;
							break search;
						}
					}
				}
			}
			// 2) Determine and set the corresponding ParticipantModelRepository object the object corresponds to:
			final ParticipantModelRepository modelRepository = modelManager.getParticipantModelRepository(modelDto.id());
			modelDao.setParticipantModelRepository(modelRepository);
			// 3) Invoke the DAO to handle the DTO update
			try {
				final Object[] args = { modelDto };
				targetDaoMethod.invoke(modelDao, args);
			} catch (final IllegalArgumentException e) {
				e.printStackTrace();
			} catch (final IllegalAccessException e) {
				e.printStackTrace();
			} catch (final InvocationTargetException e) {
				e.printStackTrace();
			}
		}

	}

	public void setModelManager(final ModelManager modelManager) {
		this.modelManager = modelManager;
	}

	public void setModelComponentFactory(final ModelComponentFactory modelComponentFactory) {
		this.modelComponentFactory = modelComponentFactory;
	}

	//	@Override
	//	public void handleInterrupt() {
	//	}
}
