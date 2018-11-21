package mat.shared.measure.measuredetails.models;

public interface MeasureDetailsComponentModel {
	public boolean equals(MeasureDetailsComponentModel model);
	public boolean isValid();
	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor);
}
