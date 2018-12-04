package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CopyrightModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable {

	public CopyrightModel() {
		super("", "");
	}
	
	public CopyrightModel(CopyrightModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}

	@Override
	public boolean isValid() {
		return false;
	}
	
	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.visit(this);
	}
}
