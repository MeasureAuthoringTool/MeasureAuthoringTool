package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class MeasureDetailsRichTextAbstractModel implements MeasureDetailsComponentModel, IsSerializable {
	private String plainText;
	
	private String formattedText;
	
	public String getPlainText() {
		return plainText;
	}
	public void setPlainText(String plainText) {
		this.plainText = plainText;
	}
	public String getFormattedText() {
		return formattedText;
	}
	public void setFormattedText(String formattedText) {
		this.formattedText = formattedText;
	}
	
	@Override
	abstract public boolean equals(MeasureDetailsComponentModel model);

	@Override
	abstract public boolean isValid();
}
