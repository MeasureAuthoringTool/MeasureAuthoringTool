package mat.model.clause;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.Hibernate;

import mat.hibernate.HibernateConf;
import mat.server.util.XmlProcessor;

public class MeasureExport {
	
	private String id;
	
	private String simpleXML;
	
	private Blob codeList;
	
	private Measure measure;

	private String humanReadable;
	
	private String hqmf; 
	
	private String cql; 
	
	private String elm; 
	
	private String json; 
	
	private XmlProcessor hqmfXMLProcessor;
	
	private XmlProcessor simpleXMLProcessor;
	
	private XmlProcessor humanReadableProcessor;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	public String getSimpleXML() {
		return simpleXML;
	}

	public void setSimpleXML(String simpleXML) {
		this.simpleXML = simpleXML;
	}

	public Measure getMeasure() {
		return measure;
	}

	public void setMeasure(Measure measure) {
		this.measure = measure;
	}

	public byte[] getCodeListBarr() {
		return toByteArray(codeList);
	}

	public void setCodeListBarr(byte[] codeListBarr) {
		this.codeList = Hibernate.getLobCreator(HibernateConf.getHibernateSession()).createBlob(codeListBarr);
	}

  	public void setCodeList(Blob codeList) {  
		  this.codeList = codeList; 
	  } 

  	public Blob getCodeList() {  
		  return this.codeList; 
	  } 
  	
	public String getHumanReadable() {
		return humanReadable;
	}

	public void setHumanReadable(String humanReadable) {
		this.humanReadable = humanReadable;
	}

	public String getHqmf() {
		return hqmf;
	}

	public void setHqmf(String hqmf) {
		this.hqmf = hqmf;
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

	public XmlProcessor getHQMFXmlProcessor() {
		return hqmfXMLProcessor;
	}

	public void setHQMFXmlProcessor(XmlProcessor xmlProcessor) {
		this.hqmfXMLProcessor = xmlProcessor;
	}

	public XmlProcessor getSimpleXMLProcessor() {
		return simpleXMLProcessor;
	}

	public void setSimpleXMLProcessor(XmlProcessor simpleXMLProcessor) {
		this.simpleXMLProcessor = simpleXMLProcessor;
	}

	public XmlProcessor getHumanReadableProcessor() {
		return humanReadableProcessor;
	}

	public void setHumanReadableProcessor(XmlProcessor humanReadableProcessor) {
		this.humanReadableProcessor = humanReadableProcessor;
	}	
}
