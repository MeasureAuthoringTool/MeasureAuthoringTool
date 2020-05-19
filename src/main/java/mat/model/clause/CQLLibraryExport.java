package mat.model.clause;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@TypeDefs({
		@TypeDef(name = "json", typeClass = JsonStringType.class),
		@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Entity
@Table(name="CQL_LIBRARY_EXPORT")
public class CQLLibraryExport {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;

	@OneToOne
	@JoinColumn(name = "CQL_LIBRARY_ID")
	private CQLLibrary cqlLibrary;

	@Column(name="CQL")
	private String cql; 
	
	@Column(name="ELM")
	private String elm; 
	
	@Column(name="JSON")
	private String json;

	@Column(name="FHIR_XML")
	private String fhirXml;

	@Column(name="ELM_JSON")
	@Type( type = "json" )
	private String elmJson;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CQLLibrary getCqlLibrary() {
		return cqlLibrary;
	}

	public void setCqlLibrary(CQLLibrary cqlLibrary) {
		this.cqlLibrary = cqlLibrary;
	}

	public String getCql() {
		return cql;
	}

	public void setCql(String cql) {
		this.cql = cql;
	}

	public String getElm() {
		return elm;
	}

	public void setElm(String elm) {
		this.elm = elm;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getFhirXml() {
		return fhirXml;
	}

	public void setFhirXml(String fhirXml) {
		this.fhirXml = fhirXml;
	}

	public String getElmJson() {
		return elmJson;
	}

	public void setElmJson(String elmJson) {
		this.elmJson = elmJson;
	}
}
