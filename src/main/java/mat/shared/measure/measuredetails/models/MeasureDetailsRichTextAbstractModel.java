package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class MeasureDetailsRichTextAbstractModel implements MeasureDetailsComponentModel, IsSerializable {
	private String plainText;
	private String formattedText;
	
	public MeasureDetailsRichTextAbstractModel() {
		this.plainText = "";
		this.formattedText = "";
	}
	
	public MeasureDetailsRichTextAbstractModel(String plainText, String formattedText) {
		this.plainText = plainText; 
		this.formattedText = formattedText;
	}
	
	public String getPlainText() {
		return plainText == null ? null : plainText.trim();
	}
	public void setPlainText(String plainText) {
		this.plainText = plainText;
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
			return this.getFormattedText().equals(richTextEditorModel.getFormattedText()) && 
					this.getPlainText().equals(richTextEditorModel.getPlainText());
		} else {
			return richTextEditorModel.getFormattedText() == null && getFormattedText() == null;
		}
	}
}
