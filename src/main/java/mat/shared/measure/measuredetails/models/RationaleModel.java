package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RationaleModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable{

	public RationaleModel() {
		super("", "");
	}
	
	public RationaleModel(RationaleModel model) {
		super(model.getPlainText(), model.getFormattedText());
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
