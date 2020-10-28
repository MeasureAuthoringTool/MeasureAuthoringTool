package mat.client.inapphelp.message;

public class InAppHelpMessages {

	private static final String INFORMATION_HELP = "<b>Information:</b> Clicking the Information icon will present you with a list of all of the shortcut keys available to you when you are typing your CQL logic into the CQL Expression Editor.<br>";

	private static final String SAVE_HELP = "<b>Save:</b> The Save icon is to save your current work.<br>";

	private static final String ERASE_HELP = "<b>Erase:</b> The Erase icon will clear any logic entered into the CQL Expression Editor. The name and comment boxes will not be cleared.<br>";

	private static final String VIEW_CQL_LIBRARY_HELP =  "<b>CQL Library Viewer:</b> Clicking on this link will display the current CQL file for the library being worked on. This is available to allow you to review other items you have already added to the library while you are working on your current expression. Clicking the link again will collapse the area to save space.";

	private static final String BUILD_CQL_EXPRESSION_HELP =  "<b>CQL Expression Editor:</b> This is the place designated for you to enter your CQL logic. For information on CQL or help with CQL logic please visit the eCQI Resource center CQL page (https://ecqi.healthit.gov/cql-clinical-quality-language)<br>";

	private static final String VIEW_CQL_MEASURE_HELP =  "<b>CQL Library Viewer:</b> Clicking on this link will display the current CQL library for the measure being worked on. This is available to allow you to review other items you have already added to the measure while you are working on your current expression. Clicking the link again will collapse the area to save space.";

	public static final String MEASURE_CQL_LIBRARY_GENERAL_INFORMATION = "<b>CQL Library Name:</b> This is a name that will become the file name for the CQL File, the ELM file, and the JSON file exports. This name must be unique to all other CQL Library Names in the MAT, start with an alpha character or underscore followed by alpha-numeric character(s) or underscore(s) and can not contain spaces. This name identifies the library of the CQL expressions being created with this measure. To edit this name, make the change in the field, ensuring it follows the rules listed above, and click save.<br>" +
			"<br>" +
			"                  <b>CQL Library Version:</b> This is the current version of the CQL library that is being created with this measure. This field is not editable.<br>" +
			"<br>" +
			"                  <b>Using Model:</b> Describes the data model this measure is using. This field is not editable.<br>" +
			"<br>" +
			"                  <b>Model Version:</b> Describes the version of the data model this measure is using. This field is not editable.<br>" +
			"<br>" +
			"<b>Comments:</b> This comment box is available to enter information that applies to the CQL library as a whole. Information entered here will show before the model declaration on both the 'CQL Library Editor section of the CQL Workspace and in the CQL file export. Click the Save icon to save comments entered. To remove a comment, remove the text from the comments field and click the Save icon again.";

	public static final String STANDALONE_CQL_LIBRARY_GENERAL_INFORMATION = "<b>CQL Library Name:</b> This is a name that will become the file name for the CQL File, the ELM file, and the JSON file exports. This name must be unique to all other CQL Library Names in the MAT, start with an alpha character or underscore followed by alpha-numeric character(s) or underscore(s) and can not contain spaces. This name identifies the library of the CQL expressions being created with this measure. To edit this name, make the change in the field, ensuring it follows the rules listed above, and click save.<br>" +
			"<br>" +
			"<b>CQL Library Version:</b> This is the current version of the CQL library. This field is not editable.<br>" +
			"<br>" +
			"<b>Using Model:</b> Describes the data model this library is using. This field is not editable.<br>" +
			"<br>" +
			"<b>Model Version:</b> Describes the version of the data model this library is using. This field is not editable.<br>" +
			"<br>" +
			"<b>Comments:</b> This comment box is available to enter information that applies to the CQL library as a whole. Information entered here will show before the model declaration on both the 'CQL Library Editor section of the CQL Workspace and in the CQL file export. Click the Save icon to save comments entered. To remove a comment, remove the text from the comments field and click the Save icon again.";

	public static final String MEASURE_CQL_LIBRARY_VALUE_SET = "<b>OID:</b> Each value set is identified with a unique Object Identifier (OID). Enter the OID for the desired value set to include in the measure and then click the Retrieve OID button. Note: You must be signed in to UMLS to retrieve a Value Set from the Value Set Authority Center (VSAC) through the MAT.<br>" +
			"<br>" +
			"<b>Program:</b> Value sets contained in the Value Set Authority Center (VSAC) can be assigned to specific programs. Use this field to indicate from which program you would like to retrieve the value set. This field is optional and may be left blank if the program is unknown.  If the program chosen does not contain the value set, an error message will be received.<br>" +
			"<br>" +
			"<b>Release:</b> In addition to a program, value sets contained in the Value Set Authority Center (VSAC) can be assigned to a specific release. Use this field to indicate from which release you would like to retrieve the value set. This field is optional and may be left blank if the release is unknown. A release can only be chosen if a program is selected. If the release chosen does not contain the value set, an error message will be received.<br>" +
			"<br>" +
			"<b>Name:</b> This field will show the name of any value set retrieved from VSAC using an OID. The other option for this field is to not retrieve a value set from VSAC and instead just enter a name into this field and click Apply. This creates a <b>User-defined value set</b> which can be used as a placeholder in measure logic until it can be replaced later with an actual value set.<br>" +
			"<br>" +
			"<b>Suffix:</b> The suffix field is available to add a numerical addition to the end of a value set name. This addition to the identifier is necessary if the user attempts to add two value sets with different OIDs but the same name. If none of the value sets in the measure have the same name, the suffix field is not needed.<br>" +
			"<br>" +
			"<b>Update From VSAC:</b> The Update From VSAC button will check the status of all value sets currently in the Applied Value Sets table. Any value sets that are no longer valid will show a caution triangle which means that the value set could not be found in the Value Set Authority Center. A green check mark indicates the value set is still valid. Updating the value sets is recommended if the measure has not been used recently. Note: User-defined value sets will always return a caution icon as they are not connected to any value set in VSAC.<br>" +
			"<br>" +
			"<b>Applied Value Sets table:</b> This table shows all value sets currently applied to this measure. This table contains an Edit icon, a Delete icon, and a checkbox which can be used in the process to copy the value set.<br>" +
			"<br>" +
			"<b>Copy, Paste, Select All, Clear:</b> These icons facilitate the copying of value sets from one measure to another measure or library. To copy a value set, select the checkboxes next to the value set(s) you wish to copy, click the Copy icon, navigate to another measure or library and click on the Paste icon above the Applied Value Sets table. To select all value sets at once, click the Select All icon, and to clear any checkmarks currently selected, click the Clear icon.<br>" +
			"<br>" +
			"<b>Editing a Value Set:</b> To edit a value set, click on the edit icon (pencil) to put the information back into the top of the screen, add an updated OID and change the program and release if necessary (optional) and click the Retrieve button. Once the information is retrieved from VSAC, changes can be made to the suffix. Then click Apply to see the changes applied in the table.";

	public static final String MEASURE_CQL_LIBRARY_COMPONENT = "<b>Left-hand Navigation:</b> In the left-hand navigation you will see a box that will contain the the aliases of the component measures currently included in the measure. To review a component measure already added to the measure, double-click on the alias. There is a search box just above this list which can be used to find a specific definition name within the list.<br>" +
			"<br>" +
			"<b>Measure Name:</b> This field will fill in with the measure name of the component measure when the alias has been double-clicked in the left-hand navigation.<br>" +
			"<br>" +
			"<b>Owner Name:</b> This field will fill in with the owner of the component measure when the alias has been double-clicked in the left-hand navigation.</br>" +
			"<br>" +
			"<b>CQL Library Name:</b> This field will fill in with the CQL Library Name associated with the component measure when the alias has been double-clicked in the left-hand navigation.</br>" +
			"<br>" +
			"<b>CQL Library Viewer:</b> This area shows you the CQL file of the component measure when the alias has been double-clicked in the left-hand navigation. This area is uneditable.</br>" +
			"<br>" +
			"<b>Making changes to the component measure list:</b> To make changes to the component measures that are included within the composite measure, navigate to the Component Measures tab of the Measure Details page and click the Edit Component Measures button.";

	public static final String STANDALONE_CQL_LIBRARY_VALUE_SET = "<b>OID:</b> Each value set is identified with a unique Object Identifier (OID). Enter the OID for the desired value set to include in the library and then click the Retrieve OID button. Note: You must be signed in to UMLS to retrieve a Value Set from the Value Set Authority Center (VSAC) through the MAT.<br>" +
			"<br>" +
			"<b>Program:</b> Value sets contained in the Value Set Authority Center (VSAC) can be assigned to specific programs. Use this field to indicate from which program you would like to retrieve the value set. This field is optional and may be left blank if the program is unknown.  If the program chosen does not contain the value set, an error message will be received.<br>" +
			"<br>" +
			"<b>Release:</b> In addition to a program, value sets contained in the Value Set Authority Center (VSAC) can be assigned to a specific release. Use this field to indicate from which release you would like to retrieve the value set. This field is optional and may be left blank if the release is unknown. A release can only be chosen if a program is selected. If the release chosen does not contain the value set, an error message will be received.<br>" +
			"<br>" +
			"<b>Name:</b> This field will show the name of any value set retrieved from VSAC using an OID. The other option for this field is to not retrieve a value set from VSAC and instead just enter a name into this field and click Apply. This creates a <b>User-defined value set</b> which can be used as a placeholder in CQL logic until it can be replaced later with an actual value set.<br>" +
			"<br>" +
			"<b>Suffix:</b> The suffix field is available to add a numerical addition to the end of a value set name. This addition to the identifier is necessary if the user attempts to add two value sets with different OIDs but the same name. If none of the value sets in the library have the same name, the suffix field is not needed.<br>" +
			"<br>" +
			"<b>Update From VSAC:</b> The Update From VSAC button will check the status of all value sets currently in the Applied Value Sets table. Any value sets that are no longer valid will show a caution triangle which means that the value set could not be found in the Value Set Authority Center. A green check mark indicates the value set is still valid. Updating the value sets is recommended if the library has not been used recently. Note: User-defined value sets will always return a caution icon as they are not connected to any value set in VSAC.<br>" +
			"<br>" +
			"<b>Applied Value Sets table:</b> This table shows all value sets currently applied to this library. This table contains an Edit icon, a Delete icon, and a checkbox which can be used in the process to copy the value set.<br>" +
			"<br>" +
			"<b>Copy, Paste, Select All, Clear:</b> These icons facilitate the copying of value sets from one library to another library or measure. To copy a value set, select the checkboxes next to the value set(s) you wish to copy, click the Copy icon, navigate to another library or measure and click on the Paste icon above the Applied Value Sets table. To select all value sets at once, click the Select All icon, and to clear any checkmarks currently selected, click the Clear icon.<br>" +
			"<br>" +
			"<b>Editing a Value Set:</b> To edit a value set, click on the edit icon (pencil) to put the information back into the top of the screen, add an updated OID and change the program and release if necessary (optional) and click the Retrieve button. Once the information is retrieved from VSAC, changes can be made to the suffix. Then click Apply to see the changes applied in the table.";

	public static final String MEASURE_CQL_LIBRARY_PARAMETER = "<b>Left-hand Navigation:</b> In the left-hand navigation you will see a box that will contain the names of any parameters already included in the measure. Each measure already contains the Measurement Period parameter as a default. To open a parameter already added to the measure, double-click on the name. There is a search box just above this list which can be used to find a specific parameter name within the list.<br>" +
			"<br>" +
			"<b>+Add New:</b> Click this link when you are ready to add a new parameter. This will clear all the fields in preparation for the new entries.<br>" +
			"<br>" +
			"<b>Parameter Name:</b> This field is to give your parameter a name. This name will be used to reference the parameter within the CQL logic and therefore, must be unique to any other identifier within the measure.<br>" +
			"<br>" +
			"<b>Comment:</b> This comment box is to add a comment specific to the particular parameter being worked on. This comment will show in the CQL Library Editor section of the CQL Workspace just above the parameter and in the CQL file export.<br>" +
			"<br>" +
			INFORMATION_HELP +
			"<br>" +
			SAVE_HELP +
			"<br>" +
			ERASE_HELP +
			"<br>" +
			"<b>Delete:</b>  The Delete icon is to delete the currently selected parameter.<br>" +
			"<br>" +
			BUILD_CQL_EXPRESSION_HELP +
			"<br>" +
			VIEW_CQL_MEASURE_HELP;

	public static final String STANDALONE_CQL_LIBRARY_PARAMETER = "<b>Left-hand Navigation:</b> In the left-hand navigation you will see a box that will contain the names of any parameters already included in the library. Each library already contains the Measurement Period parameter as a default. To open a parameter already added to the library, double-click on the name. There is a search box just above this list which can be used to find a specific parameter name within the list.<br>" +
			"<br>" +
			"<b>+Add New:</b> Click this link when you are ready to add a new parameter. This will clear all the fields in preparation for the new entries.<br>" +
			"<br>" +
			"<b>Parameter Name:</b> This field is to give your parameter a name. This name will be used to reference the parameter within the CQL logic and therefore, must be unique to any other identifier within the library.<br>" +
			"<br>" +
			"<b>Comment:</b> This comment box is to add a comment specific to the particular parameter being worked on. This comment will show in the CQL Library Editor section of the CQL Workspace just above the parameter and in the CQL file export.<br>" +
			"<br>" +
			INFORMATION_HELP +
			"<br>" +
			SAVE_HELP +
			"<br>" +
			ERASE_HELP +
			"<br>" +
			"<b>Delete:</b>  The Delete icon is to delete the currently selected parameter.<br>" +
			"<br>" +
			BUILD_CQL_EXPRESSION_HELP +
			"<br>" +
			VIEW_CQL_LIBRARY_HELP;

	public static final String MEASURE_CQL_LIBRARY_FUNCTION = "<b>Left-hand Navigation:</b> In the left-hand navigation you will see a box that will contain the names of any functions already included in the measure. To open a function already added to the measure, double-click on the name.  There is a search box just above this list which can be used to find a specific function name within the list.<br>" +
			"<br>" +
			"<b>+Add New:</b> Click this link when you are ready to add a new function. This will clear all the fields in preparation for the new entries.<br>" +
			"<br>" +
			"<b>Function Name:</b> This field is to give your function a name. This name will be used to reference the function within the CQL logic and/or measure observations and therefore, must be unique to any other identifier within the measure.<br>" +
			"<br>" +
			"<b>Context:</b> Select whether your function is Patient or Population based.<br>" +
			"<br>" +
			"<b>Comment:</b> This comment box is to add a comment specific to the particular function being worked on. This comment will show in the CQL Library Editor section of the CQL Workspace just above the function and in the CQL file export.<br>" +
			"<br>" +
			"<b>Return Type:</b> This field will populate once the function is saved and will tell you what type of data this function is returning. Note: If there are errors in your CQL file, the Return Type will not populate. Fix any errors in your measure and then the field will show the information.<br>" +
			"<br>" +
			"<b>+Add Argument:</b> Arguments are a placeholder for data and indicates what type of data needs to be provided in order for the function to be calculated. Click on the +Add Argument link to add an argument into your function.<br>" +
			"<br>" +
			"<b>Added Arguments List:</b> This list will populate with any arguments added into the function. This list contains an edit (pencil) and a delete (trashcan) icon for each argument added.<br>" +
			"<br>" +
			INFORMATION_HELP +
			"<br>" +
			"<b>Insert:</b> Clicking this icon will open a dialog box that will allow you to choose items that will then be inserted into the CQL Workspace in the correct CQL format. Items include Parameters, Definitions, Functions, Timings, Pre-defined Functions, Applied Value Sets / Codes, and Attributes.<br>" +
			"<br>" +
			SAVE_HELP +
			"<br>" +
			ERASE_HELP +
			"<br>" +
			"<b>Delete:</b> The Delete icon is to delete the currently selected function.<br>" +
			"<br>" +
			BUILD_CQL_EXPRESSION_HELP +
			"<br>" +
			VIEW_CQL_MEASURE_HELP;

	public static final String STANDALONE_CQL_LIBRARY_FUNCTION = "<b>Left-hand Navigation:</b> In the left-hand navigation you will see a box that will contain the names of any functions already included in the library. To open a function already added to the library, double-click on the name.  There is a search box just above this list which can be used to find a specific function name within the list.<br>" +
			"<br>" +
			"<b>+Add New:</b> Click this link when you are ready to add a new function. This will clear all the fields in preparation for the new entries.<br>" +
			"<br>" +
			"<b>Function Name:</b> This field is to give your function a name. This name will be used to reference the function within the CQL logic and therefore, must be unique to any other identifier within the library.<br>" +
			"<br>" +
			"<b>Context:</b> Select whether your function is Patient or Population based.<br>" +
			"<br>" +
			"<b>Comment:</b> This comment box is to add a comment specific to the particular function being worked on. This comment will show in the CQL Library Editor section of the CQL Workspace just above the function and in the CQL file export.<br>" +
			"<br>" +
			"<b>Return Type:</b> This field will populate once the function is saved and will tell you what type of data this function is returning. Note: If there are errors in your CQL file, the Return Type will not populate. Fix any errors in your library and then the field will show the information.<br>" +
			"<br>" +
			"<b>+Add Argument:</b> Arguments are a placeholder for data and indicates what type of data needs to be provided in order for the function to be calculated. Click on the +Add Argument link to add an argument into your function.<br>" +
			"<br>" +
			"<b>Added Arguments List:</b> This list will populate with any arguments added into the function. This list contains an edit (pencil) and a delete (trashcan) icon for each argument added.<br>" +
			"<br>" +
			INFORMATION_HELP +
			"<br>" +
			"<b>Insert:</b> Clicking this icon will open a dialog box that will allow you to choose items that will then be inserted into the CQL Editor in the correct CQL format. Items include Parameters, Definitions, Functions, Timings, Pre-defined Functions, Applied Value Sets / Codes, and Attributes.<br>" +
			"<br>" +
			SAVE_HELP +
			"<br>" +
			ERASE_HELP +
			"<br>" +
			"<b>Delete:</b> The Delete icon is to delete the currently selected function.<br>" +
			"<br>" +
			BUILD_CQL_EXPRESSION_HELP +
			"<br>" +
			VIEW_CQL_LIBRARY_HELP;

	public static final String MEASURE_CQL_LIBRARY_INCLUDES = "<b>Left-hand Navigation:</b> In the left-hand navigation you will see a box that will contain the assigned aliases of any included libraries. To open a library already included within the measure, double-click on the alias. There is a search box just above this list which can be used to find a specific library alias within the list.<br>" +
			"<br>" +
			"<b>Library Alias:</b> Each included library must have an alias which will be used as an identifier to reference expressions from the included library within the CQL logic. Each alias must be unique to any other identifier within the measure. The alias must start with an alpha-character or underscore followed by alpha-numeric characters or underscores and must not contain spaces.<br>" +
			"<br>" +
			"<b>Search:</b> The search box provided is to enable you to search for available libraries within the MAT that meet the criteria for inclusion into a measure. In order to be included a library must be in a versioned state, must use the same  QDM or FHIR version and model as the measure, can not have the same name as the measure, and can not be in the same measure family as the measure. Enter text into the field and then click the Search button.<br>" +
			"<br>" +
			"<b>Available Libraries:</b> This box will list the libraries that were returned from the search. To select a library to include in your measure click the checkbox next to the library you wish to include and then click the Save icon in the top-right of the screen.<br>" +
			"<br>" +
			"<b>CQL Library Viewer:</b> When a library in the list of available libraries is selected, the CQL file for that library will display here. This gives you a way to ensure this is the one you want before you click the Save button.";

	public static final String STANDALONE_CQL_LIBRARY_INCLUDES = "<b>Left-hand Navigation:</b> In the left-hand navigation you will see a box that will contain the assigned aliases of any included libraries. To open a library already included within the library, double-click on the alias. There is a search box just above this list which can be used to find a specific library alias within the list.<br>" +
			"<br>" +
			"<b>Library Alias:</b> Each included library must have an alias which will be used as an identifier to reference expressions from the included library within the CQL logic. Each alias must be unique to any other identifier within the library. The alias must start with an alpha-character or underscore followed by alpha-numeric characters or underscores and must not contain spaces.<br>" +
			"<br>" +
			"<b>Search:</b> The search box provided is to enable you to search for available libraries within the MAT that meet the criteria for inclusion into the CQL library. In order to be included a library must be in a versioned state, must use the same QDM or FHIR version and model as the library it is being included in, can not have the same name as the library it is being included in, and can not be in the same library family as the library it is being included in. Enter text into the field and then click the Search button.<br>" +
			"<br>" +
			"<b>Available Libraries:</b> This box will list the libraries that were returned from the search. To select a library to include in your library click the checkbox next to the library you wish to include and then click the Save icon in the top-right of the screen.<br>" +
			"<br>" +
			"<b>CQL Library Viewer:</b> When a library in the list of available libraries is selected, the CQL file for that library will display here. This gives you a way to ensure this is the one you want before you click the Save button.";

	public static final String CQL_LIBRARY_INSERT_MODAL ="<b>Item Type:</b> The item type field contains a list of items that can be inserted into the CQL editor. Options under Item Type include:<br>" +
			"<br>" +
			"<b>Parameters:</b> Selecting Parameters in the Item Type field will populate the Item Name dropdown with a list of the parameters that are available.<br>" +
			"<br>" +
			"<b>Definitions:</b> Selecting Definitions in the Item Type field will populate the Item Name dropdown with a list of the definitions that are available.<br>" +
			"<br>" +
			"<b>Functions:</b> Selecting Functions in the Item Type field will populate the Item Name dropdown with a list of the User-defined functions available.<br>" +
			"<br>" +
			"<b>Timing:</b> Selecting Timing in the Item Type field will populate the Item Name dropdown with a list of common timings.<br>" +
			"<br>" +
			"<b>Pre-defined Functions:</b> Selecting Pre-defined Functions in the Item Type field will populate the Item Name dropdown with a list of functions that are pre-loaded into the MAT for your use.<br>" +
			"<br>" +
			"<b>Applied Value Sets/Codes:</b> Selecting Applied Value Sets/Codes in the Item Type field will populate the Item Name dropdown with a list of all value sets and directly-referenced codes available within this measure/library. Additionally, a <b>Datatype</b> dropdown field will appear which allows you to select a QDM datatype to pair with the chosen value set or code.<br>" +
			"<br>" +
			"<b>Attributes:</b> Selecting Attributes in the Item Type field will take you to the Attribute Builder within the MAT.";

	public static final String CQL_LIBRARY_ATTRIBUTE_MODAL = "<b>Attributes By Data Type:</b> This field is optional and provides a list of the QDM data types available. Making a choice in this field will not affect how the attribute looks once it is inserted into the CQL Editor; however, it will narrow down the list of attributes in the next field to only those attributes that are applicable to the data type chosen.<br>" +
			"<br>" +
			"<b>Attributes:</b> This field is a list of the attributes available. Attributes chosen must be compatible with the type of data in your expression. If a choice was made in the Attributes By Data Type field, this list will only show the attributes that are compatible with that data type.<br>" +
			"<br>" +
			"<b>Mode:</b> the Mode field has four possible selections; Comparison, Computative, Nullable, and Value Sets. What will be displayed in this field is dependent on the choice made in the Attributes field. Select Comparison when you want to compare one item to another such as greater than or less than. Select Computative when you want to perform a mathematical computation such as plus five days. Select Nullable when you want to determine if something is or is not null. Select Value Sets when you want to determine if something is contained in a value set you have previously entered.<br>" +
			"<br>" +
			"<b>Mode Details:</b> The Mode Details field is filtered based on what was chosen in the Mode field. For example, if the mode chosen was Nullable, the options in the Mode field would be Is Null or Is Not Null. If the mode chosen was Computative, the Mode Details field would contain the mathematical actions such as +, -, /, etc.<br>" +
			"<br>" +
			"<b>Date/Time:</b> The Date/Time field is only enabled when an attribute and/or mode is chosen for which entering a date or a time makes sense. For example, if the attribute 'refills' is chosen with a mode of Comparison and <= in the Mode Details field, then adding a date or time would not make sense and therefore, the field is disabled. When entering a date, the user must state the items in this order: Year, Month, Day. For example, only a year could be entered but if a month was needed a year would need to be entered as well. If a specific day was needed, a year and a month would need to be added, and so forth. The time fields function the same way. To use minutes, the hour must be added, etc.<br>" +
			"<br>" +
			"<b>Quantity and Units:</b> The Quantity and Units fields operate together. These fields will be enabled or disabled based on the choices made for the Attribute and Mode fields. For example, if the Attribute is authorDatetime and the Mode is Comparison with the Mode Details as <=, then entering a quantity and a unit does not make sense and the fields will be disabled. However, if Mode was Computative and the Mode Details was +, then a quantity and unit entry would be valid such as 2 months.<br>" +
			"<br>" +
			"<b>Insert Button:</b> The Insert button will insert the attribute built into the CQL editor of the expression being edited at the point in which the cursor was residing prior to the Insert icon being selected.<br>" +
			"<br>" +
			"<b>Cancel Button:</b> the Cancel button will close the attribute builder and not save any changes or selections that were made inside of it.";

	public static final String CQL_LIBRARY_ATTRIBUTE_MODAL_FHIR = "<b>Attributes By Data Type:</b> This field is optional and provides a list of the FHIR data types available. Making a choice in this field will not affect how the attribute looks once it is inserted into the CQL Editor; however, it will narrow down the list of attributes in the next field to only those attributes that are applicable to the data type chosen.<br>" +
			"<br>" +
			"<b>Attributes:</b> This field is a list of the attributes available. Attributes chosen must be compatible with the type of data in your expression. If a choice was made in the Attributes By Data Type field, this list will only show the attributes that are compatible with that data type.<br>" +
			"<br>" +
			"<b>Mode:</b> the Mode field has four possible selections; Comparison, Computative, Nullable, and Value Sets. What will be displayed in this field is dependent on the choice made in the Attributes field. Select Comparison when you want to compare one item to another such as greater than or less than. Select Computative when you want to perform a mathematical computation such as plus five days. Select Nullable when you want to determine if something is or is not null. Select Value Sets when you want to determine if something is contained in a value set you have previously entered.<br>" +
			"<br>" +
			"<b>Mode Details:</b> The Mode Details field is filtered based on what was chosen in the Mode field. For example, if the mode chosen was Nullable, the options in the Mode field would be Is Null or Is Not Null. If the mode chosen was Computative, the Mode Details field would contain the mathematical actions such as +, -, /, etc.<br>" +
			"<br>" +
			"<b>Date/Time:</b> The Date/Time field is only enabled when an attribute and/or mode is chosen for which entering a date or a time makes sense. For example, if the attribute 'refills' is chosen with a mode of Comparison and <= in the Mode Details field, then adding a date or time would not make sense and therefore, the field is disabled. When entering a date, the user must state the items in this order: Year, Month, Day. For example, only a year could be entered but if a month was needed a year would need to be entered as well. If a specific day was needed, a year and a month would need to be added, and so forth. The time fields function the same way. To use minutes, the hour must be added, etc.<br>" +
			"<br>" +
			"<b>Quantity and Units:</b> The Quantity and Units fields operate together. These fields will be enabled or disabled based on the choices made for the Attribute and Mode fields. For example, if the Attribute is authorDatetime and the Mode is Comparison with the Mode Details as <=, then entering a quantity and a unit does not make sense and the fields will be disabled. However, if Mode was Computative and the Mode Details was +, then a quantity and unit entry would be valid such as 2 months.<br>" +
			"<br>" +
			"<b>Insert Button:</b> The Insert button will insert the attribute built into the CQL editor of the expression being edited at the point in which the cursor was residing prior to the Insert icon being selected.<br>" +
			"<br>" +
			"<b>Cancel Button:</b> the Cancel button will close the attribute builder and not save any changes or selections that were made inside of it.";

	private static final String EDIT_CODE_HELP = "<b>Editing a Code:</b> To edit a code, click on the edit icon (pencil) to put the information back into the top of the screen, add an updated REST API URL (from VSAC) if necessary and click the Retrieve button. Once the information is retrieved from VSAC, changes can be made to the version inclusion and the suffix if necessary. Then click Apply to see the changes applied in the table.";

	public static final String MEASURE_CQL_LIBRARY_CODES = "<b>Code URL:</b> Codes are retrieved from VSAC by use of a REST API URL found on the VSAC website. To retrieve a code, copy that REST API URL from VSAC, paste it into this field, and then click the Retrieve button. Note: you must be logged in to UMLS to successfully retrieve a code from VSAC.<br>" +
			"<br>" +
			"<b>Code Descriptor:</b> This field will fill in with the code descriptor associated with a code once it has been successfully retrieved from VSAC. This field is not editable.<br>" +
			"<br>" +
			"<b>Suffix:</b> The suffix field is available for users to add a numerical addition to the end of a code descriptor. This addition is a necessary part of the identifier if the user attempts to add two codes that have different REST API URLs but the same code descriptor. Note: if none of the codes in the measure have the same code descriptor you do not need to use the suffix field.<br>" +
			"<br>" +
			"<b>Code:</b> This field will populate with the identifier of the code when one has been successfully retrieved from VSAC. This field is not editable.<br>" +
			"<br>" +
			"<b>Code System:</b> This field will populate with the code system of the code when one has been successfully retrieved from VSAC. This field is not editable.<br>" +
			"<br>" +
			"<b>Code System Version:</b> This field will populate with the version of the code system associated with the code when one has been successfully retrieved from VSAC.<br>" +
			"<br>" +
			"<b>Include Code System Version:</b> This checkbox allows you to choose whether or not you want the code system version declared within the export files produced from the MAT. For example, if you are using a code that is no longer available in the most current code system version.<br>" +
			"<br>" +
			"<b>Applied Codes table:</b> This table shows all directly-referenced codes currently applied to this measure. This table contains an Edit icon, a Delete icon, and a checkbox which can be used in the process to copy the code.<br>" +
			"<br>" +
			"<b>Copy, Paste, Select All, Clear:</b> These icons facilitate the copying of codes from one measure to another measure or library. To copy a code, click the checkboxes next to the code(s) you wish to copy, click the Copy icon, navigate to another measure or library and click on the Paste icon above the Applied Codes table. To select all codes at once, click the Select All icon, and to clear any checkmarks currently selected, click the Clear icon.<br>" +
			"<br>"
			+ EDIT_CODE_HELP;

	public static final String STANDALONE_CQL_LIBRARY_CODES = "<b>Code URL:</b> Codes are retrieved from VSAC by use of a REST API URL found on the VSAC website. To retrieve a code, copy that REST API URL from VSAC, paste it into this field, and then click the Retrieve button. Note: you must be logged in to UMLS to successfully retrieve a code from VSAC.<br>" +
			"<br>" +
			"<b>Code Descriptor:</b> This field will fill in with the code descriptor associated with a code once it has been successfully retrieved from VSAC. This field is not editable.<br>" +
			"<br>" +
			"<b>Suffix:</b> The suffix field is available for users to add a numerical addition to the end of a code descriptor. This addition is a necessary part of the identifier if the user attempts to add two codes that have different REST API URLs but the same code descriptor. Note: if none of the codes in the library have the same code descriptor you do not need to use the suffix field.<br>" +
			"<br>" +
			"<b>Code:</b> This field will populate with the identifier of the code when one has been successfully retrieved from VSAC. This field is not editable.<br>" +
			"<br>" +
			"<b>Code System:</b> This field will populate with the code system of the code when one has been successfully retrieved from VSAC. This field is not editable.<br>" +
			"<br>" +
			"<b>Code System Version:</b> This field will populate with the version of the code system associated with the code when one has been successfully retrieved from VSAC.<br>" +
			"<br>" +
			"<b>Include Code System Version:</b> This checkbox allows you to choose whether or not you want the code system version declared within the export files produced from the MAT. For example, if you are using a code that is no longer available in the most current code system version.<br>" +
			"<br>" +
			"<b>Applied Codes table:</b> This table shows all directly-referenced codes currently applied to this library. This table contains an Edit icon, a Delete icon, and a checkbox which can be used in the process to copy the code.<br>" +
			"<br>" +
			"<b>Copy, Paste, Select All, Clear:</b> These icons facilitate the copying of codes from one library to another library or measure. To copy a code, click the checkboxes next to the code(s) you wish to copy, click the Copy icon, navigate to another library or measure and click on the Paste icon above the Applied Codes table. To select all codes at once, click the Select All icon, and to clear any checkmarks currently selected, click the Clear icon.<br>" +
			"<br>"
			+ EDIT_CODE_HELP;



	public static final String MEASURE_CQL_LIBRARY_DEFINITION = "<b>Left-hand Navigation:</b> In the left-hand navigation you will see a box that will contain the names of any definitions already included in the measure. Each measure already contains the four supplemental data element definitions as defaults. To open a definition already added to the measure, double-click on the name.  There is a search box just above this list which can be used to find a specific definition name within the list.<br>" +
			"<br>" +
			"<b>+Add New:</b> Select this link when you are ready to add a new definition. This will clear all the fields in preparation for the new entries.<br>" +
			"<br>" +
			"<b>Definition Name:</b> This field is to give your definition a name. This name will be used to reference the definition within the CQL logic and/or populations and therefore, must be unique to any other identifier within the measure.<br>" +
			"<br>" +
			"<b>Context:</b> Select whether your definition is Patient or Population based.<br>" +
			"<br>" +
			"<b>Comment:</b> This comment box is to add a comment specific to the particular definition being worked on. This comment will show in the CQL Library Editor section of the CQL Workspace just above the definition and in the CQL file export.<br>" +
			"<br>" +
			"<b>Return Type:</b> This field will populate once the definition is saved and will tell you what type of data this definition is returning. Note: If there are errors in your CQL file, the Return Type will not populate. Fix any errors in your measure and then the field will show the information.<br>" +
			"<br>" +
			INFORMATION_HELP +
			"<br>" +
			"<b>Expression Builder:</b> Selecting this icon will open the Expression Builder which will walk users through creating a CQL expression by selecting options from dropdown menus.<br>" +
			"<br>" +
			"<b>Insert:</b> Clicking this icon will open a dialog box that will allow you to choose items that will then be inserted into the CQL Workspace in the correct CQL format. Items include Parameters, Definitions, Functions, Timings, Pre-defined Functions, Applied Value Sets / Codes, and Attributes.<br>" +
			"<br>" +
			SAVE_HELP +
			"<br>" +
			ERASE_HELP +
			"<br>" +
			"<b>Delete:</b> The Delete icon is to delete the currently selected definition.<br>" +
			"<br>" +
			BUILD_CQL_EXPRESSION_HELP +
			"<br>" +
			VIEW_CQL_MEASURE_HELP;


	public static final String STANDALONE_CQL_LIBRARY_DEFINITION = "<b>Left-hand Navigation:</b> In the left-hand navigation you will see a box that will contain the names of any definitions already included in the library. To open a definition already added to the library, double-click on the name.  There is a search box just above this list which can be used to find a specific definition name within the list.<br>" +
			"<br>" +
			"<b>+Add New:</b> Select this link when you are ready to add a new definition. This will clear all the fields in preparation for the new entries.<br>" +
			"<br>" +
			"<b>Definition Name:</b> This field is to give your definition a name. This name will be used to reference the definition within the CQL logic and therefore, must be unique to any other identifier within the library.<br>" +
			"<br>" +
			"<b>Context:</b> Select whether your definition is Patient or Population based.<br>" +
			"<br>" +
			"<b>Comment:</b> This comment box is to add a comment specific to the particular definition being worked on. This comment will show in the CQL Library Editor section of the CQL Workspace just above the definition and in the CQL file export.<br>" +
			"<br>" +
			"<b>Return Type:</b> This field will populate once the definition is saved and will tell you what type of data this definition is returning. Note: If there are errors in your CQL file, the Return Type will not populate. Fix any errors in your library and then the field will show the information.<br>" +
			"<br>" +
			INFORMATION_HELP +
			"<br>" +
			"<b>Expression Builder:</b> Selecting this icon will open the Expression Builder which will walk users through creating a CQL expression by selecting options from dropdown menus.<br>" +
			"<br>" +
			"<b>Insert:</b> Clicking this icon will open a dialog box that will allow you to choose items that will then be inserted into the CQL Editor in the correct CQL format. Items include Parameters, Definitions, Functions, Timings, Pre-defined Functions, Applied Value Sets / Codes, and Attributes.<br>" +
			"<br>" +
			SAVE_HELP +
			"<br>" +
			ERASE_HELP +
			"<br>" +
			"<b>Delete:</b> The Delete icon is to delete the currently selected definition.<br>" +
			"<br>" +
			BUILD_CQL_EXPRESSION_HELP +
			"<br>" +
			VIEW_CQL_LIBRARY_HELP;

	public static final String MEASURE_CQL_LIBRARY_VIEW_CQL =
			"<b>Information:</b> Selecting the Information icon will present you with a list of all of the shortcut keys available to you when you are typing your CQL logic into the CQL Library Editor.<br>" +
			"<br>" +
			"<b>Insert:</b> Clicking this icon will open a dialog box that will allow you to choose items that will then be inserted into the CQL Library Editor in the correct CQL format. Items include Parameters, Definitions, Functions, Timings, Pre-defined Functions, Applied Value Sets / Codes, and Attributes.<br>" +
			"<br>" +
			"<b>Save:</b> The Save icon is to save your current work.<br>" +
			"<br>" +
			"<b>Export Error File:</b> If your CQL file has errors or warnings, you can export a report that will show your entire CQL file with line numbers and list which line numbers are showing the errors and/or warnings. This file will open as a .txt file.<br>" +
			"<br>" +
			"<b>Enable the CQL Library Editor:</b> The CQL Library Editor is a feature that users will need to enable before it can be used. To enable editing on this page, click on the profile icon in the upper-right-hand corner, choose MAT Account, and on the Personal Information page, click the checkbox titled Enable the CQL Library Editor, enter your current password, and click save and continue.<br>" +
			"<br>" +
			"<b>CQL Library Editor:</b> This section displays the entire CQL file for the measure as it currently stands. If there are any errors they will be indicated with a message or on the line number with a red square. If there are any warnings they will be shown with a message or on the line number with a yellow triangle. Hover over the error or warning icons to see the messages. This area is not editable by default. If you want to be able to edit your CQL library within this section, pull down the options under the profile icon in the upper-right corner of the screen, select MAT Account, go to the Personal Information Tab, and check the checkbox Enable the CQL Library Editor.";

	public static final String STANDALONE_CQL_LIBRARY_VIEW_CQL =
			"<b>Information:</b> Selecting the Information icon will present you with a list of all of the shortcut keys available to you when you are typing your CQL logic into the CQL Library Editor.<br>" +
			"<br>" +
			"<b>Insert:</b> Clicking this icon will open a dialog box that will allow you to choose items that will then be inserted into the CQL Library Editor in the correct CQL format. Items include Parameters, Definitions, Functions, Timings, Pre-defined Functions, Applied Value Sets / Codes, and Attributes.<br>" +
			"<br>" +
			"<b>Save:</b> The Save icon is to save your current work.<br>" +
			"<br>" +
			"<b>Export Error File:</b> If your CQL file has errors or warnings, you can export a report that will show your entire CQL file with line numbers and list which line numbers are showing the errors and/or warnings. This file will open as a .txt file.<br>" +
			"<br>" +
			"<b>Enable the CQL Library Editor:</b> The CQL Library Editor is a feature that users will need to enable before it can be used. To enable editing on this page, click on the profile icon in the upper-right-hand corner, choose MAT Account, and on the Personal Information page, click the checkbox titled Enable the CQL Library Editor, enter your current password, and click save and continue.<br>" +
			"<br>" +
			"<b>CQL Library Editor:</b> This section displays the entire CQL file for the library as it currently stands. If there are any errors they will be indicated with a message or on the line number with a red square. If there are any warnings they will be shown with a message or on the line number with a yellow triangle. Hover over the error or warning icons to see the messages. This area is not editable by default. If you want to be able to edit your CQL library within this section, pull down the options under the profile icon in the upper-right corner of the screen, select MAT Account, go to the Personal Information Tab, and check the checkbox Enable the CQL Library Editor.";

	public static final String EXPRESSION_BUILDER = "<b>Comparison:</b> Choose this type of expression if you want to compare one thing to another.</span><br>" +
			"<span style='margin-left:2em'>Example: AgeInYearsAt (start of “Measurement Period”) > 18</span><br>" +
			"<br>" +
			"<b>Computation:</b> Choose this type of expression if you want to perform a mathematical calculation.<br>" +
			"<span style='margin-left:2em'>Example: “Measurement Period” – 5 years</span><br>" +
			"<br>" +
			"<b>Data Element or Retrieve</b>: Choose this type of expression if you want to refer to a QDM Datatype or a QDM Datatype / Value Set or Code combination.<br>" +
			"<span style='margin-left:2em'>Example: [Encounter, Performed : “Office visit”]</span><br>" +
			"<br>" +
			"<b>Date/Time</b>: Choose this type of expression if you want to use a date, a time, or a date/time combination.<br>" +
			"<span style='margin-left:2em'>Example – Date: @2019-02-28</span><br>" +
			"<span style='margin-left:2em'>Example – Time: @T13:35:00.000</span><br>" +
			"<span style='margin-left:2em'>Example – DateTime: @2019-02-28T13:35:00.000</span><br>" +
			"<br>" +
			"<b>Definition</b>: Choose this type of expression when you want to choose a definition you have already written.<br>" +
			"<span style='margin-left:2em'>Example: “Qualifying Encounters”</span><br>" +
			"<br>" +
			"<b>Exists</b>: Choose this type of expression when you want to express that something is present.<br>" +
			"<span style='margin-left:2em'>Example: exists [Diagnosis : “Diabetes”]</span><br>" +
			"<br>" +
			"<b>Function</b>: Choose this type of expression when you want to use either a pre-defined CQL function or a function you are previously written.<br>" +
			"<span style='margin-left:2em'>Example: First (“Hospital Observation Encounter”)</span><br>" +
			"<br>" +
			"<b>Interval</b>: Choose this type of expression when you want to build an interval of values.<br>" +
			"<span style='margin-left:2em'>Example: Interval[@2019-02, @2020-02]</span><br>" +
			"<br>" +
			"<b>Negation (not)</b>: Choose this expression type when you want to say something is negative.<br>" +
			"<span style='margin-left:2em'>Example: not exists [Encounter, Performed : “Emergency Room Visit”]</span><br>" +
			"<br>" +
			"<b>Null (is null/not null)</b>: Choose this expression type when you want to say something is null or not null.<br>" +
			"<span style='margin-left:2em'>Example: [Diagnosis : “Diabetes] is not null</span><br>" +
			"<br>" +
			"<b>Quantity</b>: Choose this expression type when you want to state a particular number or number and unit combination.<br>" +
			"<span style='margin-left:2em'>Example: 5 ‘mg/dL’</span><br>" +
			"<br>" +
			"<b>Query</b>: Choose this expression type when you want to write a query that starts with a source and then filters that source data by other options.<br>" +
			"<span style='margin-left:2em'>Example: [Encounter, Performed : Office Visit] visit</span><br>" +
			"<span style='margin-left:2em'>where visit.relevantperiod during “Measurement Period”</span><br>" +
			"<br>" +
			"<b>Start of/End of</b>: Choose this expression type when you want to find the start or end of something.<br>" +
			"<span style='margin-left:2em'>Example: start of “Measurement Period”</span><br>" +
			"<br>" +
			"<b>True/False (is true/false)</b>: Choose this expression type when you want to show that something is true or false.<br>" +
			"<span style='margin-left:2em'>Example: “In Hospice” is true</span><br>" +
			"<br>" +
			"<b>Timing</b>: Choose this expression type when you want to state that something has a relationship in time to something else.<br>" +
			"<span style='margin-left:2em'>Example: “Diabetes Diagnosis”.authorDatetime starts on or before @2019-02-28</span>";

	private InAppHelpMessages() {
		throw new IllegalStateException("InAppHelpMessages class");
	}

}
