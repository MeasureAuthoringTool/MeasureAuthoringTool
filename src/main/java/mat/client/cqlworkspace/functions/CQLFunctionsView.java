package mat.client.cqlworkspace.functions;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import mat.client.CustomPager;
import mat.client.buttons.DefinitionFunctionButtonToolBar;
import mat.client.cqlworkspace.SharedCQLWorkspaceUtility;
import mat.client.cqlworkspace.shared.CQLEditorPanel;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.shared.CQLAddNewButton;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.client.util.MatTextBox;
import mat.client.validator.ErrorHandler;
import mat.model.cql.CQLFunctionArgument;
import mat.shared.ClickableSafeHtmlCell;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.client.ui.gwt.CellTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class CQLFunctionsView {

    private static final String FUNCTION = "function";

    public interface Observer {
        void onModifyClicked(CQLFunctionArgument result);

        void onDeleteClicked(CQLFunctionArgument result, int index);
    }

    private Observer observer;
    private final FocusPanel mainFunctionVerticalPanel = new FocusPanel();
    private final MatTextBox funcNameTxtArea = new MatTextBox();
    private final Button addNewArgumentButton = new Button();
    private final DefinitionFunctionButtonToolBar functionButtonBar = new DefinitionFunctionButtonToolBar(FUNCTION);
    private final CQLAddNewButton addNewButtonBar = new CQLAddNewButton(FUNCTION);
    private final List<CQLFunctionArgument> functionArgumentList = new ArrayList<>();
    private final VerticalPanel cellTablePanel = new VerticalPanel();
    private static final int TABLE_ROW_COUNT = 2;
    private final Map<String, CQLFunctionArgument> functionArgNameMap = new HashMap<>();
    private boolean isEditable = false;
    private final TextArea funcCommentTextArea = new TextArea();
    private FormGroup funcNameGroup = new FormGroup();
    private FormGroup funcCommentGroup = new FormGroup();
    private final FormGroup funcContextGroup = new FormGroup();
    private final TextBox returnTypeTextBox = new TextBox();
    private FormGroup returnTypeAndButtonPanelGroup = new FormGroup();
    private final HTML heading = new HTML();
    private final InAppHelp inAppHelp = new InAppHelp("");
    private CQLEditorPanel editorPanel = new CQLEditorPanel(FUNCTION, "CQL Expression Editor", false);
    private final CQLEditorPanel viewCQLEditorPanel = new CQLEditorPanel("functionViewCQL", "CQL Library Viewer", true);
    private final ErrorHandler errorHandler = new ErrorHandler();


    public CQLFunctionsView() {
        mainFunctionVerticalPanel.clear();
        heading.addStyleName("leftAligned");
    }

    private void buildView(boolean isEditable) {
        mainFunctionVerticalPanel.getElement().setId("mainFuncViewVerticalPanel");
        funcNameGroup.clear();
        funcCommentGroup.clear();
        funcContextGroup.clear();
        returnTypeAndButtonPanelGroup.clear();

        VerticalPanel funcVP = new VerticalPanel();
        HorizontalPanel funcFP = new HorizontalPanel();

        funcVP.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));
        funcVP.add(new SpacerWidget());
        funcVP.add(new SpacerWidget());

        this.editorPanel.getEditor().addDomHandler(event -> editorPanel.catchTabOutKeyCommand(event, functionButtonBar.getSaveButton()), KeyUpEvent.getType());
        editorPanel.setSize("650px", "200px");
        editorPanel.getPanelGroup().setMarginBottom(-10.0);
        editorPanel.getEditor().setText("");
        editorPanel.getEditor().clearAnnotations();

        viewCQLEditorPanel.setSize("655px", "200px");
        viewCQLEditorPanel.setCollabsable();

        funcNameGroup = buildFunctionNameFormGroup();
        buildAddNewArgumentButton();
        funcCommentGroup = buildFunctionCommentGroup();
        returnTypeAndButtonPanelGroup = buildReturnTypeAndButtonPanelGroup();

        setMarginInButtonBar();

        funcVP.add(addNewButtonBar);
        funcVP.add(funcNameGroup);
        funcVP.add(funcCommentGroup);
        funcVP.add(returnTypeAndButtonPanelGroup);
        funcVP.add(addNewArgumentButton);
        createAddArgumentViewForFunctions(functionArgumentList, isEditable);
        funcVP.add(cellTablePanel);

        HorizontalPanel buttonPanel = new HorizontalPanel();
        functionButtonBar.getTimingExpButton().setVisible(false);
        functionButtonBar.getCloseButton().setVisible(false);
        buttonPanel.add(functionButtonBar.getInfoButtonGroup());
        buttonPanel.add(functionButtonBar);
        funcVP.add(buttonPanel);

        funcVP.add(editorPanel);
        funcVP.add(functionButtonBar.getSaveButtonGroup());
        funcVP.add(new SpacerWidget());
        funcVP.add(new SpacerWidget());
        funcVP.add(viewCQLEditorPanel);
        funcVP.setStyleName("topping");
        funcFP.add(funcVP);
        funcFP.setStyleName("cqlRightContainer");

        mainFunctionVerticalPanel.setTitle("Function Section");
        mainFunctionVerticalPanel.setStyleName("cqlRightContainer");
        mainFunctionVerticalPanel.setWidth("725px");
        funcFP.setWidth("700px");
        funcFP.setStyleName("marginLeft15px");

        mainFunctionVerticalPanel.clear();
        mainFunctionVerticalPanel.add(funcFP);
    }

    private void buildAddNewArgumentButton() {
        addNewArgumentButton.setType(ButtonType.LINK);
        addNewArgumentButton.getElement().setId("addArgument_Button");

        addNewArgumentButton.setTitle("Click this button to add a new function argument");
        addNewArgumentButton.setText("Add Argument");
        addNewArgumentButton.setId("Add_Argument_ID");
        addNewArgumentButton.setIcon(IconType.PLUS);
        addNewArgumentButton.setSize(ButtonSize.SMALL);
        addNewArgumentButton.setMarginLeft(600.00);
        addNewArgumentButton.setMarginBottom(-10.00);
    }

    private FormGroup buildReturnTypeAndButtonPanelGroup() {
        FormGroup formGroup = new FormGroup();
        FormLabel returnTypeLabel = new FormLabel();
        returnTypeLabel.setText("Return Type");
        returnTypeLabel.setTitle("Return Type");
        returnTypeLabel.setMarginRight(41);
        returnTypeLabel.setId("returnType_Label");
        returnTypeLabel.setFor("returnTypeTextArea_Id");

        returnTypeTextBox.setId("returnTypeTextArea_Id");
        returnTypeTextBox.setTitle("Return Type of CQL Expression");
        returnTypeTextBox.setReadOnly(true);
        returnTypeTextBox.setEnabled(false);
        returnTypeTextBox.setTabIndex(-1);
        returnTypeTextBox.setSize("550px", "32px");

        HorizontalPanel returnTypeHP = new HorizontalPanel();
        returnTypeHP.add(returnTypeLabel);
        returnTypeHP.add(returnTypeTextBox);

        formGroup.add(returnTypeHP);
        return formGroup;
    }

    private FormGroup buildFunctionCommentGroup() {
        FormGroup commentGroup = new FormGroup();
        FormLabel funcCommentLabel = new FormLabel();
        funcCommentLabel.setText("Comment");
        funcCommentLabel.setTitle("Comment");
        funcCommentLabel.setMarginRight(50);
        funcCommentLabel.setId("FunctionComment_Label");
        funcCommentLabel.setFor("FunctionCommentTextArea_Id");

        funcCommentTextArea.setId("FunctionCommentTextArea_Id");
        funcCommentTextArea.setSize("550px", "32px");
        funcCommentTextArea.setText("");
        funcCommentTextArea.setName("Function Comment");
        funcCommentTextArea.setTitle("Enter Comment");

        HorizontalPanel funcCommentHPanel = new HorizontalPanel();
        funcCommentHPanel.add(funcCommentLabel);
        funcCommentHPanel.add(funcCommentTextArea);
        funcCommentHPanel.setWidth("700px");

        commentGroup.add(funcCommentHPanel);
        return commentGroup;
    }


    private FormGroup buildFunctionNameFormGroup() {
        FormGroup nameGroup = new FormGroup();
        FormLabel functionNameLabel = new FormLabel();
        functionNameLabel.setText("Function Name");
        functionNameLabel.setTitle("Function Name");
        functionNameLabel.setMarginRight(16);
        functionNameLabel.setId("FunctionName_Label");
        functionNameLabel.setFor("FunctionNameField");

        funcNameTxtArea.setText("");
        funcNameTxtArea.setSize("550px", "32px");
        funcNameTxtArea.getElement().setId("FunctionNameField");
        funcNameTxtArea.setName("FunctionName");
        funcNameTxtArea.setTitle("Enter Function Name Required");
        funcNameTxtArea.addBlurHandler(errorHandler.buildRequiredBlurHandler(funcNameTxtArea));

        HorizontalPanel funcNameHPanel = new HorizontalPanel();
        funcNameHPanel.add(functionNameLabel);
        funcNameHPanel.add(funcNameTxtArea);
        funcNameHPanel.setWidth("700px");

        nameGroup.add(funcNameHPanel);
        return nameGroup;
    }


    public void setMarginInButtonBar() {

        functionButtonBar.getElement().setAttribute("style", "margin-top:-10px;margin-left:518px;");
        functionButtonBar.getEraseButton().setMarginRight(5.00);
        functionButtonBar.getInsertButton().setMarginRight(-5.00);
        functionButtonBar.getSaveButton().setMarginLeft(480.00);
    }

    public FocusPanel getView(boolean isEditable) {
        mainFunctionVerticalPanel.clear();
        resetAll();
        buildView(isEditable);
        return mainFunctionVerticalPanel;
    }

    public void resetAll() {
        editorPanel = new CQLEditorPanel(FUNCTION, "CQL Expression Editor", false);
        getFuncNameTxtArea().setText("");
        getFunctionBodyAceEditor().setText("");
        getReturnTypeTextBox().setText("");
        getViewCQLAceEditor().setText("");
        viewCQLEditorPanel.setPanelCollapsed(true);
    }

    public PanelCollapse getPanelViewCQLCollapse() {
        return viewCQLEditorPanel.getPanelCollapse();
    }

    public AceEditor getViewCQLAceEditor() {
        return viewCQLEditorPanel.getEditor();
    }

    public void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList, boolean isEditable) {
        cellTablePanel.clear();
        cellTablePanel.setStyleName("cellTablePanel");
        this.isEditable = isEditable;

        if ((argumentList != null) && (argumentList.size() > 0)) {
            updateFunctionArgumentNameMap(argumentList);
            CellTable<CQLFunctionArgument> argumentListTable = new CellTable<>();
            argumentListTable.setStriped(true);
            argumentListTable.setCondensed(true);
            argumentListTable.setBordered(true);
            argumentListTable.setHover(true);

            argumentListTable.setPageSize(TABLE_ROW_COUNT);
            argumentListTable.redraw();
            ListDataProvider<CQLFunctionArgument> listDataProvider = new ListDataProvider<>();
            listDataProvider.refresh();
            listDataProvider.getList().addAll(argumentList);
            ListHandler<CQLFunctionArgument> sortHandler = new ListHandler<>(
                    listDataProvider.getList());
            argumentListTable.addColumnSortHandler(sortHandler);
            addColumnToTable(argumentListTable);
            listDataProvider.addDataDisplay(argumentListTable);
            CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
            MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources,
                    false, 0,
                    true, "cqlFunctionArg");
            spager.setDisplay(argumentListTable);
            spager.setPageStart(0);
            cellTablePanel.add(argumentListTable);
            cellTablePanel.add(spager);
        } else {
            Label tableHeader = new Label ("Added Arguments List");
            tableHeader.getElement().setId("tableHeader_Label");
            tableHeader.setStyleName("CqlWorkSpaceTableHeader");
            tableHeader.getElement().setAttribute("tabIndex", "-1");
            HTML desc = new HTML("<p> No Arguments Added.</p>");
            cellTablePanel.add(tableHeader);
            cellTablePanel.add(new SpacerWidget());
            cellTablePanel.add(desc);
        }

    }

    private void updateFunctionArgumentNameMap(List<CQLFunctionArgument> argumentList) {
        functionArgNameMap.clear();
        if (argumentList != null) {
            for (CQLFunctionArgument argument : argumentList) {
                functionArgNameMap.put(argument.getArgumentName().toLowerCase(), argument);
            }
        }
    }

    private void addColumnToTable(CellTable<CQLFunctionArgument> argumentListTable) {

        if (argumentListTable.getColumnCount() != TABLE_ROW_COUNT) {

            Label searchHeader = new Label("Added Arguments List");
            searchHeader.getElement().setId("searchHeader_Label");
            searchHeader.setStyleName("measureGroupingTableHeader");
            searchHeader.getElement().setAttribute("tabIndex", "-1");

            TableElement elem = argumentListTable.getElement().cast();
            TableCaptionElement caption = elem.createCaption();
            caption.appendChild(searchHeader.getElement());

            MultiSelectionModel<CQLFunctionArgument> selectionModel = new MultiSelectionModel<>();
            argumentListTable.setSelectionModel(selectionModel);
            Column<CQLFunctionArgument, SafeHtml> nameColumn = new Column<CQLFunctionArgument, SafeHtml>(
                    new SafeHtmlCell()) {

                @Override
                public SafeHtml getValue(CQLFunctionArgument object) {
                    StringBuilder title = new StringBuilder();
                    StringBuilder value = new StringBuilder(object.getArgumentName());

                    title.append("Name : ").append(value);

                    return getDataTypeColumnToolTip(value.toString(), title, object.isValid());
                }

            };
            argumentListTable.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Name\">" + "Name" + "</span>"));

            Column<CQLFunctionArgument, SafeHtml> dataTypeColumn = new Column<CQLFunctionArgument, SafeHtml>(
                    new SafeHtmlCell()) {
                @Override
                public SafeHtml getValue(CQLFunctionArgument object) {
                    StringBuilder title = new StringBuilder();
                    StringBuilder value = new StringBuilder(object.getArgumentType());
                    if (value.toString().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_QDM_DATA_TYPE) ||
                            value.toString().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_FHIR_DATA_TYPE)) {
                        value.append(":").append(object.getQdmDataType());
                        if (object.getAttributeName() != null) {
                            value.append(".").append(object.getAttributeName());
                        }
                    } else if (value.toString().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_OTHER_DATA_TYPE)) {
                        value.append(":").append(object.getOtherType());
                    }
                    title.append("Datatype : ").append(value);
                    return CellTableUtility.getColumnToolTip(value.toString(), title.toString());
                }
            };
            argumentListTable.addColumn(dataTypeColumn,
                    SafeHtmlUtils.fromSafeConstant("<span title=\"Datatype\">" + "Datatype" + "</span>"));

            String colName = "Modify";

            argumentListTable.addColumn(
                    new Column<CQLFunctionArgument, CQLFunctionArgument>(getCompositeCellForFuncArguModifyAndDelete()) {
                        @Override
                        public CQLFunctionArgument getValue(CQLFunctionArgument object) {
                            return object;
                        }
                    }, SafeHtmlUtils.fromSafeConstant("<span title='" + colName + "'>  " + colName + "</span>"));

            argumentListTable.setColumnWidth(0, 25.0, Unit.PCT);
            argumentListTable.setColumnWidth(1, 35.0, Unit.PCT);
            argumentListTable.setColumnWidth(2, 10.0, Unit.PCT);
        }
    }

    private CompositeCell<CQLFunctionArgument> getCompositeCellForFuncArguModifyAndDelete() {
        final List<HasCell<CQLFunctionArgument, ?>> cells = new LinkedList<>();
        if (isEditable) {
            cells.add(getModifyQDMButtonCell());
            cells.add(getDeleteQDMButtonCell());
        }

        return new CompositeCell<CQLFunctionArgument>(cells) {
            @Override
            public void render(Context context, CQLFunctionArgument object, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<table tabindex=\"-1\"><tbody><tr tabindex=\"-1\">");
                for (HasCell<CQLFunctionArgument, ?> hasCell : cells) {
                    render(context, object, sb, hasCell);
                }
                sb.appendHtmlConstant("</tr></tbody></table>");
            }

            @Override
            protected <X> void render(Context context, CQLFunctionArgument object, SafeHtmlBuilder sb,
                                      HasCell<CQLFunctionArgument, X> hasCell) {
                Cell<X> cell1 = hasCell.getCell();
                sb.appendHtmlConstant("<td class='emptySpaces' tabindex=\"-1\">");
                if ((object != null)) {
                    cell1.render(context, hasCell.getValue(object), sb);
                } else {
                    sb.appendHtmlConstant("<span tabindex=\"-1\"></span>");
                }
                sb.appendHtmlConstant("</td>");
            }

            @Override
            protected Element getContainerElement(Element parent) {
                return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
            }
        };
    }

    private SafeHtml getDataTypeColumnToolTip(String columnText, StringBuilder title, boolean hasImage) {
        String htmlConstant;
        if (hasImage) {
            htmlConstant = "<html>"
                    + "<head> </head> <body><img src =\"images/error.png\" alt=\"Arugment Name is InValid.\""
                    + "title = \"Arugment Name is InValid.\"/>" + "<span tabIndex = \"-1\" title='" + title + "'>"
                    + columnText + "</span></body>" + "</html>";
        } else {
            htmlConstant = "<html>" + "<head> </head> <body><span tabIndex = \"-1\" title='" + title + "'>"
                    + columnText + "</span></body>" + "</html>";
        }
        return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
    }

    private HasCell<CQLFunctionArgument, SafeHtml> getModifyQDMButtonCell() {

        return new HasCell<CQLFunctionArgument, SafeHtml>() {

            final ClickableSafeHtmlCell modifyButonCell = new ClickableSafeHtmlCell();

            @Override
            public Cell<SafeHtml> getCell() {
                return modifyButonCell;
            }

            @Override
            public FieldUpdater<CQLFunctionArgument, SafeHtml> getFieldUpdater() {

                return (index, object, value) -> {
                    if ((object != null)) {
                        observer.onModifyClicked(object);
                    }
                };
            }

            @Override
            public SafeHtml getValue(CQLFunctionArgument object) {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                String title = "Click to Modify Argument";
                String cssClass = "btn btn-link";
                String iconCss = "fa fa-pencil fa-lg";
                if (isEditable) {
                    sb.appendHtmlConstant("<button type=\"button\" title='"
                            + title + "' class=\" " + cssClass + "\" style=\"color: darkgoldenrod;\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Edit</span></button>");
                } else {
                    sb.appendHtmlConstant("<button type=\"button\" title='"
                            + title + "' class=\" " + cssClass + "\" disabled style=\"color: black;\"><i class=\" " + iconCss + "\"></i> <span style=\"font-size:0;\">Edit</span></button>");
                }
                return sb.toSafeHtml();
            }
        };
    }

    private HasCell<CQLFunctionArgument, SafeHtml> getDeleteQDMButtonCell() {

        return new HasCell<CQLFunctionArgument, SafeHtml>() {

            final ClickableSafeHtmlCell deleteButtonCell = new ClickableSafeHtmlCell();

            @Override
            public Cell<SafeHtml> getCell() {
                return deleteButtonCell;
            }

            @Override
            public FieldUpdater<CQLFunctionArgument, SafeHtml> getFieldUpdater() {

                return (index, object, value) -> observer.onDeleteClicked(object, index);
            }

            @Override
            public SafeHtml getValue(CQLFunctionArgument object) {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                String title = "Click to Delete Argument";
                String cssClass = "btn btn-link";
                String iconCss = "fa fa-trash fa-lg";

                if (isEditable) {
                    sb.appendHtmlConstant("<button type=\"button\" title='"
                            + title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"margin-left: 0px;\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Delete</span></button>");
                } else {
                    sb.appendHtmlConstant("<button type=\"button\" title='"
                            + title + "' tabindex=\"0\" class=\" " + cssClass + "\" disabled style=\"margin-left: 0px;\"><i class=\" " + iconCss + "\"></i> <span style=\"font-size:0;\">Delete</span></button>");
                }

                return sb.toSafeHtml();
            }
        };
    }

    public void setWidgetReadOnly(boolean isEditable) {
        getFuncNameTxtArea().setEnabled(isEditable);
        getFunctionCommentTextArea().setEnabled(isEditable);
        editorPanel.setIsReadOnly(!isEditable);
        getFunctionButtonBar().setEnabled(isEditable);
        getAddNewArgument().setEnabled(isEditable);
        getFunctionButtonBar().getDeleteButton().setTitle("Delete");
    }

    public void setHeading(String text, String linkName) {
        String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
        heading.setHTML(linkStr + "<h4><b>" + text + "</b></h4>");
    }

    public void setIsEditable(boolean isEditable) {
        getAddNewButtonBar().getaddNewButton().setEnabled(isEditable);
        getAddNewArgument().setEnabled(isEditable);
        getFunctionButtonBar().getSaveButton().setEnabled(isEditable);
        getFunctionButtonBar().getEraseButton().setEnabled(isEditable);
        getFunctionButtonBar().getDeleteButton().setEnabled(isEditable);
        getFunctionButtonBar().getInsertButton().setEnabled(isEditable);
        getFunctionButtonBar().getInfoButton().setEnabled(isEditable);
    }

    public void resetFormGroup() {
        getFuncCommentGroup().setValidationState(ValidationState.NONE);
    }

    public void resetFuncFormGroup() {
        getFuncNameGroup().setValidationState(ValidationState.NONE);
        getFuncCommentGroup().setValidationState(ValidationState.NONE);
    }

    public Observer getObserver() {
        return observer;
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public FocusPanel getMainFunctionVerticalPanel() {
        return mainFunctionVerticalPanel;
    }

    public MatTextBox getFuncNameTxtArea() {
        return funcNameTxtArea;
    }

    public AceEditor getFunctionBodyAceEditor() {
        return this.editorPanel.getEditor();
    }

    public Button getAddNewArgument() {
        return addNewArgumentButton;
    }

    public DefinitionFunctionButtonToolBar getFunctionButtonBar() {
        return functionButtonBar;
    }

    public List<CQLFunctionArgument> getFunctionArgumentList() {
        return functionArgumentList;
    }

    public Map<String, CQLFunctionArgument> getFunctionArgNameMap() {
        return functionArgNameMap;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void hideAceEditorAutoCompletePopUp() {
        getFunctionBodyAceEditor().detach();
    }

    public CQLAddNewButton getAddNewButtonBar() {
        return addNewButtonBar;
    }

    public TextArea getFunctionCommentTextArea() {
        return funcCommentTextArea;
    }

    public FormGroup getFuncNameGroup() {
        return funcNameGroup;
    }

    public FormGroup getFuncCommentGroup() {
        return funcCommentGroup;
    }

    public TextBox getReturnTypeTextBox() {
        return returnTypeTextBox;
    }

    public InAppHelp getInAppHelp() {
        return inAppHelp;
    }
}
