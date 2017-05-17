package mat.server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Window;

import mat.DTO.UnitDTO;
import mat.client.codelist.HasListBox;
import mat.client.cqlconstant.service.CQLConstantService;
import mat.client.shared.CQLConstantContainer;
import mat.dao.clause.QDSAttributesDAO;
import mat.model.cql.CQLKeywords;
import mat.server.service.CodeListService;
import mat.server.service.MeasureLibraryService;

@SuppressWarnings("serial")
public class CQLConstantServiceImpl extends SpringRemoteServiceServlet implements CQLConstantService {

	@Override
	public CQLConstantContainer getAllCQLConstants() {		
		CQLConstantContainer cqlConstantContainer = new CQLConstantContainer(); 
		
		// get the unit dto list
		List<UnitDTO> unitDTOList = getCodeListService().getAllUnits();
		cqlConstantContainer.setCqlUnitDTOList(getCodeListService().getAllUnits());
		
		// get the unit map in the form of <UnitName, CQLUnit>
		Map<String, String> unitMap = new LinkedHashMap<String, String>(); 
		for(UnitDTO unit : unitDTOList) {
			unitMap.put(unit.getUnit(), unit.getCqlunit());
		}
		cqlConstantContainer.setCqlAttributeList(getAttributeDAO().getAllAttributes());
		
		// get the datatypes
		List<? extends HasListBox> dataTypeListBoxList = getCodeListService().getAllDataTypes();
		List<String> datatypeList = new ArrayList<String>();
		for(int i = 0; i < dataTypeListBoxList.size(); i++) {
			datatypeList.add(dataTypeListBoxList.get(i).getItem());
		}
				
		List<String> qdmDatatypeList = new ArrayList<String>(); 
		qdmDatatypeList.addAll(datatypeList);
		datatypeList.remove("attribute");
		
		cqlConstantContainer.setCqlDatatypeList(datatypeList);
		cqlConstantContainer.setQdmDatatypeList(qdmDatatypeList);
		
		// get keywords
		CQLKeywords keywordList = getMeasureLibraryService().getCQLKeywordsLists(); 
		cqlConstantContainer.setCqlKeywordList(keywordList);
		
		return cqlConstantContainer;
	}
	
	/**
	 * Gets the code list service.
	 * 
	 * @return the code list service
	 */
	public CodeListService getCodeListService() {
		return (CodeListService)context.getBean("codeListService");
	}
	
	/**
	 * Gets the dao.
	 * 
	 * @return the dao
	 */
	public QDSAttributesDAO getAttributeDAO() {
		return (QDSAttributesDAO) context.getBean("qDSAttributesDAO");
	}
	
	/**
	 * Gets the measure library service.
	 * 
	 * @return the measure library service
	 */
	public MeasureLibraryService getMeasureLibraryService(){
		return (MeasureLibraryService) context.getBean("measureLibraryService");
	}

}
