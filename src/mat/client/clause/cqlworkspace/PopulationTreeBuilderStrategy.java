package mat.client.clause.cqlworkspace;

import com.google.gwt.xml.client.Document;

import mat.client.clause.clauseworkspace.model.CQLCellTreeNode;

public interface PopulationTreeBuilderStrategy {
	CQLCellTreeNode buildCQLTreeNode(String scoringType, Document document);
}
