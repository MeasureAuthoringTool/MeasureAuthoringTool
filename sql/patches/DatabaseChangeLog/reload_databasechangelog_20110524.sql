USE MAT_APP;
DELETE FROM DATABASECHANGELOG;
INSERT INTO `DATABASECHANGELOG` (`ID`,`AUTHOR`,`FILENAME`,`DATEEXECUTED`,`ORDEREXECUTED`,`EXECTYPE`,`MD5SUM`,`DESCRIPTION`,`COMMENTS`,`TAG`,`LIQUIBASE`) VALUES 
 ('1','mat_dev_user','classpath:/liquibase/deploy_01.00.05_20110303.xml','2011-05-24 12:28:04',1,'EXECUTED','3:df5d494ac46a1e8edbe88c7e37501007','Custom SQL','',NULL,'2.0.1'),
 ('1','mat_dev_user','classpath:/liquibase/deploy_01.00.06_20110308.xml','2011-05-24 12:28:04',2,'EXECUTED','3:aa7427ca1b29b87ea94d0f36fcdfbcbb','Custom SQL','',NULL,'2.0.1'),
 ('1','mat_dev_user','classpath:/liquibase/deploy_01.00.07_20110308.xml','2011-05-24 12:28:04',3,'EXECUTED','3:db48d204c4119697ad3149a258e08267','Custom SQL','',NULL,'2.0.1'),
 ('1','mat_dev_user','classpath:/liquibase/deploy_01.00.08_20110315.xml','2011-05-24 12:28:04',4,'EXECUTED','3:4df623114c3fc40fc0e2c92e6ccc97aa','Custom SQL','',NULL,'2.0.1'),
 ('1','mat_dev_user','classpath:/liquibase/deploy_01.00.09_20110322.xml','2011-05-24 12:28:05',5,'EXECUTED','3:8814ce4c9a828e73639897b5703f95ff','Custom SQL','',NULL,'2.0.1'),
 ('1','mat_dev_user','classpath:/liquibase/deploy_01.00.10_20110414.xml','2011-05-24 12:28:05',6,'EXECUTED','3:f7918819f1e7b3c2f110d146e293caeb','Custom SQL','',NULL,'2.0.1'),
 ('1','mat_dev_user','classpath:/liquibase/deploy_01.00.11_20110426.xml','2011-05-24 12:28:05',7,'EXECUTED','3:17b7cb50c4bd1a2e30f37a341490ec54','Custom SQL','',NULL,'2.0.1'),
 ('1','mat_dev_user','classpath:/liquibase/deploy_01.00.12_20110513.xml','2011-05-24 12:28:05',8,'EXECUTED','3:6e87d921c12c3485dfa1840289abae8a','Custom SQL','',NULL,'2.0.1'),
 ('1','mat_dev_user','classpath:/liquibase/deploy_01.00.13_20110503.xml','2011-05-24 12:28:05',9,'EXECUTED','3:05b86029c7554dc683c697d528fd103d','Custom SQL','',NULL,'2.0.1');
