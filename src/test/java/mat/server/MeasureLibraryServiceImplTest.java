package mat.server;

import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.service.CQLService;
import mat.model.clause.Measure;
import mat.model.clause.MeasureSet;
import mat.server.util.MATPropertiesService;
import org.hibernate.Hibernate;
import org.hibernate.engine.jdbc.LobCreator;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import javax.sql.rowset.serial.SerialException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class MeasureLibraryServiceImplTest {
    @Mock
    private ApplicationContext context;
    @Mock
    SessionImplementor mockSessionImplementor;
    @Mock
    SessionFactoryImplementor mockSessionFactoryImplementor;
    @Mock
    ServiceRegistryImplementor mockServiceRegistryImplementor;
    @Mock
    JdbcServices mockJdbcServices;
    @Mock
    LobCreator mockLobCreator;
    @Mock
    Blob mockBlob;
    @Mock
    CQLService cqlService;
    @InjectMocks
    private MeasureLibraryServiceImpl measureLibraryService;

    @Test
    @Disabled //TODO Hasn't been run for a time, review.
    public void setValueFromModelTest() {

        ManageMeasureDetailModel manageMeasureDetailModel = new ManageMeasureDetailModel();
        String measureModelId = UUID.randomUUID().toString();
        manageMeasureDetailModel.setId(measureModelId);
        manageMeasureDetailModel.setMeasureName("Test Measure");
        manageMeasureDetailModel.setShortName("Test Measure");
        manageMeasureDetailModel.setMeasScoring("Ratio");
        manageMeasureDetailModel.setIsPatientBased(false);
        manageMeasureDetailModel.setCQLLibraryName("Test Library");
        manageMeasureDetailModel.setMeasureSetId("testMeasureSetId1");
        MATPropertiesService.get().setQdmVersion("5.3");

        Measure testMeasure = new Measure();
        MeasureSet testMeasureSet = new MeasureSet();
        String measureSetId = UUID.randomUUID().toString();
        testMeasureSet.setId(measureSetId);
        testMeasure.setId(UUID.randomUUID().toString());
        testMeasure.setMeasureSet(testMeasureSet);
        testMeasure.setMeasureScoring("Ratio");

        Mockito.when(cqlService.checkIfLibraryNameExists(Mockito.eq("Test Library"), Mockito.eq("testMeasureSetId1"))).thenReturn(true);

        measureLibraryService.saveOrUpdateMeasure(manageMeasureDetailModel);
    }

    @Test
    public void hibernateConfigTest() throws SerialException, SQLException {
        String inputXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><measure><measureDetails><uuid>8a1616f1-63d1-9ba9-0163-d19d75310002</uuid><title>TestMeasureWithUnusedInclude</title><shortTitle>TestMeasureWithUnusedInclude</shortTitle><guid>a3923ebb-9a72-4a00-b8f8-f8526751bab5</guid><version>0.0.000</version><nqfid root=\"2.16.840.1.113883.3.560.1\"/><period calenderYear=\"true\" uuid=\"e3a14bb3-ef7c-47e3-829c-87768412bbc8\"><startDate>01/01/20XX</startDate><stopDate>12/31/20XX</stopDate></period><scoring id=\"COHORT\">Cohort</scoring><componentMeasures/><patientBasedIndicator>true</patientBasedIndicator></measureDetails><populations displayName=\"Populations\"><initialPopulations displayName=\"Initial Populations\"><clause displayName=\"Initial Population 1\" type=\"initialPopulation\" uuid=\"81E8D8E6-28DC-4619-AB64-C7A26CFBB36F\"/></initialPopulations></populations><strata displayName=\"Stratification\"><stratification displayName=\"Stratification 1\" type=\"stratification\" uuid=\"745BAB2C-8778-485D-8558-2B51708896FF\"><clause displayName=\"Stratum 1\" type=\"stratum\" uuid=\"A6CD4E69-01F2-4A86-9B64-0DA8275157C7\"/></stratification></strata><supplementalDataElements><cqldefinition displayName=\"SDE Ethnicity\" uuid=\"da40ce1a-bd58-4f21-8656-3001fcc42854\"/><cqldefinition displayName=\"SDE Payer\" uuid=\"a817e9bc-696e-4037-afdb-20c8cc1eb27d\"/><cqldefinition displayName=\"SDE Race\" uuid=\"b4dd89d5-6e62-4ad2-9ed3-ae4726a3c885\"/><cqldefinition displayName=\"SDE Sex\" uuid=\"a12ddc77-ba91-4a1a-8001-a80c5a4cacdf\"/></supplementalDataElements><riskAdjustmentVariables/><measureGrouping/><subTreeLookUp/><elementLookUp/><cqlLookUp><library>TestMeasureWithUnusedInclude</library><version>0.0.000</version><usingModel>QDM</usingModel><usingModelVersion>5.3</usingModelVersion><cqlContext>Patient</cqlContext><codeSystems/><valuesets><valueset datatype=\"Patient Characteristic Sex\" id=\"b7ff2d4b-1c97-4086-a0d6-d72b938f16c6\" name=\"ONC Administrative Sex\" oid=\"2.16.840.1.113762.1.4.1\" originalName=\"ONC Administrative Sex\" program=\"\" release=\"\" suffix=\"\" suppDataElement=\"true\" taxonomy=\"Administrative Sex\" uuid=\"3c717eaa-cc69-4286-8c98-e3c1c2eaea3a\" version=\"1.0\"/><valueset datatype=\"Patient Characteristic Race\" id=\"32bc728c-1b42-4b68-acfd-0315c2153e4e\" name=\"Race\" oid=\"2.16.840.1.114222.4.11.836\" originalName=\"Race\" program=\"\" release=\"\" suffix=\"\" suppDataElement=\"true\" taxonomy=\"CDC\" uuid=\"7f6f3d68-b4b1-43ee-ac34-f7388b9cef2d\" version=\"1.0\"/><valueset datatype=\"Patient Characteristic Ethnicity\" id=\"bc731d8d-6360-4765-9adb-68697e199bc9\" name=\"Ethnicity\" oid=\"2.16.840.1.114222.4.11.837\" originalName=\"Ethnicity\" program=\"\" release=\"\" suffix=\"\" suppDataElement=\"true\" taxonomy=\"CDC\" uuid=\"15a16f14-ef5b-40e0-8594-7f510071f64d\" version=\"1.0\"/><valueset datatype=\"Patient Characteristic Payer\" id=\"d5730fea-2e32-4d56-8e55-f98e9c82807f\" name=\"Payer\" oid=\"2.16.840.1.114222.4.11.3591\" originalName=\"Payer\" program=\"\" release=\"\" suffix=\"\" suppDataElement=\"true\" taxonomy=\"Source of Payment Typology\" uuid=\"a5a9a1ef-2163-4ef6-82ea-4dd3c49048d9\" version=\"1.0\"/></valuesets><codes/><parameters><parameter id=\"2da6d87e-4176-4710-8168-be9ea08132f5\" name=\"Measurement Period\" readOnly=\"true\"><logic>Interval&lt;DateTime&gt;</logic></parameter></parameters><definitions><definition context=\"Patient\" id=\"da40ce1a-bd58-4f21-8656-3001fcc42854\" name=\"SDE Ethnicity\" popDefinition=\"false\" supplDataElement=\"true\"><logic>[\"Patient Characteristic Ethnicity\": \"Ethnicity\"]</logic></definition><definition context=\"Patient\" id=\"a817e9bc-696e-4037-afdb-20c8cc1eb27d\" name=\"SDE Payer\" popDefinition=\"false\" supplDataElement=\"true\"><logic>[\"Patient Characteristic Payer\": \"Payer\"]</logic></definition><definition context=\"Patient\" id=\"b4dd89d5-6e62-4ad2-9ed3-ae4726a3c885\" name=\"SDE Race\" popDefinition=\"false\" supplDataElement=\"true\"><logic>[\"Patient Characteristic Race\": \"Race\"]</logic></definition><definition context=\"Patient\" id=\"a12ddc77-ba91-4a1a-8001-a80c5a4cacdf\" name=\"SDE Sex\" popDefinition=\"false\" supplDataElement=\"true\"><logic>[\"Patient Characteristic Sex\": \"ONC Administrative Sex\"] </logic></definition></definitions><functions/><includeLibrarys><includeLibrary cqlLibRefId=\"8ae46199630808f30163080c7dcf000b\" cqlLibRefName=\"A\" cqlVersion=\"2.0.000\" id=\"47a68fd5-05f2-4130-a729-7dc2e592c560\" name=\"test\" qdmVersion=\"5.3\"/></includeLibrarys></cqlLookUp></measure>";
        HibernateStatisticsFilter.setContext(context);

        InputStream targetStream = new ByteArrayInputStream(inputXML.getBytes());
        Mockito.when(mockSessionImplementor.getFactory()).thenReturn(mockSessionFactoryImplementor);
        Mockito.when(mockSessionFactoryImplementor.getServiceRegistry()).thenReturn(mockServiceRegistryImplementor);
        Mockito.when(mockServiceRegistryImplementor.getService(JdbcServices.class)).thenReturn(mockJdbcServices);
        Mockito.when(mockJdbcServices.getLobCreator(mockSessionImplementor)).thenReturn(mockLobCreator);
        Mockito.when(mockLobCreator.createBlob(inputXML.getBytes())).thenReturn(mockBlob);
        Mockito.when(mockBlob.getBinaryStream()).thenReturn(targetStream);

        Blob measureXML = Hibernate.getLobCreator(mockSessionImplementor).createBlob(inputXML.getBytes());
        assertNotNull(new String(toByteArray(measureXML)));
    }

    private byte[] toByteArray(Blob fromBlob) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            return toByteArrayImpl(fromBlob, baos);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private byte[] toByteArrayImpl(Blob fromBlob, ByteArrayOutputStream baos) throws SQLException, IOException {
        byte[] buf = new byte[4000];
        InputStream is = fromBlob.getBinaryStream();
        try {
            for (; ; ) {
                int dataSize = is.read(buf);
                if (dataSize == -1)
                    break;
                baos.write(buf, 0, dataSize);
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return baos.toByteArray();
    }
}
