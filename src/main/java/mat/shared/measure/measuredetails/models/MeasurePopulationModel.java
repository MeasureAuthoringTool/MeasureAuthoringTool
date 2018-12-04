package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MeasurePopulationModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable{
	public MeasurePopulationModel() {

	}
	
	public MeasurePopulationModel(MeasurePopulationModel model) {
		this.setFormattedText(model.getFormattedText());
		this.setPlainText(model.getPlainText());
	}
	
	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		MeasurePopulationModel measurePopulationModel = (MeasurePopulationModel) model;
		if(measurePopulationModel == null) {
			return false;
		} else if(measurePopulationModel.getFormattedText() != null && getFormattedText() != null) {
			return this.getFormattedText().equals(measurePopulationModel.getFormattedText()) && 
					this.getPlainText().equals(measurePopulationModel.getPlainText());
		} else {
			return measurePopulationModel.getFormattedText() == null && getFormattedText() == null;
		}
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
