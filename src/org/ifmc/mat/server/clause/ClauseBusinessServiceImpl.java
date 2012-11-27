package org.ifmc.mat.server.clause;

import java.util.ArrayList;
import java.util.List;

import org.ifmc.mat.dao.clause.ClauseDAO;
import org.ifmc.mat.dao.clause.ContextDAO;
import org.ifmc.mat.model.clause.Clause;
import org.ifmc.mat.model.clause.Context;
import org.ifmc.mat.shared.model.Conditional;
import org.ifmc.mat.shared.model.Decision;
import org.ifmc.mat.shared.model.FunctionTerm;
import org.ifmc.mat.shared.model.QDSMeasurementTerm;
import org.ifmc.mat.shared.model.QDSTerm;
import org.ifmc.mat.shared.model.StatementTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class ClauseBusinessServiceImpl implements ClauseBusinessService {
	
	@Autowired
	private ClauseDAO clauseDAO;

	@Autowired
	private ContextDAO contextDAO;
	
	public Clause loadClause(String measureId, String clauseId, ApplicationContext ctx) {
		return clauseDAO.loadClause(measureId, clauseId, ctx);
	}
	
	public Clause loadClauseByName(String measureId, String clauseName, ApplicationContext ctx) {
		return clauseDAO.loadClauseByName(measureId, clauseName, ctx);
	}

	public List<Clause> loadAll(String measureId, String version, ApplicationContext ctx) {

		List<Clause> clDetails = new ArrayList<Clause>();
		List<Clause> clauses = clauseDAO.findByMeasureId(measureId, version);
		for (Clause cl : clauses) {
			clDetails.add(loadClause(measureId, cl.getId(), ctx));
		}
		return clDetails;
	}

	@Override
	public boolean saveClause(Clause clause, ApplicationContext ctx) {
		try {
			clauseDAO.saveClause(clause, ctx);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public List<Context> getAllContexts() {
		return contextDAO.getAll();
	}

	@Override
	public Context getClauseContext(String description) {
		return contextDAO.getContext(description);
	}

	private void display(Clause clause) {
		System.out.println("CLAUSE: " + clause.getName());
		
		for (Decision decision : clause.getDecisions()) {
			if (decision instanceof QDSTerm) {
				QDSTerm qdsTerm = (QDSTerm)decision;
				System.out.println("QDSTERM: " + qdsTerm.getqDSRef());
			}
			else if (decision instanceof QDSMeasurementTerm) {		
				QDSMeasurementTerm qdsMeasurementTerm = (QDSMeasurementTerm)decision;
				System.out.println("QDS MEASUREMENT TERM");
				System.out.println("\tLFTERM" + qdsMeasurementTerm.getLfTerm());
				System.out.println("\tQDS OPERATOR: " + qdsMeasurementTerm.getQDSOperator());
				System.out.println("\tRTTERM" + qdsMeasurementTerm.getRtTerm());
			}			
			else if (decision instanceof StatementTerm) {
				StatementTerm statementTerm = (StatementTerm)decision;	
				System.out.println("STATEMENT TERM");
				System.out.println("\tLFTERM:   " + statementTerm.getLfTerm());
				System.out.println("\tOPERATOR: " + statementTerm.getOperator().toString());
				System.out.println("\tRTTERM:   " + statementTerm.getRtTerm());
			}	
			else if (decision instanceof Conditional) {	
				Conditional conditional = (Conditional)decision;
				System.out.println("CONDITIONAL");
				System.out.println("\tCONDITION: " + ((conditional.getOperator() == Conditional.Operator.AND) ? "AND" : "OR"));
				for (Decision conditionalDecision : conditional.getDecisions())
					System.out.println("\t\tDECISION: " + conditionalDecision.toString());
			}
			else if (decision instanceof FunctionTerm) {
				FunctionTerm functionTerm = (FunctionTerm)decision;
				System.out.println("FUNCTION TERM");
				System.out.println("\tFUNCTION: " + functionTerm.getName());
				if (functionTerm.getTerm() instanceof QDSMeasurementTerm) {
					QDSMeasurementTerm qdsMeasurementTerm = (QDSMeasurementTerm)decision;
					System.out.println("\tQDS MEASUREMENT TERM");
					System.out.println("\t\tLFTERM" + qdsMeasurementTerm.getLfTerm());
					System.out.println("\t\tQDS OPERATOR: " + qdsMeasurementTerm.getQDSOperator());
					System.out.println("\t\tRTTERM" + qdsMeasurementTerm.getRtTerm());					
				}
				else if (functionTerm.getTerm() instanceof StatementTerm) {
					StatementTerm statementTerm  = (StatementTerm)(functionTerm.getTerm());
					System.out.println("\t\tLFTERM");
					if (statementTerm.getLfTerm() instanceof Conditional) {
						Conditional conditional = (Conditional)(statementTerm.getLfTerm());
						System.out.println("\t\t\tCONDITIONAL");	
						System.out.println("\t\t\t\tCONDITION: " + ((conditional.getOperator() == Conditional.Operator.AND) ? "AND" : "OR"));
						for (Decision conditionalDecision : conditional.getDecisions())
							System.out.println("\t\t\t\t\tDECISION: " + conditionalDecision.toString());
					}
					else
						if (statementTerm.getLfTerm() instanceof QDSMeasurementTerm) {
							QDSMeasurementTerm qdsMeasurementTerm = (QDSMeasurementTerm)statementTerm.getLfTerm();
							System.out.println("\t\t\tQDS MEASUREMENT TERM");
							System.out.println("\t\t\t\tLFTERM" + qdsMeasurementTerm.getLfTerm());
							System.out.println("\t\t\t\tQDS OPERATOR: " + qdsMeasurementTerm.getQDSOperator());
							System.out.println("\t\t\t\tRTTERM" + qdsMeasurementTerm.getRtTerm());									
						}
					System.out.println("\t\tOPERATOR: " + statementTerm.getOperator().toString());
					System.out.println("\t\tRTTERM:   " + statementTerm.getRtTerm());
				}
			}
			else
				System.out.println("Unknown decision: " + decision.getClass().getSimpleName());
		}
		System.out.println("******** SERVER RECEIVED CLAUSE ********\n\n");
	}

	@Override
	public List<Clause> loadSystemClauseNames(String measureOwnerId, String userRole, ApplicationContext ctx) {
		return clauseDAO.findSystemClauses(measureOwnerId, userRole, ctx);
	}

	@Override
	public boolean saveClauses(List<Clause> clauses, ApplicationContext ctx) {
		try {
			clauseDAO.saveClauses(clauses, ctx);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void setClauseNameForMeasure(String measureId, String shortMeasureName) {
		List<Clause> clauses = clauseDAO.findSystemClausesByMeasureId(measureId, null);
		for(Clause c : clauses){
			String newName = resetClauseName(c.getName(), shortMeasureName);
			if(!c.getName().equalsIgnoreCase(newName)){
				clauseDAO.updateClauseName(c.getId(), newName);
			}
		}
	}
	
	private String resetClauseName(String clauseName, String shortMeasureName){
		String suffix = clauseName.substring(clauseName.lastIndexOf("_"));
		return shortMeasureName+suffix;
	}

}
