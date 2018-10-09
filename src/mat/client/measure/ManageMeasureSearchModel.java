package mat.client.measure;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;

import mat.client.shared.search.SearchResults;
import mat.model.LockedUserInfo;

/**
 * The Class ManageMeasureSearchModel.
 */
public class ManageMeasureSearchModel implements IsSerializable, SearchResults<ManageMeasureSearchModel.Result>{
	
	/**
	 * The Class Result.
	 */
	public static class Result implements IsSerializable {
		
		/** The id. */
		private String id;
		
		/** The name. */
		private String name;
		
		/** The status. */
		private String status;
		
		/** The scoring type. */
		private String scoringType;
		
		/** The is historical. */
		private boolean isHistorical;
		
		/** The is sharable. */
		private boolean isSharable;
		
		/** The is editable. */
		private boolean isEditable;
		
		/** The is clonable. */
		private boolean isClonable;
		
		/** The is exportable. */
		private boolean isExportable;
		
		/** The short name. */
		private String shortName;
		
		/** The is measure locked. */
		private boolean isMeasureLocked;
		
		/** The locked user info. */
		private LockedUserInfo lockedUserInfo;
		
		/** The is transferable. */
		private boolean isTransferable;
		/*US501*/
		/** The version. */
		private String version;
		
		/** The finalized date. */
		private Timestamp finalizedDate;
		
		/** The draft. */
		private boolean draft;
		
		/** The measure set id. */
		private String measureSetId;
		
		/** The is deleted. */
		private boolean isDeleted;
		
		/** The ownerfirst name. */
		private String ownerfirstName;
		
		/** The owner last name. */
		private String ownerLastName;
		
		/** The owner email address. */
		private String ownerEmailAddress;
		
		/** The e measure id. */
		private int eMeasureId;
		
		/** The is measure family. */
		private boolean isMeasureFamily;
		
		private String hqmfReleaseVersion;
		
		private boolean isDraftable;
		
		private boolean isVersionable;
		
		private Boolean isPatientBased;
		
		private String qdmVersion;
		
		private Boolean isComposite;

		/**
		 * Checks if is measure family.
		 *
		 * @return true, if is measure family
		 */
		public boolean isMeasureFamily(){
			return isMeasureFamily;
		}
		
		/**
		 * Sets the measure family.
		 *
		 * @param isMeasureFamily the new measure family
		 */
		public void setMeasureFamily(boolean isMeasureFamily) {
			this.isMeasureFamily = isMeasureFamily;
		}

		/**
		 * Gets the locked user info.
		 * 
		 * @return the locked user info
		 */
		public LockedUserInfo getLockedUserInfo() {
			return lockedUserInfo;
		}
		
		/**
		 * Sets the locked user info.
		 * 
		 * @param lockedUserInfo
		 *            the new locked user info
		 */
		public void setLockedUserInfo(LockedUserInfo lockedUserInfo) {
			this.lockedUserInfo = lockedUserInfo;
		}
		
		/**
		 * Gets the short name.
		 * 
		 * @return the short name
		 */
		public String getShortName() {
			return shortName;
		}
		
		/**
		 * Sets the short name.
		 * 
		 * @param shortName
		 *            the new short name
		 */
		public void setShortName(String shortName) {
			this.shortName = shortName;
		}
		
		/**
		 * Gets the name.
		 * 
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Sets the name.
		 * 
		 * @param name
		 *            the new name
		 */
		public void setName(String name) {
			this.name = name;
		}
		
		/**
		 * Gets the scoring type.
		 * 
		 * @return the scoring type
		 */
		public String getScoringType(){
			return scoringType;
		}
		
		/**
		 * Sets the scoring type.
		 * 
		 * @param scoringType
		 *            the new scoring type
		 */
		public void setScoringType(String scoringType){
			this.scoringType = scoringType;
		}
		
		/**
		 * Gets the status.
		 * 
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}
		
		/**
		 * Sets the status.
		 * 
		 * @param status
		 *            the new status
		 */
		public void setStatus(String status) {
			this.status = status;
		}
		
		/**
		 * Gets the id.
		 * 
		 * @return the id
		 */
		public String getId() {
			return id;
		}
		
		/**
		 * Sets the id.
		 * 
		 * @param id
		 *            the new id
		 */
		public void setId(String id) {
			this.id = id;
		}
		
		/**
		 * Checks if is historical.
		 * 
		 * @return true, if is historical
		 */
		public boolean isHistorical(){
			return isHistorical;
		}
		
		/**
		 * Sets the historical.
		 * 
		 * @param isHistorical
		 *            the new historical
		 */
		public void setHistorical(boolean isHistorical) {
			this.isHistorical = isHistorical;
		}
		
		/**
		 * Checks if is sharable.
		 * 
		 * @return true, if is sharable
		 */
		public boolean isSharable() {
			return isSharable;
		}
		
		/**
		 * Sets the sharable.
		 * 
		 * @param isSharable
		 *            the new sharable
		 */
		public void setSharable(boolean isSharable) {
			this.isSharable = isSharable;
		}
		
		/**
		 * Checks if is editable.
		 * 
		 * @return true, if is editable
		 */
		public boolean isEditable() {
			return isEditable;
		}
		
		/**
		 * Sets the editable.
		 * 
		 * @param isEditable
		 *            the new editable
		 */
		public void setEditable(boolean isEditable) {
			this.isEditable = isEditable;
		}
		
		/**
		 * Checks if is clonable.
		 * 
		 * @return true, if is clonable
		 */
		public boolean isClonable() {
			return isClonable;
		}
		
		/**
		 * Sets the clonable.
		 * 
		 * @param isClonable
		 *            the new clonable
		 */
		public void setClonable(boolean isClonable) {
			this.isClonable = isClonable;
		}
		
		/**
		 * Checks if is exportable.
		 * 
		 * @return true, if is exportable
		 */
		public boolean isExportable() {
			return isExportable;
		}
		
		/**
		 * Sets the exportable.
		 * 
		 * @param isExportable
		 *            the new exportable
		 */
		public void setExportable(boolean isExportable) {
			this.isExportable = isExportable;
		}

		/**
		 * Checks if is measure locked.
		 * 
		 * @return true, if is measure locked
		 */
		public boolean isMeasureLocked() {
			return isMeasureLocked;
		}
		
		/**
		 * Sets the measure locked.
		 * 
		 * @param isMeasureLocked
		 *            the new measure locked
		 */
		public void setMeasureLocked(boolean isMeasureLocked) {
			this.isMeasureLocked = isMeasureLocked;
		}
		
		//This method will give the lockedUserId if the lockedUser is not null.
		/**
		 * Gets the locked user id.
		 * 
		 * @param info
		 *            the info
		 * @return the locked user id
		 */
		public String getLockedUserId(LockedUserInfo info){
			if(info != null)
				return info.getUserId();
			else
				return "";
		}
		
		
		/*US501*/
		/**
		 * Gets the version.
		 * 
		 * @return the version
		 */
		public String getVersion() {
			return version;
		}
		
		/**
		 * Sets the version.
		 * 
		 * @param version
		 *            the new version
		 */
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
		
		
		/**
		 * Gets the finalized date.
		 * 
		 * @return the finalized date
		 */
		public Timestamp getFinalizedDate() {
			return finalizedDate;
		}
		
		/**
		 * Sets the finalized date.
		 * 
		 * @param finalizedDate
		 *            the new finalized date
		 */
		public void setFinalizedDate(Timestamp finalizedDate) {
			this.finalizedDate = finalizedDate;
		}
		
		/**
		 * Checks if is draft.
		 * 
		 * @return true, if is draft
		 */
		public boolean isDraft() {
			return draft;
		}
		
		/**
		 * Sets the draft.
		 * 
		 * @param draft
		 *            the new draft
		 */
		public void setDraft(boolean draft) {
			this.draft = draft;
		}
		
		/**
		 * Gets the measure set id.
		 * 
		 * @return the measure set id
		 */
		public String getMeasureSetId() {
			return measureSetId;
		}
		
		/**
		 * Sets the measure set id.
		 * 
		 * @param measureSetId
		 *            the new measure set id
		 */
		public void setMeasureSetId(String measureSetId) {
			this.measureSetId = measureSetId;
		}
		
		/**
		 * Sets the transferable.
		 * 
		 * @param isTransferable
		 *            the isTransferable to set
		 */
		public void setTransferable(boolean isTransferable) {
			this.isTransferable = isTransferable;
		}
		
		/**
		 * Checks if is transferable.
		 * 
		 * @return the isTransferable
		 */
		public boolean isTransferable() {
			return isTransferable;
		}
		
		/**
		 * Checks if is deleted.
		 * 
		 * @return true, if is deleted
		 */
		public boolean isDeleted() {
			return isDeleted;
		}
		
		/**
		 * Sets the deleted.
		 * 
		 * @param isDeleted
		 *            the new deleted
		 */
		public void setDeleted(boolean isDeleted) {
			this.isDeleted = isDeleted;
		}
		
		/**
		 * Gets the ownerfirst name.
		 * 
		 * @return the ownerfirst name
		 */
		public String getOwnerFirstName() {
			return ownerfirstName;
		}
		
		/**
		 * Sets the ownerfirst name.
		 * 
		 * @param ownerfirstName
		 *            the new ownerfirst name
		 */
		public void setOwnerfirstName(String ownerfirstName) {
			this.ownerfirstName = ownerfirstName;
		}
		
		/**
		 * Gets the owner last name.
		 * 
		 * @return the owner last name
		 */
		public String getOwnerLastName() {
			return ownerLastName;
		}
		
		/**
		 * Sets the owner last name.
		 * 
		 * @param ownerLastName
		 *            the new owner last name
		 */
		public void setOwnerLastName(String ownerLastName) {
			this.ownerLastName = ownerLastName;
		}
		
		/**
		 * Gets the owner email address.
		 * 
		 * @return the owner email address
		 */
		public String getOwnerEmailAddress() {
			return ownerEmailAddress;
		}
		
		/**
		 * Sets the owner email address.
		 * 
		 * @param ownerEmailAddress
		 *            the new owner email address
		 */
		public void setOwnerEmailAddress(String ownerEmailAddress) {
			this.ownerEmailAddress = ownerEmailAddress;
		}
		
		/**
		 * Gets the e measure id.
		 * 
		 * @return the e measure id
		 */
		public int geteMeasureId() {
			return eMeasureId;
		}
		
		/**
		 * Sets the e measure id.
		 * 
		 * @param eMeasureId
		 *            the new e measure id
		 */
		public void seteMeasureId(int eMeasureId) {
			this.eMeasureId = eMeasureId;
		}
		
		public int compare(ManageMeasureSearchModel.Result o1, ManageMeasureSearchModel.Result o2) {
			int num = o1.getId().compareTo(o2.getId());
			return num;
		}

		public String getHqmfReleaseVersion() {
			return hqmfReleaseVersion;
		}

		public void setHqmfReleaseVersion(String hqmfReleaseVersion) {
			this.hqmfReleaseVersion = hqmfReleaseVersion;
		}

		public boolean isDraftable() {
			return isDraftable;
		}

		public void setDraftable(boolean isDraftable) {
			this.isDraftable = isDraftable;
		}

		public boolean isVersionable() {
			return isVersionable;
		}

		public void setVersionable(boolean isVersionable) {
			this.isVersionable = isVersionable;
		}

		public Boolean isPatientBased() {
			return isPatientBased;
		}

		public void setPatientBased(Boolean isPatientBased) {
			this.isPatientBased = isPatientBased;
		}

		public String getQdmVersion() {
			return qdmVersion;
		}

		public void setQdmVersion(String qdmVersion) {
			this.qdmVersion = qdmVersion;
		}
		
		public Boolean getIsComposite() {
			return isComposite;
		}

		public void setIsComposite(Boolean isComposite) {
			this.isComposite = isComposite;
		}
	}
	
	/** The data. */
	private List<Result> data;
	
	/** The start index. */
	private int startIndex;
	
	/** The results total. */
	private int resultsTotal;
	
	/** The page count. */
	private int pageCount;
	
	/** The selected export ids. */
	private List<String> selectedExportIds;
	
	/** The selected transfer ids. */
	private List<String> selectedTransferIds;

	/** The selected transfer results. */
	private ArrayList<Result> selectedTransferResults;

	/** The selected export results. */
	private ArrayList<Result> selectedExportResults;
	
	
	
	
	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            the new data
	 */
	public void setData(List<Result> data) {
		this.data = data;
	}
	
    /**
	 * Gets the data.
	 * 
	 * @return the data
	 */
    public List<Result> getData(){
    	return data;
    }

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfRows()
	 */
	public int getNumberOfRows() {
		return data != null ? data.size() : 0;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getStartIndex()
	 */
	public int getStartIndex() {
		return startIndex;
	}
	
	/**
	 * Sets the start index.
	 * 
	 * @param startIndex
	 *            the new start index
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getResultsTotal()
	 */
	public int getResultsTotal() {
		return resultsTotal;
	}
	
	/**
	 * Sets the results total.
	 * 
	 * @param resultsTotal
	 *            the new results total
	 */
	public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getKey(int)
	 */
	public String getKey(int row) {
		return data.get(row).getId();
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#get(int)
	 */
	public Result get(int row) {
		return data.get(row);
	}


	/**
	 * Gets the page count.
	 * 
	 * @return the page count
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * Sets the page count.
	 * 
	 * @param pageCount
	 *            the new page count
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfColumns()
	 */
	@Override
	public int getNumberOfColumns() {
		return 0;
	}



	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnHeader(int)
	 */
	@Override
	public String getColumnHeader(int columnIndex) {
	return null;
	}



	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSortable(int)
	 */
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return false;
	}



	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSelectAll(int)
	 */
	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}



	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnFiresSelection(int)
	 */
	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return false;
	}



	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnWidth(int)
	 */
	@Override
	public String getColumnWidth(int columnIndex) {
		return null;
	}



	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getValue(int, int)
	 */
	@Override
	public Widget getValue(int row, int column) {
		return null;
	}

	/**
	 * Gets the selected export ids.
	 * 
	 * @return the selectedExportIds
	 */
	public List<String> getSelectedExportIds() {
		return selectedExportIds;
	}

	/**
	 * Sets the selected export ids.
	 * 
	 * @param selectedExportIds
	 *            the selectedExportIds to set
	 */
	public void setSelectedExportIds(List<String> selectedExportIds) {
		this.selectedExportIds = selectedExportIds;
	}

	/**
	 * Sets the selected transfer ids.
	 * 
	 * @param selectedTranferIds
	 *            the selectedTranferIds to set
	 */
	public void setSelectedTransferIds(List<String> selectedTranferIds) {
		this.selectedTransferIds = selectedTranferIds;
	}

	/**
	 * Gets the selected transfer ids.
	 * 
	 * @return the selectedTranferIds
	 */
	public List<String> getSelectedTransferIds() {
		return selectedTransferIds;
	}

	/**
	 * Sets the selected transfer results.
	 * 
	 * @param selectedTransferResults
	 *            the selectedTransferResults to set
	 */
	public void setSelectedTransferResults(ArrayList<Result> selectedTransferResults) {
		this.selectedTransferResults = selectedTransferResults;
	}
	
	/**
	 * Sets the selected export results.
	 *
	 * @param selectedExportResults the new selected export results
	 */
	public void setSelectedExportResults(ArrayList<Result> selectedExportResults) {
		this.selectedExportResults = selectedExportResults;
	}

	/**
	 * Gets the selected transfer results.
	 * 
	 * @return the selectedTransferResults
	 */
	public List<Result> getSelectedTransferResults() {
		return selectedTransferResults;
	}

	/**
	 * Gets the selected export results.
	 *
	 * @return the selected export results
	 */
	public List<Result> getSelectedExportResults() {
		return selectedExportResults;
	}

	
	
}
