package mat.shared.measure.measuredetails.components;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class RichTextEditorModel implements MeasureDetailsComponentModel, IsSerializable{
	private String plainText;
	
	private String formatedText;
	
	public String getPlainText() {
		return plainText;
	}
	public void setPlainText(String plainText) {
		this.plainText = plainText;
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
