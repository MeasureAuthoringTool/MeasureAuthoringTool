package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SupplementalDataElementsModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable{
	public SupplementalDataElementsModel() {
		super("", "");
	}
	
	public SupplementalDataElementsModel(SupplementalDataElementsModel model) {
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
