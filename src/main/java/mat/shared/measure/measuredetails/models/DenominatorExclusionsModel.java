package mat.shared.measure.measuredetails.models;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DenominatorExclusionsModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable {
	public DenominatorExclusionsModel() {
		super("", "");
	}
	
	public DenominatorExclusionsModel(DenominatorExclusionsModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}
	
	public void update(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.updateModel(this);
	}
	
	public List<String> validateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.validateModel(this);
	}
}
