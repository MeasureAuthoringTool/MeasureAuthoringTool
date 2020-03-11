package mat.client.clause.clauseworkspace.model;

import com.google.gwt.user.client.rpc.IsSerializable;


public class MeasureXmlModel implements IsSerializable {

    private String meausreExportId;

    private String measureId;

    private String xml;

    private String toReplaceNode;

    private String parentNode;

    private String measureModel;

    public String getMeausreExportId() {
        return meausreExportId;
    }

    public void setMeausreExportId(String meausreExportId) {
        this.meausreExportId = meausreExportId;
    }

    public String getMeasureId() {
        return measureId;
    }

    public void setMeasureId(String measureId) {
        this.measureId = measureId;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getToReplaceNode() {
        return toReplaceNode;
    }

    public void setToReplaceNode(String toReplaceNode) {
        this.toReplaceNode = toReplaceNode;
    }

    public String getParentNode() {
        return parentNode;
    }

    public void setParentNode(String parentNode) {
        this.parentNode = parentNode;
    }

    public String getMeasureModel() {
        return measureModel;
    }

    public void setMeasureModel(String measureModel) {
        this.measureModel = measureModel;
    }

}
