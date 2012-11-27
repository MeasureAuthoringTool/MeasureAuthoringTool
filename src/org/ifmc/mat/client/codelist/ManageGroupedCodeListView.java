package org.ifmc.mat.client.codelist;

import org.ifmc.mat.client.shared.ListBoxMVP;
import org.ifmc.mat.client.shared.MatContext;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Label;

public class ManageGroupedCodeListView extends BaseDetailView
	implements ManageGroupedCodeListPresenter.GroupedCodeListDisplay{
	
	private CodeListsSummaryWidget codeListsSummary;
	
	public ManageGroupedCodeListView(String nameType) {
		super(nameType);
	}
	
	@Override
	public void DisableAnchors() {
		MatContext.get().setVisible(codeListsSummary,false);
	}
	@Override
	public void EnableAnchors() {
		MatContext.get().setVisible(codeListsSummary,true);
	}
	@Override
	protected SummaryWidgetBase<?> getSummaryWidget() {
		if(codeListsSummary == null) {
			codeListsSummary = new CodeListsSummaryWidget();
		}
		return codeListsSummary;
	}
//	@Override 
//	public void setCodeLists(List<GroupedCodeListDTO> codeLists) {
//		//codeListsSummary.setCodesSummaryList(codeLists);
//		codeListsSummary.buildSummaryDataTable(codes, totalPagesCount, total, currentPage)
//	}
	@Override
	public HasClickHandlers getAddCodeListButton() {
		return codeListsSummary.getManageCodeListsAnchor();
	}

	@Override
	public void setAddCodeListButtonEnabled(boolean enabled) {
		codeListsSummary.getManageCodeListsAnchor().setEnabled(enabled);
		if(!enabled)//if not enabled, just display the label,not the link(Read-only link == label)
			codeListsSummary.addManageCodesLabel(new Label("Manage Value Sets"));
		else
			codeListsSummary.addManageCodesLink();
	}
	@Override
	protected boolean shouldDisplayCodeSystem() {
		return false;
	}
	@Override
	public ListBoxMVP getCodeSystemListBox() {
		// TODO Auto-generated method stub
		return null;
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
		setAddCodeListButtonEnabled(isDraft);
		
	}

	@Override
	public void setCodeLists(ManageGroupedCodeListsSummaryModel sModel,
			int pageCount, int total, int currentPage) {
		codeListsSummary.buildSummaryDataTable(sModel,pageCount, total, currentPage);
	}

	
	@Override
	public CodeListsSummaryWidget getCodeListsSummaryWidget() {
		return codeListsSummary;
	}
}
