package mat.shared.measure.measuredetails.models;

import java.util.List;

public interface MeasureDetailsComponentModel {
	public boolean equals(MeasureDetailsComponentModel model);
	public void update(MeasureDetailsModelVisitor measureDetailsModelVisitor);
	public List<String> validateModel(MeasureDetailsModelVisitor measureDetailsModelVisitor);
	public boolean isDirty(MeasureDetailsModelVisitor measureDetailsModelVisitor);
}
