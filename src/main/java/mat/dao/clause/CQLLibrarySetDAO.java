package mat.dao.clause;


import mat.dao.IDAO;
import mat.model.clause.CQLLibrarySet;


/**
 * The Interface CQLLibrarySetDAO.
 */
/**
 * @author jnarang
 *
 */
public interface CQLLibrarySetDAO extends IDAO<CQLLibrarySet, String> {
	
	/**
	 * @param cqlLibrarySet
	 */
	public void saveCQLLibrarySet(CQLLibrarySet cqlLibrarySet); 
	
	/**
	 * @param cqlLibrarySetId
	 * @return
	 */
	public CQLLibrarySet findCQLLibrarySet(String cqlLibrarySetId);
	
}
