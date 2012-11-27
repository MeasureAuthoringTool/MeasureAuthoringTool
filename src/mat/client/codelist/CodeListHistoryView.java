package mat.client.codelist;

import mat.client.history.HistoryBaseView;

import com.google.gwt.event.dom.client.HasClickHandlers;

public class CodeListHistoryView extends HistoryBaseView implements ManageCodeListSearchPresenter.HistoryDisplay{
	
	private String codeListId;
	private String codeListName;

	
	@Override
	public void setCodeListName(String name) {
		this.codeListName = name;
		nameText.setText("History For: " +name);
	}
	
	@Override
	public String getCodeListId(){
		return codeListId;
	}
	
	@Override
	public void setCodeListId(String codeListId){
		this.codeListId = codeListId;
	}

	@Override
	public String getCodeListName() {
		return codeListName;
	}

	@Override
	public void setReturnToLinkText(String s) {
		goBackLink.setText(s);
	}

	@Override
	public HasClickHandlers getReturnToLink() {
		return goBackLink;
	}
	

	
}
