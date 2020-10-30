#!/bin/bash
set -e

# clean webapps out
rm -rf /usr/local/tomcat/webapps/ROOT
rm -rf /usr/local/tomcat/webapps/docs
rm -rf /usr/local/tomcat/webapps/examples
rm -rf /usr/local/tomcat/webapps/host-manager
rm -rf /usr/local/tomcat/webapps/manager

exec "$@"

