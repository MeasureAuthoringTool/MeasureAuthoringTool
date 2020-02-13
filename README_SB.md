# Measure Authoring Tool (MAT)

## Installation
The Measure Authoring Tool (MAT) was designed using many open source products including, the Google Web Toolkit (GWT) 
framework, Java JDK, MySQL, and Eclipse. GWT allows a developer to write client side code in Java and GWT converts 
it to JavaScript. The MAT uses MySQL as its backend database server, and the IDE is Eclipse/Intellij for Java EE Developers.

Due to the use of these open source products, a working knowledge of Java development and some research into how 
the products work with each other in your environment may be necessary. Research from discussions on product forums, 
help documents, internet searches and knowledge of the local environment where the MAT will be running may all need to 
be checked if there are errors during the install.

### Configure Java (JDK)
1.	The application has not been tested with version above Java 1.8; (OPEN JDK is fine)  please ensure this version is 
in the environment.
2.	Verify that `JAVA_HOME` and `PATH` system variables are pointing to the proper folder(s).
3.	For example, in a Windows environment, the `JAVA_HOME` (Environment Variables under Advanced System Settings (
should point to the Java SDK 1.8.x folder and `PATH` should point to the Java 1.8.x/bin.

### Configure Maven
1. Install and Configure Maven (3.6.X): https://maven.apache.org/install.html

### Installing Local Maven Dependencies
Three dependencies that need to be installed locally for the Measure Authoring Tool project to work. You
can use the following commands to install them. For more information on this command, check out this
[documentation](https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html).

From the root of the git MAT project issue these 3 commands

`mvn install:install-file -Dfile=lib/CQLtoELM-1.4.6.54.jar -DgroupId=mat -DartifactId=CQLtoELM -Dversion=1.4.6.54  -Dpackaging=jar`

`mvn install:install-file -Dfile=lib/vsac-1.0.jar -DgroupId=mat -DartifactId=vsac -Dversion=1.0 -Dpackaging=jar`

`mvn install:install-file -Dfile=lib/vipuserservices-test-client-1.0.jar  -DgroupId=mat -DartifactId=vipuserservices  -Dversion=1.0 -Dpackaging=jar `


After running the two commands, right click on the Measure Authoring Tool project -> Maven -> Update Project
(ensure force update is checked) -> Ok.

### Create MAT Database
1.	Install MySQL (MAT currently has been tested withj MySQL Community Version 5.7) available from [MySQL](https://dev.mysql.com/downloads/installer/)
2.	Run the MySQL community server installer for your operating system and the MySQL workbench (which comes with the download).
3.	Create a new MySQL Connection to the database.
Note: Make sure to keep track of the username, password, url, and port you used as you will need this later.
4.	From the MAT Code base, find the `mat_schema.sql` file and then execute the script in the database that was just created. <br>
    **(Note this script is from a dump and drops and create a schema called  `MAT_APP_BLANK`)**

### Tomcat installation

Download tomcat 9 - https://tomcat.apache.org/download-90.cgi

Add the Resource to the /apache-tomcat-9.X.X/conf/context.xml file. Match the userName and password to match your mysql 
configuration.

```
<Resource name="jdbc/mat_app_tomcat"
         cachingAllowed="true"
         cacheMaxSize="1000000"
         auth="Container"
         type="javax.sql.DataSource"
         factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
         testWhileIdle="true"
         testOnBorrow="true"
         testOnReturn="false"
         validationQuery="SELECT 1"
         validationInterval="30000"
         timeBetweenEvictionRunsMillis="30000"
         maxActive="100"
         minIdle="10"
         maxWait="10000"
         initialSize="10"
         removeAbandonedTimeout="60"
         removeAbandoned="true"
         logAbandoned="true"
         minEvictableIdleTimeMillis="30000"
         jmxEnabled="true"
         jdbcInterceptors="org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;
           org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer"
         username="mat"
         password="mat"
         driverClassName="com.mysql.jdbc.Driver"
         url="jdbc:mysql://localhost:3306/MAT_APP_BLANK"/>
```

Copy mysql jdbc to tomcat lib directory.
```
cp ~/.m2/repository/mysql/mysql-connector-java/5.1.6/mysql-connector-java-5.1.6.jar /apache-tomcat-9.X.X/lib
```
#### Build project with maven

 `mvn clean install` you should see BUILD SUCCESS
 
 #### IDE setup
 
 [Intellij](README_IDEA.md)
 
 [Eclipse] todo
 
 
 ### Run MAT on App Server
 Note: If MAT is to be run on an application server, the developer will need to run the build to create a .war file.
 
 1. Run `mvn clean install`
 2. After the build has run, the file is placed into a target folder under MAT as follows: `mat/target/MeasureAuthoringTool.war`
 
 ### Log in to MAT
 To login to MAT, open MySQL Workbench and run the following queries:
 1.	 `SELECT * FROM USER where USER_ID='Admin'`
 Look at the LOGIN_ID column, the value there is your UserID. The password default is ‘gargleBlaster_10’.
 2.	Navigate to the MAT log in page GUI and use the UserID and password from the previous step and log in to MAT.
 3.	Once logged in, navigate to the [Mat Account] tab and enter the Admin user details under the [Personal Information] 
 tab and the [Security Questions] tab to setup user’s security questions.
 4.	To change the password to something new, use the [Password] tab.
 5. To create users an email is sent and this must be configured to obtain user names and passwords.
 
