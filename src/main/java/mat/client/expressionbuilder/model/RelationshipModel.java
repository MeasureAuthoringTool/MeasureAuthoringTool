package mat.client.expressionbuilder.model;

import mat.client.expressionbuilder.constant.CQLType;
import mat.client.expressionbuilder.modal.RelationshipBuilderModal;

public class RelationshipModel extends ExpressionBuilderModel {

	private String alias;
	
	private final ExpressionBuilderModel source;
	private final ExpressionBuilderModel criteria;

	public RelationshipModel(ExpressionBuilderModel source, ExpressionBuilderModel criteria, ExpressionBuilderModel parent) {
		super(parent);
		this.source = source;
		this.source.setParentModel(this);
		this.criteria = criteria;
		this.criteria.setParentModel(this);
	}

	public RelationshipModel(ExpressionBuilderModel parent) {
		super(parent);
		source = new ExpressionBuilderModel(this);
		criteria = new ExpressionBuilderModel(this); 
		alias = "";
	}
	
	public ExpressionBuilderModel getSource() {
		return source;
	}
	
	public ExpressionBuilderModel getCriteria() {
		return criteria;
	}
	
	@Override
	public String getCQL(String indentation) {		
		final StringBuilder builder = new StringBuilder();
		
		if(getParentModel().getParentModel() != null) {
			QueryModel queryModel = (QueryModel) getParentModel().getParentModel();
			builder.append(queryModel.getRelationshipType()).append(" ");
		}
						
		builder.append(source.getCQL(indentation + "  "));

		builder.append(" ").append(alias);
		
		final String filterIdentation = indentation + "  ";
		
		if (!criteria.getChildModels().isEmpty()) {
			builder.append("\n" + filterIdentation);
			builder.append("such that ");
		}
		
		if(getChildModels().size() == 1) {
			builder.append(getChildModels().get(0).getCQL(filterIdentation));
		} else {
			if (!criteria.getChildModels().isEmpty()) {
				builder.append(criteria.getCQL(filterIdentation));
			}
		}
				
		return builder.toString();
	}

	@Override
	public CQLType getType() {
		return CQLType.LIST;
	}
	
	@Override
	public String getDisplayName() {
		return RelationshipBuilderModal.SOURCE;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}
