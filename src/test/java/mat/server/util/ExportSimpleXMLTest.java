package mat.server.util;

import mat.dao.MeasureTypeDAO;
import mat.dao.OrganizationDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.MeasureDAO;
import mat.model.clause.Measure;
import mat.model.clause.MeasureXML;
import mat.model.cql.CQLModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExportSimpleXMLTest {
    @Mock
    private OrganizationDAO organizationDAO;
    @Mock
    private CQLLibraryDAO cqlLibraryDAO;
    @Mock
    private MeasureDAO measureDAO;
    @Mock
    private MeasureTypeDAO measureTypeDAO;

    @Disabled("Needs for additional research")
    @Test
    public void testExportSimpleXMLExportForCohort() {
        MeasureXML testXML = mock(MeasureXML.class);
        when(testXML.getMeasureXMLAsString()).thenReturn(buildMeasureXML());
        when(testXML.getMeasureId()).thenReturn("8ae46199641932f301641935eca30002");
        when(measureDAO.find(anyString())).thenReturn(buildTestMeasure());
        CQLModel cqlModel = new CQLModel();
        String simpleXML = ExportSimpleXML.export(testXML, measureDAO, organizationDAO, cqlLibraryDAO, cqlModel, measureTypeDAO);
        XmlProcessor xmlProcessor = new XmlProcessor(simpleXML);

        try {
            String XPATH_STRATUM = "//measure/measureGrouping/group/clause[@displayName=\"stratum\"]";
            Node measureGroupingNode = xmlProcessor.findNode(xmlProcessor.getOriginalDoc(), XPATH_STRATUM);
            assertNotNull(measureGroupingNode);
        } catch (XPathExpressionException e) {
            fail();
        }
    }

    private String buildMeasureXML() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><measure><measureDetails><uuid>8ae46199-6419-32f3-0164-1935eca30002</uuid><title>MAT-9224</title><shortTitle>MAT9224</shortTitle><guid>c18f2614-e4f6-4517-b019-6e3a39016154</guid><version>0.0.002</version><nqfid root=\"2.16.840.1.113883.3.560.1\"/><period calenderYear=\"true\" uuid=\"a769c922-a508-4ac0-a38b-29f66c0f8c4c\"><startDate>01/01/20XX</startDate><stopDate>12/31/20XX</stopDate></period><scoring id=\"COHORT\">Cohort</scoring><componentMeasures/><patientBasedIndicator>true</patientBasedIndicator></measureDetails><populations displayName=\"Populations\"><initialPopulations displayName=\"Initial Populations\"><clause displayName=\"Initial Population 1\" type=\"initialPopulation\" uuid=\"EC1BE98A-C1B1-4E58-AE7A-84FE1BF64526\"><cqldefinition displayName=\"testDef\" uuid=\"6322063b-b019-4ac9-b38d-c106d705d926\"/></clause></initialPopulations></populations><strata displayName=\"Stratification\"><stratification displayName=\"Stratification 1\" type=\"stratification\" uuid=\"F5EC2DFA-D797-4E36-8D7D-0A429CD9E373\"><clause displayName=\"Stratum 1\" type=\"stratum\" uuid=\"4303CF5D-0C0D-4AAD-8957-5CCDC676886A\"/></stratification></strata><supplementalDataElements><cqldefinition displayName=\"SDE Ethnicity\" uuid=\"a7e0b9c5-4090-4b4a-a022-2f29ccf0394e\"/><cqldefinition displayName=\"SDE Payer\" uuid=\"b99c41b3-78c6-40ba-96f4-06292f3a21eb\"/><cqldefinition displayName=\"SDE Race\" uuid=\"18a738a2-d8de-44fb-8a05-eec9a80b3f45\"/><cqldefinition displayName=\"SDE Sex\" uuid=\"51b265a8-825c-4788-a9dd-f77497c79d60\"/></supplementalDataElements><riskAdjustmentVariables/><measureGrouping><group sequence=\"1\"><packageClause isInGrouping=\"false\" name=\"Initial Population 1\" type=\"initialPopulation\" uuid=\"EC1BE98A-C1B1-4E58-AE7A-84FE1BF64526\"/></group></measureGrouping><subTreeLookUp/><elementLookUp/><cqlLookUp><library>MAT9224</library><version>0.0.002</version><usingModel>QDM</usingModel><usingModelVersion>5.3</usingModelVersion><cqlContext>Patient</cqlContext><codeSystems><codeSystem codeSystem=\"2.16.840.1.113883.6.238\" codeSystemName=\"CDCREC\" codeSystemVersion=\"1.0\" id=\"5c64f0d8614242808fe34716917b6a97\"/></codeSystems><valuesets><valueset datatype=\"Patient Characteristic Sex\" id=\"bb6c6b4c-2c77-434e-9cb6-a6b1cf44a394\" name=\"ONC Administrative Sex\" oid=\"2.16.840.1.113762.1.4.1\" originalName=\"ONC Administrative Sex\" program=\"\" release=\"\" suffix=\"\" suppDataElement=\"true\" taxonomy=\"Administrative Sex\" uuid=\"7e8272e2-60a1-4882-89cb-e16a924645e2\" version=\"1.0\"/><valueset datatype=\"Patient Characteristic Race\" id=\"2e457ec1-fe6f-42b0-810b-704605fce344\" name=\"Race\" oid=\"2.16.840.1.114222.4.11.836\" originalName=\"Race\" program=\"\" release=\"\" suffix=\"\" suppDataElement=\"true\" taxonomy=\"CDC\" uuid=\"9f109aa9-7c57-46c4-810b-99ec3e753f13\" version=\"1.0\"/><valueset datatype=\"Patient Characteristic Ethnicity\" id=\"a0c76cd6-1893-41a2-8d29-77a0d579c15f\" name=\"Ethnicity\" oid=\"2.16.840.1.114222.4.11.837\" originalName=\"Ethnicity\" program=\"\" release=\"\" suffix=\"\" suppDataElement=\"true\" taxonomy=\"CDC\" uuid=\"b94a0098-6a2a-4fd0-a01b-4a469c740caf\" version=\"1.0\"/><valueset datatype=\"Patient Characteristic Payer\" id=\"8af38efb-ca37-40c2-9238-3f9d895019d5\" name=\"Payer\" oid=\"2.16.840.1.114222.4.11.3591\" originalName=\"Payer\" program=\"\" release=\"\" suffix=\"\" suppDataElement=\"true\" taxonomy=\"Source of Payment Typology\" uuid=\"49401a4c-c55b-4032-8476-69ea4a554ab5\" version=\"1.0\"/></valuesets><codes><code codeIdentifier=\"CODE:/CodeSystem/CDCREC/Version/1.0/Code/1019-9/Info\" codeName=\"White Mountain Apache\" codeOID=\"1019-9\" codeSystemName=\"CDCREC\" codeSystemOID=\"2.16.840.1.113883.6.238\" codeSystemVersion=\"1.0\" displayName=\"White Mountain Apache\" id=\"18f9724c118a49e5a7f13a28e8103407\" isCodeSystemVersionIncluded=\"false\" readOnly=\"false\"/></codes><parameters><parameter id=\"edfba78c-11dd-411e-a77f-fba51768fbdb\" name=\"Measurement Period\" readOnly=\"true\"><logic>Interval&lt;DateTime&gt;</logic></parameter></parameters><definitions><definition context=\"Patient\" id=\"a7e0b9c5-4090-4b4a-a022-2f29ccf0394e\" name=\"SDE Ethnicity\" popDefinition=\"false\" supplDataElement=\"true\"><logic>[\"Patient Characteristic Ethnicity\": \"Ethnicity\"]</logic></definition><definition context=\"Patient\" id=\"b99c41b3-78c6-40ba-96f4-06292f3a21eb\" name=\"SDE Payer\" popDefinition=\"false\" supplDataElement=\"true\"><logic>[\"Patient Characteristic Payer\": \"Payer\"]</logic></definition><definition context=\"Patient\" id=\"18a738a2-d8de-44fb-8a05-eec9a80b3f45\" name=\"SDE Race\" popDefinition=\"false\" supplDataElement=\"true\"><logic>[\"Patient Characteristic Race\": \"Race\"]</logic></definition><definition context=\"Patient\" id=\"51b265a8-825c-4788-a9dd-f77497c79d60\" name=\"SDE Sex\" popDefinition=\"false\" supplDataElement=\"true\"><logic>[\"Patient Characteristic Sex\": \"ONC Administrative Sex\"] </logic></definition><definition context=\"Patient\" id=\"6322063b-b019-4ac9-b38d-c106d705d926\" name=\"testDef\" popDefinition=\"false\" supplDataElement=\"false\"><logic>exists [\"Device, Applied\": \"White Mountain Apache\"]</logic><comment/></definition></definitions><functions/><includeLibrarys/></cqlLookUp></measure>";
    }

    private Measure buildTestMeasure() {
        Measure testMeasure = new Measure();
        testMeasure.setVersion("0.000");
        testMeasure.setRevisionNumber("001");
        return testMeasure;
    }
}
