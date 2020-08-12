package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

public class ImprovementNotationModel extends MeasureDetailsTextAbstractModel implements IsSerializable{
	public ImprovementNotationModel() {
		super("");
	}
	
	public ImprovementNotationModel(ImprovementNotationModel model) {
		super(model.getEditorText());
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
