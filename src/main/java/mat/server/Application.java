package mat.server;

import java.util.Arrays;
import java.util.Properties;

import javax.sql.DataSource;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import liquibase.integration.spring.SpringLiquibase;
import mat.dao.impl.AuditEventListener;
import mat.dao.impl.AuditInterceptor;
import mat.server.twofactorauth.OTPValidatorInterfaceForUser;

@Configuration
@ComponentScan({"mat.model", "mat.dao", "mat.dao.impl", "mat.model.clause", "mat.server", "mat.hibernate"})
@PropertySource("classpath:MAT.properties")
@EnableTransactionManagement
@EnableConfigurationProperties
@EnableWebSecurity
@EnableCaching
@EnableScheduling
@EnableJpaRepositories
public class Application extends WebSecurityConfigurerAdapter {

    @Value("${ALGORITHM:}")
    private String algorithm;

    @Value("${PASSWORDKEY:}")
    private String passwordKey;

    @Bean
    public DataSource dataSource(@Value("${spring.datasource.jndi-name}") String jndiDataSource) {
        final JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
        return dataSourceLookup.getDataSource(jndiDataSource);
    }

    @Bean
    public StandardPBEStringEncryptor getStandardEncryptor() {
        final StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        standardPBEStringEncryptor.setAlgorithm(algorithm);
        standardPBEStringEncryptor.setPassword(passwordKey);
        return standardPBEStringEncryptor;
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

    @ConfigurationProperties(prefix = "spring.jpa.properties")
    @Bean(name = "hibernateProperties")
    public Properties hibernateProperties() {
        return new Properties();
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
        final PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        final Properties propertiesSource = new Properties();
        propertiesSource.setProperty("systemPropertiesMode", "2");
        ppc.setProperties(propertiesSource);
        return ppc;
    }

    @Bean
    public SpringLiquibase liquibase(@Autowired DataSource dataSource, @Value("${liquibase.shouldRun:true}") boolean shouldRun) {
        final SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setDataSource(dataSource);
        springLiquibase.setChangeLog("classpath:/liquibase/changelog.xml");
        springLiquibase.setContexts("prod");
        springLiquibase.setShouldRun(shouldRun);
        return springLiquibase;
    }

    @Bean
    public OTPValidatorInterfaceForUser matOtpValidator(@Value("${2FA_AUTH_CLASS}") String faAuthClass) throws ReflectiveOperationException {
        Class<?> serviceClass = Class.forName(faAuthClass);
        OTPValidatorInterfaceForUser service = OTPValidatorInterfaceForUser.class.cast(serviceClass.newInstance());
        return service;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
            .authorizeRequests()
                .antMatchers("/", "/Login.html").permitAll()
                .antMatchers("/Mat.html").authenticated()
                .antMatchers("/Bonnie.html").authenticated()
                .antMatchers("/mat/**").authenticated()
                .and()
            .formLogin()
                .loginPage("/Login.html")
                .defaultSuccessUrl("/Mat.html")
                .and()
            .formLogin()
                .loginPage("/Login.html")
                .defaultSuccessUrl("/Bonnie.html")
                .and()
            .logout()
                .permitAll()
                .and()
            .sessionManagement()
                .invalidSessionUrl("/Login.html")
                .maximumSessions(1);
    }

    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("dude")
                        .password("blahblah")
                        .roles("SUPERVISOR", "USER", "TELLER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }


    @Bean
    public CacheManager cacheManager() {
        final SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("featureFlags")));
        return cacheManager;
    }

    @Scheduled(fixedRateString = "${mat.cache.expiry.time}")
    public void clearCacheSchedule(){
        cacheManager().getCache("featureFlags").clear();
    }
}