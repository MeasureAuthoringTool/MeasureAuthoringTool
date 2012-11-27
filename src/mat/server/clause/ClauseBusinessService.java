package mat.server.clause;

import java.util.List;

import mat.model.clause.Clause;
import mat.model.clause.Context;

import org.springframework.context.ApplicationContext;

public interface ClauseBusinessService {

	List<Clause> loadAll(String measureId, String version, ApplicationContext context);
	Clause loadClause(String measureId, String clId, ApplicationContext context);
	Clause loadClauseByName(String measureId, String clauseName, ApplicationContext context);
	List<Context> getAllContexts();
	Context getClauseContext(String description);
	boolean saveClause(Clause clause, ApplicationContext ctx);
	List<Clause> loadSystemClauseNames(String measureOwnerId, String userRole, ApplicationContext ctx);
	boolean saveClauses(List<Clause> clauses, ApplicationContext ctx);
	void setClauseNameForMeasure(String measureId, String shortMeasureName);
}
