package mat.client.measure.measuredetails.components;

public class DescriptionModel extends RichTextEditorModel {
	
	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		return this.getFormatedText().equals(((DescriptionModel)  model).getFormatedText()) && 
					this.getPlanText().equals(((DescriptionModel)  model).getPlanText());
	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}
}
