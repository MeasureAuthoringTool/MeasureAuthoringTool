package mat.client;

import mat.client.shared.SpacerWidget;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class CqlLibraryView.
 */
public class CqlLibraryView implements CqlLibraryPresenter.ViewDisplay{
	
	/** The main panel. */
	private VerticalPanel mainPanel = new VerticalPanel();


	@Override
	public VerticalPanel getMainPanel() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void buildView() {
		mainPanel.setStyleName("cqlLibraryPanel");
		mainPanel.getElement().setAttribute("id", "cContent");
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		
		SimplePanel cqlLibraryHolder = new SimplePanel();
		Label cqlLibraryLabel = new Label("No Libraries to view");
		cqlLibraryHolder.add(cqlLibraryLabel);
		mainPanel.add(cqlLibraryHolder);
		
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		
	}

}
