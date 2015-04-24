package mat.reportmodel;



/*
 * This is to create group code list objects and also to group the code lists.
 * This creation of group code list is independent of Code list, although it is
 * parent of Code list class. In nut shell, this class works as an independent class.
 */
public class ListObject {
	private String id;
	private String version;
	private String name;
	
	private User objectOwner;
	
	//US 413
	private String stewardOther;
	
	private String oid;
	private String rationale;
	private String comment;
	private Category category;
	private String codeSystemVersion;
	private String measureId;
	private String codeListDeveloper;
	private String codeListContext;
	
	public String getCodeSystemVersion() {
		return codeSystemVersion;
	}
	public void setCodeSystemVersion(String codeSystemVersion) {
		this.codeSystemVersion = codeSystemVersion;
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
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getMeasureId() {
		return measureId;
	}
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
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
	public User getObjectOwner() {
		return objectOwner;
	}
	public void setObjectOwner(User objectOwner) {
		this.objectOwner = objectOwner;
	}
	
	
}
