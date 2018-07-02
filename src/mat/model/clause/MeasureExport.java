package mat.model.clause;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.Hibernate;

import mat.hibernate.HibernateConf;
import mat.server.util.XmlProcessor;


/**
 * The Class MeasureExport.
 */
public class MeasureExport {
	
	/** The id. */
	private String id;
	
	/** The simple xml. */
	private String simpleXML;
	
	/** The code list. */
	private Blob codeList;
	
	/** The measure. */
	private Measure measure;

	private String humanReadable;
	
	private String hqmf; 
	
	private String cql; 
	
	private String elm; 
	
	private String json; 
	
	/** XMLProcessor instance to be used for HQMF Export related tasks**/
	private XmlProcessor hqmfXMLProcessor;
	
	private XmlProcessor simpleXMLProcessor;
	
	private XmlProcessor humanReadableProcessor;
	
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
	 * Gets the simple xml.
	 * 
	 * @return the simple xml
	 */
	public String getSimpleXML() {
		return simpleXML;
	}
	
	/**
	 * Sets the simple xml.
	 * 
	 * @param simpleXML
	 *            the new simple xml
	 */
	public void setSimpleXML(String simpleXML) {
		this.simpleXML = simpleXML;
	}

	/**
	 * Gets the measure.
	 * 
	 * @return the measure
	 */
	public Measure getMeasure() {
		return measure;
	}
	
	/**
	 * Sets the measure.
	 * 
	 * @param measure
	 *            the new measure
	 */
	public void setMeasure(Measure measure) {
		this.measure = measure;
	}
	
	/**
	 * Gets the code list barr.
	 * 
	 * @return the code list barr
	 */
	public byte[] getCodeListBarr() {
		return toByteArray(codeList);
	}
	
	/**
	 * Sets the code list barr.
	 * 
	 * @param codeListBarr
	 *            the new code list barr
	 */
	public void setCodeListBarr(byte[] codeListBarr) {
		this.codeList = Hibernate.getLobCreator(HibernateConf.getHibernateSession()).createBlob(codeListBarr);
	}
	
	  /**
	 * Sets the code list.
	 * 
	 * @param codeList
	 *            the new code list
	 */
  	public void setCodeList(Blob codeList) {  
		  this.codeList = codeList; 
	  } 
	  
	  /**
	 * Gets the code list.
	 * 
	 * @return the code list
	 */
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
	  
	  /**
	 * To byte array.
	 * 
	 * @param fromBlob
	 *            the from blob
	 * @return the byte[]
	 */
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
	  
	  /**
	 * To byte array impl.
	 * 
	 * @param fromBlob
	 *            the from blob
	 * @param baos
	 *            the baos
	 * @return the byte[]
	 * @throws SQLException
	 *             the sQL exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
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
