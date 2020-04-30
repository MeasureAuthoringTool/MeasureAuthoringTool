package mat.server;

import liquibase.integration.spring.SpringLiquibase;
import mat.client.login.service.HarpService;
import mat.dao.impl.AuditEventListener;
import mat.dao.impl.AuditInterceptor;
import mat.server.twofactorauth.OTPValidatorInterfaceForUser;
import mat.server.util.MATPropertiesService;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.sql.DataSource;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Configuration
@PropertySource("classpath:MAT.properties")
@EnableTransactionManagement
@EnableConfigurationProperties
@EnableWebSecurity
@EnableCaching
@EnableScheduling
@EnableJpaRepositories
@Service
public class Application extends WebSecurityConfigurerAdapter {

    @Value("${ALGORITHM:}")
    private String algorithm;

    @Value("${PASSWORDKEY:}")
    private String passwordKey;

    @Bean
    public HarpService harpService() {
        return new HarpServiceImpl();
    }

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
        PreAuthnHarpFilter filter = new PreAuthnHarpFilter();
        filter.setAuthenticationManager(authentication -> {
            if (harpService().validateToken(authentication.getPrincipal().toString())) {
                authentication.setAuthenticated(true);
            } else {
                throw new PreAuthenticatedCredentialsNotFoundException("test");
            }
            return authentication;
        });

        http.csrf().disable();
        http
                .addFilter(filter)
                .authorizeRequests()
                .antMatchers("/", "/Login.html", "HarpLogin.html", "/harpLogin", "/HarpSupport.html").permitAll()
                .antMatchers("/Mat.html").authenticated()
                .antMatchers("/Bonnie.html").authenticated()
                .antMatchers("/mat/**").authenticated()
                .and()
                .logout()
                .permitAll()
                .and()
                .sessionManagement().invalidSessionUrl("/HarpLogin.html").maximumSessions(1);

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
    public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient
                = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setSSLSocketFactory(csf)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory
                = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        return new RestTemplate(requestFactory);
    }


    @Bean
    public CacheManager cacheManager() {
        final SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<Cache> caches = Arrays.asList(
                "ConversionResultDto.validate",
                "featureFlags",
                "isFhirEditEnabled",
                "fhirAttributesAndDataTypes")
                .stream().map(ConcurrentMapCache::new)
                .collect(Collectors.toList());
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Bean
    public MATPropertiesService matPropertiesService() {
        return MATPropertiesService.get();
    }

    @Scheduled(fixedRateString = "${mat.cache.expiry.time}")
    public void clearCacheSchedule() {
        cacheManager().getCacheNames().stream().forEach(cacheName ->
                cacheManager().getCache(cacheName).clear()
        );
    }
}