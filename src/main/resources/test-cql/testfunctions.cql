library testfunctions version '0.0.000'

using QDM version '5.4'

context Patient

/* test comment */
define function "test"(arg1 Boolean, arg2 Interval<DateTime>, arg3 "Encounter, Performed"):
  true

context Population

/* testpouplationfunction comment */
define function "testpopulationfunction"():
  true