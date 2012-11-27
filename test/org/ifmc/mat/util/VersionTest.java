package org.ifmc.mat.util;

import mat.simplexml.model.Version;

import org.junit.Test;

import junit.framework.TestCase;

public class VersionTest extends TestCase{

	@Test
	public void testSetVersionTtext(){
		
		String expected = "123";
		String in = "123.456";
		Version v = new Version();
		v.setTtext(in);
		String actual = v.getTtext();
		System.out.println(actual);
		assertEquals(expected, actual);
		
		expected = "1";
		in = "1";
		v.setTtext(in);
		actual = v.getTtext();
		System.out.println(actual);
		assertEquals(expected, actual);
		
		expected = "2";
		in = "2.0";
		v.setTtext(in);
		actual = v.getTtext();
		System.out.println(actual);
		assertEquals(expected, actual);
		
		expected = "3";
		in = "3.";
		v.setTtext(in);
		actual = v.getTtext();
		System.out.println(actual);
		assertEquals(expected, actual);
		
	}
}
