package mat.client.measure.measuredetails.views;

import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.measure.measuredetails.observers.MeasureTypeObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.editor.RichTextEditor;
import mat.client.util.CellTableUtility;
import mat.model.MeasureType;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import mat.shared.measure.measuredetails.models.MeasureTypeModel;

public class MeasureTypeView implements MeasureDetailViewInterface {
	private FlowPanel mainPanel = new FlowPanel();
	private MeasureTypeModel originalMeasureTypeModel;
	private MeasureTypeModel measureTypeModel;
	private CellTable<MeasureType> measureTypeCellTable;
	private MultiSelectionModel<MeasureType> measureTypeSelectioModel; 
	private List<MeasureType> measureTypeList; 
	private MeasureTypeObserver observer; 
	private static final String COMPOSITE = "COMPOSITE";
	
	public MeasureTypeView(MeasureTypeModel originaMeasureTypeModel, List<MeasureType> measureTypeList) {
		this.originalMeasureTypeModel = originaMeasureTypeModel;
		this.measureTypeList = measureTypeList; 
		measureTypeList.removeIf(m -> m.getAbbrName().equals(COMPOSITE));
		buildMeasureTypeModel(originaMeasureTypeModel);
		buildDetailView();
	}
	
	@Override
	public Widget getWidget() {
		return mainPanel;
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasUnsavedChanges() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void buildDetailView() {		
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("cellTablePanelMeasureDetails");		
		panel.setWidth("600px");
		measureTypeCellTable = new CellTable<MeasureType>();
		measureTypeCellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		ListDataProvider<MeasureType> sortProvider = new ListDataProvider<MeasureType>();

		measureTypeCellTable.setRowData(this.measureTypeList);
		measureTypeCellTable.setRowCount(this.measureTypeList.size(), true);
		sortProvider.refresh();
		sortProvider.getList().addAll(this.measureTypeList);
		addMeasureTypeColumnToTable(MatContext.get().getMeasureLockService().checkForEditPermission());
		updateMeasureTypeSelectedList(this.measureTypeModel.getMeasureTypeList());
		sortProvider.addDataDisplay(measureTypeCellTable);
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("measureTypeListSummary", "In the following Measure Type List table,Select is given in first Column and Measure Type is given in Second column");
		panel.add(invisibleLabel);
		measureTypeCellTable.getElement().setAttribute("id", "MeasureTypeListCellTable");
		measureTypeCellTable.getElement().setAttribute("aria-describedby", "measureTypeListSummary");
		measureTypeCellTable.setWidth("625px");
		panel.add(measureTypeCellTable);
		mainPanel.add(panel);
	}
	
	private void updateMeasureTypeSelectedList(List<MeasureType> measureTypeList) {
		if (measureTypeModel.getMeasureTypeList().size() != 0) {
			for (int i = 0; i < measureTypeModel.getMeasureTypeList().size(); i++) {
				for (int j = 0; j < measureTypeList.size(); j++) {
					if (measureTypeModel.getMeasureTypeList().get(i).getDescription().equalsIgnoreCase(measureTypeList.get(j).getDescription())) {
						measureTypeModel.getMeasureTypeList().set(i, measureTypeList.get(j));
						break;
					}
				}
			}
		}
	}
	
	private void addMeasureTypeColumnToTable(boolean editable) {
		Label measureSearchHeader = new Label("Measure Type List");
		measureSearchHeader.getElement().setId("measureTypeHeader_Label");
		measureSearchHeader.setStyleName("invisibleTableCaption");
		com.google.gwt.dom.client.TableElement elem = measureTypeCellTable.getElement().cast();
		measureSearchHeader.getElement().setAttribute("tabIndex", "0");
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(measureSearchHeader.getElement());
		measureTypeSelectioModel = new MultiSelectionModel<MeasureType>();
		measureTypeCellTable.setSelectionModel(measureTypeSelectioModel);
		MatCheckBoxCell chbxCell = new MatCheckBoxCell(false, true, !editable);
		
		Column<MeasureType, Boolean> selectColumn = new Column<MeasureType, Boolean>(chbxCell) {
			@Override
			public Boolean getValue(MeasureType object) {
				chbxCell.setTitle("Click checkbox to select " + object.getDescription());
				boolean isSelected = false;
				if (measureTypeModel.getMeasureTypeList().size() > 0) {
					for (int i = 0; i < measureTypeModel.getMeasureTypeList().size(); i++) {
						if (measureTypeModel.getMeasureTypeList().get(i).getDescription().equalsIgnoreCase(object.getDescription())) {
							isSelected = true;
							break;
						}
					}
				} 
				
				return isSelected;
			}
		};
		
		selectColumn.setFieldUpdater(new FieldUpdater<MeasureType, Boolean>() {
			@Override
			public void update(int index, MeasureType object, Boolean value) {
				measureTypeSelectioModel.setSelected(object, value);
				if (value) {
					measureTypeModel.getMeasureTypeList().add(object);
				} else {
					for (int i = 0; i < measureTypeModel.getMeasureTypeList().size(); i++) {
						if (measureTypeModel.getMeasureTypeList().get(i).getDescription().equalsIgnoreCase(object.getDescription())) {
							measureTypeModel.getMeasureTypeList().remove(i);
							break;
						}
					}
				}
			}
		});
		
		measureTypeCellTable.addColumn(selectColumn, SafeHtmlUtils.fromSafeConstant("<span title='Select'>"+ "Select" + "</span>"));
		
		Column<MeasureType, SafeHtml> measureNameColumn = new Column<MeasureType, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(MeasureType object) {
				return CellTableUtility.getColumnToolTip(object.getDescription());
			}
		};
		
		measureTypeCellTable.addColumn(measureNameColumn, SafeHtmlUtils.fromSafeConstant("<span title='Measure Type'>" + "Measure Type" + "</span>"));
	}

	@Override
	public void setReadOnly(boolean readOnly) {
	}

	@Override
	public ConfirmationDialogBox getSaveConfirmation() {
		return null;
	}

	@Override
	public void resetForm() {
		
	}

	@Override
	public MeasureDetailsComponentModel getMeasureDetailsComponentModel() {
		return this.measureTypeModel;
	}

	@Override
	public RichTextEditor getRichTextEditor() {
		return null;
	}

	@Override
	public void clear() {
		
	}

	@Override
	public void setMeasureDetailsComponentModel(MeasureDetailsComponentModel model) {}

	@Override
	public void setObserver(MeasureDetailsComponentObserver observer) {
		this.observer = (MeasureTypeObserver) observer; 
	}
	
	@Override
	public MeasureDetailsComponentObserver getObserver() {
		return this.observer;
	}
	
	private void buildMeasureTypeModel(MeasureTypeModel originalMeasureTypeModel) {
		this.measureTypeModel = new MeasureTypeModel(originalMeasureTypeModel);
	}
}
