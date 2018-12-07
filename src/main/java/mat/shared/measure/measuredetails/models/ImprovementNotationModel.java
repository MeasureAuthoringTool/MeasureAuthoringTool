package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ImprovementNotationModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable{

	public ImprovementNotationModel() {
		super("", "");
	}
	
	public ImprovementNotationModel(ImprovementNotationModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}
	
	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.visit(this);
	}
}
