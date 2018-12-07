package mat.shared.measure.measuredetails.models;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RateAggregationModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable{
	public RateAggregationModel() {
		super("", "");
	}
	
	public RateAggregationModel(RateAggregationModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}
	
	public void update(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.updateModel(this);
	}
	
	public List<String> validateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.validateModel(this);
	}
}
