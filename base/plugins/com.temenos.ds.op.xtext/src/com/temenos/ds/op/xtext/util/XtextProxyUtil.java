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
package com.temenos.ds.op.xtext.util;

import javax.inject.Inject;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.xtext.linking.impl.LinkingHelper;
import org.eclipse.xtext.linking.lazy.LazyURIEncoder;
import org.eclipse.xtext.nodemodel.INode;

import com.google.common.base.Preconditions;

/**
 * Utility for Xtext related to EMF Proxies.
 * 
 * Tested by XtextProxyUtilTest in refex org.xtext.example.mydsl.tests.
 * 
 * @author Michael Vorburger
 */
public class XtextProxyUtil {

	@Inject private LazyURIEncoder encoder;
	@Inject private LinkingHelper linkingHelper;

	/**
	 * Obtain the "name" of an EMF EReference cross reference instance as String.
	 * 
	 * Normally this is not needed, as you just getXYZ() to obtain the actual
	 * (non-Proxy) cross referenced instance itself. But in certain corner cases
	 * it can be useful to use this helper; e.g. when gen. code, on "broken" (red) 
	 * models with some problem in cross refs, if you'd like to still get the
	 * Cross Ref Link text (String) to... do something with it
	 * (i.e. imagine better error messages?).
	 * 
	 * @param context is the EObject that you got (via getXYZ()) the proxy from
	 * @param proxy the EObject, must be eIsProxy() - check before calling
	 * @return String textual representation of cross reference
	 * 
	 * @throws IllegalArgumentException if !eIsProxy(), or not part of a Xtext resource, etc.
	 */
	public String getProxyCrossRefAsString(EObject context, EObject proxy) {
		Preconditions.checkArgument(proxy.eIsProxy(), "EObject !eIsProxy() : %s", proxy.toString());
		URI proxyURI = ((InternalEObject) proxy).eProxyURI();
		Preconditions.checkNotNull(proxyURI, "EObject eIsProxy() BUT URI is null : %s", proxy.toString());
		String fragment = proxyURI.fragment();
		Preconditions.checkArgument(encoder.isCrossLinkFragment(context.eResource(), fragment),
				"URI is not Xtext Cross Link Fragment: %s", proxyURI);
		INode node = encoder.getNode(context, fragment);
		Preconditions.checkNotNull(node, "EObject context is not 'near' enough (it has no Node model)");
		String linkText = linkingHelper.getCrossRefNodeAsString(node, true);
		return linkText;
	}
	
}
