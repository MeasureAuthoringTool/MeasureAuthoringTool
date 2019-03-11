package mat.client.inapphelp.message;

public class InAppHelpMessages {
	public static final String MEASURE_CQL_LIBRARY_GENERAL_INFORMATION = "<b>CQL Library Name:</b> This is a name that is constructed from the measure name but has been adjusted so that it starts with an alpha-character or underscore followed by alpha-numeric character(s) or underscore(s) and does not contain spaces. This name identifies the library of CQL expressions being created with this measure. This field is not editable.<br />" + 
			"<br />" + 
			"                  <b>CQL Library Version:</b> This is the current version of the CQL library that is being created with this measure. This field is not editable.<br />" + 
			"<br />" + 
			"                  <b>Using Model:</b> Describes the data model this measure is using. This field is not editable.<br />" + 
			"<br />" + 
			"                  <b>Model Version:</b> Describes the version of the data model this measure is using. This field is not editable.<br />" + 
			"<br />" + 
			"<b>Comments:</b> This comment box is available to enter information that applies to the CQL library as a whole. Information entered here will show before the model declaration on both the 'View CQL' section of the CQL Workspace and in the CQL file export. Click the save icon to save comments entered. To remove a comment, remove the text from the comments field and click the save icon again.";
	
	public static final String STANDALONE_CQL_LIBRARY_GENERAL_INFORMATION = "<b>CQL Library Name:</b> This is a name that was given when the Library was created. CQL library names must start with an alpha-character or underscore followed by alpha-numeric character(s) or underscore(s) and can not contain spaces.<br />" + 
			"<br />" + 
			"<b>CQL Library Version:</b> This is the current version of the CQL library. This field is not editable.<br />" + 
			"<br />" + 
			"<b>Using Model:</b> Describes the data model this library is using. This field is not editable.<br />" + 
			"<br />" + 
			"<b>Model Version:</b> Describes the version of the data model this library is using. This field is not editable.<br />" + 
			"<br />" + 
			"<b>Comments:</b> This comment box is available to enter information that applies to the CQL library as a whole. Information entered here will show before the model declaration in the 'View CQL' section of the CQL Composer. Click the save icon to save comments entered. To remove a comment, remove the text from the comments field and click the save icon again.";

	public static final String MEASURE_CQL_LIBRARY_VALUE_SET = "<b>OID:</b> Each value set is identified with a unique Object Identifier (OID). Enter the OID for the desired value set to include in the measure and then click the Retrieve OID button. Note: You must be signed in to UMLS to retrieve a Value Set from the Value Set Authority Center (VSAC) through the MAT.<br />" + 
			"<br />" + 
			"<b>Program:</b> Value sets contained in the Value Set Authority Center (VSAC) can be assigned to specific programs. Use this field to indicate from which program you would like to retrieve the value set. This field is optional and may be left blank if the program is unknown.  If the program chosen does not contain the value set, an error message will be received.<br />" + 
			"<br />" + 
			"<b>Release:</b> In addition to a program, value sets contained in the Value Set Authority Center (VSAC) can be assigned to a specific release. Use this field to indicate from which release you would like to retrieve the value set. This field is optional and may be left blank if the release is unknown. A release can only be chosen if a program is selected. If the release chosen does not contain the value set, an error message will be received.<br />" + 
			"<br />" + 
			"<b>Name:</b> This field will show the name of any value set retrieved from VSAC using an OID. The other option for this field is to not retrieve a value set from VSAC and instead just enter a name into this field and click Apply. This creates a user-defined value set which can be used as a placeholder in measure logic until it can be replaced later with an actual value set.<br />" + 
			"<br />" + 
			"<b>Suffix:</b> The suffix field is available to add a numerical addition to the end of a value set name. This addition to the identifier is necessary if the user attempts to add two value sets with different OIDs but the same name. If none of the value sets in the measure have the same name, the suffix field is not needed.<br />" + 
			"<br />" + 
			"<b>Update From VSAC:</b> The Update From VSAC button will check the status of all value sets currently in the Applied Value Sets table. Any value sets that are no longer valid will show a caution triangle which means that the value set could not be found in the Value Set Authority Center. A green check mark indicates the value set is still valid. Updating the value sets is recommended if the measure has not been used recently. Note: User-defined value sets will always return a caution icon as they are not connected to any value set in VSAC.<br />" + 
			"<br />" + 
			"<b>Applied Value Sets table:</b> This table shows all value sets currently applied to this measure. This table contains an edit icon, a delete icon, and a checkbox which can be used in the process to copy the value set.<br />" + 
			"<br />" + 
			"<b>Copy, Paste, Select All, Clear:</b> These icons facilitate the copying of value sets from one measure to another measure or library. To copy a value set, select the checkboxes next to the value set(s) you wish to copy, click the copy icon, navigate to another measure or library and click on the paste icon above the Applied Value Sets table. To select all value sets at once, click the Select All icon, and to clear any checkmarks currently selected, click the Clear icon.";

	public static final String STANDALONE_CQL_LIBRARY_VALUE_SET = "<b>OID:</b> Each value set is identified with a unique Object Identifier (OID). Enter the OID for the desired value set to include in the library and then click the Retrieve OID button. Note: You must be signed in to UMLS to retrieve a Value Set from the Value Set Authority Center (VSAC) through the MAT.<br />" + 
			"<br />" + 
			"<b>Program:</b> Value sets contained in the Value Set Authority Center (VSAC) can be assigned to specific programs. Use this field to indicate from which program you would like to retrieve the value set. This field is optional and may be left blank if the program is unknown.  If the program chosen does not contain the value set, an error message will be received.<br />" + 
			"<br />" + 
			"<b>Release:</b> In addition to a program, value sets contained in the Value Set Authority Center (VSAC) can be assigned to a specific release. Use this field to indicate from which release you would like to retrieve the value set. This field is optional and may be left blank if the release is unknown. A release can only be chosen if a program is selected. If the release chosen does not contain the value set, an error message will be received.<br />" + 
			"<br />" + 
			"<b>Name:</b> This field will show the name of any value set retrieved from VSAC using an OID. The other option for this field is to not retrieve a value set from VSAC and instead just enter a name into this field and click Apply. This creates a user-defined value set which can be used as a placeholder in CQL logic until it can be replaced later with an actual value set.<br />" + 
			"<br />" + 
			"<b>Suffix:</b> The suffix field is available to add a numerical addition to the end of a value set name. This addition to the identifier is necessary if the user attempts to add two value sets with different OIDs but the same name. If none of the value sets in the library have the same name, the suffix field is not needed.<br />" + 
			"<br />" + 
			"<b>Update From VSAC:</b> The Update From VSAC button will check the status of all value sets currently in the Applied Value Sets table. Any value sets that are no longer valid will show a caution triangle which means that the value set could not be found in the Value Set Authority Center. A green check mark indicates the value set is still valid. Updating the value sets is recommended if the library has not been used recently. Note: User-defined value sets will always return a caution icon as they are not connected to any value set in VSAC.<br />" + 
			"<br />" + 
			"<b>Applied Value Sets table:</b> This table shows all value sets currently applied to this library. This table contains an edit icon, a delete icon, and a checkbox which can be used in the process to copy the value set.<br />" + 
			"<br />" + 
			"<b>Copy, Paste, Select All, Clear:</b> These icons facilitate the copying of value sets from one library to another library or measure. To copy a value set, select the checkboxes next to the value set(s) you wish to copy, click the copy icon, navigate to another library or measure and click on the paste icon above the Applied Value Sets table. To select all value sets at once, click the Select All icon, and to clear any checkmarks currently selected, click the Clear icon.";

	public static final String MEASURE_CQL_LIBRARY_PARAMETER = "<b>Left-hand Navigation:</b> In the left-hand navigation you will see a box that will contain the names of any parameters already included in the measure. Each measure already contains the Measurement Period parameter as a default. To open a parameter already added to the measure, double-click on the name. There is a search box just above this list which can be used to find a specific parameter name within the list.<br />" + 
			"<br />" + 
			"<b>+Add New:</b> Click this link when you are ready to add a new parameter. This will clear all the fields in preparation for the new entries.<br />" + 
			"<br />" + 
			"<b>Parameter Name:</b> This field is to give your parameter a name. This name will be used to reference the parameter within the CQL logic and therefore, must be unique to any other identifier within the measure.<br />" + 
			"<br />" + 
			"<b>Comment:</b> This comment box is to add a comment specific to the particular parameter being worked on. This comment will show in the View CQL section of the CQL Workspace just above the parameter and in the CQL file export.<br />" + 
			"<br />" + 
			"<b>Information:</b> Clicking the information icon will present you with a list of all of the shortcut keys available to you when you are typing your CQL logic into the CQL Editor.<br />" + 
			"<br />" + 
			"<b>Save:</b> The save icon is to save your current work.<br />" + 
			"<br />" + 
			"<b>Erase:</b> The erase icon will clear any logic entered into the CQL Editor. The name and comment boxes will not be cleared.<br />" + 
			"<br />" + 
			"<b>Delete:</b> The delete icon is to delete the currently selected parameter. Note: you will not be able to delete any default parameter or any parameter that has been used elsewhere within your CQL logic.<br />" + 
			"<br />" + 
			"<b>Build CQL Expression (CQL Editor):</b> The CQL Editor is in a box titled Build CQL Expression. This is the place designated for you to enter your CQL logic. For information on CQL or help with CQL logic please visit the eCQI Resource center CQL page (https://ecqi.healthit.gov/cql-clinical-quality-language)<br />" + 
			"<br />" + 
			"<b>Click to View CQL:</b> Clicking on this link will display the current CQL library for the measure being worked on. This is available to allow you to review other items you have already added to the measure while you are working on your current expression. Clicking the link again will collapse the area to save space.";

	public static final String STANDALONE_CQL_LIBRARY_PARAMETER = "<b>Left-hand Navigation:</b> In the left-hand navigation you will see a box that will contain the names of any parameters already included in the library. Each library already contains the Measurement Period parameter as a default. To open a parameter already added to the library, double-click on the name. There is a search box just above this list which can be used to find a specific parameter name within the list.<br />" + 
			"<br />" + 
			"<b>+Add New:</b> Click this link when you are ready to add a new parameter. This will clear all the fields in preparation for the new entries.<br />" + 
			"<br />" + 
			"<b>Parameter Name:</b> This field is to give your parameter a name. This name will be used to reference the parameter within the CQL logic and therefore, must be unique to any other identifier within the library.<br />" + 
			"<br />" + 
			"<b>Comment:</b> This comment box is to add a comment specific to the particular parameter being worked on. This comment will show in the View CQL section of the CQL Workspace just above the parameter and in the CQL file export.<br />" + 
			"<br />" + 
			"<b>Information:</b> Clicking the information icon will present you with a list of all of the shortcut keys available to you when you are typing your CQL logic into the CQL Editor.<br />" + 
			"<br />" + 
			"<b>Save:</b> The save icon is to save your current work.<br />" + 
			"<br />" + 
			"<b>Erase:</b> The erase icon will clear any logic entered into the CQL Editor. The name and comment boxes will not be cleared.<br />" + 
			"<br />" + 
			"<b>Delete:</b> The delete icon is to delete the currently selected parameter. Note: you will not be able to delete any default parameter or any parameter that has been used elsewhere within your CQL logic.<br />" + 
			"<br />" + 
			"<b>Build CQL Expression (CQL Editor):</b> The CQL Editor is in a box titled Build CQL Expression. This is the place designated for you to enter your CQL logic. For information on CQL or help with CQL logic please visit the eCQI Resource center CQL page (https://ecqi.healthit.gov/cql-clinical-quality-language)<br />" + 
			"<br />" + 
			"<b>Click to View CQL:</b> Clicking on this link will display the current CQL file for the library being worked on. This is available to allow you to review other items you have already added to the library while you are working on your current expression. Clicking the link again will collapse the area to save space.";
}
