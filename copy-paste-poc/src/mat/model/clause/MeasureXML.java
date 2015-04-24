package mat.model.clause;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.Hibernate;

/**
 * The Class MeasureXML.
 */
public class MeasureXML {
	
	/** The id. */
	private String id;
	
	/** The measure xml. */
	private Blob measureXML;
	
	/** The measure_id. */
	private String measure_id;
	
	
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
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the measure xml.
	 * 
	 * @param measureXML
	 *            the new measure xml
	 */
	public void setMeasureXML(Blob measureXML) {
		this.measureXML = measureXML;
	}
	
	/**
	 * Gets the measure xml.
	 * 
	 * @return the measure xml
	 */
	public Blob getMeasureXML() {
		return measureXML;
	}
	
	/**
	 * Sets the measure_id.
	 * 
	 * @param measure_id
	 *            the new measure_id
	 */
	public void setMeasure_id(String measure_id) {
		this.measure_id = measure_id;
	}
	
	/**
	 * Gets the measure_id.
	 * 
	 * @return the measure_id
	 */
	public String getMeasure_id() {
		return measure_id;
	}
	
	/**
	 * Gets the measure xml as string.
	 * 
	 * @return the measure xml as string
	 */
	public String getMeasureXMLAsString(){
		String xml = "";
		if(measureXML != null){
			xml = new String(toByteArray(measureXML));
		}
		return xml;
	}
	
	/**
	 * Sets the measure xml as byte array.
	 * 
	 * @param xml
	 *            the new measure xml as byte array
	 */
	public void setMeasureXMLAsByteArray(String xml){
		if(null != xml){
			byte[] xmlByteArr = xml.getBytes();
			this.measureXML = Hibernate.createBlob(xmlByteArr);	
		}else{
			this.measureXML = null;
		}
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
	
}
