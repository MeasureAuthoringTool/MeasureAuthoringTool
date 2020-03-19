FROM tomcat:9

COPY target/MeasureAuthoringTool.war /usr/local/tomcat/webapps/sbx#MeasureAuthoringTool.war
COPY context.xml /usr/local/tomcat/conf/

#COPY entrypoint.sh /

#ENTRYPOINT ["/entrypoint.sh"]
#CMD ["catalina.sh", "run"]

