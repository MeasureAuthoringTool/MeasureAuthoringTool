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
	T find(ID id) ;

	/**
	 * Save (insert or update) T.
	 * 
	 * @param entity
	 *            the entity
	 */
	void save(T entity) ;

	/**
	 * Delete all objects of T identified with id(s) in.
	 * 
	 * @param ids
	 *            the ids
	 */
	@SuppressWarnings("unchecked") void delete(ID...ids) ;

	/**
	 * Delete all objects of T.
	 * 
	 * @param entities
	 *            the entities
	 */
	@SuppressWarnings("unchecked") void delete(T...entities) ;

	/**
	 * Find all records of T.
	 * 
	 * @return the list
	 */
	List<T> find() ;

}