package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CopyrightModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable {

	public CopyrightModel() {

	}
	
	public CopyrightModel(CopyrightModel model) {
		this.setFormattedText(model.getFormattedText());
		this.setPlainText(model.getPlainText());
	}

	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		CopyrightModel copyrightModel = (CopyrightModel) model;
		if(copyrightModel == null) {
			return false;
		} else if(copyrightModel.getFormattedText() != null && getFormattedText() != null) {
			return this.getFormattedText().equals(copyrightModel.getFormattedText()) && 
					this.getPlainText().equals(copyrightModel.getPlainText());
		} else {
			return copyrightModel.getFormattedText() == null && getFormattedText() == null;
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
