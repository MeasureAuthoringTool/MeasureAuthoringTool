package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class MeasureDetailsRichTextAbstractModel implements MeasureDetailsComponentModel, IsSerializable {
	private String formattedText;
	
	public MeasureDetailsRichTextAbstractModel() {
		this.formattedText = "";
	}
	
	public MeasureDetailsRichTextAbstractModel(String formattedText) {
		this.formattedText = formattedText;
	}
	public String getFormattedText() {
		return formattedText == null ? null : formattedText.trim();
	}
	public void setFormattedText(String formattedText) {
		this.formattedText = formattedText;
	}
	
	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		MeasureDetailsRichTextAbstractModel richTextEditorModel = (MeasureDetailsRichTextAbstractModel) model;
		if(richTextEditorModel == null) {
			return false;
		} else if(richTextEditorModel.getFormattedText() != null && getFormattedText() != null) {
			return this.getFormattedText().equals(richTextEditorModel.getFormattedText());
		} else {
			return richTextEditorModel.getFormattedText() == null && getFormattedText() == null;
		}
	}
}
