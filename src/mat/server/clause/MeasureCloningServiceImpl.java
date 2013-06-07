package mat.server.clause;


import java.io.StringReader;
import java.io.StringWriter;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.service.MeasureCloningService;
import mat.client.shared.MatException;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureSetDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.dao.UserDAO;
import mat.model.User;
import mat.model.clause.Measure;
import mat.model.clause.MeasureSet;
import mat.model.clause.MeasureXML;
import mat.server.LoggedInUserUtil;
import mat.server.SpringRemoteServiceServlet;
import mat.server.util.MeasureUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@SuppressWarnings("serial")
public class MeasureCloningServiceImpl extends SpringRemoteServiceServlet implements MeasureCloningService {

	@Autowired
	private MeasureDAO measureDAO;
	@Autowired
	private MeasureXMLDAO measureXmlDAO;
	@Autowired
	private MeasureSetDAO measureSetDAO;
	@Autowired
	private UserDAO userDAO;
	
	private static final Log logger = LogFactory.getLog(MeasureCloningServiceImpl.class);
	private static final String MEASURE_DETAILS = "measureDetails";
	private static final String MEASURE = "measure";
	private static final String MEASURE_GROUPING = "measureGrouping";
	private static final String UU_ID = "uuid";
	private static final String TITLE = "title";
	private static final String SHORT_TITLE = "shortTitle";
	private static final String GUID = "guid";
	private static final String VERSION = "version";
	private static final String MEASURE_STATUS = "status";
	private static final String MEASURE_SCORING = "scoring";
	private static final String SUPPLEMENTAL_DATA_ELEMENTS = "supplementalDataElements";
	private static final String ONC_ADMIN_SEX = "ONC Administrative Sex";
	private static final String RACE = "Race";
	private static final String ETHNICITY = "Ethnicity";
	private static final String PAYER = "Payer";
	private static final String VERSION_ZERO = "0.0";
	private static final boolean TRUE = true;
		
	private Document clonedDoc;
	
	
	/*@Override
	public ManageMeasureSearchModel.Result clone(ManageMeasureDetailModel currentDetails, String loggedinUserId,boolean creatingDraft) throws MatException{
		measureDAO = (MeasureDAO)context.getBean("measureDAO");
		try {
			MeasureShareDTO dto = measureDAO.clone(currentDetails, loggedinUserId,creatingDraft, context);
			ManageMeasureSearchModel.Result result = new ManageMeasureSearchModel.Result();
			result.setId(dto.getMeasureId());
			result.setName(dto.getMeasureName());
			result.setShortName(dto.getShortName());
			result.setScoringType(dto.getScoringType());
			String formattedVersion = MeasureUtility.getVersionText(dto.getVersion(), dto.isDraft());
			result.setVersion(formattedVersion);
			result.setEditable(true);
			result.setClonable(true);
			return result;
		} catch (Exception e) {
			log(e.getMessage(), e);
			throw new MatException(e.getMessage());
		}
	}
*/		
	@Override
	public ManageMeasureSearchModel.Result clone(ManageMeasureDetailModel currentDetails, String loggedinUserId,boolean creatingDraft) throws MatException{
		logger.info("In MeasureCloningServiceImpl.clone() method..");
		measureDAO = (MeasureDAO)context.getBean("measureDAO");
		measureXmlDAO = (MeasureXMLDAO)context.getBean("measureXMLDAO");
		measureSetDAO = (MeasureSetDAO)context.getBean("measureSetDAO");
		userDAO = (UserDAO)context.getBean("userDAO");
		
		try{	
			ManageMeasureSearchModel.Result result = new ManageMeasureSearchModel.Result();
			Measure measure = measureDAO.find(currentDetails.getId());
			MeasureXML xml = measureXmlDAO.findForMeasure(currentDetails.getId());
			Measure clonedMeasure = new Measure();
			MeasureSet measureSet = new MeasureSet();
			measureSet.setId(UUID.randomUUID().toString());
			measureSetDAO.save(measureSet);
			clonedMeasure.setMeasureSet(measureSet);
			clonedMeasure.setaBBRName(currentDetails.getShortName());
			clonedMeasure.setDescription(currentDetails.getName());
			clonedMeasure.setVersion(VERSION_ZERO);
			clonedMeasure.setMeasureStatus("In Progress");
			if(currentDetails.getMeasScoring()!= null){
				clonedMeasure.setMeasureScoring(currentDetails.getMeasScoring());
			}else{
				clonedMeasure.setMeasureScoring(measure.getMeasureScoring());
			}	
			clonedMeasure.setDraft(TRUE);
			if(LoggedInUserUtil.getLoggedInUser() != null){
				User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
				clonedMeasure.setOwner(currentUser);
			}
			measureDAO.saveMeasure(clonedMeasure);
			String originalXml = xml.getMeasureXMLAsString();
			InputSource oldXmlstream = new InputSource(new StringReader(originalXml));
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document originalDoc = docBuilder.parse(oldXmlstream);
			clonedDoc = originalDoc;
			
			// Clear the measureDetails tag
			clearChildNodes(MEASURE_DETAILS);
			createNewMeasureDetails(clonedMeasure);
			// Create the measureGrouping tag
			clearChildNodes(MEASURE_GROUPING);
			clearOtherSupplementalDataElements(); 
			String clonedXMLString = convertDocumenttoString(clonedDoc);
			MeasureXML clonedXml = new MeasureXML();
			clonedXml.setMeasureXMLAsByteArray(clonedXMLString);
			clonedXml.setMeasure_id(clonedMeasure.getId());
			measureXmlDAO.save(clonedXml);
			result.setId(clonedMeasure.getId());
			result.setName(currentDetails.getName());
			result.setShortName(currentDetails.getShortName());
			result.setScoringType(clonedMeasure.getMeasureScoring());
			String formattedVersion = MeasureUtility.getVersionText(measure.getVersion(), measure.isDraft());
			result.setVersion(formattedVersion);
			result.setEditable(TRUE);
			result.setClonable(TRUE);
			return result;
		} catch (Exception e) {
 			log(e.getMessage(), e);
			throw new MatException(e.getMessage());
		}
	}

	
	public void clearChildNodes(String nodeName){
		NodeList nodeList  = clonedDoc.getElementsByTagName(nodeName);
		Node parentNode = nodeList.item(0);
		while(parentNode.hasChildNodes()){
			parentNode.removeChild(parentNode.getFirstChild());
		}
	}
	
	private void clearOtherSupplementalDataElements(){
			NodeList nodeList = clonedDoc.getElementsByTagName(SUPPLEMENTAL_DATA_ELEMENTS);
			Node parentNode =  nodeList.item(0);
			NodeList childNodesList = parentNode.getChildNodes();
			for(int i=0; i < childNodesList.getLength();i++){
				String attrName =childNodesList.item(i).getAttributes().item(1).getNodeValue();
				if(!attrName.equals(ONC_ADMIN_SEX)&& !attrName.equals(RACE)&& !attrName.equals(ETHNICITY) && !attrName.equals(PAYER)){
					parentNode.removeChild(childNodesList.item(i));
				
				}
					
			}		
	}
	
	private void createNewMeasureDetails(Measure measure){
				NodeList nodeList  = clonedDoc.getElementsByTagName(MEASURE_DETAILS);
				Node parentNode = nodeList.item(0);
				Node uuidNode = clonedDoc.createElement(UU_ID);
				uuidNode.setTextContent(measure.getId());
				Node titleNode = clonedDoc.createElement(TITLE);
				titleNode.setTextContent(measure.getDescription());
				Node shortTitleNode = clonedDoc.createElement(SHORT_TITLE);
				shortTitleNode.setTextContent(measure.getaBBRName());
				Node guidNode = clonedDoc.createElement(GUID);
				guidNode.setTextContent(measure.getMeasureSet().getId());
				Node versionNode = clonedDoc.createElement(VERSION);
				versionNode.setTextContent(measure.getVersion());
				Node statusNode = clonedDoc.createElement(MEASURE_STATUS);
				statusNode.setTextContent(measure.getMeasureStatus());
				Node measureScoringNode = clonedDoc.createElement(MEASURE_SCORING);
				measureScoringNode.setTextContent(measure.getMeasureScoring());
				parentNode.appendChild(uuidNode);
				parentNode.appendChild(titleNode);
				parentNode.appendChild(shortTitleNode);
				parentNode.appendChild(guidNode);
				parentNode.appendChild(versionNode);
				parentNode.appendChild(statusNode);
				parentNode.appendChild(measureScoringNode);
				
	}
		
	private String convertDocumenttoString(Document doc) throws Exception{
		try{	
				DOMSource domSource = new DOMSource(doc);
        		StringWriter writer = new StringWriter();
        		StreamResult result = new StreamResult(writer);
        		TransformerFactory tf = TransformerFactory.newInstance();
        		Transformer transformer = tf.newTransformer();
        		transformer.transform(domSource, result);
        		return writer.toString();
        	}catch(Exception e){
        		log(e.getMessage(), e);
        		throw new Exception(e.getMessage());
    		}
           
        	
	}
}
