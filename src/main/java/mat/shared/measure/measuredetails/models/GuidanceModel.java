package mat.shared.measure.measuredetails.models;

import java.util.List;

public class GuidanceModel extends MeasureDetailsTextAbstractModel{
	public GuidanceModel() {
		super("");
	}
	
	public GuidanceModel(GuidanceModel model) {
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
