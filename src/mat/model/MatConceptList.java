package mat.model;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MatConceptList implements IsSerializable {
	private List<MatConcept> conceptList;

	public List<MatConcept> getConceptList() {
		return conceptList;
	}
	public void setConceptList(List<MatConcept> conceptList) {
		this.conceptList = conceptList;
	}

}
