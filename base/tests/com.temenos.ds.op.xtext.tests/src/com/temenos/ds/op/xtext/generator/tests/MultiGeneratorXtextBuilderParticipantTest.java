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

import static org.eclipse.xtext.builder.EclipseOutputConfigurationProvider.OUTPUT_DIRECTORY;
import static org.eclipse.xtext.builder.EclipseOutputConfigurationProvider.OUTPUT_PREFERENCE_TAG;
import static org.eclipse.xtext.builder.EclipseOutputConfigurationProvider.USE_OUTPUT_PER_SOURCE_FOLDER;
import static org.eclipse.xtext.junit4.ui.util.IResourcesSetupUtil.addNature;
import static org.eclipse.xtext.junit4.ui.util.IResourcesSetupUtil.monitor;
import static org.eclipse.xtext.junit4.ui.util.IResourcesSetupUtil.waitForAutoBuild;
import static org.eclipse.xtext.junit4.ui.util.JavaProjectSetupUtil.addProjectReference;
import static org.eclipse.xtext.junit4.ui.util.JavaProjectSetupUtil.createJavaProject;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.xtext.builder.DerivedResourceCleanerJob;
import org.eclipse.xtext.builder.preferences.BuilderPreferenceAccess;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.OutputConfiguration;
import org.eclipse.xtext.ui.XtextProjectHelper;
import org.eclipse.xtext.ui.editor.preferences.PreferenceConstants;
import org.eclipse.xtext.ui.editor.preferences.PreferenceStoreAccessImpl;
import org.eclipse.xtext.util.StringInputStream;
import org.junit.Test;

import com.google.inject.Injector;
import com.temenos.ds.op.xtext.generator.tests.copypasted.AbstractBuilderTest;
import com.temenos.ds.op.xtext.generator.ui.MultiGeneratorsXtextBuilderParticipant;
import com.temenos.ds.op.xtext.ui.internal.NODslActivator;

/**
 * Test for MultiGeneratorsXtextBuilderParticipant.
 * 
 * @author Michael Vorburger
 */
@SuppressWarnings("restriction")
public class MultiGeneratorXtextBuilderParticipantTest extends AbstractBuilderTest{



	@Override
	public void setUp() throws Exception {
		super.setUp();
		final Injector injector = NODslActivator.getInstance().getInjector();
		participant = injector.getInstance(MultiGeneratorsXtextBuilderParticipant.class);
		preferenceStoreAccess = participant.getPreferenceStoreAccess();
	}

	private MultiGeneratorsXtextBuilderParticipant participant;
	private PreferenceStoreAccessImpl preferenceStoreAccess;

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		participant = null;
	}

	@SuppressWarnings("restriction")
	@Test
	public void testMultiGeneratorXtextBuilderParticipant() throws Exception {
		IJavaProject project = createJavaProject("testGenerateIntoProjectOutputDirectory");
		addNature(project.getProject(), XtextProjectHelper.NATURE_ID);
		preferenceStoreAccess.setLanguageNameAsQualifier("com.temenos.ds.op.xtext.generator.tests.TestMultiGenerator");
		preferenceStoreAccess.getWritablePreferenceStore(project.getJavaProject()).setValue(getDefaultOutputDirectoryKey(),
				"./test-gen");
		IFolder folder = project.getProject().getFolder("src");
		IFile file = folder.getFile("Foo" + F_EXT);
		file.create(new StringInputStream("Hello safasdt!"), true, monitor());
		project.getProject().build(IncrementalProjectBuilder.FULL_BUILD, monitor());
		waitForAutoBuild();
		IFile generatedFile = project.getProject().getFile("./test-gen/Foo.mydsl");
		assertTrue(generatedFile.exists());

		preferenceStoreAccess.setLanguageNameAsQualifier("com.temenos.ds.op.xtext.generator.tests.TestMultiGenerator");
		IPreferenceStore preferences = preferenceStoreAccess.getWritablePreferenceStore(project.getProject());
		preferences.setValue(getDefaultOutputDirectoryKey(), "other-gen");
		
		file = folder.getFile("Bar" + F_EXT);
		InputStream source = new StringInputStream("Hello bar!");
		file.create(source, true, monitor());
		project.getProject().build(IncrementalProjectBuilder.FULL_BUILD, monitor());
		waitForAutoBuild();
		generatedFile = project.getProject().getFile("./other-gen/Bar.mydsl");
		assertTrue(generatedFile.exists());
		
		//Test file deletion
		file.delete(true, monitor());
		waitForAutoBuild();
		assertTrue(!generatedFile.exists());
	}

	protected void createTwoReferencedProjects() throws CoreException {
		IJavaProject firstProject = createJavaProjectWithRootSrc("first");
		IJavaProject secondProject = createJavaProjectWithRootSrc("second");
		addProjectReference(secondProject, firstProject);
	}

	protected IJavaProject createJavaProjectWithRootSrc(String string) throws CoreException {
		IJavaProject project = createJavaProject(string);
		addNature(project.getProject(), XtextProjectHelper.NATURE_ID);
		return project;
	}

	public static void waitForResourceCleanerJob() {
		boolean wasInterrupted = false;
		do {
			try {
				Job.getJobManager().join(DerivedResourceCleanerJob.DERIVED_RESOURCE_CLEANER_JOB_FAMILY, null);
				wasInterrupted = false;
			} catch (OperationCanceledException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				wasInterrupted = true;
			}
		} while (wasInterrupted);
	}

	protected String getDefaultOutputDirectoryKey() {
		return OUTPUT_PREFERENCE_TAG + PreferenceConstants.SEPARATOR + IFileSystemAccess.DEFAULT_OUTPUT
				+ PreferenceConstants.SEPARATOR + OUTPUT_DIRECTORY;
	}

	protected String getUseOutputPerSourceFolderKey() {
		return OUTPUT_PREFERENCE_TAG + PreferenceConstants.SEPARATOR + IFileSystemAccess.DEFAULT_OUTPUT
				+ PreferenceConstants.SEPARATOR + USE_OUTPUT_PER_SOURCE_FOLDER;
	}

	protected String getOutputForSourceFolderKey(String sourceFolder) {
		return BuilderPreferenceAccess.getOutputForSourceFolderKey(new OutputConfiguration(
				IFileSystemAccess.DEFAULT_OUTPUT), sourceFolder);
	}
	
}
