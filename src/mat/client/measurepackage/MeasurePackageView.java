package mat.client.measurepackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mat.client.ImageResources;
import mat.client.measure.metadata.Grid508;
import mat.client.measurepackage.MeasurePackagePresenter.MeasurePackageSelectionHandler;
import mat.client.shared.CustomButton;
import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatContext;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


public class MeasurePackageView implements MeasurePackagePresenter.View {
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private ErrorMessageDisplay qdmErrorMessages = new ErrorMessageDisplay();
	private ErrorMessageDisplay measureErrorMessages = new ErrorMessageDisplay();
	private SuccessMessageDisplay packageSuccessMessages = new SuccessMessageDisplay();
	private SuccessMessageDisplay suppDataSuccessMessages = new SuccessMessageDisplay();
	private SuccessMessageDisplay measurePackageSuccessMsg = new SuccessMessageDisplay();
	private FlowPanel content = new FlowPanel();
	private Label packageName = new Label();
	private ListBox clausesListBox = new ListBox();
	private ListBox packagedClausesListBox = new ListBox();
	private PrimaryButton addClausesToPackage = new PrimaryButton("Save Grouping","primaryButton");
	private Grid packagesTable = new Grid508();
	private PrimaryButton packageMeasure = new PrimaryButton("Create Measure Package","primaryButton");
	private PrimaryButton createNew = new PrimaryButton("Create New Grouping");
	
	private FocusPanel addClauseRight = buildAddButton(ImageResources.INSTANCE.addRight());
	private FocusPanel addClauseLeft = buildAddButton(ImageResources.INSTANCE.addLeft());
	private FocusPanel addAllClauseRight = buildDoubleAddButton(ImageResources.INSTANCE.addAllRight());
	private FocusPanel addAllClauseLeft = buildDoubleAddButton(ImageResources.INSTANCE.addAllLeft());
	private FocusPanel addQDMRight = buildAddButton(ImageResources.INSTANCE.addRight());
	private FocusPanel addQDMLeft = buildAddButton(ImageResources.INSTANCE.addLeft());
	private FocusPanel addAllQDMRight = buildDoubleAddButton(ImageResources.INSTANCE.addAllRight());
	private FocusPanel addAllQDMLeft = buildDoubleAddButton(ImageResources.INSTANCE.addAllLeft());
	private Widget addClauseButtonPanel = buildClauseAddButtonWidget(); 
	private MeasurePackageSelectionHandler editHandler;
	private MeasurePackageSelectionHandler deleteHandler;
	private Map<String, MeasurePackageClauseDetail> clauseIdMap = new HashMap<String,MeasurePackageClauseDetail>();
	
	private FlowPanel clausesPanel = new FlowPanel();
	private FlowPanel packagedPanel = new FlowPanel();
	private Label viewOrEditLabel = new Label();
	private DateBoxWithCalendar valueSetDateInput = new DateBoxWithCalendar();
	
	//MatTabLayoutPanel packageTabPanel;
	private ListBox qdmElementsListBox = new ListBox();
	private ListBox suppElementsListBox = new ListBox();
	private Map<String, QualityDataSetDTO> qdmElementIdMap = new HashMap<String,QualityDataSetDTO>();
	private Widget addQDMElementButtonPanel = buildQDMElementAddButtonWidget();
	private FlowPanel qdmElementsPanel = new FlowPanel();
	private FlowPanel suppElementsPanel = new FlowPanel();
	private PrimaryButton addQDMElementsToMeasure = new PrimaryButton("Save Supplemental Data Elements","primaryButton");
	private Label qdmTabName = new Label("Supplemental Data Elements");
	private Label valueSetPackageDateNote = new Label("The Value Set Package Date applies to all QDM elements used in the measure logic. Only the most recently saved value sets that are closest to, but do not occur after the Value Set Package Date entered, will be included in the Value Set Export file. Value Sets in Draft mode will NOT be included in the Value Set Export file.");
	//int currentPackageTab = 1;
	
	@Override
	public void setViewIsEditable(boolean b, List<MeasurePackageDetail> packages) {
		/*MatContext.get().setVisible(addToPackage,b);
		MatContext.get().setVisible(packageMeasure,b);
		MatContext.get().setVisible(createNew,b);
		MatContext.get().setVisible(addButtonPanel,b);
		MatContext.get().setVisible(clausesPanel,b);
		*/
		packageMeasure.setEnabled(b);
		if(ConstantMessages.CONTINUOUS_VARIABLE_SCORING.equals(MatContext.get().getCurrentMeasureScoringType()) && packages.size() > 0){
			createNew.setEnabled(false);
		}else{
			createNew.setEnabled(b);
		}
		addClausesToPackage.setEnabled(b);
		addClauseButtonPanel.setVisible(b);
		addQDMElementsToMeasure.setEnabled(b);
		clausesPanel.setVisible(b);
		qdmElementsPanel.setVisible(b);
		if(b) {
			viewOrEditLabel.setText("Edit");
		}		
		else {
			viewOrEditLabel.setText("View");
		}
		if(!b) {
			packagesTable.resizeColumns(2);
		}
		
	}
	public MeasurePackageView() {
		//packageTabPanel = new MatTabLayoutPanel(false);
		addClauseLeftRightClickHandlers();
		addQDMElementLeftRightClickHandlers();
		DOM.setElementAttribute(content.getElement(), "id", "MeasurePackageView.content");
		
		content.setStylePrimaryName("searchResultsContainer");
		content.addStyleName("leftAligned");
		Panel topClauseContainer = buildClauseLeftRightPanel();
		Panel topQDMElementContainer = buildQDMElementLeftRightPanel();
		
		//packageName.addStyleName("bold");
		//packageTabPanel.add(topClauseContainer, packageName);
		//packageName.setPixelSize(150, 20);
		//qdmTabName.addStyleName("bold");
		//packageTabPanel.add(topQDMElementContainer, qdmTabName);
		//qdmTabName.setPixelSize(100, 20);

		//MatContext.get().setAriaHidden(topQDMElementContainer, true);//set clause library to Aria-hidden true when it is initially added.  
		/*packageTabPanel.forceSelectTab(0);
		packageTabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				int selectedTab = event.getSelectedItem().intValue();
				if(currentPackageTab != selectedTab){
					setAriaHiddenTrueForClosingTab(currentPackageTab);
		    		MatContext.get().setAriaHidden(packageTabPanel.getWidget(selectedTab),false);//set Aria-hidden false when the tab has been selected.
		    		currentPackageTab = selectedTab;
		    		packageTabPanel.selectTab(selectedTab);
				}
			}
		});*/
		

		content.add(measureErrorMessages);
		content.add(SkipListBuilder.buildEmbeddedLinkHolder("MeasurePackage"));
		content.add(packagesTable);
		content.add(new SpacerWidget());
		content.add(createNew);
		content.add(new SpacerWidget());
		content.add(topClauseContainer);
		content.add(topQDMElementContainer);
		//content.add(packageTabPanel);
		
		HorizontalPanel valueSetDatePanel = new HorizontalPanel();
		valueSetDatePanel.add(valueSetDateInput);
		valueSetDateInput.addStyleName("pad-right5px");
		content.add(LabelBuilder.buildRequiredLabel(valueSetDateInput, "Value Set Package Date"));
		valueSetPackageDateNote.addStyleName("pad-left21px");
		valueSetDatePanel.add(valueSetPackageDateNote);	
		content.add(valueSetDatePanel);
		content.add(new SpacerWidget());
		
		content.add(measurePackageSuccessMsg);
		packageMeasure.setTitle("Create Measure Package");
		content.add(packageMeasure);
		
		clausesListBox.setVisibleItemCount(10);
		packagedClausesListBox.setVisibleItemCount(10);
		
		qdmElementsListBox.setVisibleItemCount(10);
		suppElementsListBox.setVisibleItemCount(10);
		
		packagesTable.setStylePrimaryName("searchResultsTable");
		content.setStyleName("contentPanel");
	}
	private void addClauseLeftRightClickHandlers() {

		addClauseRight.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addClauseRight();
			}
		});
		addClauseLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addClauseLeft();
			}
		});
		addAllClauseRight.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addAllClausesRight();
			}
		});
		addAllClauseLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addAllClausesLeft();
			}
		});
	}
	// QDM elements
	private void addQDMElementLeftRightClickHandlers() {

		addQDMRight.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addQDMElementRight();
			}
		});
		addQDMLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addQDMElementLeft();
			}
		});
		addAllQDMRight.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addAllQDMElementsRight();
			}
		});
		addAllQDMLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addAllQDMElementsLeft();
			}
		});
	}
	private Panel buildClauseLeftRightPanel() {
		FlowPanel topContainer = new FlowPanel();
		packageName.addStyleName("bold");
		topContainer.add(packageName);

		topContainer.add(errorMessages);
		topContainer.add(packageSuccessMessages);
		clausesPanel.addStyleName("column");
		packagedPanel.addStyleName("column");
		addClauseButtonPanel.addStyleName("column");
		Widget clausesLabel = LabelBuilder.buildLabel(clausesListBox, "Clauses");
		clausesLabel.addStyleName("bold");
		clausesPanel.add(clausesLabel);
		clausesPanel.add(clausesListBox);
		Widget groupingLabel = LabelBuilder.buildLabel(packagedClausesListBox, "Package Grouping");
		groupingLabel.addStyleName("bold");
		packagedPanel.add(groupingLabel);
		packagedPanel.add(packagedClausesListBox);
		
		SimplePanel wrapper = new SimplePanel();
		wrapper.setStylePrimaryName("measurePackageAddButtonHolder");
		wrapper.add(addClausesToPackage);
		
		packagedPanel.add(new SpacerWidget());
		packagedPanel.add(wrapper);
		topContainer.add(clausesPanel);
		topContainer.add(addClauseButtonPanel);
		topContainer.add(packagedPanel);
		SpacerWidget spacer = new SpacerWidget();
		spacer.setStylePrimaryName("clearBoth");
		topContainer.add(spacer);
		topContainer.setStylePrimaryName("measurePackageLeftRightPanel");
		return topContainer;
	}
	// QDM elements
	private Panel buildQDMElementLeftRightPanel() {
		FlowPanel qdmTopContainer = new FlowPanel();
		qdmTabName.addStyleName("bold");
		qdmTopContainer.add(qdmTabName);
		
		qdmTopContainer.add(qdmErrorMessages);
		qdmTopContainer.add(suppDataSuccessMessages);
		qdmElementsPanel.addStyleName("column");
		suppElementsPanel.addStyleName("column");
		addQDMElementButtonPanel.addStyleName("column");
		Widget qdmElementsLabel = LabelBuilder.buildLabel(qdmElementsListBox, "QDM Elements");
		qdmElementsLabel.addStyleName("bold");
		qdmElementsPanel.add(qdmElementsLabel);
		qdmElementsPanel.add(qdmElementsListBox);
		Widget suppElementsLabel = LabelBuilder.buildLabel(suppElementsListBox, "Supplemental Data Elements");
		suppElementsLabel.addStyleName("bold");
		suppElementsPanel.add(suppElementsLabel);
		suppElementsPanel.add(suppElementsListBox);
		
		SimplePanel wrapper = new SimplePanel();
		wrapper.setStylePrimaryName("measurePackageAddButtonHolder");
		wrapper.add(addQDMElementsToMeasure);
		
		suppElementsPanel.add(new SpacerWidget());
		suppElementsPanel.add(wrapper);
		qdmTopContainer.add(qdmElementsPanel);
		qdmTopContainer.add(addQDMElementButtonPanel);
		qdmTopContainer.add(suppElementsPanel);
		SpacerWidget spacer = new SpacerWidget();
		spacer.setStylePrimaryName("clearBoth");
		qdmTopContainer.add(spacer);
		qdmTopContainer.setStylePrimaryName("measurePackageLeftRightPanel");
		return qdmTopContainer;
	}
	
	private FocusPanel buildAddButton(ImageResource imageUrl) {
		FocusPanel fPanel = new FocusPanel();
		fPanel.add(new Image(imageUrl));
		fPanel.setStylePrimaryName("greySecondaryButton");
		fPanel.addStyleName("measurePackageAddButton");
		return fPanel;
	}
	private FocusPanel buildDoubleAddButton(ImageResource imageUrl) {
		FocusPanel focusPanel = new FocusPanel();
		FlowPanel fPanel = new FlowPanel();
		fPanel.add(new Image(imageUrl));
		fPanel.add(new Image(imageUrl));
		focusPanel.add(fPanel);
		focusPanel.setStylePrimaryName("greySecondaryButton");
		focusPanel.addStyleName("measurePackageAddButton");
		return focusPanel;
	}
	private Widget buildClauseAddButtonWidget() {
		FlowPanel panel = new FlowPanel();
		panel.setStyleName("measurePackageAddButtonPanel");
		addClauseRight.setTitle("Add clause to Measure Grouping");
		addClauseRight.getElement().setAttribute("alt", "Add clause to Measure Grouping");
		addClauseLeft.setTitle("Remove clause from Measure Grouping");
		addClauseLeft.getElement().setAttribute("alt", "Remove clause from Measure Grouping");
		addAllClauseRight.setTitle("Add all clauses to Measure Grouping");
		addAllClauseRight.getElement().setAttribute("alt", "Add all clauses to Measure Grouping");
		addAllClauseLeft.setTitle("Remove all clauses from Measure Grouping");
		addAllClauseLeft.getElement().setAttribute("alt", "Remove all clauses from Measure Grouping");
		panel.add(addClauseRight);
		panel.add(addClauseLeft);
		panel.add(new SpacerWidget());
		panel.add(addAllClauseRight);
		panel.add(addAllClauseLeft);
		return panel;
	}
	// QDM elements
	private Widget buildQDMElementAddButtonWidget() {
		FlowPanel panel = new FlowPanel();
		panel.setStyleName("measurePackageAddButtonPanel");
		addQDMRight.setTitle("Add QDM element to Supplemental Data Elements");
		addQDMRight.getElement().setAttribute("alt", "Add QDM element to Supplemental Data Elements");
		addQDMLeft.setTitle("Remove QDM Element from Supplemental Data Elements");
		addQDMLeft.getElement().setAttribute("alt", "Remove QDM Element from Supplemental Data Elements");
		addAllQDMRight.setTitle("Add all QDM Elements to Supplemental Data Elements");
		addAllQDMRight.getElement().setAttribute("alt", "Add all QDM Elements to Supplemental Data Elements");
		addAllQDMLeft.setTitle("Remove all QDM Elements from Supplemental Data Elements");
		addAllQDMLeft.getElement().setAttribute("alt", "Remove all QDM Elements from Supplemental Data Elements");
		panel.add(addQDMRight);
		panel.add(addQDMLeft);
		panel.add(new SpacerWidget());
		panel.add(addAllQDMRight);
		panel.add(addAllQDMLeft);
		return panel;
	}
		
	@Override
	public void setClausesInPackage(List<MeasurePackageClauseDetail> clauses) {
		setClauseItems(packagedClausesListBox, clauses);
	}
	
	// QDM elements list
	@Override
	public void setQDMElementsInSuppElements(List<QualityDataSetDTO> clauses) {
		setQDMElementsItems(suppElementsListBox, clauses);
	}
	
	private void setClauseItems(ListBox lb, List<MeasurePackageClauseDetail> valuesArg) {
		List<MeasurePackageClauseDetail> values = new ArrayList<MeasurePackageClauseDetail>();
		values.addAll(valuesArg);
		Collections.sort(values);
		
		lb.clear();
		for(MeasurePackageClauseDetail nvp : values) {
			lb.addItem(nvp.getName(), nvp.getId());
			clauseIdMap.put(nvp.getId(), nvp);
		}
	}
	// QDM elements
	private void setQDMElementsItems(ListBox lb, List<QualityDataSetDTO> valuesArg) {
		List<QualityDataSetDTO> values = new ArrayList<QualityDataSetDTO>();
		values.addAll(valuesArg);
		Collections.sort(values, new QualityDataSetDTO.Comparator());
		
		lb.clear();
		for(QualityDataSetDTO nvp : values) {
			lb.addItem(nvp.getQDMElement(), nvp.getId());
			qdmElementIdMap.put(nvp.getId(), nvp);
		}
	}
	
	private void addClauseItem(ListBox lb, MeasurePackageClauseDetail nvp) {
		List<MeasurePackageClauseDetail> list = getClauseItems(lb);
		list.add(nvp);
		Collections.sort(list);
		setClauseItems(lb, list);
	}
	
	private void addClauseItems(ListBox lb, List<MeasurePackageClauseDetail> nvpList) {
		List<MeasurePackageClauseDetail> list = getClauseItems(lb);
		list.addAll(nvpList);
		setClauseItems(lb, list);
	}
	
	// QDM elements
	private void addQDMElementItem(ListBox lb, QualityDataSetDTO nvp) {
		List<QualityDataSetDTO> list = getQDMElementsItems(lb);
		list.add(nvp);
		Collections.sort(list, new QualityDataSetDTO.Comparator());
		setQDMElementsItems(lb, list);
	}
	// QDM elements
	private void addQDMElementItems(ListBox lb, List<QualityDataSetDTO> nvpList) {
		List<QualityDataSetDTO> list = getQDMElementsItems(lb);
		list.addAll(nvpList);
		setQDMElementsItems(lb, list);
	}
	
	private MeasurePackageClauseDetail getClauseSelectedValue(ListBox lb) {
		int index = lb.getSelectedIndex();
		MeasurePackageClauseDetail nvp = null;
		if(index >= 0) {
			nvp = clauseIdMap.get(lb.getValue(index));
		}
		return nvp;
	}
	// QDM elements
	private QualityDataSetDTO getQDMElementSelectedValue(ListBox lb) {
		int index = lb.getSelectedIndex();
		QualityDataSetDTO nvp = null;
		if(index >= 0) {
			nvp = qdmElementIdMap.get(lb.getValue(index));
		}
		return nvp;
	}
	
	private void addClauseRight() {
		MeasurePackageClauseDetail nvp = getClauseSelectedValue(clausesListBox);
		if(nvp != null) {
			removeClauseItem(clausesListBox, nvp);
			addClauseItem(packagedClausesListBox, nvp);
		}
	}
	private void addClauseLeft() {
		MeasurePackageClauseDetail nvp = getClauseSelectedValue(packagedClausesListBox);
		if(nvp != null) {
			removeClauseItem(packagedClausesListBox, nvp);
			addClauseItem(clausesListBox, nvp);
		}
	}
	private void addAllClausesRight() {
		addClauseItems(packagedClausesListBox, getClauseItems(clausesListBox));
		clausesListBox.clear();
	}
	private void addAllClausesLeft() {
		addClauseItems(clausesListBox, getClauseItems(packagedClausesListBox));
		packagedClausesListBox.clear();
	}
	// QDM elements
	private void addQDMElementRight() {
		QualityDataSetDTO nvp = getQDMElementSelectedValue(qdmElementsListBox);
		if(nvp != null) {
			removeQDMElementItem(qdmElementsListBox, nvp);
			addQDMElementItem(suppElementsListBox, nvp);
		}
	}
	private void addQDMElementLeft() {
		QualityDataSetDTO nvp = getQDMElementSelectedValue(suppElementsListBox);
		if(nvp != null) {
			removeQDMElementItem(suppElementsListBox, nvp);
			addQDMElementItem(qdmElementsListBox, nvp);
		}
	}
	private void addAllQDMElementsRight() {
		addQDMElementItems(suppElementsListBox, getQDMElementsItems(qdmElementsListBox));
		qdmElementsListBox.clear();
	}
	private void addAllQDMElementsLeft() {
		addQDMElementItems(qdmElementsListBox, getQDMElementsItems(suppElementsListBox));
		suppElementsListBox.clear();
	}
	
	@Override
	public void setClauses(List<MeasurePackageClauseDetail> clauses) {
		setClauseItems(clausesListBox, clauses);
	}
	
	// QDM elements
	@Override
	public void setQDMElements(List<QualityDataSetDTO> clauses) {
		setQDMElementsItems(qdmElementsListBox, clauses);
	}
	
	private List<MeasurePackageClauseDetail> getClauseItems(ListBox lb) {
		List<MeasurePackageClauseDetail> list = new ArrayList<MeasurePackageClauseDetail>();
		for(int i = 0; i < lb.getItemCount(); i++) {
			MeasurePackageClauseDetail detail = clauseIdMap.get(lb.getValue(i));
			list.add(detail);
		}
		return list;
	}
	
	private void removeClauseItem(ListBox lb, MeasurePackageClauseDetail nvp) {
		for(int i = 0; i < lb.getItemCount(); i++) {
			if(lb.getValue(i).equals(nvp.getId())) {
				lb.removeItem(i);
				break;
			}
		}
	}
	// QDM elements
	private List<QualityDataSetDTO> getQDMElementsItems(ListBox lb) {
		List<QualityDataSetDTO> list = new ArrayList<QualityDataSetDTO>();
		for(int i = 0; i < lb.getItemCount(); i++) {
			QualityDataSetDTO detail = qdmElementIdMap.get(lb.getValue(i));
			list.add(detail);
		}
		return list;
	}
	// QDM elements
	private void removeQDMElementItem(ListBox lb, QualityDataSetDTO nvp) {
		for(int i = 0; i < lb.getItemCount(); i++) {
			if(lb.getValue(i).equals(nvp.getId())) {
				lb.removeItem(i);
				break;
			}
		}
	}
	
	@Override
	public List<MeasurePackageClauseDetail> getClausesInPackage() {
		return getClauseItems(packagedClausesListBox);
	}
	// QDM elements
	@Override
	public List<QualityDataSetDTO> getQDMElements() {
		return getQDMElementsItems(qdmElementsListBox);
	}
	@Override
	public List<QualityDataSetDTO> getQDMElementsInSuppElements() {
		return getQDMElementsItems(suppElementsListBox);
	}
	
	@Override
	public HasClickHandlers getAddClausesToPackageButton() {
		return addClausesToPackage;
	}
	// QDM elements
	@Override
	public HasClickHandlers getAddQDMElementsToMeasureButton() {
		return addQDMElementsToMeasure;
	}

	private void buildTableHeader(Grid table) {
		table.getRowFormatter().addStyleName(0, "header");
		table.setText(0, 0, "Measure Grouping");
		table.setWidget(0, 1, viewOrEditLabel);
		table.setText(0, 2, "Delete");	
	}
	@Override
	public void setMeasurePackages(List<MeasurePackageDetail> packages) {
		if(packages.size() == 0) {
			MatContext.get().setVisible(packagesTable,false);
		}
		else {
			MatContext.get().setVisible(packagesTable,true);
			packagesTable.resize(packages.size() + 1, 3);

			buildTableHeader(packagesTable);
								
			for(int i = 0; i < packages.size(); i++) {
				final MeasurePackageDetail nvp = packages.get(i);
				packagesTable.setText(i+1, 0, nvp.getPackageName());
				//FocusableImageButton editImage = new FocusableImageButton(ImageResources.INSTANCE.g_package_edit(),"edit");
				CustomButton editImage = new CustomButton();
				editImage.removeStyleName("gwt-button");
				editImage.setStylePrimaryName("invisibleButtonText");
				editImage.setResource(ImageResources.INSTANCE.g_package_edit(), "edit");
				editImage.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if(editHandler != null) {
							editHandler.onSelection(nvp);
						}
					}
				});
				packagesTable.setWidget(i+1, 1, createImageHolder(editImage));
				
				//FocusableImageButton deleteImage = new FocusableImageButton(ImageResources.INSTANCE.g_delete(),"delete");
				CustomButton deleteImage = new CustomButton();
				deleteImage.removeStyleName("gwt-button");
				deleteImage.setStylePrimaryName("invisibleButtonText");
				deleteImage.setResource(ImageResources.INSTANCE.g_delete(),"delete");
				deleteImage.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if(deleteHandler != null) {
							deleteHandler.onSelection(nvp);
						}
					}
				});
				packagesTable.setWidget(i+1, 2, createImageHolder(deleteImage));
				
				if(i % 2 == 0) {
					packagesTable.getRowFormatter().addStyleName(i + 1, "odd");
				}
			}
		}
	}
	
	private Panel createImageHolder(CustomButton image) {
		image.setStylePrimaryName("invisibleButtonText");
		SimplePanel holder = new SimplePanel();
		holder.add(image);
		//holder.setStyleName("invisibleButtonText");
		return holder;
	}
	@Override
	public HasClickHandlers getPackageMeasureButton() {
		return packageMeasure;
	}
	@Override
	public Widget asWidget() {
		return content;
	}

	@Override
	public ErrorMessageDisplayInterface getPackageErrorMessageDisplay() {
		return errorMessages;
	}
	
	@Override
	public ErrorMessageDisplayInterface getQDMErrorMessageDisplay() {
		return qdmErrorMessages;
	}

	@Override
	public HasClickHandlers getCreateNewButton() {
		return createNew;
	}

	@Override
	public void setPackageName(String name) {
		packageName.setText(name);
	}

	@Override
	public void setSelectionHandler(MeasurePackageSelectionHandler handler) {
		this.editHandler = handler;
	}
	
	@Override
	public void setDeletionHandler(MeasurePackageSelectionHandler handler) {
		this.deleteHandler = handler;
	}
	
	@Override
	public SuccessMessageDisplayInterface getPackageSuccessMessageDisplay() {
		return packageSuccessMessages;
	}
	@Override
	public SuccessMessageDisplayInterface getSuppDataSuccessMessageDisplay() {
		return suppDataSuccessMessages;
	}
	@Override
	public ErrorMessageDisplayInterface getMeasureErrorMessageDisplay() {
		return measureErrorMessages;
	}
	@Override
	public SuccessMessageDisplayInterface getMeasurePackageSuccessMsg() {
		return measurePackageSuccessMsg;
	}
	
	@Override
	public String getValuesSetDate() {
		return valueSetDateInput.getValue();
	}
	@Override
	public void setValuesSetDate(String date) {
		valueSetDateInput.setValue(date);
	}
	
	@Override
	public DateBoxWithCalendar getValuesSetDateInputBox() {
		return valueSetDateInput;
	}
	
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return measureErrorMessages;
	}
	
	/*private void setAriaHiddenTrueForClosingTab(int deselectedIndex) {
		MatContext.get().setAriaHidden(packageTabPanel.getWidget(deselectedIndex),true);
	}*/
	
	/*@Override
	public void setTabIndex() {
		packageTabPanel.forceSelectTab(0);;
	}*/
	
}
