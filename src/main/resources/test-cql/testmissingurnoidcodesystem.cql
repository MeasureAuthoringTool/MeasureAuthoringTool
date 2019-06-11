library testmissingurnoidcodesystem version '0.0.000'

using QDM version '5.5'

codesystem "ActCode": '2.16.840.1.113883.5.4'
code "accredited behavioral health care": 'ABHC' from "ActCode" display 'accredited behavioral health care'

parameter "Measurement Period" Interval<DateTime>

context Patient

define "SDE Ethnicity":
  true