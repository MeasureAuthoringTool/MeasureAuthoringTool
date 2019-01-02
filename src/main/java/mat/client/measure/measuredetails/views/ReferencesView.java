package mat.client.measure.measuredetails.views;

import org.gwtbootstrap3.client.ui.gwt.CellTable;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

import mat.client.CustomPager;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.measure.measuredetails.observers.ReferencesObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SpacerWidget;
import mat.client.shared.editor.RichTextEditor;
import mat.shared.ClickableSafeHtmlCell;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import mat.shared.measure.measuredetails.models.ReferencesModel;

public class ReferencesView implements MeasureDetailViewInterface {
	private FlowPanel mainPanel = new FlowPanel();
	private ReferencesModel originalModel;
	private ReferencesModel referencesModel;
	private ReferencesObserver observer;
	
	public ReferencesView(ReferencesModel originalModel) {
		this.originalModel = originalModel;
		this.referencesModel = new ReferencesModel(originalModel);
		buildReferencesView();
	}
	
	private void buildReferencesView() {
		mainPanel.clear();
		VerticalPanel cellTablePanel = new VerticalPanel();
		CellTable<String> referencesTable = new CellTable<String>();
		referencesTable.setPageSize(5);
		referencesTable.redraw();
		referencesTable.setRowCount(referencesModel.getReferences().size(), true);
		ListDataProvider<String> listDataProvider = new ListDataProvider<String>();
		listDataProvider.refresh();
		listDataProvider.getList().addAll(referencesModel.getReferences());
		
		referencesTable = addColumnToTable(referencesTable);
		listDataProvider.addDataDisplay(referencesTable);
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"manageReferences");
		spager.setPageStart(0);
		spager.setDisplay(referencesTable);
		spager.setPageSize(5);
		referencesTable.setWidth("100%");
		referencesTable.setColumnWidth(0, 15.0, Unit.PCT);
		referencesTable.setColumnWidth(1, 55.0, Unit.PCT);
		referencesTable.setColumnWidth(2, 15.0, Unit.PCT);
		referencesTable.setColumnWidth(3, 15.0, Unit.PCT);
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(
				"manageReferencesSummary",
				"In the Following Manage references table, Index Number is given in the first column, Description in the "
						+ "second column, Edit Reference in the third column, and DeleteReference in the fourth column.");
		referencesTable.getElement().setAttribute("id", "manageReferencesCellTable");
		referencesTable.getElement().setAttribute("aria-describedby", "manageReferencesSummary");
		cellTablePanel.add(invisibleLabel);
		cellTablePanel.add(referencesTable);
		cellTablePanel.add(new SpacerWidget());
		cellTablePanel.add(spager);
		mainPanel.add(cellTablePanel);
	}

	private CellTable<String> addColumnToTable(CellTable<String> referencesTable) {
		// TODO Auto-generated method stub
		
		Column<String, SafeHtml> indexColumn = new Column<String, SafeHtml>(new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(String object) {
				SafeHtmlBuilder safeHtmlBuilder = new SafeHtmlBuilder();
				int indexNumber = referencesModel.getReferences() != null && referencesModel.getReferences().contains(object) ? referencesModel.getReferences().indexOf(object) : 0;
				safeHtmlBuilder.appendHtmlConstant(String.valueOf(indexNumber));
				return safeHtmlBuilder.toSafeHtml();
			}
		};
		referencesTable.addColumn(indexColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Index\">" + "#" + "</span>"));
		
		Column<String, SafeHtml> descriptionColumn = new Column<String, SafeHtml>(new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(String object) {
				SafeHtmlBuilder safeHtmlBuilder = new SafeHtmlBuilder();
				safeHtmlBuilder.appendHtmlConstant(object);
				return safeHtmlBuilder.toSafeHtml();
			}
		};
		referencesTable.addColumn(descriptionColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Description\">" + "Description" + "</span>"));
		
		Column<String, SafeHtml> editColumn = new Column<String, SafeHtml>(new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(String object) {
				return getEditColumnToolTip(object);
			}
		};
		editColumn.setFieldUpdater(new FieldUpdater<String, SafeHtml>() {
			@Override
			public void update(int index, String object, SafeHtml value) {
				observer.onEditClicked(object);
			}
		});
		referencesTable.addColumn(editColumn);
		
		Column<String, SafeHtml> deleteColumn = new Column<String, SafeHtml>(new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(String object) {
				return getDeleteColumnToolTip(object);
			}
		};
		deleteColumn.setFieldUpdater(new FieldUpdater<String, SafeHtml>() {
			@Override
			public void update(int index, String object, SafeHtml value) {
				observer.onDeleteClicked(object);
			}
		});
		referencesTable.addColumn(deleteColumn);
		
		return referencesTable;
	}
	
	private SafeHtml getEditColumnToolTip(String object) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String title = "Edit";;
		String cssClass = "btn btn-link";
		String iconCss = "fa fa-pencil fa-lg";
		sb.appendHtmlConstant("<button type=\"button\" title='"
				+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"color: darkgoldenrod;\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Edit</button>");
		return sb.toSafeHtml();
	}
	
	private SafeHtml getDeleteColumnToolTip(String object){
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String title = "Delete";;
		String cssClass = "btn btn-link";
		String iconCss ="fa fa-trash fa-lg";	
		sb.appendHtmlConstant("<button type=\"button\" title='"
				+ title + "' tabindex=\"0\" class=\"" + cssClass + "\" style=\"margin-left: 0px;\" > <i class=\"" + iconCss + "\"></i><span style=\"font-size:0;\">Delete</button>");
		return sb.toSafeHtml();
	}

	@Override
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
		// TODO Auto-generated method stub
		
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
		return referencesModel;
	}

	@Override
	public RichTextEditor getRichTextEditor() {
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
		this.observer = (ReferencesObserver) observer;
	}
	
	@Override
	public MeasureDetailsComponentObserver getObserver() {
		return null;
	}

}
