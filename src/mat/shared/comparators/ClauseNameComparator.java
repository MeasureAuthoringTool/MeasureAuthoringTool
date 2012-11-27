package mat.shared.comparators;

import java.util.Comparator;

import mat.shared.model.Clause;

public class ClauseNameComparator implements Comparator<Clause>{
	public int compare(Clause c1, Clause c2) {
		return c1.getName().compareTo(c2.getName());
	}
}