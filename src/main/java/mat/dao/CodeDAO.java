package mat.dao;

import java.util.List;

import mat.model.Code;

/**
 * The Interface CodeDAO.
 */
public interface CodeDAO extends IDAO<Code, String> {
	
	/**
	 * Delete codes.
	 * 
	 * @param codes
	 *            the codes
	 */
	public void deleteCodes(List<Code> codes);
	
	/**
	 * Search codes.
	 * 
	 * @param codeListId
	 *            the code list id
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @return the list
	 */
	public List<Code> searchCodes(String codeListId,int startIndex, int pageSize);
}
