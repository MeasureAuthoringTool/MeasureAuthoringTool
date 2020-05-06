package mat.cql;

import lombok.extern.slf4j.Slf4j;
import mat.dto.VSACCodeSystemDTO;
import mat.model.cql.CQLModel;
import mat.server.MappingSpreadsheetService;
import mat.server.service.CodeListService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class TestCqlToMatXml {
    private static final String CQL_TEST_RESOURCES_DIR = "/test-cql/";

    @Mock
    MappingSpreadsheetService mappingService;

    @Mock
    private CodeListService codeListService;

    @InjectMocks
    private CqlParser parser;

    @InjectMocks
    private CqlToMatXml cqlToMatXml;

    public void mockSpreadsheet() {
        Map<String, VSACCodeSystemDTO> map = new HashMap<>();
        map.put("urn:oid:2.16.840.1.113883.6.96",new VSACCodeSystemDTO("urn:oid:2.16.840.1.113883.6.96","http://snomed.info/sct/731000124108","SNOMEDCT","2019-03"));
        map.put("urn:oid:2.16.840.1.113883.5.1001",new VSACCodeSystemDTO("urn:oid:2.16.840.1.113883.5.1001","http://terminology.hl7.org/CodeSystem/v3-ActMood","ActMood","TBD"));
        map.put("urn:oid:2.16.840.1.113883.5.1",new VSACCodeSystemDTO("urn:oid:2.16.840.1.113883.5.1","http://hl7.org/fhir/ValueSet/v3-AdministrativeGender","SNOMEDCT","2019-03"));
        map.put("urn:oid:2.16.840.1.113883.6.88",new VSACCodeSystemDTO("urn:oid:2.16.840.1.113883.6.88","http://www.nlm.nih.gov/research/umls/rxnorm","SNOMEDCT","2019-03"));
        map.put("urn:oid:2.16.840.1.113883.6.1",new VSACCodeSystemDTO("urn:oid:2.16.840.1.113883.6.1","http://loinc.org","SNOMEDCT","2019-03"));
        map.put("urn:oid:2.16.840.1.113883.6.12",new VSACCodeSystemDTO("urn:oid:2.16.840.1.113883.6.12","http://www.ama-assn.org/go/cpt","SNOMEDCT","2019-03"));
        map.put("urn:oid:2.16.840.1.113883.12.292",new VSACCodeSystemDTO("urn:oid:2.16.840.1.113883.12.292","http://hl7.org/fhir/sid/cvx","SNOMEDCT","2019-03"));
        map.put("urn:oid:2.16.840.1.113883.6.238",new VSACCodeSystemDTO("urn:oid:2.16.840.1.113883.6.238","https://www.hl7.org/fhir/us/core/CodeSystem-cdcrec.html","SNOMEDCT","2019-03"));
        map.put("urn:oid:2.16.840.1.113883.6.259",new VSACCodeSystemDTO("urn:oid:2.16.840.1.113883.6.259","http://terminology.hl7.org/codesystem/nhsn/hsloc","SNOMEDCT","2019-03"));
        map.put("urn:oid:2.16.840.1.113883.12.112",new VSACCodeSystemDTO("urn:oid:2.16.840.1.113883.12.112","http://ToBeDone.org","DischargeDisposition","2019-03"));
        map.put("urn:oid:2.16.840.1.113883.6.285",new VSACCodeSystemDTO("urn:oid:2.16.840.1.113883.6.285","http://snomed.info/sct/731000124108","HCPCS","2019-03"));
        map.put("urn:oid:2.16.840.1.113883.6.90",new VSACCodeSystemDTO("urn:oid:2.16.840.1.113883.6.90","http://hl7.org/fhir/sid/icd-10-cm","ICD10CM","2019-03"));
        map.put("urn:oid:2.16.840.1.113883.3.221.5",new VSACCodeSystemDTO("urn:oid:2.16.840.1.113883.3.221.5","http://nahdo.org/sopt","SOP","2019-03"));
        map.put("urn:oid:2.16.840.1.113883.5.111",new VSACCodeSystemDTO("urn:oid:2.16.840.1.113883.5.111","http://terminology.hl7.org/CodeSystem/v3-RoleCode","RoleCode","2019-03"));
        map.put("urn:oid:2.15.840.1.113883.5.111",new VSACCodeSystemDTO("urn:oid:2.15.840.1.113883.5.111","http://hl7.org/fhir/v3/RoleCode","RoleCode","2019-03"));
        map.put("urn:oid:2.16.1.1.113883.5.111",new VSACCodeSystemDTO("urn:oid:2.16.1.1.113883.5.111","http://terminology.hl7.org/CodeSystem/diagnosis-role","RoleCode","2019-03"));
        map.put("urn:oid:2.16.2.1.113883.5.111",new VSACCodeSystemDTO("urn:oid:2.16.2.1.113883.5.111","http://terminology.hl7.org/CodeSystem/request-intent","RoleCode","2019-03"));
        map.put("urn:oid:2.16.3.1.113883.5.111",new VSACCodeSystemDTO("urn:oid:2.16.3.1.113883.5.111","http://terminology.hl7.org/CodeSystem/medicationrequest-category","RoleCode","2019-03"));
        map.put("urn:oid:2.16.4.1.113883.5.111",new VSACCodeSystemDTO("urn:oid:2.16.4.1.113883.5.111","http://terminology.hl7.org/CodeSystem/condition-clinical","RoleCode","2019-03"));
        map.put("urn:oid:2.16.5.1.113883.5.111",new VSACCodeSystemDTO("urn:oid:2.16.5.1.113883.5.111","http://terminology.hl7.org/CodeSystem/condition-verification","RoleCode","2019-03"));
        map.put("urn:oid:2.16.6.1.113883.5.111",new VSACCodeSystemDTO("urn:oid:2.16.6.1.113883.5.111","http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical","RoleCode","2019-03"));
        map.put("urn:oid:2.16.7.1.113883.5.111",new VSACCodeSystemDTO("urn:oid:2.16.7.1.113883.5.111","http://terminology.hl7.org/CodeSystem/allergyintolerance-verification","RoleCode","2019-03"));


        when(codeListService.getOidToVsacCodeSystemMap()).thenReturn(map);
    }


    public String loadCqlResource(String cqlResource) throws IOException {
        try (InputStream i = TestCqlToMatXml.class.getResourceAsStream(CQL_TEST_RESOURCES_DIR + cqlResource)) {
            return IOUtils.toString(i);
        }
    }

    @Test
    public void testMatGlobalCommonFunctions() throws Exception {
        mockSpreadsheet();
        String cql = loadCqlResource("MATGlobalCommonFunctions_FHIR4-4.0.000.cql");
        parser.parse(cql, cqlToMatXml);
        var destination = cqlToMatXml.getDestinationModel();
        assertEquals(10, destination.getCodeSystemList().size());
        assertEquals("LOINC", destination.getCodeSystemList().get(0).getCodeSystemName());
        assertEquals("http://loinc.org", destination.getCodeSystemList().get(0).getCodeSystem());
        assertEquals("AllergyIntoleranceVerificationStatusCodes", destination.getCodeSystemList().get(9).getCodeSystemName());
        assertEquals("http://terminology.hl7.org/CodeSystem/allergyintolerance-verification", destination.getCodeSystemList().get(9).getCodeSystem());

        assertEquals(10, destination.getCodeSystemList().size());

        assertEquals(3, destination.getValueSetList().size());
        assertEquals("Encounter Inpatient", destination.getValueSetList().get(0).getName());
        assertEquals("http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.666.5.307", destination.getValueSetList().get(0).getOid());
        assertEquals("Emergency Department Visit", destination.getValueSetList().get(1).getName());
        assertEquals("http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.117.1.7.1.292", destination.getValueSetList().get(1).getOid());
        assertEquals("Observation Services", destination.getValueSetList().get(2).getName());
        assertEquals("http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1111.143", destination.getValueSetList().get(2).getOid());

        assertEquals(25, destination.getCodeList().size());
        assertEquals("Birthdate", destination.getCodeList().get(0).getName());
        assertEquals("21112-8", destination.getCodeList().get(0).getCodeOID());
        assertEquals("LOINC", destination.getCodeList().get(0).getCodeSystemName());
        assertEquals("Birth date", destination.getCodeList().get(0).getDisplayName());

        assertEquals("allergy-refuted", destination.getCodeList().get(22).getName());
        assertEquals("refuted", destination.getCodeList().get(22).getCodeOID());
        assertEquals("AllergyIntoleranceVerificationStatusCodes", destination.getCodeList().get(22).getCodeSystemName());
        assertEquals("allergy-refuted", destination.getCodeList().get(22).getDisplayName());

        assertEquals("Community", destination.getCodeList().get(23).getName());
        assertEquals("community", destination.getCodeList().get(23).getCodeOID());
        assertEquals("MedicationRequestCategory", destination.getCodeList().get(23).getCodeSystemName());
        assertEquals("Community", destination.getCodeList().get(23).getDisplayName());

        assertEquals("Discharge", destination.getCodeList().get(24).getName());
        assertEquals("discharge", destination.getCodeList().get(24).getCodeOID());
        assertEquals("MedicationRequestCategory", destination.getCodeList().get(24).getCodeSystemName());
        assertEquals("Discharge", destination.getCodeList().get(24).getDisplayName());

    }

    @Test
    public void testFhirHelpers() throws Exception {
        String cql = loadCqlResource("FhirHelpers_FHIR4-4.0.000.cql");
        parser.parse(cql, cqlToMatXml);
        var destination = cqlToMatXml.getDestinationModel();

        assertEquals("ToInterval", destination.getCqlFunctions().get(0).getName());
        assertEquals("period", destination.getCqlFunctions().get(0).getArgumentList().get(0).getArgumentName());
        assertEquals("Others", destination.getCqlFunctions().get(0).getArgumentList().get(0).getArgumentType());
        assertEquals("FHIR.Period", destination.getCqlFunctions().get(0).getArgumentList().get(0).getOtherType());
        assertEquals("if period is null then\n" +
                "        null\n" +
                "    else\n" +
                "        Interval[period.\"start\".value, period.\"end\".value]", destination.getCqlFunctions().get(0).getLogic());

        assertEquals("ToQuantity", destination.getCqlFunctions().get(1).getName());
        assertEquals("quantity", destination.getCqlFunctions().get(1).getArgumentList().get(0).getArgumentName());
        assertEquals("Others", destination.getCqlFunctions().get(1).getArgumentList().get(0).getArgumentType());
        assertEquals("FHIR.Quantity", destination.getCqlFunctions().get(1).getArgumentList().get(0).getOtherType());
        assertEquals("if quantity is null then\n" +
                "        null\n" +
                "    else\n" +
                "        System.Quantity { value: quantity.value.value, unit: quantity.unit.value }", destination.getCqlFunctions().get(1).getLogic());

        assertEquals("ToInterval", destination.getCqlFunctions().get(2).getName());
        assertEquals("range", destination.getCqlFunctions().get(2).getArgumentList().get(0).getArgumentName());
        assertEquals("Others", destination.getCqlFunctions().get(2).getArgumentList().get(0).getArgumentType());
        assertEquals("FHIR.Range", destination.getCqlFunctions().get(2).getArgumentList().get(0).getOtherType());
        assertEquals("if range is null then\n" +
                "        null\n" +
                "    else\n" +
                "        Interval[ToQuantity(range.low), ToQuantity(range.high)]", destination.getCqlFunctions().get(2).getLogic());

        assertEquals("ToCode", destination.getCqlFunctions().get(3).getName());
        assertEquals("coding", destination.getCqlFunctions().get(3).getArgumentList().get(0).getArgumentName());
        assertEquals("Others", destination.getCqlFunctions().get(2).getArgumentList().get(0).getArgumentType());
        assertEquals("FHIR.Coding", destination.getCqlFunctions().get(3).getArgumentList().get(0).getOtherType());
        assertEquals("if coding is null then\n" +
                "        null\n" +
                "    else\n" +
                "        System.Code {\n" +
                "          code: coding.code.value,\n" +
                "          system: coding.system.value,\n" +
                "          version: coding.version.value,\n" +
                "          display: coding.display.value\n" +
                "        }", destination.getCqlFunctions().get(3).getLogic());


        assertEquals("ToConcept", destination.getCqlFunctions().get(4).getName());
        assertEquals("concept", destination.getCqlFunctions().get(4).getArgumentList().get(0).getArgumentName());
        assertEquals("FHIR.CodeableConcept", destination.getCqlFunctions().get(4).getArgumentList().get(0).getOtherType());
        assertEquals("Others", destination.getCqlFunctions().get(4).getArgumentList().get(0).getArgumentType());
        assertEquals("if concept is null then\n" +
                "        null\n" +
                "    else\n" +
                "        System.Concept {\n" +
                "            codes: concept.coding C return ToCode(C),\n" +
                "            display: concept.text.value\n" +
                "        }", destination.getCqlFunctions().get(4).getLogic());

        validateToString(destination, "FHIR.uuid", 5);
        validateToString(destination, "FHIR.TestScriptRequestMethodCode", 6);
        validateToString(destination, "FHIR.SortDirection", 7);
        validateToString(destination, "FHIR.BiologicallyDerivedProductStatus", 8);
        validateToString(destination, "FHIR.UnitsOfTime", 9);
        validateToString(destination, "FHIR.AddressType", 10);
        validateToString(destination, "FHIR.AllergyIntoleranceCategory", 11);
        validateToString(destination, "FHIR.IssueSeverity", 12);
        validateToString(destination, "FHIR.CareTeamStatus", 13);


        validateToString(destination, "FHIR.ContractResourcePublicationStatusCodes", 229);
        validateToString(destination, "FHIR.VisionBase", 230);
        validateToString(destination, "FHIR.BundleType", 231);

        assertEquals(232, destination.getCqlFunctions().size());
    }

    @Test
    public void testAudltOutpatientEncounters() throws Exception {
        String cql = loadCqlResource("AdultOutpatientEncounters_FHIR4-1.1.000.cql");
        parser.parse(cql, cqlToMatXml);
        var destination = cqlToMatXml.getDestinationModel();
        assertEquals(1, destination.getCqlParameters().size());
        assertEquals("Measurement Period", destination.getCqlParameters().get(0).getName());
        assertEquals("Interval<DateTime> default Interval[@2019-01-01T00:00:00.0, @2020-01-01T00:00:00.0)", destination.getCqlParameters().get(0).getLogic());
        assertEquals(null, destination.getCqlParameters().get(0).getCqlType());
    }

    @Test
    public void testParameterNoLogic() throws Exception {
        String cql = loadCqlResource("ParameterNoLogic.cql");
        parser.parse(cql, cqlToMatXml);
        var destination = cqlToMatXml.getDestinationModel();
        assertEquals(1, destination.getCqlParameters().size());
        assertEquals("Measurement Period", destination.getCqlParameters().get(0).getName());
        assertEquals("Interval<DateTime>", destination.getCqlParameters().get(0).getLogic());
        assertEquals(null, destination.getCqlParameters().get(0).getCqlType());
    }

    @Test
    public void testHospiceLib() throws Exception {
        mockSpreadsheet();
        String cql = loadCqlResource("Hospice_FHIR4-1.0.000.cql");
        parser.parse(cql, cqlToMatXml);
        var destination = cqlToMatXml.getDestinationModel();

        assertEquals("FHIR", destination.getUsingModel());
        assertEquals("4.0.1", destination.getUsingModelVersion());
        //TO DO: add more asserts when I get time.
        log.debug(destination.toString());

        assertEquals(2, destination.getCqlIncludeLibrarys().size());
        assertEquals("MATGlobalCommonFunctions_FHIR4", destination.getCqlIncludeLibrarys().get(0).getCqlLibraryName());
        assertEquals("4.0.000", destination.getCqlIncludeLibrarys().get(0).getVersion());
        assertEquals("Global", destination.getCqlIncludeLibrarys().get(0).getAliasName());
        assertEquals("FHIRHelpers", destination.getCqlIncludeLibrarys().get(1).getCqlLibraryName());
        assertEquals("4.0.000", destination.getCqlIncludeLibrarys().get(1).getVersion());
        assertEquals("FHIRHelpers", destination.getCqlIncludeLibrarys().get(1).getAliasName());

        assertEquals(2, destination.getValueSetList().size());
        assertEquals("Encounter Inpatient", destination.getValueSetList().get(0).getName());
        assertEquals("http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.666.5.307", destination.getValueSetList().get(0).getOid());
        assertEquals("Hospice care ambulatory", destination.getValueSetList().get(1).getName());
        assertEquals("http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113762.1.4.1108.15", destination.getValueSetList().get(1).getOid());

        assertEquals(1, destination.getCodeSystemList().size());
        assertEquals("SNOMEDCT:2017-09", destination.getCodeSystemList().get(0).getCodeSystemName());
        assertEquals("2017-09", destination.getCodeSystemList().get(0).getCodeSystemVersion());
        assertEquals("http://snomed.info/sct/731000124108", destination.getCodeSystemList().get(0).getCodeSystem());
        assertEquals("http://snomed.info/sct/731000124108/version/201709", destination.getCodeSystemList().get(0).getVersionUri());

        assertEquals(2, destination.getCodeList().size());
        assertEquals("Discharge to healthcare facility for hospice care (procedure)", destination.getCodeList().get(0).getName());
        assertEquals("CODE:/CodeSystem/SNOMEDCT/Version/2017-09/Code/428371000124100/Info", destination.getCodeList().get(0).getCodeIdentifier());
        assertEquals("428371000124100", destination.getCodeList().get(0).getCodeOID());
        assertEquals("SNOMEDCT:2017-09", destination.getCodeList().get(0).getCodeSystemName());
        assertEquals("2017-09", destination.getCodeList().get(0).getCodeSystemVersion());
        assertEquals("http://snomed.info/sct/731000124108", destination.getCodeList().get(0).getCodeSystemOID());
        assertEquals("http://snomed.info/sct/731000124108/version/201709", destination.getCodeList().get(0).getCodeSystemVersionUri());
        assertEquals(true, destination.getCodeList().get(0).isIsCodeSystemVersionIncluded());
        assertEquals("Discharge to healthcare facility for hospice care (procedure)", destination.getCodeList().get(0).getDisplayName());
        assertEquals("Discharge to home for hospice care (procedure)", destination.getCodeList().get(1).getName());
        assertEquals("CODE:/CodeSystem/SNOMEDCT/Version/2017-09/Code/428361000124107/Info", destination.getCodeList().get(1).getCodeIdentifier());
        assertEquals("428361000124107", destination.getCodeList().get(1).getCodeOID());
        assertEquals("SNOMEDCT:2017-09", destination.getCodeList().get(1).getCodeSystemName());
        assertEquals("2017-09", destination.getCodeList().get(1).getCodeSystemVersion());
        assertEquals("http://snomed.info/sct/731000124108", destination.getCodeList().get(1).getCodeSystemOID());
        assertEquals("http://snomed.info/sct/731000124108/version/201709", destination.getCodeList().get(1).getCodeSystemVersionUri());
        assertEquals(true, destination.getCodeList().get(1).isIsCodeSystemVersionIncluded());
        assertEquals("Discharge to home for hospice care (procedure)", destination.getCodeList().get(1).getDisplayName());

        assertEquals("Patient", destination.getContext());

        assertEquals(1, destination.getCqlFunctions().size());
        assertEquals("Has Hospice", destination.getCqlFunctions().get(0).getName());
        assertEquals(1, destination.getCqlFunctions().get(0).getArgumentList().size());
        assertEquals("MeasurementPeriod", destination.getCqlFunctions().get(0).getArgumentList().get(0).getArgumentName());
        assertEquals("Others", destination.getCqlFunctions().get(0).getArgumentList().get(0).getArgumentType());
        assertEquals("Interval<DateTime>", destination.getCqlFunctions().get(0).getArgumentList().get(0).getOtherType());
        assertEquals("exists (\n" +
                "\t    [Encounter: \"Encounter Inpatient\"] DischargeHospice\n" +
                "\t\t\twhere DischargeHospice.status = 'finished'\n" +
                "\t\t\t    and (\n" +
                "\t\t\t        FHIRHelpers.ToConcept(DischargeHospice.hospitalization.dischargeDisposition).codes[0] ~ \"Discharge to home for hospice care (procedure)\"\n" +
                "\t\t\t\t\t    or FHIRHelpers.ToConcept(DischargeHospice.hospitalization.dischargeDisposition).codes[0] ~ \"Discharge to healthcare facility for hospice care (procedure)\"\n" +
                "\t\t\t    )\n" +
                "\t\t\t\tand DischargeHospice.period ends during day of MeasurementPeriod\n" +
                "\t)\n" +
                "    or exists (\n" +
                "        [ServiceRequest: \"Hospice care ambulatory\"] HospiceOrder\n" +
                "            where HospiceOrder.intent = 'order'\n" +
                "                and FHIRHelpers.ToDateTime(HospiceOrder.authoredOn) in day of MeasurementPeriod\n" +
                "    )\n" +
                "    or exists (\n" +
                "        [Procedure: \"Hospice care ambulatory\"] HospicePerformed\n" +
                "            where HospicePerformed.status = 'completed'\n" +
                "                and Global.\"Normalize Interval\"(HospicePerformed.performed) overlaps MeasurementPeriod\n" +
                "    )", destination.getCqlFunctions().get(0).getLogic());
    }

    private void validateToString(CQLModel model, String type, int index) {
        assertEquals("ToString", model.getCqlFunctions().get(index).getName());
        assertEquals("value", model.getCqlFunctions().get(index).getArgumentList().get(0).getArgumentName());
        assertEquals(type, model.getCqlFunctions().get(index).getArgumentList().get(0).getOtherType());
        assertEquals("Others", model.getCqlFunctions().get(index).getArgumentList().get(0).getArgumentType());
        assertEquals("value.value", model.getCqlFunctions().get(index).getLogic());
    }
}
