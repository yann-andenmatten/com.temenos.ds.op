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

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.builder.BuilderParticipant;
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess2;
import org.eclipse.xtext.builder.IXtextBuilderParticipant;
import org.eclipse.xtext.builder.IXtextBuilderParticipant.IBuildContext;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.IResourceDescription.Delta;
import org.slf4j.Logger;


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

	//	@Override
//	public void build(IBuildContext context, IProgressMonitor monitor) throws CoreException {
//		for (IResourceDescription.Delta delta : context.getDeltas()) {
//			Resource resource = context.getResourceSet().getResource(delta.getUri(), true);
//			// TODO generate
//			// TODO if (monitor != null)
//		}
//	}

	@Override
	protected boolean isEnabled(IBuildContext context) {
		return true;
	}
	
	@Override
	protected List<Delta> getRelevantDeltas(IBuildContext context) {
		return context.getDeltas();
	}
	
}
