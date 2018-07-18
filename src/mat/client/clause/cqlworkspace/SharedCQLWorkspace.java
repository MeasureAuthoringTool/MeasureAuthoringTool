package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.shared.MatContext;
import mat.model.CQLValueSetTransferObject;
import mat.model.CodeListSearchDTO;
import mat.model.MatValueSet;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.ConstantMessages;
import mat.shared.GetUsedCQLArtifactsResult;

public abstract class SharedCQLWorkspace implements MatPresenter{
	public static final String CODES_SELECTED_SUCCESSFULLY = "All codes successfully selected.";
	
	public static final String VALUE_SETS_SELECTED_SUCCESSFULLY = "All value sets successfully selected.";
	
	protected static SharedViewDisplay searchDisplay;
	protected SimplePanel emptyWidget = new SimplePanel();
	protected CQLQualityDataSetDTO modifyValueSetDTO;
	protected CQLCode modifyCQLCode;
	protected MatValueSet currentMatValueSet= null;
	protected List<CQLQualityDataSetDTO> appliedValueSetTableList = new ArrayList<CQLQualityDataSetDTO>();
	
	protected boolean areProgramAndReleaseListBoxesEnabled = true; 
	protected boolean isRetrieveButtonEnabled = true; 
	protected boolean isApplyButtonEnabled = false; 
	
	protected boolean previousAreProgramAndReleaseListBoxesEnabled = true; 
	protected boolean previousIsRetrieveButtonEnabled = true; 
	protected boolean previousIsApplyButtonEnabled = false; 
	
	protected boolean isUserDefined = false;
	protected boolean isModified = false;
	
	protected String currentSection = "general";
	
	public SharedCQLWorkspace(final SharedViewDisplay srchDisplay) {
		searchDisplay = srchDisplay;
		emptyWidget.add(new Label("No CQL Library Selected"));
		addEventHandlers();
	}
	
	/**
	 * Gets the used artifacts.
	 *
	 * @return the used artifacts
	 */
	public void getUsedArtifacts() {
		showSearchingBusy(true);
        MatContext.get().getLibraryService().getUsedCqlArtifacts(
                     MatContext.get().getCurrentCQLLibraryId(),
                     new AsyncCallback<GetUsedCQLArtifactsResult>() {

                            @Override
                            public void onFailure(Throwable caught) {
                            	 Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
                            	 showSearchingBusy(false);
                                   
                            }

                            @Override
                            public void onSuccess(GetUsedCQLArtifactsResult result) {
                            	showSearchingBusy(false);
                            	// if there are errors, set the valuesets to not used.
                            	if(!result.getCqlErrors().isEmpty()) {
                            		for(CQLQualityDataSetDTO cqlDTo : searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList()){
                                      cqlDTo.setUsed(false);
                            		}
                            	}
                            	
                            	// otherwise, check if the valueset is in the used valusets list
                            	else {
                                   for(CQLQualityDataSetDTO cqlDTo : searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList()){
                                          if (result.getUsedCQLValueSets().contains(cqlDTo.getName())) {
                                              cqlDTo.setUsed(true);
                                          } else{
                                        	  cqlDTo.setUsed(false);
                                          }
                                   }
                            	}
                            		
                            	if(searchDisplay.getCqlLeftNavBarPanelView().getAppliedQdmTableList().size() > 0) {
                                	searchDisplay.getValueSetView().getCelltable().redraw();
                                	searchDisplay.getValueSetView().getListDataProvider().refresh();
                            	}
                                   
                            }
                            
                     });
        
	}
	
	public void setReleaseAndProgramFieldsOnEdit(CQLQualityDataSetDTO result) {
		previousAreProgramAndReleaseListBoxesEnabled = areProgramAndReleaseListBoxesEnabled;
		
		loadProgramsAndReleases();
		areProgramAndReleaseListBoxesEnabled = true;
				
		searchDisplay.getValueSetView().setProgramAndReleaseBoxesEnabled(areProgramAndReleaseListBoxesEnabled);
	}
	
	public void loadProgramsAndReleases() {
		searchDisplay.getValueSetView().loadProgramsAndReleases();
	}
	
	public void modifyValueSet() {
		// Normal Available QDM Flow
		MatValueSet modifyWithDTO = currentMatValueSet;
		if ((modifyValueSetDTO != null) && (modifyWithDTO != null)) {
			String originalName = searchDisplay.getValueSetView().getUserDefinedInput().getText();
			String suffix = searchDisplay.getValueSetView().getSuffixInput().getValue();
			String displayName = (!originalName.isEmpty() ? originalName : "")  + (!suffix.isEmpty() ? " (" + suffix + ")" : "");

			if (modifyValueSetDTO.getVersion() == null) {
				modifyValueSetDTO.setVersion("");
			}
			
			String releaseValue = searchDisplay.getValueSetView().getReleaseListBox().getSelectedValue();
			if(!releaseValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
				modifyValueSetDTO.setRelease(releaseValue);
				modifyValueSetDTO.setVersion("");
			} else {
				modifyValueSetDTO.setRelease("");
			}
			
			String programValue = searchDisplay.getValueSetView().getProgramListBox().getSelectedValue();
			modifyValueSetDTO.setProgram(programValue);
			modifyValueSetList(modifyValueSetDTO);
			
			
			if (!searchDisplay.getValueSetView().checkNameInValueSetList(displayName,appliedValueSetTableList)) {

				if(!searchDisplay.getValueSetView().getSuffixInput().getValue().isEmpty()){
					modifyValueSetDTO.setSuffix(searchDisplay.getValueSetView().getSuffixInput().getValue());
					modifyValueSetDTO.setName(originalName+" ("+searchDisplay.getValueSetView().getSuffixInput().getValue()+")");
				} else {
					modifyValueSetDTO.setName(originalName);
					modifyValueSetDTO.setSuffix(null);
				}
				modifyValueSetDTO.setOriginalCodeListName(originalName);
				updateAppliedValueSetsList(modifyWithDTO, null, modifyValueSetDTO);
			} else {
				searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
				.createAlert(MatContext.get().getMessageDelegate().getDuplicateAppliedValueSetMsg(displayName));
				appliedValueSetTableList.add(modifyValueSetDTO);
			}
			getUsedArtifacts();
		} else {
			searchDisplay.getCqlLeftNavBarPanelView().getErrorMessageAlert()
					.createAlert(MatContext.get().getMessageDelegate().getMODIFY_VALUE_SET_SELECT_ATLEAST_ONE());
		}
	}
	
	/**
	 * MAT-8977. 
	 * Get the program and releases from VSAC using REST calls and set it in the MatContext 
	 * the first time the value sets page is loaded.
	 * If the values have been loaded previously, no calls are made.
	 */
	public void getProgramsAndReleases() {

		HashMap<String, List<String>> pgmRelMap = (HashMap<String, List<String>>) MatContext.get().getProgramToReleases();

		if (pgmRelMap == null || pgmRelMap.isEmpty()) {
			MatContext.get().getProgramsAndReleasesFromVSAC();	
		}				
	}
	
	public void onModifyValueSet(CQLQualityDataSetDTO result, boolean isUserDefined) {
		String oid = isUserDefined ? "" : result.getOid();
		searchDisplay.getValueSetView().getOIDInput().setEnabled(true);

		searchDisplay.getValueSetView().getOIDInput().setValue(oid);
		searchDisplay.getValueSetView().getOIDInput().setTitle(oid);

		searchDisplay.getValueSetView().getRetrieveFromVSACButton().setEnabled(!isUserDefined);

		searchDisplay.getValueSetView().getUserDefinedInput().setEnabled(isUserDefined);
		searchDisplay.getValueSetView().getUserDefinedInput().setValue(result.getOriginalCodeListName());
		searchDisplay.getValueSetView().getUserDefinedInput().setTitle(result.getOriginalCodeListName());

		searchDisplay.getValueSetView().getSuffixInput().setEnabled(true);
		searchDisplay.getValueSetView().getSuffixInput().setValue(result.getSuffix());
		searchDisplay.getValueSetView().getSuffixInput().setTitle(result.getSuffix());
		
		setReleaseAndProgramFieldsOnEdit(result);
		searchDisplay.getValueSetView().getSaveButton().setEnabled(isUserDefined);
		alert508StateChanges();
	}

	public void modifyValueSetList(CQLQualityDataSetDTO qualityDataSetDTO) {
		for (int i = 0; i < appliedValueSetTableList.size(); i++) {
			if (qualityDataSetDTO.getName().equals(appliedValueSetTableList.get(i).getName())) {
				appliedValueSetTableList.remove(i);
				break;

			}
		}
	}
	

	public CQLValueSetTransferObject createValueSetTransferObject(String libraryID) {
		if(currentMatValueSet == null) {
			currentMatValueSet = new MatValueSet();
		}
		CQLValueSetTransferObject matValueSetTransferObject = new CQLValueSetTransferObject();
		matValueSetTransferObject.setCqlLibraryId(libraryID);
		
		String originalCodeListName = searchDisplay.getValueSetView().getUserDefinedInput().getValue(); 
		
		matValueSetTransferObject.setCqlQualityDataSetDTO(new CQLQualityDataSetDTO());
		matValueSetTransferObject.getCqlQualityDataSetDTO().setOriginalCodeListName(originalCodeListName);
		
		if(!searchDisplay.getValueSetView().getSuffixInput().getValue().isEmpty()){
			matValueSetTransferObject.getCqlQualityDataSetDTO().setSuffix(searchDisplay.getValueSetView().getSuffixInput().getValue());
			matValueSetTransferObject.getCqlQualityDataSetDTO().setName(originalCodeListName+" ("+searchDisplay.getValueSetView().getSuffixInput().getValue()+")");
		} else {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setName(originalCodeListName);
		}
		
		// set it to empty string to begin with
		matValueSetTransferObject.getCqlQualityDataSetDTO().setRelease("");
		String releaseValue = searchDisplay.getValueSetView().getReleaseListBox().getSelectedValue(); 
		if(!releaseValue.equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
			matValueSetTransferObject.getCqlQualityDataSetDTO().setRelease(releaseValue);
		}
		
		String programValue = searchDisplay.getValueSetView().getProgramListBox().getSelectedValue();
		matValueSetTransferObject.getCqlQualityDataSetDTO().setProgram(programValue);
		
		CodeListSearchDTO codeListSearchDTO = new CodeListSearchDTO();
		codeListSearchDTO.setName(searchDisplay.getValueSetView().getUserDefinedInput().getText());
		matValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
		matValueSetTransferObject.setAppliedQDMList(appliedValueSetTableList);
		matValueSetTransferObject.setMatValueSet(currentMatValueSet);
		matValueSetTransferObject.setCqlLibraryId(libraryID);
		matValueSetTransferObject.setUserDefinedText(searchDisplay.getValueSetView().getUserDefinedInput().getText());
		return matValueSetTransferObject;
	}
	
	public void addValueSetEventHandlers() {
		/**
		 * this functionality is to clear the content on the QDM Element Search
		 * Panel.
		 */
		searchDisplay.getValueSetView().getCancelQDMButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.resetMessageDisplay();
				isModified = false;
				searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
				//508 compliance for Value Sets
				searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
				
				previousAreProgramAndReleaseListBoxesEnabled = areProgramAndReleaseListBoxesEnabled;
				areProgramAndReleaseListBoxesEnabled = true; 
				
				loadProgramsAndReleases(); 
				alert508StateChanges();
			}
		});

		searchDisplay.getValueSetView().getUpdateFromVSACButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();
					updateVSACValueSets();
					//508 compliance for Value Sets
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
				}
			}
		});

		/**
		 * this functionality is to retrieve the value set from VSAC with latest
		 * information which consists of Expansion Profile list and Version
		 * List.
		 */
		searchDisplay.getValueSetView().getRetrieveFromVSACButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					searchDisplay.resetMessageDisplay();					
					String expansionProfile = searchDisplay.getValueSetView().getReleaseListBox().getSelectedValue();
					expansionProfile = MatContext.PLEASE_SELECT.equals(expansionProfile) ? null : expansionProfile;
					searchValueSetInVsac(expansionProfile);
					//508 compliance for Value Sets
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput()); 
				}
			}
		});

		/**
		 * this handler is invoked when apply button is clicked on search Panel
		 * in QDM elements tab and this is to add new value set or user Defined
		 * QDM to the Applied QDM list.
		 */
		searchDisplay.getValueSetView().getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
					MatContext.get().clearDVIMessages();
					searchDisplay.resetMessageDisplay();

					if (isModified && (modifyValueSetDTO != null)) {
						modifyValueSetOrUserDefined(isUserDefined);
					} else {
						addNewValueSet(isUserDefined);
					}
					//508 compliance for Value Sets
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput()); 
				}
			}
		});

		/**
		 * Adding value Change handler for UserDefined Input in Search Panel in
		 * QDM Elements Tab
		 * 
		 */
		searchDisplay.getValueSetView().getUserDefinedInput().addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				searchDisplay.resetMessageDisplay();
				isUserDefined = searchDisplay.getValueSetView().validateUserDefinedInput();
			}
		});

		/**
		 * Adding value change handler for OID input in Search Panel in QDM
		 * elements Tab
		 */
		searchDisplay.getValueSetView().getOIDInput().addValueChangeHandler(event -> clearOID());
		
		searchDisplay.getValueSetView().getOIDInput().sinkBitlessEvent("input");
		
		searchDisplay.getValueSetView().setObserver(new CQLAppliedValueSetView.Observer() {
			@Override
			public void onModifyClicked(CQLQualityDataSetDTO result) {
				if(!searchDisplay.getValueSetView().getIsLoading()){
					searchDisplay.resetMessageDisplay();
					searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
					isModified = true;
					modifyValueSetDTO = result;
					String displayName = result.getName();
					// Substring at 60th character length.
					if(displayName.length() >=60){
						displayName = displayName.substring(0, 59);
					}
					HTML searchHeaderText = new HTML("<strong>Modify value set ( "+displayName +")</strong>");
					searchDisplay.getValueSetView().getSearchHeader().clear();
					searchDisplay.getValueSetView().getSearchHeader().add(searchHeaderText);
					searchDisplay.getValueSetView().getMainPanel().getElement().focus();
					if(result.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)){
						isUserDefined = true;
					} else {
						isUserDefined = false;
					}
					
					onModifyValueSet(result, isUserDefined);
					//508 Compliance for Value Sets section
					searchDisplay.getCqlLeftNavBarPanelView().setFocus(searchDisplay.getValueSetView().getOIDInput());
				} else {
					//do nothing when loading.
				}
			}

			@Override
			public void onDeleteClicked(CQLQualityDataSetDTO result, final int index) {
				if(!searchDisplay.getValueSetView().getIsLoading()){
					searchDisplay.resetMessageDisplay();
					searchDisplay.getValueSetView().resetCQLValuesetearchPanel();
					if((modifyValueSetDTO!=null) && modifyValueSetDTO.getId().equalsIgnoreCase(result.getId())){
						isModified = false;
					}
					String libraryId = MatContext.get().getCurrentCQLLibraryId();
					if ((libraryId != null) && !libraryId.equals("")) {
						searchDisplay.getCqlLeftNavBarPanelView().setCurrentSelectedValueSetObjId(result.getId());
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().getMessageAlert().createAlert(MatContext.get().getMessageDelegate().getDELETE_CONFIRMATION_VALUESET(result.getName()));
						searchDisplay.getCqlLeftNavBarPanelView().getDeleteConfirmationDialogBox().show();
						//508 Compliance for Value Sets section
						searchDisplay.getValueSetView().getOIDInput().setFocus(true);
					}
				} else {
					//do nothing when loading.
				}
			}

		});
		
		searchDisplay.getValueSetView().getClearButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				if(!searchDisplay.getValueSetView().getIsLoading()){
					searchDisplay.resetMessageDisplay();
					searchDisplay.getValueSetView().clearSelectedCheckBoxes();
				}
			}
		});
		
		searchDisplay.getValueSetView().getCopyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				copyValueSets();
			}
		});
		
		searchDisplay.getValueSetView().getSelectAllButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				selectAllValueSets();
			}
		});

		searchDisplay.getValueSetView().getPasteButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(MatContext.get().getLibraryLockService().checkForEditPermission()) {
					pasteValueSets();
				} else {
					event.preventDefault();
				}
			}
		});
		
		searchDisplay.getValueSetView().getReleaseListBox().addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				isRetrieveButtonEnabled = true;
				searchDisplay.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);				
				previousIsApplyButtonEnabled = isApplyButtonEnabled;
				isApplyButtonEnabled = false;
				searchDisplay.getValueSetView().getSaveButton().setEnabled(isApplyButtonEnabled);
				alert508StateChanges();
			}
		});
		
		searchDisplay.getValueSetView().getProgramListBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				isRetrieveButtonEnabled = true;
				searchDisplay.getValueSetView().getRetrieveFromVSACButton().setEnabled(isRetrieveButtonEnabled);
				
				previousIsApplyButtonEnabled = isApplyButtonEnabled;
				isApplyButtonEnabled = false; 
				searchDisplay.getValueSetView().getSaveButton().setEnabled(isApplyButtonEnabled);
				
				searchDisplay.getValueSetView().loadReleases();
				
				alert508StateChanges();
			}
		});
	}
	
	public void addNewValueSet(final boolean isUserDefinedValueSet) {
		if (!isUserDefinedValueSet) {
			addVSACCQLValueset();
		} else {
			addUserDefinedValueSet();
		}
	}
	
	
	public void showSearchBusyOnDoubleClick(boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		searchDisplay.getCqlLeftNavBarPanelView().getGeneralInformation().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getCodesLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getAppliedQDM().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getParameterLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getDefinitionLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getFunctionLibrary().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getViewCQL().setEnabled(!busy);
		searchDisplay.getCqlLeftNavBarPanelView().getIncludesLibrary().setEnabled(!busy);
	}
	
	public void showSearchingBusy(final boolean busy) {
		showSearchBusyOnDoubleClick(busy);
		if (MatContext.get().getLibraryLockService().checkForEditPermission()) {
			switch(currentSection.toLowerCase()) {
			case(CQLWorkSpaceConstants.CQL_GENERAL_MENU): 
				searchDisplay.getCqlGeneralInformationView().setWidgetReadOnlyForCQLLibrary(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_INCLUDES_MENU):
				searchDisplay.getIncludeView().setReadOnly(!busy);				
			break;
			case(CQLWorkSpaceConstants.CQL_APPLIED_QDM): 
				searchDisplay.getValueSetView().setReadOnly(!busy);				
			break;
			case(CQLWorkSpaceConstants.CQL_CODES): 
				searchDisplay.getCodesView().setReadOnly(!busy);				
			break;
			case(CQLWorkSpaceConstants.CQL_PARAMETER_MENU): 
				searchDisplay.getCqlParametersView().setReadOnly(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_DEFINE_MENU): 
				searchDisplay.getCqlDefinitionsView().setReadOnly(!busy);
			break;
			case(CQLWorkSpaceConstants.CQL_FUNCTION_MENU): 
				searchDisplay.getCqlFunctionsView().setReadOnly(!busy);
			break;															  
			}
			
		}
		searchDisplay.getCqlLeftNavBarPanelView().setIsLoading(busy);
	}
	
	
	abstract void addEventHandlers();
	abstract void alert508StateChanges();
	abstract void updateAppliedValueSetsList(final MatValueSet matValueSet, final CodeListSearchDTO codeListSearchDTO,
			final CQLQualityDataSetDTO qualityDataSetDTO);
	abstract void updateVSACValueSets();
	abstract void searchValueSetInVsac(String expansionProfile);
	abstract void clearOID();
	abstract void modifyValueSetOrUserDefined(final boolean isUserDefined);
	abstract void addVSACCQLValueset();
	abstract void addUserDefinedValueSet();
	abstract void selectAllValueSets();
	abstract void pasteValueSets();
	abstract void copyValueSets();
}
