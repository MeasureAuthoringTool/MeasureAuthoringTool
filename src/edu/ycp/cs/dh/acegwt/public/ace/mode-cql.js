define("ace/mode/cql_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextHighlightRules = require("./text_highlight_rules").TextHighlightRules;

var SqlHighlightRules = function() {

	var keywords = (
	        "-|!|(|)|*|,|.|/|:|[|]|^|{|}|~|+|<|=|>|after|all|and|as|asc|ascending|before|begins|begun|between|Boolean|by|called|case|cast|Code|codesystem|codesystems|" +
	        "collapse|Concept|contains|context|convert|date|DateTime|day|days|Decimal|default|define|desc|descending|difference|display|distinct|div|duration|during|" +
	        "else|end|ends|except|exists|false|flatten|from|function|hour|hours|if|in|include|included in|includes|Integer|intersect|Interval|is|less|let|library|List|" +
	        "maximum|meets|millisecond|milliseconds|minimum|minute|minutes|mod|month|months|more|not|null|occurs|of|or|or after|or before|or less|or more|overlaps|parameter|" +
	        "predecessor|predecessor of|private|properly|properly included in|properly includes|public|QDM|Quantity|return|same|second|seconds|singleton|singleton from|sort|" +
	        "sort by|start|starts|String|successor|such|such that|Sum|that|then|Time|time|timezone|to|true|Tuple|union|using|valueset|version|week|weeks|when|where|width|with|" +
	        "within|without|xor|year|years"
	    );
	    
	    var matTimingKeywords = (
	    		"after|after end|after start|before|before end|before start|during|ends|ends after|ends after end|ends after start|ends before|ends before end|ends before start|" +
	    		"ends during|ends properly during|ends properly within|ends properly within end|ends properly within start|ends same as|ends same as end|ends same as start|" +
	    		"ends same or after|ends same or after end|ends same or after start|ends same or before|ends same or before end|ends same or before start|ends within|ends within end|" +
	    		"ends within start|included in|includes|includes end|includes start|meets|meets after|meets before|overlaps|overlaps after|overlaps after|overlaps before|" +
	    		"overlaps before|properly during|properly included in|properly includes|properly includes end|properly includes start|properly within|properly within end|" +
	    		"properly within start|same as|same as|same as end|same as start|same or after|same or after end|same or after start|same or before|same or before end|" +
	    		"same or before start|starts|starts after|starts after end|starts after start|starts before|starts before end|starts before start|starts during|starts properly during|" +
	    		"starts properly within|starts properly within end|starts properly within start|starts same as|starts same as end|starts same as start|starts same or after|" +
	    		"starts same or after end|starts same or after start|starts same or before|starts same or before end|starts same or before start|starts within|starts within end|" +
	    		"starts within start|within"
	    );
	    
	    var matFunctionKeywords = (
	    		"Abs|AgeInDays|AgeInDaysAt|AgeInHours|AgeInHoursAt|AgeInMinutes|AgeInMinutesAt|AgeInMonths|AgeInMonthsAt|AgeInSeconds|" +
	    		"AgeInSecondsAt|AgeInYears|AgeInYearsAt|AllTrue|AnyTrue|Avg|CalculateAgeInDays|CalculateAgeInDaysAt|CalculateAgeInHours|" +
	    		"CalculateAgeInHoursAt|CalculateAgeInMinutes|CalculateAgeInMinutesAt|CalculateAgeInMonths|CalculateAgeInMonthsAt|CalculateAgeInSeconds|" +
	    		"CalculateAgeInSecondsAt|CalculateAgeInYears|CalculateAgeInYearsAt|Ceiling|Coalesce|Count|DateTime|Exp|First|Floor|IndexOf|Last|" +
	    		"Length|Ln|Log|Max|Median|Min|Mode|Now|PopulationStdDev|PopulationVariance|Round|StdDev|Sum|Time|TimeOfDay|Today|" +
	    		"Truncate|Variance"
	    );

	    var builtinConstants = (
	        
	    	//basic constants	
	    	'Patient|Population|MeasurementPeriod|' + 
	    	
	        //data type constants
	        '"Adverse Event"|"Allergy/Intolerance"|"Assessment, Performed"|"Assessment, Not Performed"|"Assessment, Recommended"|"Assessment, Not Recommended"|"Patient Care Experience"|' +
	    	'"Provider Care Experience"|"Care Goal"|"Communication: From Patient to Provider"|"Communication: From Patient to Provider, Not Done"|"Communication: From Provider to Patient"|' +
	    	'"Communication: From Provider to Patient, Not Done"|"Communication: From Provider to Provider"|"Communication: From Provider to Provider, Not Done"|"Diagnosis"|"Device, Applied"|' +
	    	'"Device, Not Applied"|"Device, Not Ordered"|"Device, Order"|"Device, Not Recommeded"|"Device, Recommeded"|"Diagnositic Study, Order"|"Diagnositic Study, Not Ordered"|' +
	    	'"Diagnositic Study, Performed"|"Diagnositic Study, Not Performed"|"Diagnositic Study, Recommended"|"Diagnositic Study, Not Recommended"|"Encounter, Active"|"Encounter, Order"|' +
	    	'"Encounter, Not Ordered"|"Encounter, Performed"|"Encounter, Not Performed"|"Encounter, Recommended"|"Encounter, Not Recommended"|"Family History"|"Immunization, Administered"|' +
	    	'"Immunization, Not Administered"|"Immunization, Order"|"Immunization, Not Ordered"|"Participation"|"Patient Characteristic"|"Patient Characteristic Birthdate"|' +
	    	'"Patient Characteristic Clinical Trial Participant"|"Patient Characteristic Ethnicity"|"Patient Characteristic Expired"|"Patient Characteristic Payer"|"Patient Characteristic Race"|' +
	    	'"Patient Characteristic Sex"|"Provider Characteristic"|"Intervention, Not Ordered"|"Intervention, Order"|"Intervention, Not Performed"|"Intervention, Performed"|' +
	    	'"Intervention, Not Recommended"|"Intervention, Recommended"|"Laboratory Test, Order"|"Laboratory Test, Not Ordered"|"Laboratory Test, Performed"|"Laboratory Test, Not Performed"|' +
	    	'"Laboratory Test, Recommended"|"Laboratory Test, Not Recommended"|"Medication, Active"|"Medication, Administered"|"Medication, Not Administered"|"Medication, Discharge"|' +
	    	'"Medication, Not Discharged"|"Medication, Dispensed"|"Medication, Not Dispensed"|"Medication, Order"|"Medication, Not Ordered"|"Physical Exam, Order"|"Physical Exam, Not Ordered"|' +
	    	'"Physical Exam, Performed"|"Physical Exam, Not Performed"|"Physical Exam, Recommended"|"Physical Exam, Not Recommended"|"Procedure, Order"|"Procedure, Not Ordered"|' +
	    	'"Procedure, Performed"|"Procedure, Not Performed"|"Procedure, Recommended"|"Procedure, Not Recommended"|"Substance, Administered"|"Substance, Not Administered"|"Substance, Order"|' +
	    	'"Substance, Not Ordered"|"Substance, Recommended"|"Substance, Not Recommended"|"Symptom"|' +
	        
	        //data type attribute constants
	        'activeDatetime|admissionSource|anatomicalApproachSite|anatomicalLocationSite|authorDatetime|birthDatetime|cause|code|diagnosis|dischargeDisposition|dosage|expiredDatetime|' +
	        'facilityLocation|frequency|id|incisionDatetime|lengthOfStay|locationPeriod|method|negationRationale|ordinality|participationPeriod|prevalencePeriod|principalDiagnosis|' +
	        'reason|recorder|referenceRange|refills|relatedTo|relationship|relevantPeriod|reporter|result|resultDatetime|route|severity|status|supply|targetOutcome|type'
	        
	    );

	    /*var builtinFunctions = (
	        'date|time|timezone|starts|ends|occurs|overlaps|Interval|Tuple|List|DateTime'
	    );*/

    var keywordMapper = this.createKeywordMapper({
        //"support.function": builtinFunctions,
        "keyword": keywords,
        "matTimingKeyword" : matTimingKeywords,
        "matFunctionKeyword" : matFunctionKeywords,
        "constant.language": builtinConstants
    }, "identifier", false);

    this.$rules = {
        "start" : [ {
            token : "comment",
            regex : "\\/\\/.*$"
        },  {
            token : "comment",
            start : "/\\*",
            end : "\\*/"
        }, {
            token : "string",           // " string
            regex : '".*?"'
        }, {
            token : "string",           // ' string
            regex : "'.*?'"
        }, {
            token : "constant.numeric", // float
            regex : "[+-]?\\d+(?:(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)?\\b"
        }, {
            token : keywordMapper,
            regex : "[a-zA-Z_$][a-zA-Z0-9_$]*\\b"
        }, {
            token : "keyword.operator",
            regex : "\\+|\\-|\\/|\\/\\/|%|<@>|@>|<@|&|\\^|~|<|>|<=|=>|==|!=|<>|="
        }, {
            token : "paren.lparen",
            regex : "[\\(]"
        }, {
            token : "paren.rparen",
            regex : "[\\)]"
        }, {
            token : "text",
            regex : "\\s+"
        } ]
    };
    this.normalizeRules();
};

oop.inherits(SqlHighlightRules, TextHighlightRules);

exports.SqlHighlightRules = SqlHighlightRules;
});

define("ace/mode/cql",["require","exports","module","ace/lib/oop","ace/mode/text","ace/mode/cql_highlight_rules","ace/range"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextMode = require("./text").Mode;
var SqlHighlightRules = require("./cql_highlight_rules").SqlHighlightRules;
var Range = require("../range").Range;

var Mode = function() {
    this.HighlightRules = SqlHighlightRules;
};
oop.inherits(Mode, TextMode);

(function() {

    this.lineCommentStart = "//";
    this.blockComment = {start: "/*", end: "*/"};
    this.$id = "ace/mode/cql";
}).call(Mode.prototype);

exports.Mode = Mode;

});
