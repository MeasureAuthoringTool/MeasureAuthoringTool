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
	
	QdmModelInfoProvider qdmModelInfoProvider = new QdmModelInfoProvider();
	
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
		QDMContainer container = new QDMContainer();
		ModelInfo modelInfo = qdmModelInfoProvider.load();
		
		Map<String, TypeInfo> nameToProfileInfoMap = new HashMap<>();
		Map<String, ClassInfo> nameToClassInfoMap = new HashMap<>();

		// pre-process this information to be more efficient
		List<TypeInfo> typeInfos = modelInfo.getTypeInfo();
		for(TypeInfo typeInfo : typeInfos) {
			
			if(typeInfo instanceof ProfileInfo) {
				ProfileInfo currentProfileInfo = (ProfileInfo) typeInfo;
				nameToProfileInfoMap.put(currentProfileInfo.getName(), currentProfileInfo);
			}
		}
		
		for(TypeInfo typeInfo : typeInfos) {
			if(typeInfo instanceof ClassInfo) {
				ClassInfo currentClassInfo = (ClassInfo) typeInfo;
				nameToClassInfoMap.put(currentClassInfo.getName(), currentClassInfo);
				
				if(currentClassInfo.getLabel() != null) {
					nameToProfileInfoMap.put(currentClassInfo.getName(), currentClassInfo);
				}
				
			}
		}
				
		Map<String, List<String>> dataTypeToAttributeMap = new HashMap<>();
		Map<String, List<String>> attributeToCQLTypeMap = new HashMap<>();
		// create the data type label --> attribute map
		nameToProfileInfoMap.forEach((k, v) -> {
			String label = ((ClassInfo) v).getLabel(); // e.g. Encounter, Performed
			String baseType = v.getBaseType(); // e.g. QDM.EncounterPerformed
			
			// this accounts for elements that are not a profile
			// for example QDM.CareGoal is not a profile and has a base type of QDM.QDMBaseType
			// we still want to collect information about it, but we do not want attributes for the base type, but
			// rather we want attributes for the current datatype so we will use it's name (QDM.CareGoal) so that
			// we can look up information about the current datatypes attribute.
			
			// We only want to use base type information if it is a profile. For example, QDM.PositiveEncounterPerformed
			// has a base type of QDM.EncounterPerformed. We can than use this base type to look up the attributes for 
			// that datatype.
			if(baseType.equals("QDM.QDMBaseType")) {
				baseType = ((ClassInfo) v).getName();
			}
					
			ClassInfo classInfo = nameToClassInfoMap.get(baseType);
			if(!dataTypeToAttributeMap.containsKey(label)) {
				dataTypeToAttributeMap.put(label, new ArrayList<>());
			}
			
			List<ClassInfoElement> attributesForDataType = new ArrayList<>();
			attributesForDataType.addAll(classInfo.getElement()); // adds data type specific attributes
			attributesForDataType.addAll(nameToClassInfoMap.get("QDM.QDMBaseType").getElement()); // add base type attributes
			
			if(classInfo.getElement() != null) {
				for(ClassInfoElement attribute : attributesForDataType) {
					dataTypeToAttributeMap.get(label).add(attribute.getName());					
					collectAttributeTypeInformation(attributeToCQLTypeMap, label, attribute);					
				}		
			}
		});
			
		container.setDatatypeToAttributesMap(dataTypeToAttributeMap);
		container.setQdmAttributeToTypeMap(attributeToCQLTypeMap);
		return container;
	}

	private void collectAttributeTypeInformation(Map<String, List<String>> attributeToCQLTypeMap, String label,
			ClassInfoElement attribute) {
		if(!attributeToCQLTypeMap.containsKey(label)) {
			attributeToCQLTypeMap.put(attribute.getName(), new ArrayList<>());
		}
		
		// handles the case where the attribute has a type of choice type specifier
		if(attribute.getTypeSpecifier() != null) {
			getAttributesFromChoiceTypeSpecifier(attributeToCQLTypeMap, attribute);
		} 
		
		// handle the case where it is a normal attribute
		else {
			
			// we shouldn't put clarifying attributes for System.Code
			attributeToCQLTypeMap.get(attribute.getName()).add(attribute.getType());
			
		}
	}

	private void getAttributesFromChoiceTypeSpecifier(Map<String, List<String>> attributeToCQLTypeMap,
			ClassInfoElement attribute) {
		ChoiceTypeSpecifier specifier = (ChoiceTypeSpecifier) attribute.getTypeSpecifier();
		for(TypeSpecifier choice: specifier.getChoice()) {
			NamedTypeSpecifier type = (NamedTypeSpecifier) choice;
			String formattedType = type.getModelName()+ "." + type.getName();
			
			// we shouldn't put clarifying attributes for System.Code
			attributeToCQLTypeMap.get(attribute.getName()).add(formattedType);
		}	
	}
}
