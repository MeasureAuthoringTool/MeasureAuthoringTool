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
#### Windows/linux
1. Install and Configure Maven (3.6.3+): https://maven.apache.org/install.html
#### OSX 
`brew install maven` 
or if already installed 
`brew update maven`

### Installing Local Maven Dependencies
Run `./install-deps.sh` to install dependencies not in a maven repo. This script copies dependencies from lib/ into
your local maven repo.

After running the two commands, right click on the Measure Authoring Tool project -> Maven -> Update Project
(ensure force update is checked) -> Ok.

### Create MAT Database
1.	Install MySQL (MAT currently has been tested with MySQL Community Version 8.0+) available from [MySQL] (https://dev.mysql.com/downloads/mysql/)
2.	Run the MySQL community server installer for your operating system and the MySQL workbench (which comes with the download).
3.  For Mac:
  -  go to System Preferences/ MY SQL after installing.
  -  Click Initialize Database.
  -  Enter a passowrd and click on Use Legacy Password Encryption. (Remember the username/pwd you will need these in future steps.)
  -  Start the MYSQL Db.
  - In MeasureAuthoringTool/pom.xml I had to change the driver to match the community version I downloaded.
          <dependency>
              <groupId>mysql</groupId>
              <artifactId>mysql-connector-java</artifactId>
              <version>8.0.19</version>
          </dependency>
    I am always careful not to commit this change.  
3.	Create a new MySQL Connection to the database. (I use jetbrains datagrip https://www.jetbrains.com/datagrip/)
4.	From the MAT Code base, find the `scripts/Dump*.sql` file and then execute the script in the database that was just created. <br>
    **(Note this script is from a dump and drops and create a schema called  `MAT_APP_BLANK`)**

### Tomcat installation

Download tomcat 9 - https://tomcat.apache.org/download-90.cgi
- After installing in OSX i did a `sudo chmod -R 777` on the tomcat directory.

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
 
 ### Configuring VM Arguments
 
 VM arguments are used to pass environment specific parameters to the application. These parameters include the VSAC URL; Bonnie Client ID, Client Secret, and Redirect URL; Encryption Algorithms; and logging settings.
 
 [Run] -> [Run Configurations] -> Select your MAT project on the Left Hand Side and on the [Arguments] tab -> add the following information below into the VM arguments box.
 ```
 -Xmx1G 
 -DVSAC_DRC_URL=https://vsac.nlm.nih.gov/vsac
 -DSERVER_TICKET_URL=https://vsac.nlm.nih.gov/vsac/ws/Ticket
 -DSERVER_SINGLE_VALUESET_URL=https://vsac.nlm.nih.gov/vsac/ws/RetrieveValueSet? 
 -DSERVER_MULTIPLE_VALUESET_URL_NEW=https://vsac.nlm.nih.gov/vsac/svs/RetrieveMultipleValueSets? 
 -DSERVICE_URL=http://umlsks.nlm.nih.gov 
 -DENVIRONMENT=DEV 
 -Dlog4j.ignoreTCL=true 
 -DPROFILE_SERVICE=https://vsac.nlm.nih.gov/vsac/profiles 
 -DVERSION_SERVICE=https://vsac.nlm.nih.gov/vsac/oid/ 
 -D2FA_AUTH_CLASS=mat.server.twofactorauth.DefaultOTPValidatorForUser 
 -D2FA_AUTH_CLASS1=mat.server.twofactorauth.DefaultOTPValidatorForUser 
 -DBONNIE_RESPONSE_TYPE=code 
 -DBONNIE_REDIRECT_URI=https//yourredirectURI.com 
 -DBONNIE_CLIENT_ID=1234567890 
 -DBONNIE_CLIENT_SECRET=1234567890
 -DBONNIE_URI=https://bonnieURL.org 
 -DALGORITHM=EncyptionAlgorithm 
 -DPASSWORDKEY=PasswordKey 
 -DFHIR_ORCH_MEASURE_SRVC_URL=http://localhost:9080/orchestration/measure
 ```
 
 ### Run MAT on App Server
 Note: If MAT is to be run on an application server, the developer will need to run the build to create a .war file.
 
 1. Run `mvn clean install`
 2. After the build has run, the file is placed into a target folder under MAT as follows: `mat/target/MeasureAuthoringTool.war`
 
 ### Log in to MAT
 To login to MAT, open MySQL Workbench and run the following queries:
 1.	 `SELECT * FROM USER where USER_ID='Admin'`
 Look at the LOGIN_ID column, the value there is your UserID. The password default is ‘gargleBlaster_10’. Enter any three digit code for security code.
 2.	Navigate to the MAT log in page GUI and use the UserID and password from the previous step and log in to MAT.
 3.	Once logged in, navigate to the [Mat Account] tab and enter the Admin user details under the [Personal Information] 
 tab and the [Security Questions] tab to setup user’s security questions.
 4.	To change the password to something new, use the [Password] tab.
 5. To create users an email is sent and this must be configured to obtain user names and passwords.
 
 ### Important Security Setup With Git Secrets (https://github.com/awslabs/git-secrets)
 
 1. Use brew to install git secrets
     - `brew install git-secrets`
 2. Clone this repository (you can skip this if you've already cloned it from previous steps)
     - Note: You may have to reinitialize these hooks each time you clone a new copy of the repo
 3. Follow the instructions for setting up the pre-commit hooks (from https://github.com/awslabs/git-secrets):
 
 ```
 cd /path/to/bonnie
 git secrets --install
 git secrets --register-aws
 ```
 
 4. Done! Now each commit should be automatically scanned for accidental AWS secret leaks.
 

