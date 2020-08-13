package mat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CODE_SYSTEM")
public class CodeSystem {
	private String id;
	private String description;
	private String abbreviation;
	private Category category;
	private Set<ListObject> listObjects = new HashSet<>(0);
	
	@Column(name = "ABBREVIATION", length = 32)
	public String getAbbreviation() {
		return abbreviation;
	}
	
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	@Id
	@Column(name = "CODE_SYSTEM_ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "DESCRIPTION", nullable = false, length = 50)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID", nullable = false)
	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "codeSystem")
	public Set<ListObject> getListObjects() {
		return this.listObjects;
	}

	public void setListObjects(Set<ListObject> listObjects) {
		this.listObjects = listObjects;
	}
}
