package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

/**
 * Container for holding Code System information.
 *
 * **/
public class MatConceptList implements IsSerializable {
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
