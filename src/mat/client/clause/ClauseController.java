package mat.client.clause;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ClauseController {
	private SimplePanel contents = new SimplePanel();
	private QDSCodeListSearchPresenter codeListSearchPresenter;
	
	public ClauseController(){
		QDSCodeListSearchView qdsCodeList = new QDSCodeListSearchView();
		codeListSearchPresenter = new QDSCodeListSearchPresenter(qdsCodeList);
		contents.setHeight("100%");
	}

	public void displaySearch(){
		contents.clear();
		codeListSearchPresenter.resetQDSFields();
		codeListSearchPresenter.loadCodeListData();
		contents.add(codeListSearchPresenter.getWidget());
	}
	
	public Widget getWidget(){
		return contents;
	}
	
	public QDSCodeListSearchPresenter getQDSCodeListSearchPresenter(){
		return codeListSearchPresenter;
	}

}
