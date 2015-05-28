package mat.client.measurepackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.QDSAppliedListModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.measurepackage.MeasurePackagerView.Observer;
import mat.client.measurepackage.service.MeasurePackageSaveResult;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.InProgressMessageDisplay;
import mat.client.shared.MatContext;
import mat.client.shared.MeasurePackageClauseCellListWidget;
import mat.client.shared.ReadOnlyHelper;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.WarningMessageDisplay;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;
import mat.model.RiskAdjustmentDTO;
import mat.shared.MeasurePackageClauseValidator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasurePackagePresenter.
 */
public class MeasurePackagePresenter implements MatPresenter {
	
	/** The panel. */
	private SimplePanel panel = new SimplePanel();
	
	/** The view. */
	private PackageView view;
	
	/** The model. */
	private ManageMeasureDetailModel model;
	
	/** The current detail. */
	private MeasurePackageDetail currentDetail = null;
	
	/** The packageOverview. */
	private MeasurePackageOverview packageOverview;
	
	/** The db package clauses. */
	private List<MeasurePackageClauseDetail> dbPackageClauses = new ArrayList<MeasurePackageClauseDetail>();
	
	/** The db supp data elements. */
	private List<QualityDataSetDTO> dbSuppDataElements = new ArrayList<QualityDataSetDTO>();
	
	/** The db risk adj vars. */
	private List<RiskAdjustmentDTO> dbRiskAdjVars = new ArrayList<RiskAdjustmentDTO>();
	
	/** The is measure package success. */
	private boolean isMeasurePackageExportSuccess = false;
	
	/**
	 * Gets the db supp data elements.
	 *
	 * @return the db supp data elements
	 */
	public List<QualityDataSetDTO> getDbSuppDataElements() {
		Collections.sort(dbSuppDataElements,new QualityDataSetDTO.Comparator());
		return dbSuppDataElements;
	}
	
	/**
	 * Sets the db supp data elements.
	 *
	 * @param dbSuppDataElements the new db supp data elements
	 */
	public void setDbSuppDataElements(
			List<QualityDataSetDTO> dbSuppDataElements) {
		this.dbSuppDataElements = dbSuppDataElements;
	}
	
	/**
	 * Gets the db package clauses.
	 *
	 * @return the db package clauses
	 */
	public List<MeasurePackageClauseDetail> getDbPackageClauses() {
		return dbPackageClauses;
	}
	
	/**
	 * Sets the db package clauses.
	 *
	 * @param dbPackageClauses the new db package clauses
	 */
	public void setDbPackageClauses(
			List<MeasurePackageClauseDetail> dbPackageClauses) {
		this.dbPackageClauses = dbPackageClauses;
	}
	
	/**
	 * Gets the db risk adj vars.
	 *
	 * @return the db risk adj vars
	 */
	public List<RiskAdjustmentDTO> getDbRiskAdjVars() {
		Collections.sort(dbRiskAdjVars,new RiskAdjustmentDTO.Comparator());
		return dbRiskAdjVars;
	}
	
	/**
	 * Sets the db risk adj vars.
	 *
	 * @param dbRiskAdjVars the new db risk adj vars
	 */
	public void setDbRiskAdjVars(List<RiskAdjustmentDTO> dbRiskAdjVars) {
		this.dbRiskAdjVars = dbRiskAdjVars;
	}
	
	/** The service. */
	private static  MeasureServiceAsync service = MatContext.get().getMeasureService();
	
	
	
	/**
	 * The Interface View.
	 */
	public static interface PackageView {
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		/**
		 * Gets the measure package success msg.
		 * 
		 * @return the measure package success msg
		 */
		SuccessMessageDisplayInterface getMeasurePackageSuccessMsg();
		
		/**
		 * Gets the measure package warning msg.
		 * 
		 * @return the measure package warning msg
		 */
		WarningMessageDisplay getMeasurePackageWarningMsg();
		
		/**
		 * Gets the package error message display.
		 * 
		 * @return the package error message display
		 */
		ErrorMessageDisplayInterface getPackageErrorMessageDisplay();
		
		/**
		 * Gets the package measure button.
		 * 
		 * @return the package measure button
		 */
		HasClickHandlers getPackageMeasureButton();
		
		/**
		 * Gets the package success message display.
		 * 
		 * @return the package success message display
		 */
		SuccessMessageDisplayInterface getPackageSuccessMessageDisplay();
		
		/**
		 * As widget.
		 *
		 * @return the widget
		 */
		Widget asWidget();
		
		/**
		 * Sets the qDM elements in supp elements.
		 *
		 * @param clauses the new qDM elements in supp elements
		 */
		void setQDMElementsInSuppElements(List<QualityDataSetDTO> clauses);
		
		/**
		 * Gets the qDM elements in supp elements.
		 *
		 * @return the qDM elements in supp elements
		 */
		List<QualityDataSetDTO> getQDMElementsInSuppElements();
		
		/**
		 * Sets the qDM elements.
		 *
		 * @param clauses the new qDM elements
		 */
		void setQDMElements(List<QualityDataSetDTO> clauses);
		
		/**
		 * Gets the qDM elements.
		 *
		 * @return the qDM elements
		 */
		List<QualityDataSetDTO> getQDMElements();
		
		/**
		 * Gets the adds the qdm elements to measure button.
		 *
		 * @return the adds the qdm elements to measure button
		 */
		HasClickHandlers getAddQDMElementsToMeasureButton();
		
		/**
		 * Gets the adds the risk adj variables to measure.
		 *
		 * @return the adds the risk adj variables to measure
		 */
		HasClickHandlers getaddRiskAdjVariablesToMeasure();
		
		/**
		 * Gets the supp data success message display.
		 *
		 * @return the supp data success message display
		 */
		SuccessMessageDisplayInterface getSuppDataSuccessMessageDisplay();
		
		/**
		 * Sets the view is editable.
		 *
		 * @param b the b
		 * @param packages the packages
		 */
		void setViewIsEditable(boolean b,
				List<MeasurePackageDetail> packages);
		
		/**
		 * Sets the clauses.
		 *
		 * @param clauses the new clauses
		 */
		void setClauses(List<MeasurePackageClauseDetail> clauses);
		
		/**
		 * Sets the package name.
		 *
		 * @param name the new package name
		 */
		void setPackageName(String name);
		
		/**
		 * Sets the clauses in package.
		 *
		 * @param list the new clauses in package
		 */
		void setClausesInPackage(List<MeasurePackageClauseDetail> list);
		
		/**
		 * Gets the package grouping widget.
		 *
		 * @return the package grouping widget
		 */
		MeasurePackageClauseCellListWidget getPackageGroupingWidget();
		
		/**
		 * Sets the observer.
		 *
		 * @param observer the new observer
		 */
		void setObserver(Observer observer);
		
		/**
		 * Sets the applied qdm list.
		 *
		 * @param appliedListModel the new applied qdm list
		 */
		void setAppliedQdmList(QDSAppliedListModel appliedListModel);
		
		/**
		 * Gets the creates the new button.
		 *
		 * @return the creates the new button
		 */
		HasClickHandlers getCreateNewButton();
		
		/**
		 * Builds the cell table.
		 *
		 * @param packages the packages
		 */
		void buildCellTable(List<MeasurePackageDetail> packages);
		
		/**
		 * Gets the save error message display.
		 *
		 * @return the save error message display
		 */
		ErrorMessageDisplay getSaveErrorMessageDisplay();
		/**
		 * Gets the include vsac data.
		 * 
		 * @return the include vsac data
		 */
		CustomCheckBox getIncludeVSACData();
		/**
		 * Gets the measure error message display.
		 *
		 * @return the measure error message display
		 */
		ErrorMessageDisplayInterface getMeasureErrorMessageDisplay();
		
		/**
		 * Gets the package measure and export button.
		 *
		 * @return the package measure and export button
		 */
		HasClickHandlers getPackageMeasureAndExportButton();
		
		/**
		 * Sets the risk adj clause list.
		 *
		 * @param riskAdjClauseList the new risk adj clause list
		 */
		void setSubTreeClauseList(List<RiskAdjustmentDTO> riskAdjClauseList);
		
		/**
		 * Gets the risk adj clauses.
		 *
		 * @return the risk adj clauses
		 */
		List<RiskAdjustmentDTO> getRiskAdjClauses();
		
		/**
		 * Gets the risk adj var.
		 *
		 * @return the risk adj var
		 */
		List<RiskAdjustmentDTO> getRiskAdjVar();
		
		/**
		 * Gets the risk adj success message display.
		 *
		 * @return the risk adj success message display
		 */
		SuccessMessageDisplayInterface getRiskAdjSuccessMessageDisplay();
		void setSubTreeInRiskAdjVarList(
				List<RiskAdjustmentDTO> riskAdjClauseList);
		InProgressMessageDisplay getInProgressMessageDisplay();
	}
	
	/** The vsacapi service async. */
	VSACAPIServiceAsync vsacapiServiceAsync = MatContext.get()
			.getVsacapiServiceAsync();
	
	/**
	 * Instantiates a new measure package presenter.
	 *
	 * @param packageView the package view
	 */
	public MeasurePackagePresenter(PackageView packageView) {
		view = packageView;
		addAllHandlers();
	}
	
	/**
	 * Adds the all handlers.
	 */
	private void addAllHandlers() {
		
		view.getCreateNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				clearMessages();
				view.getPackageGroupingWidget().getDisclosurePanelAssociations().setVisible(false);
				view.getPackageGroupingWidget().getDisclosurePanelItemCountTable().setVisible(false);
				System.out.println("Overview Object"+ packageOverview.getClauses().size());
				setNewMeasurePackage();
			}
		});
		
		view.getPackageMeasureButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				((Button) view.getPackageMeasureButton()).setEnabled(false);
				((Button) view.getPackageMeasureAndExportButton()).setEnabled(false);
				isMeasurePackageExportSuccess = false;
				view.getInProgressMessageDisplay().setMessage(" Loading Please Wait...");
				validateGroup();
			}
		});
		
		view.getPackageMeasureAndExportButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				view.getInProgressMessageDisplay().clear();
				((Button) view.getPackageMeasureButton()).setEnabled(false);
				((Button) view.getPackageMeasureAndExportButton()).setEnabled(false);
				isMeasurePackageExportSuccess = true;
				validateGroup();
			}
		});
		view.getaddRiskAdjVariablesToMeasure().addClickHandler(
				new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						clearMessages();
						updateRiskAdjFromView(currentDetail);
						((Button) view.getPackageMeasureButton()).setEnabled(true);
						MatContext
						.get()
						.getPackageService()
						.saveRiskVariables(currentDetail,
								new AsyncCallback<Void>() {
							@Override
							public void onFailure(
									final Throwable caught) {
								Mat.hideLoadingMessage();
								view.getPackageErrorMessageDisplay()
								.setMessage(
										MatContext
										.get()
										.getMessageDelegate()
										.getUnableToProcessMessage());
							}
							
							@Override
							public void onSuccess(
									final Void result) {
								getMeasurePackageOverview(MatContext
										.get()
										.getCurrentMeasureId());
								view.getRiskAdjSuccessMessageDisplay()
								.setMessage(
										MatContext
										.get()
										.getMessageDelegate()
										.getRiskAdjSavedMessage());
							}
						});
						
					}
				});
		
		view.getAddQDMElementsToMeasureButton().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(final ClickEvent event) {
						clearMessages();
						((Button) view.getPackageMeasureButton()).setEnabled(true);
						updateSuppDataDetailsFromView(currentDetail);
						MatContext
						.get()
						.getPackageService()
						.saveQDMData(currentDetail,
								new AsyncCallback<Void>() {
							@Override
							public void onFailure(final Throwable caught) {
								Mat.hideLoadingMessage();
								view.getPackageErrorMessageDisplay().
								setMessage(MatContext.get().
										getMessageDelegate().
										getUnableToProcessMessage());
							}
							
							@Override
							public void onSuccess(final Void result) {
								getMeasurePackageOverview(MatContext.get()
										.getCurrentMeasureId());
								view.getSuppDataSuccessMessageDisplay()
								.setMessage(MatContext.get()
										.getMessageDelegate()
										.getSuppDataSavedMessage());
							}
						});
					}
				});
		
		view.getPackageGroupingWidget().getSaveGrouping().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				clearMessages();
				((Button) view.getPackageMeasureButton()).setEnabled(true);
				view.getPackageGroupingWidget().getDisclosurePanelAssociations().setVisible(false);
				view.getPackageGroupingWidget().getDisclosurePanelItemCountTable().setVisible(false);
				updateDetailsFromView(currentDetail);
				if (isValid()) {
					MatContext.get().getPackageService()
					.save(currentDetail, new AsyncCallback<MeasurePackageSaveResult>() {
						@Override
						public void onFailure(final Throwable caught) {
						}
						@Override
						public void onSuccess(final MeasurePackageSaveResult result) {
							if (result.isSuccess()) {
								getMeasurePackageOverview(MatContext.get()
										.getCurrentMeasureId());
								view.getPackageSuccessMessageDisplay().setMessage(
										MatContext.get().getMessageDelegate().
										getGroupingSavedMessage());
							} else {
								if (result.getMessages().size() > 0) {
									view.getPackageErrorMessageDisplay().
									setMessages(result.getMessages());
								} else {
									view.getPackageErrorMessageDisplay().clear();
								}
							}
						}
					});
				}
			}
		});
	}
	
	/**
	 * Validate group.
	 */
	protected void validateGroup() {
		MatContext.get().getMeasureService()
		.validateForGroup(model,new AsyncCallback<ValidateMeasureResult>() {
			@Override
			public void onFailure(final Throwable caught) {
				
				Mat.hideLoadingMessage();
				((Button) view.getPackageMeasureButton()).setEnabled(true);
				((Button) view.getPackageMeasureAndExportButton()).setEnabled(true);
				view.getPackageErrorMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate().getUnableToProcessMessage());
				view.getInProgressMessageDisplay().clear();
			}
			
			@Override
			public void onSuccess(final ValidateMeasureResult result) {
				Mat.showLoadingMessage();
				if(result.isValid()){
					validatePackageGrouping();
				}else {
					Mat.hideLoadingMessage();
					view.getInProgressMessageDisplay().clear();
					view.getMeasureErrorMessageDisplay()
					.setMessages(result.getValidationMessages());
					((Button) view.getPackageMeasureButton()).setEnabled(true);
					((Button) view.getPackageMeasureAndExportButton()).setEnabled(true);
				}
			}
			
		});
		
		
	}
	
	/**
	 * Valid grouping check.
	 * @return boolean.
	 */
	private boolean isValid() {
		
		List<MeasurePackageClauseDetail> detailList = view
				.getPackageGroupingWidget().getGroupingPopulationList();
		List<String> messages = new ArrayList<String>();
		MeasurePackageClauseValidator clauseValidator = new MeasurePackageClauseValidator();
		MeasurePackageClauseCellListWidget measurePackageClauseCellListWidget = new MeasurePackageClauseCellListWidget();
		messages = clauseValidator.isValidMeasurePackage(detailList);
		measurePackageClauseCellListWidget.CheckForNumberOfStratification((ArrayList<MeasurePackageClauseDetail>) detailList, messages);
		if (messages.size() > 0) {
			view.getPackageErrorMessageDisplay().setMessages(messages);
		} else {
			view.getPackageErrorMessageDisplay().clear();
		}
		return messages.size() == 0;
	}
	
	/**
	 * updateDetailsFromView.
	 *
	 * @param currentDetail the current detail
	 */
	public void updateDetailsFromView(MeasurePackageDetail currentDetail) {
		currentDetail.setMeasureId(MatContext.get().getCurrentMeasureId());
		currentDetail.setPackageClauses(view.getPackageGroupingWidget().getGroupingPopulationList());
		currentDetail.setToComparePackageClauses(dbPackageClauses);
		currentDetail.setValueSetDate(null);
	}
	
	
	/**
	 * Get Applied QDM List for Item Count Table.
	 *
	 * @param checkForSupplementData - Boolean.
	 * @return the applied qdm list
	 */
	public final void getAppliedQDMList(boolean checkForSupplementData) {
		String measureId = MatContext.get().getCurrentMeasureId();
		if ((measureId != null) && !measureId.equals("")) {
			service.getAppliedQDMForItemCount(measureId,
					checkForSupplementData,
					new AsyncCallback<List<QualityDataSetDTO>>() {
				@Override
				public void onFailure(final Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate()
							.getGenericErrorMessage());
				}
				
				@Override
				public void onSuccess(
						final List<QualityDataSetDTO> result) {
					QDSAppliedListModel appliedListModel = new QDSAppliedListModel();
					appliedListModel.setAppliedQDMs(result);
					view.setAppliedQdmList(appliedListModel);
				}
			});
		}
	}
	/**
	 * Clear messages.
	 */
	private void clearMessages() {
		view.getPackageSuccessMessageDisplay().clear();
		view.getSuppDataSuccessMessageDisplay().clear();
		view.getPackageErrorMessageDisplay().clear();
		view.getMeasurePackageSuccessMsg().clear();
		view.getErrorMessageDisplay().clear();
		view.getMeasurePackageWarningMsg().clear();
		view.getMeasureErrorMessageDisplay().clear();
		view.getSaveErrorMessageDisplay().clear();
		view.getRiskAdjSuccessMessageDisplay().clear();
		
	}
	/**
	 * Display Empty.
	 */
	private void displayEmpty() {
		panel.clear();
		panel.add(view.asWidget());
		view.getPackageGroupingWidget().getDisclosurePanelAssociations().setVisible(false);
		view.getPackageGroupingWidget().getDisclosurePanelItemCountTable().setVisible(false);
		view.getIncludeVSACData().setValue(false);
	}
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		currentDetail = null;
		packageOverview = null;
		view.getPackageGroupingWidget().getDisclosurePanelAssociations().setVisible(false);
		view.getPackageGroupingWidget().getDisclosurePanelItemCountTable().setVisible(false);
		view.getIncludeVSACData().setValue(false);
	}
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		Mat.hideLoadingMessage();
		clearMessages();
		if ((MatContext.get().getCurrentMeasureId() != null)
				&& !MatContext.get().getCurrentMeasureId().equals("")) {
			getMeasure(MatContext.get().getCurrentMeasureId());
			getAppliedQDMList(true);
			view.getPackageGroupingWidget().getDisclosurePanelAssociations().setVisible(false);
			view.getPackageGroupingWidget().getDisclosurePanelItemCountTable().setVisible(false);
			view.getIncludeVSACData().setValue(false);
		} else {
			displayEmpty();
		}
		MeasureComposerPresenter.setSubSkipEmbeddedLink("MeasurePackage");
		Mat.focusSkipLists("MeasureComposer");
	}
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		panel.clear();
		panel.add(view.asWidget());
		return panel;
	}
	
	/**
	 * get Measure Package Overview.
	 *
	 * @param measureId - String.
	 * @return the measure package overview
	 */
	private void getMeasurePackageOverview(final String measureId) {
		MatContext
		.get()
		.getPackageService()
		.getClausesAndPackagesForMeasure(measureId,
				new AsyncCallback<MeasurePackageOverview>() {
			@Override
			public void onFailure(final Throwable caught) {
				view.getPackageErrorMessageDisplay()
				.setMessage(
						MatContext
						.get()
						.getMessageDelegate()
						.getGenericErrorMessage());
			}
			@Override
			public void onSuccess(final MeasurePackageOverview result) {
				if ((currentDetail != null)
						&& !currentDetail.getMeasureId()
						.equalsIgnoreCase(measureId)) {
					currentDetail = null; // This will make sure
					// the package
					// information are not
					// cached across
					// measures.
				}
				
				setOverview(result);
			}
		});
		view.setObserver(new MeasurePackagerView.Observer() {
			@Override
			public void onEditClicked(MeasurePackageDetail detail) {
				
				if(!currentDetail.isEqual(view.getPackageGroupingWidget().getGroupingPopulationList(),
						dbPackageClauses)){
					view.getSaveErrorMessageDisplay().clear();
					showErrorMessage(view.getSaveErrorMessageDisplay());
					view.getSaveErrorMessageDisplay().getButtons().get(0).setFocus(true);
					handleClickEventsOnUnsavedErrorMsg(detail,view.getSaveErrorMessageDisplay().getButtons()
							, view.getSaveErrorMessageDisplay(), null);
				} else {
					currentDetail = new MeasurePackageDetail();
					currentDetail = detail;
					clearMessages();
					setMeasurePackageDetailsOnView();
				}
			}
			@Override
			public void onDeleteClicked(MeasurePackageDetail detail) {
				clearMessages();
				((Button) view.getPackageMeasureButton()).setEnabled(true);
				deleteMeasurePackage(detail);
			}
		});
	}
	
	/**
	 * Show error message.
	 *
	 * @param errorMessageDisplay the error message display
	 */
	private void showErrorMessage(ErrorMessageDisplay errorMessageDisplay) {
		String msg = MatContext.get().getMessageDelegate().getSaveErrorMsg();
		List<String> btn = new ArrayList<String>();
		btn.add("Yes");
		btn.add("No");
		errorMessageDisplay.setMessageWithButtons(msg, btn);
	}
	
	/**
	 * Handle click events on unsaved error msg.
	 *
	 * @param detail the detail
	 * @param btns the btns
	 * @param saveErrorMessage the save error message
	 * @param auditMessage the audit message
	 */
	private void handleClickEventsOnUnsavedErrorMsg(final MeasurePackageDetail detail, List<SecondaryButton> btns, final ErrorMessageDisplay saveErrorMessage
			, final String auditMessage) {
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SecondaryButton button = (SecondaryButton) event.getSource();
				// If Yes - do not navigate, set focus to the Save button on the Page and clear cell tree
				// // Else -do not navigate, set focus to the Save button on the Page
				if ("Yes".equals(button.getText())) {
					saveErrorMessage.clear();
					currentDetail = new MeasurePackageDetail();
					currentDetail = detail;
					clearMessages();
					setMeasurePackageDetailsOnView();
					
				} else if ("No".equals(button.getText())) {
					saveErrorMessage.clear();
					view.getPackageGroupingWidget().getSaveGrouping().setFocus(true);
				}
			}
		};
		for (SecondaryButton secondaryButton : btns) {
			secondaryButton.addClickHandler(clickHandler);
		}
	}
	
	/**
	 * Update supp data details from view.
	 *
	 * @param currentDetail the current detail
	 */
	public void updateSuppDataDetailsFromView(MeasurePackageDetail currentDetail) {
		currentDetail.setSuppDataElements(view.getQDMElementsInSuppElements());
		currentDetail.setQdmElements(view.getQDMElements());
		currentDetail.setToCompareSuppDataElements(dbSuppDataElements);
	}
	
	/**
	 * Update risk adj from view.
	 *
	 * @param currentDetail the current detail
	 */
	public void updateRiskAdjFromView(MeasurePackageDetail currentDetail){
		currentDetail.setRiskAdjClauses(view.getRiskAdjClauses());
		currentDetail.setRiskAdjVars(view.getRiskAdjVar());
		currentDetail.setToCompareRiskAdjVars(dbRiskAdjVars);
	}
	/**
	 * set Overview.
	 * @param result - MeasurePackageOverview.
	 */
	private void setOverview(MeasurePackageOverview result) {
		packageOverview = result;
		List <MeasurePackageClauseDetail> clauseList = new ArrayList<MeasurePackageClauseDetail>(result.getClauses());
		view.setClauses(clauseList);
		//SubTree Clauses
		view.setSubTreeClauseList(result.getSubTreeClauseList());
		// QDM elements
		view.setQDMElements(result.getQdmElements());
		List<MeasurePackageDetail> packageList = new ArrayList<MeasurePackageDetail>(result.getPackages());
		view.buildCellTable(packageList);
		if (result.getPackages().size() > 0) {
			if (currentDetail != null) {
				for (int i = 0; i < result.getPackages().size(); i++) {
					MeasurePackageDetail mpDetail = result.getPackages().get(i);
					if (mpDetail.getSequence().equalsIgnoreCase(
							currentDetail.getSequence())) {
						setMeasurePackage(result.getPackages().get(i)
								.getSequence());
					}
				}
			} else {
				setMeasurePackage(result.getPackages().get(0).getSequence());
			}
		} else {
			setNewMeasurePackage();
		}
		ReadOnlyHelper.setReadOnlyForCurrentMeasure(view.asWidget(),
				isEditable());
		view.setViewIsEditable(isEditable(), result.getPackages());
	}
	/**
	 * Delete Measure Package.
	 * @param pkg - MeasurePackageDetail.
	 */
	private void deleteMeasurePackage(final MeasurePackageDetail pkg) {
		MatContext.get().getPackageService()
		.delete(pkg, new AsyncCallback<Void>() {
			@Override
			public void onFailure(final Throwable caught) {
				view.getPackageErrorMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate()
						.getGenericErrorMessage());
			}
			@Override
			public void onSuccess(final Void result) {
				packageOverview.getPackages().remove(pkg);
				if (currentDetail.getSequence().equals(
						pkg.getSequence())) {
					currentDetail = null;
				}
				setOverview(packageOverview);
			}
		});
	}
	/**
	 * Checks if is editable.
	 *
	 * @return true, if is editable
	 */
	private boolean isEditable() {
		return MatContext.get().getMeasureLockService()
				.checkForEditPermission();
	}
	
	/**
	 * Gets the measure.
	 *
	 * @param measureId the measure id
	 * @return the measure
	 */
	private void getMeasure(final String measureId) {
		MatContext
		.get()
		.getMeasureService()
		.getMeasure(measureId,
				new AsyncCallback<ManageMeasureDetailModel>() {
			@Override
			public void onFailure(final Throwable caught) {
				view.getPackageErrorMessageDisplay()
				.setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
			@Override
			public void onSuccess(
					final ManageMeasureDetailModel result) {
				model = result;
				getMeasurePackageOverview(MatContext.get()
						.getCurrentMeasureId());
				displayMeasurePackageWorkspace();
			}
		});
	}
	/**
	 * set New MeasurePackage.
	 */
	private void setNewMeasurePackage() {
		currentDetail = new MeasurePackageDetail();
		currentDetail.setMeasureId(MatContext.get().getCurrentMeasureId());
		currentDetail.setSequence(Integer
				.toString(getMaxSequence(packageOverview) + 1));
		List<MeasurePackageDetail> packageList = new ArrayList<MeasurePackageDetail>(packageOverview.getPackages());
		view.buildCellTable(packageList);
		setMeasurePackageDetailsOnView();
	}
	/**
	 * Sets the measure package.
	 *
	 * @param measurePackageId the new measure package
	 */
	private void setMeasurePackage(final String measurePackageId) {
		for (MeasurePackageDetail detail : packageOverview.getPackages()) {
			if (detail.getSequence().equals(measurePackageId)) {
				currentDetail = detail;
				setMeasurePackageDetailsOnView();
				getItemCountListFromView(currentDetail.getPackageClauses());
				break;
			}
		}
	}
	
	/**
	 * Gets the item count list from view.
	 *
	 * @param packageClauses the package clauses
	 * @return the item count list from view
	 */
	public void getItemCountListFromView(List<MeasurePackageClauseDetail> packageClauses){
		for(int i=0; i<dbPackageClauses.size(); i++){
			dbPackageClauses.get(i).getDbItemCountList().addAll(packageClauses.get(i).getItemCountList());
			dbPackageClauses.get(i).setDbAssociatedPopulationUUID(packageClauses.get(i).getAssociatedPopulationUUID());
		}
		
	}
	/**
	 * setMeasurePackageDetailsOnView.
	 */
	private void setMeasurePackageDetailsOnView() {
		List<MeasurePackageClauseDetail> packageClauses = new ArrayList<MeasurePackageClauseDetail>(currentDetail
				.getPackageClauses());
		List<MeasurePackageClauseDetail> remainingClauses = removeClauses(
				packageOverview.getClauses(), packageClauses);
		view.setPackageName(currentDetail.getPackageName());
		view.setClausesInPackage(packageClauses);
		view.setClauses(remainingClauses);
		view.setQDMElementsInSuppElements(packageOverview.getSuppDataElements());
		view.setQDMElements(packageOverview.getQdmElements());
		view.setSubTreeInRiskAdjVarList(packageOverview.getRiskAdjList());
		view.setSubTreeClauseList(packageOverview.getSubTreeClauseList());
		dbPackageClauses.clear();
		dbPackageClauses.addAll(currentDetail.getPackageClauses());
		dbSuppDataElements.clear();
		dbSuppDataElements.addAll(packageOverview.getSuppDataElements());
		dbRiskAdjVars.clear();
		dbRiskAdjVars.addAll(packageOverview.getRiskAdjList());
		
	}
	
	/**
	 * Removes the clauses.
	 *
	 * @param master - master List of Clauses.
	 * @param toRemove - List from where to Remove.
	 * @return List.
	 */
	private List<MeasurePackageClauseDetail> removeClauses(
			final List<MeasurePackageClauseDetail> master,
			final List<MeasurePackageClauseDetail> toRemove) {
		List<MeasurePackageClauseDetail> newList = new ArrayList<MeasurePackageClauseDetail>();
		newList.addAll(master);
		for (MeasurePackageClauseDetail remove : toRemove) {
			for (int i = 0; i < newList.size(); i++) {
				if (newList.get(i).getId().equals(remove.getId())) {
					newList.remove(i);
					break;
				}
			}
		}
		return newList;
	}
	/**
	 * Display MeasurePackage Workspace.
	 */
	private void displayMeasurePackageWorkspace() {
		panel.clear();
		panel.add(view.asWidget());
		// view.setTabIndex();
	}
	/**
	 * Gets the max sequence.
	 *
	 * @param measurePackageOverview the measure package packageOverview
	 * @return the max sequence
	 */
	private int getMaxSequence(final MeasurePackageOverview measurePackageOverview) {
		int max = 0;
		for (MeasurePackageDetail detail : measurePackageOverview.getPackages()) {
			int seqInt = Integer.parseInt(detail.getSequence());
			if (seqInt > max) {
				max = seqInt;
			}
		}
		return max;
	}
	
	/**
	 * Gets the current detail.
	 *
	 * @return the current detail
	 */
	public MeasurePackageDetail getCurrentDetail() {
		return currentDetail;
	}
	
	/**
	 * Gets the view.
	 *
	 * @return the view
	 */
	public PackageView getView() {
		return view;
	}
	
	/**
	 * Validate package grouping.
	 */
	private void validatePackageGrouping(){
		
		MatContext.get().getMeasureService().validatePackageGrouping(model, new AsyncCallback<ValidateMeasureResult>(){
			
			@Override
			public void onFailure(Throwable caught) {
				Mat.hideLoadingMessage();
				((Button) view.getPackageMeasureButton()).setEnabled(true);
				((Button) view.getPackageMeasureAndExportButton()).setEnabled(true);
				view.getInProgressMessageDisplay().clear();
			}
			
			@Override
			public void onSuccess(ValidateMeasureResult result) {
				if (result.isValid()) {
					saveMeasureAtPackage();
					
				} else {
					Mat.hideLoadingMessage();
					view.getMeasurePackageWarningMsg().
					setMessage(result.getValidationMessages().get(0));
					view.getInProgressMessageDisplay().clear();
					((Button) view.getPackageMeasureButton()).setEnabled(true);
					((Button) view.getPackageMeasureAndExportButton()).setEnabled(true);
				}
			}
			
		});
		
	}
	
	/**
	 * Save measure at package.
	 */
	private void saveMeasureAtPackage(){
		
		MatContext.get().getMeasureService().saveMeasureAtPackage(model, new AsyncCallback<SaveMeasureResult>() {
			
			@Override
			public void onFailure(Throwable caught) {
				Mat.hideLoadingMessage();
				((Button) view.getPackageMeasureButton()).setEnabled(true);
				((Button) view.getPackageMeasureAndExportButton()).setEnabled(true);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				view.getInProgressMessageDisplay().clear();
			}
			
			/**
			 * On success.
			 *
			 * @param result the result
			 */
			@Override
			public void onSuccess(SaveMeasureResult result) {
				if (result.isSuccess()) {
					updateMeasureXmlForDeletedComponentMeasureAndOrg();
					
				} else {
					Mat.hideLoadingMessage();
					((Button) view.getPackageMeasureButton()).setEnabled(true);
					((Button) view.getPackageMeasureAndExportButton()).setEnabled(true);
					if (result.getFailureReason()
							== SaveMeasureResult.INVALID_VALUE_SET_DATE) {
						String message = MatContext.get()
								.getMessageDelegate()
								.getValueSetDateInvalidMessage();
						view.getErrorMessageDisplay().setMessage(message);
						((Button) view.getPackageMeasureButton()).setEnabled(true);
						view.getInProgressMessageDisplay().clear();
					}
				}
				
				
			}
		});
		
	}
	
	/**
	 * Update component measures from xml.
	 */
	private void updateMeasureXmlForDeletedComponentMeasureAndOrg(){
		
		MatContext.get().getMeasureService().updateMeasureXmlForDeletedComponentMeasureAndOrg(model.getId(), new AsyncCallback<Void>() {
			
			@Override
			public void onFailure(Throwable caught) {
				Mat.hideLoadingMessage();
				((Button) view.getPackageMeasureButton()).setEnabled(true);
				((Button) view.getPackageMeasureAndExportButton()).setEnabled(true);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				view.getInProgressMessageDisplay().clear();
			}
			
			@Override
			public void onSuccess(Void result) {
				String measureId = MatContext.get()
						.getCurrentMeasureId();
				if (view.getIncludeVSACData().getValue().equals(Boolean.TRUE)) {
					updateValueSetsBeforePackaging(measureId);
				} else {
					validateMeasureAndExport(measureId, null);
				}
			}
		});
	}
	
	/**
	 * Service call to VSAC to update Measure Xml before invoking simple xml and value set sheet generation.
	 * @param measureId - String.
	 */
	private void updateValueSetsBeforePackaging(final String measureId) {
		vsacapiServiceAsync.updateAllVSACValueSetsAtPackage(measureId,
				new AsyncCallback<VsacApiResult>() {
			
			@Override
			public void onFailure(final Throwable caught) {
				Mat.hideLoadingMessage();
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				((Button) view.getPackageMeasureButton()).setEnabled(true);
				((Button) view.getPackageMeasureAndExportButton()).setEnabled(true);
				view.getInProgressMessageDisplay().clear();
			}
			
			@Override
			public void onSuccess(final VsacApiResult result) {
				validateMeasureAndExport(measureId,
						result);
			}
		});
	}
	
	/**
	 * Service Call to generate Simple Xml and value set sheet.
	 * @param measureId - String.
	 * @param updateVsacResult - VsacApiResult.
	 */
	private void validateMeasureAndExport(final String measureId,
			final VsacApiResult updateVsacResult) {
		List<MatValueSet> vsacResponse = null;
		if (updateVsacResult != null) {
			vsacResponse = updateVsacResult.getVsacResponse();
		}
		
		MatContext.get().getMeasureService()
		.validateMeasureForExport(measureId, vsacResponse,
				new AsyncCallback<ValidateMeasureResult>() {
			@Override
			public void onFailure(final Throwable caught) {
				Mat.hideLoadingMessage();
				((Button) view.getPackageMeasureAndExportButton()).setEnabled(true);
				view.getInProgressMessageDisplay().clear();
				((Button) view.getPackageMeasureButton()).setEnabled(true);
				view.getPackageErrorMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate().getUnableToProcessMessage());
			}
			
			@Override
			public void onSuccess(final ValidateMeasureResult result) {
				Mat.hideLoadingMessage();
				if (updateVsacResult != null) {
					if (result.isValid() && updateVsacResult.isSuccess()) {
						if(updateVsacResult.getRetrievalFailedOIDs().size() > 0){
							if (isMeasurePackageExportSuccess) {
								((Button) view.getPackageMeasureButton()).setEnabled(true);
								saveExport();
							} else {
								view.getMeasurePackageSuccessMsg()
								.setAmberMessage(MatContext.get().getMessageDelegate()
										.getPackageSuccessAmberMessage());
								((Button) view.getPackageMeasureButton()).setEnabled(true);
								view.getInProgressMessageDisplay().clear();
								((Button) view.getPackageMeasureAndExportButton()).setEnabled(true);
							}
						} else {
							if (isMeasurePackageExportSuccess) {
								((Button) view.getPackageMeasureButton()).setEnabled(true);
								saveExport();
							} else {
								view.getMeasurePackageSuccessMsg()
								.setMessage(MatContext.get().getMessageDelegate()
										.getPackageSuccessMessage());
								((Button) view.getPackageMeasureButton()).setEnabled(true);
								((Button) view.getPackageMeasureAndExportButton()).setEnabled(true);
								view.getInProgressMessageDisplay().clear();
							}
						}
						
					} else if (result.isValid() && !updateVsacResult.isSuccess()) {
						if (updateVsacResult.getFailureReason()
								== VsacApiResult.UMLS_NOT_LOGGEDIN) {
							if (isMeasurePackageExportSuccess) {
								((Button) view.getPackageMeasureButton()).setEnabled(true);
								saveExport();
							} else {
								view.getMeasurePackageWarningMsg()
								.setMessage(MatContext.get().getMessageDelegate()
										.getMEASURE_PACKAGE_UMLS_NOT_LOGGED_IN());
								((Button) view.getPackageMeasureButton()).setEnabled(true);
								((Button) view.getPackageMeasureAndExportButton()).setEnabled(true);
								view.getInProgressMessageDisplay().clear();
							}
						}else if(VsacApiResult.VSAC_REQUEST_TIMEOUT == updateVsacResult.getFailureReason()){
							view.getMeasureErrorMessageDisplay()
							.setMessage(MatContext.get().getMessageDelegate()
									.getMEASURE_PACKAGE_VSAC_TIMEOUT());
							((Button) view.getPackageMeasureButton()).setEnabled(true);
							((Button) view.getPackageMeasureAndExportButton()).setEnabled(true);
							view.getInProgressMessageDisplay().clear();
						}
					} else {
						view.getMeasureErrorMessageDisplay()
						.setMessages(result.getValidationMessages());
						((Button) view.getPackageMeasureButton()).setEnabled(true);
						((Button) view.getPackageMeasureAndExportButton()).setEnabled(true);
						view.getInProgressMessageDisplay().clear();
					}
				} else {
					if (result.isValid()) {
						//to Export the Measure.
						if (isMeasurePackageExportSuccess) {
							((Button) view.getPackageMeasureButton()).setEnabled(true);
							saveExport();
						} else {
							view.getMeasurePackageSuccessMsg()
							.setMessage(MatContext.get().getMessageDelegate()
									.getPackageSuccessMessage());
							((Button) view.getPackageMeasureButton()).setEnabled(true);
							((Button) view.getPackageMeasureAndExportButton()).setEnabled(true);
							view.getInProgressMessageDisplay().clear();
						}
					} else {
						view.getMeasureErrorMessageDisplay()
						.setMessages(result.getValidationMessages());
						((Button) view.getPackageMeasureButton()).setEnabled(true);
						((Button) view.getPackageMeasureAndExportButton()).setEnabled(true);
						view.getInProgressMessageDisplay().clear();
					}
				}
			}
		});
	}
	
	/**
	 * Save export.
	 */
	private void saveExport() {
		((Button) view.getPackageMeasureAndExportButton()).setEnabled(false);
		String url = GWT.getModuleBaseURL() + "export?id=" + model.getId()
				+ "&format=zip";
		Window.Location.replace(url + "&type=save");
	}
	
	
	
}




