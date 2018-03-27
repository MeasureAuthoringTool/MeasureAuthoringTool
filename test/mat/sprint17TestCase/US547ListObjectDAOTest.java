/**
 * 
 */
package mat.sprint17TestCase;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import mat.dao.ListObjectDAO;
import mat.dao.SpringInitializationTest;
import mat.model.ListObject;

/**
 * @author vandavar
 * 
 *
 */
public class US547ListObjectDAOTest extends SpringInitializationTest{
	
	@Autowired
	private ListObjectDAO listObjectDAO;
	
	
	private Date currentDate = new Date();
	
	private Timestamp valueSetPackageDate; 
	
	private ListObject sampleListObject;
	
	@Before
	public void loadData() {
		List<ListObject> los = listObjectDAO.find();
		for(ListObject lo : los){
			if(lo.getLastModified() != null){
				sampleListObject = lo;
			}
		}
		valueSetPackageDate = new Timestamp(currentDate.getTime());
	}
    
	/*@Test
	public void testToFindMostRecentValueSet(){
		if(sampleListObject != null){
			ListObject mostRecentVS =  listObjectDAO.findMostRecentValueSet(sampleListObject, valueSetPackageDate);
			if(mostRecentVS != null){
				System.out.println("The most recent value set from the given sample ListObject family is:-");
				System.out.println(mostRecentVS.getName());
			}else{
				 System.out.println("Something is null");
			}
		}else{
			System.out.println("There exists only old data with no last modified date");
		}
	}*/
}
