package mat.shared.measure.measuredetails.models;

import java.util.List;

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
	
	public void update(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.updateModel(this);
	}
	
	public List<String> validateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		return measureDetailsModelVisitor.validateModel(this);
	}
}
