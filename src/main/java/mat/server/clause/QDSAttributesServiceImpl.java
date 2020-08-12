package mat.server.clause;

import mat.client.clause.QDSAttributesService;
import mat.dao.DataTypeDAO;
import mat.dao.clause.ModesAttributesDAO;
import mat.dao.clause.ModesDAO;
import mat.dao.clause.QDSAttributesDAO;
import mat.model.DataType;
import mat.model.clause.QDSAttributes;
import mat.server.MappingSpreadsheetService;
import mat.server.SpringRemoteServiceServlet;
import mat.server.logging.LogFactory;
import mat.server.util.ResourceLoader;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The Class QDSAttributesServiceImpl.
 */
@SuppressWarnings("serial")
public class QDSAttributesServiceImpl extends SpringRemoteServiceServlet
        implements QDSAttributesService {

    private static final Log logger = LogFactory.getLog(QDSAttributesServiceImpl.class);
    private static final char DOT_CHAR = '.';

    @Override
    public List<QDSAttributes> getAllDataTypeAttributes(String qdmName) {
        List<QDSAttributes> attrs = getDAO().findByDataType(qdmName, context);
        List<QDSAttributes> attrs1 = getAllDataFlowAttributeName();
        Collections.sort(attrs, attributeComparator);
        Collections.sort(attrs1, attributeComparator);
        attrs.addAll(attrs1);
        return attrs;
    }

    @Override
    public List<QDSAttributes> getAllAttributesByDataType(String dataTypeName) {
        List<QDSAttributes> attrs = getDAO().findByDataTypeName(dataTypeName,
                context);
        List<QDSAttributes> attrs1 = getAllDataFlowAttributeName();
        Collections.sort(attrs, attributeComparator);
        attrs.addAll(attrs1);
        return attrs;
    }

    @Override
    public List<String> getAllAttributesByDataTypeForFhir(String dataTypeName) {
        return getMappingService().resourceDefinitions().stream().
                filter(r -> StringUtils.isNotBlank(r.getElementId()) &&
                        StringUtils.contains(r.getElementId(), DOT_CHAR)).
                filter(r -> StringUtils.equalsIgnoreCase(r.getElementId().substring(0, r.getElementId().lastIndexOf(DOT_CHAR)), dataTypeName)).
                map(r -> r.getElementId().substring(r.getElementId().lastIndexOf(DOT_CHAR) + 1)).
                map(a -> StringUtils.trimToNull(a)).
                distinct().
                sorted(String.CASE_INSENSITIVE_ORDER).
                collect(Collectors.toList());
    }

    /**
     * The attribute comparator.
     */
    private Comparator<QDSAttributes> attributeComparator = new Comparator<QDSAttributes>() {
        @Override
        public int compare(QDSAttributes arg0, QDSAttributes arg1) {
            return arg0.getName().toLowerCase()
                    .compareTo(arg1.getName().toLowerCase());
        }
    };

    @Override
    public List<QDSAttributes> getAllDataFlowAttributeName() {
        return getDAO().getAllDataFlowAttributeName();
    }

    /**
     * Gets the dao.
     *
     * @return the dao
     */
    public QDSAttributesDAO getDAO() {
        return context.getBean(QDSAttributesDAO.class);
    }

    public MappingSpreadsheetService getMappingService() {
        return context.getBean(MappingSpreadsheetService.class);
    }

    /**
     * Gets the modes dao.
     *
     * @return the dao
     */
    public ModesDAO getModesDAO() {
        return context.getBean(ModesDAO.class);
    }

    /**
     * Gets the mode attribute dao.
     *
     * @return the dao
     */
    public ModesAttributesDAO getModeAttrDAO() {
        return context.getBean(ModesAttributesDAO.class);
    }

    @Override
    public boolean checkIfQDMDataTypeIsPresent(String dataTypeName) {
        boolean checkIfDataTypeIsPresent = false;
        DataTypeDAO dataTypeDAO = context.getBean(DataTypeDAO.class);
        DataType dataType = dataTypeDAO.findByDataTypeName(dataTypeName);
        if (dataType != null) {
            checkIfDataTypeIsPresent = true;
        }
        return checkIfDataTypeIsPresent;
    }

    @Override
    public Map<String, List<String>> getDatatypeList(List<String> dataTypeList) {

        DataTypeDAO dataTypeDAO = context.getBean(DataTypeDAO.class);
        Map<String, List<String>> dataTypeListMap = new HashMap<String, List<String>>();

        for (String dataType : dataTypeList) {
            DataType qdmDataType = dataTypeDAO.findByDataTypeName(dataType);
            if (qdmDataType != null) {
                List<String> qdsAttributeList = getAllQDMAttributesbyDataType(dataType);
                dataTypeListMap.put(dataType, qdsAttributeList);
            }
        }

        return dataTypeListMap;
    }

    /**
     * Gets the all qdm attributesby data type.
     *
     * @param dataType the data type
     * @return the all qdm attributesby data type
     */
    private List<String> getAllQDMAttributesbyDataType(String dataType) {
        List<QDSAttributes> qdsAttributeList = getAllAttributesByDataType(dataType);
        List<String> qdsAttributes = new ArrayList<>();
        for (QDSAttributes qdsAttribtue : qdsAttributeList) {
            qdsAttributes.add(qdsAttribtue.getName());
        }
        return qdsAttributes;
    }

    @Override
    public String getJSONObjectFromXML() {
        String result = null;
        try {
            JSONObject jsonObject = XML.toJSONObject(convertXmlToString());
            result = jsonObject.toString(4);

        } catch (Exception e) {
            logger.error("Error in getJSONObjectFromXML: " + e.getMessage(), e);
        }

        return result;
    }

    @Override
    public String getModeDetailsJSONObjectFromXML() {
        String result = null;
        try {
            JSONObject jsonObject = XML.toJSONObject(convertModeDetailsXmlToString());
            result = jsonObject.toString(4);

        } catch (Exception e) {
            logger.error("Error in getModeDetailsJSONObjectFromXML: " + e.getMessage(), e);
        }

        return result;
    }

    /**
     * Convert xml to string.
     *
     * @return the string
     */
    private String convertXmlToString() {
        String fileName = "SimplifiedAttributePatterns.xml";
        URL templateFileUrl = new ResourceLoader().getResourceAsURL(fileName);
        File xmlFile = null;
        FileReader fr;
        String line = "";
        StringBuilder sb = new StringBuilder();
        try {
            xmlFile = new File(templateFileUrl.toURI());
            fr = new FileReader(xmlFile);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }
        } catch (URISyntaxException | IOException e) {
            logger.error("Error in convertXmlToString: " + e.getMessage(), e);
        }
        return sb.toString();
    }

    /**
     * Convert xml to string.
     *
     * @return the string
     */
    private String convertModeDetailsXmlToString() {
        String fileName = "ModeDetails.xml";
        URL templateFileUrl = new ResourceLoader().getResourceAsURL(fileName);
        File xmlFile = null;
        FileReader fr;
        String line = "";
        StringBuilder sb = new StringBuilder();
        try {
            xmlFile = new File(templateFileUrl.toURI());
            fr = new FileReader(xmlFile);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }
        } catch (URISyntaxException | IOException e) {
            logger.error("Error in convertModeDetailsXmlToString: " + e.getMessage(), e);
        }
        return sb.toString();
    }

    @Override
    public List<String> getAllAttributes() {
        return getDAO().getAllAttributes();
    }

}
