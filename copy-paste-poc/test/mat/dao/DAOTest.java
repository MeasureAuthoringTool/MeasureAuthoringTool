package mat.dao;

import java.util.List;

import mat.dao.StewardDAO;
import mat.dao.UserDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.search.CriteriaQuery;
import mat.dao.search.SearchCriteria;
import mat.model.Category;
import mat.model.CodeList;
import mat.model.CodeSystem;
import mat.model.DataType;
import mat.model.ListObject;
import mat.model.MeasureSteward;
import mat.model.QualityDataSet;
import mat.model.SecurityRole;
import mat.model.Status;
import mat.model.User;
import mat.model.clause.Measure;
import mat.model.clause.MeasureShareDTO;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;


public class DAOTest extends SpringInitializationTest {

	@Autowired
	private StewardDAO stewardDAO;
	@Autowired
	private MeasureDAO measureDAO;
	@Autowired
	private UserDAO userDAO;
	
	private Measure measure;
	private MeasureSteward steward;
	
	
	//start CodeDAOTest
	
	@Before
	public void loadData() {
		steward = stewardDAO.find().get(0);
		measure = measureDAO.find().get(0);
	}
	
	CodeDAOTestHelper codeDAOHelp = new CodeDAOTestHelper();
	@Test
	public void testCreateCodeListWithoutCodes() {
		Category c = getCategory();
		CodeSystem cs = getCodeSystem();
		User u = getUser();
		CodeList codeList = codeDAOHelp.getCodeListForTestCreateCodeListWithoutCodes(c, cs, u, steward);
		getService().getCodeListDAO().save(codeList);
	}
	
	@Test
	public void testCreateCodeListWithCodes() {
		Category c = getCategory();
		CodeSystem cs = getCodeSystem();
		User u = getUser();
		CodeList codeList = codeDAOHelp.getCodeListForTestCreateCodeListWithCodes(c, cs, u, steward);
		
		getService().getCodeListDAO().save(codeList);
	}
	
	@Test
	public void testCreateGroupedCodeList() {
		Category c = getCategory();
		CodeSystem cs = getCodeSystem();
		User u = getUser();
		List<CodeList> cls = getCodeLists();
		ListObject listObject = codeDAOHelp.getListObjectForTestCreateGroupedCodeList(c, cs, u, steward, cls);
		getService().getListObjectDAO().save(listObject);
	}
	
	@Test
	public void testCreateQDS() {
		DataType dt = getDataType();
		ListObject lo = getListObject();
		QualityDataSet qds = codeDAOHelp.getQDSForTestCreateQDS(dt, lo, measure);
		getService().getQualityDataSetDAO().save(qds);
	}
	
	private Category getCategory(){
		return getService().getCategoryDAO().find("1");
	}
	private CodeSystem getCodeSystem(){
		return getService().getCodeSystemDAO().find("1");
	}
	private User getUser(){
		return getService().getUserDAO().find("Admin");
	}
	private List<CodeList> getCodeLists(){
		return getService().getCodeListDAO().find();
	}
	private DataType getDataType(){
		return getService().getDataTypeDAO().find("1");
	}
	private ListObject getListObject(){
		return getService().getCodeListDAO().find().get(0);
	}
	//end CodeDAOTest
	
	
	//start UserDAOTest
	
	UserDAOTestHelper userDAOHelp = new UserDAOTestHelper();
	
	@Test
	@Rollback
	public void testUserDAO() {
		Status s = getStatus();
		SecurityRole sr = getSecurityRole();
		User user = userDAOHelp.getUserForTestUserDAO(s, sr);
		getService().getUserDAO().save(user);
		
		String fnupdate = user.getFirstName() + " updated";
		String lnupdate = user.getLastName() + " updated";
		user.setFirstName(fnupdate);
		user.setLastName(lnupdate);
		getService().getUserDAO().save(user);

		User user2 = getService().getUserDAO().find(user.getId());
		
		assertEquals(fnupdate, user2.getFirstName());
		assertEquals(lnupdate, user2.getLastName());
		
		testDeleteUser(user.getId());
	}
	private Status getStatus(){
		return getService().getStatusDAO().find().get(0);
	}
	private SecurityRole getSecurityRole(){
		return getService().getSecurityRoleDAO().find().get(0);
	}
	
	public void testDeleteUser(String id) {
		User user = getService().getUserDAO().find(id);
		getService().getUserDAO().delete(user);
	}
	//end UserDAOTest
	
	//start DAOSearchTest
	DAOSearchTestHelper daoSearchHelp = new DAOSearchTestHelper();
	@Test
	public void testFind() {
		Status status = getService().getStatusDAO().find("1");
		System.out.println(status.getId() + "  " + status.getDescription());		
	}

	@Test
	public void testFindAll() {
		List<Status> types = getService().getStatusDAO().find();
		for (Status type : types) {
			System.out.println(type.getId() + "  " + type.getDescription());
		}
	}
	
	@Test
	public void testSearch() {
		List<User> users = getService().getUserDAO().find();
		for(User user : users)
			System.out.println(user.getFirstName());
	}
	
	public void testPassword() {
		SearchCriteria sc1 = daoSearchHelp.getSearchCriteriaForTestPassword();
		User user = getService().getUserDAO().find(new CriteriaQuery(sc1)).get(0);
		System.out.println(user.getFirstName());
	}
	
	//end DAOSearchTest
	
	@Test
	public void S16_US506_MeasureNewFieldsTest() {		
		for(User user : userDAO.find()){
			System.out.println(user.getFirstName());
			List<MeasureShareDTO> dtos = measureDAO.getMeasureShareInfoForUser(user, 0, 100);
			for(MeasureShareDTO dto : dtos){
				//inspect the dtos for new fields and assert they are populated
				assertTrue(dto.getMeasureSetId() != null);
				assertTrue(dto.isDraft() == dto.isDraft());
				assertTrue(dto.getVersion() != null);
			}
		}
	}
	
	/*@Test
	public void emailAddressCaseInsensitiveTest() {
		assertTrue(userDAO.userExists("Admin").equals(Boolean.TRUE));
		assertTrue(userDAO.findByLoginId("Admin") != null);
		assertTrue(userDAO.getUser("Admin") != null);
		
		assertTrue(userDAO.userExists("admin").equals(Boolean.TRUE));
		assertTrue(userDAO.findByLoginId("admin") != null);
		assertTrue(userDAO.getUser("admin") != null);
	}*/
}
