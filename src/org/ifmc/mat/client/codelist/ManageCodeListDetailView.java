package org.ifmc.mat.client.codelist;

import org.ifmc.mat.client.shared.ContentWithHeadingWidget;
import org.ifmc.mat.client.shared.ListBoxMVP;
import org.ifmc.mat.client.shared.MatContext;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Label;

public class ManageCodeListDetailView extends BaseDetailView
	implements ManageCodeListDetailPresenter.CodeListDetailDisplay {

	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	private CodesSummaryWidget codesSummary;

	public ManageCodeListDetailView(String nameType) {
		super(nameType);
	}
	
	@Override
	protected SummaryWidgetBase<?> getSummaryWidget() {
		if(codesSummary == null) {
			 codesSummary = new CodesSummaryWidget();
		}
		return codesSummary;
	}
	
	@Override
	public CodesSummaryWidget getCodesSummary(){
		return codesSummary;
	}
	
	@Override
	public void DisableAnchors(){
		MatContext.get().setVisible(codesSummary,false);
	}
	

	@Override
	public String getCategoryValue() {
		return categoryInput.getValue(categoryInput.getSelectedIndex());
	}
	

		@Override
	public void EnableAnchors(ManageCodesSummaryModel codes,int pageCount,int total,int currentPage) {
		codesSummary.buildSummaryDataTable(codes,pageCount,total,currentPage);
		MatContext.get().setVisible(codesSummary,true);
	}




	@Override
	public String getTitle() {
		return containerPanel.getTitle();
	}


	@Override
	public HasClickHandlers getAddCodeButton() {
		return codesSummary.getManageCodeListsAnchor();
	}

	@Override
	protected boolean shouldDisplayCodeSystem() {
		return true;
	}
	@Override
	public void setAddCodeButtonEnabled(boolean enabled) {
		codesSummary.getManageCodeListsAnchor().setEnabled(enabled);
		if(!enabled)//if not enabled, just display the label,not the link(Read-only link == label)
			  codesSummary.addManageCodesLabel(new Label("Manage Codes"));
		else
			  codesSummary.addManageCodesLink();
	}

	@Override
	public ListBoxMVP getCodeSystemListBox() {
		return codeSystemInput;
	}

	@Override
	public ListBoxMVP getCategoryListBox() {
		return categoryInput;
	}

	@Override
	public void setSaveCompleteButtonEnabled(boolean enabled) {
		buttonBar.getSaveCompleteButton().setEnabled(enabled);
	}

	@Override
	public void enableValueSetWidgetsBasedOnDraft(boolean isDraft) {
		
		nameInput.setEnabled(isDraft);
		stewardInput.setEnabled(isDraft);
		stewardOtherInput.setEnabled(isDraft);
		codeSystemInput.setEnabled(isDraft);
		codeSystemVersionInput.setEnabled(isDraft);
		codeListOidInput.setEnabled(isDraft);
		setOIDButtonEnabled(isDraft);
		rationaleInput.setEnabled(isDraft);
		commentsInput.setEnabled(isDraft);
		setSaveButtonEnabled(isDraft);
		setAddCodeButtonEnabled(isDraft);
		
	}
}
