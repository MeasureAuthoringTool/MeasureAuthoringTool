package mat.shared.measure.measuredetails.components;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RateAggregationModel extends RichTextEditorModel implements IsSerializable{

	@Override
	public boolean equals(MeasureDetailsComponentModel model) {
		// TODO Auto-generated method stub
		return false;
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
