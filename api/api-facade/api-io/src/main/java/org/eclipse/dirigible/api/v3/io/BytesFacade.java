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
package org.eclipse.dirigible.api.v3.io;

import java.io.UnsupportedEncodingException;

public class BytesFacade {
	
	public static byte[] textToByteArray(String text) {
		byte[] bytes = text.getBytes();
		return bytes;
	}
	
	public static byte[] textToByteArray(String text, String charset) throws UnsupportedEncodingException {
		byte[] bytes = text.getBytes(charset);
		return bytes;
	}

}
