package mat.server.simplexml.hqmf;

import mat.model.clause.MeasureExport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public interface Generator {
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
	
	public static final String OBSERVATION_CRITERIA = "observationCriteria";
	
	public static final String OUTBOUND_RELATIONSHIP = "outboundRelationship";
	
	public static final String UUID = "uuid";
	
	public static final String TAXONOMY = "taxonomy";
	
	public static final String OID = "oid";
	
	public static final String NAME = "name";
	
	public static final String CODE = "code";
	
	public static final String VERSIONID = "2014-11-24";
	
	public static final String VALUE_SET = "Value Set";
	
	public static final String ANATOMICAL_LOCATION_SITE = "Anatomical Location Site";
	
	public static final String ORDINALITY = "Ordinality";
	
	public static final String LATERALITY = "Laterality";
	
	public static final String ROUTE = "route";
	
	public static final String FACILITY_LOCATION = "facility location";
	
	public static final String FACILITY_LOCATION_ARRIVAL_DATETIME = "facility location arrival datetime";
	
	public static final String FACILITY_LOCATION_DEPARTURE_DATETIME = "facility location departure datetime";
	
	public static final String ATTRIBUTE_MODE = "attributeMode";
	
	public static final String ATTRIBUTE_NAME = "attributeName";
	
	public static final String NEGATION_RATIONALE = "negation rationale";
	
	public static final String ATTRIBUTE_DATE = "attrDate";
	/** The Constant logger. */
	public final Log LOG = LogFactory.getLog(HQMFDataCriteriaGenerator.class);
	
	/** The name space. */
	public final String nameSpace = "http://www.w3.org/2001/XMLSchema-instance";
	
	public static final String LESS_THAN = "Less Than";
	
	public static final String GREATER_THAN = "Greater Than";
	
	public static final String EQUAL_TO = "Equal To";
	public abstract String generate(MeasureExport me) throws Exception;
	
}