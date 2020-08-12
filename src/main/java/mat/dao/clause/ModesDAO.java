package mat.dao.clause;

import mat.dao.IDAO;
import mat.model.clause.Modes;

import java.util.List;

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
