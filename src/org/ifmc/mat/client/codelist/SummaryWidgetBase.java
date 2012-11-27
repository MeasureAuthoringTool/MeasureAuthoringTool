package org.ifmc.mat.client.codelist;

import org.ifmc.mat.client.shared.MatContext;
import org.ifmc.mat.client.shared.SpacerWidget;
import org.ifmc.mat.client.shared.search.SearchView;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class SummaryWidgetBase<T> extends Composite{
	
	private VerticalPanel manageCodesPanel;
	
	protected SimplePanel codesPanelHolder = new SimplePanel();
	
	private Anchor manageCodesAnchor = new Anchor(getManageLinkText());
	
	private SimplePanel manageCodesLink = new SimplePanel();
	public static final int  currentPage = 1;
	protected int pageCount = 1;
	private SearchView<?> view = getSearchView();
	protected PageSelectionView psv = new PageSelectionView();
	
	public SummaryWidgetBase(String labelStr){
    	manageCodesPanel = new VerticalPanel();
    	manageCodesPanel.addStyleName("rightSideForm");
  		manageCodesPanel.add(buildManageCodesLink(labelStr));
		manageCodesPanel.add(codesPanelHolder);
		manageCodesPanel.add(new SpacerWidget());
		manageCodesPanel.add(psv.asWidget());
		initWidget(manageCodesPanel);
      }
      
	protected abstract String getManageLinkText();
	protected abstract String getNoneAvailableText();
    protected abstract String getValue(T value);
    protected abstract String getDescription(T value);
    protected abstract String getSummaryHeaderLabel();
    protected abstract SearchView<?> getSearchView();
 
    
  	private Widget buildManageCodesLink(String labelStr){
		SimplePanel codesLabel = new SimplePanel();
		Label l = new Label(labelStr);
		codesLabel.add(l);
		manageCodesLink.setStylePrimaryName("manageCodes");
		manageCodesLink.add( manageCodesAnchor);
		FlowPanel addCodes = new FlowPanel();
		addCodes.add(manageCodesLink);
		addCodes.add(codesLabel);
		return addCodes;
	}
  	
  	
  
   
  	
  	public void setVisible(boolean visible){
  		MatContext.get().setVisible(manageCodesPanel,visible);
  	}

	
	public Anchor getManageCodeListsAnchor() {
		return manageCodesAnchor;
	}
	
	/* This method is added to make the manageCodes link appear as label, since there is no real way to disable a link in GWT. */
	public void addManageCodesLabel(Label l){
		manageCodesLink.clear();
		manageCodesLink.add(l);
	}
	
	public void addManageCodesLink(){
		manageCodesLink.clear();
		manageCodesLink.add(manageCodesAnchor);
	}
	
	protected void buildCodesWidget(Widget summaryView){
		codesPanelHolder.clear();
		summaryView.setSize("450px","400px");
		codesPanelHolder.add(summaryView);
	}

	protected void buildPageSelectionView(int totalPagesCount){
		psv.buildPageSelector(totalPagesCount);
		
	}
	
}
