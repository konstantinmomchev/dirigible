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
package org.eclipse.dirigible.repository.db.dao;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The Database File Content Definition
 */
@Table(name = "DIRIGIBLE_FILES_CONTENT")
public class DatabaseFileContentDefinition {

	/** The path. */
	@Id
	@Column(name = "FILE_PATH", columnDefinition = "VARCHAR", nullable = false, length = 255)
	private String path;

	/** The content. */
	@Column(name = "FILE_CONTENT", columnDefinition = "BLOB", nullable = true)
	private byte[] content;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public byte[] getContent() {
		return content != null ? content.clone() : new byte[] {};
	}

	public void setContent(byte[] content) {
		this.content = content != null ? content.clone() : null;
	}

}
