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
package com.temenos.ds.op.xtext.generator.tests;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.generator.IFileSystemAccess;

import com.temenos.ds.op.xtext.generator.IMultiGenerator;

/**
 * Example IMultiGenerator, for Tests.
 * 
 * @author Michael Vorburger
 */
public class TestMultiGenerator implements IMultiGenerator {

	@Override
	public void doGenerate(URI input, IFileSystemAccess fsa) throws Exception {
		fsa.generateFile("test.txt", "hello, world");
	}

}
