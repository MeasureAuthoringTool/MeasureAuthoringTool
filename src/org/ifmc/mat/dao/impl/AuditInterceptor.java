package org.ifmc.mat.dao.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.ifmc.mat.model.AuditLog;
import org.ifmc.mat.server.LoggedInUserUtil;



public class AuditInterceptor extends EmptyInterceptor {
	private static final long serialVersionUID = 4716276255122587594L;
		
	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		
		if(entity instanceof AuditLog) {
			setValue(currentState, propertyNames, "updateDate", new Date());
			setValue(currentState, propertyNames, "updatedBy", LoggedInUserUtil.getLoggedInUser());
		}
		return super.onFlushDirty(entity, id, currentState, previousState,
				propertyNames, types);
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		if(entity instanceof AuditLog) {
			if(getValue(state, propertyNames, "createDate") == null) {
				setValue(state, propertyNames, "createDate", new Date());
			}
			if(getValue(state, propertyNames, "createdBy") == null) {
				setValue(state, propertyNames, "createdBy", 
						LoggedInUserUtil.getLoggedInUser());
			}
			return true;
		}
		return super.onSave(entity, id, state, propertyNames, types);
	}

	private Object getValue(Object[] currentState, String[] propertyNames,
			String propertyToSet) {
		int index = Arrays.asList(propertyNames).indexOf(propertyToSet);
		return currentState[index];
	}
	private void setValue(Object[] currentState, String[] propertyNames,
				String propertyToSet, Object value) {
		int index = Arrays.asList(propertyNames).indexOf(propertyToSet);
		if(index >= 0) {
			currentState[index] = value;
		}
	}

}
