package mat.server.service.impl;

import mat.dao.clause.MeasureDAO;
import mat.server.service.MeasurePackageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeasurePackageServiceImplTest {

    @Mock
    private MeasureDAO measureDAO;

    @InjectMocks
    private MeasurePackageService measurePackageService;

    @Test
    private void testReturnMaxEMeasureId() {
        when(measureDAO.getMaxEMeasureId()).thenReturn(1024);
        int maxId = measurePackageService.returnMaxEMeasureId();
        assertEquals(maxId, 1025);
    }
}
