package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class StratificationModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable{
	public StratificationModel() {
		super("", "");
	}
	
	public StratificationModel(StratificationModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}
	
	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.visit(this);
	}
}
