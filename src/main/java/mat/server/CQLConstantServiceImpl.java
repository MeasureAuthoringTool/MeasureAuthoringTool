package mat.server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.cqframework.cql.cql2elm.QdmModelInfoProvider;
import org.cqframework.cql.cql2elm.SystemModelInfoProvider;
import org.hl7.elm_modelinfo.r1.ChoiceTypeSpecifier;
import org.hl7.elm_modelinfo.r1.ClassInfo;
import org.hl7.elm_modelinfo.r1.ClassInfoElement;
import org.hl7.elm_modelinfo.r1.ModelInfo;
import org.hl7.elm_modelinfo.r1.NamedTypeSpecifier;
import org.hl7.elm_modelinfo.r1.ProfileInfo;
import org.hl7.elm_modelinfo.r1.TypeInfo;
import org.hl7.elm_modelinfo.r1.TypeSpecifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import edu.emory.mathcs.backport.java.util.Arrays;
import mat.DTO.DataTypeDTO;
import mat.DTO.UnitDTO;
import mat.client.cqlconstant.service.CQLConstantService;
import mat.client.shared.CQLConstantContainer;
import mat.client.shared.CQLTypeContainer;
import mat.client.shared.MatContext;
import mat.client.shared.QDMContainer;
import mat.dao.clause.QDSAttributesDAO;
import mat.model.cql.CQLKeywords;
import mat.server.service.CodeListService;
import mat.server.service.MeasureLibraryService;
import mat.server.util.MATPropertiesService;
import mat.server.util.QDMUtil;
import mat.shared.cql.model.FunctionSignature;

@Service
public class CQLConstantServiceImpl extends SpringRemoteServiceServlet implements CQLConstantService {
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
		Collections.sort(datatypeList);
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
		
		cqlConstantContainer.setQdmContainer(getQDMInformation());
		cqlConstantContainer.setCqlTypeContainer(getCQLTypeInformation());
		
		cqlConstantContainer.setFunctionSignatures(getFunctionSignatures());
			
		return cqlConstantContainer;
	}
	
	@SuppressWarnings("unchecked")
	private List<FunctionSignature> getFunctionSignatures() {		
		ClassLoader classLoader = getClass().getClassLoader();
		FunctionSignature[] signatureArray = {};
		try {
			Gson gson = new Gson();
			signatureArray = gson.fromJson(
					new FileReader(classLoader.getResource("functions/signatures.json").getFile()),
					FunctionSignature[].class
			);
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return (List<FunctionSignature>) Arrays.asList(signatureArray);
	}
	
	@SuppressWarnings("unchecked")
	private CQLTypeContainer getCQLTypeInformation() {
		CQLTypeContainer container = new CQLTypeContainer();
		
		SystemModelInfoProvider systemModelInfoProvider = new SystemModelInfoProvider();
		ModelInfo modelInfo = systemModelInfoProvider.load();
		
		Map<String, List<String>> typeToTypeAttributeMap = new HashMap<>();
		for(TypeInfo typeInfo : modelInfo.getTypeInfo()) {
			if(typeInfo instanceof ClassInfo) {
				ClassInfo currentClassInfo = (ClassInfo) typeInfo;		
				
				if(!typeToTypeAttributeMap.containsKey(currentClassInfo.getName())) {
					typeToTypeAttributeMap.put(currentClassInfo.getName(), new ArrayList<>());
				}
				
				for(ClassInfoElement attribute : currentClassInfo.getElement()) {
					typeToTypeAttributeMap.get(currentClassInfo.getName()).add(attribute.getName());
				}
			}
		}
		
		// TODO: Find a better way to do this instead of hardcoding.		
		typeToTypeAttributeMap.remove("System.Code");
		typeToTypeAttributeMap.remove("list<System.Code");
		typeToTypeAttributeMap.put("QDM.Id", Arrays.asList(new String[]{"namingSystem", "value"}));
		typeToTypeAttributeMap.put("list<QDM.Id>", Arrays.asList(new String[]{"namingSystem", "value"}));
		typeToTypeAttributeMap.put("QDM.FacilityLocation", Arrays.asList(new String[]{"code", "locationPeriod"}));
		typeToTypeAttributeMap.put("list<QDM.FacilityLocation>", Arrays.asList(new String[]{"code", "locationPeriod"}));
		typeToTypeAttributeMap.put("interval<System.DateTime>", Arrays.asList(new String[]{"low", "high"}));
		typeToTypeAttributeMap.put("interval<System.Quantity>", Arrays.asList(new String[]{"low", "high"}));
		typeToTypeAttributeMap.put("list<QDM.Component>", Arrays.asList(new String[]{"code", "referenceRange", "result"}));
		typeToTypeAttributeMap.put("list<QDM.ResultComponent>", Arrays.asList(new String[]{"code", "referenceRange", "result"}));

		container.setTypeToTypeAttributeMap(typeToTypeAttributeMap);
		return container;
	}
	
	private QDMContainer getQDMInformation() {
		return QDMUtil.getQDMContainer();
	}
}
