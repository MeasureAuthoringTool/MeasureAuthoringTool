package org.ifmc.mat.client.clause;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClauseCloningServiceAsync {
	//TODO:- need to remove the following clone service method, since it is not used. which was called from Appcontroller's copyToMeasurePhraseLibrary,which inturn not used
	//void clone(String cloneToMeasureId, Clause clause, String newClauseName, AsyncCallback<Boolean> callback);
	void clone(String cloneFromMeasureId, String cloneToMeasureId, String clauseName, String newClauseName,AsyncCallback<Boolean> callback);
}