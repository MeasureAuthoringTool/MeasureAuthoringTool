package mat.server.service;

import mat.client.measurepackage.MeasurePackageDetail;
import mat.client.measurepackage.MeasurePackageOverview;
import mat.client.measurepackage.service.MeasurePackageSaveResult;
import mat.shared.packager.error.SaveRiskAdjustmentVariableException;
import mat.shared.packager.error.SaveSupplementalDataElementException;

import javax.xml.xpath.XPathExpressionException;

public interface PackagerService {

    /**
     * Builds the overview for measure.
     *
     * @param measureId the measure id
     * @return the measure package overview
     */
    public MeasurePackageOverview buildOverviewForMeasure(String measureId);

    /**
     * Save.
     *
     * @param detail the detail
     * @return the measure package save result
     */
    public MeasurePackageSaveResult save(MeasurePackageDetail detail);

    /**
     * Delete.
     *
     * @param detail the detail
     */
    public void delete(MeasurePackageDetail detail);

    /**
     * Save qdm data.
     *
     * @param detail the detail
     * @throws SaveSupplementalDataElementException
     * @throws XPathExpressionException
     */
    public void saveQDMData(MeasurePackageDetail detail) throws SaveSupplementalDataElementException;

    /**
     * Save risk variables.
     *
     * @param detail the detail
     * @throws XPathExpressionException
     * @throws SaveRiskAdjustmentVariableException
     */
    void saveRiskAdjVariables(MeasurePackageDetail detail) throws SaveRiskAdjustmentVariableException;
}
