package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.clause.Clause;

import org.springframework.context.ApplicationContext;

public interface ClauseDAO extends IDAO<Clause, String> {
	public void saveClause(Clause clause);
	public void saveClause(Clause clause, ApplicationContext context);
	public Clause loadClause(String measureId, String clauseId, ApplicationContext context);
	public Clause loadClauseByName(String measureId, String clauseName, ApplicationContext context);
	public java.util.List<Clause> findByMeasureId(String measureId, String version);
	public Clause findByNameAndMeasureId(String name, String measureId);
	public Clause findByClauseIdAndMeasureId(String clauseID, String measureId);
	public java.util.List<Clause> findSystemClauses(String measureOwnerId, String userRole, ApplicationContext context);
	public void clone(String cloneToMeasureId, Clause clause, String newClauseName,boolean replaceName, boolean keepContext, ApplicationContext context);
	public void clone(String cloneFromMeasureId, String cloneToMeasureId, 
			String clauseToBeClonedName, String newClauseName, boolean replaceName, boolean keepContext, ApplicationContext context);
	public java.util.List<Clause> findSystemClausesByMeasureId(String measureId, String version);
	
	public void saveClauses(java.util.List<Clause> clauses);
	public void saveClauses(java.util.List<Clause> clauses, ApplicationContext context);
	public void updateClauseName(String id, String name);
	public List<Clause> getAllStratificationClauses(String measureId,String contextId);
}
