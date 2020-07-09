package mat.model.clause;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import mat.hibernate.HibernateConf;

@Entity
@Table(name = "MEASURE_XML")
public class MeasureXML {
    private String id;
    private Blob measureXML;
    private String measureId;
    private String severeErrorCql;

    public void setId(String id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID", unique = true, nullable = false, length = 64)
    public String getId() {
        return id;
    }


    public void setMeasureXML(Blob measureXML) {
        this.measureXML = measureXML;
    }

    @Lob
    @Column(name = "MEASURE_XML")
    public Blob getMeasureXML() {
        return measureXML;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    @Column(name = "MEASURE_ID", nullable = false)
    public String getMeasureId() {
        return measureId;
    }

    /**
     * Gets the measure xml as string.
     *
     * @return the measure xml as string
     */
    @Transient
    public String getMeasureXMLAsString() {
        String xml = "";
        if (measureXML != null) {
            xml = new String(toByteArray(measureXML));
        }
        return xml;
    }

    /**
     * Sets the measure xml as byte array.
     *
     * @param xml the new measure xml as byte array
     */
    public void setMeasureXMLAsByteArray(String xml) {
        if (null != xml) {
            byte[] xmlByteArr = xml.getBytes();
            this.measureXML = Hibernate.getLobCreator(HibernateConf.getHibernateSession()).createBlob(xmlByteArr);
        } else {
            this.measureXML = null;
        }
    }

    /**
     * To byte array.
     *
     * @param fromBlob the from blob
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
     * @param fromBlob the from blob
     * @param baos     the baos
     * @return the byte[]
     * @throws SQLException the sQL exception
     * @throws IOException  Signals that an I/O exception has occurred.
     */
    private byte[] toByteArrayImpl(Blob fromBlob, ByteArrayOutputStream baos) throws SQLException, IOException {
        byte[] buf = new byte[4000];
        InputStream is = fromBlob.getBinaryStream();
        try {
            for (; ; ) {
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

    public String getSevereErrorCql() {
        return severeErrorCql;
    }

    public void setSevereErrorCql(String severeErrorCql) {
        this.severeErrorCql = severeErrorCql;
    }
}
