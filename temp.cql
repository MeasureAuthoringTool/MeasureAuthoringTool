library CWP_HEDIS_2020 version '1.2.000'

using QDM version '5.5'

include NCQA_Common version '5.1.000' called Common

codesystem "LOINC": 'urn:oid:2.16.840.1.113883.6.1'

valueset "Comorbid Conditions": 'urn:oid:2.16.840.1.113883.3.464.1004.1066'
valueset "Competing Diagnosis": 'urn:oid:2.16.840.1.113883.3.464.1004.1067'
valueset "COPD": 'urn:oid:2.16.840.1.113883.3.464.1004.1053'
valueset "CWP Antibiotic Medications": 'urn:oid:2.16.840.1.113883.3.464.1004.1734'
valueset "Disorders of the Immune System": 'urn:oid:2.16.840.1.113883.3.464.1004.1139'
valueset "ED": 'urn:oid:2.16.840.1.113883.3.464.1004.1086'
valueset "Emphysema": 'urn:oid:2.16.840.1.113883.3.464.1004.1091'
valueset "Group A Strep Tests": 'urn:oid:2.16.840.1.113883.3.464.1004.1109'
valueset "HIV": 'urn:oid:2.16.840.1.113883.3.464.1004.1110'
valueset "HIV Type 2": 'urn:oid:2.16.840.1.113883.3.464.1004.1406'
valueset "Inpatient Stay": 'urn:oid:2.16.840.1.113883.3.464.1004.1395'
valueset "Malignant Neoplasms": 'urn:oid:2.16.840.1.113883.3.464.1004.1167'
valueset "Observation": 'urn:oid:2.16.840.1.113883.3.464.1004.1191'
valueset "Online Assessments": 'urn:oid:2.16.840.1.113883.3.464.1004.1446'
valueset "Other Malignant Neoplasm of Skin": 'urn:oid:2.16.840.1.113883.3.464.1004.1463'
valueset "Outpatient": 'urn:oid:2.16.840.1.113883.3.464.1004.1202'
valueset "Pharyngitis": 'urn:oid:2.16.840.1.113883.3.464.1004.1210'
valueset "Telephone Visits": 'urn:oid:2.16.840.1.113883.3.464.1004.1246'

code "Birth date": '21112-8' from "LOINC" display 'Birth date'

parameter "Measurement Period" Interval<DateTime>
parameter "Product Line" String

context Patient

define "Antibiotic Active Overlaps Episode and Starts More than 30 Days Prior to Episode":
  "Episode Date" EpisodeDate
    with ["Medication, Active": "CWP Antibiotic Medications"] ActiveAntibiotic
      such that ActiveAntibiotic.relevantPeriod starts 31 days or more before day of start of EpisodeDate.relevantPeriod
        and ActiveAntibiotic.relevantPeriod overlaps day of EpisodeDate.relevantPeriod

define "Antibiotic Dispensed During 30 Days Prior":
  "Episode Date" EpisodeDate
    with ["Medication, Dispensed": "CWP Antibiotic Medications"] DispensedAntibiotic
      such that DispensedAntibiotic.relevantDatetime during day of Interval[( start of EpisodeDate.relevantPeriod - 30 days ), ( start of EpisodeDate.relevantPeriod - 1 day )]

define "Comorbid Condition History":
  "Episode Date" EpisodeDate
    with ( ["Diagnosis": "HIV"]
      union ["Diagnosis": "HIV Type 2"]
      union ["Diagnosis": "Malignant Neoplasms"]
      union ["Diagnosis": "Other Malignant Neoplasm of Skin"]
      union ["Diagnosis": "Emphysema"]
      union ["Diagnosis": "COPD"]
      union ["Diagnosis": "Disorders of the Immune System"]
      union ["Diagnosis": "Comorbid Conditions"] ) ComorbidCondition
      such that ComorbidCondition.prevalencePeriod starts during day of Interval[( start of EpisodeDate.relevantPeriod - 12 months ), ( start of EpisodeDate.relevantPeriod )]

define "Competing Diagnosis History":
  "Episode Date" EpisodeDate
    with ["Diagnosis": "Competing Diagnosis"] CompetingCondition
      such that start of CompetingCondition.prevalencePeriod during day of Interval[( start of EpisodeDate.relevantPeriod ), ( start of EpisodeDate.relevantPeriod + 3 days )]

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
  ( ( ["Encounter, Performed": "Outpatient"]
    union ["Encounter, Performed": "Observation"]
    union ["Encounter, Performed": "ED"]
    union ["Encounter, Performed": "Telephone Visits"]
    union ["Encounter, Performed": "Online Assessments"] ) Encounter
    where exists ( Encounter.diagnoses Dx
        where Dx.code in "Pharyngitis"
    ) ) PharynEncounter
    where PharynEncounter.relevantPeriod starts during "Intake Period"

define "Encounter with Pharyngitis and Antibiotic":
  "Encounter with Pharyngitis" AllowablePharynEncounter
    with ["Medication, Dispensed": "CWP Antibiotic Medications"] DispensedAntibiotic
      such that DispensedAntibiotic.relevantDatetime during day of Interval[( start of AllowablePharynEncounter.relevantPeriod ), ( start of AllowablePharynEncounter.relevantPeriod + 3 days )]

define "Episode Date":
  // Participation logic is included in the 'Episode Date' definition
  "Encounter with Pharyngitis and Antibiotic" PharynAntibioticEncounter
    with ["Patient Characteristic Birthdate": "Birth date"] BirthDate
      such that Common."CalendarAgeInYearsAt" ( BirthDate.birthDatetime, start of PharynAntibioticEncounter.relevantPeriod ) >= 3
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
    with ["Laboratory Test, Performed": "Group A Strep Tests"] StrepTest
      such that StrepTest.relevantDatetime during day of Interval[( start of IP.relevantPeriod - 3 days ), ( start of IP.relevantPeriod + 3 days )]

define "Outpatient ED or Observation Visit Resulting in Inpatient Stay":
  ( ["Encounter, Performed": "Observation"]
    union ["Encounter, Performed": "ED"]
    union ["Encounter, Performed": "Outpatient"] ) Encounter
    with ["Encounter, Performed": "Inpatient Stay"] InpatientStay
      such that InpatientStay.relevantPeriod starts during day of Interval[start of Encounter.relevantPeriod, start of Encounter.relevantPeriod + 1 day]

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
    with ["Patient Characteristic Birthdate": "Birth date"] BirthDate
      such that Common."CalendarAgeInYearsAt" ( BirthDate.birthDatetime, start of IP.relevantPeriod ) between 3 and 17

define "Stratification 2":
  "Initial Population" IP
    with ["Patient Characteristic Birthdate": "Birth date"] BirthDate
      such that Common."CalendarAgeInYearsAt" ( BirthDate.birthDatetime, start of IP.relevantPeriod ) between 18 and 64

define "Stratification 3":
  "Initial Population" IP
    with ["Patient Characteristic Birthdate": "Birth date"] BirthDate
      such that Common."CalendarAgeInYearsAt" ( BirthDate.birthDatetime, start of IP.relevantPeriod ) >= 65

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