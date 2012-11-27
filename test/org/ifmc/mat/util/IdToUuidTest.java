package org.ifmc.mat.util;

import org.ifmc.mat.server.util.UuidUtility;
import org.junit.Test;

import junit.framework.TestCase;

public class IdToUuidTest extends TestCase {

	@Test
	public void testIdToUuid(){
		String expected = "81DAAD84-4CD8-4952-9B87-EC4168EEA504";
		String id = "81DAAD844CD849529B87EC4168EEA504";
		
		String actual = UuidUtility.idToUuid(id);
		System.out.println(actual);
		assertEquals(expected, actual);
	}
	
}
