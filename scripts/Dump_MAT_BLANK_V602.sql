-- MySQL dump 10.13  Distrib 8.0.21, for macos10.15 (x86_64)
--
-- Host: 127.0.0.1    Database: mat
-- ------------------------------------------------------
-- Server version	8.0.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ATTRIBUTE_DETAILS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ATTRIBUTE_DETAILS` (
                                     `ATTRIBUTE_DETAILS_ID` varchar(64) NOT NULL,
                                     `ATTR_NAME` varchar(200) NOT NULL,
                                     `CODE` varchar(200) NOT NULL,
                                     `CODE_SYSTEM` varchar(200) NOT NULL,
                                     `CODE_SYSTEM_NAME` varchar(200) NOT NULL,
                                     `MODE` varchar(200) NOT NULL,
                                     `TYPE_CODE` varchar(200) NOT NULL,
                                     PRIMARY KEY (`ATTRIBUTE_DETAILS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ATTRIBUTES`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ATTRIBUTES` (
                              `ID` int NOT NULL AUTO_INCREMENT,
                              `ATTRIBUTE_NAME` varchar(100) NOT NULL,
                              PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ATTRIBUTES_MODES`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ATTRIBUTES_MODES` (
                                    `ID` int NOT NULL AUTO_INCREMENT,
                                    `ATTRIBUTE_ID` varchar(32) NOT NULL,
                                    `MODE_ID` int NOT NULL,
                                    PRIMARY KEY (`ID`),
                                    KEY `MODE_FK` (`MODE_ID`),
                                    CONSTRAINT `MODE_FK` FOREIGN KEY (`MODE_ID`) REFERENCES `MODES` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AUDIT_LOG`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AUDIT_LOG` (
                             `AUDIT_LOG_ID` varchar(32) NOT NULL,
                             `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             `CREATE_USER` varchar(40) NOT NULL,
                             `UPDATE_DATE` timestamp NULL DEFAULT NULL,
                             `UPDATE_USER` varchar(40) DEFAULT NULL,
                             `ACTIVITY_TYPE` varchar(32) DEFAULT NULL,
                             `MEASURE_ID` varchar(32) DEFAULT NULL,
                             `LIST_OBJECT_ID` varchar(32) DEFAULT NULL,
                             `CLAUSE_ID` varchar(32) DEFAULT NULL,
                             `QDM_ID` varchar(36) DEFAULT NULL,
                             PRIMARY KEY (`AUDIT_LOG_ID`),
                             KEY `USER_FK2` (`CREATE_USER`),
                             KEY `USER_FK3` (`UPDATE_USER`),
                             CONSTRAINT `USER_FK2` FOREIGN KEY (`CREATE_USER`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                             CONSTRAINT `USER_FK3` FOREIGN KEY (`UPDATE_USER`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AUTHOR`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AUTHOR` (
                          `ID` varchar(32) NOT NULL,
                          `AUTHOR_NAME` varchar(200) NOT NULL,
                          PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CATEGORY`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CATEGORY` (
                            `CATEGORY_ID` varchar(32) NOT NULL,
                            `DESCRIPTION` varchar(50) NOT NULL,
                            `ABBREVIATION` varchar(50) NOT NULL,
                            PRIMARY KEY (`CATEGORY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CATEGORY_BACKUP_AUG2015`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CATEGORY_BACKUP_AUG2015` (
                                           `CATEGORY_ID` varchar(32) NOT NULL,
                                           `DESCRIPTION` varchar(50) NOT NULL,
                                           `ABBREVIATION` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CLAUSE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLAUSE` (
                          `ID` varchar(32) NOT NULL,
                          `NAME` varchar(100) DEFAULT NULL,
                          `CUSTOM_NAME` varchar(100) DEFAULT NULL,
                          `DESCRIPTION` varchar(2000) DEFAULT NULL,
                          `MEASURE_ID` varchar(64) DEFAULT NULL,
                          `CONTEXT_ID` varchar(32) DEFAULT NULL,
                          `DECISION_ID` varchar(64) DEFAULT NULL,
                          `CLAUSE_TYPE_ID` varchar(32) DEFAULT NULL,
                          `VERSION` varchar(32) DEFAULT NULL,
                          PRIMARY KEY (`ID`),
                          UNIQUE KEY `UNIQUENAME_PERMEASURE` (`NAME`,`MEASURE_ID`),
                          KEY `CONTEXT_FK` (`CONTEXT_ID`),
                          KEY `MEASURE_FK` (`MEASURE_ID`),
                          CONSTRAINT `CONTEXT_FK` FOREIGN KEY (`CONTEXT_ID`) REFERENCES `CONTEXT` (`CONTEXT_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                          CONSTRAINT `MEASURE_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CLAUSE_BACKUP`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CLAUSE_BACKUP` (
                                 `ID` varchar(32) NOT NULL,
                                 `NAME` varchar(100) DEFAULT NULL,
                                 `CUSTOM_NAME` varchar(100) DEFAULT NULL,
                                 `DESCRIPTION` varchar(2000) DEFAULT NULL,
                                 `MEASURE_ID` varchar(64) DEFAULT NULL,
                                 `CONTEXT_ID` varchar(32) DEFAULT NULL,
                                 `DECISION_ID` varchar(64) DEFAULT NULL,
                                 `CLAUSE_TYPE_ID` varchar(32) DEFAULT NULL,
                                 `VERSION` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CODE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CODE` (
                        `CODE_ID` varchar(36) NOT NULL,
                        `CODE` varchar(32) NOT NULL,
                        `DESCRIPTION` varchar(1400) NOT NULL,
                        `CODE_LIST_ID` varchar(32) NOT NULL,
                        PRIMARY KEY (`CODE_ID`),
                        UNIQUE KEY `CODE_PERCODELIST_UC` (`CODE`,`CODE_LIST_ID`),
                        KEY `CODE_LIST_FK` (`CODE_LIST_ID`),
                        CONSTRAINT `CODE_LIST_FK` FOREIGN KEY (`CODE_LIST_ID`) REFERENCES `CODE_LIST` (`CODE_LIST_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CODE_LIST`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CODE_LIST` (
                             `CODE_LIST_ID` varchar(32) NOT NULL,
                             PRIMARY KEY (`CODE_LIST_ID`),
                             KEY `CODE_LIST_OBJECT_FK` (`CODE_LIST_ID`),
                             CONSTRAINT `CODE_LIST_OBJECT_FK` FOREIGN KEY (`CODE_LIST_ID`) REFERENCES `LIST_OBJECT` (`LIST_OBJECT_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CODE_LIST_AUDIT_LOG`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CODE_LIST_AUDIT_LOG` (
                                       `ID` varchar(32) NOT NULL,
                                       `CODE_LIST_ID` varchar(32) NOT NULL,
                                       `ACTIVITY_TYPE` varchar(40) NOT NULL,
                                       `USER_ID` varchar(40) NOT NULL,
                                       `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       `ADDL_INFO` varchar(2000) DEFAULT NULL,
                                       PRIMARY KEY (`ID`),
                                       KEY `MEASURE_ID_FK` (`CODE_LIST_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CODE_SYSTEM`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CODE_SYSTEM` (
                               `CODE_SYSTEM_ID` varchar(32) NOT NULL,
                               `DESCRIPTION` varchar(50) NOT NULL,
                               `CATEGORY_ID` varchar(32) NOT NULL,
                               `ABBREVIATION` varchar(32) DEFAULT NULL,
                               PRIMARY KEY (`CODE_SYSTEM_ID`),
                               KEY `CODE_SYS_CAT_FK` (`CATEGORY_ID`),
                               CONSTRAINT `CODE_SYS_CAT_FK` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `CATEGORY` (`CATEGORY_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `COMPONENT_MEASURES`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `COMPONENT_MEASURES` (
                                      `ID` int NOT NULL AUTO_INCREMENT,
                                      `COMPOSITE_MEASURE_ID` varchar(64) NOT NULL,
                                      `COMPONENT_MEASURE_ID` varchar(64) NOT NULL,
                                      `ALIAS` text,
                                      PRIMARY KEY (`ID`),
                                      KEY `COMPOSITE_MEASURE_ID_FK` (`COMPOSITE_MEASURE_ID`),
                                      KEY `COMPONENT_MEASURE_ID_FK` (`COMPONENT_MEASURE_ID`),
                                      CONSTRAINT `COMPONENT_MEASURE_ID_FK` FOREIGN KEY (`COMPONENT_MEASURE_ID`) REFERENCES `MEASURE` (`ID`),
                                      CONSTRAINT `COMPOSITE_MEASURE_ID_FK` FOREIGN KEY (`COMPOSITE_MEASURE_ID`) REFERENCES `MEASURE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=262 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CONTEXT`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CONTEXT` (
                           `CONTEXT_ID` varchar(64) NOT NULL,
                           `DESCRIPTION` varchar(100) NOT NULL,
                           PRIMARY KEY (`CONTEXT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CQL_AUDIT_LOG`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CQL_AUDIT_LOG` (
                                 `ID` varchar(32) NOT NULL,
                                 `CQL_ID` varchar(32) NOT NULL,
                                 `ACTIVITY_TYPE` varchar(40) NOT NULL,
                                 `USER_ID` varchar(40) NOT NULL,
                                 `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 `ADDL_INFO` varchar(2000) DEFAULT NULL,
                                 PRIMARY KEY (`ID`),
                                 KEY `CQL_ID_FK` (`CQL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CQL_DATA`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CQL_DATA` (
                            `ID` varchar(64) NOT NULL,
                            `MEASURE_ID` varchar(64) NOT NULL,
                            `CQL_DATA` longblob,
                            PRIMARY KEY (`ID`),
                            KEY `CQL_DATA_FK` (`MEASURE_ID`),
                            CONSTRAINT `CQL_DATA_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CQL_LIBRARY`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CQL_LIBRARY` (
                               `ID` varchar(64) NOT NULL,
                               `MEASURE_ID` varchar(64) DEFAULT NULL,
                               `SET_ID` varchar(45) NOT NULL,
                               `CQL_NAME` varchar(500) DEFAULT NULL,
                               `DRAFT` tinyint(1) DEFAULT '1',
                               `VERSION` decimal(6,3) DEFAULT '0.000',
                               `FINALIZED_DATE` timestamp NULL DEFAULT NULL,
                               `RELEASE_VERSION` varchar(45) DEFAULT NULL,
                               `OWNER_ID` varchar(40) NOT NULL,
                               `LOCKED_USER` varchar(40) DEFAULT NULL,
                               `LOCKED_OUT_DATE` timestamp NULL DEFAULT NULL,
                               `CQL_XML` longblob,
                               `REVISION_NUMBER` int(3) unsigned zerofill DEFAULT '000',
                               `QDM_VERSION` varchar(45) DEFAULT NULL,
                               `LAST_MODIFIED_ON` timestamp NULL DEFAULT NULL,
                               `LAST_MODIFIED_BY` varchar(40) DEFAULT NULL,
                               `LIBRARY_MODEL` varchar(10) DEFAULT NULL,
                               `FHIR_VERSION` varchar(45) DEFAULT NULL,
                               `SEVERE_ERROR_CQL` longtext,
                               `DESCRIPTION` varchar(2000) DEFAULT NULL,
                               `LIBRARY_STEWARD_ID` varchar(32) DEFAULT NULL,
                               `EXPERIMENTAL` bit(1) DEFAULT b'0',
                               PRIMARY KEY (`ID`),
                               KEY `LOCKED_USER_ID_FK_idx` (`LOCKED_USER`),
                               KEY `CQL_OWNER_ID_FK_idx` (`OWNER_ID`),
                               KEY `fk_library_user` (`LAST_MODIFIED_BY`),
                               KEY `idx_set_id` (`SET_ID`),
                               KEY `idx_library_cql_name` (`CQL_NAME`),
                               CONSTRAINT `CQL_OWNER_ID_FK` FOREIGN KEY (`OWNER_ID`) REFERENCES `USER` (`USER_ID`),
                               CONSTRAINT `fk_library_user` FOREIGN KEY (`LAST_MODIFIED_BY`) REFERENCES `USER` (`USER_ID`),
                               CONSTRAINT `LOCKED_USER_ID_FK` FOREIGN KEY (`LOCKED_USER`) REFERENCES `USER` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CQL_LIBRARY_ASSOCIATION`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CQL_LIBRARY_ASSOCIATION` (
                                           `ID` varchar(64) NOT NULL,
                                           `ASSOCIATION_ID` varchar(64) NOT NULL,
                                           `CQL_LIBRARY_ID` varchar(64) NOT NULL,
                                           PRIMARY KEY (`ID`),
                                           KEY `idx_association_id` (`ASSOCIATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CQL_LIBRARY_BACKUP`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CQL_LIBRARY_BACKUP` (
                                      `ID` varchar(64) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
                                      `MEASURE_ID` varchar(64) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
                                      `MEASURE_SET_ID` varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
                                      `CQL_SET_ID` varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
                                      `CQL_NAME` varchar(300) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
                                      `DRAFT` tinyint(1) DEFAULT '1',
                                      `VERSION` decimal(6,3) DEFAULT '0.000',
                                      `FINALIZED_DATE` timestamp NULL DEFAULT NULL,
                                      `RELEASE_VERSION` varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
                                      `OWNER_ID` varchar(40) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
                                      `LOCKED_USER` varchar(40) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
                                      `LOCKED_OUT_DATE` timestamp NULL DEFAULT NULL,
                                      `CQL_XML` longblob,
                                      `REVISION_NUMBER` int(3) unsigned zerofill DEFAULT '000',
                                      `QDM_VERSION` varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT '5.0.2'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CQL_LIBRARY_EXPORT`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CQL_LIBRARY_EXPORT` (
                                      `ID` varchar(64) NOT NULL DEFAULT '',
                                      `CQL_LIBRARY_ID` varchar(64) NOT NULL,
                                      `CQL` longtext,
                                      `ELM` longtext,
                                      `JSON` longtext,
                                      PRIMARY KEY (`ID`),
                                      KEY `CQL_LIBRARY_ID_FK` (`CQL_LIBRARY_ID`),
                                      CONSTRAINT `CQL_LIBRARY_ID_FK` FOREIGN KEY (`CQL_LIBRARY_ID`) REFERENCES `CQL_LIBRARY` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CQL_LIBRARY_HISTORY`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CQL_LIBRARY_HISTORY` (
                                       `ID` int NOT NULL AUTO_INCREMENT,
                                       `MEASURE_ID` varchar(64) DEFAULT NULL,
                                       `LIBRARY_ID` varchar(64) DEFAULT NULL,
                                       `LAST_MODIFIED_BY` varchar(40) NOT NULL,
                                       `CQL_LIBRARY` longblob,
                                       `LAST_MODIFIED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       `FREE_TEXT_EDITOR_USED` tinyint(1) DEFAULT '1',
                                       PRIMARY KEY (`ID`),
                                       KEY `CQL_LIBRARY_HISTORY_USER_ID_FK` (`LAST_MODIFIED_BY`),
                                       KEY `CQL_LIBRARY_HISTORY_MEASURE_ID_FK` (`MEASURE_ID`),
                                       KEY `CQL_LIBRARY_HISTORY_LIBRARY_ID_FK` (`LIBRARY_ID`),
                                       CONSTRAINT `CQL_LIBRARY_HISTORY_LIBRARY_ID_FK` FOREIGN KEY (`LIBRARY_ID`) REFERENCES `CQL_LIBRARY` (`ID`) ON DELETE CASCADE,
                                       CONSTRAINT `CQL_LIBRARY_HISTORY_MEASURE_ID_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE,
                                       CONSTRAINT `CQL_LIBRARY_HISTORY_USER_ID_FK` FOREIGN KEY (`LAST_MODIFIED_BY`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2250 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CQL_LIBRARY_SET`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CQL_LIBRARY_SET` (
                                   `ID` varchar(36) NOT NULL,
                                   `NAME` varchar(200) DEFAULT NULL,
                                   PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CQL_LIBRARY_SHARE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CQL_LIBRARY_SHARE` (
                                     `CQL_LIBRARY_SHARE_ID` varchar(32) NOT NULL,
                                     `CQL_LIBRARY_ID` varchar(32) NOT NULL,
                                     `CQL_LIBRARY_OWNER_USER_ID` varchar(40) NOT NULL,
                                     `SHARE_USER_ID` varchar(40) NOT NULL,
                                     `SHARE_LEVEL_ID` varchar(32) NOT NULL,
                                     PRIMARY KEY (`CQL_LIBRARY_SHARE_ID`),
                                     KEY `CQL_LIBRARY_SHARE_CQL_LIBRARY_FK` (`CQL_LIBRARY_ID`),
                                     KEY `CQL_LIBRARY_SHARE_OWNER_FK` (`CQL_LIBRARY_OWNER_USER_ID`),
                                     KEY `CQL_LIBRARY_SHARE_SHARE_FK` (`SHARE_USER_ID`),
                                     KEY `CQL_LIBRARY_SHARE_LEVEL_ID` (`SHARE_LEVEL_ID`),
                                     CONSTRAINT `CQL_LIBRARY_SHARE_CQL_LIBRARY_FK` FOREIGN KEY (`CQL_LIBRARY_ID`) REFERENCES `CQL_LIBRARY` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                                     CONSTRAINT `CQL_LIBRARY_SHARE_LEVEL_ID` FOREIGN KEY (`SHARE_LEVEL_ID`) REFERENCES `SHARE_LEVEL` (`SHARE_LEVEL_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                                     CONSTRAINT `CQL_LIBRARY_SHARE_OWNER_FK` FOREIGN KEY (`CQL_LIBRARY_OWNER_USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                                     CONSTRAINT `CQL_LIBRARY_SHARE_SHARE_FK` FOREIGN KEY (`SHARE_USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DATA_TYPE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DATA_TYPE` (
                             `DATA_TYPE_ID` varchar(32) NOT NULL,
                             `DESCRIPTION` varchar(50) NOT NULL,
                             `CATEGORY_ID` varchar(32) NOT NULL,
                             PRIMARY KEY (`DATA_TYPE_ID`),
                             KEY `DATA_TYPE_CAT_FK` (`CATEGORY_ID`),
                             CONSTRAINT `DATA_TYPE_CAT_FK` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `CATEGORY` (`CATEGORY_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DATA_TYPE_BACKUP_AUG2015`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DATA_TYPE_BACKUP_AUG2015` (
                                            `DATA_TYPE_ID` varchar(32) NOT NULL,
                                            `DESCRIPTION` varchar(50) NOT NULL,
                                            `CATEGORY_ID` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DATABASECHANGELOG`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DATABASECHANGELOG` (
                                     `ID` varchar(63) NOT NULL,
                                     `AUTHOR` varchar(63) NOT NULL,
                                     `FILENAME` varchar(200) NOT NULL,
                                     `DATEEXECUTED` datetime NOT NULL,
                                     `ORDEREXECUTED` int NOT NULL,
                                     `EXECTYPE` varchar(10) NOT NULL,
                                     `MD5SUM` varchar(35) DEFAULT NULL,
                                     `DESCRIPTION` varchar(255) DEFAULT NULL,
                                     `COMMENTS` varchar(255) DEFAULT NULL,
                                     `TAG` varchar(255) DEFAULT NULL,
                                     `LIQUIBASE` varchar(20) DEFAULT NULL,
                                     `CONTEXTS` varchar(255) DEFAULT NULL,
                                     `LABELS` varchar(255) DEFAULT NULL,
                                     `DEPLOYMENT_ID` varchar(10) DEFAULT NULL,
                                     PRIMARY KEY (`ID`,`AUTHOR`,`FILENAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DATABASECHANGELOGLOCK`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DATABASECHANGELOGLOCK` (
                                         `ID` int NOT NULL,
                                         `LOCKED` tinyint(1) NOT NULL,
                                         `LOCKGRANTED` datetime DEFAULT NULL,
                                         `LOCKEDBY` varchar(255) DEFAULT NULL,
                                         PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DECISION`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DECISION` (
                            `ID` varchar(64) NOT NULL,
                            `OPERATOR` varchar(45) NOT NULL,
                            `PARENT_ID` varchar(64) DEFAULT NULL,
                            `ORDER_NUM` varchar(32) DEFAULT NULL,
                            `CLAUSE_ID` varchar(64) DEFAULT NULL,
                            `ATTRIBUTE_ID` varchar(64) DEFAULT NULL,
                            PRIMARY KEY (`ID`),
                            KEY `CLAUSE_FK` (`CLAUSE_ID`),
                            KEY `DECISION_FK3` (`PARENT_ID`),
                            CONSTRAINT `CLAUSE_FK` FOREIGN KEY (`CLAUSE_ID`) REFERENCES `CLAUSE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                            CONSTRAINT `DECISION_FK3` FOREIGN KEY (`PARENT_ID`) REFERENCES `DECISION` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DECISION_BACKUP`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DECISION_BACKUP` (
                                   `ID` varchar(64) NOT NULL,
                                   `OPERATOR` varchar(45) NOT NULL,
                                   `PARENT_ID` varchar(64) DEFAULT NULL,
                                   `ORDER_NUM` varchar(32) DEFAULT NULL,
                                   `CLAUSE_ID` varchar(64) DEFAULT NULL,
                                   `ATTRIBUTE_ID` varchar(64) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EMAIL_AUDIT_LOG`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `EMAIL_AUDIT_LOG` (
                                   `ID` varchar(32) NOT NULL,
                                   `LOGIN_ID` varchar(40) NOT NULL,
                                   `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                   `ACTIVITY_TYPE` varchar(64) DEFAULT NULL,
                                   PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FEATURE_FLAGS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `FEATURE_FLAGS` (
                                 `ID` int NOT NULL AUTO_INCREMENT,
                                 `FLAG_NAME` varchar(100) NOT NULL,
                                 `FLAG_ON` tinyint(1) NOT NULL,
                                 PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GROUPED_CODE_LISTS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `GROUPED_CODE_LISTS` (
                                      `GROUPED_CODE_LISTS_ID` varchar(32) NOT NULL,
                                      `GROUP_LIST_ID` varchar(32) NOT NULL,
                                      `CODE_LIST_ID` varchar(32) NOT NULL,
                                      `DESCRIPTION` varchar(1000) NOT NULL,
                                      PRIMARY KEY (`GROUPED_CODE_LISTS_ID`),
                                      KEY `GR_CODE_LIST_FK` (`CODE_LIST_ID`),
                                      KEY `GR_LIST_OBJ_FK` (`GROUP_LIST_ID`),
                                      CONSTRAINT `GR_CODE_LIST_FK` FOREIGN KEY (`CODE_LIST_ID`) REFERENCES `CODE_LIST` (`CODE_LIST_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                                      CONSTRAINT `GR_LIST_OBJ_FK` FOREIGN KEY (`GROUP_LIST_ID`) REFERENCES `LIST_OBJECT` (`LIST_OBJECT_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_BINARY_STORAGE_BLOB`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_BINARY_STORAGE_BLOB` (
                                           `BLOB_ID` varchar(200) NOT NULL,
                                           `BLOB_DATA` longblob NOT NULL,
                                           `CONTENT_TYPE` varchar(100) NOT NULL,
                                           `BLOB_HASH` varchar(128) DEFAULT NULL,
                                           `PUBLISHED_DATE` datetime NOT NULL,
                                           `RESOURCE_ID` varchar(100) NOT NULL,
                                           `BLOB_SIZE` int DEFAULT NULL,
                                           PRIMARY KEY (`BLOB_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_BLK_EXPORT_COLFILE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_BLK_EXPORT_COLFILE` (
                                          `PID` bigint NOT NULL,
                                          `RES_ID` varchar(100) NOT NULL,
                                          `COLLECTION_PID` bigint NOT NULL,
                                          PRIMARY KEY (`PID`),
                                          KEY `FK_BLKEXCOLFILE_COLLECT` (`COLLECTION_PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_BLK_EXPORT_COLLECTION`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_BLK_EXPORT_COLLECTION` (
                                             `PID` bigint NOT NULL,
                                             `TYPE_FILTER` varchar(1000) DEFAULT NULL,
                                             `RES_TYPE` varchar(40) NOT NULL,
                                             `OPTLOCK` int NOT NULL,
                                             `JOB_PID` bigint NOT NULL,
                                             PRIMARY KEY (`PID`),
                                             KEY `FK_BLKEXCOL_JOB` (`JOB_PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_BLK_EXPORT_JOB`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_BLK_EXPORT_JOB` (
                                      `PID` bigint NOT NULL,
                                      `CREATED_TIME` datetime NOT NULL,
                                      `EXP_TIME` datetime NOT NULL,
                                      `JOB_ID` varchar(36) NOT NULL,
                                      `REQUEST` varchar(500) NOT NULL,
                                      `EXP_SINCE` datetime DEFAULT NULL,
                                      `JOB_STATUS` varchar(10) NOT NULL,
                                      `STATUS_MESSAGE` varchar(500) DEFAULT NULL,
                                      `STATUS_TIME` datetime NOT NULL,
                                      `OPTLOCK` int NOT NULL,
                                      PRIMARY KEY (`PID`),
                                      UNIQUE KEY `IDX_BLKEX_JOB_ID` (`JOB_ID`),
                                      KEY `IDX_BLKEX_EXPTIME` (`EXP_TIME`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_FORCED_ID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_FORCED_ID` (
                                 `PID` bigint NOT NULL,
                                 `FORCED_ID` varchar(100) NOT NULL,
                                 `RESOURCE_PID` bigint NOT NULL,
                                 `RESOURCE_TYPE` varchar(100) DEFAULT '',
                                 PRIMARY KEY (`PID`),
                                 UNIQUE KEY `IDX_FORCEDID_RESID` (`RESOURCE_PID`),
                                 UNIQUE KEY `IDX_FORCEDID_TYPE_FID` (`RESOURCE_TYPE`,`FORCED_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_HISTORY_TAG`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_HISTORY_TAG` (
                                   `PID` bigint NOT NULL,
                                   `TAG_ID` bigint DEFAULT NULL,
                                   `RES_ID` bigint NOT NULL,
                                   `RES_TYPE` varchar(40) NOT NULL,
                                   `RES_VER_PID` bigint NOT NULL,
                                   PRIMARY KEY (`PID`),
                                   UNIQUE KEY `IDX_RESHISTTAG_TAGID` (`RES_VER_PID`,`TAG_ID`),
                                   KEY `FKtderym7awj6q8iq5c51xv4ndw` (`TAG_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_IDX_CMP_STRING_UNIQ`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_IDX_CMP_STRING_UNIQ` (
                                           `PID` bigint NOT NULL,
                                           `IDX_STRING` varchar(200) NOT NULL,
                                           `RES_ID` bigint DEFAULT NULL,
                                           PRIMARY KEY (`PID`),
                                           UNIQUE KEY `IDX_IDXCMPSTRUNIQ_STRING` (`IDX_STRING`),
                                           KEY `IDX_IDXCMPSTRUNIQ_RESOURCE` (`RES_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_RES_LINK`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_RES_LINK` (
                                `PID` bigint NOT NULL,
                                `SRC_PATH` varchar(200) NOT NULL,
                                `SRC_RESOURCE_ID` bigint NOT NULL,
                                `SOURCE_RESOURCE_TYPE` varchar(40) NOT NULL,
                                `TARGET_RESOURCE_ID` bigint DEFAULT NULL,
                                `TARGET_RESOURCE_TYPE` varchar(40) NOT NULL,
                                `TARGET_RESOURCE_URL` varchar(200) DEFAULT NULL,
                                `SP_UPDATED` datetime DEFAULT NULL,
                                PRIMARY KEY (`PID`),
                                KEY `IDX_RL_TPATHRES` (`SRC_PATH`,`TARGET_RESOURCE_ID`),
                                KEY `IDX_RL_SRC` (`SRC_RESOURCE_ID`),
                                KEY `IDX_RL_DEST` (`TARGET_RESOURCE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_RES_PARAM_PRESENT`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_RES_PARAM_PRESENT` (
                                         `PID` bigint NOT NULL,
                                         `HASH_PRESENCE` bigint DEFAULT NULL,
                                         `SP_PRESENT` bit(1) NOT NULL,
                                         `RES_ID` bigint NOT NULL,
                                         PRIMARY KEY (`PID`),
                                         KEY `IDX_RESPARMPRESENT_RESID` (`RES_ID`),
                                         KEY `IDX_RESPARMPRESENT_HASHPRES` (`HASH_PRESENCE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_RES_REINDEX_JOB`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_RES_REINDEX_JOB` (
                                       `PID` bigint NOT NULL,
                                       `JOB_DELETED` bit(1) NOT NULL,
                                       `REINDEX_COUNT` int DEFAULT NULL,
                                       `RES_TYPE` varchar(100) DEFAULT NULL,
                                       `SUSPENDED_UNTIL` datetime DEFAULT NULL,
                                       `UPDATE_THRESHOLD_HIGH` datetime NOT NULL,
                                       `UPDATE_THRESHOLD_LOW` datetime DEFAULT NULL,
                                       PRIMARY KEY (`PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_RES_TAG`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_RES_TAG` (
                               `PID` bigint NOT NULL,
                               `TAG_ID` bigint DEFAULT NULL,
                               `RES_ID` bigint DEFAULT NULL,
                               `RES_TYPE` varchar(40) NOT NULL,
                               PRIMARY KEY (`PID`),
                               UNIQUE KEY `IDX_RESTAG_TAGID` (`RES_ID`,`TAG_ID`),
                               KEY `FKbfcjbaftmiwr3rxkwsy23vneo` (`TAG_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_RES_VER`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_RES_VER` (
                               `PID` bigint NOT NULL,
                               `RES_DELETED_AT` datetime DEFAULT NULL,
                               `RES_VERSION` varchar(7) DEFAULT NULL,
                               `HAS_TAGS` bit(1) NOT NULL,
                               `RES_PUBLISHED` datetime NOT NULL,
                               `RES_UPDATED` datetime NOT NULL,
                               `RES_ENCODING` varchar(5) NOT NULL,
                               `RES_TEXT` longblob,
                               `RES_ID` bigint DEFAULT NULL,
                               `RES_TYPE` varchar(40) NOT NULL,
                               `RES_VER` bigint NOT NULL,
                               `FORCED_ID_PID` bigint DEFAULT NULL,
                               PRIMARY KEY (`PID`),
                               UNIQUE KEY `IDX_RESVER_ID_VER` (`RES_ID`,`RES_VER`),
                               KEY `IDX_RESVER_TYPE_DATE` (`RES_TYPE`,`RES_UPDATED`),
                               KEY `IDX_RESVER_ID_DATE` (`RES_ID`,`RES_UPDATED`),
                               KEY `IDX_RESVER_DATE` (`RES_UPDATED`),
                               KEY `FKh20i7lcbchkaxekvwg9ix4hc5` (`FORCED_ID_PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_RES_VER_PROV`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_RES_VER_PROV` (
                                    `RES_VER_PID` bigint NOT NULL,
                                    `REQUEST_ID` varchar(16) DEFAULT NULL,
                                    `SOURCE_URI` varchar(100) DEFAULT NULL,
                                    `RES_PID` bigint NOT NULL,
                                    PRIMARY KEY (`RES_VER_PID`),
                                    KEY `IDX_RESVERPROV_SOURCEURI` (`SOURCE_URI`),
                                    KEY `IDX_RESVERPROV_REQUESTID` (`REQUEST_ID`),
                                    KEY `FK_RESVERPROV_RES_PID` (`RES_PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_RESOURCE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_RESOURCE` (
                                `RES_ID` bigint NOT NULL,
                                `RES_DELETED_AT` datetime DEFAULT NULL,
                                `RES_VERSION` varchar(7) DEFAULT NULL,
                                `HAS_TAGS` bit(1) NOT NULL,
                                `RES_PUBLISHED` datetime NOT NULL,
                                `RES_UPDATED` datetime NOT NULL,
                                `SP_HAS_LINKS` bit(1) DEFAULT NULL,
                                `HASH_SHA256` varchar(64) DEFAULT NULL,
                                `SP_INDEX_STATUS` bigint DEFAULT NULL,
                                `RES_LANGUAGE` varchar(20) DEFAULT NULL,
                                `SP_CMPSTR_UNIQ_PRESENT` bit(1) DEFAULT NULL,
                                `SP_COORDS_PRESENT` bit(1) DEFAULT NULL,
                                `SP_DATE_PRESENT` bit(1) DEFAULT NULL,
                                `SP_NUMBER_PRESENT` bit(1) DEFAULT NULL,
                                `SP_QUANTITY_PRESENT` bit(1) DEFAULT NULL,
                                `SP_STRING_PRESENT` bit(1) DEFAULT NULL,
                                `SP_TOKEN_PRESENT` bit(1) DEFAULT NULL,
                                `SP_URI_PRESENT` bit(1) DEFAULT NULL,
                                `RES_PROFILE` varchar(200) DEFAULT NULL,
                                `RES_TYPE` varchar(40) NOT NULL,
                                `RES_VER` bigint DEFAULT NULL,
                                `FORCED_ID_PID` bigint DEFAULT NULL,
                                PRIMARY KEY (`RES_ID`),
                                KEY `IDX_RES_DATE` (`RES_UPDATED`),
                                KEY `IDX_RES_LANG` (`RES_TYPE`,`RES_LANGUAGE`),
                                KEY `IDX_RES_PROFILE` (`RES_PROFILE`),
                                KEY `IDX_RES_TYPE` (`RES_TYPE`),
                                KEY `IDX_INDEXSTATUS` (`SP_INDEX_STATUS`),
                                KEY `FKhjgj8cp879gfxko25cx5o692r` (`FORCED_ID_PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_SEARCH`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_SEARCH` (
                              `PID` bigint NOT NULL,
                              `CREATED` datetime NOT NULL,
                              `SEARCH_DELETED` bit(1) DEFAULT NULL,
                              `EXPIRY_OR_NULL` datetime DEFAULT NULL,
                              `FAILURE_CODE` int DEFAULT NULL,
                              `FAILURE_MESSAGE` varchar(500) DEFAULT NULL,
                              `LAST_UPDATED_HIGH` datetime DEFAULT NULL,
                              `LAST_UPDATED_LOW` datetime DEFAULT NULL,
                              `NUM_BLOCKED` int DEFAULT NULL,
                              `NUM_FOUND` int NOT NULL,
                              `PREFERRED_PAGE_SIZE` int DEFAULT NULL,
                              `RESOURCE_ID` bigint DEFAULT NULL,
                              `RESOURCE_TYPE` varchar(200) DEFAULT NULL,
                              `SEARCH_LAST_RETURNED` datetime NOT NULL,
                              `SEARCH_PARAM_MAP` longblob,
                              `SEARCH_QUERY_STRING` longtext,
                              `SEARCH_QUERY_STRING_HASH` int DEFAULT NULL,
                              `SEARCH_TYPE` int NOT NULL,
                              `SEARCH_STATUS` varchar(10) NOT NULL,
                              `TOTAL_COUNT` int DEFAULT NULL,
                              `SEARCH_UUID` varchar(36) NOT NULL,
                              `OPTLOCK_VERSION` int DEFAULT NULL,
                              PRIMARY KEY (`PID`),
                              UNIQUE KEY `IDX_SEARCH_UUID` (`SEARCH_UUID`),
                              KEY `IDX_SEARCH_LASTRETURNED` (`SEARCH_LAST_RETURNED`),
                              KEY `IDX_SEARCH_RESTYPE_HASHS` (`RESOURCE_TYPE`,`SEARCH_QUERY_STRING_HASH`,`CREATED`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_SEARCH_INCLUDE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_SEARCH_INCLUDE` (
                                      `PID` bigint NOT NULL,
                                      `SEARCH_INCLUDE` varchar(200) NOT NULL,
                                      `INC_RECURSE` bit(1) NOT NULL,
                                      `REVINCLUDE` bit(1) NOT NULL,
                                      `SEARCH_PID` bigint NOT NULL,
                                      PRIMARY KEY (`PID`),
                                      KEY `FK_SEARCHINC_SEARCH` (`SEARCH_PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_SEARCH_RESULT`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_SEARCH_RESULT` (
                                     `PID` bigint NOT NULL,
                                     `SEARCH_ORDER` int NOT NULL,
                                     `RESOURCE_PID` bigint NOT NULL,
                                     `SEARCH_PID` bigint NOT NULL,
                                     PRIMARY KEY (`PID`),
                                     UNIQUE KEY `IDX_SEARCHRES_ORDER` (`SEARCH_PID`,`SEARCH_ORDER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_SPIDX_COORDS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_SPIDX_COORDS` (
                                    `SP_ID` bigint NOT NULL,
                                    `SP_MISSING` bit(1) DEFAULT NULL,
                                    `SP_NAME` varchar(100) NOT NULL,
                                    `RES_ID` bigint NOT NULL,
                                    `RES_TYPE` varchar(100) NOT NULL,
                                    `SP_UPDATED` datetime DEFAULT NULL,
                                    `HASH_IDENTITY` bigint DEFAULT NULL,
                                    `SP_LATITUDE` double DEFAULT NULL,
                                    `SP_LONGITUDE` double DEFAULT NULL,
                                    PRIMARY KEY (`SP_ID`),
                                    KEY `IDX_SP_COORDS_HASH` (`HASH_IDENTITY`,`SP_LATITUDE`,`SP_LONGITUDE`),
                                    KEY `IDX_SP_COORDS_UPDATED` (`SP_UPDATED`),
                                    KEY `IDX_SP_COORDS_RESID` (`RES_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_SPIDX_DATE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_SPIDX_DATE` (
                                  `SP_ID` bigint NOT NULL,
                                  `SP_MISSING` bit(1) DEFAULT NULL,
                                  `SP_NAME` varchar(100) NOT NULL,
                                  `RES_ID` bigint NOT NULL,
                                  `RES_TYPE` varchar(100) NOT NULL,
                                  `SP_UPDATED` datetime DEFAULT NULL,
                                  `HASH_IDENTITY` bigint DEFAULT NULL,
                                  `SP_VALUE_HIGH` datetime DEFAULT NULL,
                                  `SP_VALUE_LOW` datetime DEFAULT NULL,
                                  PRIMARY KEY (`SP_ID`),
                                  KEY `IDX_SP_DATE_HASH` (`HASH_IDENTITY`,`SP_VALUE_LOW`,`SP_VALUE_HIGH`),
                                  KEY `IDX_SP_DATE_UPDATED` (`SP_UPDATED`),
                                  KEY `IDX_SP_DATE_RESID` (`RES_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_SPIDX_NUMBER`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_SPIDX_NUMBER` (
                                    `SP_ID` bigint NOT NULL,
                                    `SP_MISSING` bit(1) DEFAULT NULL,
                                    `SP_NAME` varchar(100) NOT NULL,
                                    `RES_ID` bigint NOT NULL,
                                    `RES_TYPE` varchar(100) NOT NULL,
                                    `SP_UPDATED` datetime DEFAULT NULL,
                                    `HASH_IDENTITY` bigint DEFAULT NULL,
                                    `SP_VALUE` decimal(19,2) DEFAULT NULL,
                                    PRIMARY KEY (`SP_ID`),
                                    KEY `IDX_SP_NUMBER_HASH_VAL` (`HASH_IDENTITY`,`SP_VALUE`),
                                    KEY `IDX_SP_NUMBER_UPDATED` (`SP_UPDATED`),
                                    KEY `IDX_SP_NUMBER_RESID` (`RES_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_SPIDX_QUANTITY`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_SPIDX_QUANTITY` (
                                      `SP_ID` bigint NOT NULL,
                                      `SP_MISSING` bit(1) DEFAULT NULL,
                                      `SP_NAME` varchar(100) NOT NULL,
                                      `RES_ID` bigint NOT NULL,
                                      `RES_TYPE` varchar(100) NOT NULL,
                                      `SP_UPDATED` datetime DEFAULT NULL,
                                      `HASH_IDENTITY` bigint DEFAULT NULL,
                                      `HASH_IDENTITY_AND_UNITS` bigint DEFAULT NULL,
                                      `HASH_IDENTITY_SYS_UNITS` bigint DEFAULT NULL,
                                      `SP_SYSTEM` varchar(200) DEFAULT NULL,
                                      `SP_UNITS` varchar(200) DEFAULT NULL,
                                      `SP_VALUE` decimal(19,2) DEFAULT NULL,
                                      PRIMARY KEY (`SP_ID`),
                                      KEY `IDX_SP_QUANTITY_HASH` (`HASH_IDENTITY`,`SP_VALUE`),
                                      KEY `IDX_SP_QUANTITY_HASH_UN` (`HASH_IDENTITY_AND_UNITS`,`SP_VALUE`),
                                      KEY `IDX_SP_QUANTITY_HASH_SYSUN` (`HASH_IDENTITY_SYS_UNITS`,`SP_VALUE`),
                                      KEY `IDX_SP_QUANTITY_UPDATED` (`SP_UPDATED`),
                                      KEY `IDX_SP_QUANTITY_RESID` (`RES_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_SPIDX_STRING`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_SPIDX_STRING` (
                                    `SP_ID` bigint NOT NULL,
                                    `SP_MISSING` bit(1) DEFAULT NULL,
                                    `SP_NAME` varchar(100) NOT NULL,
                                    `RES_ID` bigint NOT NULL,
                                    `RES_TYPE` varchar(100) NOT NULL,
                                    `SP_UPDATED` datetime DEFAULT NULL,
                                    `HASH_EXACT` bigint DEFAULT NULL,
                                    `HASH_IDENTITY` bigint DEFAULT NULL,
                                    `HASH_NORM_PREFIX` bigint DEFAULT NULL,
                                    `SP_VALUE_EXACT` varchar(200) DEFAULT NULL,
                                    `SP_VALUE_NORMALIZED` varchar(200) DEFAULT NULL,
                                    PRIMARY KEY (`SP_ID`),
                                    KEY `IDX_SP_STRING_HASH_IDENT` (`HASH_IDENTITY`),
                                    KEY `IDX_SP_STRING_HASH_NRM` (`HASH_NORM_PREFIX`,`SP_VALUE_NORMALIZED`),
                                    KEY `IDX_SP_STRING_HASH_EXCT` (`HASH_EXACT`),
                                    KEY `IDX_SP_STRING_UPDATED` (`SP_UPDATED`),
                                    KEY `IDX_SP_STRING_RESID` (`RES_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_SPIDX_TOKEN`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_SPIDX_TOKEN` (
                                   `SP_ID` bigint NOT NULL,
                                   `SP_MISSING` bit(1) DEFAULT NULL,
                                   `SP_NAME` varchar(100) NOT NULL,
                                   `RES_ID` bigint NOT NULL,
                                   `RES_TYPE` varchar(100) NOT NULL,
                                   `SP_UPDATED` datetime DEFAULT NULL,
                                   `HASH_IDENTITY` bigint DEFAULT NULL,
                                   `HASH_SYS` bigint DEFAULT NULL,
                                   `HASH_SYS_AND_VALUE` bigint DEFAULT NULL,
                                   `HASH_VALUE` bigint DEFAULT NULL,
                                   `SP_SYSTEM` varchar(200) DEFAULT NULL,
                                   `SP_VALUE` varchar(200) DEFAULT NULL,
                                   PRIMARY KEY (`SP_ID`),
                                   KEY `IDX_SP_TOKEN_HASH` (`HASH_IDENTITY`),
                                   KEY `IDX_SP_TOKEN_HASH_S` (`HASH_SYS`),
                                   KEY `IDX_SP_TOKEN_HASH_SV` (`HASH_SYS_AND_VALUE`),
                                   KEY `IDX_SP_TOKEN_HASH_V` (`HASH_VALUE`),
                                   KEY `IDX_SP_TOKEN_UPDATED` (`SP_UPDATED`),
                                   KEY `IDX_SP_TOKEN_RESID` (`RES_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_SPIDX_URI`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_SPIDX_URI` (
                                 `SP_ID` bigint NOT NULL,
                                 `SP_MISSING` bit(1) DEFAULT NULL,
                                 `SP_NAME` varchar(100) NOT NULL,
                                 `RES_ID` bigint NOT NULL,
                                 `RES_TYPE` varchar(100) NOT NULL,
                                 `SP_UPDATED` datetime DEFAULT NULL,
                                 `HASH_IDENTITY` bigint DEFAULT NULL,
                                 `HASH_URI` bigint DEFAULT NULL,
                                 `SP_URI` varchar(254) DEFAULT NULL,
                                 PRIMARY KEY (`SP_ID`),
                                 KEY `IDX_SP_URI` (`RES_TYPE`,`SP_NAME`,`SP_URI`),
                                 KEY `IDX_SP_URI_HASH_IDENTITY` (`HASH_IDENTITY`,`SP_URI`),
                                 KEY `IDX_SP_URI_HASH_URI` (`HASH_URI`),
                                 KEY `IDX_SP_URI_RESTYPE_NAME` (`RES_TYPE`,`SP_NAME`),
                                 KEY `IDX_SP_URI_UPDATED` (`SP_UPDATED`),
                                 KEY `IDX_SP_URI_COORDS` (`RES_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_SUBSCRIPTION_STATS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_SUBSCRIPTION_STATS` (
                                          `PID` bigint NOT NULL,
                                          `CREATED_TIME` datetime NOT NULL,
                                          `RES_ID` bigint DEFAULT NULL,
                                          PRIMARY KEY (`PID`),
                                          UNIQUE KEY `IDX_SUBSC_RESID` (`RES_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `HFJ_TAG_DEF`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `HFJ_TAG_DEF` (
                               `TAG_ID` bigint NOT NULL,
                               `TAG_CODE` varchar(200) DEFAULT NULL,
                               `TAG_DISPLAY` varchar(200) DEFAULT NULL,
                               `TAG_SYSTEM` varchar(200) DEFAULT NULL,
                               `TAG_TYPE` int NOT NULL,
                               PRIMARY KEY (`TAG_ID`),
                               UNIQUE KEY `IDX_TAGDEF_TYPESYSCODE` (`TAG_TYPE`,`TAG_SYSTEM`,`TAG_CODE`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LIST_OBJECT`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `LIST_OBJECT` (
                               `LIST_OBJECT_ID` varchar(32) NOT NULL,
                               `NAME` varchar(255) NOT NULL,
                               `STEWARD` varchar(255) DEFAULT NULL,
                               `OID` varchar(255) NOT NULL,
                               `RATIONALE` varchar(2000) NOT NULL DEFAULT 'N/A',
                               `COMMENT` varchar(2000) DEFAULT NULL,
                               `OBJECT_OWNER` varchar(32) DEFAULT NULL,
                               `CATEGORY_ID` varchar(32) NOT NULL,
                               `CODE_SYS_VERSION` varchar(255) NOT NULL,
                               `CODE_SYSTEM_ID` varchar(32) NOT NULL,
                               `MEASURE_ID` varchar(32) DEFAULT NULL,
                               `STEWARD_OTHER` varchar(200) DEFAULT NULL,
                               `CODE_LIST_DEVELOPER` varchar(200) DEFAULT NULL,
                               `CODE_LIST_CONTEXT` varchar(200) DEFAULT NULL,
                               `LAST_MODIFIED` timestamp NULL DEFAULT NULL,
                               `DRAFT` tinyint(1) NOT NULL DEFAULT '1',
                               PRIMARY KEY (`LIST_OBJECT_ID`),
                               KEY `LIST_OBJECT_USER_FK` (`OBJECT_OWNER`),
                               KEY `LIST_OBJECT_CAT_FK` (`CATEGORY_ID`),
                               KEY `LIST_OBJECT_CODE_SYSTEM_FK` (`CODE_SYSTEM_ID`),
                               KEY `LIST_OBJECT_STEWARD_FK` (`STEWARD`),
                               KEY `LIST_OBJECT_MEASURE_FK` (`MEASURE_ID`),
                               CONSTRAINT `LIST_OBJECT_CAT_FK` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `CATEGORY` (`CATEGORY_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                               CONSTRAINT `LIST_OBJECT_CODE_SYSTEM_FK` FOREIGN KEY (`CODE_SYSTEM_ID`) REFERENCES `CODE_SYSTEM` (`CODE_SYSTEM_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                               CONSTRAINT `LIST_OBJECT_MEASURE_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                               CONSTRAINT `LIST_OBJECT_STEWARD_FK` FOREIGN KEY (`STEWARD`) REFERENCES `STEWARD_ORG` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                               CONSTRAINT `LIST_OBJECT_USER_FK` FOREIGN KEY (`OBJECT_OWNER`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MAT_FLAG`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MAT_FLAG` (
                            `ID` varchar(32) NOT NULL DEFAULT '1',
                            `FLAG` varchar(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE` (
                           `ID` varchar(64) NOT NULL,
                           `MEASURE_OWNER_ID` varchar(40) NOT NULL,
                           `ABBR_NAME` varchar(45) DEFAULT NULL,
                           `DESCRIPTION` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                           `MEASURE_STATUS` varchar(32) DEFAULT NULL,
                           `EXPORT_TS` timestamp NULL DEFAULT NULL,
                           `LOCKED_OUT_DATE` timestamp NULL DEFAULT NULL,
                           `LOCKED_USER_ID` varchar(40) DEFAULT NULL,
                           `SCORING` varchar(200) NOT NULL,
                           `MEASURE_SET_ID` varchar(36) NOT NULL,
                           `FINALIZED_DATE` timestamp NULL DEFAULT NULL,
                           `DRAFT` tinyint(1) NOT NULL DEFAULT '1',
                           `VERSION` decimal(6,3) NOT NULL DEFAULT '0.000',
                           `VALUE_SET_DATE` timestamp NULL DEFAULT NULL,
                           `EMEASURE_ID` int DEFAULT '0',
                           `PRIVATE` tinyint(1) NOT NULL DEFAULT '0',
                           `DELETED` varchar(20) DEFAULT NULL,
                           `REVISION_NUMBER` int(3) unsigned zerofill DEFAULT '000',
                           `RELEASE_VERSION` varchar(50) DEFAULT NULL,
                           `LAST_MODIFIED_ON` timestamp NULL DEFAULT NULL,
                           `LAST_MODIFIED_BY` varchar(64) DEFAULT NULL,
                           `PATIENT_BASED` tinyint(1) DEFAULT NULL,
                           `QDM_VERSION` varchar(45) DEFAULT NULL,
                           `IS_COMPOSITE_MEASURE` tinyint(1) NOT NULL DEFAULT '0',
                           `COMPOSITE_SCORING` varchar(200) DEFAULT NULL,
                           `MEASURE_STEWARD_ID` int DEFAULT NULL,
                           `NQF_NUMBER` text,
                           `MEASUREMENT_PERIOD_FROM` timestamp NULL DEFAULT NULL,
                           `MEASUREMENT_PERIOD_TO` timestamp NULL DEFAULT NULL,
                           `CQL_NAME` varchar(500) DEFAULT NULL,
                           `MEASURE_MODEL` varchar(10) DEFAULT NULL,
                           `SOURCE_MEASURE_ID` varchar(64) DEFAULT NULL,
                           `FHIR_VERSION` varchar(45) DEFAULT NULL,
                           `EXPERIMENTAL` bit(1) DEFAULT b'0',
                           `POPULATION_BASIS` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`ID`),
                           KEY `MEASURE_OWNER_FK` (`MEASURE_OWNER_ID`),
                           KEY `MEASURE_LOCK_USER_FK` (`LOCKED_USER_ID`),
                           KEY `MEASURE_SET_FK` (`MEASURE_SET_ID`),
                           KEY `LAST_MODIFIED_BY` (`LAST_MODIFIED_BY`),
                           KEY `MEASURE_STEWARD_ID` (`MEASURE_STEWARD_ID`),
                           KEY `idx_measure_cql_name` (`CQL_NAME`),
                           CONSTRAINT `MEASURE_ibfk_1` FOREIGN KEY (`LAST_MODIFIED_BY`) REFERENCES `USER` (`USER_ID`),
                           CONSTRAINT `MEASURE_ibfk_2` FOREIGN KEY (`MEASURE_STEWARD_ID`) REFERENCES `ORGANIZATION` (`ORG_ID`),
                           CONSTRAINT `MEASURE_LOCK_USER_FK` FOREIGN KEY (`LOCKED_USER_ID`) REFERENCES `USER` (`USER_ID`),
                           CONSTRAINT `MEASURE_OWNER_FK` FOREIGN KEY (`MEASURE_OWNER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                           CONSTRAINT `MEASURE_SET_FK` FOREIGN KEY (`MEASURE_SET_ID`) REFERENCES `MEASURE_SET` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_AUDIT_LOG`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_AUDIT_LOG` (
                                     `ID` varchar(32) NOT NULL,
                                     `MEASURE_ID` varchar(32) NOT NULL,
                                     `ACTIVITY_TYPE` varchar(40) NOT NULL,
                                     `USER_ID` varchar(40) NOT NULL,
                                     `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     `ADDL_INFO` varchar(2000) DEFAULT NULL,
                                     PRIMARY KEY (`ID`),
                                     KEY `MEASURE_ID_FK` (`MEASURE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_AUDIT_LOG_BACKUP`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_AUDIT_LOG_BACKUP` (
                                            `ID` varchar(32) NOT NULL,
                                            `MEASURE_ID` varchar(32) NOT NULL,
                                            `ACTIVITY_TYPE` varchar(40) NOT NULL,
                                            `USER_ID` varchar(40) NOT NULL,
                                            `TIMESTAMP` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
                                            `ADDL_INFO` varchar(2000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_BACKUP`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_BACKUP` (
                                  `ID` varchar(64) NOT NULL,
                                  `MEASURE_OWNER_ID` varchar(40) NOT NULL,
                                  `ABBR_NAME` varchar(45) DEFAULT NULL,
                                  `DESCRIPTION` varchar(2000) DEFAULT NULL,
                                  `MEASURE_STATUS` varchar(32) DEFAULT NULL,
                                  `EXPORT_TS` timestamp NULL DEFAULT NULL,
                                  `LOCKED_OUT_DATE` timestamp NULL DEFAULT NULL,
                                  `LOCKED_USER_ID` varchar(40) DEFAULT NULL,
                                  `SCORING` varchar(200) NOT NULL,
                                  `MEASURE_SET_ID` varchar(36) NOT NULL,
                                  `FINALIZED_DATE` timestamp NULL DEFAULT NULL,
                                  `DRAFT` tinyint(1) NOT NULL DEFAULT '1',
                                  `VERSION` decimal(6,3) NOT NULL DEFAULT '0.000',
                                  `VALUE_SET_DATE` timestamp NULL DEFAULT NULL,
                                  `EMEASURE_ID` int DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_BACKUP_OCT2015`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_BACKUP_OCT2015` (
                                          `ID` varchar(64) NOT NULL,
                                          `MEASURE_OWNER_ID` varchar(40) NOT NULL,
                                          `ABBR_NAME` varchar(45) DEFAULT NULL,
                                          `DESCRIPTION` varchar(2000) DEFAULT NULL,
                                          `MEASURE_STATUS` varchar(32) DEFAULT NULL,
                                          `EXPORT_TS` timestamp NULL DEFAULT NULL,
                                          `LOCKED_OUT_DATE` timestamp NULL DEFAULT NULL,
                                          `LOCKED_USER_ID` varchar(40) DEFAULT NULL,
                                          `SCORING` varchar(200) NOT NULL,
                                          `MEASURE_SET_ID` varchar(36) NOT NULL,
                                          `FINALIZED_DATE` timestamp NULL DEFAULT NULL,
                                          `DRAFT` tinyint(1) NOT NULL DEFAULT '1',
                                          `VERSION` decimal(6,3) NOT NULL DEFAULT '0.000',
                                          `VALUE_SET_DATE` timestamp NULL DEFAULT NULL,
                                          `EMEASURE_ID` int DEFAULT '0',
                                          `PRIVATE` tinyint(1) NOT NULL DEFAULT '0',
                                          `DELETED` varchar(20) DEFAULT NULL,
                                          `REVISION_NUMBER` int(3) unsigned zerofill DEFAULT '000'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_DETAILS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_DETAILS` (
                                   `ID` int NOT NULL AUTO_INCREMENT,
                                   `MEASURE_ID` varchar(64) NOT NULL,
                                   `DESCRIPTION` longtext,
                                   `COPYRIGHT` longtext,
                                   `DISCLAIMER` longtext,
                                   `STRATIFICATION` longtext,
                                   `RISK_ADJUSTMENT` longtext,
                                   `RATE_AGGREGATION` longtext,
                                   `RATIONALE` longtext,
                                   `CLINICAL_RECOMMENDATION` longtext,
                                   `IMPROVEMENT_NOTATION` longtext,
                                   `DEFINITION` longtext,
                                   `GUIDANCE` longtext,
                                   `TRANSMISSION_FORMAT` longtext,
                                   `INITIAL_POPULATION` longtext,
                                   `DENOMINATOR` longtext,
                                   `DENOMINATOR_EXCLUSIONS` longtext,
                                   `NUMERATOR` longtext,
                                   `NUMERATOR_EXCLUSIONS` longtext,
                                   `MEASURE_OBSERVATIONS` longtext,
                                   `MEASURE_POPULATION` longtext,
                                   `MEASURE_POPULATION_EXCLUSIONS` longtext,
                                   `DENOMINATOR_EXCEPTIONS` longtext,
                                   `SUPPLEMENTAL_DATA_ELEMENTS` longtext,
                                   `MEASURE_SET` longtext,
                                   PRIMARY KEY (`ID`),
                                   KEY `MEASURE_DETAILS_MEASURE_ID_FK` (`MEASURE_ID`),
                                   CONSTRAINT `MEASURE_DETAILS_MEASURE_ID_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1739 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_DETAILS_REFERENCE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_DETAILS_REFERENCE` (
                                             `ID` int NOT NULL AUTO_INCREMENT,
                                             `MEASURE_DETAILS_ID` int NOT NULL,
                                             `REFERENCE` longtext,
                                             `REFERENCE_NUMBER` int DEFAULT NULL,
                                             `REFERENCE_TYPE` varchar(15) NOT NULL DEFAULT 'UNKNOWN',
                                             PRIMARY KEY (`ID`),
                                             KEY `MEASURE_DETAILS_REFERENCE_MEASURE_DETAILS_ID_FK` (`MEASURE_DETAILS_ID`),
                                             CONSTRAINT `MEASURE_DETAILS_REFERENCE_MEASURE_DETAILS_ID_FK` FOREIGN KEY (`MEASURE_DETAILS_ID`) REFERENCES `MEASURE_DETAILS` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20957 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_DEVELOPER_ASSOCIATION`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_DEVELOPER_ASSOCIATION` (
                                                 `ID` int NOT NULL AUTO_INCREMENT,
                                                 `MEASURE_ID` varchar(64) NOT NULL,
                                                 `MEASURE_DEVELOPER_ID` int DEFAULT NULL,
                                                 PRIMARY KEY (`ID`),
                                                 UNIQUE KEY `MEASURE_DEVELOPER_UNIQUE_CONSTRAINT` (`MEASURE_ID`,`MEASURE_DEVELOPER_ID`),
                                                 KEY `MEASURE_DEVELOPER_ID_FK` (`MEASURE_DEVELOPER_ID`),
                                                 CONSTRAINT `MEASURE_DEVELOPER_ID_FK` FOREIGN KEY (`MEASURE_DEVELOPER_ID`) REFERENCES `ORGANIZATION` (`ORG_ID`) ON DELETE CASCADE,
                                                 CONSTRAINT `MEASURE_MEASURE_DEVELOPER_MEASURE_ID_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3492 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_EXPORT`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_EXPORT` (
                                  `MEASURE_EXPORT_ID` varchar(64) NOT NULL,
                                  `MEASURE_ID` varchar(64) NOT NULL,
                                  `SIMPLE_XML` longtext NOT NULL,
                                  `CODE_LIST` longblob,
                                  `HUMAN_READABLE` longtext,
                                  `HQMF` longtext,
                                  `CQL` longtext,
                                  `ELM` longtext,
                                  `JSON` longtext,
                                  `FHIR_LIBS_JSON` longtext,
                                  `FHIR_LIBS_XML` longtext,
                                  PRIMARY KEY (`MEASURE_EXPORT_ID`),
                                  KEY `MEASURE_EXPORT_ID_MEASURE_FK` (`MEASURE_ID`),
                                  CONSTRAINT `MEASURE_EXPORT_ID_MEASURE_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_NOTES_BACKUP`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_NOTES_BACKUP` (
                                        `ID` varchar(32) NOT NULL,
                                        `MEASURE_ID` varchar(32) NOT NULL,
                                        `TITLE` varchar(200) NOT NULL,
                                        `DESCRIPTION` varchar(3000) DEFAULT NULL,
                                        `CREATE_USER_ID` varchar(40) NOT NULL,
                                        `MODIFY_USER_ID` varchar(40) DEFAULT NULL,
                                        `LAST_MODIFIED_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_NOTES_BACKUP_SEP2017`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_NOTES_BACKUP_SEP2017` (
                                                `ID` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
                                                `MEASURE_ID` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
                                                `TITLE` varchar(200) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
                                                `DESCRIPTION` varchar(3000) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
                                                `CREATE_USER_ID` varchar(40) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
                                                `MODIFY_USER_ID` varchar(40) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
                                                `LAST_MODIFIED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_SCORE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_SCORE` (
                                 `ID` varchar(32) NOT NULL,
                                 `SCORE` varchar(200) NOT NULL,
                                 PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_SET`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_SET` (
                               `ID` varchar(36) NOT NULL,
                               `NAME` varchar(200) DEFAULT NULL,
                               PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_SHARE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_SHARE` (
                                 `MEASURE_SHARE_ID` varchar(32) NOT NULL,
                                 `MEASURE_ID` varchar(32) NOT NULL,
                                 `MEASURE_OWNER_USER_ID` varchar(40) NOT NULL,
                                 `SHARE_USER_ID` varchar(40) NOT NULL,
                                 `SHARE_LEVEL_ID` varchar(32) NOT NULL,
                                 PRIMARY KEY (`MEASURE_SHARE_ID`),
                                 KEY `MEASURE_SHARE_MEASURE_FK` (`MEASURE_ID`),
                                 KEY `MEASURE_SHARE_OWNER_FK` (`MEASURE_OWNER_USER_ID`),
                                 KEY `MEASURE_SHARE_SHARE_FK` (`SHARE_USER_ID`),
                                 KEY `MEASURE_SHARE_LEVEL_ID` (`SHARE_LEVEL_ID`),
                                 CONSTRAINT `MEASURE_SHARE_LEVEL_ID` FOREIGN KEY (`SHARE_LEVEL_ID`) REFERENCES `SHARE_LEVEL` (`SHARE_LEVEL_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                                 CONSTRAINT `MEASURE_SHARE_MEASURE_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                                 CONSTRAINT `MEASURE_SHARE_OWNER_FK` FOREIGN KEY (`MEASURE_OWNER_USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                                 CONSTRAINT `MEASURE_SHARE_SHARE_FK` FOREIGN KEY (`SHARE_USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_TYPE_ASSOCIATION`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_TYPE_ASSOCIATION` (
                                            `ID` int NOT NULL AUTO_INCREMENT,
                                            `MEASURE_ID` varchar(64) NOT NULL,
                                            `MEASURE_TYPE_ID` varchar(32) NOT NULL,
                                            PRIMARY KEY (`ID`),
                                            UNIQUE KEY `MEASURE_TYPE_UNIQUE_CONSTRAINT` (`MEASURE_ID`,`MEASURE_TYPE_ID`),
                                            KEY `MEASURE_TYPE_ID_FK` (`MEASURE_TYPE_ID`),
                                            CONSTRAINT `MEASURE_MEASURE_TYPE_MEASURE_ID_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE,
                                            CONSTRAINT `MEASURE_TYPE_ID_FK` FOREIGN KEY (`MEASURE_TYPE_ID`) REFERENCES `MEASURE_TYPES` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3444 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_TYPES`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_TYPES` (
                                 `ID` varchar(32) NOT NULL,
                                 `NAME` varchar(50) NOT NULL,
                                 `ABBR_NAME` varchar(45) DEFAULT NULL,
                                 PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_VALIDATION_LOG`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_VALIDATION_LOG` (
                                          `ID` varchar(32) NOT NULL,
                                          `MEASURE_ID` varchar(32) NOT NULL,
                                          `ACTIVITY_TYPE` varchar(40) NOT NULL,
                                          `USER_ID` varchar(40) NOT NULL,
                                          `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                          `INTERIM_BLOB` longblob,
                                          PRIMARY KEY (`ID`),
                                          KEY `MEASURE_ID_FK` (`MEASURE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASURE_XML`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_XML` (
                               `ID` varchar(64) NOT NULL,
                               `MEASURE_ID` varchar(64) NOT NULL,
                               `MEASURE_XML` longblob,
                               `SEVERE_ERROR_CQL` longtext,
                               PRIMARY KEY (`ID`),
                               KEY `MEASURE_XML_FK` (`MEASURE_ID`),
                               CONSTRAINT `MEASURE_XML_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MEASUREMENT_TERM`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASUREMENT_TERM` (
                                    `ID` varchar(64) NOT NULL,
                                    `UNIT` varchar(45) DEFAULT NULL,
                                    `QUANTITY` varchar(100) NOT NULL,
                                    `DECISION_ID` varchar(64) NOT NULL,
                                    PRIMARY KEY (`ID`),
                                    KEY `DECISION_FK2` (`DECISION_ID`),
                                    CONSTRAINT `DECISION_FK2` FOREIGN KEY (`DECISION_ID`) REFERENCES `DECISION` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `METADATA`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `METADATA` (
                            `METADATA_ID` varchar(64) NOT NULL,
                            `NAME` varchar(100) NOT NULL,
                            `VALUE` varchar(30000) NOT NULL,
                            `MEASURE_ID` varchar(64) NOT NULL,
                            PRIMARY KEY (`METADATA_ID`),
                            KEY `MEASURE_ID_METADATA_FK` (`MEASURE_ID`),
                            CONSTRAINT `MEASURE_ID_METADATA_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MODES`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MODES` (
                         `ID` int NOT NULL AUTO_INCREMENT,
                         `MODE_NAME` varchar(50) NOT NULL,
                         PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `OBJECT_STATUS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `OBJECT_STATUS` (
                                 `OBJECT_STATUS_ID` varchar(32) NOT NULL,
                                 `DESCRIPTION` varchar(50) NOT NULL,
                                 PRIMARY KEY (`OBJECT_STATUS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `OPERATOR`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `OPERATOR` (
                            `ID` varchar(32) NOT NULL,
                            `LONG_NAME` varchar(45) DEFAULT NULL,
                            `SHORT_NAME` varchar(45) DEFAULT NULL,
                            `FK_OPERATOR_TYPE` varchar(32) NOT NULL,
                            PRIMARY KEY (`ID`),
                            KEY `OPERATOR_TYPE_FK` (`FK_OPERATOR_TYPE`),
                            CONSTRAINT `OPERATOR_TYPE_FK` FOREIGN KEY (`FK_OPERATOR_TYPE`) REFERENCES `OPERATOR_TYPE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `OPERATOR_BACKUP`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `OPERATOR_BACKUP` (
                                   `ID` varchar(32) NOT NULL,
                                   `LONG_NAME` varchar(45) DEFAULT NULL,
                                   `SHORT_NAME` varchar(45) DEFAULT NULL,
                                   `FK_OPERATOR_TYPE` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `OPERATOR_TYPE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `OPERATOR_TYPE` (
                                 `ID` varchar(32) NOT NULL,
                                 `NAME` varchar(45) DEFAULT NULL,
                                 PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ORGANIZATION`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ORGANIZATION` (
                                `ORG_ID` int NOT NULL AUTO_INCREMENT,
                                `ORG_NAME` varchar(150) DEFAULT NULL,
                                `ORG_OID` varchar(50) NOT NULL,
                                PRIMARY KEY (`ORG_ID`),
                                UNIQUE KEY `ORG_OID_UNIQUE` (`ORG_OID`)
) ENGINE=InnoDB AUTO_INCREMENT=215 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PACKAGER`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PACKAGER` (
                            `PACKAGER_ID` varchar(32) NOT NULL,
                            `MEASURE_ID` varchar(32) NOT NULL,
                            `CLAUSE_ID` varchar(32) NOT NULL,
                            `SEQUENCE` int NOT NULL,
                            PRIMARY KEY (`PACKAGER_ID`),
                            KEY `PACKAGER_MEASURE_FK` (`MEASURE_ID`),
                            KEY `PACKAGER_CLAUSE_FK` (`CLAUSE_ID`),
                            CONSTRAINT `PACKAGER_CLAUSE_FK` FOREIGN KEY (`CLAUSE_ID`) REFERENCES `CLAUSE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                            CONSTRAINT `PACKAGER_MEASURE_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QDM_ATTRIBUTES`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QDM_ATTRIBUTES` (
                                  `ID` int NOT NULL AUTO_INCREMENT,
                                  `NAME` varchar(32) NOT NULL,
                                  `DATA_TYPE_ID` varchar(32) NOT NULL,
                                  `QDM_ATTRIBUTE_TYPE` varchar(32) NOT NULL,
                                  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=852 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QDM_ATTRIBUTES_BACKUP`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QDM_ATTRIBUTES_BACKUP` (
                                         `ID` varchar(64) NOT NULL,
                                         `NAME` varchar(100) DEFAULT NULL,
                                         `DATA_TYPE_ID` varchar(32) DEFAULT NULL,
                                         `QDM_ATTRIBUTE_TYPE` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QDM_ATTRIBUTES_BACKUP_AUG2015`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QDM_ATTRIBUTES_BACKUP_AUG2015` (
                                                 `ID` varchar(64) NOT NULL,
                                                 `NAME` varchar(100) DEFAULT NULL,
                                                 `DATA_TYPE_ID` varchar(32) DEFAULT NULL,
                                                 `QDM_ATTRIBUTE_TYPE` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QDM_ATTRIBUTES_BACKUP_SEP2015`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QDM_ATTRIBUTES_BACKUP_SEP2015` (
                                                 `ID` varchar(64) NOT NULL,
                                                 `NAME` varchar(100) DEFAULT NULL,
                                                 `DATA_TYPE_ID` varchar(32) DEFAULT NULL,
                                                 `QDM_ATTRIBUTE_TYPE` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QDM_TERM`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QDM_TERM` (
                            `ID` varchar(64) NOT NULL,
                            `QDM_ELEMENT_ID` varchar(64) NOT NULL,
                            `DECISION_ID` varchar(64) NOT NULL,
                            PRIMARY KEY (`ID`),
                            KEY `DECISION_FK4` (`DECISION_ID`),
                            KEY `QUALITY_DATA_SET_FK2` (`QDM_ELEMENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QUALITY_DATA_MODEL`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QUALITY_DATA_MODEL` (
                                      `QUALITY_DATA_MODEL_ID` varchar(36) NOT NULL,
                                      `DATA_TYPE_ID` varchar(32) NOT NULL,
                                      `LIST_OBJECT_ID` varchar(32) NOT NULL,
                                      `MEASURE_ID` varchar(32) NOT NULL,
                                      `VERSION` varchar(32) NOT NULL,
                                      `OID` varchar(255) DEFAULT NULL,
                                      `OCCURRENCE` varchar(200) DEFAULT NULL,
                                      `IS_SUPP_DATA_ELEMENT` tinyint(1) NOT NULL DEFAULT '0',
                                      PRIMARY KEY (`QUALITY_DATA_MODEL_ID`),
                                      KEY `QDM_DATA_TYPE_FK` (`DATA_TYPE_ID`),
                                      KEY `QDM_CODE_LIST_FK` (`LIST_OBJECT_ID`),
                                      KEY `QDM_MEASURE_ID_FK` (`MEASURE_ID`),
                                      CONSTRAINT `QDM_CODE_LIST_FK` FOREIGN KEY (`LIST_OBJECT_ID`) REFERENCES `LIST_OBJECT` (`LIST_OBJECT_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                                      CONSTRAINT `QDM_DATA_TYPE_FK` FOREIGN KEY (`DATA_TYPE_ID`) REFERENCES `DATA_TYPE` (`DATA_TYPE_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                                      CONSTRAINT `QDM_MEASURE_ID_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `QUALITY_DATA_MODEL_OID_GEN`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QUALITY_DATA_MODEL_OID_GEN` (
                                              `OID_GEN_ID` bigint NOT NULL AUTO_INCREMENT,
                                              PRIMARY KEY (`OID_GEN_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11034 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RECENT_CQL_ACTIVITY_LOG`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RECENT_CQL_ACTIVITY_LOG` (
                                           `ID` varchar(32) NOT NULL,
                                           `CQL_ID` varchar(64) DEFAULT NULL,
                                           `USER_ID` varchar(40) NOT NULL,
                                           `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                           PRIMARY KEY (`ID`),
                                           KEY `CQL_ID_FK` (`CQL_ID`),
                                           KEY `USER_ID_FK` (`USER_ID`),
                                           CONSTRAINT `RECENT_CQL_ACTIVITY_LOG_ibfk_1` FOREIGN KEY (`CQL_ID`) REFERENCES `CQL_LIBRARY` (`ID`) ON DELETE CASCADE,
                                           CONSTRAINT `RECENT_CQL_ACTIVITY_LOG_ibfk_2` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `RECENT_MSR_ACTIVITY_LOG`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RECENT_MSR_ACTIVITY_LOG` (
                                           `ID` varchar(32) NOT NULL,
                                           `MEASURE_ID` varchar(32) NOT NULL,
                                           `USER_ID` varchar(40) NOT NULL,
                                           `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                           PRIMARY KEY (`ID`),
                                           KEY `MEASURE_ID_FK` (`MEASURE_ID`),
                                           KEY `USER_ID_FK` (`USER_ID`),
                                           CONSTRAINT `RECENT_MSR_ACTIVITY_LOG_ibfk_1` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE,
                                           CONSTRAINT `RECENT_MSR_ACTIVITY_LOG_ibfk_2` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SECURITY_QUESTIONS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SECURITY_QUESTIONS` (
                                      `QUESTION_ID` int NOT NULL,
                                      `QUESTION` varchar(100) NOT NULL,
                                      PRIMARY KEY (`QUESTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SECURITY_ROLE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SECURITY_ROLE` (
                                 `SECURITY_ROLE_ID` varchar(32) NOT NULL,
                                 `DESCRIPTION` varchar(50) NOT NULL,
                                 PRIMARY KEY (`SECURITY_ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_BLKEXCOL_PID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_BLKEXCOL_PID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_BLKEXCOLFILE_PID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_BLKEXCOLFILE_PID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_BLKEXJOB_PID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_BLKEXJOB_PID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_CNCPT_MAP_GRP_ELM_TGT_PID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_CNCPT_MAP_GRP_ELM_TGT_PID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_CODESYSTEM_PID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_CODESYSTEM_PID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_CODESYSTEMVER_PID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_CODESYSTEMVER_PID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_CONCEPT_DESIG_PID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_CONCEPT_DESIG_PID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_CONCEPT_MAP_GROUP_PID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_CONCEPT_MAP_GROUP_PID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_CONCEPT_MAP_GRP_ELM_PID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_CONCEPT_MAP_GRP_ELM_PID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_CONCEPT_MAP_PID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_CONCEPT_MAP_PID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_CONCEPT_PC_PID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_CONCEPT_PC_PID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_CONCEPT_PID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_CONCEPT_PID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_CONCEPT_PROP_PID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_CONCEPT_PROP_PID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_FORCEDID_ID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_FORCEDID_ID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_HISTORYTAG_ID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_HISTORYTAG_ID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_IDXCMPSTRUNIQ_ID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_IDXCMPSTRUNIQ_ID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_RES_REINDEX_JOB`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_RES_REINDEX_JOB` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_RESLINK_ID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_RESLINK_ID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_RESOURCE_HISTORY_ID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_RESOURCE_HISTORY_ID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_RESOURCE_ID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_RESOURCE_ID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_RESPARMPRESENT_ID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_RESPARMPRESENT_ID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_RESTAG_ID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_RESTAG_ID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_SEARCH`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_SEARCH` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_SEARCH_INC`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_SEARCH_INC` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_SEARCH_RES`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_SEARCH_RES` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_SPIDX_COORDS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_SPIDX_COORDS` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_SPIDX_DATE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_SPIDX_DATE` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_SPIDX_NUMBER`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_SPIDX_NUMBER` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_SPIDX_QUANTITY`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_SPIDX_QUANTITY` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_SPIDX_STRING`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_SPIDX_STRING` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_SPIDX_TOKEN`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_SPIDX_TOKEN` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_SPIDX_URI`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_SPIDX_URI` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_SUBSCRIPTION_ID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_SUBSCRIPTION_ID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_TAGDEF_ID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_TAGDEF_ID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_VALUESET_C_DSGNTN_PID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_VALUESET_C_DSGNTN_PID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_VALUESET_CONCEPT_PID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_VALUESET_CONCEPT_PID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SEQ_VALUESET_PID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SEQ_VALUESET_PID` (
    `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `SHARE_LEVEL`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SHARE_LEVEL` (
                               `SHARE_LEVEL_ID` varchar(32) NOT NULL,
                               `DESCRIPTION` varchar(50) DEFAULT NULL,
                               PRIMARY KEY (`SHARE_LEVEL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `STATUS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STATUS` (
                          `STATUS_ID` varchar(32) NOT NULL,
                          `DESCRIPTION` varchar(50) NOT NULL,
                          PRIMARY KEY (`STATUS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `STEWARD_ORG`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STEWARD_ORG` (
                               `ID` varchar(32) NOT NULL,
                               `ORG_NAME` varchar(200) NOT NULL,
                               `ORG_OID` varchar(100) DEFAULT NULL,
                               PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRANSACTION_AUDIT_LOG`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRANSACTION_AUDIT_LOG` (
                                         `ID` varchar(32) NOT NULL,
                                         `PRIMARY_ID` varchar(40) DEFAULT NULL,
                                         `SECONDARY_ID` varchar(40) DEFAULT NULL,
                                         `ACTIVITY_TYPE` varchar(40) NOT NULL,
                                         `USER_ID` varchar(40) NOT NULL,
                                         `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         `ADDL_INFO` varchar(2000) DEFAULT NULL,
                                         PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRM_CODESYSTEM`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRM_CODESYSTEM` (
                                  `PID` bigint NOT NULL,
                                  `CODE_SYSTEM_URI` varchar(200) NOT NULL,
                                  `CURRENT_VERSION_PID` bigint DEFAULT NULL,
                                  `CS_NAME` varchar(200) DEFAULT NULL,
                                  `RES_ID` bigint DEFAULT NULL,
                                  PRIMARY KEY (`PID`),
                                  UNIQUE KEY `IDX_CS_CODESYSTEM` (`CODE_SYSTEM_URI`),
                                  KEY `FK_TRMCODESYSTEM_CURVER` (`CURRENT_VERSION_PID`),
                                  KEY `FK_TRMCODESYSTEM_RES` (`RES_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRM_CODESYSTEM_VER`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRM_CODESYSTEM_VER` (
                                      `PID` bigint NOT NULL,
                                      `CS_DISPLAY` varchar(200) DEFAULT NULL,
                                      `CODESYSTEM_PID` bigint DEFAULT NULL,
                                      `CS_VERSION_ID` varchar(200) DEFAULT NULL,
                                      `RES_ID` bigint NOT NULL,
                                      PRIMARY KEY (`PID`),
                                      KEY `FK_CODESYSVER_CS_ID` (`CODESYSTEM_PID`),
                                      KEY `FK_CODESYSVER_RES_ID` (`RES_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRM_CONCEPT`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRM_CONCEPT` (
                               `PID` bigint NOT NULL,
                               `CODEVAL` varchar(500) NOT NULL,
                               `CODESYSTEM_PID` bigint DEFAULT NULL,
                               `DISPLAY` varchar(400) DEFAULT NULL,
                               `INDEX_STATUS` bigint DEFAULT NULL,
                               `PARENT_PIDS` longtext,
                               `CODE_SEQUENCE` int DEFAULT NULL,
                               `CONCEPT_UPDATED` datetime DEFAULT NULL,
                               PRIMARY KEY (`PID`),
                               UNIQUE KEY `IDX_CONCEPT_CS_CODE` (`CODESYSTEM_PID`,`CODEVAL`),
                               KEY `IDX_CONCEPT_INDEXSTATUS` (`INDEX_STATUS`),
                               KEY `IDX_CONCEPT_UPDATED` (`CONCEPT_UPDATED`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRM_CONCEPT_DESIG`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRM_CONCEPT_DESIG` (
                                     `PID` bigint NOT NULL,
                                     `LANG` varchar(500) DEFAULT NULL,
                                     `USE_CODE` varchar(500) DEFAULT NULL,
                                     `USE_DISPLAY` varchar(500) DEFAULT NULL,
                                     `USE_SYSTEM` varchar(500) DEFAULT NULL,
                                     `VAL` varchar(2000) NOT NULL,
                                     `CS_VER_PID` bigint DEFAULT NULL,
                                     `CONCEPT_PID` bigint DEFAULT NULL,
                                     PRIMARY KEY (`PID`),
                                     KEY `FK_CONCEPTDESIG_CSV` (`CS_VER_PID`),
                                     KEY `FK_CONCEPTDESIG_CONCEPT` (`CONCEPT_PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRM_CONCEPT_MAP`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRM_CONCEPT_MAP` (
                                   `PID` bigint NOT NULL,
                                   `RES_ID` bigint DEFAULT NULL,
                                   `SOURCE_URL` varchar(200) DEFAULT NULL,
                                   `TARGET_URL` varchar(200) DEFAULT NULL,
                                   `URL` varchar(200) NOT NULL,
                                   PRIMARY KEY (`PID`),
                                   UNIQUE KEY `IDX_CONCEPT_MAP_URL` (`URL`),
                                   KEY `FK_TRMCONCEPTMAP_RES` (`RES_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRM_CONCEPT_MAP_GROUP`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRM_CONCEPT_MAP_GROUP` (
                                         `PID` bigint NOT NULL,
                                         `CONCEPT_MAP_URL` varchar(200) DEFAULT NULL,
                                         `SOURCE_URL` varchar(200) NOT NULL,
                                         `SOURCE_VS` varchar(200) DEFAULT NULL,
                                         `SOURCE_VERSION` varchar(200) DEFAULT NULL,
                                         `TARGET_URL` varchar(200) NOT NULL,
                                         `TARGET_VS` varchar(200) DEFAULT NULL,
                                         `TARGET_VERSION` varchar(200) DEFAULT NULL,
                                         `CONCEPT_MAP_PID` bigint NOT NULL,
                                         PRIMARY KEY (`PID`),
                                         KEY `FK_TCMGROUP_CONCEPTMAP` (`CONCEPT_MAP_PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRM_CONCEPT_MAP_GRP_ELEMENT`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRM_CONCEPT_MAP_GRP_ELEMENT` (
                                               `PID` bigint NOT NULL,
                                               `SOURCE_CODE` varchar(500) NOT NULL,
                                               `CONCEPT_MAP_URL` varchar(200) DEFAULT NULL,
                                               `SOURCE_DISPLAY` varchar(400) DEFAULT NULL,
                                               `SYSTEM_URL` varchar(200) DEFAULT NULL,
                                               `SYSTEM_VERSION` varchar(200) DEFAULT NULL,
                                               `VALUESET_URL` varchar(200) DEFAULT NULL,
                                               `CONCEPT_MAP_GROUP_PID` bigint NOT NULL,
                                               PRIMARY KEY (`PID`),
                                               KEY `IDX_CNCPT_MAP_GRP_CD` (`SOURCE_CODE`),
                                               KEY `FK_TCMGELEMENT_GROUP` (`CONCEPT_MAP_GROUP_PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRM_CONCEPT_MAP_GRP_ELM_TGT`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRM_CONCEPT_MAP_GRP_ELM_TGT` (
                                               `PID` bigint NOT NULL,
                                               `TARGET_CODE` varchar(500) NOT NULL,
                                               `CONCEPT_MAP_URL` varchar(200) DEFAULT NULL,
                                               `TARGET_DISPLAY` varchar(400) DEFAULT NULL,
                                               `TARGET_EQUIVALENCE` varchar(50) DEFAULT NULL,
                                               `SYSTEM_URL` varchar(200) DEFAULT NULL,
                                               `SYSTEM_VERSION` varchar(200) DEFAULT NULL,
                                               `VALUESET_URL` varchar(200) DEFAULT NULL,
                                               `CONCEPT_MAP_GRP_ELM_PID` bigint NOT NULL,
                                               PRIMARY KEY (`PID`),
                                               KEY `IDX_CNCPT_MP_GRP_ELM_TGT_CD` (`TARGET_CODE`),
                                               KEY `FK_TCMGETARGET_ELEMENT` (`CONCEPT_MAP_GRP_ELM_PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRM_CONCEPT_PC_LINK`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRM_CONCEPT_PC_LINK` (
                                       `PID` bigint NOT NULL,
                                       `CHILD_PID` bigint DEFAULT NULL,
                                       `CODESYSTEM_PID` bigint NOT NULL,
                                       `PARENT_PID` bigint DEFAULT NULL,
                                       `REL_TYPE` int DEFAULT NULL,
                                       PRIMARY KEY (`PID`),
                                       KEY `FK_TERM_CONCEPTPC_CHILD` (`CHILD_PID`),
                                       KEY `FK_TERM_CONCEPTPC_CS` (`CODESYSTEM_PID`),
                                       KEY `FK_TERM_CONCEPTPC_PARENT` (`PARENT_PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRM_CONCEPT_PROPERTY`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRM_CONCEPT_PROPERTY` (
                                        `PID` bigint NOT NULL,
                                        `PROP_CODESYSTEM` varchar(500) DEFAULT NULL,
                                        `PROP_DISPLAY` varchar(500) DEFAULT NULL,
                                        `PROP_KEY` varchar(500) NOT NULL,
                                        `PROP_TYPE` int NOT NULL,
                                        `PROP_VAL` varchar(500) DEFAULT NULL,
                                        `PROP_VAL_LOB` longblob,
                                        `CS_VER_PID` bigint DEFAULT NULL,
                                        `CONCEPT_PID` bigint DEFAULT NULL,
                                        PRIMARY KEY (`PID`),
                                        KEY `FK_CONCEPTPROP_CSV` (`CS_VER_PID`),
                                        KEY `FK_CONCEPTPROP_CONCEPT` (`CONCEPT_PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRM_VALUESET`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRM_VALUESET` (
                                `PID` bigint NOT NULL,
                                `EXPANSION_STATUS` varchar(50) NOT NULL,
                                `VSNAME` varchar(200) DEFAULT NULL,
                                `RES_ID` bigint DEFAULT NULL,
                                `TOTAL_CONCEPT_DESIGNATIONS` bigint NOT NULL DEFAULT '0',
                                `TOTAL_CONCEPTS` bigint NOT NULL DEFAULT '0',
                                `URL` varchar(200) NOT NULL,
                                PRIMARY KEY (`PID`),
                                UNIQUE KEY `IDX_VALUESET_URL` (`URL`),
                                KEY `FK_TRMVALUESET_RES` (`RES_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRM_VALUESET_C_DESIGNATION`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRM_VALUESET_C_DESIGNATION` (
                                              `PID` bigint NOT NULL,
                                              `VALUESET_CONCEPT_PID` bigint NOT NULL,
                                              `LANG` varchar(500) DEFAULT NULL,
                                              `USE_CODE` varchar(500) DEFAULT NULL,
                                              `USE_DISPLAY` varchar(500) DEFAULT NULL,
                                              `USE_SYSTEM` varchar(500) DEFAULT NULL,
                                              `VAL` varchar(2000) NOT NULL,
                                              `VALUESET_PID` bigint NOT NULL,
                                              PRIMARY KEY (`PID`),
                                              KEY `FK_TRM_VALUESET_CONCEPT_PID` (`VALUESET_CONCEPT_PID`),
                                              KEY `FK_TRM_VSCD_VS_PID` (`VALUESET_PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `TRM_VALUESET_CONCEPT`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRM_VALUESET_CONCEPT` (
                                        `PID` bigint NOT NULL,
                                        `CODEVAL` varchar(500) NOT NULL,
                                        `DISPLAY` varchar(400) DEFAULT NULL,
                                        `VALUESET_ORDER` int NOT NULL,
                                        `SYSTEM_URL` varchar(200) NOT NULL,
                                        `VALUESET_PID` bigint NOT NULL,
                                        PRIMARY KEY (`PID`),
                                        UNIQUE KEY `IDX_VS_CONCEPT_CS_CD` (`VALUESET_PID`,`SYSTEM_URL`,`CODEVAL`),
                                        UNIQUE KEY `IDX_VS_CONCEPT_ORDER` (`VALUESET_PID`,`VALUESET_ORDER`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UNIT`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UNIT` (
                        `ID` varchar(32) NOT NULL,
                        `NAME` varchar(45) DEFAULT NULL,
                        `SORT_ORDER` int NOT NULL DEFAULT '0',
                        `CQL_UNIT` varchar(60) DEFAULT NULL,
                        PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UNIT_TYPE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UNIT_TYPE` (
                             `ID` varchar(32) NOT NULL,
                             `NAME` varchar(45) DEFAULT NULL,
                             PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `UNIT_TYPE_MATRIX`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UNIT_TYPE_MATRIX` (
                                    `ID` varchar(32) NOT NULL,
                                    `FK_UNIT_ID` varchar(32) NOT NULL,
                                    `FK_UNIT_TYPE_ID` varchar(32) NOT NULL,
                                    PRIMARY KEY (`ID`),
                                    KEY `UNIT_FK` (`FK_UNIT_ID`),
                                    KEY `UNIT_TYPE_FK` (`FK_UNIT_TYPE_ID`),
                                    CONSTRAINT `UNIT_FK` FOREIGN KEY (`FK_UNIT_ID`) REFERENCES `UNIT` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                                    CONSTRAINT `UNIT_TYPE_FK` FOREIGN KEY (`FK_UNIT_TYPE_ID`) REFERENCES `UNIT_TYPE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER` (
                        `USER_ID` varchar(40) NOT NULL,
                        `FIRST_NAME` varchar(100) NOT NULL,
                        `MIDDLE_INITIAL` varchar(45) DEFAULT NULL,
                        `LAST_NAME` varchar(100) NOT NULL,
                        `EMAIL_ADDRESS` varchar(254) NOT NULL,
                        `PHONE_NO` varchar(45) NOT NULL,
                        `TITLE` varchar(45) DEFAULT NULL,
                        `TERMINATION_DATE` date DEFAULT NULL,
                        `ACTIVATION_DATE` date NOT NULL,
                        `SIGN_IN_DATE` timestamp NULL DEFAULT NULL,
                        `SIGN_OUT_DATE` timestamp NULL DEFAULT NULL,
                        `LOCKED_OUT_DATE` timestamp NULL DEFAULT NULL,
                        `STATUS_ID` varchar(32) NOT NULL,
                        `AUDIT_ID` varchar(32) NOT NULL,
                        `SECURITY_ROLE_ID` varchar(32) NOT NULL,
                        `LOGIN_ID` varchar(45) DEFAULT NULL,
                        `ORG_ID` int NOT NULL,
                        `SESSION_ID` varchar(64) DEFAULT NULL,
                        `HARP_ID` varchar(45) DEFAULT NULL,
                        `FHIR_FLAG` bit(1) DEFAULT b'0',
                        PRIMARY KEY (`USER_ID`),
                        UNIQUE KEY `LOGIN_ID_UNIQUE` (`LOGIN_ID`),
                        KEY `USER_SECURITY_ROLE_FK` (`SECURITY_ROLE_ID`),
                        KEY `USER_AUDIT_FK` (`AUDIT_ID`),
                        KEY `USER_STATUS_FK` (`STATUS_ID`),
                        KEY `ORG_ID_FK_idx` (`ORG_ID`),
                        CONSTRAINT `ORG_ID_FK` FOREIGN KEY (`ORG_ID`) REFERENCES `ORGANIZATION` (`ORG_ID`),
                        CONSTRAINT `USER_AUDIT_FK` FOREIGN KEY (`AUDIT_ID`) REFERENCES `AUDIT_LOG` (`AUDIT_LOG_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                        CONSTRAINT `USER_SECURITY_ROLE_FK` FOREIGN KEY (`SECURITY_ROLE_ID`) REFERENCES `SECURITY_ROLE` (`SECURITY_ROLE_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
                        CONSTRAINT `USER_STATUS_FK` FOREIGN KEY (`STATUS_ID`) REFERENCES `STATUS` (`STATUS_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_AUDIT_LOG`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_AUDIT_LOG` (
                                  `ID` varchar(32) NOT NULL,
                                  `USER_ID` varchar(32) NOT NULL,
                                  `ACTION_TYPE` varchar(32) NOT NULL,
                                  `ACTIVITY_TYPE` varchar(40) NOT NULL,
                                  `USER_EMAIL` varchar(40) NOT NULL,
                                  `TIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  `ADDL_INFO` varchar(2000) DEFAULT NULL,
                                  PRIMARY KEY (`ID`),
                                  KEY `AUDIT_USER_FK` (`USER_ID`),
                                  CONSTRAINT `AUDIT_USER_FK` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_BACKUP`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_BACKUP` (
                               `USER_ID` varchar(40) NOT NULL,
                               `FIRST_NAME` varchar(100) NOT NULL,
                               `MIDDLE_INITIAL` varchar(45) DEFAULT NULL,
                               `LAST_NAME` varchar(100) NOT NULL,
                               `EMAIL_ADDRESS` varchar(254) NOT NULL,
                               `PHONE_NO` varchar(45) NOT NULL,
                               `TITLE` varchar(45) DEFAULT NULL,
                               `TERMINATION_DATE` date DEFAULT NULL,
                               `ACTIVATION_DATE` date NOT NULL,
                               `SIGN_IN_DATE` timestamp NULL DEFAULT NULL,
                               `SIGN_OUT_DATE` timestamp NULL DEFAULT NULL,
                               `LOCKED_OUT_DATE` timestamp NULL DEFAULT NULL,
                               `STATUS_ID` varchar(32) NOT NULL,
                               `AUDIT_ID` varchar(32) NOT NULL,
                               `SECURITY_ROLE_ID` varchar(32) NOT NULL,
                               `ORGANIZATION_NAME` varchar(80) NOT NULL,
                               `ORG_OID` varchar(50) NOT NULL,
                               `ROOT_OID` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_BACKUP_FOR_ORGANIZATION`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_BACKUP_FOR_ORGANIZATION` (
                                                `USER_ID` varchar(40) NOT NULL,
                                                `FIRST_NAME` varchar(100) NOT NULL,
                                                `MIDDLE_INITIAL` varchar(45) DEFAULT NULL,
                                                `LAST_NAME` varchar(100) NOT NULL,
                                                `EMAIL_ADDRESS` varchar(254) NOT NULL,
                                                `PHONE_NO` varchar(45) NOT NULL,
                                                `TITLE` varchar(45) DEFAULT NULL,
                                                `TERMINATION_DATE` date DEFAULT NULL,
                                                `ACTIVATION_DATE` date NOT NULL,
                                                `SIGN_IN_DATE` timestamp NULL DEFAULT NULL,
                                                `SIGN_OUT_DATE` timestamp NULL DEFAULT NULL,
                                                `LOCKED_OUT_DATE` timestamp NULL DEFAULT NULL,
                                                `STATUS_ID` varchar(32) NOT NULL,
                                                `AUDIT_ID` varchar(32) NOT NULL,
                                                `SECURITY_ROLE_ID` varchar(32) NOT NULL,
                                                `ORGANIZATION_NAME` varchar(80) NOT NULL,
                                                `ORG_OID` varchar(50) NOT NULL,
                                                `LOGIN_ID` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_BACKUP_FOR_ROOTID`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_BACKUP_FOR_ROOTID` (
                                          `USER_ID` varchar(40) NOT NULL,
                                          `FIRST_NAME` varchar(100) NOT NULL,
                                          `MIDDLE_INITIAL` varchar(45) DEFAULT NULL,
                                          `LAST_NAME` varchar(100) NOT NULL,
                                          `EMAIL_ADDRESS` varchar(254) NOT NULL,
                                          `PHONE_NO` varchar(45) NOT NULL,
                                          `TITLE` varchar(45) DEFAULT NULL,
                                          `TERMINATION_DATE` date DEFAULT NULL,
                                          `ACTIVATION_DATE` date NOT NULL,
                                          `SIGN_IN_DATE` timestamp NULL DEFAULT NULL,
                                          `SIGN_OUT_DATE` timestamp NULL DEFAULT NULL,
                                          `LOCKED_OUT_DATE` timestamp NULL DEFAULT NULL,
                                          `STATUS_ID` varchar(32) NOT NULL,
                                          `AUDIT_ID` varchar(32) NOT NULL,
                                          `SECURITY_ROLE_ID` varchar(32) NOT NULL,
                                          `ORGANIZATION_NAME` varchar(80) NOT NULL,
                                          `ORG_OID` varchar(50) NOT NULL,
                                          `ROOT_OID` varchar(50) NOT NULL,
                                          `LOGIN_ID` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_BONNIE_ACCESS_INFO`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_BONNIE_ACCESS_INFO` (
                                           `ID` int NOT NULL AUTO_INCREMENT,
                                           `USER_ID` varchar(40) NOT NULL,
                                           `REFRESH_TOKEN` varchar(250) NOT NULL,
                                           `ACCESS_TOKEN` varchar(250) NOT NULL,
                                           PRIMARY KEY (`ID`),
                                           KEY `USER_ID` (`USER_ID`),
                                           CONSTRAINT `USER_BONNIE_ACCESS_INFO_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=316 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_PASSWORD`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_PASSWORD` (
                                 `USER_PASSWORD_ID` varchar(32) NOT NULL,
                                 `USER_ID` varchar(40) NOT NULL,
                                 `PWD_LOCK_COUNTER` int DEFAULT NULL,
                                 `FORGOT_PWD_LOCK_COUNTER` int DEFAULT NULL,
                                 `PASSWORD` varchar(100) NOT NULL,
                                 `SALT` varchar(100) NOT NULL,
                                 `INITIAL_PWD` tinyint(1) DEFAULT '0',
                                 `CREATE_DATE` date NOT NULL,
                                 `FIRST_FAILED_ATTEMPT_TIME` timestamp NULL DEFAULT NULL,
                                 `TEMP_PWD` tinyint(1) DEFAULT '0',
                                 PRIMARY KEY (`USER_PASSWORD_ID`),
                                 UNIQUE KEY `USER_ID_UNIQUE` (`USER_ID`),
                                 KEY `PASSWORD_USER_FK` (`USER_ID`),
                                 CONSTRAINT `PASSWORD_USER_FK` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_PASSWORD_HISTORY`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_PASSWORD_HISTORY` (
                                         `USER_PASSWORD_HISTORY_ID` varchar(32) NOT NULL,
                                         `USER_ID` varchar(40) NOT NULL,
                                         `PASSWORD` varchar(100) NOT NULL,
                                         `SALT` varchar(100) NOT NULL,
                                         `CREATE_DATE` date NOT NULL,
                                         PRIMARY KEY (`USER_PASSWORD_HISTORY_ID`),
                                         KEY `PASSWORD_HISTORY_USER_FK` (`USER_ID`),
                                         CONSTRAINT `PASSWORD_HISTORY_USER_FK` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_PASSWORD_TEMP`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_PASSWORD_TEMP` (
                                      `USER_PASSWORD_ID` varchar(32) NOT NULL,
                                      `USER_ID` varchar(40) NOT NULL,
                                      `PWD_LOCK_COUNTER` int DEFAULT NULL,
                                      `FORGOT_PWD_LOCK_COUNTER` int DEFAULT NULL,
                                      `PASSWORD` varchar(100) NOT NULL,
                                      `SALT` varchar(100) NOT NULL,
                                      `INITIAL_PWD` tinyint(1) DEFAULT '0',
                                      `CREATE_DATE` date NOT NULL,
                                      `FIRST_FAILED_ATTEMPT_TIME` timestamp NULL DEFAULT NULL,
                                      `TEMP_PWD` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_PREFERENCE`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_PREFERENCE` (
                                   `ID` int NOT NULL AUTO_INCREMENT,
                                   `USER_ID` varchar(40) NOT NULL,
                                   `FREE_TEXT_EDITOR_ENABLED` tinyint(1) DEFAULT NULL,
                                   PRIMARY KEY (`ID`),
                                   KEY `USER_PREFERENCE_USER_ID_FK` (`USER_ID`),
                                   CONSTRAINT `USER_PREFERENCE_USER_ID_FK` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_REVOKE_ORG_CHANGE_BKP`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_REVOKE_ORG_CHANGE_BKP` (
                                              `USER_ID` varchar(40) NOT NULL,
                                              `FIRST_NAME` varchar(100) NOT NULL,
                                              `MIDDLE_INITIAL` varchar(45) DEFAULT NULL,
                                              `LAST_NAME` varchar(100) NOT NULL,
                                              `EMAIL_ADDRESS` varchar(254) NOT NULL,
                                              `PHONE_NO` varchar(45) NOT NULL,
                                              `TITLE` varchar(45) DEFAULT NULL,
                                              `TERMINATION_DATE` date DEFAULT NULL,
                                              `ACTIVATION_DATE` date NOT NULL,
                                              `SIGN_IN_DATE` timestamp NULL DEFAULT NULL,
                                              `SIGN_OUT_DATE` timestamp NULL DEFAULT NULL,
                                              `LOCKED_OUT_DATE` timestamp NULL DEFAULT NULL,
                                              `STATUS_ID` varchar(32) NOT NULL,
                                              `AUDIT_ID` varchar(32) NOT NULL,
                                              `SECURITY_ROLE_ID` varchar(32) NOT NULL,
                                              `LOGIN_ID` varchar(45) DEFAULT NULL,
                                              `ORG_ID` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_SECURITY_QUESTIONS`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_SECURITY_QUESTIONS` (
                                           `USER_SECURITY_QUESTIONS_ID` int NOT NULL AUTO_INCREMENT,
                                           `USER_ID` varchar(40) NOT NULL,
                                           `ROW_ID` int NOT NULL,
                                           `ANSWER` varchar(100) DEFAULT NULL,
                                           `QUESTION_ID` int DEFAULT NULL,
                                           `SALT` varchar(100) DEFAULT NULL,
                                           PRIMARY KEY (`USER_SECURITY_QUESTIONS_ID`),
                                           KEY `SECURITY_QUES_USER_FK` (`USER_ID`),
                                           KEY `FK_SECURITY_QUESTIONS` (`QUESTION_ID`),
                                           CONSTRAINT `FK_SECURITY_QUESTIONS` FOREIGN KEY (`QUESTION_ID`) REFERENCES `SECURITY_QUESTIONS` (`QUESTION_ID`),
                                           CONSTRAINT `SECURITY_QUES_USER_FK` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1914 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_SECURITY_QUESTIONS_BACKUP_SEPT2015`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_SECURITY_QUESTIONS_BACKUP_SEPT2015` (
                                                           `USER_ID` varchar(40) NOT NULL,
                                                           `ROW_ID` int NOT NULL,
                                                           `ANSWER` varchar(100) DEFAULT NULL,
                                                           `QUESTION_ID` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_SECURITY_QUESTIONS_BCKUP`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_SECURITY_QUESTIONS_BCKUP` (
                                                 `USER_ID` varchar(40) NOT NULL,
                                                 `ROW_ID` int NOT NULL,
                                                 `QUESTION` varchar(100) NOT NULL,
                                                 `ANSWER` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_SECURITY_QUESTIONS_TEMP`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_SECURITY_QUESTIONS_TEMP` (
                                                `USER_ID` varchar(40) NOT NULL,
                                                `ROW_ID` int NOT NULL,
                                                `QUESTION` varchar(100) NOT NULL,
                                                `ANSWER` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-10-30  9:19:47

SET FOREIGN_KEY_CHECKS=0;
INSERT INTO SECURITY_ROLE (SECURITY_ROLE_ID, DESCRIPTION) VALUES ('1', 'Administrator');
INSERT INTO SECURITY_ROLE (SECURITY_ROLE_ID, DESCRIPTION) VALUES ('2', 'Top Level User');
INSERT INTO SECURITY_ROLE (SECURITY_ROLE_ID, DESCRIPTION) VALUES ('3', 'User');


INSERT INTO AUDIT_LOG(AUDIT_LOG_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER, ACTIVITY_TYPE, MEASURE_ID, LIST_OBJECT_ID, CLAUSE_ID, QDM_ID) VALUES ('2c9280827233d21d01723c6cef310092', '2020-05-22 08:47:51', '2c9280827233d21d01723c6cef310091', null, null, null, null, null, null, null);
INSERT INTO USER(USER_ID, FIRST_NAME, MIDDLE_INITIAL, LAST_NAME, EMAIL_ADDRESS, PHONE_NO, TITLE, TERMINATION_DATE, ACTIVATION_DATE, SIGN_IN_DATE, SIGN_OUT_DATE, LOCKED_OUT_DATE, STATUS_ID, AUDIT_ID, SECURITY_ROLE_ID, LOGIN_ID, ORG_ID, SESSION_ID, HARP_ID, FHIR_FLAG) VALUES ('2c9280827233d21d01723c6cef310091', 'Dev', '', 'User', 'dev.user@yourdomain.com', '555-555-5555', '', null, '2020-05-22', '2020-10-29 19:39:19', '2020-10-29 19:40:38', null, '1', '2c9280827233d21d01723c6cef310092', '1', 'devUser8762', 1, null, 'okta-user-id-goes-here', false);
INSERT INTO `USER_PASSWORD` VALUES ('1','2c9280827233d21d01723c6cef310091',0,0,'8a06ddf2a3da6e7d558c91951fb48d3f5787906904724b25d23ff161f92b1e70','d0dccfba-9178-4466-9a1a-981023b721a9',1,sysdate(),NULL,0);


INSERT INTO AUTHOR (ID, AUTHOR_NAME) VALUES ('14', 'American Medical Association-convened Physician Consortium for Performance Improvement(R) (AMA-PCPI)');
INSERT INTO AUTHOR (ID, AUTHOR_NAME) VALUES ('15', 'Centers for Medicare & Medicaid Services');
INSERT INTO AUTHOR (ID, AUTHOR_NAME) VALUES ('29', 'Cleveland Clinic');
INSERT INTO AUTHOR (ID, AUTHOR_NAME) VALUES ('55', 'National Committee for Quality Assurance');
INSERT INTO AUTHOR (ID, AUTHOR_NAME) VALUES ('80', 'Other');
INSERT INTO AUTHOR (ID, AUTHOR_NAME) VALUES ('81', 'National Quality Forum');
INSERT INTO AUTHOR (ID, AUTHOR_NAME) VALUES ('82', 'Joint Commission');
INSERT INTO AUTHOR (ID, AUTHOR_NAME) VALUES ('83', 'Oklahoma Foundation for Medical Quality');
INSERT INTO AUTHOR (ID, AUTHOR_NAME) VALUES ('84', 'American Board of Internal Medicine');
INSERT INTO AUTHOR (ID, AUTHOR_NAME) VALUES ('85', 'Kaiser Permanente');

INSERT INTO FEATURE_FLAGS (ID, FLAG_NAME, FLAG_ON) VALUES (1, 'MAT_ON_FHIR', 1);
INSERT INTO FEATURE_FLAGS (ID, FLAG_NAME, FLAG_ON) VALUES (2, 'FhirBonnie', 1);

INSERT INTO `ATTRIBUTES` VALUES (1,'activeDatetime'),(2,'admissionSource'),(3,'anatomicalApproachSite'),(4,'anatomicalLocationSite'),(5,'authorDatetime'),(6,'cause'),(7,'code'),(8,'birthDatetime'),(9,'expiredDatetime'),(10,'diagnoses'),(11,'dischargeDisposition'),(12,'dosage'),(13,'supply'),(14,'facilityLocation'),(15,'frequency'),(16,'incisionDatetime'),(17,'lengthOfStay'),(19,'method'),(20,'negationRationale'),(21,'ordinality'),(22,'prevalencePeriod'),(23,'principalDiagnosis'),(26,'reason'),(27,'referenceRange'),(28,'refills'),(29,'relatedTo'),(30,'relationship'),(31,'relevantPeriod'),(32,'result'),(33,'resultDatetime'),(34,'route'),(35,'severity'),(36,'status'),(37,'targetOutcome'),(38,'type'),(39,'id'),(40,'recorder'),(41,'reporter'),(42,'components'),(43,'participationPeriod'),(44,'facilityLocations');
INSERT INTO `ATTRIBUTES_MODES` VALUES (1,'1',1),(2,'1',2),(3,'1',3),(4,'2',3),(5,'2',4),(6,'3',3),(7,'3',4),(8,'4',3),(9,'4',4),(10,'5',1),(11,'5',2),(12,'5',3),(13,'6',3),(14,'6',4),(15,'7',3),(16,'7',4),(17,'8',1),(18,'8',2),(19,'8',3),(20,'9',1),(21,'9',2),(22,'9',3),(23,'10',3),(24,'10',4),(25,'11',3),(26,'11',4),(27,'12',1),(28,'12',3),(29,'13',1),(30,'13',3),(31,'14',3),(32,'14',4),(33,'15',3),(34,'15',4),(35,'16',1),(36,'16',2),(37,'16',3),(38,'17',1),(39,'17',3),(40,'18',3),(41,'19',3),(42,'19',4),(43,'20',3),(44,'20',4),(45,'21',3),(46,'21',4),(47,'22',3),(48,'23',3),(49,'23',4),(50,'24',1),(51,'24',3),(52,'25',1),(53,'25',3),(54,'26',3),(55,'26',4),(56,'27',3),(57,'28',1),(58,'28',3),(59,'29',3),(60,'29',4),(61,'30',3),(62,'30',4),(63,'31',3),(64,'32',1),(65,'32',3),(66,'32',4),(67,'33',1),(68,'33',2),(69,'33',3),(70,'34',3),(71,'34',4),(72,'35',3),(73,'35',4),(74,'36',3),(75,'36',4),(76,'37',1),(77,'37',3),(78,'37',4),(79,'38',3),(80,'38',4),(81,'39',4),(82,'40',4),(83,'41',4);
INSERT INTO `ATTRIBUTE_DETAILS` VALUES ('1','anatomical structure','91723000','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('10','negation rationale','','','','N',''),('11','number','107651007','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('12','ordinality','117363000','2.16.840.1.113883.6.96','SNOMED-CT','S','SUBJ'),('13','patient preference','PAT','2.16.840.1.113883.5.8','HL7 Act Accomodation Reason','S','RSON'),('14','provider preference','103323008','2.16.840.1.113883.6.96','SNOMED-CT','S','RSON'),('15','radiation dosage','228815006','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('16','radiationduration','218190002','2.16.840.1.113883.6.96','SNOMED-CT','S(I)/S(I)','REFR'),('17','reaction','263851003','2.16.840.1.113883.6.96','SNOMED-CT','S','MFST'),('18','reason','410666004','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('19','refills','18623-9','2.16.840.1.113883.6.1','LOINC','S','REFR'),('2','cumulative medication duration','363819003','2.16.840.1.113883.6.96','SNOMED-CT','S','SUBJ'),('20','result','385676005','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('21','route','263513008','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('22','severity','SEV','2.16.840.1.113883.5.4','HL7 Act Code','S','SUBJ'),('23','start datetime','398201009','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('24','status','263490005','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('25','stop datetime','397898000','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('3','dose','398232005','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('31','Health Record Field','','','','AC',''),('32','admission datetime','399423000','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('33','discharge datetime','442864001','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('34','environment','285202004','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('35','date','410672004','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('36','discharge status','309039003','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('37','incision datetime','34896006','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('38','laterality','182353008','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('39','laterality','182353008','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('4','length of stay','183797002','2.16.840.1.113883.6.96','SNOMED-CT','S','SUBJ'),('40','time','410669006','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('41','removal datetime','118292001','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('42','facility location','','2.16.840.1.113883.3.560.101.1','','E','SUBJ'),('43','facility location arrival datetime','','2.16.840.1.113883.3.560.101.1','','E','SUBJ'),('44','facility location departure datetime','','2.16.840.1.113883.3.560.101.1','','E','SUBJ'),('46','radiation duration','306751006','2.16.840.1.113883.6.96','SNOMED-CT','S','SUBJ'),('47','related to','REL','2.16.840.1.113883.1.11.11603','HL7 Role Link Type','S','REFR'),('48','recorder','419358007','2.16.840.1.113883.6.96','SNOMED-CT','S','SUBJ'),('49','source','260753009','2.16.840.1.113883.6.96','SNOMED-CT','S','SUBJ'),('6','frequency','260864003','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('7','hospital location','','','','P','REFR'),('9','method','414679000','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR');
INSERT INTO `DATA_TYPE` VALUES ('1','Patient Care Experience','1'),('100','Patient Characteristic Payer','9'),('101','Patient Characteristic Sex','9'),('102','Patient Characteristic Ethnicity','9'),('103','Patient Characteristic Race','9'),('104','Medication, Discharge','12'),('105','Family History','4'),('106','Symptom','19'),('107','Immunization, Administered','24'),('108','Immunization, Order','24'),('111','Diagnosis','4'),('112','Assessment, Recommended','25'),('113','Assessment, Performed','25'),('114','Adverse Event','26'),('115','Allergy/Intolerance','27'),('116','Assessment, Not Performed','25'),('117','Assessment, Not Recommended','25'),('121','Device, Not Applied','5'),('122','Device, Not Ordered','5'),('123','Device, Not Recommended','5'),('124','Diagnostic Study, Not Ordered','6'),('125','Diagnostic Study, Not Performed','6'),('126','Diagnostic Study, Not Recommended','6'),('127','Encounter, Not Ordered','7'),('128','Encounter, Not Performed','7'),('129','Encounter, Not Recommended','7'),('13','Device, Applied','5'),('130','Immunization, Not Administered','24'),('131','Immunization, Not Ordered','24'),('132','Intervention, Not Ordered','10'),('133','Intervention, Not Performed','10'),('134','Intervention, Not Recommended','10'),('135','Laboratory Test, Not Ordered','11'),('136','Laboratory Test, Not Performed','11'),('137','Laboratory Test, Not Recommended','11'),('138','Medication, Not Administered','12'),('139','Medication, Not Discharged','12'),('140','Medication, Not Dispensed','12'),('141','Medication, Not Ordered','12'),('142','Physical Exam, Not Ordered','14'),('143','Physical Exam, Not Performed','14'),('144','Physical Exam, Not Recommended','14'),('145','Procedure, Not Ordered','16'),('146','Procedure, Not Performed','16'),('147','Procedure, Not Recommended','16'),('148','Substance, Not Administered','18'),('149','Substance, Not Ordered','18'),('15','Device, Order','5'),('150','Substance, Not Recommended','18'),('151','Participation','28'),('152','Assessment, Order','25'),('153','Assessment, Not Ordered','25'),('154','Communication, Performed','3'),('155','Communication, Not Performed','3'),('156','Related Person','29'),('18','Diagnostic Study, Order','6'),('19','Diagnostic Study, Performed','6'),('2','Provider Care Experience','1'),('21','Encounter, Order','7'),('22','Encounter, Performed','7'),('26','Patient Characteristic','9'),('3','Care Goal','2'),('30','Intervention, Order','10'),('31','Intervention, Performed','10'),('35','Laboratory Test, Order','11'),('36','Laboratory Test, Performed','11'),('38','Medication, Active','12'),('39','Medication, Administered','12'),('42','Medication, Dispensed','12'),('44','Medication, Order','12'),('56','Physical Exam, Order','14'),('57','Physical Exam, Performed','14'),('62','Procedure, Order','16'),('63','Procedure, Performed','16'),('66','Substance, Administered','18'),('70','Substance, Order','18'),('78','Device, Recommended','5'),('79','Encounter, Recommended','7'),('81','Intervention, Recommended','10'),('82','Laboratory Test, Recommended','11'),('87','Physical Exam, Recommended','14'),('88','Procedure, Recommended','16'),('89','Substance, Recommended','18'),('90','Diagnostic Study, Recommended','6'),('92','attribute','23'),('96','Timing Element','22'),('97','Patient Characteristic Birthdate','9'),('98','Patient Characteristic Expired','9'),('99','Patient Characteristic Clinical Trial Participant','9');
INSERT INTO `DATA_TYPE_BACKUP_AUG2015` VALUES ('1','Patient Care Experience','1'),('10','Diagnosis, Resolved','4'),('100','Patient Characteristic Payer','9'),('101','Patient Characteristic Sex','9'),('102','Patient Characteristic Ethnicity','9'),('103','Patient Characteristic Race','9'),('104','Medication, Discharge','12'),('11','Device, Adverse Event','5'),('12','Device, Allergy','5'),('13','Device, Applied','5'),('14','Device, Intolerance','5'),('15','Device, Order','5'),('16','Diagnostic Study, Adverse Event','6'),('17','Diagnostic Study, Intolerance','6'),('18','Diagnostic Study, Order','6'),('19','Diagnostic Study, Performed','6'),('2','Provider Care Experience','1'),('21','Encounter, Order','7'),('22','Encounter, Performed','7'),('23','Functional Status, Order','8'),('24','Functional Status, Performed','8'),('26','Patient Characteristic','9'),('27','Provider Characteristic','9'),('28','Intervention, Adverse Event','10'),('29','Intervention, Intolerance','10'),('3','Care Goal','2'),('30','Intervention, Order','10'),('31','Intervention, Performed','10'),('33','Laboratory Test, Adverse Event','11'),('34','Laboratory Test, Intolerance','11'),('35','Laboratory Test, Order','11'),('36','Laboratory Test, Performed','11'),('38','Medication, Active','12'),('39','Medication, Administered','12'),('4','Communication: From Provider to Provider','3'),('40','Medication, Adverse Effects','12'),('41','Medication, Allergy','12'),('42','Medication, Dispensed','12'),('43','Medication, Intolerance','12'),('44','Medication, Order','12'),('5','Communication: From Patient to Provider','3'),('56','Physical Exam, Order','14'),('57','Physical Exam, Performed','14'),('6','Communication: From Provider to Patient','3'),('60','Procedure, Adverse Event','16'),('61','Procedure, Intolerance','16'),('62','Procedure, Order','16'),('63','Procedure, Performed','16'),('65','Risk Category Assessment','17'),('66','Substance, Administered','18'),('67','Substance, Adverse Event','18'),('68','Substance, Allergy','18'),('69','Substance, Intolerance','18'),('7','Diagnosis, Active','4'),('70','Substance, Order','18'),('71','Symptom, Active','19'),('72','Symptom, Assessed','19'),('73','Symptom, Inactive','19'),('74','Symptom, Resolved','19'),('76','Transfer From','21'),('77','Transfer To','21'),('78','Device, Recommended','5'),('79','Encounter, Recommended','7'),('8','Diagnosis, Family History','4'),('80','Functional Status, Recommended','8'),('81','Intervention, Recommended','10'),('82','Laboratory Test, Recommended','11'),('87','Physical Exam, Recommended','14'),('88','Procedure, Recommended','16'),('89','Substance, Recommended','18'),('9','Diagnosis, Inactive','4'),('90','Diagnostic Study, Recommended','6'),('92','attribute','23'),('95','Encounter, Active','7'),('96','Timing Element','22'),('97','Patient Characteristic Birthdate','9'),('98','Patient Characteristic Expired','9'),('99','Patient Characteristic Clinical Trial Participant','9');
INSERT INTO `LIST_OBJECT` VALUES ('8a4d8c81309da15201309e46121d00d4','Measurement Period','80','2.16.840.1.113883.3.67.1.101.1.53','N/A',NULL,NULL,'22','1','60',NULL,NULL,NULL,'Default Measurement CodeList','1970-01-01 06:00:00',0),('8a4d8c81309da15201309e46124800e4','Measurement Start Date','80','2.16.840.1.113883.3.67.1.101.1.54','N/A',NULL,NULL,'22','1','60',NULL,NULL,NULL,'Default Measurement CodeList','1970-01-01 06:00:00',0),('8a4d8c81309da15201309e4612567F35','Dead','85','419099009','N/A',NULL,NULL,'9','2015','69',NULL,NULL,NULL,NULL,'2015-06-11 19:18:29',0),('8a4d8c81309da15201309e46126d00f4','Measurement End Date','80','2.16.840.1.113883.3.67.1.101.1.55','N/A',NULL,NULL,'22','1','60',NULL,NULL,NULL,'Default Measurement CodeList','1970-01-01 06:00:00',0),('8a4d8c81309da15201309e46126DA2E0','Birthdate','85','21112-8','N/A',NULL,NULL,'9','2015','69',NULL,NULL,NULL,NULL,'2015-06-11 19:18:29',0),('8ae452962e3a223a012e3a254b808889','Male','80','2.16.840.1.113883.3.560.100.1','N/A',NULL,NULL,'9','HL7 v2.5','132',NULL,NULL,'National Quality Forum','Default Gender CodeList','2011-07-27 15:47:00',0),('8ae452962e3a223a012e3a254b808890','Female','80','2.16.840.1.113883.3.560.100.2','N/A',NULL,NULL,'9','HL7 v2.5','132',NULL,NULL,'National Quality Forum','Default Gender CodeList','2011-07-27 15:47:00',0),('8ae452962e3a223a012e3a254b808891','Unknown Sex','80','2.16.840.1.113883.3.560.100.3','N/A',NULL,NULL,'9','HL7 v2.5','132',NULL,NULL,'National Quality Forum','Default Gender CodeList','2011-07-27 15:47:00',0),('8ae452962e3a223a012e3a254b808892','birth date','80','2.16.840.1.113883.3.560.100.4','N/A',NULL,NULL,'9','2.36','130',NULL,NULL,'National Quality Forum','Default Gender CodeList','2011-09-20 05:00:00',0),('bae50f18267111e1a17a78acc0b65c43','ONC Administrative Sex','86','2.16.840.1.113762.1.4.1','N/A',NULL,NULL,'9','HL7 v2.5','132',NULL,NULL,'National Library of Medicine','Supplimental CodeList','2011-07-27 15:47:00',1),('bae85d87267111e1a17a78acc0b65c43','Race','87','2.16.840.1.114222.4.11.836','N/A',NULL,NULL,'9','1.0','133',NULL,NULL,'CDC NCHS','Supplimental CodeList','2007-03-30 05:00:00',1),('bae86046267111e1a17a78acc0b65c43','Ethnicity','87','2.16.840.1.114222.4.11.837','N/A',NULL,NULL,'9','1.0','133',NULL,NULL,'CDC NCHS','Supplimental CodeList','2007-03-30 05:00:00',1),('bae86261267111e1a17a78acc0b65c43','Payer','88','2.16.840.1.114222.4.11.3591','N/A',NULL,NULL,'9','4.0','134',NULL,NULL,'PHDSC','Supplimental CodeList','2011-10-01 05:00:00',1);
INSERT INTO `MEASURE_SCORE` VALUES ('1','Continuous Variable'),('2','Proportion'),('3','Ratio'),('4','Cohort');
INSERT INTO `MEASURE_TYPES` VALUES ('1','Composite','COMPOSITE'),('10','Appropriate Use Process','APPROPRIATE'),('2','Cost/Resource Use','RESOURCE'),('3','Efficiency','EFFICIENCY'),('4','Outcome','OUTCOME'),('5','Patient Engagement/Experience','EXPERIENCE'),('6','Process','PROCESS'),('7','Structure','STRUCTURE'),('8','Patient Reported Outcome Performance','PRO-PM'),('9','Intermediate Clinical Outcome','INTERM-OM');
INSERT INTO `MODES` VALUES (1,'Comparison'),(2,'Computative'),(3,'Nullable'),(4,'Value Sets');
INSERT INTO `OBJECT_STATUS` VALUES ('1','In Progress'),('2','Complete');
INSERT INTO `OPERATOR` VALUES ('1','AND','AND','1'),('10','Ends During','EDU','2'),('11','Starts After End Of','SAE','2'),('12','Starts After Start Of','SAS','2'),('13','Starts Before Start Of','SBS','2'),('14','Starts Before End Of','SBE','2'),('15','Starts Concurrent With','SCW','2'),('16','Starts During','SDU','2'),('2','OR','OR','1'),('22','Avg','AVG','4'),('23','Count','COUNT','4'),('25','Max','MAX','4'),('26','Min','MIN','4'),('27','Median','MEDIAN','4'),('28','Sum','SUM','4'),('3','Concurrent With','CONCURRENT','2'),('30','First','FIRST','4'),('31','Second','SECOND','4'),('32','Third','THIRD','4'),('33','Fourth','FOURTH','4'),('34','Fifth','FIFTH','4'),('35','Most Recent','MOST RECENT','4'),('37','Less Than','<','5'),('38','Greater Than','>','5'),('39','Less Than or Equal To','<=','5'),('4','During','DURING','2'),('40','Greater Than or Equal To','>=','5'),('41','Equal To','=','5'),('42','Union','UNION','6'),('43','Intersection','INTERSECTION','6'),('44','AND NOT','AND NOT','1'),('45','OR NOT','OR NOT','1'),('46','Ends Concurrent With Start Of','ECWS','2'),('47','Starts Concurrent With End Of','SCWE','2'),('48','Overlaps','Overlap','2'),('49','Age At','AGE AT','4'),('5','Ends After End Of','EAE','2'),('50','Starts Before Or Concurrent With Start Of','SBSORSCW','2'),('51','Starts After Or Concurrent With Start Of','SASORSCW','2'),('52','Starts Before Or Concurrent With End Of','SBEORSCWE','2'),('53','Starts After Or Concurrent With End Of','SAEORSCWE','2'),('54','Ends Before Or Concurrent With End Of','EBEORECW','2'),('55','Ends After Or Concurrent With End Of','EAEORECW','2'),('56','Ends Before Or Concurrent With Start Of','EBSORECWS','2'),('57','Ends After Or Concurrent With Start Of','EASORECWS','2'),('58','Satisfies All','SATISFIES ALL','4'),('59','Satisfies Any','SATISFIES ANY','4'),('6','Ends After Start Of','EAS','2'),('60','Fulfills','FULFILLS','3'),('61','Datetimediff','DATETIMEDIFF','4'),('7','Ends Before End Of','EBE','2'),('8','Ends Before Start Of','EBS','2'),('9','Ends Concurrent With','ECW','2');
INSERT INTO `OPERATOR_BACKUP` VALUES ('1','AND','AND','1'),('10','Ends During','EDU','2'),('11','Starts After End Of','SAE','2'),('12','Starts After Start Of','SAS','2'),('13','Starts Before Start Of','SBS','2'),('14','Starts Before End Of','SBE','2'),('15','Starts Concurrent With','SCW','2'),('16','Starts During','SDU','2'),('2','OR','OR','1'),('22','AVG','AVG','4'),('23','COUNT','COUNT','4'),('25','MAX','MAX','4'),('26','MIN','MIN','4'),('27','MEDIAN','MEDIAN','4'),('28','SUM','SUM','4'),('3','Concurrent With','CONCURRENT','2'),('30','FIRST','FIRST','4'),('31','SECOND','SECOND','4'),('32','THIRD','THIRD','4'),('33','FOURTH','FOURTH','4'),('34','FIFTH','FIFTH','4'),('35','MOST RECENT','MOST RECENT','4'),('37','Less Than','<','5'),('38','Greater Than','>','5'),('39','Less Than or Equal To','<=','5'),('4','During','DURING','2'),('40','Greater Than or Equal To','>=','5'),('41','Equal To','=','5'),('42','UNION','UNION','6'),('43','INTERSECTION','INTERSECTION','6'),('44','AND NOT','AND NOT','1'),('45','OR NOT','OR NOT','1'),('46','Ends Concurrent With Start Of','ECWS','2'),('47','Starts Concurrent With End Of','SCWE','2'),('48','Overlaps','Overlap','2'),('49','AGE AT','AGE AT','4'),('5','Ends After End Of','EAE','2'),('50','Starts Before Or Concurrent With Start Of','SBSORSCW','2'),('51','Starts After Or Concurrent With Start Of','SASORSCW','2'),('52','Starts Before Or Concurrent With End Of','SBEORSCWE','2'),('53','Starts After Or Concurrent With End Of','SAEORSCWE','2'),('54','Ends Before Or Concurrent With End Of','EBEORECW','2'),('55','Ends After Or Concurrent With End Of','EAEORECW','2'),('56','Ends Before Or Concurrent With Start Of','EBSORECWS','2'),('57','Ends After Or Concurrent With Start Of','EASORECWS','2'),('58','SATISFIES ALL','SATISFIES ALL','4'),('59','SATISFIES ANY','SATISFIES ANY','4'),('6','Ends After Start Of','EAS','2'),('60','Fulfills','FULFILLS','3'),('61','DATETIMEDIFF','DATETIMEDIFF','4'),('7','Ends Before End Of','EBE','2'),('8','Ends Before Start Of','EBS','2'),('9','Ends Concurrent With','ECW','2');
INSERT INTO `OPERATOR_TYPE` VALUES ('1','Logical Operators'),('2','Relative Timings'),('3','Relative Associations'),('4','Functions'),('5','Comparison Operator'),('6','Set Operators');
INSERT INTO `QDM_ATTRIBUTES` VALUES (1,'code','114','Data Type'),(3,'type','114','Data Type'),(4,'severity','114','Data Type'),(5,'facilityLocation','114','Data Type'),(6,'id','114','Data Type'),(7,'recorder','114','Data Type'),(9,'code','115','Data Type'),(10,'prevalencePeriod','115','Data Type'),(11,'type','115','Data Type'),(12,'severity','115','Data Type'),(13,'id','115','Data Type'),(14,'recorder','115','Data Type'),(16,'code','116','Data Type'),(17,'negationRationale','116','Data Type'),(18,'id','116','Data Type'),(21,'code','117','Data Type'),(22,'negationRationale','117','Data Type'),(23,'id','117','Data Type'),(26,'code','113','Data Type'),(27,'authorDatetime','113','Data Type'),(28,'method','113','Data Type'),(29,'reason','113','Data Type'),(30,'result','113','Data Type'),(31,'id','113','Data Type'),(34,'authorDatetime','112','Data Type'),(35,'code','112','Data Type'),(37,'reason','112','Data Type'),(38,'id','112','Data Type'),(41,'code','3','Data Type'),(42,'relatedTo','3','Data Type'),(43,'relevantPeriod','3','Data Type'),(44,'targetOutcome','3','Data Type'),(45,'id','3','Data Type'),(79,'anatomicalLocationSite','13','Data Type'),(80,'code','13','Data Type'),(81,'reason','13','Data Type'),(82,'relevantPeriod','13','Data Type'),(83,'id','13','Data Type'),(86,'code','121','Data Type'),(87,'negationRationale','121','Data Type'),(88,'id','121','Data Type'),(91,'code','122','Data Type'),(92,'negationRationale','122','Data Type'),(93,'id','122','Data Type'),(96,'code','123','Data Type'),(97,'negationRationale','123','Data Type'),(98,'id','123','Data Type'),(101,'authorDatetime','15','Data Type'),(102,'code','15','Data Type'),(103,'reason','15','Data Type'),(104,'id','15','Data Type'),(107,'authorDatetime','78','Data Type'),(108,'code','78','Data Type'),(109,'reason','78','Data Type'),(110,'id','78','Data Type'),(113,'anatomicalLocationSite','111','Data Type'),(114,'code','111','Data Type'),(115,'prevalencePeriod','111','Data Type'),(116,'severity','111','Data Type'),(117,'id','111','Data Type'),(118,'recorder','111','Data Type'),(120,'code','124','Data Type'),(121,'negationRationale','124','Data Type'),(122,'id','124','Data Type'),(125,'code','125','Data Type'),(126,'negationRationale','125','Data Type'),(127,'id','125','Data Type'),(130,'code','126','Data Type'),(131,'negationRationale','126','Data Type'),(132,'id','126','Data Type'),(135,'authorDatetime','18','Data Type'),(136,'code','18','Data Type'),(140,'reason','18','Data Type'),(141,'id','18','Data Type'),(144,'code','19','Data Type'),(145,'facilityLocation','19','Data Type'),(146,'method','19','Data Type'),(149,'reason','19','Data Type'),(150,'relevantPeriod','19','Data Type'),(151,'result','19','Data Type'),(152,'resultDatetime','19','Data Type'),(153,'status','19','Data Type'),(154,'id','19','Data Type'),(157,'code','90','Data Type'),(161,'authorDatetime','90','Data Type'),(162,'id','90','Data Type'),(174,'code','127','Data Type'),(175,'negationRationale','127','Data Type'),(176,'id','127','Data Type'),(179,'code','128','Data Type'),(180,'negationRationale','128','Data Type'),(181,'id','128','Data Type'),(184,'code','129','Data Type'),(185,'negationRationale','129','Data Type'),(186,'id','129','Data Type'),(189,'authorDatetime','21','Data Type'),(190,'code','21','Data Type'),(191,'facilityLocation','21','Data Type'),(192,'reason','21','Data Type'),(193,'id','21','Data Type'),(196,'admissionSource','22','Data Type'),(197,'code','22','Data Type'),(198,'diagnoses','22','Data Type'),(199,'dischargeDisposition','22','Data Type'),(201,'lengthOfStay','22','Data Type'),(205,'relevantPeriod','22','Data Type'),(206,'id','22','Data Type'),(209,'authorDatetime','79','Data Type'),(210,'code','79','Data Type'),(211,'facilityLocation','79','Data Type'),(212,'reason','79','Data Type'),(213,'id','79','Data Type'),(216,'authorDatetime','105','Data Type'),(217,'code','105','Data Type'),(218,'relationship','105','Data Type'),(219,'id','105','Data Type'),(220,'recorder','105','Data Type'),(222,'authorDatetime','107','Data Type'),(223,'code','107','Data Type'),(224,'dosage','107','Data Type'),(226,'reason','107','Data Type'),(227,'route','107','Data Type'),(228,'id','107','Data Type'),(231,'code','130','Data Type'),(232,'negationRationale','130','Data Type'),(233,'id','130','Data Type'),(236,'code','131','Data Type'),(237,'negationRationale','131','Data Type'),(238,'id','131','Data Type'),(241,'activeDatetime','108','Data Type'),(242,'authorDatetime','108','Data Type'),(243,'code','108','Data Type'),(244,'supply','108','Data Type'),(245,'reason','108','Data Type'),(246,'route','108','Data Type'),(247,'dosage','108','Data Type'),(248,'id','108','Data Type'),(251,'code','132','Data Type'),(252,'negationRationale','132','Data Type'),(253,'id','132','Data Type'),(256,'code','133','Data Type'),(257,'negationRationale','133','Data Type'),(258,'id','133','Data Type'),(261,'code','134','Data Type'),(262,'negationRationale','134','Data Type'),(263,'id','134','Data Type'),(266,'authorDatetime','30','Data Type'),(267,'code','30','Data Type'),(268,'reason','30','Data Type'),(269,'id','30','Data Type'),(272,'code','31','Data Type'),(273,'reason','31','Data Type'),(274,'relevantPeriod','31','Data Type'),(275,'result','31','Data Type'),(276,'status','31','Data Type'),(277,'id','31','Data Type'),(280,'authorDatetime','81','Data Type'),(281,'code','81','Data Type'),(282,'reason','81','Data Type'),(283,'id','81','Data Type'),(286,'code','135','Data Type'),(287,'negationRationale','135','Data Type'),(288,'id','135','Data Type'),(291,'code','136','Data Type'),(292,'negationRationale','136','Data Type'),(293,'id','136','Data Type'),(296,'code','137','Data Type'),(297,'negationRationale','137','Data Type'),(298,'id','137','Data Type'),(301,'authorDatetime','35','Data Type'),(302,'code','35','Data Type'),(304,'reason','35','Data Type'),(305,'id','35','Data Type'),(308,'code','36','Data Type'),(309,'method','36','Data Type'),(310,'referenceRange','36','Data Type'),(311,'relevantPeriod','36','Data Type'),(312,'result','36','Data Type'),(313,'resultDatetime','36','Data Type'),(314,'status','36','Data Type'),(315,'reason','36','Data Type'),(316,'id','36','Data Type'),(319,'authorDatetime','82','Data Type'),(320,'code','82','Data Type'),(322,'reason','82','Data Type'),(323,'id','82','Data Type'),(326,'code','38','Data Type'),(327,'dosage','38','Data Type'),(329,'frequency','38','Data Type'),(330,'relevantPeriod','38','Data Type'),(331,'route','38','Data Type'),(332,'id','38','Data Type'),(333,'recorder','38','Data Type'),(335,'code','39','Data Type'),(336,'dosage','39','Data Type'),(338,'frequency','39','Data Type'),(339,'relevantPeriod','39','Data Type'),(340,'route','39','Data Type'),(341,'reason','39','Data Type'),(342,'id','39','Data Type'),(345,'authorDatetime','104','Data Type'),(346,'code','104','Data Type'),(347,'dosage','104','Data Type'),(348,'supply','104','Data Type'),(349,'frequency','104','Data Type'),(350,'refills','104','Data Type'),(351,'route','104','Data Type'),(352,'id','104','Data Type'),(353,'recorder','104','Data Type'),(355,'authorDatetime','42','Data Type'),(356,'code','42','Data Type'),(357,'dosage','42','Data Type'),(358,'supply','42','Data Type'),(359,'frequency','42','Data Type'),(360,'refills','42','Data Type'),(361,'route','42','Data Type'),(362,'id','42','Data Type'),(365,'code','138','Data Type'),(366,'negationRationale','138','Data Type'),(367,'id','138','Data Type'),(370,'code','139','Data Type'),(371,'negationRationale','139','Data Type'),(372,'id','139','Data Type'),(375,'code','140','Data Type'),(376,'negationRationale','140','Data Type'),(377,'id','140','Data Type'),(380,'code','141','Data Type'),(381,'negationRationale','141','Data Type'),(382,'id','141','Data Type'),(386,'authorDatetime','44','Data Type'),(387,'code','44','Data Type'),(388,'dosage','44','Data Type'),(389,'supply','44','Data Type'),(390,'frequency','44','Data Type'),(392,'reason','44','Data Type'),(393,'refills','44','Data Type'),(394,'route','44','Data Type'),(395,'id','44','Data Type'),(398,'authorDatetime','1','Data Type'),(399,'code','1','Data Type'),(400,'id','1','Data Type'),(401,'recorder','1','Data Type'),(403,'authorDatetime','26','Data Type'),(404,'code','26','Data Type'),(405,'id','26','Data Type'),(408,'code','97','Data Type'),(409,'birthDatetime','97','Data Type'),(410,'id','97','Data Type'),(413,'code','99','Data Type'),(414,'reason','99','Data Type'),(415,'relevantPeriod','99','Data Type'),(416,'id','99','Data Type'),(419,'code','102','Data Type'),(420,'id','102','Data Type'),(423,'cause','98','Data Type'),(424,'code','98','Data Type'),(425,'expiredDatetime','98','Data Type'),(426,'id','98','Data Type'),(429,'code','100','Data Type'),(430,'relevantPeriod','100','Data Type'),(431,'id','100','Data Type'),(434,'code','103','Data Type'),(435,'id','103','Data Type'),(438,'code','101','Data Type'),(439,'id','101','Data Type'),(442,'code','142','Data Type'),(443,'negationRationale','142','Data Type'),(444,'id','142','Data Type'),(447,'code','143','Data Type'),(448,'negationRationale','143','Data Type'),(449,'id','143','Data Type'),(452,'code','144','Data Type'),(453,'negationRationale','144','Data Type'),(454,'id','144','Data Type'),(457,'anatomicalLocationSite','56','Data Type'),(458,'authorDatetime','56','Data Type'),(459,'code','56','Data Type'),(461,'reason','56','Data Type'),(462,'id','56','Data Type'),(465,'anatomicalLocationSite','57','Data Type'),(466,'code','57','Data Type'),(467,'method','57','Data Type'),(468,'reason','57','Data Type'),(469,'relevantPeriod','57','Data Type'),(470,'result','57','Data Type'),(471,'id','57','Data Type'),(474,'anatomicalLocationSite','87','Data Type'),(475,'authorDatetime','87','Data Type'),(476,'code','87','Data Type'),(478,'reason','87','Data Type'),(479,'id','87','Data Type'),(482,'code','145','Data Type'),(483,'negationRationale','145','Data Type'),(484,'id','145','Data Type'),(487,'negationRationale','146','Data Type'),(488,'code','146','Data Type'),(489,'id','146','Data Type'),(492,'code','147','Data Type'),(493,'negationRationale','147','Data Type'),(494,'id','147','Data Type'),(498,'anatomicalLocationSite','62','Data Type'),(499,'authorDatetime','62','Data Type'),(500,'code','62','Data Type'),(504,'reason','62','Data Type'),(505,'id','62','Data Type'),(509,'anatomicalLocationSite','63','Data Type'),(510,'code','63','Data Type'),(511,'incisionDatetime','63','Data Type'),(512,'method','63','Data Type'),(516,'reason','63','Data Type'),(517,'relevantPeriod','63','Data Type'),(518,'result','63','Data Type'),(519,'status','63','Data Type'),(520,'id','63','Data Type'),(524,'anatomicalLocationSite','88','Data Type'),(525,'authorDatetime','88','Data Type'),(526,'code','88','Data Type'),(529,'reason','88','Data Type'),(530,'id','88','Data Type'),(533,'authorDatetime','2','Data Type'),(534,'code','2','Data Type'),(535,'id','2','Data Type'),(536,'recorder','2','Data Type'),(543,'code','66','Data Type'),(544,'dosage','66','Data Type'),(545,'frequency','66','Data Type'),(546,'relevantPeriod','66','Data Type'),(547,'route','66','Data Type'),(548,'id','66','Data Type'),(551,'code','148','Data Type'),(552,'negationRationale','148','Data Type'),(553,'id','148','Data Type'),(556,'code','149','Data Type'),(557,'negationRationale','149','Data Type'),(558,'id','149','Data Type'),(561,'code','150','Data Type'),(562,'negationRationale','150','Data Type'),(563,'id','150','Data Type'),(566,'authorDatetime','70','Data Type'),(567,'code','70','Data Type'),(568,'dosage','70','Data Type'),(569,'frequency','70','Data Type'),(571,'reason','70','Data Type'),(572,'refills','70','Data Type'),(573,'route','70','Data Type'),(574,'supply','70','Data Type'),(575,'id','70','Data Type'),(578,'authorDatetime ','89','Data Type'),(579,'code','89','Data Type'),(580,'dosage','89','Data Type'),(581,'frequency','89','Data Type'),(583,'reason','89','Data Type'),(584,'refills','89','Data Type'),(585,'route','89','Data Type'),(586,'id','89','Data Type'),(589,'code','106','Data Type'),(590,'prevalencePeriod','106','Data Type'),(591,'severity','106','Data Type'),(592,'id','106','Data Type'),(593,'recorder','106','Data Type'),(595,'authorDatetime','36','Data Type'),(596,'authorDatetime','142','Data Type'),(597,'authorDatetime','116','Data Type'),(598,'authorDatetime','117','Data Type'),(602,'authorDatetime','13','Data Type'),(603,'authorDatetime','121','Data Type'),(604,'authorDatetime','122','Data Type'),(605,'authorDatetime','123','Data Type'),(606,'authorDatetime','124','Data Type'),(607,'authorDatetime','125','Data Type'),(608,'authorDatetime','126','Data Type'),(609,'authorDatetime','19','Data Type'),(610,'authorDatetime','127','Data Type'),(611,'authorDatetime','128','Data Type'),(612,'authorDatetime','129','Data Type'),(613,'authorDatetime','22','Data Type'),(614,'authorDatetime','130','Data Type'),(615,'authorDatetime','131','Data Type'),(616,'authorDatetime','132','Data Type'),(617,'authorDatetime','133','Data Type'),(618,'authorDatetime','134','Data Type'),(619,'authorDatetime','31','Data Type'),(620,'authorDatetime','135','Data Type'),(621,'authorDatetime','136','Data Type'),(622,'authorDatetime','137','Data Type'),(623,'authorDatetime','39','Data Type'),(624,'authorDatetime','138','Data Type'),(625,'authorDatetime','139','Data Type'),(626,'authorDatetime','140','Data Type'),(627,'authorDatetime','141','Data Type'),(628,'authorDatetime','143','Data Type'),(629,'authorDatetime','144','Data Type'),(630,'authorDatetime','57','Data Type'),(631,'authorDatetime','145','Data Type'),(632,'authorDatetime','146','Data Type'),(633,'authorDatetime','147','Data Type'),(634,'authorDatetime','63','Data Type'),(635,'authorDatetime','66','Data Type'),(636,'authorDatetime','148','Data Type'),(637,'authorDatetime','149','Data Type'),(638,'authorDatetime','150','Data Type'),(639,'components','113','Data Type'),(640,'components','19','Data Type'),(641,'components','36','Data Type'),(642,'components','57','Data Type'),(643,'components','63','Data Type'),(644,'authorDatetime','111','Data Type'),(645,'authorDatetime','114','Data Type'),(646,'authorDatetime','115','Data Type'),(647,'code','151','Data Type'),(648,'id','151','Data Type'),(649,'participationPeriod','151','Data Type'),(651,'recorder','151','Data Type'),(652,'facilityLocations','22','Data Type'),(656,'relatedTo','113','Data Type'),(659,'relevantPeriod','44','Data Type'),(660,'relevantPeriod','42','Data Type'),(661,'authorDatetime','152','Data Type'),(662,'reason','152','Data Type'),(663,'code','152','Data Type'),(664,'id','152','Data Type'),(667,'authorDatetime','153','Data Type'),(668,'negationRationale','153','Data Type'),(669,'code','153','Data Type'),(670,'id','153','Data Type'),(673,'setting','44','Data Type'),(674,'authorDatetime','154','Data Type'),(675,'category','154','Data Type'),(676,'medium','154','Data Type'),(677,'code','154','Data Type'),(678,'sender','154','Data Type'),(679,'recipient','154','Data Type'),(681,'relatedTo','154','Data Type'),(682,'id','154','Data Type'),(685,'authorDatetime','155','Data Type'),(686,'category','155','Data Type'),(687,'negationRationale','155','Data Type'),(688,'code','155','Data Type'),(689,'sender','155','Data Type'),(690,'recipient','155','Data Type'),(691,'id','155','Data Type'),(694,'daysSupplied','44','Data Type'),(695,'daysSupplied','104','Data Type'),(696,'daysSupplied','42','Data Type'),(700,'id','156','Data Type'),(702,'sentDatetime','154','Data Type'),(703,'receivedDatetime','154','Data Type'),(704,'relevantDatetime','114','Data Type'),(705,'relevantDatetime','113','Data Type'),(706,'relevantDatetime','13','Data Type'),(707,'relevantDatetime','19','Data Type'),(708,'relevantDatetime','107','Data Type'),(709,'relevantDatetime','31','Data Type'),(710,'relevantDatetime','36','Data Type'),(711,'relevantDatetime','38','Data Type'),(712,'relevantDatetime','39','Data Type'),(713,'relevantDatetime','42','Data Type'),(714,'relevantDatetime','57','Data Type'),(715,'relevantDatetime','63','Data Type'),(716,'relevantDatetime','66','Data Type'),(717,'priority','21','Data Type'),(718,'priority','22','Data Type'),(719,'priority','62','Data Type'),(720,'priority','63','Data Type'),(721,'performer','113','Data Type'),(722,'performer','3','Data Type'),(723,'performer','13','Data Type'),(724,'performer','19','Data Type'),(725,'performer','107','Data Type'),(726,'performer','31','Data Type'),(727,'performer','36','Data Type'),(728,'performer','39','Data Type'),(729,'performer','57','Data Type'),(730,'performer','63','Data Type'),(731,'performer','66','Data Type'),(732,'requester','152','Data Type'),(733,'requester','112','Data Type'),(734,'requester','15','Data Type'),(735,'requester','78','Data Type'),(736,'requester','18','Data Type'),(737,'requester','90','Data Type'),(738,'requester','21','Data Type'),(739,'requester','79','Data Type'),(740,'requester','108','Data Type'),(741,'requester','30','Data Type'),(742,'requester','81','Data Type'),(743,'requester','35','Data Type'),(744,'requester','82','Data Type'),(745,'requester','56','Data Type'),(746,'requester','87','Data Type'),(747,'requester','62','Data Type'),(748,'requester','88','Data Type'),(749,'requester','70','Data Type'),(750,'requester','89','Data Type'),(751,'relevantPeriod','70','Data Type'),(752,'statusDate','3','Data Type'),(753,'prescriber','104','Data Type'),(754,'prescriber','42','Data Type'),(755,'prescriber','44','Data Type'),(756,'relevantPeriod','113','Data Type'),(757,'participant','22','Data Type'),(758,'dispenser','42','Data Type'),(759,'patientId','114','Data Type'),(760,'patientId','115','Data Type'),(761,'patientId','153','Data Type'),(762,'patientId','116','Data Type'),(763,'patientId','117','Data Type'),(764,'patientId','152','Data Type'),(765,'patientId','113','Data Type'),(766,'patientId','112','Data Type'),(767,'patientId','3','Data Type'),(768,'patientId','154','Data Type'),(769,'patientId','155','Data Type'),(770,'patientId','13','Data Type'),(771,'patientId','121','Data Type'),(772,'patientId','122','Data Type'),(773,'patientId','123','Data Type'),(774,'patientId','78','Data Type'),(775,'patientId','15','Data Type'),(776,'patientId','111','Data Type'),(777,'patientId','124','Data Type'),(778,'patientId','125','Data Type'),(779,'patientId','126','Data Type'),(780,'patientId','18','Data Type'),(781,'patientId','19','Data Type'),(782,'patientId','90','Data Type'),(783,'patientId','127','Data Type'),(784,'patientId','128','Data Type'),(785,'patientId','129','Data Type'),(786,'patientId','21','Data Type'),(787,'patientId','22','Data Type'),(788,'patientId','79','Data Type'),(789,'patientId','105','Data Type'),(790,'patientId','107','Data Type'),(791,'patientId','130','Data Type'),(792,'patientId','131','Data Type'),(793,'patientId','108','Data Type'),(794,'patientId','132','Data Type'),(795,'patientId','133','Data Type'),(796,'patientId','134','Data Type'),(797,'patientId','30','Data Type'),(798,'patientId','31','Data Type'),(799,'patientId','81','Data Type'),(800,'patientId','135','Data Type'),(801,'patientId','136','Data Type'),(802,'patientId','137','Data Type'),(803,'patientId','35','Data Type'),(804,'patientId','36','Data Type'),(805,'patientId','82','Data Type'),(806,'patientId','38','Data Type'),(807,'patientId','39','Data Type'),(808,'patientId','104','Data Type'),(809,'patientId','42','Data Type'),(810,'patientId','138','Data Type'),(811,'patientId','139','Data Type'),(812,'patientId','140','Data Type'),(813,'patientId','141','Data Type'),(814,'patientId','44','Data Type'),(815,'patientId','151','Data Type'),(816,'patientId','1','Data Type'),(817,'patientId','26','Data Type'),(818,'patientId','97','Data Type'),(819,'patientId','99','Data Type'),(820,'patientId','102','Data Type'),(821,'patientId','98','Data Type'),(822,'patientId','100','Data Type'),(823,'patientId','103','Data Type'),(824,'patientId','101','Data Type'),(825,'patientId','142','Data Type'),(826,'patientId','143','Data Type'),(827,'patientId','144','Data Type'),(828,'patientId','56','Data Type'),(829,'patientId','57','Data Type'),(830,'patientId','87','Data Type'),(831,'patientId','145','Data Type'),(832,'patientId','146','Data Type'),(833,'patientId','147','Data Type'),(834,'patientId','62','Data Type'),(835,'patientId','63','Data Type'),(836,'patientId','88','Data Type'),(837,'patientId','2','Data Type'),(838,'patientId','156','Data Type'),(839,'patientId','66','Data Type'),(840,'patientId','148','Data Type'),(841,'patientId','149','Data Type'),(842,'patientId','150','Data Type'),(843,'patientId','70','Data Type'),(844,'patientId','89','Data Type'),(845,'patientId','106','Data Type'),(846,'rank','62','Data Type'),(847,'rank','63','Data Type'),(848,'rank','88','Data Type'),(849,'identifier','156','Data Type'),(850,'linkedPatientId','156','Data Type'),(851,'code','156','Data Type');
INSERT INTO `QDM_ATTRIBUTES_BACKUP` VALUES ('10','provider preference','1','Data Type'),('1001','facility location','19','Data Type'),('101','negation rationale','14','Data Type'),('1010','start datetime','97','Data Type'),('1011','stop datetime','97','Data Type'),('1012','date','98','Data Type'),('1013','time','98','Data Type'),('1014','cause','98','Data Type'),('1015','start datetime','99','Data Type'),('1016','stop datetime','99','Data Type'),('1017','reason','99','Data Type'),('1018','start datetime','100','Data Type'),('1019','stop datetime','100','Data Type'),('102','patient preference','14','Data Type'),('1020','start datetime','101','Data Type'),('1021','stop datetime','101','Data Type'),('1023','dose','104','Data Type'),('1024','frequency','104','Data Type'),('1026','refills','104','Data Type'),('1027','route','104','Data Type'),('1028','start datetime','104','Data Type'),('1029','stop datetime','104','Data Type'),('103','provider preference','14','Data Type'),('1030','negation rationale','104','Data Type'),('1031','anatomical location site','7','Data Type'),('1032','anatomical location site','10','Data Type'),('1033','anatomical location site','9','Data Type'),('1034','anatomical location site','13','Data Type'),('1038','anatomical location site','56','Data Type'),('1039','anatomical location site','57','Data Type'),('104','reaction','14','Data Type'),('1040','anatomical location site','87','Data Type'),('1041','anatomical location site','62','Data Type'),('1042','anatomical location site','63','Data Type'),('1043','anatomical approach site','13','Data Type'),('1047','anatomical approach site','56','Data Type'),('1048','anatomical approach site','87','Data Type'),('1049','anatomical approach site','57','Data Type'),('105','start datetime','14','Data Type'),('1050','anatomical approach site','63','Data Type'),('1051','anatomical approach site','62','Data Type'),('1052','result','19','Data Type'),('1053','result','36','Data Type'),('1054','result','57','Data Type'),('1055','result','24','Data Type'),('1056','result','31','Data Type'),('1057','status','19','Data Type'),('1058','status','31','Data Type'),('1059','status','36','Data Type'),('106','stop datetime','14','Data Type'),('1060','patient preference','104','Data Type'),('1061','provider preference','104','Data Type'),('1062','cumulative medication duration','39','Data Type'),('1063','method','61','Data Type'),('1064','ordinality','61','Data Type'),('1065','radiation duration','62','Data Type'),('1066','status','63','Data Type'),('1067','radiation duration','63','Data Type'),('1068','radiation dosage','63','Data Type'),('1069','anatomical approach site','88','Data Type'),('1070','anatomical location site','88','Data Type'),('1071','active datetime','44','Data Type'),('1072','signed datetime','44','Data Type'),('1073','target outcome','3','Data Type'),('108','negation rationale','15','Data Type'),('109','patient preference','15','Data Type'),('11','start datetime','1','Data Type'),('110','provider preference','15','Data Type'),('111','reason','15','Data Type'),('112','start datetime','15','Data Type'),('113','stop datetime','15','Data Type'),('115','negation rationale','78','Data Type'),('116','patient preference','78','Data Type'),('117','provider preference','78','Data Type'),('118','reason','78','Data Type'),('119','start datetime','78','Data Type'),('12','stop datetime','1','Data Type'),('120','stop datetime','78','Data Type'),('122','negation rationale','16','Data Type'),('123','patient preference','16','Data Type'),('124','provider preference','16','Data Type'),('125','radiation dosage','16','Data Type'),('126','radiation duration','16','Data Type'),('127','reaction','16','Data Type'),('128','start datetime','16','Data Type'),('129','stop datetime','16','Data Type'),('131','negation rationale','17','Data Type'),('132','patient preference','17','Data Type'),('133','provider preference','17','Data Type'),('134','radiation dosage','17','Data Type'),('135','radiation duration','17','Data Type'),('136','reaction','17','Data Type'),('137','start datetime','17','Data Type'),('138','stop datetime','17','Data Type'),('14','negation rationale','2','Data Type'),('140','method','18','Data Type'),('141','negation rationale','18','Data Type'),('142','patient preference','18','Data Type'),('143','provider preference','18','Data Type'),('144','radiation dosage','18','Data Type'),('145','radiation duration','18','Data Type'),('146','reason','18','Data Type'),('147','start datetime','18','Data Type'),('148','stop datetime','18','Data Type'),('15','patient preference','2','Data Type'),('150','method','19','Data Type'),('151','negation rationale','19','Data Type'),('152','patient preference','19','Data Type'),('153','provider preference','19','Data Type'),('154','radiation dosage','19','Data Type'),('155','radiation duration','19','Data Type'),('156','reason','19','Data Type'),('157','start datetime','19','Data Type'),('158','stop datetime','19','Data Type'),('16','provider preference','2','Data Type'),('160','method','90','Data Type'),('161','negation rationale','90','Data Type'),('162','patient preference','90','Data Type'),('163','provider preference','90','Data Type'),('164','radiation dosage','90','Data Type'),('165','radiation duration','90','Data Type'),('166','start datetime','90','Data Type'),('167','stop datetime','90','Data Type'),('17','start datetime','2','Data Type'),('179','length of stay','95','Data Type'),('18','stop datetime','2','Data Type'),('181','facility location','95','Data Type'),('182','negation rationale','95','Data Type'),('183','patient preference','95','Data Type'),('184','provider preference','95','Data Type'),('185','reason','95','Data Type'),('186','admission datetime','95','Data Type'),('187','discharge datetime','95','Data Type'),('198','facility location','21','Data Type'),('199','negation rationale','21','Data Type'),('20','negation rationale','3','Data Type'),('200','patient preference','21','Data Type'),('201','provider preference','21','Data Type'),('202','reason','21','Data Type'),('203','start datetime','21','Data Type'),('204','stop datetime','21','Data Type'),('205','length of stay','22','Data Type'),('207','facility location','22','Data Type'),('208','negation rationale','22','Data Type'),('209','patient preference','22','Data Type'),('21','patient preference','3','Data Type'),('210','provider preference','22','Data Type'),('211','reason','22','Data Type'),('212','admission datetime','22','Data Type'),('213','discharge datetime','22','Data Type'),('215','facility location','79','Data Type'),('216','negation rationale','79','Data Type'),('217','patient preference','79','Data Type'),('218','provider preference','79','Data Type'),('219','reason','79','Data Type'),('22','provider preference','3','Data Type'),('220','start datetime','79','Data Type'),('221','stop datetime','79','Data Type'),('223','method','23','Data Type'),('224','negation rationale','23','Data Type'),('225','patient preference','23','Data Type'),('226','provider preference','23','Data Type'),('227','reason','23','Data Type'),('228','start datetime','23','Data Type'),('229','stop datetime','23','Data Type'),('23','start datetime','3','Data Type'),('231','method','24','Data Type'),('232','negation rationale','24','Data Type'),('233','patient preference','24','Data Type'),('234','provider preference','24','Data Type'),('235','reason','24','Data Type'),('236','start datetime','24','Data Type'),('237','stop datetime','24','Data Type'),('239','method','80','Data Type'),('24','stop datetime','3','Data Type'),('240','negation rationale','80','Data Type'),('241','patient preference','80','Data Type'),('242','provider preference','80','Data Type'),('243','reason','80','Data Type'),('244','start datetime','80','Data Type'),('245','stop datetime','80','Data Type'),('256','start datetime','26','Data Type'),('257','stop datetime','26','Data Type'),('259','negation rationale','27','Data Type'),('26','negation rationale','5','Data Type'),('260','start datetime','27','Data Type'),('261','stop datetime','27','Data Type'),('263','negation rationale','28','Data Type'),('264','patient preference','28','Data Type'),('265','provider preference','28','Data Type'),('266','reaction','28','Data Type'),('267','start datetime','28','Data Type'),('268','stop datetime','28','Data Type'),('27','patient preference','5','Data Type'),('270','negation rationale','29','Data Type'),('271','patient preference','29','Data Type'),('272','provider preference','29','Data Type'),('273','reaction','29','Data Type'),('274','start datetime','29','Data Type'),('275','stop datetime','29','Data Type'),('278','negation rationale','30','Data Type'),('279','patient preference','30','Data Type'),('28','provider preference','5','Data Type'),('280','provider preference','30','Data Type'),('281','reason','30','Data Type'),('282','start datetime','30','Data Type'),('283','stop datetime','30','Data Type'),('286','negation rationale','31','Data Type'),('287','patient preference','31','Data Type'),('288','provider preference','31','Data Type'),('289','reason','31','Data Type'),('29','start datetime','5','Data Type'),('290','start datetime','31','Data Type'),('291','stop datetime','31','Data Type'),('294','negation rationale','81','Data Type'),('295','patient preference','81','Data Type'),('296','provider preference','81','Data Type'),('297','reason','81','Data Type'),('298','start datetime','81','Data Type'),('299','stop datetime','81','Data Type'),('30','stop datetime','5','Data Type'),('310','negation rationale','33','Data Type'),('311','patient preference','33','Data Type'),('312','provider preference','33','Data Type'),('313','reaction','33','Data Type'),('314','start datetime','33','Data Type'),('315','stop datetime','33','Data Type'),('317','negation rationale','34','Data Type'),('318','patient preference','34','Data Type'),('319','provider preference','34','Data Type'),('32','negation rationale','6','Data Type'),('320','reaction','34','Data Type'),('321','start datetime','34','Data Type'),('322','stop datetime','34','Data Type'),('324','method','35','Data Type'),('325','negation rationale','35','Data Type'),('326','patient preference','35','Data Type'),('327','provider preference','35','Data Type'),('328','reason','35','Data Type'),('329','start datetime','35','Data Type'),('33','patient preference','6','Data Type'),('330','stop datetime','35','Data Type'),('332','method','36','Data Type'),('333','negation rationale','36','Data Type'),('334','patient preference','36','Data Type'),('335','provider preference','36','Data Type'),('336','reason','36','Data Type'),('337','start datetime','36','Data Type'),('338','stop datetime','36','Data Type'),('34','provider preference','6','Data Type'),('340','method','82','Data Type'),('341','negation rationale','82','Data Type'),('342','patient preference','82','Data Type'),('343','provider preference','82','Data Type'),('344','reason','82','Data Type'),('345','start datetime','82','Data Type'),('346','stop datetime','82','Data Type'),('35','start datetime','6','Data Type'),('356','cumulative medication duration','38','Data Type'),('357','dose','38','Data Type'),('358','frequency','38','Data Type'),('36','stop datetime','6','Data Type'),('360','negation rationale','38','Data Type'),('362','patient preference','38','Data Type'),('363','provider preference','38','Data Type'),('365','route','38','Data Type'),('366','start datetime','38','Data Type'),('367','stop datetime','38','Data Type'),('368','dose','39','Data Type'),('369','frequency','39','Data Type'),('371','negation rationale','39','Data Type'),('373','patient preference','39','Data Type'),('374','provider preference','39','Data Type'),('376','route','39','Data Type'),('377','start datetime','39','Data Type'),('378','stop datetime','39','Data Type'),('38','negation rationale','4','Data Type'),('382','negation rationale','40','Data Type'),('384','patient preference','40','Data Type'),('385','provider preference','40','Data Type'),('386','reaction','40','Data Type'),('389','start datetime','40','Data Type'),('39','patient preference','4','Data Type'),('390','stop datetime','40','Data Type'),('394','negation rationale','41','Data Type'),('396','patient preference','41','Data Type'),('397','provider preference','41','Data Type'),('398','reaction','41','Data Type'),('40','provider preference','4','Data Type'),('401','start datetime','41','Data Type'),('402','stop datetime','41','Data Type'),('403','cumulative medication duration','42','Data Type'),('404','dose','42','Data Type'),('405','frequency','42','Data Type'),('407','negation rationale','42','Data Type'),('409','patient preference','42','Data Type'),('41','start datetime','4','Data Type'),('410','provider preference','42','Data Type'),('411','refills','42','Data Type'),('412','route','42','Data Type'),('413','start datetime','42','Data Type'),('414','stop datetime','42','Data Type'),('418','negation rationale','43','Data Type'),('42','stop datetime','4','Data Type'),('420','patient preference','43','Data Type'),('421','provider preference','43','Data Type'),('422','reaction','43','Data Type'),('425','start datetime','43','Data Type'),('426','stop datetime','43','Data Type'),('427','cumulative medication duration','44','Data Type'),('428','dose','44','Data Type'),('429','frequency','44','Data Type'),('431','method','44','Data Type'),('432','negation rationale','44','Data Type'),('434','patient preference','44','Data Type'),('435','provider preference','44','Data Type'),('436','reason','44','Data Type'),('437','refills','44','Data Type'),('438','route','44','Data Type'),('439','start datetime','44','Data Type'),('44','negation rationale','7','Data Type'),('440','stop datetime','44','Data Type'),('45','ordinality','7','Data Type'),('453','method','56','Data Type'),('454','negation rationale','56','Data Type'),('455','patient preference','56','Data Type'),('456','provider preference','56','Data Type'),('457','reason','56','Data Type'),('458','start datetime','56','Data Type'),('459','stop datetime','56','Data Type'),('46','patient preference','7','Data Type'),('462','method','57','Data Type'),('463','negation rationale','57','Data Type'),('464','patient preference','57','Data Type'),('465','provider preference','57','Data Type'),('466','reason','57','Data Type'),('467','start datetime','57','Data Type'),('468','stop datetime','57','Data Type'),('47','provider preference','7','Data Type'),('471','method','87','Data Type'),('472','negation rationale','87','Data Type'),('473','patient preference','87','Data Type'),('474','provider preference','87','Data Type'),('475','reason','87','Data Type'),('476','start datetime','87','Data Type'),('477','stop datetime','87','Data Type'),('479','negation rationale','60','Data Type'),('48','severity','7','Data Type'),('480','patient preference','60','Data Type'),('481','provider preference','60','Data Type'),('482','reaction','60','Data Type'),('483','start datetime','60','Data Type'),('484','stop datetime','60','Data Type'),('486','negation rationale','61','Data Type'),('487','patient preference','61','Data Type'),('488','provider preference','61','Data Type'),('489','reaction','61','Data Type'),('49','start datetime','7','Data Type'),('490','start datetime','61','Data Type'),('491','stop datetime','61','Data Type'),('493','method','62','Data Type'),('494','negation rationale','62','Data Type'),('495','patient preference','62','Data Type'),('496','provider preference','62','Data Type'),('497','reason','62','Data Type'),('498','start datetime','62','Data Type'),('499','stop datetime','62','Data Type'),('501','method','63','Data Type'),('502','negation rationale','63','Data Type'),('503','patient preference','63','Data Type'),('504','provider preference','63','Data Type'),('505','reason','63','Data Type'),('506','start datetime','63','Data Type'),('507','stop datetime','63','Data Type'),('509','method','88','Data Type'),('510','negation rationale','88','Data Type'),('511','patient preference','88','Data Type'),('512','provider preference','88','Data Type'),('513','reason','88','Data Type'),('514','start datetime','88','Data Type'),('515','stop datetime','88','Data Type'),('52','stop datetime','7','Data Type'),('526','negation rationale','65','Data Type'),('527','patient preference','65','Data Type'),('528','provider preference','65','Data Type'),('529','start datetime','65','Data Type'),('530','stop datetime','65','Data Type'),('531','dose','66','Data Type'),('533','frequency','66','Data Type'),('534','negation rationale','66','Data Type'),('536','patient preference','66','Data Type'),('537','provider preference','66','Data Type'),('539','route','66','Data Type'),('54','negation rationale','8','Data Type'),('540','start datetime','66','Data Type'),('541','stop datetime','66','Data Type'),('545','negation rationale','67','Data Type'),('547','patient preference','67','Data Type'),('548','provider preference','67','Data Type'),('549','reaction','67','Data Type'),('55','ordinality','8','Data Type'),('552','start datetime','67','Data Type'),('553','stop datetime','67','Data Type'),('557','negation rationale','68','Data Type'),('559','patient preference','68','Data Type'),('56','patient preference','8','Data Type'),('560','provider preference','68','Data Type'),('561','reaction','68','Data Type'),('564','start datetime','68','Data Type'),('565','stop datetime','68','Data Type'),('569','negation rationale','69','Data Type'),('57','provider preference','8','Data Type'),('571','patient preference','69','Data Type'),('572','provider preference','69','Data Type'),('573','reaction','69','Data Type'),('576','start datetime','69','Data Type'),('577','stop datetime','69','Data Type'),('578','dose','70','Data Type'),('58','severity','8','Data Type'),('580','frequency','70','Data Type'),('581','method','70','Data Type'),('582','negation rationale','70','Data Type'),('584','patient preference','70','Data Type'),('585','provider preference','70','Data Type'),('586','reason','70','Data Type'),('587','refills','70','Data Type'),('588','route','70','Data Type'),('589','start datetime','70','Data Type'),('59','start datetime','8','Data Type'),('590','stop datetime','70','Data Type'),('591','dose','89','Data Type'),('593','frequency','89','Data Type'),('594','method','89','Data Type'),('595','negation rationale','89','Data Type'),('597','patient preference','89','Data Type'),('598','provider preference','89','Data Type'),('599','reason','89','Data Type'),('60','status','8','Data Type'),('600','refills','89','Data Type'),('601','route','89','Data Type'),('602','start datetime','89','Data Type'),('603','stop datetime','89','Data Type'),('605','negation rationale','71','Data Type'),('606','ordinality','71','Data Type'),('607','patient preference','71','Data Type'),('608','provider preference','71','Data Type'),('609','severity','71','Data Type'),('61','stop datetime','8','Data Type'),('610','start datetime','71','Data Type'),('612','stop datetime','71','Data Type'),('614','negation rationale','72','Data Type'),('615','ordinality','72','Data Type'),('616','patient preference','72','Data Type'),('617','provider preference','72','Data Type'),('618','severity','72','Data Type'),('619','start datetime','72','Data Type'),('621','stop datetime','72','Data Type'),('623','negation rationale','73','Data Type'),('624','ordinality','73','Data Type'),('625','patient preference','73','Data Type'),('626','provider preference','73','Data Type'),('627','severity','73','Data Type'),('628','start datetime','73','Data Type'),('63','negation rationale','9','Data Type'),('630','stop datetime','73','Data Type'),('632','negation rationale','74','Data Type'),('633','ordinality','74','Data Type'),('634','patient preference','74','Data Type'),('635','provider preference','74','Data Type'),('636','severity','74','Data Type'),('637','start datetime','74','Data Type'),('639','stop datetime','74','Data Type'),('64','ordinality','9','Data Type'),('641','negation rationale','75','Data Type'),('642','start datetime','75','Data Type'),('643','stop datetime','75','Data Type'),('645','negation rationale','76','Data Type'),('646','patient preference','76','Data Type'),('647','provider preference','76','Data Type'),('648','start datetime','76','Data Type'),('649','stop datetime','76','Data Type'),('65','patient preference','9','Data Type'),('651','negation rationale','77','Data Type'),('652','patient preference','77','Data Type'),('653','provider preference','77','Data Type'),('654','start datetime','77','Data Type'),('655','stop datetime','77','Data Type'),('66','provider preference','9','Data Type'),('662','Health Record Field','','Data Flow'),('663','laterality','7','Data Type'),('664','reason','13','Data Type'),('665','discharge status','22','Data Type'),('669','facility location arrival datetime','95','Data Type'),('67','severity','9','Data Type'),('670','facility location departure datetime','95','Data Type'),('671','facility location arrival datetime','22','Data Type'),('672','facility location departure datetime','22','Data Type'),('673','reason','39','Data Type'),('676','ordinality','62','Data Type'),('677','ordinality','63','Data Type'),('678','result','63','Data Type'),('679','incision datetime','63','Data Type'),('68','start datetime','9','Data Type'),('680','ordinality','88','Data Type'),('685','result','65','Data Type'),('689','related to','3','Data Type'),('70','stop datetime','9','Data Type'),('701','Source','','Data Flow'),('702','Recorder','','Data Flow'),('72','negation rationale','10','Data Type'),('73','ordinality','10','Data Type'),('74','patient preference','10','Data Type'),('75','provider preference','10','Data Type'),('76','severity','10','Data Type'),('77','start datetime','10','Data Type'),('79','stop datetime','10','Data Type'),('8','negation rationale','1','Data Type'),('81','negation rationale','11','Data Type'),('82','patient preference','11','Data Type'),('83','provider preference','11','Data Type'),('84','reaction','11','Data Type'),('85','start datetime','11','Data Type'),('86','stop datetime','11','Data Type'),('88','negation rationale','12','Data Type'),('89','patient preference','12','Data Type'),('9','patient preference','1','Data Type'),('90','provider preference','12','Data Type'),('91','reaction','12','Data Type'),('92','start datetime','12','Data Type'),('93','stop datetime','12','Data Type'),('95','negation rationale','13','Data Type'),('96','patient preference','13','Data Type'),('97','provider preference','13','Data Type'),('98','start datetime','13','Data Type'),('99','removal datetime','13','Data Type');
INSERT INTO `QDM_ATTRIBUTES_BACKUP_AUG2015` VALUES ('10','provider preference','1','Data Type'),('1001','facility location','19','Data Type'),('101','negation rationale','14','Data Type'),('1010','start datetime','97','Data Type'),('1011','stop datetime','97','Data Type'),('1012','date','98','Data Type'),('1013','time','98','Data Type'),('1014','cause','98','Data Type'),('1015','start datetime','99','Data Type'),('1016','stop datetime','99','Data Type'),('1017','reason','99','Data Type'),('1018','start datetime','100','Data Type'),('1019','stop datetime','100','Data Type'),('102','patient preference','14','Data Type'),('1020','start datetime','101','Data Type'),('1021','stop datetime','101','Data Type'),('1023','dose','104','Data Type'),('1024','frequency','104','Data Type'),('1026','refills','104','Data Type'),('1027','route','104','Data Type'),('1028','start datetime','104','Data Type'),('1029','stop datetime','104','Data Type'),('103','provider preference','14','Data Type'),('1030','negation rationale','104','Data Type'),('1031','anatomical location site','7','Data Type'),('1032','anatomical location site','10','Data Type'),('1033','anatomical location site','9','Data Type'),('1034','anatomical location site','13','Data Type'),('1038','anatomical location site','56','Data Type'),('1039','anatomical location site','57','Data Type'),('104','reaction','14','Data Type'),('1040','anatomical location site','87','Data Type'),('1041','anatomical location site','62','Data Type'),('1042','anatomical location site','63','Data Type'),('1043','anatomical approach site','13','Data Type'),('105','start datetime','14','Data Type'),('1050','anatomical approach site','63','Data Type'),('1051','anatomical approach site','62','Data Type'),('1052','result','19','Data Type'),('1053','result','36','Data Type'),('1054','result','57','Data Type'),('1055','result','24','Data Type'),('1056','result','31','Data Type'),('1057','status','19','Data Type'),('1058','status','31','Data Type'),('1059','status','36','Data Type'),('106','stop datetime','14','Data Type'),('1060','patient preference','104','Data Type'),('1061','provider preference','104','Data Type'),('1062','cumulative medication duration','39','Data Type'),('1064','ordinality','61','Data Type'),('1065','radiation duration','62','Data Type'),('1066','status','63','Data Type'),('1067','radiation duration','63','Data Type'),('1068','radiation dosage','63','Data Type'),('1069','anatomical approach site','88','Data Type'),('1070','anatomical location site','88','Data Type'),('1071','active datetime','44','Data Type'),('1072','signed datetime','44','Data Type'),('1073','target outcome','3','Data Type'),('108','negation rationale','15','Data Type'),('109','patient preference','15','Data Type'),('11','start datetime','1','Data Type'),('110','provider preference','15','Data Type'),('111','reason','15','Data Type'),('112','start datetime','15','Data Type'),('113','stop datetime','15','Data Type'),('115','negation rationale','78','Data Type'),('116','patient preference','78','Data Type'),('117','provider preference','78','Data Type'),('118','reason','78','Data Type'),('119','start datetime','78','Data Type'),('12','stop datetime','1','Data Type'),('120','stop datetime','78','Data Type'),('122','negation rationale','16','Data Type'),('123','patient preference','16','Data Type'),('124','provider preference','16','Data Type'),('125','radiation dosage','16','Data Type'),('126','radiation duration','16','Data Type'),('127','reaction','16','Data Type'),('128','start datetime','16','Data Type'),('129','stop datetime','16','Data Type'),('131','negation rationale','17','Data Type'),('132','patient preference','17','Data Type'),('133','provider preference','17','Data Type'),('134','radiation dosage','17','Data Type'),('135','radiation duration','17','Data Type'),('136','reaction','17','Data Type'),('137','start datetime','17','Data Type'),('138','stop datetime','17','Data Type'),('14','negation rationale','2','Data Type'),('140','method','18','Data Type'),('141','negation rationale','18','Data Type'),('142','patient preference','18','Data Type'),('143','provider preference','18','Data Type'),('144','radiation dosage','18','Data Type'),('145','radiation duration','18','Data Type'),('146','reason','18','Data Type'),('147','start datetime','18','Data Type'),('148','stop datetime','18','Data Type'),('15','patient preference','2','Data Type'),('150','method','19','Data Type'),('151','negation rationale','19','Data Type'),('152','patient preference','19','Data Type'),('153','provider preference','19','Data Type'),('154','radiation dosage','19','Data Type'),('155','radiation duration','19','Data Type'),('156','reason','19','Data Type'),('157','start datetime','19','Data Type'),('158','stop datetime','19','Data Type'),('16','provider preference','2','Data Type'),('160','method','90','Data Type'),('161','negation rationale','90','Data Type'),('162','patient preference','90','Data Type'),('163','provider preference','90','Data Type'),('164','radiation dosage','90','Data Type'),('165','radiation duration','90','Data Type'),('166','start datetime','90','Data Type'),('167','stop datetime','90','Data Type'),('17','start datetime','2','Data Type'),('179','length of stay','95','Data Type'),('18','stop datetime','2','Data Type'),('181','facility location','95','Data Type'),('182','negation rationale','95','Data Type'),('183','patient preference','95','Data Type'),('184','provider preference','95','Data Type'),('185','reason','95','Data Type'),('186','admission datetime','95','Data Type'),('187','discharge datetime','95','Data Type'),('198','facility location','21','Data Type'),('199','negation rationale','21','Data Type'),('20','negation rationale','3','Data Type'),('200','patient preference','21','Data Type'),('201','provider preference','21','Data Type'),('202','reason','21','Data Type'),('203','start datetime','21','Data Type'),('204','stop datetime','21','Data Type'),('205','length of stay','22','Data Type'),('207','facility location','22','Data Type'),('208','negation rationale','22','Data Type'),('209','patient preference','22','Data Type'),('21','patient preference','3','Data Type'),('210','provider preference','22','Data Type'),('211','reason','22','Data Type'),('212','admission datetime','22','Data Type'),('213','discharge datetime','22','Data Type'),('215','facility location','79','Data Type'),('216','negation rationale','79','Data Type'),('217','patient preference','79','Data Type'),('218','provider preference','79','Data Type'),('219','reason','79','Data Type'),('22','provider preference','3','Data Type'),('220','start datetime','79','Data Type'),('221','stop datetime','79','Data Type'),('223','method','23','Data Type'),('224','negation rationale','23','Data Type'),('225','patient preference','23','Data Type'),('226','provider preference','23','Data Type'),('227','reason','23','Data Type'),('228','start datetime','23','Data Type'),('229','stop datetime','23','Data Type'),('23','start datetime','3','Data Type'),('231','method','24','Data Type'),('232','negation rationale','24','Data Type'),('233','patient preference','24','Data Type'),('234','provider preference','24','Data Type'),('235','reason','24','Data Type'),('236','start datetime','24','Data Type'),('237','stop datetime','24','Data Type'),('239','method','80','Data Type'),('24','stop datetime','3','Data Type'),('240','negation rationale','80','Data Type'),('241','patient preference','80','Data Type'),('242','provider preference','80','Data Type'),('243','reason','80','Data Type'),('244','start datetime','80','Data Type'),('245','stop datetime','80','Data Type'),('256','start datetime','26','Data Type'),('257','stop datetime','26','Data Type'),('259','negation rationale','27','Data Type'),('26','negation rationale','5','Data Type'),('260','start datetime','27','Data Type'),('261','stop datetime','27','Data Type'),('263','negation rationale','28','Data Type'),('264','patient preference','28','Data Type'),('265','provider preference','28','Data Type'),('266','reaction','28','Data Type'),('267','start datetime','28','Data Type'),('268','stop datetime','28','Data Type'),('27','patient preference','5','Data Type'),('270','negation rationale','29','Data Type'),('271','patient preference','29','Data Type'),('272','provider preference','29','Data Type'),('273','reaction','29','Data Type'),('274','start datetime','29','Data Type'),('275','stop datetime','29','Data Type'),('278','negation rationale','30','Data Type'),('279','patient preference','30','Data Type'),('28','provider preference','5','Data Type'),('280','provider preference','30','Data Type'),('281','reason','30','Data Type'),('282','start datetime','30','Data Type'),('283','stop datetime','30','Data Type'),('286','negation rationale','31','Data Type'),('287','patient preference','31','Data Type'),('288','provider preference','31','Data Type'),('289','reason','31','Data Type'),('29','start datetime','5','Data Type'),('290','start datetime','31','Data Type'),('291','stop datetime','31','Data Type'),('294','negation rationale','81','Data Type'),('295','patient preference','81','Data Type'),('296','provider preference','81','Data Type'),('297','reason','81','Data Type'),('298','start datetime','81','Data Type'),('299','stop datetime','81','Data Type'),('30','stop datetime','5','Data Type'),('310','negation rationale','33','Data Type'),('311','patient preference','33','Data Type'),('312','provider preference','33','Data Type'),('313','reaction','33','Data Type'),('314','start datetime','33','Data Type'),('315','stop datetime','33','Data Type'),('317','negation rationale','34','Data Type'),('318','patient preference','34','Data Type'),('319','provider preference','34','Data Type'),('32','negation rationale','6','Data Type'),('320','reaction','34','Data Type'),('321','start datetime','34','Data Type'),('322','stop datetime','34','Data Type'),('324','method','35','Data Type'),('325','negation rationale','35','Data Type'),('326','patient preference','35','Data Type'),('327','provider preference','35','Data Type'),('328','reason','35','Data Type'),('329','start datetime','35','Data Type'),('33','patient preference','6','Data Type'),('330','stop datetime','35','Data Type'),('332','method','36','Data Type'),('333','negation rationale','36','Data Type'),('334','patient preference','36','Data Type'),('335','provider preference','36','Data Type'),('336','reason','36','Data Type'),('337','start datetime','36','Data Type'),('338','stop datetime','36','Data Type'),('34','provider preference','6','Data Type'),('340','method','82','Data Type'),('341','negation rationale','82','Data Type'),('342','patient preference','82','Data Type'),('343','provider preference','82','Data Type'),('344','reason','82','Data Type'),('345','start datetime','82','Data Type'),('346','stop datetime','82','Data Type'),('35','start datetime','6','Data Type'),('356','cumulative medication duration','38','Data Type'),('357','dose','38','Data Type'),('358','frequency','38','Data Type'),('36','stop datetime','6','Data Type'),('360','negation rationale','38','Data Type'),('362','patient preference','38','Data Type'),('363','provider preference','38','Data Type'),('365','route','38','Data Type'),('366','start datetime','38','Data Type'),('367','stop datetime','38','Data Type'),('368','dose','39','Data Type'),('369','frequency','39','Data Type'),('371','negation rationale','39','Data Type'),('373','patient preference','39','Data Type'),('374','provider preference','39','Data Type'),('376','route','39','Data Type'),('377','start datetime','39','Data Type'),('378','stop datetime','39','Data Type'),('38','negation rationale','4','Data Type'),('382','negation rationale','40','Data Type'),('384','patient preference','40','Data Type'),('385','provider preference','40','Data Type'),('386','reaction','40','Data Type'),('389','start datetime','40','Data Type'),('39','patient preference','4','Data Type'),('390','stop datetime','40','Data Type'),('394','negation rationale','41','Data Type'),('396','patient preference','41','Data Type'),('397','provider preference','41','Data Type'),('398','reaction','41','Data Type'),('40','provider preference','4','Data Type'),('401','start datetime','41','Data Type'),('402','stop datetime','41','Data Type'),('403','cumulative medication duration','42','Data Type'),('404','dose','42','Data Type'),('405','frequency','42','Data Type'),('407','negation rationale','42','Data Type'),('409','patient preference','42','Data Type'),('41','start datetime','4','Data Type'),('410','provider preference','42','Data Type'),('411','refills','42','Data Type'),('412','route','42','Data Type'),('413','start datetime','42','Data Type'),('414','stop datetime','42','Data Type'),('418','negation rationale','43','Data Type'),('42','stop datetime','4','Data Type'),('420','patient preference','43','Data Type'),('421','provider preference','43','Data Type'),('422','reaction','43','Data Type'),('425','start datetime','43','Data Type'),('426','stop datetime','43','Data Type'),('427','cumulative medication duration','44','Data Type'),('428','dose','44','Data Type'),('429','frequency','44','Data Type'),('431','method','44','Data Type'),('432','negation rationale','44','Data Type'),('434','patient preference','44','Data Type'),('435','provider preference','44','Data Type'),('436','reason','44','Data Type'),('437','refills','44','Data Type'),('438','route','44','Data Type'),('439','start datetime','44','Data Type'),('44','negation rationale','7','Data Type'),('440','stop datetime','44','Data Type'),('45','ordinality','7','Data Type'),('453','method','56','Data Type'),('454','negation rationale','56','Data Type'),('455','patient preference','56','Data Type'),('456','provider preference','56','Data Type'),('457','reason','56','Data Type'),('458','start datetime','56','Data Type'),('459','stop datetime','56','Data Type'),('46','patient preference','7','Data Type'),('462','method','57','Data Type'),('463','negation rationale','57','Data Type'),('464','patient preference','57','Data Type'),('465','provider preference','57','Data Type'),('466','reason','57','Data Type'),('467','start datetime','57','Data Type'),('468','stop datetime','57','Data Type'),('47','provider preference','7','Data Type'),('471','method','87','Data Type'),('472','negation rationale','87','Data Type'),('473','patient preference','87','Data Type'),('474','provider preference','87','Data Type'),('475','reason','87','Data Type'),('476','start datetime','87','Data Type'),('477','stop datetime','87','Data Type'),('479','negation rationale','60','Data Type'),('48','severity','7','Data Type'),('480','patient preference','60','Data Type'),('481','provider preference','60','Data Type'),('482','reaction','60','Data Type'),('483','start datetime','60','Data Type'),('484','stop datetime','60','Data Type'),('486','negation rationale','61','Data Type'),('487','patient preference','61','Data Type'),('488','provider preference','61','Data Type'),('489','reaction','61','Data Type'),('49','start datetime','7','Data Type'),('490','start datetime','61','Data Type'),('491','stop datetime','61','Data Type'),('493','method','62','Data Type'),('494','negation rationale','62','Data Type'),('495','patient preference','62','Data Type'),('496','provider preference','62','Data Type'),('497','reason','62','Data Type'),('498','start datetime','62','Data Type'),('499','stop datetime','62','Data Type'),('501','method','63','Data Type'),('502','negation rationale','63','Data Type'),('503','patient preference','63','Data Type'),('504','provider preference','63','Data Type'),('505','reason','63','Data Type'),('506','start datetime','63','Data Type'),('507','stop datetime','63','Data Type'),('509','method','88','Data Type'),('510','negation rationale','88','Data Type'),('511','patient preference','88','Data Type'),('512','provider preference','88','Data Type'),('513','reason','88','Data Type'),('514','start datetime','88','Data Type'),('515','stop datetime','88','Data Type'),('52','stop datetime','7','Data Type'),('526','negation rationale','65','Data Type'),('527','patient preference','65','Data Type'),('528','provider preference','65','Data Type'),('529','start datetime','65','Data Type'),('530','stop datetime','65','Data Type'),('531','dose','66','Data Type'),('533','frequency','66','Data Type'),('534','negation rationale','66','Data Type'),('536','patient preference','66','Data Type'),('537','provider preference','66','Data Type'),('539','route','66','Data Type'),('54','negation rationale','8','Data Type'),('540','start datetime','66','Data Type'),('541','stop datetime','66','Data Type'),('545','negation rationale','67','Data Type'),('547','patient preference','67','Data Type'),('548','provider preference','67','Data Type'),('549','reaction','67','Data Type'),('55','ordinality','8','Data Type'),('552','start datetime','67','Data Type'),('553','stop datetime','67','Data Type'),('557','negation rationale','68','Data Type'),('559','patient preference','68','Data Type'),('56','patient preference','8','Data Type'),('560','provider preference','68','Data Type'),('561','reaction','68','Data Type'),('564','start datetime','68','Data Type'),('565','stop datetime','68','Data Type'),('569','negation rationale','69','Data Type'),('57','provider preference','8','Data Type'),('571','patient preference','69','Data Type'),('572','provider preference','69','Data Type'),('573','reaction','69','Data Type'),('576','start datetime','69','Data Type'),('577','stop datetime','69','Data Type'),('578','dose','70','Data Type'),('58','severity','8','Data Type'),('580','frequency','70','Data Type'),('581','method','70','Data Type'),('582','negation rationale','70','Data Type'),('584','patient preference','70','Data Type'),('585','provider preference','70','Data Type'),('586','reason','70','Data Type'),('587','refills','70','Data Type'),('588','route','70','Data Type'),('589','start datetime','70','Data Type'),('59','start datetime','8','Data Type'),('590','stop datetime','70','Data Type'),('591','dose','89','Data Type'),('593','frequency','89','Data Type'),('594','method','89','Data Type'),('595','negation rationale','89','Data Type'),('597','patient preference','89','Data Type'),('598','provider preference','89','Data Type'),('599','reason','89','Data Type'),('60','status','8','Data Type'),('600','refills','89','Data Type'),('601','route','89','Data Type'),('602','start datetime','89','Data Type'),('603','stop datetime','89','Data Type'),('605','negation rationale','71','Data Type'),('606','ordinality','71','Data Type'),('607','patient preference','71','Data Type'),('608','provider preference','71','Data Type'),('609','severity','71','Data Type'),('61','stop datetime','8','Data Type'),('610','start datetime','71','Data Type'),('612','stop datetime','71','Data Type'),('614','negation rationale','72','Data Type'),('615','ordinality','72','Data Type'),('616','patient preference','72','Data Type'),('617','provider preference','72','Data Type'),('618','severity','72','Data Type'),('619','start datetime','72','Data Type'),('621','stop datetime','72','Data Type'),('623','negation rationale','73','Data Type'),('624','ordinality','73','Data Type'),('625','patient preference','73','Data Type'),('626','provider preference','73','Data Type'),('627','severity','73','Data Type'),('628','start datetime','73','Data Type'),('63','negation rationale','9','Data Type'),('630','stop datetime','73','Data Type'),('632','negation rationale','74','Data Type'),('633','ordinality','74','Data Type'),('634','patient preference','74','Data Type'),('635','provider preference','74','Data Type'),('636','severity','74','Data Type'),('637','start datetime','74','Data Type'),('639','stop datetime','74','Data Type'),('64','ordinality','9','Data Type'),('641','negation rationale','75','Data Type'),('642','start datetime','75','Data Type'),('643','stop datetime','75','Data Type'),('645','negation rationale','76','Data Type'),('646','patient preference','76','Data Type'),('647','provider preference','76','Data Type'),('648','start datetime','76','Data Type'),('649','stop datetime','76','Data Type'),('65','patient preference','9','Data Type'),('651','negation rationale','77','Data Type'),('652','patient preference','77','Data Type'),('653','provider preference','77','Data Type'),('654','start datetime','77','Data Type'),('655','stop datetime','77','Data Type'),('66','provider preference','9','Data Type'),('662','Health Record Field','','Data Flow'),('663','laterality','7','Data Type'),('664','reason','13','Data Type'),('665','discharge status','22','Data Type'),('669','facility location arrival datetime','95','Data Type'),('67','severity','9','Data Type'),('670','facility location departure datetime','95','Data Type'),('671','facility location arrival datetime','22','Data Type'),('672','facility location departure datetime','22','Data Type'),('673','reason','39','Data Type'),('676','ordinality','62','Data Type'),('677','ordinality','63','Data Type'),('678','result','63','Data Type'),('679','incision datetime','63','Data Type'),('68','start datetime','9','Data Type'),('680','ordinality','88','Data Type'),('685','result','65','Data Type'),('689','related to','3','Data Type'),('70','stop datetime','9','Data Type'),('701','Source','','Data Flow'),('702','Recorder','','Data Flow'),('72','negation rationale','10','Data Type'),('73','ordinality','10','Data Type'),('74','patient preference','10','Data Type'),('75','provider preference','10','Data Type'),('76','severity','10','Data Type'),('77','start datetime','10','Data Type'),('79','stop datetime','10','Data Type'),('8','negation rationale','1','Data Type'),('81','negation rationale','11','Data Type'),('82','patient preference','11','Data Type'),('83','provider preference','11','Data Type'),('84','reaction','11','Data Type'),('85','start datetime','11','Data Type'),('86','stop datetime','11','Data Type'),('88','negation rationale','12','Data Type'),('89','patient preference','12','Data Type'),('9','patient preference','1','Data Type'),('90','provider preference','12','Data Type'),('91','reaction','12','Data Type'),('92','start datetime','12','Data Type'),('93','stop datetime','12','Data Type'),('95','negation rationale','13','Data Type'),('96','patient preference','13','Data Type'),('97','provider preference','13','Data Type'),('98','start datetime','13','Data Type'),('99','removal datetime','13','Data Type');
INSERT INTO `QDM_ATTRIBUTES_BACKUP_SEP2015` VALUES ('1001','facility location','19','Data Type'),('1010','start datetime','97','Data Type'),('1011','stop datetime','97','Data Type'),('1012','date','98','Data Type'),('1013','time','98','Data Type'),('1014','cause','98','Data Type'),('1015','start datetime','99','Data Type'),('1016','stop datetime','99','Data Type'),('1017','reason','99','Data Type'),('1018','start datetime','100','Data Type'),('1019','stop datetime','100','Data Type'),('1020','start datetime','101','Data Type'),('1021','stop datetime','101','Data Type'),('1023','dose','104','Data Type'),('1024','frequency','104','Data Type'),('1026','refills','104','Data Type'),('1027','route','104','Data Type'),('1028','start datetime','104','Data Type'),('1029','stop datetime','104','Data Type'),('1030','negation rationale','104','Data Type'),('1031','anatomical location site','7','Data Type'),('1032','anatomical location site','10','Data Type'),('1033','anatomical location site','9','Data Type'),('1034','anatomical location site','13','Data Type'),('1038','anatomical location site','56','Data Type'),('1039','anatomical location site','57','Data Type'),('104','reaction','14','Data Type'),('1040','anatomical location site','87','Data Type'),('1041','anatomical location site','62','Data Type'),('1042','anatomical location site','63','Data Type'),('1043','anatomical approach site','13','Data Type'),('105','start datetime','14','Data Type'),('1050','anatomical approach site','63','Data Type'),('1051','anatomical approach site','62','Data Type'),('1052','result','19','Data Type'),('1053','result','36','Data Type'),('1054','result','57','Data Type'),('1055','result','24','Data Type'),('1056','result','31','Data Type'),('1057','status','19','Data Type'),('1058','status','31','Data Type'),('1059','status','36','Data Type'),('106','stop datetime','14','Data Type'),('1062','cumulative medication duration','39','Data Type'),('1064','ordinality','61','Data Type'),('1065','radiation duration','62','Data Type'),('1066','status','63','Data Type'),('1067','radiation duration','63','Data Type'),('1068','radiation dosage','63','Data Type'),('1069','anatomical approach site','88','Data Type'),('1070','anatomical location site','88','Data Type'),('1071','active datetime','44','Data Type'),('1072','signed datetime','44','Data Type'),('1073','target outcome','3','Data Type'),('1074','relationship','105','Data Type'),('1075','onset age','105','Data Type'),('1076','recorded datetime','105','Data Type'),('1077','severity','106','Data Type'),('1078','onset datetime','106','Data Type'),('1079','abatement datetime','106','Data Type'),('108','negation rationale','15','Data Type'),('1080','reference range low','36','Data Type'),('1081','reference range high','36','Data Type'),('1083','dose','107','Data Type'),('1084','start datetime','107','Data Type'),('1085','route','107','Data Type'),('1086','reason','107','Data Type'),('1087','stop datetime','107','Data Type'),('1088','negation rationale','107','Data Type'),('1089','route','108','Data Type'),('1090','dose','108','Data Type'),('1091','start datetime','108','Data Type'),('1092','stop datetime','108','Data Type'),('1093','active datetime','108','Data Type'),('1094','signed datetime','108','Data Type'),('1095','negation rationale','108','Data Type'),('1096','reason','108','Data Type'),('1097','reaction','109','Data Type'),('1098','start datetime','109','Data Type'),('1099','stop datetime','109','Data Type'),('11','start datetime','1','Data Type'),('1100','reaction','110','Data Type'),('1101','start datetime','110','Data Type'),('1102','stop datetime','110','Data Type'),('1103','diagnosis','22','Data Type'),('1104','principal diagnosis','22','Data Type'),('1105','onset datetime','111','Data Type'),('1106','abatement datetime','111','Data Type'),('1107','anatomical location site','111','Data Type'),('1108','severity','111','Data Type'),('111','reason','15','Data Type'),('112','start datetime','15','Data Type'),('113','stop datetime','15','Data Type'),('115','negation rationale','78','Data Type'),('118','reason','78','Data Type'),('119','start datetime','78','Data Type'),('12','stop datetime','1','Data Type'),('120','stop datetime','78','Data Type'),('125','radiation dosage','16','Data Type'),('126','radiation duration','16','Data Type'),('127','reaction','16','Data Type'),('128','start datetime','16','Data Type'),('129','stop datetime','16','Data Type'),('134','radiation dosage','17','Data Type'),('135','radiation duration','17','Data Type'),('136','reaction','17','Data Type'),('137','start datetime','17','Data Type'),('138','stop datetime','17','Data Type'),('140','method','18','Data Type'),('141','negation rationale','18','Data Type'),('144','radiation dosage','18','Data Type'),('145','radiation duration','18','Data Type'),('146','reason','18','Data Type'),('147','start datetime','18','Data Type'),('148','stop datetime','18','Data Type'),('150','method','19','Data Type'),('151','negation rationale','19','Data Type'),('154','radiation dosage','19','Data Type'),('155','radiation duration','19','Data Type'),('156','reason','19','Data Type'),('157','start datetime','19','Data Type'),('158','stop datetime','19','Data Type'),('160','method','90','Data Type'),('161','negation rationale','90','Data Type'),('164','radiation dosage','90','Data Type'),('165','radiation duration','90','Data Type'),('166','start datetime','90','Data Type'),('167','stop datetime','90','Data Type'),('17','start datetime','2','Data Type'),('179','length of stay','95','Data Type'),('18','stop datetime','2','Data Type'),('181','facility location','95','Data Type'),('185','reason','95','Data Type'),('186','admission datetime','95','Data Type'),('187','discharge datetime','95','Data Type'),('198','facility location','21','Data Type'),('199','negation rationale','21','Data Type'),('202','reason','21','Data Type'),('203','start datetime','21','Data Type'),('204','stop datetime','21','Data Type'),('205','length of stay','22','Data Type'),('207','facility location','22','Data Type'),('208','negation rationale','22','Data Type'),('211','reason','22','Data Type'),('212','admission datetime','22','Data Type'),('213','discharge datetime','22','Data Type'),('215','facility location','79','Data Type'),('216','negation rationale','79','Data Type'),('219','reason','79','Data Type'),('220','start datetime','79','Data Type'),('221','stop datetime','79','Data Type'),('223','method','23','Data Type'),('224','negation rationale','23','Data Type'),('227','reason','23','Data Type'),('228','start datetime','23','Data Type'),('229','stop datetime','23','Data Type'),('23','start datetime','3','Data Type'),('231','method','24','Data Type'),('232','negation rationale','24','Data Type'),('235','reason','24','Data Type'),('236','start datetime','24','Data Type'),('237','stop datetime','24','Data Type'),('239','method','80','Data Type'),('24','stop datetime','3','Data Type'),('240','negation rationale','80','Data Type'),('243','reason','80','Data Type'),('244','start datetime','80','Data Type'),('245','stop datetime','80','Data Type'),('256','start datetime','26','Data Type'),('257','stop datetime','26','Data Type'),('26','negation rationale','5','Data Type'),('260','start datetime','27','Data Type'),('261','stop datetime','27','Data Type'),('266','reaction','28','Data Type'),('267','start datetime','28','Data Type'),('268','stop datetime','28','Data Type'),('273','reaction','29','Data Type'),('274','start datetime','29','Data Type'),('275','stop datetime','29','Data Type'),('278','negation rationale','30','Data Type'),('281','reason','30','Data Type'),('282','start datetime','30','Data Type'),('283','stop datetime','30','Data Type'),('286','negation rationale','31','Data Type'),('289','reason','31','Data Type'),('29','start datetime','5','Data Type'),('290','start datetime','31','Data Type'),('291','stop datetime','31','Data Type'),('294','negation rationale','81','Data Type'),('297','reason','81','Data Type'),('298','start datetime','81','Data Type'),('299','stop datetime','81','Data Type'),('30','stop datetime','5','Data Type'),('313','reaction','33','Data Type'),('314','start datetime','33','Data Type'),('315','stop datetime','33','Data Type'),('32','negation rationale','6','Data Type'),('320','reaction','34','Data Type'),('321','start datetime','34','Data Type'),('322','stop datetime','34','Data Type'),('324','method','35','Data Type'),('325','negation rationale','35','Data Type'),('328','reason','35','Data Type'),('329','start datetime','35','Data Type'),('330','stop datetime','35','Data Type'),('332','method','36','Data Type'),('333','negation rationale','36','Data Type'),('336','reason','36','Data Type'),('337','start datetime','36','Data Type'),('338','stop datetime','36','Data Type'),('340','method','82','Data Type'),('341','negation rationale','82','Data Type'),('344','reason','82','Data Type'),('345','start datetime','82','Data Type'),('346','stop datetime','82','Data Type'),('35','start datetime','6','Data Type'),('356','cumulative medication duration','38','Data Type'),('357','dose','38','Data Type'),('358','frequency','38','Data Type'),('36','stop datetime','6','Data Type'),('365','route','38','Data Type'),('366','start datetime','38','Data Type'),('367','stop datetime','38','Data Type'),('368','dose','39','Data Type'),('369','frequency','39','Data Type'),('371','negation rationale','39','Data Type'),('376','route','39','Data Type'),('377','start datetime','39','Data Type'),('378','stop datetime','39','Data Type'),('38','negation rationale','4','Data Type'),('386','reaction','40','Data Type'),('389','start datetime','40','Data Type'),('390','stop datetime','40','Data Type'),('398','reaction','41','Data Type'),('401','start datetime','41','Data Type'),('402','stop datetime','41','Data Type'),('403','cumulative medication duration','42','Data Type'),('404','dose','42','Data Type'),('405','frequency','42','Data Type'),('407','negation rationale','42','Data Type'),('41','start datetime','4','Data Type'),('411','refills','42','Data Type'),('412','route','42','Data Type'),('413','start datetime','42','Data Type'),('414','stop datetime','42','Data Type'),('42','stop datetime','4','Data Type'),('422','reaction','43','Data Type'),('425','start datetime','43','Data Type'),('426','stop datetime','43','Data Type'),('427','cumulative medication duration','44','Data Type'),('428','dose','44','Data Type'),('429','frequency','44','Data Type'),('431','method','44','Data Type'),('432','negation rationale','44','Data Type'),('436','reason','44','Data Type'),('437','refills','44','Data Type'),('438','route','44','Data Type'),('439','start datetime','44','Data Type'),('440','stop datetime','44','Data Type'),('45','ordinality','7','Data Type'),('453','method','56','Data Type'),('454','negation rationale','56','Data Type'),('457','reason','56','Data Type'),('458','start datetime','56','Data Type'),('459','stop datetime','56','Data Type'),('462','method','57','Data Type'),('463','negation rationale','57','Data Type'),('466','reason','57','Data Type'),('467','start datetime','57','Data Type'),('468','stop datetime','57','Data Type'),('471','method','87','Data Type'),('472','negation rationale','87','Data Type'),('475','reason','87','Data Type'),('476','start datetime','87','Data Type'),('477','stop datetime','87','Data Type'),('48','severity','7','Data Type'),('482','reaction','60','Data Type'),('483','start datetime','60','Data Type'),('484','stop datetime','60','Data Type'),('489','reaction','61','Data Type'),('49','start datetime','7','Data Type'),('490','start datetime','61','Data Type'),('491','stop datetime','61','Data Type'),('493','method','62','Data Type'),('494','negation rationale','62','Data Type'),('497','reason','62','Data Type'),('498','start datetime','62','Data Type'),('499','stop datetime','62','Data Type'),('501','method','63','Data Type'),('502','negation rationale','63','Data Type'),('505','reason','63','Data Type'),('506','start datetime','63','Data Type'),('507','stop datetime','63','Data Type'),('509','method','88','Data Type'),('510','negation rationale','88','Data Type'),('513','reason','88','Data Type'),('514','start datetime','88','Data Type'),('515','stop datetime','88','Data Type'),('52','stop datetime','7','Data Type'),('526','negation rationale','65','Data Type'),('529','start datetime','65','Data Type'),('530','stop datetime','65','Data Type'),('531','dose','66','Data Type'),('533','frequency','66','Data Type'),('534','negation rationale','66','Data Type'),('539','route','66','Data Type'),('540','start datetime','66','Data Type'),('541','stop datetime','66','Data Type'),('549','reaction','67','Data Type'),('552','start datetime','67','Data Type'),('553','stop datetime','67','Data Type'),('561','reaction','68','Data Type'),('564','start datetime','68','Data Type'),('565','stop datetime','68','Data Type'),('573','reaction','69','Data Type'),('576','start datetime','69','Data Type'),('577','stop datetime','69','Data Type'),('578','dose','70','Data Type'),('580','frequency','70','Data Type'),('581','method','70','Data Type'),('582','negation rationale','70','Data Type'),('586','reason','70','Data Type'),('587','refills','70','Data Type'),('588','route','70','Data Type'),('589','start datetime','70','Data Type'),('590','stop datetime','70','Data Type'),('591','dose','89','Data Type'),('593','frequency','89','Data Type'),('594','method','89','Data Type'),('595','negation rationale','89','Data Type'),('599','reason','89','Data Type'),('600','refills','89','Data Type'),('601','route','89','Data Type'),('602','start datetime','89','Data Type'),('603','stop datetime','89','Data Type'),('64','ordinality','9','Data Type'),('641','negation rationale','75','Data Type'),('642','start datetime','75','Data Type'),('643','stop datetime','75','Data Type'),('645','negation rationale','76','Data Type'),('648','start datetime','76','Data Type'),('649','stop datetime','76','Data Type'),('651','negation rationale','77','Data Type'),('654','start datetime','77','Data Type'),('655','stop datetime','77','Data Type'),('662','Health Record Field','','Data Flow'),('663','laterality','7','Data Type'),('664','reason','13','Data Type'),('665','discharge status','22','Data Type'),('669','facility location arrival datetime','95','Data Type'),('67','severity','9','Data Type'),('670','facility location departure datetime','95','Data Type'),('671','facility location arrival datetime','22','Data Type'),('672','facility location departure datetime','22','Data Type'),('673','reason','39','Data Type'),('676','ordinality','62','Data Type'),('677','ordinality','63','Data Type'),('678','result','63','Data Type'),('679','incision datetime','63','Data Type'),('68','start datetime','9','Data Type'),('680','ordinality','88','Data Type'),('685','result','65','Data Type'),('689','related to','3','Data Type'),('70','stop datetime','9','Data Type'),('701','Source','','Data Flow'),('702','Recorder','','Data Flow'),('73','ordinality','10','Data Type'),('76','severity','10','Data Type'),('77','start datetime','10','Data Type'),('79','stop datetime','10','Data Type'),('84','reaction','11','Data Type'),('85','start datetime','11','Data Type'),('86','stop datetime','11','Data Type'),('91','reaction','12','Data Type'),('92','start datetime','12','Data Type'),('93','stop datetime','12','Data Type'),('95','negation rationale','13','Data Type'),('98','start datetime','13','Data Type'),('99','removal datetime','13','Data Type');
INSERT INTO `SECURITY_QUESTIONS` VALUES (1,'What was your dream job as a child?'),(2,'What is your preferred musical genre?'),(3,'What is the name of your favorite childhood friend?'),(4,'What was the make of your first car?'),(5,'In what city or town was your first job?'),(6,'What was the name of your elementary / primary school?'),(7,'What school did you attend for sixth grade?'),(8,'What was the first sport you ever played as a child?');
INSERT INTO `SHARE_LEVEL` VALUES ('1','View Only'),('2','Modify');
INSERT INTO `STATUS` VALUES ('1','Active'),('2','Revoked');
INSERT INTO `STEWARD_ORG` VALUES ('14','American Medical Association-convened Physician Consortium for Performance Improvement(R) (AMA-PCPI)','2.16.840.1.113883.3.526'),('15','Centers for Medicare & Medicaid Services','2.16.840.1.113883.3.560.3.31'),('29','Cleveland Clinic','2.16.840.1.114222.4.1.213632'),('55','National Committee for Quality Assurance','2.16.840.1.113883.3.464'),('80','National Quality Forum','2.16.840.1.113883.3.560'),('81','Joint Commission','1.3.6.1.4.1.33895'),('82','Oklahoma Foundation for Medical Quality','2.16.840.1.113883'),('83','American Board of Internal Medicine','2.16.840.1.113883.3.797'),('84','Kaiser Permanente','1.3.6.1.4.1.26580'),('85','Other',NULL),('86','National Library of Medicine','2.16.840.1.113883.1.11.1'),('87','CDC NCHS','2.16.840.1.114222.4.11.836'),('88','PHDSC','2.16.840.1.113883.221.5');
INSERT INTO `TRANSACTION_AUDIT_LOG` VALUES ('8ae455fa4de40f05014de41120f00001',NULL,NULL,'ADMIN_ACCT_TAB_EVENT','Admin','2015-06-11 19:20:37','[Admin] adminAccountTab0'),('8ae455fa4de40f05014de41120f00002',NULL,NULL,'LOGIN_EVENT','Admin','2015-06-11 19:20:37','[Admin] null'),('8ae455fa4de40f05014de41121320003',NULL,NULL,'MAIN_TAB_EVENT','Admin','2015-06-11 19:20:37','[Admin] mainTab0'),('8ae455fa4de40f05014de41135260004',NULL,NULL,'SIGN_OUT_EVENT','Admin','2015-06-11 19:20:42','[Admin]'),('8ae455fa4de40f05014de41162d20005',NULL,NULL,'LOGIN_EVENT','Admin','2015-06-11 19:20:54','[Admin] null'),('8ae455fa4de40f05014de41162df0006',NULL,NULL,'ADMIN_ACCT_TAB_EVENT','Admin','2015-06-11 19:20:54','[Admin] adminAccountTab0'),('8ae455fa4de40f05014de41162f40007',NULL,NULL,'MAIN_TAB_EVENT','Admin','2015-06-11 19:20:54','[Admin] mainTab0'),('8ae455fa4de40f05014de4116d020008',NULL,NULL,'SIGN_OUT_EVENT','Admin','2015-06-11 19:20:56','[Admin]');
INSERT INTO `UNIT` VALUES ('1','seconds',4,'seconds'),('10','bpm',19,'{beats}/min'),('11','cm',20,'cm'),('12','dL',22,'dL'),('13','eq',23,'eq'),('14','g',24,'g'),('15','kg',25,'kg'),('16','Liter',26,'L'),('17','mEq',27,'meq'),('18','mg',28,'mg'),('19','mg/dL',29,'mg/dL'),('2','minutes',6,'minutes'),('20','mL',30,'mL'),('21','mm',31,'mm'),('22','mmHg',32,'mm[Hg]'),('23','mmol/L',33,'mmol/L'),('24','ng/dL',34,'ng/dL'),('25','kg/m2',36,'kg/m2'),('26','RAD',38,'RAD'),('27','per mm3',37,'/mm3'),('28','copies/mL',21,'{copies}/mL'),('29','ng/mL',35,'ng/mL'),('3','hours',8,'hours'),('30','IU',39,'[iU]'),('31','IU/L',40,'[iU]/L'),('32','U/L',41,'U/L'),('33','AU',42,'[AU]'),('34','BAU',43,'[BAU]'),('35','millisecond',1,'millisecond'),('36','milliseconds',2,'milliseconds'),('37','second',3,'second'),('38','minute',5,'minute'),('39','hour',7,'hour'),('4','days',10,'days'),('40','day',9,'day'),('41','week',11,'week'),('42','month',13,'month'),('43','year',15,'year'),('5','weeks',12,'weeks'),('6','months',14,'months'),('7','years',16,'years'),('8','%',17,'%'),('9','celsius',18,'Cel');
INSERT INTO `UNIT_TYPE` VALUES ('1','Function'),('2','Comparison'),('3','TemporalComparison'),('4','Attribute');
INSERT INTO `UNIT_TYPE_MATRIX` VALUES ('1','1','1'),('10','10','1'),('11','11','1'),('12','12','1'),('13','13','1'),('14','14','1'),('15','15','1'),('16','16','1'),('17','17','1'),('18','18','1'),('19','19','1'),('2','2','1'),('20','20','1'),('21','21','1'),('22','22','1'),('23','23','1'),('24','24','1'),('25','1','2'),('26','2','2'),('27','3','2'),('28','4','2'),('29','5','2'),('3','3','1'),('30','6','2'),('31','7','2'),('32','1','3'),('33','2','3'),('34','3','3'),('35','4','3'),('36','5','3'),('37','6','3'),('38','7','3'),('39','1','4'),('4','4','1'),('40','2','4'),('41','3','4'),('42','4','4'),('43','5','4'),('44','6','4'),('45','7','4'),('46','8','4'),('47','9','4'),('48','10','4'),('49','11','4'),('5','5','1'),('50','12','4'),('51','13','4'),('52','14','4'),('53','15','4'),('54','16','4'),('55','17','4'),('56','18','4'),('57','19','4'),('58','20','4'),('59','21','4'),('6','6','1'),('60','22','4'),('61','23','4'),('62','24','4'),('63','25','1'),('64','26','1'),('65','25','4'),('66','26','4'),('67','27','1'),('68','27','4'),('69','28','1'),('7','7','1'),('70','28','4'),('71','29','1'),('72','29','4'),('73','30','1'),('74','30','4'),('75','31','1'),('76','31','4'),('77','32','1'),('78','32','4'),('79','33','1'),('8','8','1'),('80','33','4'),('81','34','1'),('82','34','4'),('83','35','1'),('84','36','1'),('85','37','1'),('86','38','1'),('87','39','1'),('88','40','1'),('89','41','1'),('9','9','1'),('90','42','1'),('91','43','1');
INSERT INTO `USER_SECURITY_QUESTIONS` VALUES (1,'2c9280827233d21d01723c6cef310091',0,'child',1,NULL),(2,'2c9280827233d21d01723c6cef310091',1,'genre',2,NULL),(3,'2c9280827233d21d01723c6cef310091',2,'friend',3,NULL);
INSERT INTO `ORGANIZATION` VALUES (1,'Telligen','2.16.840.1.113883.3.67');
SET FOREIGN_KEY_CHECKS=1;