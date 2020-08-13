package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

public class DisclaimerModel extends MeasureDetailsTextAbstractModel implements IsSerializable {

	public DisclaimerModel() {
		super("");
	}
	
	public DisclaimerModel(DisclaimerModel model) {
		super(model.getEditorText());
	}

	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		DisclaimerModel disclaimerModel = (DisclaimerModel) model;
		if(disclaimerModel == null) {
			return false;
		} else if(disclaimerModel.getEditorText() != null && getEditorText() != null) {
			return this.getEditorText().equals(disclaimerModel.getEditorText());
		} else {
			return disclaimerModel.getEditorText() == null && getEditorText() == null;
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
