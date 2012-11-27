package mat.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import mat.model.clause.Measure;

import org.hibernate.Hibernate;

/**
 * Data Structure to store for information about a validation event: type, user, measure, interimXML
 * @author aschmidt
 *
 */
public class MeasureValidationLog {
	
	private String id;
	private String activityType;	
	private Timestamp time;
	private String userId;
	private Measure measure;
	private Blob interimBlob;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Date created) {
		this.time = new Timestamp(created.getTime());
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Measure getMeasure() {
		return measure;
	}
	public void setMeasure(Measure measure) {
		this.measure = measure;
	}
	
	public byte[] getInterimBarr() {
		return toByteArray(interimBlob);
	}
	public void setInterimBarr(byte[] interimBarr) {
		this.interimBlob = Hibernate.createBlob(interimBarr);
	}
	
	  public void setInterimBlob(Blob codeList) {  
		  this.interimBlob = codeList; 
	  } 
	  
	  public Blob getInterimBlob() {  
		  return this.interimBlob; 
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
