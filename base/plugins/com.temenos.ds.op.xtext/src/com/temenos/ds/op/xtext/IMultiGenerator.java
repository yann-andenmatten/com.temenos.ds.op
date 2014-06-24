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
package com.temenos.ds.op.xtext;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.generator.IFileSystemAccess;

/**
 * Generator which, contrary to {@link org.eclipse.xtext.generator.IGenerator},
 * is not language specific, and of which there can be several for a given
 * language.
 * 
 * <p>TODO Document how to register these!
 *
 * <p> * Implementations can have stuff @Inject'd, most notably typically a
 * {@link ResourceLoader}, which makes it easy to get actual models objects from
 * the input URI; use it only if your Generator is going to actually consume the
 * URI. Note that the correct language specific dependencies will be @Inject'ed.
 * 
 * <p>Generate throws Exception, as most cartridges don't want to and shouldn't
 * have to deal with any exception handling themselves - the caller will
 * correctly log it, and continue.
 * 
 * <p>Implementations must be stateless - do not "hang on" to resources between
 * invocations. This is enforced by the implementation by re-creating a new
 * instance of this for every model resource (which is also needed to inject the
 * correct model type specific context). And, please, though shall not not use
 * trickery and resort to hacks using "static".
 * 
 * @see org.eclipse.xtext.generator.IGenerator
 * 
 * @author Michael Vorburger
 */
public interface IMultiGenerator {

	/**
	 * Generate stuff.
	 * 
	 * @param input an EMF URI (within the IDE UI, it's typically an Eclipse platform:// URI; in standalone, some other scheme which the injected ResourceLoader accepts as argument) 
	 * @param fsa utility to write out what this Generator produces
	 */
	void doGenerate(URI input, IFileSystemAccess fsa) throws Exception;

}
