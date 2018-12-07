package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DefinitionModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable {
	
	public DefinitionModel() {
		super("", "");
	}
	
	public DefinitionModel(DefinitionModel model) {
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
