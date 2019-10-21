FROM centos

RUN mkdir /opt/tomcat/

WORKDIR /opt/tomcat
RUN curl -O https://archive.apache.org/dist/tomcat/tomcat-7/v7.0.96/bin/apache-tomcat-7.0.96.tar.gz
RUN tar xvfz apache*.tar.gz
RUN mv apache-tomcat-7.0.96/* /opt/tomcat/.
RUN yum -y install java
RUN java -version

ENV CATALINA_OPTS="-Xms512M -Xmx1024M -server -XX:+UseParallelGC"

ENV JAVA_OPTS="-Djava.awt.headless=true -DVSAC_DRC_URL=https://vsac.nlm.nih.gov/vsac -DSERVER_TICKET_URL=https://vsac.nlm.nih.gov/vsac/ws/Ticket -DSERVER_SINGLE_VALUESET_URL=https://vsac.nlm.nih.gov/vsac/ws/RetrieveValueSet? -DSERVER_MULTIPLE_VALUESET_URL_NEW=https://vsac.nlm.nih.gov/vsac/svs/RetrieveMultipleValueSets? -DSERVICE_URL=http://umlsks.nlm.nih.gov -DENVIRONMENT=DEV -Dlog4j.ignoreTCL=true -DPROFILE_SERVICE=https://vsac.nlm.nih.gov/vsac/profiles -DVERSION_SERVICE=https://vsac.nlm.nih.gov/vsac/oid/ -D2FA_AUTH_CLASS=mat.server.twofactorauth.DefaultOTPValidatorForUser -D2FA_AUTH_CLASS1=mat.server.twofactorauth.DefaultOTPValidatorForUser -DBONNIE_RESPONSE_TYPE=code -DBONNIE_REDIRECT_URI=https//yourredirectURI.com -DBONNIE_CLIENT_ID=1234567890 -DBONNIE_CLIENT_SECRET=1234567890 -DBONNIE_URI=https://bonnieURL.org -DALGORITHM=EncyptionAlgorithm -DPASSWORDKEY=PasswordKey -Dlog4j.configuration=log4j.properties -Djava.security.egd=file:/dev/./urandom"

ADD MeasureAuthoringTool.war /opt/tomcat/webapps/
COPY context.xml /opt/tomcat/conf/
COPY mysql-connector-java-5.1.48.jar /opt/tomcat/lib/
WORKDIR /opt/tomcat/

EXPOSE 8080

CMD ["/opt/tomcat/bin/catalina.sh", "run"]
