package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RateAggregationModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable{

	public RateAggregationModel() {
		super("", "");
	}
	
	public RateAggregationModel(RateAggregationModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}
	
	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.visit(this);
	}
}
