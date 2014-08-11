/*package mat.client.codelist;

import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.SearchView;
import mat.model.Code;

*//**
 * The Class CodesSummaryWidget.
 *//*
public class CodesSummaryWidget extends SummaryWidgetBase<Code> implements ManageCodeListDetailPresenter.CodesSummaryDisplay{
	
	*//** The view. *//*
	private ManageCodesSearchView view ;
	
	*//**
	 * Instantiates a new codes summary widget.
	 *//*
	public CodesSummaryWidget(){
		super("Code(s)");
	}
	
	 (non-Javadoc)
	 * @see mat.client.codelist.SummaryWidgetBase#getManageLinkText()
	 
	@Override
	public String getManageLinkText() {
		return "Manage Codes";
	}
	
	 (non-Javadoc)
	 * @see mat.client.codelist.SummaryWidgetBase#getNoneAvailableText()
	 
	@Override
	public String getNoneAvailableText() {
		return "No Codes Available";
	}
	
	 (non-Javadoc)
	 * @see mat.client.codelist.SummaryWidgetBase#getSummaryHeaderLabel()
	 
	@Override
	public String getSummaryHeaderLabel(){
		return "Code";
	}
	
	 (non-Javadoc)
	 * @see mat.client.codelist.SummaryWidgetBase#getValue(java.lang.Object)
	 
	@Override
	protected String getValue(Code value) {
		return value.getCode();
	}

	 (non-Javadoc)
	 * @see mat.client.codelist.SummaryWidgetBase#getDescription(java.lang.Object)
	 
	@Override
	protected String getDescription(Code value) {
		return value.getDescription();
	}

	

	 (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.CodesSummaryDisplay#buildSummaryDataTable(mat.client.codelist.ManageCodesSummaryModel, int, int, int)
	 
	@Override 
	public void buildSummaryDataTable(ManageCodesSummaryModel codes,int totalPagesCount, int total,int currentPage){
		view.buildManageCodesDataTable(codes, true, false, total, totalPagesCount, currentPage);
		buildCodesWidget(view.asWidget());
		buildPageSelectionView(totalPagesCount);
		
	}
	
	 (non-Javadoc)
	 * @see mat.client.codelist.SummaryWidgetBase#getSearchView()
	 
	@Override
	protected SearchView<?> getSearchView() {
		if(view == null) {
			view = new ManageCodesSearchView();
		}
		return view;
	}

	
	 (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.CodesSummaryDisplay#getPageSelectionTool()
	 
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return psv;
	}

	
	 (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.CodesSummaryDisplay#getPageSize()
	 
	@Override
	public int getPageSize() {
		return view.getPageSize();
	}
	
	
	
	 (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.CodesSummaryDisplay#setPageSize(int)
	 
	@Override
	public void setPageSize(int pageSize) {
		view.setPageSize(pageSize);
	}

	 (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.CodesSummaryDisplay#getCurrentPage()
	 
	@Override
	public int getCurrentPage() {
		return psv.getCurrentPage();
	}

	 (non-Javadoc)
	 * @see mat.client.codelist.ManageCodeListDetailPresenter.CodesSummaryDisplay#setCurrentPage(int)
	 
	@Override
	public void setCurrentPage(int pageNumber) {
		psv.setCurrentPage(pageNumber);
	}

	

}
*/