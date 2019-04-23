package mat.model.clause;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import mat.hibernate.HibernateConf;
import mat.model.User;
import mat.model.cql.CQLLibraryShare;

@Entity
@Table(name = "CQL_LIBRARY")
public class CQLLibrary {

	private String id;

	private String name;

	private String measureId;

	private User ownerId;
	
	private String set_id;

	private String version;

	private boolean draft;

	private Timestamp finalizedDate;

	private String releaseVersion;
	
	private String revisionNumber;
	
	private String qdmVersion;

	private User lockedUserId;

	private Timestamp lockedOutDate;
	
	private LocalDateTime lastModifiedOn; 
	
	private User lastModifiedBy;
	
	private Set<CQLLibraryShare> shares;

	private Blob cqlXML;

	@Id
	@GeneratedValue(generator="uuid")
	@GenericGenerator(name="uuid", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 64)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "CQL_NAME", length = 500)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "MEASURE_ID", length = 64)
	public String getMeasureId() {
		return measureId;
	}

	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OWNER_ID", nullable = false)
	public User getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(User ownerId) {
		this.ownerId = ownerId;
	}

	@Column(name = "VERSION", precision = 6, scale = 3)
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "QDM_VERSION", nullable = false, length = 45)
	public String getQdmVersion() {
		return qdmVersion;
	}

	public void setQdmVersion(String qdmVersion) {
		this.qdmVersion = qdmVersion;
	}

	@Column(name = "DRAFT")
	public boolean isDraft() {
		return draft;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
	}

	@Column(name = "FINALIZED_DATE", length = 19)
	public Timestamp getFinalizedDate() {
		return finalizedDate;
	}

	public void setFinalizedDate(Timestamp finalizedDate) {
		this.finalizedDate = finalizedDate;
	}

	@Lob
	@Column(name = "CQL_XML")
	public Blob getCqlXML() {
		return cqlXML;
	}
	
	public void setCqlXML(Blob cqlXML) {
		this.cqlXML = cqlXML; 
	}

	@Transient
	public void setCQLByteArray(byte[] cqlByteArray) {
		this.cqlXML = Hibernate.getLobCreator(HibernateConf.getHibernateSession()).createBlob(cqlByteArray);
	}

	@Transient
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

	@Column(name = "RELEASE_VERSION", length = 45)
	public String getReleaseVersion() {
		return releaseVersion;
	}

	public void setReleaseVersion(String releaseVersion) {
		this.releaseVersion = releaseVersion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCKED_USER")
	public User getLockedUserId() {
		return lockedUserId;
	}

	public void setLockedUserId(User lockedUserId) {
		this.lockedUserId = lockedUserId;
	}

	@Column(name = "LOCKED_OUT_DATE", length = 19)
	public Timestamp getLockedOutDate() {
		return lockedOutDate;
	}

	public void setLockedOutDate(Timestamp lockedOutDate) {
		this.lockedOutDate = lockedOutDate;
	}

	@Column(name = "REVISION_NUMBER", length = 45)
	public String getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(String revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	@Transient
	public double getVersionNumber(){
		double versionNumber = 0;
		if ((version != null) && !version.isEmpty()) {
			versionNumber = Double.parseDouble(version);
		}
		return versionNumber;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cqlLibrary")
	public Set<CQLLibraryShare> getShares() {
		return shares;
	}

	public void setShares(Set<CQLLibraryShare> shares) {
		this.shares = shares;
	}

	@Column(name = "SET_ID", nullable = false, length = 45)
	public String getSet_id() {
		return set_id;
	}

	public void setSet_id(String set_id) {
		this.set_id = set_id;
	}
	
	@Column(name = "LAST_MODIFIED_ON", length = 19)
	public LocalDateTime getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(LocalDateTime lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LAST_MODIFIED_BY")
	public User getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

}
