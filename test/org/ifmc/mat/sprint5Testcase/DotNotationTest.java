package org.ifmc.mat.sprint5Testcase;

//import java.util.SortedSet;

import junit.framework.AssertionFailedError;

import org.ifmc.mat.server.service.impl.DotCompare;
import org.junit.Test;

//import java.lang.reflect.Field;

public class DotNotationTest {

	
	
	int pin(int x){
		if(x<0)
			return -1;
		if(x>0)
			return 1;
		return 0;
	}
	

	
	
	@Test
	public void test() {
		
		String[] testCases ={
				"",
				"1.01.1.1",
				"1.1.1.1",
				"1.02.3.4",
				"1.2.3.4",
				"1.14.5.6",
				"2.3.4.5",
				"2.3.4.5.0",
				"11.1.2.3.4",
				"11.1.2.3.30",
				"a",
				"asfdsdf3",
				"ndfdsfdfi",
				"zz","zz10001","zz9"};
		int i,j;
		DotCompare dc = new DotCompare();
		for(i=0;i<testCases.length;i++){
			for(j=0;j<testCases.length;j++){
				int ret = dc.dotNotationCompare(testCases[i],testCases[j]);
				if(pin(ret)!= pin(i-j)){
					throw new AssertionFailedError("Fail: compare("+
							testCases[i]+"; "+testCases[j]+")="
							+dc.dotNotationCompare(testCases[i],testCases[j]));
				}
			}
			
		}
		return;
		//if(dc.dotNotationCompare("1.2.", "1.2") <= 0){
		//	ret =dc.dotNotationCompare("1.2.", "1.2");
		//	throw new AssertionFailedError("Eats trailing dot: " + ret);
		//}
	}
	
}
