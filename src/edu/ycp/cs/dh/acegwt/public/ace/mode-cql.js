define("ace/mode/cql_highlight_rules",["require","exports","module","ace/lib/oop","ace/mode/text_highlight_rules"], function(require, exports, module) {
"use strict";

var oop = require("../lib/oop");
var TextHighlightRules = require("./text_highlight_rules").TextHighlightRules;

var SqlHighlightRules = function() {

    var keywords = (
        "library|version|using|include|called|public|private|parameter|default|codesystem|valueset|codesystems|define|function|with|such that|without|" +
        "in|from|where|return|all|distinct|sort|by|asc|desc|is|not|cast|as|between|difference|contains|and|or|xor|union|intersection|year|month|day|hour|minute|" +
        "second|millisecond|when|then|or before|or after|or more|or less|context"
    );
    
    var matTimingKeywords = (
    		"Concurrent With|During|Ends After End Of|Ends After Or Concurrent With End Of|Ends After Or Concurrent With Start Of|Ends After Start Of|"+
		    "Ends Before End Of|Ends Before Or Concurrent With End Of|Ends Before Or Concurrent With Start Of|"+
		    "Ends Before Start Of|Ends Concurrent With|Ends Concurrent With Start Of|Ends During|Fulfills|Overlaps|Starts After End Of|"+
		    "Starts After Or Concurrent With End Of|Starts After Or Concurrent With Start Of|Starts After Start Of|Starts Before End Of|"+
		    "Starts Before Or Concurrent With End Of|Starts Before Or Concurrent With Start Of|Starts Before Start Of|Starts Concurrent With|"+
		    "Starts Concurrent With End Of|Starts During"
    );
    
    var matFunctionKeywords = (
    		"Age At|Avg|Count|Datetimediff|Fifth|First|Fourth|Max|Median|Min|Most Recent|Satisfies All|Satisfies Any|Second|Sum|Third"
    );

    var builtinConstants = (
        "true|false|null|QDM|Patient|Population|MeasurementPeriod"
    );

    var builtinFunctions = (
        "date|time|timezone|starts|ends|occurs|overlaps|Interval|Tuple|List"
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
