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

import java.util.Collections;
import java.util.Map;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.shared.SharedStateModule;
import org.eclipse.xtext.util.Modules2;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Activator.
 * 
 * @author Michael Vorburger
 */
public class NODslActivator extends AbstractUIPlugin {
	private static final Logger logger = LoggerFactory.getLogger(NODslActivator.class);
	
	private static NODslActivator INSTANCE;
	
	private Injector injector;	
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		injector = null;
		INSTANCE = null;
		super.stop(context);
	}
	
	public static NODslActivator getInstance() {
		return INSTANCE;
	}
	
	public Injector getInjector() {
		synchronized (injector) {
			if (injector == null) {
				injector = createInjector();
			}
			return injector;
		}
	}
	
	protected Injector createInjector() {
		try {
			Module runtimeModule = getRuntimeModule();
			Module sharedStateModule = getSharedStateModule();
			Module uiModule = getUiModule();
			Module mergedModule = Modules2.mixin(runtimeModule, sharedStateModule, uiModule);
			return Guice.createInjector(mergedModule);
		} catch (Exception e) {
			logger.error("Failed to create injector: " + e.getMessage(), e);
			throw new RuntimeException("Failed to create injector", e);
		}
	}

	protected Module getRuntimeModule() {
		return new NODslRuntimeModule();
	}
	
	protected Module getUiModule() {
		return new NODslUiModule(this);
	}
	
	protected Module getSharedStateModule() {
		return new SharedStateModule();
	}
	
}
