package mat.server;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.emory.mathcs.backport.java.util.Collections;
import mat.DTO.OperatorDTO;
import mat.DTO.UnitDTO;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.codelist.AdminManageCodeListSearchModel;
import mat.client.codelist.HasListBox;
import mat.client.codelist.ManageCodeListDetailModel;
import mat.client.codelist.ManageCodeListSearchModel;
import mat.client.codelist.ManageValueSetSearchModel;
import mat.client.codelist.TransferOwnerShipModel;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.dao.DataTypeDAO;
import mat.model.Code;
import mat.model.DataType;
import mat.model.GroupedCodeListDTO;
import mat.model.MatValueSetTransferObject;
import mat.model.QualityDataSetDTO;
import mat.model.User;
import mat.server.exception.ExcelParsingException;
import mat.server.service.CodeListNotUniqueException;
import mat.server.service.CodeListOidNotUniqueException;
import mat.server.service.CodeListService;
import mat.server.service.InvalidLastModifiedDateException;
import mat.server.service.UserService;
import mat.server.service.ValueSetLastModifiedDateNotUniqueException;
import mat.shared.ConstantMessages;
import mat.shared.ListObjectModelValidator;

/**
 * The Class CodeListServiceImpl.
 */
@SuppressWarnings("serial")
public class CodeListServiceImpl extends SpringRemoteServiceServlet
implements mat.client.codelist.service.CodeListService {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(CodeListServiceImpl.class);
	
	//US193
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#createClone(java.lang.String)
	 */
	@Override
	public ManageValueSetSearchModel createClone(String id) {
		CodeListService cls = getCodeListService();
		ManageValueSetSearchModel model = cls.createClone(id);
		return model;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#createDraft(java.lang.String, java.lang.String)
	 */
	@Override
	public ManageValueSetSearchModel createDraft(String id, String oid) {
		CodeListService cls = getCodeListService();
		ManageValueSetSearchModel model = cls.createDraft(id, oid);
		return model;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#deleteCodes(java.lang.String, java.util.List)
	 */
	@Override
	public ManageCodeListDetailModel deleteCodes(String codeListID,
			List<Code> Codes) {
		return  getCodeListService().deleteCodes(codeListID, Codes);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#generateUniqueOid(mat.client.codelist.ManageCodeListDetailModel)
	 */
	@Override
	public String generateUniqueOid(ManageCodeListDetailModel currentDetails) {
		return getCodeListService().generateUniqueOid(currentDetails);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getAllDataTypes()
	 */
	@Override
	public List<? extends HasListBox> getAllDataTypes() {
		List<? extends HasListBox> ret = getCodeListService().getAllDataTypes();
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getAllOperators()
	 */
	@Override
	public List<OperatorDTO> getAllOperators() {
		return getCodeListService().getAllOperators();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getAllUnits()
	 */
	@Override
	public List<String> getAllUnits() {
		
		logger.info("getAllUnits");
		List<String> units = new ArrayList<String>();
		List<UnitDTO> data =  getCodeListService().getAllUnits();
		for(int i=0;i<data.size();i++){
			units.add(data.get(i).getUnit());
			
		}
		return units;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getCodeList(java.lang.String)
	 */
	@Override
	public ManageCodeListDetailModel getCodeList(String key) {
		ManageCodeListDetailModel model = getCodeListService().getCodeList(key);
		return model;
	}
	
	/**
	 * Gets the code list service.
	 * 
	 * @return the code list service
	 */
	public CodeListService getCodeListService() {
		return (CodeListService)context.getBean("codeListService");
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getCodeListsForCategory(java.lang.String)
	 */
	@Override
	public List<? extends HasListBox> getCodeListsForCategory(String category) {
		return getCodeListService().getCodeListsForCategory(category);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getCodes(java.lang.String, int, int)
	 */
	@Override
	public List<Code> getCodes(String codeListId, int startIndex,int pageSize) {
		return getCodeListService().getCodes(codeListId, startIndex, pageSize);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getCodeSystemsForCategory(java.lang.String)
	 */
	@Override
	public List<? extends HasListBox> getCodeSystemsForCategory(String category) {
		return getCodeListService().getCodeSystemsForCategory(category);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getGroupedCodeList(java.lang.String)
	 */
	@Override
	public ManageCodeListDetailModel getGroupedCodeList(String key) {
		ManageCodeListDetailModel model = getCodeListService().getGroupedCodeList(key);
		return model;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getGroupedCodeList(java.lang.String, int, int)
	 */
	@Override
	public ManageCodeListDetailModel getGroupedCodeList(String key,
			int startIndex, int pageSize) {
		ManageCodeListDetailModel mm = getCodeListService().getGroupedCodeList(key);
		List<GroupedCodeListDTO> setOfCodeList = mm.getCodeLists();
		Collections.sort(setOfCodeList,new GroupedCodeListDTO.Comparator());
		List<GroupedCodeListDTO> filteredCodeList = new ArrayList<GroupedCodeListDTO>();
		if(setOfCodeList.size() > pageSize){
			filteredCodeList = getOnlyFilteredCodes(pageSize,setOfCodeList,startIndex);
			mm.setCodeLists(filteredCodeList);
		}
		return mm;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getListBoxData()
	 */
	@Override
	public mat.client.codelist.service.CodeListService.ListBoxData getListBoxData() {
		
		logger.info("getListBoxData");
		mat.client.codelist.service.CodeListService.ListBoxData data =
				new mat.client.codelist.service.CodeListService.ListBoxData();
		data = getCodeListService().getListBoxData();
		return data;
	}
	
	/**
	 * Gets the only filtered codes.
	 * 
	 * @param pageSize
	 *            the page size
	 * @param codes
	 *            the codes
	 * @param startIndex
	 *            the start index
	 * @return the only filtered codes
	 */
	private ArrayList<GroupedCodeListDTO> getOnlyFilteredCodes(int pageSize, List<GroupedCodeListDTO> codes,int startIndex){
		ArrayList<GroupedCodeListDTO> codesList = new ArrayList<GroupedCodeListDTO>();
		int counter = 1;
		for(int i = startIndex;i<codes.size(); i++){
			if(counter > pageSize){
				break;
			}else{
				counter++;
				codesList.add(codes.get(i));
			}
		}
		return codesList;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getQDSDataTypeForCategory(java.lang.String)
	 */
	@Override
	public List<? extends HasListBox> getQDSDataTypeForCategory(String category) {
		return getCodeListService().getQDSDataTypeForCategory(category);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getQDSElements(java.lang.String, java.lang.String)
	 */
	@Override
	public List<QualityDataSetDTO> getQDSElements(String measureId,
			String version) {
		List<QualityDataSetDTO> qdsElements = getCodeListService().getQDSElements(measureId, version);
		List<QualityDataSetDTO> filteredQDSElements = new ArrayList<QualityDataSetDTO>();
		for(QualityDataSetDTO dataSet : qdsElements) {
			if((dataSet.getOid() != null) && !dataSet.getOid().equals(ConstantMessages.GENDER_OID)
					&& !dataSet.getOid().equals(ConstantMessages.RACE_OID) && !dataSet.getOid().equals(ConstantMessages.ETHNICITY_OID)
					&& !dataSet.getOid().equals(ConstantMessages.PAYER_OID)){
				filteredQDSElements.add(dataSet);
			} else {
				System.out.println();
			}
			
		}
		Collections.sort(filteredQDSElements, new Comparator<QualityDataSetDTO>() {
			@Override
			public int compare(QualityDataSetDTO o1, QualityDataSetDTO o2) {
				return o1.getCodeListName().compareToIgnoreCase(o2.getCodeListName());
			}
		});
		return filteredQDSElements;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getRelAssociationsOperators()
	 */
	@Override
	public Map<String, String> getRelAssociationsOperators() {
		List<OperatorDTO> operators = getCodeListService().getRelAssociationsOperators();
		Map<String, String> relOpsMap = new TreeMap<String, String>();
		for (OperatorDTO operatorDTO : operators) {
			relOpsMap.put(operatorDTO.getItem(), operatorDTO.getId());
		}
		return relOpsMap;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#getTimingOperators()
	 */
	@Override
	public Map<String, String> getTimingOperators() {
		List<OperatorDTO> operators = getCodeListService().getTimingOperators();
		Map<String, String> timingOpsMap = new TreeMap<String, String>();
		for (OperatorDTO operatorDTO : operators) {
			timingOpsMap.put(operatorDTO.getItem(), operatorDTO.getId());
		}
		return timingOpsMap;
	}
	
	/**
	 * Gets the user service.
	 * 
	 * @return the user service
	 */
	private UserService getUserService() {
		return (UserService)context.getBean("userService");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#isCodeAlreadyExists(java.lang.String, mat.model.Code)
	 */
	@Override
	public boolean isCodeAlreadyExists(String codeListId, Code code) {
		return getCodeListService().isCodeAlreadyExists(codeListId, code);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#saveorUpdateCodeList(mat.client.codelist.ManageCodeListDetailModel)
	 */
	@Override
	public SaveUpdateCodeListResult saveorUpdateCodeList(
			ManageCodeListDetailModel currentDetails) {
		SaveUpdateCodeListResult result = new SaveUpdateCodeListResult();
		try {
			List<String> validationMessages = new ArrayList<String>();
			ListObjectModelValidator validator = new ListObjectModelValidator();
			validationMessages = validator.ValidateListObject(currentDetails);
			for(String message :validator.validatecodeListonlyFields(currentDetails)){
				validationMessages.add(message);
			}
			if(validationMessages.size() > 0){
				result.setSuccess(false);
				result.setFailureReason(SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION);
				for(String message : validationMessages){
					System.out.println("Server-Side Validation Message for List Object Model:" + message);
				}
			}else{
				result = getCodeListService().saveorUpdateCodeList(currentDetails);
				result.setSuccess(true);
			}
		}
		catch(CodeListNotUniqueException exc) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.NOT_UNIQUE);
		}
		catch(CodeListOidNotUniqueException e) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.OID_NOT_UNIQUE);
		}
		catch(ExcelParsingException e){
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.CODES_NOT_UNIQUE);
		}catch(InvalidLastModifiedDateException e){
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.INVALID_LAST_MODIFIED_DATE);
		}catch(ValueSetLastModifiedDateNotUniqueException e){
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.LAST_MODIFIED_DATE_DUPLICATE);
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#saveorUpdateGroupedCodeList(mat.client.codelist.ManageCodeListDetailModel)
	 */
	@Override
	public SaveUpdateCodeListResult saveorUpdateGroupedCodeList(
			ManageCodeListDetailModel currentDetails) {
		SaveUpdateCodeListResult result = new SaveUpdateCodeListResult();
		try {
			List<String> validationMessages = new ArrayList<String>();
			ListObjectModelValidator validator = new ListObjectModelValidator();
			validationMessages = validator.ValidateListObject(currentDetails);
			if(validationMessages.size() > 0){
				result.setSuccess(false);
				result.setFailureReason(SaveUpdateCodeListResult.SERVER_SIDE_VALIDATION);
				for(String message : validationMessages){
					System.out.println("Server-Side Validation Message for List Object Model:" + message);
				}
			}else{
				result = getCodeListService().saveorUpdateGroupedCodeList(currentDetails);
				result.setSuccess(true);
			}
		}
		catch(CodeListNotUniqueException exc) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.NOT_UNIQUE);
		}
		catch(CodeListOidNotUniqueException e) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.OID_NOT_UNIQUE);
		}catch(InvalidLastModifiedDateException e){
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.INVALID_LAST_MODIFIED_DATE);
		} catch (ValueSetLastModifiedDateNotUniqueException e) {
			result.setSuccess(false);
			result.setFailureReason(SaveUpdateCodeListResult.LAST_MODIFIED_DATE_DUPLICATE);
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.codelist.service.CodeListService#saveQDStoMeasure(mat.model
	 * .MatValueSetTransferObject)
	 */
	@Override
	public SaveUpdateCodeListResult saveQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject) {
		return getCodeListService().saveQDStoMeasure(matValueSetTransferObject);
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.codelist.service.CodeListService#saveUserDefinedQDStoMeasure
	 * (mat.model.MatValueSetTransferObject)
	 */
	@Override
	public SaveUpdateCodeListResult saveUserDefinedQDStoMeasure(MatValueSetTransferObject matValueSetTransferObject) {
		return getCodeListService().saveUserDefinedQDStoMeasure(matValueSetTransferObject);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#search(java.lang.String, int, int, java.lang.String, boolean, boolean, int)
	 */
	@Override
	public ManageCodeListSearchModel search(String searchText,
			int startIndex,	int pageSize, String sortColumn, boolean isAsc,boolean defaultCodeList, int filter) {
		
		ManageCodeListSearchModel result = new ManageCodeListSearchModel();
		result.setData(getCodeListService().search(searchText,
				startIndex, pageSize, sortColumn, isAsc,defaultCodeList, filter));
		result.setResultsTotal(getCodeListService().countSearchResultsWithFilter(searchText, defaultCodeList, filter));
		result.setStartIndex(startIndex);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#search(java.lang.String, int, int, java.lang.String, boolean, boolean, int, java.lang.String)
	 */
	@Override
	public ManageCodeListSearchModel search(String searchText,
			int startIndex,	int pageSize, String sortColumn, boolean isAsc,boolean defaultCodeList, int filter, String categoryId) {
		
		ManageCodeListSearchModel result = new ManageCodeListSearchModel();
		result.setData(getCodeListService().search(searchText,
				startIndex, pageSize, sortColumn, isAsc,defaultCodeList, filter, categoryId));
		result.setResultsTotal(getCodeListService().countSearchResultsWithFilter(searchText, defaultCodeList, filter));
		result.setStartIndex(startIndex);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#searchForAdmin(java.lang.String, int, int, java.lang.String, boolean, boolean, int)
	 */
	@Override
	public AdminManageCodeListSearchModel searchForAdmin(String searchText,
			int startIndex, int pageSize, String sortColumn, boolean isAsc,
			boolean defaultCodeList, int filter) {
		
		AdminManageCodeListSearchModel result = new AdminManageCodeListSearchModel();
		result.setData(getCodeListService().search(searchText,
				startIndex, pageSize, sortColumn, isAsc,defaultCodeList, filter));
		result.setResultsTotal(getCodeListService().countSearchResultsWithFilter(searchText, defaultCodeList, filter));
		result.setStartIndex(startIndex);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#searchUsers(int, int)
	 */
	@Override
	public TransferOwnerShipModel searchUsers(int startIndex, int pageSize) {
		
		UserService userService = getUserService();
		List<User> searchResults = userService.searchNonAdminUsers("",startIndex, pageSize);
		logger.info("User search returned " + searchResults.size());
		
		TransferOwnerShipModel result = new TransferOwnerShipModel();
		List<TransferOwnerShipModel.Result> detailList = new ArrayList<TransferOwnerShipModel.Result>();
		for(User user : searchResults) {
			TransferOwnerShipModel.Result r = new TransferOwnerShipModel.Result();
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
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#searchValueSetsForDraft(int, int)
	 */
	@Override
	public ManageValueSetSearchModel searchValueSetsForDraft(int startIndex, int pageSize) {
		CodeListService cls = getCodeListService();
		ManageValueSetSearchModel model = cls.searchValueSetsForDraft(startIndex, pageSize);
		return model;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#transferOwnerShipToUser(java.util.List, java.lang.String)
	 */
	@Override
	public void transferOwnerShipToUser(List<String> list, String toEmail){
		CodeListService cls = getCodeListService();
		cls.transferOwnerShipToUser(list, toEmail);
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.codelist.service.CodeListService#updateCodeListToMeasure(mat
	 * .model.MatValueSetTransferObject)
	 */
	@Override
	public SaveUpdateCodeListResult updateCodeListToMeasure(MatValueSetTransferObject matValueSetTransferObject) {
		matValueSetTransferObject.scrubForMarkUp();
		return getCodeListService().updateQDStoMeasure(matValueSetTransferObject);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.service.CodeListService#saveCopiedQDMListToMeasure(mat.model.GlobalCopyPasteObject, java.util.List, java.lang.String)
	 */
	@Override
	public SaveUpdateCodeListResult saveCopiedQDMListToMeasure(mat.model.GlobalCopyPasteObject gbCopyPaste,
			List<QualityDataSetDTO> qdmList, String measureId) {
		SaveUpdateCodeListResult result = new SaveUpdateCodeListResult();
		List<MatValueSetTransferObject> matValueSetTransferObjects = gbCopyPaste.getMatValueSetList();
		StringBuilder finalXmlString = new StringBuilder("<elementLookUp>");
		StringBuilder finalNewXmlString = new StringBuilder("<valuesets>");
		for (MatValueSetTransferObject  transferObject : matValueSetTransferObjects) {
			transferObject.scrubForMarkUp();
			transferObject.setAppliedQDMList(qdmList);
			DataTypeDAO dataTypeDAO = (DataTypeDAO) context.getBean("dataTypeDAO");
			DataType dataType = dataTypeDAO.findByDataTypeName(transferObject.getDatatype());
			if (dataType == null) {
				continue;
			}
			transferObject.setDatatype(dataType.getId());
			SaveUpdateCodeListResult saCodeListResult = new SaveUpdateCodeListResult();
			if ((transferObject.getUserDefinedText() != null)
					&& StringUtils.isNotEmpty(transferObject.getUserDefinedText())) {
				saCodeListResult = getCodeListService().saveUserDefinedQDStoMeasure(transferObject);
				if ((saCodeListResult.getXmlString() != null) && !StringUtils.isEmpty(saCodeListResult.getXmlString())) {
					finalXmlString = finalXmlString.append(saCodeListResult.getXmlString());
					finalNewXmlString = finalNewXmlString.append(saCodeListResult.getnewXmlString());
				}
			} else {
				saCodeListResult = getCodeListService().saveQDStoMeasure(transferObject);
				if ((saCodeListResult.getXmlString() != null) && !StringUtils.isEmpty(saCodeListResult.getXmlString())) {
					finalXmlString = finalXmlString.append(saCodeListResult.getXmlString());
					finalNewXmlString = finalNewXmlString.append(saCodeListResult.getnewXmlString());
				}
			}
		}
		finalXmlString.append("</elementLookUp>");
		finalNewXmlString.append("</valuesets>");
		result.setXmlString(finalXmlString.toString());
		result.setnewXmlString(finalNewXmlString.toString());
		// Temporary Commentted - Add code to append ElementLoopUp tags and then Invoke this method.
		saveAndAppendElementLookup(result, measureId);
		result.setAppliedQDMList(qdmList);
		return result;
	}
	
	/**
	 * Invoke MeasureLibrary Service to save global paste qdm Elements in MeasureXml.
	 * @param result - {@link SaveUpdateCodeListResult}
	 * @param measureId - Current Measure Id
	 */
	private void saveAndAppendElementLookup(SaveUpdateCodeListResult result, String measureId) {
		String nodeName = "qdm";
		String newNodeName = "valueset";
		MeasureXmlModel exportModal = new MeasureXmlModel();
		exportModal.setMeasureId(measureId);
		exportModal.setParentNode("/measure/elementLookUp");
		exportModal.setToReplaceNode("qdm");
		MeasureXmlModel newExportModal = new MeasureXmlModel();
		newExportModal.setMeasureId(measureId);
		newExportModal.setParentNode("/measure/cqlLookUp/valuesets");
		newExportModal.setToReplaceNode("valueset");
		System.out.println("XML " + result.getXmlString());
		exportModal.setXml(result.getXmlString());
		newExportModal.setXml(result.getnewXmlString());
		System.out.println("New XML " + result.getnewXmlString());
		MeasureLibraryServiceImpl measureService = (MeasureLibraryServiceImpl) context.getBean("measureLibraryService");
		measureService.appendAndSaveNode(exportModal,nodeName,newExportModal, newNodeName);
	}
}