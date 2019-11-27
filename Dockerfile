FROM tomcat:7
COPY target/MeasureAuthoringTool.war /usr/local/tomcat/webapps/MeasureAuthoringTool.war
COPY target/MeasureAuthoringTool/WEB-INF/lib/mysql-connector-java-5.1.6.jar /usr/local/tomcat/lib/
