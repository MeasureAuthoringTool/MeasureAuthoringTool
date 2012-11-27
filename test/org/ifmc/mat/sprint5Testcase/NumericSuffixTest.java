package org.ifmc.mat.sprint5Testcase;


import junit.framework.AssertionFailedError;

import mat.dao.impl.clause.*;

import org.junit.Test;

public class NumericSuffixTest {

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
				"3",
				"A",
				"A1",
				"A2",
				"A10",
				"a",
				"asfdsdf3",
				"B1",
				"Bat1",
				"Bat2",
				"bat1",
				"bat2",
				"ndfdsfdfi",
				"zz",
				"zz9",
				"  zz10  ",
				"zz10001",
				"    zz10002    "};
		
		/* Rules:
		 * Empty numeric suffix is zero.
		 * Weaved case order: A,a,B,b,C,c,D,d,E,e ...
		 * Empty prefix is min.
		 * 
		 * 
		 * 
		 * 
		 */
		
		int i,j;
		NumericSuffix suff = new NumericSuffix();
		for(i=0;i<testCases.length;i++){
			for(j=0;j<testCases.length;j++){
				int ret = suff.compare(testCases[i],testCases[j]);
				if(pin(ret )!= pin(i-j)){
					throw new AssertionFailedError("Fail: compare("+
							testCases[i]+";"+testCases[j]+")="
							+suff.compare(testCases[i],testCases[j]));
				}
			}
			
		}
		return;
	}
}

