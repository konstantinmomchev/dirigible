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
package org.eclipse.dirigible.engine.js.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.eclipse.dirigible.commons.api.scripting.ScriptingDependencyException;
import org.eclipse.dirigible.commons.api.service.AbstractRestService;
import org.eclipse.dirigible.commons.api.service.IRestService;
import org.eclipse.dirigible.engine.js.processor.JavascriptEngineProcessor;
import org.eclipse.dirigible.repository.api.RepositoryNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * Front facing REST service serving the Javascript backend services.
 */
@Singleton
@Path("/js")
@Api(value = "JavaScript Engine", authorizations = { @Authorization(value = "basicAuth", scopes = {}) })
@ApiResponses({ @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
		@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Internal Server Error") })
public class JavascriptEngineRestService extends AbstractRestService implements IRestService {

	private static final Logger logger = LoggerFactory.getLogger(JavascriptEngineRestService.class.getCanonicalName());

	@Inject
	private JavascriptEngineProcessor processor;

	@Context
	private HttpServletResponse response;

	/**
	 * Execute service.
	 *
	 * @param path
	 *            the path
	 * @return result of the execution of the service
	 */
	@GET
	@Path("/{path:.*}")
	@ApiOperation("Execute Server Side JavaScript Resource")
	@ApiResponses({ @ApiResponse(code = 200, message = "Execution Result") })
	public Response executeJavascriptServiceGet(@PathParam("path") String path) {
		try {
			processor.executeService(path);
			return Response.ok().build();
		} catch (ScriptingDependencyException e) {
			logger.error(e.getMessage());
			return Response.status(Response.Status.ACCEPTED).entity(e.getMessage()).build();
		} catch (RepositoryNotFoundException e) {
			String message = e.getMessage() + ". Try to publish the service before execution.";
			logger.error(message);
			return Response.status(Response.Status.ACCEPTED).entity(message).build();	
		} catch (Throwable e) {
			String message = e.getMessage();
			logger.error(message, e);
			sendErrorInternalServerError(response, message);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message).build();
		}
	}

	/**
	 * Execute service post.
	 *
	 * @param path
	 *            the path
	 * @return result of the execution of the service
	 */
	@POST
	@Path("/{path:.*}")
	@ApiOperation("Execute Server Side JavaScript Resource")
	@ApiResponses({ @ApiResponse(code = 200, message = "Execution Result") })
	public Response executeJavascriptServicePost(@PathParam("path") String path) {
		return executeJavascriptServiceGet(path);
	}

	/**
	 * Execute service put.
	 *
	 * @param path
	 *            the path
	 * @return result of the execution of the service
	 */
	@PUT
	@Path("/{path:.*}")
	@ApiOperation("Execute Server Side JavaScript Resource")
	@ApiResponses({ @ApiResponse(code = 200, message = "Execution Result") })
	public Response executeJavascriptServicePut(@PathParam("path") String path) {
		return executeJavascriptServiceGet(path);
	}

	/**
	 * Execute service delete.
	 *
	 * @param path
	 *            the path
	 * @return result of the execution of the service
	 */
	@DELETE
	@Path("/{path:.*}")
	@ApiOperation("Execute Server Side JavaScript Resource")
	@ApiResponses({ @ApiResponse(code = 200, message = "Execution Result") })
	public Response executeJavascriptServiceDelete(@PathParam("path") String path) {
		return executeJavascriptServiceGet(path);
	}

	/**
	 * Execute service head.
	 *
	 * @param path
	 *            the path
	 * @return result of the execution of the service
	 */
	@HEAD
	@Path("/{path:.*}")
	@ApiOperation("Execute Server Side JavaScript Resource")
	@ApiResponses({ @ApiResponse(code = 200, message = "Execution Result") })
	public Response executeJavascriptServiceHead(@PathParam("path") String path) {
		return executeJavascriptServiceGet(path);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.commons.api.service.IRestService#getType()
	 */
	@Override
	public Class<? extends IRestService> getType() {
		return JavascriptEngineRestService.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.commons.api.service.AbstractRestService#getLogger()
	 */
	@Override
	protected Logger getLogger() {
		return logger;
	}
}
