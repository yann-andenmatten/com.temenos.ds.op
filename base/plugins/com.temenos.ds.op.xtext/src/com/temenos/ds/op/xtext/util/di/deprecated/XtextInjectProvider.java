package com.temenos.ds.op.xtext.util.di.deprecated;

import javax.inject.Inject;

import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.IResourceServiceProvider;

import com.google.common.base.Preconditions;

/**
 * Provider for Xtext-language specific implementations.
 * 
 * @author Michael Vorburger
 */
public class XtextInjectProvider<T> {

	protected @Inject IResourceServiceProvider.Registry registry;
	protected Class<T> klass;

	/**
	 * Constructor.
	 * 
	 * @param klass the Java Class of which you would like to obtain an instance 
	 */
	public XtextInjectProvider(Class<T> klass) {
		this.klass = klass;
	}
	
	/**
	 * Get Instance from a DI container.
	 * 
	 * @param URI EMF URI which, based on extension, is used to look up DI container 
	 * @return instance of type klass
	 * 
	 * @throws IllegalArgumentException if no provider for klass is registered in the respective (language specific) Injector for this URI
	 */
	public T get(URI uri) {
		Preconditions.checkNotNull(uri, "uri == null");
		Preconditions.checkNotNull(registry, "registry == null");
		IResourceServiceProvider resourceServiceProvider = registry.getResourceServiceProvider(uri);
		if (resourceServiceProvider == null)
			throw new IllegalStateException("IResourceServiceProvider.Registry getResourceServiceProvider() returned null for URI: " + uri.toString());
		if (!resourceServiceProvider.canHandle(uri))
			throw new IllegalStateException("Now that's curious, the IResourceServiceProvider.Registry returned an IResourceServiceProvider which !canHandle() this URI: " + uri.toString());

		// Injector injector = resourceServiceProvider.get(Injector.class);
		try {
			// return injector.getInstance(klass);
			return resourceServiceProvider.get(klass);
		} catch (Exception e) {
			throw new IllegalArgumentException("getInstance(" + klass.getName() + " failed for Injector obtained for URI: " + uri.toString(), e);
		}
	}
}
