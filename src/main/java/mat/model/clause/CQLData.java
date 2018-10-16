package mat.model.clause;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.Hibernate;

import mat.hibernate.HibernateConf;

/**
 * The Class CQLData.
 */
public class CQLData {

	/** The id. */
	private String id;

	/** The cql string. */
	private Blob cqlString;

	/** The clq_id. */
	private String measure_id;

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
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the cql string.
	 *
	 * @return the cql string
	 */
	public Blob getCqlString() {
		return cqlString;
	}

	/**
	 * Sets the cql string.
	 *
	 * @param cqlString the new cql string
	 */
	public void setCqlString(Blob cqlString) {
		this.cqlString = cqlString;
	}


	public String getCQLAsString(){
		String xml = "";
		if(cqlString != null){
			xml = new String(toByteArray(cqlString));
		}
		return xml;
	}

	public void setMeasureXMLAsByteArray(String xml){
		if(null != xml){
			byte[] xmlByteArr = xml.getBytes();
			cqlString = Hibernate.getLobCreator(HibernateConf.getHibernateSession()).createBlob(xmlByteArr);
		}else{
			cqlString = null;
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
				if (dataSize == -1) {
					break;
				}
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

	public String getMeasure_id() {
		return measure_id;
	}

	public void setMeasure_id(String measure_id) {
		this.measure_id = measure_id;
	}



}
