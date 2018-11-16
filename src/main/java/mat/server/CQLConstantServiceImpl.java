package mat.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mat.DTO.DataTypeDTO;
import mat.DTO.UnitDTO;
import mat.client.cqlconstant.service.CQLConstantService;
import mat.client.shared.CQLConstantContainer;
import mat.client.shared.MatContext;
import mat.dao.clause.QDSAttributesDAO;
import mat.model.cql.CQLKeywords;
import mat.server.service.CodeListService;
import mat.server.service.MeasureLibraryService;
import mat.server.util.MATPropertiesService;

@Service
public class CQLConstantServiceImpl extends SpringRemoteServiceServlet implements CQLConstantService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private CodeListService codeListService;
	
	@Autowired
	private QDSAttributesDAO qDSAttributesDAO;
	
	@Autowired
	private MeasureLibraryService measureLibraryService;
	
	@Override
	public CQLConstantContainer getAllCQLConstants() {		
		final CQLConstantContainer cqlConstantContainer = new CQLConstantContainer(); 
		
		// get the unit dto list
		final List<UnitDTO> unitDTOList = codeListService.getAllUnits();
		cqlConstantContainer.setCqlUnitDTOList(unitDTOList);
		
		// get the unit map in the form of <UnitName, CQLUnit>
		final Map<String, String> unitMap = new LinkedHashMap<>(); 
		unitMap.put(MatContext.PLEASE_SELECT, MatContext.PLEASE_SELECT);
		for(final UnitDTO unit : unitDTOList) {
			unitMap.put(unit.getUnit(), unit.getCqlunit());
		}
		cqlConstantContainer.setCqlUnitMap(unitMap);
		
		final List<String> cqlAttributesList = qDSAttributesDAO.getAllAttributes();
		cqlConstantContainer.setCqlAttributeList(cqlAttributesList);
		
		// get the datatypes
		final List<DataTypeDTO> dataTypeListBoxList = codeListService.getAllDataTypes();
		final List<String> datatypeList = new ArrayList<>();
		for(int i = 0; i < dataTypeListBoxList.size(); i++) {
			datatypeList.add(dataTypeListBoxList.get(i).getItem());
		}
				
		final List<String> qdmDatatypeList = new ArrayList<>(); 
		qdmDatatypeList.addAll(datatypeList);
		datatypeList.remove("attribute");
		
		cqlConstantContainer.setCqlDatatypeList(datatypeList);
		cqlConstantContainer.setQdmDatatypeList(qdmDatatypeList);
		
		// get keywords
		final CQLKeywords keywordList = measureLibraryService.getCQLKeywordsLists(); 
		cqlConstantContainer.setCqlKeywordList(keywordList);
		
		// get timings
		final List<String> timings = keywordList.getCqlTimingList();
		Collections.sort(timings);
		cqlConstantContainer.setCqlTimingList(timings);
		
		cqlConstantContainer.setCurrentQDMVersion(MATPropertiesService.get().getQmdVersion());
		cqlConstantContainer.setCurrentReleaseVersion(MATPropertiesService.get().getCurrentReleaseVersion());
		
		return cqlConstantContainer;
		
	}

}
