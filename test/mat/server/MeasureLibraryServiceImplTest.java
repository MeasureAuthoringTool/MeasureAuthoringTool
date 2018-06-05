package mat.server;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import mat.client.measure.ManageMeasureDetailModel;
import mat.dao.clause.MeasureDAO;
import mat.model.clause.Measure;
import mat.model.clause.MeasureSet;
import mat.server.service.impl.MeasurePackageServiceImpl;
import mat.server.util.MATPropertiesService;

@RunWith(MockitoJUnitRunner.class)
public class MeasureLibraryServiceImplTest {
	@Mock private ApplicationContext context;
	@Mock private MeasureDAO measureDAO;
	@Mock private MeasurePackageServiceImpl measurePackageService;
	@Mock private CQLLibraryService cqlLibraryService;
	@InjectMocks private MeasureLibraryServiceImpl measureLibraryService;
	
	@Test
	public void setValueFromModelTest() {
		
		ManageMeasureDetailModel manageMeasureDetailModel = new ManageMeasureDetailModel();
		String measureModelId = UUID.randomUUID().toString();
		manageMeasureDetailModel.setId(measureModelId);
		manageMeasureDetailModel.setName("Test Measure");
		manageMeasureDetailModel.setShortName("Test Measure");
		manageMeasureDetailModel.setMeasScoring("Ratio");
		manageMeasureDetailModel.setIsPatientBased(false);
		MATPropertiesService.get().setQmdVersion("5.3");

		Measure testMeasure = new Measure();
		MeasureSet testMeasureSet = new MeasureSet();
		String measureSetId = UUID.randomUUID().toString();
		testMeasureSet.setId(measureSetId);
		testMeasure.setId(UUID.randomUUID().toString());
		testMeasure.setMeasureSet(testMeasureSet);
		testMeasure.setMeasureScoring("Ratio");

		Mockito.when(context.getBean("measurePackageService")).thenReturn(measurePackageService);		
		Mockito.when(measurePackageService.getById(measureModelId)).thenReturn(testMeasure);
		Mockito.when(measurePackageService.findMeasureSet(measureSetId)).thenReturn(testMeasureSet);

		measureLibraryService.save(manageMeasureDetailModel);
	}
}
