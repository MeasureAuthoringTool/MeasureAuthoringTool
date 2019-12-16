package mat.client.shared;

import com.google.gwt.cell.client.*;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import mat.shared.model.util.MeasureDetailsUtil;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Label;

import mat.client.cql.CQLLibrarySearchView.Observer;
import mat.client.util.CellTableUtility;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.shared.ClickableSafeHtmlCell;

public class CQLLibraryResultTable {

	private Observer observer;
	long lastClick = 0;

	public CellTable<CQLLibraryDataSetObject> addColumnToTable(String Label, CellTable<CQLLibraryDataSetObject> table, HasSelectionHandlers<CQLLibraryDataSetObject> fireEvent) {
		Label cqlLibrarySearchHeader = new Label(Label);
		cqlLibrarySearchHeader.getElement().setId("cqlLibrarySearchHeader_Label");
		cqlLibrarySearchHeader.setStyleName("recentSearchHeader");
		com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
		cqlLibrarySearchHeader.getElement().setAttribute("tabIndex", "0");
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(cqlLibrarySearchHeader.getElement());


		//Checkboxes to select specific row
		SingleSelectionModel<CQLLibraryDataSetObject> selectionModel = new SingleSelectionModel<>();
        table.setSelectionModel(selectionModel);

		//Todo Imlemented during MAT - 63/68

        CheckboxCell selectedCell = new CheckboxCell(true, false);
        Column<CQLLibraryDataSetObject,Boolean> selectedCol = new Column<CQLLibraryDataSetObject, Boolean>(selectedCell){
            @Override
            public Boolean getValue(CQLLibraryDataSetObject object) {
                return selectionModel.isSelected(object);
            }
        };
		if(MatContext.get().getMatOnFHIR().getFlagOn()) {
			table.addColumn(selectedCol);
		}

		// CQL Library Name Column
		Column<CQLLibraryDataSetObject, SafeHtml> cqlLibraryName = new Column<CQLLibraryDataSetObject, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(CQLLibraryDataSetObject object) {
				return getCQLLibraryNameColumnToolTip(object);
			}
			/*
				Single Click to select row and enable checkbox
				Double Click to navigate to CQL Composer tab
			*/
			@Override
			public void onBrowserEvent(Cell.Context context, Element elem, CQLLibraryDataSetObject object, NativeEvent event) {
				if (lastClick < System.currentTimeMillis() - 500)
				{
					object.setSelected(!object.isSelected());
					selectionModel.setSelected(object, object.isSelected());
				}else{
					SelectionEvent.fire(fireEvent, object);
				}
				lastClick = System.currentTimeMillis();
			}
		};
		table.addColumn(cqlLibraryName,
				SafeHtmlUtils.fromSafeConstant("<span title='CQL Library Name'>" + "CQL Library Name" + "</span>"));

		// Version Column
		Column<CQLLibraryDataSetObject, SafeHtml> version = new Column<CQLLibraryDataSetObject, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(CQLLibraryDataSetObject object) {
				return CellTableUtility.getColumnToolTip(object.getVersion());
			}
			/*
				Single Click to select row and enable checkbox
				Double Click to navigate to CQL Composer tab
			*/
			@Override
			public void onBrowserEvent(Cell.Context context, Element elem, CQLLibraryDataSetObject object, NativeEvent event) {
				if (lastClick < System.currentTimeMillis() - 500)
				{
					object.setSelected(!object.isSelected());
					selectionModel.setSelected(object, object.isSelected());
				}else{
					SelectionEvent.fire(fireEvent, object);
				}
				lastClick = System.currentTimeMillis();
			}
		};
		table.addColumn(version, SafeHtmlUtils.fromSafeConstant("<span title='Version'>" + "Version" + "</span>"));

		//Library Model Type
		Column<CQLLibraryDataSetObject, SafeHtml> model = new Column<CQLLibraryDataSetObject, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(CQLLibraryDataSetObject object) {
				if(object.getLibraryModelType() != null && !object.getLibraryModelType().isEmpty())
					return CellTableUtility.getColumnToolTip(object.getLibraryModelType());
				else
					return CellTableUtility.getColumnToolTip(MeasureDetailsUtil.PRE_CQL);
			}
			/*
				Single Click to select row and enable checkbox
				Double Click to navigate to CQL Composer tab
			*/
			@Override
			public void onBrowserEvent(Cell.Context context, Element elem, CQLLibraryDataSetObject object, NativeEvent event) {
				if (lastClick < System.currentTimeMillis() - 500)
				{
					object.setSelected(!object.isSelected());
					selectionModel.setSelected(object, object.isSelected());
				}else{
					SelectionEvent.fire(fireEvent, object);
				}
				lastClick = System.currentTimeMillis();
			}
		};
		if(MatContext.get().getMatOnFHIR().getFlagOn())
			table.addColumn(model, SafeHtmlUtils.fromSafeConstant("<span title='Version'>" + "Model" + "</span>"));

		ButtonCell buttonCell = new ButtonCell(ButtonType.LINK);
		Column<CQLLibraryDataSetObject,String> draftOrVersionCol = new Column<CQLLibraryDataSetObject, String>(buttonCell) {

			@Override
			public String getValue(CQLLibraryDataSetObject object) {
				return object.getCqlName();
			}

			@Override
			public void render(Context context, CQLLibraryDataSetObject object, SafeHtmlBuilder sb) {
				if (object.isDraftable()) {
					sb.appendHtmlConstant("<button class=\"btn btn-link\" type=\"button\" title =\"Click to create draft\" tabindex=\"0\">");
					sb.appendHtmlConstant("<i class=\"fa fa-pencil-square-o fa-lg\" style=\"margin-left: 15px;\"></i>");
					sb.appendHtmlConstant("<span class=\"invisibleButtonText\">Create Draft</span>");
					sb.appendHtmlConstant("</button>");
				} else if (object.isVersionable()) {
					sb.appendHtmlConstant("<button class=\"btn btn-link\" type=\"button\" tabindex=\"0\" title =\"Click to create version\" style=\"color: goldenrod;\" >");
					sb.appendHtmlConstant("<i class=\"fa fa-star fa-lg\" style=\"margin-left: 15px;\"></i>");
					sb.appendHtmlConstant("<span class=\"invisibleButtonText\">Create Version</span>");
					sb.appendHtmlConstant("</button>");
				}

			}
			@Override
			public void onBrowserEvent(Context context, Element elem, CQLLibraryDataSetObject object,
					NativeEvent event) {

				String type = event.getType();
				if(type.equalsIgnoreCase(BrowserEvents.CLICK)){
					if(!object.isDraftable() && !object.isVersionable()){
						event.preventDefault();
					} else {
						observer.onDraftOrVersionClick(object);
					}
				} else {
					if(!object.isDraftable() && !object.isVersionable()){
						event.preventDefault();
					}
				}

			}

		};

		table.addColumn(draftOrVersionCol,
				SafeHtmlUtils.fromSafeConstant("<span title='Create Version/Draft'>" + "Create Version/Draft" + "</span>"));


		// History
		Cell<String> historyButton = new MatButtonCell("Click to view history", "btn btn-link", "fa fa-clock-o fa-lg" , "History");
		Column<CQLLibraryDataSetObject, String> historyColumn = new Column<CQLLibraryDataSetObject, String>(
				historyButton) {
			@Override
			public String getValue(CQLLibraryDataSetObject object) {
				return "";
			}
		};
		historyColumn.setFieldUpdater(new FieldUpdater<CQLLibraryDataSetObject, String>() {
			@Override
			public void update(int index, CQLLibraryDataSetObject object, String value) {
				observer.onHistoryClicked(object);
			}
		});
		table.addColumn(historyColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='History'>" + "History" + "</span>"));

		//Edit
		Column<CQLLibraryDataSetObject, SafeHtml> editColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(
				new ClickableSafeHtmlCell()) {

			@Override
			public SafeHtml getValue(CQLLibraryDataSetObject object) {
				return getEditColumnToolTip(object);
			}
		};
		editColumn.setFieldUpdater(new FieldUpdater<CQLLibraryDataSetObject, SafeHtml>() {
			@Override
			public void update(int index, CQLLibraryDataSetObject object, SafeHtml value) {
				if (object.isEditable() && !object.isLocked()) {
					observer.onEditClicked(object);
				}
			}
		});
		table.addColumn(editColumn, SafeHtmlUtils.fromSafeConstant("<span title='Edit'>" + "Edit" + "</span>"));

		// Share
		Column<CQLLibraryDataSetObject, SafeHtml> shareColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(CQLLibraryDataSetObject object) {
				return getShareColumnToolTip(object);
			}
		};
		shareColumn.setFieldUpdater(new FieldUpdater<CQLLibraryDataSetObject, SafeHtml>() {
			@Override
			public void update(int index, CQLLibraryDataSetObject object, SafeHtml value) {
				if (object.isSharable())
					observer.onShareClicked(object);
			}
		});
		table.addColumn(shareColumn, SafeHtmlUtils.fromSafeConstant("<span title='Share'>" + "Share" + "</span>"));

		//Delete Column
				ButtonCell deleteButtonCell = new ButtonCell(ButtonType.LINK);
				Column<CQLLibraryDataSetObject,String> deleteColumn = new Column<CQLLibraryDataSetObject, String>(deleteButtonCell) {

					@Override
					public String getValue(CQLLibraryDataSetObject object) {
						return object.getCqlName();
					}

					@Override
					public void render(Context context, CQLLibraryDataSetObject object, SafeHtmlBuilder sb) {
						if (object.isDeletable()) {
							sb.appendHtmlConstant("<button class=\"btn btn-link\" type=\"button\" title =\"Click to delete library\" tabindex=\"0\">");
							sb.appendHtmlConstant("<i class=\"fa fa-trash fa-lg\" style=\"margin-left: 5px;\"></i>");
							sb.appendHtmlConstant("<span class=\"invisibleButtonText\">Delete CQL Library</span>");
							sb.appendHtmlConstant("</button>");
						}
					}
					@Override
					public void onBrowserEvent(Context context, Element elem, CQLLibraryDataSetObject object,
							NativeEvent event) {
						String type = event.getType();
						if(type.equalsIgnoreCase(BrowserEvents.CLICK)){
							if(!object.isDeletable()){
								event.preventDefault();
							} else {
								observer.onDeleteClicked(object);
							}
						} else {
							if(!object.isDeletable()){
								event.preventDefault();
							}
						}
					}

				};

				table.addColumn(deleteColumn,
						SafeHtmlUtils.fromSafeConstant("<span title='Delete'>" + "Delete" + "</span>"));

		return table;
	}

	private SafeHtml getShareColumnToolTip(CQLLibraryDataSetObject object) {

		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String title = "Shareable";
		String cssClass = "btn btn-link";
		String iconCss = "fa fa-share-square fa-lg";
		if (object.isSharable()) {
			sb.appendHtmlConstant("<button type=\"button\" title='"
				+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"color: darkseagreen;\"><i class=\" " + iconCss + "\"></i> <span style=\"font-size:0;\">Shareable</span></button>");
		} else {
			sb.appendHtmlConstant("<button type=\"button\" title='"
					+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" disabled style=\"color: gray;\"><i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Shareable</span></button>");
		}

		return sb.toSafeHtml();
	}

	private SafeHtml getEditColumnToolTip(CQLLibraryDataSetObject object){
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String title;
		String cssClass = "btn btn-link";
		String iconCss;
		if (object.isEditable()) {
			if (object.isLocked()) {
				String emailAddress = object.getLockedUserInfo().getEmailAddress();
				title = "Library in use by " + emailAddress;
				iconCss = "fa fa-lock fa-lg";
			} else {
				title = "Edit";
				iconCss = "fa fa-pencil fa-lg";

			}
			sb.appendHtmlConstant("<button type=\"button\" title='"
					+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"color: darkgoldenrod;\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Edit</button>");
		} else {
			title = "Read-Only";
			iconCss = "fa fa-newspaper-o fa-lg";
			sb.appendHtmlConstant("<button type=\"button\" title='"
					+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" disabled style=\"color: black;\"><i class=\" "+iconCss + "\"></i> <span style=\"font-size:0;\">Read-Only</span></button>");
		}

		return sb.toSafeHtml();
	}

	private SafeHtml getCQLLibraryNameColumnToolTip(CQLLibraryDataSetObject object) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String cssClass = "customCascadeButton";
		String editState =  MatContext.get().getMatOnFHIR().getFlagOn() ? getEditStateOfLibrary(object) : "";
		if (object.isFamily()) {
			sb.appendHtmlConstant("<div id='container' tabindex=\"-1\"><a href=\"javascript:void(0);\" "
					+ "style=\"text-decoration:none\" tabindex=\"-1\">");
			sb.appendHtmlConstant(editState);
			sb.appendHtmlConstant("<button id='div1' class='textEmptySpaces' tabindex=\"-1\" disabled='disabled'></button>");
			sb.appendHtmlConstant("<span id='div2' title=\" Click to open " + object.getCqlName() + "\" tabindex=\"0\">"
					+ object.getCqlName() + "</span>");
			sb.appendHtmlConstant("</a></div>");
		} else {
			sb.appendHtmlConstant("<div id='container' tabindex=\"-1\"><a href=\"javascript:void(0);\" "
					+ "style=\"text-decoration:none\" tabindex=\"-1\" >");
			sb.appendHtmlConstant(editState);
			sb.appendHtmlConstant("<button id='div1' type=\"button\" title=\"" + object.getCqlName()
					+ "\" tabindex=\"-1\" class=\" " + cssClass + "\"></button>");
			sb.appendHtmlConstant("<span id='div2' title=\" Click to open " + object.getCqlName() + "\" tabindex=\"0\">"
					+ object.getCqlName() + "</span>");
			sb.appendHtmlConstant("</a></div>");
		}
		return sb.toSafeHtml();
	}

	/**
	 * This method creates icons relevant to the edit state of CQL Library
	 * @param object CQLLibraryDataSetObject
	 * @return string containing edit state icon
	 */
	private String getEditStateOfLibrary(CQLLibraryDataSetObject object) {
		String title;
		String iconCss;
		if (object.isEditable()) {
			if (object.isLocked()) {
				String emailAddress = object.getLockedUserInfo().getEmailAddress();
				title = "Library in use by " + emailAddress;
				iconCss = "fa fa-lock fa-lg";
			} else {
				title = "Edit";
				iconCss = "fa fa-pencil fa-lg width-14x";
			}
		} else {
			title = "Read-Only";
			iconCss = "fa fa-eye fa-lg width-14x";
		}
		return "<i class=\"pull-left " + iconCss + "\" title=\""+title+"\"></i>";
	}
	
	public Observer getObserver() {
		return observer;
	}
	
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
}
