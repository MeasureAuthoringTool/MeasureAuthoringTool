package mat.server.service.impl;

import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"mat-persistence-mock.xml", "applicationContext-mail-mock.xml",
		"file:**/applicationContext-service.xml" }) 
public abstract class BaseServiceMockedTest  extends TestCase {
	@Autowired
	protected ApplicationContext applicationContext;

} 
