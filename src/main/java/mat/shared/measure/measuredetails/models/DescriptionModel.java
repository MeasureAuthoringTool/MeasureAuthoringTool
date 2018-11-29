package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DescriptionModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable {
	
	public DescriptionModel() {

	}
	
	public DescriptionModel(DescriptionModel model) {
		this.setFormattedText(model.getFormattedText());
		this.setPlainText(model.getPlainText());
	}
	
	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		DescriptionModel descriptionModel = (DescriptionModel) model;
		if(descriptionModel == null || model == null) {
			return false;
		}
		return this.getFormattedText().equals(descriptionModel.getFormattedText()) && 
					this.getPlainText().equals(descriptionModel.getPlainText());
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
