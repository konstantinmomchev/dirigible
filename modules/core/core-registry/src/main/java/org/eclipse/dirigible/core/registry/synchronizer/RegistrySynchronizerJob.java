/**
 * Copyright (c) 2010-2018 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 */
package org.eclipse.dirigible.core.registry.synchronizer;

import org.eclipse.dirigible.commons.api.module.StaticInjector;
import org.eclipse.dirigible.core.scheduler.api.AbstractSynchronizerJob;
import org.eclipse.dirigible.core.scheduler.api.ISynchronizer;

public class RegistrySynchronizerJob extends AbstractSynchronizerJob {

	private RegistrySynchronizer synchronizer = StaticInjector.getInjector().getInstance(RegistrySynchronizer.class);

	@Override
	protected ISynchronizer getSynchronizer() {
		return synchronizer;
	}

}
