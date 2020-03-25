library HospitalHarmOpioidRelatedAdverseEvents version '1.0.000'

using QDM version '5.5'

include MATGlobalCommonFunctions version '4.1.000' called Global

codesystem "LOINC": 'urn:oid:2.16.840.1.113883.6.1'
codesystem "SNOMEDCT": 'urn:oid:2.16.840.1.113883.6.96'
codesystem "HSLOC": 'urn:oid:2.16.840.1.113883.6.259'

valueset "Emergency Department Visit": 'urn:oid:2.16.840.1.113883.3.117.1.7.1.292'
valueset "Encounter Inpatient": 'urn:oid:2.16.840.1.113883.3.666.5.307'
valueset "Ethnicity": 'urn:oid:2.16.840.1.114222.4.11.837'
valueset "Narcotic Antagonist": 'urn:oid:2.16.840.1.113762.1.4.1179.1'
valueset "Observation Services": 'urn:oid:2.16.840.1.113762.1.4.1111.143'
valueset "ONC Administrative Sex": 'urn:oid:2.16.840.1.113762.1.4.1'
valueset "Opioids, All": 'urn:oid:2.16.840.1.113762.1.4.1196.226'
valueset "Payer": 'urn:oid:2.16.840.1.114222.4.11.3591'
valueset "Race": 'urn:oid:2.16.840.1.114222.4.11.836'

code "Birthdate": '21112-8' from "LOINC" display 'Birthdate'
code "Dead": '419099009' from "SNOMEDCT" display 'Dead'
code "Operating Room/Suite": '1096-7' from "HSLOC" display 'Operating Room/Suite'

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

define "Denominator":
  "Initial Population"

define "Numerator":
  "Denominator"
    intersect "Naloxone Administered During Encounter Outside of Operating Room"

define "Qualifying Inpatient Encounters":
  ( ["Encounter, Performed": "Encounter Inpatient"] ) InpatientEncounter
    where InpatientEncounter.relevantPeriod during "Measurement Period"

define "Qualifying Encounters":
  "Qualifying Inpatient Encounters" QualifyingEncounter
    where AgeInYearsAt(start of Global."HospitalizationWithObservation"(QualifyingEncounter))>= 18

define "Initial Population":
  "Qualifying Encounters"
    intersect "Opioid Administered During Hospitalization"

define "Naloxone Administered During Encounter Outside of Operating Room":
  from
    ["Medication, Administered": "Narcotic Antagonist"] NaloxoneAdministration,
    "Opioid Administered" OpioidAdministration,
    "Denominator" EncounterWithQualifyingAge	// Naloxone not administered in operating room
    where not exists ( EncounterWithQualifyingAge.facilityLocations EncounterLocation
        where EncounterLocation.code = "Operating Room/Suite"
          and NaloxoneAdministration.relevantPeriod overlaps EncounterLocation.locationPeriod
    )	// AND, naloxone administered after start of encounter and opioids administered before naloxone
      and ( NaloxoneAdministration.relevantPeriod starts during Global."HospitalizationWithObservation" ( EncounterWithQualifyingAge )
          and OpioidAdministration.relevantPeriod during Global."HospitalizationWithObservation" ( EncounterWithQualifyingAge )
          and OpioidAdministration.relevantPeriod ends 24 hours or less before start of NaloxoneAdministration.relevantPeriod
      )
    return EncounterWithQualifyingAge

define "Opioid Administered":
  ["Medication, Administered": "Opioids, All"]

define "Opioid Administered During Hospitalization":
  "Qualifying Encounters" QualifyingEncounter
    with ["Medication, Administered": "Opioids, All"] Opioid
      such that Opioid.relevantPeriod during Global."HospitalizationWithObservation" ( QualifyingEncounter )

define function "Hospitalization, Potentially Starting in Emergency Department and or with Observation"(Inpatient_Encounter "Encounter, Performed" ):
  Inpatient_Encounter QualifyingInpatientEncounter
    let LastObservationVisit: Last(["Encounter, Performed": "Observation Services"] ObservationVisit
        where ObservationVisit.relevantPeriod ends 1 hour or less on or before start of QualifyingInpatientEncounter.relevantPeriod
        sort by
        end of relevantPeriod
    ),
    HospitalVisitStart: Coalesce(start of LastObservationVisit.relevantPeriod, start of QualifyingInpatientEncounter.relevantPeriod),
    LastEDVisit: Last(["Encounter, Performed": "Emergency Department Visit"] EDVisit
        where EDVisit.relevantPeriod ends 1 hour or less on or before HospitalVisitStart
        sort by
        end of relevantPeriod
    )
    return Interval[Coalesce(start of LastEDVisit.relevantPeriod, HospitalVisitStart),
    end of QualifyingInpatientEncounter.relevantPeriod]

