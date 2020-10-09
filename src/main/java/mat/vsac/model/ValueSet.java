package mat.vsac.model;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Container for holding VSAC value set.
 *
 * **/

public class ValueSet  implements Serializable {
    /**
    * VSAC value set ID.
	*
	 * **/
	private String ID;
   /**
    * Measure XML QDM ID.
	*
    * **/
	private String qdmId;
	/**
	 * VSAC value set Display name.
	 *
	 * **/
	private String displayName;
	/**
	 * VSAC value set version.
	 *
	 * **/
	private String version;
	/**
	 * VSAC value set conceptList.
	 *
	 * **/
	private MatConceptList conceptList;
	/**
	 * VSAC value set source.
	 *
	 * **/
	private String source;
	/**
	 * VSAC value set Type.
	 *
	 * **/
	private String type;
	/**
	 * VSAC value set Binding.
	 *
	 * **/
	private String binding;
	/**
	 * VSAC value set Status.
	 *
	 * **/
	private String status;
	/**
	 * VSAC value set Revision Date.
	 *
	 * **/
	private String revisionDate;
	/**
	 * VSAC value set Definition.
	 *
	 * **/
	private String definition;
	/**
	 * VSAC value set Group List.
	 *
	 * **/
	private List<MatGroup> groupList;
	/**
	 * VSAC value set Grouped Value Set.
	 *
	 * **/
	private List<ValueSet> groupedValueSet;
	
	private String expansionProfile;

	/**
	 * To string.
	 * 
	 * @return String with ID , Display name and Type.
	 * 
	 *         *
	 */

	public final String toString() {
		return getID() + " - " + getDisplayName() + " - " + getType();
	}

	/**
	 * Checks if is grouping.
	 * 
	 * @return Boolean.
	 * 
	 *         *
	 */
	public final boolean isGrouping() {
		return (getType().equalsIgnoreCase("grouping"));
	}
	
	/**
	 * getCodeSystemName method returns the code system name of the first concept in this "conceptList" object.
	 * This method is used to get code system name for "Extensional" Type ValueSet.
	 * For "Grouping" Type ValueSet, iterate through this groupedValueSet list object and call getCodeSystemName() for each object in groupedValueSet list.
	 * @return String Code System Name.
	 */
	public String getCodeSystemName() {
		if (getConceptList() != null) {
			List<MatConcept> matConcepts = getConceptList().getConceptList();
			if (matConcepts != null && !matConcepts.isEmpty()) {
				if (matConcepts.get(0) != null) {
					return matConcepts.get(0).getCodeSystemName();
				}
			}
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Getter -ID.
	 * @return OID.
	 *
	 * **/

	public final String getID() {
		return ID;
	}

	/**
	 *Setter for ID.
	 *@param iD - OID.
	 * **/
	public final void setID(final String iD) {
		ID = iD;
	}

	/**
	 * Getter -displayName.
	 * @return displayName.
	 *
	 * **/
	public final String getDisplayName() {
		return displayName;
	}

	/**
	 *Setter for displayName.
	 *@param vDisplayName - displayName.
	 * **/
	public final void setDisplayName(final String vDisplayName) {
		this.displayName = vDisplayName;
	}

	/**
	 * Getter -version.
	 * @return version.
	 *
	 * **/
	public final String getVersion() {
		return version;
	}

	/**
	 *Setter for version.
	 *@param vVersion - version.
	 * **/
	public final void setVersion(final String vVersion) {
		this.version = vVersion;
	}

	/**
	 * Getter -conceptList.
	 * @return conceptList.
	 *
	 * **/
	public final MatConceptList getConceptList() {
		return conceptList;
	}

	/**
	 *Setter for conceptList.
	 *@param conceptLists - conceptList.
	 * **/
	public final void setConceptList(final MatConceptList conceptLists) {
		this.conceptList = conceptLists;
	}

	/**
	 * Getter -source.
	 * @return source.
	 *
	 * **/
	public final String getSource() {
		return source;
	}

	/**
	 *Setter for source.
	 *@param vSource - source.
	 * **/
	public final void setSource(final String vSource) {
		this.source = vSource;
	}

	/**
	 * Getter -type.
	 * @return type.
	 *
	 * **/
	public final String getType() {
		return type;
	}

	/**
	 *Setter for type.
	 *@param vType - type.
	 * **/
	public final void setType(final String vType) {
		this.type = vType;
	}

	/**
	 * Getter -binding.
	 * @return binding.
	 *
	 * **/
	public final String getBinding() {
		return binding;
	}

	/**
	 *Setter for binding.
	 *@param vBinding - binding.
	 * **/
	public final void setBinding(final String vBinding) {
		this.binding = vBinding;
	}

	/**
	 * Getter -status.
	 * @return status.
	 *
	 * **/
	public final String getStatus() {
		return status;
	}

	/**
	 *Setter for status.
	 *@param vStatus - status.
	 * **/
	public final void setStatus(final String vStatus) {
		this.status = vStatus;
	}

	/**
	 * Getter -revisionDate.
	 * @return revisionDate.
	 *
	 * **/
	public final String getRevisionDate() {
		return revisionDate;
	}

	/**
	 *Setter for revisionDate.
	 *@param revisionDates - revisionDate.
	 * **/
	public final void setRevisionDate(final String revisionDates) {
		this.revisionDate = revisionDates;
	}

	/**
	 * Getter -groupList.
	 * @return groupList.
	 *
	 * **/
	public final List<MatGroup> getGroupList() {
		return groupList;
	}

	/**
	 *Setter for groupList.
	 *@param groupLists - groupList.
	 * **/
	public final void setGroupList(final List<MatGroup> groupLists) {
		this.groupList = groupLists;
	}

	/**
	 * Getter -ID.
	 * @return OID.
	 *
	 * **/
	public final String getDefinition() {
		return definition;
	}

	/**
	 *Setter for definition.
	 *@param definitions - definition.
	 * **/
	public final void setDefinition(final String definitions) {
		this.definition = definitions;
	}

	/**
	 * Getter -groupedValueSet.
	 * @return groupedValueSet.
	 *
	 * **/
	public final List<ValueSet> getGroupedValueSet() {
		return groupedValueSet;
	}

	/**
	 *Setter for groupedValueSet.
	 *@param groupedValueSets - ValueSet.
	 * **/
	public final void setGroupedValueSet(final List<ValueSet> groupedValueSets) {
		this.groupedValueSet = groupedValueSets;
	}

	/**
	 * Gets the measure XML QDM ID.
	 * 
	 * @return the measure XML QDM ID
	 */
	public String getQdmId() {
		return qdmId;
	}

	/**
	 * Sets the measure XML QDM ID.
	 * 
	 * @param qdmId
	 *            the new measure XML QDM ID
	 */
	public void setQdmId(String qdmId) {
		this.qdmId = qdmId;
	}

	public String getExpansionProfile() {
		return expansionProfile;
	}

	public void setExpansionProfile(String expansionProfile) {
		this.expansionProfile = expansionProfile;
	}
}