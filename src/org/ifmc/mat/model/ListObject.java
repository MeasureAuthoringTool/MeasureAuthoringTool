package org.ifmc.mat.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/*
 * This is to create group code list objects and also to group the code lists.
 * This creation of group code list is independent of Code list, although it is
 * parent of Code list class. In nut shell, this class works as an independent class.
 */
public class ListObject {
	private String id;
	private User objectOwner;
	private String name;
	private MeasureSteward steward;
	private Timestamp lastModified;
	private Boolean draft;
	
	//US 413
	private String stewardOther;
	
	private String oid;
	private String rationale;
	private String comment;
	private Category category;
	private Set<GroupedCodeList> codesLists = new HashSet<GroupedCodeList>();
	private String codeSystemVersion;
	private CodeSystem codeSystem;
	private Set<Code> codes = new HashSet<Code>();
	private String measureId;
	private String codeListDeveloper;
	private String codeListContext;
	private Set<QualityDataSet> qualityDataSets;
	
	public Set<Code> getCodes() {
		return codes;
	}
	public void setCodes(Set<Code> codes) {
		this.codes = codes;
	}
	public String getCodeSystemVersion() {
		return codeSystemVersion;
	}
	public void setCodeSystemVersion(String codeSystemVersion) {
		this.codeSystemVersion = codeSystemVersion;
	}
	public CodeSystem getCodeSystem() {
		return codeSystem;
	}
	public void setCodeSystem(CodeSystem codeSystem) {
		this.codeSystem = codeSystem;
	}

	public Set<GroupedCodeList> getCodesLists() {
		return codesLists;
	}

	public void setCodesLists(Set<GroupedCodeList> codesLists) {
		this.codesLists = codesLists;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public MeasureSteward getSteward() {
		return steward;
	}
	public void setSteward(MeasureSteward steward) {
		this.steward = steward;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getRationale() {
		return rationale;
	}
	public void setRationale(String rationale) {
		this.rationale = rationale;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public User getObjectOwner() {
		return objectOwner;
	}
	public void setObjectOwner(User objectOwner) {
		this.objectOwner = objectOwner;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getMeasureId() {
		return measureId;
	}
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}
	public Set<QualityDataSet> getQualityDataSets() {
		return qualityDataSets;
	}
	public void setQualityDataSets(Set<QualityDataSet> qualityDataSets) {
		this.qualityDataSets = qualityDataSets;
	}
	
	//US 413
	public String getStewardOther() {
		return stewardOther;
	}
	public void setStewardOther(String stewardOther) {
		this.stewardOther = stewardOther;
	}	
	
	public String getCodeListDeveloper() {
		return codeListDeveloper;
	}
	public void setCodeListDeveloper(String codeListDeveloper) {
		this.codeListDeveloper = codeListDeveloper;
	}
	public String getCodeListContext() {
		return codeListContext;
	}
	public void setCodeListContext(String codeListContext) {
		this.codeListContext = codeListContext;
	}
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}
	public Timestamp getLastModified() {
		return lastModified;
	}
	public void setDraft(Boolean draft) {
		this.draft = draft;
	}
	public Boolean isDraft() {
		return draft;
	}
	
	public ListObject clone(){
		//grouped value sets are list objects, regular value sets are code lists
		ListObject clone = this instanceof CodeList ? new CodeList() :
			new ListObject();
		
		clone.setCategory(this.getCategory());
		clone.setCodeListContext(this.getCodeListContext());
		clone.setCodeListDeveloper(this.getCodeListDeveloper());
		for(Code c : this.getCodes()){
			clone.getCodes().add(c.clone());
		}
		for(GroupedCodeList gcl : this.getCodesLists()){
			clone.getCodesLists().add(gcl.clone());
		}
		
		clone.setCodeSystem(this.getCodeSystem());
		clone.setCodeSystemVersion(this.getCodeSystemVersion());
		clone.setComment(this.getComment());
		clone.setDraft(this.isDraft());
		// do not set id
		clone.setLastModified(this.getLastModified());
		// do not set measure id
		clone.setName(this.getName());
		clone.setObjectOwner(this.getObjectOwner());
		clone.setOid(this.getOid());
		// do not set quality data sets
		clone.setRationale(this.getRationale());
		clone.setSteward(this.getSteward());
		clone.setStewardOther(this.getStewardOther());
		
		return clone;
	}
}
