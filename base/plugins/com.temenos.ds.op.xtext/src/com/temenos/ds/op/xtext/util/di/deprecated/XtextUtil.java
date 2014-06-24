/*******************************************************************************
 * Copyright (c) 2014 Michael Vorburger.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Michael Vorburger - initial API and implementation
 ******************************************************************************/
package com.temenos.ds.op.xtext.util.di.deprecated;

import org.eclipse.emf.ecore.EObject;

import com.temenos.ds.op.xtext.util.XtextProxyUtil;

/**
 * Utilities for Xtext.
 * 
 * @author Michael Vorburger
 */
public class XtextUtil {

	private static final XtextInjectProvider<XtextProxyUtil> xTextProxyUtilProvider = XtextInjectProviderFactory.getProvider(XtextProxyUtil.class);
	
	/**
	 * @see XtextUtil#getProxyCrossRefAsString(EObject)
	 */
	public static String getProxyCrossRefAsString(EObject context, EObject proxy) {
		XtextProxyUtil proxyUtil = xTextProxyUtilProvider.get(context.eResource().getURI());
		return proxyUtil.getProxyCrossRefAsString(context, proxy);
	}

}
