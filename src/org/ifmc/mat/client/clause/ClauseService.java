package org.ifmc.mat.client.clause;

import java.util.List;

import org.ifmc.mat.model.clause.Clause;
import org.ifmc.mat.model.clause.Context;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */	
@RemoteServiceRelativePath("clause")
public interface ClauseService extends RemoteService {
	Clause load(String measureId, String clauseId);
	Clause loadClauseByName(String measureId, String clauseName);
	boolean save(Clause clause);
	Context getContext(String description);
	List<Context> getAllContexts();
	List<Clause> loadAll(String measureId, String version);	
	List<Clause> loadSystemClauseNames(String measureOwnerId, String userRole);
	boolean save(List<Clause> clauses);
}
