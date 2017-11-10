/*
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 */

package org.eclipse.dirigible.core.messaging.service;

import static java.text.MessageFormat.format;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

import org.eclipse.dirigible.api.v3.security.UserFacade;
import org.eclipse.dirigible.commons.api.helpers.GsonHelper;
import org.eclipse.dirigible.core.messaging.api.DestinationType;
import org.eclipse.dirigible.core.messaging.api.IMessagingCoreService;
import org.eclipse.dirigible.core.messaging.api.MessagingException;
import org.eclipse.dirigible.core.messaging.definition.ListenerDefinition;
import org.eclipse.dirigible.database.persistence.PersistenceManager;
import org.eclipse.dirigible.database.sql.SqlFactory;

/**
 * The Class MessagingCoreService.
 */
@Singleton
public class MessagingCoreService implements IMessagingCoreService {

	@Inject
	private DataSource dataSource;

	@Inject
	private PersistenceManager<ListenerDefinition> listenerPersistenceManager;

	// Listener

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.core.messaging.api.IMessagingCoreService#createListener(java.lang.String,
	 * java.lang.String, org.eclipse.dirigible.core.messaging.api.DestinationType, java.lang.String, java.lang.String)
	 */
	@Override
	public ListenerDefinition createListener(String location, String name, DestinationType type, String module, String description)
			throws MessagingException {
		ListenerDefinition listenerDefinition = new ListenerDefinition();
		listenerDefinition.setLocation(location);
		listenerDefinition.setName(name);
		listenerDefinition.setType(new Integer(type.ordinal()).byteValue());
		listenerDefinition.setModule(module);
		listenerDefinition.setDescription(description);
		listenerDefinition.setCreatedBy(UserFacade.getName());
		listenerDefinition.setCreatedAt(new Timestamp(new java.util.Date().getTime()));

		try {
			Connection connection = dataSource.getConnection();
			try {
				listenerPersistenceManager.insert(connection, listenerDefinition);
				return listenerDefinition;
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (SQLException e) {
			throw new MessagingException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.core.messaging.api.IMessagingCoreService#getListener(java.lang.String)
	 */
	@Override
	public ListenerDefinition getListener(String location) throws MessagingException {
		try {
			Connection connection = dataSource.getConnection();
			try {
				return listenerPersistenceManager.find(connection, ListenerDefinition.class, location);
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (SQLException e) {
			throw new MessagingException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.core.messaging.api.IMessagingCoreService#getListenerByName(java.lang.String)
	 */
	@Override
	public ListenerDefinition getListenerByName(String name) throws MessagingException {
		try {
			Connection connection = dataSource.getConnection();
			try {
				String sql = SqlFactory.getNative(connection).select().column("*").from("DIRIGIBLE_LISTENERS").where("LISTENER_NAME = ?").toString();
				List<ListenerDefinition> listenerDefinitions = listenerPersistenceManager.query(connection, ListenerDefinition.class, sql,
						Arrays.asList(name));
				if (listenerDefinitions.isEmpty()) {
					return null;
				}
				if (listenerDefinitions.size() > 1) {
					throw new MessagingException(format("There are more that one Listeners with the same name [{0}] at locations: [{1}] and [{2}].",
							name, listenerDefinitions.get(0).getLocation(), listenerDefinitions.get(1).getLocation()));
				}
				return listenerDefinitions.get(0);
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (SQLException e) {
			throw new MessagingException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.core.messaging.api.IMessagingCoreService#removeListener(java.lang.String)
	 */
	@Override
	public void removeListener(String location) throws MessagingException {
		try {
			Connection connection = dataSource.getConnection();
			try {
				listenerPersistenceManager.delete(connection, ListenerDefinition.class, location);
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (SQLException e) {
			throw new MessagingException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.core.messaging.api.IMessagingCoreService#updateListener(java.lang.String,
	 * java.lang.String, org.eclipse.dirigible.core.messaging.api.DestinationType, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateListener(String location, String name, DestinationType type, String module, String description) throws MessagingException {
		try {
			Connection connection = dataSource.getConnection();
			try {
				ListenerDefinition listenerDefinition = getListener(location);
				listenerDefinition.setName(name);
				listenerDefinition.setType(new Integer(type.ordinal()).byteValue());
				listenerDefinition.setModule(module);
				listenerDefinition.setDescription(description);
				listenerPersistenceManager.update(connection, listenerDefinition, location);
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (SQLException e) {
			throw new MessagingException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.core.messaging.api.IMessagingCoreService#getListeners()
	 */
	@Override
	public List<ListenerDefinition> getListeners() throws MessagingException {
		try {
			Connection connection = dataSource.getConnection();
			try {
				return listenerPersistenceManager.findAll(connection, ListenerDefinition.class);
			} finally {
				if (connection != null) {
					connection.close();
				}
			}
		} catch (SQLException e) {
			throw new MessagingException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.core.messaging.api.IMessagingCoreService#existsListener(java.lang.String)
	 */
	@Override
	public boolean existsListener(String location) throws MessagingException {
		return getListener(location) != null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.core.messaging.api.IMessagingCoreService#parseListener(java.lang.String)
	 */
	@Override
	public ListenerDefinition parseListener(String json) {
		return GsonHelper.GSON.fromJson(json, ListenerDefinition.class);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.core.messaging.api.IMessagingCoreService#parseListener(byte[])
	 */
	@Override
	public ListenerDefinition parseListener(byte[] json) {
		return GsonHelper.GSON.fromJson(new InputStreamReader(new ByteArrayInputStream(json), StandardCharsets.UTF_8), ListenerDefinition.class);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.core.messaging.api.IMessagingCoreService#serializeListener(org.eclipse.dirigible.core.
	 * messaging.definition.ListenerDefinition)
	 */
	@Override
	public String serializeListener(ListenerDefinition listenerDefinition) {
		return GsonHelper.GSON.toJson(listenerDefinition);
	}

}
