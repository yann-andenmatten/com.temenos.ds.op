package com.temenos.ds.op.sirius;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.business.api.session.SavingPolicyImpl;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionListener;
import org.eclipse.sirius.business.api.session.SessionManagerListener2;

public class ResourceIsModifiedSavingPolicySessionManagerListener extends SessionManagerListener2.Stub {

	public ResourceIsModifiedSavingPolicySessionManagerListener() {
	}

	@Override
	public void notifyAddSession(Session newSession) {
		newSession.setSavingPolicy(new SavingPolicyImpl(newSession.getTransactionalEditingDomain()) {
			@Override
			protected boolean hasChangesToSave(Resource resource) {
				return resource.isModified();
			}
		});
	}
	
	@Override
	public void notify(Session updated, int notification) {
		if (notification == SessionListener.OPENED) {
			for (Resource res : updated.getSemanticResources()) {
				res.setTrackingModification(true);
			}
		}
	}

}