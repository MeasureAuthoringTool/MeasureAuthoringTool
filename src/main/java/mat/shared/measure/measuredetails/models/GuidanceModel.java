package mat.shared.measure.measuredetails.models;

public class GuidanceModel extends MeasureDetailsRichTextAbstractModel{
	
	public GuidanceModel() {
		super("", "");
	}
	
	public GuidanceModel(GuidanceModel model) {
		super(model.getPlainText(), model.getFormattedText());
	}

	public void accept(MeasureDetailsModelVisitor measureDetailsModelVisitor) {
		measureDetailsModelVisitor.visit(this);
	}
}
