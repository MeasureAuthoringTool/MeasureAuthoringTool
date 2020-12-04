package mat.client.cqlworkspace.valuesets;

import com.google.gwt.cell.client.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import mat.client.CustomPager;
import mat.client.buttons.CodesValuesetsButtonToolBar;
import mat.client.cqlworkspace.SharedCQLWorkspaceUtility;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.shared.*;
import mat.client.util.CellTableUtility;
import mat.client.util.MatTextBox;
import mat.client.validator.ErrorHandler;
import mat.model.CQLValueSetTransferObject;
import mat.model.CodeListSearchDTO;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.ClickableSafeHtmlCell;
import mat.shared.ConstantMessages;
import mat.vsacmodel.MatConcept;
import mat.vsacmodel.MatConceptList;
import mat.vsacmodel.ValueSet;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static mat.client.cqlworkspace.valuesets.CQLAppliedValueSetUtility.getOidFromUrl;
import static mat.client.cqlworkspace.valuesets.CQLAppliedValueSetUtility.isFhirUrl;

public class CQLAppliedValueSetView implements HasSelectionHandlers<Boolean> {
    private static final Logger log = Logger.getLogger(CQLAppliedValueSetView.class.getSimpleName());

    private Boolean isLoading = false;
    private final String TEXT_APPLY = "Apply";
    private final String TEXT_CANCEL = "Cancel";
    private final String TEXT_OID = "OID";
    private final String TEXT_NAME = "Name";
    private final String TEXT_PROGRAM = "Program";
    private final String TEXT_RELEASE = "Release";
    private final String ENTER_OID = "Enter OID Required";
    private final String ENTER_NAME = "Enter Name Required";
    private final String RETRIEVE_OID = "Retrieve OID";

    public interface Observer {

        void onModifyClicked(CQLQualityDataSetDTO result);

        void onDeleteClicked(CQLQualityDataSetDTO result, int index);

    }

    private Observer observer;
    private SimplePanel containerPanel = new SimplePanel();
    private HandlerManager handlerManager = new HandlerManager(this);
    private Panel cellTablePanel = new Panel();
    private PanelBody cellTablePanelBody = new PanelBody();
    private static final int TABLE_ROW_COUNT = 10;
    private CellTable<CQLQualityDataSetDTO> table;
    private List<CQLQualityDataSetDTO> qdmSelectedList;
    private List<CQLQualityDataSetDTO> allValueSetsList;
    private MultiSelectionModel<CQLQualityDataSetDTO> selectionModel;
    private ListDataProvider<CQLQualityDataSetDTO> listDataProvider;
    private Button updateVSACButton = new Button("Update From VSAC ");
    private CQLQualityDataSetDTO lastSelectedObject;
    private ListBox programListBox = new ListBox();
    private ListBox releaseListBox = new ListBox();
    private MatTextBox nameInput = new MatTextBox();
    private MatTextBox oidInput = new MatTextBox() {
        @Override
        public void onBrowserEvent(Event event) {
            super.onBrowserEvent(event);
            if ("input".equals(event.getType()) && this.getText().trim().isEmpty()) {
                ValueChangeEvent.fire(this, this.getText());
            }
        }
    };
    private Button goButton = new Button(RETRIEVE_OID);
    private CustomQuantityTextBox suffixInput = new CustomQuantityTextBox(4);
    private boolean isEditable;
    private CheckBox specificOcurChkBox;
    private MatSimplePager spager;
    private Button saveValueSet = new Button(TEXT_APPLY);
    private Button cancelButton = new Button(TEXT_CANCEL);
    private VerticalPanel mainPanel;
    private PanelHeader searchHeader = new PanelHeader();
    private HelpBlock helpBlock = new HelpBlock();
    SimplePanel cellTableMainPanel = new SimplePanel();
    HTML heading = new HTML();
    private CodesValuesetsButtonToolBar copyPasteClearButtonToolBar = new CodesValuesetsButtonToolBar("valueset");
    private InAppHelp inAppHelp = new InAppHelp("");
    private ErrorHandler errorHandler = new ErrorHandler();

    public CQLAppliedValueSetView() {

        VerticalPanel verticalPanel = new VerticalPanel();
        HorizontalPanel mainPanel = new HorizontalPanel();
        mainPanel.getElement().setId("mainPanel_HorizontalPanel");
        SimplePanel simplePanel = new SimplePanel();
        simplePanel.getElement().setId("simplePanel_SimplePanel");
        simplePanel.setWidth("5px");

        HorizontalPanel hp = new HorizontalPanel();
        hp.getElement().setId("hp_HorizontalPanel");
        hp.add(buildElementWithVSACValueSetWidget());
        hp.add(simplePanel);

        verticalPanel.getElement().setId("vPanel_VerticalPanel");

        heading.addStyleName("leftAligned");
        heading.getElement().setTabIndex(-1);

        verticalPanel.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));
        verticalPanel.add(new SpacerWidget());

        verticalPanel.add(new SpacerWidget());
        verticalPanel.add(new SpacerWidget());
        verticalPanel.add(hp);
        verticalPanel.add(new SpacerWidget());

        mainPanel.add(verticalPanel);
        containerPanel.getElement().setAttribute("id",
                "subQDMAPPliedListContainerPanel");
        containerPanel.add(mainPanel);
        containerPanel.setStyleName("cqlqdsContentPanel");
    }

    public SimplePanel buildCellTableWidget() {
        cellTableMainPanel.clear();
        VerticalPanel vPanel = new VerticalPanel();
        vPanel.setStyleName("cqlqdsContentPanel");
        vPanel.getElement().setId("hPanel_HorizontalPanel");
        vPanel.setWidth("100%");
        updateVSACButton.setType(ButtonType.PRIMARY);
        updateVSACButton.setTitle("Retrieve the most recent versions of value sets from VSAC");
        updateVSACButton.getElement().setId("updateVsacButton_Button");

        updateVSACButton.setMarginTop(10);
        updateVSACButton.setMarginRight(2);
        updateVSACButton.setTitle("Update From VSAC");
        updateVSACButton.setText("Update From VSAC");
        updateVSACButton.setIcon(IconType.REFRESH);
        updateVSACButton.setIconSize(IconSize.LARGE);
        updateVSACButton.setPull(Pull.RIGHT);
        vPanel.add(updateVSACButton);
        vPanel.add(new SpacerWidget());
        vPanel.add(copyPasteClearButtonToolBar.getButtonToolBar());
        vPanel.add(cellTablePanel);

        cellTableMainPanel.add(vPanel);
        return cellTableMainPanel;
    }

    private Widget buildElementWithVSACValueSetWidget() {
        mainPanel = new VerticalPanel();
        mainPanel.getElement().setId("mainPanel_VerticalPanel");

        mainPanel.add(buildSearchPanel());
        mainPanel.add(new SpacerWidget());
        mainPanel.add(new SpacerWidget());
        return mainPanel;
    }


    private Widget buildSearchPanel() {
        HorizontalPanel buttonLayout = new HorizontalPanel();
        buttonLayout.getElement().setId("buttonLayout_HorizontalPanel");
        buttonLayout.setStylePrimaryName("myAccountButtonLayout");
        Panel searchPanel = new Panel();
        PanelBody searchPanelBody = new PanelBody();
        searchPanel.getElement().setId("searchPanel_VerticalPanel");
        searchPanel.setStyleName("cqlvalueSetSearchPanel");

        searchHeader.setStyleName("CqlWorkSpaceTableHeader");
        searchPanel.add(searchHeader);
        searchPanelBody.add(new SpacerWidget());

        FormGroup messageFormGroup = new FormGroup();
        messageFormGroup.add(helpBlock);
        messageFormGroup.getElement().setAttribute("role", "alert");
        helpBlock.setColor("transparent");
        helpBlock.setHeight("0px");
        searchPanelBody.add(messageFormGroup);

        nameInput.getElement().setId("nameInput_TextBox");
        nameInput.getElement().setAttribute("tabIndex", "0");

        nameInput.setTitle(ENTER_NAME);
        nameInput.setWidth("450px");
        nameInput.setHeight("30px");

        suffixInput.getElement().setId("suffixInput_TextBox");

        suffixInput.setTitle("Suffix must be an integer between 1-4 characters");
        suffixInput.setWidth("150px");
        suffixInput.setHeight("30px");

        saveValueSet.setText(TEXT_APPLY);
        saveValueSet.setTitle(TEXT_APPLY);
        saveValueSet.setType(ButtonType.PRIMARY);

        cancelButton.setType(ButtonType.DANGER);
        cancelButton.setTitle(TEXT_CANCEL);

        ButtonToolBar buttonToolBar = new ButtonToolBar();
        buttonToolBar.add(saveValueSet);
        buttonToolBar.add(cancelButton);

        VerticalPanel buttonPanel = new VerticalPanel();
        buttonPanel.add(new SpacerWidget());
        buttonPanel.add(buttonToolBar);
        buttonPanel.add(new SpacerWidget());

        VerticalPanel searchWidgetFormGroup = new VerticalPanel();
        searchWidgetFormGroup.getElement().getStyle().setProperty("padding", "10px");
        searchWidgetFormGroup.getElement().getStyle().setProperty("border", "solid 1px #e8eff7");
        searchWidgetFormGroup.getElement().getStyle().setProperty("marginBottom", "10px");
        FormLabel oidLabel = new FormLabel();

        oidLabel.setText(TEXT_OID);
        oidLabel.setTitle(TEXT_OID);
        searchWidgetFormGroup.add(oidLabel);
        oidInput.setWidth("595px");
        oidInput.setTitle(ENTER_OID);
        oidInput.addBlurHandler(errorHandler.buildRequiredBlurHandler(oidInput));
        searchWidgetFormGroup.add(oidInput);
        searchWidgetFormGroup.add(new SpacerWidget());

        Grid programReleaseGrid = new Grid(1, 3);

        VerticalPanel programPanel = new VerticalPanel();
        programPanel.setWidth("225px");
        FormLabel programLabel = new FormLabel();
        programLabel.setText(TEXT_PROGRAM);
        programLabel.setTitle(TEXT_PROGRAM);
        programPanel.add(programLabel);
        programListBox.setTitle("Program selection list");
        programListBox.setWidth("200px");
        programPanel.add(programListBox);
        CQLAppliedValueSetUtility.getProgramsAndReleases();
        CQLAppliedValueSetUtility.loadPrograms(getProgramListBox());

        VerticalPanel releasePanel = new VerticalPanel();
        releasePanel.setWidth("225px");
        FormLabel releaseLabel = new FormLabel();
        releaseLabel.setText(TEXT_RELEASE);
        releaseLabel.setTitle(TEXT_RELEASE);
        releasePanel.add(releaseLabel);
        releaseListBox.setTitle("Release selection list");
        releaseListBox.setWidth("200px");
        releasePanel.add(releaseListBox);
        CQLAppliedValueSetUtility.initializeReleaseListBoxContent(getReleaseListBox());

        VerticalPanel goPanel = new VerticalPanel();
        goPanel.setWidth("150px");
        goPanel.add(new SpacerWidget());
        goPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        goButton.setType(ButtonType.PRIMARY);
        goButton.setIcon(IconType.SEARCH);
        goButton.setIconPosition(IconPosition.LEFT);
        goButton.setSize(ButtonSize.SMALL);
        goButton.setTitle(RETRIEVE_OID);
        goButton.setPull(Pull.RIGHT);
        goButton.getElement().getStyle().setProperty("marginRight", "5px");
        goPanel.add(goButton);

        programReleaseGrid.setWidget(0, 0, programPanel);
        programReleaseGrid.setWidget(0, 1, releasePanel);
        programReleaseGrid.setWidget(0, 2, goPanel);
        programReleaseGrid.setWidth("600px");
        searchWidgetFormGroup.add(programReleaseGrid);
        searchWidgetFormGroup.add(new SpacerWidget());

        VerticalPanel namePanel = new VerticalPanel();
        FormLabel nameLabel = new FormLabel();
        nameLabel.setText(TEXT_NAME);
        nameLabel.setTitle(TEXT_NAME);
        nameLabel.setFor("nameInput_TextBox");
        namePanel.add(nameLabel);
        namePanel.add(nameInput);
        namePanel.add(new SpacerWidget());

        VerticalPanel suffixPanel = new VerticalPanel();
        FormLabel suffixLabel = new FormLabel();
        suffixLabel.setText("Suffix (Max Length 4)");
        suffixLabel.setTitle("Suffix");
        suffixLabel.setFor("suffixInput_TextBox");
        suffixPanel.add(suffixLabel);
        suffixPanel.add(suffixInput);
        suffixPanel.add(new SpacerWidget());

        VerticalPanel buttonFormGroup = new VerticalPanel();
        buttonFormGroup.add(buttonToolBar);
        buttonFormGroup.add(new SpacerWidget());

        Grid oidGrid = new Grid(1, 1);
        oidGrid.setWidget(0, 0, searchWidgetFormGroup);
        Grid nameGrid = new Grid(1, 2);
        nameGrid.setWidget(0, 0, namePanel);
        nameGrid.setWidget(0, 1, suffixPanel);
        nameGrid.getElement().getStyle().setProperty("marginLeft", "10px");
        Grid buttonGrid = new Grid(2, 1);
        buttonGrid.setWidget(1, 0, buttonFormGroup);
        buttonGrid.getElement().getStyle().setProperty("marginLeft", "10px");

        searchPanelBody.add(oidGrid);
        searchPanelBody.add(nameGrid);
        searchPanelBody.add(buttonGrid);

        searchPanel.add(searchPanelBody);
        return searchPanel;
    }


    public void setProgramReleaseBoxEnabled(Boolean isEnabled) {
        getProgramListBox().setEnabled(isEnabled);
        getReleaseListBox().setEnabled(isEnabled);
    }

    public void buildAppliedValueSetCellTable(List<CQLQualityDataSetDTO> appliedValueSetList, boolean isEditable) {
        log.log(Level.INFO, "Entering buildAppliedValueSetCellTable(...," + isEditable + ")\n" + appliedValueSetList);
        appliedValueSetList = appliedValueSetList.stream().filter(v -> v.getOriginalCodeListName() != null).collect(Collectors.toList());
        log.log(Level.INFO, "After filter: " + appliedValueSetList);
        cellTablePanel.clear();
        cellTablePanelBody.clear();
        cellTablePanel.setStyleName("cellTablePanel");
        PanelHeader qdmElementsHeader = new PanelHeader();
        qdmElementsHeader.getElement().setId("searchHeader_Label");
        qdmElementsHeader.setStyleName("CqlWorkSpaceTableHeader");
        qdmElementsHeader.getElement().setAttribute("tabIndex", "-1");

        HTML searchHeaderText = new HTML("<strong>Applied Value Sets</strong>");
        qdmElementsHeader.add(searchHeaderText);
        cellTablePanel.add(qdmElementsHeader);
        if ((appliedValueSetList != null)
                && (appliedValueSetList.size() > 0)) {
            table = new CellTable<>();
            allValueSetsList = appliedValueSetList;
            setEditable(isEditable);
            table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
            listDataProvider = new ListDataProvider<>();
            qdmSelectedList = new ArrayList<>();
            table.setPageSize(TABLE_ROW_COUNT);
            table.redraw();
            listDataProvider.refresh();
            listDataProvider.getList().addAll(appliedValueSetList);
            ListHandler<CQLQualityDataSetDTO> sortHandler = new ListHandler<>(listDataProvider.getList());
            table.addColumnSortHandler(sortHandler);
            table = addColumnToTable(table, sortHandler, isEditable);
            listDataProvider.addDataDisplay(table);
            CustomPager.Resources pagerResources = GWT
                    .create(CustomPager.Resources.class);
            spager = new MatSimplePager(CustomPager.TextLocation.CENTER,
                    pagerResources, false, 0, true, "valuesetAndCodes");
            spager.setDisplay(table);
            spager.setPageStart(0);
            com.google.gwt.user.client.ui.Label invisibleLabel;
            if (isEditable) {
                invisibleLabel = (com.google.gwt.user.client.ui.Label) LabelBuilder
                        .buildInvisibleLabel(
                                "appliedQDMTableSummary",
                                "In the Following Applied Value Sets table Name in First Column"
                                        + "OID in the Second Column, Program in the third column, Release in the fourth column, "
                                        + "Edit in the fifth Column, Delete in the sixth Column"
                                        + "and Copy in seventh Column. The Applied Value Sets are listed alphabetically in a table.");


            } else {
                invisibleLabel = (com.google.gwt.user.client.ui.Label) LabelBuilder
                        .buildInvisibleLabel(
                                "appliedQDMTableSummary",
                                "In the Following Applied Value Sets table Name in First Column"
                                        + "OID in the Second Column, Program in the third column, Release in the fourth column, "
                                        + "Edit in the fifth Column, Delete in the sixth Column"
                                        + "and Copy in seventh Column. The Applied Value Sets are listed alphabetically in a table.");
            }
            table.getElement().setAttribute("id", "AppliedQDMTable");
            table.getElement().setAttribute("aria-describedby",
                    "appliedQDMTableSummary");

            cellTablePanel.add(invisibleLabel);
            cellTablePanel.add(table);
            cellTablePanel.add(spager);
            cellTablePanel.add(cellTablePanelBody);

            log.log(Level.INFO, "After table");

        } else {
            HTML desc = new HTML("<p> No value sets.</p>");
            cellTablePanelBody.add(desc);
            cellTablePanel.add(desc);
        }
    }

    private CellTable<CQLQualityDataSetDTO> addColumnToTable(
            final CellTable<CQLQualityDataSetDTO> table,
            ListHandler<CQLQualityDataSetDTO> sortHandler, final boolean isEditable) {
        if (table.getColumnCount() != TABLE_ROW_COUNT) {
            Label searchHeader = new Label("Value Sets");
            searchHeader.getElement().setId("searchHeader_Label");
            searchHeader.getElement().setAttribute("tabIndex", "-1");
            com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
            TableCaptionElement caption = elem.createCaption();
            searchHeader.setVisible(false);
            caption.appendChild(searchHeader.getElement());
            selectionModel = new MultiSelectionModel<>();
            table.setSelectionModel(selectionModel);


            table.addColumn(createNameColumn(), SafeHtmlUtils.fromSafeConstant("<span title=\"Name\">" + "Name" + "</span>"));

            table.addColumn(createOIDColumn(), SafeHtmlUtils.fromSafeConstant("<span title=\"OID\">" + "OID" + "</span>"));

            table.addColumn(createProgramColumn(), SafeHtmlUtils.fromSafeConstant("<span title=\"Program\">" + "Program" + "</span>"));

            table.addColumn(createReleaseColumn(), SafeHtmlUtils.fromSafeConstant("<span title=\"Release\">" + "Release" + "</span>"));

            String colName = "";
            colName = "Edit";
            table.addColumn(createEditColumn(), SafeHtmlUtils.fromSafeConstant("<span title='" + colName + "'>  " + colName + "</span>"));

            colName = "Delete";
            table.addColumn(createDeleteColumn(), SafeHtmlUtils.fromSafeConstant("<span title='" + colName + "'>  " + colName + "</span>"));

            colName = "Copy";
            table.addColumn(createCopyColumn(), SafeHtmlUtils.fromSafeConstant("<span title='" + colName + "'>  " + colName + "</span>"));

            table.setWidth("100%", true);
            table.setColumnWidth(0, 25.0, Unit.PCT);
            table.setColumnWidth(1, 25.0, Unit.PCT);
            table.setColumnWidth(2, 14.0, Unit.PCT);
            table.setColumnWidth(3, 14.0, Unit.PCT);
            table.setColumnWidth(4, 7.0, Unit.PCT);
            table.setColumnWidth(5, 8.0, Unit.PCT);
            table.setColumnWidth(6, 7.0, Unit.PCT);
            table.setStyleName("tableWrap");
        }

        return table;
    }

    public Column<CQLQualityDataSetDTO, SafeHtml> createReleaseColumn() {
        Column<CQLQualityDataSetDTO, SafeHtml> releaseColumn = new Column<CQLQualityDataSetDTO, SafeHtml>(
                new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(CQLQualityDataSetDTO object) {
                String title = CQLAppliedValueSetUtility.buildReleaseTitle(object);
                String release = CQLAppliedValueSetUtility.buildReleaseColumnRelease(object);
                log.log(Level.INFO, "getValue for release column: " + release + " title=" + title);
                return CellTableUtility.getColumnToolTip(release, title);
            }
        };
        return releaseColumn;
    }

    public Column<CQLQualityDataSetDTO, SafeHtml> createOIDColumn() {
        Column<CQLQualityDataSetDTO, SafeHtml> oidColumn = new Column<CQLQualityDataSetDTO, SafeHtml>(
                new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(CQLQualityDataSetDTO object) {
                StringBuilder title = CQLAppliedValueSetUtility.buildOIDTitle(object);
                String oid = CQLAppliedValueSetUtility.buildOidColumnOid(object);
                if (MatContext.get().isCurrentModelTypeFhir() && oid != null) {
                    oid = ".../" + getOidFromUrl(oid);
                }
                log.log(Level.INFO, "getValue for oid column: " + oid + " title=" + title);
                return CQLAppliedValueSetUtility.getOIDColumnToolTip(oid, title, object.isHasModifiedAtVSAC(), object.isNotFoundInVSAC());
            }
        };
        return oidColumn;
    }


    public Column<CQLQualityDataSetDTO, SafeHtml> createProgramColumn() {
        Column<CQLQualityDataSetDTO, SafeHtml> programColumn = new Column<CQLQualityDataSetDTO, SafeHtml>(
                new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(CQLQualityDataSetDTO object) {
                String program = CQLAppliedValueSetUtility.getProgramColumnProgram(object);
                String title = CQLAppliedValueSetUtility.getProgramTitle(object, program);
                log.log(Level.INFO, "getValue for oid column: " + program + " title=" + title);
                return CellTableUtility.getColumnToolTip(program, title);
            }
        };
        return programColumn;
    }

    private Column<CQLQualityDataSetDTO, CQLQualityDataSetDTO> createEditColumn() {
        Column<CQLQualityDataSetDTO, CQLQualityDataSetDTO> col = new Column<CQLQualityDataSetDTO, CQLQualityDataSetDTO>(
                getCompositeCell(isEditable, getModifyButtonCell())) {

            @Override
            public CQLQualityDataSetDTO getValue(CQLQualityDataSetDTO object) {
                return object;
            }
        };

        return col;
    }

    private Column<CQLQualityDataSetDTO, CQLQualityDataSetDTO> createDeleteColumn() {
        Column<CQLQualityDataSetDTO, CQLQualityDataSetDTO> col = new Column<CQLQualityDataSetDTO, CQLQualityDataSetDTO>(
                getCompositeCell(isEditable, getDeleteButtonCell())) {

            @Override
            public CQLQualityDataSetDTO getValue(CQLQualityDataSetDTO object) {
                return object;
            }
        };

        return col;
    }

    public Column<CQLQualityDataSetDTO, SafeHtml> createNameColumn() {
        Column<CQLQualityDataSetDTO, SafeHtml> nameColumn = new Column<CQLQualityDataSetDTO, SafeHtml>(
                new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(CQLQualityDataSetDTO object) {
                String value = CQLAppliedValueSetUtility.buildNameValue(object);
                String title = CQLAppliedValueSetUtility.buildNameTitle(value);
                log.log(Level.INFO, "getValue for name column: " + value + " title=" + title);
                return CellTableUtility.getNameColumnToolTip(value, title);
            }
        };
        return nameColumn;
    }

    private Column<CQLQualityDataSetDTO, CQLQualityDataSetDTO> createCopyColumn() {
        Column<CQLQualityDataSetDTO, CQLQualityDataSetDTO> col = new Column<CQLQualityDataSetDTO, CQLQualityDataSetDTO>(
                getCompositeCell(true, getCheckBoxCell())) {

            @Override
            public CQLQualityDataSetDTO getValue(CQLQualityDataSetDTO object) {
                return object;
            }
        };

        return col;
    }


    private CompositeCell<CQLQualityDataSetDTO> getCompositeCell(final boolean isEditable, HasCell<CQLQualityDataSetDTO, ?> cellToAdd) {
        final List<HasCell<CQLQualityDataSetDTO, ?>> cells = new LinkedList<>();

        if (isEditable) {
            cells.add(cellToAdd);
        }

        CompositeCell<CQLQualityDataSetDTO> cell = new CompositeCell<CQLQualityDataSetDTO>(
                cells) {
            @Override
            public void render(Context context, CQLQualityDataSetDTO object,
                               SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<table tabindex=\"-1\"><tbody><tr tabindex=\"-1\">");
                for (HasCell<CQLQualityDataSetDTO, ?> hasCell : cells) {
                    render(context, object, sb, hasCell);
                }
                sb.appendHtmlConstant("</tr></tbody></table>");
            }

            @Override
            protected <X> void render(Context context,
                                      CQLQualityDataSetDTO object, SafeHtmlBuilder sb,
                                      HasCell<CQLQualityDataSetDTO, X> hasCell) {
                Cell<X> cell = hasCell.getCell();
                sb.appendHtmlConstant("<td class='emptySpaces' tabindex=\"-1\">");
                if ((object != null)) {
                    cell.render(context, hasCell.getValue(object), sb);
                } else {
                    sb.appendHtmlConstant("<span tabindex=\"-1\"></span>");
                }
                sb.appendHtmlConstant("</td>");
            }

            @Override
            protected Element getContainerElement(Element parent) {
                return parent.getFirstChildElement().getFirstChildElement()
                        .getFirstChildElement();
            }
        };
        return cell;
    }

    private HasCell<CQLQualityDataSetDTO, SafeHtml> getModifyButtonCell() {

        HasCell<CQLQualityDataSetDTO, SafeHtml> hasCell = new HasCell<CQLQualityDataSetDTO, SafeHtml>() {

            ClickableSafeHtmlCell modifyButonCell = new ClickableSafeHtmlCell();

            @Override
            public Cell<SafeHtml> getCell() {
                return modifyButonCell;
            }

            @Override
            public FieldUpdater<CQLQualityDataSetDTO, SafeHtml> getFieldUpdater() {
                return (index, object, value) -> {
                    if ((object != null)) {
                        observer.onModifyClicked(object);
                    }
                };
            }

            @Override
            public SafeHtml getValue(CQLQualityDataSetDTO object) {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                String title = "Click to modify value set";
                String cssClass = "btn btn-link";
                String iconCss = "fa fa-pencil fa-lg";
                if (isEditable) {
                    sb.appendHtmlConstant("<button type=\"button\" title='"
                            + title + "' class=\" " + cssClass + "\" style=\"color: darkgoldenrod;\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Edit</button>");
                } else {
                    sb.appendHtmlConstant("<button type=\"button\" title='"
                            + title + "' class=\" " + cssClass + "\" disabled style=\"color: black;\"><i class=\" " + iconCss + "\"></i> <span style=\"font-size:0;\">Edit</span></button>");
                }

                return sb.toSafeHtml();
            }
        };

        return hasCell;
    }

    private HasCell<CQLQualityDataSetDTO, SafeHtml> getDeleteButtonCell() {

        HasCell<CQLQualityDataSetDTO, SafeHtml> hasCell = new HasCell<CQLQualityDataSetDTO, SafeHtml>() {

            ClickableSafeHtmlCell deleteButonCell = new ClickableSafeHtmlCell();

            @Override
            public Cell<SafeHtml> getCell() {
                return deleteButonCell;
            }

            @Override
            public FieldUpdater<CQLQualityDataSetDTO, SafeHtml> getFieldUpdater() {
                return (index, object, value) -> {
                    if (object != null) {
                        lastSelectedObject = object;
                        observer.onDeleteClicked(object, index);
                    }
                };
            }

            @Override
            public SafeHtml getValue(CQLQualityDataSetDTO object) {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                String title = "Click to delete value set";
                String cssClass = "btn btn-link";
                String iconCss = "fa fa-trash fa-lg";

                sb.appendHtmlConstant("<button type=\"button\" title='"
                        + title + "' class=\" " + cssClass + "\" style=\"margin-left: 0px;margin-right: 10px;\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Delete</button>");
                return sb.toSafeHtml();
            }


        };

        return hasCell;
    }

    private HasCell<CQLQualityDataSetDTO, Boolean> getCheckBoxCell() {
        HasCell<CQLQualityDataSetDTO, Boolean> hasCell = new HasCell<CQLQualityDataSetDTO, Boolean>() {

            private MatCheckBoxCell cell = new MatCheckBoxCell(false, true);

            @Override
            public Cell<Boolean> getCell() {
                return cell;
            }

            @Override
            public Boolean getValue(CQLQualityDataSetDTO object) {
                boolean isSelected = false;
                if (qdmSelectedList.size() > 0) {
                    for (int i = 0; i < qdmSelectedList.size(); i++) {
                        if (qdmSelectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
                            isSelected = true;
                            selectionModel.setSelected(object, isSelected);
                            break;
                        }
                    }
                } else {
                    isSelected = false;
                    selectionModel.setSelected(object, isSelected);
                }

                if (isSelected) {
                    cell.setTitle("Click to remove " + object.getName() + " from clipboard");
                } else {
                    cell.setTitle("Click to add " + object.getName() + " to clipboard");
                }

                return isSelected;
            }

            @Override
            public FieldUpdater<CQLQualityDataSetDTO, Boolean> getFieldUpdater() {
                return new FieldUpdater<CQLQualityDataSetDTO, Boolean>() {
                    @Override
                    public void update(int index, CQLQualityDataSetDTO object,
                                       Boolean isCBChecked) {

                        if (isCBChecked) {
                            qdmSelectedList.add(object);
                        } else {
                            for (int i = 0; i < qdmSelectedList.size(); i++) {
                                if (qdmSelectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
                                    qdmSelectedList.remove(i);
                                    break;
                                }
                            }
                        }

                        if (isCBChecked) {
                            cell.setTitle("Click to remove " + object.getName() + " from clipboard");
                        } else {
                            cell.setTitle("Click to add " + object.getName() + " to clipboard");
                        }

                        selectionModel.setSelected(object, isCBChecked);
                    }

                };
            }


        };
        return hasCell;
    }

    public Widget asWidget() {
        return containerPanel;
    }

    public void clearSelectedCheckBoxes() {
        if (table != null) {
            List<CQLQualityDataSetDTO> displayedItems = new ArrayList<>();
            displayedItems.addAll(qdmSelectedList);
            qdmSelectedList = new ArrayList<>();
            for (CQLQualityDataSetDTO dto : displayedItems) {
                selectionModel.setSelected(dto, false);
            }
            table.redraw();
        }
    }

    public void selectAll() {
        if (table != null) {
            for (CQLQualityDataSetDTO dto : allValueSetsList) {
                if (!qdmSelectedList.contains(dto)) {
                    qdmSelectedList.add(dto);
                }
                selectionModel.setSelected(dto, true);
            }
            table.redraw();
        }
    }

    public CheckBox getSpecificOccChkBox() {
        return specificOcurChkBox;
    }

    public void resetVSACValueSetWidget() {
        errorHandler.clearErrors();
        if (CQLAppliedValueSetUtility.checkForEnable()) {
            oidInput.setTitle(ENTER_OID);
            nameInput.setTitle(ENTER_NAME);
        }
        HTML searchHeaderText = new HTML("<strong>Search</strong>");
        searchHeader.clear();
        searchHeader.add(searchHeaderText);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }

    @Override
    public HandlerRegistration addSelectionHandler(
            SelectionHandler<Boolean> handler) {
        return handlerManager.addHandler(SelectionEvent.getType(), handler);
    }

    public Observer getObserver() {
        return observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }


    public Button getCancelQDMButton() {
        return cancelButton;
    }

    public org.gwtbootstrap3.client.ui.Button getRetrieveFromVSACButton() {
        return goButton;
    }

    public Button getSaveButton() {
        return saveValueSet;
    }

    public Button getUpdateFromVSACButton() {
        return updateVSACButton;
    }

    public CQLQualityDataSetDTO getSelectedElementToRemove() {
        return lastSelectedObject;
    }

    public ListBox getProgramListBox() {
        return programListBox;
    }

    public ListBox getReleaseListBox() {
        return releaseListBox;
    }

    public TextBox getOIDInput() {
        return oidInput;
    }

    public TextBox getUserDefinedInput() {
        return nameInput;
    }

    public CustomQuantityTextBox getSuffixInput() {
        return suffixInput;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public PanelHeader getSearchHeader() {
        return searchHeader;
    }

    public ListDataProvider<CQLQualityDataSetDTO> getListDataProvider() {
        return listDataProvider;
    }

    public MatSimplePager getSimplePager() {
        return spager;
    }


    public CellTable<CQLQualityDataSetDTO> getCelltable() {
        return table;
    }


    public MatSimplePager getPager() {
        return spager;
    }


    public VerticalPanel getMainPanel() {
        return mainPanel;
    }

    public void setWidgetsReadOnly(boolean editable) {
        log.log(Level.INFO, "setWidgetsReadOnly " + editable);
        getOIDInput().setEnabled(editable);
        getUserDefinedInput().setEnabled(editable);
        getCancelQDMButton().setEnabled(editable);
        getRetrieveFromVSACButton().setEnabled(editable);
        getUpdateFromVSACButton().setEnabled(editable);
        getSaveButton().setEnabled(false);
        getProgramListBox().setEnabled(editable);
        getSuffixInput().setEnabled(editable);
        log.log(Level.INFO, "leaving setWidgetsReadOnly");
    }

    public void setWidgetToDefault() {
        getOIDInput().setValue("");
        getUserDefinedInput().setValue("");
        getSaveButton().setEnabled(false);
    }


    public SimplePanel getCellTableMainPanel() {
        return cellTableMainPanel;
    }

    public void clearCellTableMainPanel() {
        cellTableMainPanel.clear();
    }


    public boolean validateUserDefinedInput() {

        boolean hasName = (getUserDefinedInput().getValue().length() > 0) ? true : false;

        if (hasName) {
            getOIDInput().setEnabled(false);
            getProgramListBox().setEnabled(false);
            getReleaseListBox().setEnabled(false);
            getRetrieveFromVSACButton().setEnabled(false);

            getSaveButton().setEnabled(true);

            getUserDefinedInput().setTitle(getUserDefinedInput().getValue());

        } else {
            getOIDInput().setEnabled(true);
            getProgramListBox().setEnabled(true);
            getRetrieveFromVSACButton().setEnabled(true);

            getSaveButton().setEnabled(false);

            getUserDefinedInput().setTitle(ENTER_NAME);
        }

        return hasName;
    }

    public boolean validateOIDInput() {

        boolean isUserDefined = false;

        if (getOIDInput().getValue().length() > 0) {
            getUserDefinedInput().setEnabled(false);
            getSaveButton().setEnabled(false);
            getRetrieveFromVSACButton().setEnabled(true);
            getOIDInput().setTitle(getOIDInput().getValue());

        } else if (getUserDefinedInput().getValue().length() > 0) {
            isUserDefined = true;
            getUserDefinedInput().setEnabled(true);
            getSaveButton().setEnabled(true);
            getOIDInput().setTitle(ENTER_OID);
        } else {
            getUserDefinedInput().setEnabled(true);
        }

        return isUserDefined;
    }

    public void resetCQLValuesetearchPanel() {
        log.log(Level.INFO, "resetCQLValuesetearchPanel");
        HTML searchHeaderText = new HTML("<strong>Search</strong>");
        getSearchHeader().clear();
        getSearchHeader().add(searchHeaderText);

        getOIDInput().setEnabled(true);
        getOIDInput().setValue("");
        getOIDInput().setTitle(ENTER_OID);

        getUserDefinedInput().setEnabled(true);
        getUserDefinedInput().setValue("");
        getUserDefinedInput().setTitle(ENTER_NAME);

        getSuffixInput().setEnabled(true);
        getSuffixInput().setValue("");
        getSuffixInput().setTitle("Suffix must be an integer between 1-4 characters");

        getSaveButton().setEnabled(false);


        getProgramListBox().setSelectedIndex(0); // go back to '--Select--'
        getProgramListBox().setEnabled(true);
        CQLAppliedValueSetUtility.initializeReleaseListBoxContent(getReleaseListBox());

        getUpdateFromVSACButton().setEnabled(true);
        log.log(Level.INFO, "leaving resetCQLValuesetearchPanel");
    }

    public void setHeading(String text, String linkName) {
        String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
        heading.setHTML(linkStr + "<h4><b>" + text + "</b></h4>");
    }

    public Boolean getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(Boolean isLoading) {
        this.isLoading = isLoading;
    }


    public Button getClearButton() {
        return copyPasteClearButtonToolBar.getClearButton();
    }

    public Button getCopyButton() {
        return copyPasteClearButtonToolBar.getCopyButton();
    }

    public Button getSelectAllButton() {
        return copyPasteClearButtonToolBar.getSelectAllButton();
    }

    public Button getPasteButton() {
        return copyPasteClearButtonToolBar.getPasteButton();
    }

    public void setIsEditable(boolean isEditable) {
        getCancelQDMButton().setEnabled(isEditable);
        getUpdateFromVSACButton().setEnabled(isEditable);
        getRetrieveFromVSACButton().setEnabled(isEditable);
        getProgramListBox().setEnabled(isEditable);
        this.setIsLoading(!isEditable);
    }

    public List<CQLQualityDataSetDTO> getQdmSelectedList() {
        return qdmSelectedList;
    }

    public void setQdmSelectedList(List<CQLQualityDataSetDTO> qdmSelectedList) {
        this.qdmSelectedList = qdmSelectedList;
    }


    /**
     * Method to generate List of {@link CQLValueSetTransferObject} when user pastes the list of selected {@link CQLQualityDataSetDTO}.
     *
     * @param copiedValueSetList
     * @return List of {@link CQLValueSetTransferObject}
     */
    public List<CQLValueSetTransferObject> setValueSetListForValueSets(List<CQLQualityDataSetDTO> copiedValueSetList, List<CQLQualityDataSetDTO> appliedValueSetTableList) {
        List<CQLValueSetTransferObject> cqlValueSetTransferObjectsList = new ArrayList<>();
        for (CQLQualityDataSetDTO cqlQualityDataSetDTO : copiedValueSetList) {
            boolean isFhir = MatContext.get().isCurrentModelTypeFhir();
            if (!checkNameInValueSetList(cqlQualityDataSetDTO.getName(), appliedValueSetTableList)) {
                CQLValueSetTransferObject cqlValueSetTransferObject = new CQLValueSetTransferObject();
                cqlValueSetTransferObject.setCqlQualityDataSetDTO(cqlQualityDataSetDTO);

                boolean isUrlValueSet = isFhirUrl(cqlQualityDataSetDTO.getOid());

                if (isFhir == isUrlValueSet) {
                    if (cqlQualityDataSetDTO.getOid().equals(ConstantMessages.USER_DEFINED_QDM_OID)) {
                        cqlValueSetTransferObject.setUserDefinedText(cqlQualityDataSetDTO.getOriginalCodeListName());
                    } else {
                        ValueSet ValueSet = new ValueSet();
                        List<MatConcept> matConcepts = new ArrayList<>();
                        MatConcept matConcept = new MatConcept();
                        ValueSet.setType(cqlQualityDataSetDTO.getValueSetType());
                        matConcept.setCodeSystemName(cqlQualityDataSetDTO.getTaxonomy());
                        matConcepts.add(matConcept);
                        MatConceptList matConceptList = new MatConceptList();
                        matConceptList.setConceptList(matConcepts);
                        ValueSet.setConceptList(matConceptList);

                        ValueSet.setID(cqlQualityDataSetDTO.getOid());
                        ValueSet.setDisplayName(cqlQualityDataSetDTO.getName());
                        cqlValueSetTransferObject.setValueSet(ValueSet);
                    }
                    CodeListSearchDTO codeListSearchDTO = new CodeListSearchDTO();
                    codeListSearchDTO.setName(cqlQualityDataSetDTO.getOriginalCodeListName());
                    cqlValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
                    cqlValueSetTransferObjectsList.add(cqlValueSetTransferObject);
                } else {
                    log.log(Level.INFO, "Not adding pasted valueset because you can only cnp fhir to fhir or qdm to qdm.");
                }
            }
        }

        return cqlValueSetTransferObjectsList;
    }

    public boolean checkNameInValueSetList(String userDefinedInput, List<CQLQualityDataSetDTO> appliedValueSetTableList) {
        if (appliedValueSetTableList.size() > 0) {
            Iterator<CQLQualityDataSetDTO> iterator = appliedValueSetTableList.iterator();
            while (iterator.hasNext()) {
                CQLQualityDataSetDTO dataSetDTO = iterator.next();
                if (!dataSetDTO.getOriginalCodeListName().isEmpty() && dataSetDTO.getOriginalCodeListName() != null && dataSetDTO.getName().equalsIgnoreCase(userDefinedInput)) {
                    return true;
                }
            }
        }
        return false;
    }

    public HelpBlock getHelpBlock() {
        return helpBlock;
    }

    public void setHelpBlock(HelpBlock helpBlock) {
        this.helpBlock = helpBlock;
    }

    public void setSelectedValueIndex(final ListBox listbox, final String value) {
        for (int i = 0; i < listbox.getItemCount(); i++) {
            if (listbox.getValue(i).equals(value)) {
                listbox.setSelectedIndex(i);
                return;
            }
        }
    }

    public List<CQLQualityDataSetDTO> getAllValueSets() {
        return allValueSetsList;
    }

    public InAppHelp getInAppHelp() {
        return inAppHelp;
    }

    public void setInAppHelp(InAppHelp inAppHelp) {
        this.inAppHelp = inAppHelp;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }
}