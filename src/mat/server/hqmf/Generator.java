package mat.server.hqmf;

import mat.model.clause.MeasureExport;
import mat.shared.MatConstants;

public interface Generator extends MatConstants {
	
	public static final String HIGH = "high";
	
	public static final String STOP_DATETIME = "stop datetime";
	
	public static final String START_DATETIME = "start datetime";
	
	public static final String FLAVOR_ID = "flavorId";
	
	public static final String LOW = "low";
	
	public static final String EFFECTIVE_TIME = "effectiveTime";	
	
	public static final String ATTRIBUTE_UUID = "attributeUUID";
	
	public static final String RELATED_TO = "related to";
	
	public static final String CHECK_IF_PRESENT = "Check if Present";
	
	public static final String TYPE = "type";
	
	public static final String GROUPING_CHECK = "isInGrouping";
	
	public static final String MOOD = "mood";
	
	public static final String CLASS = "class";
	
	public static final String XSI_TYPE = "xsi:type";
	
	public static final String VALUE = "value";
	
	public static final String TITLE = "title";
	
	public static final String DISPLAY_NAME = "displayName";
	
	public static final String CODE_SYSTEM = "codeSystem";
	
	public static final String CODE_SYSTEM_NAME = "codeSystemName";
	
	public static final String CODE_SYSTEM_DISPLAY_NAME = "codeDisplayName";
	
	public static final String ID = "id";
	
	public static final String ROOT = "root";
	
	public static final String ITEM = "item";
	
	public static final String TEMPLATE_ID = "templateId";
	
	public static final String MOOD_CODE = "moodCode";
	
	public static final String CLASS_CODE = "classCode";
	
	public static final String TYPE_CODE = "typeCode";
	
	public static final String RAV = "riskAdjVar";
	
	public static final String OBSERVATION_CRITERIA = "observationCriteria";
	
	public static final String OUTBOUND_RELATIONSHIP = "outboundRelationship";
	
	public static final String UUID = "uuid";
	
	public static final String TAXONOMY = "taxonomy";
	
	public static final String OID = "oid";
	
	public static final String NAME = "name";
	
	public static final String CODE = "code";
	
	public static final String VERSION_5_0_ID = "2017-05-01";
		
	public static final String VERSION_4_1_2_ID = "2014-11-24";
	
	public static final String VERSION_4_3_ID = "2015-09-30";
	
	public static final String POPULATION_CRITERIA_EXTENSION = "2015-12-01";
	
	public static final String POPULATION_CRITERIA_EXTENSION_CQL = "2017-08-01";
	
	public static final String VALUE_SET = "Value Set";
	
	public static final String ANATOMICAL_LOCATION_SITE = "Anatomical Location Site";
	
	public static final String ANATOMICAL_APPROACH_SITE = "Anatomical Approach Site";

	public static final String ORDINALITY = "Ordinality";
	
	public static final String LATERALITY = "Laterality";
	
	public static final String ROUTE = "route";
	
	public static final String FACILITY_LOCATION = "facility location";
	
	public static final String FACILITY_LOCATION_ARRIVAL_DATETIME = "facility location arrival datetime";
	
	public static final String FACILITY_LOCATION_DEPARTURE_DATETIME = "facility location departure datetime";
	
	public static final String REFILLS = "refills";
	
	public static final String CUMULATIVE_MEDICATION_DURATION = "cumulative medication duration";
	
	public static final String FREQUENCY = "frequency";
	
	public static final String ADMISSION_DATETIME = "admission datetime";

	public static final String DISCHARGE_STATUS ="discharge status";

	public static final String DISCHARGE_DATETIME = "discharge datetime";
	
	public static final String REMOVAL_DATETIME = "removal datetime";
	
	public static final String INCISION_DATETIME = "incision datetime";
	
	public static final String SIGNED_DATETIME = "signed datetime";
	
	public static final String ACTIVE_DATETIME = "active datetime";
	
	public static final String TIME = "time";
	
	public static final String DATE = "date";
	
	public static final String ATTRIBUTE_MODE = "attributeMode";
	
	public static final String ATTRIBUTE_NAME = "attributeName";
	
	public static final String NEGATION_RATIONALE = "negation rationale";
	
	public static final String ATTRIBUTE_DATE = "attrDate";
	
	public final String nameSpace = "http://www.w3.org/2001/XMLSchema-instance";
	
	public static final String LESS_THAN = "Less Than";
	
	public static final String GREATER_THAN = "Greater Than";
	
	public static final String EQUAL_TO = "Equal To";
	
	public static final String DOSE = "dose";
	
	public static final String LENGTH_OF_STAY = "length of stay";
	
	public static final String TRANSLATION = "translation";
	
	public static final String NULL_FLAVOR ="nullFlavor";
	
	public static final String RADIATION_DURATION = "radiation duration";
	
	public static final String RADIATION_DOSAGE = "radiation dosage";
	
	public static final String STATUS_CODE = "statusCode";
	
	public static final String ONSET_DATETIME = "onset datetime";
	
	public static final String ABATEMENT_DATETIME = "abatement datetime";
	
	public static final String RECORDED_DATETIME = "recorded datetime";
	
	public static final String REPEAT_NUMBER = "repeatNumber";
	
	public static final String ONSET_AGE = "Onset Age";
	
	public static final String REFERENCE = "reference";
	
	public static final String RELATIONSHIP = "relationship";
	
	public static final String DIAGNOSIS = "diagnosis";
	
	public static final String PRINCIPAL_DIAGNOSIS = "principal diagnosis";
	
	public static final String ACTION_NEGATION_IND = "actionNegationInd";
	
	public String generate(MeasureExport me) throws Exception;
}