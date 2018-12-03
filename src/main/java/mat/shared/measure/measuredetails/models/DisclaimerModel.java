package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DisclaimerModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable {

	public DisclaimerModel() {
		
	}
	
	public DisclaimerModel(DisclaimerModel model) {
		this.setFormattedText(model.getFormattedText());
		this.setPlainText(model.getPlainText());
	}

	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		DisclaimerModel disclaimerModel = (DisclaimerModel) model;
		if(disclaimerModel == null) {
			return false;
		} else if(disclaimerModel.getFormattedText() != null && getFormattedText() != null) {
			return this.getFormattedText().equals(disclaimerModel.getFormattedText()) && 
					this.getPlainText().equals(disclaimerModel.getPlainText());
		} else {
			return disclaimerModel.getFormattedText() == null && getFormattedText() == null;
		}
	}

	@Override
	public boolean isValid() {
		return false;
	}
	
	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.visit(this);
	}
}
