export const newLibraryBtn = '#newLibrary_button'
export const title = '.contentWithHeadingHeader > h1'

//Search
export const filterByMyLibrariesChkBox = '#SearchFilterWidget_HorizontalPanel_forCqlLibrary > tbody > tr > :nth-child(1) > .searchWidgetCheckBox > .customCheckBox'
export const searchBtn = '#SearchWidgetButton_forCqlLibrary'
export const searchInputBox = '#SearchFilterWidget_SearchInputHPanel_forCqlLibrary > tbody > tr > td > .form-control'
export const modelTypeListBox = '#SearchFilterWidget_verticalPanel_forCqlLibrary #modelType'

//Recent Activity Table
export const row1RecentActivity = '#searchPanel_VerticalPanel [__gwt_row="0"]'

export const row1RecentActivityCheckbox = ':nth-child(3) > .GB-MJYKBPC > .GB-MJYKBBD > div > input'
export const row2RecentActivity = '#searchPanel_VerticalPanel [__gwt_row="1"]'
export const row2RecentActivityCheckbox = ':nth-child(3) > .GB-MJYKBPD > .GB-MJYKBBD > div > input'

//Library Search Table
export const row1CqlLibrarySearch = '#CQLLibrarySearchCellTable [__gwt_row="0"]'
export const row2CqlLibrarySearch = '#CQLLibrarySearchCellTable [__gwt_row="1"]'
export const row3CqlLibrarySearch = '#CQLLibrarySearchCellTable [__gwt_row="2"]'
export const row1CqlLibrarySearchCheckbox = '#CQLLibrarySearchCellTable [__gwt_row="0"] input'
export const row2CqlLibrarySearchCheckbox = '#CQLLibrarySearchCellTable [__gwt_row="1"] input'
export const row1CqlLibraryName = '#CQLLibrarySearchCellTable [__gwt_row="0"] #div2'
export const row2CqlLibraryName = '#CQLLibrarySearchCellTable [__gwt_row="1"] #div2'
export const row1Models = '#CQLLibrarySearchCellTable [__gwt_row="0"] > .GB-MJYKBGJ'
export const row2Models = '#CQLLibrarySearchCellTable [__gwt_row="1"] > .GB-MJYKBGJ'

//Recent Activity Button Bar
export const recentActivityButtonBar = '#recentActivitySummary_gridToolbar > [type="button"]'

export const createVersionDraftRecentActivityBtn = '#recentActivitySummary_gridToolbar > [title="Click to create version or draft"]'
export const historyRecentActivityBtn = '#recentActivitySummary_gridToolbar > [title="Click to view history"]'
export const viewRecentActivityBtn = '#recentActivitySummary_gridToolbar > [title="Read-Only"]'
export const editRecentActivityEnabledBtn = '#recentActivitySummary_gridToolbar > [title="Click to edit"]'
export const shareRecentActivityBtn = '#recentActivitySummary_gridToolbar > [title="Click to share"]'
export const deleteRecentActivityBtn = '#recentActivitySummary_gridToolbar > [title="Click to delete library"]'
export const createVersionRecentActivityBtn = '#recentActivitySummary_gridToolbar > [title="Click to create version"]'
export const createDraftRecentActivityBtn = '#recentActivitySummary_gridToolbar > [title="Click to create draft"]'

//Library search columns
export const cqlLibrarySearchTable = '#cqlCellTablePanel_VerticalPanel > .cellTablePanel > :nth-child(1) > :nth-child(7) > [align="left"]'
export const row1CqlLibraryModelVersion = '#CQLLibrarySearchCellTable > :nth-child(3) > :nth-child(1) > :nth-child(3)'
export const row2CqlLibraryModelVersion = '#CQLLibrarySearchCellTable > :nth-child(3) > :nth-child(2) > :nth-child(3)'
export const row2CqlLibraryVersionColumn = '#CQLLibrarySearchCellTable > :nth-child(3) > :nth-child(2) > :nth-child(4)'

//All CQL Libraries Button Bar
export const createVersionDraftCqllibrariesBtn = '#cqlLibrarySearchCellTable_gridToolbar > [title="Click to create version or draft"]'
export const historyCqllibrariesBtn = '#cqlLibrarySearchCellTable_gridToolbar > [aria-label="Search History View History"]'
export const viewCqllibrariesBtn = '#cqlLibrarySearchCellTable_gridToolbar > [title="Read-Only"]'
export const editCqllibrariesEnabledBtn = '#cqlLibrarySearchCellTable_gridToolbar > [title="Edit"]'
export const shareCqllibrariesBtn = '#cqlLibrarySearchCellTable_gridToolbar > [title="Click to share"]'
export const deleteCqllibrariesBtn = '#cqlLibrarySearchCellTable_gridToolbar > [title="Delete library"]'
export const confirmDeleteText = '#password_PasswordTextBox'
export const confirmDeleteBtn = '.modal-footer > :nth-child(2)'
export const createVersionCqllibrariesBtn = '#cqlLibrarySearchCellTable_gridToolbar > [title="Click to create version"]'
export const createDraftCqllibrariesBtn = '#cqlLibrarySearchCellTable_gridToolbar > [title="Click to create draft"]'
export const convertToFhirLibrarySearchBtn = '#cqlLibrarySearchCellTable_gridToolbar > [aria-label="Search History Convert"]'

//Draft Version
export const majorVersionTypeRadio = '#cqlmajorRadio_RadioButton'
export const minorVersionTypeRadio = '#cqlminorRadio_RadioButton'
export const versionSaveAndContinueBtn = '#SaveAndContinueButton_cqlVersion'
export const cancelBtn = '#CancelButton_cqlVersion'
export const draftCancelBtn = '#CancelButton_cqlDetail'

//Draft CQL Library
export const draftSaveAndContinueBtn = '#SaveAndContinueButton_cqlDetail'

//Draft Confirmation
export const confirmationContinue = '#yes_Button'

//return to CQL Library
export const returnToCqlLibrary = ':nth-child(6) > td > .gwt-Anchor'

//modal dialog
export const modal = '.modal-dialog'
export const modalCloseBtn = '.close'

// FHIR Converstion Warning dialog
export const fhirConversionWarningMessage = '.modal-body'
export const fhirConversionReturnBtn = '#Yes_ConfirmDialogBox'

export const fhirConversionNoBtn = '#No_ConfirmDialogBox'

// History UI
export const historyConvertToFHIRUserActionLogEntry = '#HistorySearchCellTable [title=" Converted from QDM/CQL to FHIR"]'
export const historyCQLLibraryCreatedUserActionLogEntry = '#HistorySearchCellTable [title=" CQL Library Created"]'

// Library Sharing
export const libraryName = '#cqlLibraryNamePanel_FlowPanel'
export const shareSearchField = '#CQLLibrarySharing_Search'
export const shareSearchBtn = '#CQLLibrarySharing_Button'
export const shareWithFirstUserCheckBox = '[__gwt_row="0"] > .GB-MJYKBLD > div > input'
export const shareWithSecondUserCheckBox = '[__gwt_row="1"] > .GB-MJYKBLD > div > input'
export const shareSaveAndContinueBtn = '#SaveAndContinueButton_cqlShare'
export const shareCancelBtn = '#CancelButton_cqlShare'
export const shareWarningMessage = '#CQLLibrary_MainPanel > .alert-success'
