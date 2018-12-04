package mat.shared.measure.measuredetails.models;

public class GuidanceModel extends MeasureDetailsRichTextAbstractModel{
	
	public GuidanceModel() {
		super("", "");
	}
	
	public GuidanceModel(GuidanceModel model) {
		super(model.getPlainText(), model.getFormattedText());
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
