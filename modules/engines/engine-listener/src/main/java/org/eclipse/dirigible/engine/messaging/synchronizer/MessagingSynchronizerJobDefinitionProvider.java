/**
 * Copyright (c) 2010-2020 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 */
package org.eclipse.dirigible.engine.messaging.synchronizer;

import org.eclipse.dirigible.commons.config.Configuration;
import org.eclipse.dirigible.core.scheduler.api.IJobDefinitionProvider;
import org.eclipse.dirigible.core.scheduler.api.ISchedulerCoreService;
import org.eclipse.dirigible.core.scheduler.service.definition.JobDefinition;

/**
 * The Class MessagingSynchronizerJobDefinitionProvider.
 */
public class MessagingSynchronizerJobDefinitionProvider implements IJobDefinitionProvider {

	private static final String DIRIGIBLE_JOB_EXPRESSION_MESSAGING = "DIRIGIBLE_JOB_EXPRESSION_MESSAGING";
	private static final String MESSAGING_SYNCHRONIZER_JOB = "Messaging Synchronizer Job";
	private static final String DIRIGIBLE_INTERNAL_MESSAGING_SYNCHRONIZER_JOB = "dirigible-internal-messaging-synchronizer-job";

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.core.scheduler.api.IJobDefinitionProvider#getJobDefinition()
	 */
	@Override
	public JobDefinition getJobDefinition() {
		JobDefinition jobDefinition = new JobDefinition();
		jobDefinition.setName(DIRIGIBLE_INTERNAL_MESSAGING_SYNCHRONIZER_JOB);
		jobDefinition.setGroup(ISchedulerCoreService.JOB_GROUP_INTERNAL);
		jobDefinition.setClazz(MessagingSynchronizerJob.class.getCanonicalName());
		jobDefinition.setDescription(MESSAGING_SYNCHRONIZER_JOB);
		String expression = Configuration.get(DIRIGIBLE_JOB_EXPRESSION_MESSAGING, "0/10 * * * * ?");
		jobDefinition.setExpression(expression);
		jobDefinition.setSingleton(true);
		return jobDefinition;
	}

}
