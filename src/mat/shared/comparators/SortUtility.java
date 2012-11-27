package mat.shared.comparators;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import mat.model.ListObject;
import mat.model.QualityDataSet;

public class SortUtility {
	
	public SortedSet<ListObject> sortQDMsToListObjects(List<QualityDataSet> qdms){
		//Unit Test: Assert they are inserted in primary OID order
		ListObjectOIDComparator comp = new ListObjectOIDComparator();
		SortedSet<ListObject> sortedListObjects = new TreeSet<ListObject>(comp);
		for(QualityDataSet qdm: qdms){
			ListObject lo = qdm.getListObject();
			sortedListObjects.add(lo);
		}
		return sortedListObjects;
	}
}
