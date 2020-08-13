package mat.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Set;

/*
 * This is to create group code list objects and also to group the code lists.
 * This creation of group code list is independent of Code list, although it is
 * parent of Code list class. In nut shell, this class works as an independent class.
 */
@Entity
@Table(name = "LIST_OBJECT")
@Inheritance(strategy=InheritanceType.JOINED)
public class ListObject implements Cloneable {
	
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
	
	private Set<GroupedCodeList> codesLists;
	
	private String codeSystemVersion;
	
	private CodeSystem codeSystem;
	
	private Set<Code> codes;
	
	private String measureId;
	
	private String codeListDeveloper;
	
	private String codeListContext;
	
	private Set<QualityDataSet> qualityDataSets;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "id")
	public Set<Code> getCodes() {
		return codes;
	}
	
	public void setCodes(Set<Code> codes) {
		this.codes = codes;
	}
	
	@Column(name = "CODE_SYS_VERSION", nullable = false)
	public String getCodeSystemVersion() {
		return codeSystemVersion;
	}
	
	public void setCodeSystemVersion(String codeSystemVersion) {
		this.codeSystemVersion = codeSystemVersion;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODE_SYSTEM_ID", nullable = false)
	public CodeSystem getCodeSystem() {
		return codeSystem;
	}
	
	public void setCodeSystem(CodeSystem codeSystem) {
		this.codeSystem = codeSystem;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "listObject")
	public Set<GroupedCodeList> getCodesLists() {
		return codesLists;
	}

	public void setCodesLists(Set<GroupedCodeList> codesLists) {
		this.codesLists = codesLists;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID", nullable = false)
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STEWARD")
	public MeasureSteward getSteward() {
		return steward;
	}
	
	public void setSteward(MeasureSteward steward) {
		this.steward = steward;
	}
	
	@Column(name = "OID", nullable = false)
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}

	@Column(name = "RATIONALE", nullable = false, length = 2000)
	public String getRationale() {
		return rationale;
	}
	
	public void setRationale(String rationale) {
		this.rationale = rationale;
	}
	
	@Column(name = "COMMENT", length = 2000)
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OBJECT_OWNER", nullable = false, insertable=false, updatable=false)
	public User getObjectOwner() {
		return objectOwner;
	}
	
	public void setObjectOwner(User objectOwner) {
		this.objectOwner = objectOwner;
	}
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy = "uuid")
	@Column(name = "LIST_OBJECT_ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "MEASURE_ID")
	public String getMeasureId() {
		return measureId;
	}
	
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "listObject")
	public Set<QualityDataSet> getQualityDataSets() {
		return qualityDataSets;
	}
	
	public void setQualityDataSets(Set<QualityDataSet> qualityDataSets) {
		this.qualityDataSets = qualityDataSets;
	}
	
	//US 413
	@Column(name = "STEWARD_OTHER", length = 200)
	public String getStewardOther() {
		return stewardOther;
	}
	
	public void setStewardOther(String stewardOther) {
		this.stewardOther = stewardOther;
	}	
	
	@Column(name = "CODE_LIST_DEVELOPER", length = 200)
	public String getCodeListDeveloper() {
		return codeListDeveloper;
	}
	
	public void setCodeListDeveloper(String codeListDeveloper) {
		this.codeListDeveloper = codeListDeveloper;
	}
	
	@Column(name = "CODE_LIST_CONTEXT", length = 200)
	public String getCodeListContext() {
		return codeListContext;
	}
	
	public void setCodeListContext(String codeListContext) {
		this.codeListContext = codeListContext;
	}
	
	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}
	
	@Column(name = "LAST_MODIFIED", length = 19)
	public Timestamp getLastModified() {
		return lastModified;
	}
	
	public void setDraft(Boolean draft) {
		this.draft = draft;
	}
	
	@Column(name = "DRAFT", nullable = false)
	public Boolean isDraft() {
		return draft;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
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
