package mat.server.clause;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.service.CheckMeasureForConversionResult;
import mat.dao.clause.MeasureDAO;
import mat.model.clause.Measure;
import mat.model.clause.ModelTypeHelper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FhirMeasureRemoteServiceImplCheckTest {
    private static final String MEASURE_SET_ID = "measureSetId";
    private static final String SOURCE_ID = "sourceId";
    private static final String UNRELATED_SOURCE = "unrelatedSourceId";

    @Mock
    private MeasureDAO measureDAO;
    @InjectMocks
    private FhirMeasureRemoteServiceImpl service;

    private ManageMeasureSearchModel.Result sourceMeasure;

    @BeforeEach
    public void setUp() {
        sourceMeasure = new ManageMeasureSearchModel.Result();
        sourceMeasure.setId(SOURCE_ID);
        sourceMeasure.setMeasureSetId(MEASURE_SET_ID);
    }

    @AfterEach
    public void tearDown() {
        verify(measureDAO, times(1)).getDraftMeasuresBySet(eq(MEASURE_SET_ID));
    }


    @Test
    public void testCheckNoDraftsProceedConversion() {
        ManageMeasureSearchModel.Result sourceMeasure = new ManageMeasureSearchModel.Result();
        sourceMeasure.setMeasureSetId(MEASURE_SET_ID);

        when(measureDAO.getDraftMeasuresBySet(eq(MEASURE_SET_ID))).thenReturn(List.of());
        CheckMeasureForConversionResult result = service.checkMeasureForConversion(sourceMeasure);
        assertNotNull(result);
        assertTrue(result.isProceedImmediately());
        assertFalse(result.isConfirmBeforeProceed());

        verify(measureDAO, times(1)).getDraftMeasuresBySet(eq(MEASURE_SET_ID));
    }

    @Test
    public void testCheckHasQdmDraft() {
        Measure draft = new Measure();
        draft.setMeasureModel(ModelTypeHelper.QDM);

        when(measureDAO.getDraftMeasuresBySet(eq(MEASURE_SET_ID))).thenReturn(List.of(draft));
        CheckMeasureForConversionResult result = service.checkMeasureForConversion(sourceMeasure);
        assertNotNull(result);
        assertFalse(result.isProceedImmediately());
        assertFalse(result.isConfirmBeforeProceed());

        verify(measureDAO, times(1)).getDraftMeasuresBySet(eq(MEASURE_SET_ID));
    }

    @Test
    public void testCheckHasFhirUnrelatedDraft() {
        Measure draft = new Measure();
        draft.setMeasureModel(ModelTypeHelper.FHIR);
        draft.setSourceMeasureId(UNRELATED_SOURCE);

        when(measureDAO.getDraftMeasuresBySet(eq(MEASURE_SET_ID))).thenReturn(List.of(draft));
        CheckMeasureForConversionResult result = service.checkMeasureForConversion(sourceMeasure);
        assertNotNull(result);
        assertFalse(result.isProceedImmediately());
        assertFalse(result.isConfirmBeforeProceed());

        verify(measureDAO, times(1)).getDraftMeasuresBySet(eq(MEASURE_SET_ID));
    }

    @Test
    public void testCheckHasFhirRelatedDraft() {
        Measure draft = new Measure();
        draft.setMeasureModel(ModelTypeHelper.FHIR);
        draft.setSourceMeasureId(SOURCE_ID);

        when(measureDAO.getDraftMeasuresBySet(eq(MEASURE_SET_ID))).thenReturn(List.of(draft));
        CheckMeasureForConversionResult result = service.checkMeasureForConversion(sourceMeasure);
        assertNotNull(result);
        assertFalse(result.isProceedImmediately());
        assertTrue(result.isConfirmBeforeProceed());

        verify(measureDAO, times(1)).getDraftMeasuresBySet(eq(MEASURE_SET_ID));
    }

    @Test
    public void testQdmDraftOverrideFhirDrafts() {
        ManageMeasureSearchModel.Result sourceMeasure = new ManageMeasureSearchModel.Result();
        sourceMeasure.setId(SOURCE_ID);
        sourceMeasure.setMeasureSetId(MEASURE_SET_ID);

        Measure qdmMeasure = new Measure();
        qdmMeasure.setMeasureModel(ModelTypeHelper.QDM);

        Measure fhirUnrelatedMeasure = new Measure();
        fhirUnrelatedMeasure.setMeasureModel(ModelTypeHelper.FHIR);
        fhirUnrelatedMeasure.setSourceMeasureId(UNRELATED_SOURCE);

        Measure fhirRelatedMeasure = new Measure();
        fhirRelatedMeasure.setMeasureModel(ModelTypeHelper.FHIR);
        fhirRelatedMeasure.setSourceMeasureId(SOURCE_ID);

        List<Measure> drafts = List.of(qdmMeasure, fhirUnrelatedMeasure, fhirRelatedMeasure);

        when(measureDAO.getDraftMeasuresBySet(eq(MEASURE_SET_ID))).thenReturn(drafts);
        CheckMeasureForConversionResult result = service.checkMeasureForConversion(sourceMeasure);
        assertNotNull(result);
        assertFalse(result.isProceedImmediately());
        assertFalse(result.isConfirmBeforeProceed());

        verify(measureDAO, times(1)).getDraftMeasuresBySet(eq(MEASURE_SET_ID));
    }

}
