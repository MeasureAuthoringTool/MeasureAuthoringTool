package org.ifmc.mat.client.clause;

import java.util.List;

import org.ifmc.mat.model.clause.Clause;
import org.ifmc.mat.model.clause.Context;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>ClauseServiceAsync</code>.
 */
public interface ClauseServiceAsync {
	void save(Clause clause, AsyncCallback<Boolean> callback);
	void loadAll(String measureId, String version,
			AsyncCallback<List<Clause>> callback);
	void getContext(String description, AsyncCallback<Context> callback);
	void getAllContexts(AsyncCallback<List<Context>> callback);
	void load(String measureId, String clauseId, AsyncCallback<Clause> callback);
	void loadClauseByName(String measureId, String clauseName, AsyncCallback<Clause> callback);
	void loadSystemClauseNames(String measureOwnerId, String userRole, AsyncCallback<List<Clause>> callback);
	void save(List<Clause> clauses, AsyncCallback<Boolean> callback);
}