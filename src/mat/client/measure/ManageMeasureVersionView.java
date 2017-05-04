/**
 * 
 */
package mat.client.measure;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MessageAlert;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;

/**
 * The Class ManageMeasureVersionView.
 * 
 * @author vandavar
 * 
 *         An view class to manage the widgets for the Version creation.
 */
public class ManageMeasureVersionView implements ManageMeasurePresenter.VersionDisplay {
	
	private ManageMeasureSearchModel.Result selectedMeasure;
	
	/** CellTable Page Size. */
	private static final int PAGE_SIZE = 25;
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar("measuVersion");
	
	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	
	/** The error messages. */
	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	
	/** The major radio. */
	private RadioButton majorRadio = new RadioButton("group", "Major");
	
	/** The minor radio. */
	private RadioButton minorRadio = new RadioButton("group", "Minor");
	
	
	
	/**
	 * Instantiates a new manage measure version view.
	 */
	public ManageMeasureVersionView() {
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(errorMessages);
		errorMessages.getElement().setId("errorMessages_ErrorMessageDisplay");
		
		VerticalPanel radioPanel = new VerticalPanel();
		radioPanel.getElement().getStyle().setMarginLeft(5, Unit.PX);
		radioPanel.add(new Label("Select Version Type"));
		radioPanel.add(new SpacerWidget());
		radioPanel.add(majorRadio);
		majorRadio.getElement().setId("majorRadio_RadioButton");
		radioPanel.add(minorRadio);
		minorRadio.getElement().setId("minorRadio_RadioButton");
		mainPanel.add(cellTablePanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(radioPanel);
		mainPanel.add(new SpacerWidget());
		
		SimplePanel buttonPanel = new SimplePanel();
		buttonBar.getSaveButton().setText("Save and Continue");
		buttonBar.getSaveButton().setTitle("Save and Continue");
		buttonBar.getCancelButton().setTitle("Cancel");
		buttonPanel.add(buttonBar);
		buttonPanel.setWidth("100%");
		mainPanel.add(buttonPanel);
		
	}
	
	public void buildHTML(){
		cellTablePanel.clear();
		if(selectedMeasure != null){
			String selectedItemName = selectedMeasure.getName();
			String selectedItemDraftText = selectedMeasure.getVersion();
			StringBuilder paragraph = new StringBuilder("<p>You are creating version of <b>\""+ selectedItemName + " " + selectedItemDraftText +"\"</b>");
			paragraph.append("</p>");
			HTML paragraphHtml = new HTML(paragraph.toString());
			cellTablePanel.add(paragraphHtml);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		buildHTML();
		return mainPanel;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#buildDataTable(mat.client.shared.search.SearchResults)
	 */
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#getErrorMessageDisplay()
	 */
	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getMajorRadioButton()
	 */
	@Override
	public RadioButton getMajorRadioButton() {
		return majorRadio;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getMinorRadioButton()
	 */
	@Override
	public RadioButton getMinorRadioButton() {
		return minorRadio;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.VersionDisplay#getSelectedMeasure()
	 */
	@Override
	public Result getSelectedMeasure() {
		return selectedMeasure;
	}
	@Override
	public void setSelectedMeasure(ManageMeasureSearchModel.Result selectedMeasure) {
		this.selectedMeasure = selectedMeasure;
	}

	
	
}
