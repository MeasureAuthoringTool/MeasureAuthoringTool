package mat.shared.measure.measuredetails.models;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ReferencesModel implements MeasureDetailsComponentModel, IsSerializable{
	private List<String> references;

	public ReferencesModel(ReferencesModel originalModel) {
		references = new LinkedList<String>();
		if(originalModel != null && originalModel.getReferences() != null) {
			for(String reference: originalModel.getReferences()) {
				references.add(reference);
			}
		}
	}

	public ReferencesModel() {
	}

	public List<String> getReferences() {
		return references;
	}

	public void setReferences(List<String> references) {
		this.references = references;
	}

	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		ReferencesModel originalModel = (ReferencesModel) model;
		if(originalModel.getReferences() != null) {
			return (originalModel.getReferences().size() == getReferences().size()) && originalModel.getReferences().stream().allMatch(str -> getReferences().contains(str));
		} else {
			return getReferences().isEmpty();
		}
	}

	public void update(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.updateModel(this);
	}
	
	public List<String> validateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.validateModel(this);
	}

	@Override
	public boolean isDirty(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.isDirty(this);
	}
}
