package mat.server.service.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.UUID;
import mat.DTO.CodeSystemDTO;
import mat.DTO.DataTypeDTO;
import mat.DTO.HasListBoxDTO;
import mat.DTO.OperatorDTO;
import mat.DTO.UnitDTO;
import mat.client.codelist.HasListBox;
import mat.client.codelist.ManageCodeListDetailModel;
import mat.client.codelist.ManageValueSetSearchModel;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.dao.AuthorDAO;
import mat.dao.CategoryDAO;
import mat.dao.CodeDAO;
import mat.dao.CodeListAuditLogDAO;
import mat.dao.CodeListDAO;
import mat.dao.CodeSystemDAO;
import mat.dao.DataTypeDAO;
import mat.dao.ListObjectDAO;
import mat.dao.ListObjectLTDAO;
import mat.dao.MeasureScoreDAO;
import mat.dao.MeasureTypeDAO;
import mat.dao.ObjectStatusDAO;
import mat.dao.QualityDataSetDAO;
import mat.dao.StewardDAO;
import mat.dao.UnitDAO;
import mat.dao.UnitTypeDAO;
import mat.dao.UnitTypeMatrixDAO;
import mat.dao.UserDAO;
import mat.dao.clause.OperatorDAO;
import mat.model.Category;
import mat.model.Code;
import mat.model.CodeList;
import mat.model.CodeListSearchDTO;
import mat.model.CodeSystem;
import mat.model.DataType;
import mat.model.GroupedCodeList;
import mat.model.GroupedCodeListDTO;
import mat.model.ListObject;
import mat.model.ListObjectLT;
import mat.model.MatValueSet;
import mat.model.MatValueSetTransferObject;
import mat.model.MeasureSteward;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.User;
import mat.server.LoggedInUserUtil;
import mat.server.exception.ExcelParsingException;
import mat.server.service.CodeListNotUniqueException;
import mat.server.service.CodeListOidNotUniqueException;
import mat.server.service.CodeListService;
import mat.server.service.InvalidLastModifiedDateException;
import mat.server.service.ValueSetLastModifiedDateNotUniqueException;
import mat.server.util.ResourceLoader;
import mat.shared.ConstantMessages;
import mat.shared.DateStringValidator;
import mat.shared.DateUtility;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The Class ManageCodeListServiceImpl.
 */
public class ManageCodeListServiceImpl implements CodeListService {
	
	/** The Constant ASCII_END. */
	private static final int ASCII_END = 90;
	
	/** The Constant ASCII_START. */
	private static final int ASCII_START = 65;
	
	/** The Constant DEFAULT_PAGE_SIZE. */
	private static final int DEFAULT_PAGE_SIZE = 50;
	
	/** The Constant logger. */
	private static final Log logger = LogFactory
			.getLog(ManageCodeListServiceImpl.class);
	
	/** The author dao. */
	@Autowired
	private AuthorDAO authorDAO;
	
	/** The category dao. */
	@Autowired
	private CategoryDAO categoryDAO;
	
	/** The code dao. */
	@Autowired
	private CodeDAO codeDAO;
	
	/** The code list audit log dao. */
	@Autowired
	private CodeListAuditLogDAO codeListAuditLogDAO;
	
	/** The code list dao. */
	@Autowired
	private CodeListDAO codeListDAO;
	
	/** The code system dao. */
	@Autowired
	private CodeSystemDAO codeSystemDAO;
	
	/** The data type dao. */
	@Autowired
	private DataTypeDAO dataTypeDAO;
	
	/** The list object dao. */
	@Autowired
	private ListObjectDAO listObjectDAO;
	
	/** The list object ltdao. */
	@Autowired
	private ListObjectLTDAO listObjectLTDAO;
	
	/** The measure dao. */
	@Autowired
	private mat.dao.clause.MeasureDAO measureDAO;
	
	/** The measure score dao. */
	@Autowired
	private MeasureScoreDAO measureScoreDAO;
	
	/** The measure type dao. */
	@Autowired
	private MeasureTypeDAO measureTypeDAO;
	
	/** The object status dao. */
	@Autowired
	private ObjectStatusDAO objectStatusDAO;
	
	/** The operator dao. */
	@Autowired
	private OperatorDAO operatorDAO;
	
	/** The quality data set dao. */
	@Autowired
	private QualityDataSetDAO qualityDataSetDAO;
	
	/** The steward dao. */
	@Autowired
	private StewardDAO stewardDAO;
	
	/** The unit dao. */
	@Autowired
	private UnitDAO unitDAO;
	
	/** The unit type dao. */
	@Autowired
	private UnitTypeDAO unitTypeDAO;
	
	/** The unit type matrix dao. */
	@Autowired
	private UnitTypeMatrixDAO unitTypeMatrixDAO;
	
	/** The user dao. */
	@Autowired
	private UserDAO userDAO;
	
	/**
	 * Method to extract qdm element node created after marshalling of
	 * QualityDataModelWrapper object.
	 * @param qualityDataSetDTOWrapper - {@link QualityDataModelWrapper}.
	 * @return String - {@link String}.
	 * */
	private String addAppliedQDMInMeasureXML(
			final QualityDataModelWrapper qualityDataSetDTOWrapper) {
		logger.info("addAppliedQDMInMeasureXML Method Call Start.");
		ByteArrayOutputStream stream = createXML(qualityDataSetDTOWrapper);
		int startIndex = stream.toString().indexOf("<qdm", 0);
		int lastIndex = stream.toString().indexOf("/>", startIndex);
		String xmlString = stream.toString().substring(startIndex,
				lastIndex + 2);
		logger.debug("addAppliedQDMInMeasureXML Method Call xmlString :: "
				+ xmlString);
		return xmlString;
	}
	
	/**
	 * Check for duplicate code list.
	 * 
	 * @param currentcodeList
	 *            the currentcode list
	 * @param listObject
	 *            the list object
	 * @return true, if successful
	 */
	private boolean checkForDuplicateCodeList(CodeList currentcodeList,
			ListObject listObject) {
		boolean duplicateExists = false;
		for (GroupedCodeList gcl : listObject.getCodesLists()) {
			String existingOID = gcl.getCodeList().getOid();
			String currentOID = currentcodeList.getOid();
			if ((existingOID != null) && (currentOID != null)
					&& existingOID.trim().equalsIgnoreCase(currentOID.trim())) {
				duplicateExists = true;
				logger.info("Trying to add a CodeList with same name and Taxonomy which was already added in the Grouped CodeList.");
				return duplicateExists;
			}
		}
		return duplicateExists;
	}
	/**
	 * Check for duplicates.
	 * @param matValueSetTransferObject
	 *            the mat Value Set Transfer Object
	 * @param isVSACValueSet
	 *            the is vsac value set
	 * @return true, if successful
	 */
	private boolean checkForDuplicates(MatValueSetTransferObject matValueSetTransferObject, boolean isVSACValueSet) {
		logger.info(" checkForDuplicates Method Call Start.");
		boolean isQDSExist = false;
		DataType dt = dataTypeDAO.find(matValueSetTransferObject.getDatatype());
		String qdmCompareNameOrID = "";
		if (isVSACValueSet) {
			qdmCompareNameOrID = matValueSetTransferObject.getMatValueSet().getID();
		} else {
			qdmCompareNameOrID = matValueSetTransferObject.getCodeListSearchDTO().getName();
		}
		List<QualityDataSetDTO> existingQDSList = matValueSetTransferObject.getAppliedQDMList();
		for (QualityDataSetDTO dataSetDTO : existingQDSList) {
			//For "Element without VSAC value set", duplicates should not be checked in
			// elements with VSAC value set in applied QDM list.
			if (!isVSACValueSet && !dataSetDTO.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
				continue;
			}
			String codeListNameOrOID = "";
			if (isVSACValueSet) {
				codeListNameOrOID = dataSetDTO.getOid();
			} else {
				codeListNameOrOID = dataSetDTO.getCodeListName();
			}
			if (dt.getDescription().equalsIgnoreCase(dataSetDTO.getDataType())
					&& (codeListNameOrOID.equalsIgnoreCase(qdmCompareNameOrID))
					&& StringUtils.isBlank(dataSetDTO.getOccurrenceText())) {
				// if the same dataType exists and the occurrenceText is also
				// null
				// then there is a any occurrence exists for that dataType.
				isQDSExist = true;
				break;
			}
		}
		logger.info("checkForDuplicates Method Call End.Check resulted in :"
				+ isQDSExist);
		return isQDSExist;
	}
	/*
	 * This method is used to find the number of occurrences that exist for the
	 * given measure ,codeList and datType. null can be passed to
	 * getQDSElementsFor()...since the whole purpose of this measure is to find
	 * how many occurrence exists for the given dataType not for the specific
	 * occurrence.
	 */
	/**
	 * Check for occurrence count vsac api.
	 * @param dataTypeId
	 *            the data type id
	 * @param matValueSet
	 *            the mat value set
	 * @param appliedQDM
	 *            the applied qdm
	 * @return the int
	 */
	private int checkForOccurrenceCountVsacApi(final String dataTypeId,
			final MatValueSet matValueSet,
			final ArrayList<QualityDataSetDTO> appliedQDM) {
		int occurrenceCount = ASCII_START;
		ListIterator<QualityDataSetDTO> qdsIterator = appliedQDM.listIterator();
		DataType dt = null;
		dt = dataTypeDAO.find(dataTypeId);
		while (qdsIterator.hasNext()) {
			QualityDataSetDTO dto = qdsIterator.next();
			if (matValueSet.getID().equalsIgnoreCase(dto.getOid())) {
				if (dt.getDescription().equalsIgnoreCase(dto.getDataType())
						&& (dto.getOccurrenceText() != null) && StringUtils.isNotEmpty(dto.getOccurrenceText())
						&& StringUtils.isNotBlank(dto.getOccurrenceText())) {
					String nextOccString = dto.getOccurrenceText();
					Character text = nextOccString.charAt(nextOccString
							.length() - 1);
					int newOcc = (text);
					if (newOcc >= occurrenceCount) {
						occurrenceCount = ++newOcc;
					}
				}
			}
		}
		return occurrenceCount;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#countSearchResultsWithFilter(java.lang.String, boolean, int)
	 */
	@Override
	public int countSearchResultsWithFilter(final String searchText,
			final boolean defaultCodeList, final int filter) {
		String loggedInUserid = LoggedInUserUtil.getLoggedInUser();
		return listObjectLTDAO.countSearchResultsWithFilter(searchText,
				loggedInUserid, defaultCodeList, filter);
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#createClone(java.lang.String)
	 */
	@Override
	public ManageValueSetSearchModel createClone(String id) {
		ManageValueSetSearchModel model = new ManageValueSetSearchModel();
		ListObject lo = listObjectDAO.find(id);
		ListObject clone = lo.clone();
		User user = getLoggedInUser();
		clone.setOid(listObjectDAO.generateUniqueOid(user));
		String name = listObjectDAO.generateUniqueName(clone.getName(), user);
		if (name == null) {
			return null;
		}
		clone.setName(name);
		// non-clone specific alterations, we want these fields to have these
		// values
		clone.setDraft(true);
		clone.setLastModified(null);
		clone.setObjectOwner(user);
		listObjectDAO.save(clone);
		ManageValueSetSearchModel.Result result = new ManageValueSetSearchModel.Result();
		result.setId(clone.getId());
		result.setGrouped(clone.getCodeSystem().getDescription()
				.equalsIgnoreCase("Grouping"));
		model.getData().add(result);
		model.setResultsTotal(1);
		return model;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#createDraft(java.lang.String, java.lang.String)
	 */
	@Override
	public ManageValueSetSearchModel createDraft(String id, String oid) {
		ManageValueSetSearchModel model = new ManageValueSetSearchModel();
		if (!listObjectDAO.hasDraft(oid)) {
			ListObject lo = listObjectDAO.find(id);
			ListObject clone = lo.clone();
			// non-clone specific alterations, we want these fields to have
			// these values
			clone.setDraft(true);
			clone.setLastModified(null);
			// if(clone.getCodeSystemVersion().equalsIgnoreCase("Grouping")){
			listObjectDAO.save(clone);
			// }else{
			// codeListDAO.save(clone);
			// }
			qualityDataSetDAO.updateListObjectId(id, clone.getId());
			model.setResultsTotal(1);
		}
		return model;
	}
	/**
	 * Method to create XML from QualityDataModelWrapper object.
	 * @param qualityDataSetDTO - {@link QualityDataModelWrapper}.
	 * @return {@link ByteArrayOutputStream}.
	 * */
	private ByteArrayOutputStream createXML(
			final QualityDataModelWrapper qualityDataSetDTO) {
		logger.info("In ManageCodeLiseServiceImpl.createXml()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader()
			.getResourceAsURL("QualityDataModelMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(
					stream));
			marshaller.setMapping(mapping);
			marshaller.marshal(qualityDataSetDTO);
			logger.debug("Marshalling of QualityDataSetDTO is successful.."
					+ stream.toString());
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
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#deleteCodes(java.lang.String, java.util.List)
	 */
	@Override
	public ManageCodeListDetailModel deleteCodes(final String key,
			final List<Code> codes) {
		if (codes != null) {
			codeDAO.deleteCodes(codes);
		}
		ManageCodeListDetailModel detailModel = getCodeList(key);
		return detailModel;
	}
	/**
	 * Filter code lists for current category.
	 * @param listObject
	 *            the list object
	 */
	public void filterCodeListsForCurrentCategory(ListObject listObject) {
		if (listObject.getCodesLists() != null) {
			List<GroupedCodeList> toBeRemoved = new ArrayList<GroupedCodeList>();
			for (GroupedCodeList cl : listObject.getCodesLists()) {
				if (!cl.getCodeList().getCategory().getId()
						.equals(listObject.getCategory().getId())) {
					toBeRemoved.add(cl);
				}
			}
			listObject.getCodesLists().removeAll(toBeRemoved);
		}
	}
	/**
	 * Find page count for code lists.
	 * @param codeLists
	 *            the code lists
	 * @return the int
	 */
	private int findPageCountForCodeLists(List<GroupedCodeListDTO> codeLists) {
		int totalRows = codeLists.size();
		int numberOfRowsPerPage = DEFAULT_PAGE_SIZE;
		int mod = totalRows % numberOfRowsPerPage;
		int pageCount = totalRows / numberOfRowsPerPage;
		pageCount = ((mod > 0) ? (pageCount + 1) : pageCount);
		return pageCount;
	}
	/**
	 * Find page count for codes.
	 * @param codes
	 *            the codes
	 * @return the int
	 */
	private int findPageCountForCodes(List<Code> codes) {
		int totalRows = codes.size();
		int numberOfRowsPerPage = DEFAULT_PAGE_SIZE;
		int mod = totalRows % numberOfRowsPerPage;
		int pageCount = totalRows / numberOfRowsPerPage;
		pageCount = (mod > 0) ? (pageCount + 1) : pageCount;
		return pageCount;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#generateUniqueOid(mat.client.codelist.ManageCodeListDetailModel)
	 */
	@Override
	public String generateUniqueOid(ManageCodeListDetailModel currentDetails) {
		User user = (currentDetails != null) && (currentDetails.getID() != null) ? listObjectDAO
				.find(currentDetails.getID()).getObjectOwner()
				: getLoggedInUser();
				String oid = listObjectDAO.generateUniqueOid(user);
				return oid;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#getAllDataTypes()
	 */
	@Override
	public List<? extends HasListBox> getAllDataTypes() {
		List<DataTypeDTO> retList = new ArrayList<DataTypeDTO>();
		List<DataType> dt = dataTypeDAO.findAllDataType();
		for (DataType dataType : dt) {
			if (!dataType.getCategoryId().equalsIgnoreCase("22")) { // Filter
				// Timing
				// Element
				// Category
				// as this will be added by default to measure at create time.
				DataTypeDTO dto = new DataTypeDTO();
				dto.setDescription(dataType.getDescription());
				dto.setId(dataType.getId());
				retList.add(dto);
			}
		}
		return retList;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#getAllOperators()
	 */
	@Override
	public List<OperatorDTO> getAllOperators() {
		return operatorDAO.getAllOperators();
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#getAllUnits()
	 */
	@Override
	public List<UnitDTO> getAllUnits() {
		List<UnitDTO> data = unitDAO.getAllUnits();
		return data;
	}
	/**
	 * Gets the and validate last modified date.
	 * @param lastModifiedStr
	 *            the last modified str
	 * @return the and validate last modified date
	 * @throws InvalidLastModifiedDateException
	 *             the invalid last modified date exception
	 */
	private Timestamp getAndValidateLastModifiedDate(String lastModifiedStr)
			throws InvalidLastModifiedDateException {
		Timestamp ts = null;
		DateStringValidator dsv = new DateStringValidator();
		int validationCode = dsv.isValidDateString(lastModifiedStr);
		if (validationCode != DateStringValidator.VALID) {
			throw new InvalidLastModifiedDateException();
		}
		try {
			Date lastModifiedDt = DateUtility
					.convertStringToDate(lastModifiedStr);
			ts = new Timestamp(lastModifiedDt.getTime());
		} catch (Exception e) {
			throw new InvalidLastModifiedDateException();
		}
		// cannot be a future date
		if (System.currentTimeMillis() < ts.getTime()) {
			throw new InvalidLastModifiedDateException();
		}
		// else do not care
		return ts;
	}
	/**
	 * Gets the category.
	 * @param value
	 *            the value
	 * @return the category
	 */
	private Category getCategory(String value) {
		Category category = categoryDAO.find(value);
		return category;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#getCodeList(java.lang.String)
	 */
	@Override
	public ManageCodeListDetailModel getCodeList(String key) {
		ManageCodeListDetailModel codeListModel = new ManageCodeListDetailModel();
		CodeList codeList = codeListDAO.find(key);
		setCodeListonModel(codeList, codeListModel);
		return codeListModel;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#getCodeListsForCategory(java.lang.String)
	 */
	@Override
	public List<? extends HasListBox> getCodeListsForCategory(String categoryId) {
		List<HasListBoxDTO> retList = new ArrayList<HasListBoxDTO>();
		if ((categoryId != null) && !"".equals(categoryId)) {
			List<CodeList> searchResults = new ArrayList<CodeList>();
			searchResults = codeListDAO.getValueSetsForCategory(categoryId);
			for (CodeList searchResult : searchResults) {
				HasListBoxDTO dto = new HasListBoxDTO();
				dto.setId(searchResult.getId());
				dto.setDescription(searchResult.getName());
				dto.setOid(searchResult.getOid());
				retList.add(dto);
			}
		}
		return retList;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#getCodes(java.lang.String, int, int)
	 */
	@Override
	public List<Code> getCodes(String codeListId, int startIndex, int pageSize) {
		return codeDAO.searchCodes(codeListId, startIndex, pageSize);
	}
	/**
	 * Gets the code system.
	 * @param value
	 *            the value
	 * @return the code system
	 */
	private CodeSystem getCodeSystem(String value) {
		CodeSystem codeSystem = codeSystemDAO.find(value);
		return codeSystem;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#getCodeSystemsForCategory(java.lang.String)
	 */
	@Override
	public List<? extends HasListBox> getCodeSystemsForCategory(
			String categoryId) {
		List<CodeSystemDTO> retList = new ArrayList<CodeSystemDTO>();
		if ((categoryId != null) && !"".equals(categoryId)) {
			Category category = categoryDAO.find(categoryId);
			for (CodeSystem codeSys : category.getCodeSystems()) {
				String codeSysDesc = codeSys.getDescription();
				// TODO:- don't need to check for grouping code System. Since in
				// another user story it is been removed.
				// US 424. As per new Appendix LOINC should be displayed
				// US 594. The Supplimental Value Sets Code System should not be
				// included in the codeSystem dropDown.
				if (!ConstantMessages.GROUPING_CODE_SYSTEM
						.equalsIgnoreCase(codeSysDesc)
						&& !ConstantMessages.HL7_ADMINGENDER_CODE_SYSTEM
						.equalsIgnoreCase(codeSysDesc)
						&& !ConstantMessages.CDC_CODE_SYSTEM
						.equalsIgnoreCase(codeSysDesc)
						&& !ConstantMessages.SOURCE_OF_PAYMENT
						.equalsIgnoreCase(codeSysDesc)) {
					CodeSystemDTO dto = new CodeSystemDTO();
					dto.setDescription(codeSys.getDescription());
					dto.setId(codeSys.getId());
					retList.add(dto);
				}
			}
		}
		return retList;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#getGroupedCodeList(java.lang.String)
	 */
	@Override
	public ManageCodeListDetailModel getGroupedCodeList(String key) {
		ManageCodeListDetailModel codeListModel = new ManageCodeListDetailModel();
		ListObject codeList = listObjectDAO.find(key);
		setListObjectOnModel(codeList, codeListModel);
		return codeListModel;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#getGroupedCodeListCodeSystemsForCategory(java.lang.String)
	 */
	@Override
	public String getGroupedCodeListCodeSystemsForCategory(String categoryId) {
		if ((categoryId != null) && !"".equals(categoryId)) {
			Category category = categoryDAO.find(categoryId);
			for (CodeSystem codeSys : category.getCodeSystems()) {
				if ("Grouping".equals(codeSys.getDescription())) {
					return codeSys.getId();
				}
			}
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#getListBoxData()
	 */
	@Override
	public mat.client.codelist.service.CodeListService.ListBoxData getListBoxData() {
		mat.client.codelist.service.CodeListService.ListBoxData data =
				new mat.client.codelist.service.CodeListService.ListBoxData();
		data.setCategoryList(categoryDAO.getAllCategories());
		data.setCodeSystemList(codeSystemDAO.getAllCodeSystem());
		data.setStatusList(objectStatusDAO.getAllObjectStatus());
		data.setMeasureStewardList(stewardDAO.getAllStewardOrg());
		data.setAuthorsList(authorDAO.getAllAuthors());
		data.setMeasureTypeList(measureTypeDAO.getAllMeasureTypes());
		// US 421
		data.setScoringList(measureScoreDAO.getAllMeasureScores());
		// US 62
		data.setUnitList(unitDAO.getAllUnits());
		data.setUnitTypeList(unitTypeDAO.getAllUnitTypes());
		data.setUnitTypeMatrixList(unitTypeMatrixDAO.getAllUnitMatrix());
		// US 171
		data.setLogicalOperatorList(operatorDAO.getLogicalOperators());
		data.setRelTimingOperatorList(operatorDAO.getRelTimingperators());
		data.setRelAssocOperatorList(operatorDAO.getRelAssociationsOperators());
		return data;
	}
	/**
	 * Gets the logged in user.
	 * @return the logged in user
	 */
	private User getLoggedInUser() {
		String userId = LoggedInUserUtil.getLoggedInUser();
		User user = userDAO.find(userId);
		return user;
	}
	/**
	 * Gets the only filtered codes.
	 * @param pageSize
	 *            the page size
	 * @param codes
	 *            the codes
	 * @return the only filtered codes
	 */
	private ArrayList<Code> getOnlyFilteredCodes(int pageSize,
			ArrayList<Code> codes) {
		ArrayList<Code> codesList = new ArrayList<Code>();
		int counter = 1;
		for (Code code : codes) {
			if (counter > pageSize) {
				break;
			} else {
				counter++;
				codesList.add(code);
			}
		}
		return codesList;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#getQDSDataTypeForCategory(java.lang.String)
	 */
	@Override
	public List<? extends HasListBox> getQDSDataTypeForCategory(
			String categoryId) {
		List<DataTypeDTO> retList = new ArrayList<DataTypeDTO>();
		if ((categoryId != null) && !"".equals(categoryId)) {
			Category category = categoryDAO.find(categoryId);
			for (DataType dataType : category.getDataTypes()) {
				DataTypeDTO dto = new DataTypeDTO();
				dto.setDescription(dataType.getDescription());
				dto.setId(dataType.getId());
				retList.add(dto);
			}
		}
		return retList;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#getQDSElements(java.lang.String, java.lang.String)
	 */
	@Override
	public java.util.List<QualityDataSetDTO> getQDSElements(final String measureId,
			final String version) {
		return qualityDataSetDAO.getQDSElements(false, measureId);
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#getRelAssociationsOperators()
	 */
	@Override
	public List<OperatorDTO> getRelAssociationsOperators() {
		return operatorDAO.getRelAssociationsOperators();
	}
	/**
	 * Gets the steward.
	 * @param value
	 *            the value
	 * @return the steward
	 */
	private MeasureSteward getSteward(String value) {
		MeasureSteward stewardOrg = stewardDAO.find(value);
		return stewardOrg;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#getSupplimentalCodeList()
	 */
	@Override
	public List<ListObject> getSupplimentalCodeList() {
		List<ListObject> listOfSuppElements = listObjectDAO
				.getSupplimentalCodeList();
		return listOfSuppElements;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#getTimingOperators()
	 */
	@Override
	public List<OperatorDTO> getTimingOperators() {
		return operatorDAO.getRelTimingperators();
	}
	/**
	 * Insert default code list.
	 * @param createdUser
	 *            the created user
	 * @param codeListName
	 *            the code list name
	 */
	private void insertdefaultCodeList(final User createdUser, final String codeListName) {
		CodeList codeList = new CodeList();
		String oid = generateUniqueOid(null);
		codeList.setOid(oid);
		ListObject defaultObject = codeList;
		defaultObject.setName(codeListName);
		Category defaultCategory = new Category();
		defaultCategory.setId("22");
		defaultObject.setCategory(defaultCategory);
		defaultObject.setObjectOwner(createdUser);
		CodeSystem defaultCodeSystem = new CodeSystem();
		defaultCodeSystem.setId("60");
		defaultObject.setCodeSystem(defaultCodeSystem);
		MeasureSteward steward = new MeasureSteward();
		steward.setOrgName(createdUser.getOrganizationName());
		steward.setId("80");
		defaultObject.setSteward(steward);
		defaultObject.setDraft(true);
		// Should we have a default lastModified timestamp?
		defaultObject.setCodeSystemVersion("1");
		defaultObject.setRationale("N/A");
		listObjectDAO.save(defaultObject);
	}
	/**
	 * Insert ready to use code list.
	 * @param createdUser
	 *            the created user
	 * @param codeListName
	 *            the code list name
	 */
	private void insertReadyToUseCodeList(User createdUser, String codeListName) {
		CodeList codeList = new CodeList();
		if (codeListName
				.equalsIgnoreCase(ConstantMessages.ADMINISTRATIVE_GENDER_MALE)) {
			codeList.setOid(ConstantMessages.GENDER_MALE_OID);
		} else if (codeListName
				.equalsIgnoreCase(ConstantMessages.ADMINISTRATIVE_GENDER_FEMALE)) {
			codeList.setOid(ConstantMessages.GENDER_FEMALE_OID);
		} else if (codeListName
				.equalsIgnoreCase(ConstantMessages.ADMINISTRATIVE_GENDER_UNDIFFERENTIATED)) {
			codeList.setOid(ConstantMessages.GENDER_UNDIFFERENTIATED_OID);
		} else {
			codeList.setOid(ConstantMessages.BIRTH_DATE_OID);
		}
		ListObject defaultObject = codeList;
		defaultObject.setName(codeListName);
		Category defaultCategory = new Category();
		defaultCategory.setId("9");
		defaultObject.setCategory(defaultCategory);
		defaultObject.setObjectOwner(createdUser);
		CodeSystem defaultCodeSystem = new CodeSystem();
		if (codeListName.equalsIgnoreCase(ConstantMessages.BIRTH_DATE)) {
			defaultCodeSystem.setId("94");// LOINC
			defaultObject.setCodeSystemVersion("2.36");
		} else {
			defaultCodeSystem.setId("93");// HL7 Administrative Gender
			defaultObject.setCodeSystemVersion("v3");
		}
		defaultObject.setCodeSystem(defaultCodeSystem);
		MeasureSteward steward = new MeasureSteward();
		steward.setOrgName(createdUser.getOrganizationName());
		steward.setId("80");
		defaultObject.setSteward(steward);
		defaultObject.setDraft(true);
		// Should we have a default lastModified timestamp?
		defaultObject.setRationale("N/A");
		listObjectDAO.save(defaultObject);
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#isCodeAlreadyExists(java.lang.String, mat.model.Code)
	 */
	@Override
	public boolean isCodeAlreadyExists(String codeListId, Code code) {
		CodeList codeList = codeListDAO.find(codeListId);
		ArrayList<Code> existingCodes = new ArrayList<Code>(codeList.getCodes());
		boolean codeExists = false;
		for (Code existingCode : existingCodes) {
			if (existingCode.getCode().equalsIgnoreCase(code.getCode())) {
				codeExists = true;
			}
		}
		return codeExists;
	}
	/**
	 * Checks if is code list last modified date unique.
	 * @param currentDetails
	 *            the current details
	 * @param ts
	 *            the ts
	 * @return true, if is code list last modified date unique
	 */
	boolean isCodeListLastModifiedDateUnique(
			ManageCodeListDetailModel currentDetails, Timestamp ts) {
		List<CodeList> cls = codeListDAO.getCodeList(currentDetails, ts);
		return cls.isEmpty();
	}
	// US 413. Added Steward Other parameter
	/**
	 * Checks if is code list unique.
	 * @param currentDetails
	 *            the current details
	 * @return true, if is code list unique
	 */
	public boolean isCodeListUnique(ManageCodeListDetailModel currentDetails) {
		boolean unique = true;
		// US 413 added parameter steward other
		// check for an already existing code with that info and a different oid
		CodeList existingCodeList = codeListDAO.getCodeList(currentDetails,
				LoggedInUserUtil.getLoggedInUser());
		if (existingCodeList != null) {
			if (!currentDetails.isExistingCodeList()
					|| !existingCodeList.getId().equals(currentDetails.getID())) {
				unique = false;
			}
		}
		return unique;
	}
	/**
	 * Checks if is grouped code list last modified date unique.
	 * @param currentDetails
	 *            the current details
	 * @param ts
	 *            the ts
	 * @return true, if is grouped code list last modified date unique
	 */
	boolean isGroupedCodeListLastModifiedDateUnique(
			ManageCodeListDetailModel currentDetails, Timestamp ts) {
		List<ListObject> los = listObjectDAO.getListObject(currentDetails, ts);
		return los.isEmpty();
	}
	// US 413. Added Steward Other parameter
	/**
	 * Checks if is grouped code list unique.
	 * @param currentDetails
	 *            the current details
	 * @return true, if is grouped code list unique
	 */
	public boolean isGroupedCodeListUnique(
			ManageCodeListDetailModel currentDetails) {
		boolean unique = true;
		ListObject existingListObject = listObjectDAO.getListObject(
				currentDetails, LoggedInUserUtil.getLoggedInUser());
		if (existingListObject != null) {
			if ((currentDetails.getID() == null)
					|| !existingListObject.getId().equals(
							currentDetails.getID())) {
				unique = false;
			}
		}
		return unique;
	}
	// US 551. check if last modified date changed for event logging purpose.
	/**
	 * Checks if is last modified date change.
	 * @param oldDate
	 *            the old date
	 * @param newDate
	 *            the new date
	 * @return true, if is last modified date change
	 */
	private boolean isLastModifiedDateChange(String oldDate, String newDate) {
		if (StringUtils.isNotBlank(oldDate) && StringUtils.isNotBlank(newDate)) {
			if (!oldDate.equalsIgnoreCase(newDate)) {
				return true;
			}
		}
		return false;
	}
	// US 551. check if name changed for event logging purpose.
	/**
	 * Checks if is name change.
	 * @param oldName
	 *            the old name
	 * @param newName
	 *            the new name
	 * @return true, if is name change
	 */
	private boolean isNameChange(String oldName, String newName) {
		if (StringUtils.isNotBlank(oldName) && StringUtils.isNotBlank(newName)) {
			if (!oldName.equalsIgnoreCase(newName)) {
				return true;
			}
		}
		return false;
	}
	// US 383. check if oid changed for event logging purpose.
	/**
	 * Checks if is oid change.
	 * @param oldOID
	 *            the old oid
	 * @param newOID
	 *            the new oid
	 * @return true, if is oid change
	 */
	private boolean isOidChange(String oldOID, String newOID) {
		if (StringUtils.isNotBlank(oldOID) && StringUtils.isNotBlank(newOID)) {
			if (!oldOID.equalsIgnoreCase(newOID)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Log complete event.
	 * @param codeList
	 *            the code list
	 */
	private void logCompleteEvent(CodeList codeList) {
		if ((codeList.getCodeSystem() != null)
				&& (codeList.getCodeSystem().getDescription() != null)
				&& codeList
				.getCodeSystem()
				.getDescription()
				.equalsIgnoreCase(ConstantMessages.GROUPED_CODE_LIST_CS)) {
			codeListAuditLogDAO.recordCodeListEvent(codeList,
					"Group Value Set Saved", null);
		} else {
			codeListAuditLogDAO.recordCodeListEvent(codeList,
					"Value Set Saved", null);
		}
	}
	/**
	 * Log group complete event.
	 * @param codeList
	 *            the code list
	 */
	private void logGroupCompleteEvent(ListObject codeList) {
		if ((codeList.getCodeSystem() != null)
				&& (codeList.getCodeSystem().getDescription() != null)
				&& codeList
				.getCodeSystem()
				.getDescription()
				.equalsIgnoreCase(ConstantMessages.GROUPED_CODE_LIST_CS)) {
			codeListAuditLogDAO.recordCodeListEvent(codeList,
					"Group Value Set Saved", null);
		} else {
			codeListAuditLogDAO.recordCodeListEvent(codeList,
					"Value Set Saved", null);
		}
	}
	/**
	 * Modify applied element list.
	 * @param dataSetDTO
	 *            the data set dto
	 * @param appliedQDM
	 *            the applied qdm
	 * @return the quality data model wrapper
	 */
	private QualityDataModelWrapper modifyAppliedElementList(
			final QualityDataSetDTO dataSetDTO,
			final ArrayList<QualityDataSetDTO> appliedQDM) {
		QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
		Iterator<QualityDataSetDTO> iterator = appliedQDM.iterator();
		while (iterator.hasNext()) {
			QualityDataSetDTO qualityDataSetDTO = iterator.next();
			if (qualityDataSetDTO.getUuid().equals(dataSetDTO.getUuid())) {
				iterator.remove();
				break;
			}
		}
		appliedQDM.add(dataSetDTO);
		wrapper.setQualityDataDTO(appliedQDM);
		return wrapper;
	}
	/**
	 * Oid already exists.
	 * @param oid
	 *            the oid
	 * @param id
	 *            the id
	 * @return the boolean
	 */
	public Boolean oidAlreadyExists(String oid, String id) {
		if ((oid == null) || (oid.length() == 0)) {
			return false;
		}
		return listObjectDAO.countListObjectsByOidAndNotId(oid, id) > 0;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#saveDefaultCodeList(mat.model.User)
	 */
	@Override
	public SaveUpdateCodeListResult saveDefaultCodeList(final User createdUser)
			throws CodeListNotUniqueException {
		SaveUpdateCodeListResult result = new SaveUpdateCodeListResult();
		insertdefaultCodeList(createdUser, ConstantMessages.MEASUREMENT_PERIOD);
		insertdefaultCodeList(createdUser,
				ConstantMessages.MEASUREMENT_START_DATE);
		insertdefaultCodeList(createdUser,
				ConstantMessages.MEASUREMENT_END_DATE);
		insertReadyToUseCodeList(createdUser,
				ConstantMessages.ADMINISTRATIVE_GENDER_MALE);
		insertReadyToUseCodeList(createdUser,
				ConstantMessages.ADMINISTRATIVE_GENDER_FEMALE);
		insertReadyToUseCodeList(createdUser,
				ConstantMessages.ADMINISTRATIVE_GENDER_UNDIFFERENTIATED);
		insertReadyToUseCodeList(createdUser, ConstantMessages.BIRTH_DATE);
		result.setSuccess(true);
		return result;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#saveorUpdateCodeList(mat.client.codelist.ManageCodeListDetailModel)
	 */
	@Override
	public SaveUpdateCodeListResult saveorUpdateCodeList(
			ManageCodeListDetailModel currentDetails)
					throws CodeListNotUniqueException, CodeListOidNotUniqueException,
					ExcelParsingException, InvalidLastModifiedDateException,
					ValueSetLastModifiedDateNotUniqueException {
		String oldOID = null;
		String newOID = null;
		String oldName = null;
		String newName = null;
		String oldDate = null;
		String newDate = null;
		SaveUpdateCodeListResult result = new SaveUpdateCodeListResult();
		Code newCode = null;
		int duplicateCount = 0;
		List<Code> newCodesList = currentDetails.getCodes();
		if (!isCodeListUnique(currentDetails)) {
			throw new CodeListNotUniqueException();
		} else if (oidAlreadyExists(currentDetails.getOid(),
				currentDetails.getID())) {
			throw new CodeListOidNotUniqueException();
		}
		CodeList codeList = null;
		if (currentDetails.isExistingCodeList()) {
			codeList = codeListDAO.find(currentDetails.getID());
			Set<Code> existingCodes = codeList.getCodes();
			if (currentDetails.getCode() != null) {
				newCode = currentDetails.getCode();
				existingCodes.add(newCode);
				codeList.setCodes(existingCodes);
			} else if ((newCodesList != null) && !newCodesList.isEmpty()
					&& currentDetails.isImportFlag()) { // do this only when we
				// are importing
				for (Code code : newCodesList) {
					if (!existingCodes.add(code)) {
						++duplicateCount;
						result.setDuplicateExists(true);
						result.setCodeListName(codeList.getName());
					}
				}
				codeList.setCodes(existingCodes);
			}
		} else {
			codeList = new CodeList();
			String oid = currentDetails.getOid() != null ? currentDetails
					.getOid() : generateUniqueOid(currentDetails);
					codeList.setOid(oid);
					codeList.setDraft(true);
		}
		Timestamp ts = null;
		// first time completing the code list
		if (codeList.isDraft() && !currentDetails.isDraft()) {
			ts = DateUtility.getCurrentDateWithMinutePrecision();
			result.setLastModifiedDate(DateUtility.convertDateToString(ts));
			logCompleteEvent(codeList);
			// code list has already been completed
		} else if (!codeList.isDraft() && !currentDetails.isDraft()) {
			String lastModifiedStr = currentDetails.getLastModifiedDate();
			ts = getAndValidateLastModifiedDate(lastModifiedStr);
			if (!isCodeListLastModifiedDateUnique(currentDetails, ts)) {
				throw new ValueSetLastModifiedDateNotUniqueException();
			}
		}
		// else do not care
		// US 383
		oldOID = codeList.getOid();
		newOID = currentDetails.getOid();
		// US 551
		oldName = codeList.getName();
		newName = currentDetails.getName();
		oldDate = DateUtility.convertDateToString(codeList.getLastModified());
		newDate = DateUtility.convertDateToString(ts);
		codeList.setLastModified(ts);
		codeList.setDraft(currentDetails.isDraft());
		if (!currentDetails.isImportFlag()) {
			setModelFieldsOnCodeList(currentDetails, codeList); // do this only
			// when the user
			// is not
			// uploading sets
			// of codes
		}
		codeListDAO.save(codeList);
		result.setSuccess(true);
		result.setId(codeList.getId());
		result.setCodeListName(codeList.getName());
		if (newCode != null) {
			result.setCodeId(newCode.getId());
		}
		result.setDuplicateCount(duplicateCount);
		if (newCodesList.size() == duplicateCount) {
			result.setAllCodesDups(true);
		}
		if (currentDetails.isExistingCodeList()) { // Do set the model to the
			// result only if it is
			// existing CodeList.
			// US 600 adding the codeListDetailModel as part of the result, to
			// update the model on the client-side to reflect the addition of
			// code changes.
			result.setCodeListDetailModel(getCodeList(currentDetails.getID()));
		}
		// US 383. log OID change event if any
		if (isOidChange(oldOID, newOID)) {
			String logMessage = "OID changed from " + oldOID + " to " + newOID;
			codeListAuditLogDAO.recordCodeListEvent(codeList,
					"Value Set OID Updated", logMessage);
			listObjectDAO.updateFamilyOid(oldOID, newOID);
		}
		// US 551. log Name change event if any
		if (isNameChange(oldName, newName)) {
			String logNameMessage = "Name changed from " + oldName + " to "
					+ newName;
			codeListAuditLogDAO.recordCodeListEvent(codeList,
					"Value Set Name Updated", logNameMessage);
		}
		// US 551. log Last modified date change event if any
		if (isLastModifiedDateChange(oldDate, newDate)) {
			String logDateMessage = "Last Modified Date/Time changed from "
					+ oldDate + " to " + newDate;
			codeListAuditLogDAO.recordCodeListEvent(codeList,
					"Value Set Last Modified Date Updated", logDateMessage);
		}
		return result;
	}
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#saveorUpdateGroupedCodeList(mat.client.codelist.ManageCodeListDetailModel)
	 */
	@Override
	public SaveUpdateCodeListResult saveorUpdateGroupedCodeList(
			ManageCodeListDetailModel currentDetails)
					throws CodeListNotUniqueException, CodeListOidNotUniqueException,
					InvalidLastModifiedDateException,
					ValueSetLastModifiedDateNotUniqueException {
		String oldOID = null;
		String newOID = null;
		String oldName = null;
		String newName = null;
		String oldDate = null;
		String newDate = null;
		SaveUpdateCodeListResult result = new SaveUpdateCodeListResult();
		if (!isGroupedCodeListUnique(currentDetails)) {
			throw new CodeListNotUniqueException();
		} else if (oidAlreadyExists(currentDetails.getOid(),
				currentDetails.getID())) {
			throw new CodeListOidNotUniqueException();
		}
		ListObject codeList = new ListObject();
		if (currentDetails.getID() != null) {
			codeList = listObjectDAO.find(currentDetails.getID());
		} else {
			String oid = currentDetails.getOid() != null ? currentDetails
					.getOid() : generateUniqueOid(currentDetails);
					currentDetails.setOid(oid);
					codeList.setDraft(true);
		}
		Timestamp ts = null;
		// first time completing the code list
		if (codeList.isDraft() && !currentDetails.isDraft()) {
			ts = DateUtility.getCurrentDateWithMinutePrecision();
			result.setLastModifiedDate(DateUtility.convertDateToString(ts));
			logGroupCompleteEvent(codeList);
			// code list has already been completed
		} else if (!codeList.isDraft() && !currentDetails.isDraft()) {
			String lastModifiedStr = currentDetails.getLastModifiedDate();
			ts = getAndValidateLastModifiedDate(lastModifiedStr);
			if (!isGroupedCodeListLastModifiedDateUnique(currentDetails, ts)) {
				// if(!isCodeListLastModifiedDateUnique(currentDetails, ts)){
				throw new ValueSetLastModifiedDateNotUniqueException();
			}
		}
		// else do not care
		// US 383
		oldOID = codeList.getOid();
		newOID = currentDetails.getOid();
		// US 551
		oldName = codeList.getName();
		newName = currentDetails.getName();
		oldDate = DateUtility.convertDateToString(codeList.getLastModified());
		newDate = DateUtility.convertDateToString(ts);
		codeList.setLastModified(ts);
		codeList.setDraft(currentDetails.isDraft());
		setModelFieldsOnListObject(currentDetails, codeList);
		filterCodeListsForCurrentCategory(codeList);
		if (codeList.getCodeSystem() == null) {
			if ((codeList.getCategory() != null)
					&& (codeList.getCategory().getCodeSystems() != null)) {
				for (CodeSystem cs : codeList.getCategory().getCodeSystems()) {
					if (cs.getDescription().equals(
							ConstantMessages.GROUPED_CODE_LIST_CS)) {
						codeList.setCodeSystem(cs);
					}
				}
			}
			codeList.setCodeSystemVersion(ConstantMessages.GROUPED_CODE_LIST_CS);
		}
		listObjectDAO.save(codeList);
		result.setSuccess(true);
		result.setId(codeList.getId());
		if (currentDetails.isExistingCodeList()) { // Do set the model to the
			// result only if it is
			// existing CodeList.
			// US 600 adding the codeListDetailModel as part of the result, to
			// update the model on the client-side to reflect the addition of
			// code changes.
			result.setCodeListDetailModel(getGroupedCodeList(currentDetails
					.getID()));
		}
		// US 383. log OID change event if any
		if (isOidChange(oldOID, newOID)) {
			String logMessage = "OID changed from " + oldOID + " to " + newOID;
			codeListAuditLogDAO.recordCodeListEvent(codeList,
					"Value Set OID Updated", logMessage);
			listObjectDAO.updateFamilyOid(oldOID, newOID);
		}
		// US 551. log Name change event if any
		if (isNameChange(oldName, newName)) {
			String logNameMessage = "Name changed from " + oldName + " to "
					+ newName;
			codeListAuditLogDAO.recordCodeListEvent(codeList,
					"Value Set Name Updated", logNameMessage);
		}
		// US 551. log Last modified date change event if any
		if (isLastModifiedDateChange(oldDate, newDate)) {
			String logDateMessage = "Last Modified Date/Time changed from "
					+ oldDate + " to " + newDate;
			codeListAuditLogDAO.recordCodeListEvent(codeList,
					"Value Set Last Modified Date Updated", logDateMessage);
		}
		return result;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.server.service.CodeListService#saveQDStoMeasure(mat.model.
	 * MatValueSetTransferObject)
	 */
	@Override
	public final SaveUpdateCodeListResult saveQDStoMeasure(
			MatValueSetTransferObject valueSetTransferObject) {
		SaveUpdateCodeListResult result = new SaveUpdateCodeListResult();
		QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
		ArrayList<QualityDataSetDTO> qdsList = new ArrayList<QualityDataSetDTO>();
		wrapper.setQualityDataDTO(qdsList);
		QualityDataSetDTO qds = new QualityDataSetDTO();
		String dataType = valueSetTransferObject.getDatatype();
		if (dataType != null) {
			DataType dt = dataTypeDAO.find(dataType);
			qds.setDataType(dt.getDescription());
		}
		MatValueSet matValueSet = valueSetTransferObject.getMatValueSet();
		qds.setOid(matValueSet.getID());
		qds.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		qds.setCodeListName(matValueSet.getDisplayName());
		if (matValueSet.isGrouping()) {
			qds.setTaxonomy(ConstantMessages.GROUPING_CODE_SYSTEM);
		} else {
			qds.setTaxonomy(matValueSet.getCodeSystemName());
		}
		qds.setUuid(UUID.randomUUID().toString());
		if (valueSetTransferObject.isVersionDate() || valueSetTransferObject.isEffectiveDate()) {
			qds.setVersion(valueSetTransferObject.getMatValueSet().getVersion());
		} else {
			qds.setVersion("1.0");
		}
		if (valueSetTransferObject.isEffectiveDate()) {
			qds.setEffectiveDate(valueSetTransferObject.getMatValueSet().getRevisionDate());
		}
		if (valueSetTransferObject.isSpecificOccurrence()) {
			int occurrenceCount = checkForOccurrenceCountVsacApi(dataType,
					matValueSet, (ArrayList<QualityDataSetDTO>) valueSetTransferObject.getAppliedQDMList());
			if (occurrenceCount < ASCII_END) { // Alphabet ASCII Integer Values.
				char occTxt = (char) occurrenceCount;
				qds.setOccurrenceText("Occurrence" + " " + occTxt);
				wrapper.getQualityDataDTO().add(qds);
				result.setOccurrenceMessage(qds.getOccurrenceText());
				String qdmXMLString = addAppliedQDMInMeasureXML(wrapper);
				result.setSuccess(true);
				result.setXmlString(qdmXMLString);
			}
		} else { // Treat as regular QDM
			if (!checkForDuplicates(valueSetTransferObject, true)) {
				wrapper.getQualityDataDTO().add(qds);
				result.setOccurrenceMessage(qds.getOccurrenceText());
				String qdmXMLString = addAppliedQDMInMeasureXML(wrapper);
				result.setSuccess(true);
				result.setXmlString(qdmXMLString);
			} else {
				result.setSuccess(false);
				result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
			}
		}
		return result;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.server.service.CodeListService#saveUserDefinedQDStoMeasure
	 * (java.lang.String, java.lang.String, java.lang.String,
	 * java.util.ArrayList)
	 */
	@Override
	public SaveUpdateCodeListResult saveUserDefinedQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject) {
		SaveUpdateCodeListResult result = new SaveUpdateCodeListResult();
		QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
		ArrayList<QualityDataSetDTO> qdsList = new ArrayList<QualityDataSetDTO>();
		List<QualityDataSetDTO> existingQDSList = matValueSetTransferObject.getAppliedQDMList();
		String dataType = matValueSetTransferObject.getDatatype();
		DataType dt = dataTypeDAO.find(dataType);
		boolean isQDSExist = false;
		for (QualityDataSetDTO dataSetDTO : existingQDSList) {
			if (dataSetDTO.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
				if (dt.getDescription().equalsIgnoreCase(dataSetDTO.getDataType())
						&& (dataSetDTO.getCodeListName().equalsIgnoreCase(
								matValueSetTransferObject.getUserDefinedText()))
								&& (dataSetDTO.getOccurrenceText() == null)) {
					// if the same dataType exists and the occurrenceText is also
					// null
					// then there is a any occurrence exists for that dataType.
					isQDSExist = true;
					break;
				}
			}
		}
		if (!isQDSExist) {
			wrapper.setQualityDataDTO(qdsList);
			QualityDataSetDTO qds = new QualityDataSetDTO();
			qds.setDataType(dt.getDescription());
			qds.setOid(ConstantMessages.USER_DEFINED_QDM_OID);
			qds.setId(UUID.randomUUID().toString());
			qds.setCodeListName(matValueSetTransferObject.getUserDefinedText());
			qds.setTaxonomy(ConstantMessages.USER_DEFINED_QDM_NAME);
			qds.setUuid(UUID.randomUUID().toString());
			qds.setVersion("1.0");
			wrapper.getQualityDataDTO().add(qds);
			String qdmXMLString = addAppliedQDMInMeasureXML(wrapper);
			result.setSuccess(true);
			result.setXmlString(qdmXMLString);
		} else {
			result.setSuccess(true);
			result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
		}
		return result;
	}
	/* US566 */
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#search(java.lang.String, int, int, java.lang.String, boolean, boolean, int)
	 */
	@Override
	public List<CodeListSearchDTO> search(String searchText, int startIndex,
			int pageSize, String sortColumn, boolean isAsc,
			boolean defaultCodeList, int filter) {
		String loggedInUserid = LoggedInUserUtil.getLoggedInUser();
		List<CodeListSearchDTO> codeList = listObjectDAO.searchWithFilter(
				searchText, loggedInUserid, startIndex - 1, pageSize,
				sortColumn, isAsc, defaultCodeList, filter);
		return codeList;
	}
	/*
	 * (non-Javadoc)
	 * @see mat.server.service.CodeListService#search(java.lang.String, int,
	 * int, java.lang.String, boolean, boolean, int, java.lang.String)
	 */
	@Override
	public List<CodeListSearchDTO> search(String searchText, int startIndex,
			int pageSize, String sortColumn, boolean isAsc,
			boolean defaultCodeList, int filter, String categoryId) {
		
		String loggedInUserid = LoggedInUserUtil.getLoggedInUser();
		List<CodeListSearchDTO> codeList = listObjectDAO.searchWithFilter(
				searchText, loggedInUserid, startIndex - 1, pageSize,
				sortColumn, isAsc, defaultCodeList, filter);
		
		List<CodeListSearchDTO> retList = new ArrayList<CodeListSearchDTO>();
		
		for (CodeListSearchDTO dto : codeList) {
			if (dto.isGroupedCodeList()) {
				continue;
			}
			if (!dto.getCategoryCode().equalsIgnoreCase(categoryId)) {
				continue;
			}
			retList.add(dto);
		}
		
		return retList;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#searchValueSetsForDraft(int, int)
	 */
	@Override
	public ManageValueSetSearchModel searchValueSetsForDraft(int startIndex,
			int pageSize) {
		List<ListObjectLT> los = listObjectLTDAO.getListObjectsToDraft();
		ManageValueSetSearchModel model = new ManageValueSetSearchModel();
		List<ManageValueSetSearchModel.Result> data = new ArrayList<ManageValueSetSearchModel.Result>();
		
		for (ListObjectLT lo : los) {
			ManageValueSetSearchModel.Result result = new ManageValueSetSearchModel.Result();
			result.setDraft(true);
			result.setLastModified(lo.getLastModified());
			result.setOid(lo.getOid());
			result.setName(lo.getName());
			result.setId(lo.getId());
			data.add(result);
		}
		model.setResultsTotal(data.size());
		model.setData(data);
		return model;
	}
	
	/**
	 * Sets the code liston model.
	 * 
	 * @param codeList
	 *            the code list
	 * @param codeListModel
	 *            the code list model
	 */
	private void setCodeListonModel(CodeList codeList,
			ManageCodeListDetailModel codeListModel) {
		// Always by default the model will have only 50 codes.
		setListObjectOnModel(codeList, codeListModel);
		if (codeList.getCodes() != null) {
			ArrayList<Code> codes = new ArrayList<Code>(codeList.getCodes());
			Collections.sort(codes, new Code.Comparator());
			int numberofPagesForCodes = findPageCountForCodes(codes);
			codeListModel.setTotalCodes(codes.size());
			codeListModel.setCodesPageCount(numberofPagesForCodes);
			ArrayList<Code> filteredCodes = getOnlyFilteredCodes(
					DEFAULT_PAGE_SIZE, codes);
			codeListModel.setCodes(filteredCodes);
		}
	}
	
	/**
	 * Sets the dto values from model.
	 * 
	 * @param cl
	 *            the cl
	 * @param dto
	 *            the dto
	 */
	void setDTOValuesFromModel(final ListObject cl, final CodeListSearchDTO dto) {
		dto.setCategoryCode(cl.getCategory().getId());
		dto.setCategoryDisplay(cl.getCategory().getDescription());
		dto.setAbbreviatedCategory(cl.getCategory().getAbbreviation());
		dto.setAbbreviatedCodeSystem(cl.getCodeSystem().getDescription());
		dto.setId(cl.getId());
		dto.setName(cl.getName());
		dto.setOid(cl.getOid());
		dto.setSteward(cl.getSteward().getOrgName());
		dto.setCodeSystem(cl.getCodeSystem().getDescription());
		dto.setGroupedCodeList(true);
		if (cl.isDraft()) {
			dto.setLastModified("Draft");
		} else {
			dto.setLastModified(DateUtility.convertDateToString(cl
					.getLastModified()));
		}
	}
	
	/**
	 * Sets the list object on model.
	 * 
	 * @param codeList
	 *            the code list
	 * @param codeListModel
	 *            the code list model
	 */
	private void setListObjectOnModel(ListObject codeList,
			ManageCodeListDetailModel codeListModel) {
		codeListModel.setCategory(codeList.getCategory().getId());
		codeListModel.setComments(codeList.getComment());
		codeListModel.setID(codeList.getId());
		codeListModel.setName(codeList.getName());
		codeListModel.setRationale(codeList.getRationale());
		codeListModel.setSteward(codeList.getSteward().getId());
		
		// US 413
		codeListModel.setStewardOther(codeList.getStewardOther());
		
		codeListModel.setExistingCodeList(true);
		List<GroupedCodeListDTO> codeLists = new ArrayList<GroupedCodeListDTO>();
		codeListModel.setCodeSystem(codeList.getCodeSystem().getId());
		codeListModel.setCodeSystemVersion(codeList.getCodeSystemVersion());
		codeListModel.setOid(codeList.getOid());
		for (GroupedCodeList cl : codeList.getCodesLists()) {
			GroupedCodeListDTO dto = new GroupedCodeListDTO();
			dto.setId(cl.getCodeList().getId());
			dto.setName(cl.getCodeList().getName());
			dto.setDescription(cl.getDescription());
			dto.setCodeSystem(cl.getCodeList().getCodeSystem().getId());
			dto.setOid(cl.getCodeList().getOid());
			codeLists.add(dto);
		}
		codeListModel.setCodeLists(codeLists);
		codeListModel.setTotalCodeList(codeLists.size());
		codeListModel
		.setCodeListsPageCount(findPageCountForCodeLists(codeLists));
		/* US537 */
		codeListModel.setDraft(codeList.isDraft());
		codeListModel.setLastModifiedDate(DateUtility
				.convertDateToString(codeList.getLastModified()));
		// is my value set
		String userId = LoggedInUserUtil.getLoggedInUser();
		if (userId != null) {
			boolean isMyValueSet = listObjectDAO.isMyValueSet(codeList.getId(),
					userId);
			codeListModel.setMyValueSet(isMyValueSet);
		}
		
	}
	
	/**
	 * Sets the model fields on code list.
	 * 
	 * @param model
	 *            the model
	 * @param codeList
	 *            the code list
	 */
	private void setModelFieldsOnCodeList(ManageCodeListDetailModel model,
			CodeList codeList) {
		setModelFieldsOnListObject(model, codeList);
		codeList.setCodeSystem(getCodeSystem(model.getCodeSystem()));
		codeList.setCodeSystemVersion(model.getCodeSystemVersion());
	}
	
	/**
	 * Sets the model fields on list object.
	 * 
	 * @param model
	 *            the model
	 * @param listObject
	 *            the list object
	 */
	private void setModelFieldsOnListObject(ManageCodeListDetailModel model,
			ListObject listObject) {
		if (StringUtils.isNotBlank(model.getMeasureId())) {
			listObject.setMeasureId(model.getMeasureId());
		}
		listObject.setName(model.getName());
		listObject.setCategory(getCategory(model.getCategory()));
		listObject.setComment(model.getComments());
		listObject.setRationale(model.getRationale());
		listObject.setSteward(getSteward(model.getSteward()));
		
		// US 413
		listObject.setStewardOther(model.getStewardOther());
		
		if (listObject.getObjectOwner() == null) {
			listObject.setObjectOwner(getLoggedInUser());
		}
		listObject.setOid(model.getOid());
		
		if (model.getAddValueSetsMap().size() > 0) {
			for (GroupedCodeListDTO dto : model.getAddValueSetsMap().values()) {
				CodeList codeList = codeListDAO.find(dto.getId());
				boolean dupExist = checkForDuplicateCodeList(codeList,
						listObject);
				if (!dupExist) {// This is to prevent from server-side
					GroupedCodeList gcl = new GroupedCodeList();
					gcl.setCodeList(codeList);
					gcl.setDescription(dto.getDescription());
					logger.info("adding " + gcl.getCodeList().getName());
					listObject.getCodesLists().add(gcl);
				}
			}
		} else if (model.getRemoveValueSetsMap().size() > 0) {
			HashSet<GroupedCodeList> toRemove = new HashSet<GroupedCodeList>();
			for (GroupedCodeList gcl : listObject.getCodesLists()) {
				GroupedCodeListDTO dto = model.getRemoveValueSetsMap().get(
						gcl.getCodeList().getId());
				if (dto != null) {
					toRemove.add(gcl);
				}
			}
			for (GroupedCodeList gcl : toRemove) {
				logger.info("deleting " + gcl.getCodeList().getName());
				listObject.getCodesLists().remove(gcl);
			}
		}
		
	}
	
	/**
	 * Sort quality data set list.
	 * 
	 * @param finalList
	 *            the final list
	 * @return the array list
	 */
	private List<QualityDataSetDTO> sortQualityDataSetList(
			final List<QualityDataSetDTO> finalList) {
		
		Collections.sort(finalList, new Comparator<QualityDataSetDTO>() {
			@Override
			public int compare(final QualityDataSetDTO o1, final QualityDataSetDTO o2) {
				return o1.getCodeListName().compareToIgnoreCase(
						o2.getCodeListName());
			}
		});
		
		return finalList;
		
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.CodeListService#transferOwnerShipToUser(java.util.List, java.lang.String)
	 */
	@Override
	public void transferOwnerShipToUser(List<String> list, String toEmail) {
		
		User userTo = userDAO.findByEmail(toEmail);
		
		for (int i = 0; i < list.size(); i++) {
			ListObject codeList = listObjectDAO.find(list.get(i));
			List<ListObject> allCodes = listObjectDAO
					.getListObject(codeList.getOid());
			for (int j = 0; j < allCodes.size(); j++) {
				String additionalInfo = "Value Set Owner transferred from "
						+ allCodes.get(j).getObjectOwner().getEmailAddress()
						+ " to " + toEmail;
				allCodes.get(j).setObjectOwner(userTo);
				listObjectDAO.save(allCodes.get(j));
				codeListAuditLogDAO.recordCodeListEvent(allCodes.get(j),
						"Value Set Ownership Changed ", additionalInfo);
				additionalInfo = "";
			}
			
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see mat.server.service.CodeListService#updateQDStoMeasure(mat.model.
	 * MatValueSetTransferObject)
	 */
	@Override
	public final SaveUpdateCodeListResult updateQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject) {
		SaveUpdateCodeListResult result = null;
		if (matValueSetTransferObject.getMatValueSet() != null) {
			result = updateVSACValueSetInElementLookUp(matValueSetTransferObject);
		} else if (matValueSetTransferObject.getCodeListSearchDTO() != null) {
			result = updateUserDefineQDMInElementLookUp(matValueSetTransferObject);
		}
		return result;
	}
	/**
	 * Update user define qdm in element look up.
	 * @param matValueSetTransferObject
	 *            - mat Value Set Transfer Object
	 * @return SaveUpdateCodeListResult
	 */
	private SaveUpdateCodeListResult updateUserDefineQDMInElementLookUp(MatValueSetTransferObject matValueSetTransferObject) {
		QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
		SaveUpdateCodeListResult result = new SaveUpdateCodeListResult();
		if (!checkForDuplicates(matValueSetTransferObject, false)) {
			ArrayList<QualityDataSetDTO> qdsList = new ArrayList<QualityDataSetDTO>();
			wrapper.setQualityDataDTO(qdsList);
			QualityDataSetDTO qds = matValueSetTransferObject.getQualityDataSetDTO();
			DataType dt = dataTypeDAO.find(matValueSetTransferObject.getDatatype());
			qds.setDataType(dt.getDescription());
			qds.setOid(ConstantMessages.USER_DEFINED_QDM_OID);
			qds.setId(UUID.randomUUID().toString());
			qds.setCodeListName(matValueSetTransferObject.getCodeListSearchDTO().getName());
			qds.setTaxonomy(ConstantMessages.USER_DEFINED_QDM_NAME);
			qds.setOccurrenceText(null);
			qds.setVersion("1.0");
			wrapper = modifyAppliedElementList(qds, (ArrayList<QualityDataSetDTO>)
					matValueSetTransferObject.getAppliedQDMList());
			String qdmXMLString = addAppliedQDMInMeasureXML(wrapper);
			result.setSuccess(true);
			result.setAppliedQDMList(sortQualityDataSetList(wrapper
					.getQualityDataDTO()));
			result.setXmlString(qdmXMLString);
			result.setDataSetDTO(qds);
		} else {
			result.setSuccess(true);
			result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
		}
		return result;
	}
	/**
	 * Update vsac value set in element look up.
	 * @param matValueSetTransferObject
	 *            - mat Value Set Transfer Object
	 * @return SaveUpdateCodeListResult
	 */
	private SaveUpdateCodeListResult updateVSACValueSetInElementLookUp(MatValueSetTransferObject matValueSetTransferObject
			) {
		SaveUpdateCodeListResult result = new SaveUpdateCodeListResult();
		if (matValueSetTransferObject.isSpecificOccurrence()) {
			QualityDataSetDTO qds = matValueSetTransferObject.getQualityDataSetDTO();
			String dataType = matValueSetTransferObject.getDatatype();
			MatValueSet matValueSet = matValueSetTransferObject.getMatValueSet();
			qds.setOid(matValueSet.getID());
			qds.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			qds.setCodeListName(matValueSet.getDisplayName());
			if (matValueSet.isGrouping()) {
				qds.setTaxonomy(ConstantMessages.GROUPING_CODE_SYSTEM);
			} else {
				qds.setTaxonomy(matValueSet.getCodeSystemName());
			}
			if (matValueSetTransferObject.isVersionDate() || matValueSetTransferObject.isEffectiveDate()) {
				qds.setVersion(matValueSetTransferObject.getMatValueSet().getVersion());
			} else {
				qds.setVersion("1.0");
			}
			if (matValueSetTransferObject.isEffectiveDate()) {
				qds.setEffectiveDate(matValueSetTransferObject.getMatValueSet().getRevisionDate());
			} else {
				qds.setEffectiveDate(null);
			}
			int occurrenceCount = checkForOccurrenceCountVsacApi(dataType,
					matValueSet, (ArrayList<QualityDataSetDTO>) matValueSetTransferObject.getAppliedQDMList());
			if (occurrenceCount < ASCII_END) { // Alphabet ASCII Integer Values.
				char occTxt = (char) occurrenceCount;
				qds.setOccurrenceText("Occurrence" + " " + occTxt);
				qds.setSpecificOccurrence(true);
				if (dataType != null) {
					DataType dt = dataTypeDAO.find(dataType);
					qds.setDataType(dt.getDescription());
				}
				QualityDataModelWrapper wrapper = modifyAppliedElementList(qds,
						(ArrayList<QualityDataSetDTO>) matValueSetTransferObject.getAppliedQDMList());
				result.setOccurrenceMessage(qds.getOccurrenceText());
				result.setSuccess(true);
				result.setAppliedQDMList(sortQualityDataSetList(wrapper
						.getQualityDataDTO()));
				result.setDataSetDTO(qds);
			}
		} else { // Treat as regular QDM
			List<QualityDataSetDTO> origAppliedQDMList = matValueSetTransferObject.getAppliedQDMList();
			List<QualityDataSetDTO> tempAppliedQDMList = new ArrayList<QualityDataSetDTO>();
			tempAppliedQDMList.addAll(matValueSetTransferObject.getAppliedQDMList());
			//Removing the QDS that is being modified from the tempAppliedQDMList.
			Iterator<QualityDataSetDTO> iterator = tempAppliedQDMList.iterator();
			while (iterator.hasNext()) {
				QualityDataSetDTO qualityDataSetDTO = iterator.next();
				if (qualityDataSetDTO.getUuid().equals(matValueSetTransferObject.getQualityDataSetDTO().getUuid())) {
					iterator.remove();
					break;
				}
			}
			matValueSetTransferObject.setAppliedQDMList(tempAppliedQDMList);
			
			if (!checkForDuplicates(matValueSetTransferObject, true)) {
				matValueSetTransferObject.setAppliedQDMList(origAppliedQDMList);
				
				QualityDataSetDTO qds = matValueSetTransferObject.getQualityDataSetDTO();
				String dataType = matValueSetTransferObject.getDatatype();
				if (dataType != null) {
					DataType dt = dataTypeDAO.find(dataType);
					qds.setDataType(dt.getDescription());
				}
				MatValueSet matValueSet = matValueSetTransferObject.getMatValueSet();
				qds.setOid(matValueSet.getID());
				qds.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				qds.setCodeListName(matValueSet.getDisplayName());
				if (matValueSet.isGrouping()) {
					qds.setTaxonomy(ConstantMessages.GROUPING_CODE_SYSTEM);
				} else {
					qds.setTaxonomy(matValueSet.getCodeSystemName());
				}
				if (matValueSetTransferObject.isVersionDate() || matValueSetTransferObject.isEffectiveDate()) {
					qds.setVersion(matValueSetTransferObject.getMatValueSet().getVersion());
				} else {
					qds.setVersion("1.0");
				}
				if (matValueSetTransferObject.isEffectiveDate()) {
					qds.setEffectiveDate(matValueSetTransferObject.getMatValueSet().getRevisionDate());
				} else {
					qds.setEffectiveDate(null);
				}
				qds.setOccurrenceText(null);
				QualityDataModelWrapper wrapper = modifyAppliedElementList(qds,
						(ArrayList<QualityDataSetDTO>) matValueSetTransferObject.getAppliedQDMList());
				result.setOccurrenceMessage(qds.getOccurrenceText());
				result.setSuccess(true);
				result.setAppliedQDMList(sortQualityDataSetList(wrapper
						.getQualityDataDTO()));
				result.setDataSetDTO(qds);
			} else {
				matValueSetTransferObject.setAppliedQDMList(origAppliedQDMList);
				
				result.setSuccess(true);
				result.setFailureReason(SaveUpdateCodeListResult.ALREADY_EXISTS);
			}
		}
		return result;
	}
}
