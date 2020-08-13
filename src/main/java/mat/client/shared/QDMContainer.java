package mat.client.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class QDMContainer implements IsSerializable {

	private static final String SYSTEM_QUANTITY = "System.Quantity";
	private Map<String, List<String>> datatypeToAttributesMap = new HashMap<>();
	private Map<String, List<String>> qdmAttributeToTypeMap = new HashMap<>();

	public QDMContainer() {

	}

	public QDMContainer(final Map<String, List<String>> datatypeToAttributesMap) {
		this.datatypeToAttributesMap = datatypeToAttributesMap;
	}

	public Map<String, List<String>> getDatatypeToAttributesMap() {
		return datatypeToAttributesMap;
	}

	public void setDatatypeToAttributesMap(final Map<String, List<String>> datatypeToAttributesMap ) {
		this.datatypeToAttributesMap = datatypeToAttributesMap;
	}

	public Map<String, List<String>> getQdmAttributeToTypeMap() {
		return qdmAttributeToTypeMap;
	}

	public void setQdmAttributeToTypeMap(final Map<String, List<String>> qdmAttributeToTypeMap) {
		this.qdmAttributeToTypeMap = qdmAttributeToTypeMap;
	}

	/**
	 * Gets the attributes for a given datatype
	 * @param datatype the datatype to find by, e.g. Encounter, Performed
	 * @return the list of attributes for that datatype
	 */
	public List<String> getAttributesByDatatype(final String datatype) {
		return datatypeToAttributesMap.get(datatype);
	}

	/**
	 * Gets a list of all distinct attributes
	 */
	public List<String> getAttributes() {
		final Set<String> attributes = new HashSet<>();
		datatypeToAttributesMap.forEach((k, v) -> attributes.addAll(v));

		final List<String> attributesList = new ArrayList<>(attributes);
		attributesList.addAll(getSubAttributesList());
		attributesList.sort(Comparator.naturalOrder());
		return attributesList;
	}

	/**
	 * Gets a list of all distinct qdm datatypes
	 * @return
	 */
	public List<String> getDatatypes() {
		return new ArrayList<>(datatypeToAttributesMap.keySet());
	}

	public List<String> getCQLTypeByAttribute(final String attribute) {
		return qdmAttributeToTypeMap.get(attribute);
	}

	private List<String> getSubAttributesList(){
		qdmAttributeToTypeMap.put("locationPeriod", Arrays.asList("interval<System.DateTime>"));
		qdmAttributeToTypeMap.put("denominator", Arrays.asList(SYSTEM_QUANTITY));
		qdmAttributeToTypeMap.put("numerator", Arrays.asList(SYSTEM_QUANTITY));
		qdmAttributeToTypeMap.put("low", Arrays.asList(SYSTEM_QUANTITY));
		qdmAttributeToTypeMap.put("high", Arrays.asList(SYSTEM_QUANTITY));
		return Arrays.asList("denominator", "high", "locationPeriod", "low", "namingSystem", "numerator", "unit", "value", "presentOnAdmissionIndicator", "specialty", "role", "qualification");
	}
}
