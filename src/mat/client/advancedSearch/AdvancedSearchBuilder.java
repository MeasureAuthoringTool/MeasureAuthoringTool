package mat.client.advancedSearch;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AdvancedSearchBuilder {
	protected AdvancedSearchModel modal;
	protected Anchor anchor;
	protected VerticalPanel panel;
	
	public AdvancedSearchBuilder() {
		createAdvancedSearch();
		initAnchor();
		buildPanel();
	}
	
	private void initAnchor() {
		anchor = new Anchor("Advanced Search");

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
	
	public AdvancedSearchModel getAdvancedSearchModel(){
		return modal;
	}
	
	public VerticalPanel getAdvancedSearchPanel() {
		return panel;
	}
	
	public Widget asWidget() {
		return panel.asWidget();
	}
	
	protected abstract void createAdvancedSearch();

}
