package mat.shared.measure.measuredetails.models;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InitialPopulationModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable {

	public InitialPopulationModel() {
		super("", "");
	}
	
	public InitialPopulationModel(MeasureDetailsRichTextAbstractModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}
	
	public void update(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.updateModel(this);
	}

	public List<String> validateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.validateModel(this);
	}
}
