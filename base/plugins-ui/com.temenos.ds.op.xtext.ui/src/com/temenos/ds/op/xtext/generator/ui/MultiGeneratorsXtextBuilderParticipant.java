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

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.emf.ecore.plugin.RegistryReader;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.builder.BuilderParticipant;
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess2;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.generator.OutputConfiguration;
import org.eclipse.xtext.generator.OutputConfigurationProvider;
import org.eclipse.xtext.resource.IResourceDescription.Delta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.temenos.ds.op.xtext.ui.internal.NODslActivator;


/**
 * An IXtextBuilderParticipant which... TODO Doc! ;-)
 * 
 * @author Michael Vorburger
 */
public class MultiGeneratorsXtextBuilderParticipant extends BuilderParticipant /* implements IXtextBuilderParticipant */ {
	// TODO MAYBE later send a proposal to Xtext core to split up BuilderParticipant so it's more suitable to be extended here
	
	// private final static Logger logger = LoggerFactory.getLogger(MultiGeneratorsXtextBuilderParticipant.class);
	
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

				// TODO inject generator with lang specific configuration.. is to return only class, not instance, and re-llokup from lang specific injector obtained from extension going to have any perf. drawback? measure it.
				for (IGenerator generator : getGenerators()) {
					generator.doGenerate(resource, fileSystemAccess);
				}

			} catch (RuntimeException e) {
				if (e.getCause() instanceof CoreException) {
					throw (CoreException) e.getCause();
				}
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
					GeneratorReader<IGenerator> reader = new GeneratorReader<IGenerator>(extensionRegistry, pluginID, "multigenerator", classToGenerator);
					reader.readRegistry();
				}
			}
			result = ImmutableList.copyOf(classToGenerator.values());
			generators = result;
		}
		return result;
	}

	// This inner class is inspired by org.eclipse.xtext.builder.impl.RegistryBuilderParticipant.BuilderParticipantReader - 
	// TODO If later sending a proposal to Xtext core to split up BuilderParticipant so it's more suitable to be extended here, refactor to make this re-usable across instead copy/paste 
	// It's intentionally static to make sure it doesn't access members of the outer class, and will thus be easier to factor out later 
	public static class GeneratorReader<T> extends RegistryReader {
		private final static Logger logger = LoggerFactory.getLogger(MultiGeneratorsXtextBuilderParticipant.GeneratorReader.class);

		private static final String ATT_CLASS = "class";
		
		protected final String extensionPointID;
		protected final Map<String, T> classNameToInstance;

		public GeneratorReader(IExtensionRegistry pluginRegistry, String pluginID, String extensionPointID, Map<String, T> classNameToInstance) {
			super(pluginRegistry, pluginID, extensionPointID);
			this.extensionPointID = extensionPointID;
			this.classNameToInstance = classNameToInstance;
		}
		
		@Override
		protected boolean readElement(IConfigurationElement element, boolean add) {
			if (element.getName().equals(extensionPointID)) {
				String className = element.getAttribute(ATT_CLASS);
				if (className == null) {
					logMissingAttribute(element, ATT_CLASS);
				} else if (add) {
					if (classNameToInstance.containsKey(className)) {
						logger.warn("The builder participant '" + className + "' was registered twice."); //$NON-NLS-1$ //$NON-NLS-2$
					}
					Optional<T> instance = get(element);
					if (!instance.isPresent()) {
						return false;
					}
					classNameToInstance.put(className, instance.get());
					// ? participants = null;
					return true;
				} else {
					classNameToInstance.remove(className);
					// ? participants = null;
					return true;
				}
			}
			return false;
		}

		@SuppressWarnings("unchecked")
		protected Optional<T> get(IConfigurationElement element) {
			try {
				return Optional.of((T) element.createExecutableExtension(ATT_CLASS));
			} catch (CoreException e) {
				logError(element, "CoreException from createExecutableExtension(): " + e.getMessage() /* TODO , e */);
			} catch (NoClassDefFoundError e) {
				logError(element, e.getMessage());
			}
			return Optional.absent();
		}

		@Override
		protected void logError(IConfigurationElement element, String text) {
			IExtension extension = element.getDeclaringExtension();
			logger.error("Plugin " + extension.getContributor().getName() + ", extension " + extension.getExtensionPointUniqueIdentifier()); //$NON-NLS-1$ //$NON-NLS-2$
			logger.error(text);
		}
	}
	
	@Override
	protected boolean isEnabled(IBuildContext context) {
		// TODO better later read this from.. an IGenerator specific Preference
		return true;
	}
	
	@Override
	protected List<Delta> getRelevantDeltas(IBuildContext context) {
		// TODO better for future compat. to just make sure we @Inject an resourceServiceProvider where canHandle => true always instead of this short term solution:
		return context.getDeltas();
	}

}
