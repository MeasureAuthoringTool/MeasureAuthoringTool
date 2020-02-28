library testmissingurnoidvalueset version '0.0.000'

using QDM version '5.4'

valueset "ONC Administrative Sex": '2.16.840.1.113762.1.4.1' 

parameter "Measurement Period" Interval<DateTime>

context Patient

define "SDE Ethnicity":
  true
