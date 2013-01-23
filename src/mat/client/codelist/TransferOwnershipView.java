package mat.client.codelist;

import java.util.List;
import mat.client.codelist.TransferOwnerShipModel.Result;
import mat.client.measure.metadata.Grid508;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.HorizontalFlowPanel;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.model.CodeListSearchDTO;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


public class TransferOwnershipView  implements ManageCodeListSearchPresenter.TransferDisplay {
	
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	private FlowPanel mainPanel = new FlowPanel();
	private Button saveButton = new PrimaryButton("Save");
	private Button Cancel = new PrimaryButton("Cancel");
	protected SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private SearchView<Result> view = new SearchView<TransferOwnerShipModel.Result>("Users");
	HorizontalPanel valueSetNamePanel = new HorizontalPanel();
	public Grid508 dataTable = view.getDataTable();
	public TransferOwnershipView() {
		mainPanel.add(new SpacerWidget());
		mainPanel.add(successMessages);
		mainPanel.add(errorMessages);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(valueSetNamePanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(view.asWidget());
		mainPanel.setStyleName("contentPanel");
		HorizontalPanel hp = new HorizontalPanel();
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		hp.add(saveButton);
		hp.add(Cancel);
		mainPanel.add(hp);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		containerPanel.setContent(mainPanel);
		
	}
	@Override
	public Widget asWidget() {
		return containerPanel;
	}

	@Override
	public
	void buildDataTable(SearchResults<TransferOwnerShipModel.Result> results , List<CodeListSearchDTO> codeListIDs) {
		if(results == null) {
			return;
		}
		int numRows = results.getNumberOfRows();
		int numColumns = results.getNumberOfColumns();
		dataTable.clear();
		dataTable.resize((int)numRows + 1, (int)numColumns);
		view.buildSearchResultsColumnHeaders(numRows,numColumns,results, false,false);
		buildSearchResults(numRows,numColumns,results);
        view.setViewingRange(results.getStartIndex(),results.getStartIndex() + numRows - 1,results.getResultsTotal());
		view.buildPageSizeSelector();
	}
	
	
	protected void buildSearchResults(int numRows,int numColumns,final SearchResults<TransferOwnerShipModel.Result> results){
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
					if(results.isColumnFiresSelection(j)) {
						String innerText = results.getValue(i, j).getElement().getInnerText();
						innerText = addSpaces(innerText, 27);
						Label a = new Label();
						a.setText(innerText);
						Panel holder = new HorizontalFlowPanel();
						SimplePanel innerPanel = new SimplePanel();
						innerPanel.setStylePrimaryName("pad-left21px");
						innerPanel.add(a);
						holder.add(innerPanel);
						dataTable.setWidget(i+1, j, holder);
					}
					else {
						dataTable.setWidget(i+1, j,results.getValue(i, j));
					}
				}
				dataTable.getRowFormatter().addStyleName(i + 1, "odd");
			}
		}
	@Override
	public void buildHTMLForValueSets(List<CodeListSearchDTO> codeListIDs){
		valueSetNamePanel.clear();
//		Label headingVS = new Label("Value Sets :");
//		headingVS.addStyleName("bold");
//		valueSetNamePanel.add(headingVS);
//		VerticalPanel vp = new VerticalPanel();
		StringBuilder paragraph = new StringBuilder("<p><b>Value Sets :</b>");
		for(int i=0;i<codeListIDs.size();i++){
			paragraph.append(codeListIDs.get(i).getName());
			if(i < codeListIDs.size()-1){
				paragraph.append(",");
			}
//			HTML desc = new HTML(codeListIDs.get(i).getName());
//			vp.add(desc);
//			HTML descBR = new HTML("</br>");
//			vp.add(descBR);
		}
		paragraph.append("</p>");
		HTML paragraphHtml = new HTML(paragraph.toString());
		valueSetNamePanel.add(paragraphHtml);
	}
	
	
	protected String addSpaces(String in, int frequency){
			
			if(in.length() <= frequency)
				return in;
			
			char[] inArr = in.toCharArray();
			StringBuffer sb = new StringBuffer();
			int i = 0;
			for(char c : inArr){
				if(i == frequency){
					sb.append(' ');
					i = 0;
				}else if(c == ' ')
					i = 0;
				else
					i++;
				sb.append(c);
			}
				
			return sb.toString();
	}
	
	public Grid508 getDataTable() {
		return dataTable;
	}
	public void setDataTable(Grid508 dataTable) {
		this.dataTable = dataTable;
	}
	
	@Override
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}
	@Override
	public HasClickHandlers getCancelButton() {
		
		return Cancel;
	}
	
	
	@Override
	public String getSelectedValue() {
		return null;
	}
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}
	@Override
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return view;
	}
	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return view;
	}
	@Override
	public int getPageSize() {
		return view.getPageSize();
	}
	
}
