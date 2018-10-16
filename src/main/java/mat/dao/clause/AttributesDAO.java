package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.clause.Attributes;

/**
 * The Interface AttributesDAO.
 */
public interface AttributesDAO extends IDAO<Attributes, String> {
	
	/**
	 * Find by name.
	 * 
	 * @param attrName
	 *            the attr name
	 * @return the attribute details
	 */
	public List<Attributes> findByName(String attrName);
	
	/**
	 * Find by id.
	 * 
	 * @param id
	 *            the attr id
	 * @return the attribute details
	 */
	public List<Attributes> findById(String id);
}
