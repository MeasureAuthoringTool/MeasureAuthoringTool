package mat.server;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mat.DTO.OperatorDTO;
import mat.DTO.UnitDTO;
import mat.client.codelist.AdminManageCodeListSearchModel;
import mat.client.codelist.HasListBox;
import mat.client.codelist.ManageCodeListDetailModel;
import mat.client.codelist.ManageCodeListSearchModel;
import mat.client.codelist.ManageValueSetSearchModel;
import mat.client.codelist.TransferOwnerShipModel;
import mat.client.codelist.service.SaveUpdateCodeListResult;
import mat.model.Code;
import mat.model.CodeListSearchDTO;
import mat.model.GroupedCodeListDTO;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.emory.mathcs.backport.java.util.Collections;

@SuppressWarnings("serial")
public class CodeListServiceImpl extends SpringRemoteServiceServlet 
implements mat.client.codelist.service.CodeListService {
	private static final Log logger = LogFactory.getLog(CodeListServiceImpl.class);


	
	
	
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

	@Override
	public ManageCodeListDetailModel getCodeList(String key) {
		ManageCodeListDetailModel model = getCodeListService().getCodeList(key);
		return model;
	}
	@Override
	public ManageCodeListDetailModel getGroupedCodeList(String key) {
		ManageCodeListDetailModel model = getCodeListService().getGroupedCodeList(key);
		return model;
	}

	public CodeListService getCodeListService() {
		return (CodeListService)context.getBean("codeListService");
	}
	
	@Override
	public mat.client.codelist.service.CodeListService.ListBoxData getListBoxData() {
		
		logger.info("getListBoxData");
		mat.client.codelist.service.CodeListService.ListBoxData data = 
			new mat.client.codelist.service.CodeListService.ListBoxData();
		data = getCodeListService().getListBoxData();
		return data;
	}
	
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


	@Override
	public List<? extends HasListBox> getCodeSystemsForCategory(String category) {
		return getCodeListService().getCodeSystemsForCategory(category);
	}

	@Override
	public List<? extends HasListBox> getCodeListsForCategory(String category) {
		return getCodeListService().getCodeListsForCategory(category);
	}

	@Override
	public List<? extends HasListBox> getQDSDataTypeForCategory(String category) {
		return getCodeListService().getQDSDataTypeForCategory(category);
	}

	
	@Override
	public ManageCodeListDetailModel deleteCodes(String codeListID,
			List<Code> Codes) {
		return  getCodeListService().deleteCodes(codeListID, Codes);
	}

	@Override
	public SaveUpdateCodeListResult addCodeListToMeasure(String measureId,String dataType,
			CodeListSearchDTO codeList,boolean isSpecificOccurrence,ArrayList<QualityDataSetDTO> appliedQDM) {
		return getCodeListService().saveQDStoMeasure(measureId,dataType,codeList,isSpecificOccurrence,appliedQDM);
	}

	@Override
	public SaveUpdateCodeListResult updateCodeListToMeasure(String measureID,
			String dataType, CodeListSearchDTO codeListSearchDTO,
			QualityDataSetDTO qualityDataSetDTO, Boolean isSpecificOccurrence,
			ArrayList<QualityDataSetDTO> appliedQDMList) {
		
		return getCodeListService().updateQDStoMeasure(measureID, dataType, codeListSearchDTO, qualityDataSetDTO, isSpecificOccurrence, appliedQDMList);
	}

	@Override
	public List<QualityDataSetDTO> getQDSElements(String measureId,
			String version) {
		List<QualityDataSetDTO> qdsElements = getCodeListService().getQDSElements(measureId, version);
		List<QualityDataSetDTO> filteredQDSElements = new ArrayList<QualityDataSetDTO>();
		for(QualityDataSetDTO dataSet : qdsElements) {
			if(dataSet.getOid() != null && !dataSet.getOid().equals(ConstantMessages.GENDER_OID)
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

	@Override
	public String generateUniqueOid(ManageCodeListDetailModel currentDetails) {
		return getCodeListService().generateUniqueOid(currentDetails);
	}

	/*US537 TODO implement*/
	@Override
	public ManageValueSetSearchModel searchValueSetsForDraft(int startIndex, int pageSize) {
		CodeListService cls = getCodeListService();
		ManageValueSetSearchModel model = cls.searchValueSetsForDraft(startIndex, pageSize);
		return model;
	}

	@Override
	public ManageValueSetSearchModel createDraft(String id, String oid) {
		CodeListService cls = getCodeListService();
		ManageValueSetSearchModel model = cls.createDraft(id, oid);
		return model;
	}


	@Override
	public List<Code> getCodes(String codeListId, int startIndex,int pageSize) {
		return getCodeListService().getCodes(codeListId, startIndex, pageSize);
	}

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
	
	@Override
	public boolean isCodeAlreadyExists(String codeListId, Code code) {
		return getCodeListService().isCodeAlreadyExists(codeListId, code);
	}
	
	//US193
	@Override
	public ManageValueSetSearchModel createClone(String id) {
		CodeListService cls = getCodeListService();
		ManageValueSetSearchModel model = cls.createClone(id);
		return model;
	}

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
	@Override
	public void transferOwnerShipToUser(List<String> list, String toEmail){
		CodeListService cls = getCodeListService();
		cls.transferOwnerShipToUser(list, toEmail);
	}
	
	private UserService getUserService() {
		return (UserService)context.getBean("userService");
	}

	@Override
	public Map<String, String> getTimingOperators() {
		List<OperatorDTO> operators = getCodeListService().getTimingOperators();
		Map<String, String> timingOpsMap = new TreeMap<String, String>();
		for (OperatorDTO operatorDTO : operators) {
			timingOpsMap.put(operatorDTO.getItem(), operatorDTO.getId());
		}
		return timingOpsMap;
	}
	
	@Override
	public Map<String, String> getRelAssociationsOperators() {
		List<OperatorDTO> operators = getCodeListService().getRelAssociationsOperators();
		Map<String, String> relOpsMap = new TreeMap<String, String>();
		for (OperatorDTO operatorDTO : operators) {
			relOpsMap.put(operatorDTO.getItem(), operatorDTO.getId());
		}
		return relOpsMap;
	}

	@Override
	public List<OperatorDTO> getAllOperators() {
		return getCodeListService().getAllOperators();
	}

	
	
}