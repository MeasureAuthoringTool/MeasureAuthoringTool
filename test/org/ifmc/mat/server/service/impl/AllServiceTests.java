package org.ifmc.mat.server.service.impl;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.ifmc.mat.util.CodeListRequiredFieldTest;

public class AllServiceTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllServiceTests.class.getName());
		//$JUnit-BEGIN$

//		suite.addTestSuite(ManageCodeListServiceImpMockedlTest.class);
//		suite.addTestSuite(QDSAttributesServiceImplTest.class);
//		suite.addTestSuite(UserServiceImpMockedlTest.class);
//		suite.addTestSuite(ClauseServiceImplTest.class);
//		suite.addTestSuite(PackagerServiceImpMockedlTest.class);
//		suite.addTestSuite(LoginServiceImplTest.class);
//		suite.addTestSuite(UserServiceImplTest.class);
//		suite.addTestSuite(AdminServiceImplTest.class);
//		suite.addTestSuite(ManageQDSImplTest.class);
		
		/* added as a regular test not part of this suite */
		/*
		 	suite.addTestSuite(CodeListRequiredFieldTest.class);
			suite.addTestSuite(ResetUserPasswordTest.class);
			suite.addTestSuite(SimpleEMeasureServiceImplTest.class);
			suite.addTestSuite(ManageCodeListServiceImpllTest.class);
		*/

		//$JUnit-END$
		return suite;
	}

}
