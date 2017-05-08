package mat.dao;

import mat.model.CodeList;

/**
 * The Interface CodeListDAO.
 */
public interface CodeListDAO extends IDAO<CodeList, String> {
	//US 413. Parameter added --> stewardOther
	/**
	 * Gets the code list.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @param userid
	 *            the userid
	 * @return the code list
	 */
	//public CodeList getCodeList(ManageCodeListDetailModel currentDetails, String userid);
	
	/**
	 * Gets the value sets for category.
	 * 
	 * @param categoryId
	 *            the category id
	 * @return the value sets for category
	 */
	//public List<CodeList> getValueSetsForCategory(String categoryId);
	
	/**
	 * Gets the code lists for category by measure.
	 * 
	 * @param categoryId
	 *            the category id
	 * @param measure
	 *            the measure
	 * @return the code lists for category by measure
	 */
	//public List<CodeList> getCodeListsForCategoryByMeasure(String categoryId, Measure measure);
	
	/**
	 * Gets the codes.
	 * 
	 * @param codeListId
	 *            the code list id
	 * @return the codes
	 */
	//public Set<CodesDTO> getCodes(String codeListId);
	
	/**
	 * Gets the code list.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @param ts
	 *            the ts
	 * @return the code list
	 */
	//public List<CodeList> getCodeList(ManageCodeListDetailModel currentDetails, Timestamp ts);
}
