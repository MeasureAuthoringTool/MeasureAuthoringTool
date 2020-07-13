FROM tomcat:7

ARG STOREPASS
ARG NR_APM_KEY
ARG NR_APM_NAME

ENV CATALINA_OPTS="-Xms512M -Xmx1024M -server -XX:+UseParallelGC"

RUN curl -O https://s3.amazonaws.com/rds-downloads/rds-ca-2019-root.pem \
    && curl -O https://s3.amazonaws.com/rds-downloads/rds-ca-2019-us-east-1.pem \
    && keytool -import -keystore $JAVA_HOME/jre/lib/security/cacerts -trustcacerts -storepass ${STOREPASS} -alias "AWSrdsRootCACert" -file rds-ca-2019-root.pem --noprompt \
    && keytool -import -keystore $JAVA_HOME/jre/lib/security/cacerts -trustcacerts -storepass ${STOREPASS} -alias "AWSrdsIntCACert" -file rds-ca-2019-us-east-1.pem --noprompt

RUN curl -O https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip \
    && unzip newrelic-java.zip \
    && sed -i "s/'<%= license_key %>'/${NR_APM_KEY}/" newrelic/newrelic.yml \
    && sed -i "s/My Application/${NR_APM_NAME}/" newrelic/newrelic.yml

COPY target/MeasureAuthoringTool.war /usr/local/tomcat/webapps/
COPY context.xml /usr/local/tomcat/conf/
COPY target/MeasureAuthoringTool/WEB-INF/lib/mysql-connector-java-5.1.6.jar /usr/local/tomcat/lib/
COPY entrypoint.sh /

ENTRYPOINT ["/entrypoint.sh"]
CMD ["catalina.sh", "run"]

