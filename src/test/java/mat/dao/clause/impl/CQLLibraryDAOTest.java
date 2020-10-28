package mat.dao.clause.impl;

import mat.config.DaoTestConfig;
import mat.dao.OrganizationDAO;
import mat.dao.SecurityRoleDAO;
import mat.dao.StatusDAO;
import mat.dao.UserDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.model.AuditLog;
import mat.model.Organization;
import mat.model.SecurityRole;
import mat.model.Status;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.server.util.MATPropertiesService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DaoTestConfig.class)
public class CQLLibraryDAOTest {

    @Autowired
    private CQLLibraryDAO cqlLibraryDAO;

    @Autowired
    private OrganizationDAO organizationDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SecurityRoleDAO securityRoleDAO;

    @Autowired
    private StatusDAO statusDAO;

    private Organization organization;
    private User user;
    private List<CQLLibrary> cqlLibraries = new ArrayList<>();

    @Before
    public void setup() {
        organization =  new Organization();
        organization.setOrganizationName("CMS");
        organization.setOrganizationOID("fake-oid");
        organizationDAO.save(organization);

        SecurityRole securityRole = new SecurityRole();
        securityRole.setId("1");
        securityRole.setDescription("top level admin");
        securityRoleDAO.save(securityRole);
        Status status = new Status();
        status.setStatusId("1");
        status.setDescription("Active status");
        statusDAO.save(status);

        AuditLog auditLog = new AuditLog();
        auditLog.setActivityType("CREATE");
        auditLog.setCreatedBy("admin");
        auditLog.setCreateDate(new Timestamp(new Date().getTime()));

        user = new User();
        user.setOrganization(organization);
        user.setFirstName("Test1");
        user.setLastName("Test2");
        user.setEmailAddress("Test2@test.com");
        user.setTitle("QA");
        user.setPhoneNumber("555-555-5555");
        user.setSecurityRole(securityRole);
        user.setStatus(status);
        user.setAuditLog(auditLog);
        user.setActivationDate(new Date());
        userDAO.save(user);

        CQLLibrary cqlLibrary1 = createLibraryWithDetails(user, "qdmlib1", "sssdsddwwd", "QDM");
        cqlLibraryDAO.save(cqlLibrary1);

        CQLLibrary cqlLibrary2 = createLibraryWithDetails(user, "qdmlib1", "sssdsddwwd", "QDM");
        cqlLibraryDAO.save(cqlLibrary2);

        CQLLibrary cqlLibrary3 = createLibraryWithDetails(user, "fhirlib1", "nmdsddwwd", "FHIR");
        cqlLibraryDAO.save(cqlLibrary3);

        CQLLibrary cqlLibrary4 = createLibraryWithDetails(user, "fhirlib2", "qwdsddwwd", "FHIR");
        cqlLibraryDAO.save(cqlLibrary4);

        CQLLibrary cqlLibrary5 = createLibraryWithDetails(user, "pre-cql1", "qwdsddwwd", null);
        cqlLibraryDAO.save(cqlLibrary5);

        CQLLibrary cqlLibrary6 = createLibraryWithDetails(user, "pre-cql2", "qwdsddwwd", null);
        cqlLibraryDAO.save(cqlLibrary6);

        cqlLibraries.add(cqlLibrary1);
        cqlLibraries.add(cqlLibrary2);
        cqlLibraries.add(cqlLibrary3);
        cqlLibraries.add(cqlLibrary4);
        cqlLibraries.add(cqlLibrary5);
        cqlLibraries.add(cqlLibrary6);
    }

    private CQLLibrary createLibraryWithDetails(User user, String name, String setId, String modelType) {
        CQLLibrary cqlLibrary = new CQLLibrary();
        cqlLibrary.setName(name);
        cqlLibrary.setQdmVersion("5.5");
        cqlLibrary.setSetId(setId);
        cqlLibrary.setDraft(false);
        cqlLibrary.setLibraryModelType(modelType);
        cqlLibrary.setOwnerId(user);
        return cqlLibrary;
    }

    @Test
    public void testSearchForIncludes() {
        MATPropertiesService.get().setQdmVersion("5.5");
        List<CQLLibrary> cqlLibs = cqlLibraryDAO.searchForIncludes("aadsddwwd", "alzlib1",
                "", "QDM");
        Assertions.assertNotNull(cqlLibs);
        Assertions.assertEquals(cqlLibs.size(), 2);
        Assertions.assertEquals(cqlLibs.get(0).getLibraryModelType(), "QDM");
        Assertions.assertEquals(cqlLibs.get(1).getLibraryModelType(), "QDM");
    }

    @Test
    public void testSearchForIncludesWhenModelTypeNull() {
        MATPropertiesService.get().setQdmVersion("5.5");
        List<CQLLibrary> cqlLibs = cqlLibraryDAO.searchForIncludes("wqqwdsddwwd", "testlib",
                "", null);
        Assertions.assertNotNull(cqlLibs);
        Assertions.assertEquals(cqlLibs.size(), 2);
        Assertions.assertNull(cqlLibs.get(0).getLibraryModelType());
        Assertions.assertNull(cqlLibs.get(1).getLibraryModelType());
    }

    @After
    public void tearDown() {
        for (CQLLibrary lib : cqlLibraries) {
            cqlLibraryDAO.delete(lib.getId());
        }
        userDAO.delete(user.getId());
        organizationDAO.delete(organization.getId());
    }
}
