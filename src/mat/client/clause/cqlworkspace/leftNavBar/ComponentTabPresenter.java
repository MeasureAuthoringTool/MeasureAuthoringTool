package mat.client.clause.cqlworkspace.leftNavBar;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class ComponentTabPresenter {

	private ComponentTabView view;
	
	public ComponentTabPresenter() {
		view = new ComponentTabView();
		addHandlers();
	}
	
	private void addHandlers() {
		addSuggestBoxClickHandler();
		addSuggestHandler();
		addListBoxHandler();
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
	
	private void addListBoxHandler() {
		view.getListBox().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				System.out.println("listbox change event:" + event.getAssociatedType().getName());
				int selectedIndex = view.getListBox().getSelectedIndex();
				String selectedItem = view.getListBox().getItemText(selectedIndex);
				view.getSuggestBox().setText(selectedItem);
			}
		});

	}
	
//	public void updateComponentsMap() {
//		view.getNameMap().clear();
//		view.getLibraryMap().clear();
//		
//		for (ComponentMeasureTabObject measure : view.getComponentObjects()) {
//			view.getNameMap().put(measure.getComponentId(), measure.getAlias());
//			view.getLibraryMap().put(measure.getComponentId(), measure.getCqlLibrary());
//		}
//		
//		updateNewSuggestComponentOracle();
//		
//		if (view.getComponentObjects().size() < 10) {
//			view.getBadge().setText("0" + view.getComponentObjects().size());
//		} else {
//			view.getBadge().setText("" + view.getComponentObjects().size());
//		}
//	}
	
	private void updateNewSuggestComponentOracle() {
		if (view.getSuggestBox() != null) {
			//CQLSuggestOracle cqlSuggestOracle = new CQLSuggestOracle(view.getNameMap().values());
			
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
	
	
}