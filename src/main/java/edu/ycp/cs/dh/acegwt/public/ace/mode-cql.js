define("ace/mode/cql_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextHighlightRules = require("./text_highlight_rules").TextHighlightRules;

var SqlHighlightRules = function() {

	var keywords = (
	        "after|all|and|as|asc|ascending|before|begins|begun|between|Boolean|by|called|case|cast|Choice|Code|codesystem|codesystems|" +
	        "collapse|Concept|contains|context|convert|date|Date|DateTime|day|days|Decimal|default|define|desc|descending|difference|display|distinct|div|duration|during|" +
	        "else|end|ends|except|exists|expand|false|flatten|from|function|hour|hours|if|implies|in|include|included|included in|includes|Integer|intersect|Interval|is|less|let|library|List|" +
	        "maximum|meets|millisecond|milliseconds|minimum|minute|minutes|mod|month|months|more|not|null|occurs|of|on|or|or after|or before|or less|or more|overlaps|parameter|per|" +
	        "predecessor|predecessor of|private|properly|properly included in|properly includes|public|QDM|Quantity|Ratio|return|same|second|seconds|singleton|singleton from|sort|" +
	        "sort by|start|starts|String|successor|such|such that|Sum|that|than|then|Time|time|timezone|to|true|Tuple|union|using|valueset|version|week|weeks|when|where|width|with|" +
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
	    		"AgeInSecondsAt|AgeInWeeks|AgeInWeeksAt|AgeInYears|AgeInYearsAt|AllTrue|AnyTrue|Avg|CalculateAgeInDays|CalculateAgeInDaysAt|CalculateAgeInHours|" +
	    		"CalculateAgeInHoursAt|CalculateAgeInMinutes|CalculateAgeInMinutesAt|CalculateAgeInMonths|CalculateAgeInMonthsAt|CalculateAgeInSeconds|" +
	    		"CalculateAgeInSecondsAt|CalculateAgeInWeeks|CalculateAgeInWeeksAt|CalculateAgeInYears|CalculateAgeInYearsAt|Ceiling|Coalesce|Count|DateTime|Exp|First|" +
	    		"Floor|GeometricMean|HighBoundary|IndexOf|Last|Length|Ln|Log|LowBoundary|Lower|Matches|Max|Median|Min|Mode|Now|PopulationStdDev|PopulationVariance|Precision|" +
	    		"Product|Round|Size|Skip|Split|SplitOnMatches|StartsWith|StdDev|Substring|Sum|Tail|Take|Time|TimeOfDay|ToBoolean|ToDate|ToDateTime|ToDecimal|ToInteger|ToQuantity|" +
	    		"ToString|ToTime|Today|Truncate|Upper|Variance|"
	    );

	    var builtinConstants = (
	        
	    	//basic constants	
	    	'Patient|' + 
	    		        
	        //data type attribute constants
	        'activeDatetime|admissionSource|anatomicalLocationSite|authorDatetime|birthDatetime|category|cause|code|components|daysSupplied|denominator|diagnoses|dischargeDisposition|dispenser|dosage|expiredDatetime|' +
	        'facilityLocation|facilityLocations|frequency|high|id|identifier|incisionDatetime|lengthOfStay|linkedPatientId|locationPeriod|low|medium|method|namingSystem|negationRationale|numerator|participationPeriod|patientId|performer|presentOnAdmissionIndicator|prevalencePeriod|' +
	        'priority|prescriber|qualification|rank|reason|recipient|receivedDatetime|recorder|referenceRange|refills|relatedTo|relationship|relevantDatetime|relevantPeriod|result|resultDatetime|requester|role|route|sender|specialty|statusDate|sentDatetime|setting|severity|status|supply|targetOutcome|type|' + 
	        'unit|value|Entity|PatientEntity|Organization|participant|Practitioner|CarePartner|' +

            //new attributes for QDM 5.6
            'class|interpretation'
	    );
	    
    var keywordMapper = this.createKeywordMapper({
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
            regex : "\\w+"
        }, {
            token : "keyword.operator",
            regex : "\\+|\\-|\\/|\\/\\/|\\*|%|<@>|@>|<@|&|\\^|~|!~|<|>|<=|=>|==|!=|<>|="
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
