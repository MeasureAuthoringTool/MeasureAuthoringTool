package mat.server;

import com.amazonaws.services.s3.model.PutObjectResult;
import mat.client.clause.clauseworkspace.model.MeasureDetailResult;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.shared.GenericResult;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.dto.MeasureTransferDTO;
import mat.model.User;
import mat.model.clause.Measure;
import mat.model.clause.MeasureExport;
import mat.model.clause.MeasureSet;
import mat.model.clause.ModelTypeHelper;
import mat.server.service.MeasureLibraryService;
import mat.server.util.MeasureTransferUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;


import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ModelTypeHelper.class, MeasureTransferUtil.class})
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
class MeasureServiceImplTest {

    public MeasureServiceImplTest() {

    }

    @Mock
    private ApplicationContext context;

    @Mock
    private MeasureLibraryService measureLibraryService;

    @Mock
    private MeasureExportDAO measureExportDAO;

    @Mock
    private MeasureDAO measureDAO;

    @Captor
    private ArgumentCaptor<MeasureTransferDTO> measureTransferDTOArgumentCaptor;

    @InjectMocks
    private MeasureServiceImpl measureService;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(ModelTypeHelper.class);
        PowerMockito.mockStatic(MeasureTransferUtil.class);
    }

    @Test
    public void testTransferMeasureToMadieFhir() throws Exception {
        // given
        MeasureExport export = new MeasureExport();
        Measure measure = new Measure();
        measure.setMeasureModel(ModelTypeHelper.FHIR);
        export.setMeasure(measure);
        export.setMeasureJson("GOOD MEASURE JSON HERE");
        export.setFhirIncludedLibsJson("GOOD INCLUDED LIBS JSON");
        when(measureExportDAO.findByMeasureId(anyString()))
                .thenReturn(export);
        PowerMockito.when(ModelTypeHelper.isFhir(anyString())).thenReturn(true);

        ManageMeasureDetailModel detailModel = new ManageMeasureDetailModel();
        when(measureLibraryService.getMeasure(anyString()))
                .thenReturn(detailModel);
        MeasureDetailResult measureDetailResult = new MeasureDetailResult();
        detailModel.setMeasureDetailResult(measureDetailResult);

        when(measureDAO.find(anyString())).thenReturn(measure);

        PowerMockito.when(MeasureTransferUtil.uploadMeasureDataToS3Bucket(any(MeasureTransferDTO.class), anyString()))
                .thenReturn(new PutObjectResult());

        // when
        GenericResult output = measureService.transferMeasureToMadie("measure123");

        // then
        assertThat(output.isSuccess(), is(true));
        PowerMockito.verifyStatic(MeasureTransferUtil.class, times(1));
        MeasureTransferUtil.uploadMeasureDataToS3Bucket(measureTransferDTOArgumentCaptor.capture(), anyString());
        MeasureTransferDTO measureTransferDto = measureTransferDTOArgumentCaptor.getValue();
        assertThat(measureTransferDto.getFhirMeasureResourceJson(), is(equalTo("GOOD MEASURE JSON HERE")));
        assertThat(measureTransferDto.getFhirLibraryResourcesJson(), is(equalTo("GOOD INCLUDED LIBS JSON")));
    }

    @Test
    public void testTransferMeasureToMadieQdm() throws Exception {
        // given
        MeasureExport export = new MeasureExport();
        Measure measure = new Measure();
        measure.setMeasureModel(ModelTypeHelper.FHIR);
        export.setMeasure(measure);
        export.setMeasureJson("SOME ELM JSON");
        export.setCql("SOME AWESOME CQL HERE");
        export.setSimpleXML("<fake>WOW THIS IS GREAT NOT-XML</fake>");
        when(measureExportDAO.findByMeasureId(anyString()))
                .thenReturn(export);
        PowerMockito.when(ModelTypeHelper.isFhir(anyString())).thenReturn(false);

        ManageMeasureDetailModel detailModel = new ManageMeasureDetailModel();
        when(measureLibraryService.getMeasure(anyString()))
                .thenReturn(detailModel);
        MeasureDetailResult measureDetailResult = new MeasureDetailResult();
        detailModel.setMeasureDetailResult(measureDetailResult);

        when(measureDAO.find(anyString())).thenReturn(measure);

        PowerMockito.when(MeasureTransferUtil.uploadMeasureDataToS3Bucket(any(MeasureTransferDTO.class), anyString()))
                .thenReturn(new PutObjectResult());

        // when
        GenericResult output = measureService.transferMeasureToMadie("measure123");

        // then
        assertThat(output.isSuccess(), is(true));
        PowerMockito.verifyStatic(MeasureTransferUtil.class, times(1));
        MeasureTransferUtil.uploadMeasureDataToS3Bucket(measureTransferDTOArgumentCaptor.capture(), anyString());
        MeasureTransferDTO measureTransferDto = measureTransferDTOArgumentCaptor.getValue();
        assertThat(measureTransferDto.getCql(), is(equalTo("SOME AWESOME CQL HERE")));
        assertThat(measureTransferDto.getSimpleXml(), is(equalTo("<fake>WOW THIS IS GREAT NOT-XML</fake>")));
    }


    @Test
    public void testIsMeasureTransferableToMadieReturnsTrueForSingleMeasureNotTransferred() {
        // given
        User owner = new User();
        owner.setId("User1");
        MeasureSet measureSet = new MeasureSet();
        measureSet.setName("MS999");
        measureSet.setId("measureSet999");
        Measure measure = new Measure();
        measure.setOwner(owner);
        measure.setMeasureSet(measureSet);
        measure.setTransferredToMadieBucket(false);
        when(measureDAO.find(anyString())).thenReturn(measure);

        when(measureDAO.getAllMeasuresBySetID(anyString()))
                .thenReturn(List.of(measure));

        // when
        Boolean output = measureService.isMeasureTransferableToMadie("measure123", "measureSet999", "User1");

        // then
        assertThat(output, is(true));
    }

    @Test
    public void testIsMeasureTransferableToMadieReturnsTrueForMultipleMeasuresNotTransferred() {
        // given
        User owner = new User();
        owner.setId("User1");
        MeasureSet measureSet = new MeasureSet();
        measureSet.setName("MS999");
        measureSet.setId("measureSet999");
        Measure measure = new Measure();
        measure.setId("measure123");
        measure.setOwner(owner);
        measure.setMeasureSet(measureSet);
        measure.setTransferredToMadieBucket(false);
        when(measureDAO.find(anyString())).thenReturn(measure);

        Measure measure2 = new Measure();
        measure2.setId("measure234");
        measure2.setOwner(owner);
        measure2.setMeasureSet(measureSet);
        measure2.setTransferredToMadieBucket(false);

        when(measureDAO.getAllMeasuresBySetID(anyString()))
                .thenReturn(List.of(measure, measure2));

        // when
        Boolean output = measureService.isMeasureTransferableToMadie("measure123", "measureSet999", "User1");

        // then
        assertThat(output, is(true));
    }

    @Test
    public void testIsMeasureTransferableToMadieReturnsFalseForMultipleMeasuresOneTransferred() {
        // given
        User owner = new User();
        owner.setId("User1");
        MeasureSet measureSet = new MeasureSet();
        measureSet.setName("MS999");
        measureSet.setId("measureSet999");
        Measure measure = new Measure();
        measure.setId("measure123");
        measure.setOwner(owner);
        measure.setMeasureSet(measureSet);
        measure.setTransferredToMadieBucket(false);
        when(measureDAO.find(anyString())).thenReturn(measure);

        Measure measure2 = new Measure();
        measure2.setId("measure234");
        measure2.setOwner(owner);
        measure2.setMeasureSet(measureSet);
        measure2.setTransferredToMadieBucket(true);

        when(measureDAO.getAllMeasuresBySetID(anyString()))
                .thenReturn(List.of(measure, measure2));

        // when
        Boolean output = measureService.isMeasureTransferableToMadie("measure123", "measureSet999", "User1");

        // then
        assertThat(output, is(false));
    }
}