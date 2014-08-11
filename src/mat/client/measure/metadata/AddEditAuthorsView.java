package mat.client.measure.metadata;

import java.util.ArrayList;
import java.util.List;

import mat.client.CustomPager;
import mat.client.codelist.HasListBox;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.resource.CellTableResource;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.SearchResults;
import mat.client.util.CellTableUtility;
import mat.model.Author;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

// TODO: Auto-generated Javadoc
/**
 * The Class AddEditAuthorsView.
 */
public class AddEditAuthorsView implements MetaDataPresenter.AddEditAuthorsDisplay{

	/** The author input. */
	private ListBoxMVP authorInput;
	
	/** The author h panel. */
	private HorizontalPanel authorHPanel = new HorizontalPanel();
	
	/** The message h panel. */
	private HorizontalPanel messageHPanel = new HorizontalPanel();
	
	/** The view. */
	//private SearchView<Author> view;
	
	
	/** The author v panel. */
	private VerticalPanel authorVPanel = new VerticalPanel();
	
	/** The message v panel. */
	private VerticalPanel messageVPanel = new VerticalPanel();
	
	/** The add success msg panel. */
	VerticalPanel addSuccessMsgPanel =  new VerticalPanel();
	
	/** The cell table css style. */
	private List<String> cellTableCssStyle;
	
	
	/** The author cell table. */
	private CellTable<Author> authorCellTable;
	
	/** The author selection model. */
	private MultiSelectionModel<Author> authorSelectionModel;
	
	/** The author selected list. */
	private List<Author> authorSelectedList;
	
	/** The list of all author. */
	private List<Author> listOfAllAuthor;
	
	/** The cancel button. */
	private Button addEditcancelButton = new PrimaryButton("cancel");
	
	/** The add button. */
	private Button addButton = new PrimaryButton("Add to List");
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	
	/** The Constant PAGE_SIZE. */
	private static final int PAGE_SIZE = 25;
	
	/** The measure dev input. */
	private TextBox measureDevInput = new TextBox(); 
	
	/** The simp panel. */
	private SimplePanel simpPanel = new SimplePanel();
	
	/** The index. */
	private int index;
	
	/** The even. */
	private Boolean even;
	
	/** The cell table even row. */
	private String cellTableEvenRow = "cellTableEvenRow";
	/** The cell table odd row. */
	private String cellTableOddRow = "cellTableOddRow";
	
	/** The return button. */
	protected Button returnButton = new PrimaryButton("Return to Previous");
	
	/** The success messages. */
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();

	
	
	/**
	 * Instantiates a new adds the edit authors view.
	 */
	public AddEditAuthorsView() {		
		addSuccessMsgPanel.addStyleName("marginTop");
		addSuccessMsgPanel.setWidth("800px");
		simpPanel.setWidth("100px");
		authorVPanel.setWidth("600px");
		mainPanel.setStyleName("contentPanel");
		authorHPanel.add(authorVPanel);
		authorHPanel.add(simpPanel);
		authorHPanel.add(buildAddMeasureDevPanel());
		messageHPanel.add(messageVPanel);		
		mainPanel.add(authorHPanel);
		mainPanel.add(messageHPanel);
	}
	
	/**
	 * Builds the add measure dev panel.
	 *
	 * @return the widget
	 */
	public Widget buildAddMeasureDevPanel(){
		VerticalPanel addMeasureDevPanel = new VerticalPanel();		
		addMeasureDevPanel.getElement().setId("searchPanel_VerticalPanel");
		addMeasureDevPanel.setStyleName("cellTablePanel");
		//addMeasureDevPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label addMeasureDevHeader = new Label("Add Measure Developer");
		addMeasureDevHeader.getElement().setId("measureDevHeader_Label");
		addMeasureDevHeader.setStyleName("recentSearchHeader");
		addMeasureDevHeader.getElement().setAttribute("tabIndex", "0");
		addMeasureDevPanel.add(addMeasureDevHeader);
		Label name = new Label();
		name.setText("Name");
		name.setStyleName("measureDevAddList");
		measureDevInput.getElement().setId("measureDevInput_TextBox");
		measureDevInput.getElement().setAttribute("tabIndex", "0");
		measureDevInput.setTitle("Enter Measure Developer's Name");
		measureDevInput.setWidth("150px");
		measureDevInput.setMaxLength(200);
		measureDevInput.setStyleName("measureDevAddList");
		HorizontalPanel bottomButtonPanel = new HorizontalPanel();
	//	bottomButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		bottomButtonPanel.setStyleName("measureDevAddList");
		addMeasureDevPanel.add(new SpacerWidget());		  
		bottomButtonPanel.add(addButton);
		bottomButtonPanel.add(addEditcancelButton);
		addEditcancelButton.setTitle("Cancel");
		bottomButtonPanel.setWidth("200px");
		addMeasureDevPanel.add(name);
		measureDevInput.setText("");
		addMeasureDevPanel.add(measureDevInput);
		addMeasureDevPanel.add(new SpacerWidget());
		addMeasureDevPanel.add(bottomButtonPanel);
		addMeasureDevPanel.add(new SpacerWidget());
		return addMeasureDevPanel;
	}
	
	/** The add to measure developer list. */
	Button addToMeasureDeveloperList = new PrimaryButton("Add To Measure Developer List");
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.AddEditMetadataBaseView#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return mainPanel;
	}

		/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.AddEditAuthorsDisplay#buildAuthorCellTable(java.util.List, boolean)
	 */
	@Override
	public void buildAuthorCellTable(List<Author> authorList, boolean editable, List<Author> authorsSelectedList){
		measureDevInput.setText("");
		listOfAllAuthor = authorList;
		successMessages.clear();
		authorVPanel.clear();
		addSuccessMsgPanel.clear();
		messageVPanel.clear();
		authorCellTable = new CellTable<Author>(PAGE_SIZE,
				(Resources) GWT.create(CellTableResource.class));
		//authorCellTable.setStyleName("cellTablePanel");
		authorVPanel.setStyleName("cellTablePanel");
		authorSelectedList = new ArrayList<Author>();
		authorSelectedList.addAll(authorsSelectedList);
		authorCellTable.setPageSize(PAGE_SIZE);
		if(authorList.size()>0){
			
			authorCellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			authorCellTable.setRowData(authorList);
			authorCellTable.setRowCount(authorList.size(), true);
			authorCellTable.redraw();
			addAuthorColumnToTable(editable);
			authorCellTable.setWidth("100%");
			ListDataProvider<Author> provider = new ListDataProvider<Author>();
			provider.refresh();
			provider.getList().addAll(authorList);
			provider.addDataDisplay(authorCellTable);
			CustomPager.Resources pagerResources = GWT
					.create(CustomPager.Resources.class);
			MatSimplePager spager = new MatSimplePager(
					CustomPager.TextLocation.CENTER, pagerResources, false, 0,
					true);
			spager.setPageStart(0);
			buildAuthorCellTableCssStyle();
			spager.setDisplay(authorCellTable);
			spager.setPageSize(PAGE_SIZE);
			Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("authorListSummary",
					"In the following Measure Developer List table,Select is given in first Column and Author is given in Second column");
			authorCellTable.getElement().setAttribute("id", "MeasureDeveloperListCellTable");
			authorCellTable.getElement().setAttribute("aria-describedby", "measureDeveloperListSummary");
			//authorCellTable.setPageSize(PAGE_SIZE);
			authorVPanel.add(invisibleLabel);
			authorVPanel.add(authorCellTable);
			authorVPanel.add(new SpacerWidget());
			authorVPanel.add(spager);
			addSuccessMsgPanel.add(successMessages);		
			messageVPanel.add(addSuccessMsgPanel);
			messageVPanel.add(addToMeasureDeveloperList);
			messageVPanel.add(new SpacerWidget());
			messageVPanel.add(returnButton);
			
		}

			
	}

	/**
	 * Adds the author column to table.
	 *
	 * @param editable the editable
	 */
	private void addAuthorColumnToTable(boolean editable) {
		Label measureSearchHeader = new Label("Measure Developer List");
		measureSearchHeader.getElement().setId("measureDeveloperHeader_Label");
		measureSearchHeader.setStyleName("recentSearchHeader");
		com.google.gwt.dom.client.TableElement elem = authorCellTable.getElement().cast();
		measureSearchHeader.getElement().setAttribute("tabIndex", "0");
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(measureSearchHeader.getElement());
		authorSelectionModel = new MultiSelectionModel<Author>();
		authorCellTable.setSelectionModel(authorSelectionModel);
		MatCheckBoxCell chbxCell = new MatCheckBoxCell(false, true);
		Column<Author, Boolean> selectColumn = new Column<Author, Boolean>(
				chbxCell) {

			@Override
			public Boolean getValue(Author object) {
				boolean isSelected = false;
				if (authorSelectedList != null && authorSelectedList.size() > 0) {
					for (int i = 0; i < authorSelectedList.size(); i++) {
						if (authorSelectedList.get(i).getOrgId()
								.equalsIgnoreCase(object.getOrgId())) {
						isSelected = true;
							break;
						}
					}
				} else {
				isSelected = false;
				}
				return isSelected;
				
			}
		};

		selectColumn.setFieldUpdater(new FieldUpdater<Author, Boolean>() {

			@Override
			public void update(int index, Author object, Boolean value) {
				authorSelectionModel.setSelected(object, value);
				if (value) {
					authorSelectedList.add(object);
				} else {
					for (int i = 0; i < authorSelectedList.size(); i++) {
						if (authorSelectedList.get(i).getOrgId()
								.equalsIgnoreCase(object.getOrgId())) {
							authorSelectedList.remove(i);
							break;
						}
					}

				}
			}
		});
		authorCellTable.addColumn(selectColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='Select'>"
						+ "Select" + "</span>"));

		Column<Author, SafeHtml> measureNameColumn = new Column<Author, SafeHtml>(
				new SafeHtmlCell()) {

			@Override
			public SafeHtml getValue(Author object) {
				return CellTableUtility.getColumnToolTip(object.getAuthorName());
			}
		};

		authorCellTable.addColumn(measureNameColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='Measure Developers Name'>"
						+ "Measure Developer" + "</span>"));

		
	}
	
	/**
	 * Builds the author cell table css style.
	 */
	private void buildAuthorCellTableCssStyle() {
		cellTableCssStyle = new ArrayList<String>();
		for (int i = 0; i < listOfAllAuthor.size(); i++) {
			cellTableCssStyle.add(i, null);
		}
		authorCellTable.setRowStyles(new RowStyles<Author>() {
			@Override
			public String getStyleNames(
					Author rowObject, int rowIndex) {
				if (rowIndex > PAGE_SIZE - 1) {
					rowIndex = rowIndex - index;
				}
				if (rowIndex != 0) {
					if (cellTableCssStyle.get(rowIndex) == null) {
						if (even) {
							if (rowObject.getOrgId().equalsIgnoreCase(
									listOfAllAuthor.get(rowIndex - 1)
											.getOrgId())) {
								even = true;
								cellTableCssStyle
										.add(rowIndex, cellTableOddRow);
								return cellTableOddRow;
							} else {
								even = false;
								cellTableCssStyle.add(rowIndex,
										cellTableEvenRow);
								return cellTableEvenRow;
							}
						} else {
							if (rowObject.getOrgId().equalsIgnoreCase(
									listOfAllAuthor.get(rowIndex - 1)
											.getOrgId())) {
								even = false;
								cellTableCssStyle.add(rowIndex,
										cellTableEvenRow);
								return cellTableEvenRow;
							} else {
								even = true;
								cellTableCssStyle
										.add(rowIndex, cellTableOddRow);
								return cellTableOddRow;
							}
						}
					} else {
						return cellTableCssStyle.get(rowIndex);
					}
				} else {
					if (cellTableCssStyle.get(rowIndex) == null) {
						even = true;
						cellTableCssStyle.add(rowIndex, cellTableOddRow);
						return cellTableOddRow;
					} else {
						return cellTableCssStyle.get(rowIndex);
					}
				}
			}
		});
	}

	/**
	 * Gets the author selected list.
	 *
	 * @return the authorSelectedList
	 */
	@Override
	public List<Author> getAuthorSelectedList() {
		return authorSelectedList;
	}

	/**
	 * Sets the author selected list.
	 *
	 * @param authorSelectedList the authorSelectedList to set
	 */
	@Override
	public void setAuthorSelectedList(List<Author> authorSelectedList) {
		this.authorSelectedList = authorSelectedList;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.AddEditAuthorsDisplay#getAddToMeasureDeveloperListBtn()
	 */
	@Override
	public Button getAddToMeasureDeveloperListBtn() {
		return addToMeasureDeveloperList;
	}

	/**
	 * Gets the adds the to measure developer list.
	 *
	 * @return the addToMeasureDeveloperList
	 */
	public Button getAddToMeasureDeveloperList() {
		return addToMeasureDeveloperList;
	}

	/**
	 * Sets the adds the to measure developer list.
	 *
	 * @param addToMeasureDeveloperList the addToMeasureDeveloperList to set
	 */
	public void setAddToMeasureDeveloperList(Button addToMeasureDeveloperList) {
		this.addToMeasureDeveloperList = addToMeasureDeveloperList;
	}

	/**
	 * Gets the adds the button.
	 *
	 * @return the addButton
	 */
	@Override
	public Button getAddButton() {
		return addButton;
	}

	/**
	 * Gets the measure dev input.
	 *
	 * @return the measureDevInput
	 */
	@Override
	public TextBox getMeasureDevInput() {
		return measureDevInput;
	}

	/**
	 * Sets the measure dev input.
	 *
	 * @param measureDevInput the measureDevInput to set
	 */
	@Override
	public void setMeasureDevInput(TextBox measureDevInput) {
		this.measureDevInput = measureDevInput;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.AddEditAuthorsDisplay#getListOfAllAuthor()
	 */
	@Override
	public List<Author> getListOfAllAuthor() {
		// TODO Auto-generated method stub
		return listOfAllAuthor;
	}

	/**
	 * Gets the adds the edit cancel button.
	 *
	 * @return the addEditcancelButton
	 */
	@Override
	public Button getAddEditCancelButton() {
		return addEditcancelButton;
	}

	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.AddEditAuthorsDisplay#getSuccessMessageDisplay()
	 */
	@Override
	public SuccessMessageDisplay getSuccessMessageDisplay() {
		return successMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.AddEditAuthorsDisplay#getReturnButton()
	 */
	@Override
	public HasClickHandlers getReturnButton() {
		// TODO Auto-generated method stub
		return returnButton;
	}
	
}
