package mat.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.client.shared.SearchWidgetWithFilter;

public class LibrarySearchModel extends SearchModel implements IsSerializable {

	public LibrarySearchModel() {
		this.searchTerm = "";
		this.versionType = VersionType.ALL;
		this.modifiedDate = 0;
		this.modifiedOwner = "";
		this.owner = "";
		this.startIndex = 1;
		this.pageSize = Integer.MAX_VALUE;
		this.isMyMeasureSearch = SearchWidgetWithFilter.MY;
	}

	public LibrarySearchModel(int myMeasureSearch, int startIndex, int pageSize, String searchTerm, boolean matOnFhir) {
		this.isMyMeasureSearch = myMeasureSearch;
		this.startIndex = startIndex;
		this.pageSize = pageSize;
		this.searchTerm = searchTerm;
		this.matOnFhir = matOnFhir;
	}

}
