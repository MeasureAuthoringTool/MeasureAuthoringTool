package mat.shared.measure.measuredetails.components;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DescriptionModel extends RichTextEditorModel implements IsSerializable{
	
	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		return this.getFormatedText().equals(((DescriptionModel)  model).getFormatedText()) && 
					this.getPlainText().equals(((DescriptionModel)  model).getPlainText());
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
