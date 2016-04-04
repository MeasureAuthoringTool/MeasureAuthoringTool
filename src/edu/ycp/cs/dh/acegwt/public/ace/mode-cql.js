define("ace/mode/cql_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextHighlightRules = require("./text_highlight_rules").TextHighlightRules;

var SqlHighlightRules = function() {

	var keywords = (
	        "library|version|using|include|called|public|private|parameter|default|codesystem|valueset|codesystems|define|function|with|such that|without|" +
	        "in|from|where|return|all|distinct|sort|by|asc|desc|is|not|cast|as|between|difference|contains|and|or|xor|union|intersection|year|month|day|hour|minute|" +
	        "second|millisecond|when|then|or before|or after|or more|or less|context|using QDM|Interval<DateTime>|context Patient|context Population"
	    );
	    
	    var matTimingKeywords = (
	    		"after|before|during|ends|includes|included in|meets|overlaps|same|starts|within|start of|such that"
	    );
	    
	    var matFunctionKeywords = (
	    		"Age At|Avg|Count|Datetimediff|Fifth|First|Fourth|Max|Median|Min|Most Recent|Satisfies All|Satisfies Any|Second|Sum|Third"
	    );

	    var builtinConstants = (
	        
	    	//basic constants	
	    	'true|false|null|QDM|Patient|Population|MeasurementPeriod|' + 
	    	
	        //data type constants
	        '"Patient Characteristic Clinical Trial Participant"|"Patient Characteristic Expired"|"Patient Characteristic Birthdate"|' +
	        '"Timing Element"|"Encounter, Active"|"Diagnostic Study, Recommended"|"Substance, Recommended"|"Procedure, Recommended"|"Physical Exam, Recommended"|"Laboratory Test, Recommended"|' +
	        '"Intervention, Recommended"|"Functional Status, Recommended"|"Encounter, Recommended"|"Device, Recommended"|"Transfer To"|"Transfer From"|"Substance, Order"|"Substance, Intolerance"|' +
	        '"Substance, Allergy"|"Substance, Adverse Event"|"Substance, Administered"|"Risk Category Assessment"|"Procedure, Performed"|"Procedure, Order"|"Procedure, Intolerance"|"Procedure, Adverse Event"|' +
	        '"Communication: From Provider to Patient"|"Physical Exam, Performed"|"Physical Exam, Order"|"Communication: From Patient to Provider"|"Medication, Order"|"Medication, Intolerance"|' +
	        '"Medication, Dispensed"|"Medication, Allergy"|"Medication, Adverse Effects"|"Communication: From Provider to Provider"|"Medication, Administered"|"Medication, Active"|' +
	        '"Laboratory Test, Performed"|"Laboratory Test, Order"|"Laboratory Test, Intolerance"|"Laboratory Test, Adverse Event"|"Intervention, Performed"|"Intervention, Order"|' +
	        '"Care Goal"|"Intervention, Intolerance"|"Intervention, Adverse Event"|"Provider Characteristic"|"Patient Characteristic"|"Functional Status, Performed"|"Functional Status, Order"|' +
	        '"Encounter, Performed"|"Encounter, Order"|"Provider Care Experience"|"Diagnostic Study, Performed"|"Diagnostic Study, Order"|"Diagnostic Study, Intolerance"|"Diagnostic Study, Adverse Event"|' +
	        '"Device, Order"|"Device, Intolerance"|"Device, Applied"|"Device, Allergy"|"Diagnosis"|"Immunization, Intolerance"|"Immunization, Allergy"|"Immunization, Order"|"Immunization, Administered"|' +
	        '"Device, Adverse Event"|"Medication, Discharge"|"Patient Characteristic Race"|"Patient Characteristic Ethnicity"|"Patient Characteristic Sex"|"Patient Characteristic Payer"|"Patient Care Experience"|' +
	        
	        //data type attribute constants
	        '"anatomical structure"|"negation rationale"|"number"|"ordinality"|"patient preference"|"provider preference"|"radiation dosage"|"radiationduration"|"reaction"|"reason"|"refills"|' +
	        '"cumulative medication duration"|"result"|"route"|"severity"|"start datetime"|"signed datetime"|"status"|"stop datetime"|"dose"|"Health Record Field"|"admission datetime"|"discharge datetime"|' +
	        '"environment"|"discharge status"|"incision datetime"|"laterality"|"length of stay"|"removal datetime"|"facility location"|"facility location arrival datetime"|"facility location departure datetime"|' +
	        '"radiation duration"|"related to"|"recorder"|"source"|"frequency"|"hospital location"|"method"'
	        
	    );

	    var builtinFunctions = (
	        'date|time|timezone|starts|ends|occurs|overlaps|Interval|Tuple|List|DateTime|AgeInYearsAt'
	    );

    var keywordMapper = this.createKeywordMapper({
        "support.function": builtinFunctions,
        "keyword": keywords,
        "matTimingKeyword" : matTimingKeywords,
        "matFunctionKeyword" : matFunctionKeywords,
        "constant.language": builtinConstants
    }, "identifier", false);

    this.$rules = {
        "start" : [ {
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

    this.lineCommentStart = "--";

    this.$id = "ace/mode/cql";
}).call(Mode.prototype);

exports.Mode = Mode;

});
