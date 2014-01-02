package mat.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import mat.DTO.MeasureNoteDTO;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.MeasureNotesModel;
import mat.client.measure.NqfModel;
import mat.client.measure.PeriodModel;
import mat.client.measure.TransferMeasureOwnerShipModel;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.shared.MatException;
import mat.dao.MeasureNotesDAO;
import mat.dao.RecentMSRActivityLogDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.dao.clause.QDSAttributesDAO;
import mat.model.Author;
import mat.model.MatValueSet;
import mat.model.MeasureNotes;
import mat.model.MeasureType;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.RecentMSRActivityLog;
import mat.model.SecurityRole;
import mat.model.User;
import mat.model.clause.Measure;
import mat.model.clause.MeasureSet;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.QDSAttributes;
import mat.model.clause.ShareLevel;
import mat.server.service.InvalidValueSetDateException;
import mat.server.service.MeasureLibraryService;
import mat.server.service.MeasureNotesService;
import mat.server.service.MeasurePackageService;
import mat.server.service.UserService;
import mat.server.util.MeasureUtility;
import mat.server.util.ResourceLoader;
import mat.server.util.UuidUtility;
import mat.server.util.XmlProcessor;
import mat.shared.ConstantMessages;
import mat.shared.DateStringValidator;
import mat.shared.DateUtility;
import mat.shared.model.util.MeasureDetailsUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The Class MeasureLibraryServiceImpl.
 */
public class MeasureLibraryServiceImpl implements MeasureLibraryService {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(MeasureLibraryServiceImpl.class);
	
	/** The Constant MEASURE. */
	private static final String MEASURE = "measure";
	
	/** The Constant MEASURE_DETAILS. */
	private static final String MEASURE_DETAILS = "measureDetails";
	
	/**
	 * Comparator.
	 * **/
	private Comparator<QDSAttributes> attributeComparator = new Comparator<QDSAttributes>() {
		@Override
		public int compare(final QDSAttributes arg0, final QDSAttributes arg1) {
			return arg0.getName().toLowerCase().compareTo(arg1.getName().toLowerCase());
		}
	};
	
	/** The context. */
	@Autowired
	private ApplicationContext context;
	
	/** The measure package service. */
	@Autowired
	private MeasurePackageService measurePackageService;
	
	/** The qds attributes dao. */
	@Autowired
	private QDSAttributesDAO qDSAttributesDAO;
	
	/** The recent msr activity log dao. */
	@Autowired
	private RecentMSRActivityLogDAO recentMSRActivityLogDAO;
	
	/** The user service. */
	@Autowired
	private UserService userService;
	
	/** The x path. */
	javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#appendAndSaveNode(mat.client.clause.clauseworkspace.model.MeasureXmlModel, java.lang.String)
	 */
	@Override
	public final void appendAndSaveNode(final MeasureXmlModel measureXmlModel, final String nodeName) {
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
		if (((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) && ((nodeName != null) && StringUtils.isNotBlank(nodeName))) {
			String result = callAppendNode(xmlModel, measureXmlModel.getXml(), nodeName, measureXmlModel.getParentNode());
			measureXmlModel.setXml(result);
			getService().saveMeasureXml(measureXmlModel);
		}
		
	}
	
	/**
	 * Method to call XMLProcessor appendNode method to append new xml nodes
	 * into existing xml.
	 * 
	 * @param measureXmlModel
	 *            the measure xml model
	 * @param newXml
	 *            the new xml
	 * @param nodeName
	 *            the node name
	 * @param parentNodeName
	 *            the parent node name
	 * @return the string
	 */
	private String callAppendNode(final MeasureXmlModel measureXmlModel, final String newXml, final String nodeName,
			final String parentNodeName) {
		XmlProcessor xmlProcessor = new XmlProcessor(measureXmlModel.getXml());
		String result = null;
		try {
			result = xmlProcessor.appendNode(newXml, nodeName, parentNodeName);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * This method will use XMLProcessor to determine if which of the 3 Timing
	 * Elements are missing from the measure xml 'elementLookUp' tag. Based on
	 * which one is missing it will fetch it from ListObject and add it to
	 * 'elementLookUp'.
	 * 
	 * @param xmlProcessor
	 *            the xml processor
	 */
	@Override
	public void checkForTimingElementsAndAppend(XmlProcessor xmlProcessor) {
		
		List<String> missingTimingElementOIDList = xmlProcessor.checkForTimingElements();
		
		if (missingTimingElementOIDList.isEmpty()) {
			logger.info("All timing elements present in the measure.");
			return;
		}
		logger.info("Found the following timing elements missing:" + missingTimingElementOIDList);
		
		QualityDataModelWrapper wrapper = getMeasureXMLDAO().createTimingElementQDMs(missingTimingElementOIDList);
		
		// Object to XML for elementLookUp
		ByteArrayOutputStream streamQDM = XmlProcessor.convertQualityDataDTOToXML(wrapper);
		
		String filteredString = removePatternFromXMLString(streamQDM.toString().substring(streamQDM.toString().indexOf("<measure>", 0)),
				"<measure>", "");
		filteredString = removePatternFromXMLString(filteredString, "</measure>", "");
		
		try {
			xmlProcessor.appendNode(filteredString, "qdm", "/measure/elementLookUp");
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Method called when Measure Details Clone operation is done or Drafting of
	 * a version measure is done. TODO: Sangeethaa This method will have to
	 * change when we get all the page items captued as XML 1) The
	 * MeasureDAO.clone() method should be re written in here
	 * 
	 * @param creatingDraft
	 *            the creating draft
	 * @param oldMeasureId
	 *            the old measure id
	 * @param clonedMeasureId
	 *            the cloned measure id
	 */
	@Override
	public final void cloneMeasureXml(final boolean creatingDraft, final String oldMeasureId, final String clonedMeasureId) {
		logger.info("In MeasureLibraryServiceImpl.cloneMeasureXml() method. Clonig for Measure: " + oldMeasureId);
		ManageMeasureDetailModel measureDetailModel = null;
		if (creatingDraft) {
			measureDetailModel = getMeasure(oldMeasureId);// get the
			// measureDetailsmodel
			// object for which
			// draft have to be
			// created..
			Measure measure = getService().getById(clonedMeasureId);// get the
			// Cloned Measure Revision Number reset to '000' when cloned.
			measure.setRevisionNumber("000");
			// Cloned
			// version
			// of the
			// Measure.
			createMeasureDetailsModelFromMeasure(measureDetailModel, measure); // apply
			// measure
			// values
			// in
			// the
			// created
			// MeasureDetailsModel.
		} else {
			measureDetailModel = getMeasure(clonedMeasureId);
		}
		MeasureXmlModel measureXmlModel = new MeasureXmlModel();
		measureXmlModel.setMeasureId(measureDetailModel.getId());
		measureXmlModel.setXml(createXml(measureDetailModel).toString());
		measureXmlModel.setToReplaceNode(MEASURE_DETAILS);
		saveMeasureXml(measureXmlModel);
		logger.info("Clone of Measure_xml is Successful");
	}
	
	/**
	 * Adding additonal fields in model from measure table.
	 * 
	 * @param manageMeasureDetailModel
	 *            - {@link ManageMeasureDetailModel}.
	 * @param measure
	 *            - {@link Measure}.
	 */
	private void convertAddlXmlElementsToModel(final ManageMeasureDetailModel manageMeasureDetailModel, final Measure measure) {
		logger.info("In easureLibraryServiceImpl.convertAddlXmlElementsToModel()");
		manageMeasureDetailModel.setId(measure.getId());
		manageMeasureDetailModel.setMeasFromPeriod(manageMeasureDetailModel.getPeriodModel() != null ? manageMeasureDetailModel
				.getPeriodModel().getStartDate() : null);
		manageMeasureDetailModel.setMeasToPeriod(manageMeasureDetailModel.getPeriodModel() != null ? manageMeasureDetailModel
				.getPeriodModel().getStopDate() : null);
		manageMeasureDetailModel.setEndorseByNQF((StringUtils.isNotBlank(
				manageMeasureDetailModel.getEndorsement()) ? true : false));
		manageMeasureDetailModel.setOrgVersionNumber(MeasureUtility.formatVersionText(measure.getRevisionNumber(),
				String.valueOf(measure.getVersionNumber())));
		manageMeasureDetailModel.setVersionNumber(MeasureUtility.getVersionText(manageMeasureDetailModel.getOrgVersionNumber(),
				measure.isDraft()));
		manageMeasureDetailModel.setFinalizedDate(DateUtility.convertDateToString(measure.getFinalizedDate()));
		manageMeasureDetailModel.setDraft(measure.isDraft());
		manageMeasureDetailModel.setValueSetDate(DateUtility.convertDateToStringNoTime(measure.getValueSetDate()));
		manageMeasureDetailModel.setNqfId(manageMeasureDetailModel.getNqfModel() != null ?
				manageMeasureDetailModel.getNqfModel()
				.getExtension() : null);
		manageMeasureDetailModel.seteMeasureId(measure.geteMeasureId());
		manageMeasureDetailModel.setMeasureOwnerId(measure.getOwner().getId());
		logger.info("Exiting easureLibraryServiceImpl.convertAddlXmlElementsToModel() method..");
	}
	
	/**
	 * Method un marshalls MeasureXML into ManageMeasureDetailModel object.
	 * 
	 * @param xmlModel
	 *            -MeasureXmlModel
	 * @param measure
	 *            - Measure
	 * @return ManageMeasureDetailModel
	 */
	private ManageMeasureDetailModel convertXmltoModel(final MeasureXmlModel xmlModel, final Measure measure) {
		logger.info("In MeasureLibraryServiceImpl.convertXmltoModel()");
		ManageMeasureDetailModel details = null;
		String xml = null;
		if ((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) {
			xml = new XmlProcessor(xmlModel.getXml()).getXmlByTagName(MEASURE_DETAILS);
			// logger.info("xml by tag name measureDetails" + xml);
		}
		try {
			if (xml == null) { // TODO: This Check should be replaced when the
				// DataConversion is complete.
				logger.info("xml is null or xml doesn't contain measureDetails tag");
				details = new ManageMeasureDetailModel();
				createMeasureDetailsModelFromMeasure(details, measure);
			} else {
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("MeasureDetailsModelMapping.xml"));
				Unmarshaller unmar = new Unmarshaller(mapping);
				unmar.setClass(ManageMeasureDetailModel.class);
				unmar.setWhitespacePreserve(true);
				// logger.info("unmarshalling xml.. " + xml);
				details = (ManageMeasureDetailModel) unmar.unmarshal(new InputSource(new StringReader(xml)));
				// logger.info("unmarshalling complete.." + details.toString());
				convertAddlXmlElementsToModel(details, measure);
			}
			
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load MeasureDetailsModelMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else {
				logger.info("Other Exception" + e);
			}
		}
		return details;
	}
	
	/**
	 * Convert xmlto quality data dto model.
	 * 
	 * @param xmlModel
	 *            the xml model
	 * @return the quality data model wrapper
	 */
	private QualityDataModelWrapper convertXmltoQualityDataDTOModel(final MeasureXmlModel xmlModel) {
		logger.info("In MeasureLibraryServiceImpl.convertXmltoQualityDataDTOModel()");
		QualityDataModelWrapper details = null;
		String xml = null;
		if ((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) {
			xml = new XmlProcessor(xmlModel.getXml()).getXmlByTagName("measure");
			// logger.info("xml by tag name elementlookup" + xml);
		}
		try {
			if (xml == null) {// TODO: This Check should be replaced when the
				// DataConversion is complete.
				logger.info("xml is null or xml doesn't contain elementlookup tag");
				
			} else {
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("QualityDataModelMapping.xml"));
				Unmarshaller unmar = new Unmarshaller(mapping);
				unmar.setClass(QualityDataModelWrapper.class);
				unmar.setWhitespacePreserve(true);
				// logger.info("unmarshalling xml..elementlookup " + xml);
				details = (QualityDataModelWrapper) unmar.unmarshal(new InputSource(new StringReader(xml)));
				logger.info("unmarshalling complete..elementlookup" + details.getQualityDataDTO().get(0).getCodeListName());
			}
			
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load QualityDataModelMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else {
				logger.info("Other Exception" + e);
			}
		}
		return details;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#createAndSaveElementLookUp(java.util.ArrayList, java.lang.String)
	 */
	@Override
	public final void createAndSaveElementLookUp(final ArrayList<QualityDataSetDTO> list, final String measureID) {
		QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
		wrapper.setQualityDataDTO(list);
		ByteArrayOutputStream stream = createQDMXML(wrapper);
		int startIndex = stream.toString().indexOf("<elementLookUp>", 0);
		int lastIndex = stream.toString().indexOf("</measure>", startIndex);
		String xmlString = stream.toString().substring(startIndex, lastIndex);
		String nodeName = "elementLookUp";
		
		MeasureXmlModel exportModal = new MeasureXmlModel();
		exportModal.setMeasureId(measureID);
		exportModal.setParentNode("/measure");
		exportModal.setToReplaceNode("elementLookUp");
		System.out.println("XML " + xmlString);
		
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureID);
		if (((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) && ((nodeName != null) && StringUtils.isNotBlank(nodeName))) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			String result = xmlProcessor.replaceNode(xmlString, nodeName, "measure");
			System.out.println("result" + result);
			exportModal.setXml(result);
			getService().saveMeasureXml(exportModal);
		}
		
	}
	
	/**
	 * This should be removed when we do a batch save in Measure_XML on
	 * production.
	 * 
	 * @param model
	 *            the model
	 * @param measure
	 *            the measure
	 */
	private void createMeasureDetailsModelFromMeasure(final ManageMeasureDetailModel model, final Measure measure) {
		logger.info("In MeasureLibraryServiceImpl.createMeasureDetailsModelFromMeasure()");
		model.setId(measure.getId());
		model.setName(measure.getDescription());
		model.setShortName(measure.getaBBRName());
		model.setMeasScoring(measure.getMeasureScoring());
		model.setOrgVersionNumber(MeasureUtility.formatVersionText(measure.getRevisionNumber(),
				String.valueOf(measure.getVersionNumber())));
		model.setVersionNumber(MeasureUtility.getVersionText(model.getOrgVersionNumber(), measure.isDraft()));
		model.setFinalizedDate(DateUtility.convertDateToString(measure.getFinalizedDate()));
		model.setDraft(measure.isDraft());
		model.setMeasureSetId(measure.getMeasureSet().getId());
		model.setValueSetDate(DateUtility.convertDateToStringNoTime(measure.getValueSetDate()));
		model.seteMeasureId(measure.geteMeasureId());
		model.setMeasureStatus(measure.getMeasureStatus());
		model.setMeasureOwnerId(measure.getOwner().getId());
		logger.info("Exiting MeasureLibraryServiceImpl.createMeasureDetailsModelFromMeasure()");
	}
	
	/**
	 * Creates the measure details xml.
	 * 
	 * @param measureDetailModel
	 *            the measure detail model
	 * @param measure
	 *            the measure
	 * @return the string
	 */
	public final String createMeasureDetailsXml(final ManageMeasureDetailModel measureDetailModel, final Measure measure) {
		logger.info("In MeasureLibraryServiceImpl.createMeasureDetailsXml()");
		setAdditionalAttrsForMeasureXml(measureDetailModel, measure);
		logger.info("creating XML from Measure Details Model");
		ByteArrayOutputStream stream = createXml(measureDetailModel);
		System.out.println(stream.toString());
		return stream.toString();
	}
	
	/**
	 * Creates the measure xml model.
	 * 
	 * @param manageMeasureDetailModel
	 *            the manage measure detail model
	 * @param measure
	 *            the measure
	 * @param replaceNode
	 *            the replace node
	 * @param parentNode
	 *            the parent node
	 * @return the measure xml model
	 */
	private MeasureXmlModel createMeasureXmlModel(final ManageMeasureDetailModel manageMeasureDetailModel, final Measure measure,
			final String replaceNode, final String parentNode) {
		MeasureXmlModel measureXmlModel = new MeasureXmlModel();
		measureXmlModel.setMeasureId(measure.getId());
		measureXmlModel.setXml(createMeasureDetailsXml(manageMeasureDetailModel, measure));
		measureXmlModel.setToReplaceNode(replaceNode);
		measureXmlModel.setParentNode(parentNode);
		return measureXmlModel;
	}
	
	/**
	 * Method to create XML from QualityDataModelWrapper object.
	 * 
	 * @param qualityDataSetDTO
	 *            - {@link QualityDataModelWrapper}.
	 * @return {@link ByteArrayOutputStream}.
	 * */
	private ByteArrayOutputStream createQDMXML(final QualityDataModelWrapper qualityDataSetDTO) {
		logger.info("In ManageCodeLiseServiceImpl.createXml()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("QualityDataModelMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(qualityDataSetDTO);
			logger.info("Marshalling of QualityDataSetDTO is successful.." + stream.toString());
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load QualityDataModelMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else if (e instanceof ValidationException) {
				logger.info("Validation Exception" + e);
			} else {
				logger.info("Other Exception" + e);
			}
		}
		logger.info("Exiting ManageCodeLiseServiceImpl.createXml()");
		return stream;
	}
	
	/**
	 * Creates the xml.
	 * 
	 * @param measureDetailModel
	 *            the measure detail model
	 * @return the byte array output stream
	 */
	private ByteArrayOutputStream createXml(final ManageMeasureDetailModel measureDetailModel) {
		logger.info("In MeasureLibraryServiceImpl.createXml()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("MeasureDetailsModelMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(measureDetailModel);
			logger.info("Marshalling of ManageMeasureDetailsModel is successful.." + stream.toString());
		} catch (Exception e) {
			if (e instanceof IOException) {
				logger.info("Failed to load MeasureDetailsModelMapping.xml" + e);
			} else if (e instanceof MappingException) {
				logger.info("Mapping Failed" + e);
			} else if (e instanceof MarshalException) {
				logger.info("Unmarshalling Failed" + e);
			} else if (e instanceof ValidationException) {
				logger.info("Validation Exception" + e);
			} else {
				logger.info("Other Exception" + e);
			}
		}
		logger.info("Exiting MeasureLibraryServiceImpl.createXml()");
		return stream;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#deleteMeasureNotes(mat.DTO.MeasureNoteDTO)
	 */
	@Override
	public final void deleteMeasureNotes(final MeasureNoteDTO measureNoteDTO) {
		MeasureNotesDAO measureNotesDAO = getMeasureNotesDAO();
		MeasureNotes measureNotes = measureNotesDAO.find(measureNoteDTO.getId());
		try {
			getMeasureNotesService().deleteMeasureNote(measureNotes);
			logger.info("MeasureNotes Deleted Successfully :: " + measureNotes.getId());
		} catch (Exception e) {
			logger.info("MeasureNotes not deleted. Exception occured. Measure notes Id :: " + measureNotes.getId());
		}
	}
	
	/**
	 * Extract measure search model detail.
	 * 
	 * @param currentUserId
	 *            - {@link String}
	 * @param isSuperUser
	 *            - {@link Boolean}
	 * @param dto
	 *            - {@link MeasureShareDTO}.
	 * @return {@link Result}.
	 */
	private ManageMeasureSearchModel.Result extractMeasureSearchModelDetail(final String currentUserId, final boolean isSuperUser,
			final MeasureShareDTO dto) {
		boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
		ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
		detail.setName(dto.getMeasureName());
		detail.setShortName(dto.getShortName());
		detail.setScoringType(dto.getScoringType());
		detail.setStatus(dto.getStatus());
		detail.setId(dto.getMeasureId());
		detail.setStatus(dto.getStatus());
		detail.seteMeasureId(dto.geteMeasureId());
		detail.setClonable(isOwner || isSuperUser);
		detail.setEditable((isOwner || isSuperUser || ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) && dto.isDraft());
		detail.setExportable(dto.isPackaged());
		detail.setSharable(isOwner || isSuperUser);
		detail.setMeasureLocked(dto.isLocked());
		detail.setLockedUserInfo(dto.getLockedUserInfo());
		User user = getUserService().getById(dto.getOwnerUserId());
		detail.setOwnerfirstName(user.getFirstName());
		detail.setOwnerLastName(user.getLastName());
		detail.setOwnerEmailAddress(user.getEmailAddress());
		detail.setDraft(dto.isDraft());
		String formattedVersion = MeasureUtility.getVersionText(dto.getVersion(), dto.isDraft());
		detail.setVersion(formattedVersion);
		detail.setFinalizedDate(dto.getFinalizedDate());
		detail.setMeasureSetId(dto.getMeasureSetId());
		return detail;
	}
	
	/**
	 * Find out maximum version number.
	 * 
	 * @param measureSetId
	 *            - {@link String}.
	 * @return {@link String}. *
	 */
	private String findOutMaximumVersionNumber(final String measureSetId) {
		String maxVerNum = getService().findOutMaximumVersionNumber(measureSetId);
		return maxVerNum;
	}
	
	/**
	 * Find out version number.
	 * 
	 * @param measureId
	 *            - {@link String}.
	 * @param measureSetId
	 *            - {@link String}.
	 * @return {@link String}. *
	 */
	private String findOutVersionNumber(final String measureId, final String measureSetId) {
		String maxVerNum = getService().findOutVersionNumber(measureId, measureSetId);
		return maxVerNum;
	}
	
	/**
	 * * Find All QDM's which are used in Clause Workspace tag's or in
	 * Supplemental Data Elements or in Attribute tags.
	 * 
	 * @param appliedQDMList
	 *            the applied qdm list
	 * @param measureXmlModel
	 *            the measure xml model
	 * @return the array list
	 */
	private ArrayList<QualityDataSetDTO> findUsedQDMs(final ArrayList<QualityDataSetDTO> appliedQDMList,
			final MeasureXmlModel measureXmlModel) {
		
		XmlProcessor processor = new XmlProcessor(measureXmlModel.getXml());
		javax.xml.xpath.XPath xPath = XPathFactory.newInstance().newXPath();
		for (QualityDataSetDTO dataSetDTO : appliedQDMList) {
			String XPATH_EXPRESSION = "/measure//clause//@id=";
			XPATH_EXPRESSION = XPATH_EXPRESSION.concat("'").concat(dataSetDTO.getUuid()).concat("' or /measure//clause//@qdmUUID= '")
					.concat(dataSetDTO.getUuid()).concat("' or /measure/supplementalDataElements//@id='").concat(dataSetDTO.getUuid())
					.concat("'");
			
			try {
				Boolean isUsed = (Boolean) xPath.evaluate(XPATH_EXPRESSION, processor.getOriginalDoc().getDocumentElement(),
						XPathConstants.BOOLEAN);
				dataSetDTO.setUsed(isUsed);
			} catch (XPathExpressionException e) {
				
				e.printStackTrace();
			}
		}
		
		return appliedQDMList;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#generateAndSaveMaxEmeasureId(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public final int generateAndSaveMaxEmeasureId(final ManageMeasureDetailModel measureModel) {
		MeasurePackageService service = getService();
		Measure meas = service.getById(measureModel.getId());
		return service.saveAndReturnMaxEMeasureId(meas);
	}
	
	/**
	 * Gets the all data type attributes.
	 * 
	 * @param qdmName
	 *            - {@link String}.
	 * @return {@link List} of {@link QDSAttributes}. *
	 */
	private List<QDSAttributes> getAllDataTypeAttributes(final String qdmName) {
		List<QDSAttributes> attrs = getAttributeDAO().findByDataType(qdmName, context);
		List<QDSAttributes> attrs1 = getAttributeDAO().getAllDataFlowAttributeName();
		Collections.sort(attrs, attributeComparator);
		Collections.sort(attrs1, attributeComparator);
		attrs.addAll(attrs1);
		// Collections.sort(attrs, attributeComparator);
		return attrs;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getAllMeasureNotesByMeasureID(java.lang.String)
	 */
	@Override
	public final MeasureNotesModel getAllMeasureNotesByMeasureID(final String measureID) {
		MeasureNotesModel measureNotesModel = new MeasureNotesModel();
		ArrayList<MeasureNoteDTO> data = new ArrayList<MeasureNoteDTO>();
		Measure measure = getMeasureDAO().find(measureID);
		if (measure != null) {
			List<MeasureNotes> measureNotesList = getMeasureNotesService().getAllMeasureNotesByMeasureID(measureID);
			if ((measureNotesList != null) && !measureNotesList.isEmpty()) {
				for (MeasureNotes measureNotes : measureNotesList) {
					if (measureNotes != null) {
						MeasureNoteDTO measureNoteDTO = new MeasureNoteDTO();
						measureNoteDTO.setMeasureId(measureID);
						measureNoteDTO.setId(measureNotes.getId());
						if (measureNotes.getModifyUser() != null) {
							measureNoteDTO.setLastModifiedByEmailAddress(
									measureNotes.getModifyUser().getEmailAddress());
						} else if (measureNotes.getCreateUser() != null) {
							measureNoteDTO.setLastModifiedByEmailAddress(
									measureNotes.getCreateUser().getEmailAddress());
						}
						measureNoteDTO.setNoteDesc(measureNotes.getNoteDesc());
						measureNoteDTO.setNoteTitle(measureNotes.getNoteTitle());
						Date lastModifiedDate = measureNotes.getLastModifiedDate();
						SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a z");
						if (lastModifiedDate != null) {
							measureNoteDTO.setLastModifiedDate(dateFormat.format(lastModifiedDate));
						}
						data.add(measureNoteDTO);
					}
				}
			}
		}
		measureNotesModel.setData(data);
		return measureNotesModel;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getAllRecentMeasureForUser(java.lang.String)
	 */
	/** Method to retrieve all Recently searched measure's for the logged in User from 'Recent_MSR_Activity_Log' table.
	 * @param userId - String logged in user id.
	 * @return {@link ManageMeasureSearchModel}. **/
	@Override
	public ManageMeasureSearchModel getAllRecentMeasureForUser(String userId) {
		// Call to fetch
		ArrayList<RecentMSRActivityLog> recentMeasureActivityList = (ArrayList<RecentMSRActivityLog>)
				recentMSRActivityLogDAO.getRecentMeasureActivityLog(userId);
		ManageMeasureSearchModel manageMeasureSearchModel = new ManageMeasureSearchModel();
		List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<ManageMeasureSearchModel.Result>();
		manageMeasureSearchModel.setData(detailModelList);
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		for (RecentMSRActivityLog activityLog : recentMeasureActivityList) {
			Measure measure = getMeasureDAO().find(activityLog.getMeasureId());
			ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
			detail.setName(measure.getDescription());
			detail.setShortName(measure.getaBBRName());
			detail.setId(measure.getId());
			detail.setDraft(measure.isDraft());
			detail.setExportable(measure.getExportedDate() != null); // to show export icon.
			detail.setStatus(measure.getMeasureStatus());
			String formattedVersion = MeasureUtility.getVersionText(measure.getVersion(),
					measure.isDraft());
			detail.setVersion(formattedVersion);
			detail.setFinalizedDate(measure.getFinalizedDate());
			detail.setOwnerfirstName(measure.getOwner().getFirstName());
			detail.setOwnerLastName(measure.getOwner().getLastName());
			detail.setOwnerEmailAddress(measure.getOwner().getEmailAddress());
			detail.setMeasureSetId(measure.getMeasureSet().getId());
			detail.setScoringType(measure.getMeasureScoring());
			detail.setMeasureLocked(measure.getLockedOutDate() != null);
			List<MeasureShareDTO> measureShare = getMeasureDAO().
					getMeasureShareInfoForMeasureAndUser(measure.getOwner().getId(), measure.getId());
			if (measureShare.size() > 0) {
				detail.setEditable((currentUserId.equals(measure.getOwner().getId()) || isSuperUser
						|| ShareLevel.MODIFY_ID.equals(
								measureShare.get(0).getShareLevel())) && measure.isDraft());
			} else {
				detail.setEditable((currentUserId.equals(measure.getOwner().getId()) || isSuperUser));
			}
			detailModelList.add(detail);
		}
		return manageMeasureSearchModel;
	}
	
	/** Gets the and validate value set date.
	 * 
	 * @param valueSetDateStr - {@link String}.
	 * @return the and validate value set date
	 * @throws InvalidValueSetDateException - {@link Exception}. * */
	private void getAndValidateValueSetDate(final String valueSetDateStr) throws InvalidValueSetDateException {
		if (StringUtils.isNotBlank(valueSetDateStr)) {
			DateStringValidator dsv = new DateStringValidator();
			int validationCode = dsv.isValidDateString(valueSetDateStr);
			if (validationCode != DateStringValidator.VALID) {
				throw new InvalidValueSetDateException();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getAppliedQDMFromMeasureXml(java.lang.String, boolean)
	 */
	@Override
	public final ArrayList<QualityDataSetDTO> getAppliedQDMFromMeasureXml(final String measureId,
			final boolean checkForSupplementData) {
		MeasureXmlModel measureXmlModel = getMeasureXmlForMeasure(measureId);
		QualityDataModelWrapper details = convertXmltoQualityDataDTOModel(measureXmlModel);
		ArrayList<QualityDataSetDTO> finalList = new ArrayList<QualityDataSetDTO>();
		if (details != null) {
			if ((details.getQualityDataDTO() != null) && (details.getQualityDataDTO().size() != 0)) {
				logger.info(" details.getQualityDataDTO().size() :" + details.getQualityDataDTO().size());
				for (QualityDataSetDTO dataSetDTO : details.getQualityDataDTO()) {
					if (dataSetDTO.getCodeListName() != null) {
						if ((checkForSupplementData && dataSetDTO.isSuppDataElement())) {
							continue;
						} else {
							finalList.add(dataSetDTO);
						}
					}
				}
			}
			Collections.sort(finalList, new Comparator<QualityDataSetDTO>() {
				@Override
				public int compare(final QualityDataSetDTO o1, final QualityDataSetDTO o2) {
					return o1.getCodeListName().compareToIgnoreCase(o2.getCodeListName());
				}
			});
		}
		
		finalList = findUsedQDMs(finalList, measureXmlModel);
		logger.info("finalList()of QualityDataSetDTO ::" + finalList.size());
		return finalList;
		
	}
	
	/**
	 * Gets the attribute dao.
	 * 
	 * @return the attribute dao
	 */
	private QDSAttributesDAO getAttributeDAO() {
		return ((QDSAttributesDAO) context.getBean("qDSAttributesDAO"));
		
	}
	
	/**
	 * Gets the context.
	 * 
	 * @return the context
	 */
	public ApplicationContext getContext() {
		return context;
	}
	
	/**
	 * Gets the locked user.
	 * 
	 * @param existingMeasure
	 *            the existing measure
	 * @return the locked user
	 */
	private User getLockedUser(final Measure existingMeasure) {
		return existingMeasure.getLockedUser();
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getMaxEMeasureId()
	 */
	@Override
	public final int getMaxEMeasureId() {
		MeasurePackageService service = getService();
		int emeasureId = service.getMaxEMeasureId();
		logger.info("**********Current Max EMeasure Id from DB ******************" + emeasureId);
		return emeasureId;
		// return 2012;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getMeasure(java.lang.String)
	 */
	@Override
	public final ManageMeasureDetailModel getMeasure(final String key) {
		logger.info("In MeasureLibraryServiceImpl.getMeasure() method..");
		logger.info("Loading Measure for MeasueId: " + key);
		Measure measure = getService().getById(key);
		MeasureXmlModel xml = getMeasureXmlForMeasure(key);
		return convertXmltoModel(xml, measure);
		
	}
	
	/**
	 * Gets the measure dao.
	 * 
	 * @return the measure dao
	 */
	private MeasureDAO getMeasureDAO() {
		return ((MeasureDAO) context.getBean("measureDAO"));
	}
	
	/**
	 * Gets the measure notes dao.
	 * 
	 * @return the measure notes dao
	 */
	private MeasureNotesDAO getMeasureNotesDAO() {
		return ((MeasureNotesDAO) context.getBean("measureNotesDAO"));
	}
	
	/**
	 * Gets the measure notes service.
	 * 
	 * @return the measure notes service
	 */
	private MeasureNotesService getMeasureNotesService() {
		return ((MeasureNotesService) context.getBean("measureNotesService"));
		
	}
	
	/**
	 * Gets the measure package service.
	 * 
	 * @return {@link MeasurePackageService}. *
	 */
	public final MeasurePackageService getMeasurePackageService() {
		return (MeasurePackageService) context.getBean("measurePackageService");
	}
	
	/**
	 * Gets the measure xml dao.
	 * 
	 * @return the measure xml dao
	 */
	private MeasureXMLDAO getMeasureXMLDAO() {
		return ((MeasureXMLDAO) context.getBean("measureXMLDAO"));
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getMeasureXmlForMeasure(java.lang.String)
	 */
	@Override
	public final MeasureXmlModel getMeasureXmlForMeasure(final String measureId) {
		logger.info("In MeasureLibraryServiceImpl.getMeasureXmlForMeasure()");
		MeasureXmlModel measureXmlModel = getService().getMeasureXmlForMeasure(measureId);
		
		if (measureXmlModel == null) {
			logger.info("Measure XML is null");
		}
		logger.info("XML ::: " + measureXmlModel.getXml());
		return measureXmlModel;
	}
	
	/** Gets the page count.
	 * 
	 * @param userId the user id
	 * @return {@link Integer}. * */
	/*private int getPageCount(final long totalRows, final int numberOfRows) {
		int pageCount = 0;
		int mod = (int) (totalRows % numberOfRows);
		pageCount = (int) (totalRows / numberOfRows);
		pageCount = (mod > 0) ? (pageCount + 1) : pageCount;
		return pageCount;
	}*/
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getRecentMeasureActivityLog(java.lang.String)
	 */
	@Override
	public List<RecentMSRActivityLog> getRecentMeasureActivityLog(String userId) {
		return recentMSRActivityLogDAO.getRecentMeasureActivityLog(userId);
	}
	
	/**
	 * Gets the service.
	 * 
	 * @return the service
	 */
	private MeasurePackageService getService() {
		return (MeasurePackageService) context.getBean("measurePackageService");
	}
	
	/**
	 * Gets the user service.
	 * 
	 * @return the user service
	 */
	private UserService getUserService() {
		return (UserService) context.getBean("userService");
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#getUsersForShare(java.lang.String, int, int)
	 */
	@Override
	public final ManageMeasureShareModel getUsersForShare(final String measureId, final int startIndex, final int pageSize) {
		ManageMeasureShareModel model = new ManageMeasureShareModel();
		List<MeasureShareDTO> dtoList = getService().getUsersForShare(measureId, startIndex, pageSize);
		model.setResultsTotal(getService().countUsersForMeasureShare());
		List<MeasureShareDTO> dataList = new ArrayList<MeasureShareDTO>();
		for (MeasureShareDTO dto : dtoList) {
			dataList.add(dto);
		}
		// model.setData(dtoList); Directly setting dtoList causes the RPC
		// serialization exception(java.util.RandomaccessSubList) since we are
		// sublisting it.
		model.setData(dataList);
		model.setStartIndex(startIndex);
		model.setMeasureId(measureId);
		model.setPrivate(getService().getById(measureId).getIsPrivate());
		return model;
	}
	
	/**
	 * Increment version number and save.
	 * 
	 * @param maximumVersionNumber
	 *            - {@link String}.
	 * @param incrementBy
	 *            - {@link String}.
	 * @param mDetail
	 *            - {@link ManageMeasureDetailModel}.
	 * @param meas
	 *            - {@link Measure}.
	 * @return {@link SaveMeasureResult}. *
	 */
	private SaveMeasureResult incrementVersionNumberAndSave(final String maximumVersionNumber, final String incrementBy,
			final ManageMeasureDetailModel mDetail, final Measure meas) {
		BigDecimal mVersion = new BigDecimal(maximumVersionNumber);
		mVersion = mVersion.add(new BigDecimal(incrementBy));
		mDetail.setVersionNumber(mVersion.toString());
		Date currentDate = new Date();
		mDetail.setFinalizedDate(DateUtility.convertDateToString(currentDate));
		mDetail.setDraft(false);
		setValueFromModel(mDetail, meas);
		getService().save(meas);
		saveMeasureXml(createMeasureXmlModel(mDetail, meas, MEASURE_DETAILS, MEASURE));
		SaveMeasureResult result = new SaveMeasureResult();
		result.setSuccess(true);
		result.setId(meas.getId());
		String versionStr = meas.getMajorVersionStr() + "." + meas.getMinorVersionStr();
		result.setVersionStr(versionStr);
		logger.info("Result passed for Version Number " + versionStr);
		return result;
	}
	
	// TODO refactor this logic into a shared location: see MeasureDAO.
	/**
	 * Checks if Measure is locked.
	 * 
	 * @param m
	 *            the Measure
	 * @return true, if is locked
	 */
	private boolean isLocked(final Measure m) {
		if (m.getLockedOutDate() == null) {
			return false;
		}
		long lockTime = m.getLockedOutDate().getTime();
		long currentTime = System.currentTimeMillis();
		long threshold = 3 * 60 * 1000;
		boolean isLockExpired = (currentTime - lockTime) > threshold;
		return !isLockExpired;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#isMeasureLocked(java.lang.String)
	 */
	@Override
	public final boolean isMeasureLocked(final String id) {
		MeasurePackageService service = getService();
		boolean isLocked = service.isMeasureLocked(id);
		return isLocked;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#recordRecentMeasureActivity(java.lang.String, java.lang.String)
	 */
	@Override
	public void recordRecentMeasureActivity(String measureId, String userId) {
		recentMSRActivityLogDAO.recordRecentMeasureActivity(measureId, userId);
	}
	
	/**
	 * Removes the pattern from xml string.
	 * 
	 * @param xmlString
	 *            the xml string
	 * @param patternStart
	 *            the pattern start
	 * @param replaceWith
	 *            the replace with
	 * @return the string
	 */
	private String removePatternFromXMLString(final String xmlString, final String patternStart, final String replaceWith) {
		String newString = xmlString;
		if (patternStart != null) {
			newString = newString.replaceAll(patternStart, replaceWith);
		}
		return newString;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.measure.service.MeasureService#resetLockedDate(java.lang.String
	 * , java.lang.String) This method has been added to release the Measure
	 * lock. It gets the existingMeasureLock and checks the loggedinUserId and
	 * the LockedUserid to release the lock.
	 */
	@Override
	public final SaveMeasureResult resetLockedDate(final String measureId, final String userId) {
		Measure existingMeasure = null;
		User lockedUser = null;
		SaveMeasureResult result = new SaveMeasureResult();
		if ((measureId != null) && (userId != null) && StringUtils.isNotBlank(measureId)) {
			existingMeasure = getService().getById(measureId);
			if (existingMeasure != null) {
				lockedUser = getLockedUser(existingMeasure);
				if ((lockedUser != null) && lockedUser.getId().equalsIgnoreCase(userId)) {
					// Only if the lockedUser and loggedIn User are same we can
					// allow the user to unlock the measure.
					if (existingMeasure.getLockedOutDate() != null) {
						// if it is not null then set it to null and save it.
						existingMeasure.setLockedOutDate(null);
						existingMeasure.setLockedUser(null);
						getService().updateLockedOutDate(existingMeasure);
						result.setSuccess(true);
					}
				}
			}
			result.setId(existingMeasure.getId());
		}
		
		return result;
	}
	
	/**
	 * Return failure reason.
	 * 
	 * @param rs
	 *            - {@link SaveMeasureResult}.
	 * @param failureReason
	 *            - {@link Integer}.
	 * @return {@link SaveMeasureResult}. *
	 */
	private SaveMeasureResult returnFailureReason(final SaveMeasureResult rs, final int failureReason) {
		rs.setFailureReason(failureReason);
		rs.setSuccess(false);
		return rs;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveMeasureAtPackage(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public SaveMeasureResult saveMeasureAtPackage(ManageMeasureDetailModel model) {
		Measure measure = getService().getById(model.getId());
		Integer revisionNumber = new Integer(000);
		if ((measure.getRevisionNumber() != null) && StringUtils.isNotEmpty(measure.getRevisionNumber())) {
			revisionNumber = Integer.parseInt(measure.getRevisionNumber());
			revisionNumber = revisionNumber + 1;
			measure.setRevisionNumber(String.format("%03d", revisionNumber));
			model.setRevisionNumber(String.format("%03d", revisionNumber));
		} else {
			revisionNumber = revisionNumber + 1;
			measure.setRevisionNumber(String.format("%03d", revisionNumber));
			model.setRevisionNumber(String.format("%03d", revisionNumber));
		}
		getService().save(measure);
		SaveMeasureResult result = save(model);
		return result;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#save(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public final SaveMeasureResult save(ManageMeasureDetailModel model) {
		
		Measure pkg = null;
		MeasureSet measureSet = null;
		if (model.getId() != null) {
			// editing an existing measure
			pkg = getService().getById(model.getId());
			model.setVersionNumber(pkg.getVersion());
			if (pkg.isDraft()) {
				model.setRevisionNumber(pkg.getRevisionNumber());
			} else {
				model.setRevisionNumber("000");
			}
			if (pkg.getMeasureSet().getId() != null) {
				measureSet = getService().findMeasureSet(pkg.getMeasureSet().getId());
			}
			if (!pkg.getMeasureScoring().equalsIgnoreCase(model.getMeasScoring())) {
				// US 194 User is changing the measure scoring. Make sure to
				// delete any groupings for that measure and save.
				getMeasurePackageService().deleteExistingPackages(pkg.getId());
			}
		} else {
			// creating a new measure.
			
			pkg = new Measure();
			model.setMeasureStatus("In Progress");
			model.setRevisionNumber("000");
			measureSet = new MeasureSet();
			measureSet.setId(UUID.randomUUID().toString());
			getService().save(measureSet);
		}
		
		pkg.setMeasureSet(measureSet);
		setValueFromModel(model, pkg);
		SaveMeasureResult result = new SaveMeasureResult();
		try {
			getAndValidateValueSetDate(model.getValueSetDate());
			pkg.setValueSetDate(DateUtility.addTimeToDate(pkg.getValueSetDate()));
			getService().save(pkg);
		} catch (InvalidValueSetDateException e) {
			result.setSuccess(false);
			result.setFailureReason(SaveMeasureResult.INVALID_VALUE_SET_DATE);
			result.setId(pkg.getId());
			return result;
		}
		result.setSuccess(true);
		result.setId(pkg.getId());
		saveMeasureXml(createMeasureXmlModel(model, pkg, MEASURE_DETAILS, MEASURE));
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveAndDeleteMeasure(java.lang.String)
	 */
	@Override
	public final void saveAndDeleteMeasure(final String measureID) {
		logger.info("MeasureLibraryServiceImpl: saveAndDeleteMeasure start : measureId:: " + measureID);
		MeasureDAO measureDAO = getMeasureDAO();
		Measure m = measureDAO.find(measureID);
		
		logger.info("Measure Deletion Started for measure Id :: " + measureID);
		try {
			measureDAO.delete(m);
			logger.info("Measure Deleted Successfully :: " + measureID);
		} catch (Exception e) {
			logger.info("Measure not deleted.Something went wrong for measure Id :: " + measureID);
		}
		
		logger.info("MeasureLibraryServiceImpl: saveAndDeleteMeasure End : measureId:: " + measureID);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveFinalizedVersion(java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public final SaveMeasureResult saveFinalizedVersion(final String measureId, final boolean isMajor, final String version) {
		logger.info("In MeasureLibraryServiceImpl.saveFinalizedVersion() method..");
		Measure m = getService().getById(measureId);
		logger.info("Measure Loaded for: " + measureId);
		String versionNumber = null;
		if (isMajor) {
			versionNumber = findOutMaximumVersionNumber(m.getMeasureSet().getId());
			// For new measure's only draft entry will be
			// available.findOutMaximumVersionNumber will return null.
			if (versionNumber == null) {
				versionNumber = "0.000";
			}
			logger.info("Max Version Number loaded from DB: " + versionNumber);
		} else {
			int versionIndex = version.indexOf('v');
			logger.info("Min Version number passed from Page Model: " + versionIndex);
			String selectedVersion = version.substring(versionIndex + 1);
			logger.info("Min Version number after trim: " + selectedVersion);
			versionNumber = findOutVersionNumber(m.getMeasureSet().getId(), selectedVersion);
			
		}
		ManageMeasureDetailModel mDetail = getMeasure(measureId);
		SaveMeasureResult rs = new SaveMeasureResult();
		int endIndex = versionNumber.indexOf('.');
		String majorVersionNumber = versionNumber.substring(0, endIndex);
		if (!versionNumber.equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_VERSION)) {
			String[] versionArr = versionNumber.split("\\.");
			if (isMajor) {
				if (!versionArr[0].equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_MAJOR_VERSION)) {
					return incrementVersionNumberAndSave(majorVersionNumber, "1", mDetail, m);
				} else {
					return returnFailureReason(rs, SaveMeasureResult.REACHED_MAXIMUM_MAJOR_VERSION);
				}
				
			} else {
				if (!versionArr[1].equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_MINOR_VERSION)) {
					return incrementVersionNumberAndSave(versionNumber, "0.001", mDetail, m);
				} else {
					return returnFailureReason(rs, SaveMeasureResult.REACHED_MAXIMUM_MINOR_VERSION);
				}
			}
		} else {
			return returnFailureReason(rs, SaveMeasureResult.REACHED_MAXIMUM_VERSION);
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveMeasureDetails(mat.client.measure.ManageMeasureDetailModel)
	 */
	@Override
	public final SaveMeasureResult saveMeasureDetails(final ManageMeasureDetailModel model) {
		logger.info("In MeasureLibraryServiceImpl.saveMeasureDetails() method..");
		Measure measure = null;
		if (model.getId() != null) {
			measure = getService().getById(model.getId());
			if ((measure.getMeasureStatus() != null) && !measure.getMeasureStatus().equalsIgnoreCase(model.getMeasureStatus())) {
				measure.setMeasureStatus(model.getMeasureStatus());
				getService().save(measure);
			}
		}
		model.setRevisionNumber(measure.getRevisionNumber());
		logger.info("Saving Measure_Xml");
		saveMeasureXml(createMeasureXmlModel(model, measure, MEASURE_DETAILS, MEASURE));
		SaveMeasureResult result = new SaveMeasureResult();
		result.setSuccess(true);
		logger.info("Saving of Measure Details Success");
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveMeasureNote(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public final void saveMeasureNote(final String noteTitle, final String noteDescription, final String measureId, final String userId) {
		try {
			MeasureNotes measureNote = new MeasureNotes();
			measureNote.setNoteTitle(noteTitle);
			measureNote.setNoteDesc(noteDescription);
			Measure measure = getMeasureDAO().find(measureId);
			if (measure != null) {
				measureNote.setMeasure_id(measureId);
			}
			User user = getUserService().getById(userId);
			if (user != null) {
				measureNote.setCreateUser(user);
			}
			measureNote.setLastModifiedDate(new Date());
			getMeasureNotesService().saveMeasureNote(measureNote);
			logger.info("MeasureNotes Saved Successfully.");
		} catch (Exception e) {
			logger.info("Failed to save MeasureNotes. Exception occured.");
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#saveMeasureXml(mat.client.clause.clauseworkspace.model.MeasureXmlModel)
	 */
	@Override
	public final void saveMeasureXml(final MeasureXmlModel measureXmlModel) {
		
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
		if ((xmlModel != null) && StringUtils.isNotBlank(xmlModel.getXml())) {
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			String newXml = xmlProcessor.replaceNode(measureXmlModel.getXml(), measureXmlModel.getToReplaceNode(),
					measureXmlModel.getParentNode());
			xmlProcessor.checkForScoringType();
			checkForTimingElementsAndAppend(xmlProcessor);
			newXml = xmlProcessor.transform(xmlProcessor.getOriginalDoc());
			measureXmlModel.setXml(newXml);
		} else {
			XmlProcessor processor = new XmlProcessor(measureXmlModel.getXml());
			processor.addParentNode(MEASURE);
			processor.checkForScoringType();
			checkForTimingElementsAndAppend(processor);
			measureXmlModel.setXml(processor.transform(processor.getOriginalDoc()));
			
			QualityDataModelWrapper wrapper = getMeasureXMLDAO().createSupplimentalQDM(measureXmlModel.getMeasureId(), false, null);
			// Object to XML for elementLookUp
			ByteArrayOutputStream streamQDM = XmlProcessor.convertQualityDataDTOToXML(wrapper);
			// Object to XML for supplementalDataElements
			ByteArrayOutputStream streamSuppDataEle = XmlProcessor.convertQDMOToSuppleDataXML(wrapper);
			// Remove <?xml> and then replace.
			String filteredString = removePatternFromXMLString(
					streamQDM.toString().substring(streamQDM.toString().indexOf("<measure>", 0)), "<measure>", "");
			filteredString = removePatternFromXMLString(filteredString, "</measure>", "");
			// Remove <?xml> and then replace.
			String filteredStringSupp = removePatternFromXMLString(
					streamSuppDataEle.toString().substring(streamSuppDataEle.toString().indexOf("<measure>", 0)), "<measure>", "");
			filteredStringSupp = removePatternFromXMLString(filteredStringSupp, "</measure>", "");
			// Add Supplemental data to elementLoopUp
			String result = callAppendNode(measureXmlModel, filteredString, "qdm", "/measure/elementLookUp");
			measureXmlModel.setXml(result);
			// Add Supplemental data to supplementalDataElements
			result = callAppendNode(measureXmlModel, filteredStringSupp, "elementRef", "/measure/supplementalDataElements");
			measureXmlModel.setXml(result);
			
		}
		getService().saveMeasureXml(measureXmlModel);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#search(java.lang.String, int, int, int)
	 */
	@Override
	public final ManageMeasureSearchModel search(final String searchText, final int startIndex, final int pageSize, final int filter) {
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		ManageMeasureSearchModel searchModel = new ManageMeasureSearchModel();
		
		if (SecurityRole.ADMIN_ROLE.equals(userRole)) {
			List<MeasureShareDTO> measureList = getService().searchForAdminWithFilter(searchText, startIndex, pageSize, filter);
			List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<ManageMeasureSearchModel.Result>();
			searchModel.setData(detailModelList);
			for (MeasureShareDTO dto : measureList) {
				ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
				detail.setName(dto.getMeasureName());
				detail.setId(dto.getMeasureId());
				detail.seteMeasureId(dto.geteMeasureId());
				detail.setDraft(dto.isDraft());
				String formattedVersion = MeasureUtility.getVersionText(dto.getVersion(), dto.isDraft());
				detail.setVersion(formattedVersion);
				detail.setFinalizedDate(dto.getFinalizedDate());
				detail.setStatus(dto.getStatus());
				User user = getUserService().getById(dto.getOwnerUserId());
				detail.setOwnerfirstName(user.getFirstName());
				detail.setOwnerLastName(user.getLastName());
				detail.setOwnerEmailAddress(user.getEmailAddress());
				detail.setMeasureSetId(dto.getMeasureSetId());
				detailModelList.add(detail);
			}
		} else {
			List<MeasureShareDTO> measureList = getService().searchWithFilter(searchText, startIndex, pageSize, filter);
			searchModel.setStartIndex(startIndex);
			searchModel.setResultsTotal((int) getService().count(filter));
			List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<ManageMeasureSearchModel.Result>();
			searchModel.setData(detailModelList);
			for (MeasureShareDTO dto : measureList) {
				ManageMeasureSearchModel.Result detail = extractMeasureSearchModelDetail(currentUserId, isSuperUser, dto);
				detailModelList.add(detail);
			}
		}
		
		return searchModel;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#searchMeasuresForDraft(java.lang.String)
	 */
	@Override
	public final ManageMeasureSearchModel searchMeasuresForDraft(final String searchText) {
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		ManageMeasureSearchModel searchModel = new ManageMeasureSearchModel();
		List<MeasureShareDTO> measureList = getService().searchMeasuresForDraft(searchText);
		searchModel.setResultsTotal((int) getService().countMeasuresForDraft(searchText));
		List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<ManageMeasureSearchModel.Result>();
		searchModel.setData(detailModelList);
		for (MeasureShareDTO dto : measureList) {
			setDTOtoModel(detailModelList, dto, currentUserId, isSuperUser);
		}
		return searchModel;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#searchMeasuresForVersion(java.lang.String)
	 */
	@Override
	public final ManageMeasureSearchModel searchMeasuresForVersion(final String searchText) {
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		ManageMeasureSearchModel searchModel = new ManageMeasureSearchModel();
		List<MeasureShareDTO> measureList = getService().searchMeasuresForVersion(searchText);
		searchModel.setResultsTotal((int) getService().countMeasuresForVersion(searchText));
		List<ManageMeasureSearchModel.Result> detailModelList = new ArrayList<ManageMeasureSearchModel.Result>();
		searchModel.setData(detailModelList);
		
		for (MeasureShareDTO dto : measureList) {
			setDTOtoModel(detailModelList, dto, currentUserId, isSuperUser);
		}
		return searchModel;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#searchUsers(int, int)
	 */
	@Override
	public final TransferMeasureOwnerShipModel searchUsers(final int startIndex, final int pageSize) {
		UserService userService = getUserService();
		List<User> searchResults = userService.searchNonAdminUsers("", startIndex, pageSize);
		logger.info("User search returned " + searchResults.size());
		
		TransferMeasureOwnerShipModel result = new TransferMeasureOwnerShipModel();
		List<TransferMeasureOwnerShipModel.Result> detailList = new ArrayList<TransferMeasureOwnerShipModel.Result>();
		for (User user : searchResults) {
			TransferMeasureOwnerShipModel.Result r = new TransferMeasureOwnerShipModel.Result();
			r.setFirstName(user.getFirstName());
			r.setLastName(user.getLastName());
			r.setEmailId(user.getEmailAddress());
			r.setKey(user.getId());
			detailList.add(r);
		}
		result.setData(detailList);
		result.setStartIndex(startIndex);
		result.setResultsTotal(getUserService().countSearchResultsNonAdmin(""));
		
		return result;
		
	}
	
	/**
	 * Setting Additional Attributes for Measure Xml.
	 * 
	 * @param measureDetailModel
	 *            - {@link ManageMeasureDetailModel}.
	 * @param measure
	 *            - {@link Measure}.
	 */
	private void setAdditionalAttrsForMeasureXml(final ManageMeasureDetailModel measureDetailModel, final Measure measure) {
		logger.info("In MeasureLibraryServiceImpl.setAdditionalAttrsForMeasureXml()");
		measureDetailModel.setId(measure.getId());
		measureDetailModel.setMeasureSetId(measure.getMeasureSet() != null ? measure.getMeasureSet().getId() : null);
		measureDetailModel.setOrgVersionNumber(MeasureUtility.formatVersionText(
				measureDetailModel.getRevisionNumber(), String.valueOf(measure.getVersionNumber())));
		measureDetailModel.setVersionNumber(MeasureUtility.getVersionText(measureDetailModel.getOrgVersionNumber(),
				measureDetailModel.getRevisionNumber(), measure.isDraft()));
		measureDetailModel.setId(UuidUtility.idToUuid(measureDetailModel.getId())); // have
		// to
		// change
		// on
		// unmarshalling.
		if (StringUtils.isNotBlank(measureDetailModel.getMeasFromPeriod()) || StringUtils.isNotBlank(measureDetailModel.getMeasToPeriod())) {
			PeriodModel periodModel = new PeriodModel();
			periodModel.setUuid(UUID.randomUUID().toString());
			if (StringUtils.isNotBlank(measureDetailModel.getMeasFromPeriod())) {
				periodModel.setStartDate(measureDetailModel.getMeasFromPeriod());
				periodModel.setStartDateUuid(UUID.randomUUID().toString());
			}
			if (StringUtils.isNotBlank(measureDetailModel.getMeasToPeriod())) {
				periodModel.setStopDate(measureDetailModel.getMeasToPeriod());
				periodModel.setStopDateUuid(UUID.randomUUID().toString());
			}
			measureDetailModel.setPeriodModel(periodModel);
		}
		
		if (StringUtils.isNotBlank(measureDetailModel.getMeasSteward())
				&& !StringUtils.equalsIgnoreCase(measureDetailModel.getMeasSteward(), "Other")) {
			String oid = getService().retrieveStewardOID(measureDetailModel.getMeasSteward().trim());
			measureDetailModel.setStewardUuid(oid);
		} else if (StringUtils.equalsIgnoreCase(measureDetailModel.getMeasSteward(), "Other")
				&& StringUtils.isNotBlank(measureDetailModel.getMeasStewardOther())) {
			measureDetailModel.setStewardUuid(UUID.randomUUID().toString());
		}
		
		if (StringUtils.isNotBlank(measureDetailModel.getGroupName())) {
			measureDetailModel.setQltyMeasureSetUuid(UUID.randomUUID().toString());
		}
		
		setOrgIdInAuthor(measureDetailModel.getAuthorList());
		setMeasureTypeAbbreviation(measureDetailModel.getMeasureTypeList());
		measureDetailModel.setScoringAbbr(setScoringAbbreviation(measureDetailModel.getMeasScoring()));
		
		if ((measureDetailModel.getEndorseByNQF() != null) && measureDetailModel.getEndorseByNQF()) {
			measureDetailModel.setEndorsement("National Quality Forum");
			measureDetailModel.setEndorsementId("2.16.840.1.113883.3.560");
		} else {
			measureDetailModel.setEndorsement(null);
			measureDetailModel.setEndorsementId(null);
		}
		NqfModel nqfModel = new NqfModel();
		nqfModel.setExtension(measureDetailModel.getNqfId());
		nqfModel.setRoot("2.16.840.1.113883.3.560.1");
		measureDetailModel.setNqfModel(nqfModel);
		if (CollectionUtils.isEmpty(MeasureDetailsUtil.getTrimmedList(measureDetailModel.getReferencesList()))) {
			measureDetailModel.setReferencesList(null);
		}
		logger.info("Exiting MeasureLibraryServiceImpl.setAdditionalAttrsForMeasureXml()..");
	}
	
	/**
	 * Sets the context.
	 * 
	 * @param context
	 *            the new context
	 */
	public void setContext(ApplicationContext context) {
		this.context = context;
	}
	
	/**
	 * Sets the dt oto model.
	 * 
	 * @param detailModelList
	 *            - {@link Result}.
	 * @param dto
	 *            - {@link MeasureShareDTO}.
	 * @param currentUserId
	 *            - {@link String}.
	 * @param isSuperUser
	 *            - {@link Boolean}. *
	 */
	private void setDTOtoModel(final List<ManageMeasureSearchModel.Result> detailModelList, final MeasureShareDTO dto,
			final String currentUserId, final boolean isSuperUser) {
		boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
		ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
		detail.setName(dto.getMeasureName());
		detail.setShortName(dto.getShortName());
		detail.setStatus(dto.getStatus());
		detail.setId(dto.getMeasureId());
		detail.setStatus(dto.getStatus());
		detail.setClonable(isOwner || isSuperUser);
		detail.setEditable((isOwner || isSuperUser || ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) && dto.isDraft());
		detail.setMeasureLocked(dto.isLocked());
		detail.setExportable(dto.isPackaged());
		detail.setSharable(isOwner || isSuperUser);
		detail.setLockedUserInfo(dto.getLockedUserInfo());
		detail.setDraft(dto.isDraft());
		String formattedVersion = MeasureUtility.getVersionText(dto.getVersion(), dto.isDraft());
		detail.setVersion(formattedVersion);
		detail.setScoringType(dto.getScoringType());
		detail.setMeasureSetId(dto.getMeasureSetId());
		detailModelList.add(detail);
	}
	
	/**
	 * Sets the measure package service.
	 * 
	 * @param measurePackageService
	 *            the new measure package service
	 */
	public final void setMeasurePackageService(final MeasurePackageService measurePackageService) {
		this.measurePackageService = measurePackageService;
	}
	
	/**
	 * Sets the measure type abbreviation.
	 * 
	 * @param measureTypeList
	 *            the new measure type abbreviation
	 */
	private void setMeasureTypeAbbreviation(final List<MeasureType> measureTypeList) {
		if (measureTypeList != null) {
			for (MeasureType measureType : measureTypeList) {
				measureType.setAbbrDesc(MeasureDetailsUtil.getMeasureTypeAbbr(measureType.getDescription()));
			}
		}
	}
	
	/**
	 * Sets the org id in author.
	 * 
	 * @param authors
	 *            the new org id in author
	 */
	private void setOrgIdInAuthor(final List<Author> authors) {
		if (CollectionUtils.isNotEmpty(authors)) {
			for (Author author : authors) {
				String oid = getService().retrieveStewardOID(author.getAuthorName().trim());
				author.setOrgId((oid != null) && !oid.equals("") ? oid : UUID.randomUUID().toString());
			}
		}
	}
	
	/**
	 * Sets the scoring abbreviation.
	 * 
	 * @param measScoring
	 *            the meas scoring
	 * @return the string
	 */
	private String setScoringAbbreviation(final String measScoring) {
		return MeasureDetailsUtil.getScoringAbbr(measScoring);
	}
	
	/**
	 * Sets the user service.
	 * 
	 * @param userService
	 *            the new user service
	 */
	public final void setUserService(final UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Sets the value from model.
	 * 
	 * @param model
	 *            the model
	 * @param measure
	 *            the measure
	 */
	private void setValueFromModel(final ManageMeasureDetailModel model, final Measure measure) {
		measure.setDescription(model.getName());
		measure.setaBBRName(model.getShortName());
		// US 421. Scoring choice is not part of core measure.
		measure.setMeasureScoring(model.getMeasScoring());
		measure.setVersion(model.getVersionNumber());
		measure.setDraft(model.isDraft());
		measure.setRevisionNumber(model.getRevisionNumber());
		measure.setMeasureStatus(model.getMeasureStatus());
		measure.seteMeasureId(model.geteMeasureId());
		if ((model.getFinalizedDate() != null) && !model.getFinalizedDate().equals("")) {
			measure.setFinalizedDate(new Timestamp(DateUtility.convertStringToDate(model.getFinalizedDate()).getTime()));
		}
		if ((model.getValueSetDate() != null) && !model.getValueSetDate().equals("")) {
			measure.setValueSetDate(new Timestamp(DateUtility.convertStringToDate(model.getValueSetDate()).getTime()));
		}
	}
	
	/** This method updates MeasureXML - Attributes Nodes
	 * 
	 * *.
	 * 
	 * @param list the list
	 * @param toEmail the to email */
	
	/*
	 * private void updateAttributes(final XmlProcessor processor, final
	 * QualityDataSetDTO modifyWithDTO, final QualityDataSetDTO modifyDTO) {
	 * 
	 * logger.debug(" MeasureLibraryServiceImpl: updateAttributes Start :  ");
	 * 
	 * String XPATH_EXPRESSION_ATTRIBUTE =
	 * "/measure//clause//attribute[@qdmUUID='"
	 * +modifyDTO.getUuid()+"']";//XPath to find all elementRefs in
	 * supplementalDataElements for to be modified QDM.
	 * 
	 * try { NodeList nodesATTR = (NodeList)
	 * xPath.evaluate(XPATH_EXPRESSION_ATTRIBUTE, processor.getOriginalDoc(),
	 * XPathConstants.NODESET); for(int i=0 ;i<nodesATTR.getLength();i++){ Node
	 * newNode = nodesATTR.item(i); newNode
	 * .getAttributes().getNamedItem("name").setNodeValue(modifyWithDTO
	 * .getDataType()); }
	 * 
	 * } catch (XPathExpressionException e) { e.printStackTrace(); }
	 * 
	 * logger.debug(" MeasureLibraryServiceImpl: updateAttributes End : "); }
	 */
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#transferOwnerShipToUser(java.util.List, java.lang.String)
	 */
	@Override
	public final void transferOwnerShipToUser(final List<String> list, final String toEmail) {
		MeasurePackageService service = getService();
		service.transferMeasureOwnerShipToUser(list, toEmail);
	}
	
	/**
	 * This method updates MeasureXML - QDM nodes under ElementLookUp.
	 * 
	 * *
	 * 
	 * @param processor
	 *            the processor
	 * @param modifyWithDTO
	 *            the modify with dto
	 * @param modifyDTO
	 *            the modify dto
	 */
	private void updateElementLookUp(final XmlProcessor processor, final QualityDataSetDTO modifyWithDTO, final QualityDataSetDTO modifyDTO) {
		
		logger.debug(" MeasureLibraryServiceImpl: updateElementLookUp Start :  ");
		String XPATH_EXPRESSION_ELEMENTLOOKUP = "/measure/elementLookUp/qdm[@uuid='" + modifyDTO.getUuid() + "']";// XPath
		// Expression
		// to
		// find
		// all
		// elementRefs in elementLookUp
		// for to be modified QDM.
		try {
			NodeList nodesElementLookUp = (NodeList) xPath.evaluate(XPATH_EXPRESSION_ELEMENTLOOKUP, processor.getOriginalDoc(),
					XPathConstants.NODESET);
			for (int i = 0; i < nodesElementLookUp.getLength(); i++) {
				Node newNode = nodesElementLookUp.item(i);
				newNode.getAttributes().getNamedItem("name").setNodeValue(modifyWithDTO.getCodeListName());
				newNode.getAttributes().getNamedItem("id").setNodeValue(modifyWithDTO.getId());
				if ((newNode.getAttributes().getNamedItem("codeSystemName") == null)
						&& (modifyWithDTO.getCodeSystemName() != null)) {
					Attr attrNode = processor.getOriginalDoc().createAttribute("codeSystemName");
					attrNode.setNodeValue(modifyWithDTO.getCodeSystemName());
					newNode.getAttributes().setNamedItem(attrNode);
				} else if ((newNode.getAttributes().getNamedItem("codeSystemName") != null)
						&& (modifyWithDTO.getCodeSystemName() == null)) {
					newNode.getAttributes().getNamedItem("codeSystemName").setNodeValue(null);
				} else if ((newNode.getAttributes().getNamedItem("codeSystemName") != null)
						&& (modifyWithDTO.getCodeSystemName() != null)) {
					newNode.getAttributes().getNamedItem("codeSystemName").setNodeValue(
							modifyWithDTO.getCodeSystemName());
				}
				newNode.getAttributes().getNamedItem("datatype").setNodeValue(modifyWithDTO.getDataType());
				newNode.getAttributes().getNamedItem("oid").setNodeValue(modifyWithDTO.getOid());
				newNode.getAttributes().getNamedItem("taxonomy").setNodeValue(modifyWithDTO.getTaxonomy());
				newNode.getAttributes().getNamedItem("version").setNodeValue(modifyWithDTO.getVersion());
				if (modifyWithDTO.isSuppDataElement()) {
					newNode.getAttributes().getNamedItem("suppDataElement").setNodeValue("true");
				} else {
					newNode.getAttributes().getNamedItem("suppDataElement").setNodeValue("false");
				}
				if (newNode.getAttributes().getNamedItem("instance") != null) {
					if (!StringUtils.isBlank(modifyWithDTO.getOccurrenceText())) {
						newNode.getAttributes().getNamedItem("instance").setNodeValue(
								modifyWithDTO.getOccurrenceText());
					} else {
						newNode.getAttributes().removeNamedItem("instance");
					}
				} else {
					if (!StringUtils.isEmpty(modifyWithDTO.getOccurrenceText())) {
						Attr instance = processor.getOriginalDoc().createAttribute("instance");
						instance.setNodeValue(modifyWithDTO.getOccurrenceText());
						newNode.getAttributes().setNamedItem(instance);
					}
				}
				if (newNode.getAttributes().getNamedItem("effectiveDate") != null) {
					if (!StringUtils.isBlank(modifyWithDTO.getEffectiveDate())) {
						newNode.getAttributes().getNamedItem("effectiveDate").setNodeValue(
								modifyWithDTO.getEffectiveDate());
					} else {
						newNode.getAttributes().removeNamedItem("effectiveDate");
					}
				} else {
					if (!StringUtils.isEmpty(modifyWithDTO.getEffectiveDate())) {
						Attr effectiveDateAttr = processor.getOriginalDoc().createAttribute("effectiveDate");
						effectiveDateAttr.setNodeValue(modifyWithDTO.getEffectiveDate());
						newNode.getAttributes().setNamedItem(effectiveDateAttr);
					}
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		logger.debug(" MeasureLibraryServiceImpl: updateElementLookUp End :  ");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.measure.service.MeasureService#updateLockedDate
	 * (java.lang.String, java.lang.String).This method has been added to update
	 * the measureLock Date. This method first gets the exisitingMeasure and
	 * then adds the lockedOutDate if it is not there.
	 */
	@Override
	public final SaveMeasureResult updateLockedDate(final String measureId, final String userId) {
		Measure existingmeasure = null;
		User user = null;
		SaveMeasureResult result = new SaveMeasureResult();
		if ((measureId != null) && (userId != null)) {
			existingmeasure = getService().getById(measureId);
			if (existingmeasure != null) {
				if (!isLocked(existingmeasure)) {
					user = getUserService().getById(userId);
					existingmeasure.setLockedUser(user);
					existingmeasure.setLockedOutDate(new Timestamp(new Date().getTime()));
					getService().save(existingmeasure);
					result.setSuccess(true);
				}
			}
		}
		
		result.setId(existingmeasure.getId());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#updateMeasureNotes(mat.DTO.MeasureNoteDTO, java.lang.String)
	 */
	@Override
	public final void updateMeasureNotes(final MeasureNoteDTO measureNoteDTO, final String userId) {
		try {
			MeasureNotesDAO measureNotesDAO = getMeasureNotesDAO();
			MeasureNotes measureNotes = measureNotesDAO.find(measureNoteDTO.getId());
			measureNotes.setNoteTitle(measureNoteDTO.getNoteTitle());
			measureNotes.setNoteDesc(measureNoteDTO.getNoteDesc());
			User user = getUserService().getById(userId);
			if (user != null) {
				measureNotes.setModifyUser(user);
			}
			measureNotes.setLastModifiedDate(new Date());
			getMeasureNotesService().saveMeasureNote(measureNotes);
			logger.info("Edited MeasureNotes Saved Successfully. Measure notes Id :: " + measureNoteDTO.getId());
		} catch (Exception e) {
			logger.info("Edited MeasureNotes not saved. Exception occured. Measure notes Id :: " + measureNoteDTO.getId());
		}
	}
	
	/**
	 * This method updates MeasureXML - ElementLookUpNode,ElementRef's under
	 * Population Node and Stratification Node, SupplementDataElements. It also
	 * removes attributes nodes if there is mismatch in data types of newly
	 * selected QDM and already applied QDM.
	 * 
	 * *
	 * 
	 * @param modifyWithDTO
	 *            the modify with dto
	 * @param modifyDTO
	 *            the modify dto
	 * @param measureId
	 *            the measure id
	 */
	@Override
	public final void updateMeasureXML(final QualityDataSetDTO modifyWithDTO, final QualityDataSetDTO modifyDTO, final String measureId) {
		logger.debug(" MeasureLibraryServiceImpl: updateMeasureXML Start : Measure Id :: " + measureId);
		MeasureXmlModel model = getMeasureXmlForMeasure(measureId);
		
		if (model != null) {
			XmlProcessor processor = new XmlProcessor(model.getXml());
			if (modifyDTO.isUsed()) {
				if (modifyDTO.getDataType().equalsIgnoreCase("Attribute")) {
					// update All Attributes.
					// updateAttributes(processor, modifyWithDTO, modifyDTO);
				} else {
					// Update all elementRef's in Populations and Stratification
					updatePopulationAndStratification(processor, modifyWithDTO, modifyDTO);
				}
				
				// update elementLookUp Tag
				updateElementLookUp(processor, modifyWithDTO, modifyDTO);
				updateSupplementalDataElement(processor, modifyWithDTO, modifyDTO);
				model.setXml(processor.transform(processor.getOriginalDoc()));
				getService().saveMeasureXml(model);
				
			} else {
				// update elementLookUp Tag
				updateElementLookUp(processor, modifyWithDTO, modifyDTO);
				model.setXml(processor.transform(processor.getOriginalDoc()));
				getService().saveMeasureXml(model);
			}
			
		}
		logger.debug(" MeasureLibraryServiceImpl: updateMeasureXML End : Measure Id :: " + measureId);
	}
	
	/**
	 * This method updates MeasureXML - ElementRef's under Population and
	 * Stratification Node
	 * 
	 * *.
	 * 
	 * @param processor
	 *            the processor
	 * @param modifyWithDTO
	 *            the modify with dto
	 * @param modifyDTO
	 *            the modify dto
	 */
	private void updatePopulationAndStratification(final XmlProcessor processor, final QualityDataSetDTO modifyWithDTO,
			final QualityDataSetDTO modifyDTO) {
		
		logger.debug(" MeasureLibraryServiceImpl: updatePopulationAndStratification Start :  ");
		String XPATH_EXPRESSION_CLAUSE_ELEMENTREF = "/measure//clause//elementRef[@id='" + modifyDTO.getUuid() + "']"; // XPath
		// to
		// find
		// All
		// elementRef's
		// under clause element nodes
		// for to be modified QDM.
		try {
			NodeList nodesClauseWorkSpace = (NodeList) xPath.evaluate(XPATH_EXPRESSION_CLAUSE_ELEMENTREF, processor.getOriginalDoc(),
					XPathConstants.NODESET);
			ArrayList<QDSAttributes> attr = (ArrayList<QDSAttributes>) getAllDataTypeAttributes(modifyWithDTO.getDataType());
			for (int i = 0; i < nodesClauseWorkSpace.getLength(); i++) {
				Node newNode = nodesClauseWorkSpace.item(i);
				String displayName = new String();
				if (!StringUtils.isBlank(modifyWithDTO.getOccurrenceText())) {
					displayName = displayName.concat(modifyWithDTO.getOccurrenceText() + " of ");
				}
				displayName = displayName.concat(modifyWithDTO.getCodeListName() + " : " + modifyWithDTO.getDataType());
				
				newNode.getAttributes().getNamedItem("displayName").setNodeValue(displayName);
				if (newNode.getChildNodes() != null) {
					NodeList childList = newNode.getChildNodes();
					for (int j = 0; j < childList.getLength(); j++) {
						Node childNode = childList.item(j);
						if (childNode.getAttributes().getNamedItem("qdmUUID") != null) {
							String childNodeAttrName = childNode.getAttributes().getNamedItem("name").getNodeValue();
							boolean isRemovable = true;
							for (QDSAttributes attributes : attr) {
								if (attributes.getName().equalsIgnoreCase(childNodeAttrName)) {
									isRemovable = false;
									break;
								}
							}
							if (isRemovable) {
								Node parentNode = childNode.getParentNode();
								parentNode.removeChild(childNode);
							}
						}
					}
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			
		}
		logger.debug(" MeasureLibraryServiceImpl: updatePopulationAndStratification End :  ");
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#updatePrivateColumnInMeasure(java.lang.String, boolean)
	 */
	@Override
	public final void updatePrivateColumnInMeasure(final String measureId, final boolean isPrivate) {
		getService().updatePrivateColumnInMeasure(measureId, isPrivate);
	}
	
	/** This method updates MeasureXML - ElementRef's under SupplementalDataElement Node.
	 * 
	 * @param processor the processor
	 * @param modifyWithDTO QualityDataSetDTO
	 * @param modifyDTO QualityDataSetDTO */
	private void updateSupplementalDataElement(final XmlProcessor processor, final QualityDataSetDTO modifyWithDTO,
			final QualityDataSetDTO modifyDTO) {
		
		logger.debug(" MeasureLibraryServiceImpl: updateSupplementalDataElement Start :  ");
		String XPATH_EXPRESSION_SDE_ELEMENTREF = "/measure/supplementalDataElements/elementRef[@id='" + modifyDTO.getUuid() + "']";// XPath
		// to
		// find
		// all
		// elementRefs
		// in
		// supplementalDataElements for
		// to be modified QDM.
		
		try {
			NodeList nodesSDE = (NodeList) xPath.evaluate(XPATH_EXPRESSION_SDE_ELEMENTREF, processor.getOriginalDoc(),
					XPathConstants.NODESET);
			for (int i = 0; i < nodesSDE.getLength(); i++) {
				Node newNode = nodesSDE.item(i);
				newNode.getAttributes().getNamedItem("name").setNodeValue(modifyWithDTO.getCodeListName());
			}
			
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		logger.debug(" MeasureLibraryServiceImpl: updateSupplementalDataElement End :  ");
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasureLibraryService#updateUsersShare(mat.client.measure.ManageMeasureShareModel)
	 */
	@Override
	public final void updateUsersShare(final ManageMeasureShareModel model) {
		getService().updateUsersShare(model);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.server.service.MeasureLibraryService#validateMeasureForExport(java.lang.String, java.util.ArrayList)
	 */
	@Override
	public final ValidateMeasureResult validateMeasureForExport(final String key, final List<MatValueSet> matValueSetList)
			throws MatException {
		try {
			return getService().validateMeasureForExport(key, matValueSetList);
		} catch (Exception exc) {
			logger.info("Exception validating export for " + key, exc);
			throw new MatException(exc.getMessage());
		}
	}
}
