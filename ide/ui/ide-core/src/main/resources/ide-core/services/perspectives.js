/*
 * Copyright (c) 2010-2019 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 */
var extensions = require('core/v4/extensions');
var response = require('http/v4/response');

var perspectives = [];
var perspectiveExtensions = extensions.getExtensions('ide-perspective');

for (var i = 0; i < perspectiveExtensions.length; i++) {
    var module = perspectiveExtensions[i];
    try {
    	var perspectiveExtension = require(module);
    	var perspective = perspectiveExtension.getPerspective();
    	perspectives.push(perspective);
    } catch(error) {
    	console.error('Error occured while loading metadata for the perspective: ' + module);
    	console.error(error);
    }
    
}

perspectives.sort(function(p, n) {
	return (parseInt(p.order) - parseInt(n.order));
});

response.println(JSON.stringify(perspectives));
