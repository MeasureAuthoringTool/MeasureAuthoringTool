library testfunctions version '0.0.000'

using FHIR version '4.0.1'

context Patient

/* testfunction comment */
define function "testfunction"(arg1 Boolean, arg2 Interval<DateTime>, arg3 "Encounter, Performed"):
  true

/* testpopulationfunction comment */
define function "testpopulationfunction"():
  true