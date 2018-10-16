package mat.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.Hibernate;

import mat.hibernate.HibernateConf;
import mat.model.clause.Measure;

/**
 * Data Structure to store for information about a validation event: type, user,
 * measure, interimXML.
 * 
 * @author aschmidt
 */
public class MeasureValidationLog {
	
	/** The id. */
	private String id;
	
	/** The activity type. */
	private String activityType;	
	
	/** The time. */
	private Timestamp time;
	
	/** The user id. */
	private String userId;
	
	/** The measure. */
	private Measure measure;
	
	/** The interim blob. */
	private Blob interimBlob;
	
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
	 * Gets the activity type.
	 * 
	 * @return the activity type
	 */
	public String getActivityType() {
		return activityType;
	}
	
	/**
	 * Sets the activity type.
	 * 
	 * @param activityType
	 *            the new activity type
	 */
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	
	/**
	 * Gets the time.
	 * 
	 * @return the time
	 */
	public Timestamp getTime() {
		return time;
	}
	
	/**
	 * Sets the time.
	 * 
	 * @param created
	 *            the new time
	 */
	public void setTime(Date created) {
		this.time = new Timestamp(created.getTime());
	}
	
	/**
	 * Gets the user id.
	 * 
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * Sets the user id.
	 * 
	 * @param userId
	 *            the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
	 * Gets the interim barr.
	 * 
	 * @return the interim barr
	 */
	public byte[] getInterimBarr() {
		return toByteArray(interimBlob);
	}
	
	/**
	 * Sets the interim barr.
	 * 
	 * @param interimBarr
	 *            the new interim barr
	 */
	public void setInterimBarr(byte[] interimBarr) {
		this.interimBlob = Hibernate.getLobCreator(HibernateConf.getHibernateSession()).createBlob(interimBarr);
	}
	
	  /**
	 * Sets the interim blob.
	 * 
	 * @param codeList
	 *            the new interim blob
	 */
  	public void setInterimBlob(Blob codeList) {  
		  this.interimBlob = codeList; 
	  } 
	  
	  /**
	 * Gets the interim blob.
	 * 
	 * @return the interim blob
	 */
  	public Blob getInterimBlob() {  
		  return this.interimBlob; 
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
