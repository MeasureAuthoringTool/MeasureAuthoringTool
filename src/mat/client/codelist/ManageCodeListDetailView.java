package mat.client.codelist;

import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Label;

/**
 * The Class ManageCodeListDetailView.
 */
public class ManageCodeListDetailView extends BaseDetailView
	implements ManageCodeListDetailPresenter.CodeListDetailDisplay {

	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	
	/** The codes summary. */
	private CodesSummaryWidget codesSummary;

	/**
	 * Instantiates a new manage code list detail view.
	 * 
	 * @param nameType
	 *            the name type
	 */
	public ManageCodeListDetailView(String nameType) {
		super(nameType);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.BaseDetailView#getSummaryWidget()
	 */
	@Override
	protected SummaryWidgetBase<?> getSummaryWidget() {
		if(codesSummary == null) {
			 codesSummary = new CodesSummaryWidget();
		}
		return codesSummary;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.CodeListDetailDisplay#getCodesSummary()
	 */
	@Override
	public CodesSummaryWidget getCodesSummary(){
		return codesSummary;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.CodeListDetailDisplay#DisableAnchors()
	 */
	@Override
	public void DisableAnchors(){
		MatContext.get().setVisible(codesSummary,false);
	}
	

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.CodeListDetailDisplay#getCategoryValue()
	 */
	@Override
	public String getCategoryValue() {
		return categoryInput.getValue(categoryInput.getSelectedIndex());
	}
	

		/* (non-Javadoc)
		 * @see mat.client.codelist.ManageCodeListDetailPresenter.CodeListDetailDisplay#EnableAnchors(mat.client.codelist.ManageCodesSummaryModel, int, int, int)
		 */
		@Override
	public void EnableAnchors(ManageCodesSummaryModel codes,int pageCount,int total,int currentPage) {
		codesSummary.buildSummaryDataTable(codes,pageCount,total,currentPage);
		MatContext.get().setVisible(codesSummary,true);
	}




	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.CodeListDetailDisplay#getTitle()
	 */
	@Override
	public String getTitle() {
		return containerPanel.getTitle();
	}


	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.CodeListDetailDisplay#getAddCodeButton()
	 */
	@Override
	public HasClickHandlers getAddCodeButton() {
		return codesSummary.getManageCodeListsAnchor();
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.BaseDetailView#shouldDisplayCodeSystem()
	 */
	@Override
	protected boolean shouldDisplayCodeSystem() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.CodeListDetailDisplay#setAddCodeButtonEnabled(boolean)
	 */
	@Override
	public void setAddCodeButtonEnabled(boolean enabled) {
		codesSummary.getManageCodeListsAnchor().setEnabled(enabled);
		if(!enabled)//if not enabled, just display the label,not the link(Read-only link == label)
			  codesSummary.addManageCodesLabel(new Label("Manage Codes"));
		else
			  codesSummary.addManageCodesLink();
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.BaseDetailPresenter.BaseDisplay#getCodeSystemListBox()
	 */
	@Override
	public ListBoxMVP getCodeSystemListBox() {
		return codeSystemInput;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.BaseDetailView#getCategoryListBox()
	 */
	@Override
	public ListBoxMVP getCategoryListBox() {
		return categoryInput;
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
		setAddCodeButtonEnabled(isDraft);
		
	}
}
