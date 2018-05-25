package mat.hibernate;

import javax.annotation.PostConstruct;

import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mat.dao.impl.AuditEventListener;

@Component
public class HibernateListenerRegistrar {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@PostConstruct
	protected void init() {

		final EventListenerRegistry eventListenerRegistry =  ((SessionFactoryImplementor) sessionFactory).
				getServiceRegistry().getService(EventListenerRegistry.class);
		
		AuditEventListener auditEventListener = new AuditEventListener();
		eventListenerRegistry.prependListeners(EventType.PRE_INSERT, auditEventListener);
		eventListenerRegistry.prependListeners(EventType.PRE_UPDATE, auditEventListener);
		eventListenerRegistry.prependListeners(EventType.PRE_DELETE, auditEventListener);

	}
}
