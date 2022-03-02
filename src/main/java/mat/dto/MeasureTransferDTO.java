package mat.dto;

import mat.client.measure.ManageMeasureDetailModel;

import java.io.Serializable;

public class MeasureTransferDTO implements Serializable {
    private String emailId;
    private String harpId;
    private ManageMeasureDetailModel manageMeasureDetailModel;
    private String measureResourceJson;
    private String libraryResourcesJson;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getHarpId() {
        return harpId;
    }

    public void setHarpId(String harpId) {
        this.harpId = harpId;
    }

    public ManageMeasureDetailModel getManageMeasureDetailModel() {
        return manageMeasureDetailModel;
    }

    public void setManageMeasureDetailModel(ManageMeasureDetailModel manageMeasureDetailModel) {
        this.manageMeasureDetailModel = manageMeasureDetailModel;
    }

    public String getMeasureResourceJson() {
        return measureResourceJson;
    }

    public void setMeasureResourceJson(String measureResourceJson) {
        this.measureResourceJson = measureResourceJson;
    }

    public String getLibraryResourcesJson() {
        return libraryResourcesJson;
    }

    public void setLibraryResourcesJson(String libraryResourcesJson) {
        this.libraryResourcesJson = libraryResourcesJson;
    }
}
