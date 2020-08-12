package mat.dao.impl;

import mat.model.AuditLog;
import mat.server.LoggedInUserUtil;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;


@Repository
public class AuditInterceptor extends EmptyInterceptor {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4716276255122587594L;
		
	/* (non-Javadoc)
	 * @see org.hibernate.EmptyInterceptor#onFlushDirty(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
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

	/* (non-Javadoc)
	 * @see org.hibernate.EmptyInterceptor#onSave(java.lang.Object, java.io.Serializable, java.lang.Object[], java.lang.String[], org.hibernate.type.Type[])
	 */
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

	/**
	 * Gets the value.
	 * 
	 * @param currentState
	 *            the current state
	 * @param propertyNames
	 *            the property names
	 * @param propertyToSet
	 *            the property to set
	 * @return the value
	 */
	private Object getValue(Object[] currentState, String[] propertyNames,
			String propertyToSet) {
		int index = Arrays.asList(propertyNames).indexOf(propertyToSet);
		return currentState[index];
	}
	
	/**
	 * Sets the value.
	 * 
	 * @param currentState
	 *            the current state
	 * @param propertyNames
	 *            the property names
	 * @param propertyToSet
	 *            the property to set
	 * @param value
	 *            the value
	 */
	private void setValue(Object[] currentState, String[] propertyNames,
				String propertyToSet, Object value) {
		int index = Arrays.asList(propertyNames).indexOf(propertyToSet);
		if(index >= 0) {
			currentState[index] = value;
		}
	}

}
