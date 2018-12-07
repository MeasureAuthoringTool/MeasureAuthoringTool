package mat.shared.measure.measuredetails.models;

public interface MeasureDetailsComponentModel {
	public boolean equals(MeasureDetailsComponentModel model);
	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor);
}
