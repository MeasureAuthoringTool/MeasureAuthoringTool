package mat.model.clause;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import mat.hibernate.HibernateConf;
import mat.server.util.XmlProcessor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@TypeDefs({
		@TypeDef(name = "json", typeClass = JsonStringType.class),
		@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Entity
@Table(name = "MEASURE_EXPORT")
public class MeasureExport {

	private String id;

	private String simpleXML;

	private  Blob codeList;

	private Measure measure;

	private String humanReadable;

	private String hqmf;

	private String cql;

	private String elm;

	private String json;

	private String elmJson;

	private String fhirXml;

	private String fhirLibsXml;

	private String fhirLibsJson;

	private XmlProcessor hqmfXMLProcessor;

	private XmlProcessor simpleXMLProcessor;

	private XmlProcessor humanReadableProcessor;

	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy = "uuid")
	@Column(name = "MEASURE_EXPORT_ID", unique = true, nullable = false, length = 64)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "SIMPLE_XML", nullable = false)
	public String getSimpleXML() {
		return simpleXML;
	}

	public void setSimpleXML(String simpleXML) {
		this.simpleXML = simpleXML;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEASURE_ID", nullable = false)
	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	@Transient
	public byte[] getCodeListBarr() {
		return toByteArray(codeList);
	}

	@Transient
	public void setCodeListBarr(byte[] codeListBarr) {
		this.codeList = Hibernate.getLobCreator(HibernateConf.getHibernateSession()).createBlob(codeListBarr);
	}

  	public void setCodeList(Blob codeList) {
		  this.codeList = codeList;
	}

  	@Column(name = "CODE_LIST")
  	public  Blob getCodeList() {
		  return this.codeList;
	}

  	@Column(name = "HUMAN_READABLE")
	public String getHumanReadable() {
		return humanReadable;
	}

	public void setHumanReadable(String humanReadable) {
		this.humanReadable = humanReadable;
	}

	@Column(name = "HQMF")
	public String getHqmf() {
		return hqmf;
	}

	public void setHqmf(String hqmf) {
		this.hqmf = hqmf;
	}

	@Column(name = "CQL")
	public String getCql() {
		return cql;
	}

	public void setCql(String cql) {
		this.cql = cql;
	}

	@Column(name = "ELM")
	public String getElm() {
		return elm;
	}

	public void setElm(String elm) {
		this.elm = elm;
	}

	@Column(name = "JSON")
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	@Column(name = "ELM_JSON")
	@Type( type = "json" )
	public String getElmJson() {
		return elmJson;
	}

	public void setElmJson(String elmJson) {
		this.elmJson = elmJson;
	}

	@Column(name = "FHIR_XML")
	public String getFhirXml() {
		return fhirXml;
	}

	public void setFhirXml(String fhirXml) {
		this.fhirXml = fhirXml;
	}

	@Column(name = "FHIR_LIBS_XML")
	public String getFhirLibsXml() {
		return fhirLibsXml;
	}

	public void setFhirLibsXml(String fhirLibsXml) {
		this.fhirLibsXml = fhirLibsXml;
	}

	@Column(name = "FHIR_LIBS_JSON")
	@Type( type = "json" )
	public String getFhirLibsJson() {
		return fhirLibsJson;
	}

	public void setFhirLibsJson(String fhirLibsJson) {
		this.fhirLibsJson = fhirLibsJson;
	}

	private byte[] toByteArray(Blob fromBlob) {
		  ByteArrayOutputStream baos = new ByteArrayOutputStream();
		  try {
			  return toByteArrayImpl(fromBlob, baos);
		  } catch (SQLException e) {
			  throw new RuntimeException(e);
		  } catch (IOException e) {
			  throw new RuntimeException(e);
		  } finally {
			  if (baos != null) {
				  try {
					  baos.close();
				  } catch (IOException ex) {
					  ex.printStackTrace();
				  }
			  }
		  }
	  }

  	private byte[] toByteArrayImpl(Blob fromBlob, ByteArrayOutputStream baos) throws SQLException, IOException {
		  byte[] buf = new byte[4000];
		  InputStream is = fromBlob.getBinaryStream();
		  try {
			  for (;;) {
				  int dataSize = is.read(buf);
				  if (dataSize == -1)
					  break;
				  baos.write(buf, 0, dataSize);
			  }
		  }
		  finally {
			  if (is != null) {
				  try {
					  is.close();
				  } catch (IOException ex) {
					  ex.printStackTrace();
				  }
			  }
		  }
		  return baos.toByteArray();
	}

  	@Transient
	public XmlProcessor getHQMFXmlProcessor() {
		return hqmfXMLProcessor;
	}

	public void setHQMFXmlProcessor(XmlProcessor xmlProcessor) {
		this.hqmfXMLProcessor = xmlProcessor;
	}

  	@Transient
	public XmlProcessor getSimpleXMLProcessor() {
		return simpleXMLProcessor;
	}

	public void setSimpleXMLProcessor(XmlProcessor simpleXMLProcessor) {
		this.simpleXMLProcessor = simpleXMLProcessor;
	}

  	@Transient
	public XmlProcessor getHumanReadableProcessor() {
		return humanReadableProcessor;
	}

	public void setHumanReadableProcessor(XmlProcessor humanReadableProcessor) {
		this.humanReadableProcessor = humanReadableProcessor;
	}
}
