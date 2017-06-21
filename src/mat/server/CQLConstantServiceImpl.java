package mat.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Window;

import mat.DTO.UnitDTO;
import mat.client.codelist.HasListBox;
import mat.client.cqlconstant.service.CQLConstantService;
import mat.client.shared.CQLConstantContainer;
import mat.client.shared.MatContext;
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
		unitMap.put(MatContext.get().PLEASE_SELECT, MatContext.get().PLEASE_SELECT);
		for(UnitDTO unit : unitDTOList) {
			unitMap.put(unit.getUnit(), unit.getCqlunit());
		}
		cqlConstantContainer.setCqlUnitMap(unitMap);
		
		List<String> cqlAttributesList = getAttributeDAO().getAllAttributes();
		Collections.sort(cqlAttributesList);
		cqlConstantContainer.setCqlAttributeList(cqlAttributesList);
		
		// get the datatypes
		List<? extends HasListBox> dataTypeListBoxList = getCodeListService().getAllDataTypes();
		List<String> datatypeList = new ArrayList<String>();
		for(int i = 0; i < dataTypeListBoxList.size(); i++) {
			datatypeList.add(dataTypeListBoxList.get(i).getItem());
		}
				
		List<String> qdmDatatypeList = new ArrayList<String>(); 
		Collections.sort(datatypeList);
		qdmDatatypeList.addAll(datatypeList);
		datatypeList.remove("attribute");
		
		cqlConstantContainer.setCqlDatatypeList(datatypeList);
		cqlConstantContainer.setQdmDatatypeList(qdmDatatypeList);
		
		// get keywords
		CQLKeywords keywordList = getMeasureLibraryService().getCQLKeywordsLists(); 
		cqlConstantContainer.setCqlKeywordList(keywordList);
		
		// get timings
		List<String> timings = keywordList.getCqlTimingList();
		Collections.sort(timings);
		cqlConstantContainer.setCqlTimingList(timings);
		
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
