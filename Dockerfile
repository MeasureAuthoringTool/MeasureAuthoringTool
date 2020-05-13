FROM tomcat:9

RUN curl -O https://s3.amazonaws.com/rds-downloads/rds-ca-2019-root.pem
RUN curl -O https://s3.amazonaws.com/rds-downloads/rds-ca-2019-us-east-1.pem
RUN keytool -import -keystore $JAVA_HOME/lib/security/cacerts -trustcacerts -storepass changeit -alias "AWSrdsRootCACert" -file rds-ca-2019-root.pem --noprompt
RUN keytool -import -keystore $JAVA_HOME/lib/security/cacerts -trustcacerts -storepass changeit -alias "AWSrdsIntCACert" -file rds-ca-2019-us-east-1.pem --noprompt

COPY target/MeasureAuthoringTool.war /usr/local/tomcat/webapps/sbx#MeasureAuthoringTool.war
COPY context.xml /usr/local/tomcat/conf/

