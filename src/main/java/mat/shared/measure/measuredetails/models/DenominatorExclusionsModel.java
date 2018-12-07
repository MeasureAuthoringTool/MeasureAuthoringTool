package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DenominatorExclusionsModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable {
	public DenominatorExclusionsModel() {
		super("", "");
	}
	
	public DenominatorExclusionsModel(DenominatorExclusionsModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}
	
	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.visit(this);
	}
}
