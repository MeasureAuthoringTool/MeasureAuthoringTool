package mat.client.clause;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import mat.client.CustomPager;
import mat.client.codelist.HasListBox;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.model.MatValueSet;
import mat.shared.ConstantMessages;
import org.apache.commons.lang3.StringUtils;
import org.gwtbootstrap3.client.ui.Button;

import java.util.List;
import java.util.ListIterator;


/**
 * The Class QDSCodeListSearchView.
 */
public class QDSCodeListSearchView  implements QDSCodeListSearchPresenter.SearchDisplay {

	/** The all data type input. */
	private ListBoxMVP allDataTypeInput = new ListBoxMVP();

	/** The apply to measure button. */
	private Button applyToMeasureButton = new PrimaryButton("Apply to Measure", "primaryButton");

	/** The cancel button. */
	private Button cancelButton = new SecondaryButton("Cancel");

	/** The container panel. */
	private SimplePanel containerPanel = new SimplePanel();

	/** The current mat value set. */
	private MatValueSet currentMatValueSet;

	/** The data type change handler. */
	private  ValueChangeHandler<String> dataTypeChangeHandler = new ValueChangeHandler<String>() {
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			specificOccurrence.setValue(false);
			String selectedValue = event.getValue();
		    if (!selectedValue.isEmpty() && !selectedValue.equals("")) {
		    	applyToMeasureButton.setEnabled(true);
		    } else {
		    	applyToMeasureButton.setEnabled(false);
		    }

		    ListBoxMVP listbox = (ListBoxMVP) event.getSource();
		    if (listbox.getItemText(listbox.getSelectedIndex()).equalsIgnoreCase(ConstantMessages.ATTRIBUTE)) {
		    	specificOccurrence.setValue(false);
		    	specificOccurrence.setEnabled(false);
		    } else {
		    	specificOccurrence.setEnabled(true);
		    }
		}
	};

	/** The data type panel. */
	private SimplePanel dataTypePanel = new SimplePanel();

	/** The data types list box. */
	private ListBoxMVP dataTypesListBox = new ListBoxMVP();

	/** The date input. */
	private DateBoxWithCalendar dateInput = new DateBoxWithCalendar(DateTimeFormat.getFormat("yyyyMMdd"));

	/** The disclosure panel. */
	private DisclosurePanel disclosurePanel = new DisclosurePanel("Element without VSAC value set");

	/** The disclosure panel vsac. */
	private DisclosurePanel disclosurePanelVSAC = new DisclosurePanel("Element with VSAC value set");

	/** Effective Date. */
	private CustomCheckBox effectiveDate = new CustomCheckBox("Select Effective Date", "Effective Date", 1);
	

	/** EffectiveDate change handler. */
	private  ValueChangeHandler<Boolean> effectiveDateChangeHandler = new ValueChangeHandler<Boolean>() {
		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			if (effectiveDate.getValue().equals(Boolean.TRUE)) {
				version.setValue(Boolean.FALSE);
				dateInput.setEnabled(true);
			} else {
				dateInput.setValue(StringUtils.EMPTY);
				dateInput.setEnabled(false);
			}
		}
	};

	/** The error message panel. */
	private ErrorMessageDisplay errorMessagePanel = new ErrorMessageDisplay();

	/** The error message user defined panel. */
	private ErrorMessageDisplay errorMessageUserDefinedPanel = new ErrorMessageDisplay();

	/** The oid input. */
	private TextBox oidInput = new TextBox();

	/** The psuedo qdm to measure. */
	private Button psuedoQDMToMeasure = new PrimaryButton("Apply to Measure", "primaryButton");

	/** The retrieve button. */
	private Button retrieveButton = new PrimaryButton("Search", "primaryMetaDataButton");

	/** The specific occurrence. */
	private CustomCheckBox specificOccurrence = new CustomCheckBox(ConstantMessages.TOOLTIP_FOR_OCCURRENCE,
												"Specific Occurrence", true); //US 450

	/** The success message panel. */
	private SuccessMessageDisplay successMessagePanel = new SuccessMessageDisplay();

	/** The success message user defined panel. */
	private SuccessMessageDisplay successMessageUserDefinedPanel = new SuccessMessageDisplay();

	/** The user defined input. */
	private TextBox userDefinedInput = new TextBox();

	/** The value set details panel. */
	private VerticalPanel valueSetDetailsPanel = new VerticalPanel();

	/** Version. */
	private CustomCheckBox version = new CustomCheckBox("Select Version", "Version", 1);

	/** Version change handler. */
	private  ValueChangeHandler<Boolean> versionChangeHandler = new ValueChangeHandler<Boolean>() {
		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			if (version.getValue().equals(Boolean.TRUE)) {
				effectiveDate.setValue(Boolean.FALSE);
				dateInput.setEnabled(true);
			} else {
				dateInput.setValue(StringUtils.EMPTY);
				dateInput.setEnabled(false);
			}
		}
	};

	/**
	 * Instantiates a new qDS code list search view.
	 */
	public QDSCodeListSearchView() {
		VerticalPanel vp = new VerticalPanel();
		vp.getElement().setId("vp_VerticalPanel");
		vp.setWidth("100%");
		vp.add(buildElementWithVSACValueSetWidget());
		vp.add(new SpacerWidget());
		vp.add(buildUserDefinedDisclosureWidget());
		vp.add(new SpacerWidget());
		vp.add(new SpacerWidget());
		vp.add(new SpacerWidget());
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.getElement().setId("mainPanel_HorizontalPanel");
		mainPanel.setWidth("100%");
		mainPanel.add(vp);
		containerPanel.getElement().setId("subContainerPanel");
		containerPanel.setWidth("100%");
		containerPanel.add(mainPanel);
		containerPanel.setStyleName("qdsContentPanel");
		MatContext.get().setQDSView(this);
		valueSetDetailsPanel.setVisible(false);
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return containerPanel;
	}

	/**
	 * Builds the element with vsac value set widget.
	 *
	 * @return the widget
	 */
	private Widget buildElementWithVSACValueSetWidget() {
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.getElement().setId("mainPanel_VerticalPanel");
		mainPanel.setWidth("100%");
		mainPanel.add(successMessagePanel);
		mainPanel.add(errorMessagePanel);
		mainPanel.add(buildSearchPanel());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		valueSetDetailsPanel.getElement().setId("valueSetDetailsPanel_VerticalPanel");
		valueSetDetailsPanel.setStyleName("valueSetDetailsPanel");
		valueSetDetailsPanel.setWidth("95%");
		mainPanel.add(valueSetDetailsPanel);
		disclosurePanelVSAC.setWidth("100%");
		disclosurePanelVSAC.add(mainPanel);
		disclosurePanelVSAC.setOpen(true);
		return disclosurePanelVSAC;
	}

	/**
	 * Builds the search panel.
	 *
	 * @return the widget
	 */
	private Widget buildSearchPanel() {
		VerticalPanel searchPanel = new VerticalPanel();
		searchPanel.getElement().setId("searchPanel_VerticalPanel");
		searchPanel.setStyleName("valueSetSearchPanel");
		Label searchHeader = new Label("Search");
		searchHeader.getElement().setId("searchHeader_Label");
		searchHeader.setStyleName("valueSetHeader");
		searchHeader.getElement().setAttribute("tabIndex", "0");
		searchPanel.add(searchHeader);
		searchPanel.add(new SpacerWidget());
		oidInput.getElement().setId("oidInput_TextBox");
		oidInput.getElement().setAttribute("tabIndex", "0");
		oidInput.setTitle("Enter OID");
		oidInput.setWidth("300px");
		oidInput.setMaxLength(200);
		effectiveDate.getElement().setAttribute("id", "effectiveDate_CheckBox");
		version.getElement().setAttribute("id", "version_CheckBox");
		HorizontalPanel versionEffectiveDatePanel = new HorizontalPanel();
		versionEffectiveDatePanel.getElement().setId("versionEffectiveDate_HorizontalPanel");
		versionEffectiveDatePanel.add(version);
		versionEffectiveDatePanel.add(effectiveDate);
		version.addValueChangeHandler(versionChangeHandler);
		effectiveDate.addValueChangeHandler(effectiveDateChangeHandler);
		effectiveDate.addStyleName("secondLabel");
		versionEffectiveDatePanel.addStyleName("marginTop");
		dateInput.getElement().setId("dateInput_DateBoxWithCalendar");
		dateInput.setTitle("Enter Date");
		dateInput.getElement().setAttribute("tabIndex", "0");
		dateInput.setEnabled(false);
		retrieveButton.getElement().setId("retrieveButton_Button");
		retrieveButton.getElement().setAttribute("tabIndex", "0");
		retrieveButton.setTitle("Search");
		Grid queryGrid = new Grid(5, 1);
		queryGrid.setWidget(0, 0, LabelBuilder.buildRequiredLabel(new Label(), "OID:"));
		queryGrid.setWidget(1, 0, oidInput);
		queryGrid.setWidget(2, 0, versionEffectiveDatePanel);
		queryGrid.setWidget(3, 0, dateInput);
		queryGrid.setWidget(4, 0, retrieveButton);
		queryGrid.setStyleName("secondLabel");
		searchPanel.add(queryGrid);
		return searchPanel;
	}

	/**
	 * Builds the user defined disclosure widget.
	 *
	 * @return the widget
	 */
	private Widget buildUserDefinedDisclosureWidget() {
		HorizontalPanel horiPanel = new HorizontalPanel();
		VerticalPanel valueSetPanel = new VerticalPanel();
		VerticalPanel dataTypePanel = new VerticalPanel();

		Widget widgetValueSet = LabelBuilder.buildLabel(userDefinedInput, "Name");
		valueSetPanel.add(widgetValueSet);
		valueSetPanel.add(new SpacerWidget());
		userDefinedInput.setWidth("230px");
		userDefinedInput.setMaxLength(255);
		userDefinedInput.getElement().setId("userDefinedInput_TextBox");
		userDefinedInput.getElement().setTitle("userDefinedInput_TextBox");
		valueSetPanel.add(userDefinedInput);

		Widget widgetDataType = LabelBuilder.buildLabel("Select Datatype", "Select Datatype");
		dataTypePanel.add(widgetDataType);
		dataTypePanel.add(new SpacerWidget());
		allDataTypeInput.getElement().setId("allDataTypeInput_ListBoxMVP");
		allDataTypeInput.getElement().setTitle("allDataTypeInput_ListBoxMVP");
		dataTypePanel.add(allDataTypeInput);
		allDataTypeInput.addFocusHandler(
				new FocusHandler() {
					@Override
					public void onFocus(FocusEvent event) {
						MatContext.get().clearDVIMessages();
					}
		});
		dataTypePanel.setStyleName("marginLeftRight");
		horiPanel.add(valueSetPanel);
		horiPanel.add(dataTypePanel);

		HorizontalPanel buttonHorizontalPanel = new HorizontalPanel();
		psuedoQDMToMeasure.setTitle("Apply to Measure");
		psuedoQDMToMeasure.getElement().setId("psuedoQDMToMeasure_Button");
		psuedoQDMToMeasure.getElement().setTitle("psuedoQDMToMeasure_Button");
		buttonHorizontalPanel.add(psuedoQDMToMeasure);
		buttonHorizontalPanel.add(new SpacerWidget());

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(horiPanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(successMessageUserDefinedPanel);
		mainPanel.add(errorMessageUserDefinedPanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buttonHorizontalPanel);
		mainPanel.add(new SpacerWidget());
		disclosurePanel.add(mainPanel);
		return disclosurePanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#buildValueSetDetailsWidget(java.util.ArrayList)
	 */
	@Override
	public void buildValueSetDetailsWidget(List<MatValueSet> matValueSets) {
		if (matValueSets != null) {
			MatValueSet matValueSet = matValueSets.get(0);
			currentMatValueSet = matValueSet;
	
			valueSetDetailsPanel.clear();
			valueSetDetailsPanel.add(createDetailsWidget(matValueSet));
			if (matValueSet.isGrouping()) {
				valueSetDetailsPanel.add(new SpacerWidget());
				valueSetDetailsPanel.add(createGroupingMembersCellTable(matValueSet));
			}
			valueSetDetailsPanel.add(new SpacerWidget());
			valueSetDetailsPanel.add(new SpacerWidget());
			valueSetDetailsPanel.add(new SpacerWidget());
			valueSetDetailsPanel.add(createDataTypeWidget());
		}
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#clearVSACValueSetMessages()
	 */
	@Override
	public void clearVSACValueSetMessages() {
		getSuccessMessageDisplay().clear();
		getErrorMessageDisplay().clear();
	}

	/**
	 * Creates the data type widget.
	 *
	 * @return the widget
	 */
	private Widget createDataTypeWidget() {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.getElement().setId("vPanel_VerticalPanel");
		vPanel.addStyleName("valueSetMarginLeft_7px");
		vPanel.add(LabelBuilder.buildLabel(new Label(), "Select Datatype"));
		dataTypesListBox.getElement().setId("dataTypesListBox_ListBox");
		dataTypesListBox.setTitle("Select Datatype");
		dataTypesListBox.setSelectedIndex(0);
		dataTypesListBox.addValueChangeHandler(dataTypeChangeHandler);
		vPanel.add(dataTypesListBox);
		vPanel.add(new SpacerWidget());
		specificOccurrence.getElement().setId("specificOccurrence_CheckBox");
		specificOccurrence.setValue(false);
		vPanel.add(specificOccurrence);
		vPanel.add(new SpacerWidget());
		vPanel.add(new SpacerWidget());
		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.getElement().setId("buttonsPanel_HorizontalPanel");
		applyToMeasureButton.getElement().setId("applyToMeasureButton_Button");
		applyToMeasureButton.addStyleName("firstLabel");
		applyToMeasureButton.setTitle("Apply To Measure");
		applyToMeasureButton.setEnabled(false);
		buttonsPanel.add(applyToMeasureButton);
		cancelButton.getElement().setId("cancelButton_Button");
		cancelButton.setTitle("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resetVSACValueSetWidget();
				clearVSACValueSetMessages();
			}
		});
		buttonsPanel.add(cancelButton);

		boolean editable = MatContext.get().getMeasureLockService().checkForEditPermission();
		dataTypesListBox.setEnabled(editable);
		specificOccurrence.setEnabled(editable);

		vPanel.add(buttonsPanel);
		return vPanel;
	}

	/**
	 * Creates the details widget.
	 *
	 * @param matValueSet
	 *            the mat value set
	 * @return the widget
	 */
	private Widget createDetailsWidget(MatValueSet matValueSet) {
		VerticalPanel detailsPanel = new VerticalPanel();
		detailsPanel.getElement().setId("detailsPanel_VerticalPanel");
		detailsPanel.setWidth("100%");

		Label detailsHeader = new Label("Value set details");
		detailsHeader.getElement().setId("detailsHeader_Label");
		detailsHeader.getElement().setAttribute("tabIndex", "0");
		detailsHeader.setStyleName("valueSetHeader");
		detailsPanel.add(detailsHeader);

		Grid details = new Grid(6, 3);
		details.setCellSpacing(5);
		details.getColumnFormatter().setWidth(0, "35%");
		details.getColumnFormatter().setWidth(1, "35%");
		details.getColumnFormatter().setWidth(2, "30%");
		details.getRowFormatter().addStyleName(0, "bold");
		details.getRowFormatter().addStyleName(2, "bold");
		details.getRowFormatter().addStyleName(4, "bold");
		details.getRowFormatter().setVerticalAlign(1, HasVerticalAlignment.ALIGN_TOP);
		details.getRowFormatter().setVerticalAlign(3, HasVerticalAlignment.ALIGN_TOP);
		details.getRowFormatter().setVerticalAlign(5, HasVerticalAlignment.ALIGN_TOP);

		details.setWidget(0, 0, createHTML("Name:", "Name", null));
		details.setWidget(1, 0, createHTML(matValueSet.getDisplayName(), "NameValue", null));
		details.setWidget(0, 1, createHTML("OID:", "OID", "valueSetMarginLeft"));
		details.setWidget(1, 1, createHTML(matValueSet.getID(), "oidValue", "valueSetMarginLeft"));
		details.setWidget(0, 2, createHTML("Code System:", "CodeSystem", "valueSetMarginLeft"));
		details.setWidget(1, 2, createHTML(getCodeSystem(matValueSet), "CodeSystemValue", "valueSetMarginLeft"));
		details.setWidget(2, 0, createHTML("Type:", "Type", "valueSetMarginTop"));
		details.setWidget(3, 0, createHTML(matValueSet.getType(), "TypeValue", null));
		details.setWidget(2, 1, createHTML("Version:", "Version", "valueSetMarginLeft,valueSetMarginTop"));
		details.setWidget(3, 1, createHTML(matValueSet.getVersion(), "VersionValue", "valueSetMarginLeft"));
		details.setWidget(2, 2, createHTML("Effective Date:", "EffectiveDate", "valueSetMarginLeft,valueSetMarginTop"));
		details.setWidget(3, 2, createHTML(matValueSet.getRevisionDate(), "EffectiveDateValue", "valueSetMarginLeft"));
		details.setWidget(4, 0, createHTML("Steward:", "Steward", "valueSetMarginTop"));
		details.setWidget(5, 0, createHTML(matValueSet.getSource(), "StewardValue", null));
		details.setWidget(4, 1, createHTML("Status:", "Status", "valueSetMarginLeft,valueSetMarginTop"));
		details.setWidget(5, 1, createHTML(matValueSet.getStatus(), "StatusValue", "valueSetMarginLeft"));
		detailsPanel.add(details);

		return detailsPanel;
	}

	/**
	 * Creates the grouping members cell table.
	 *
	 * @param matValueSet
	 *            the mat value set
	 * @return the widget
	 */
	private Widget createGroupingMembersCellTable(MatValueSet matValueSet) {
		List<MatValueSet> groupedMatValueSets = matValueSet.getGroupedValueSet();

		CellTable<MatValueSet> groupingValueSetTable = new CellTable<MatValueSet>();
		groupingValueSetTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		groupingValueSetTable.getElement().setAttribute("tabIndex", "0");
		groupingValueSetTable.addStyleName("valueSetMarginLeft_7px");
		groupingValueSetTable.addStyleName("valueSetMarginTop");
		groupingValueSetTable.setPageSize(4);
		groupingValueSetTable.redraw();

		TextColumn<MatValueSet> valuesetNameColumn = new TextColumn<MatValueSet>() {
			@Override
			public String getValue(MatValueSet object) {
				return object.getDisplayName();
			}
		};
		groupingValueSetTable.addColumn(valuesetNameColumn, "Value Set Name");

		TextColumn<MatValueSet> oidColumn = new TextColumn<MatValueSet>() {
			@Override
			public String getValue(MatValueSet object) {
				return object.getID();
			}
		};
		groupingValueSetTable.addColumn(oidColumn, "OID");

		TextColumn<MatValueSet> codeSystemColumn = new TextColumn<MatValueSet>() {
			@Override
			public String getValue(MatValueSet object) {
				return object.getCodeSystemName();
			}
		};
		groupingValueSetTable.addColumn(codeSystemColumn, "CodeSystem");

		ListDataProvider<MatValueSet> listDataProvider = new ListDataProvider<MatValueSet>();
		listDataProvider.refresh();
		listDataProvider.getList().addAll(groupedMatValueSets);
		listDataProvider.addDataDisplay(groupingValueSetTable);

		VerticalPanel groupingPanel = new VerticalPanel();
		groupingPanel.getElement().setId("groupingPanel_VerticalPanel");
		groupingPanel.setWidth("100%");
		Label groupingHeader = new Label("Grouping value set");
		groupingHeader.getElement().setId("groupingHeader_Label");
		groupingHeader.getElement().setAttribute("tabIndex", "0");
		groupingHeader.setStyleName("valueSetHeader");
		groupingHeader.setWidth("150px");
		groupingPanel.add(groupingHeader);
		groupingPanel.add(groupingValueSetTable);

		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"AppliedQDM");
		spager.addStyleName("valueSetMarginLeft_7px");
        spager.setDisplay(groupingValueSetTable);
        spager.setPageStart(0);
       /* spager.setToolTipAndTabIndex(spager);*/
        groupingPanel.add(spager);

		return groupingPanel;
	}

	/**
	 * Creates the html.
	 *
	 * @param value
	 *            the value
	 * @param id
	 *            the id
	 * @param styles
	 *            the styles
	 * @return the html
	 */
	private HTML createHTML(String value, String id, String styles) {
		HTML html = new HTML(value);
		if ((id != null) && !id.trim().isEmpty()) {
			html.getElement().setId(id);
		} else {
			html.getElement().setId(value);
		}
		if ((styles != null) && !styles.trim().isEmpty()) {
			String[] stylesArray = styles.split(",");
			for (String style : stylesArray) {
				html.addStyleName(style.trim());
			}
		}
		html.setHeight("100%");
		html.setTitle(id);
		html.getElement().setAttribute("tabIndex", "0");
		return html;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getAllDataTypeInput()
	 */
	@Override
	public ListBoxMVP getAllDataTypeInput() {
		return allDataTypeInput;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getApplyToMeasureButton()
	 */
	@Override
	public Button getApplyToMeasureButton() {
		return applyToMeasureButton;
	}

	/**
	 * Gets the code system.
	 *
	 * @param matValueSet
	 *            the mat value set
	 * @return the code system
	 */
	private String getCodeSystem(MatValueSet matValueSet) {
		if (matValueSet.isGrouping()) {
			String codeSystem = StringUtils.EMPTY;
			List<MatValueSet> groupedMatValueSets = matValueSet.getGroupedValueSet();
			if (groupedMatValueSets != null) {
				ListIterator<MatValueSet> itr = groupedMatValueSets.listIterator();
				while (itr.hasNext()) {
					MatValueSet groupedMatValueSet = itr.next();
					String codeSystemName = groupedMatValueSet.getCodeSystemName();
					if (codeSystemName != null) {
						codeSystem += codeSystemName;
						if (itr.hasNext() && !codeSystem.trim().isEmpty() && !codeSystemName.trim().isEmpty()) {
							codeSystem += ", ";
						}
					}
				}
			}
			return codeSystem;
		} else {
			return matValueSet.getCodeSystemName();
		}
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getCurrentMatValueSet()
	 */
	@Override
	public MatValueSet getCurrentMatValueSet() {
		return currentMatValueSet;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getDataTypesListBox()
	 */
	@Override
	public ListBoxMVP getDataTypesListBox() {
		return dataTypesListBox;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getDataTypeText(mat.client.shared.ListBoxMVP)
	 */
	@Override
	public String getDataTypeText(ListBoxMVP inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getItemText(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getDataTypeValue(mat.client.shared.ListBoxMVP)
	 */
	@Override
	public String getDataTypeValue(ListBoxMVP inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getDataTypeWidget()
	 */
	@Override
	public Widget getDataTypeWidget() {
		return dataTypePanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getDateInput()
	 */
	@Override
	public DateBoxWithCalendar getDateInput() {
		return dateInput;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getDisclosurePanel()
	 */
	@Override
	public DisclosurePanel getDisclosurePanel() {
		return disclosurePanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getDisclosurePanelVSAC()
	 */
	@Override
	public DisclosurePanel getDisclosurePanelVSAC() {
		return disclosurePanelVSAC;
	}

	/**
	 * Gets the effective date.
	 *
	 * @return the effective date
	 */
	@Override
	public CustomCheckBox getEffectiveDate() {
		return effectiveDate;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplay getErrorMessageDisplay() {
		return errorMessagePanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getErrorMessageUserDefinedPanel()
	 */
	@Override
	public ErrorMessageDisplay getErrorMessageUserDefinedPanel() {
		return errorMessageUserDefinedPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getOIDInput()
	 */
	@Override
	public TextBox getOIDInput() {
		return oidInput;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getPsuedoQDMToMeasure()
	 */
	@Override
	public Button getPsuedoQDMToMeasure() {
		return psuedoQDMToMeasure;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getRetrieveButton()
	 */
	@Override
	public Button getRetrieveButton() {
		return retrieveButton;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getSpecificOccurrenceInput()
	 */
	@Override
	public CustomCheckBox getSpecificOccurrenceInput(){
		return specificOccurrence;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getSuccessMessageDisplay()
	 */
	@Override
	public SuccessMessageDisplay getSuccessMessageDisplay() {
		return successMessagePanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getSuccessMessageUserDefinedPanel()
	 */
	@Override
	public SuccessMessageDisplay getSuccessMessageUserDefinedPanel() {
		return successMessageUserDefinedPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getUserDefinedInput()
	 */
	@Override
	public TextBox getUserDefinedInput() {
		return userDefinedInput;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#getValueSetDetailsPanel()
	 */
	@Override
	public VerticalPanel getValueSetDetailsPanel() {
		return valueSetDetailsPanel;
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	@Override
	public CustomCheckBox getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#resetVSACValueSetWidget()
	 */
	@Override
	public void resetVSACValueSetWidget() {
		getOIDInput().setValue(StringUtils.EMPTY);
		getVersion().setValue(Boolean.FALSE);
		getEffectiveDate().setValue(Boolean.FALSE);
		getDateInput().setValue(StringUtils.EMPTY);
		getDateInput().setEnabled(false);
		getValueSetDetailsPanel().setVisible(false);
	}

	/**
	 * Sets the all data type input.
	 *
	 * @param allDataTypeInput
	 *            the new all data type input
	 */
	public void setAllDataTypeInput(ListBoxMVP allDataTypeInput) {
		this.allDataTypeInput = allDataTypeInput;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#setAllDataTypeOptions(java.util.List)
	 */
	@Override
	public void setAllDataTypeOptions(List<? extends HasListBox> texts) {
		setListBoxItems(allDataTypeInput, texts, MatContext.PLEASE_SELECT);
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSCodeListSearchPresenter.SearchDisplay#setDataTypesListBoxOptions(java.util.List)
	 */
	@Override
	public void setDataTypesListBoxOptions(List<? extends HasListBox> texts) {
		setListBoxItems(dataTypesListBox, texts, MatContext.PLEASE_SELECT);
	}

	/**
	 * Sets the disclosure panel.
	 * 
	 * @param disclosurePanel
	 *            the new disclosure panel
	 */
	public void setDisclosurePanel(DisclosurePanel disclosurePanel) {
		this.disclosurePanel = disclosurePanel;
	}

	/**
	 * Sets the disclosure panel vsac.
	 *
	 * @param disclosurePanelVSAC
	 *            the new disclosure panel vsac
	 */
	public void setDisclosurePanelVSAC(DisclosurePanel disclosurePanelVSAC) {
		this.disclosurePanelVSAC = disclosurePanelVSAC;
	}

	/**
	 * Sets the error message user defined panel.
	 *
	 * @param errorMessageUserDefinedPanel
	 *            the new error message user defined panel
	 */
	public void setErrorMessageUserDefinedPanel(
			ErrorMessageDisplay errorMessageUserDefinedPanel) {
		this.errorMessageUserDefinedPanel = errorMessageUserDefinedPanel;
	}

	/**
	 * Sets the list box items.
	 *
	 * @param listBox
	 *            the list box
	 * @param itemList
	 *            the item list
	 * @param defaultOption
	 *            the default option
	 */
	private void setListBoxItems(ListBox listBox, List<? extends HasListBox> itemList, String defaultOption) {
		listBox.clear();
		listBox.addItem(defaultOption, "");
		if (itemList != null) {
			for (HasListBox listBoxContent : itemList) {
				//MAT-4366
				if(! listBoxContent.getItem().equalsIgnoreCase("Patient Characteristic Birthdate") && ! listBoxContent.getItem().equalsIgnoreCase("Patient Characteristic Expired"))
				listBox.addItem(listBoxContent.getItem(),""+listBoxContent.getValue());
			}
			
			SelectElement selectElement = SelectElement.as(listBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
					.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(optionElement.getText());
			}
		}
	}

	/**
	 * Sets the psuedo qdm to measure.
	 *
	 * @param psuedoQDMToMeasure
	 *            the new psuedo qdm to measure
	 */
	public void setPsuedoQDMToMeasure(Button psuedoQDMToMeasure) {
		this.psuedoQDMToMeasure = psuedoQDMToMeasure;
	}

	/**
	 * Sets the success message user defined panel.
	 *
	 * @param successMessageUserDefinedPanel
	 *            the new success message user defined panel
	 */
	public void setSuccessMessageUserDefinedPanel(
			SuccessMessageDisplay successMessageUserDefinedPanel) {
		this.successMessageUserDefinedPanel = successMessageUserDefinedPanel;
	}

	/**
	 * Sets the user defined input.
	 *
	 * @param userDefinedInput
	 *            the new user defined input
	 */
	public void setUserDefinedInput(TextBox userDefinedInput) {
		this.userDefinedInput = userDefinedInput;
	}
}
