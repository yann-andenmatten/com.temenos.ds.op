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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.builder.BuilderParticipant;
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess2;
import org.eclipse.xtext.generator.OutputConfiguration;
import org.eclipse.xtext.resource.IResourceDescription.Delta;


/**
 * An IXtextBuilderParticipant which... TODO Doc! ;-)
 * 
 * @author Michael Vorburger
 */
public class MultiGeneratorXtextBuilderParticipant extends BuilderParticipant /* implements IXtextBuilderParticipant */ {
	// TODO MAYBE later send a proposal to Xtext core to split up BuilderParticipant so it's more suitable to be extended here
	
	// TODO private final static Logger logger = LoggerFactory.
	
	@Override
	protected void handleChangedContents(Delta delta, IBuildContext context,
			EclipseResourceFileSystemAccess2 fileSystemAccess) throws CoreException {

		// copy/paste from super() -- TODO refactor BuilderParticipant with more protected methods so that this can be done cleanly
		Resource resource = context.getResourceSet().getResource(delta.getUri(), true);
		if (shouldGenerate(resource, context)) {
			try {
				// NO NEED FOR US? registerCurrentSourceFolder(context, delta, fileSystemAccess);
				// TODO generator.doGenerate(resource, fileSystemAccess);
				System.out.println(delta.getUri());
			} catch (RuntimeException e) {
				// TODO logging
				e.printStackTrace();
			}
		}
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
		return new HashMap<String, OutputConfiguration>(); // TODO ...
	}
	
}
