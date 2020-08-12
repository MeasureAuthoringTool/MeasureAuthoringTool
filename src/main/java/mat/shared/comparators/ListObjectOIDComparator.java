package mat.shared.comparators;

import mat.model.ListObject;

import java.util.Comparator;

/**
 * The Class ListObjectOIDComparator.
 */
public class ListObjectOIDComparator implements Comparator<ListObject>{
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(ListObject o1, ListObject o2) {
		return o1.getOid().compareTo(o2.getOid());
	}
}

