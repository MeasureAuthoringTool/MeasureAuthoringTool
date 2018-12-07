package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MeasurePopulationModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable{
	public MeasurePopulationModel() {
		super("", "");
	}
	
	public MeasurePopulationModel(MeasurePopulationModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}
	
	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.visit(this);
	}
}
