package mat.model.cql;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CQL_LIBRARY_ASSOCIATION")
public class CQLLibraryAssociation {

	private String id;
	
	private String associationId;

	private String cqlLibraryId;


	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 64)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "CQL_LIBRARY_ID", nullable = false, length = 64)
	public String getCqlLibraryId() {
		return cqlLibraryId;
	}


	public void setCqlLibraryId(String cqlLibraryId) {
		this.cqlLibraryId = cqlLibraryId;
	}


	@Column(name = "ASSOCIATION_ID", nullable = false, length = 64)
	public String getAssociationId() {
		return associationId;
	}


	public void setAssociationId(String associationId) {
		this.associationId = associationId;
	}

	
}
