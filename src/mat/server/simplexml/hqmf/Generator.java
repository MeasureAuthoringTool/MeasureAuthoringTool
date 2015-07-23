package mat.server.simplexml.hqmf;

import mat.model.clause.MeasureExport;
import mat.shared.MatConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * The Interface Generator.
 */
public interface Generator extends MatConstants {
	
	/** The Constant HIGH. */
	public static final String HIGH = "high";
	
	/** The Constant STOP_DATETIME. */
	public static final String STOP_DATETIME = "stop datetime";
	
	/** The Constant START_DATETIME. */
	public static final String START_DATETIME = "start datetime";
	
	/** The Constant FLAVOR_ID. */
	public static final String FLAVOR_ID = "flavorId";
	
	/** The Constant LOW. */
	public static final String LOW = "low";
	
	/** The Constant EFFECTIVE_TIME. */
	public static final String EFFECTIVE_TIME = "effectiveTime";
	
	/** The Constant ATTRIBUTE_UUID. */
	public static final String ATTRIBUTE_UUID = "attributeUUID";
	
	/** The Constant RELATED_TO. */
	public static final String RELATED_TO = "related to";
	
	/** The Constant CHECK_IF_PRESENT. */
	public static final String CHECK_IF_PRESENT = "Check if Present";
	
	/** The Constant TYPE. */
	public static final String TYPE = "type";
	
	/** The Constant MOOD. */
	public static final String MOOD = "mood";
	
	/** The Constant CLASS. */
	public static final String CLASS = "class";
	
	/** The Constant XSI_TYPE. */
	public static final String XSI_TYPE = "xsi:type";
	
	/** The Constant VALUE. */
	public static final String VALUE = "value";
	
	/** The Constant TITLE. */
	public static final String TITLE = "title";
	
	/** The Constant DISPLAY_NAME. */
	public static final String DISPLAY_NAME = "displayName";
	
	/** The Constant CODE_SYSTEM. */
	public static final String CODE_SYSTEM = "codeSystem";
	
	/** The Constant CODE_SYSTEM_NAME. */
	public static final String CODE_SYSTEM_NAME = "codeSystemName";
	
	/** The Constant CODE_SYSTEM_DISPLAY_NAME. */
	public static final String CODE_SYSTEM_DISPLAY_NAME = "codeDisplayName";
	
	/** The Constant ID. */
	public static final String ID = "id";
	
	/** The Constant ROOT. */
	public static final String ROOT = "root";
	
	/** The Constant ITEM. */
	public static final String ITEM = "item";
	
	/** The Constant TEMPLATE_ID. */
	public static final String TEMPLATE_ID = "templateId";
	
	/** The Constant MOOD_CODE. */
	public static final String MOOD_CODE = "moodCode";
	
	/** The Constant CLASS_CODE. */
	public static final String CLASS_CODE = "classCode";
	
	/** The Constant TYPE_CODE. */
	public static final String TYPE_CODE = "typeCode";
	
	/**
	 * The Constant RAV
	 */
	public static final String RAV = "riskAdjVar";
	
	/** The Constant OBSERVATION_CRITERIA. */
	public static final String OBSERVATION_CRITERIA = "observationCriteria";
	
	/** The Constant OUTBOUND_RELATIONSHIP. */
	public static final String OUTBOUND_RELATIONSHIP = "outboundRelationship";
	
	/** The Constant UUID. */
	public static final String UUID = "uuid";
	
	/** The Constant TAXONOMY. */
	public static final String TAXONOMY = "taxonomy";
	
	/** The Constant OID. */
	public static final String OID = "oid";
	
	/** The Constant NAME. */
	public static final String NAME = "name";
	
	/** The Constant CODE. */
	public static final String CODE = "code";
	
	/** The Constant VERSIONID. */
	public static final String VERSIONID = "2014-11-24";
	
	/** The Constant VALUE_SET. */
	public static final String VALUE_SET = "Value Set";
	
	/** The Constant ANATOMICAL_LOCATION_SITE. */
	public static final String ANATOMICAL_LOCATION_SITE = "Anatomical Location Site";
	
	/** The Constant ANATOMICAL_APPROACH_SITE. */
	public static final String ANATOMICAL_APPROACH_SITE = "Anatomical Approach Site";
	/** The Constant ORDINALITY. */
	public static final String ORDINALITY = "Ordinality";
	
	/** The Constant LATERALITY. */
	public static final String LATERALITY = "Laterality";
	
	/** The Constant ROUTE. */
	public static final String ROUTE = "route";
	
	/** The Constant FACILITY_LOCATION. */
	public static final String FACILITY_LOCATION = "facility location";
	
	/** The Constant FACILITY_LOCATION_ARRIVAL_DATETIME. */
	public static final String FACILITY_LOCATION_ARRIVAL_DATETIME = "facility location arrival datetime";
	
	/** The Constant FACILITY_LOCATION_DEPARTURE_DATETIME. */
	public static final String FACILITY_LOCATION_DEPARTURE_DATETIME = "facility location departure datetime";
	
	/** The Constant REFILLS. */
	public static final String REFILLS = "refills";
	
	/** The Constant CUMULATIVE_MEDICATION_DURATION. */
	public static final String CUMULATIVE_MEDICATION_DURATION = "cumulative medication duration";
	
	/** The Constant FREQUENCY. */
	public static final String FREQUENCY = "frequency";
	
	/** The Constant ADMISSION_DATETIME. */
	public static final String ADMISSION_DATETIME = "admission datetime";
	/**
	 * The Constant DISCHARGE_STATUS.
	 */
	public static final String DISCHARGE_STATUS ="discharge status";
	/** The Constant DEPARTURE_DATETIME. */
	public static final String DISCHARGE_DATETIME = "discharge datetime";
	
	/** The Constant REMOVAL_DATETIME. */
	public static final String REMOVAL_DATETIME = "removal datetime";
	
	/** The Constant INCISION_DATETIME. */
	public static final String INCISION_DATETIME = "incision datetime";
	
	/** The Constant SIGNED_DATETIME. */
	public static final String SIGNED_DATETIME = "signed datetime";
	
	/** The Constant ACTIVE_DATETIME. */
	public static final String ACTIVE_DATETIME = "active datetime";
	
	public static final String TIME = "time";
	
	public static final String DATE = "date";
	
	/** The Constant ATTRIBUTE_MODE. */
	public static final String ATTRIBUTE_MODE = "attributeMode";
	
	/** The Constant ATTRIBUTE_NAME. */
	public static final String ATTRIBUTE_NAME = "attributeName";
	
	/** The Constant NEGATION_RATIONALE. */
	public static final String NEGATION_RATIONALE = "negation rationale";
	
	/** The Constant ATTRIBUTE_DATE. */
	public static final String ATTRIBUTE_DATE = "attrDate";
	/** The Constant logger. */
	public final Log LOG = LogFactory.getLog(HQMFDataCriteriaGenerator.class);
	
	/** The name space. */
	public final String nameSpace = "http://www.w3.org/2001/XMLSchema-instance";
	
	/** The Constant LESS_THAN. */
	public static final String LESS_THAN = "Less Than";
	
	/** The Constant GREATER_THAN. */
	public static final String GREATER_THAN = "Greater Than";
	
	/** The Constant EQUAL_TO. */
	public static final String EQUAL_TO = "Equal To";
	
	/** The Constant DOSE. */
	public static final String DOSE = "dose";
	
	/** The Constant DOSE_QUANTITY. */
	public static final String LENGTH_OF_STAY = "length of stay";
	
	/** The Constant TRANSLATION. */
	public static final String TRANSLATION = "translation";
	
	/** The Constant NULL_FLAVOR. */
	public static final String NULL_FLAVOR ="nullFlavor";
	
	public static final String RADIATION_DURATION = "radiation duration";
	
	public static final String RADIATION_DOSAGE = "radiation dosage";
	
	public static final String STATUS_CODE = "statusCode";
	
	/**
	 * Generate.
	 *
	 * @param me the me
	 * @return the string
	 * @throws Exception the exception
	 */
	public abstract String generate(MeasureExport me) throws Exception;
	
}