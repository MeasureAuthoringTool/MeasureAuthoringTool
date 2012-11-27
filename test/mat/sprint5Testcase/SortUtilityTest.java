package mat.sprint5Testcase;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.SortedSet;

import junit.framework.AssertionFailedError;

import mat.model.ListObject;
import mat.model.QualityDataSet;
import mat.shared.comparators.SortUtility;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

public class SortUtilityTest {
	List<QualityDataSet> qdms;
	HSSFWorkbook wkbk;
	
	

	
	public void setup(){
		//setup mock up test data
		qdms = new ArrayList<QualityDataSet>();
		if(qdms.isEmpty()==false)
			throw new AssertionFailedError("who inserted something...");
		qdms.add(new QualityDataSet());
		qdms.add(new QualityDataSet());
		qdms.add(new QualityDataSet());
		qdms.get(0).setId("foo");
		qdms.get(1).setId("bar");
		qdms.get(2).setId("baz");
		qdms.get(0).setOid("0.1.2");
		qdms.get(1).setOid("2.3.4");
		qdms.get(2).setOid("1.5.6");
		// Be warned these are logically different OIDs
		// Just using this for convenience.
		qdms.get(0).setListObject(new ListObject());
		qdms.get(1).setListObject(new ListObject());
		qdms.get(2).setListObject(new ListObject());
		ListObject lo;
		lo = new ListObject();
		lo.setOid(qdms.get(0).getOid());
		qdms.get(0).setListObject(lo);
		lo = new ListObject();
		lo.setOid(qdms.get(1).getOid());
		qdms.get(1).setListObject(lo);
		lo = new ListObject();
		lo.setOid(qdms.get(2).getOid());
		qdms.get(2).setListObject(lo);		
	}
	
	private boolean isInOrder(Collection<ListObject> los){
		if(los.isEmpty())
			return true;
		String prev = "";
		
		//Get the first
		for(ListObject lo: los){
			String curr = lo.getOid();
			if(!curr.equalsIgnoreCase("")){
				prev = curr;
				break;
			}
		}
		
		for(ListObject lo: los){
			String curr = lo.getOid();
			if(curr.compareTo(prev) < 0)
				return false;
		}
		return true;
	}
	@Test
	public void testCodeListExport() {
		setup();
		SortUtility su = new SortUtility();
		SortedSet<ListObject> sortedListObjects = su.sortQDMsToListObjects(qdms);
		
		if(!isInOrder(sortedListObjects))
			throw new AssertionFailedError("not sorted");
	}
}
