package mat.shared.measure.measuredetails.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DescriptionModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable{
	
	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		return this.getFormattedText().equals(((DescriptionModel)  model).getFormattedText()) && 
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
