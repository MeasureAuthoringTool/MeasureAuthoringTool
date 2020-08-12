package mat.client.measure.measuredetails.views;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.measuredetails.MeasureDetailsObserver;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatSafeHTMLCell;
import mat.shared.ClickableSafeHtmlCell;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComponentMeasuresView implements MeasureDetailViewInterface{
	private VerticalPanel componentsAppliedPanel = new VerticalPanel();
	private FlowPanel mainPanel = new FlowPanel();
	private CellTable<Result> cellTable = new CellTable<>();
	private VerticalPanel searchPanel = new VerticalPanel();
	private ManageCompositeMeasureDetailModel originalManageCompositeMeasureDetailModel;
	private ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel;
	private Map<String, String> aliasMapping = new HashMap<>();
	Button editComponentMeasuresButton = new Button("Edit Component Measures");
	
	public ComponentMeasuresView(MeasureDetailsObserver measureDetailsObserver, ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
		this.originalManageCompositeMeasureDetailModel = new ManageCompositeMeasureDetailModel(manageCompositeMeasureDetailModel);
		this.manageCompositeMeasureDetailModel = new ManageCompositeMeasureDetailModel(manageCompositeMeasureDetailModel);
		this.aliasMapping = originalManageCompositeMeasureDetailModel.getAliasMapping();
		
		searchPanel.clear();
		searchPanel.getElement().setId("searchPanel_VerticalPanel");
		searchPanel.setStyleName("recentSearchPanel");
		
		buildCellTable();
		componentsAppliedPanel.add(searchPanel);
		componentsAppliedPanel.setWidth("625px");
		componentsAppliedPanel.getElement().setId("COMPONENTS");
		mainPanel.add(componentsAppliedPanel);
		
		editComponentMeasuresButton.setType(ButtonType.PRIMARY);
		editComponentMeasuresButton.setPull(Pull.RIGHT);
		editComponentMeasuresButton.setMarginTop(10);
		editComponentMeasuresButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				measureDetailsObserver.handleEditCompositeMeasures(manageCompositeMeasureDetailModel);
			}
		});
		mainPanel.add(editComponentMeasuresButton);
		mainPanel.setWidth("625px");
	}
	
	void buildCellTable() {
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		ListDataProvider<Result> sortProvider = new ListDataProvider<>();
		ArrayList<Result> selectedMeasureList = new ArrayList<>();
		selectedMeasureList.addAll(originalManageCompositeMeasureDetailModel.getAppliedComponentMeasures());
		cellTable.redraw();
		cellTable.setRowCount(selectedMeasureList.size(), true);
		sortProvider.refresh();
		sortProvider.getList().addAll(originalManageCompositeMeasureDetailModel.getAppliedComponentMeasures());
		cellTable = addColumnToTable(cellTable);
		sortProvider.addDataDisplay(cellTable);
		Label invisibleLabel = (Label) LabelBuilder
				.buildInvisibleLabel(
						"componentMeasureSummary",
						"In the following Component Measures table, Measure Name is given in first column,"
								+ " Alias in second column and version in third column.");
		cellTable.getElement().setAttribute("id", "componentMeasureCellTable");
		cellTable.getElement().setAttribute("aria-describedby", "componentMeasureSummary");
		cellTable.setVisibleRange(0, selectedMeasureList.size());
		searchPanel.add(invisibleLabel);
		searchPanel.add(cellTable);
	}
	
	private CellTable<Result> addColumnToTable(final CellTable<Result> table) {
			Label searchHeader = new Label("Component Measures");
			searchHeader.getElement().setId("componentMeasures_Label");
			searchHeader.setStyleName("recentSearchHeader");
			com.google.gwt.dom.client.TableElement elem = cellTable.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(searchHeader.getElement());
			Column<Result, SafeHtml> measureName =
					new Column<Result, SafeHtml>(new
							ClickableSafeHtmlCell()) {
				@Override
				public SafeHtml getValue(Result object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant("<div id='#measureNameContainer' tabindex=\"-1\">");
					sb.appendHtmlConstant("<span title=\" " + object.getName() + "\" tabindex=\"0\">" + object.getName() + "</span>");
					sb.appendHtmlConstant("</div>");
					return sb.toSafeHtml();
				}
			};
			table.addColumn(measureName, SafeHtmlUtils.fromSafeConstant(
					"<span title='Measure Name Column'>" + "Measure Name" + "</span>"));
			
			Column<Result, SafeHtml> alias =
					new Column<Result, SafeHtml>(new
							ClickableSafeHtmlCell()) {
				@Override
				public SafeHtml getValue(Result object) { 
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant("<div id='#aliasContainer' tabindex=\"-1\">");
					sb.appendHtmlConstant("<span title=\" " + aliasMapping.get(object.getId()) + "\" tabindex=\"0\">" + aliasMapping.get(object.getId()) + "</span>");
					sb.appendHtmlConstant("</div>");
					return sb.toSafeHtml();
				}
			};
			table.addColumn(alias, SafeHtmlUtils.fromSafeConstant(
					"<span title='Alias Column'>" + "Alias" + "</span>"));
			
			Column<Result, SafeHtml> version =
					new Column<Result, SafeHtml>(
							new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(Result object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant("<div id='#appliedComponentContainer' tabindex=\"-1\">");
					sb.appendHtmlConstant("<span title=\" " + object.getVersion() + "\" tabindex=\"0\">" + object.getVersion() + "</span>");
					sb.appendHtmlConstant("</div>");
					return sb.toSafeHtml();
				}
			};
			table.addColumn(version, SafeHtmlUtils.fromSafeConstant(
					"<span title='Version Column'>" + "Version" + "</span>"));
			
			table.setColumnWidth(0, 350.0, Unit.PX);
			table.setColumnWidth(1, 150.0, Unit.PX);
			table.setColumnWidth(2, 100, Unit.PX);
			
			table.setWidth("615px");
		return table;
	}
	
	public Widget getWidget() {
		return mainPanel;
	}

	@Override
	public boolean hasUnsavedChanges() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void buildDetailView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		editComponentMeasuresButton.setEnabled(!readOnly);
	}

	@Override
	public ConfirmationDialogBox getSaveConfirmation() {
		return null;
	}

	@Override
	public void resetForm() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MeasureDetailsComponentModel getMeasureDetailsComponentModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextArea getTextEditor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setObserver(MeasureDetailsComponentObserver observer) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public MeasureDetailsComponentObserver getObserver() {
		return null;
	}

	@Override
	public Widget getFirstElement() {
		return cellTable.asWidget();
	}

}
