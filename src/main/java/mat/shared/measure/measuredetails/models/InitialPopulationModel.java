package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InitialPopulationModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable{

	public InitialPopulationModel() {
		
	}
	
	public InitialPopulationModel(InitialPopulationModel model) {
		this.setFormattedText(model.getFormattedText());
		this.setPlainText(model.getPlainText());
	}

	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		InitialPopulationModel initialPopulationModel = (InitialPopulationModel) model;
		if(initialPopulationModel == null) {
			return false;
		} else if(initialPopulationModel.getFormattedText() != null && getFormattedText() != null) {
			return this.getFormattedText().equals(initialPopulationModel.getFormattedText()) && 
					this.getPlainText().equals(initialPopulationModel.getPlainText());
		} else {
			return initialPopulationModel.getFormattedText() == null && getFormattedText() == null;
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
