package mat.server;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import liquibase.integration.spring.SpringLiquibase;
import mat.dao.impl.AuditEventListener;
import mat.dao.impl.AuditInterceptor;

@Configuration
@ComponentScan({"mat.model","mat.dao","mat.dao.impl","mat.model.clause","mat.server","mat.hibernate"})
@PropertySource("classpath:MAT.properties")
@EnableTransactionManagement
public class Application {
	
	private static final String algorithm = System.getProperty("ALGORITHM");
	
	private static final String passwordKey = System.getProperty("PASSWORDKEY");
	
	/* This is only for developers
	 *
	@Bean
	public DataSource dataSource(){
	  BasicDataSource dataSource = new BasicDataSource();
	  dataSource.setUrl("jdbc:mysql://***REMOVED***:3306/MAT_STAGE_08312017");
	  dataSource.setUsername("userId");
	  dataSource.setPassword("password");
	  return dataSource;
	}*/
	
	@Bean
	public DataSource dataSource(){
	  JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
	  DataSource dataSource = dataSourceLookup.getDataSource("java:/comp/env/jdbc/mat_app_tomcat");
	  return dataSource;
	}
	
	
	
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
    public LocalSessionFactoryBean sessionFactory(@Autowired DataSource dataSource,@Autowired AuditInterceptor auditInterceptor) throws IOException {
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
	    hibernateProperties.setProperty("hibernate.show_sql","true");
	    hibernateProperties.setProperty("hibernate.default_batch_fetch_size","20");
	    hibernateProperties.setProperty("hibernate.connection.release_mode","auto");
	    hibernateProperties.setProperty("hibernate.cache.use_second_level_cache","true");
	    hibernateProperties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQL5Dialect");	
	    hibernateProperties.setProperty("entityInterceptor", "mat.dao.impl.AuditInterceptor");
	    return hibernateProperties;
	}
	
	@Bean
	public AuditEventListener auditEventListener() {
		return new AuditEventListener();
	}
	
	@Bean
	public AuditInterceptor auditInterceptor() {
		return new AuditInterceptor();
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
		Properties propertiesSource = new Properties();
		propertiesSource.setProperty("systemPropertiesMode", "2");
		ppc.setProperties(propertiesSource);
		return ppc;
	}
	
	@Bean
	public SpringLiquibase liquibase(@Autowired DataSource dataSource) {
		SpringLiquibase springLiquibase=new SpringLiquibase();
		springLiquibase.setDataSource(dataSource);
		springLiquibase.setChangeLog("classpath:/liquibase/changelog.xml");
		springLiquibase.setContexts("prod");
		return springLiquibase;
	}
}
