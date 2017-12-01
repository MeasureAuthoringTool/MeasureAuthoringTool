package mat.client.clause.cqlworkspace;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.clauseworkspace.model.SortedClauseMapResult;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.MatContext;

import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

/**
 * The Class CQLPopulationWorkSpacePresenter.
 */
public class CQLPopulationWorkSpacePresenter implements MatPresenter {

	/** The panel. */
	private SimplePanel panel = new SimplePanel();

	/** The clicked menu. */
	private String currentSection = CQLWorkSpaceConstants.CQL_VIEWPOPULATIONS;
	/** The next clicked menu. */
	private String nextSection = CQLWorkSpaceConstants.CQL_VIEWPOPULATIONS;

	/** The search display. */
	private ViewDisplay searchDisplay;

	/** The service. */
	private MeasureServiceAsync service = MatContext.get().getMeasureService();

	/**
	 * The Interface ViewDisplay.
	 */
	public static interface ViewDisplay {

		/**
		 * Top Main panel of CQL Workspace Tab.
		 * 
		 * @return HorizontalPanel
		 */
		VerticalPanel getMainPanel();

		/**
		 * Generates View for CQLWorkSpace tab.
		 * @param document 
		 */
		void buildView(Document document, PopulationDataModel populationDataModel);

			/**
		 * Gets the main v panel.
		 *
		 * @return the main v panel
		 */
		Widget asWidget();

		/**
		 * Gets the clicked menu.
		 *
		 * @return the clicked menuF
		 */
		String getClickedMenu();

		/**
		 * Sets the clicked menu.
		 *
		 * @param clickedMenu
		 *            the new clicked menu
		 */
		void setClickedMenu(String clickedMenu);

		/**
		 * Gets the main h panel.
		 *
		 * @return the main h panel
		 */
		HorizontalPanel getMainHPanel();

		/**
		 * Sets the next clicked menu.
		 *
		 * @param nextClickedMenu
		 *            the new next clicked menu
		 */
		void setNextClickedMenu(String nextClickedMenu);

		/**
		 * Gets the next clicked menu.
		 *
		 * @return the next clicked menu
		 */
		Object getNextClickedMenu();

	
		/**
		 * Gets the main flow panel.
		 *
		 * @return the main flow panel
		 */
		FlowPanel getMainFlowPanel();

		/**
		 * Gets the cql left nav bar panel view.
		 *
		 * @return the cql left nav bar panel view
		 */
		CQLPopulationLeftNavBarPanelView getCqlLeftNavBarPanelView();

		
		/**
		 * Reset message display.
		 */
		void resetMessageDisplay();

		/**
		 * Reset all.
		 */
		void resetAll();

		void setHeadingBasedOnCurrentSection(String headingText, String panelId);

		void displayInitialPopulations();

		void displayNumerators();
		CQLViewPopulationsDisplay getCqlViewPopulationsDisplay();

		void buildViewPopulations();

		void displayDenominator();

		void displayDenominatorExceptions();

		void displayDenominatorExclusions();

		void displayNumeratorExclusion();

		void displayMeasurePopulations();

		void displayMeasurePopulationsExclusions();

		void displayMeasureObservations();

		void displayStratification();
	}

	/**
	 * Instantiates a new CQL presenter nav bar with list.
	 *
	 * @param srchDisplay
	 *            the srch display
	 */
	public CQLPopulationWorkSpacePresenter(final ViewDisplay srchDisplay) {
		searchDisplay = srchDisplay;
	}

	/**
	 * Method to Unset current Left Nav section and set next selected section
	 * when user clicks yes on warning message (Dirty Check).
	 */
	private void changeSectionSelection() {
	
		// Unset current selected section.
		switch (currentSection) {
		/*case (CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
			setActiveMenuItem(currentSection, false);
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setActive(false);
		break;
		case (CQLWorkSpaceConstants.CQL_APPLIED_QDM):
			setActiveMenuItem(currentSection, false);
		searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().setActive(false);
		break;
		case (CQLWorkSpaceConstants.CQL_CODES):
			setActiveMenuItem(currentSection, false);
		searchDisplay.getCqlLeftNavBarPanelView().getCodesLibrary().setActive(false);
		break;
		case (CQLWorkSpaceConstants.CQL_FUNCTION_MENU):
			setActiveMenuItem(currentSection, false);
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setActive(false);
		break;
		case (CQLWorkSpaceConstants.CQL_PARAMETER_MENU):
			setActiveMenuItem(currentSection, false);
		searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().setActive(false);
		break;
		case (CQLWorkSpaceConstants.CQL_DEFINE_MENU):
			setActiveMenuItem(currentSection, false);
		searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setActive(false);
		break;
		case (CQLWorkSpaceConstants.CQL_GENERAL_MENU):
			setActiveMenuItem(currentSection, false);
		searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setActive(false);
		break;
		case (CQLWorkSpaceConstants.CQL_VIEW_MENU):
			setActiveMenuItem(currentSection, false);
		searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().setActive(false);
		break;*/
		default:
			break;
		}
		// Set Next Selected Section.
		switch (nextSection) {
		case (CQLWorkSpaceConstants.CQL_INITIALPOPULATION):
			currentSection = nextSection;
		initialPopulationEvent();
		break;
		
		default:
			break;
		}
	}

	
	/**
	 * Before closing display.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		panel.clear();
		
	}

	/**
	 * Before display.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		currentSection = CQLWorkSpaceConstants.CQL_VIEWPOPULATIONS;
		nextSection = CQLWorkSpaceConstants.CQL_VIEWPOPULATIONS;

		final String currentMeasureId = MatContext.get().getCurrentMeasureId();
		if ((currentMeasureId != null) && !"".equals(currentMeasureId)) {
			service.getMeasureXmlForMeasureAndSortedSubTreeMap(currentMeasureId,
					new AsyncCallback<SortedClauseMapResult>() { // Loading the measure's SimpleXML from the Measure_XML table
				@Override
				public void onSuccess(SortedClauseMapResult result) {
					buildPopulationWorkspace(result);
				}

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}});	
			
			MeasureComposerPresenter.setSubSkipEmbeddedLink("CQLPopulationWorkspaceView.containerPanel");
			Mat.focusSkipLists("MeasureComposer");
			
		}
	}

	private void buildPopulationWorkspace(
			SortedClauseMapResult result) {
		try {
			String xml = result != null ? result.getMeasureXmlModel().getXml() : null;
			Document document = XMLParser.parse(xml);
			
			//create a Populations Data model object from the Measure XML.
			PopulationDataModel populationDataModel = new PopulationDataModel(document);
			
			searchDisplay.buildView(document, populationDataModel);
			addLeftNavEventHandler();
			searchDisplay.resetMessageDisplay();
			panel.add(searchDisplay.asWidget());
			viewPopulationsEvent();
		}catch(Exception e){
			e.printStackTrace();
		}		
	}	

	/**
	 * Adding handlers for Anchor Items.
	 */
	private void addLeftNavEventHandler() {

		searchDisplay.getCqlLeftNavBarPanelView().getInitialPopulation().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				initialPopulationEvent();
			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getNumerator().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				numeratorEvent();
			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getNumeratorExclusions().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				numeratorExclusionEvent();
			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getDenominator().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				denominatorEvent();
			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getDenominatorExclusions().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				denominatorExclusionsEvent();
			}
		});
		
		searchDisplay.getCqlLeftNavBarPanelView().getMeasurePopulations().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				measurePopulationsEvent();
			}
		});
		
		searchDisplay.getCqlLeftNavBarPanelView().getMeasurePopulationExclusions().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				measurePopulationExclusionsEvent();
			}
		});
		
		searchDisplay.getCqlLeftNavBarPanelView().getDenominatorExceptions().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				denominatorExceptionsEvent();
			}
		});
		
		searchDisplay.getCqlLeftNavBarPanelView().getStratifications().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				stratificationsEvent(); 
			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getMeasureObservations().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				measureObservationsEvent();
			}
		});

		searchDisplay.getCqlLeftNavBarPanelView().getViewPopulations().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				viewPopulationsEvent();
			}
		});

	}

	/**
	 * Build View for Initial Population when Initial Population AnchorList item is clicked.
	 */
	private void initialPopulationEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);

		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {

		} else {
			setActiveMenuItem(currentSection, false);
			setActiveMenuItem(CQLWorkSpaceConstants.CQL_INITIALPOPULATION, true);
			currentSection = CQLWorkSpaceConstants.CQL_INITIALPOPULATION;
			searchDisplay.displayInitialPopulations();
		}
		//searchDisplay.setHeadingBasedOnCurrentSection("Population Workspace > Initial Populations", "headingPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	/**
	 * Build View for Numerator when Numerator AnchorList item is clicked.
	 */
	private void numeratorEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
	
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {

		} else {
			setActiveMenuItem(currentSection, false);
			setActiveMenuItem(CQLWorkSpaceConstants.CQL_NUMERATOR, true);
			currentSection = CQLWorkSpaceConstants.CQL_NUMERATOR;
			searchDisplay.displayNumerators();
		}
		searchDisplay.setHeadingBasedOnCurrentSection("Population Workspace > Numerators", "headingPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	/**
	 * Build View for Numerator Exclusions when Numerator Exclusions AnchorList item is clicked.
	 */
	private void numeratorExclusionEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
	
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
	
		} else {
			setActiveMenuItem(currentSection, false);
			setActiveMenuItem(CQLWorkSpaceConstants.CQL_NUMERATOREXCLUSIONS, true);
			currentSection = CQLWorkSpaceConstants.CQL_NUMERATOREXCLUSIONS;
			searchDisplay.displayNumeratorExclusion();
		}
		searchDisplay.setHeadingBasedOnCurrentSection("Population Workspace > Numerator Exclusions", "headingPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	/**
	 * Build View for Denominator when Denominator AnchorList item is clicked.
	 */
	private void denominatorEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
	
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {

		} else {
			setActiveMenuItem(currentSection, false);
			setActiveMenuItem(CQLWorkSpaceConstants.CQL_DENOMINATOR, true);
			currentSection = CQLWorkSpaceConstants.CQL_DENOMINATOR;
			searchDisplay.displayDenominator();
		}
		searchDisplay.setHeadingBasedOnCurrentSection("Population Workspace > Denominators", "headingPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	/**
	 * Build View for Denominator Exclusions when Denominator Exclusions AnchorList item is clicked.
	 */
	private void denominatorExclusionsEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
		
		} else {
			setActiveMenuItem(currentSection, false);
			setActiveMenuItem(CQLWorkSpaceConstants.CQL_DENOMINATOREXCLUSIONS, true);
			currentSection = CQLWorkSpaceConstants.CQL_DENOMINATOREXCLUSIONS;
			searchDisplay.displayDenominatorExclusions();
		}
		searchDisplay.setHeadingBasedOnCurrentSection("Population Workspace > Denominator Exclusions", "headingPanel");
		Mat.focusSkipLists("MeasureComposer");
	}
	
	/**
	 * Build View for Denominator Exceptions when Denominator Exceptions AnchorList item is clicked.
	 */
	private void denominatorExceptionsEvent() {
		
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
		
		} else {
			setActiveMenuItem(currentSection, false);
			setActiveMenuItem(CQLWorkSpaceConstants.CQL_DENOMINATOREXCEPTIONS, true);
			currentSection = CQLWorkSpaceConstants.CQL_DENOMINATOREXCEPTIONS;
			searchDisplay.displayDenominatorExceptions();
		}
		searchDisplay.setHeadingBasedOnCurrentSection("Population Workspace > Denominator Exceptions", "headingPanel");
		Mat.focusSkipLists("MeasureComposer");
	}
	
	/**
	 * Build View for Measure Populations when Measure Populations AnchorList item is clicked.
	 */
	private void measurePopulationsEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
		
		} else {
			setActiveMenuItem(currentSection, false);
			setActiveMenuItem(CQLWorkSpaceConstants.CQL_MEASUREPOPULATIONS, true);
			currentSection = CQLWorkSpaceConstants.CQL_MEASUREPOPULATIONS;
			searchDisplay.displayMeasurePopulations();
		}
		searchDisplay.setHeadingBasedOnCurrentSection("Population Workspace > Measure Populations", "headingPanel");
		Mat.focusSkipLists("MeasureComposer");
	}
	
	/**
	 * Build View for Measure Populations Exclusions when Measure Populations Exclusions AnchorList item is clicked.
	 */
	private void measurePopulationExclusionsEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
		
		} else {
			setActiveMenuItem(currentSection, false);
			setActiveMenuItem(CQLWorkSpaceConstants.CQL_MEASUREPOPULATIONEXCLUSIONS, true);
			currentSection = CQLWorkSpaceConstants.CQL_MEASUREPOPULATIONEXCLUSIONS;
			searchDisplay.displayMeasurePopulationsExclusions();
		}
		searchDisplay.setHeadingBasedOnCurrentSection("Population Workspace > Measure Population Exclusions", "headingPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	/**
	 * Build View for Stratifications when General Info AnchorList item is clicked.
	 */
	private void stratificationsEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);

		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {

		} else {
			setActiveMenuItem(currentSection, false);
			setActiveMenuItem(CQLWorkSpaceConstants.CQL_STRATIFICATIONS, true);
			currentSection = CQLWorkSpaceConstants.CQL_STRATIFICATIONS;
			searchDisplay.displayStratification();
		}
		searchDisplay.setHeadingBasedOnCurrentSection("Population Workspace > Stratification", "headingPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	/**
	 * Build View for Measure Observations when General Info AnchorList item is clicked.
	 */
	private void measureObservationsEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
	
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
	
		} else {
			setActiveMenuItem(currentSection, false);
			setActiveMenuItem(CQLWorkSpaceConstants.CQL_MEASUREOBSERVATIONS, true); 
			currentSection = CQLWorkSpaceConstants.CQL_MEASUREOBSERVATIONS;
			searchDisplay.displayMeasureObservations();
		}
		searchDisplay.setHeadingBasedOnCurrentSection("Population Workspace > Measure Observations", "headingPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	/**
	 * Build View for View Populations when View Populations AnchorList item is clicked.
	 */
	private void viewPopulationsEvent() {
		searchDisplay.getCqlLeftNavBarPanelView().setIsNavBarClick(true);
		searchDisplay.getCqlLeftNavBarPanelView().setIsDoubleClick(false);
		
		if (searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
		
		} else {
			setActiveMenuItem(currentSection, false);
			setActiveMenuItem(CQLWorkSpaceConstants.CQL_VIEWPOPULATIONS, true); 
			currentSection = CQLWorkSpaceConstants.CQL_VIEWPOPULATIONS;
			searchDisplay.buildViewPopulations();
			
		}
		searchDisplay.setHeadingBasedOnCurrentSection("Population Workspace > View Populations", "headingPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	

	/**
	 * Method to unset Anchor List Item selection for previous selection and set
	 * for new selections.
	 *
	 * @param menuClickedBefore
	 *            the menu clicked before
	 */
	private void setActiveMenuItem(String menuClickedBefore, boolean isSet) {
		if (!searchDisplay.getCqlLeftNavBarPanelView().getIsPageDirty()) {
			searchDisplay.resetMessageDisplay();
			if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_INITIALPOPULATION)) {
				searchDisplay.getCqlLeftNavBarPanelView().getInitialPopulation().setActive(isSet);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_DENOMINATOR)) {
				searchDisplay.getCqlLeftNavBarPanelView().getDenominator().setActive(isSet);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_NUMERATOR)) {
				searchDisplay.getCqlLeftNavBarPanelView().getNumerator().setActive(isSet);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_NUMERATOREXCLUSIONS)) {
				searchDisplay.getCqlLeftNavBarPanelView().getNumeratorExclusions().setActive(isSet);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_DENOMINATOREXCLUSIONS)) {
				searchDisplay.getCqlLeftNavBarPanelView().getDenominatorExclusions().setActive(isSet);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_DENOMINATOREXCEPTIONS)) {
				searchDisplay.getCqlLeftNavBarPanelView().getDenominatorExceptions().setActive(isSet);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MEASUREPOPULATIONS)) {
				searchDisplay.getCqlLeftNavBarPanelView().getMeasurePopulations().setActive(isSet);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MEASUREPOPULATIONEXCLUSIONS)) {
				searchDisplay.getCqlLeftNavBarPanelView().getMeasurePopulationExclusions().setActive(isSet);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_STRATIFICATIONS)) {
				searchDisplay.getCqlLeftNavBarPanelView().getStratifications().setActive(isSet);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MEASUREOBSERVATIONS)) {
				searchDisplay.getCqlLeftNavBarPanelView().getMeasureObservations().setActive(isSet);
			} else if (menuClickedBefore.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_VIEWPOPULATIONS)) {
				searchDisplay.getCqlLeftNavBarPanelView().getViewPopulations().setActive(isSet);
			} 

		}
	}


	

	/**
	 * Gets the widget.
	 *
	 * @return the widget
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		panel.setStyleName("contentPanel");
		return panel;
	}

	

	/**
	 * returns the searchDisplay.
	 * 
	 * @return ViewDisplay.
	 */
	public ViewDisplay getSearchDisplay() {
		return searchDisplay;
	}

	


	/**
	 * Show searching busy.
	 *
	 * @param busy the busy
	 */
	private void showSearchingBusy(final boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
	
		}
		searchDisplay.getCqlLeftNavBarPanelView().setIsLoading(busy);

	}

	
}
