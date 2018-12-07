package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TransmissionFormatModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable {
	public TransmissionFormatModel() {
		super("", "");
	}
	
	public TransmissionFormatModel(TransmissionFormatModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}
	
	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.visit(this);
	}
}
