/*******************************************************************************
 * Copyright (c) 2016 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 *******************************************************************************/

package org.eclipse.dirigible.ide.template.ui.js.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dirigible.ide.common.status.LogProgressMonitor;
import org.eclipse.dirigible.ide.template.ui.common.GenerationException;
import org.eclipse.dirigible.ide.template.ui.common.TemplateType;
import org.eclipse.dirigible.ide.template.ui.common.TemplateTypesEnumerator;
import org.eclipse.dirigible.ide.template.ui.common.service.AbstractGenerationWorker;
import org.eclipse.dirigible.ide.template.ui.js.wizard.JavascriptServiceTemplateGenerator;
import org.eclipse.dirigible.ide.template.ui.js.wizard.JavascriptServiceTemplateModel;
import org.eclipse.dirigible.ide.template.ui.js.wizard.TableColumn;
import org.eclipse.dirigible.repository.api.ICommonConstants;
import org.eclipse.dirigible.repository.api.IRepository;
import org.eclipse.dirigible.repository.datasource.DBSupportedTypesMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ScriptingGenerationWorker extends AbstractGenerationWorker {

	private static final String PARAM_COLUMN_VISIBLE = "visible";
	private static final String PARAM_COLUMN_PRIMARY_KEY = "primaryKey";
	private static final String PARAM_COLUMN_TYPE = "type";
	private static final String PARAM_COLUMN_NAME = "name";
	private static final String PARAM_TABLE_TYPE = "tableType";
	private static final String PARAM_TABLE_NAME = "tableName";
	private static final String PARAM_COLUMNS = "columns";

	public ScriptingGenerationWorker(IRepository repository, IWorkspace workspace) {
		super(repository, workspace);
	}

	@Override
	public String generate(String parameters, HttpServletRequest request) throws GenerationException {
		try {
			JsonElement parametersElement = new JsonParser().parse(parameters);
			JsonObject parametersObject = parametersElement.getAsJsonObject();

			JavascriptServiceTemplateModel model = new JavascriptServiceTemplateModel();
			JavascriptServiceTemplateGenerator generator = new JavascriptServiceTemplateGenerator(model);

			TemplateType[] templates = TemplateTypesEnumerator.prepareTemplateTypes(ScriptingServiceTemplateTypeDiscriminator.getTemplatesPath(),
					ScriptingServiceTemplateTypeDiscriminator.getCategory(), request);

			setParametersToModel(parametersObject, model, templates);

			generator.generate(request);

		} catch (Exception e) {
			throw new GenerationException(e);
		}
		return GENERATION_PASSED_SUCCESSFULLY;
	}

	protected void setParametersToModel(JsonObject parametersObject, JavascriptServiceTemplateModel model, TemplateType[] templates)
			throws GenerationException {

		// template type
		if (parametersObject.has(PARAM_TEMPLATE_TYPE)) {
			String templateType = parametersObject.get(PARAM_TEMPLATE_TYPE).getAsString();
			for (TemplateType template : templates) {
				if (template.getLocation().substring(ScriptingServiceTemplateTypeDiscriminator.getTemplatesPath().length())
						.indexOf(templateType + ICommonConstants.SEPARATOR) == 0) {
					model.setTemplate(template);
					break;
				}
			}
			if (model.getTemplate() == null) {
				throw new GenerationException(String.format(TEMPLATE_S_DOES_NOT_EXIST_IN_THIS_INSTANCE, templateType));
			}
		} else {
			throw new GenerationException(String.format(MANDATORY_PARAMETER_S_HAS_NOT_BEEN_PROVIDED, PARAM_TEMPLATE_TYPE));
		}

		// file name
		if (parametersObject.has(PARAM_FILE_NAME)) {
			model.setFileName(parametersObject.get(PARAM_FILE_NAME).getAsString());
		} else {
			throw new GenerationException(String.format(MANDATORY_PARAMETER_S_HAS_NOT_BEEN_PROVIDED, PARAM_FILE_NAME));
		}

		// project name
		if (parametersObject.has(PARAM_PROJECT_NAME)) {
			String projectName = parametersObject.get(PARAM_PROJECT_NAME).getAsString();
			IProject project = getWorkspace().getRoot().getProject(projectName);
			if (!project.exists()) {
				try {
					project.create(new LogProgressMonitor());
				} catch (CoreException e) {
					throw new GenerationException(e);
				}
			}
			model.setTargetContainer(project.getFullPath().toString());
		} else {
			throw new GenerationException(String.format(MANDATORY_PARAMETER_S_HAS_NOT_BEEN_PROVIDED, PARAM_PROJECT_NAME));
		}

		// package name
		if (parametersObject.has(PARAM_PACKAGE_NAME)) {
			String packageName = parametersObject.get(PARAM_PACKAGE_NAME).getAsString();
			model.setProjectPackageName(ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES + IRepository.SEPARATOR + packageName);
			model.setPackageName(packageName);
		} else {
			throw new GenerationException(String.format(MANDATORY_PARAMETER_S_HAS_NOT_BEEN_PROVIDED, PARAM_PACKAGE_NAME));
		}

		// columns
		if (parametersObject.has(PARAM_COLUMNS)) {
			List<TableColumn> columnDefinitions = new ArrayList<TableColumn>();
			JsonArray columns = parametersObject.get(PARAM_COLUMNS).getAsJsonArray();
			Iterator<JsonElement> iter = columns.iterator();
			while (iter.hasNext()) {
				JsonElement columnElement = iter.next();
				if (columnElement.isJsonObject()) {
					JsonObject columnObject = columnElement.getAsJsonObject();
					TableColumn columnDefinition = new TableColumn(columnObject.get(PARAM_COLUMN_NAME).getAsString(),
							DBSupportedTypesMap.getTypeNumber(columnObject.get(PARAM_COLUMN_TYPE).getAsString()),
							columnObject.get(PARAM_COLUMN_PRIMARY_KEY).getAsBoolean(), columnObject.get(PARAM_COLUMN_VISIBLE).getAsBoolean());
					columnDefinitions.add(columnDefinition);
				}
			}

			model.setTableColumns(columnDefinitions.toArray(new TableColumn[] {}));
		} else {
			checkIfRequired(model, PARAM_COLUMNS);
		}

		// table name
		if (parametersObject.has(PARAM_TABLE_NAME)) {
			model.setTableName(parametersObject.get(PARAM_TABLE_NAME).getAsString());
		} else {
			checkIfRequired(model, PARAM_TABLE_NAME);
		}

		// table type
		if (parametersObject.has(PARAM_TABLE_TYPE)) {
			model.setTableType(parametersObject.get(PARAM_TABLE_TYPE).getAsString());
		} else {
			checkIfRequired(model, PARAM_TABLE_TYPE);
		}

	}

	@Override
	public String getTemplates(HttpServletRequest request) throws GenerationException {
		try {
			TemplateType[] templates = TemplateTypesEnumerator.prepareTemplateTypes(ScriptingServiceTemplateTypeDiscriminator.getTemplatesPath(),
					ScriptingServiceTemplateTypeDiscriminator.getCategory(), request);
			String result = new Gson().toJson(templates);
			return result;
		} catch (Exception e) {
			throw new GenerationException(e);
		}
	}

}
