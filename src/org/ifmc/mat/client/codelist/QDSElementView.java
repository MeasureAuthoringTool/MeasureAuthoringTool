package org.ifmc.mat.client.codelist;


import java.util.List;

import org.ifmc.mat.client.shared.ContentWithHeadingWidget;
import org.ifmc.mat.client.shared.LabelBuilder;
import org.ifmc.mat.client.shared.ListBoxMVP;
import org.ifmc.mat.client.shared.MatContext;
import org.ifmc.mat.client.shared.SaveCancelButtonBar;
import org.ifmc.mat.client.shared.SpacerWidget;
import org.ifmc.mat.client.shared.SuccessMessageDisplay;
import org.ifmc.mat.client.shared.SuccessMessageDisplayInterface;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class QDSElementView implements ManageCodeListDetailPresenter.AddQDSDisplay{
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	private SimplePanel mainPanel = new SimplePanel();
	
	
	private ListBoxMVP dataTypeInput = new ListBoxMVP();
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	private ManageCodeListDetailModel currentCodeListModel = new ManageCodeListDetailModel();
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	
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
	
	public Widget asWidget() {
		return containerPanel;
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}

	@Override 
	public void setTitle(String title) {
		containerPanel.setHeading(title,"");
	}

	@Override
	public void setCodeListDetailModel(ManageCodeListDetailModel currentDetails) {
		currentCodeListModel = currentDetails;
		Label codeListNameValue = new Label(currentCodeListModel.getName());
		containerPanel.setCodeListInfo(codeListNameValue);
		
	}

	@Override
	public HasValue<String> getDataType() {
		return dataTypeInput;
	}

	@Override
	public void setDataType(int value) {
		
		
	}

	@Override
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}
	
	private void setListBoxItems(ListBox listBox, List<? extends HasListBox> itemList, String defaultOption){
		listBox.clear();
		listBox.addItem(defaultOption,"");
		if(itemList != null){
			for(HasListBox listBoxContent : itemList){
				listBox.addItem(listBoxContent.getItem(),"" +listBoxContent.getValue());
			}
		}
	}
	
	@Override
	public void setDataTypeOptions(List<? extends HasListBox> texts) {
		setListBoxItems(dataTypeInput, texts, MatContext.PLEASE_SELECT);
	}

	@Override
	public String getQDSDataTypeValue() {
		return dataTypeInput.getValue(dataTypeInput.getSelectedIndex());
	}

}
