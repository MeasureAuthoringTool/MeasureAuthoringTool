package mat.shared.measure.measuredetails.models;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MeasurePopulationExclusionsModel extends MeasureDetailsTextAbstractModel implements IsSerializable{
	public MeasurePopulationExclusionsModel() {
		super("");
	}
	
	public MeasurePopulationExclusionsModel(MeasurePopulationExclusionsModel model) {
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
