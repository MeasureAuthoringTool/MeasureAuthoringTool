package mat.client.clause.cqlworkspace.leftNavBar;

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
	}

	public ComponentTabView getView() {
		return view;
	}

	public void closeSearch() {
		view.getCollapse().getElement().setClassName("panel-collapse collapse");
		view.getSuggestBox().setText("Search");
		view.getSuggestBox().setTitle("Search Component Alias");
	}

	
	public void clickEventOnListBox() {
		view.fireEvent(new DoubleClickEvent(){});
	}
	
	
}