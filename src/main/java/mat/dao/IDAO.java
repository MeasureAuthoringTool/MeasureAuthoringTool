package mat.dao;

import java.io.Serializable;
import java.util.List;

/**
 * The Interface IDAO.
 * 
 * @param <T>
 *            the generic type
 * @param <ID>
 *            the generic type
 */
public interface IDAO<T, ID extends Serializable> {

	/**
	 * Find Object T by ID, where id is the same type declared in the .hbm.xml
	 * file for class T.
	 * 
	 * @param id
	 *            the id
	 * @return the t
	 */
	public abstract T find(ID id) ;

	/**
	 * Save (insert or update) T.
	 * 
	 * @param entity
	 *            the entity
	 */
	public abstract void save(T entity) ;

	/**
	 * Delete all objects of T identified with id(s) in.
	 * 
	 * @param ids
	 *            the ids
	 */
	@SuppressWarnings("unchecked")
	public abstract void delete(ID...ids) ;

	/**
	 * Delete all objects of T.
	 * 
	 * @param entities
	 *            the entities
	 */
	@SuppressWarnings("unchecked")
	public abstract void delete(T...entities) ;

	/**
	 * Find all records of T.
	 * 
	 * @return the list
	 */
	public abstract List<T> find() ;


	/**
	 * Get the count of records matching the passed query.
	 * 
	 * @param query
	 *            the query
	 * @return the long
	 */
	public abstract long count(IQuery query) ;

	/**
	 * Find all records matching the passed query.
	 * 
	 * @param query
	 *            the query
	 * @return the list
	 */
	public abstract List<T> find(IQuery query) ;

	/**
	 * Find all IDs of records matching the passed query.
	 * 
	 * @param query
	 *            the query
	 * @return the list
	 */
	public abstract List<ID> findID(IQuery query) ;

	/**
	 * Find all IDs of records matching the passed query.
	 * 
	 * @param query
	 *            the query
	 * @param properties
	 *            the properties
	 * @return the list
	 */
	public abstract List<Object[]> find(IQuery query, String[] properties)
			;

}