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
package com.temenos.ds.op.xtext.ui.internal;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import com.google.inject.AbstractModule;

/**
 * Guice Module binding Eclipse IExtensionRegistry etc.
 * 
 * @author Michael Vorburger
 */
public class NODslEclipseModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IExtensionRegistry.class).toInstance(Platform.getExtensionRegistry());
	}

}
