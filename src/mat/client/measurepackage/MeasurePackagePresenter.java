package mat.client.measurepackage;

import java.util.ArrayList;
import java.util.List;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.event.MeasureSelectedEvent;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.ReadOnlyHelper;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class MeasurePackagePresenter implements MatPresenter {
	private SimplePanel emptyPanel = new SimplePanel();

	private SimplePanel panel = new SimplePanel();
	public static interface MeasurePackageSelectionHandler {
		public void onSelection(MeasurePackageDetail detail);
	}
	
	
	public static interface View {
		public void setClauses(List<MeasurePackageClauseDetail> clauses);
		public void setClausesInPackage(List<MeasurePackageClauseDetail> list);
		public List<MeasurePackageClauseDetail> getClausesInPackage();
		public void setPackageName(String name);
		public HasClickHandlers getAddClausesToPackageButton();
		public HasClickHandlers getCreateNewButton();
		
		public void setMeasurePackages(List<MeasurePackageDetail> packages);
		public HasClickHandlers getPackageMeasureButton();
		public ErrorMessageDisplayInterface getPackageErrorMessageDisplay();
		public ErrorMessageDisplayInterface getMeasureErrorMessageDisplay();
		public SuccessMessageDisplayInterface getPackageSuccessMessageDisplay();
		public SuccessMessageDisplayInterface getSuppDataSuccessMessageDisplay();
		public SuccessMessageDisplayInterface getMeasurePackageSuccessMsg();
		public Widget asWidget();
		public void setSelectionHandler(MeasurePackageSelectionHandler handler);
		public void setDeletionHandler(MeasurePackageSelectionHandler handler);
		public void setViewIsEditable(boolean b);
		
		public String getValuesSetDate();
		public void setValuesSetDate(String date);
		public DateBoxWithCalendar getValuesSetDateInputBox();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		// QDM elements
		public void setQDMElementsInSuppElements(List<QualityDataSetDTO> list);
		public void setQDMElements(List<QualityDataSetDTO> clauses);
		public List<QualityDataSetDTO> getQDMElementsInSuppElements();
		public List<QualityDataSetDTO> getQDMElements();
		public HasClickHandlers getAddQDMElementsToMeasureButton();
		//public void setTabIndex();
		public ErrorMessageDisplayInterface getQDMErrorMessageDisplay();
	}
	
	
	private View view;
	private MeasurePackageDetail currentDetail;
	private ManageMeasureDetailModel model;
	private MeasurePackageOverview overview;
	
		
	public MeasurePackagePresenter(View viewArg) {
		this.view = viewArg;
		
		view.getCreateNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				setNewMeasurePackage();
				//view.setTabIndex();
			}
		});
		
		view.getPackageMeasureButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//O&M 17
				((Button)view.getPackageMeasureButton()).setEnabled(false);
				
				clearMessages();
				String valueSetDate = view.getValuesSetDate();
				if(isValidDate(valueSetDate)) {
					MeasureSelectedEvent mse = MatContext.get().getCurrentMeasureInfo();
					String msg = " [measure] "+mse.getMeasureName()+" [version] "+mse.getMeasureVersion()+" [package date] "+valueSetDate;
					String mid = mse.getMeasureId();
					MatContext.get().recordTransactionEvent(mid, null, "MEASURE_PACKAGE_EVENT", msg, ConstantMessages.DB_LOG);
					/*
					 * correction for packaging error thrown
					 * accessing MatContext fields individually first before using them
					 */
					model.setValueSetDate(view.getValuesSetDate());
					MatContext.get().getMeasureService().save(model, new AsyncCallback<SaveMeasureResult>() {
						
						@Override
						public void onSuccess(SaveMeasureResult result) { 
							if(result.isSuccess()) {
								Mat.showLoadingMessage();
								String measureId = MatContext.get().getCurrentMeasureId();
								MatContext.get().getMeasureService().validateMeasureForExport(measureId,
									new AsyncCallback<ValidateMeasureResult>() {
										@Override
										public void onFailure(Throwable caught) {
											//O&M 17
											((Button)view.getPackageMeasureButton()).setEnabled(true);
											
											Mat.hideLoadingMessage();
											view.getPackageErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getUnableToProcessMessage());
										}
			
										@Override
										public void onSuccess(ValidateMeasureResult result) {
											//O&M 17
											((Button)view.getPackageMeasureButton()).setEnabled(true);
											
											if(result.isValid()) {
												Mat.hideLoadingMessage();
												view.getMeasurePackageSuccessMsg().setMessage(MatContext.get().getMessageDelegate().getPackageSuccessMessage());
												
											}
											else {
												Mat.hideLoadingMessage();
												view.getMeasureErrorMessageDisplay().setMessages(result.getValidationMessages());
											}
										}
									});
							} else{
								//O&M 17
								((Button)view.getPackageMeasureButton()).setEnabled(true);
								
								if(result.getFailureReason() == SaveMeasureResult.INVALID_VALUE_SET_DATE){
									String message = MatContext.get().getMessageDelegate().getValueSetDateInvalidMessage();
									view.getErrorMessageDisplay().setMessage(message);
								}
							}
						}
						@Override
						public void onFailure(Throwable caught) {
							//O&M 17
							((Button)view.getPackageMeasureButton()).setEnabled(true);
							
							view.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
							MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
						}
					});						
						
				}
			}
		});
		
		view.getAddClausesToPackageButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				updateDetailsFromView();
			
				if(isValid()) {
					MatContext.get().getPackageService().save(currentDetail, new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							view.getQDMErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getUnableToProcessMessage());
						}
						@Override
						public void onSuccess(Void result) {
							//process result of save op
							//and set error message if needed 
							//view.getPackageErrorMessageDisplay()
							if(!overview.getPackages().contains(currentDetail)) {
								overview.getPackages().add(currentDetail);
								setOverview(overview);
							}
							view.getPackageSuccessMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGroupingSavedMessage());
						}
					});
				}
			}
		});
		
		// QDM elements
		view.getAddQDMElementsToMeasureButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				updateSuppDataDetailsFromView();
				MatContext.get().getPackageService().saveQDMData(currentDetail, new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						view.getPackageErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getUnableToProcessMessage());
					}
					@Override
					public void onSuccess(Void result) {
						//process result of save op
						//and set error message if needed 
						//view.getPackageErrorMessageDisplay()
						if(!overview.getPackages().contains(currentDetail)) {
							overview.getPackages().add(currentDetail);
							setOverview(overview);
						}
						view.getSuppDataSuccessMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getSuppDataSavedMessage());
					}
				});
			}
		});
	}
	
	private void clearMessages() {
		view.getPackageSuccessMessageDisplay().clear();
		view.getSuppDataSuccessMessageDisplay().clear();
		view.getPackageErrorMessageDisplay().clear();
		view.getMeasureErrorMessageDisplay().clear();
		view.getMeasurePackageSuccessMsg().clear();
		view.getErrorMessageDisplay().clear();
	}
	@Override
	public void beforeDisplay() {
		Mat.hideLoadingMessage();
		clearMessages();
		
		if(MatContext.get().getCurrentMeasureId() != null && !MatContext.get().getCurrentMeasureId().equals("")){
			getMeasure(MatContext.get().getCurrentMeasureId());
			Command isSavingCmd = new Command() {
	    	   public void execute() {
	    		  //Call getMeasurePackageOverview once the clauses saving process is done
	    		  if((!MatContext.get().getSynchronizationDelegate().isSavingClauses())){
	    			  getMeasurePackageOverview(MatContext.get().getCurrentMeasureId());
	    		  }else{
	    			  DeferredCommand.addCommand(this);
	    		  }
	    	   }
	    	};
	    	isSavingCmd.execute();
	    	displayMeasurePackageWorkspace();
		}else{
			displayEmpty();
		}
		MeasureComposerPresenter.setSubSkipEmbeddedLink("MeasurePackage");
		Mat.focusSkipLists("MeasureComposer");
	}
	@Override 
	public void beforeClosingDisplay() {
		
	}

	private void setMeasurePackage(String measurePackageId) {
		for(MeasurePackageDetail detail : overview.getPackages()) {
			if(detail.getSequence().equals(measurePackageId)) {
				currentDetail = detail;
				setMeasurePackageDetailsOnView();
				break;
			}
		}
	}
	
	private boolean isValidDate(String valueSetDate) {
		List<String> message = new ArrayList<String>();
		if(valueSetDate == null || "".equals(valueSetDate.trim())) {
			message.add(MatContext.get().getMessageDelegate().getValueSetDateRequiredMessage());
		} /*else if (!isValidDateCheck(valueSetDate)) {
			message.add(MatContext.get().getMessageDelegate().getValueSetDateInvalidMessage());
		}*/
		
		boolean valid = message.size() == 0;
		if(!valid) {
			view.getErrorMessageDisplay().setMessages(message);
		}
		else {
			view.getErrorMessageDisplay().clear();
		}
		return valid;
	}
	
	
	private boolean isValid() {
		List<MeasurePackageClauseDetail> detailList = view.getClausesInPackage();
		List<String> messages = new ArrayList<String>();
		
		String scoring = MatContext.get().getCurrentMeasureScoringType();
		
		//TODO refactor this into a common shared class so the server can use it for validation also
		if(ConstantMessages.CONTINUOUS_VARIABLE_SCORING.equalsIgnoreCase(scoring)){
			if((countDetailsWithType(detailList, ConstantMessages.POPULATION_CONTEXT_ID) != 1) 
				||(countDetailsWithType(detailList, ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) != 1)
				||(countDetailsWithType(detailList, ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 1)
				){
						messages.add(MatContext.get().getMessageDelegate().getContinuousVariableWrongNumMessage());
			}
		
			if((countDetailsWithType(detailList, ConstantMessages.NUMERATOR_CONTEXT_ID) != 0)
				|| (countDetailsWithType(detailList, ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) != 0)
				||	(countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_CONTEXT_ID) != 0) 
				|| (countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) != 0)
			    || (countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) != 0) 
			) {
				messages.add(MatContext.get().getMessageDelegate().getContinuousVariableMayNotContainMessage());
			}
			
		} 
		/* PROPORTION
			at least one and only one
			Population, Denominator 
			at least one or more
			Numerator
			zero or one
			Denominator Exclusions
			Denominator Exceptions
			and no Numerator Exclusions, Measure Population, Measure Observations
		 */
		else if(ConstantMessages.PROPORTION_SCORING.equalsIgnoreCase(scoring)){
			/* at least one and only one
			   Population, Denominator */
			if((countDetailsWithType(detailList, ConstantMessages.POPULATION_CONTEXT_ID) != 1)
					|| (countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_CONTEXT_ID) != 1)
			){
				messages.add(MatContext.get().getMessageDelegate().getProportionWrongNumMessage());
			}
			/* at least one or more
			   Numerator */
			if((countDetailsWithType(detailList, ConstantMessages.NUMERATOR_CONTEXT_ID) < 1)
			){
				messages.add(MatContext.get().getMessageDelegate().getProportionTooFewMessage());
			}
			/* zero or one
			   Denominator Exclusions, Denominator Exceptions */
			if((countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) > 1)
					|| (countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) > 1)
			){
				messages.add(MatContext.get().getMessageDelegate().getProportionTooManyMessage());
			}
			/* no Numerator Exclusions, Measure Population, Measure Observations */
			if((countDetailsWithType(detailList, ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) != 0)
				|| (countDetailsWithType(detailList, ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 0)
				|| (countDetailsWithType(detailList, ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) != 0)
				){
				messages.add(MatContext.get().getMessageDelegate().getProportionMayNotContainMessage());
			}
		}
		
		/*
			at least one and only one
			Population, Denominator, Numerator, 
			zero or one
			Denominator Exclusions
			and no Denominator Exceptions, Measure Observation, Measure Population
		 */
		else if(ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring)){
			
			if((countDetailsWithType(detailList, ConstantMessages.POPULATION_CONTEXT_ID) != 1)
					|| (countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_CONTEXT_ID) != 1)
					|| (countDetailsWithType(detailList, ConstantMessages.NUMERATOR_CONTEXT_ID) != 1)
			){
				messages.add(MatContext.get().getMessageDelegate().getRatioWrongNumMessage());
			}
			/* zero or one
			   Denominator Exclusions */
			if((countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) > 1)
					|| (countDetailsWithType(detailList, ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) > 1)
			){
				messages.add(MatContext.get().getMessageDelegate().getRatioTooManyMessage());
			}
			
			if((countDetailsWithType(detailList, ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) !=0) 
					|| (countDetailsWithType(detailList, ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList, ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 0)
					
			){
				messages.add(MatContext.get().getMessageDelegate().getRatioMayNotContainMessage());
			}	
		}
		
		if(messages.size() > 0) {
			view.getPackageErrorMessageDisplay().setMessages(messages);
		}
		else {
			view.getPackageErrorMessageDisplay().clear();
		}
		return messages.size() == 0;
	}
	
	private void updateDetailsFromView() {
		currentDetail.setPackageClauses(view.getClausesInPackage());
		currentDetail.setValueSetDate(view.getValuesSetDate());
	}
	// QDM elements
	private void updateSuppDataDetailsFromView() {
		currentDetail.setSuppDataElements(view.getQDMElementsInSuppElements());
		currentDetail.setQdmElements(view.getQDMElements());
	}
	
	
	private int countDetailsWithType(List<MeasurePackageClauseDetail> detailList, String type) {
		int count = 0;
		for(MeasurePackageClauseDetail detail : detailList) {
			if(type.equals(detail.getType())) {
				count++;
			}
		}
		return count;
	}
	
	private void setMeasurePackageDetailsOnView() {
		List<MeasurePackageClauseDetail> packageClauses = currentDetail.getPackageClauses();
		List<MeasurePackageClauseDetail> remainingClauses = removeClauses(overview.getClauses(), packageClauses);
		
		view.setPackageName(currentDetail.getPackageName());
		view.setClausesInPackage(packageClauses);
		view.setValuesSetDate(model.getValueSetDate());
		view.setClauses(remainingClauses);
		setQDMElements(overview.getQdmElements());				
	}
	// QDM elements
	private void setQDMElements(List<QualityDataSetDTO> master) {
		List<QualityDataSetDTO> suppDataList = new ArrayList<QualityDataSetDTO>();
		List<QualityDataSetDTO> qdmList = new ArrayList<QualityDataSetDTO>();
		qdmList.addAll(master);
		for(int i = 0; i < master.size(); i++) {
			QualityDataSetDTO qds = master.get(i);
			if(qds.isSuppDataElement()) {
				qdmList.remove(qds);
				suppDataList.add(qds);
			}
		}
		if(suppDataList != null && suppDataList.size() > 0) {
			overview.setSuppDataElements(suppDataList);
		}
		view.setQDMElementsInSuppElements(overview.getSuppDataElements());
		view.setQDMElements(qdmList);
	}
	
	private List<MeasurePackageClauseDetail> removeClauses(List<MeasurePackageClauseDetail> master, List<MeasurePackageClauseDetail> toRemove) {
		List<MeasurePackageClauseDetail> newList = new ArrayList<MeasurePackageClauseDetail>();
		newList.addAll(master);
		for(MeasurePackageClauseDetail remove : toRemove) {
			for(int i = 0; i < newList.size(); i++) {
				if(newList.get(i).getId().equals(remove.getId())) {
					newList.remove(i);
					break;
				}
			}
		}
		return newList;
	}
	private void setOverview(MeasurePackageOverview result) {
		this.overview = result;
		view.setClauses(result.getClauses());
		// QDM elements
		view.setQDMElements(result.getQdmElements());
		view.setMeasurePackages(result.getPackages());
		
		if(result.getPackages().size() > 0) {
			if(currentDetail != null){
				for(int i=0; i<result.getPackages().size(); i++){
					MeasurePackageDetail mpDetail = result.getPackages().get(i);
					if(mpDetail.getSequence().equalsIgnoreCase(currentDetail.getSequence())){
						setMeasurePackage(result.getPackages().get(i).getSequence());
					}
				}
			}else
				setMeasurePackage(result.getPackages().get(0).getSequence());
				
		}
		else {
			setNewMeasurePackage();
		}
		
		ReadOnlyHelper.setReadOnlyForCurrentMeasure(view.asWidget(),isEditable());
		view.setViewIsEditable(isEditable());
		view.getValuesSetDateInputBox().setEnabled(isEditable());
	}
	private void getMeasurePackageOverview(final String measureId) {
		MatContext.get().getPackageService().getClausesAndPackagesForMeasure(measureId, new AsyncCallback<MeasurePackageOverview>() {
			@Override
			public void onSuccess(MeasurePackageOverview result) {
				if(currentDetail != null && !currentDetail.getMeasureId().equalsIgnoreCase(measureId)){
					currentDetail = null; // This will make sure the package information are not cached across measures.
				}
				setOverview(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				view.getPackageErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
		view.setSelectionHandler(new MeasurePackageSelectionHandler() {
			@Override
			public void onSelection(MeasurePackageDetail detail) {
				currentDetail = detail;
				clearMessages();
				setMeasurePackageDetailsOnView();
			}
		}) ;
		view.setDeletionHandler(new MeasurePackageSelectionHandler() {
			
			@Override
			public void onSelection(MeasurePackageDetail detail) {
				clearMessages();
				deleteMeasurePackage(detail);
			}
		});
	}
	
	private void getMeasure(final String measureId) {
		MatContext.get().getMeasureService().getMeasure(measureId, new AsyncCallback<ManageMeasureDetailModel>() {
			@Override
			public void onSuccess(ManageMeasureDetailModel result) {
				model = result;
			}
			
			@Override
			public void onFailure(Throwable caught) {
				view.getPackageErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
		
	}
	
	private void deleteMeasurePackage(final MeasurePackageDetail pkg) {
		MatContext.get().getPackageService().delete(pkg, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				view.getPackageErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(Void result) {
				overview.getPackages().remove(pkg);
				setOverview(overview);
			}
			
		});
	}
	private int getMaxSequence(MeasurePackageOverview overview) {
		int max = 0;
		for(MeasurePackageDetail detail : overview.getPackages()) {
			int seqInt = Integer.parseInt(detail.getSequence());
			if(seqInt > max) {
				max = seqInt;
			}
		}
		return max;
	}
	private void setNewMeasurePackage() {
		currentDetail = new MeasurePackageDetail();
		currentDetail.setMeasureId(MatContext.get().getCurrentMeasureId());
		currentDetail.setSequence(Integer.toString(getMaxSequence(overview) + 1));
		view.setMeasurePackages(overview.getPackages());
		setMeasurePackageDetailsOnView();
	}
	
	public Widget getWidget() {
		panel.clear();
		panel.add(view.asWidget());
		return panel;
	}
	
	private void displayEmpty(){
		panel.clear();
		panel.add(emptyPanel);
	}
	
	private void displayMeasurePackageWorkspace(){
		panel.clear();
		panel.add(view.asWidget());
		//view.setTabIndex();
	}
	
	private boolean isEditable(){
		return MatContext.get().getMeasureLockService().checkForEditPermission();
	}

	
}
