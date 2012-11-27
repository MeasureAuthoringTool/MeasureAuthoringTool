package org.ifmc.mat.util;

import junit.framework.TestCase;

import mat.client.codelist.ManageCodeListDetailModel;

import org.junit.Test;


public class CodeListRequiredFieldTest extends TestCase{
	@Test
	public void testManageCodeListDetailModelSetters(){
		ManageCodeListDetailModel model = new ManageCodeListDetailModel();
		model.setName(" ");//Test for space
		model.setSteward("	"); //Test for Tab
		model.setCategory("	"); //Test for Tab
		model.setRationale("	"); //Test for Tab
		
		assertTrue("Name was not trimmed.", "".equals(model.getName().trim()));
		assertTrue("Steward was not trimmed.", "".equals(model.getSteward().trim()));
		assertTrue("Category was not trimmed.", "".equals(model.getCategory().trim()));
		assertTrue("Rationale was not trimmed.", "".equals(model.getRationale().trim()));
		
	}

}
