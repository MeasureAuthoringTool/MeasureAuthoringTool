package org.ifmc.mat.dao;

public enum PropertyOperator {
	EQ /* Equal */,
	NE /* Not Equal */,
	GT /* Greater than */,
	LT /* Less than */,
	GTE /* Greater than equal to */,
	LTE /* Less than equal to */,
	LIKE /* Like for Patterns */,
	CONTAINS /*Collection check */,
	BETWEEN /* Between */,
	IN /*in list of Values*/
}