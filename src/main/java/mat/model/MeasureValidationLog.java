package mat.model;

import mat.model.clause.Measure;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "MEASURE_VALIDATION_LOG")
public class MeasureValidationLog {
	
	private String id;
	
	private String activityType;	
	
	private Timestamp time;
	
	private String userId;
	
	private Measure measure;
	
	private byte[] interimBlob;
	
	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "ACTIVITY_TYPE", nullable = false, length = 40)
	public String getActivityType() {
		return activityType;
	}
	
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	
	@Column(name = "TIMESTAMP", nullable = false, length = 19)
	public Timestamp getTime() {
		return time;
	}
	
	public void setTime(Date created) {
		this.time = new Timestamp(created.getTime());
	}
	
	@Column(name = "USER_ID", nullable = false, length = 40)
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEASURE_ID", nullable = false)
	public Measure getMeasure() {
		return measure;
	}
	
	public void setMeasure(Measure measure) {
		this.measure = measure;
	}
	
	public byte[] getInterimBarr() {
		return interimBlob;
	}
	
	public void setInterimBarr(byte[] interimBarr) {
		this.interimBlob = interimBarr;
	}
	
  	public void setInterimBlob(byte[] codeList) {  
		  this.interimBlob = codeList; 
	  } 
	  
	@Column(name = "INTERIM_BLOB")
  	public byte[] getInterimBlob() {  
		  return this.interimBlob; 
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
