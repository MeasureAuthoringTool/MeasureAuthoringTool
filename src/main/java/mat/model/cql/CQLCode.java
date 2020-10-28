package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Objects;

/**
 * The Class CQLCode.
 */
public class CQLCode implements CQLExpression, IsSerializable {

    /**
     * The id.
     */
    private String id;

    /**
     * The code system.
     */
    private String codeName;

    /**
     * The code system name.
     */
    private String codeSystemName;

    /**
     * The code system version.
     */
    private String codeSystemVersion;

    /**
     * The code system version uri.
     */
    private String codeSystemVersionUri;

    private String codeSystemOID;

    /**
     * The OID.
     */
    private String codeOID;

    /**
     * The Display Name.
     */
    private String displayName;


    private String codeIdentifier;

    private boolean isUsed;

    private boolean readOnly;

    private String suffix;

    private boolean isCodeSystemVersionIncluded;


    private String isValidatedWithVsac = VsacStatus.VALID.toString();



    public void setValidatedWithVsac(String validatedWithVsac) {
        isValidatedWithVsac = validatedWithVsac;
    }


    public boolean isIsCodeSystemVersionIncluded() {
        return isCodeSystemVersionIncluded;
    }


    public void setIsCodeSystemVersionIncluded(boolean isCodeSystemVersionIncluded) {
        this.isCodeSystemVersionIncluded = isCodeSystemVersionIncluded;
    }

    /**
     * Gets the code system name.
     *
     * @return the code system name
     */
    public String getCodeSystemName() {
        return codeSystemName;
    }

    /**
     * Sets the code system name.
     *
     * @param codeSystemName the new code system name
     */
    public void setCodeSystemName(String codeSystemName) {
        this.codeSystemName = codeSystemName;
    }

    /**
     * Gets the code system version.
     *
     * @return the code system version
     */
    public String getCodeSystemVersion() {
        return codeSystemVersion;
    }

    /**
     * Sets the code system version.
     *
     * @param codeSystemVersion the new code system version
     */
    public void setCodeSystemVersion(String codeSystemVersion) {
        this.codeSystemVersion = codeSystemVersion;
    }

    public String getCodeOID() {
        return codeOID;
    }

    public void setCodeOID(String codeOID) {
        this.codeOID = codeOID;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public String getCodeIdentifier() {
        return codeIdentifier;
    }

    public void setCodeIdentifier(String codeIdentifier) {
        this.codeIdentifier = codeIdentifier;
    }

    public String getCodeSystemOID() {
        return codeSystemOID;
    }

    public void setCodeSystemOID(String codeSystemOID) {
        this.codeSystemOID = codeSystemOID;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof CQLCode)) {
            return false;
        }
        CQLCode castOther = (CQLCode) other;
        return Objects.equals(codeName, castOther.codeName) && Objects.equals(codeSystemName, castOther.codeSystemName)
                && Objects.equals(codeSystemVersion, castOther.codeSystemVersion) && Objects.equals(codeSystemOID, castOther.codeSystemOID)
                && Objects.equals(codeOID, castOther.codeOID) && Objects.equals(codeIdentifier, castOther.codeIdentifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeName, codeSystemName, codeSystemVersion, codeSystemOID, codeOID, codeIdentifier);
    }

    public String getCodeName() {
        return this.codeName;
    }

    public void setCodeName(String name) {
        this.codeName = name;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return getCodeName();
    }

    @Override
    public void setName(String name) {
        setCodeName(name);
    }


    @Override
    public String getLogic() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void setLogic(String logic) {
        // TODO Auto-generated method stub

    }

    public String getCodeSystemVersionUri() {
        return codeSystemVersionUri;
    }

    public void setCodeSystemVersionUri(String codeSystemVersionUri) {
        this.codeSystemVersionUri = codeSystemVersionUri;
    }

    public String isValidatedWithVsac() {
        return isValidatedWithVsac;
    }

    public VsacStatus obtainValidatedWithVsac() {
        if (isValidatedWithVsac == null) {
            return null;
        }

        return VsacStatus.valueOf(isValidatedWithVsac);
    }

    public String getValidatedWithVsac() {
        return isValidatedWithVsac;
    }

    public void addValidatedWithVsac(VsacStatus validatedWithVsac) {
        isValidatedWithVsac = validatedWithVsac.toString();
    }
    @Override
    public String toString() {
        return "CQLCode{" +
                "id='" + id + '\'' +
                ", codeName='" + codeName + '\'' +
                ", codeSystemName='" + codeSystemName + '\'' +
                ", codeSystemVersion='" + codeSystemVersion + '\'' +
                ", codeSystemVersionUri='" + codeSystemVersionUri + '\'' +
                ", codeSystemOID='" + codeSystemOID + '\'' +
                ", codeOID='" + codeOID + '\'' +
                ", displayName='" + displayName + '\'' +
                ", codeIdentifier='" + codeIdentifier + '\'' +
                ", isUsed=" + isUsed +
                ", readOnly=" + readOnly +
                ", suffix='" + suffix + '\'' +
                ", isCodeSystemVersionIncluded=" + isCodeSystemVersionIncluded +
                ", isValidatedWithVsac=" + isValidatedWithVsac +
                '}';
    }
}
