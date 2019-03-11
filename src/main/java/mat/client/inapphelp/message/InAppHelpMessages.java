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
}
