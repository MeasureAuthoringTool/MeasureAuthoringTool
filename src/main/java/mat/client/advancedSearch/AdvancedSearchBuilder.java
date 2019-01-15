package mat.client.advancedSearch;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.shared.MeasureSearchModel;

public abstract class AdvancedSearchBuilder {
	private AdvancedSearchModal modal;
	private Anchor anchor;
	private VerticalPanel panel;
	
	public AdvancedSearchBuilder() {
		createAdvancedSearch();
		initAnchor();
		buildPanel();
	}
	
	private void initAnchor() {
		

		anchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				modal.showAdvanceSearch();
			}
		});
	}
	
	private void buildPanel() {
		panel = new VerticalPanel();
		panel.add(anchor);
		panel.setStylePrimaryName("advanceSearch");
	}
	
	public AdvancedSearchModal getModal() {
		return modal;
	}
	
	public void setModal(AdvancedSearchModal modal) {
		this.modal = modal;
	}
	
	public Widget asWidget() {
		return panel.asWidget();
	}
	
	protected abstract void createAdvancedSearch();
	
	protected abstract MeasureSearchModel generateAdvancedSearchModel();

}
