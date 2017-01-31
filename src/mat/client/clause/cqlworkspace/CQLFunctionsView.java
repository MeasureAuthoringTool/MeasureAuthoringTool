/**
 * 
 */
package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InlineRadio;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.client.CustomPager;

import mat.client.shared.CQLButtonToolBar;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.client.util.MatTextBox;
import mat.model.cql.CQLFunctionArgument;
import mat.shared.ClickableSafeHtmlCell;

/**
 * @author jnarang
 *
 */
public class CQLFunctionsView {
	/**
	 * The Interface Observer.
	 */
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
	private VerticalPanel mainFunctionVerticalPanel = new VerticalPanel();
	private MatTextBox funcNameTxtArea = new MatTextBox();
	/** The Function Body ace editor. */
	private AceEditor functionBodyAceEditor = new AceEditor();
	private Button addNewArgument = new Button();

	/** The function button bar. */
	CQLButtonToolBar functionButtonBar = new CQLButtonToolBar("function");
	
	/** The context pat toggle switch. */
	private InlineRadio contextFuncPATRadioBtn = new InlineRadio("Patient");
	
	/** The context pop toggle switch. */
	private InlineRadio contextFuncPOPRadioBtn = new InlineRadio("Population");
	
	/** The function argument list. */
	private List<CQLFunctionArgument> functionArgumentList = new ArrayList<CQLFunctionArgument>();
	
	
	private VerticalPanel cellTablePanel = new VerticalPanel();
	
	/** Cell Table Row Count. */
	private static final int TABLE_ROW_COUNT = 2;
	
	/** The argument list table. */
	private CellTable<CQLFunctionArgument> argumentListTable;
	
	/** The sort provider. */
	private ListDataProvider<CQLFunctionArgument> listDataProvider;
	
	/** The spager. */
	private MatSimplePager spager;
	
	private Map<String, CQLFunctionArgument> functionArgNameMap = new  HashMap<String, CQLFunctionArgument>();
	boolean isEditable = false;
	

	public CQLFunctionsView() {
		// TODO Auto-generated constructor stub
		mainFunctionVerticalPanel.clear();
		functionBodyAceEditor.startEditor();
	}

	@SuppressWarnings("static-access")
	private void buildView() {
		VerticalPanel funcVP = new VerticalPanel();
		HorizontalPanel funcFP = new HorizontalPanel();

		Label functionNameLabel = new Label(LabelType.INFO, "Function Name");
		functionNameLabel.setMarginTop(5);
		functionNameLabel.setId("Function_Label");
		funcNameTxtArea.setText("");
		// funcNameTxtArea.setPlaceholder("Enter Function Name here.");
		funcNameTxtArea.setSize("260px", "25px");
		funcNameTxtArea.getElement().setId("FunctionNameField");
		funcNameTxtArea.setName("FunctionName");
		functionNameLabel.setText("Function Name");

		SimplePanel funcAceEditorPanel = new SimplePanel();
		funcAceEditorPanel.setSize("685", "510");
		functionBodyAceEditor.setText("");
		functionBodyAceEditor.setMode(AceEditorMode.CQL);
		functionBodyAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		functionBodyAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		functionBodyAceEditor.setSize("675px", "500px");
		functionBodyAceEditor.setAutocompleteEnabled(true);
		functionBodyAceEditor.addAutoCompletions();
		functionBodyAceEditor.setUseWrapMode(true);
		functionBodyAceEditor.clearAnnotations();
		functionBodyAceEditor.removeAllMarkers();
		functionBodyAceEditor.redisplay();
		functionBodyAceEditor.getElement().setAttribute("id", "Func_AceEditorID");
		funcAceEditorPanel.add(functionBodyAceEditor);
		funcAceEditorPanel.getElement().setAttribute("id", "SimplePanel_Function_AceEditor");
		funcAceEditorPanel.setStyleName("cqlRightContainer");

		addNewArgument.setType(ButtonType.LINK);
		addNewArgument.getElement().setId("addArgument_Button");

		addNewArgument.setTitle("Add Argument");
		addNewArgument.setText("Add Argument");
		addNewArgument.setId("Add_Argument_ID");
		addNewArgument.setIcon(IconType.PLUS);
		addNewArgument.setSize(ButtonSize.SMALL);

		addNewArgument.setPull(Pull.RIGHT);

		Label funcContextLabel = new Label(LabelType.INFO, "Context");
		FlowPanel funcConextPanel = new FlowPanel();

		contextFuncPATRadioBtn.setValue(true);
		contextFuncPATRadioBtn.setText("Patient");
		contextFuncPATRadioBtn.setId("context_PatientRadioButton");
		contextFuncPOPRadioBtn.setValue(false);
		contextFuncPOPRadioBtn.setText("Population");
		contextFuncPOPRadioBtn.setId("context_PopulationRadioButton");
		functionButtonBar.getTimingExpButton().setVisible(false);
		functionButtonBar.getCloseButton().setVisible(false);
		funcConextPanel.add(contextFuncPATRadioBtn);
		funcConextPanel.add(contextFuncPOPRadioBtn);
		funcConextPanel.setStyleName("contextToggleSwitch");

		funcVP.add(new SpacerWidget());
		funcVP.add(functionNameLabel);
		funcVP.add(new SpacerWidget());
		funcVP.add(funcNameTxtArea);
		funcVP.add(new SpacerWidget());
		funcVP.add(funcContextLabel);
		funcVP.add(new SpacerWidget());
		funcVP.add(funcConextPanel);
		funcVP.add(new SpacerWidget());
		funcVP.add(addNewArgument);
		createAddArgumentViewForFunctions(functionArgumentList);
		funcVP.add(cellTablePanel);
		funcVP.add(functionButtonBar);
		funcVP.add(new SpacerWidget());
		funcVP.add(funcAceEditorPanel);
		funcVP.add(new SpacerWidget());
		funcVP.setStyleName("topping");
		funcFP.add(funcVP);
		funcFP.setStyleName("cqlRightContainer");

		mainFunctionVerticalPanel.setStyleName("cqlRightContainer");
		mainFunctionVerticalPanel.setWidth("700px");
		mainFunctionVerticalPanel.setHeight("500px");
		funcFP.setWidth("700px");
		funcFP.setStyleName("marginLeft15px");
	
		mainFunctionVerticalPanel.clear();
		mainFunctionVerticalPanel.add(funcFP);
		mainFunctionVerticalPanel.setHeight("675px");
	}
	
	
	public VerticalPanel getView() {
		mainFunctionVerticalPanel.clear();
		resetAll();
		buildView();
		return mainFunctionVerticalPanel;
	}
	
	public void resetAll() {
		getFuncNameTxtArea().setText("");
		getFunctionBodyAceEditor().setText("");
	}
	
	public void createAddArgumentViewForFunctions(List<CQLFunctionArgument> argumentList) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		isEditable = MatContext.get().getMeasureLockService().checkForEditPermission();
		
		if ((argumentList != null) && (argumentList.size() > 0)) {
			updateFunctionArgumentNameMap(argumentList);
			argumentListTable = new CellTable<CQLFunctionArgument>();
			argumentListTable.setStriped(true);
			argumentListTable.setCondensed(true);
			argumentListTable.setBordered(true);
			argumentListTable.setHover(true);
			
			argumentListTable.setPageSize(TABLE_ROW_COUNT);
			argumentListTable.redraw();
			listDataProvider = new ListDataProvider<CQLFunctionArgument>();
			listDataProvider.refresh();
			listDataProvider.getList().addAll(argumentList);
			ListHandler<CQLFunctionArgument> sortHandler = new ListHandler<CQLFunctionArgument>(
					listDataProvider.getList());
			argumentListTable.addColumnSortHandler(sortHandler);
			argumentListTable = addColumnToTable(argumentListTable, sortHandler);
			listDataProvider.addDataDisplay(argumentListTable);
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
			spager.setDisplay(argumentListTable);
			spager.setPageStart(0);
			cellTablePanel.add(argumentListTable);
			// cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(spager);
		} else {
			com.google.gwt.user.client.ui.Label tableHeader = new com.google.gwt.user.client.ui.Label(
					"Added Arguments List");
			tableHeader.getElement().setId("tableHeader_Label");
			tableHeader.setStyleName("measureGroupingTableHeader");
			tableHeader.getElement().setAttribute("tabIndex", "0");
			HTML desc = new HTML("<p> No Arguments Added.</p>");
			cellTablePanel.add(tableHeader);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(desc);
		}
		/*
		 * } else { com.google.gwt.user.client.ui.Label tableHeader = new
		 * com.google.gwt.user.client.ui.Label("Added Arguments List");
		 * tableHeader.getElement().setId("tableHeader_Label");
		 * tableHeader.setStyleName("measureGroupingTableHeader");
		 * tableHeader.getElement().setAttribute("tabIndex", "0"); HTML desc =
		 * new HTML("<p> No Arguments Added.</p>");
		 * cellTablePanel.add(tableHeader); cellTablePanel.add(new
		 * SpacerWidget()); cellTablePanel.add(desc); }
		 */
		
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
			
			MultiSelectionModel<CQLFunctionArgument> selectionModel = new MultiSelectionModel<CQLFunctionArgument>();
			table.setSelectionModel(selectionModel);
			Column<CQLFunctionArgument, SafeHtml> nameColumn = new Column<CQLFunctionArgument, SafeHtml>(
					new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue(CQLFunctionArgument object) {
					StringBuilder title = new StringBuilder();
					StringBuilder value = new StringBuilder(object.getArgumentName());
					
					title = title.append("Name : ").append(value);
					/*
					 * return
					 * CellTableUtility.getColumnToolTip(value.toString(),
					 * title.toString());
					 */
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
			
			// Modify by Delete Column
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
		final List<HasCell<CQLFunctionArgument, ?>> cells = new LinkedList<HasCell<CQLFunctionArgument, ?>>();
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
	
	/**
	 * Gets the data type column tool tip.
	 *
	 * @param columnText
	 *            the column text
	 * @param title
	 *            the title
	 * @param hasImage
	 *            the has image
	 * @return the data type column tool tip
	 */
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
	
	/**
	 * Gets the modify qdm button cell.
	 *
	 * @return the modify qdm button cell
	 */
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
				String title = "Click to Modify QDM";
				String cssClass = "customEditButton";
				
				if (isEditable) {
					sb.appendHtmlConstant("<button tabindex=\"0\" type=\"button\" title='" + title + "' class=\" "
							+ cssClass + "\">Editable</button>");
				} else {
					sb.appendHtmlConstant("<button tabindex=\"0\" type=\"button\" title='" + title + "' class=\" "
							+ cssClass + "\" disabled/>Editable</button>");
				}
				
				return sb.toSafeHtml();
			}
		};
		
		return hasCell;
	}
	
	/**
	 * Gets the delete qdm button cell.
	 * 
	 * @return the delete qdm button cell
	 */
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
				String title = "Click to Delete QDM";
				String cssClass;
				
				cssClass = "customDeleteButton";
				if (isEditable) {
					sb.appendHtmlConstant("<button tabindex=\"0\"type=\"button\" title='" + title + "' class=\" "
							+ cssClass + "\"/>Delete</button>");
					
				} else {
					sb.appendHtmlConstant("<button tabindex=\"0\"type=\"button\" title='" + title + "' class=\" "
							+ cssClass + "\" disabled/>Delete</button>");
				}
				
				return sb.toSafeHtml();
			}
		};
		
		return hasCell;
	}

	public Observer getObserver() {
		return observer;
	}

	public void setObserver(Observer observer) {
		this.observer = observer;
	}

	public VerticalPanel getMainFunctionVerticalPanel() {
		return mainFunctionVerticalPanel;
	}

	public void setMainFunctionVerticalPanel(VerticalPanel mainFunctionVerticalPanel) {
		this.mainFunctionVerticalPanel = mainFunctionVerticalPanel;
	}

	public MatTextBox getFuncNameTxtArea() {
		return funcNameTxtArea;
	}

	public void setFuncNameTxtArea(MatTextBox funcNameTxtArea) {
		this.funcNameTxtArea = funcNameTxtArea;
	}

	public AceEditor getFunctionBodyAceEditor() {
		return functionBodyAceEditor;
	}

	public void setFunctionBodyAceEditor(AceEditor functionBodyAceEditor) {
		this.functionBodyAceEditor = functionBodyAceEditor;
	}

	public Button getAddNewArgument() {
		return addNewArgument;
	}

	public void setAddNewArgument(Button addNewArgument) {
		this.addNewArgument = addNewArgument;
	}

	public CQLButtonToolBar getFunctionButtonBar() {
		return functionButtonBar;
	}

	public void setFunctionButtonBar(CQLButtonToolBar functionButtonBar) {
		this.functionButtonBar = functionButtonBar;
	}

	public InlineRadio getContextFuncPATRadioBtn() {
		return contextFuncPATRadioBtn;
	}

	public void setContextFuncPATRadioBtn(InlineRadio contextFuncPATRadioBtn) {
		this.contextFuncPATRadioBtn = contextFuncPATRadioBtn;
	}

	public InlineRadio getContextFuncPOPRadioBtn() {
		return contextFuncPOPRadioBtn;
	}

	public void setContextFuncPOPRadioBtn(InlineRadio contextFuncPOPRadioBtn) {
		this.contextFuncPOPRadioBtn = contextFuncPOPRadioBtn;
	}

	public List<CQLFunctionArgument> getFunctionArgumentList() {
		return functionArgumentList;
	}

	public void setFunctionArgumentList(List<CQLFunctionArgument> functionArgumentList) {
		this.functionArgumentList = functionArgumentList;
	}

	public Map<String, CQLFunctionArgument> getFunctionArgNameMap() {
		return functionArgNameMap;
	}

	public void setFunctionArgNameMap(Map<String, CQLFunctionArgument> functionArgNameMap) {
		this.functionArgNameMap = functionArgNameMap;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	
	public void hideAceEditorAutoCompletePopUp() {
		getFunctionBodyAceEditor().detach();
	}
}
