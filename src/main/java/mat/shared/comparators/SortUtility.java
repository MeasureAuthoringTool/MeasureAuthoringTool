package mat.shared.comparators;

import mat.model.ListObject;
import mat.model.QualityDataSet;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * The Class SortUtility.
 */
public class SortUtility {
	
	/**
	 * Sort qd ms to list objects.
	 * 
	 * @param qdms
	 *            the qdms
	 * @return the sorted set
	 */
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
