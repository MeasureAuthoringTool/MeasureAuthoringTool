package mat.shared.measure.measuredetails.models;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ClinicalRecommendationModel extends MeasureDetailsRichTextAbstractModel implements IsSerializable{
	public ClinicalRecommendationModel()  {
		super("", "");
	}
	
	public ClinicalRecommendationModel(ClinicalRecommendationModel model) {
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
