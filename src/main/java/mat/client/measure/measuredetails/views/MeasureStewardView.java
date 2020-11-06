package mat.client.measure.measuredetails.views;

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
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import mat.client.measure.measuredetails.observers.MeasureDetailsComponentObserver;
import mat.client.measure.measuredetails.observers.MeasureStewardDeveloperObserver;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.model.Author;
import mat.model.MeasureSteward;
import mat.shared.measure.measuredetails.models.MeasureDetailsComponentModel;
import mat.shared.measure.measuredetails.models.MeasureStewardDeveloperModel;
import org.gwtbootstrap3.client.ui.FormLabel;

import java.util.ArrayList;
import java.util.List;

public class MeasureStewardView implements MeasureDetailViewInterface{
	private final FlowPanel mainPanel = new FlowPanel();

	private String stewardId;
	private String stewardValue;

	private CellTable<Author> authorCellTable;
	private MultiSelectionModel<Author> authorSelectionModel;
	private List<Author> authorsSelectedList;
	private ListBoxMVP stewardListBox = new ListBoxMVP();

	private MeasureStewardDeveloperModel model;
	private MeasureStewardDeveloperModel originalModel;
	private MeasureStewardDeveloperObserver measureStewardDeveloperObserver;
	
	protected ScrollPanel authorSPanel = new ScrollPanel();
	protected ScrollPanel stewardSPanel = new ScrollPanel();
	private FormLabel stewardTableLabel;

	public MeasureStewardView(MeasureStewardDeveloperModel measureStewardDeveloperModel) {
		this.originalModel = measureStewardDeveloperModel; 
		stewardId = measureStewardDeveloperModel.getStewardId();
		stewardValue = measureStewardDeveloperModel.getStewardValue();
		if (measureStewardDeveloperModel.getSelectedDeveloperList() != null) {
			this.authorsSelectedList = new ArrayList<>();
			authorsSelectedList.addAll(measureStewardDeveloperModel.getSelectedDeveloperList());	
		}
		setOptionsInStewardList(measureStewardDeveloperModel.getMeasureStewardList());
		buildAuthorCellTable(measureStewardDeveloperModel.getMeasureDeveloperList());
		this.model = new MeasureStewardDeveloperModel(this.originalModel);
		buildDetailView();
	}

	private void buildStewardListComponent(VerticalPanel moreMeasureDetailsVP) {
		stewardTableLabel = new FormLabel();
		stewardTableLabel.setText("Measure Steward List");
		stewardTableLabel.setId("stewardTableLabel");
		stewardTableLabel.setFor("stewardListBox");
		stewardListBox.setId("stewardListBox");
		stewardListBox.setTitle("Measure Steward List");

		moreMeasureDetailsVP.add(stewardTableLabel);
		stewardSPanel.add(stewardListBox);
		moreMeasureDetailsVP.add(stewardSPanel);
		moreMeasureDetailsVP.setWidth("575px");
		mainPanel.add(moreMeasureDetailsVP);
	}

	private void setOptionsInStewardList(List<MeasureSteward> allStewardList) {
		int i=1;
		getStewardListBox().clear();
		getStewardListBox().addItem("--Select--");
		getStewardListBox().setSelectedIndex(i);
		for(final MeasureSteward m : allStewardList){
			getStewardListBox().insertItem(m.getOrgName(), m.getId(), m.getOrgOid());
			if(getStewardId() != null && m.getId().equals(getStewardId())){
				getStewardListBox().setSelectedIndex(i);
			}
			i= i+1;
		}
		getStewardListBox().setEnabled(MatContext.get().getMeasureLockService().checkForEditPermission());
	}

	private void buildAuthorTableComponent(VerticalPanel moreMeasureDetailsVP) {
		final FormLabel authorTableLabel = new FormLabel();
		authorTableLabel.setText("Measure Developer List");
		moreMeasureDetailsVP.add(authorTableLabel);
		moreMeasureDetailsVP.add(authorSPanel);
		mainPanel.add(moreMeasureDetailsVP);
	}

	public void buildAuthorCellTable(List<Author> currentAuthorsList) {
		authorSPanel.clear();
		authorSPanel.setStyleName("cellTablePanelMeasureDetails");
		authorSPanel.setWidth("625px");
		authorSPanel.setHeight("400px");
		authorCellTable = new CellTable<>();
		authorCellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		final ListDataProvider<Author> sortProvider = new ListDataProvider<>();
		authorCellTable.setRowData(currentAuthorsList);
		if(!authorsSelectedList.isEmpty()){
			final List<Author> selectauthorsList = new ArrayList<>();
			updateMeasureDevelopersSelectedList(currentAuthorsList);
			selectauthorsList.addAll(swapMeasureDevelopersList(currentAuthorsList));
			authorCellTable.setRowData(selectauthorsList);
			authorCellTable.setRowCount(selectauthorsList.size(), true);
			sortProvider.refresh();
			sortProvider.getList().addAll(selectauthorsList);
		} else {
			authorCellTable.setRowData(currentAuthorsList);
			authorCellTable.setRowCount(currentAuthorsList.size(), true);
			sortProvider.refresh();
			sortProvider.getList().addAll(currentAuthorsList);
		}
		authorCellTable.setRowCount(currentAuthorsList.size(), true);
		sortProvider.refresh();
		sortProvider.getList().addAll(currentAuthorsList);
		addAuthorColumnToTable(MatContext.get().getMeasureLockService().checkForEditPermission());
		sortProvider.addDataDisplay(authorCellTable);

		final Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("authorListSummary",
				"In the following Measure Type List table,Select is given in first Column for selection and Author is given in Second column.");
		authorCellTable.getElement().setAttribute("id", "AuthorListCellTable");
		authorCellTable.getElement().setAttribute("aria-describedby", "authorListSummary");

		final VerticalPanel vp = new VerticalPanel();
		vp.add(invisibleLabel);
		vp.add(authorCellTable);
		vp.setWidth("100%");
		authorSPanel.setWidget(vp);
	}

	private void addAuthorColumnToTable(boolean editable) {
		final Label measureSearchHeader = new Label("Measure Developer List");
		measureSearchHeader.getElement().setId("measureDeveloperHeader_Label");
		measureSearchHeader.setStyleName("invisibleTableCaption");
		final com.google.gwt.dom.client.TableElement elem = authorCellTable.getElement().cast();
		final TableCaptionElement caption = elem.createCaption();
		caption.appendChild(measureSearchHeader.getElement());
		authorSelectionModel = new MultiSelectionModel<>();
		authorCellTable.setSelectionModel(authorSelectionModel);
		final MatCheckBoxCell chbxCell = new MatCheckBoxCell(false, true, !editable);
		final Column<Author, Boolean> selectColumn = new Column<Author, Boolean>(chbxCell) {
			@Override
			public Boolean getValue(Author object) {
				chbxCell.setTitle("Click checkbox to select " + object.getAuthorName());
				boolean isSelected = false;
				for (int i = 0; i < authorsSelectedList.size(); i++) {
					if (authorsSelectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
						isSelected = true;
						break;
					}
				}
				return isSelected;
			}
		};

		selectColumn.setFieldUpdater(new FieldUpdater<Author, Boolean>() {
			@Override
			public void update(int index, Author object, Boolean value) {
				authorSelectionModel.setSelected(object, value);
				if (value) {
					model.getSelectedDeveloperList().add(object);
				} else {
					for (int i = 0; i < model.getSelectedDeveloperList().size(); i++) {
						if (model.getSelectedDeveloperList().get(i).getId().equalsIgnoreCase(object.getId())) {
							model.getSelectedDeveloperList().remove(i);
							break;
						}
					}

				}

			}
		});

		authorCellTable.addColumn(selectColumn, SafeHtmlUtils.fromSafeConstant("<span title='Select'>" + "Select" + "</span>"));

		final Column<Author, SafeHtml> measureNameColumn = new Column<Author, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(Author object) {
				return CellTableUtility.getColumnToolTip(object.getAuthorName(), " OID " + object.getOrgId());
			}
		};

		authorCellTable.addColumn(measureNameColumn, SafeHtmlUtils.fromSafeConstant("<span title='Measure Developers Name'>" + "Measure Developer" + "</span>"));
	}

	public void updateMeasureDevelopersSelectedList(List<Author> measureDeveloperList) {
		if (!authorsSelectedList.isEmpty()) {
			for (int i = 0; i < authorsSelectedList.size(); i++) {
				for (int j = 0; j < measureDeveloperList.size(); j++) {
					if (authorsSelectedList.get(i).getId().equalsIgnoreCase(measureDeveloperList.get(j).getId())) {
						authorsSelectedList.set(i, measureDeveloperList.get(j));
						break;
					}
				}
			}
		}

	}

	private  List<Author> swapMeasureDevelopersList(List<Author> authorsList) {
		final List<Author> authorsListSelected = new ArrayList<>();
		authorsListSelected.addAll(authorsSelectedList);
		for (int i = 0; i < authorsList.size(); i++) {
			if (!authorsSelectedList.contains(authorsList.get(i))) {
				authorsListSelected.add(authorsList.get(i));
			}
		}

		return authorsListSelected;
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
		final VerticalPanel moreMeasureDetailsVP = new VerticalPanel();
		buildStewardListComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		moreMeasureDetailsVP.add(new SpacerWidget());
		buildAuthorTableComponent(moreMeasureDetailsVP);
		moreMeasureDetailsVP.add(new SpacerWidget());
		moreMeasureDetailsVP.add(new SpacerWidget());
		getStewardListBox().addChangeHandler(event -> measureStewardDeveloperObserver.handleValueChanged());
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
		return this.model;
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
		this.originalModel = (MeasureStewardDeveloperModel) model;
		this.model = new MeasureStewardDeveloperModel(this.originalModel);
	}

	@Override
	public void setObserver(MeasureDetailsComponentObserver observer) {
		this.measureStewardDeveloperObserver = (MeasureStewardDeveloperObserver) observer;
	}

	@Override
	public MeasureDetailsComponentObserver getObserver() {
		return this.measureStewardDeveloperObserver;
	}

	public ListBoxMVP getStewardListBox() {
		return stewardListBox;
	}

	public void setStewardListBox(ListBoxMVP stewardListBox) {
		this.stewardListBox = stewardListBox;
	}

	public String getStewardId() {
		return stewardId;
	}

	public void setStewardId(String stewardId) {
		this.stewardId = stewardId;
	}

	public String getStewardValue() {
		return stewardValue;
	}

	public void setStewardValue(String stewardValue) {
		this.stewardValue = stewardValue;
	}

	public List<Author> getAuthorsSelectedList() {
		return authorsSelectedList;
	}

	public void setAuthorsSelectedList(List<Author> authorsSelectedList) {
		this.authorsSelectedList = authorsSelectedList;
	}

	@Override
	public Widget getFirstElement() {
		return stewardListBox.asWidget();
	}

}
