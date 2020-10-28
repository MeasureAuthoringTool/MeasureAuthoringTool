package mat.shared;

import mat.client.admin.ManageUsersDetailModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AdminManageUserModelValidatorTest {

    private AdminManageUserModelValidator validator = new AdminManageUserModelValidator();
    private ManageUsersDetailModel validModel;

    @BeforeEach
    public void setUp() {
        validModel = new ManageUsersDetailModel() {
            {
                setHarpId("harp@example.com");
                setEmailAddress("harp@example.com");
                setFirstName("firstname");
                setLastName("lastname");
                setPhoneNumber("5555512311");
                setOid("oid");
                setOrganizationId("asd");
            }
        };
    }


    @Test
    public void testEmptyHasErrors() {
        ManageUsersDetailModel model = new ManageUsersDetailModel();
        assertEquals(9, validator.isValidUsersDetail(model).size());
    }

    @Test
    public void testEmptyNoErrors() {
        List<String> validUsersDetail = validator.isValidUsersDetail(validModel);
        assertEquals(0, validUsersDetail.size());
    }

    @Test
    public void testNewUserRequireHarpErrors() {
        validModel.setHarpId(null);
        List<String> validUsersDetail = validator.isValidUsersDetail(validModel);
        assertEquals(1, validUsersDetail.size());
        assertEquals("HARP ID is required.", validUsersDetail.get(0));
    }

    @Test
    public void testExistingUserDontRequireHarp() {
        validModel.setHarpId(null);
        validModel.setExistingUser(true);
        List<String> validUsersDetail = validator.isValidUsersDetail(validModel);
        assertEquals(0, validUsersDetail.size());
    }

    @Test
    public void testEmailErrors() {
        validModel.setEmailAddress(null);
        List<String> validUsersDetail = validator.isValidUsersDetail(validModel);
        assertEquals(1, validUsersDetail.size());
        assertEquals("Email Address is required.", validUsersDetail.get(0));
    }

    @Test
    public void testFirstNameErrors() {
        validModel.setFirstName(null);
        List<String> validUsersDetail = validator.isValidUsersDetail(validModel);
        assertEquals(Arrays.asList("First Name is required.", "First Name must be at least two characters."), validUsersDetail);
    }

    @Test
    public void testLastNameErrors() {
        validModel.setLastName(null);
        List<String> validUsersDetail = validator.isValidUsersDetail(validModel);
        assertEquals(Arrays.asList("Last Name is required."), validUsersDetail);
    }

    @Test
    public void testOidErrors() {
        validModel.setOid(null);
        List<String> validUsersDetail = validator.isValidUsersDetail(validModel);
        assertEquals(Arrays.asList("OID is required."), validUsersDetail);
    }

    @Test
    public void testOrgErrors() {
        validModel.setOrganizationId(null);
        List<String> validUsersDetail = validator.isValidUsersDetail(validModel);
        assertEquals(Arrays.asList("Organization is required."), validUsersDetail);
    }

    @Test
    public void testPhoneNameErrors() {
        validModel.setPhoneNumber(null);
        List<String> validUsersDetail = validator.isValidUsersDetail(validModel);
        assertEquals(Arrays.asList("Phone Number is required.", "Phone Number is required to be 10 digits."), validUsersDetail);
    }

}
