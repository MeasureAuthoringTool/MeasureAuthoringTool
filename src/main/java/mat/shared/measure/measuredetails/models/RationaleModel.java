package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

public class RationaleModel extends MeasureDetailsTextAbstractModel implements IsSerializable{
	public RationaleModel() {
		super("");
	}
	
	public RationaleModel(RationaleModel model) {
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
