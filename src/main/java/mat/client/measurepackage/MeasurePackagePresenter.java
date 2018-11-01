package mat.client.measurepackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.constants.AlertType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.QDSAppliedListModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.measurepackage.MeasurePackagerView.Observer;
import mat.client.measurepackage.service.MeasurePackageSaveResult;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatContext;
import mat.client.shared.MeasurePackageClauseCellListWidget;
import mat.client.shared.MessageAlert;
import mat.client.shared.MessageDelegate;
import mat.client.shared.ReadOnlyHelper;
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.client.shared.WarningMessageAlert;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.client.umls.service.VsacTicketInformation;
import mat.model.MatValueSet;
import mat.model.QualityDataSetDTO;
import mat.model.RiskAdjustmentDTO;
import mat.model.cql.CQLDefinition;
import mat.shared.MeasurePackageClauseValidator;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.bonnie.error.BonnieUnauthorizedException;
import mat.shared.bonnie.error.UMLSNotActiveException;
import mat.shared.bonnie.result.BonnieUserInformationResult;
import mat.shared.packager.error.SaveRiskAdjustmentVariableException;
import mat.shared.packager.error.SaveSupplementalDataElementException;

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
	
	/** The db cql supp data elements. */
	private List<CQLDefinition> dbCQLSuppDataElements = new ArrayList<CQLDefinition>();
	
	/** The db risk adj vars. */
	private List<RiskAdjustmentDTO> dbRiskAdjVars = new ArrayList<RiskAdjustmentDTO>();
	
	/** The is measure package success. */
	private boolean isMeasurePackageAndExport = false;
	
	private boolean isExportToBonnie = false;
	
	private static final String SIGN_INTO_UMLS = "Please sign into UMLS";
	
	private static final String SIGN_INTO_BONNIE_MESSAGE = "Please sign into Bonnie.";
	
	private VsacTicketInformation vsacInfo = null;
	
	/**
	 * Gets the db cql supp data elements.
	 * 
	 * @return the dbCQLSuppDataElements
	 */
	public List<CQLDefinition> getDbCQLSuppDataElements() {
		return dbCQLSuppDataElements;
	}

	/**
	 * Sets the db cql supp data elements.
	 *
	 * @param dbCQLSuppDataElements the dbCQLSuppDataElements to set
	 */
	public void setDbCQLSuppDataElements(List<CQLDefinition> dbCQLSuppDataElements) {
		this.dbCQLSuppDataElements = dbCQLSuppDataElements;
	}

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
	
	/**
	 * The Interface View.
	 */
	public static interface PackageView {
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		ErrorMessageAlert getErrorMessageDisplay();
		/**
		 * Gets the measure package success msg.
		 * 
		 * @return the measure package success msg
		 */
		MessageAlert getMeasurePackageSuccessMsg();
		
		/**
		 * Gets the measure package warning msg.
		 * 
		 * @return the measure package warning msg
		 */
		WarningMessageAlert getMeasurePackageWarningMsg();
		
		/**
		 * Gets the package error message display.
		 * 
		 * @return the package error message display
		 */
		ErrorMessageAlert getPackageErrorMessageDisplay();
		
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
		MessageAlert getPackageSuccessMessageDisplay();
		
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
		 * Sets the CQL qDM elements in supp elements.
		 *
		 * @param clauses the new CQL qDM elements in supp elements
		 */
		void setCQLElementsInSuppElements(List<CQLDefinition> clauses);
		
		/**
		 * Gets the qDM elements in supp elements.
		 *
		 * @return the qDM elements in supp elements
		 */
		List<QualityDataSetDTO> getQDMElementsInSuppElements();
		
		/**
		 * Gets the CQL qDM elements in supp elements.
		 *
		 * @return the CQL qDM elements in supp elements
		 */
		List<CQLDefinition> getCQLElementsInSuppElements();
		
		/**
		 * Sets the cql qDM elements.
		 *
		 * @param clauses the new cql qDM elements
		 */
		void setCQLQDMElements(List<CQLDefinition> clauses);
		
		/**
		 * Gets the cql qDM elements.
		 *
		 * @return the cql qDM elements
		 */
		List<CQLDefinition> getCQLQDMElements();
		
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
		MessageAlert getSupplementalDataElementSuccessMessageDisplay();
		
		MessageAlert getSupplementalDataElementErrorMessageDisplay(); 
		
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
		Button getCreateNewButton();
		
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
		WarningConfirmationMessageAlert getSaveErrorMessageDisplay();
		/**
		 * Gets the include vsac data.
		 * 
		 * @return the include vsac data
		 */

		/**
		 * Gets the measure error message display.
		 *
		 * @return the measure error message display
		 */
		MessageAlert getMeasureErrorMessageDisplay();
		
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
		MessageAlert getRiskAdjustmentVariableSuccessMessageDisplay();
		
		MessageAlert getRiskAdjustmentVariableErrorMessageDisplay();

		
		/**
		 * Sets the sub tree in risk adj var list.
		 *
		 * @param riskAdjClauseList the new sub tree in risk adj var list
		 */
		void setSubTreeInRiskAdjVarList(
				List<RiskAdjustmentDTO> riskAdjClauseList);
		
		/**
		 * Gets the in progress message display.
		 *
		 * @return the in progress message display
		 */
		MessageAlert getInProgressMessageDisplay();
		
		/**
		 * Sets the CQL measure.
		 *
		 * @param isCQLMeasure the new CQL measure
		 */
		void setCQLMeasure(boolean isCQLMeasure);
		
		/**
		 * Sets the risk adjust label.
		 *
		 * @param isCQLMeasure the new risk adjust label
		 */
		void setRiskAdjustLabel(boolean isCQLMeasure);
		
		void setQdmElementsLabel(boolean isCQLMeasure);
		WarningConfirmationMessageAlert getSaveErrorMessageDisplayOnEdit();
		void setSaveErrorMessageDisplayOnEdit(WarningConfirmationMessageAlert saveErrorMessageDisplayOnEdit);
		public Panel getCellTablePanel();
		public void setCellTablePanel(Panel cellTablePanel);
		HasClickHandlers getPackageMeasureAndExportToBonnieButton();
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
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
					clearMessages();
					view.getPackageGroupingWidget().getAddAssociationsPanel().setVisible(false);
					setNewMeasurePackage();
				}
			}
		});
		
		view.getPackageMeasureButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
					clearMessages();
					enablePackageButtons(false);
					isMeasurePackageAndExport = false;
					isExportToBonnie = false;
					view.getInProgressMessageDisplay().createAlert("Loading Please Wait...");
					validateGroup();
					clearMessages(); 
				}
			}
		});
		
		view.getPackageMeasureAndExportButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				enablePackageButtons(false);
				isMeasurePackageAndExport = true;
				isExportToBonnie = false;
				view.getInProgressMessageDisplay().createAlert("Loading Please Wait...");
				validateGroup();
				clearMessages(); 
			}
		});
		
		view.getPackageMeasureAndExportToBonnieButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				enablePackageButtons(false);
				isMeasurePackageAndExport = false;
				isExportToBonnie = true;
				view.getInProgressMessageDisplay().createAlert("Loading Please Wait...");
				validateUMLSLogIn();
				clearMessages(); 
			}
		});
		
		
		view.getaddRiskAdjVariablesToMeasure().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showMeasurePackagerBusy(true);
				clearMessages();
				updateRiskAdjFromView(currentDetail);
				((Button) view.getPackageMeasureButton()).setEnabled(true);
				MatContext.get().getPackageService().saveRiskVariables(currentDetail, new AsyncCallback<Void>() {
					@Override
					public void onFailure(final Throwable caught) {
						if(caught instanceof SaveRiskAdjustmentVariableException) {
							getMeasurePackageOverview(MatContext.get().getCurrentMeasureId());
							view.getRiskAdjustmentVariableErrorMessageDisplay().createAlert(caught.getLocalizedMessage());
						} else {
							view.getRiskAdjustmentVariableErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getUnableToProcessMessage());
						}
						
						Mat.hideLoadingMessage();
						showMeasurePackagerBusy(false);
					}

					@Override
					public void onSuccess(final Void result) {
						getMeasurePackageOverview(MatContext.get().getCurrentMeasureId());
						view.getRiskAdjustmentVariableSuccessMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getRiskAdjSavedMessage());
						showMeasurePackagerBusy(false);
					}
				});

			}
		});

		view.getAddQDMElementsToMeasureButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				showMeasurePackagerBusy(true);
				clearMessages();
				((Button) view.getPackageMeasureButton()).setEnabled(true);
				updateSuppDataDetailsFromView(currentDetail);
				MatContext.get().getPackageService().saveQDMData(currentDetail, new AsyncCallback<Void>() {
					@Override
					public void onFailure(final Throwable caught) {
						view.getSupplementalDataElementSuccessMessageDisplay().setType(AlertType.DANGER);
						if(caught instanceof SaveSupplementalDataElementException) {
							getMeasurePackageOverview(MatContext.get().getCurrentMeasureId());
							view.getSupplementalDataElementErrorMessageDisplay().createAlert(caught.getLocalizedMessage());
						}
						
						else {
							view.getSupplementalDataElementErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getUnableToProcessMessage());
						}
						

						showMeasurePackagerBusy(false);
					}

					@Override
					public void onSuccess(final Void result) {
						getMeasurePackageOverview(MatContext.get().getCurrentMeasureId());
						view.getSupplementalDataElementSuccessMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getSuppDataSavedMessage());
						showMeasurePackagerBusy(false);
					}
				});
			}
		});
		
		view.getPackageGroupingWidget().getSaveGrouping().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				clearMessages();
				((Button) view.getPackageMeasureButton()).setEnabled(true);
				final MeasurePackageDetail tempMeasurePackageDetails = new MeasurePackageDetail(currentDetail);
				updateDetailsFromView(tempMeasurePackageDetails);
			
				if (isValid()) {
					showMeasurePackagerBusy(true);
					MatContext.get().getPackageService()
					.save(tempMeasurePackageDetails, new AsyncCallback<MeasurePackageSaveResult>() {
						@Override
						public void onFailure(final Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							showMeasurePackagerBusy(false);
						}
						
						@Override
						public void onSuccess(final MeasurePackageSaveResult result) {
							if (result.isSuccess()) {
								updateDetailsFromView(currentDetail);
								getMeasurePackageOverview(MatContext.get()
										.getCurrentMeasureId());
								view.getPackageSuccessMessageDisplay().createAlert(
										MatContext.get().getMessageDelegate().
										getGroupingSavedMessage());
								
								showMeasurePackagerBusy(false);

								
							} else {
								if (result.getMessages().size() > 0) {
									view.getPackageErrorMessageDisplay().
									createAlert(result.getMessages());
								} else {
									view.getPackageErrorMessageDisplay().clearAlert();
								}
							}
							
						}
					});
					
					showMeasurePackagerBusy(false);
				}
			}
		});
	}
	
	protected void validateUMLSLogIn() {
		MatContext.get().getVsacapiServiceAsync().getTicketGrantingToken(new AsyncCallback<VsacTicketInformation>() {
			
			@Override
			public void onSuccess(VsacTicketInformation result) {
				if(result == null) {
					
					Mat.hideLoadingMessage();
					enablePackageButtons(true);
					view.getMeasureErrorMessageDisplay().createAlert(SIGN_INTO_UMLS);
					view.getInProgressMessageDisplay().clearAlert();
				}
				else {
					vsacInfo = result;
					validateGroup();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Mat.hideLoadingMessage();
				enablePackageButtons(true);
				view.getErrorMessageDisplay().createAlert(
						(MatContext.get().getMessageDelegate().getGenericErrorMessage()));
				view.getInProgressMessageDisplay().clearAlert();
			}

			
		});
	}
	
	private void enablePackageButtons(boolean enable) {
		((Button) view.getPackageMeasureButton()).setEnabled(enable);
		((Button) view.getPackageMeasureAndExportButton()).setEnabled(enable);
		if(!enable) {
			((Button) view.getPackageMeasureAndExportToBonnieButton()).setEnabled(enable);
		} else {
			checkBonnieLogIn();
		}
	}
	
	protected void validateGroup() {
		MatContext.get().getMeasureService().validateForGroup(model,new AsyncCallback<ValidateMeasureResult>() {
			@Override
			public void onFailure(final Throwable caught) {
				
				Mat.hideLoadingMessage();
				enablePackageButtons(true);
				view.getPackageErrorMessageDisplay().createAlert(
						MatContext.get().getMessageDelegate().getUnableToProcessMessage());
				view.getInProgressMessageDisplay().clearAlert();
			}
			
			@Override
			public void onSuccess(final ValidateMeasureResult result) {
				Mat.showLoadingMessage();
				if(result.isValid()){
					validatePackageGrouping();
				}else {
					Mat.hideLoadingMessage();
					view.getInProgressMessageDisplay().clearAlert();
					view.getMeasureErrorMessageDisplay().createAlert(result.getValidationMessages());
					enablePackageButtons(true);
				}
			}
			
		});
	}
	
	private void validatePackageGrouping(){
		
		MatContext.get().getMeasureService().validatePackageGrouping(model, new AsyncCallback<ValidateMeasureResult>(){
			
			@Override
			public void onFailure(Throwable caught) {
				Mat.hideLoadingMessage();
				enablePackageButtons(true);
				view.getInProgressMessageDisplay().clearAlert();
			}
			
			@Override
			public void onSuccess(ValidateMeasureResult result) {
				if (result.isValid()) {
					updateMeasureXmlForDeletedComponentMeasureAndOrg();
				} else {
					Mat.hideLoadingMessage();
					if (result.getValidationMessages() != null) {
						view.getMeasurePackageWarningMsg().createWarningMultiLineAlert(result.getValidationMessages());
					}
					view.getInProgressMessageDisplay().clearAlert();
					enablePackageButtons(true);
				}
			}
			
		});
		
	}
	
	private void updateMeasureXmlForDeletedComponentMeasureAndOrg(){
		
		MatContext.get().getMeasureService().updateMeasureXmlForDeletedComponentMeasureAndOrg(model.getId(), new AsyncCallback<Void>() {
			
			@Override
			public void onFailure(Throwable caught) {
				Mat.hideLoadingMessage();
				enablePackageButtons(true);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				view.getInProgressMessageDisplay().clearAlert();
			}
			
			@Override
			public void onSuccess(Void result) {
				String measureId = MatContext.get().getCurrentMeasureId();
				validateExports(measureId);
			}
		});
	}
	
	private void validateExports(final String measureId) {
		MatContext.get().getMeasureService().validateExports(measureId, new AsyncCallback<ValidateMeasureResult>() {

			@Override
			public void onFailure(Throwable caught) {
				Mat.hideLoadingMessage();
				enablePackageButtons(true);
				view.getInProgressMessageDisplay().clearAlert();
				view.getPackageErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getUnableToProcessMessage());				
			}

			@Override
			public void onSuccess(ValidateMeasureResult result) {
				Mat.hideLoadingMessage();
				if(!result.isValid()) {
					handleUnsuccessfulPackage(result);
				} else {
					saveMeasureAtPackage();
				}
				
			}
		});
	}
	
private void saveMeasureAtPackage(){
		
		MatContext.get().getMeasureService().saveMeasureAtPackage(model, new AsyncCallback<SaveMeasureResult>() {
			
			@Override
			public void onFailure(Throwable caught) {
				Mat.hideLoadingMessage();
				enablePackageButtons(true);
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
					createExports(MatContext.get().getCurrentMeasureId());
					
				} else {
					Mat.hideLoadingMessage();
					enablePackageButtons(true);
					if (result.getFailureReason()
							== SaveMeasureResult.INVALID_VALUE_SET_DATE) {
						String message = MatContext.get()
								.getMessageDelegate()
								.getValueSetDateInvalidMessage();
						view.getErrorMessageDisplay().createAlert(message);
						((Button) view.getPackageMeasureButton()).setEnabled(true);
						view.getInProgressMessageDisplay().clearAlert();
					}
				}
			}
		});
		
	}

	private void createExports(final String measureId) {
		
		MatContext.get().getMeasureService().createExports(measureId, null, true, new AsyncCallback<ValidateMeasureResult>() {
			@Override
			public void onFailure(final Throwable caught) {
				Mat.hideLoadingMessage();
				enablePackageButtons(true);
				view.getInProgressMessageDisplay().clearAlert();
				view.getPackageErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getUnableToProcessMessage());
			}
			
			@Override
			public void onSuccess(final ValidateMeasureResult result) {
				Mat.hideLoadingMessage();
				if (result.isValid()) {
					//to Export the Measure.
					if (isMeasurePackageAndExport) {
						handleSuccessfulPackageAndExport();
					} else if(isExportToBonnie) {
						handleSuccessfulPackageAndExportToBonnie(measureId);
					}
					else {
						handleSuccessfulPackage();
					}
					recordMeasurePackageEvent(measureId);
				} else {
					handleUnsuccessfulPackage(result);
				}
			}
	
	
		});
	}
	private void handleSuccessfulPackageAndExport() {
		resetPackageButtonsAndMessages();
		saveExport();
	}
	
	private void handleUnsuccessfulPackage(final ValidateMeasureResult result) {
		view.getMeasureErrorMessageDisplay().createAlert(result.getValidationMessages());
		resetPackageButtonsAndMessages();
	}
	/**
	 * Valid grouping check.
	 * @return boolean.
	 */
	private boolean isValid() {
		List<MeasurePackageClauseDetail> detailList = view.getPackageGroupingWidget().getGroupingPopulationList();
		List<String> messages = new ArrayList<String>();
		MeasurePackageClauseValidator clauseValidator = new MeasurePackageClauseValidator();
		MeasurePackageClauseCellListWidget measurePackageClauseCellListWidget = new MeasurePackageClauseCellListWidget();
		messages = clauseValidator.isValidMeasurePackage(detailList);
		measurePackageClauseCellListWidget.checkForNumberOfStratification((ArrayList<MeasurePackageClauseDetail>) detailList, messages);
		if (messages.size() > 0) {
			view.getPackageErrorMessageDisplay().createAlert(messages);
		} else {
			view.getPackageErrorMessageDisplay().clearAlert();
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
	 * Clear messages.
	 */
	private void clearMessages() {
		view.getPackageSuccessMessageDisplay().clearAlert();
		view.getSupplementalDataElementSuccessMessageDisplay().clearAlert();
		view.getSupplementalDataElementErrorMessageDisplay().clearAlert(); 
		view.getPackageErrorMessageDisplay().clearAlert();
		view.getMeasurePackageSuccessMsg().clearAlert();
		view.getErrorMessageDisplay().clearAlert();
		view.getMeasurePackageWarningMsg().clearAlert();
		view.getMeasureErrorMessageDisplay().clearAlert();
		view.getSaveErrorMessageDisplay().clearAlert();
		view.getSaveErrorMessageDisplayOnEdit().clearAlert();
		view.getRiskAdjustmentVariableSuccessMessageDisplay().clearAlert();
		view.getRiskAdjustmentVariableErrorMessageDisplay().clearAlert(); 
		view.getInProgressMessageDisplay().clearAlert();
	}
	/**
	 * Display Empty.
	 */
	private void displayEmpty() {
		panel.clear();
		panel.add(view.asWidget());
		view.getPackageGroupingWidget().getAddAssociationsPanel().setVisible(false);
	}

	@Override
	public void beforeClosingDisplay() {
		currentDetail = null;
		packageOverview = null;
		view.getPackageGroupingWidget().getAddAssociationsPanel().setVisible(false);
	}

	@Override
	public void beforeDisplay() {
		view.getCellTablePanel().setVisible(false);
		view.getCreateNewButton().setVisible(false);
		showMeasurePackagerBusy(true);
		clearMessages();
		if ((MatContext.get().getCurrentMeasureId() != null)
				&& !MatContext.get().getCurrentMeasureId().equals("")) {
			
			MatContext.get().getMeasureService().getMeasureCQLFileData(MatContext.get().getCurrentMeasureId(),
					new AsyncCallback<SaveUpdateCQLResult>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							showMeasurePackagerBusy(false);
						}

						@Override
						public void onSuccess(SaveUpdateCQLResult result) {
														
							if(result.getCqlErrors().size() == 0){
								getMeasure(MatContext.get().getCurrentMeasureId());
							}else{
								panel.clear();
								panel.getElement().setId("MeasurePackagerContentFlowPanel");
								ErrorMessageAlert errorMessageAlert = new ErrorMessageAlert();
								panel.add(errorMessageAlert);
																
								MatContext.get().getMessageDelegate();
								errorMessageAlert.createAlert(MessageDelegate.getPACKAGER_CQL_ERROR());
							}		
							view.getPackageGroupingWidget().checkAssociations();
							showMeasurePackagerBusy(false);
						}
			});
			
			
		} else {
			displayEmpty();
		}
		
		MeasureComposerPresenter.setSubSkipEmbeddedLink("MeasurePackagerContentFlowPanel");
		Mat.focusSkipLists("MeasureComposer");
	}

	@Override
	public Widget getWidget() {
		panel.clear();
		panel.add(view.asWidget());
		return panel;
	}
	
	private void checkBonnieLogIn() {
		String matUserId = MatContext.get().getLoggedinUserId();
		MatContext.get().getBonnieService().getBonnieUserInformationForUser(matUserId, new AsyncCallback<BonnieUserInformationResult>() {
			
			@Override
			public void onSuccess(BonnieUserInformationResult result) {
				showMeasurePackagerBusy(false);
				((Button) view.getPackageMeasureAndExportToBonnieButton()).setEnabled(true);
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				showMeasurePackagerBusy(false);
				((Button) view.getPackageMeasureAndExportToBonnieButton()).setEnabled(false);
				
			}
		});
		
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
				.createAlert(
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
				clearMessages();
				if(!currentDetail.isEqual(view.getPackageGroupingWidget().getGroupingPopulationList(),
						dbPackageClauses)){
					
					view.getSaveErrorMessageDisplay().clearAlert();
					view.getSaveErrorMessageDisplayOnEdit().clearAlert();
					view.getSaveErrorMessageDisplayOnEdit().createAlert();
					view.getSaveErrorMessageDisplayOnEdit().getWarningConfirmationYesButton().setFocus(true);
					handleClickEventsOnUnsavedErrorMsg(detail, view.getSaveErrorMessageDisplayOnEdit(), null);
					
				} else {
					currentDetail = new MeasurePackageDetail();
					currentDetail = detail;
					setMeasurePackageDetailsOnView();
					getAssociationListFromView(currentDetail.getPackageClauses());
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
	 * Handle click events on unsaved error msg.
	 *
	 * @param detail the detail
	 * @param btns the btns
	 * @param saveErrorMessage the save error message
	 * @param auditMessage the audit message
	 */
	private void handleClickEventsOnUnsavedErrorMsg(final MeasurePackageDetail detail,  final WarningConfirmationMessageAlert saveErrorMessage
			, final String auditMessage) {
		
		
		saveErrorMessage.getWarningConfirmationYesButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				
				saveErrorMessage.clearAlert();
				currentDetail = new MeasurePackageDetail();
				currentDetail = detail;
				clearMessages();
				setMeasurePackageDetailsOnView();
			}
		});

		saveErrorMessage.getWarningConfirmationNoButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				saveErrorMessage.clearAlert();
				view.getPackageGroupingWidget().getSaveGrouping().setFocus(true);
			}
		});
	}
	
	/**
	 * Update supp data details from view.
	 *
	 * @param currentDetail the current detail
	 */
	public void updateSuppDataDetailsFromView(MeasurePackageDetail currentDetail) {
		currentDetail.setSuppDataElements(view.getQDMElementsInSuppElements());
		currentDetail.setCqlSuppDataElements(view.getCQLElementsInSuppElements());
		currentDetail.setQdmElements(view.getQDMElements());
		currentDetail.setCqlQdmElements(view.getCQLQDMElements());
		currentDetail.setToCompareSuppDataElements(dbSuppDataElements);
		currentDetail.setToCompareCqlSuppDataElements(dbCQLSuppDataElements);
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
		
		if(result.isComposite()) {
			// don't show the cell table or create new grouping button for composite measures. 
			view.getCellTablePanel().setVisible(false);
			view.getCreateNewButton().setVisible(false);
		} else {
			view.buildCellTable(packageList);
			view.getCreateNewButton().setVisible(true);
		}
		
		if (result.getPackages().size() > 0) {
			if (currentDetail != null) {
				for (int i = 0; i < result.getPackages().size(); i++) {
					MeasurePackageDetail mpDetail = result.getPackages().get(i);
					if (mpDetail.getSequence().equalsIgnoreCase(
							currentDetail.getSequence())) {
						setMeasurePackage(result.getPackages().get(i).getSequence());
					}
				}
			} else {
				setMeasurePackage(result.getPackages().get(0).getSequence());
			}
		} else {
			setNewMeasurePackage();
		}
		ReadOnlyHelper.setReadOnlyForCurrentMeasure(view.asWidget(), isEditable());
		view.setViewIsEditable(isEditable(), result.getPackages());
		checkBonnieLogIn();
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
				view.getPackageErrorMessageDisplay().createAlert(
						MatContext.get().getMessageDelegate()
						.getGenericErrorMessage());
			}
			@Override
			public void onSuccess(final Void result) {
				getMeasurePackageOverview(MatContext.get().getCurrentMeasureId());
				if (!packageOverview.getPackages().isEmpty()) {
					//Setting the first item in the list as selected item
					currentDetail = packageOverview.getPackages().get(0);
				}
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
				.createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
			@Override
			public void onSuccess(
					final ManageMeasureDetailModel result) {
				model = result;
				getMeasurePackageOverview(MatContext.get().getCurrentMeasureId());
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
		currentDetail.setSequence(Integer.toString(getMaxSequence(packageOverview) + 1));
		List<MeasurePackageDetail> packageList = new ArrayList<MeasurePackageDetail>(packageOverview.getPackages());
		
		if(packageOverview.isComposite()) {
			// don't show the cell table or create new grouping button for composite measures. 
			view.getCellTablePanel().setVisible(false);
			view.getCreateNewButton().setVisible(false);
		} else {
			view.buildCellTable(packageList);
			view.getCreateNewButton().setVisible(true);
		}
		
		setMeasurePackageDetailsOnView();
	}

	private void setMeasurePackage(final String measurePackageId) {
		for (MeasurePackageDetail detail : packageOverview.getPackages()) {
			if (detail.getSequence().equals(measurePackageId)) {
				currentDetail = detail;
				setMeasurePackageDetailsOnView();
				getAssociationListFromView(currentDetail.getPackageClauses());
				break;
			}
		}
	}
	
	public void getAssociationListFromView(List<MeasurePackageClauseDetail> packageClauses){
		for(int i=0; i<dbPackageClauses.size(); i++){
			dbPackageClauses.get(i).setDbAssociatedPopulationUUID(packageClauses.get(i).getAssociatedPopulationUUID());
		}
	}

	private void setMeasurePackageDetailsOnView() {
		List<MeasurePackageClauseDetail> packageClauses = new ArrayList<MeasurePackageClauseDetail>(currentDetail.getPackageClauses());
		List<MeasurePackageClauseDetail> remainingClauses = removeClauses(packageOverview.getClauses(), packageClauses);
		view.setPackageName(currentDetail.getPackageName());
		view.setClausesInPackage(packageClauses);
		view.setClauses(remainingClauses);
		if(packageOverview.getReleaseVersion() != null && MatContext.get().isCQLMeasure(packageOverview.getReleaseVersion())){
			view.setCQLMeasure(true);
			view.setRiskAdjustLabel(true);
			view.setQdmElementsLabel(true);
			//Set supple data to empty if CQL measure
			view.setQDMElementsInSuppElements(Collections.<QualityDataSetDTO>emptyList());
			view.setQDMElements(Collections.<QualityDataSetDTO>emptyList());
			view.setCQLElementsInSuppElements(packageOverview.getCqlSuppDataElements());
			view.setCQLQDMElements(packageOverview.getCqlQdmElements());
		}
		else {
			view.setCQLMeasure(false);
			view.setRiskAdjustLabel(false);
			view.setQdmElementsLabel(false);
			//Set CQL Suppl data to empty
			view.setCQLElementsInSuppElements(Collections.<CQLDefinition>emptyList());
			view.setCQLQDMElements(Collections.<CQLDefinition>emptyList());
			//Set QDM and Supplemental Data Elements.
			view.setQDMElementsInSuppElements(packageOverview.getSuppDataElements());
			view.setQDMElements(packageOverview.getQdmElements());	
		}

		view.setSubTreeInRiskAdjVarList(packageOverview.getRiskAdjList());
		view.setSubTreeClauseList(packageOverview.getSubTreeClauseList());
		dbPackageClauses.clear();
		dbPackageClauses.addAll(currentDetail.getPackageClauses());
		dbSuppDataElements.clear();
		dbSuppDataElements.addAll(packageOverview.getSuppDataElements());
		dbCQLSuppDataElements.clear();
		dbCQLSuppDataElements.addAll(packageOverview.getCqlSuppDataElements());
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
	private List<MeasurePackageClauseDetail> removeClauses(final List<MeasurePackageClauseDetail> master, final List<MeasurePackageClauseDetail> toRemove) {
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
	
	private void recordMeasurePackageEvent(final String measureId) {
		MatContext.get().getAuditService().recordMeasureEvent(measureId, "Measure Package Created", "", false, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Boolean result) {

			}
		});
	}

	private void handleSuccessfulPackage() {
		view.getMeasurePackageSuccessMsg().createAlert(MatContext.get().getMessageDelegate().getPackageSuccessMessage());
		resetPackageButtonsAndMessages();
	}

	private void resetPackageButtonsAndMessages() {
		enablePackageButtons(true);
		view.getInProgressMessageDisplay().clearAlert();
	}

	
	private void handleSuccessfulPackageAndExportToBonnie(String measureId) {
		String matUserId = MatContext.get().getLoggedinUserId();
		MatContext.get().getBonnieService().getUpdateOrUploadMeasureToBonnie(measureId, matUserId, vsacInfo, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				
				if(caught instanceof UMLSNotActiveException) {
					view.getMeasureErrorMessageDisplay().createAlert(SIGN_INTO_UMLS);
					Mat.hideUMLSActive(true);
				}
				if(caught instanceof BonnieUnauthorizedException) {
					view.getMeasureErrorMessageDisplay().createAlert(SIGN_INTO_BONNIE_MESSAGE);
					Mat.hideBonnieActive(true);
				} else {
					view.getMeasureErrorMessageDisplay().createAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				}
				Mat.hideLoadingMessage();
			}

			@Override
			public void onSuccess(String result) {
				getExportFromBonnieForMeasure(measureId, matUserId, result);
			}

		});
	}
	
	private void getExportFromBonnieForMeasure(String measureId, String matUserId, String successMessage) {
		String url = GWT.getModuleBaseURL() + "export?id=" + measureId + "&userId=" + matUserId + "&format=calculateBonnieMeasureResult";
		Window.open(url + "&type=open", "_blank", "");
		view.getMeasurePackageSuccessMsg().createAlert(successMessage);
		resetPackageButtonsAndMessages();
		Mat.hideLoadingMessage();
		
	}
	
	private void showMeasurePackagerBusy(boolean isBusy) {
		if(isBusy) {
			Mat.showLoadingMessage();
		}
		
		else {
			Mat.hideLoadingMessage(); 
		}
		
		view.setViewIsEditable(!isBusy, null);
	}
	
	
	/**
	 * Save export.
	 */
	private void saveExport() {
		String url = GWT.getModuleBaseURL() + "export?id=" + model.getId()
				+ "&format=zip";
		Window.Location.replace(url + "&type=save");
	}

	/**
	 * Checks if is measure package details same.
	 *
	 * @param measurePackagePresenter the measure package presenter
	 * @return true, if is measure package details same
	 */
	public boolean isMeasurePackageDetailsSame(){
		if(getCurrentDetail() == null){
			return true;
		}
		
		MeasurePackageDetail pageData = new MeasurePackageDetail();
		updateDetailsFromView(pageData);
		updateSuppDataDetailsFromView(pageData);
		updateRiskAdjFromView(pageData);
		MeasurePackageDetail dbData = getCurrentDetail();
		pageData.setToComparePackageClauses(pageData.getPackageClauses());
		dbData.setToComparePackageClauses(getDbPackageClauses());
		pageData.setToCompareSuppDataElements(pageData.getSuppDataElements());
		dbData.setToCompareSuppDataElements(getDbSuppDataElements());
		pageData.setToCompareCqlSuppDataElements(pageData.getCqlSuppDataElements());
		dbData.setToCompareCqlSuppDataElements(getDbCQLSuppDataElements());
		pageData.setToCompareRiskAdjVars(pageData.getRiskAdjVars());
		dbData.setToCompareRiskAdjVars(getDbRiskAdjVars());
		return pageData.equals(dbData);
	}

	public boolean isMeasurePackageValid() {
		return isMeasurePackageDetailsSame();
	}
	
}
