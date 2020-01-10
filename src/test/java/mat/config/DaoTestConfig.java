package mat.config;

import mat.dao.impl.AuditInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan({"mat.dao"})
@PropertySource("classpath:mat-test.properties")
@EnableTransactionManagement
public class DaoTestConfig {

    @Value("${datasource.driver-class-name}")
    private String driverClass;

    @Value("${datasource.url}")
    private String dbUrl;

    @Value("${datasource.username}")
    private String dbUser;

    @Value("${datasource.password}")
    private String dbPassword;

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(driverClass);
        ds.setUrl(dbUrl);
        ds.setUsername(dbUser);
        ds.setPassword(dbPassword);
        return ds;
    }

    @Bean
    public HibernateTransactionManager txManager(@Autowired LocalSessionFactoryBean sessionFactory) {
        final HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory.getObject());
        return txManager;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(@Autowired DataSource dataSource, @Autowired AuditInterceptor auditInterceptor) {
        final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("mat.model", "mat.server.model");
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setEntityInterceptor(auditInterceptor);
        return sessionFactory;
    }

    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        hibernateProperties.setProperty("entityInterceptor", "mat.dao.impl.AuditInterceptor");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create");
        return hibernateProperties;
    }
}
