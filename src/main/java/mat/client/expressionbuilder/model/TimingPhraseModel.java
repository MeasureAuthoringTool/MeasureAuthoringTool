package mat.client.expressionbuilder.model;

public class TimingPhraseModel extends ExpressionBuilderModel {

	public TimingPhraseModel(ExpressionBuilderModel parent) {
		super(parent);
	}
	
	@Override
	public String getCQL(String indentation) {
		StringBuilder builder = new StringBuilder();
		
		for(IExpressionBuilderModel model : this.getChildModels()) {
			builder.append(model.getCQL("") + " ");
		}
		
		return builder.toString().trim();
	}
}
