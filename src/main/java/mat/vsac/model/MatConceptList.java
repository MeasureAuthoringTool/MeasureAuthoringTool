package mat.vsac.model;

import java.io.Serializable;
import java.util.List;

/**
 * Container for holding Code System information.
 *
 * **/
public class MatConceptList implements Serializable {
	/**
	 * Container for holding Code System information.
	 *
	 * **/
private List<MatConcept> conceptList;

/**
 * Getter - conceptList.
 * @return - conceptList.
 *
 * **/
public final List<MatConcept> getConceptList() {
return conceptList;
}

/**
 * Setter - conceptList.
 * @param conceptLists -List of MatConcept.
 *
 * **/
public final void setConceptList(final List<MatConcept> conceptLists) {
this.conceptList = conceptLists;
}

}
