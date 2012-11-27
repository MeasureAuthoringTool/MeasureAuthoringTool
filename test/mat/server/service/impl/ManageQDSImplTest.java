package mat.server.service.impl;

import java.util.List;
import java.util.ListIterator;

import mat.DTO.DataTypeDTO;
import mat.client.codelist.HasListBox;
import mat.dao.CategoryDAO;
import mat.dao.CodeListDAO;
import mat.dao.ListObjectDAO;
import mat.dao.SpringInitializationTest;

import org.easymock.EasyMock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ManageQDSImplTest extends SpringInitializationTest{
	
	

	private String categoryId = "3";//Change this category id value 

	@Before
	public void setUp(){
		
	}
	
	@After
	public void teardown() {
	
	}
	
	
	@Test
	public void testForQDSDataType(){
		List<? extends HasListBox>  dataTypeList = getCodeListService().getQDSDataTypeForCategory(categoryId);
		ListIterator<? extends HasListBox> it = dataTypeList.listIterator();
		while(it.hasNext()){
			DataTypeDTO dataTypeObject =(DataTypeDTO) it.next();
			System.out.println(dataTypeObject.getDescription());
		}
	}
}
