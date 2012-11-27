package mat.server.clause;

import mat.client.clause.ClauseCloningService;
import mat.dao.clause.ClauseDAO;
import mat.server.SpringRemoteServiceServlet;

import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("serial")
public class ClauseCloningServiceImpl extends SpringRemoteServiceServlet implements ClauseCloningService {

	@Autowired
	private ClauseDAO clauseDAO;
	
	/*
	 * //TODO:- need to remove the following clone service method, since it is not used. 
	 * which was called from Appcontroller's copyToMeasurePhraseLibrary,which in turn not used
	@Override
	public boolean clone(String cloneToMeasureId, Clause clause, String newClauseName) {
		clauseDAO = (ClauseDAO)context.getBean("clauseDAO");
		try {
			boolean replaceName = true;
			boolean keepContext = false;
			clauseDAO.clone(cloneToMeasureId, clause, newClauseName, replaceName, keepContext, context);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}*/
	
	@Override
	public boolean clone(String cloneFromMeasureId, String cloneToMeasureId, String clauseName, String newClauseName) {
		clauseDAO = (ClauseDAO)context.getBean("clauseDAO");
		try {
			boolean replaceName = true;
			boolean keepContext = true;
			clauseDAO.clone(cloneFromMeasureId, cloneToMeasureId, clauseName, 
					newClauseName, replaceName, keepContext, context);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
