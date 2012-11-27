package org.ifmc.mat.dao;

import java.util.HashMap;
import java.util.List;

import mat.dao.PropertyOperator;
import mat.dao.search.CriteriaQuery;
import mat.dao.search.SearchCriteria;
import mat.model.Status;
import mat.model.User;

import org.junit.Test;

public class DAOSearchTestHelper{
	
	public SearchCriteria getSearchCriteriaForTestPassword() {
		
		SearchCriteria sc1 = new SearchCriteria();
		sc1.setPropertyName("up.userId");
		sc1.setPropertyValue("Admin");
		sc1.setOperator(PropertyOperator.EQ);	
		HashMap<String, String> alias = new HashMap<String, String>();
		alias.put("password", "up");
		sc1.setAlias(alias);
		
		return sc1;	
	}
}
