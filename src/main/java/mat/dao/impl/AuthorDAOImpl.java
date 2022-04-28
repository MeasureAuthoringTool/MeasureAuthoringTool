package mat.dao.impl;

import mat.dao.search.GenericDAO;
import mat.dto.AuthorDTO;
import mat.model.Author;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("authorDAO")
public class AuthorDAOImpl extends GenericDAO<Author, String> implements mat.dao.AuthorDAO {

	private static final Logger logger = LoggerFactory.getLogger(AuthorDAOImpl.class);

	public AuthorDAOImpl(@Autowired SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public List<AuthorDTO> getAllAuthors(){
		logger.debug("Getting all the rows from the Author table");
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
