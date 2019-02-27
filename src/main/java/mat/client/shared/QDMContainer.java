package mat.client.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

public class QDMContainer implements IsSerializable {

	private Map<String, List<String>> datatypeToAttributesMap = new HashMap<>();
	private Map<String, List<String>> qdmAttributeToTypeMap = new HashMap<>();
	
	public QDMContainer() {
		
	}
	
	public QDMContainer(Map<String, List<String>> datatypeToAttributesMap) {
		this.datatypeToAttributesMap = datatypeToAttributesMap;
	}
	
	public Map<String, List<String>> getDatatypeToAttributesMap() {
		return this.datatypeToAttributesMap;
	}
	
	public void setDatatypeToAttributesMap(Map<String, List<String>> datatypeToAttributesMap ) {
		this.datatypeToAttributesMap = datatypeToAttributesMap;
	}
	
	public Map<String, List<String>> getQdmAttributeToTypeMap() {
		return qdmAttributeToTypeMap;
	}

	public void setQdmAttributeToTypeMap(Map<String, List<String>> qdmAttributeToTypeMap) {
		this.qdmAttributeToTypeMap = qdmAttributeToTypeMap;
	}
	
	/**
	 * Gets the attributes for a given datatype
	 * @param datatype the datatype to find by, e.g. Encounter, Performed
	 * @return the list of attributes for that datatype
	 */
	public List<String> getAttributesByDatatype(String datatype) {
		return this.datatypeToAttributesMap.get(datatype);
	}
	
	/**
	 * Gets a list of all distinct attributes 
	 */
	public List<String> getAttributes() {
		Set<String> attributes = new HashSet<>();
		datatypeToAttributesMap.forEach((k, v) -> {
			attributes.addAll(v);
		});
		
		List<String> attributesList = new ArrayList<>(attributes);
		attributesList.sort((a1, a2)-> a1.compareTo(a2));
		return attributesList;
	}
	
	/**
	 * Gets a list of all distinct qdm datatypes 
	 * @return
	 */
	public List<String> getDatatypes() {
		return new ArrayList<>(datatypeToAttributesMap.keySet());
	}

	public List<String> getCQLTypeByAttribute(String attribute) {
		return this.qdmAttributeToTypeMap.get(attribute);
	}
}
