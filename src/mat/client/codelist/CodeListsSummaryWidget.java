package mat.client.codelist;

import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.SearchView;
import mat.model.GroupedCodeListDTO;

import com.google.gwt.user.client.ui.Label;

public class CodeListsSummaryWidget extends SummaryWidgetBase<GroupedCodeListDTO>implements ManageGroupedCodeListPresenter.CodeListsSummaryDisplay{
	
	private ManageCodesSearchView view ;
	
	private Label summaryHeaderLabel = new Label(getSummaryHeaderLabel()); 
	
	public CodeListsSummaryWidget(){
		super("Value Sets");
	}
	
	@Override
	public String getManageLinkText() {
		return "Manage Value Sets";
	}
	
	@Override
	public String getNoneAvailableText() {
		return "No Value Sets Available";
	}

	@Override
	public String getSummaryHeaderLabel(){
		return "Value Set";
	}
	@Override
	protected String getValue(GroupedCodeListDTO value) {
		return value.getName();
	}

	@Override
	protected String getDescription(GroupedCodeListDTO value) {
		return value.getDescription();
	}
	
	
	@Override
	protected SearchView<?> getSearchView() {
		if(view == null) {
			view = new ManageCodesSearchView();
		}
		return view;
	}

	@Override
	public void buildSummaryDataTable(ManageGroupedCodeListsSummaryModel codes,
			int totalPagesCount, int total, int currentPage) {
		view.buildManageCodesDataTable(codes, true, false, total, totalPagesCount, currentPage);
		buildCodesWidget(view.asWidget());
		buildPageSelectionView(totalPagesCount);
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