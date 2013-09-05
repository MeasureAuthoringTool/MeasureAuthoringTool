package mat.model.clause;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.Hibernate;

public class MeasureXML {
	
	private String id;
	private Blob measureXML;
	private String measure_id;
	
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setMeasureXML(Blob measureXML) {
		this.measureXML = measureXML;
	}
	public Blob getMeasureXML() {
		return measureXML;
	}
	public void setMeasure_id(String measure_id) {
		this.measure_id = measure_id;
	}
	public String getMeasure_id() {
		return measure_id;
	}
	
	public String getMeasureXMLAsString(){
		String xml = "";
		if(measureXML != null){
			xml = new String(toByteArray(measureXML));
		}
		return xml;
	}
	
	public void setMeasureXMLAsByteArray(String xml){
		if(null != xml){
			byte[] xmlByteArr = xml.getBytes();
			this.measureXML = Hibernate.createBlob(xmlByteArr);	
		}else{
			this.measureXML = null;
		}
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
	
}
