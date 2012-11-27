package org.ifmc.mat.client.clause;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */	
@RemoteServiceRelativePath("clauseCloning")
public interface ClauseCloningService extends RemoteService {
	//TODO:- need to remove the following clone service method, since it is not used. which was called from Appcontroller's copyToMeasurePhraseLibrary,which inturn not used
	//boolean clone(String cloneToMeasureId, Clause clause, String newClauseName);
	boolean clone(String cloneFromMeasureId, String cloneToMeasureId, String clauseName, String newClauseName);
}

