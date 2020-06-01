library testparameters version '0.0.000'

using FHIR version '4.0.1'

/*measurement period comment*/
parameter "Measurement Period" Interval<DateTime> /* measurement period logic comment */

/*other parameter comment*/
parameter "Other Parameter" Interval<DateTime> /* other parameter logic comment */

context Patient

define "test":
  true