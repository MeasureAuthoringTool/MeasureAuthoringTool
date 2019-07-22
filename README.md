# Measure Authoring Tool

## Installation
The Measure Authoring Tool (MAT) was designed using many open source products including the Google Web Toolkit (GWT) framework, Java JDK, MySQL and Eclipse. GWT allows a developer to write client side code in Java and GWT converts it to JavaScript. The MAT uses MySQL as its backend database server and the IDE is Eclipse for Java EE Developers.

Due to the use of these open source products, a working knowledge of Java development and some research into how the products work with each other in your environment may be necessary. Research from discussions on product forums, help documents, internet searches and knowledge of the local environment where the MAT will be running may all need to be checked if there are errors during the install.

### Configure Java (JDK)
1.	The application has not been tested with version above Java 1.8; please ensure this version is in the environment.
2.	Verify that `JAVA_HOME` and `PATH` system variables are pointing to the proper folder(s).
3.	For example, in a Windows environment, the `JAVA_HOME` (Environment Variables under Advanced System Settings (should point to the Java SDK 1.8.x folder and `PATH` should point to the Java 1.8.x/bin.

### Configure Maven
1. Install and Configure Maven: https://maven.apache.org/install.html

### Install Eclipse
Download Eclipse (MAT has been tested for Oxygen) for Java EE Developers. Eclipse IDE is a free, open source IDE for writing Java applications found on the [Eclipse download page](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/oxygen1a) at  
1.	Select the version that best fits your operating system.  
2.	Extract the zip file to a location where you would like Eclipse to be installed.
3.	Run the application file.

### Install Google Web Toolkit for Eclipse (GWT)
1.	 Open Eclipse IDE, navigate to the workbench, and select [Help] -> [Eclipse Marketplace]
2.	 Search for http://marketplace.eclipse.org/content/gwt-eclipse-plugin
3.	 Install the result with the title GWT Eclipse Plugin 3.0.0

### Import MAT Code Base
1.	From the MAT GitHub source code link, extract the code base into the Eclipse workspace folder in the MAT Environment.
2.	Import the code base into an Eclipse project:
[File] -> [Import] -> [Maven] -> [Existing Maven Projects] ->
Browse to root directory and select the MAT Project -> [Finish]

### Installing Local Maven Dependencies
There are two dependencies that need to be installed locally for the Measure Authoring Tool project to work. You
can use the following commands to install them. For more information on this command, check out this
[documentation](https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html).

`mvn install:install-file -Dfile=</path/to/project/mat/lib/CQLtoELM-{version}.jar> -DgroupId=mat -
DartifactId=CQLtoELM -Dversion=<version> -Dpackaging=jar`

`mvn install:install-file -Dfile=</path/to/project/mat/lib/vsac-{version}.jar> -DgroupId=mat -DartificatId=vsac -
Dversion=<version> -Dpackaging=jar`

After running the two commands, right click on the Measure Authoring Tool project -> Maven -> Update Project
(ensure force update is checked) -> Ok.

### Create MAT Database
1.	Install MySQL (MAT currently uses MySQL Community Version 6.x) available from [MySQL](https://dev.mysql.com/downloads/installer/)
2.	Run the MySQL community server installer for your operating system and the MySQL workbench (which comes with the download).
3.	Create a new MySQL Connection to the database.
Note: Make sure to keep track of the username, password, url, and port you used as you will need this later.
4.	From the MAT Code base, find the `mat_schema.sql` file and then execute the script in the database that was just created.

### Configure Files for Development
Changes will need to be made to some of the files in the code base to allow the MAT to connect to the MySQL Database.

#### add `mat/war/WEB-INF/jetty-env.xml`
```xml
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "http://www.eclipse.org/jetty/configure.dtd">
<Configure id="Server" class="org.eclipse.jetty.webapp.WebAppContext">
<New id="MatJndi" class="org.eclipse.jetty.plus.jndi.Resource">
  <Arg></Arg>
  <Arg>java:/comp/env/jdbc/mat_app_tomcat</Arg>
  <Arg>
    <New class="com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource">
        <Set name="User">username</Set>
        <Set name="Password">password </Set>
        <Set name="DatabaseName">schemaname</Set>
        <Set name="ServerName">serveraddress</Set>
        <Set name="PortNumber">port</Set>
      </New>
    </Arg>
</New>
</Configure>
```
If you get exception as below `'com.google.gwt.dev.shell.jetty.JettyLauncher.WebAppContextWithReload'` is not of type `'org.eclipse.jetty.webapp.WebAppContext'` then you will need to find a way to load the jetty class instead of `gwt-dev.jar`. One way to make it work is to change the `gwt-dev` dependency in the `pom.xml` to be `provided` rather than `compile`. If you do this, then when running `mvn clean install` you will have to change it back.
```xml
<dependency>
    <groupId>com.google.gwt</groupId>
    <artifactId>gwt-dev</artifactId>
    <version>${gwtVersion}</version>
    <scope>provided</scope>
    <exclusions>
        <exclusion>
            <artifactId>commons-collections</artifactId>
            <groupId>commons-collections</groupId>
        </exclusion>
        <exclusion>
            <groupId>org.eclipse.jetty</groupId>
            <artifactId>apache-jsp</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

#### update `mat/war/WEB-INF/applicationContext-mail.xml`
Update the bean with `id="mailSender"` to use your mail server host.
```xml    
<bean id="mailSender" class="org.springframework.mail.javamail.JavaMailSenderImpl">
    <property name="host" value="mail.some-server.org"/>
</bean>
```

Update the bean with `id="templateMessage"` to use the email address you want the emails to send from

```xml
<bean id="templateMessage" class="org.springframework.mail.SimpleMailMessage">
  <property name="from" value="someaddr@someaddr.org"/>
</bean>
```

### Configuring Program Arguments
Program arguments are used for GWT to determine how to run the application.
[Run] -> [Run Configurations] -> Select your MAT project on the Left Hand Side and in the [Arguments] tab -> add the following information below into the Program arguments box.

`-logLevel INFO -port 8888 -remoteUI "${gwt_remote_ui_server_port}:${unique_id}" -codeServerPort 9997 mat.Login mat.Mat mat.Bonnie -war \war mat.Login mat.Bonnie mat.Mat`

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
-Dlog4j.configuration=PathToLog4jPropertiesFile
```

### Compiling MAT code with GWT
1.	Right click on the MAT project folder, then go to Properties -> GWT -> General Settings and select Use GWT. Make sure that the SDK is using GWT 2.8.1.
2.	In Eclipse, select GWT Pulldown (Red Google Icon Button) -> [GWT Compile Project].

In the GWT Compile Wizard:
1.	Browse to find the MAT project.
2.	Set the Log level to Debug
3.	Add the Login, Bonnie and MAT Entry Point Modules.

### Run MAT
1.	Make sure your database server is running
2.	In the eclipse IDE Select, [Run] -> [Run Configurations] -> [Web Application] -> [New]
3.	Set Main Class: “com.google.gwt.dev.DevMode”.
4.	Select [Apply] and followed by [Run].

### Run MAT on App Server
Note: If MAT is to be run on an application server, the developer will need to run the build to create a .war file.

1. Run `mvn clean install`
2. After the build has run, the file is placed into a target folder under MAT as follows: `mat/target/MeasureAuthoringTool.war`

### Log in to MAT
To login to MAT, open MySQL Workbench and run the following queries:
1.	 `SELECT * FROM USER where USER_ID='Admin'`
Look at the LOGIN_ID column, the value there is your UserID. The password default is ‘gargleBlaster_10’.
2.	Navigate to the MAT log in page GUI and use the UserID and password from the previous step and log in to MAT.
3.	Once logged in, navigate to the [Mat Account] tab and enter the Admin user details under the [Personal Information] tab and the [Security Questions] tab to setup user’s security questions.
4.	To change the password to something new, use the [Password] tab.
