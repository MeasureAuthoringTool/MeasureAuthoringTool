package mat.client.codelist;

import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.SearchView;
import mat.model.GroupedCodeListDTO;

import com.google.gwt.user.client.ui.Label;

/**
 * The Class CodeListsSummaryWidget.
 */
public class CodeListsSummaryWidget extends SummaryWidgetBase<GroupedCodeListDTO>implements ManageGroupedCodeListPresenter.CodeListsSummaryDisplay{
	
	/** The view. */
	private ManageCodesSearchView view ;
	
	/** The summary header label. */
	private Label summaryHeaderLabel = new Label(getSummaryHeaderLabel()); 
	
	/**
	 * Instantiates a new code lists summary widget.
	 */
	public CodeListsSummaryWidget(){
		super("Value Sets");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.SummaryWidgetBase#getManageLinkText()
	 */
	@Override
	public String getManageLinkText() {
		return "Manage Value Sets";
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.SummaryWidgetBase#getNoneAvailableText()
	 */
	@Override
	public String getNoneAvailableText() {
		return "No Value Sets Available";
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.SummaryWidgetBase#getSummaryHeaderLabel()
	 */
	@Override
	public String getSummaryHeaderLabel(){
		return "Value Set";
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.SummaryWidgetBase#getValue(java.lang.Object)
	 */
	@Override
	protected String getValue(GroupedCodeListDTO value) {
		return value.getName();
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.SummaryWidgetBase#getDescription(java.lang.Object)
	 */
	@Override
	protected String getDescription(GroupedCodeListDTO value) {
		return value.getDescription();
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.SummaryWidgetBase#getSearchView()
	 */
	@Override
	protected SearchView<?> getSearchView() {
		if(view == null) {
			view = new ManageCodesSearchView();
		}
		return view;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.CodeListsSummaryDisplay#buildSummaryDataTable(mat.client.codelist.ManageGroupedCodeListsSummaryModel, int, int, int)
	 */
	@Override
	public void buildSummaryDataTable(ManageGroupedCodeListsSummaryModel codes,
			int totalPagesCount, int total, int currentPage) {
		view.buildManageCodesDataTable(codes, true, false, total, totalPagesCount, currentPage);
		buildCodesWidget(view.asWidget());
		buildPageSelectionView(totalPagesCount);
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.CodeListsSummaryDisplay#getPageSelectionTool()
	 */
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return psv;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.CodeListsSummaryDisplay#getPageSize()
	 */
	@Override
	public int getPageSize() {
		return view.getPageSize();
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.CodeListsSummaryDisplay#setPageSize(int)
	 */
	@Override
	public void setPageSize(int pageSize) {
		view.setPageSize(pageSize);
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.CodeListsSummaryDisplay#getCurrentPage()
	 */
	@Override
	public int getCurrentPage() {
		return psv.getCurrentPage();
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.CodeListsSummaryDisplay#setCurrentPage(int)
	 */
	@Override
	public void setCurrentPage(int pageNumber) {
		psv.setCurrentPage(pageNumber);
	}
  	
}