package mat.shared.comparators;

import java.util.Comparator;

import mat.model.ListObject;

public class ListObjectOIDComparator implements Comparator<ListObject>{
	public int compare(ListObject o1, ListObject o2) {
		return o1.getOid().compareTo(o2.getOid());
	}
}

