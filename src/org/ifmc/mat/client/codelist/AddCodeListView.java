package org.ifmc.mat.client.codelist;


import java.util.Collections;
import java.util.List;

import org.ifmc.mat.DTO.HasListBoxDTO;
import org.ifmc.mat.client.shared.ListBoxMVP;
import org.ifmc.mat.client.shared.PrimaryButton;
import org.ifmc.mat.client.shared.search.HasPageSelectionHandler;
import org.ifmc.mat.client.shared.search.HasSelectAllHandler;
import org.ifmc.mat.client.shared.search.SearchResults;
import org.ifmc.mat.client.shared.search.SearchView;
import org.ifmc.mat.model.GroupedCodeListDTO;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class AddCodeListView extends AddBaseView implements  ManageGroupedCodeListPresenter.AddCodeListDisplay{
	private ListBoxMVP codeInput;
	private ManageCodesSearchView view;
	
	private Button searchButton;
	
	public AddCodeListView(){
		super("Add Value Set");
	}
	
	public Widget getVSSFPanel(){
		return vssfp.getPanel();
	}
	
	@Override
	protected Widget getValueInput() {
		if(codeInput == null) {
			codeInput = new ListBoxMVP(true, 10);
			//codeInput.setMaxWidth(50);
		}
		
		return codeInput;
	}
	@Override
	protected String getValueInputLabel() {
		return "Select Value Set";
	}
	
	@Override
	protected SearchView<?> getSearchView() {
		if(view == null) {
			view = new ManageCodesSearchView();
		}
		return view;
	}
	@Override
	public void buildDataTable(SearchResults<GroupedCodeListDTO> codeLists,boolean isChecked,int totalResutls, int totalPages,int currentPage) {		
		view.buildManageCodesDataTable(codeLists, true, isChecked, totalResutls, totalPages, currentPage);
		buildPageSelectionView(totalPages);
		
	}

	@Override
	public HasValue<String> getCodeId() {
		return codeInput;
	}

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
	
	@Override
	public void setCodeListName(String name) {
		setParentName("Grouped Value Set Name:", name);
	}
	
	@Override
	public String getCodeListId() {
		return codeInput.getValue(codeInput.getSelectedIndex());
	}
	
	@Override
	public String getCodeListName(){
		return codeInput.getItemText(codeInput.getSelectedIndex());
	}
	@Override
	public HasSelectAllHandler getSelectAllTool() {
		return view;
	}
	
	@Override
	public ListBoxMVP getCodeListInput(){
		return codeInput;
	}

	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return psv;
	}

	@Override
	public int getCurrentPage() {
		return psv.getCurrentPage();
	}

	@Override
	public void setCurrentPage(int pageNumber) {
		psv.setCurrentPage(pageNumber);
	}
	
	private void buildPageSelectionView(int totalPagesCount){
		psv.buildPageSelector(totalPagesCount);
		
	}
	@Override
	public String getCodeListOid(){
		return codeInput.getItemTitle(codeInput.getSelectedIndex());
	}

	@Override
	public Button getSearchButton() {
		if(searchButton == null) {
			searchButton = new PrimaryButton();
			searchButton.setText("Search");
		}
		return searchButton;
	}

	@Override
	public ListBox getValueSetSearchFilterPanel() {
		return vssfp.listBox;
	}
}
