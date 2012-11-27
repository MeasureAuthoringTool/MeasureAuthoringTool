package mat.client.codelist;

import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.SearchView;
import mat.model.Code;

import com.google.gwt.user.client.ui.Widget;

public class CodesSummaryWidget extends SummaryWidgetBase<Code> implements ManageCodeListDetailPresenter.CodesSummaryDisplay{
	
	private ManageCodesSearchView view ;
	public CodesSummaryWidget(){
		super("Code(s)");
	}
	
	@Override
	public String getManageLinkText() {
		return "Manage Codes";
	}
	
	@Override
	public String getNoneAvailableText() {
		return "No Codes Available";
	}
	
	@Override
	public String getSummaryHeaderLabel(){
		return "Code";
	}
	
	@Override
	protected String getValue(Code value) {
		return value.getCode();
	}

	@Override
	protected String getDescription(Code value) {
		return value.getDescription();
	}

	

	@Override 
	public void buildSummaryDataTable(ManageCodesSummaryModel codes,int totalPagesCount, int total,int currentPage){
		view.buildManageCodesDataTable(codes, true, false, total, totalPagesCount, currentPage);
		buildCodesWidget(view.asWidget());
		buildPageSelectionView(totalPagesCount);
		
	}
	@Override
	protected SearchView<?> getSearchView() {
		if(view == null) {
			view = new ManageCodesSearchView();
		}
		return view;
	}

	
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return psv;
	}

	
	@Override
	public int getPageSize() {
		return view.getPageSize();
	}
	
	
	
	@Override
	public void setPageSize(int pageSize) {
		view.setPageSize(pageSize);
	}

	@Override
	public int getCurrentPage() {
		return psv.getCurrentPage();
	}

	@Override
	public void setCurrentPage(int pageNumber) {
		psv.setCurrentPage(pageNumber);
	}

	

}
