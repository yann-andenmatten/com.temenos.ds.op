package com.temenos.ds.op.xtext.generator.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.xtext.builder.preferences.BuilderPreferencePage;
import org.eclipse.xtext.ui.editor.preferences.PreferenceStoreAccessImpl;

import com.google.inject.Inject;

@SuppressWarnings("restriction")
public class MultiGeneratorBuilderPreferencePage extends BuilderPreferencePage implements IExecutableExtension {

	// just because it's private instead of protected in the super class
	protected PreferenceStoreAccessImpl _preferenceStoreAccessImpl;

	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		String id = config.getAttribute("id");
		this.setLanguageName(id);
		_preferenceStoreAccessImpl.setLanguageNameAsQualifier(id);
	}

	@Inject
	@Override
	public void setPreferenceStoreAccessImpl(PreferenceStoreAccessImpl preferenceStoreAccessImpl) {
		this._preferenceStoreAccessImpl = preferenceStoreAccessImpl;
		super.setPreferenceStoreAccessImpl(preferenceStoreAccessImpl);
	}
}
