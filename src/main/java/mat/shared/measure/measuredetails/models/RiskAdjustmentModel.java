package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RiskAdjustmentModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable {
	public RiskAdjustmentModel() {
		super("", "");
	}
	
	public RiskAdjustmentModel(RiskAdjustmentModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.visit(this);
	}
}
