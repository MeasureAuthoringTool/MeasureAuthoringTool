package mat.client.codelist;

import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Label;

/**
 * The Class ManageGroupedCodeListView.
 */
public class ManageGroupedCodeListView extends BaseDetailView
	implements ManageGroupedCodeListPresenter.GroupedCodeListDisplay{
	
	/** The code lists summary. */
	private CodeListsSummaryWidget codeListsSummary;
	
	/**
	 * Instantiates a new manage grouped code list view.
	 * 
	 * @param nameType
	 *            the name type
	 */
	public ManageGroupedCodeListView(String nameType) {
		super(nameType);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.GroupedCodeListDisplay#DisableAnchors()
	 */
	@Override
	public void DisableAnchors() {
		MatContext.get().setVisible(codeListsSummary,false);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.GroupedCodeListDisplay#EnableAnchors()
	 */
	@Override
	public void EnableAnchors() {
		MatContext.get().setVisible(codeListsSummary,true);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.BaseDetailView#getSummaryWidget()
	 */
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
	/* (non-Javadoc)
 * @see mat.client.codelist.ManageGroupedCodeListPresenter.GroupedCodeListDisplay#getAddCodeListButton()
 */
@Override
	public HasClickHandlers getAddCodeListButton() {
		return codeListsSummary.getManageCodeListsAnchor();
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.GroupedCodeListDisplay#setAddCodeListButtonEnabled(boolean)
	 */
	@Override
	public void setAddCodeListButtonEnabled(boolean enabled) {
		codeListsSummary.getManageCodeListsAnchor().setEnabled(enabled);
		if(!enabled)//if not enabled, just display the label,not the link(Read-only link == label)
			codeListsSummary.addManageCodesLabel(new Label("Manage Value Sets"));
		else
			codeListsSummary.addManageCodesLink();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.BaseDetailView#shouldDisplayCodeSystem()
	 */
	@Override
	protected boolean shouldDisplayCodeSystem() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.BaseDetailPresenter.BaseDisplay#getCodeSystemListBox()
	 */
	@Override
	public ListBoxMVP getCodeSystemListBox() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.BaseDetailPresenter.BaseDisplay#setSaveCompleteButtonEnabled(boolean)
	 */
	@Override
	public void setSaveCompleteButtonEnabled(boolean enabled) {
		buttonBar.getSaveCompleteButton().setEnabled(enabled);
		
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.BaseDetailPresenter.BaseDisplay#enableValueSetWidgetsBasedOnDraft(boolean)
	 */
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

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.GroupedCodeListDisplay#setCodeLists(mat.client.codelist.ManageGroupedCodeListsSummaryModel, int, int, int)
	 */
	@Override
	public void setCodeLists(ManageGroupedCodeListsSummaryModel sModel,
			int pageCount, int total, int currentPage) {
		codeListsSummary.buildSummaryDataTable(sModel,pageCount, total, currentPage);
	}

	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.GroupedCodeListDisplay#getCodeListsSummaryWidget()
	 */
	@Override
	public CodeListsSummaryWidget getCodeListsSummaryWidget() {
		return codeListsSummary;
	}
}
