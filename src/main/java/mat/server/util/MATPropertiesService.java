package mat.server.util;

import org.springframework.beans.factory.annotation.Value;

public class MATPropertiesService {

    private static MATPropertiesService instance = new MATPropertiesService();


    @Value("${mat.measure.current.release.version}")
    private String currentReleaseVersion;

    @Value("${mat.measure.current.qdm.version}")
    private String qdmVersion;

    @Value("${mat.measure.current.fhir.version}")
    private String fhirVersion;

    public static MATPropertiesService get() {
        return instance;
    }

    public String getCurrentReleaseVersion() {
        return currentReleaseVersion;
    }

    public void setCurrentReleaseVersion(String currentReleaseVersion) {
        this.currentReleaseVersion = currentReleaseVersion;
    }

    public String getQdmVersion() {
        return qdmVersion;
    }

    public void setQdmVersion(String qdmVersion) {
        this.qdmVersion = qdmVersion;
    }

    public String getFhirVersion() {
        return fhirVersion;
    }

    public void setFhirVersion(String fhirVersion) {
        this.fhirVersion = fhirVersion;
    }
}
