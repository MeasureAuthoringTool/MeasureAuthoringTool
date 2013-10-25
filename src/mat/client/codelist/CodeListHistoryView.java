package mat.client.codelist;

import mat.client.history.HistoryBaseView;

import com.google.gwt.event.dom.client.HasClickHandlers;

/**
 * The Class CodeListHistoryView.
 */
public class CodeListHistoryView extends HistoryBaseView implements ManageCodeListSearchPresenter.HistoryDisplay{
	
	/** The code list id. */
	private String codeListId;
	
	/** The code list name. */
	private String codeListName;

	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.HistoryDisplay#setCodeListName(java.lang.String)
	 */
	@Override
	public void setCodeListName(String name) {
		this.codeListName = name;
		nameText.setText("History For: " +name);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.HistoryDisplay#getCodeListId()
	 */
	@Override
	public String getCodeListId(){
		return codeListId;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.HistoryDisplay#setCodeListId(java.lang.String)
	 */
	@Override
	public void setCodeListId(String codeListId){
		this.codeListId = codeListId;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.HistoryDisplay#getCodeListName()
	 */
	@Override
	public String getCodeListName() {
		return codeListName;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.HistoryDisplay#setReturnToLinkText(java.lang.String)
	 */
	@Override
	public void setReturnToLinkText(String s) {
		goBackLink.setText(s);
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListSearchPresenter.HistoryDisplay#getReturnToLink()
	 */
	@Override
	public HasClickHandlers getReturnToLink() {
		return goBackLink;
	}
	

	
}
