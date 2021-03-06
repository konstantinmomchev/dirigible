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
package org.eclipse.dirigible.runtime.ide.generation.model.entity;

import java.util.List;

/**
 * Transport object for the Entity Data Model
 *
 */
public class EntityDataModelRoot {
	
	private List<EntityDataModelEntity> entities;

	/**
	 * Gets the entities
	 * 
	 * @return the entities
	 */
	public List<EntityDataModelEntity> getEntities() {
		return entities;
	}

	/**
	 * Sets the entities
	 * 
	 * @param entities the entities to set
	 */
	public void setEntities(List<EntityDataModelEntity> entities) {
		this.entities = entities;
	}

}
