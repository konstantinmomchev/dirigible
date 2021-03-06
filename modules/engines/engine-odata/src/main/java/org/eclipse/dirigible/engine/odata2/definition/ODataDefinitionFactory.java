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
package org.eclipse.dirigible.engine.odata2.definition;

import java.sql.Timestamp;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.dirigible.api.v3.security.UserFacade;
import org.eclipse.dirigible.commons.api.helpers.GsonHelper;

public class ODataDefinitionFactory {
	
	public static ODataDefinition parseOData(String contentPath, String data) {
		ODataDefinition odataDefinition = GsonHelper.GSON.fromJson(data, ODataDefinition.class);
		odataDefinition.setLocation(contentPath);
		odataDefinition.setHash(DigestUtils.md5Hex(data));
		odataDefinition.setCreatedBy(UserFacade.getName());
		odataDefinition.setCreatedAt(new Timestamp(new java.util.Date().getTime()));
		return odataDefinition;
	}

}
