/*
 * Copyright (c) 2010-2020 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 */
exports.getTemplate = function() {
	var view = {
			"name":"database-view",
			"label":"Database View",
			"extension":"view",
			"data":'{"name":"MYVIEW","type":"VIEW","query":"SELECT * FROM MYTABLE","dependencies":[{"name":"MYTABLE","type":"TABLE"}]}'
	};
	return view;
};
