package mat.client.codelist;

import mat.client.ImageResources;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.FocusableImageButton;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ExternalLinkDisclaimerView.
 */
public class ExternalLinkDisclaimerView implements ManageCodeListDetailPresenter.ExternalLinkDisclaimerDisplay{

	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	
	/** The main panel. */
	private SimplePanel mainPanel = new SimplePanel();
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	
	/**
	 * Instantiates a new external link disclaimer view.
	 */
	public ExternalLinkDisclaimerView(){
		mainPanel.setStylePrimaryName("searchResultsContainer");
		mainPanel.addStyleName("leftAligned");
		VerticalPanel mainContentVP = new VerticalPanel();
		HTML header = new HTML("<b>External Link Disclaimer</b>");
		HTML disclaimer = new HTML("<p>You are leaving the eMeasure Measure Authoring Tool and entering another Web site. ### cannot attest to the accuracy of information provided by linked sites. You will be subject to the destination site's Privacy Policy when you leave the  ### Measure Authoring Tool.</p>");
		mainContentVP.add(new SpacerWidget());
		mainContentVP.add(header);
		mainContentVP.add(new SpacerWidget());
		mainContentVP.add(disclaimer);
		mainContentVP.add(new SpacerWidget());
		mainContentVP.add(new SpacerWidget());
	    HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setId("hp_HorizontalPanel");
	    hp.add(new HTML("&nbsp;"));
	    hp.add(new FocusableImageButton(ImageResources.INSTANCE.icon_newWindow(),"Excel Viewer"));
	    hp.add(new HTML("&nbsp;"));
	    hp.add(new Label("Would you like to continue to Excel Viewer download page?"));
	    mainContentVP.add(hp);
	    mainContentVP.add(new SpacerWidget());
	    buttonBar.getSaveButton().setText("Yes");
	    buttonBar.getCancelButton().setText("No");
	    mainContentVP.add(buttonBar);
	    mainPanel.add(mainContentVP);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.ExternalLinkDisclaimerDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.ExternalLinkDisclaimerDisplay#getYesButton()
	 */
	@Override
	public HasClickHandlers getYesButton() {
		return buttonBar.getSaveButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.ExternalLinkDisclaimerDisplay#getNoButton()
	 */
	@Override
	public HasClickHandlers getNoButton() {
		return buttonBar.getCancelButton();
	}

}
