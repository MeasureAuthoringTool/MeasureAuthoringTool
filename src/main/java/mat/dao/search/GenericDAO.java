package mat.dao.search;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mat.dao.IDAO;


/**
 * The Class GenericDAO.
 *
 * @param <T>  the generic type
 * @param <ID> the generic type
 */
@Transactional
@Repository
public abstract class GenericDAO<T, ID extends Serializable> implements IDAO<T, ID> {

    protected final Class<T> clazz;

    private final Class<ID> clazzId;

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public GenericDAO() {
        final Type[] parameters = ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments();

        this.clazz = (Class<T>) parameters[0];
        this.clazzId = (Class<ID>) parameters[1];
    }

    @Override
    public void save(T entity) {
        if (isEmpty(entity)) return;
        final Session session = getSessionFactory().getCurrentSession();
        session.saveOrUpdate(entity);
    }

    @SuppressWarnings("unchecked")
    public void save(T... entities) {
        if (isEmpty(entities)) return;
        Session session = null;
        Transaction transaction = null;
        try {
            session = getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            for (int i = 0; i < entities.length; i++) {
                session.save(entities[i]);
                if (i % 20 == 0) { //20, same as the JDBC batch size
                    //flush a batch of inserts and release memory:
                    session.flush();
                    session.clear();
                }
            }
            transaction.commit();
        } finally {
            rollbackUncommitted(transaction);
        }
    }

    /* (non-Javadoc)
     * @see org.ifmc.hcmf.hibernate.dao.IDAO#delete(ID)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void delete(ID... ids) {
        //if ids is empty or full of nulls, just return
        if (isEmpty((Object[]) ids)) return;

        final boolean quoted = (clazzId == String.class);
        boolean first = true;

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.length; ++i) {
            final ID id = ids[i];
            if (id != null) {
                if (!first)
                    sb.append(',');
                if (quoted)
                    sb.append('\'');
                sb.append(id);
                if (quoted)
                    sb.append('\'');
                first = false;
            }
        }

        final String query = MessageFormat.format("delete from {0} sometable where sometable.id IN ({1})", clazz.getName(), sb);

        final Session session = getSessionFactory().getCurrentSession();
        final Query<?> q = session.createQuery(query);
        q.executeUpdate();
    }

    /* (non-Javadoc)
     * @see org.ifmc.hcmf.hibernate.dao.IDAO#delete(T)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void delete(T... entities) {
        //if entities is empty or full of nulls, just return
        if (isEmpty(entities)) return;

        final Session session = getSessionFactory().getCurrentSession();
        for (final T entity : entities) {
            if (entity != null)
                session.delete(entity);
        }
    }

    /* (non-Javadoc)
     * @see org.ifmc.hcmf.hibernate.dao.IDAO#find(ID)
     */
    @Override
    public T find(ID id) {
        if (isEmpty(id)) {
            return null;
        }

        return getSessionFactory().getCurrentSession().load(clazz, id);
    }


    /* (non-Javadoc)
     * @see mat.dao.IDAO#find()
     */
    @Override
    //for look up tables
    public List<T> find() {
        final Session session = getSessionFactory().getCurrentSession();
        final CriteriaBuilder cb = session.getCriteriaBuilder();
        final CriteriaQuery<T> query = cb.createQuery(clazz);
        query.from(clazz);
        return session.createQuery(query).getResultList();
    }


    /**
     * Rollback uncommitted.
     *
     * @param transaction the transaction
     */
    protected void rollbackUncommitted(Transaction transaction) {
        if (transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) {
            transaction.rollback();
        }
    }

    /**
     * Close session.
     *
     * @param s the s
     */
    protected void closeSession(Session s) {
        if (s != null) {
            s.close();
        }
    }


    /**
     * Get the Hibernate SessionFactory for this product. The actual
     * SessionFactory should be cached somewhere, so only one is created for
     * each product. It is also recommended that the actual implementation be
     * synchronized.
     *
     * @return the session factory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Sets the session factory.
     *
     * @param f the new session factory
     */
    public void setSessionFactory(SessionFactory f) {
        sessionFactory = f;
    }

    /**
     * Close the SessionFactory for this product. The actual SessionFactory
     * should be cached somewhere, so only one is created for each product. It
     * is also recommended that the actual implementation be synchronized.
     */
    public void closeSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed())
            sessionFactory.close();
        sessionFactory = null;
    }

    /**
     * Utility method to determine if objects is empty or all elements are null
     * for optimization purposes.
     *
     * @param objects the objects
     * @return true, if is empty
     */
    protected boolean isEmpty(Object... objects) {
        if (objects.length == 0) return true;

        for (final Object o : objects) {
            if (o != null) {
                return false;
            }
        }

        return true;
    }


}