package mat.client.codelist;


import java.util.Collections;
import java.util.List;

import mat.DTO.HasListBoxDTO;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.PrimaryButton;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasSelectAllHandler;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.model.GroupedCodeListDTO;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class AddCodeListView.
 */
public class AddCodeListView extends AddBaseView implements  ManageGroupedCodeListPresenter.AddCodeListDisplay{
	
	/** The code input. */
	private ListBoxMVP codeInput;
	
	/** The view. */
	private ManageCodesSearchView view;
	
	/** The search button. */
	private Button searchButton;
	
	/**
	 * Instantiates a new adds the code list view.
	 */
	public AddCodeListView(){
		super("Add Value Set");
	}
	
	/**
	 * Gets the vSSF panel.
	 * 
	 * @return the vSSF panel
	 */
	public Widget getVSSFPanel(){
		return vssfp.getPanel();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.AddBaseView#getValueInput()
	 */
	@Override
	protected Widget getValueInput() {
		if(codeInput == null) {
			codeInput = new ListBoxMVP(true, 10);
			//codeInput.setMaxWidth(50);
		}
		
		return codeInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.AddBaseView#getValueInputLabel()
	 */
	@Override
	protected String getValueInputLabel() {
		return "Select Value Set";
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.AddBaseView#getSearchView()
	 */
	@Override
	protected SearchView<?> getSearchView() {
		if(view == null) {
			view = new ManageCodesSearchView();
		}
		return view;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.BaseDetailPresenter.BaseAddDisplay#buildDataTable(mat.client.shared.search.SearchResults, boolean, int, int, int)
	 */
	@Override
	public void buildDataTable(SearchResults<GroupedCodeListDTO> codeLists,boolean isChecked,int totalResutls, int totalPages,int currentPage) {		
		view.buildManageCodesDataTable(codeLists, true, isChecked, totalResutls, totalPages, currentPage);
		buildPageSelectionView(totalPages);
		
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.AddCodeListDisplay#getCodeId()
	 */
	@Override
	public HasValue<String> getCodeId() {
		return codeInput;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.AddCodeListDisplay#setCodeListOptions(java.util.List)
	 */
	@Override
	public void setCodeListOptions(List<? extends HasListBox> texts) {
		codeInput.clear();
		Collections.sort(texts,new HasListBox.Comparator());
		int i =0;
		for(HasListBox option : texts) {
			String label = option.getItem();
			if(option instanceof HasListBoxDTO){
				String s = ((HasListBoxDTO)option).getTitle();
				codeInput.insertItem(label, option.getValue(), label+"-"+((HasListBoxDTO)option).getTitle(),i++);
			}else{
				codeInput.addItem(label, option.getValue());
			}
		}
		codeInput.getElement().setAttribute("role", "alert");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.AddCodeListDisplay#setCodeListName(java.lang.String)
	 */
	@Override
	public void setCodeListName(String name) {
		setParentName("Grouped Value Set Name:", name);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.AddCodeListDisplay#getCodeListId()
	 */
	@Override
	public String getCodeListId() {
		return codeInput.getValue(codeInput.getSelectedIndex());
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.AddCodeListDisplay#getCodeListName()
	 */
	@Override
	public String getCodeListName(){
		return codeInput.getItemText(codeInput.getSelectedIndex());
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.BaseDetailPresenter.BaseAddDisplay#getSelectAllTool()
	 */
	@Override
	public HasSelectAllHandler getSelectAllTool() {
		return view;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.AddCodeListDisplay#getCodeListInput()
	 */
	@Override
	public ListBoxMVP getCodeListInput(){
		return codeInput;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.BaseDetailPresenter.BaseAddDisplay#getPageSelectionTool()
	 */
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return psv;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.AddCodeListDisplay#getCurrentPage()
	 */
	@Override
	public int getCurrentPage() {
		return psv.getCurrentPage();
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.AddCodeListDisplay#setCurrentPage(int)
	 */
	@Override
	public void setCurrentPage(int pageNumber) {
		psv.setCurrentPage(pageNumber);
	}
	
	/**
	 * Builds the page selection view.
	 * 
	 * @param totalPagesCount
	 *            the total pages count
	 */
	private void buildPageSelectionView(int totalPagesCount){
		psv.buildPageSelector(totalPagesCount);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.AddCodeListDisplay#getCodeListOid()
	 */
	@Override
	public String getCodeListOid(){
		return codeInput.getItemTitle(codeInput.getSelectedIndex());
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.AddBaseView#getSearchButton()
	 */
	@Override
	public Button getSearchButton() {
		if(searchButton == null) {
			searchButton = new PrimaryButton("Search","primaryGreyLeftButton");
			searchButton.setText("Search");
		}
		return searchButton;
	}

	/* (non-Javadoc)
	 * @see mat.client.codelist.ManageGroupedCodeListPresenter.AddCodeListDisplay#getValueSetSearchFilterPanel()
	 */
	@Override
	public ListBox getValueSetSearchFilterPanel() {
		return vssfp.listBox;
	}
}
