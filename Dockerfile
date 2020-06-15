FROM tomcat:7

ENV CATALINA_OPTS="-Xms512M -Xmx1024M -server -XX:+UseParallelGC"

COPY target/MeasureAuthoringTool.war /usr/local/tomcat/webapps/
COPY context.xml /usr/local/tomcat/conf/
COPY target/MeasureAuthoringTool/WEB-INF/lib/mysql-connector-java-5.1.6.jar /usr/local/tomcat/lib/

COPY entrypoint.sh /

ENTRYPOINT ["/entrypoint.sh"]
CMD ["catalina.sh", "run"]


