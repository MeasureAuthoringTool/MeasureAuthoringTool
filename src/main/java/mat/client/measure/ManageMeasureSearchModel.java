package mat.client.measure;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;
import mat.client.shared.search.SearchResults;
import mat.model.LockedUserInfo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Class ManageMeasureSearchModel.
 */
public class ManageMeasureSearchModel implements IsSerializable, SearchResults<ManageMeasureSearchModel.Result> {

    /**
     * The Class Result.
     */
    public static class Result implements IsSerializable {

        private String id;
        private String name;
        private String measureModel;
        private String status;
        private String scoringType;
        private boolean isHistorical;
        private boolean isSharable;
        private boolean isEditable;
        private boolean isClonable;
        private boolean isExportable;
        private boolean isFhirConvertible;
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

        private int eMeasureId;

        private boolean isMeasureFamily;

        private String hqmfReleaseVersion;

        private boolean isDraftable;

        private boolean isVersionable;

        private Boolean isPatientBased;

        private String qdmVersion;

        private String fhirVersion;

        private Boolean isComposite;

        private String cqlLibraryName;

        private boolean isValidatable;

        private int clickCount = 0;

        private boolean isMeasureEditOrViewable;

        public Result() {
        }

        public Result(Result result) {
            this.id = result.getId();
            this.name = result.getName();
            this.status = result.getStatus();
            this.scoringType = result.getScoringType();
            this.isHistorical = result.isHistorical();
            this.isSharable = result.isSharable();
            this.isEditable = result.isEditable();
            this.isClonable = result.isClonable();
            this.isExportable = result.isExportable();
            this.isFhirConvertible = result.isFhirConvertible();
            this.isValidatable = result.isValidatable();
            this.shortName = result.getShortName();
            this.isMeasureLocked = result.isMeasureLocked();
            this.lockedUserInfo = result.getLockedUserInfo();
            this.isTransferable = result.isTransferable();
            this.version = result.getVersion();
            this.finalizedDate = result.getFinalizedDate();
            this.draft = result.isDraft();
            this.measureSetId = result.getMeasureSetId();
            this.isDeleted = result.isDeleted();
            this.ownerfirstName = result.getOwnerFirstName();
            this.ownerLastName = result.getOwnerLastName();
            this.ownerEmailAddress = result.getOwnerEmailAddress();
            this.eMeasureId = result.geteMeasureId();
            this.isMeasureFamily = result.isMeasureFamily();
            this.hqmfReleaseVersion = result.getHqmfReleaseVersion();
            this.isDraftable = result.isDraftable();
            this.isVersionable = result.isVersionable();
            this.isPatientBased = result.isPatientBased();
            this.qdmVersion = result.getQdmVersion();
            this.isComposite = result.getIsComposite();
            this.cqlLibraryName = result.getCqlLibraryName();
            this.measureModel = result.getMeasureModel();
            this.isMeasureEditOrViewable = result.isMeasureEditOrViewable();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Result result = (Result) o;
            return Objects.equals(id, result.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        /**
         * Checks if is measure family.
         *
         * @return true, if is measure family
         */
        public boolean isMeasureFamily() {
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
         * @param lockedUserInfo the new locked user info
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
         * @param shortName the new short name
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
         * @param name the new name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Gets the Measure Model.
         *
         * @return the Measure Model
         */
        public String getMeasureModel() {
            return measureModel;
        }

        /**
         * Sets the Measure Model.
         *
         * @param measureModel the new Measure Model
         */
        public void setMeasureModel(String measureModel) {
            this.measureModel = measureModel;
        }

        /**
         * Gets the scoring type.
         *
         * @return the scoring type
         */
        public String getScoringType() {
            return scoringType;
        }

        /**
         * Sets the scoring type.
         *
         * @param scoringType the new scoring type
         */
        public void setScoringType(String scoringType) {
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
         * @param status the new status
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
         * @param id the new id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * Checks if is historical.
         *
         * @return true, if is historical
         */
        public boolean isHistorical() {
            return isHistorical;
        }

        /**
         * Sets the historical.
         *
         * @param isHistorical the new historical
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
         * @param isSharable the new sharable
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
         * @param isEditable the new editable
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
         * @param isClonable the new clonable
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
         * @param isExportable the new exportable
         */
        public void setExportable(boolean isExportable) {
            this.isExportable = isExportable;
        }

        public boolean isFhirConvertible() {
            return isFhirConvertible;
        }

        public void setFhirConvertible(boolean fhirConvertible) {
            isFhirConvertible = fhirConvertible;
        }

        public boolean isMeasureEditOrViewable() {
            return this.isMeasureEditOrViewable;
        }

        public void setMeasureEditOrViewable(boolean isMeasureEditOrViewable) {
            this.isMeasureEditOrViewable = isMeasureEditOrViewable;
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
         * @param isMeasureLocked the new measure locked
         */
        public void setMeasureLocked(boolean isMeasureLocked) {
            this.isMeasureLocked = isMeasureLocked;
        }

        //This method will give the lockedUserId if the lockedUser is not null.

        /**
         * Gets the locked user id.
         *
         * @param info the info
         * @return the locked user id
         */
        public String getLockedUserId(LockedUserInfo info) {
            if (info != null)
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
         * @param version the new version
         */
        public void setVersion(String version) {
            this.version = version;
        }

        /**
         * Gets the isValidatable.
         *
         * @return the isValidatable
         */
        public boolean isValidatable() {
            return this.isValidatable;
        }

        /**
         * Sets the isValidatable.
         *
         * @param isValidatable the new isValidatable
         */
        public void setValidatable(boolean isValidatable) {
            this.isValidatable = isValidatable;
        }

        /**
         * given version is a String of the form:
         * Draft of v#.#
         *
         * @return #.#
         */
        public String getVersionValue() {
            int offset = version.lastIndexOf("v") + 1;
            if (offset < version.length())
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

        public void setTransferable(boolean isTransferable) {
            this.isTransferable = isTransferable;
        }

        public boolean isTransferable() {
            return isTransferable;
        }

        public boolean isDeleted() {
            return isDeleted;
        }

        public void setDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
        }

        public String getOwnerFirstName() {
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

        public int geteMeasureId() {
            return eMeasureId;
        }

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

        public String getFhirVersion() {
            return fhirVersion;
        }

        public void setFhirVersion(String fhirVersion) {
            this.fhirVersion = fhirVersion;
        }

        public Boolean getIsComposite() {
            return isComposite;
        }

        public void setIsComposite(Boolean isComposite) {
            this.isComposite = isComposite;
        }

        public String getCqlLibraryName() {
            return cqlLibraryName;
        }

        public void setCqlLibraryName(String cqlLibraryName) {
            this.cqlLibraryName = cqlLibraryName;
        }

        public int getClickCount() {
            return clickCount;
        }

        public int setClickCount(int val) {
            return clickCount = val;
        }

        public void incrementClickCount() {
            this.clickCount++;
        }
    }

    /**
     * The data.
     */
    private List<Result> data;

    /**
     * The start index.
     */
    private int startIndex;

    /**
     * The results total.
     */
    private int resultsTotal;

    /**
     * The page count.
     */
    private int pageCount;

    /**
     * The selected transfer ids.
     */
    private List<String> selectedTransferIds;

    /**
     * The selected transfer results.
     */
    private ArrayList<Result> selectedTransferResults;

    /**
     * Sets the data.
     *
     * @param data the new data
     */
    public void setData(List<Result> data) {
        this.data = data;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public List<Result> getData() {
        return data;
    }

    /* (non-Javadoc)
     * @see mat.client.shared.search.SearchResults#getNumberOfRows()
     */
    public int getNumberOfRows() {
        return data != null ? data.size() : 0;
    }


    public int getStartIndex() {
        return startIndex;
    }

    /**
     * Sets the start index.
     *
     * @param startIndex the new start index
     */
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getResultsTotal() {
        return resultsTotal;
    }

    /**
     * Sets the results total.
     *
     * @param resultsTotal the new results total
     */
    public void setResultsTotal(int resultsTotal) {
        this.resultsTotal = resultsTotal;
    }

    public String getKey(int row) {
        return data.get(row).getId();
    }

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
     * @param pageCount the new page count
     */
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

    public void setSelectedTransferIds(List<String> selectedTranferIds) {
        this.selectedTransferIds = selectedTranferIds;
    }

    public List<String> getSelectedTransferIds() {
        return selectedTransferIds;
    }

    public void setSelectedTransferResults(ArrayList<Result> selectedTransferResults) {
        this.selectedTransferResults = selectedTransferResults;
    }

    public List<Result> getSelectedTransferResults() {
        return selectedTransferResults;
    }

}
