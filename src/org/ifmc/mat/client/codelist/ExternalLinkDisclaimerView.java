package org.ifmc.mat.client.codelist;

import org.ifmc.mat.client.ImageResources;
import org.ifmc.mat.client.shared.ContentWithHeadingWidget;
import org.ifmc.mat.client.shared.FocusableImageButton;
import org.ifmc.mat.client.shared.SaveCancelButtonBar;
import org.ifmc.mat.client.shared.SpacerWidget;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ExternalLinkDisclaimerView implements ManageCodeListDetailPresenter.ExternalLinkDisclaimerDisplay{

	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	private SimplePanel mainPanel = new SimplePanel();
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	
	public ExternalLinkDisclaimerView(){
		mainPanel.setStylePrimaryName("searchResultsContainer");
		mainPanel.addStyleName("leftAligned");
		VerticalPanel mainContentVP = new VerticalPanel();
		HTML header = new HTML("<b>External Link Disclaimer</b>");
		HTML disclaimer = new HTML("<p>You are leaving the ### Measure Authoring Tool and entering another Web site. ### cannot attest to the accuracy of information provided by linked sites. You will be subject to the destination site's Privacy Policy when you leave the  ### Measure Authoring Tool.</p>");
		mainContentVP.add(new SpacerWidget());
		mainContentVP.add(header);
		mainContentVP.add(new SpacerWidget());
		mainContentVP.add(disclaimer);
		mainContentVP.add(new SpacerWidget());
		mainContentVP.add(new SpacerWidget());
	    HorizontalPanel hp = new HorizontalPanel();
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
	
	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public HasClickHandlers getYesButton() {
		return buttonBar.getSaveButton();
	}

	@Override
	public HasClickHandlers getNoButton() {
		return buttonBar.getCancelButton();
	}

}
