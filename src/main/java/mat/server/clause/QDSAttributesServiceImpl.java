package mat.server.clause;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import mat.server.ConversionMapping;
import mat.server.CqlAttributesRemoteCallService;
import org.json.JSONObject;
import org.json.XML;

import mat.client.clause.QDSAttributesService;
import mat.dao.DataTypeDAO;
import mat.dao.clause.ModesAttributesDAO;
import mat.dao.clause.ModesDAO;
import mat.dao.clause.QDSAttributesDAO;
import mat.model.DataType;
import mat.model.clause.QDSAttributes;
import mat.server.SpringRemoteServiceServlet;
import mat.server.util.ResourceLoader;
import org.springframework.stereotype.Service;

/**
 * The Class QDSAttributesServiceImpl.
 */
@SuppressWarnings("serial")
@Service
public class QDSAttributesServiceImpl extends SpringRemoteServiceServlet
        implements QDSAttributesService {

    /*
     * (non-Javadoc)
     *
     * @see
     * mat.client.clause.QDSAttributesService#getAllDataTypeAttributes(java.
     * lang.String)
     */
    @Override
    public List<QDSAttributes> getAllDataTypeAttributes(String qdmName) {
        List<QDSAttributes> attrs = getDAO().findByDataType(qdmName, context);
        List<QDSAttributes> attrs1 = getAllDataFlowAttributeName();
        Collections.sort(attrs, attributeComparator);
        Collections.sort(attrs1, attributeComparator);
        attrs.addAll(attrs1);

        return attrs;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * mat.client.clause.QDSAttributesService#getAllAttributesByDataType(java
     * .lang.String)
     */
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
        HashSet<String> fhirAttributeSet = new HashSet();

        ConversionMapping[] conversionMappings = getCqlAttributesRemoteCallService().getFhirAttributeAndDataTypes();
        for(ConversionMapping conversionMapping : conversionMappings) {
            if(conversionMapping.getFhirResource().equalsIgnoreCase(dataTypeName)) {
                fhirAttributeSet.add(conversionMapping.getFhirElement());
            }
        }
        List<String> filterAttrByDataTypeList = new ArrayList<>(fhirAttributeSet);
        return filterAttrByDataTypeList;
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

    /*
     * (non-Javadoc)
     *
     * @see mat.client.clause.QDSAttributesService#getAllDataFlowAttributeName()
     */
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

    public CqlAttributesRemoteCallService getCqlAttributesRemoteCallService() {
        return context.getBean(CqlAttributesRemoteCallService.class);
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

    /*
     * (non-Javadoc)
     *
     * @see
     * mat.client.clause.QDSAttributesService#checkIfQDMDataTypeIsPresent(java
     * .lang.String)
     */
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

    /*
     * (non-Javadoc)
     *
     * @see
     * mat.client.clause.QDSAttributesService#getDatatypeList(java.util.List)
     */
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
        List<String> qdsAttributes = new ArrayList<String>();
        for (QDSAttributes qdsAttribtue : qdsAttributeList) {
            qdsAttributes.add(qdsAttribtue.getName());
        }
        return qdsAttributes;
    }


    /* (non-Javadoc)
     * @see mat.client.clause.QDSAttributesService#getJSONObjectFromXML()
     */
    @Override
    public String getJSONObjectFromXML() {
        String result = null;
        try {
            JSONObject jsonObject = XML.toJSONObject(convertXmlToString());
            result = jsonObject.toString(4);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /* (non-Javadoc)
     * @see mat.client.clause.QDSAttributesService#getModeDetailsJSONObjectFromXML()
     */
    @Override
    public String getModeDetailsJSONObjectFromXML() {
        String result = null;
        try {
            JSONObject jsonObject = XML.toJSONObject(convertModeDetailsXmlToString());
            result = jsonObject.toString(4);

        } catch (Exception e) {
            e.printStackTrace();
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
            try {
                xmlFile = new File(templateFileUrl.toURI());
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
            fr = new FileReader(xmlFile);
            BufferedReader br = new BufferedReader(fr);

            try {
                while ((line = br.readLine()) != null) {
                    sb.append(line.trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
            try {
                xmlFile = new File(templateFileUrl.toURI());
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
            fr = new FileReader(xmlFile);
            BufferedReader br = new BufferedReader(fr);

            try {
                while ((line = br.readLine()) != null) {
                    sb.append(line.trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /* (non-Javadoc)
     * @see mat.client.clause.QDSAttributesService#getAllAttributes()
     */
    @Override
    public List<String> getAllAttributes() {
        return getDAO().getAllAttributes();
    }

}
