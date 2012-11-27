package org.ifmc.mat.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;

public interface IDAO<T, ID extends Serializable> {

	/**
	 * Find Object T by ID, where id is the same type declared in the .hbm.xml file for class T.
	 * @param id
	 * @return
	 * @throws HibernateException
	 */
	public abstract T find(ID id) ;

	/**
	 * Save (insert or update) T.
	 * @param entity
	 * @throws HibernateException
	 */
	public abstract void save(T entity) ;

	/**
	 * Delete all objects of T identified with id(s) in 
	 * @param ids
	 * @throws HibernateException
	 */
	public abstract void delete(ID...ids) ;

	/**
	 * Delete all objects of T.
	 * @param entities
	 * @throws HibernateException
	 */
	public abstract void delete(T...entities) ;

	/**
	 * Find all records of T.
	 * @return
	 * @throws HibernateException
	 */
	public abstract List<T> find() ;


	/**
	 * Get the count of records matching the passed query.
	 * @param query
	 * @return
	 * @throws HibernateException
	 */
	public abstract long count(IQuery query) ;

	/**
	 * Find all records matching the passed query.
	 * @param query
	 * @return
	 * @throws HibernateException
	 */
	public abstract List<T> find(IQuery query) ;

	/**
	 * Find all IDs of records matching the passed query.
	 * @param query
	 * @return
	 * @throws HibernateException
	 */
	public abstract List<ID> findID(IQuery query) ;

	/**
	 * Find all IDs of records matching the passed query.
	 * @param query
	 * @return
	 * @throws HibernateException
	 */
	public abstract List<Object[]> find(IQuery query, String[] properties)
			;

}