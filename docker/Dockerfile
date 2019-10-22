FROM tomcat:7.0.96

ENV CATALINA_OPTS="-Xms512M -Xmx1024M -server -XX:+UseParallelGC"

ENV JAVA_OPTS="-Djava.awt.headless=true -DVSAC_DRC_URL=https://vsac.nlm.nih.gov/vsac -DSERVER_TICKET_URL=https://vsac.nlm.nih.gov/vsac/ws/Ticket -DSERVER_SINGLE_VALUESET_URL=https://vsac.nlm.nih.gov/vsac/ws/RetrieveValueSet? -DSERVER_MULTIPLE_VALUESET_URL_NEW=https://vsac.nlm.nih.gov/vsac/svs/RetrieveMultipleValueSets? -DSERVICE_URL=http://umlsks.nlm.nih.gov -DENVIRONMENT=DEV -Dlog4j.ignoreTCL=true -DPROFILE_SERVICE=https://vsac.nlm.nih.gov/vsac/profiles -DVERSION_SERVICE=https://vsac.nlm.nih.gov/vsac/oid/ -D2FA_AUTH_CLASS=mat.server.twofactorauth.DefaultOTPValidatorForUser -D2FA_AUTH_CLASS1=mat.server.twofactorauth.DefaultOTPValidatorForUser -DBONNIE_RESPONSE_TYPE=code -DBONNIE_REDIRECT_URI=https//yourredirectURI.com -DBONNIE_CLIENT_ID=1234567890 -DBONNIE_CLIENT_SECRET=1234567890 -DBONNIE_URI=https://bonnieURL.org -DALGORITHM=EncyptionAlgorithm -DPASSWORDKEY=PasswordKey -Dlog4j.configuration=log4j.properties -Djava.security.egd=file:/dev/./urandom"

COPY MeasureAuthoringTool.war /usr/local/tomcat/webapps/
COPY context.xml /usr/local/tomcat/conf/
COPY mysql-connector-java-5.1.48.jar /usr/local/tomcat/lib/


