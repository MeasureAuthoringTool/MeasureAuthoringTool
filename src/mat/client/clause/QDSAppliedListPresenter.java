package mat.client.clause;

import mat.client.MatPresenter;
import mat.client.codelist.service.CodeListService;
import mat.client.codelist.service.CodeListServiceAsync;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.model.QualityDataSetDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;


public class QDSAppliedListPresenter implements MatPresenter {

	private SimplePanel panel = new SimplePanel();
	private SearchDisplay searchDisplay;
	List<QualityDataSetDTO> qdsList = new ArrayList<QualityDataSetDTO>();
	MeasureServiceAsync service = MatContext.get().getMeasureService();
	List<JSONObject> codeListQDSEL = new ArrayList<JSONObject>();
	List<String> codeListString = new ArrayList<String>();
//	Map<String,List<JSONObject>> codeMap = new HashMap<String,List<JSONObject>>();
	public static interface SearchDisplay {
		public SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public  void buildCellListWidget(QDSAppliedListModel appliedListModel);
		public Widget asWidget();
		void buildCellList(List<JSONObject> codeListQDSEL);
	}

	public QDSAppliedListPresenter(SearchDisplay sDisplayArg) {
		this.searchDisplay = sDisplayArg;
		showAppliedQDMsInMeasure(MatContext.get().getCurrentMeasureId());
	}

	void loadCodeListData() {
		panel.clear();
		showAppliedQDMsInMeasure(MatContext.get().getCurrentMeasureId());
		displaySearch();
		getXMLForAppliedQDM();
	}

	public Widget getWidget() {
		return panel;
	}

	public void resetQDSFields() {
		searchDisplay.getApplyToMeasureSuccessMsg().clear();
		searchDisplay.getErrorMessageDisplay().clear();
	}

	private void displaySearch() {
		panel.clear();
		panel.add(searchDisplay.asWidget());
	}
	/**
	 * Method for fetching all applied Value Sets in a measure which is loaded
	 * in context.
	 * 
	 * */
	public void showAppliedQDMsInMeasure(String measureId) {
		measureId = MatContext.get().getCurrentMeasureId();
		CodeListServiceAsync codeListService = (CodeListServiceAsync) GWT
				.create(CodeListService.class);
		if (measureId != null && measureId != "") {
			codeListService.getQDSElements(measureId, null,
					new AsyncCallback<List<QualityDataSetDTO>>() {
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate()
									.getGenericErrorMessage());
						}

						@Override
						public void onSuccess(List<QualityDataSetDTO> result) {
							QDSAppliedListModel appliedListModel = new QDSAppliedListModel();
							appliedListModel.setAppliedQDMs(result);
							searchDisplay.buildCellListWidget(appliedListModel);
						}
					});
		}
	}
	
	public void getXMLForAppliedQDM(){
		String measureId = MatContext.get().getCurrentMeasureId();
		if (measureId != null && measureId != "") {
			service.getJSONObjectFromXML(measureId, new AsyncCallback<String>(){

				@Override
				public void onFailure(Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate()
							.getGenericErrorMessage());
					
				}

				@Override
				public void onSuccess(String result) {
					//Window.alert(result);
					extractJSONObject(result);
					searchDisplay.buildCellList(codeListQDSEL);
				}});
			
		}
		
	}
		
	private void extractJSONObject(String jsonString){
		codeListQDSEL.removeAll(codeListQDSEL);
		if(jsonString != null){
			JSONValue jsonValue = JSONParser.parse(jsonString);
			if(jsonValue.isObject()!=null){
				JSONObject jsonObject = (JSONObject) jsonValue.isObject().get("measure");
				if(jsonObject.get("elementlookup").isArray() !=null){
					JSONArray arrayObject = jsonObject.get("elementlookup").isArray();
					for(int i=0;i<arrayObject.size();i++){
						codeListQDSEL.add(arrayObject.get(i).isObject());
					}
				}else if (jsonObject.get("elementlookup").isObject() !=null){
					JSONObject elementLookUpObject = jsonObject.get("elementlookup").isObject();
					if(elementLookUpObject.get("qdsel").isArray()!=null){
						for(int i=0;i<elementLookUpObject.get("qdsel").isArray().size();i++){
							codeListQDSEL.add(elementLookUpObject.get("qdsel").isArray().get(i).isObject());
						}
					}else if(elementLookUpObject.get("qdsel").isObject()!=null){
						codeListQDSEL.add(elementLookUpObject.get("qdsel").isObject());
					}

					if(elementLookUpObject.get("iqdsel").isArray()!=null){
						for(int i=0;i<elementLookUpObject.get("iqdsel").isArray().size();i++){
							codeListQDSEL.add(elementLookUpObject.get("iqdsel").isArray().get(i).isObject());
						}
					}else if(elementLookUpObject.get("iqdsel").isObject()!=null){
						codeListQDSEL.add(elementLookUpObject.get("iqdsel").isObject());
					}

					if(elementLookUpObject.get("measureel").isArray()!=null){
						for(int i=0;i<elementLookUpObject.get("measureel").isArray().size();i++){
							codeListQDSEL.add(elementLookUpObject.get("measureel").isArray().get(i).isObject());
						}
					}else if(elementLookUpObject.get("measureel").isObject()!=null){
						codeListQDSEL.add(elementLookUpObject.get("measureel").isObject());
					}
				}

			}
		}
		
		/*JSONObject[] arr = new JSONObject[codeListQDSEL.size()]; 
		codeListQDSEL.toArray(arr);
		
		
		String container = "{container: {\"qdselArr\": [ \r\n";
		for(JSONObject jObj: codeListQDSEL){
			container += jObj.toString() + " ,";
		}
		container = container.substring(0,container.length()-1);
		container += " ]} }";
		Window.alert(container);
		JSONValue value = (JSONValue) JSONParser.parse(container);
		JSONObject jsonObject = (JSONObject) value.isObject();
		Window.alert(jsonObject.getJavaScriptObject().createArray().toString());
		
		JsArray<JavaScriptObject> jarr = JavaScriptObject.createArray().cast();
		jarr.set(0, codeListQDSEL.get(0).getJavaScriptObject());
		Window.alert(jarr.toSource());
		*/
	}
	
	
	@Override
	public void beforeDisplay() {
		resetQDSFields();
		loadCodeListData();
		
	}

	@Override
	public void beforeClosingDisplay() {
	}

	
}
