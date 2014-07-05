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
package com.temenos.ds.op.xtext.generator.ui;

import static com.google.common.collect.Maps.uniqueIndex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.builder.BuilderParticipant;
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess2;
import org.eclipse.xtext.builder.IXtextBuilderParticipant.IBuildContext;
import org.eclipse.xtext.builder.impl.RegistryBuilderParticipant;
import org.eclipse.xtext.builder.impl.RegistryBuilderParticipant.BuilderParticipantReader;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.generator.OutputConfiguration;
import org.eclipse.xtext.generator.OutputConfigurationProvider;
import org.eclipse.xtext.resource.IResourceDescription.Delta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.temenos.ds.op.xtext.ui.internal.NODslActivator;


/**
 * An IXtextBuilderParticipant which... TODO Doc! ;-)
 * 
 * @author Michael Vorburger
 */
@SuppressWarnings("restriction")
public class MultiGeneratorsXtextBuilderParticipant extends BuilderParticipant /* implements IXtextBuilderParticipant */ {
	// TODO MAYBE later send a proposal to Xtext core to split up BuilderParticipant so it's more suitable to be extended here
	
	private final static Logger logger = LoggerFactory.getLogger(MultiGeneratorsXtextBuilderParticipant.class);
	
	// This, incl. volatile, inspired by (copy/pasted from) org.eclipse.xtext.builder.impl.RegistryBuilderParticipant.participants
	@Inject
	private IExtensionRegistry extensionRegistry;
	private volatile ImmutableList<IGenerator> generators;
	private Map<String, IGenerator> classToGenerator;
	
	@Override
	protected void handleChangedContents(Delta delta, IBuildContext context,
			EclipseResourceFileSystemAccess2 fileSystemAccess) throws CoreException {

		// copy/paste from super() -- TODO refactor BuilderParticipant with more protected methods so that this can be done cleanly
		Resource resource = context.getResourceSet().getResource(delta.getUri(), true);
		if (shouldGenerate(resource, context)) {
			try {
				registerCurrentSourceFolder(context, delta, fileSystemAccess);

				// TODO remove.. just for quick tests
				System.out.println(delta.getUri());
				fileSystemAccess.generateFile("test.txt", "hello, world: " + delta.getUri().toString());

				// TODO inject generator with lang specific configuration.. is to return only class, not instance, and re-llokup from lang specific injector obtained from extension going to have any perf. drawback? measure it.
//				for (IGenerator generator : getGenerators()) {
//					generator.doGenerate(resource, fileSystemAccess);
//				}

			} catch (RuntimeException e) {
				if (e.getCause() instanceof CoreException) {
					e = (RuntimeException) e.getCause();
				}
				logger.error("RuntimeException during doGenerate() for URI: " + delta.getUri(), e);
				// TODO wouldn't it be better if this, and the parent method where I stole this from, would throw a new wrapping CoreException ?
				throw e;
			}
		}
	}

	// inspired by (copy/pasted from) org.eclipse.xtext.builder.impl.RegistryBuilderParticipant.getParticipants()
	protected ImmutableList<IGenerator> getGenerators() {
		ImmutableList<IGenerator> result = generators;
		if (generators == null) {
			result = initGenerators();
		}
		return result;
	}
	
	// inspired by (copy/pasted from) org.eclipse.xtext.builder.impl.RegistryBuilderParticipant.initParticipants()
	// If later sending a proposal to Xtext core to split up BuilderParticipant so it's more suitable to be extended here, refactor to make this re-usable across instead copy/paste 
	private synchronized ImmutableList<IGenerator> initGenerators() {
		ImmutableList<IGenerator> result = generators;
		if (result == null) {
			if (classToGenerator == null) {
				classToGenerator = Maps.newHashMap();
				NODslActivator activator = NODslActivator.getInstance();
				if (activator != null) {
					String pluginID = activator.getBundle().getSymbolicName();
					String extensionPointID = "multigenerator";
					RegistryBuilderParticipant uselessOuterRBP = null;
					BuilderParticipantReader reader = uselessOuterRBP.new BuilderParticipantReader(extensionRegistry, pluginID, extensionPointID);
					reader.readRegistry();
				}
			}
			result = ImmutableList.copyOf(classToGenerator.values());
			generators = result;
		}
		return result;
	}

	@Override
	protected boolean isEnabled(IBuildContext context) {
		return true;
	}
	
	@Override
	protected List<Delta> getRelevantDeltas(IBuildContext context) {
		return context.getDeltas();
	}

	@Override
	protected Map<String, OutputConfiguration> getOutputConfigurations(IBuildContext context) {
		// NOT // outputConfigurationProvider.getOutputConfigurations(context.getBuiltProject());
		Set<OutputConfiguration> configurations = new OutputConfigurationProvider().getOutputConfigurations();
		// copy/paste from super()
		return uniqueIndex(configurations, new Function<OutputConfiguration, String>() {
			public String apply(OutputConfiguration from) {
				return from.getName();
			}
		});
	}

//	protected Map<String, OutputConfiguration> getOutputConfigurations(IBuildContext context) {
//		return new HashMap<String, OutputConfiguration>(); // TODO ...
//	}
	
}
