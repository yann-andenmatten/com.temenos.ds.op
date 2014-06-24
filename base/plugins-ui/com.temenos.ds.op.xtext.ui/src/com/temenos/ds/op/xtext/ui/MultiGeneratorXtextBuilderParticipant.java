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
package com.temenos.ds.op.xtext.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.builder.BuilderParticipant;
import org.eclipse.xtext.builder.IXtextBuilderParticipant;
import org.eclipse.xtext.resource.IResourceDescription;
import org.eclipse.xtext.resource.IResourceServiceProvider;


/**
 * An IXtextBuilderParticipant which... TODO Doc! ;-)
 * 
 * @author Michael Vorburger
 */
public class MultiGeneratorXtextBuilderParticipant extends BuilderParticipant /* implements IXtextBuilderParticipant */ {
	// TODO MAYBE later send a proposal to Xtext core to split up BuilderParticipant so it's more suitable to be extended here
	
//	@Override
//	public void build(IBuildContext context, IProgressMonitor monitor) throws CoreException {
//		for (IResourceDescription.Delta delta : context.getDeltas()) {
//			Resource resource = context.getResourceSet().getResource(delta.getUri(), true);
//			// TODO generate
//			// TODO if (monitor != null)
//		}
//	}

}
