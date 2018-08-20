package mat.model.cql;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class CQLIncludeLibraryWrapper.
 */
public class CQLIncludeLibraryWrapper {

	private List<CQLIncludeLibrary> cqlIncludeLibrary = new ArrayList<CQLIncludeLibrary>();

	public List<CQLIncludeLibrary> getCqlIncludeLibrary() {
		return cqlIncludeLibrary;
	}

	public void setCqlIncludeLibrary(List<CQLIncludeLibrary> cqlIncludeLibrary) {
		this.cqlIncludeLibrary = cqlIncludeLibrary;
	}
}
