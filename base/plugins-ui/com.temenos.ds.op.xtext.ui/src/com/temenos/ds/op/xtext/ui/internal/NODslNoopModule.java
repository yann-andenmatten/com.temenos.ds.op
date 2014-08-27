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

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.xtext.Constants;
import org.eclipse.xtext.builder.EclipseOutputConfigurationProvider;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.generator.IGenerator.NullGenerator;
import org.eclipse.xtext.parser.IEncodingProvider;
import org.eclipse.xtext.resource.IResourceDescription.Manager;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.service.AbstractGenericModule;
import org.eclipse.xtext.ui.editor.preferences.IPreferenceStoreAccess;
import org.eclipse.xtext.ui.editor.preferences.PreferenceStoreAccessImpl;
import org.eclipse.xtext.validation.IResourceValidator;

import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * Needed because MultiGeneratorsXtextBuilderParticipant currently extends
 * BuilderParticipant for convenience, as for MultiGenerator there is no single
 * language specific IGenerator, IPreferenceStoreAccess. Will be removed later
 * if BuilderParticipant is refactored upstream.
 * 
 * @author Michael Vorburger
 */
public class NODslNoopModule extends AbstractGenericModule {

	public Class<? extends IGenerator> bindIGenerator() {
		return NullGenerator.class;
	}
	
	public Class<? extends IResourceServiceProvider> bindIResourceServiceProvider() {
		return NullResourceServiceProvider.class;
	}
	
	public Class<? extends EclipseOutputConfigurationProvider> bindEclipseOutputConfigurationProvider() {
		return EclipseOutputConfigurationProvider.class; 
	}
	
	public Class<? extends IPreferenceStoreAccess> bindIPreferenceStoreAccess() {
		return PreferenceStoreAccessImpl.class;
	}

	public void configureLanguageName(Binder binder) {
		// PreferenceStoreAccessImpl's setLanguageNameAsQualifier needs a languageName,
		// so we have to bind SOMETHING, anything, here... it's not actually used though,
		// because we programmatically call setLanguageNameAsQualifier() again anyway again later.
		binder.bind(String.class).annotatedWith(Names.named(Constants.LANGUAGE_NAME)).toInstance(NODslNoopModule.class.getName());
	}

	
	public static class NullResourceServiceProvider implements IResourceServiceProvider {

		@Override
		public IResourceValidator getResourceValidator() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Manager getResourceDescriptionManager() {
			throw new UnsupportedOperationException();
		}

		@Override
		public org.eclipse.xtext.resource.IContainer.Manager getContainerManager() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean canHandle(URI uri) {
			return true; // sic!
		}

		@Override
		public IEncodingProvider getEncodingProvider() {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T> T get(Class<T> t) {
			throw new UnsupportedOperationException();
		}
	}
	
	public static class NullPreferenceStoreAccess implements IPreferenceStoreAccess {

		@Override
		public IPreferenceStore getPreferenceStore() {
			throw new UnsupportedOperationException();
		}

		@Override
		public IPreferenceStore getContextPreferenceStore(Object context) {
			throw new UnsupportedOperationException();
		}

		@Override
		public IPreferenceStore getWritablePreferenceStore() {
			throw new UnsupportedOperationException();
		}

		@Override
		public IPreferenceStore getWritablePreferenceStore(Object context) {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
