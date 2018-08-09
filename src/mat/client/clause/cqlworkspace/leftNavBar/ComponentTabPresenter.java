package mat.client.clause.cqlworkspace.leftNavBar;

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
	
	private String owner = "";
	private String name = "";
	private String content = "";
	
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
				System.out.println("listbox change event:" + event.getAssociatedType().getName());
				int selectedIndex = view.getListBox().getSelectedIndex();
				String selectedItem = view.getListBox().getItemText(selectedIndex);
				view.getSuggestBox().setText(selectedItem);
			}
		});
		
//		view.getListBox().addKeyPressHandler(new KeyPressHandler() {
//			@Override
//			public void onKeyPress(KeyPressEvent event) {
//				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
//					DomEvent.fireNativeEvent(Document.get().createDblClickEvent(view.getListBox().getSelectedIndex(), 0, 0, 0, 0, false, false, false, false), view.getListBox());
//				}
//			}
//		});
//		
//		view.getListBox().addDoubleClickHandler(new DoubleClickHandler() {
//			@Override
//			public void onDoubleClick(DoubleClickEvent event) {
//				int selectedIndex = view.getListBox().getSelectedIndex();
//				if(selectedIndex != -1) {
//					final String selectedComponentMeasureId = view.getListBox().getValue(selectedIndex);
//					view.setSelectedAlias(view.getAliases().get(selectedComponentMeasureId));
//					view.setSelectedOwner(view.getLibOwner().get(selectedComponentMeasureId));
//					view.setSelectedLibraryName(view.getLibName().get(selectedComponentMeasureId));
//					view.setSelectedContent(view.getLibContent().get(selectedComponentMeasureId));
//				}
//			}
//		});
		
	}
	
	public void updateComponentTabInformation() {
		view.getAliases().clear();
		view.getLibContent().clear();
		view.getLibName().clear();
		view.getLibOwner().clear();
		
		updateListOfComponentObjects();
		
		for(ComponentMeasureTabObject measure : view.getComponentObjects()) {
			view.getAliases().put(measure.getComponentId(), measure.getAlias());
			view.getLibContent().put(measure.getComponentId(), measure.getLibContent());
			view.getLibName().put(measure.getComponentId(), measure.getLibName());
			view.getLibOwner().put(measure.getComponentId(), measure.getLibOwner());
		}
		
		updateNewSuggestComponentOracle();
		view.setBadgeNumber();
	}
	
	private void updateNewSuggestComponentOracle() {
		if (view.getSuggestBox() != null) {
			CQLSuggestOracle cqlSuggestOracle = new CQLSuggestOracle(view.getAliases().values());
		}
	}
	
	
	public ComponentTabView getView() {
		return view;
	}

	public void closeSearch() {
		view.getCollapse().getElement().setClassName("panel-collapse collapse");
	}

	public void populateSearchBox() {
		view.clearAndAddToListBox();
	}
	
	private void updateListOfComponentObjects(){
		view.getComponentObjects().clear();
		
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
							owner = result;
							
							//Get the name of the cql library of the component measure
							MatContext.get().getCQLLibraryService().getCQLLibraryNameFromMeasureId(res.getId(), new AsyncCallback<String>() {
								@Override
								public void onFailure(Throwable caught) {}
								@Override
								public void onSuccess(String result) {
									name = result;
									
									//Get the content of the cql library of the component measure
									MatContext.get().getCQLLibraryService().getCQLLibraryContentFromMeasureId(res.getId(), new AsyncCallback<String>() {
										@Override
										public void onFailure(Throwable caught) {}
										@Override
										public void onSuccess(String result) {
											content = result;
											
											//Populate the componentObject list
											ComponentMeasureTabObject obj = new ComponentMeasureTabObject(res.getName(), owner , name , content, res.getId());
											view.getComponentObjects().add(obj);
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