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
package com.temenos.ds.op.xtext.generator;

/**
 * Load a resource (or directly an EMF EObject).  
 * 
 * Essentially just "sugar", but intentionally not related to/extending ResourceSet (albeit somewhat similar), to distinguish the more focused & limited idea.
 * 
 * @see IMultiGenerator into which an instance of this is typically injected
 * 
 * @author Michael Vorburger
 */
public interface ResourceLoader {

}
