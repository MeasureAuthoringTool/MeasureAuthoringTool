package mat.client.codelist;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import mat.dto.OperatorDTO;
import mat.dto.UnitDTO;
import mat.client.codelist.service.CodeListService;
import mat.client.shared.MatContext;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ListBoxCodeProvider implements IsSerializable {
	private static final String NONE = " ";
	private static final String LOGICAL_OP_LABEL = "-- Logical Operators --";
	private static final String TIMING_OP_LABEL = "-- Time Relationships --";
	private static final String ASSOCIATIONS_OP_LABEL = "-- Relationships --";
	private List<? extends HasListBox> categoryList;
	private List<AsyncCallback<List<? extends HasListBox>>> pendingCategoryCallbacks = new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	private List<? extends HasListBox> codeSystemList;
	private List<AsyncCallback<List<? extends HasListBox>>> pendingCodeSystemCallbacks = new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	private List<AsyncCallback<List<? extends HasListBox>>> pendingObjectStatusList = new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	private List<AsyncCallback<List<? extends HasListBox>>> pendingStewardList = new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	private List<AsyncCallback<List<? extends HasListBox>>> pendingAuthorList = new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	private List<AsyncCallback<List<? extends HasListBox>>> pendingMeasureTypeList = new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	private List<AsyncCallback<List<? extends HasListBox>>> pendingScoringList = new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	private List<AsyncCallback<List<? extends HasListBox>>> pendingUnitList = new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	private List<AsyncCallback<List<? extends HasListBox>>> pendingUnitTypeList = new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	private List<AsyncCallback<List<? extends HasListBox>>> pendingUnitTypeMatrixList = new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
    private List<AsyncCallback<List<? extends HasListBox>>> pendingLogicalOperatorList = new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	private List<AsyncCallback<List<? extends HasListBox>>> pendingTimingOperatorList = new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	private List<AsyncCallback<List<? extends HasListBox>>> pendingAssocOperatorList = new ArrayList<AsyncCallback<List<? extends HasListBox>>>();
	private List<? extends HasListBox> stewardList;
	private List<? extends HasListBox> authorList;
	private List<? extends HasListBox> measureTypeList;
	private List<? extends HasListBox> scoringList;
	private List<? extends HasListBox> unitList;
	private List<? extends HasListBox> unitTypeList;
	private List<? extends HasListBox> unitTypeMatrixList;
	private List<? extends HasListBox> logicalOperatorList;
	private List<? extends HasListBox> relTimingOperatorList;
	private List<? extends HasListBox> relAssocOperatorList;
	private HashMap<String, List<? extends HasListBox>> matrixMap;
	boolean retrieved = false;	

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

				for(AsyncCallback<List<? extends HasListBox>> callback : pendingUnitList) {
					callback.onFailure(caught);
				}	
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingUnitTypeList) {
					callback.onFailure(caught);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingUnitTypeMatrixList) {
					callback.onFailure(caught);
				}

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

				pendingUnitList.clear();
				pendingUnitTypeList.clear();
				pendingUnitTypeMatrixList.clear();

				pendingLogicalOperatorList.clear();
				pendingTimingOperatorList.clear();
				pendingAssocOperatorList.clear();
			}

			@Override
			public void onSuccess(CodeListService.ListBoxData listBoxResults) {
				categoryList = listBoxResults.getCategoryList();
				codeSystemList = listBoxResults.getCodeSystemList();
				stewardList = listBoxResults.getMeasureStewardList();
				authorList = listBoxResults.getAuthorsList();
				measureTypeList = listBoxResults.getMeasureTypeList();
				scoringList = listBoxResults.getScoringList();
				unitList = listBoxResults.getUnitList();
				unitTypeList = listBoxResults.getUnitTypeList();
				unitTypeMatrixList = listBoxResults.getUnitTypeMatrixList();

				logicalOperatorList = listBoxResults.getLogicalOperatorList();
				relTimingOperatorList = listBoxResults.getRelTimingOperatorList();
				relAssocOperatorList = listBoxResults.getRelAssocOperatorList();
				
				HasListBox.Comparator comparator = new HasListBox.Comparator();
				Collections.sort(categoryList, comparator);
				Collections.sort(codeSystemList, comparator);
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

				for(AsyncCallback<List<? extends HasListBox>> callback : pendingUnitList) {
					callback.onSuccess(unitList);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingUnitTypeList) {
					callback.onSuccess(unitTypeList);
				}
				for(AsyncCallback<List<? extends HasListBox>> callback : pendingUnitTypeMatrixList) {
					callback.onSuccess(unitTypeMatrixList);
				}

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

				pendingUnitList.clear();
				pendingUnitTypeList.clear();
				pendingUnitTypeMatrixList.clear();

				pendingLogicalOperatorList.clear();
				pendingTimingOperatorList.clear();
				pendingAssocOperatorList.clear();
			}
		});
	}
	
	public void getCategoryList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(categoryList);
		}
		else {
			pendingCategoryCallbacks.add(callback);
		}
	}
	
	public void getStewardList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(stewardList);
		}
		else {
			pendingStewardList.add(callback);
		}
	}

	public void getScoringList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(scoringList);
		}
		else {
			pendingScoringList.add(callback);
		}
	}

	public void getAuthorsList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(authorList);
		}
		else {
			pendingAuthorList.add(callback);
		}
	}
	
	public void getMeasureTypeList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(measureTypeList);
		}
		else {
			pendingMeasureTypeList.add(callback);
		}
	}

	public void setCategoryList(List<? extends HasListBox> categoryList) {
		this.categoryList = categoryList;
	}
	
	public void getCodeSystemList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(codeSystemList);
		}
		else {
			pendingCodeSystemCallbacks.add(callback);			
		}
	}

	public void setCodeSystemList(List<? extends HasListBox> codeSystemList) {
		this.codeSystemList = codeSystemList;
	}
	
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
	
	public void getUnitList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(unitList);
		}
		else {
			pendingUnitList.add(callback);
		}
	}
	
	public void getUnitTypeList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(unitTypeList);
		}
		else {
			pendingUnitTypeList.add(callback);
		}
	}

	public void getUnitTypeMatrixList(AsyncCallback<List<? extends HasListBox>> callback) {
		if(retrieved) {
			callback.onSuccess(unitTypeMatrixList);
		}
		else {
			pendingUnitTypeMatrixList.add(callback);
		}
	}
	
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
	
	public void getLogicalOperatorList(AsyncCallback<List<? extends HasListBox>> callback){
		if(retrieved){
			callback.onSuccess(logicalOperatorList);
		}else {
			pendingLogicalOperatorList.add(callback);
		}
	}
	
	public void getRelTimingOperatorList(AsyncCallback<List<? extends HasListBox>> callback){
		if(retrieved){
			callback.onSuccess(relTimingOperatorList);
		}else {
			pendingTimingOperatorList.add(callback);
		}
	}
	
	public void getRelAssocOperatorList(AsyncCallback<List<? extends HasListBox>> callback){
		if(retrieved){
			callback.onSuccess(relAssocOperatorList);
		}else {
			pendingAssocOperatorList.add(callback);
		}
	}
	
	public List<? extends HasListBox> getLogicalOperatorList() {
		return logicalOperatorList;
	}

	public void setLogicalOperatorList(
			List<? extends HasListBox> logicalOperatorList) {
		this.logicalOperatorList = logicalOperatorList;
	}

	public List<? extends HasListBox> getRelTimingOperatorList() {
		return relTimingOperatorList;
	}

	public void setRelTimingOperatorList(
			List<? extends HasListBox> relTimingOperatorList) {
		this.relTimingOperatorList = relTimingOperatorList;
	}

	public List<? extends HasListBox> getRelAssocOperatorList() {
		return relAssocOperatorList;
	}


	public void setRelAssocOperatorList(
			List<? extends HasListBox> relAssocOperatorList) {
		this.relAssocOperatorList = relAssocOperatorList;
	}


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
	
	private OperatorDTO createNoneDTO(){
		OperatorDTO noneDTO = new OperatorDTO();
		noneDTO.setId("NONE");
		noneDTO.setItem(NONE);
		return noneDTO;
	}
	
	private OperatorDTO  createLabel(String labelName){
		OperatorDTO logDTO = new OperatorDTO();
		logDTO.setId("NONE");
		logDTO.setItem(labelName);
		return logDTO;
	}
	
	private void addOperators(List<? extends HasListBox> listBoxValues, ArrayList<OperatorDTO> listofCond){
		for(HasListBox logical : listBoxValues){
	    	OperatorDTO logicalOP = new OperatorDTO();
	    	logicalOP.setId(logical.getValue());
	    	logicalOP.setItem(logical.getItem());
	    	listofCond.add(logicalOP);
	    }
	}

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
	
	public List<String> getRelAssociationOperatorList(){
		ArrayList<String> relAssocList = new ArrayList<String>();
		for(HasListBox operator : relAssocOperatorList){
			relAssocList.add(operator.getItem());
		}
		return relAssocList;
	}
}
