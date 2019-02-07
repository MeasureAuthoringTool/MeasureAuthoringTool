package mat.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.client.shared.SearchWidgetWithFilter;

public abstract class SearchModel implements IsSerializable {
	
	protected int startIndex;
	protected int pageSize;
	protected int modifiedDate;
	protected int isMyMeasureSearch;
	
	protected String searchTerm;
	protected String modifiedOwner;
	protected String owner;

	protected VersionType versionType;
	
	public enum VersionType {ALL, VERSION, DRAFT}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(int modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getIsMyMeasureSearch() {
		return isMyMeasureSearch;
	}

	public void setIsMyMeasureSearch(int isMyMeasureSearch) {
		this.isMyMeasureSearch = isMyMeasureSearch;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = trimWhiteSpaces(searchTerm);
	}

	public String getModifiedOwner() {
		return modifiedOwner;
	}

	public void setModifiedOwner(String modifiedOwner) {
		this.modifiedOwner = trimWhiteSpaces(modifiedOwner);
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = trimWhiteSpaces(owner);
	}

	public VersionType isDraft() {
		return versionType;
	}
	
	public void setIsDraft(VersionType version) {
		this.versionType = version;
	}
	
	public void reset() {
		searchTerm = "";
		versionType = VersionType.ALL;
		modifiedDate = 0;
		modifiedOwner = "";
		owner = "";
		isMyMeasureSearch = SearchWidgetWithFilter.MY;
	}
	
	private String trimWhiteSpaces(String str) {
		return str != null ? str.trim() : null;
	}
}
