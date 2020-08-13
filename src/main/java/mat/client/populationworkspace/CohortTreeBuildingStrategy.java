package mat.client.populationworkspace;

import com.google.gwt.xml.client.Document;
import mat.client.clause.clauseworkspace.model.CQLCellTreeNode;
import mat.client.clause.clauseworkspace.model.CQLCellTreeNodeImpl;
import mat.client.clause.clauseworkspace.presenter.CQLXmlConversionlHelper;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.shared.MatContext;

import java.util.ArrayList;
import java.util.List;

public class CohortTreeBuildingStrategy implements PopulationTreeBuilderStrategy {
	
	@Override
	public CQLCellTreeNode buildCQLTreeNode(String scoringType, Document document, boolean isPatientBased) {
		List<CQLCellTreeNode> parentchilds = new ArrayList<CQLCellTreeNode>();
		CQLCellTreeNode parentNode = new CQLCellTreeNodeImpl();
		parentNode.setName(MatContext.get().getCurrentShortName());
		parentNode.setLabel(MatContext.get().getCurrentShortName());
		parentNode.setNodeType(CQLCellTreeNode.MAIN_NODE);

		CQLCellTreeNode populationsNode = CQLXmlConversionlHelper.createCQLCellTreeNode(document, PopulationWorkSpaceConstants.ROOT_NODE_POPULATIONS);
		populationsNode.setLabel(PopulationWorkSpaceConstants.get(PopulationWorkSpaceConstants.ROOT_NODE_POPULATIONS));
		parentchilds.add(populationsNode.getChilds().get(0));
		populationsNode.setParent(parentNode);
		populationsNode.setOpen(true);
		
		CQLCellTreeNode stratificationNode = CQLXmlConversionlHelper.createCQLCellTreeNode(document, PopulationWorkSpaceConstants.MASTER_ROOT_NODE_STRATA);
		stratificationNode.setLabel(PopulationWorkSpaceConstants.get(PopulationWorkSpaceConstants.MASTER_ROOT_NODE_STRATA));
		parentchilds.add(stratificationNode.getChilds().get(0));
		stratificationNode.setParent(parentNode);
		stratificationNode.setOpen(true);
		
		parentNode.setChilds(parentchilds);
		parentNode.setOpen(true);
		return parentNode;
	}
}
