package mat.client.measurepackage;

import java.util.ArrayList;
import java.util.List;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.MeasurePackageClauseCellListWidget;
import mat.client.shared.ReadOnlyHelper;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.WarningMessageDisplay;
import mat.model.QualityDataSetDTO;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasurePackagePresenter.
 */
public class MeasurePackagePresenter implements MatPresenter {
	/** The empty panel. */
	private SimplePanel emptyPanel = new SimplePanel();
	/** The panel. */
	private SimplePanel panel = new SimplePanel();
	
	/** The view. */
	private PackageView view;
	/** The model. */
	private ManageMeasureDetailModel model;
	
	/** The current detail. */
	private MeasurePackageDetail currentDetail;
	
	/** The overview. */
	private MeasurePackageOverview overview;
	
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
		void setClauses(List<MeasurePackageClauseDetail> clauses);
		void setPackageName(String name);
		void setClausesInPackage(List<MeasurePackageClauseDetail> list);
		void setMeasurePackages(List<MeasurePackageDetail> packages);
		MeasurePackageClauseCellListWidget getPackageGroupingWidget();
	}
	
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
		view.getPackageMeasureButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				((Button) view.getPackageMeasureButton()).setEnabled(false);
				MatContext.get().getMeasureService().saveMeasureAtPackage(model, new AsyncCallback<SaveMeasureResult>() {
					
					@Override
					public void onFailure(Throwable caught) {
						((Button) view.getPackageMeasureButton()).setEnabled(true);
					}
					
					@Override
					public void onSuccess(SaveMeasureResult result) {
						((Button) view.getPackageMeasureButton()).setEnabled(true);
					}
				});
			}
		});
		
		view.getAddQDMElementsToMeasureButton().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(final ClickEvent event) {
						clearMessages();
						updateSuppDataDetailsFromView();
						MatContext
						.get()
						.getPackageService()
						.saveQDMData(currentDetail,
								new AsyncCallback<Void>() {
							@Override
							public void onFailure(final Throwable caught) {
								view.getPackageErrorMessageDisplay().
								setMessage(MatContext.get().
										getMessageDelegate().
										getUnableToProcessMessage());
							}
							
							@Override
							public void onSuccess(final Void result) {
								if (!overview.getPackages().contains(
										currentDetail)) {
									if ((currentDetail
											.getPackageClauses() != null)
											&& (currentDetail.
													getPackageClauses()
													.size() > 0)) {
										overview.getPackages()
										.add(currentDetail);
									}
									overview.setQdmElements(
											currentDetail
											.getQdmElements());
									overview.
									setSuppDataElements(currentDetail
											.getSuppDataElements());
									setOverview(overview);
								}
								view.getSuppDataSuccessMessageDisplay()
								.setMessage(MatContext.get()
										.getMessageDelegate()
										.getSuppDataSavedMessage());
							}
						});
					}
				});
	}
	
	
	/**
	 * Clear messages.
	 */
	private void clearMessages() {
		view.getPackageSuccessMessageDisplay().clear();
		view.getSuppDataSuccessMessageDisplay().clear();
		view.getPackageErrorMessageDisplay().clear();
		//view.getMeasureErrorMessageDisplay().clear();
		view.getMeasurePackageSuccessMsg().clear();
		view.getErrorMessageDisplay().clear();
		view.getMeasurePackageWarningMsg().clear();
	}
	/**
	 * Display Empty.
	 */
	private void displayEmpty() {
		panel.clear();
		panel.add(emptyPanel);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		view.getPackageGroupingWidget().getDisclosurePanelAssociations().setVisible(false);
		view.getPackageGroupingWidget().getDisclosurePanelItemCountTable().setVisible(false);
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
	 * @param measureId
	 *            - String.
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
	}
	
	/**
	 * Update supp data details from view.
	 */
	private void updateSuppDataDetailsFromView() {
		currentDetail.setSuppDataElements(view.getQDMElementsInSuppElements());
		currentDetail.setQdmElements(view.getQDMElements());
	}
	
	/**
	 * set Overview.
	 * @param result - MeasurePackageOverview.
	 */
	private void setOverview(final MeasurePackageOverview result) {
		overview = result;
		view.setClauses(result.getClauses());
		// QDM elements
		view.setQDMElements(result.getQdmElements());
		
		view.setMeasurePackages(result.getPackages());
		
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
				.toString(getMaxSequence(overview) + 1));
		//view.setMeasurePackages(overview.getPackages());
		setMeasurePackageDetailsOnView();
	}
	
	/**
	 * Sets the measure package.
	 *
	 * @param measurePackageId the new measure package
	 */
	private void setMeasurePackage(final String measurePackageId) {
		for (MeasurePackageDetail detail : overview.getPackages()) {
			if (detail.getSequence().equals(measurePackageId)) {
				currentDetail = detail;
				setMeasurePackageDetailsOnView();
				break;
			}
		}
	}
	
	/**
	 * setMeasurePackageDetailsOnView.
	 */
	private void setMeasurePackageDetailsOnView() {
		List<MeasurePackageClauseDetail> packageClauses = currentDetail
				.getPackageClauses();
		List<MeasurePackageClauseDetail> remainingClauses = removeClauses(
				overview.getClauses(), packageClauses);
		
		view.setPackageName(currentDetail.getPackageName());
		view.setClausesInPackage(packageClauses);
		view.setClauses(remainingClauses);
		view.setQDMElementsInSuppElements(overview.getSuppDataElements());
		view.setQDMElements(overview.getQdmElements());
	}
	
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
	 * @param measurePackageOverview the measure package overview
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
	
	
}


