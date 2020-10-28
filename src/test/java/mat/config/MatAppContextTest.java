package mat.config;

import mat.server.service.jobs.CheckUserChangePasswordLimit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertNotNull;


@TestPropertySource(properties = {
        "2FA_AUTH_CLASS=mat.server.twofactorauth.DefaultOTPValidatorForUser",
        "ALGORITHM=EncyptionAlgorithm",
        "PASSWORDKEY=PasswordKey",
        "liquibase.shouldRun=false"
})
@ExtendWith(SpringExtension.class)
@DirtiesContext
@Import(MatTestConfig.class)
public class MatAppContextTest {

    @Autowired
    private CheckUserChangePasswordLimit checkUserChangePasswordLimit;

    @BeforeAll
    public static void configInitialization() {
        // Multiple services directly query system properties, instead of spring env properties.
        System.setProperty("VSAC_DRC_URL", "https://vsac.nlm.nih.gov/vsac");
        System.setProperty("SERVER_TICKET_URL", "https://vsac.nlm.nih.gov/vsac/ws/Ticket");
        System.setProperty("SERVER_SINGLE_VALUESET_URL", "https://vsac.nlm.nih.gov/vsac/ws/RetrieveValueSet?");
        System.setProperty("SERVER_MULTIPLE_VALUESET_URL_NEW", "https://vsac.nlm.nih.gov/vsac/svs/RetrieveMultipleValueSets?");
        System.setProperty("SERVICE_URL", "http://umlsks.nlm.nih.gov");
        System.setProperty("PROFILE_SERVICE", "https://vsac.nlm.nih.gov/vsac/profiles");
        System.setProperty("VERSION_SERVICE", "https://vsac.nlm.nih.gov/vsac/oid/");
    }

    @Test
    public void setup() {
        // Successful Spring Initialization
        assertNotNull("CheckUserChangePasswordLimit bean created successfully", checkUserChangePasswordLimit);
    }

}
