package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.clause.Modes;

/**
 * The Interface ModesDAO.
 */
public interface ModesDAO extends IDAO<Modes, String> {
	
	/**
	 * Find by name.
	 * 
	 * @param modeName
	 *            the mode name
	 * @return the mode details
	 */
	public List<Modes> findByName(String modeName);
	
	/**
	 * Find by id.
	 * 
	 * @param id
	 *            the mode id
	 * @return the mode details
	 */
	public List<Modes> findById(String id);
}
