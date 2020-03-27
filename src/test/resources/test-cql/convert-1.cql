library UseofOpioidsfromMultipleProviders_FHIR4 version '1.0.000' 

using FHIR version '4.0.0'
include FHIRHelpers version '4.0.0' called FHIRHelpers
include SupplementalDataElements_FHIR4 version '1.0.0' called SDE
include MATGlobalCommonFunctions_FHIR4 version '4.0.000' called Global

include NCQA_Common_FHIR4 version '5.1.000' called Common

codesystem "LOINC": 'urn:oid:2.16.840.1.113883.6.1' 
codesystem "SNOMEDCT": 'urn:oid:2.16.840.1.113883.6.96' 

valueset "Acetaminophen Butalbital Caffeine Codeine": 'urn:oid:2.16.840.1.113883.3.464.1004.1548' 
valueset "Acetaminophen Caffeine Dihydrocodeine": 'urn:oid:2.16.840.1.113883.3.464.1004.1561' 
valueset "Acetaminophen Codeine": 'urn:oid:2.16.840.1.113883.3.464.1004.1547' 
valueset "Acetaminophen Hydrocodone": 'urn:oid:2.16.840.1.113883.3.464.1004.1550' 
valueset "Acetaminophen Oxycodone": 'urn:oid:2.16.840.1.113883.3.464.1004.1552' 
valueset "Acetaminophen Tramadol": 'urn:oid:2.16.840.1.113883.3.464.1004.1562' 
valueset "Aspirin Butalbital Caffeine Codeine": 'urn:oid:2.16.840.1.113883.3.464.1004.1549' 
valueset "Aspirin Caffeine Dihydrocodeine": 'urn:oid:2.16.840.1.113883.3.464.1004.1551' 
valueset "Aspirin Carisoprodol Codeine": 'urn:oid:2.16.840.1.113883.3.464.1004.1556' 
valueset "Aspirin Oxycodone": 'urn:oid:2.16.840.1.113883.3.464.1004.1553' 
valueset "Belladonna Opium": 'urn:oid:2.16.840.1.113883.3.464.1004.1555' 
valueset "Buprenorphine": 'urn:oid:2.16.840.1.113883.3.464.1004.1545' 
valueset "Butorphanol": 'urn:oid:2.16.840.1.113883.3.464.1004.1544' 
valueset "Codeine Sulfate": 'urn:oid:2.16.840.1.113883.3.464.1004.1534' 
valueset "Ethnicity": 'urn:oid:2.16.840.1.114222.4.11.837' 
valueset "Fentanyl": 'urn:oid:2.16.840.1.113883.3.464.1004.1537' 
valueset "Hospice Encounter": 'urn:oid:2.16.840.1.113883.3.464.1004.1761' 
valueset "Hospice Intervention": 'urn:oid:2.16.840.1.113883.3.464.1004.1762' 
valueset "Hydrocodone": 'urn:oid:2.16.840.1.113883.3.464.1004.1546' 
valueset "Hydrocodone Ibuprofen": 'urn:oid:2.16.840.1.113883.3.464.1004.1560' 
valueset "Hydromorphone": 'urn:oid:2.16.840.1.113883.3.464.1004.1538' 
valueset "Ibuprofen Oxycodone": 'urn:oid:2.16.840.1.113883.3.464.1004.1564' 
valueset "Levorphanol": 'urn:oid:2.16.840.1.113883.3.464.1004.1542' 
valueset "Meperidine": 'urn:oid:2.16.840.1.113883.3.464.1004.1535' 
valueset "Meperidine Promethazine": 'urn:oid:2.16.840.1.113883.3.464.1004.1554' 
valueset "Methadone": 'urn:oid:2.16.840.1.113883.3.464.1004.1536' 
valueset "Morphine": 'urn:oid:2.16.840.1.113883.3.464.1004.1539' 
valueset "Morphine Naltrexone": 'urn:oid:2.16.840.1.113883.3.464.1004.1566' 
valueset "Naloxone Pentazocine": 'urn:oid:2.16.840.1.113883.3.464.1004.1557' 
valueset "ONC Administrative Sex": 'urn:oid:2.16.840.1.113762.1.4.1' 
valueset "Opium": 'urn:oid:2.16.840.1.113883.3.464.1004.1541' 
valueset "Oxycodone": 'urn:oid:2.16.840.1.113883.3.464.1004.1540' 
valueset "Oxymorphone": 'urn:oid:2.16.840.1.113883.3.464.1004.1543' 
valueset "Payer": 'urn:oid:2.16.840.1.114222.4.11.3591' 
valueset "Race": 'urn:oid:2.16.840.1.114222.4.11.836' 
valueset "Tapentadol": 'urn:oid:2.16.840.1.113883.3.464.1004.1565' 
valueset "Tramadol": 'urn:oid:2.16.840.1.113883.3.464.1004.1559' 

code "Birth date": '21112-8' from "LOINC" display 'Birth date'
code "Dead": '419099009' from "SNOMEDCT" display 'Dead'

parameter "Measurement Period" Interval<DateTime>
parameter "Product Line" String

context Patient

define "Denominator":
  "Initial Population"

define "Exclusions":
  exists Common."Hospice Intervention or Encounter"

define "Medication Dispensed from 4 Different Pharmacies during Measurement Period":
  ( ( Count("Opioid Medication Dispensed" MedDispensed
          return MedDispensed.dispenser.id
      )+ Count("Opioid Medication Dispensed" MedDispensed
          where MedDispensed.dispenser.id is null
      )
    ) >= 4
  )

define "Medication Dispensed from 4 Different Prescribers and 4 Different Pharmacies during Measurement Period":
  ( ( Count("Opioid Medication Dispensed" MedDispensed
          return MedDispensed.prescriber.id
      )+ Count("Opioid Medication Dispensed" MedDispensed
          where MedDispensed.prescriber.id is null
      )
    ) >= 4
      and ( Count("Opioid Medication Dispensed" MedDispensed
            return MedDispensed.dispenser.id
        )+ Count("Opioid Medication Dispensed" MedDispensed
            where MedDispensed.dispenser.id is null
        )
      ) >= 4
  )

define "Medication Dispensed from 4 Different Prescribers during Measurement Period":
  ( ( Count("Opioid Medication Dispensed" MedDispensed
          return MedDispensed.prescriber.id
      )+ Count("Opioid Medication Dispensed" MedDispensed
          where MedDispensed.prescriber.id is null
      )
    ) >= 4
  )

define "Numerator for Population Criteria 1":
  "Medication Dispensed from 4 Different Prescribers during Measurement Period"

define "Numerator for Population Criteria 2":
  "Medication Dispensed from 4 Different Pharmacies during Measurement Period"

define "Numerator for Population Criteria 3":
  "Medication Dispensed from 4 Different Prescribers and 4 Different Pharmacies during Measurement Period"

define "Opioid Medication Coverage Days":
  Sum((collapse "Opioid Medication Coverage Intervals")CoverageInterval
      return all(duration in days of CoverageInterval + 1)
  )

define "Opioid Medication Coverage Intervals":
  ( flatten { RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Acetaminophen Butalbital Caffeine Codeine"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Acetaminophen Caffeine Dihydrocodeine"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Acetaminophen Codeine"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Acetaminophen Hydrocodone"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Acetaminophen Oxycodone"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Acetaminophen Tramadol"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Aspirin Butalbital Caffeine Codeine"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Aspirin Caffeine Dihydrocodeine"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Meperidine Promethazine"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Aspirin Carisoprodol Codeine"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Aspirin Oxycodone"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Belladonna Opium"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Buprenorphine"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Butorphanol"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Codeine Sulfate"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Fentanyl"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Hydrocodone"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Hydrocodone Ibuprofen"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Hydromorphone"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Ibuprofen Oxycodone"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Levorphanol"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Meperidine"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Methadone"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Morphine"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Morphine Naltrexone"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Naloxone Pentazocine"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Opium"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Oxycodone"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Oxymorphone"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Tapentadol"
      return all OpioidMedInterval.period
  ), RolloutIntervals("Opioid Medication Intervals" OpioidMedInterval
      where OpioidMedInterval.code in "Tramadol"
      return all OpioidMedInterval.period
  )} ) OpioidIntervals
    return all OpioidIntervals
      intersect "Measurement Period"

define "Opioid Medication Dispensed":
  ( ( ["MedicationDispense": "Codeine Sulfate"]
      union ["MedicationDispense": "Meperidine"]
      union ["MedicationDispense": "Methadone"]
      union ["MedicationDispense": "Fentanyl"]
      union ["MedicationDispense": "Hydrocodone"]
      union ["MedicationDispense": "Morphine"]
      union ["MedicationDispense": "Oxycodone"]
      union ["MedicationDispense": "Opium"]
      union ["MedicationDispense": "Levorphanol"]
      union ["MedicationDispense": "Oxymorphone"]
      union ["MedicationDispense": "Butorphanol"]
      union ["MedicationDispense": "Buprenorphine"]
      union ["MedicationDispense": "Hydromorphone"]
      union ["MedicationDispense": "Acetaminophen Codeine"]
      union ["MedicationDispense": "Acetaminophen Butalbital Caffeine Codeine"]
      union ["MedicationDispense": "Aspirin Butalbital Caffeine Codeine"]
      union ["MedicationDispense": "Acetaminophen Hydrocodone"]
      union ["MedicationDispense": "Aspirin Caffeine Dihydrocodeine"]
      union ["MedicationDispense": "Acetaminophen Oxycodone"]
      union ["MedicationDispense": "Aspirin Oxycodone"]
      union ["MedicationDispense": "Meperidine Promethazine"]
      union ["MedicationDispense": "Belladonna Opium"]
      union ["MedicationDispense": "Aspirin Carisoprodol Codeine"]
      union ["MedicationDispense": "Naloxone Pentazocine"]
      union ["MedicationDispense": "Tramadol"]
      union ["MedicationDispense": "Hydrocodone Ibuprofen"]
      union ["MedicationDispense": "Acetaminophen Caffeine Dihydrocodeine"]
      union ["MedicationDispense": "Acetaminophen Tramadol"]
      union ["MedicationDispense": "Ibuprofen Oxycodone"]
      union ["MedicationDispense": "Tapentadol"]
      union ["MedicationDispense": "Morphine Naltrexone"] ) Medlist
      where Medlist.extension:validityPeriod overlaps "Measurement Period"
        or Interval[Medlist.whenHandedOver, Medlist.whenHandedOver + ToDays(Medlist.Unknown "Measurement Period"
  )

define "Opioid Medication Intervals":
  "Opioid Medication Dispensed" OpioidMedDispensed
    let period: if OpioidMedDispensed.daysSupplied is not null then Interval[OpioidMedDispensed.relevantDatetime, OpioidMedDispensed.relevantDatetime + ToDays(OpioidMedDispensed.daysSupplied)]
      else OpioidMedDispensed.relevantPeriod
    where period overlaps "Measurement Period"
    return all {
      code: OpioidMedDispensed.code,
      period: period
    }

define "SDE Ethnicity":
  SDE."SDE Ethnicity"

define "SDE Payer":
  SDE."SDE Payer"

define "SDE Race":
  SDE."SDE Race"

define "SDE Sex":
  SDE."SDE Sex"

define "Two Opioid Medications Dispensed on Different Dates of Service":
  ( Count("Opioid Medication Dispensed" MedDispensedOne
        with "Opioid Medication Dispensed" MedDispensedTwo
          such that MedDispensedOne.relevantDatetime 1 day or more after MedDispensedTwo.relevantDatetime
            or MedDispensedTwo.relevantDatetime 1 day or more after MedDispensedOne.relevantDatetime
            or MedDispensedOne.relevantPeriod starts 1 day or more after start of MedDispensedTwo.relevantPeriod
            or MedDispensedTwo.relevantPeriod starts 1 day or more after start of MedDispensedOne.relevantPeriod
    )
  ) >= 2

define "Initial Population":
  ( exists ( ["Patient": "Birth date"] BirthDate
        where Common."CalendarAgeInYearsAt" ( BirthDate.birthDate, start of "Measurement Period" ) >= 18
    )
      and "Two Opioid Medications Dispensed on Different Dates of Service"
      and "Opioid Medication Coverage Days" >= 15
      and "Enrolled During Participation Period"
  )

define "Enrolled During Participation Period":
  Common."Is Enrolled" ( "Product Line", 
  end of "Measurement Period", "Measurement Period", 45 )

define function "ToDays"(Value Integer ):
  if Value is not null then Quantity { value: Value, unit: 'd' } 
    else null

define function "test1"(Test_1 "Test \" 1" ):
  RolloutIntervalsOnce(RolloutIntervalsOnce(RolloutIntervalsOnce(RolloutIntervalsOnce(Intervals))))

define function "test2"(Test_2 "Test , 2" ):
  RolloutIntervalsOnce(RolloutIntervalsOnce(RolloutIntervalsOnce(RolloutIntervalsOnce(Intervals))))

define function "test3"(Test_3 "Test ,\" 3" , Value Integer ):
  RolloutIntervalsOnce(RolloutIntervalsOnce(RolloutIntervalsOnce(RolloutIntervalsOnce(Intervals))))

define function "RolloutIntervals"(Intervals List<Interval<DateTime>> ):
  RolloutIntervalsOnce(RolloutIntervalsOnce(RolloutIntervalsOnce(RolloutIntervalsOnce(Intervals))))

define function "RolloutIntervalsOnce"(Intervals List<Interval<DateTime>> ):
  ( collapse Intervals ) RollOutInterval
    return Interval[start of RollOutInterval, start of RollOutInterval + Quantity { value: Sum(Intervals X
        where X during RollOutInterval
        return all duration in days of X
    ), unit: 'day' }]
