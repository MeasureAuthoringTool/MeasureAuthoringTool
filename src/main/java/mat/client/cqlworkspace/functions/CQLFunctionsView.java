/**
 * 
 */
package mat.client.cqlworkspace.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.TextArea;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.client.ui.gwt.CellTable;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import edu.ycp.cs.dh.acegwt.client.ace.AceCommand;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.client.CustomPager;
import mat.client.buttons.DefinitionFunctionButtonToolBar;
import mat.client.cqlworkspace.SharedCQLWorkspaceUtility;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.shared.CQLAddNewButton;
import mat.client.shared.CQLCollapsibleCQLPanelWidget;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.client.util.MatTextBox;
import mat.model.cql.CQLFunctionArgument;
import mat.shared.ClickableSafeHtmlCell;


public class CQLFunctionsView {
	
	public static interface Observer {
		
		/**
		 * On modify clicked.
		 * 
		 * @param result
		 *            the result
		 */
		void onModifyClicked(CQLFunctionArgument result);
		
		/**
		 * On delete clicked.
		 *
		 * @param result
		 *            the result
		 * @param index
		 *            the index
		 */
		void onDeleteClicked(CQLFunctionArgument result, int index);
	}
	
	private Observer observer;
	
	private FocusPanel mainFunctionVerticalPanel = new FocusPanel();
	
	private MatTextBox funcNameTxtArea = new MatTextBox();
	private AceEditor functionBodyAceEditor = new AceEditor();
	
	private Button addNewArgument = new Button();

	private DefinitionFunctionButtonToolBar functionButtonBar = new DefinitionFunctionButtonToolBar("function");
	
	private ButtonGroup contextGroup = new ButtonGroup();
	
	private InlineRadio contextFuncPATRadioBtn = new InlineRadio("Patient");
	
	private InlineRadio contextFuncPOPRadioBtn = new InlineRadio("Population");
	
	private CQLAddNewButton addNewButtonBar = new CQLAddNewButton("function");
	
	private List<CQLFunctionArgument> functionArgumentList = new ArrayList<>();
	
	
	private VerticalPanel cellTablePanel = new VerticalPanel();
	
	private static final int TABLE_ROW_COUNT = 2;
	
	private CellTable<CQLFunctionArgument> argumentListTable;
	
	private ListDataProvider<CQLFunctionArgument> listDataProvider;
	
	private MatSimplePager spager;
	
	private Map<String, CQLFunctionArgument> functionArgNameMap = new HashMap<>();
	
	boolean isEditable = false;
	
	private CQLCollapsibleCQLPanelWidget collapsibleCQLPanelWidget = new CQLCollapsibleCQLPanelWidget();
	
	private TextArea funcCommentTextArea = new TextArea();
	
	private FormGroup funcNameGroup = new FormGroup();
	
	private FormGroup funcCommentGroup = new FormGroup();
	
	private FormGroup funcContextGroup = new FormGroup();
	
	private TextBox returnTypeTextBox = new TextBox();
	private FormGroup returnTypeAndButtonPanelGroup = new FormGroup();

	HTML heading = new HTML();
	
	private InAppHelp inAppHelp = new InAppHelp("");
	
	public CQLFunctionsView() {
		mainFunctionVerticalPanel.clear();
		functionBodyAceEditor.startEditor();
		heading.addStyleName("leftAligned");
		collapsibleCQLPanelWidget.getViewCQLAceEditor().startEditor();
		collapsibleCQLPanelWidget.getViewCQLAnchor().setDataToggle(Toggle.COLLAPSE);
		collapsibleCQLPanelWidget.getViewCQLAnchor().setDataParent("#panelGroup");
		collapsibleCQLPanelWidget.getViewCQLAnchor().setHref("#panelCollapse");
		collapsibleCQLPanelWidget.getViewCQLAnchor().setText("Click to View CQL");
		collapsibleCQLPanelWidget.getViewCQLAnchor().setColor("White");
	}

	@SuppressWarnings("static-access")
	private void buildView(boolean isEditable) {
		
		mainFunctionVerticalPanel.getElement().setId("mainFuncViewVerticalPanel");
		
		
		collapsibleCQLPanelWidget.getPanelViewCQLCollapse().clear();
		funcNameGroup.clear();
		funcCommentGroup.clear();
		funcContextGroup.clear();
		returnTypeAndButtonPanelGroup.clear();
		VerticalPanel funcVP = new VerticalPanel();
		HorizontalPanel funcFP = new HorizontalPanel();
		
		funcVP.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));
		funcVP.add(new SpacerWidget());
		funcVP.add(new SpacerWidget());
		
		FormLabel functionNameLabel = new FormLabel();
		functionNameLabel.setText("Function Name");
		functionNameLabel.setTitle("Function Name");
		functionNameLabel.setMarginRight(15);
		functionNameLabel.setId("FunctionName_Label");
		functionNameLabel.setFor("FunctionNameField");
		
		funcNameTxtArea.setText("");
		funcNameTxtArea.setSize("550px", "32px");
		funcNameTxtArea.getElement().setId("FunctionNameField");
		funcNameTxtArea.setName("FunctionName");
		funcNameTxtArea.setTitle("Enter Function Name Required");
		
		HorizontalPanel funcNameHPanel = new HorizontalPanel();
		funcNameHPanel.add(functionNameLabel);
		funcNameHPanel.add(funcNameTxtArea);
		funcNameHPanel.setWidth("700px");
		
		funcNameGroup.add(funcNameHPanel);
		
		Panel aceEditorPanel = new Panel(PanelType.PRIMARY);
		PanelHeader header = new PanelHeader();
		header.setText("Build CQL Expression");
		PanelBody body = new PanelBody();
		
		SimplePanel funcAceEditorPanel = new SimplePanel();
		funcAceEditorPanel.setSize("650", "200");
		functionBodyAceEditor.setText("");
		functionBodyAceEditor.setMode(AceEditorMode.CQL);
		functionBodyAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		functionBodyAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		functionBodyAceEditor.setSize("650px", "200px");
		functionBodyAceEditor.setAutocompleteEnabled(true);
		functionBodyAceEditor.addAutoCompletions();
		functionBodyAceEditor.setUseWrapMode(true);
		functionBodyAceEditor.clearAnnotations();
		functionBodyAceEditor.removeAllMarkers();
		functionBodyAceEditor.getElement().setAttribute("id", "Func_AceEditorID");
		functionBodyAceEditor.getElement().getElementsByTagName("textarea").getItem(0).setTitle("Build CQL Expression");
		
		// MAT-8735 Disable tab and shift-tab
		functionBodyAceEditor.removeCommand(AceCommand.INDENT);
		functionBodyAceEditor.removeCommand(AceCommand.OUTDENT);

		funcAceEditorPanel.add(functionBodyAceEditor);
		funcAceEditorPanel.getElement().setAttribute("id", "SimplePanel_Function_AceEditor");
		body.add(funcAceEditorPanel);
		aceEditorPanel.add(header);
		aceEditorPanel.add(body);
		aceEditorPanel.setMarginBottom(-10.00);
		
		addNewArgument.setType(ButtonType.LINK);
		addNewArgument.getElement().setId("addArgument_Button");

		addNewArgument.setTitle("Click this button to add a new function argument");
		addNewArgument.setText("Add Argument");
		addNewArgument.setId("Add_Argument_ID");
		addNewArgument.setIcon(IconType.PLUS);
		addNewArgument.setSize(ButtonSize.SMALL);
		
		FormLabel funcContextLabel = new FormLabel();
		funcContextLabel.setText("Context");
		funcContextLabel.setTitle("Context");
		funcContextLabel.setId("FunctionContext_Label");

		contextFuncPATRadioBtn.setValue(true);
		contextFuncPATRadioBtn.setText("Patient");
		contextFuncPATRadioBtn.setId("context_PatientRadioButton");
		contextFuncPOPRadioBtn.setValue(false);
		contextFuncPOPRadioBtn.setText("Population");
		contextFuncPOPRadioBtn.setId("context_PopulationRadioButton");
		functionButtonBar.getTimingExpButton().setVisible(false);
		functionButtonBar.getCloseButton().setVisible(false);
		contextGroup.add(contextFuncPATRadioBtn);
		contextGroup.add(contextFuncPOPRadioBtn);
		contextGroup.setStyleName("contextToggleSwitch");
		
		HorizontalPanel funcContextHPanel = new HorizontalPanel();
		funcContextHPanel.add(funcContextLabel);
		funcContextHPanel.add(contextGroup);
		funcContextHPanel.setWidth("500px");
		
		funcContextGroup.add(funcContextHPanel);
		
		FormLabel funcCommentLabel = new FormLabel();
		funcCommentLabel.setText("Comment");
		funcCommentLabel.setTitle("Comment");
		funcCommentLabel.setMarginRight(50);
		funcCommentLabel.setId("FunctionComment_Label");
		funcCommentLabel.setFor("FunctionCommentTextArea_Id");
		
		funcCommentTextArea.setId("FunctionCommentTextArea_Id");
		funcCommentTextArea.setSize("550px", "40px");
		funcCommentTextArea.setText("");
		funcCommentTextArea.setName("Function Comment");
		funcCommentTextArea.setTitle("Enter Comment");
		
		HorizontalPanel funcCommentHPanel = new HorizontalPanel();
		funcCommentHPanel.add(funcCommentLabel);
		funcCommentHPanel.add(funcCommentTextArea);
		funcCommentHPanel.setWidth("700px");
		
		funcCommentGroup.add(funcCommentHPanel);
		
		FormLabel returnTypeLabel = new FormLabel();
		returnTypeLabel.setText("Return Type");
		returnTypeLabel.setTitle("Return Type");
		returnTypeLabel.setMarginRight(42);
		returnTypeLabel.setId("returnType_Label");
		returnTypeLabel.setFor("returnTypeTextArea_Id");

		returnTypeTextBox.setId("returnTypeTextArea_Id");
		returnTypeTextBox.setTitle("Return Type of CQL Expression");
		returnTypeTextBox.setReadOnly(true);
		returnTypeTextBox.setWidth("550px");
		
		
		addNewArgument.setMarginLeft(580.00);
		addNewArgument.setMarginBottom(-10.00);
		HorizontalPanel returnTypeHP = new HorizontalPanel();
		returnTypeHP.add(returnTypeLabel);
		returnTypeHP.add(returnTypeTextBox);

		returnTypeAndButtonPanelGroup.add(returnTypeHP);
		setMarginInButtonBar();
		
		funcVP.add(addNewButtonBar);
		funcVP.add(funcNameGroup);
		funcVP.add(funcContextGroup);
		funcVP.add(funcCommentGroup);
		funcVP.add(returnTypeAndButtonPanelGroup);
		funcVP.add(addNewArgument);
		createAddArgumentViewForFunctions(functionArgumentList,isEditable);
		funcVP.add(cellTablePanel);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.add(functionButtonBar.getInfoButtonGroup());
		buttonPanel.add(functionButtonBar);
		funcVP.add(buttonPanel);
		
		funcVP.add(aceEditorPanel);
		funcVP.add(functionButtonBar.getSaveButtonGroup());
		funcVP.add(new SpacerWidget());
		funcVP.add(collapsibleCQLPanelWidget.buildViewCQLCollapsiblePanel());
		funcVP.add(new SpacerWidget());
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


	public void setMarginInButtonBar() {
		
		functionButtonBar.getElement().setAttribute("style", "margin-top:-10px;margin-left:518px;");
		functionButtonBar.getEraseButton().setMarginRight(5.00);
		functionButtonBar.getInsertButton().setMarginRight(-5.00);
		functionButtonBar.getSaveButton().setMarginLeft(480.00);
	}
	
	
	
	/**
	 * Gets the view.
	 *
	 * @param isEditable the is editable
	 * @return the view
	 */
	public FocusPanel getView(boolean isEditable) {
		mainFunctionVerticalPanel.clear();
		resetAll();
		buildView(isEditable);
		return mainFunctionVerticalPanel;
	}
	
	/**
	 * Reset all.
	 */
	public void resetAll() {
		getFuncNameTxtArea().setText("");
		getFunctionBodyAceEditor().setText("");
		getReturnTypeTextBox().setText("");
		getViewCQLAceEditor().setText("");
		collapsibleCQLPanelWidget.getPanelViewCQLCollapse().getElement().setClassName("panel-collapse collapse");
	}
	
	/**
	 * Gets the panel view CQL collapse.
	 *
	 * @return the panel view CQL collapse
	 */
	public PanelCollapse getPanelViewCQLCollapse() {
		return collapsibleCQLPanelWidget.getPanelViewCQLCollapse();
	}

	/**
	 * Gets the view CQL ace editor.
	 *
	 * @return the view CQL ace editor
	 */
	public AceEditor getViewCQLAceEditor() {
		return collapsibleCQLPanelWidget.getViewCQLAceEditor();
	}
	
	/**
	 * Creates the add argument view for functions.
	 *
	 * @param argumentList the argument list
	 * @param isEditable the is editable
	 */
	public void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList, boolean isEditable) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		this.isEditable = isEditable;
		
		if ((argumentList != null) && (argumentList.size() > 0)) {
			updateFunctionArgumentNameMap(argumentList);
			argumentListTable = new CellTable<>();
			argumentListTable.setStriped(true);
			argumentListTable.setCondensed(true);
			argumentListTable.setBordered(true);
			argumentListTable.setHover(true);
			
			argumentListTable.setPageSize(TABLE_ROW_COUNT);
			argumentListTable.redraw();
			listDataProvider = new ListDataProvider<>();
			listDataProvider.refresh();
			listDataProvider.getList().addAll(argumentList);
			ListHandler<CQLFunctionArgument> sortHandler = new ListHandler<>(
					listDataProvider.getList());
			argumentListTable.addColumnSortHandler(sortHandler);
			argumentListTable = addColumnToTable(argumentListTable, sortHandler);
			listDataProvider.addDataDisplay(argumentListTable);
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"cqlFunctionArg");
			spager.setDisplay(argumentListTable);
			spager.setPageStart(0);
			cellTablePanel.add(argumentListTable);
			cellTablePanel.add(spager);
		} else {
			com.google.gwt.user.client.ui.Label tableHeader = new com.google.gwt.user.client.ui.Label(
					"Added Arguments List");
			tableHeader.getElement().setId("tableHeader_Label");
			tableHeader.setStyleName("CqlWorkSpaceTableHeader");
			tableHeader.getElement().setAttribute("tabIndex", "0");
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
	

	private CellTable<CQLFunctionArgument> addColumnToTable(CellTable<CQLFunctionArgument> table,
			ListHandler<CQLFunctionArgument> sortHandler) {
		if (table.getColumnCount() != TABLE_ROW_COUNT) {
			com.google.gwt.user.client.ui.Label searchHeader = new com.google.gwt.user.client.ui.Label(
					"Added Arguments List");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.setStyleName("measureGroupingTableHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(searchHeader.getElement());
			
			MultiSelectionModel<CQLFunctionArgument> selectionModel = new MultiSelectionModel<>();
			table.setSelectionModel(selectionModel);
			Column<CQLFunctionArgument, SafeHtml> nameColumn = new Column<CQLFunctionArgument, SafeHtml>(
					new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CQLFunctionArgument object) {
					StringBuilder title = new StringBuilder();
					StringBuilder value = new StringBuilder(object.getArgumentName());
					
					title = title.append("Name : ").append(value);

					return getDataTypeColumnToolTip(value.toString(), title, object.isValid());
				}
				
			};
			table.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Name\">" + "Name" + "</span>"));
			
			Column<CQLFunctionArgument, SafeHtml> dataTypeColumn = new Column<CQLFunctionArgument, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLFunctionArgument object) {
					StringBuilder title = new StringBuilder();
					StringBuilder value = new StringBuilder(object.getArgumentType());
					if (value.toString().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MODEL_DATA_TYPE)) {
						value = value.append(":").append(object.getQdmDataType());
						if (object.getAttributeName() != null) {
							value = value.append(".").append(object.getAttributeName());
						}
					} else if (value.toString().equalsIgnoreCase(CQLWorkSpaceConstants.CQL_OTHER_DATA_TYPE)) {
						value = value.append(":").append(object.getOtherType());
					}
					title = title.append("Datatype : ").append(value);
					return CellTableUtility.getColumnToolTip(value.toString(), title.toString());
				}
			};
			table.addColumn(dataTypeColumn,
					SafeHtmlUtils.fromSafeConstant("<span title=\"Datatype\">" + "Datatype" + "</span>"));
			
			String colName = "Modify";
			
			table.addColumn(
					new Column<CQLFunctionArgument, CQLFunctionArgument>(getCompositeCellForFuncArguModifyAndDelete()) {
						
						@Override
						public CQLFunctionArgument getValue(CQLFunctionArgument object) {
							return object;
						}
					}, SafeHtmlUtils.fromSafeConstant("<span title='" + colName + "'>  " + colName + "</span>"));
			
			table.setColumnWidth(0, 25.0, Unit.PCT);
			table.setColumnWidth(1, 35.0, Unit.PCT);
			table.setColumnWidth(2, 10.0, Unit.PCT);
		}
		return table;
	}
	

	private CompositeCell<CQLFunctionArgument> getCompositeCellForFuncArguModifyAndDelete() {
		final List<HasCell<CQLFunctionArgument, ?>> cells = new LinkedList<>();
		if (isEditable) {
			cells.add(getModifyQDMButtonCell());
			cells.add(getDeleteQDMButtonCell());
		}
		
		CompositeCell<CQLFunctionArgument> cell = new CompositeCell<CQLFunctionArgument>(cells) {
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
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td class='emptySpaces' tabindex=\"0\">");
				if ((object != null)) {
					cell.render(context, hasCell.getValue(object), sb);
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
		return cell;
	}
	
	private SafeHtml getDataTypeColumnToolTip(String columnText, StringBuilder title, boolean hasImage) {
		if (hasImage) {
			String htmlConstant = "<html>"
					+ "<head> </head> <Body><img src =\"images/error.png\" alt=\"Arugment Name is InValid.\""
					+ "title = \"Arugment Name is InValid.\"/>" + "<span tabIndex = \"0\" title='" + title + "'>"
					+ columnText + "</span></body>" + "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
		} else {
			String htmlConstant = "<html>" + "<head> </head> <Body><span tabIndex = \"0\" title='" + title + "'>"
					+ columnText + "</span></body>" + "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
		}
	}
	
	private HasCell<CQLFunctionArgument, SafeHtml> getModifyQDMButtonCell() {
		
		HasCell<CQLFunctionArgument, SafeHtml> hasCell = new HasCell<CQLFunctionArgument, SafeHtml>() {
			
			ClickableSafeHtmlCell modifyButonCell = new ClickableSafeHtmlCell();
			
			@Override
			public Cell<SafeHtml> getCell() {
				return modifyButonCell;
			}
			
			@Override
			public FieldUpdater<CQLFunctionArgument, SafeHtml> getFieldUpdater() {
				
				return new FieldUpdater<CQLFunctionArgument, SafeHtml>() {
					@Override
					public void update(int index, CQLFunctionArgument object, SafeHtml value) {
						if ((object != null)) {
							observer.onModifyClicked(object);
						}
					}
				};
			}
			
			@Override
			public SafeHtml getValue(CQLFunctionArgument object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String title = "Click to Modify Argument";
				String cssClass = "btn btn-link";
				String iconCss = "fa fa-pencil fa-lg";
				if(isEditable){
					sb.appendHtmlConstant("<button type=\"button\" title='"
							+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"color: darkgoldenrod;\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Edit</button>");
				} else {
					sb.appendHtmlConstant("<button type=\"button\" title='"
							+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" disabled style=\"color: black;\"><i class=\" "+iconCss + "\"></i> <span style=\"font-size:0;\">Edit</span></button>");
				}
				
				return sb.toSafeHtml();
			}
		};
		
		return hasCell;
	}
	
	private HasCell<CQLFunctionArgument, SafeHtml> getDeleteQDMButtonCell() {
		
		HasCell<CQLFunctionArgument, SafeHtml> hasCell = new HasCell<CQLFunctionArgument, SafeHtml>() {
			
			ClickableSafeHtmlCell deleteButonCell = new ClickableSafeHtmlCell();
			
			@Override
			public Cell<SafeHtml> getCell() {
				return deleteButonCell;
			}
			
			@Override
			public FieldUpdater<CQLFunctionArgument, SafeHtml> getFieldUpdater() {
				
				return new FieldUpdater<CQLFunctionArgument, SafeHtml>() {
					@Override
					public void update(int index, CQLFunctionArgument object, SafeHtml value) {
						observer.onDeleteClicked(object, index);
					}
				};
			}
			
			@Override
			public SafeHtml getValue(CQLFunctionArgument object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String title = "Click to Delete Argument";
				String cssClass = "btn btn-link";
				String iconCss = "fa fa-trash fa-lg";
				
				if (isEditable) {
					sb.appendHtmlConstant("<button type=\"button\" title='"
							+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"margin-left: 0px;\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Delete</button>");
				} else {
					sb.appendHtmlConstant("<button type=\"button\" title='"
							+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" disabled style=\"margin-left: 0px;\"><i class=\" "+iconCss + "\"></i> <span style=\"font-size:0;\">Delete</span></button>");
				}
				
				return sb.toSafeHtml();
			}
		};
		
		return hasCell;
	}

	/**
	 * Gets the observer.
	 *
	 * @return the observer
	 */
	public Observer getObserver() {
		return observer;
	}

	/**
	 * Sets the observer.
	 *
	 * @param observer the new observer
	 */
	public void setObserver(Observer observer) {
		this.observer = observer;
	}

	/**
	 * Gets the main function vertical panel.
	 *
	 * @return the main function vertical panel
	 */
	public FocusPanel getMainFunctionVerticalPanel() {
		return mainFunctionVerticalPanel;
	}

	/**
	 * Sets the main function vertical panel.
	 *
	 * @param mainFunctionVerticalPanel the new main function vertical panel
	 */
	public void setMainFunctionVerticalPanel(FocusPanel mainFunctionVerticalPanel) {
		this.mainFunctionVerticalPanel = mainFunctionVerticalPanel;
	}

	/**
	 * Gets the func name txt area.
	 *
	 * @return the func name txt area
	 */
	public MatTextBox getFuncNameTxtArea() {
		return funcNameTxtArea;
	}

	/**
	 * Sets the func name txt area.
	 *
	 * @param funcNameTxtArea the new func name txt area
	 */
	public void setFuncNameTxtArea(MatTextBox funcNameTxtArea) {
		this.funcNameTxtArea = funcNameTxtArea;
	}

	/**
	 * Gets the function body ace editor.
	 *
	 * @return the function body ace editor
	 */
	public AceEditor getFunctionBodyAceEditor() {
		return functionBodyAceEditor;
	}

	/**
	 * Sets the function body ace editor.
	 *
	 * @param functionBodyAceEditor the new function body ace editor
	 */
	public void setFunctionBodyAceEditor(AceEditor functionBodyAceEditor) {
		this.functionBodyAceEditor = functionBodyAceEditor;
	}

	/**
	 * Gets the adds the new argument.
	 *
	 * @return the adds the new argument
	 */
	public Button getAddNewArgument() {
		return addNewArgument;
	}

	/**
	 * Sets the adds the new argument.
	 *
	 * @param addNewArgument the new adds the new argument
	 */
	public void setAddNewArgument(Button addNewArgument) {
		this.addNewArgument = addNewArgument;
	}

	/**
	 * Gets the function button bar.
	 *
	 * @return the function button bar
	 */
	public DefinitionFunctionButtonToolBar getFunctionButtonBar() {
		return functionButtonBar;
	}

	/**
	 * Gets the context group.
	 *
	 * @return the contextGroup
	 */
	public ButtonGroup getContextGroup() {
		return contextGroup;
	}

	/**
	 * Sets the context group.
	 *
	 * @param contextGroup the contextGroup to set
	 */
	public void setContextGroup(ButtonGroup contextGroup) {
		this.contextGroup = contextGroup;
	}

	/**
	 * Sets the function button bar.
	 *
	 * @param functionButtonBar the new function button bar
	 */
	public void setFunctionButtonBar(DefinitionFunctionButtonToolBar functionButtonBar) {
		this.functionButtonBar = functionButtonBar;
	}

	/**
	 * Gets the context func PAT radio btn.
	 *
	 * @return the context func PAT radio btn
	 */
	public InlineRadio getContextFuncPATRadioBtn() {
		return contextFuncPATRadioBtn;
	}

	/**
	 * Sets the context func PAT radio btn.
	 *
	 * @param contextFuncPATRadioBtn the new context func PAT radio btn
	 */
	public void setContextFuncPATRadioBtn(InlineRadio contextFuncPATRadioBtn) {
		this.contextFuncPATRadioBtn = contextFuncPATRadioBtn;
	}

	/**
	 * Gets the context func POP radio btn.
	 *
	 * @return the context func POP radio btn
	 */
	public InlineRadio getContextFuncPOPRadioBtn() {
		return contextFuncPOPRadioBtn;
	}

	/**
	 * Sets the context func POP radio btn.
	 *
	 * @param contextFuncPOPRadioBtn the new context func POP radio btn
	 */
	public void setContextFuncPOPRadioBtn(InlineRadio contextFuncPOPRadioBtn) {
		this.contextFuncPOPRadioBtn = contextFuncPOPRadioBtn;
	}

	/**
	 * Gets the function argument list.
	 *
	 * @return the function argument list
	 */
	public List<CQLFunctionArgument> getFunctionArgumentList() {
		return functionArgumentList;
	}

	/**
	 * Sets the function argument list.
	 *
	 * @param functionArgumentList the new function argument list
	 */
	public void setFunctionArgumentList(List<CQLFunctionArgument> functionArgumentList) {
		this.functionArgumentList = functionArgumentList;
	}

	/**
	 * Gets the function arg name map.
	 *
	 * @return the function arg name map
	 */
	public Map<String, CQLFunctionArgument> getFunctionArgNameMap() {
		return functionArgNameMap;
	}

	/**
	 * Sets the function arg name map.
	 *
	 * @param functionArgNameMap the function arg name map
	 */
	public void setFunctionArgNameMap(Map<String, CQLFunctionArgument> functionArgNameMap) {
		this.functionArgNameMap = functionArgNameMap;
	}

	/**
	 * Checks if is editable.
	 *
	 * @return true, if is editable
	 */
	public boolean isEditable() {
		return isEditable;
	}

	/**
	 * Sets the editable.
	 *
	 * @param isEditable the new editable
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	
	/**
	 * Hide ace editor auto complete pop up.
	 */
	public void hideAceEditorAutoCompletePopUp() {
		getFunctionBodyAceEditor().detach();
	}
	
	/**
	 * Gets the adds the new button bar.
	 *
	 * @return the addNewButtonBar
	 */
	public CQLAddNewButton getAddNewButtonBar() {
		return addNewButtonBar;
	}

	/**
	 * Sets the adds the new button bar.
	 *
	 * @param addNewButtonBar the addNewButtonBar to set
	 */
	public void setAddNewButtonBar(CQLAddNewButton addNewButtonBar) {
		this.addNewButtonBar = addNewButtonBar;
	}

	/**
	 * Sets the widget read only.
	 *
	 * @param isEditable the new widget read only
	 */
	public void setWidgetReadOnly(boolean isEditable) {

		getFuncNameTxtArea().setEnabled(isEditable);
		getFunctionCommentTextArea().setEnabled(isEditable);
		getFunctionBodyAceEditor().setReadOnly(!isEditable);
		getFunctionButtonBar().setEnabled(isEditable);
		getAddNewArgument().setEnabled(isEditable);
		getContextFuncPATRadioBtn().setEnabled(isEditable);
		getContextFuncPOPRadioBtn().setEnabled(isEditable);
		getFunctionButtonBar().getDeleteButton().setTitle("Delete");

	}
	
	/**
	 * Reseet form group.
	 */
	public void reseetFormGroup(){
		getFuncCommentGroup().setValidationState(ValidationState.NONE);
	}

	/**
	 * Gets the function comment text area.
	 *
	 * @return the function comment text area
	 */
	public TextArea getFunctionCommentTextArea() {
		return funcCommentTextArea;
	}

	/**
	 * Sets the function comment text area.
	 *
	 * @param functionCommentTextArea the new function comment text area
	 */
	public void setFunctionCommentTextArea(TextArea functionCommentTextArea) {
		this.funcCommentTextArea = functionCommentTextArea;
	}

	/**
	 * Gets the func name group.
	 *
	 * @return the func name group
	 */
	public FormGroup getFuncNameGroup() {
		return funcNameGroup;
	}

	/**
	 * Sets the func name group.
	 *
	 * @param funcNameGroup the new func name group
	 */
	public void setFuncNameGroup(FormGroup funcNameGroup) {
		this.funcNameGroup = funcNameGroup;
	}

	/**
	 * Gets the func comment group.
	 *
	 * @return the func comment group
	 */
	public FormGroup getFuncCommentGroup() {
		return funcCommentGroup;
	}

	/**
	 * Sets the func comment group.
	 *
	 * @param funcCommentGroup the new func comment group
	 */
	public void setFuncCommentGroup(FormGroup funcCommentGroup) {
		this.funcCommentGroup = funcCommentGroup;
	}

	/**
	 * Gets the func context group.
	 *
	 * @return the func context group
	 */
	public FormGroup getFuncContextGroup() {
		return funcContextGroup;
	}

	/**
	 * Sets the func context group.
	 *
	 * @param funcContextGroup the new func context group
	 */
	public void setFuncContextGroup(FormGroup funcContextGroup) {
		this.funcContextGroup = funcContextGroup;
	}
	
	/**
	 * Reset func form group.
	 */
	public void resetFuncFormGroup(){
		getFuncNameGroup().setValidationState(ValidationState.NONE);
		getFuncCommentGroup().setValidationState(ValidationState.NONE);
	}

	public TextBox getReturnTypeTextBox() {
		return returnTypeTextBox;
	}
	
	public void setHeading(String text,String linkName) {
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr +"<h4><b>" + text + "</b></h4>");
	}
	
	/**
	 * Added this method as part of MAT-8882.
	 * @param isEditable
	 */
	public void setReadOnly(boolean isEditable) {		
		getAddNewButtonBar().getaddNewButton().setEnabled(isEditable);
		getAddNewArgument().setEnabled(isEditable);
		getFunctionButtonBar().getSaveButton().setEnabled(isEditable);
		getFunctionButtonBar().getEraseButton().setEnabled(isEditable);
		getFunctionButtonBar().getDeleteButton().setEnabled(isEditable);
		getFunctionButtonBar().getInsertButton().setEnabled(isEditable);
		getFunctionButtonBar().getInfoButton().setEnabled(isEditable);
	}

	public InAppHelp getInAppHelp() {
		return inAppHelp;
	}

	public void setInAppHelp(InAppHelp inAppHelp) {
		this.inAppHelp = inAppHelp;
	}	
}
