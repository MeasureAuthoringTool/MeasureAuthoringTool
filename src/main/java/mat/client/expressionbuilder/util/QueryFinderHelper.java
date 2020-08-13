package mat.client.expressionbuilder.util;

import mat.client.expressionbuilder.constant.ExpressionType;
import mat.client.expressionbuilder.modal.RelationshipBuilderModal;
import mat.client.expressionbuilder.model.AliasModel;
import mat.client.expressionbuilder.model.AttributeModel;
import mat.client.expressionbuilder.model.ComparisonModel;
import mat.client.expressionbuilder.model.ComputationModel;
import mat.client.expressionbuilder.model.FunctionModel;
import mat.client.expressionbuilder.model.IExpressionBuilderModel;
import mat.client.expressionbuilder.model.IntervalModel;
import mat.client.expressionbuilder.model.MembershipInModel;
import mat.client.expressionbuilder.model.QueryModel;
import mat.client.expressionbuilder.model.QuerySortModel;
import mat.client.expressionbuilder.model.RelationshipModel;
import mat.client.expressionbuilder.model.TimingModel;

import java.util.ArrayList;
import java.util.List;

public class QueryFinderHelper {

	private QueryFinderHelper() {
		throw new IllegalStateException();
	}
	
	public static boolean isPartOfSort(IExpressionBuilderModel currentModel) {
		if(currentModel == null) {
			return false;
		}
		
		else if(currentModel instanceof QuerySortModel) {
			return true;
		}
		
		else {
			return isPartOfSort(currentModel.getParentModel());
		}
	}
	
	public static void updateAliasInChildModels(QueryModel current, String previousAlias, String updatedAlias) {
		current.getSource().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
		current.getFilter().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
		current.getSort().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
		current.getRelationship().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
	}
	
	private static void updateAliasInRelationships(RelationshipModel current, String previousAlias, String updatedAlias) {
		current.getSource().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
		current.getCriteria().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
	}
	
	private static void updateAlias(IExpressionBuilderModel model, String previousAlias, String updatedAlias) {
		if(model.getDisplayName().equals(ExpressionType.ALIAS.getDisplayName())) {
			if(((AliasModel) model).getAlias().equals(previousAlias)) {
				((AliasModel) model).setAlias(updatedAlias);
			}
		}
		
		if(model.getDisplayName().equals(ExpressionType.ATTRIBUTE.getDisplayName())) {
			((AttributeModel) model).getSource().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
		}
				
		if(model.getDisplayName().equals(ExpressionType.COMPARISON.getDisplayName())) {
			((ComparisonModel) model).getRightHandSide().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
			((ComparisonModel) model).getLeftHandSide().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
		}
		
		if(model.getDisplayName().equals(ExpressionType.COMPUTATION.getDisplayName())) {
			((ComputationModel) model).getRightHandSide().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
			((ComputationModel) model).getLeftHandSide().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
		}
		
		if(model.getDisplayName().equals(ExpressionType.FUNCTION.getDisplayName())) {
			((FunctionModel) model).getArguments().forEach(a -> a.getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias)));
		}
		
		if(model.getDisplayName().equals(ExpressionType.INTERVAL.getDisplayName())) {
			((IntervalModel) model).getUpperBound().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
			((IntervalModel) model).getLowerBound().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
		}
		
		if(model.getDisplayName().equals(ExpressionType.IN.getDisplayName())) {
			((MembershipInModel) model).getRightHandSide().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
			((MembershipInModel) model).getLeftHandSide().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
		}
		
		if(model.getDisplayName().equals(ExpressionType.TIMING.getDisplayName())) {
			((TimingModel) model).getRightHandSide().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
			((TimingModel) model).getLeftHandSide().getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
		}
		
		if(model.getDisplayName().equals(ExpressionType.QUERY.getDisplayName())) {
			updateAliasInChildModels(((QueryModel) model), previousAlias, updatedAlias);
		}
		
		if(model.getDisplayName().equals(RelationshipBuilderModal.SOURCE.toString())) {
			updateAliasInRelationships(((RelationshipModel) model), previousAlias, updatedAlias);
		}
		
		model.getChildModels().forEach(m -> updateAlias(m, previousAlias, updatedAlias));
	}
	
	public static List<AliasModel> findAliasNames(IExpressionBuilderModel currentModel) {
		List<AliasModel> aliasNames = new ArrayList<>();
		findAliasNames(currentModel, aliasNames);
		return aliasNames;
	}
	
	
	public static boolean isPartOfQuery(IExpressionBuilderModel currentModel) {
		if(currentModel == null) {
			return false;
		}
		
		if(currentModel instanceof QueryModel) {
			return true;
		}
						
		return isPartOfQuery(currentModel.getParentModel());
	}	
	
	private static void findAliasNames(IExpressionBuilderModel currentModel, List<AliasModel> aliasNames) {
		if(currentModel == null) {
			return;
		}
		
		if(currentModel instanceof QueryModel) {
			QueryModel queryModel = (QueryModel) currentModel;
			if(!queryModel.getAlias().isEmpty()) {
				aliasNames.add(new AliasModel(queryModel, queryModel.getAlias(), "Query Source") );
			}
		}
		
		if(currentModel instanceof RelationshipModel) {
			RelationshipModel relationshipModel = (RelationshipModel) currentModel;
			if(!relationshipModel.getAlias().isEmpty()) {
				aliasNames.add(new AliasModel(relationshipModel, relationshipModel.getAlias(), RelationshipBuilderModal.SOURCE) );
			}
		}
		
		findAliasNames(currentModel.getParentModel(), aliasNames);
	}	
}
