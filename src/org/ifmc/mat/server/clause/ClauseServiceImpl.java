package org.ifmc.mat.server.clause;

import java.util.List;

import org.ifmc.mat.client.clause.ClauseService;
import org.ifmc.mat.model.clause.Clause;
import org.ifmc.mat.model.clause.Context;
import org.ifmc.mat.server.SpringRemoteServiceServlet;

@SuppressWarnings("serial")
public class ClauseServiceImpl extends SpringRemoteServiceServlet implements ClauseService {

	@Override
	public List<Clause> loadAll(String measureId, String version) {
		return getClauseService().loadAll(measureId, version, context);
	}

	@Override
	public Clause load(String measureId, String clauseId) {
		return getClauseService().loadClause(measureId, clauseId, context);
	}
	
	@Override
	public Clause loadClauseByName(String measureId, String clauseName) {
		return getClauseService().loadClauseByName(measureId, clauseName, context);
	}

	private ClauseBusinessService getClauseService() {
		return (ClauseBusinessService)context.getBean("clauseBusinessService");
	}

	@Override
	public boolean save(Clause clause) {
		try {
			getClauseService().saveClause(clause, context);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public List<Context> getAllContexts() {
		return getClauseService().getAllContexts();
	}

	@Override
	public Context getContext(String description) {
		return getClauseService().getClauseContext(description);
	}

	@Override
	public List<Clause> loadSystemClauseNames(String measureOwnerId, String userRole) {
		return getClauseService().loadSystemClauseNames(measureOwnerId, userRole, context);
	}

	@Override
	public boolean save(List<Clause> clauses) {
		try {
			getClauseService().saveClauses(clauses, context);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}