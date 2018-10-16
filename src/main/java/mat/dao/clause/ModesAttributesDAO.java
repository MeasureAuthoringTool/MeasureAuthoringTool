package mat.dao.clause;

import java.util.List;

import mat.dao.IDAO;
import mat.model.clause.ModesAttributes;

/**
 * The Interface ModesAttributesDAO.
 */
public interface ModesAttributesDAO extends IDAO<ModesAttributes, String> {
	
	/**
	 * Find by attrId.
	 * 
	 * @param attrId
	 *            the attr id
	 * @return the mode
	 */
	public List<ModesAttributes> findByAttrId(String attrId);
	
	/**
	 * Find by modeId.
	 * 
	 * @param modeId
	 *            the mode id
	 * @return the attribute 
	 */
	public List<ModesAttributes> findByModeId(String modeId);
}
