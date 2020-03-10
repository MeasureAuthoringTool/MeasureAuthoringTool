package mat.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import mat.server.HibernateStatisticsFilter;

@EnableTransactionManagement
public class HibernateConf{

	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {

		return buildSessionFactory();	
	}

	private static SessionFactory buildSessionFactory() {
		if (sessionFactory == null) {
			try {				
				sessionFactory = HibernateStatisticsFilter.getContext().getBean(SessionFactory.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sessionFactory;
	}

	public static Session getHibernateSession() {

		return HibernateConf.getSessionFactory().getCurrentSession();		
	}

	public static Session createHibernateSession() {

		SessionFactory sessionFactory = HibernateConf.getSessionFactory();
		Session session = sessionFactory.openSession();

		return session;		
	}

}
