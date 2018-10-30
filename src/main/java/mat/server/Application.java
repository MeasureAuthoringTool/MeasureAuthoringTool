package mat.server;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import mat.dao.impl.AuditInterceptor;
import mat.hibernate.HibernateConf;

@Configuration
@ComponentScan({"mat.model","mat.dao","mat.model.clause","mat.server","mat.hibernate"})
@EnableTransactionManagement
@Import(HibernateConf.class)
public class Application {
	
	private static final String algorithm = System.getProperty("ALGORITHM");
	
	private static final String passwordKey = System.getProperty("PASSWORDKEY");
	
	@Autowired
	private AuditInterceptor auditInterceptor;
	
	@Autowired 
	private DataSource dataSource;
	
	@Bean
	public StandardPBEStringEncryptor getStandardEncryptor() {
		StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
		standardPBEStringEncryptor.setAlgorithm(algorithm);
		standardPBEStringEncryptor.setPassword(passwordKey);
		return standardPBEStringEncryptor;
	}
	
	@Bean
	public HibernateTransactionManager txManager(@Autowired LocalSessionFactoryBean sessionFactory) throws IOException {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory.getObject());
		return txManager;
	}
	
	@Bean
    public LocalSessionFactoryBean sessionFactory() throws IOException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan(new String[] {"mat.model","mat.server.model"});
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setEntityInterceptor(auditInterceptor);
        return sessionFactory;
    }

	private final Properties hibernateProperties() {
	    Properties hibernateProperties = new Properties();
	    hibernateProperties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
	    hibernateProperties.setProperty("hibernate.cache.use_query_cache","true");
	    hibernateProperties.setProperty("hibernate.cache.use_second_level_cache","true");
	    hibernateProperties.setProperty("hibernate.show_sql","false");
	    hibernateProperties.setProperty("hibernate.default_batch_fetch_size","20");
	    hibernateProperties.setProperty("hibernate.connection.release_mode","auto");
	    hibernateProperties.setProperty("hibernate.cache.use_second_level_cache","true");
	    hibernateProperties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQL5Dialect");	
	    hibernateProperties.setProperty("entityInterceptor", "mat.dao.impl.AuditInterceptor");
	    return hibernateProperties;
	}
	
	
}
