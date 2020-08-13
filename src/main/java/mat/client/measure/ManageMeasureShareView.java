package mat.client.measure;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import mat.client.CustomPager;
import mat.client.buttons.SaveContinueCancelButtonBar;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MeasureNameLabel;
import mat.client.shared.MessageAlert;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SpacerWidget;
import mat.client.shared.WarningMessageAlert;
import mat.client.util.CellTableUtility;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.ShareLevel;
import org.gwtbootstrap3.client.ui.FormLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class ManageMeasureShareView.
 */
public class ManageMeasureShareView implements ShareDisplay {
	
	private static final int PAGE_SIZE = 25;
	private SaveContinueCancelButtonBar buttonBar = new SaveContinueCancelButtonBar("measureVersion");
	private VerticalPanel cellTablePanel = new VerticalPanel();
	private FlowPanel content = new FlowPanel();

	private MessageAlert errorMessages = new ErrorMessageAlert();
	private MessageAlert warningMessages = new WarningMessageAlert();

	private MeasureNameLabel measureNameLabel = new MeasureNameLabel();

	private CustomCheckBox privateCheck = new CustomCheckBox("Click to mark measure private", "Click to mark measure private", false);

	private SearchWidgetBootStrap searchWidgetBootStrap = new SearchWidgetBootStrap("Search", "Search User Name");

	private FocusPanel searchWidgetFocusPanel = new FocusPanel();

	public ManageMeasureShareView() {
		content.setStylePrimaryName("contentPanel");
		content.addStyleName("leftAligned");
		buttonBar.getSaveButton().setText("Save and Continue");
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.getElement().setId("horizontalPanel_HorizontalPanel");
		horizontalPanel.add(measureNameLabel);
		FormLabel privateCheckLabel = new FormLabel();
		privateCheckLabel.getElement().setAttribute("style","margin-top:10px;");
		privateCheckLabel.setText("Private Measure");
		privateCheckLabel.setTitle("Private Measure");
		privateCheckLabel.setFor("privateMeasure_CheckBox");
		privateCheck.getElement().setId("privateMeasure_CheckBox");
		privateCheck.setTitle("Click to mark measure as private");
		privateCheck.addValueChangeHandler(event -> {
			if(privateCheck.isChecked()) {
				privateCheck.setTitle("Click to mark measure public");
			} else {
				privateCheck.setTitle("Click to mark measure private");
			}
		});
			
		
		horizontalPanel.add(privateCheck);
		horizontalPanel.add(privateCheckLabel);
		horizontalPanel.setStyleName("horizontalPanel");
		content.add(horizontalPanel);
	
		VerticalPanel vp = new VerticalPanel();
		vp.add(searchWidgetBootStrap.getSearchWidget("Search User Name", "MeasureSharing"));
		content.add(new SpacerWidget());
		searchWidgetFocusPanel.add(vp);
		searchWidgetFocusPanel.setWidth("300px");
		content.add(searchWidgetFocusPanel);		
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		
		cellTablePanel.getElement().setId("cellTablePanel_VerticalPanel");
		cellTablePanel.setWidth("77%");

		content.add(cellTablePanel);
		content.add(new SpacerWidget());
		content.add(errorMessages);
		content.add(warningMessages);
		content.add(buttonBar);
	}

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
		MatCheckBoxCell shareTransferCB = new MatCheckBoxCell();
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
				
				if(shareValue) {
					shareTransferCB.setTitle("Click to unshare measure with " + object.getFirstName() + " " + object.getLastName());
				} else {
					shareTransferCB.setTitle("Click to share measure with " + object.getFirstName() + " " + object.getLastName());
				}
				
				return shareValue;
			}
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

	@Override
	public Widget asWidget() {
		return content;
	}

	@Override
	public void buildDataTable(UserShareInfoAdapter adapter) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("QdmAppliedListSearchPanel");

		if ((adapter.getData().getData() != null)
				&& (adapter.getData().getData().size() > 0)) {
			CellTable<MeasureShareDTO> cellTable = new CellTable<MeasureShareDTO>();
			ListDataProvider<MeasureShareDTO> sortProvider = new ListDataProvider<MeasureShareDTO>();
			List<MeasureShareDTO> measureShareList = new ArrayList<MeasureShareDTO>();
			measureShareList.addAll(adapter.getData().getData());
			cellTable.setPageSize(PAGE_SIZE);
			cellTable.redraw();
			cellTable.setRowCount(measureShareList.size(), true);
			sortProvider.refresh();
			sortProvider.getList().addAll(measureShareList);
			cellTable = addColumnToTable(cellTable);
			sortProvider.addDataDisplay(cellTable);
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"measureShare");
			spager.setPageStart(0);
			spager.setDisplay(cellTable);
			spager.setPageSize(PAGE_SIZE);
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
		}
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}
	
	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}
	
	@Override
	public MessageAlert getWarningMessageDisplay() {
		return warningMessages;
	}
	
	@Override
	public void resetMessageDisplay() {
		errorMessages.clearAlert();
		warningMessages.clearAlert();
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}

	@Override
	public HasValueChangeHandlers<Boolean> privateCheckbox() {
		return privateCheck;
	}

	@Override
	public void setMeasureName(String name) {
		measureNameLabel.setMeasureName(name);
	}

	@Override
	public void setPrivate(boolean isPrivate) {
		privateCheck.setValue(isPrivate);
		
		if(!isPrivate) {
			privateCheck.setTitle("Click to mark measure as private");
		} else {
			privateCheck.setTitle("Click to mark measure as public");
		}
	}

	public SearchWidgetBootStrap getSearchWidgetBootStrap() {
		return searchWidgetBootStrap;
	}

	@Override
	public FocusPanel getSearchWidgetFocusPanel() {
		return searchWidgetFocusPanel;
	}
	
}
