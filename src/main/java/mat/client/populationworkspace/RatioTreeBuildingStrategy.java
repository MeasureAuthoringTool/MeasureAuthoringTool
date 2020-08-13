package mat.client.populationworkspace;

import com.google.gwt.xml.client.Document;
import mat.client.clause.clauseworkspace.model.CQLCellTreeNode;
import mat.client.clause.clauseworkspace.model.CQLCellTreeNodeImpl;
import mat.client.clause.clauseworkspace.presenter.CQLXmlConversionlHelper;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.shared.MatContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RatioTreeBuildingStrategy implements PopulationTreeBuilderStrategy{
	
	@Override
	public CQLCellTreeNode buildCQLTreeNode(String scoringType, Document document, boolean isPatientBased) {
		List<CQLCellTreeNode> parentchilds = new ArrayList<CQLCellTreeNode>();
		CQLCellTreeNode parentNode = new CQLCellTreeNodeImpl();
		parentNode.setName(MatContext.get().getCurrentShortName());
		parentNode.setLabel(MatContext.get().getCurrentShortName());
		parentNode.setNodeType(CQLCellTreeNode.MAIN_NODE);

		CQLCellTreeNode populationsNode = CQLXmlConversionlHelper.createCQLCellTreeNode(document, PopulationWorkSpaceConstants.ROOT_NODE_POPULATIONS);
		populationsNode.setLabel(PopulationWorkSpaceConstants.get(PopulationWorkSpaceConstants.ROOT_NODE_POPULATIONS));
		CQLCellTreeNode firstLevelChild = populationsNode.getChilds().get(0);
		List<CQLCellTreeNode> childNodes = firstLevelChild.getChilds();
		Collections.sort(childNodes, new PopulationsNodeComparator());
		
		firstLevelChild.setChilds(childNodes);
		parentchilds.add(firstLevelChild);
		populationsNode.setParent(parentNode);
		populationsNode.setOpen(true);
		
		if (!isPatientBased) {
			CQLCellTreeNode moNode = CQLXmlConversionlHelper.createCQLCellTreeNode(document, PopulationWorkSpaceConstants.ROOT_NODE_MEASURE_OBSERVATIONS);
			moNode.setLabel(PopulationWorkSpaceConstants.get(PopulationWorkSpaceConstants.ROOT_NODE_MEASURE_OBSERVATIONS));
			parentchilds.add(moNode.getChilds().get(0));
			moNode.setParent(parentNode);
			moNode.setOpen(true);
		}
		
		parentNode.setChilds(parentchilds);
		parentNode.setOpen(true);
		return parentNode;
	}
}
