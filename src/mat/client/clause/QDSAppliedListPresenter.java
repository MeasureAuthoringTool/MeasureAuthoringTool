package mat.client.clause;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.InProgressMessageDisplay;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * QDSAppliedListPresenter.java.
 */
public class QDSAppliedListPresenter implements MatPresenter {
	
	/**
	 * Search Display inner interface which is implemented by QDSAppliedListView
	 * class.
	 */
	public static interface SearchDisplay {
		
		/**
		 * As widget.
		 * 
		 * @return {@link Widget}
		 */
		Widget asWidget();
		
		/** Builds the cell Table.
		 * 
		 * @param appliedListModel - {@link QDSAppliedListModel} */
		void buildCellTable(QDSAppliedListModel appliedListModel);
		
		/**
		 * Gets the all applied qdm list.
		 * 
		 * @return {@link List} of {@link QualityDataSetDTO}
		 */
		List<QualityDataSetDTO> getAllAppliedQDMList();
		
		/**
		 * Gets the apply to measure success msg.
		 * 
		 * @return {@link SuccessMessageDisplayInterface}
		 */
		SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return {@link ErrorMessageDisplayInterface}
		 */
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Gets the modify button.
		 * 
		 * @return {@link Button}
		 */
		Button getModifyButton();
		
		/**
		 * Gets the removes the button.
		 * 
		 * @return {@link Button}
		 */
		Button getRemoveButton();
		
		/**
		 * Gets the selected element to remove.
		 * 
		 * @return {@link QualityDataSetDTO}
		 */
		QualityDataSetDTO getSelectedElementToRemove();
		
		/**
		 * Gets the update vsac button.
		 * 
		 * @return {@link Button}
		 */
		Button getUpdateVsacButton();
		
		/**
		 * Sets the applied qdm list.
		 * 
		 * @param appliedQDMList
		 *            the new applied qdm list {@link ArrayList} of
		 *            {@link QualityDataSetDTO}
		 */
		void setAppliedQDMList(List<QualityDataSetDTO> appliedQDMList);
		
		InProgressMessageDisplay getInProgressMessageDisplay();
	}
	/**
	 * List of all Applied QDM's.
	 */
	private List<QualityDataSetDTO> allQdsList = new ArrayList<QualityDataSetDTO>();
	/**
	 * SimplePanel Instance.
	 */
	private SimplePanel panel = new SimplePanel();
	
	/**
	 * SearchDisplay instance.
	 */
	private SearchDisplay searchDisplay;
	/**
	 * Measure Service Instance.
	 */
	private MeasureServiceAsync service = MatContext.get().getMeasureService();
	
	
	/**
	 * VSACAPIService Instance.
	 */
	private VSACAPIServiceAsync vsacapiServiceAsync = MatContext.get().getVsacapiServiceAsync();
	
	/**
	 * Instantiates a new qDS applied list presenter.
	 * 
	 * @param sDisplayArg
	 *            the s display arg {@link SearchDisplay}
	 */
	public QDSAppliedListPresenter(SearchDisplay sDisplayArg) {
		searchDisplay = sDisplayArg;
		getAppliedQDMList(true);
		searchDisplay.getRemoveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(final ClickEvent event) {
				resetQDSFields();
				searchDisplay.getInProgressMessageDisplay().setMessage("Loading Please Wait...");
				if (searchDisplay.getSelectedElementToRemove() != null) {
					service.getAppliedQDMFromMeasureXml(MatContext.get()
							.getCurrentMeasureId(), false,
							new AsyncCallback<QualityDataModelWrapper>() {
						
						@Override
						public void onFailure(final Throwable caught) {
							searchDisplay.getInProgressMessageDisplay().clear();
							Window.alert(MatContext.get()
									.getMessageDelegate()
									.getGenericErrorMessage());
							
						}
						
						@Override
						public void onSuccess(
								final QualityDataModelWrapper result) {
							allQdsList = result.getQualityDataDTO();
							if (allQdsList.size() > 0) {
								Iterator<QualityDataSetDTO> iterator = allQdsList
										.iterator();
								while (iterator.hasNext()) {
									QualityDataSetDTO dataSetDTO = iterator
											.next();
									if (dataSetDTO
											.getUuid()
											.equals(searchDisplay
													.getSelectedElementToRemove()
													.getUuid())) {
										iterator.remove();
									}
								}
								searchDisplay
								.setAppliedQDMList(allQdsList);
								saveMeasureXML(allQdsList);
							}
							
						}
						
					});
				} else {
					searchDisplay
					.getErrorMessageDisplay()
					.setMessage(
							"Please select at least one unused value set to delete.");
				}
			}
			
		});
		
		searchDisplay.getModifyButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				searchDisplay.getApplyToMeasureSuccessMsg().clear();
				searchDisplay.getErrorMessageDisplay().clear();
				QualityDataSetDTO dataSetDTO = searchDisplay
						.getSelectedElementToRemove();
				if (dataSetDTO != null) {
					QDMAvailableValueSetWidget availableValueSetWidget = new QDMAvailableValueSetWidget();
					QDMAvailableValueSetPresenter availableValueSetPresenter = new QDMAvailableValueSetPresenter(
							availableValueSetWidget, dataSetDTO, searchDisplay);
					availableValueSetPresenter.beforeDisplay();
				}
			}
		});
		
		searchDisplay.getUpdateVsacButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				resetQDSFields();
				searchDisplay.getInProgressMessageDisplay().setMessage("Loading Please Wait...");
			//	updateVSACValueSets();
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		resetQDSFields();
		loadAppliedListData();
		MeasureComposerPresenter.setSubSkipEmbeddedLink("subQDMAPPliedListContainerPanel");
		Mat.focusSkipLists("MeasureComposer");
		
		
	}
	
	/**
	 * Method to get Message against failure reason.
	 * @param id
	 *            - {@link Integer}
	 * @return {@link String}
	 */
	private String convertMessage(int id) {
		String message;
		switch (id) {
			case VsacApiResult.UMLS_NOT_LOGGEDIN:
				message = MatContext.get().getMessageDelegate()
				.getUMLS_NOT_LOGGEDIN();
				break;
			default:
				message = MatContext.get().getMessageDelegate()
				.getUnknownFailMessage();
		}
		return message;
	}
	
	/**
	 * Method to put view on Main Panel.
	 */
	private void displaySearch() {
		panel.clear();
		if (MatContext.get().isCurrentMeasureEditable()) {
			searchDisplay.getUpdateVsacButton().setEnabled(true);
		} else {
			searchDisplay.getUpdateVsacButton().setEnabled(false);
		}
		panel.add(searchDisplay.asWidget());
	}
	
	/**
	 * Method for fetching all applied Value Sets in a measure which is loaded
	 * in context.
	 * 
	 * @param checkForSupplementData
	 *            - {@link Boolean}
	 * @return the applied qdm list
	 */
	public final void getAppliedQDMList(boolean checkForSupplementData) {
		String measureId = MatContext.get().getCurrentMeasureId();
		if ((measureId != null) && !measureId.equals("")) {
			service.getAppliedQDMFromMeasureXml(measureId,
					checkForSupplementData,
					new AsyncCallback<QualityDataModelWrapper>() {
				
				private void filterTimingQDMs(
						List<QualityDataSetDTO> result) {
					List<QualityDataSetDTO> timingQDMs = new ArrayList<QualityDataSetDTO>();
					
					for (QualityDataSetDTO qdsDTO : result) {
						if ("Timing Element".equals(qdsDTO
								.getDataType()) || ConstantMessages.BIRTHDATE_OID.equals(qdsDTO
										.getOid()) || ConstantMessages.EXPIRED_OID.equals(qdsDTO
												.getOid())) {
							timingQDMs.add(qdsDTO);
						}
					}
					result.removeAll(timingQDMs);
				}
				
				@Override
				public void onFailure(final Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate()
							.getGenericErrorMessage());
				}
				
				@Override
				public void onSuccess(
						final QualityDataModelWrapper result) {
					QDSAppliedListModel appliedListModel = new QDSAppliedListModel();
					filterTimingQDMs(result.getQualityDataDTO());
					appliedListModel.setAppliedQDMs(result.getQualityDataDTO());
					searchDisplay.buildCellTable(appliedListModel);
					searchDisplay.setAppliedQDMList(result.getQualityDataDTO());
				}
			});
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return panel;
	}
	
	/**
	 * Method to load Applied QDM's List in to view.
	 */
	final void loadAppliedListData() {
		panel.clear();
		getAppliedQDMList(true);
		displaySearch();
	}
	
	/**
	 * Method to reset Success and Error Messages.
	 */
	public final void resetQDSFields() {
		searchDisplay.getApplyToMeasureSuccessMsg().clear();
		searchDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getInProgressMessageDisplay().clear();
	}
	
	/**
	 * Service Call to create and save QDM's in Element Look Up tag.
	 * @param list
	 *            - {@link ArrayList} of {@link QualityDataSetDTO}
	 */
	private void saveMeasureXML(final List<QualityDataSetDTO> list) {
		service.createAndSaveElementLookUp(list, MatContext.get()
				.getCurrentMeasureId(), null, new AsyncCallback<Void>() {
			
			@Override
			public void onFailure(final Throwable caught) {
				searchDisplay.getInProgressMessageDisplay().clear();
				Window.alert(MatContext.get().getMessageDelegate()
						.getGenericErrorMessage());
				
			}
			
			@Override
			public void onSuccess(final Void result) {
				allQdsList.removeAll(allQdsList);
				resetQDSFields();
				loadAppliedListData();
				searchDisplay.getApplyToMeasureSuccessMsg().setMessage(
						"Successfully removed selected QDM element.");
				
			}
		});
	}
	
	/**
	 * Method to show/hide Loading please wait message.
	 * @param busy
	 *            - {@link Boolean}
	 */
	private void showSearchingBusy(final boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		searchDisplay.getUpdateVsacButton().setEnabled(!busy);
	}
	
	/**
	 * Service Call to updateVsacValueSet.
	 */
	private void updateVSACValueSets() {
		//showSearchingBusy(true);
		
	}
	
}
