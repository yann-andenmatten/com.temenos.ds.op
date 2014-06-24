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

import org.eclipse.xtext.resource.IResourceServiceProvider;

/**
 * Util for Xtext's Guice-based Dependence Injection.
 * 
 * You should try to minimize use of this util.. this only exists for code which
 * isn't fully DI, yet. Normally, this should not be needed anymore. If you use
 * it more than once in a class, it's a good indication that you are doing
 * something wrong and didn't completely understand DI yet. Ask for help.
 * 
 * If you are in DI and need something language specific, using @Inject
 * XtextInjectProvider instead of this is what you want to do.
 * 
 * @author Michael Vorburger
 */
public class XtextInjectProviderFactory {

	public static <T> XtextInjectProvider<T> getProvider(Class<T> clazz) {
		XtextInjectProvider<T> p = new XtextInjectProvider<T>(clazz);
		p.registry = IResourceServiceProvider.Registry.INSTANCE;
		return p;
	}

}