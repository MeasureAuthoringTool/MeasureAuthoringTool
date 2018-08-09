package mat.client.clause.cqlworkspace.leftNavBar;

import com.google.gwt.core.client.GWT;
import java.util.List;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.CQLSuggestOracle;
import mat.client.shared.ComponentMeasureTabObject;
import mat.client.shared.MatContext;

public class ComponentTabPresenter {

	private ComponentTabView view;
	
	private String libOwner = "";
	private String libName = "";
	private String libContent = "";
	
	public ComponentTabPresenter() {
		view = new ComponentTabView();
		addHandlers();
	}
	
	private void addHandlers() {
		addSuggestBoxClickHandler();
		addSuggestHandler();
		addListBoxHandlers();
		addDoubleClickHandler();
	}
	
	
	private void addDoubleClickHandler() {
		// Double Click causing issues.So Event is not propogated
		view.getAnchor().addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				event.stopPropagation();
			}
		});
	}
	
	private void addSuggestBoxClickHandler() {
		view.getSuggestBox().getValueBox().addClickHandler(new ClickHandler() {
	
			@Override
			public void onClick(ClickEvent event) {
				if ("Search".equals(view.getSuggestBox().getText())) {
					view.getSuggestBox().setText("");
				}
			}
		});
	}
	
	private void addSuggestHandler() {
		view.getSuggestBox().addSelectionHandler(new SelectionHandler<Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String selectedQDMName = event.getSelectedItem().getReplacementString();
				for (int i = 0; i < view.getListBox().getItemCount(); i++) {
					if (selectedQDMName.equals(view.getListBox().getItemText(i))) {
						view.getListBox().setItemSelected(i, true);
						view.getListBox().setFocus(true);
						break;
					}
				}
			}
		});
	}
	
	private void addListBoxHandlers() {
		view.getListBox().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int selectedIndex = view.getListBox().getSelectedIndex();
				String selectedItem = view.getListBox().getItemText(selectedIndex);
				view.getSuggestBox().setText(selectedItem);
			}
		});
		
		view.getListBox().addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					DomEvent.fireNativeEvent(Document.get().createDblClickEvent(view.getListBox().getSelectedIndex(), 0, 0, 0, 0, false, false, false, false), view.getListBox());
				}
			}
		});
		
<<<<<<< HEAD
	}
	
	public void populateComponentInformation() {
		int selectedIndex = view.getListBox().getSelectedIndex();
		if(selectedIndex != -1) {
			final String selectedComponentMeasureId = view.getListBox().getValue(selectedIndex);
			view.setSelectedAlias(view.getComponentObjectsMap().get(selectedComponentMeasureId).getMeasureName());
			view.setSelectedOwner(view.getComponentObjectsMap().get(selectedComponentMeasureId).getOwnerName());
			view.setSelectedLibraryName(view.getComponentObjectsMap().get(selectedComponentMeasureId).getLibraryName());
			view.setSelectedContent(view.getComponentObjectsMap().get(selectedComponentMeasureId).getLibraryContent());
		}
=======
	}
	
	public void populateComponentInformation() {
		int selectedIndex = view.getListBox().getSelectedIndex();
		if(selectedIndex != -1) {
			final String selectedComponentMeasureId = view.getListBox().getValue(selectedIndex);
			view.setSelectedAlias(view.getComponentObjectsMap().get(selectedComponentMeasureId).getAlias());
			view.setSelectedOwner("owner");//TODO view.getComponentObjectsMap().get(selectedComponentMeasureId).getLibOwner());
			view.setSelectedLibraryName(view.getComponentObjectsMap().get(selectedComponentMeasureId).getLibName());
			view.setSelectedContent(view.getComponentObjectsMap().get(selectedComponentMeasureId).getLibContent());
		}
	}
	
	public void updateComponentTabInformation() {
		view.getComponentObjectsList().clear();
		view.getComponentObjectsMap().clear();
		
		updateListOfComponentObjects();

		updateNewSuggestComponentOracle();
		view.setBadgeNumber();
	}
	
	private void updateNewSuggestComponentOracle() {
		view.setSuggestBox(new CQLSuggestOracle(view.getAliases().values()));
>>>>>>> 9302: TODO- direct update button to the newly made edit composite measure screen; fix issue with getting library owner name; get the cql library name, not the measure library name; put alias in list box/search box; get cql library as a string, not as a blob; fix method to remove component tab if not composite (checkCompositeMeasure() in CQLWorkspacePresenter)
	}

	public ComponentTabView getView() {
		return view;
	}

	public void closeSearch() {
		view.getCollapse().getElement().setClassName("panel-collapse collapse");
		view.getListBox().clear();
	}

	
	public void clickEventOnListBox() {
		view.fireEvent(new DoubleClickEvent(){});
	}
	
	private void updateListOfComponentObjects(){
		//Get list of component measures
		MatContext.get().getMeasureService().getComponentMeasures(MatContext.get().getCurrentMeasureId(), new AsyncCallback<ManageMeasureSearchModel>() {
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(ManageMeasureSearchModel result) {
				List<Result> componentMeasures = result.getData();
				for(Result res : componentMeasures) {
					
					//get the owner name of the cql library of the component measure
					MatContext.get().getCQLLibraryService().getCQLLibraryOwnerNameFromMeasureId(res.getId(), new AsyncCallback<String>() {
						@Override
						public void onFailure(Throwable caught) {}
						@Override
						public void onSuccess(String result) {
							libOwner = result;
							
							//Get the name of the cql library of the component measure
							MatContext.get().getCQLLibraryService().getCQLLibraryNameFromMeasureId(res.getId(), new AsyncCallback<String>() {
								@Override
								public void onFailure(Throwable caught) {}
								@Override
								public void onSuccess(String result) {
									libName = result;
									
									//Get the content of the cql library of the component measure
									MatContext.get().getCQLLibraryService().getCQLLibraryContentFromMeasureId(res.getId(), new AsyncCallback<String>() {
										@Override
										public void onFailure(Throwable caught) {}
										@Override
										public void onSuccess(String result) {
											libContent = result;
											
											//Populate the componentObject list
											ComponentMeasureTabObject obj = new ComponentMeasureTabObject(res.getName(), libOwner , libName , libContent, res.getId());
											view.getComponentObjectsList().add(obj);
											view.getComponentObjectsMap().put(obj.getComponentId(), obj);
											view.clearAndAddToListBox();
										}
									});
								}
							});
						}
					});
				}
			}
		});
	}
	
	
	public void clickEventOnListBox() {
		view.fireEvent(new DoubleClickEvent(){});
	}
	
	
}