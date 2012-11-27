package org.ifmc.mat.model;

import java.sql.Timestamp;

/**
 * A Lightweight data structure to handle ListObject data (no codes)
 *
 * @author aschmidt
 *
 */
public class ListObjectLT {
	private String id;
	private User objectOwner;
	private String name;
	private MeasureSteward steward;
	private Timestamp lastModified;
	private Boolean draft;

	private String stewardOther;
	
	private String oid;
	private String rationale;
	private String comment;
	private Category category;
	private String codeSystemVersion;
	private CodeSystem codeSystem;
	private String measureId;
	private String codeListDeveloper;
	private String codeListContext;
	
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
	
}
