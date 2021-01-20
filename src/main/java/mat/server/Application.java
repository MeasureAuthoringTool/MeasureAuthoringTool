package mat.server;

import ca.uhn.fhir.context.FhirContext;
import liquibase.integration.spring.SpringLiquibase;
import mat.client.login.service.HarpService;
import mat.dao.impl.AuditEventListener;
import mat.dao.impl.AuditInterceptor;
import mat.server.logging.LogFactory;
import mat.server.logging.RequestResponseLoggingInterceptor;
import mat.server.logging.RequestResponseLoggingMdcInternalInterceptor;
import mat.server.twofactorauth.OTPValidatorInterfaceForUser;
import mat.server.util.MATPropertiesService;
import mat.vsac.RefreshTokenManagerImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
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
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import mat.vsac.VsacService;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.net.ssl.SSLContext;
import javax.sql.DataSource;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
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
    private static Log log = LogFactory.getLog(Application.class);

    @Value("${ALGORITHM:}")
    private String algorithm;

    @Value("${PASSWORDKEY:}")
    private String passwordKey;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        final PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        final Properties propertiesSource = new Properties();
        propertiesSource.setProperty("systemPropertiesMode", "2");
        ppc.setProperties(propertiesSource);
        return ppc;
    }

    /**
     * Force UTC timezone locally.
     */
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        log.info("Set timezone to UTC.");
    }

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
            }
            return authentication;
        });

        http.csrf().disable();
        http
                .addFilter(filter)
                .authorizeRequests()
                .antMatchers("/", "/Login.html", "/harpLogin", "/HarpSupport.html").permitAll()
                .antMatchers("/Mat.html").authenticated()
                .antMatchers("/Bonnie.html").authenticated()
                .antMatchers("/mat/**").authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/Login.html")
                .and()
                .logout()
                .permitAll()
                .and()
                .sessionManagement().invalidSessionUrl("/Login.html").maximumSessions(1);

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

    @Bean(name = "internalRestTemplate")
    @Primary
    public RestTemplate getRestTemplateInternal() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return buildRestTemplate(new RequestResponseLoggingMdcInternalInterceptor());
    }

    @Bean(name = "externalRestTemplate")
    public RestTemplate getRestTemplateExternal() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        return buildRestTemplate(new RequestResponseLoggingInterceptor());
    }


    private RestTemplate buildRestTemplate(RequestResponseLoggingInterceptor requestResponseLoggingInterceptor) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient
                = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build();
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpComponentsClientHttpRequestFactory.setHttpClient(httpClient);

        ClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(httpComponentsClientHttpRequestFactory);

        RestTemplate restTemplate = new RestTemplate(bufferingClientHttpRequestFactory);

        restTemplate
                .setInterceptors(List.of(requestResponseLoggingInterceptor));

        return restTemplate;
    }


    @Bean
    public CacheManager cacheManager() {
        final SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<Cache> caches = Arrays.asList(
                "ConversionResultDto.validate",
                "featureFlags",
                "oidToVSACDodeSystemDTO",
                "spreadSheetfhirTypes",
                "spreadSheetMatAttributes",
                "spreadSheetQdmToQiCoreMapping",
                "spreadSheetDataTypes",
                "typesForFunctionArgs",
                "fhirAssociation",
                "populationBasisValidValues",
                "spreadSheetResourceDefinitions")
                .stream().map(ConcurrentMapCache::new)
                .collect(Collectors.toList());
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Bean
    public MATPropertiesService matPropertiesService() {
        return MATPropertiesService.get();
    }

    // On the hour every hour
    @Scheduled(cron =  "${mat.cache.expiry.cron}")
    public void clearCacheSchedule() {
        //cacheManager() is actually spring magic. Another bean isn't recreated.
        //https://stackoverflow.com/questions/27990060/calling-a-bean-annotated-method-in-spring-java-configuration
        cacheManager().getCacheNames().forEach(cacheName -> {
                    cacheManager().getCache(cacheName).clear();
                    log.info("Cleared cache: " + cacheName);
                }
        );
    }

    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forR4();
    }

    @Bean
    public VsacService vsacService(@Named("externalRestTemplate") RestTemplate restTemplate) {
        String ticketBase =  System.getProperty("VSAC_TICKET_URL_BASE");
        String urlBase = System.getProperty("VSAC_URL_BASE");

        //Default for test cases.
        if (StringUtils.isEmpty(ticketBase)) {
            ticketBase = "https://utslogin.nlm.nih.gov/cas/v1";
        }
        if (StringUtils.isEmpty(urlBase)) {
            urlBase = "https://vsac.nlm.nih.gov";
        }

        return new VsacService(ticketBase,urlBase,restTemplate, RefreshTokenManagerImpl.getInstance());
    }
}