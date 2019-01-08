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
import mat.client.cqlworkspace.DeleteConfirmationDialogBox;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.measure.measuredetails.observers.ReferencesObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MessagePanel;
import mat.client.shared.SpacerWidget;
import mat.client.shared.editor.RichTextEditor;
import mat.shared.ClickableSafeHtmlCell;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import mat.shared.measure.measuredetails.models.ReferencesModel;

public class ReferencesView implements MeasureDetailViewInterface {
	private FlowPanel mainPanel = new FlowPanel();
	private MeasureDetailsRichTextEditor measureDetailsRichTextEditor; 
	private ReferencesModel originalModel;
	private ReferencesModel referencesModel;
	private ReferencesObserver observer;
	private Column<String, SafeHtml> editColumn;
	private MessagePanel messagePanel;
	private Integer editingIndex = null;
	private boolean isReadOnly = false;
	
	public ReferencesView(ReferencesModel originalModel) {
		this.originalModel = originalModel;
		this.referencesModel = new ReferencesModel(originalModel);
		messagePanel = new MessagePanel();
		buildDetailView();
	}

	private CellTable<String> addColumnToTable(CellTable<String> referencesTable) {
		Column<String, SafeHtml> descriptionColumn = new Column<String, SafeHtml>(new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(String object) {
				SafeHtmlBuilder safeHtmlBuilder = new SafeHtmlBuilder();
				safeHtmlBuilder.appendHtmlConstant(object);
				return safeHtmlBuilder.toSafeHtml();
			}
		};
		referencesTable.addColumn(descriptionColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Description\">" + "Description" + "</span>"));
		
		editColumn = new Column<String, SafeHtml>(new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(String object) {
				return getEditColumnToolTip(object);
			}
		};
		
		editColumn.setFieldUpdater(new FieldUpdater<String, SafeHtml>() {
				@Override
				public void update(int index, String object, SafeHtml value) {
					if(isEditorDirty(object)) {
						displayDirtyCheck();
						messagePanel.getWarningConfirmationYesButton().addClickHandler(event -> handleYesButtonClicked(index, object));
						messagePanel.getWarningConfirmationNoButton().addClickHandler(event -> hideDirtyCheck());
						messagePanel.getWarningConfirmationYesButton().setFocus(true);
					} else {
						observer.handleEditClicked(index, object);
					}
				}
			});

		String columnText = isReadOnly ? "View" : "Edit";
		referencesTable.addColumn(editColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Index\">" + columnText + "</span>"));
		
		Column<String, SafeHtml> deleteColumn = new Column<String, SafeHtml>(new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(String object) {
				return getDeleteColumnToolTip(object);
			}
		};
		deleteColumn.setFieldUpdater(new FieldUpdater<String, SafeHtml>() {
			@Override
			public void update(int index, String object, SafeHtml value) {
				if(!isReadOnly) {
					displayDeleteConfirmationDialog(index, object);
				}
			}
		});
		referencesTable.addColumn(deleteColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Index\">" + "Delete" + "</span>"));
		
		return referencesTable;
	}
	
	private void displayDeleteConfirmationDialog(int index, String object) {
		DeleteConfirmationDialogBox deleteConfirmation = new DeleteConfirmationDialogBox();
		deleteConfirmation.getMessageAlert().createAlert("You have selected to delete reference: " + (object.length()>60 ? object.substring(0, 59) : object) + ". Please confirm that you want to remove this reference permanently.");
		deleteConfirmation.getYesButton().addClickHandler(event -> observer.handleDeleteReference(index, object));
		deleteConfirmation.show();
	}
	
	public void displayDirtyCheck() {
		hideDirtyCheck();
		messagePanel.getWarningConfirmationMessageAlert().createWarningAlert();
	}
	
	public void hideDirtyCheck() {
		messagePanel.clearAlerts();
	}
	
	private void handleYesButtonClicked(int index, String reference) {
		observer.handleEditClicked(index, reference);
		hideDirtyCheck();
	}
	
	private SafeHtml getEditColumnToolTip(String object) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String title = isReadOnly ? "View" : "Edit";
		String cssColor = isReadOnly ? "black" : "darkgoldenrod";
		String cssClass = "btn btn-link";
		String iconCss = isReadOnly ? "fa fa-binoculars fa-lg" : "fa fa-pencil fa-lg";
		sb.appendHtmlConstant("<button type=\"button\" title='"
				+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"color: " + cssColor + ";\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Edit</button>");
		return sb.toSafeHtml();
	}
	
	private SafeHtml getDeleteColumnToolTip(String object){
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String title = "Delete";
		String cssClass = "btn btn-link";
		String iconCss = "fa fa-trash fa-lg";	
		String disabledMarkup = isReadOnly ? "disabled " : "";
		sb.appendHtmlConstant("<button type=\"button\" title='"
				+ title + "' tabindex=\"0\" class=\"" + cssClass + "\" style=\"margin-left: 0px;\""  + disabledMarkup + "\"> <i class=\"" + iconCss + "\"></i><span style=\"font-size:0;\")>Delete</button>");
		return sb.toSafeHtml();
	}


	@Override
	public Widget getWidget() {
		return mainPanel;
	}

	@Override
	public boolean hasUnsavedChanges() {
		return false;
	}

	@Override
	public void buildDetailView() {
		mainPanel.clear();
		VerticalPanel cellTablePanel = new VerticalPanel();
		cellTablePanel.setWidth("100%");
		cellTablePanel.add(messagePanel);
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
		
		measureDetailsRichTextEditor = new MeasureDetailsRichTextEditor(mainPanel);
		measureDetailsRichTextEditor.getRichTextEditor().setTitle("References");
		addEventHandlers();
		
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.isReadOnly = readOnly;
		buildDetailView();
		measureDetailsRichTextEditor.setReadOnly(readOnly);	
	}

	@Override
	public ConfirmationDialogBox getSaveConfirmation() {
		return null;
	}

	@Override
	public void resetForm() {
		isReadOnly = false;
		editingIndex = null;
		hideDirtyCheck();
		this.referencesModel = new ReferencesModel(originalModel);
		measureDetailsRichTextEditor.getRichTextEditor().setText("");
		buildDetailView();
	}

	@Override
	public MeasureDetailsComponentModel getMeasureDetailsComponentModel() {
		return referencesModel;
	}

	@Override
	public RichTextEditor getRichTextEditor() {
		return measureDetailsRichTextEditor.getRichTextEditor();
	}

	@Override
	public void clear() {
		hideDirtyCheck();
		measureDetailsRichTextEditor.getRichTextEditor().setText("");
	}

	@Override
	public void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model) {
		this.referencesModel = (ReferencesModel) model;
	}

	@Override
	public void setObserver(MeasureDetailsComponentObserver observer) {
		this.observer = (ReferencesObserver) observer;
	}
	
	@Override
	public MeasureDetailsComponentObserver getObserver() {
		return observer;
	}
	
	private void addEventHandlers() {
		measureDetailsRichTextEditor.getRichTextEditor().addKeyUpHandler(event -> observer.handleValueChanged());
	}

	public boolean isEditorDirty(String referenceValue) {
		String textValue = measureDetailsRichTextEditor.getRichTextEditor().getValue();
		return (!isReadOnly && (!textValue.isEmpty() && !textValue.equals(referenceValue)));
	}
	
	public boolean isEditorDirty() {
		String textValue = measureDetailsRichTextEditor.getRichTextEditor().getValue();
		return !isReadOnly && !textValue.isEmpty();
	}

	public void saveModel() {
		if(editingIndex != null) {
			String textValue = measureDetailsRichTextEditor.getRichTextEditor().getValue();
			if(textValue.isEmpty()) {
				displayDeleteConfirmationDialog(editingIndex, textValue);
			} else {
				observer.handleEditReference();
				observer.saveReferences();
			}
		} else {
			observer.handleAddReference();
			observer.saveReferences();
		}
	}
	
	public Integer getEditingIndex() {
		return editingIndex;
	}

	public void setEditingIndex(Integer editingIndex) {
		this.editingIndex = editingIndex;
	}
}
