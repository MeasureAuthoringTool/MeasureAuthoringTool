package mat.client.measure.measuredetails.components;

public abstract class RichTextEditorModel implements MeasureDetailsComponentModel{
	private String planText;
	
	private String formatedText;
	
	public String getPlanText() {
		return planText;
	}
	public void setPlanText(String planText) {
		this.planText = planText;
	}
	public String getFormatedText() {
		return formatedText;
	}
	public void setFormatedText(String formatedText) {
		this.formatedText = formatedText;
	}
	
	@Override
	abstract public boolean equals(MeasureDetailsComponentModel model);

	@Override
	abstract public boolean isValid();
}
