package mat.client.populationworkspace;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import mat.client.MainLayout;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.clauseworkspace.model.SortedClauseMapResult;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.populationworkspace.model.PopulationClauseObject;
import mat.client.populationworkspace.model.PopulationDataModel;
import mat.client.populationworkspace.model.PopulationsObject;
import mat.client.populationworkspace.model.StrataDataModel;
import mat.client.populationworkspace.model.StratificationsObject;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.MatContext;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.UUIDUtilClient;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import java.util.List;
import java.util.function.Predicate;

/**
 * The Class CQLPopulationWorkSpacePresenter.
 */
public class CQLPopulationWorkSpacePresenter implements MatPresenter {

	private static final String MEASURE_COMPOSER = "MeasureComposer";
	
	private static final String FUNC_AGG_FUNC_RQD_MSG = "Both an Aggregate Function and a User Defined Function are required for a measure observation.";

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
		 * 
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

		CQLViewPopulationsDisplay getCqlViewPopulationsDisplay();

		void buildViewPopulations();

		void displayMeasureObservations();

		void displayStratification();

		void displayPopulationDetailView(String populationType);

		void setObserver(CQLPopulationObserver cqlPopulationObserver);

		CQLStratificationDetailView getCqlStratificationDetailView();

		CQLMeasureObservationDetailView getCqlMeasureObservationDetailView();

		CQLPopulationDetail getCqlPopulationDetailView();

		void setCqlStratificationDetailView(CQLStratificationDetailView cqlStratificationDetailView);

		void setCqlMeasureObservationDetailView(CQLMeasureObservationDetailView cqlMeasureObservationDetailView);

		void setCqlPopulationDetailView(CQLPopulationDetail cqlPopulationDetailView);

	}

	/**
	 * Instantiates a new CQL presenter nav bar with list.
	 *
	 * @param srchDisplay
	 *            the srch display
	 */
	public CQLPopulationWorkSpacePresenter(final ViewDisplay srchDisplay) {
		searchDisplay = srchDisplay;
		addEventHandlers();
	}

	private void addEventHandlers() {
		addPopulationObserverEventHandlers();
		addWarningConfirmationHandlers();
	}

	/**
	 * CQLPopulationObserver Event Handlers.
	 */
	private void addPopulationObserverEventHandlers() {
		searchDisplay.setObserver(new CQLPopulationObserver() {
			
			private PopulationClauseObject buildStratum(int sequenceNumber) {
				PopulationClauseObject stratum = new PopulationClauseObject(UUIDUtilClient.uuid());
				stratum.setDisplayName(CQLWorkSpaceConstants.CQL_STRATUM + " " + sequenceNumber);
				stratum.setSequenceNumber(sequenceNumber);
				stratum.setType("stratum");
				return stratum;
			}
			
			@Override
			public void onViewHRClick(PopulationClauseObject population) {
				
				searchDisplay.resetMessageDisplay();
				
				if (population.getCqlExpressionDisplayName().isEmpty()) {
					showHumanReadableDialogBox("<html></html>", population.getDisplayName());
				} else {
					MatContext.get().getMeasureService().getHumanReadableForNode(MatContext.get().getCurrentMeasureId(),
							population.toXML(), new AsyncCallback<String>() {
						@Override
						public void onSuccess(String result) {
							showHumanReadableDialogBox(result, population.getDisplayName());
						}

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						}
					});
				}

			}

			@Override
			public void onSaveClick(PopulationsObject populationsObject) {

				searchDisplay.resetMessageDisplay();
				
				//MAT-9042. Validation Message for Aggregate function.
				if(funcOrAggFuncExistAlone(populationsObject.getPopulationClauseObjectList())) {
					searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(FUNC_AGG_FUNC_RQD_MSG);					
				} else {
					if (CQLWorkSpaceConstants.CQL_MEASUREOBSERVATIONS.equalsIgnoreCase(currentSection)) {
						searchDisplay.getCqlMeasureObservationDetailView().setIsDirty(false);					
					} else {
						searchDisplay.getCqlPopulationDetailView().setIsDirty(false);
					}

					MainLayout.showLoadingMessage();
					MatContext.get().getPopulationService().savePopulations(MatContext.get().getCurrentMeasureId(), populationsObject.getPopulationName(), 
							populationsObject.toXML(populationsObject), new AsyncCallback<SaveUpdateCQLResult>() {

						@Override
						public void onFailure(Throwable caught) {
							MainLayout.hideLoadingMessage();
							searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
									MatContext.get().getMessageDelegate().getGenericErrorMessage());
							MatContext.get().recordTransactionEvent(MatContext.get().getCurrentMeasureId(),
									null, "Saving " + populationsObject.getPopulationName(), "Unhandled Exception: " + caught.getLocalizedMessage(), 0);
						}

						@Override
						public void onSuccess(SaveUpdateCQLResult result) {
							MainLayout.hideLoadingMessage();
							panel.clear();
							buildPopulationWorkspace(result.getXml(), false);

							if(CQLWorkSpaceConstants.CQL_MEASUREOBSERVATIONS.equals(currentSection)) {
								measureObservationsEvent(null);	
							}else {
								buildPopulationEvent(null, currentSection);							
							}

							searchDisplay.getCqlLeftNavBarPanelView().getViewPopulations().setActive(false);
							searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert("Changes to " + populationsObject.getDisplayName() +" have been successfully saved.");						
						}

					});		
				}}

			@Override			
			public void onSaveClick(StrataDataModel strataDataModel) {
				
				searchDisplay.resetMessageDisplay();

				searchDisplay.getCqlStratificationDetailView().setIsDirty(false);

				MainLayout.showLoadingMessage();
				MatContext.get().getPopulationService().savePopulations(MatContext.get().getCurrentMeasureId(), "strata", 
						strataDataModel.toXML(strataDataModel.getStratificationObjectList()), new AsyncCallback<SaveUpdateCQLResult>() {

					@Override
					public void onFailure(Throwable caught) {
						MainLayout.hideLoadingMessage();
						searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert().createAlert(
								MatContext.get().getMessageDelegate().getGenericErrorMessage());
						MatContext.get().recordTransactionEvent(MatContext.get().getCurrentMeasureId(),
								null, "Saving Stratifications", "Unhandled Exception: " + caught.getLocalizedMessage(), 0);

					}

					@Override
					public void onSuccess(SaveUpdateCQLResult result) {
						MainLayout.hideLoadingMessage();
						panel.clear();
						buildPopulationWorkspace(result.getXml(), false);
						stratificationsEvent(null);
						searchDisplay.getCqlLeftNavBarPanelView().getViewPopulations().setActive(false);
						searchDisplay.getCqlLeftNavBarPanelView().getSuccessMessageAlert().createAlert("Changes to Stratification have been successfully saved.");
					}

				});

			}
			
			@Override
			public void onDeleteClick(Grid grid, PopulationClauseObject clauseObject) {
				
				searchDisplay.resetMessageDisplay();
				
				if (currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MEASUREOBSERVATIONS)) {
					int rowIndex =  searchDisplay.getCqlMeasureObservationDetailView().getPopulationsObject()
							.getPopulationClauseObjectList().indexOf(clauseObject);
					grid.removeRow(rowIndex);
					searchDisplay.getCqlMeasureObservationDetailView().getPopulationsObject().getPopulationClauseObjectList().remove(clauseObject);
					if (searchDisplay.getCqlMeasureObservationDetailView().getPopulationsObject().getPopulationClauseObjectList().size() == 1) {
						((Button) grid.getWidget(0, 3)).setEnabled(false);
					}
				} else  {
					int rowIndex =  searchDisplay.getCqlPopulationDetailView().getPopulationsObject()
							.getPopulationClauseObjectList().indexOf(clauseObject);
					grid.removeRow(rowIndex);
					searchDisplay.getCqlPopulationDetailView().getPopulationsObject().getPopulationClauseObjectList().remove(clauseObject);
					if (searchDisplay.getCqlPopulationDetailView().getPopulationsObject().getPopulationClauseObjectList().size() == 1) {
						((Button) grid.getWidget(0, 2)).setEnabled(false);
					}
				}
			}

			@Override
			public void onAddNewClick(Grid populationGrid, PopulationsObject populationsObject) {
				
				searchDisplay.resetMessageDisplay();
				
				int sequenceNumber = populationsObject.getLastClauseSequenceNumber() + 1;
				String displayName = populationsObject.getPopulationType() + " " + (sequenceNumber);
				String populationTyp = populationsObject.getPopulationClauseObjectList().get(0).getType();
				
				PopulationClauseObject popClause = new PopulationClauseObject(UUIDUtilClient.uuid());
				popClause.setType(populationTyp);
				popClause.setDisplayName(displayName);
				popClause.setSequenceNumber(sequenceNumber);
				populationsObject.getPopulationClauseObjectList().add(popClause);
				
				if (currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MEASUREOBSERVATIONS)) {
					searchDisplay.getCqlMeasureObservationDetailView().populateGrid(populationsObject.getPopulationClauseObjectList(), populationGrid,
							populationsObject.getPopulationClauseObjectList().size() - 1);
					if (searchDisplay.getCqlMeasureObservationDetailView().getPopulationsObject().getPopulationClauseObjectList().size() > 1) {
						((Button) populationGrid.getWidget(0, 3)).setEnabled(true);
					}
				} else {
					searchDisplay.getCqlPopulationDetailView().populateGrid(populationsObject.getPopulationClauseObjectList(), populationGrid,
							populationsObject.getPopulationClauseObjectList().size() - 1);
					if (searchDisplay.getCqlPopulationDetailView().getPopulationsObject().getPopulationClauseObjectList().size() > 1) {
						((Button) populationGrid.getWidget(0, 2)).setEnabled(true);
					}
				}
			}
			
			@Override
			public void onAddNewStratificationClick(StrataDataModel strataDataModel) {
				
				searchDisplay.resetMessageDisplay();
				
				int sequenceNumber = strataDataModel.getLastPopulationSequenceNumber() + 1;
				int initialStratumSequenceNumber = 1;
				String displayName = "Stratification " + sequenceNumber;
				StratificationsObject stratificationsObject = new StratificationsObject("stratification", UUIDUtilClient.uuid());
				stratificationsObject.setSequenceNumber(sequenceNumber);
				stratificationsObject.setDisplayName(displayName);
				
				if(strataDataModel.getStratificationObjectList().size() <= 1) {
					// enable the first delete button
					Button deleteButton = (Button) searchDisplay.getCqlStratificationDetailView().getParentGridList().get(0).getWidget(0, 2);
					deleteButton.setEnabled(true);
				}
				
				strataDataModel.getStratificationObjectList().add(stratificationsObject);
				searchDisplay.getCqlStratificationDetailView().addStratificationGrid(stratificationsObject);
				PopulationClauseObject popClause = buildStratum(initialStratumSequenceNumber);
				stratificationsObject.getPopulationClauseObjectList().add(popClause);
				searchDisplay.getCqlStratificationDetailView().addStratumGrid(stratificationsObject);				
			}

			@Override
			public void onAddNewStratumClick(StratificationsObject stratificationsObject) {
				
				searchDisplay.resetMessageDisplay();
				
				int sequenceNumber = stratificationsObject.getLastClauseSequenceNumber() + 1;
				PopulationClauseObject popClause = buildStratum(sequenceNumber);
				
				if(stratificationsObject.getPopulationClauseObjectList().size() <= 1) {
					Button deleteButton = (Button) searchDisplay.getCqlStratificationDetailView()
							.getParentToChildGridMap()
							.get(stratificationsObject.getDisplayName())
							.getWidget(0, 2);
					deleteButton.setEnabled(true);
				}
				
				stratificationsObject.getPopulationClauseObjectList().add(popClause);
				searchDisplay.getCqlStratificationDetailView().addStratumGrid(stratificationsObject);				
			}

			

			@Override
			public void onDeleteStratificationClick(Grid stratificationGrid, StratificationsObject stratification) {
				
				searchDisplay.resetMessageDisplay();
				
				// get the view
				CQLStratificationDetailView view = searchDisplay.getCqlStratificationDetailView();
				
				// remove from the view
				stratificationGrid.removeFromParent(); 
				Grid stratumGrid = view.getParentToChildGridMap().get(stratification.getDisplayName()); 
				stratumGrid.removeFromParent();
	
				// remove from the list
				view.getParentGridList().remove(stratificationGrid);
				view.getParentToChildGridMap().remove(stratification.getDisplayName());
				
				// remove from the model
				view.getStrataDataModel().getStratificationObjectList().remove(stratification);
				
				// disable delete button if there is only one left after delete
				if(view.getStrataDataModel().getStratificationObjectList().size() <= 1) {
					Button deleteButton = (Button) view.getParentGridList().get(0).getWidget(0, 2);
					deleteButton.setEnabled(false);
				}
			}

			@Override
			public void onDeleteStratumClick(Grid stratumGrid, StratificationsObject stratification, PopulationClauseObject stratum) {
				
				searchDisplay.resetMessageDisplay();
				
				int index = stratification.getPopulationClauseObjectList().indexOf(stratum);
								
				// remove from the view
				stratumGrid.removeRow(index);
				
				// remove from the model	
				stratification.getPopulationClauseObjectList().remove(stratum);
				
				if(stratification.getPopulationClauseObjectList().size() <=1) {
					Button button = (Button) stratumGrid.getWidget(0, 2);
					button.setEnabled(false);
				}
			}
			
			@Override
			public void clearMessagesOnDropdown() {
				searchDisplay.resetMessageDisplay();
			}
		});
	}

	private void addWarningConfirmationHandlers() {

		searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationYesButton()
				.addClickHandler(event -> handleYesConfirmation());
		searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationNoButton()
				.addClickHandler(event -> handleNoConfirmation());
	}

	private void handleYesConfirmation() {
		setPageDirty(false);
		setActiveMenuItem(currentSection, false);
		setActiveMenuItem(nextSection, true);
		searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationMessageAlert().clearAlert();
		if (nextSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MEASUREOBSERVATIONS)) {
			searchDisplay.displayMeasureObservations();
		} else if (nextSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_STRATIFICATIONS)) {
			searchDisplay.displayStratification();
		} else if(nextSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_VIEWPOPULATIONS)){
			buildViewPopulationView();
		} else {
			searchDisplay.displayPopulationDetailView(nextSection);
		}
		currentSection = nextSection;
	}

	private void handleNoConfirmation() {
		setActiveMenuItem(currentSection, true);
		setActiveMenuItem(nextSection, false);
		searchDisplay.getCqlLeftNavBarPanelView().getWarningConfirmationMessageAlert().clearAlert();
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
		searchDisplay.setCqlPopulationDetailView(null);
		searchDisplay.setCqlMeasureObservationDetailView(null);
		searchDisplay.setCqlStratificationDetailView(null);
		searchDisplay.resetMessageDisplay();
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
					new AsyncCallback<SortedClauseMapResult>() { // Loading the measure's SimpleXML from the Measure_XML
																	// table
						@Override
						public void onSuccess(SortedClauseMapResult result) {
							buildPopulationWorkspace(result.getMeasureXmlModel().getXml(), true);
						}

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());

						}
					});

			MeasureComposerPresenter.setSubSkipEmbeddedLink("CQLPopulationWorkspaceView.containerPanel");
			Mat.focusSkipLists(MEASURE_COMPOSER);

		}
	}

	private void buildPopulationWorkspace(String result, boolean isDefault) {
		try {
			String xml = result != null ? result : null;
			Document document = XMLParser.parse(xml);

			// create a Populations Data model object from the Measure XML.
			PopulationDataModel populationDataModel = new PopulationDataModel(document);

			searchDisplay.buildView(document, populationDataModel);
			addLeftNavEventHandler();
			searchDisplay.resetMessageDisplay();
			panel.add(searchDisplay.asWidget());
			if(isDefault) {
				buildViewPopulationView();	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adding handlers for Anchor Items.
	 */
	private void addLeftNavEventHandler() {
		searchDisplay.getCqlLeftNavBarPanelView().getInitialPopulation().addClickHandler(event -> initialPopulationEvent(event));
		searchDisplay.getCqlLeftNavBarPanelView().getNumerator().addClickHandler(event -> numeratorEvent(event) );
		searchDisplay.getCqlLeftNavBarPanelView().getNumeratorExclusions().addClickHandler(event -> numeratorExclusionEvent(event) );
		searchDisplay.getCqlLeftNavBarPanelView().getDenominator().addClickHandler(event -> denominatorEvent(event) );
		searchDisplay.getCqlLeftNavBarPanelView().getDenominatorExclusions().addClickHandler(event -> denominatorExclusionsEvent(event) );
		searchDisplay.getCqlLeftNavBarPanelView().getMeasurePopulations().addClickHandler(event -> measurePopulationsEvent(event) );
		searchDisplay.getCqlLeftNavBarPanelView().getMeasurePopulationExclusions().addClickHandler(event -> measurePopulationExclusionsEvent(event) );
		searchDisplay.getCqlLeftNavBarPanelView().getDenominatorExceptions().addClickHandler(event -> denominatorExceptionsEvent(event) );
		searchDisplay.getCqlLeftNavBarPanelView().getStratifications().addClickHandler(event -> stratificationsEvent(event) );
		searchDisplay.getCqlLeftNavBarPanelView().getMeasureObservations().addClickHandler(event -> measureObservationsEvent(event) );
		searchDisplay.getCqlLeftNavBarPanelView().getViewPopulations().addClickHandler(event -> viewPopulationsEvent(event) );
	}
	
	private void buildPopulationEvent(ClickEvent event, String populationName) {
		
		if (isDirty()) {
			nextSection = populationName;
			searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
			event.stopPropagation();
			showSearchingBusy(false);
		} else {			
			showSearchingBusy(true);
			setNextActiveMenuItem(currentSection, populationName);

			if (CQLWorkSpaceConstants.CQL_MEASUREOBSERVATIONS.equals(populationName)) {
				searchDisplay.displayMeasureObservations();
			} else if(CQLWorkSpaceConstants.CQL_STRATIFICATIONS.equals(populationName)) {
				searchDisplay.displayStratification();
			} else {
				searchDisplay.displayPopulationDetailView(populationName);	
			}			

			Mat.focusSkipLists(MEASURE_COMPOSER);
			showSearchingBusy(false);
		}	

	}

	
	/**
	 * Build View for Initial Population when Initial Population AnchorList item is
	 * clicked.
	 */
	private void initialPopulationEvent(ClickEvent event) {
		buildPopulationEvent(event, CQLWorkSpaceConstants.CQL_INITIALPOPULATION);		
	}
	
	/**
	 * Build View for Numerator when Numerator AnchorList item is clicked.
	 */
	private void numeratorEvent(ClickEvent event) {
		buildPopulationEvent(event, CQLWorkSpaceConstants.CQL_NUMERATOR);		
	}

	/**
	 * Build View for Numerator Exclusions when Numerator Exclusions AnchorList item
	 * is clicked.
	 */
	private void numeratorExclusionEvent(ClickEvent event) {
		buildPopulationEvent(event, CQLWorkSpaceConstants.CQL_NUMERATOREXCLUSIONS);		
	}

	/**
	 * Build View for Denominator when Denominator AnchorList item is clicked.
	 */
	private void denominatorEvent(ClickEvent event) {
		buildPopulationEvent(event, CQLWorkSpaceConstants.CQL_DENOMINATOR);		
	}

	/**
	 * Build View for Denominator Exclusions when Denominator Exclusions AnchorList
	 * item is clicked.
	 */
	private void denominatorExclusionsEvent(ClickEvent event) {
		buildPopulationEvent(event, CQLWorkSpaceConstants.CQL_DENOMINATOREXCLUSIONS);		
	}

	/**
	 * Build View for Denominator Exceptions when Denominator Exceptions AnchorList
	 * item is clicked.
	 */
	private void denominatorExceptionsEvent(ClickEvent event) {
		buildPopulationEvent(event, CQLWorkSpaceConstants.CQL_DENOMINATOREXCEPTIONS);
	}

	/**
	 * Build View for Measure Populations when Measure Populations AnchorList item
	 * is clicked.
	 */
	private void measurePopulationsEvent(ClickEvent event) {
		buildPopulationEvent(event, CQLWorkSpaceConstants.CQL_MEASUREPOPULATIONS);
	}

	/**
	 * Build View for Measure Populations Exclusions when Measure Populations
	 * Exclusions AnchorList item is clicked.
	 */
	private void measurePopulationExclusionsEvent(ClickEvent event) {
		buildPopulationEvent(event, CQLWorkSpaceConstants.CQL_MEASUREPOPULATIONEXCLUSIONS);
	}

	/**
	 * Build View for Stratifications when Stratifications AnchorList item is clicked.
	 */
	private void stratificationsEvent(ClickEvent event) {
		buildPopulationEvent(event, CQLWorkSpaceConstants.CQL_STRATIFICATIONS);		
	}
		
	/**
	 * Build View for Measure Observations when Measure Observations AnchorList item is
	 * clicked.
	 */
	private void measureObservationsEvent(ClickEvent event) {
		buildPopulationEvent(event, CQLWorkSpaceConstants.CQL_MEASUREOBSERVATIONS);		
	}

	/**
	 * Build View for View Populations when View Populations AnchorList item is
	 * clicked.
	 */
	private void viewPopulationsEvent(ClickEvent event) {
		if(event != null && isDirty()) {
			nextSection = CQLWorkSpaceConstants.CQL_VIEWPOPULATIONS;
			searchDisplay.getCqlLeftNavBarPanelView().showUnsavedChangesWarning();
			event.stopPropagation();
		} else { 
			buildViewPopulationView();
		}
	}

	/**
	 * Build View Population Section
	 */
	private void buildViewPopulationView() {
		setNextActiveMenuItem(currentSection,CQLWorkSpaceConstants.CQL_VIEWPOPULATIONS );
		searchDisplay.buildViewPopulations();
		searchDisplay.setHeadingBasedOnCurrentSection("Population Workspace > View Populations", "headingPanel");
		Mat.focusSkipLists(MEASURE_COMPOSER);
	}

	
	private void setNextActiveMenuItem(String currSection, String nexSection) {
		searchDisplay.resetMessageDisplay();
		setActiveMenuItem(currSection, false);
		setActiveMenuItem(nexSection, true);
		this.currentSection= nexSection;
	}
	/**
	 * Method to unset Anchor List Item selection for previous selection and set for
	 * new selections.
	 *
	 * @param menuClickedBefore
	 *            the menu clicked before
	 */
	private void setActiveMenuItem(String menuClickedBefore, boolean isSet) {
		if (!isDirty()) {
			searchDisplay.resetMessageDisplay();
			switch (menuClickedBefore) {
			case CQLWorkSpaceConstants.CQL_INITIALPOPULATION:
				searchDisplay.getCqlLeftNavBarPanelView().getInitialPopulation().setActive(isSet);
				break;
			case CQLWorkSpaceConstants.CQL_DENOMINATOR:
				searchDisplay.getCqlLeftNavBarPanelView().getDenominator().setActive(isSet);
				break;
			case CQLWorkSpaceConstants.CQL_NUMERATOR:
				searchDisplay.getCqlLeftNavBarPanelView().getNumerator().setActive(isSet);
				break;
			case CQLWorkSpaceConstants.CQL_NUMERATOREXCLUSIONS:
				searchDisplay.getCqlLeftNavBarPanelView().getNumeratorExclusions().setActive(isSet);
				break;
			case CQLWorkSpaceConstants.CQL_DENOMINATOREXCLUSIONS:
				searchDisplay.getCqlLeftNavBarPanelView().getDenominatorExclusions().setActive(isSet);
				break;
			case CQLWorkSpaceConstants.CQL_DENOMINATOREXCEPTIONS:
				searchDisplay.getCqlLeftNavBarPanelView().getDenominatorExceptions().setActive(isSet);
				break;
			case CQLWorkSpaceConstants.CQL_MEASUREPOPULATIONS:
				searchDisplay.getCqlLeftNavBarPanelView().getMeasurePopulations().setActive(isSet);
				break;
			case CQLWorkSpaceConstants.CQL_MEASUREPOPULATIONEXCLUSIONS:
				searchDisplay.getCqlLeftNavBarPanelView().getMeasurePopulationExclusions().setActive(isSet);
				break;
			case CQLWorkSpaceConstants.CQL_STRATIFICATIONS:
				searchDisplay.getCqlLeftNavBarPanelView().getStratifications().setActive(isSet);
				break;
			case CQLWorkSpaceConstants.CQL_MEASUREOBSERVATIONS:
				searchDisplay.getCqlLeftNavBarPanelView().getMeasureObservations().setActive(isSet);
				break;
			case CQLWorkSpaceConstants.CQL_VIEWPOPULATIONS:
				searchDisplay.getCqlLeftNavBarPanelView().getViewPopulations().setActive(isSet);
				break;
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
	 * Show human readable dialog box.
	 *
	 * @param result
	 *            the result
	 * @param populationName
	 *            the population name
	 */
	public static native void showHumanReadableDialogBox(String result, String populationName) /*-{
		var dummyURL = window.location.protocol + "//"
				+ window.location.hostname + ":" + window.location.port + "/"
				+ "mat/humanreadable.html";
		var humanReadableWindow = window.open(dummyURL, "",
				"width=1200,height=700,scrollbars=yes,resizable=yes");

		if (humanReadableWindow && humanReadableWindow.top) {
			humanReadableWindow.document.write(result);
			humanReadableWindow.document.title = populationName;
		}
	}-*/;

	/**
	 * Show searching busy.
	 *
	 * @param busy
	 *            the busy
	 */
	private void showSearchingBusy(final boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}

		searchDisplay.getCqlLeftNavBarPanelView().getInitialPopulation().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getNumerator().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getDenominator().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getNumeratorExclusions().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getDenominatorExclusions().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getDenominatorExceptions().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getStratifications().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getMeasureObservations().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getMeasurePopulations().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getMeasurePopulationExclusions().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().setIsLoading(!busy);
	}

	/**
	 * This method identifies current section and finds if that section has unsaved changes or not.
	 * @return boolean
	 */
	public boolean isDirty() {
		boolean isViewDirty = false;
		if (currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MEASUREOBSERVATIONS)) {
			isViewDirty = searchDisplay.getCqlMeasureObservationDetailView().isDirty();
		} else if (currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_STRATIFICATIONS)) {
			isViewDirty = searchDisplay.getCqlStratificationDetailView().isDirty();
		} else if (currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_VIEWPOPULATIONS)) {
			isViewDirty = false;
		} else {
			isViewDirty = searchDisplay.getCqlPopulationDetailView().isDirty();
		}

		return isViewDirty;
	}

	/**
	 * This method set the page dirty if there are unsaved changes.
	 * @param isPageDirty
	 */
	private void setPageDirty(boolean isPageDirty) {
		if (currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MEASUREOBSERVATIONS)) {
			searchDisplay.getCqlMeasureObservationDetailView().setIsDirty(isPageDirty);
		} else if (currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_STRATIFICATIONS)) {
			searchDisplay.getCqlStratificationDetailView().setIsDirty(isPageDirty);
		} else if (!currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_VIEWPOPULATIONS)) {
			searchDisplay.getCqlPopulationDetailView().setIsDirty(isPageDirty);
		}

	}
	
	/**
	 * This method is to used to check if when a Function is selected there is a corresponding selection
	 * for Aggregate Function for a given Measure Observation.
	 * Conditions : 1. Has to be in Measure Observation section.
	 * 	            2. Both Aggregate Function and Function are selected 
	 * 				   from the corresponding dropdown for any one Measure Observation
	 * 				
	 * @param populationClauseObjectList
	 * @return true if only one of aggregate function or function is selected for a given measure observation 
	 * 		   false if both are selected or if both are not selected 
	 */
	private boolean funcOrAggFuncExistAlone(List<PopulationClauseObject> populationClauseObjectList) {
		
		//Either Func or Agg Func is selected
		Predicate<PopulationClauseObject> p1 = pco -> !pco.getAggFunctionName().isEmpty() || !pco.getCqlExpressionDisplayName().isEmpty() ;
		//Either Func or Agg Func has no selection
		Predicate<PopulationClauseObject> p2 = pco -> pco.getAggFunctionName().isEmpty() || pco.getCqlExpressionDisplayName().isEmpty() ;
		
		return currentSection.equalsIgnoreCase(CQLWorkSpaceConstants.CQL_MEASUREOBSERVATIONS) &&				
				populationClauseObjectList.stream().anyMatch(p1.and(p2));

	}

	public boolean isPopulationWorkSpaceValid() {
		getSearchDisplay().resetMessageDisplay();
		return !isDirty();
	}

}
