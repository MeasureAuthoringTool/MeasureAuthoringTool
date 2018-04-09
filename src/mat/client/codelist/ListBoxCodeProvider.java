package mat.client.codelist;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import mat.DTO.OperatorDTO;
import mat.DTO.UnitDTO;
import mat.client.codelist.service.CodeListService;
import mat.client.shared.MatContext;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class ListBoxCodeProvider.
 */

public class ListBoxCodeProvider implements IsSerializable {
	
	/** The Constant NONE. */
	private static final String NONE = " ";
	
	/** The Constant LOGICAL_OP_LABEL. */
	private static final String LOGICAL_OP_LABEL = "-- Logical Operators --";
	
	/** The Constant TIMING_OP_LABEL. */
	private static final String TIMING_OP_LABEL = "-- Time Relationships --";
	
	/** The Constant ASSOCIATIONS_OP_LABEL. */
	private static final String ASSOCIATIONS_OP_LABEL = "-- Relationships --";
	
	/** The category list. */
	private List<? extends HasListBox> categoryList;
	
	/** The pending category callbacks. */
	private List<AsyncCallback<List<? extends HasListBox>>> pendingCategoryCallbacks =
		new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	
	/** The code system list. */
	private List<? extends HasListBox> codeSystemList;
	
	/** The pending code system callbacks. */
	private List<AsyncCallback<List<? extends HasListBox>>> pendingCodeSystemCallbacks =
		new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	
	/** The pending object status list. */
	private List<AsyncCallback<List<? extends HasListBox>>> pendingObjectStatusList =
		new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	
	/** The pending steward list. */
	private List<AsyncCallback<List<? extends HasListBox>>> pendingStewardList =
		new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	
	/** The pending author list. */
	private List<AsyncCallback<List<? extends HasListBox>>> pendingAuthorList =
		new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	
	/** The pending measure type list. */
	private List<AsyncCallback<List<? extends HasListBox>>> pendingMeasureTypeList =
		new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	
	/** The pending scoring list. */
	private List<AsyncCallback<List<? extends HasListBox>>> pendingScoringList =
		new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	

	/** The pending unit list. */
	private List<AsyncCallback<List<? extends HasListBox>>> pendingUnitList =
		new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	
	/** The pending unit type list. */
	private List<AsyncCallback<List<? extends HasListBox>>> pendingUnitTypeList =
		new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	
	/** The pending unit type matrix list. */
	private List<AsyncCallback<List<? extends HasListBox>>> pendingUnitTypeMatrixList =
		new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	
    //US 171
	/** The pending logical operator list. */
    private List<AsyncCallback<List<? extends HasListBox>>> pendingLogicalOperatorList =
		new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	
	/** The pending timing operator list. */
	private List<AsyncCallback<List<? extends HasListBox>>> pendingTimingOperatorList =
		new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	
	/** The pending assoc operator list. */
	private List<AsyncCallback<List<? extends HasListBox>>> pendingAssocOperatorList =
		new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	
	/** The object status list. */
	private List<? extends HasListBox> objectStatusList;
	
	/** The steward list. */
	private List<? extends HasListBox> stewardList;
	
	/** The author list. */
	private List<? extends HasListBox> authorList;
	
	/** The measure type list. */
	private List<? extends HasListBox> measureTypeList;
	
	/** The scoring list. */
	private List<? extends HasListBox> scoringList;
	
	/** The unit list. */
	private List<? extends HasListBox> unitList;
	
	/** The unit type list. */
	private List<? extends HasListBox> unitTypeList;
	
	/** The unit type matrix list. */
	private List<? extends HasListBox> unitTypeMatrixList;
	
	//US 171
	/** The logical operator list. */
	private List<? extends HasListBox> logicalOperatorList;
	
	/** The rel timing operator list. */
	private List<? extends HasListBox> relTimingOperatorList;
	
	/** The rel assoc operator list. */
	private List<? extends HasListBox> relAssocOperatorList;
	
	/** The matrix map. */
	private HashMap<String, List<? extends HasListBox>> matrixMap;
	
	/** The retrieved. */
	boolean retrieved = false;	
	
	/**
	 * Instantiates a new list box code provider.
	 */
	public ListBoxCodeProvider(){
		MatContext.get().getListBoxData(new AsyncCallback<CodeListService.ListBoxData>() {

			@Override
			public void onFailure(Throwable caught) {
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingCategoryCallbacks) {
					callback.onFailure(caught);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingCodeSystemCallbacks) {
					callback.onFailure(caught);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingObjectStatusList) {
					callback.onFailure(caught);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingStewardList) {
					callback.onFailure(caught);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingAuthorList) {
					callback.onFailure(caught);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingMeasureTypeList) {
					callback.onFailure(caught);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingScoringList) {
					callback.onFailure(caught);
				}

				//US 62
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingUnitList) {
					callback.onFailure(caught);
				}	
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingUnitTypeList) {
					callback.onFailure(caught);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingUnitTypeMatrixList) {
					callback.onFailure(caught);
				}

				//US 171
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingLogicalOperatorList) {
					callback.onFailure(caught);
				}				
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingTimingOperatorList) {
					callback.onFailure(caught);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingAssocOperatorList) {
					callback.onFailure(caught);
				}
				
				pendingCategoryCallbacks.clear();
				pendingCodeSystemCallbacks.clear();
				pendingObjectStatusList.clear();
				pendingStewardList.clear();
				pendingAuthorList.clear();
				pendingMeasureTypeList.clear();
				pendingScoringList.clear();

				//US 62
				pendingUnitList.clear();
				pendingUnitTypeList.clear();
				pendingUnitTypeMatrixList.clear();
				
				//US 171
				pendingLogicalOperatorList.clear();
				pendingTimingOperatorList.clear();
				pendingAssocOperatorList.clear();
			}

			@Override
			public void onSuccess(CodeListService.ListBoxData listBoxResults) {
				categoryList = listBoxResults.getCategoryList();
				codeSystemList = listBoxResults.getCodeSystemList();
				objectStatusList = listBoxResults.getStatusList();
				stewardList = listBoxResults.getMeasureStewardList();
				authorList = listBoxResults.getAuthorsList();
				measureTypeList = listBoxResults.getMeasureTypeList();
				scoringList = listBoxResults.getScoringList();
				unitList = listBoxResults.getUnitList();
				unitTypeList = listBoxResults.getUnitTypeList();
				unitTypeMatrixList = listBoxResults.getUnitTypeMatrixList();
				
				//US 171
				logicalOperatorList = listBoxResults.getLogicalOperatorList();
				relTimingOperatorList = listBoxResults.getRelTimingOperatorList();
				relAssocOperatorList = listBoxResults.getRelAssocOperatorList();
				
				HasListBox.Comparator comparator = new HasListBox.Comparator();
				Collections.sort(categoryList, comparator);
				Collections.sort(codeSystemList, comparator);
				Collections.sort(objectStatusList, comparator);
				Collections.sort(stewardList, comparator);
				Collections.sort(authorList, comparator);
				Collections.sort(measureTypeList, comparator);
				Collections.sort(scoringList, comparator);
				
				retrieved = true;
				
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingCategoryCallbacks) {
					callback.onSuccess(categoryList);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingCodeSystemCallbacks) {
					callback.onSuccess(codeSystemList);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingObjectStatusList) {
					callback.onSuccess(objectStatusList);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingStewardList) {
					callback.onSuccess(stewardList);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingAuthorList) {
					callback.onSuccess(authorList);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingMeasureTypeList) {
					callback.onSuccess(measureTypeList);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingScoringList) {
					callback.onSuccess(scoringList);
				}
				
				//US 62
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingUnitList) {
					callback.onSuccess(unitList);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingUnitTypeList) {
					callback.onSuccess(unitTypeList);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingUnitTypeMatrixList) {
					callback.onSuccess(unitTypeMatrixList);
				}

				//US 171
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingLogicalOperatorList) {
					callback.onSuccess(logicalOperatorList);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingTimingOperatorList) {
					callback.onSuccess(relTimingOperatorList);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingAssocOperatorList) {
					callback.onSuccess(relAssocOperatorList);
				}
				pendingCategoryCallbacks.clear();
				pendingCodeSystemCallbacks.clear();
				pendingObjectStatusList.clear();
				pendingStewardList.clear();
				pendingAuthorList.clear();
				pendingMeasureTypeList.clear();
				pendingScoringList.clear();

				//US 62
				pendingUnitList.clear();
				pendingUnitTypeList.clear();
				pendingUnitTypeMatrixList.clear();
				
				//US 171
				pendingLogicalOperatorList.clear();
				pendingTimingOperatorList.clear();
				pendingAssocOperatorList.clear();
			}
		});
	}
	
	/**
	 * Gets the category list.
	 * 
	 * @param callback
	 *            the callback
	 * @return the category list
	 */
	public void getCategoryList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(categoryList);
		}
		else {
			pendingCategoryCallbacks.add(callback);
		}
	}
	
	/**
	 * Gets the steward list.
	 * 
	 * @param callback
	 *            the callback
	 * @return the steward list
	 */
	public void getStewardList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(stewardList);
		}
		else {
			pendingStewardList.add(callback);
		}
	}

	//US 421. Retrieve the scoring choices from DB
	/**
	 * Gets the scoring list.
	 * 
	 * @param callback
	 *            the callback
	 * @return the scoring list
	 */
	public void getScoringList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(scoringList);
		}
		else {
			pendingScoringList.add(callback);
		}
	}

	/**
	 * Gets the authors list.
	 * 
	 * @param callback
	 *            the callback
	 * @return the authors list
	 */
	public void getAuthorsList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(authorList);
		}
		else {
			pendingAuthorList.add(callback);
		}
	}
	
	/**
	 * Gets the measure type list.
	 * 
	 * @param callback
	 *            the callback
	 * @return the measure type list
	 */
	public void getMeasureTypeList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(measureTypeList);
		}
		else {
			pendingMeasureTypeList.add(callback);
		}
	}
	
	/**
	 * Gets the status list.
	 * 
	 * @param callback
	 *            the callback
	 * @return the status list
	 */
	public void getStatusList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(objectStatusList);
		}
		else {
			pendingObjectStatusList.add(callback);
		}
	}
	
	/**
	 * Sets the category list.
	 * 
	 * @param categoryList
	 *            the new category list
	 */
	public void setCategoryList(List<? extends HasListBox> categoryList) {
		this.categoryList = categoryList;
	}
	
	/**
	 * Gets the code system list.
	 * 
	 * @param callback
	 *            the callback
	 * @return the code system list
	 */
	public void getCodeSystemList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(codeSystemList);
		}
		else {
			pendingCodeSystemCallbacks.add(callback);			
		}
	}
	
	/**
	 * Sets the code system list.
	 * 
	 * @param codeSystemList
	 *            the new code system list
	 */
	public void setCodeSystemList(List<? extends HasListBox> codeSystemList) {
		this.codeSystemList = codeSystemList;
	}
	
	/**
	 * Gets the object status list.
	 * 
	 * @return the object status list
	 */
	public List<? extends HasListBox> getObjectStatusList() {
		return objectStatusList;
	}

	/**
	 * Sets the object status list.
	 * 
	 * @param objectStatusList
	 *            the new object status list
	 */
	public void setObjectStatusList(List<? extends HasListBox> objectStatusList) {
		this.objectStatusList = objectStatusList;
	}

	
	
	
	/**
	 * Gets the qDS data type for category.
	 * 
	 * @param category
	 *            the category
	 * @param callback
	 *            the callback
	 * @return the qDS data type for category
	 */
	public void getQDSDataTypeForCategory(String category, final AsyncCallback<List<? extends HasListBox>> callback){
		MatContext.get().getCodeListService().getQDSDataTypeForCategory(category, new AsyncCallback<List<? extends HasListBox>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				Collections.sort(result, new HasListBox.Comparator());
				callback.onSuccess(result);
			}
		});
	}
	
	
	/**
	 * Gets the all data type.
	 * 
	 * @param callback
	 *            the callback
	 * @return the all data type
	 */
	public void getAllDataType(final AsyncCallback<List<? extends HasListBox>> callback){
		MatContext.get().getCodeListService().getAllDataTypes(new AsyncCallback<List<? extends HasListBox>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				Collections.sort(result, new HasListBox.Comparator());
				callback.onSuccess(result);
			}
		});
	}
	
	
	//US 62	
	/**
	 * Gets the unit list.
	 * 
	 * @param callback
	 *            the callback
	 * @return the unit list
	 */
	public void getUnitList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(unitList);
		}
		else {
			pendingUnitList.add(callback);
		}
	}
	
	/**
	 * Gets the unit type list.
	 * 
	 * @param callback
	 *            the callback
	 * @return the unit type list
	 */
	public void getUnitTypeList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(unitTypeList);
		}
		else {
			pendingUnitTypeList.add(callback);
		}
	}

	/**
	 * Gets the unit type matrix list.
	 * 
	 * @param callback
	 *            the callback
	 * @return the unit type matrix list
	 */
	public void getUnitTypeMatrixList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(unitTypeMatrixList);
		}
		else {
			pendingUnitTypeMatrixList.add(callback);
		}
	}
	
	/**
	 * Gets the unit matrix list by category.
	 * 
	 * @param unitCategory
	 *            the unit category
	 * @param callback
	 *            the callback
	 * @return the unit matrix list by category
	 */
	public void getUnitMatrixListByCategory(String unitCategory, AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			if(matrixMap == null){
				initUnitMatrix();
			}
			callback.onSuccess(matrixMap.get(unitCategory));
		}
		else {
			pendingUnitList.add(callback);
			pendingUnitTypeList.add(callback);
			pendingUnitTypeMatrixList.add(callback);
		}
	}
	
	/**
	 * Initializes units based on the Unit type and sorts by id. 
	 */
	private void initUnitMatrix(){
		matrixMap = new HashMap<String, List<? extends HasListBox>>();
		for(HasListBox unitType: unitTypeList){
			String id = unitType.getValue();
			List<UnitDTO> matrixList = new ArrayList<UnitDTO>();
			for(HasListBox matrix: unitTypeMatrixList){
				if(matrix.getValue().equals(id)){
					String unitId = matrix.getItem();
					for(HasListBox unit: unitList){
						if(unitId.equals(unit.getValue())){
							UnitDTO dto = new UnitDTO();
							dto.setId(unitId);
							dto.setItem(unit.getItem());
							dto.setSortOrder(unit.getSortOrder());
							matrixList.add(dto);
						}
					}
				}
			}
			
			Collections.sort(matrixList, new Comparator<Object>(){
				public int compare(Object obj1, Object obj2) {
					int id1 = ((UnitDTO)obj1).getSortOrder();
					int id2 = ((UnitDTO)obj2).getSortOrder();			 
					if(id1>id2) return 1;
					else if(id1<id2) return -1;
					else return 0;
				}
				
			});
			
			matrixMap.put(unitType.getItem(),matrixList);
		}
	}
	
	//US 171
	/**
	 * Gets the logical operator list.
	 * 
	 * @param callback
	 *            the callback
	 * @return the logical operator list
	 */
	public void getLogicalOperatorList(AsyncCallback<List<? extends HasListBox>> callback){
		if(retrieved){
			callback.onSuccess(logicalOperatorList);
		}else {
			pendingLogicalOperatorList.add(callback);
		}
	}
	
	//US 171
	/**
	 * Gets the rel timing operator list.
	 * 
	 * @param callback
	 *            the callback
	 * @return the rel timing operator list
	 */
	public void getRelTimingOperatorList(AsyncCallback<List<? extends HasListBox>> callback){
		if(retrieved){
			callback.onSuccess(relTimingOperatorList);
		}else {
			pendingTimingOperatorList.add(callback);
		}
	}
	
	/**
	 * Gets the rel assoc operator list.
	 * 
	 * @param callback
	 *            the callback
	 * @return the rel assoc operator list
	 */
	public void getRelAssocOperatorList(AsyncCallback<List<? extends HasListBox>> callback){
		if(retrieved){
			callback.onSuccess(relAssocOperatorList);
		}else {
			pendingAssocOperatorList.add(callback);
		}
	}
	
	/**
	 * Gets the logical operator list.
	 * 
	 * @return the logical operator list
	 */
	public List<? extends HasListBox> getLogicalOperatorList() {
		return logicalOperatorList;
	}

	/**
	 * Sets the logical operator list.
	 * 
	 * @param logicalOperatorList
	 *            the new logical operator list
	 */
	public void setLogicalOperatorList(
			List<? extends HasListBox> logicalOperatorList) {
		this.logicalOperatorList = logicalOperatorList;
	}

	/**
	 * Gets the rel timing operator list.
	 * 
	 * @return the rel timing operator list
	 */
	public List<? extends HasListBox> getRelTimingOperatorList() {
		return relTimingOperatorList;
	}

	/**
	 * Sets the rel timing operator list.
	 * 
	 * @param relTimingOperatorList
	 *            the new rel timing operator list
	 */
	public void setRelTimingOperatorList(
			List<? extends HasListBox> relTimingOperatorList) {
		this.relTimingOperatorList = relTimingOperatorList;
	}

	/**
	 * Gets the rel assoc operator list.
	 * 
	 * @return the rel assoc operator list
	 */
	public List<? extends HasListBox> getRelAssocOperatorList() {
		return relAssocOperatorList;
	}

	/**
	 * Sets the rel assoc operator list.
	 * 
	 * @param relAssocOperatorList
	 *            the new rel assoc operator list
	 */
	public void setRelAssocOperatorList(
			List<? extends HasListBox> relAssocOperatorList) {
		this.relAssocOperatorList = relAssocOperatorList;
	}

	/*
	 * usePhraseConditions == logical + relative Timing + relative Associations
	 */
	//US 171
	/**
	 * Gets the use phrase conditions.
	 * 
	 * @param callback
	 *            the callback
	 * @return the use phrase conditions
	 */
	public void getUsePhraseConditions(AsyncCallback<List<? extends HasListBox>> callback){
		ArrayList<OperatorDTO> listofConditions = new ArrayList<OperatorDTO>();
		listofConditions.add(createNoneDTO());
		listofConditions.add(createLabel(LOGICAL_OP_LABEL));
	    addOperators(logicalOperatorList, listofConditions);
	    listofConditions.add(createNoneDTO());
		listofConditions.add(createLabel(TIMING_OP_LABEL));
	    addOperators(relTimingOperatorList, listofConditions);
	    listofConditions.add(createNoneDTO());
		listofConditions.add(createLabel(ASSOCIATIONS_OP_LABEL));
	    addOperators(relAssocOperatorList, listofConditions);
	    callback.onSuccess(listofConditions);
	}
	
	/**
	 * Creates the none dto.
	 * 
	 * @return the operator dto
	 */
	private OperatorDTO createNoneDTO(){
		OperatorDTO noneDTO = new OperatorDTO();
		noneDTO.setId("NONE");
		noneDTO.setItem(NONE);
		return noneDTO;
	}
	
	/**
	 * Creates the label.
	 * 
	 * @param labelName
	 *            the label name
	 * @return the operator dto
	 */
	private OperatorDTO  createLabel(String labelName){
		OperatorDTO logDTO = new OperatorDTO();
		logDTO.setId("NONE");
		logDTO.setItem(labelName);
		return logDTO;
	}
	
	/**
	 * Adds the operators.
	 * 
	 * @param listBoxValues
	 *            the list box values
	 * @param listofCond
	 *            the listof cond
	 */
	private void addOperators(List<? extends HasListBox> listBoxValues, ArrayList<OperatorDTO> listofCond){
		for(HasListBox logical : listBoxValues){
	    	OperatorDTO logicalOP = new OperatorDTO();
	    	logicalOP.setId(logical.getValue());
	    	logicalOP.setItem(logical.getItem());
	    	listofCond.add(logicalOP);
	    }
	}
	//US 171, Getting rid of Rel class static hard coded values. Using a Map to hold the RelTimings and Rel Associations.
	/**
	 * Gets the timing conditions map.
	 * 
	 * @return the timing conditions map
	 */
	public HashMap<String,String> getTimingConditionsMap(){
		HashMap<String,String> timingConditionsMap = new HashMap<String,String>();
		for(HasListBox operator : relTimingOperatorList){
			timingConditionsMap.put(operator.getItem(), operator.getValue());
		}
		for(HasListBox operator : relAssocOperatorList){
			timingConditionsMap.put(operator.getItem(), operator.getValue());
		}
		return timingConditionsMap;
	}
	
	//US 171 Getting rid of Rel class static hard coded values. Getting only the relativeAssociationList.
	/**
	 * Gets the rel association operator list.
	 * 
	 * @return the rel association operator list
	 */
	public List<String> getRelAssociationOperatorList(){
		ArrayList<String> relAssocList = new ArrayList<String>();
		for(HasListBox operator : relAssocOperatorList){
			relAssocList.add(operator.getItem());
		}
		return relAssocList;
	}
}
