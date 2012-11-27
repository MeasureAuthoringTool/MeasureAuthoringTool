package mat.model.clause;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.Hibernate;


public class MeasureExport {
	private String id;
	private String simpleXML;
	private Blob codeList;
	private Measure measure;
	
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
		this.codeList = Hibernate.createBlob(codeListBarr);
	}
	
	  public void setCodeList(Blob codeList) {  
		  this.codeList = codeList; 
	  } 
	  
	  public Blob getCodeList() {  
		  return this.codeList; 
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
				  }
			  }  
		  }  
		  return baos.toByteArray();
	}
	  
	
}
