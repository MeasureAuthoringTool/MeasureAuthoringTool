package mat.client.measure;

import java.sql.Timestamp;
import java.util.List;

import mat.client.shared.search.SearchResults;
import mat.model.LockedUserInfo;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;

public class ManageMeasureSearchModel implements IsSerializable, SearchResults<ManageMeasureSearchModel.Result>{
	public static class Result implements IsSerializable {
		private String id;
		private String name;
		private String status;
		private String scoringType;
		private boolean isHistorical;
		private boolean isSharable;
		private boolean isEditable;
		private boolean isClonable;
		private boolean isExportable;
		private String shortName;
		private boolean isMeasureLocked;
		private LockedUserInfo lockedUserInfo;
		private boolean isTransferable;
		/*US501*/
		private String version;
		private Timestamp finalizedDate;
		private boolean draft;
		private String measureSetId;
		private boolean isDeleted;
		private String ownerfirstName;
		private String ownerLastName;
		private String ownerEmailAddress;
		
		public LockedUserInfo getLockedUserInfo() {
			return lockedUserInfo;
		}
		public void setLockedUserInfo(LockedUserInfo lockedUserInfo) {
			this.lockedUserInfo = lockedUserInfo;
		}
		
		public String getShortName() {
			return shortName;
		}
		public void setShortName(String shortName) {
			this.shortName = shortName;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		public String getScoringType(){
			return scoringType;
		}
		public void setScoringType(String scoringType){
			this.scoringType = scoringType;
		}
		
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public boolean isHistorical(){
			return isHistorical;
		}
		public void setHistorical(boolean isHistorical) {
			this.isHistorical = isHistorical;
		}
		
		public boolean isSharable() {
			return isSharable;
		}
		public void setSharable(boolean isSharable) {
			this.isSharable = isSharable;
		}
		public boolean isEditable() {
			return isEditable;
		}
		public void setEditable(boolean isEditable) {
			this.isEditable = isEditable;
		}
		public boolean isClonable() {
			return isClonable;
		}
		public void setClonable(boolean isClonable) {
			this.isClonable = isClonable;
		}
		public boolean isExportable() {
			return isExportable;
		}
		public void setExportable(boolean isExportable) {
			this.isExportable = isExportable;
		}

		public boolean isMeasureLocked() {
			return isMeasureLocked;
		}
		public void setMeasureLocked(boolean isMeasureLocked) {
			this.isMeasureLocked = isMeasureLocked;
		}
		
		//This method will give the lockedUserId if the lockedUser is not null.
		public String getLockedUserId(LockedUserInfo info){
			if(info != null)
				return info.getUserId();
			else
				return "";
		}
		
		
		/*US501*/
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		
		/**
		 * given version is a String of the form:
		 * Draft of v#.#
		 * @return #.#
		 */
		public String getVersionValue(){
			int offset = version.lastIndexOf("v")+1;
			if(offset < version.length())
					return version.substring(offset);
			return version;
		}
		
		
		public Timestamp getFinalizedDate() {
			return finalizedDate;
		}
		public void setFinalizedDate(Timestamp finalizedDate) {
			this.finalizedDate = finalizedDate;
		}
		public boolean isDraft() {
			return draft;
		}
		public void setDraft(boolean draft) {
			this.draft = draft;
		}
		public String getMeasureSetId() {
			return measureSetId;
		}
		public void setMeasureSetId(String measureSetId) {
			this.measureSetId = measureSetId;
		}
		/**
		 * @param isTransferable the isTransferable to set
		 */
		public void setTransferable(boolean isTransferable) {
			this.isTransferable = isTransferable;
		}
		/**
		 * @return the isTransferable
		 */
		public boolean isTransferable() {
			return isTransferable;
		}
		public boolean isDeleted() {
			return isDeleted;
		}
		public void setDeleted(boolean isDeleted) {
			this.isDeleted = isDeleted;
		}
		public String getOwnerfirstName() {
			return ownerfirstName;
		}
		public void setOwnerfirstName(String ownerfirstName) {
			this.ownerfirstName = ownerfirstName;
		}
		public String getOwnerLastName() {
			return ownerLastName;
		}
		public void setOwnerLastName(String ownerLastName) {
			this.ownerLastName = ownerLastName;
		}
		public String getOwnerEmailAddress() {
			return ownerEmailAddress;
		}
		public void setOwnerEmailAddress(String ownerEmailAddress) {
			this.ownerEmailAddress = ownerEmailAddress;
		}
		
		
		
	}
	
	private List<Result> data;
	private int startIndex;
	private int resultsTotal;
	private int pageCount;
	private List<String> selectedExportIds;
	private List<String> selectedTransferIds;
	private List<Result> selectedTransferResults;
	
	
	public void setData(List<Result> data) {
		this.data = data;
	}
	
    public List<Result> getData(){
    	return data;
    }

	public int getNumberOfRows() {
		return data != null ? data.size() : 0;
	}

	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	public int getResultsTotal() {
		return resultsTotal;
	}
	public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
	}

	public String getKey(int row) {
		return data.get(row).getId();
	}

	public Result get(int row) {
		return data.get(row);
	}


	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	@Override
	public int getNumberOfColumns() {
		return 0;
	}



	@Override
	public String getColumnHeader(int columnIndex) {
	return null;
	}



	@Override
	public boolean isColumnSortable(int columnIndex) {
		return false;
	}



	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}



	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return false;
	}



	@Override
	public String getColumnWidth(int columnIndex) {
		return null;
	}



	@Override
	public Widget getValue(int row, int column) {
		return null;
	}

	/**
	 * @return the selectedExportIds
	 */
	public List<String> getSelectedExportIds() {
		return selectedExportIds;
	}

	/**
	 * @param selectedExportIds the selectedExportIds to set
	 */
	public void setSelectedExportIds(List<String> selectedExportIds) {
		this.selectedExportIds = selectedExportIds;
	}

	/**
	 * @param selectedTranferIds the selectedTranferIds to set
	 */
	public void setSelectedTransferIds(List<String> selectedTranferIds) {
		this.selectedTransferIds = selectedTranferIds;
	}

	/**
	 * @return the selectedTranferIds
	 */
	public List<String> getSelectedTransferIds() {
		return selectedTransferIds;
	}

	/**
	 * @param selectedTransferResults the selectedTransferResults to set
	 */
	public void setSelectedTransferResults(List<Result> selectedTransferResults) {
		this.selectedTransferResults = selectedTransferResults;
	}

	/**
	 * @return the selectedTransferResults
	 */
	public List<Result> getSelectedTransferResults() {
		return selectedTransferResults;
	}

	


	
}
