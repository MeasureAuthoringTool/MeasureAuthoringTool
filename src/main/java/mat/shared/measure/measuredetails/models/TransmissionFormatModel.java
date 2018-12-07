package mat.shared.measure.measuredetails.models;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TransmissionFormatModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable {
	public TransmissionFormatModel() {
		super("", "");
	}
	
	public TransmissionFormatModel(TransmissionFormatModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}
	
	public void update(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.updatemodel(this);
	}
	
	public List<String> validateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.validateModel(this);
	}
}
