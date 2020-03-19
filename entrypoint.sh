#!/bin/bash
set -e

# clean webapps out
rm -rf /usr/local/tomcat/webapps/ROOT
rm -rf /usr/local/tomcat/webapps/docs
rm -rf /usr/local/tomcat/webapps/examples
rm -rf /usr/local/tomcat/webapps/host-manager
rm -rf /usr/local/tomcat/webapps/manager

# get AWS RDS cert (root and intermediate) and import into cacert
curl -O https://s3.amazonaws.com/rds-downloads/rds-ca-2019-root.pem 
curl -O https://s3.amazonaws.com/rds-downloads/rds-ca-2019-us-east-1.pem
keytool -import -keystore $JAVA_HOME/jre/lib/security/cacerts -trustcacerts -storepass changeit -alias "AWSrdsRootCACert" -file rds-ca-2019-root.pem --noprompt
keytool -import -keystore $JAVA_HOME/jre/lib/security/cacerts -trustcacerts -storepass changeit -alias "AWSrdsIntCACert" -file rds-ca-2019-us-east-1.pem --noprompt

exec "$@"




