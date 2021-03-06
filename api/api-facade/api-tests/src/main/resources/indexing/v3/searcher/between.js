/*
 * Copyright (c) 2010-2018 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 */
var writer = require('indexing/v3/writer');
var searcher = require('indexing/v3/searcher');

writer.add("index2", "myfile1", "apache lucene", new Date(123));
writer.add("index2", "myfile2", "lucene - the search engine", new Date(234), {"name2":"value2"});
writer.add("index2", "myfile3", "search engine", new Date(345), {"name2":"value2"});

var found = searcher.between("index2", new Date(124), new Date(344));

console.log(JSON.stringify(found));

((found !== null) && (found !== undefined) && found.length === 1);