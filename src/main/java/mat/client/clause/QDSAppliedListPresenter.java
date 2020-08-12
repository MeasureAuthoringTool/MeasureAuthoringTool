package mat.client.clause;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.InProgressMessageDisplay;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Deprecated
/*
 * This class is for code that is non maintained anymore. It should not be changed. 
 */
public class QDSAppliedListPresenter implements MatPresenter {
	
	public static interface SearchDisplay {
		
		Widget asWidget();
		
		void buildCellTable(QDSAppliedListModel appliedListModel);
		
		List<QualityDataSetDTO> getAllAppliedQDMList();
		
		SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg();
		
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		Button getModifyButton();
		
		Button getRemoveButton();
		
		QualityDataSetDTO getSelectedElementToRemove();
		
		Button getUpdateVsacButton();
		
		void setAppliedQDMList(List<QualityDataSetDTO> appliedQDMList);
		
		InProgressMessageDisplay getInProgressMessageDisplay();
	}

	private List<QualityDataSetDTO> allQdsList = new ArrayList<QualityDataSetDTO>();

	private SimplePanel panel = new SimplePanel();

	private SearchDisplay searchDisplay;
	
	private MeasureServiceAsync service = MatContext.get().getMeasureService();

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
			}
		});
	}
	
	@Override
	public void beforeClosingDisplay() {
	}
	
	@Override
	public void beforeDisplay() {
		resetQDSFields();
		loadAppliedListData();
		MeasureComposerPresenter.setSubSkipEmbeddedLink("subQDMAPPliedListContainerPanel");
		Mat.focusSkipLists("MeasureComposer");
		
		
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
										.getOid()) || ConstantMessages.DEAD_OID.equals(qdsDTO
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
	
	
	@Override
	public Widget getWidget() {
		return panel;
	}
	
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
	
}
