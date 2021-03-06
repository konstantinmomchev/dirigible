/**
 * Copyright (c) 2010-2019 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 */
package org.eclipse.dirigible.commons.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.dirigible.commons.config.HealthStatus.Jobs.JobStatus;

public class HealthStatus {

	private static final HealthStatus INSTANCE = new HealthStatus();

	public static HealthStatus getInstance() {
		return INSTANCE;
	}

	private static void setCurrentStatus() {
		HealthStatus healthStatus = getInstance();
		healthStatus.currentStatus = Status.Ready;
		for (JobStatus next : healthStatus.getJobs().getJobsStatuses()) {
			if (next.equals(JobStatus.Running)) {
				healthStatus.currentStatus = Status.Running;
				break;
			} else if (next.equals(JobStatus.Failed)) {
				healthStatus.currentStatus = Status.NotReady;
				break;
			}
		}
		if (!healthStatus.status.equals(Status.Ready) && healthStatus.currentStatus.equals(Status.Ready)) {
			healthStatus.status = Status.Ready;
		}
	}

	public enum Status {
		Ready, NotReady, Running
	}

	private Status status = Status.NotReady;
	private Status currentStatus = Status.NotReady;
	private Jobs jobs = new Jobs();

	private HealthStatus() {
	
	}

	public Status getStatus() {
		return status;
	}

	public Status getCurrentStatus() {
		return currentStatus;
	}

	public Jobs getJobs() {
		return jobs;
	}


	public static class Jobs {
		public enum JobStatus {
			Running, Succeeded, Failed 
		}

		private Map<String, JobStatus> statuses = new HashMap<String, JobStatus>();


		public Map<String, JobStatus> getStatuses() {
			return statuses;
		}

		public void setStatus(String name, JobStatus status) {
			statuses.put(name, status);
			setCurrentStatus();
		}

		private Collection<JobStatus> getJobsStatuses() {
			return statuses.values();
		}

	}
}
