package mat.model.cql;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Container for holding Code System information.
 *
 * **/
public class CQLMatConceptList implements IsSerializable {
	/**
	 * Container for holding Code System information.
	 *
	 * **/
private List<CQLMatConcept> conceptList;

/**
 * Getter - conceptList.
 * @return - conceptList.
 *
 * **/
public final List<CQLMatConcept> getConceptList() {
return conceptList;
}

/**
 * Setter - conceptList.
 * @param conceptLists -List of MatConcept.
 *
 * **/
public final void setConceptList(final List<CQLMatConcept> conceptLists) {
this.conceptList = conceptLists;
}

}
