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
package org.eclipse.dirigible.engine.api.script;

import java.util.Arrays;

import org.eclipse.dirigible.repository.api.IEntityInformation;

/**
 * The Module.
 */
public class Module {

	private String path;

	private byte[] content;

	private IEntityInformation entityInformation;

	/**
	 * Instantiates a new module.
	 *
	 * @param path
	 *            the path
	 * @param content
	 *            the content
	 */
	public Module(String path, byte[] content) {
		this(path, content, null);
	}

	/**
	 * Instantiates a new module.
	 *
	 * @param path
	 *            the path
	 * @param content
	 *            the content
	 * @param entityInformation
	 *            the entity information
	 */
	public Module(String path, byte[] content, IEntityInformation entityInformation) {
		this.path = path;
		this.content = content;
		this.entityInformation = entityInformation;
	}

	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public byte[] getContent() {
		return Arrays.copyOf(content, content.length);
	}

	/**
	 * Gets the entity information.
	 *
	 * @return the entity information
	 */
	public IEntityInformation getEntityInformation() {
		return entityInformation;
	}
}
