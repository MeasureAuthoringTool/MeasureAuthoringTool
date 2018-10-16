package mat.model;

import java.sql.Timestamp;

/**
 * A Lightweight data structure to handle ListObject data (no codes).
 * 
 * @author aschmidt
 */
public class ListObjectLT {
	
	/** The id. */
	private String id;
	
	/** The object owner. */
	private User objectOwner;
	
	/** The name. */
	private String name;
	
	/** The steward. */
	private MeasureSteward steward;
	
	/** The last modified. */
	private Timestamp lastModified;
	
	/** The draft. */
	private Boolean draft;

	/** The steward other. */
	private String stewardOther;
	
	/** The oid. */
	private String oid;
	
	/** The rationale. */
	private String rationale;
	
	/** The comment. */
	private String comment;
	
	/** The category. */
	private Category category;
	
	/** The code system version. */
	private String codeSystemVersion;
	
	/** The code system. */
	private CodeSystem codeSystem;
	
	/** The measure id. */
	private String measureId;
	
	/** The code list developer. */
	private String codeListDeveloper;
	
	/** The code list context. */
	private String codeListContext;
	
	/**
	 * Gets the code system version.
	 * 
	 * @return the code system version
	 */
	public String getCodeSystemVersion() {
		return codeSystemVersion;
	}
	
	/**
	 * Sets the code system version.
	 * 
	 * @param codeSystemVersion
	 *            the new code system version
	 */
	public void setCodeSystemVersion(String codeSystemVersion) {
		this.codeSystemVersion = codeSystemVersion;
	}
	
	/**
	 * Gets the code system.
	 * 
	 * @return the code system
	 */
	public CodeSystem getCodeSystem() {
		return codeSystem;
	}
	
	/**
	 * Sets the code system.
	 * 
	 * @param codeSystem
	 *            the new code system
	 */
	public void setCodeSystem(CodeSystem codeSystem) {
		this.codeSystem = codeSystem;
	}

	/**
	 * Gets the category.
	 * 
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}
	
	/**
	 * Sets the category.
	 * 
	 * @param category
	 *            the new category
	 */
	public void setCategory(Category category) {
		this.category = category;
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
	 * Gets the steward.
	 * 
	 * @return the steward
	 */
	public MeasureSteward getSteward() {
		return steward;
	}
	
	/**
	 * Sets the steward.
	 * 
	 * @param steward
	 *            the new steward
	 */
	public void setSteward(MeasureSteward steward) {
		this.steward = steward;
	}
	
	/**
	 * Gets the oid.
	 * 
	 * @return the oid
	 */
	public String getOid() {
		return oid;
	}
	
	/**
	 * Sets the oid.
	 * 
	 * @param oid
	 *            the new oid
	 */
	public void setOid(String oid) {
		this.oid = oid;
	}

	/**
	 * Gets the rationale.
	 * 
	 * @return the rationale
	 */
	public String getRationale() {
		return rationale;
	}
	
	/**
	 * Sets the rationale.
	 * 
	 * @param rationale
	 *            the new rationale
	 */
	public void setRationale(String rationale) {
		this.rationale = rationale;
	}
	
	/**
	 * Gets the comment.
	 * 
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * Sets the comment.
	 * 
	 * @param comment
	 *            the new comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * Gets the object owner.
	 * 
	 * @return the object owner
	 */
	public User getObjectOwner() {
		return objectOwner;
	}
	
	/**
	 * Sets the object owner.
	 * 
	 * @param objectOwner
	 *            the new object owner
	 */
	public void setObjectOwner(User objectOwner) {
		this.objectOwner = objectOwner;
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
	 * Gets the measure id.
	 * 
	 * @return the measure id
	 */
	public String getMeasureId() {
		return measureId;
	}
	
	/**
	 * Sets the measure id.
	 * 
	 * @param measureId
	 *            the new measure id
	 */
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

	/**
	 * Gets the steward other.
	 * 
	 * @return the steward other
	 */
	public String getStewardOther() {
		return stewardOther;
	}
	
	/**
	 * Sets the steward other.
	 * 
	 * @param stewardOther
	 *            the new steward other
	 */
	public void setStewardOther(String stewardOther) {
		this.stewardOther = stewardOther;
	}	
	
	/**
	 * Gets the code list developer.
	 * 
	 * @return the code list developer
	 */
	public String getCodeListDeveloper() {
		return codeListDeveloper;
	}
	
	/**
	 * Sets the code list developer.
	 * 
	 * @param codeListDeveloper
	 *            the new code list developer
	 */
	public void setCodeListDeveloper(String codeListDeveloper) {
		this.codeListDeveloper = codeListDeveloper;
	}
	
	/**
	 * Gets the code list context.
	 * 
	 * @return the code list context
	 */
	public String getCodeListContext() {
		return codeListContext;
	}
	
	/**
	 * Sets the code list context.
	 * 
	 * @param codeListContext
	 *            the new code list context
	 */
	public void setCodeListContext(String codeListContext) {
		this.codeListContext = codeListContext;
	}
	
	/**
	 * Sets the last modified.
	 * 
	 * @param lastModified
	 *            the new last modified
	 */
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}
	
	/**
	 * Gets the last modified.
	 * 
	 * @return the last modified
	 */
	public Timestamp getLastModified() {
		return lastModified;
	}
	
	/**
	 * Sets the draft.
	 * 
	 * @param draft
	 *            the new draft
	 */
	public void setDraft(Boolean draft) {
		this.draft = draft;
	}
	
	/**
	 * Checks if is draft.
	 * 
	 * @return the boolean
	 */
	public Boolean isDraft() {
		return draft;
	}
	
}
