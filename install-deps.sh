#mvn install:install-file -Dfile=lib/CQLtoELM-1.4.6.54.jar -DgroupId=mat -DartifactId=CQLtoELM -Dversion=1.4.6.54 -Dpackaging=jar
#mvn install:install-file -Dfile=lib/vsac-1.0.jar -DgroupId=mat -DartifactId=vsac -Dversion=1.0 -Dpackaging=jar
mvn validate -P install-libs