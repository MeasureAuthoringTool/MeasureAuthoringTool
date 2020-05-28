package mat.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mat.dto.AuthorDTO;
import mat.dao.search.GenericDAO;
import mat.model.Author;

@Repository("authorDAO")
public class AuthorDAOImpl extends GenericDAO<Author, String> implements mat.dao.AuthorDAO {

	private static final Log logger = LogFactory.getLog(AuthorDAOImpl.class);

	public AuthorDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public List<AuthorDTO> getAllAuthors(){
		logger.info("Getting all the rows from the Author table");
		final Session session = getSessionFactory().getCurrentSession();
		final CriteriaBuilder cb = session.getCriteriaBuilder();
		final CriteriaQuery<AuthorDTO> query = cb.createQuery(AuthorDTO.class);
		final Root<Author> root = query.from(Author.class);

		query.select(cb.construct(
				AuthorDTO.class, 
				root.get("id"),
				root.get("authorName")));

		query.where(cb.notEqual(root.get("authorName"), "other"));	

		return session.createQuery(query).getResultList();

	}
}
