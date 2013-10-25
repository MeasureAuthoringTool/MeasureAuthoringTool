package mat.client.codelist;

import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.client.shared.search.SearchView;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class SummaryWidgetBase.
 * 
 * @param <T>
 *            the generic type
 */
public abstract class SummaryWidgetBase<T> extends Composite{
	
	/** The manage codes panel. */
	private VerticalPanel manageCodesPanel;
	
	/** The codes panel holder. */
	protected SimplePanel codesPanelHolder = new SimplePanel();
	
	/** The manage codes anchor. */
	private Anchor manageCodesAnchor = new Anchor(getManageLinkText());
	
	/** The manage codes link. */
	private SimplePanel manageCodesLink = new SimplePanel();
	
	/** The Constant currentPage. */
	public static final int  currentPage = 1;
	
	/** The page count. */
	protected int pageCount = 1;
	
	/** The view. */
	private SearchView<?> view = getSearchView();
	
	/** The psv. */
	protected PageSelectionView psv = new PageSelectionView();
	
	/**
	 * Instantiates a new summary widget base.
	 * 
	 * @param labelStr
	 *            the label str
	 */
	public SummaryWidgetBase(String labelStr){
		codesPanelHolder.getElement().setId("codesPanelHolder_SimplePanel");
		manageCodesLink.getElement().setId("manageCodesLink_SimplePanel");
    	manageCodesPanel = new VerticalPanel();
    	manageCodesPanel.getElement().setId("manageCodesPanel_VerticalPanel");
    	manageCodesPanel.addStyleName("rightSideForm");
  		manageCodesPanel.add(buildManageCodesLink(labelStr));  		
		manageCodesPanel.add(codesPanelHolder);
		manageCodesPanel.add(new SpacerWidget());
		manageCodesPanel.add(psv.asWidget());
		initWidget(manageCodesPanel);
      }
      
	/**
	 * Gets the manage link text.
	 * 
	 * @return the manage link text
	 */
	protected abstract String getManageLinkText();
	
	/**
	 * Gets the none available text.
	 * 
	 * @return the none available text
	 */
	protected abstract String getNoneAvailableText();
    
    /**
	 * Gets the value.
	 * 
	 * @param value
	 *            the value
	 * @return the value
	 */
    protected abstract String getValue(T value);
    
    /**
	 * Gets the description.
	 * 
	 * @param value
	 *            the value
	 * @return the description
	 */
    protected abstract String getDescription(T value);
    
    /**
	 * Gets the summary header label.
	 * 
	 * @return the summary header label
	 */
    protected abstract String getSummaryHeaderLabel();
    
    /**
	 * Gets the search view.
	 * 
	 * @return the search view
	 */
    protected abstract SearchView<?> getSearchView();
 
    
  	/**
		 * Builds the manage codes link.
		 * 
		 * @param labelStr
		 *            the label str
		 * @return the widget
		 */
	  private Widget buildManageCodesLink(String labelStr){
		SimplePanel codesLabel = new SimplePanel();
		Label l = new Label(labelStr);
		codesLabel.add(l);
		codesLabel.getElement().setId("codesLabel_SimplePanel");
		manageCodesLink.setStylePrimaryName("manageCodes");
		manageCodesLink.add( manageCodesAnchor);
		FlowPanel addCodes = new FlowPanel();
		addCodes.getElement().setId("addCodes_FlowPanel");
		addCodes.add(manageCodesLink);
		addCodes.add(codesLabel);
		return addCodes;
	}
  	
  	
  
   
  	
  	/* (non-Javadoc)
	   * @see com.google.gwt.user.client.ui.UIObject#setVisible(boolean)
	   */
	  public void setVisible(boolean visible){
  		MatContext.get().setVisible(manageCodesPanel,visible);
  	}

	
	/**
	 * Gets the manage code lists anchor.
	 * 
	 * @return the manage code lists anchor
	 */
	public Anchor getManageCodeListsAnchor() {
		return manageCodesAnchor;
	}
	
	/* This method is added to make the manageCodes link appear as label, since there is no real way to disable a link in GWT. */
	/**
	 * Adds the manage codes label.
	 * 
	 * @param l
	 *            the l
	 */
	public void addManageCodesLabel(Label l){
		manageCodesLink.clear();
		manageCodesLink.add(l);
	}
	
	/**
	 * Adds the manage codes link.
	 */
	public void addManageCodesLink(){
		manageCodesLink.clear();
		manageCodesLink.add(manageCodesAnchor);
	}
	
	/**
	 * Builds the codes widget.
	 * 
	 * @param summaryView
	 *            the summary view
	 */
	protected void buildCodesWidget(Widget summaryView){
		codesPanelHolder.clear();
		summaryView.setSize("450px","400px");
		codesPanelHolder.add(summaryView);
	}

	/**
	 * Builds the page selection view.
	 * 
	 * @param totalPagesCount
	 *            the total pages count
	 */
	protected void buildPageSelectionView(int totalPagesCount){
		psv.buildPageSelector(totalPagesCount);
		
	}
	
}
