package org.ifmc.mat.dao.search;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.ifmc.mat.dao.IDAO;
import org.ifmc.mat.dao.IQuery;
import org.springframework.transaction.annotation.Transactional;



@Transactional
public abstract class GenericDAO <T, ID extends Serializable> implements IDAO<T, ID> {
	private Class<T> clazz; 
	private Class<ID> clazzId; 
	private SessionFactory sessionFactory = null;
	
	public GenericDAO() {
		Type[] parameters = ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments();
		
		this.clazz = (Class<T>)parameters[0];
		this.clazzId = (Class<ID>)parameters[1];
	}
	
	/* (non-Javadoc)
	 * @see org.ifmc.hcmf.hibernate.dao.IDAO#save(T)
	 */
	public void save(T entity) {
		if(isEmpty(entity)) return;
		
		Session session = getSessionFactory().getCurrentSession();
		session.saveOrUpdate(entity);
	}

	public void save(T...entities){
		if(isEmpty(entities)) return;
		Session session = null;
		Transaction transaction = null;
		try {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			for ( int i=0; i<entities.length; i++ ) {
			    session.save(entities[i]);
			    if ( i % 20 == 0 ) { //20, same as the JDBC batch size
			        //flush a batch of inserts and release memory:
			        session.flush();
			        session.clear();
			    }
			}
			transaction.commit();
		}
		finally {
	    	rollbackUncommitted(transaction);
	    	closeSession(session);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.ifmc.hcmf.hibernate.dao.IDAO#delete(ID)
	 */
	public void delete(ID...ids)  {
		//if ids is empty or full of nulls, just return
		if(isEmpty((Object[])ids)) return;

		boolean quoted = (clazzId == String.class);
		boolean first = true;
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < ids.length; ++ i) {
			ID id = ids[i];
			if(id != null) {
				if(!first)
					sb.append(',');
				if(quoted)
					sb.append('\'');
				sb.append(id);
				if(quoted)
					sb.append('\'');
				first = false;
			}
		}

		String query = MessageFormat.format("delete from {0} sometable where sometable.id IN ({1})", clazz.getName(), sb.toString());

		Session session = getSessionFactory().getCurrentSession();
		org.hibernate.Query q = session.createQuery(query);
		q.executeUpdate();
	}

	/* (non-Javadoc)
	 * @see org.ifmc.hcmf.hibernate.dao.IDAO#delete(T)
	 */
	public void delete(T...entities) {
		//if entities is empty or full of nulls, just return
		if(isEmpty(entities)) return;
		
		Session session = getSessionFactory().getCurrentSession();
		for(T entity : entities) {
			if(entity != null)
				session.delete(entity);
		}
	}
			
	
	@Override
	public long count(IQuery query) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = query.buildCriteria(session, clazz);
		
		// We only wish to get the # of records matching this query
		return (Long)criteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	/* (non-Javadoc)
	 * @see org.ifmc.hcmf.hibernate.dao.IDAO#find(ID)
	 */
	public T find(ID id)  {
		if(isEmpty(id)) return null;
		
		Session session = getSessionFactory().getCurrentSession();
		T t = (T)session.load(clazz, id);
		return t;
	}
	
	
	@Override
	//for look up tables
	public List<T> find() {
		List<T>	list = null;
		
		Session	session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(clazz);				
		// obtain the actual records
        list = criteria.list();
		return list;
	}	
	
	
	
	@Override
	public List<T> find(IQuery query) {
		List<T>	list = null;
		
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = query.buildCriteria(session, clazz);
		// obtain the actual records
        list = criteria.list();
		return list;
	}

	@Override
	public List<ID> findID(IQuery query) {
		List<ID> list = null;
		
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = query.buildCriteria(session, clazz);

		criteria.setProjection(Projections.id());
		// obtain the actual records
        list = criteria.list();
		return list;
	}

	@Override
	public List<Object[]> find(IQuery query, String[] properties) {
		List<Object[]> list = null;
		
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = query.buildCriteria(session, clazz);

		ProjectionList projList = Projections.projectionList();
		for(String property : properties) {
	        projList.add(Projections.property(property));
		}
		criteria.setProjection(projList);
		// obtain the actual records
        list = criteria.list();
		
		return list;
	}

	protected void rollbackUncommitted(Transaction transaction) {
	    if (transaction != null && !transaction.wasCommitted()) {
	    	transaction.rollback();
	    }
	}
	
	protected void closeSession(Session s) {
		if(s != null) {
			s.close();
		}
	}
	
	
	
	/**
	 * Get the Hibernate SessionFactory for this product.
	 * The actual SessionFactory should be cached somewhere, so only one is created for each product.
	 * It is also recommended that the actual implementation be synchronized. 
	 * @return
	 * @throws HibernateException
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory f) {
		sessionFactory = f;
	}
	/**
	 * Close the SessionFactory for this product.
	 * The actual SessionFactory should be cached somewhere, so only one is created for each product.
	 * It is also recommended that the actual implementation be synchronized. 
	 * @throws HibernateException
	 */
	public void closeSessionFactory() {
		if(sessionFactory != null && !sessionFactory.isClosed())
			sessionFactory.close();
		sessionFactory = null;
	}
	
	/**
	 * Utility method to determine if objects is empty or all elements are null for optimization purposes
	 * @param objects
	 * @return
	 */
	protected boolean isEmpty(Object...objects) {
		if(objects.length == 0) return true;
		
		for(Object o : objects) {
			if(o != null) {
				return false;
			}
		}

		return true;
	}
	
	
}