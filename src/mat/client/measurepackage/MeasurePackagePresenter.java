package mat.client.measurepackage;

import java.util.ArrayList;
import java.util.List;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.QDSAppliedListModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measurepackage.MeasurePackagerView.Observer;
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
	/** The empty panel. */
	private SimplePanel emptyPanel = new SimplePanel();
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
		 * @param clauses
		 */
		void setClauses(List<MeasurePackageClauseDetail> clauses);
		/**
		 * @param name
		 */
		void setPackageName(String name);
		/**
		 * @param list
		 */
		void setClausesInPackage(List<MeasurePackageClauseDetail> list);
		
		/**
		 * @return
		 */
		MeasurePackageClauseCellListWidget getPackageGroupingWidget();
		/**
		 * @param observer
		 */
		void setObserver(Observer observer);
		/**
		 * @param appliedListModel
		 */
		void setAppliedQdmList(QDSAppliedListModel appliedListModel);
		/**
		 * @return
		 */
		HasClickHandlers getCreateNewButton();
		/**
		 * @param packages
		 */
		void buildCellTable(List<MeasurePackageDetail> packages);
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
		
		view.getCreateNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				clearMessages();
				System.out.println("Overview Object"+ packageOverview.getClauses().size());
				setNewMeasurePackage();
			}
		});
		
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
								if (!packageOverview.getPackages().contains(
										currentDetail)) {
									if ((currentDetail
											.getPackageClauses() != null)
											&& (currentDetail.
													getPackageClauses()
													.size() > 0)) {
										packageOverview.getPackages()
										.add(currentDetail);
									}
									packageOverview.setQdmElements(
											currentDetail
											.getQdmElements());
									packageOverview.
									setSuppDataElements(currentDetail
											.getSuppDataElements());
									setOverview(packageOverview);
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
	
	public final void getAppliedQDMList(boolean checkForSupplementData) {
		String measureId = MatContext.get().getCurrentMeasureId();
		if ((measureId != null) && !measureId.equals("")) {
			service.getAppliedQDMFromMeasureXml(measureId,
					checkForSupplementData,
					new AsyncCallback<ArrayList<QualityDataSetDTO>>() {
				
				private void filterTimingQDMs(
						ArrayList<QualityDataSetDTO> result) {
					List<QualityDataSetDTO> timingQDMs = new ArrayList<QualityDataSetDTO>();
					for (QualityDataSetDTO qdsDTO : result) {
						if ("Timing Element".equals(qdsDTO
								.getDataType())) {
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
						final ArrayList<QualityDataSetDTO> result) {
					QDSAppliedListModel appliedListModel = new QDSAppliedListModel();
					filterTimingQDMs(result);
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
		view.getPackageGroupingWidget().getDisclosurePanelAssociations().setVisible(false);
		view.getPackageGroupingWidget().getDisclosurePanelItemCountTable().setVisible(false);
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
	 * @return the measure package packageOverview
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
				currentDetail = detail;
				clearMessages();
				setMeasurePackageDetailsOnView();
				
			}
			
			@Override
			public void onDeleteClicked(MeasurePackageDetail detail) {
				// TODO Auto-generated method stub
				
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
		packageOverview = result;
		view.setClauses(result.getClauses());
		// QDM elements
		view.setQDMElements(result.getQdmElements());
		view.buildCellTable(result.getPackages());
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
				.toString(getMaxSequence(packageOverview) + 1));
		view.buildCellTable(packageOverview.getPackages());
		//view.setMeasurePackages(packageOverview.getPackages());
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
				packageOverview.getClauses(), packageClauses);
		view.setPackageName(currentDetail.getPackageName());
		view.setClausesInPackage(packageClauses);
		view.setClauses(remainingClauses);
		view.setQDMElementsInSuppElements(packageOverview.getSuppDataElements());
		view.setQDMElements(packageOverview.getQdmElements());
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
	
	
}


