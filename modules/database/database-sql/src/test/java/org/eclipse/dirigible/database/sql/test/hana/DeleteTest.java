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
package org.eclipse.dirigible.database.sql.test.hana;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.dirigible.database.sql.SqlFactory;
import org.eclipse.dirigible.database.sql.dialects.hana.HanaSqlDialect;
import org.junit.Test;

// TODO: Auto-generated Javadoc
/**
 * The Class DeleteTest.
 */
public class DeleteTest {
	
	/**
	 * Delete simple.
	 */
	@Test
	public void deleteSimple() {
		String sql = SqlFactory.getNative(new HanaSqlDialect())
			.delete()
			.from("CUSTOMERS")
			.build();
		
		assertNotNull(sql);
		assertEquals("DELETE FROM CUSTOMERS", sql);
	}

	/**
	 * Delete where.
	 */
	@Test
	public void deleteWhere() {
		String sql = SqlFactory.getNative(new HanaSqlDialect())
				.delete()
				.from("CUSTOMERS")
				.where("AGE > ?")
				.where("COMPANY = 'SAP'")
				.build();
			
			assertNotNull(sql);
			assertEquals("DELETE FROM CUSTOMERS WHERE (AGE > ?) AND (COMPANY = 'SAP')", sql);
	}

}
