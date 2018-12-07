package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MeasureObservationsModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable {

	public MeasureObservationsModel() {
		super("", "");
	}
	
	public MeasureObservationsModel(MeasureObservationsModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}
	
	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.visit(this);
	}
}
