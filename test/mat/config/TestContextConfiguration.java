package mat.server.service.jobs;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestConfiguration.class})
public class TestContextConfiguration {
	@Autowired CheckUserChangePasswordLimit checkUserChangePasswordLimit;
	
	@BeforeClass
	public static void configInitialization() {
		System.setProperty("VSAC_DRC_URL", "https://vsac.nlm.nih.gov/vsac");
		System.setProperty("SERVER_TICKET_URL", "https://vsac.nlm.nih.gov/vsac/ws/Ticket");
		System.setProperty("SERVER_SINGLE_VALUESET_URL", "https://vsac.nlm.nih.gov/vsac/ws/RetrieveValueSet?");
		System.setProperty("SERVER_MULTIPLE_VALUESET_URL_NEW", "https://vsac.nlm.nih.gov/vsac/svs/RetrieveMultipleValueSets?");
		System.setProperty("SERVICE_URL", "http://umlsks.nlm.nih.gov");
		System.setProperty("PROFILE_SERVICE", "https://vsac.nlm.nih.gov/vsac/profiles");
		System.setProperty("VERSION_SERVICE", "https://vsac.nlm.nih.gov/vsac/oid/");
		System.setProperty("2FA_AUTH_CLASS", "mat.server.twofactorauth.DefaultOTPValidatorForUser");
	}
	
	@Test
	public void setup() {
		//Successful Spring Initialization
		assertNotNull("CheckUserChangePasswordLimit bean created successfully", checkUserChangePasswordLimit);
	}
}
