package mat.client.codelist;


import java.util.List;

import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class QDSElementView.
 */
public class QDSElementView implements ManageCodeListDetailPresenter.AddQDSDisplay{
	
	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	
	/** The main panel. */
	private SimplePanel mainPanel = new SimplePanel();
	
	
	/** The data type input. */
	private ListBoxMVP dataTypeInput = new ListBoxMVP();
	
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	
	/** The current code list model. */
	private ManageCodeListDetailModel currentCodeListModel = new ManageCodeListDetailModel();
	
	/** The success messages. */
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	
	/**
	 * Instantiates a new qDS element view.
	 */
	public QDSElementView(){
		mainPanel.setStylePrimaryName("searchResultsContainer");
		mainPanel.addStyleName("leftAligned");

		FlowPanel fPanel = new FlowPanel();
		fPanel.add(successMessages);
		fPanel.add(new SpacerWidget());
		fPanel.add(LabelBuilder.buildLabel(dataTypeInput, "Data Type"));
		fPanel.add(dataTypeInput);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(buttonBar);
		
		mainPanel.add(fPanel);
		containerPanel.setContent(mainPanel);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddQDSDisplay#asWidget()
	 */
	public Widget asWidget() {
		return containerPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddQDSDisplay#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddQDSDisplay#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddQDSDisplay#setTitle(java.lang.String)
	 */
	@Override 
	public void setTitle(String title) {
		containerPanel.setHeading(title,"");
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddQDSDisplay#setCodeListDetailModel(mat.client.codelist.ManageCodeListDetailModel)
	 */
	@Override
	public void setCodeListDetailModel(ManageCodeListDetailModel currentDetails) {
		currentCodeListModel = currentDetails;
		Label codeListNameValue = new Label(currentCodeListModel.getName());
		containerPanel.setCodeListInfo(codeListNameValue);
		
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddQDSDisplay#getDataType()
	 */
	@Override
	public HasValue<String> getDataType() {
		return dataTypeInput;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddQDSDisplay#setDataType(int)
	 */
	@Override
	public void setDataType(int value) {
		
		
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddQDSDisplay#getSuccessMessageDisplay()
	 */
	@Override
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}
	
	/**
	 * Sets the list box items.
	 * 
	 * @param listBox
	 *            the list box
	 * @param itemList
	 *            the item list
	 * @param defaultOption
	 *            the default option
	 */
	private void setListBoxItems(ListBox listBox, List<? extends HasListBox> itemList, String defaultOption){
		listBox.clear();
		listBox.addItem(defaultOption,"");
		if(itemList != null){
			for(HasListBox listBoxContent : itemList){
				listBox.addItem(listBoxContent.getItem(),"" +listBoxContent.getValue());
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddQDSDisplay#setDataTypeOptions(java.util.List)
	 */
	@Override
	public void setDataTypeOptions(List<? extends HasListBox> texts) {
		setListBoxItems(dataTypeInput, texts, MatContext.PLEASE_SELECT);
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.AddQDSDisplay#getQDSDataTypeValue()
	 */
	@Override
	public String getQDSDataTypeValue() {
		return dataTypeInput.getValue(dataTypeInput.getSelectedIndex());
	}

}
