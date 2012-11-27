package org.ifmc.mat.sprint2Testcase;

import org.easymock.EasyMock;
import org.ifmc.mat.client.measure.ManageMeasureDetailModel;
import org.ifmc.mat.client.measure.ManageMeasureDetailView;
import org.ifmc.mat.client.measure.ManageMeasurePresenter;
import org.junit.Test;

import junit.framework.TestCase;

public class ValidationTestForCloning extends TestCase{
	
	@Test
	public void testForValidationOfEmptyFields(){
		final ManageMeasurePresenter mockMMPresenter = EasyMock.createMock(ManageMeasurePresenter.class);
		final ManageMeasureDetailView mockMMDetailView = EasyMock.createMock(ManageMeasureDetailView.class);
		ManageMeasureDetailModel mmDetailModel = new ManageMeasureDetailModel();
		mmDetailModel.setName("");
		mmDetailModel.setShortName("");
		EasyMock.expect(mockMMPresenter.isValid(mmDetailModel)).andReturn(false);
	    EasyMock.replay(mockMMPresenter);
	    EasyMock.replay(mockMMDetailView);
	    assertEquals("Name field cannot be empty","" ,mmDetailModel.getName());
	    assertEquals("Short Name field cannot be empty","" ,mmDetailModel.getName());
		assertFalse(mockMMPresenter.isValid(mmDetailModel));
		
	}

}
