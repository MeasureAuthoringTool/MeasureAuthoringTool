package mat.client.clause.cqlworkspace.leftNavBar.sections;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import mat.client.event.EditCompositeMeasureEvent;
import mat.client.shared.MatContext;

public class CQLComponentLibraryPresenter {
	
	CQLComponentLibraryView view;
	
	public CQLComponentLibraryPresenter() {
		view = new CQLComponentLibraryView();
		addEventHandlers();
	}

	private void addEventHandlers() {
		addUpdateButtonClickHandler();
	}

	
	private void addUpdateButtonClickHandler() {
		view.getUpdateButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				view.getErrorMessageAlert().clearAlert();
				view.getSuccessMessageAlert().clearAlert();
				goToEditCompositeMeasuresScreen();
			}
		});
	}
	
	private void goToEditCompositeMeasuresScreen() {
		EditCompositeMeasureEvent editEvent = new EditCompositeMeasureEvent();
		MatContext.get().getEventBus().fireEvent(editEvent);	
	}
	
	public CQLComponentLibraryView getView() {
		return view;
	}
}