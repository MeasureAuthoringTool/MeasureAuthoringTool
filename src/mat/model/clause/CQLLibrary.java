package mat.model.clause;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Set;

import org.hibernate.Hibernate;

import mat.model.User;
import mat.model.cql.CQLLibraryShare;

public class CQLLibrary {

	/**
	 * The cql library id
	 */
	private String id;

	/**
	 * The name
	 */
	private String name;

	/**
	 * The measure id
	 */
	private String measureId;

	/**
	 * The owner id
	 */
	private User ownerId;

	/**
	 * The measure set id
	 */
	/*private MeasureSet measureSet;

	 * The cql set id
	 *//*
	private CQLLibrarySet cqlSet;*/
	
	private String set_id;

	/**
	 * The version
	 */
	private String version;

	/**
	 * The draft
	 */
	private boolean draft;

	/**
	 * The finalized date
	 */
	private Timestamp finalizedDate;

	/**
	 * The release version
	 */
	private String releaseVersion;
	
	private String revisionNumber;
	
	/**
	 * The qdm version
	 */
	private String qdmVersion;

	/**
	 * The locked user id
	 */
	private User lockedUserId;

	/**
	 * The locked out date
	 */
	private Timestamp lockedOutDate;
	
	/** The shares. */
	private Set<CQLLibraryShare> shares;

	/**
	 * The cql xml
	 */
	private Blob cqlXML;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMeasureId() {
		return measureId;
	}

	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

	public User getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(User ownerId) {
		this.ownerId = ownerId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getQdmVersion() {
		return qdmVersion;
	}

	public void setQdmVersion(String qdmVersion) {
		this.qdmVersion = qdmVersion;
	}

	public boolean isDraft() {
		return draft;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
	}

	public Timestamp getFinalizedDate() {
		return finalizedDate;
	}

	public void setFinalizedDate(Timestamp finalizedDate) {
		this.finalizedDate = finalizedDate;
	}

	public Blob getCqlXML() {
		return cqlXML;
	}
	
	public void setCqlXML(Blob cqlXML) {
		this.cqlXML = cqlXML; 
	}

	public void setCQLByteArray(byte[] cqlByteArray) {
		this.cqlXML = Hibernate.createBlob(cqlByteArray);
	}

	public byte[] getCQLByteArray() {
		return toByteArray(cqlXML);
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
		} finally {
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

	public String getReleaseVersion() {
		return releaseVersion;
	}

	public void setReleaseVersion(String releaseVersion) {
		this.releaseVersion = releaseVersion;
	}

	public User getLockedUserId() {
		return lockedUserId;
	}

	public void setLockedUserId(User lockedUserId) {
		this.lockedUserId = lockedUserId;
	}

	public Timestamp getLockedOutDate() {
		return lockedOutDate;
	}

	public void setLockedOutDate(Timestamp lockedOutDate) {
		this.lockedOutDate = lockedOutDate;
	}

	/*public MeasureSet getMeasureSet() {
		return measureSet;
	}

	public void setMeasureSet(MeasureSet measureSetId) {
		this.measureSet = measureSetId;
	}

	public CQLLibrarySet getCqlSet() {
		return cqlSet;
	}

	public void setCqlSet(CQLLibrarySet cqlSet) {
		this.cqlSet = cqlSet;
	}*/

	public String getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(String revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	public double getVersionNumber(){
		double versionNumber = 0;
		if ((version != null) && !version.isEmpty()) {
			versionNumber = Double.valueOf(version).doubleValue();
		}
		return versionNumber;
	}

	public Set<CQLLibraryShare> getShares() {
		return shares;
	}

	public void setShares(Set<CQLLibraryShare> shares) {
		this.shares = shares;
	}

	public String getSet_id() {
		return set_id;
	}

	public void setSet_id(String set_id) {
		this.set_id = set_id;
	}
	
}
