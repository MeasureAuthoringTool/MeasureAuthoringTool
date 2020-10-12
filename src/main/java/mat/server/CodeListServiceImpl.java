package mat.server;


import mat.client.codelist.HasListBox;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.dto.OperatorDTO;
import mat.dto.UnitDTO;
import mat.dto.VSACCodeSystemDTO;
import mat.model.QualityDataSetDTO;
import mat.model.MatValueSetTransferObject;
import mat.server.logging.LogFactory;
import mat.server.service.CodeListService;
import mat.shared.ConstantMessages;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class CodeListServiceImpl extends SpringRemoteServiceServlet
        implements mat.client.codelist.service.CodeListService {

    private static final Log logger = LogFactory.getLog(CodeListServiceImpl.class);
    @Autowired
    private CodeListService codeListService;

    @Override
    public List<? extends HasListBox> getAllDataTypes() {
        return getCodeListService().getAllDataTypes();
    }

    @Override
    public List<OperatorDTO> getAllOperators() {
        return getCodeListService().getAllOperators();
    }

    public CodeListService getCodeListService() {
        return codeListService;
    }

    @Override
    public mat.client.codelist.service.CodeListService.ListBoxData getListBoxData() {
        logger.info("getListBoxData");
        return getCodeListService().getListBoxData();
    }

    @Override
    public List<? extends HasListBox> getQDSDataTypeForCategory(String category) {
        return getCodeListService().getQDSDataTypeForCategory(category);
    }

    @Override
    public List<QualityDataSetDTO> getQDSElements(String measureId,
                                                  String version) {
        List<QualityDataSetDTO> qdsElements = getCodeListService().getQDSElements(measureId, version);
        List<QualityDataSetDTO> filteredQDSElements = new ArrayList<>();
        for (QualityDataSetDTO dataSet : qdsElements) {
            if ((dataSet.getOid() != null) && !dataSet.getOid().equals(ConstantMessages.GENDER_OID)
                    && !dataSet.getOid().equals(ConstantMessages.RACE_OID) && !dataSet.getOid().equals(ConstantMessages.ETHNICITY_OID)
                    && !dataSet.getOid().equals(ConstantMessages.PAYER_OID)) {
                filteredQDSElements.add(dataSet);
            }

        }
        Collections.sort(filteredQDSElements, (o1, o2) -> o1.getCodeListName().compareToIgnoreCase(o2.getCodeListName()));
        return filteredQDSElements;
    }

    @Override
    public SaveUpdateCodeListResult saveQDStoMeasure(MatValueSetTransferObject ValueSetTransferObject) {
        return getCodeListService().saveQDStoMeasure(ValueSetTransferObject);
    }

    @Override
    public SaveUpdateCodeListResult saveUserDefinedQDStoMeasure(MatValueSetTransferObject ValueSetTransferObject) {
        return getCodeListService().saveUserDefinedQDStoMeasure(ValueSetTransferObject);
    }

    @Override
    public SaveUpdateCodeListResult updateCodeListToMeasure(MatValueSetTransferObject ValueSetTransferObject) {
        ValueSetTransferObject.scrubForMarkUp();
        return getCodeListService().updateQDStoMeasure(ValueSetTransferObject);
    }

    @Override
    public List<UnitDTO> getAllCqlUnits() {
        logger.info("getAllCqlUnits");
        return getCodeListService().getAllUnits();
    }

    @Override
    public Map<String, VSACCodeSystemDTO> getOidToVsacCodeSystemMap() {
        logger.info("getOidToVsacCodeSystemMap");
        return getCodeListService().getOidToVsacCodeSystemMap();
    }
}