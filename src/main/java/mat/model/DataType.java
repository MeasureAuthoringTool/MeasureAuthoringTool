package mat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DATA_TYPE")
public class DataType {
	private String id;
	private String description;
	private Category category;
	
	@Id
	@Column(name = "DATA_TYPE_ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID", nullable = false)
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Column(name = "DESCRIPTION", nullable = false, length = 50)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	//TODO
/*	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dataType")
	public Set<QualityDataModel> getQualityDataModels() {
		return this.qualityDataModels;
	}

	public void setQualityDataModels(Set<QualityDataModel> qualityDataModels) {
		this.qualityDataModels = qualityDataModels;
	}*/
	
}
