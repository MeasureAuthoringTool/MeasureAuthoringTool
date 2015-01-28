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
import java.util.List;
import java.util.Map;
import mat.client.clause.QDSAttributesService;
import mat.dao.DataTypeDAO;
import mat.dao.clause.QDSAttributesDAO;
import mat.model.DataType;
import mat.model.clause.QDSAttributes;
import mat.server.SpringRemoteServiceServlet;
import mat.server.util.ResourceLoader;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.springframework.beans.factory.annotation.Autowired;

// TODO: Auto-generated Javadoc
/**
 * The Class QDSAttributesServiceImpl.
 */
@SuppressWarnings("serial")
public class QDSAttributesServiceImpl extends SpringRemoteServiceServlet
implements QDSAttributesService {
	
	/** The q ds attributes dao. */
	@Autowired
	private QDSAttributesDAO qDSAttributesDAO;
	
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
		// Collections.sort(attrs, attributeComparator);
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
	
	/** The attribute comparator. */
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
		return (QDSAttributesDAO) context.getBean("qDSAttributesDAO");
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
		DataTypeDAO dataTypeDAO = (DataTypeDAO) context.getBean("dataTypeDAO");
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
		
		DataTypeDAO dataTypeDAO = (DataTypeDAO) context.getBean("dataTypeDAO");
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
	 * @param dataType
	 *            the data type
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
			result = convertXmlToString();
			XMLSerializer xmlSerializer = new XMLSerializer();
			xmlSerializer.setForceTopLevelObject(true);
			xmlSerializer.setTypeHintsEnabled(false);
			JSON json = xmlSerializer.read(result);
			JSONObject jsonObject = JSONObject.fromObject(json.toString());
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
	@SuppressWarnings("resource")
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
	
}
