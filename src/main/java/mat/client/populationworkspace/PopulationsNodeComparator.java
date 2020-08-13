package mat.client.populationworkspace;

import mat.client.clause.clauseworkspace.model.CQLCellTreeNode;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class PopulationsNodeComparator implements Comparator<CQLCellTreeNode>{
	private final List<String> sortingList = new LinkedList<String>() {
		private static final long serialVersionUID = 6067186830603198160L;
		{
			add("Initial Populations");
			add("Measure Populations");
			add("Measure Population Exclusions");
			add("Denominators");
			add("Denominator Exclusions");
			add("Numerators");
			add("Numerator Exclusions");
			add("Denominator Exceptions");
		}
	};
	@Override
	public int compare(CQLCellTreeNode node1, CQLCellTreeNode node2) {
		return new Integer(sortingList.indexOf(node1.getLabel())).compareTo(sortingList.indexOf(node2.getLabel()));
	}

}
