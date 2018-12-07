package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DenominatorModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable{

	public DenominatorModel() {
		super("", "");
	}
	
	public DenominatorModel(DenominatorModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}
	
	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.visit(this);
	}
}
