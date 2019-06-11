library testwrongcommentsbetweenvaluesets version '0.0.000'

using QDM version '5.4'
/* a comment should not go here */


valueset "Ethnicity": 'urn:oid:2.16.840.1.114222.4.11.837' 
valueset "ONC Administrative Sex": 'urn:oid:2.16.840.1.113762.1.4.1' 
/* a comment should not go here */


valueset "Payer": 'urn:oid:2.16.840.1.114222.4.11.3591' 
valueset "Race": 'urn:oid:2.16.840.1.114222.4.11.836' 

parameter "Measurement Period" Interval<DateTime>

context Patient

define "SDE Ethnicity":
  ["Patient Characteristic Ethnicity": "Ethnicity"]

define "SDE Payer":
  ["Patient Characteristic Payer": "Payer"]

define "SDE Race":
  ["Patient Characteristic Race": "Race"]

define "SDE Sex":
  ["Patient Characteristic Sex": "ONC Administrative Sex"]

