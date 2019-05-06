package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class MeasureDetailsTextAbstractModel implements MeasureDetailsComponentModel, IsSerializable {
	private String editorText;
	
	public MeasureDetailsTextAbstractModel() {
		this.editorText = "";
	}
	
	public MeasureDetailsTextAbstractModel(String text) {
		this.editorText = text;
	}
	public String getEditorText() {
		return editorText == null ? null : editorText.trim();
	}
	public void setEditorText(String text) {
		this.editorText = text;
	}
	
	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		MeasureDetailsTextAbstractModel textEditorModel = (MeasureDetailsTextAbstractModel) model;
		if(textEditorModel == null) {
			return false;
		} else if(textEditorModel.getEditorText() != null && getEditorText() != null) {
			return this.getEditorText().equals(textEditorModel.getEditorText());
		} else {
			return textEditorModel.getEditorText() == null && getEditorText() == null;
		}
	}
}
