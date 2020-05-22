package mat.server.service;

import java.util.ArrayList;
import java.util.List;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.service.ValidateMeasureResult;
import mat.model.DataType;
import mat.model.MatValueSet;
import mat.model.QualityDataSet;
import mat.model.clause.ComponentMeasure;
import mat.model.clause.Measure;
import mat.model.clause.MeasureExport;
import mat.model.clause.MeasureSet;
import mat.model.clause.MeasureShareDTO;
import mat.shared.MeasureSearchModel;

public interface MeasurePackageService {
    /**
     * Count users for measure share.
     *
     * @return {@link Integer}. *
     */
    int countUsersForMeasureShare();

    /**
     * Delete existing packages.
     *
     * @param measureId - {@link String}. *
     */
    void deleteExistingPackages(String measureId);

    /**
     * Find data type for supplimental code list.
     *
     * @param dataTypeName - {@link String}.
     * @param categoryId   - {@link String}.
     * @return {@link DataType}. *
     */
    DataType findDataTypeForSupplimentalCodeList(String dataTypeName,
                                                 String categoryId);

    /**
     * Find measure set.
     *
     * @param id - {@link String}.
     * @return {@link MeasureSet}. *
     */
    MeasureSet findMeasureSet(String id);

    /**
     * Find out maximum version number.
     *
     * @param measureSetId - {@link String}.
     * @return {@link String}. *
     */
    String findOutMaximumVersionNumber(String measureSetId);

    /**
     * Find out version number.
     *
     * @param measureId    - {@link String}.
     * @param measureSetId - {@link String}.
     * @return {@link String}. *
     */
    String findOutVersionNumber(String measureId, String measureSetId);

    /**
     * Gets the by id.
     *
     * @param id - {@link String}.
     * @return {@link Measure}. *
     */
    Measure getById(String id);

    /**
     * Gets the max e measure id.
     *
     * @return {@link Integer}. *
     */
    int getMaxEMeasureId();

    /**
     * Gets the measure xml for measure.
     *
     * @param measureId - {@link String}.
     * @return {@link MeasureXmlModel}. *
     */
    MeasureXmlModel getMeasureXmlForMeasure(String measureId);


    /**
     * Gets the unique oid.
     *
     * @return {@link String}. *
     */
    String getUniqueOid();

    /**
     * Gets the users for share.
     *
     * @param userName   - {@link String}.
     * @param measureId  - {@link String}.
     * @param startIndex - {@link Integer}.
     * @param pageSize   - {@link Integer}.
     * @return {@link List} {@link MeasureShareDTO}.
     */
    List<MeasureShareDTO> getUsersForShare(String userName, String measureId,
                                           int startIndex, int pageSize);

    /**
     * Checks if is measure locked.
     *
     * @param id - {@link String}
     * @return {@link Boolean}. *
     */
    boolean isMeasureLocked(String id);

    /**
     * Save.
     *
     * @param measurePackage the measure package
     */
    void save(Measure measurePackage);

    /**
     * Save.
     *
     * @param measureSet - {@link MeasureSet}. *
     */
    void save(MeasureSet measureSet);

    /**
     * Save and return max e measure id.
     *
     * @param measure - {@link Measure}
     * @return {@link Integer}. *
     */
    int saveAndReturnMaxEMeasureId(Measure measure);

    /**
     * Save measure xml.
     *
     * @param measureXmlModel - {@link MeasureXmlModel}. *
     */
    void saveMeasureXml(MeasureXmlModel measureXmlModel);

    /**
     * Save supplimental qdm.
     *
     * @param qds - {@link QualityDataSet}. *
     */
    void saveSupplimentalQDM(QualityDataSet qds);

    /**
     * Search for admin with filter.
     *
     * @param searchText - {@link String}.
     * @param startIndex - {@link Integer}.
     * @param numResults - {@link Integer}.
     * @param filter     - {@link Integer}.
     * @return {@link List} of {@link MeasureShareDTO}. *
     */
    List<MeasureShareDTO> searchForAdminWithFilter(String searchText, int startIndex,
                                                   int numResults, int filter);


    List<MeasureShareDTO> searchWithFilter(MeasureSearchModel advancedSearchModel);

    /**
     * Transfer measure owner ship to user.
     *
     * @param list    - {@link List} of {@link String}.
     * @param toEmail - {@link String}.
     *                <p>
     *                *
     */
    void transferMeasureOwnerShipToUser(List<String> list, String toEmail);

    /**
     * Update locked out date.
     *
     * @param m - {@link Measure}. *
     */
    void updateLockedOutDate(Measure m);

    /**
     * Update private column in measure.
     *
     * @param measureId - {@link String}.
     * @param isPrivate - {@link Boolean}. *
     */
    void updatePrivateColumnInMeasure(String measureId, boolean isPrivate);

    /**
     * Update users share.
     *
     * @param model - {@link ManageMeasureShareModel}. *
     */
    void updateUsersShare(ManageMeasureShareModel model);

    /**
     * Validate measure for export.
     *
     * @param key             - {@link String}.
     * @param matValueSetList - {@link ArrayList} of {@link MatValueSet}.
     * @return {@link ValidateMeasureResult}.
     * @throws Exception - {@link Exception}.
     */
    ValidateMeasureResult createExports(String key, List<MatValueSet> matValueSetList, boolean shouldCreateArtifacts) throws Exception;

    /**
     * Gets the human readable for node.
     *
     * @param measureId        the measure id
     * @param populationSubXML the population sub xml
     * @return the human readable for node
     */
    String getHumanReadableForNode(String measureId, String populationSubXML);

    /**
     * Gets the component measures info.
     *
     * @param measureId the measure ids
     * @return the component measures info
     */
    List<Measure> getComponentMeasuresInfo(String measureId);

    /**
     * Gets the measure.
     *
     * @param measureId the measure id
     * @return the measure
     */
    boolean getMeasure(String measureId);

    List<MeasureShareDTO> searchComponentMeasuresWithFilter(MeasureSearchModel measureSearchModel);

    void saveComponentMeasures(List<ComponentMeasure> componentMeasuresList);

    void updateComponentMeasures(String compositeMeasureId, List<ComponentMeasure> componentMeasuresList);

    void createPackageArtifacts(Measure measure, MeasureExport export);

    ValidateMeasureResult validateExportsForCompositeMeasures(String key) throws Exception;
}
