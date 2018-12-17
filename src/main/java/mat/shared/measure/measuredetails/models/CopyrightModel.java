package mat.shared.measure.measuredetails.models;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CopyrightModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable {

	public CopyrightModel() {
		super("", "");
	}
	
	public CopyrightModel(CopyrightModel model) {
		super(model.getPlainText(), model.getFormattedText());
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
