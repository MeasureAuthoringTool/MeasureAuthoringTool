package mat.client.measure;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.CheckBox;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

import mat.client.CustomPager;
import mat.client.measure.ManageMeasurePresenter.ShareDisplay;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MeasureNameLabel;
import mat.client.shared.MessageAlert;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.ShareLevel;

/**
 * The Class ManageMeasureShareView.
 */
public class ManageMeasureShareView implements ShareDisplay {
	
	private static final int PAGE_SIZE = 25;
	/** The button bar. */
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar("measureVersion");
	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	/** The content. */
	private FlowPanel content = new FlowPanel();
	
	/** The error messages. */
	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	/** The measure name label. */
	private MeasureNameLabel measureNameLabel = new MeasureNameLabel();
	
	/** The private check. */
	private CheckBox privateCheck = new CheckBox();
	
	/**The search widget. */
	private SearchWidgetBootStrap searchWidgetBootStrap = new SearchWidgetBootStrap("Search", "Search User Name");
	
	/** The search widget focus panel. */
	private FocusPanel searchWidgetFocusPanel = new FocusPanel();

	/**
	 * Instantiates a new manage measure share view.
	 */
	public ManageMeasureShareView() {
		content.setStylePrimaryName("contentPanel");
		content.addStyleName("leftAligned");
		buttonBar.getSaveButton().setText("Save and Continue");
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.getElement().setId("horizontalPanel_HorizontalPanel");
		horizontalPanel.add(measureNameLabel);
		InlineLabel privateCheckLabel = new InlineLabel("Private Measure");
		privateCheck.setStyleName("gwt-CheckBox");
		privateCheckLabel.setStyleName("qdmLabel");
		horizontalPanel.add(privateCheck);
		horizontalPanel.add(privateCheckLabel);
		horizontalPanel.setStyleName("horizontalPanel");
		// content.add(measureNameLabel);
		content.add(horizontalPanel);
		
		//MAT-8907
		VerticalPanel vp = new VerticalPanel();
		vp.add(searchWidgetBootStrap.getSearchWidget("-Search User Name-", "MeasureSharing"));
		content.add(new SpacerWidget());
		searchWidgetFocusPanel.add(vp);
		searchWidgetFocusPanel.setWidth("300px");
		content.add(searchWidgetFocusPanel);		
		content.add(new SpacerWidget());
		// content.add(new Label("Select users with whom you wish to share modify access:"));
		content.add(new SpacerWidget());
		
		cellTablePanel.getElement().setId("cellTablePanel_VerticalPanel");
		cellTablePanel.setWidth("77%");

		content.add(cellTablePanel);
		content.add(new SpacerWidget());
		content.add(buttonBar);
		
	}
	/** Adds the column to table.
	 * @param cellTable the cell table
	 * @return the cell table */
	private CellTable<MeasureShareDTO> addColumnToTable(final CellTable<MeasureShareDTO> cellTable) {
		Label searchHeader = new Label("Select users with whom you wish to share modify access.");
		searchHeader.getElement().setId("measureShareTableHeader_Label");
		searchHeader.setStyleName("recentSearchHeader");
		searchHeader.getElement().setAttribute("tabIndex", "0");
		com.google.gwt.dom.client.TableElement elem = cellTable.getElement().cast();
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(searchHeader.getElement());
		Column<MeasureShareDTO, SafeHtml> userNameColumn = new Column<MeasureShareDTO, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(MeasureShareDTO object) {
				String title = "User Name :" + object.getFirstName() + " " + object.getLastName();
				String name = object.getFirstName() + " " + object.getLastName();
				return CellTableUtility.getColumnToolTip(name, title);
			}
		};
		cellTable.addColumn(userNameColumn, SafeHtmlUtils.fromSafeConstant("<span title='User Name'>"
				+ "User Name" + "</span>"));
		Column<MeasureShareDTO, SafeHtml> organizationColumn = new Column<MeasureShareDTO, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(MeasureShareDTO object) {
				String title = "Organization :" + object.getOrganizationName();
				String name = object.getOrganizationName();
				return CellTableUtility.getColumnToolTip(name, title);
			}
		};
		cellTable.addColumn(organizationColumn, SafeHtmlUtils.fromSafeConstant("<span title='Organization'>"
				+ "Organization" + "</span>"));
		Cell<Boolean> shareTransferCB = new MatCheckBoxCell();
		Column<MeasureShareDTO, Boolean> shareColumn = new Column<MeasureShareDTO, Boolean>(shareTransferCB) {
			@Override
			public Boolean getValue(MeasureShareDTO object) {
				String currentShare = object.getShareLevel();
				Boolean shareValue = false;
				if (ShareLevel.VIEW_ONLY_ID.equals(currentShare)) {
					shareValue = false;
				} else if (ShareLevel.MODIFY_ID.equals(currentShare)) {
					shareValue = true;
				}
				return shareValue;
			}
			/*
			 * @Override public void render(com.google.gwt.cell.client.Cell.Context context, MeasureShareDTO data, SafeHtmlBuilder sb) {
			 * String title = "<div title=\"Select User " + data.getFirstName() + " " + data.getLastName() + " to Share Measure." +
			 * "\"></div>"; sb.appendHtmlConstant(title).toSafeHtml(); super.render(context, data, sb); }
			 */
		};
		shareColumn.setFieldUpdater(new FieldUpdater<MeasureShareDTO, Boolean>() {
			@Override
			public void update(int index, MeasureShareDTO object, Boolean value) {
				if (value) {
					object.setShareLevel(ShareLevel.MODIFY_ID);
				} else {
					object.setShareLevel(ShareLevel.VIEW_ONLY_ID);
				}
			}
		});
		cellTable.addColumn(shareColumn, SafeHtmlUtils.fromSafeConstant("<span title='Check for Share'>" + "Share"
				+ "</span>"));
		return cellTable;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return content;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#buildDataTable(mat.client.shared.search.SearchResults)
	 */
	@Override
	public void buildDataTable(UserShareInfoAdapter adapter) {
		// searchView.buildDataTable(results);
		cellTablePanel.clear();
		cellTablePanel.setStyleName("QdmAppliedListSearchPanel");
		/*
		 * cellTablePanel.add(searchHeader); cellTablePanel.add(new SpacerWidget());
		 */
		if ((adapter.getData().getData() != null)
				&& (adapter.getData().getData().size() > 0)) {
			CellTable<MeasureShareDTO> cellTable = new CellTable<MeasureShareDTO>();
			ListDataProvider<MeasureShareDTO> sortProvider = new ListDataProvider<MeasureShareDTO>();
			List<MeasureShareDTO> measureShareList = new ArrayList<MeasureShareDTO>();
			measureShareList.addAll(adapter.getData().getData());
			cellTable.setPageSize(PAGE_SIZE);
			cellTable.redraw();
			cellTable.setRowCount(measureShareList.size(), true);
			// cellTable.setSelectionModel(getSelectionModelWithHandler());
			sortProvider.refresh();
			sortProvider.getList().addAll(measureShareList);
			cellTable = addColumnToTable(cellTable);
			sortProvider.addDataDisplay(cellTable);
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"measureShare");
			spager.setPageStart(0);
			spager.setDisplay(cellTable);
			spager.setPageSize(PAGE_SIZE);
			/* spager.setToolTipAndTabIndex(spager); */
			cellTable.setWidth("100%");
			cellTable.setColumnWidth(0, 40.0, Unit.PCT);
			cellTable.setColumnWidth(1, 40.0, Unit.PCT);
			cellTable.setColumnWidth(2, 20.0, Unit.PCT);
			Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(
					"measureShareSummary",
					"In the Following Measure sharing table, User name is given in first column, Organization "
							+ "in second column and Share in third column with Check boxes positioned to the "
							+ "right of the table.");
			cellTable.getElement().setAttribute("id", "measureShareCellTable");
			cellTable.getElement().setAttribute("aria-describedby", "measureShareSummary");
			cellTablePanel.add(invisibleLabel);
			cellTablePanel.add(cellTable);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(spager);
			/*
			 * cellTable.addCellPreviewHandler(new Handler<MeasureShareDTO>() {
			 * 
			 * @Override public void onCellPreview(CellPreviewEvent<MeasureShareDTO> event) { if
			 * ("mouseover".equals(event.getNativeEvent().getType())) { Element cellElement =
			 * event.getNativeEvent().getEventTarget().cast(); cellElement.setTitle("cell contents go here."); } } });
			 */
		} else {
			HTML desc = new HTML("<p> No Users available for sharing.</p>");
			cellTablePanel.add(desc);
		}
		
		
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}
	/** Gets the column value with tool tip.
	 * 
	 * @param columnName the column name
	 * @param columnValue the column value
	 * @return the column value with tool tip */
	private SafeHtml getColumnValueWithToolTip(String columnName, String columnValue) {
		String htmlConstant = "<span tabindex=\"0\" title='" + columnName + ": " + columnValue + "'>" + columnValue + "</span>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#getErrorMessageDisplay()
	 */
	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#getPageSelectionTool()
	 */
//	@Override
//	public HasPageSelectionHandler getPageSelectionTool() {
//		return searchView;
//	}
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#getPageSize()
//	 */
//	@Override
//	public int getPageSize() {
//		return searchView.getPageSize();
//	}
//	/* (non-Javadoc)
//	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#getPageSizeSelectionTool()
//	 */
//	@Override
//	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
//		return searchView;
//	}
	/** Gets the selection model with handler.
	 * 
	 * @return the selection model with handler */
	/*
	 * private SingleSelectionModel<MeasureShareDTO> getSelectionModelWithHandler() { selectionModel = new
	 * SingleSelectionModel<MeasureShareDTO>(); selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
	 * 
	 * @Override public void onSelectionChange(SelectionChangeEvent event) { getErrorMessageDisplay().clear(); } }); return selectionModel;
	 * }
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#getShareButton()
	 */
	@Override
	public HasClickHandlers getShareButton() {
		return buttonBar.getSaveButton();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#privateCheckbox()
	 */
	@Override
	public HasValueChangeHandlers<Boolean> privateCheckbox() {
		return privateCheck;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#setMeasureName(java.lang.String)
	 */
	@Override
	public void setMeasureName(String name) {
		measureNameLabel.setMeasureName(name);
	}
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ShareDisplay#setPrivate(boolean)
	 */
	@Override
	public void setPrivate(boolean isPrivate) {
		privateCheck.setValue(isPrivate);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.SearchDisplay#getSearchButton()
	 */
	public SearchWidgetBootStrap getSearchWidgetBootStrap() {
		return searchWidgetBootStrap;
	}
	/**
	 * Gets the focus panel.
	 *
	 * @return the focus panel
	 */
	@Override
	public FocusPanel getSearchWidgetFocusPanel() {
		return searchWidgetFocusPanel;
	}
	
}
