package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MeasureSetModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable{

	public MeasureSetModel() {
		super("", "");
	}
	
	public MeasureSetModel(MeasureSetModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}
	
	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.visit(this);
	}
}
