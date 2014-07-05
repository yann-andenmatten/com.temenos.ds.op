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

import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.xtext.builder.EclipseOutputConfigurationProvider;
import org.eclipse.xtext.generator.IOutputConfigurationProvider;
import org.eclipse.xtext.generator.OutputConfiguration;

import com.google.inject.Inject;

/**
 * IOutputConfigurationProvider that returns the default (delegate) fixed
 * non-configurable non-project specific OutputConfiguration.
 * 
 * This is intended as temporary short-term, until I make the
 * MultiGeneratorsXtextBuilderParticipant able to instantiate language specific
 * (by extension) implementations, and thus find the per-lang preference etc.
 * 
 * @author Michael Vorburger
 */
public class SimpleDefaultEclipseOutputConfigurationProvider extends EclipseOutputConfigurationProvider {

	@Inject
	public SimpleDefaultEclipseOutputConfigurationProvider(IOutputConfigurationProvider delegate) {
		super(delegate);
	}

	public Set<OutputConfiguration> getOutputConfigurations(IProject project) {
		return getOutputConfigurations();
	}
	
}
