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
var extensions = require('core/v4/extensions');
var response = require('http/v4/response');

var rs = require("http/v4/rs");

rs.service()
    .resource("")
        .get(function(ctx, request, response) {
            var templates = getTemplates();
			response.println(JSON.stringify(templates));
        })
    .resource("extensions")
        .get(function(ctx, request, response) {
    	    var templates = getTemplates();
    	    var fileExtensions = [];
    	    templates.forEach(template => {if (template.extension) fileExtensions.push(template.extension);});
			response.println(JSON.stringify(fileExtensions));
    })
.execute();

function getTemplates() {
	var templates = [];
	var templateExtensions = extensions.getExtensions('ide-template');
	for (var i = 0; i < templateExtensions.length; i++) {
	    var module = templateExtensions[i];
	    try {
	    	var templateExtension = require(module);
	    	var template = templateExtension.getTemplate();
	    	template.id = module;
	    	templates.push(template);	
	    } catch(error) {
	    	console.error('Error occured while loading metadata for the template: ' + module);
	    	console.error(error);
	    }
	}
	return templates;
}
