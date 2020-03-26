library CWP_HEDIS_2020_CARSON_FHIR4 version '1.0.000' 

using FHIR version '4.0.0'
include FHIRHelpers version '4.0.0' called FHIRHelpers
include SupplementalDataElements_FHIR4 version '1.0.0' called SDE
include MATGlobalCommonFunctions_FHIR4 version '4.0.000' called Global

include NCQA_Common_FHIR4 version '5.1.000' called Common

codesystem "LOINC": 'urn:oid:2.16.840.1.113883.6.1' 
codesystem "SOP": 'urn:oid:2.16.840.1.113883.3.221.5' 

valueset "Commercial": 'urn:oid:2.16.840.1.113883.3.464.1004.1518' 
valueset "Comorbid Conditions": 'urn:oid:2.16.840.1.113883.3.464.1004.1066' 
valueset "Competing Diagnosis": 'urn:oid:2.16.840.1.113883.3.464.1004.1067' 
valueset "COPD": 'urn:oid:2.16.840.1.113883.3.464.1004.1053' 
valueset "CWP Antibiotic Medications": 'urn:oid:2.16.840.1.113883.3.464.1004.1734' 
valueset "Cystic Fibrosis": 'urn:oid:2.16.840.1.113883.3.464.1004.1068' 
valueset "Disorders of the Immune System": 'urn:oid:2.16.840.1.113883.3.464.1004.1139' 
valueset "ED": 'urn:oid:2.16.840.1.113883.3.464.1004.1086' 
valueset "Emphysema": 'urn:oid:2.16.840.1.113883.3.464.1004.1091' 
valueset "Ethnicity": 'urn:oid:2.16.840.1.114222.4.11.837' 
valueset "Group A Strep Tests": 'urn:oid:2.16.840.1.113883.3.464.1004.1109' 
valueset "HIV": 'urn:oid:2.16.840.1.113883.3.464.1004.1110' 
valueset "HIV Type 2": 'urn:oid:2.16.840.1.113883.3.464.1004.1406' 
valueset "Hospice Encounter": 'urn:oid:2.16.840.1.113883.3.464.1004.1761' 
valueset "Hospice Intervention": 'urn:oid:2.16.840.1.113883.3.464.1004.1762' 
valueset "Inpatient Stay": 'urn:oid:2.16.840.1.113883.3.464.1004.1395' 
valueset "Malignant Neoplasms": 'urn:oid:2.16.840.1.113883.3.464.1004.1167' 
valueset "Medicaid": 'urn:oid:2.16.840.1.113883.3.464.1004.1517' 
valueset "Medicare": 'urn:oid:2.16.840.1.113883.3.464.1004.1516' 
valueset "Observation": 'urn:oid:2.16.840.1.113883.3.464.1004.1191' 
valueset "ONC Administrative Sex": 'urn:oid:2.16.840.1.113762.1.4.1' 
valueset "Online Assessments": 'urn:oid:2.16.840.1.113883.3.464.1004.1446' 
valueset "Other Malignant Neoplasm of Skin": 'urn:oid:2.16.840.1.113883.3.464.1004.1463' 
valueset "Outpatient": 'urn:oid:2.16.840.1.113883.3.464.1004.1202' 
valueset "Payer": 'urn:oid:2.16.840.1.114222.4.11.3591' 
valueset "Pharyngitis": 'urn:oid:2.16.840.1.113883.3.464.1004.1210' 
valueset "Race": 'urn:oid:2.16.840.1.114222.4.11.836' 
valueset "Telephone Visits": 'urn:oid:2.16.840.1.113883.3.464.1004.1246' 

code "Birth date": '21112-8' from "LOINC" display 'Birth date'
code "MEDICAID": '2' from "SOP" display 'MEDICAID'
code "MEDICARE": '1' from "SOP" display 'MEDICARE'
code "PRIVATE HEALTH INSURANCE": '5' from "SOP" display 'PRIVATE HEALTH INSURANCE'

parameter "Measurement Period" Interval<DateTime>
parameter "Product Line" String

context Patient

define "Antibiotic Active Overlaps Episode and Starts More than 30 Days Prior to Episode":
  "Episode Date" EpisodeDate
    with ["MedicationStatement": "CWP Antibiotic Medications"] ActiveAntibiotic
      such that ActiveAntibiotic.effectiveDateTime starts 31 days or more before day of start of EpisodeDate.relevantPeriod
        and ActiveAntibiotic.effectiveDateTime overlaps day of EpisodeDate.relevantPeriod

define "Antibiotic Dispensed During 30 Days Prior":
  "Episode Date" EpisodeDate
    with ["MedicationDispense": "CWP Antibiotic Medications"] DispensedAntibiotic
      such that DispensedAntibiotic.whenHandedOver during day of Interval[( start of EpisodeDate.relevantPeriod - 30 days ), ( start of EpisodeDate.relevantPeriod - 1 day )]

define "Comorbid Condition History":
  "Episode Date" EpisodeDate
    with ( ["Condition": "HIV"]
      union ["Condition": "HIV Type 2"]
      union ["Condition": "Malignant Neoplasms"]
      union ["Condition": "Other Malignant Neoplasm of Skin"]
      union ["Condition": "Emphysema"]
      union ["Condition": "COPD"]
      union ["Condition": "Disorders of the Immune System"]
      union ["Condition": "Comorbid Conditions"] ) ComorbidCondition
      such that ComorbidCondition.onsetDateTime starts during day of Interval[( start of EpisodeDate.relevantPeriod - 12 months ), ( start of EpisodeDate.relevantPeriod )]

define "Competing Diagnosis History":
  "Episode Date" EpisodeDate
    with ["Condition": "Competing Diagnosis"] CompetingCondition
      such that start of CompetingCondition.onsetDateTime during day of Interval[( start of EpisodeDate.relevantPeriod ), ( start of EpisodeDate.relevantPeriod + 3 days )]

define "Deduplicated Episode":
  ( { "First Episode", "Second Episode", "Third Episode", "Fourth Episode", "Fifth Episode", "Sixth Episode", "Seventh Episode", "Eighth Episode", "Ninth Episode", "Tenth Episode", "Eleventh Episode", "Twelfth Episode" } ) Episode
    where Episode is not null

define "Denominator":
  "Initial Population"

define "Eighth Episode":
  First("Episode Without Exclusions" WithoutExclusions
      where start of WithoutExclusions.relevantPeriod 31 days or more on or after day of start of "Seventh Episode".relevantPeriod
      sort by start of relevantPeriod
  )

define "Eleventh Episode":
  First("Episode Without Exclusions" WithoutExclusions
      where start of WithoutExclusions.relevantPeriod 31 days or more on or after day of start of "Tenth Episode".relevantPeriod
      sort by start of relevantPeriod
  )

define "Encounter with Pharyngitis":
  ( ( ["Encounter": "Outpatient"]
    union ["Encounter": "Observation"]
    union ["Encounter": "ED"]
    union ["Encounter": "Telephone Visits"]
    union ["Encounter": "Online Assessments"] ) Encounter
    where exists ( Encounter.Unknown Dx
        where Dx.code in "Pharyngitis"
    ) ) PharynEncounter
    where PharynEncounter.period starts during "Intake Period"

define "Encounter with Pharyngitis and Antibiotic":
  "Encounter with Pharyngitis" AllowablePharynEncounter
    with ["MedicationDispense": "CWP Antibiotic Medications"] DispensedAntibiotic
      such that DispensedAntibiotic.whenHandedOver during day of Interval[( start of AllowablePharynEncounter.relevantPeriod ), ( start of AllowablePharynEncounter.relevantPeriod + 3 days )]

define "Episode Date":
  // Participation logic is included in the 'Episode Date' definition
  "Encounter with Pharyngitis and Antibiotic" PharynAntibioticEncounter
    with ["Patient": "Birth date"] BirthDate
      such that Common."CalendarAgeInYearsAt" ( BirthDate.birthDate, start of PharynAntibioticEncounter.relevantPeriod ) >= 3
    where ( Common."Is Enrolled" ( "Product Line", 
      end of PharynAntibioticEncounter.relevantPeriod, Interval[start of PharynAntibioticEncounter.relevantPeriod - 30 days, 
      end of PharynAntibioticEncounter.relevantPeriod + 3 days], 0 )
    )

define "Episode Date with Hospice Intervention or Encounter":
  "Initial Population" IP
    where exists Common."Hospice Intervention or Encounter"

define "Episode Without Exclusions":
  "Episode Date"
    except ( "Outpatient ED or Observation Visit Resulting in Inpatient Stay"
        union "Comorbid Condition History"
        union "Medication History"
        union "Competing Diagnosis History"
    )

define "Exclusions":
  "Episode Date with Hospice Intervention or Encounter"

define "Fifth Episode":
  First("Episode Without Exclusions" WithoutExclusions
      where start of WithoutExclusions.relevantPeriod 31 days or more on or after day of start of "Fourth Episode".relevantPeriod
      sort by start of relevantPeriod
  )

define "First Episode":
  First("Episode Without Exclusions" WithoutExclusions
      sort by start of relevantPeriod
  )

define "Fourth Episode":
  First("Episode Without Exclusions" WithoutExclusions
      where start of WithoutExclusions.relevantPeriod 31 days or more on or after day of start of "Third Episode".relevantPeriod
      sort by start of relevantPeriod
  )

define "Initial Population":
  "Deduplicated Episode"

define "Intake Period":
  Interval[( start of "Measurement Period" - 6 months ), ( start of "Measurement Period" + 6 months ) )

define "Medication History":
  "Antibiotic Dispensed During 30 Days Prior"
    union "Antibiotic Active Overlaps Episode and Starts More than 30 Days Prior to Episode"

define "Ninth Episode":
  First("Episode Without Exclusions" WithoutExclusions
      where start of WithoutExclusions.relevantPeriod 31 days or more on or after day of start of "Eighth Episode".relevantPeriod
      sort by start of relevantPeriod
  )

define "Numerator":
  "Initial Population" IP
    with ["Observation": "Group A Strep Tests"] StrepTest
      such that StrepTest.effectiveDateTime during day of Interval[( start of IP.relevantPeriod - 3 days ), ( start of IP.relevantPeriod + 3 days )]

define "Outpatient ED or Observation Visit Resulting in Inpatient Stay":
  ( ["Encounter": "Observation"]
    union ["Encounter": "ED"]
    union ["Encounter": "Outpatient"] ) Encounter
    with ["Encounter": "Inpatient Stay"] InpatientStay
      such that InpatientStay.period starts during day of Interval[start of Encounter.period, start of Encounter.period + 1 day]

define "SDE Ethnicity":
  SDE."SDE Ethnicity"

define "SDE Payer":
  SDE."SDE Payer"

define "SDE Race":
  SDE."SDE Race"

define "SDE Sex":
  SDE."SDE Sex"

define "Second Episode":
  First("Episode Without Exclusions" WithoutExclusions
      where start of WithoutExclusions.relevantPeriod 31 days or more on or after day of start of "First Episode".relevantPeriod
      sort by start of relevantPeriod
  )

define "Seventh Episode":
  First("Episode Without Exclusions" WithoutExclusions
      where start of WithoutExclusions.relevantPeriod 31 days or more on or after day of start of "Sixth Episode".relevantPeriod
      sort by start of relevantPeriod
  )

define "Sixth Episode":
  First("Episode Without Exclusions" WithoutExclusions
      where start of WithoutExclusions.relevantPeriod 31 days or more on or after day of start of "Fifth Episode".relevantPeriod
      sort by start of relevantPeriod
  )

define "Stratification 1":
  "Initial Population" IP
    with ["Patient": "Birth date"] BirthDate
      such that Common."CalendarAgeInYearsAt" ( BirthDate.birthDate, start of IP.relevantPeriod ) between 3 and 17

define "Stratification 2":
  "Initial Population" IP
    with ["Patient": "Birth date"] BirthDate
      such that Common."CalendarAgeInYearsAt" ( BirthDate.birthDate, start of IP.relevantPeriod ) between 18 and 64

define "Stratification 3":
  "Initial Population" IP
    with ["Patient": "Birth date"] BirthDate
      such that Common."CalendarAgeInYearsAt" ( BirthDate.birthDate, start of IP.relevantPeriod ) >= 65

define "Tenth Episode":
  First("Episode Without Exclusions" WithoutExclusions
      where start of WithoutExclusions.relevantPeriod 31 days or more on or after day of start of "Ninth Episode".relevantPeriod
      sort by start of relevantPeriod
  )

define "Third Episode":
  First("Episode Without Exclusions" WithoutExclusions
      where start of WithoutExclusions.relevantPeriod 31 days or more on or after day of start of "Second Episode".relevantPeriod
      sort by start of relevantPeriod
  )

define "Twelfth Episode":
  First("Episode Without Exclusions" WithoutExclusions
      where start of WithoutExclusions.relevantPeriod 31 days or more on or after day of start of "Eleventh Episode".relevantPeriod
      sort by start of relevantPeriod
  )
