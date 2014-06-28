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

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.DefaultUiModule;

import com.google.inject.Binder;
import com.google.inject.Module;

public class NODslUiModule extends DefaultUiModule {

	public NODslUiModule(AbstractUIPlugin plugin) {
		super(plugin);
	}

	public com.google.inject.Provider<org.eclipse.xtext.resource.containers.IAllContainersState> provideIAllContainersState() {
		return org.eclipse.xtext.ui.shared.Access.getJavaProjectsState();
	}

}
