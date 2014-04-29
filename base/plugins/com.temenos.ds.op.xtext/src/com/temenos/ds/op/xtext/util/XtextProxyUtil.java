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
	 * @param eObject the EObject, must be eIsProxy() - check before calling
	 * @return String textual representation of cross reference
	 * 
	 * @throws IllegalArgumentException if !eIsProxy(), or not part of a Xtext resource, etc.
	 */
	public String getProxyCrossRefAsString(EObject eObject) {
		Preconditions.checkArgument(eObject.eIsProxy(), "EObject !eIsProxy() : %s", eObject.toString());
		URI proxyURI = ((InternalEObject) eObject).eProxyURI();
		Preconditions.checkNotNull(proxyURI, "EObject eIsProxy() BUT URI is null : %s", eObject.toString());
		String fragment = proxyURI.fragment();
		Preconditions.checkArgument(encoder.isCrossLinkFragment(eObject.eResource(), fragment),
				"URI is not Xtext Cross Link Fragment: %s", proxyURI);
		INode node = encoder.getNode(eObject, fragment);
		String linkText = linkingHelper.getCrossRefNodeAsString(node, true);
		return linkText;
	}
	
}
