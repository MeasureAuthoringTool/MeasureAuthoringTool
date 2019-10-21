CREATE DATABASE  IF NOT EXISTS `MAT_APP_BLANK` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `MAT_APP_BLANK`;
-- MySQL dump 10.13  Distrib 8.0.17, for macos10.14 (x86_64)
--
-- Host: 127.0.0.1    Database: MAT_APP_BLANK
-- ------------------------------------------------------
-- Server version	5.7.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ATTRIBUTES`
--

DROP TABLE IF EXISTS `ATTRIBUTES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ATTRIBUTES` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ATTRIBUTE_NAME` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ATTRIBUTES`
--

LOCK TABLES `ATTRIBUTES` WRITE;
/*!40000 ALTER TABLE `ATTRIBUTES` DISABLE KEYS */;
INSERT INTO `ATTRIBUTES` VALUES (1,'activeDatetime'),(2,'admissionSource'),(3,'anatomicalApproachSite'),(4,'anatomicalLocationSite'),(5,'authorDatetime'),(6,'cause'),(7,'code'),(8,'birthDatetime'),(9,'expiredDatetime'),(10,'diagnoses'),(11,'dischargeDisposition'),(12,'dosage'),(13,'supply'),(14,'facilityLocation'),(15,'frequency'),(16,'incisionDatetime'),(17,'lengthOfStay'),(19,'method'),(20,'negationRationale'),(21,'ordinality'),(22,'prevalencePeriod'),(23,'principalDiagnosis'),(26,'reason'),(27,'referenceRange'),(28,'refills'),(29,'relatedTo'),(30,'relationship'),(31,'relevantPeriod'),(32,'result'),(33,'resultDatetime'),(34,'route'),(35,'severity'),(36,'status'),(37,'targetOutcome'),(38,'type'),(39,'id'),(40,'recorder'),(41,'reporter'),(42,'components'),(43,'participationPeriod'),(44,'facilityLocations');
/*!40000 ALTER TABLE `ATTRIBUTES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ATTRIBUTES_MODES`
--

DROP TABLE IF EXISTS `ATTRIBUTES_MODES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ATTRIBUTES_MODES` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ATTRIBUTE_ID` varchar(32) NOT NULL,
  `MODE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `MODE_FK` (`MODE_ID`),
  CONSTRAINT `MODE_FK` FOREIGN KEY (`MODE_ID`) REFERENCES `MODES` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ATTRIBUTES_MODES`
--

LOCK TABLES `ATTRIBUTES_MODES` WRITE;
/*!40000 ALTER TABLE `ATTRIBUTES_MODES` DISABLE KEYS */;
INSERT INTO `ATTRIBUTES_MODES` VALUES (1,'1',1),(2,'1',2),(3,'1',3),(4,'2',3),(5,'2',4),(6,'3',3),(7,'3',4),(8,'4',3),(9,'4',4),(10,'5',1),(11,'5',2),(12,'5',3),(13,'6',3),(14,'6',4),(15,'7',3),(16,'7',4),(17,'8',1),(18,'8',2),(19,'8',3),(20,'9',1),(21,'9',2),(22,'9',3),(23,'10',3),(24,'10',4),(25,'11',3),(26,'11',4),(27,'12',1),(28,'12',3),(29,'13',1),(30,'13',3),(31,'14',3),(32,'14',4),(33,'15',3),(34,'15',4),(35,'16',1),(36,'16',2),(37,'16',3),(38,'17',1),(39,'17',3),(40,'18',3),(41,'19',3),(42,'19',4),(43,'20',3),(44,'20',4),(45,'21',3),(46,'21',4),(47,'22',3),(48,'23',3),(49,'23',4),(50,'24',1),(51,'24',3),(52,'25',1),(53,'25',3),(54,'26',3),(55,'26',4),(56,'27',3),(57,'28',1),(58,'28',3),(59,'29',3),(60,'29',4),(61,'30',3),(62,'30',4),(63,'31',3),(64,'32',1),(65,'32',3),(66,'32',4),(67,'33',1),(68,'33',2),(69,'33',3),(70,'34',3),(71,'34',4),(72,'35',3),(73,'35',4),(74,'36',3),(75,'36',4),(76,'37',1),(77,'37',3),(78,'37',4),(79,'38',3),(80,'38',4),(81,'39',4),(82,'40',4),(83,'41',4);
/*!40000 ALTER TABLE `ATTRIBUTES_MODES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ATTRIBUTE_DETAILS`
--

DROP TABLE IF EXISTS `ATTRIBUTE_DETAILS`;
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
-- Dumping data for table `ATTRIBUTE_DETAILS`
--

LOCK TABLES `ATTRIBUTE_DETAILS` WRITE;
/*!40000 ALTER TABLE `ATTRIBUTE_DETAILS` DISABLE KEYS */;
INSERT INTO `ATTRIBUTE_DETAILS` VALUES ('1','anatomical structure','91723000','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('10','negation rationale','','','','N',''),('11','number','107651007','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('12','ordinality','117363000','2.16.840.1.113883.6.96','SNOMED-CT','S','SUBJ'),('13','patient preference','PAT','2.16.840.1.113883.5.8','HL7 Act Accomodation Reason','S','RSON'),('14','provider preference','103323008','2.16.840.1.113883.6.96','SNOMED-CT','S','RSON'),('15','radiation dosage','228815006','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('16','radiationduration','218190002','2.16.840.1.113883.6.96','SNOMED-CT','S(I)/S(I)','REFR'),('17','reaction','263851003','2.16.840.1.113883.6.96','SNOMED-CT','S','MFST'),('18','reason','410666004','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('19','refills','18623-9','2.16.840.1.113883.6.1','LOINC','S','REFR'),('2','cumulative medication duration','363819003','2.16.840.1.113883.6.96','SNOMED-CT','S','SUBJ'),('20','result','385676005','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('21','route','263513008','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('22','severity','SEV','2.16.840.1.113883.5.4','HL7 Act Code','S','SUBJ'),('23','start datetime','398201009','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('24','status','263490005','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('25','stop datetime','397898000','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('3','dose','398232005','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('31','Health Record Field','','','','AC',''),('32','admission datetime','399423000','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('33','discharge datetime','442864001','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('34','environment','285202004','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('35','date','410672004','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('36','discharge status','309039003','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('37','incision datetime','34896006','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('38','laterality','182353008','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('39','laterality','182353008','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('4','length of stay','183797002','2.16.840.1.113883.6.96','SNOMED-CT','S','SUBJ'),('40','time','410669006','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('41','removal datetime','118292001','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('42','facility location','','2.16.840.1.113883.3.560.101.1','','E','SUBJ'),('43','facility location arrival datetime','','2.16.840.1.113883.3.560.101.1','','E','SUBJ'),('44','facility location departure datetime','','2.16.840.1.113883.3.560.101.1','','E','SUBJ'),('46','radiation duration','306751006','2.16.840.1.113883.6.96','SNOMED-CT','S','SUBJ'),('47','related to','REL','2.16.840.1.113883.1.11.11603','HL7 Role Link Type','S','REFR'),('48','recorder','419358007','2.16.840.1.113883.6.96','SNOMED-CT','S','SUBJ'),('49','source','260753009','2.16.840.1.113883.6.96','SNOMED-CT','S','SUBJ'),('6','frequency','260864003','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('7','hospital location','','','','P','REFR'),('9','method','414679000','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR');
/*!40000 ALTER TABLE `ATTRIBUTE_DETAILS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AUDIT_LOG`
--

DROP TABLE IF EXISTS `AUDIT_LOG`;
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
-- Dumping data for table `AUDIT_LOG`
--

LOCK TABLES `AUDIT_LOG` WRITE;
/*!40000 ALTER TABLE `AUDIT_LOG` DISABLE KEYS */;
INSERT INTO `AUDIT_LOG` VALUES ('1','2010-10-28 20:08:27','Admin',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `AUDIT_LOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AUTHOR`
--

DROP TABLE IF EXISTS `AUTHOR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `AUTHOR` (
  `ID` varchar(32) NOT NULL,
  `AUTHOR_NAME` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AUTHOR`
--

LOCK TABLES `AUTHOR` WRITE;
/*!40000 ALTER TABLE `AUTHOR` DISABLE KEYS */;
INSERT INTO `AUTHOR` VALUES ('14','American Medical Association-convened Physician Consortium for Performance Improvement(R) (AMA-PCPI)'),('15','Centers for Medicare & Medicaid Services'),('29','Cleveland Clinic'),('55','National Committee for Quality Assurance'),('80','Other'),('81','National Quality Forum'),('82','Joint Commission'),('83','Oklahoma Foundation for Medical Quality'),('84','American Board of Internal Medicine'),('85','Kaiser Permanente');
/*!40000 ALTER TABLE `AUTHOR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CATEGORY`
--

DROP TABLE IF EXISTS `CATEGORY`;
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
-- Dumping data for table `CATEGORY`
--

LOCK TABLES `CATEGORY` WRITE;
/*!40000 ALTER TABLE `CATEGORY` DISABLE KEYS */;
INSERT INTO `CATEGORY` VALUES ('1','Care Experience','EXP'),('10','Intervention','INT'),('11','Laboratory Test','LAB'),('12','Medication','MED'),('14','Physical Exam','PE'),('16','Procedure','PRC'),('17','Risk Category/Assessment','RSK'),('18','Substance','SUB'),('19','Symptom','SX'),('2','Care Goal','GOL'),('20','System Characteristic','SYS'),('21','Transfer of Care','TRN'),('22','Measure Timing','TMG'),('23','Attribute','ATT'),('24','Immunization','IMM'),('25','Assessment','ASM'),('26','Adverse Event','ADV'),('27','Allergy/Intolerance','AGY'),('28','Participation','PAR'),('29','Related Person','RP'),('3','Communication','COM'),('4','Condition/Diagnosis/Problem','CDP'),('5','Device','DEV'),('6','Diagnostic Study','DXS'),('7','Encounter','ENC'),('8','Functional Status','FXS'),('9','Individual Characteristic','IND');
/*!40000 ALTER TABLE `CATEGORY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CATEGORY_BACKUP_AUG2015`
--

DROP TABLE IF EXISTS `CATEGORY_BACKUP_AUG2015`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CATEGORY_BACKUP_AUG2015` (
  `CATEGORY_ID` varchar(32) NOT NULL,
  `DESCRIPTION` varchar(50) NOT NULL,
  `ABBREVIATION` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CATEGORY_BACKUP_AUG2015`
--

LOCK TABLES `CATEGORY_BACKUP_AUG2015` WRITE;
/*!40000 ALTER TABLE `CATEGORY_BACKUP_AUG2015` DISABLE KEYS */;
INSERT INTO `CATEGORY_BACKUP_AUG2015` VALUES ('1','Care Experience','EXP'),('10','Intervention','INT'),('11','Laboratory Test','LAB'),('12','Medication','MED'),('14','Physical Exam','PE'),('16','Procedure','PRC'),('17','Risk Category/Assessment','RSK'),('18','Substance','SUB'),('19','Symptom','SX'),('2','Care Goal','GOL'),('20','System Characteristic','SYS'),('21','Transfer of Care','TRN'),('22','Measure Timing','TMG'),('23','Attribute','ATT'),('3','Communication','COM'),('4','Condition/Diagnosis/Problem','CDP'),('5','Device','DEV'),('6','Diagnostic Study','DXS'),('7','Encounter','ENC'),('8','Functional Status','FXS'),('9','Individual Characteristic','IND');
/*!40000 ALTER TABLE `CATEGORY_BACKUP_AUG2015` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLAUSE`
--

DROP TABLE IF EXISTS `CLAUSE`;
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
-- Dumping data for table `CLAUSE`
--

LOCK TABLES `CLAUSE` WRITE;
/*!40000 ALTER TABLE `CLAUSE` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLAUSE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CODE`
--

DROP TABLE IF EXISTS `CODE`;
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
-- Dumping data for table `CODE`
--

LOCK TABLES `CODE` WRITE;
/*!40000 ALTER TABLE `CODE` DISABLE KEYS */;
INSERT INTO `CODE` VALUES ('8a4d8f813254dd37013255ae43a606ef','21112-8','Birth date','8ae452962e3a223a012e3a254b808892'),('8a4d8f813254dd37013255ae43a607ef','M','Male','8ae452962e3a223a012e3a254b808889'),('8a4d8f813254dd37013255ae43a608ef','F','Female','8ae452962e3a223a012e3a254b808890'),('8a4d8f813254dd37013255ae43a609ef','U','Unknown','8ae452962e3a223a012e3a254b808891'),('a31eed09-106e-11e5-b6bc-005056970072','F','Female','bae50f18267111e1a17a78acc0b65c43'),('a31ef26a-106e-11e5-b6bc-005056970072','M','Male','bae50f18267111e1a17a78acc0b65c43'),('a31ef526-106e-11e5-b6bc-005056970072','UN','Undifferentiated','bae50f18267111e1a17a78acc0b65c43'),('a31ef7a3-106e-11e5-b6bc-005056970072','1002-5','American Indian or Alaska Native','bae85d87267111e1a17a78acc0b65c43'),('a31efa30-106e-11e5-b6bc-005056970072','2028-9','Asian','bae85d87267111e1a17a78acc0b65c43'),('a31efdda-106e-11e5-b6bc-005056970072','2054-5','Black or African American','bae85d87267111e1a17a78acc0b65c43'),('a31f0055-106e-11e5-b6bc-005056970072','2076-8','Native Hawaiian or Other Pacific Islander','bae85d87267111e1a17a78acc0b65c43'),('a31f02c8-106e-11e5-b6bc-005056970072','2106-3','White','bae85d87267111e1a17a78acc0b65c43'),('a31f053f-106e-11e5-b6bc-005056970072','2131-1','Other Race','bae85d87267111e1a17a78acc0b65c43'),('a31f07ad-106e-11e5-b6bc-005056970072','2135-2','Hispanic or Latino','bae86046267111e1a17a78acc0b65c43'),('a31f0a1b-106e-11e5-b6bc-005056970072','2186-5','Not Hispanic or Latino','bae86046267111e1a17a78acc0b65c43'),('a31f0c88-106e-11e5-b6bc-005056970072','1','MEDICARE','bae86261267111e1a17a78acc0b65c43'),('a31f0efb-106e-11e5-b6bc-005056970072','11','Medicare (Managed Care)','bae86261267111e1a17a78acc0b65c43'),('a31f116f-106e-11e5-b6bc-005056970072','111','Medicare HMO','bae86261267111e1a17a78acc0b65c43'),('a31f13da-106e-11e5-b6bc-005056970072','112','Medicare PPO','bae86261267111e1a17a78acc0b65c43'),('a31f162f-106e-11e5-b6bc-005056970072','113','Medicare POS','bae86261267111e1a17a78acc0b65c43'),('a31f189b-106e-11e5-b6bc-005056970072','119','Medicare Managed Care Other','bae86261267111e1a17a78acc0b65c43'),('a31f1b09-106e-11e5-b6bc-005056970072','12','Medicare (Non-managed Care)','bae86261267111e1a17a78acc0b65c43'),('a31f1d69-106e-11e5-b6bc-005056970072','121','Medicare FFS','bae86261267111e1a17a78acc0b65c43'),('a31f2118-106e-11e5-b6bc-005056970072','122','Drug Benefit','bae86261267111e1a17a78acc0b65c43'),('a31f9a5b-106e-11e5-b6bc-005056970072','123','Medicare Medical Savings Account (MSA)','bae86261267111e1a17a78acc0b65c43'),('a31fa0c7-106e-11e5-b6bc-005056970072','129','Medicare Non-managed Care Other','bae86261267111e1a17a78acc0b65c43'),('a31fa368-106e-11e5-b6bc-005056970072','19','Medicare Other','bae86261267111e1a17a78acc0b65c43'),('a31fa733-106e-11e5-b6bc-005056970072','2','MEDICAID','bae86261267111e1a17a78acc0b65c43'),('a31fa9de-106e-11e5-b6bc-005056970072','21','Medicaid (Managed Care)','bae86261267111e1a17a78acc0b65c43'),('a31fac51-106e-11e5-b6bc-005056970072','211','Medicaid HMO','bae86261267111e1a17a78acc0b65c43'),('a31fb017-106e-11e5-b6bc-005056970072','212','Medicaid PPO','bae86261267111e1a17a78acc0b65c43'),('a31fb2bf-106e-11e5-b6bc-005056970072','213','Medicaid PCCM (Primary Care Case Management)','bae86261267111e1a17a78acc0b65c43'),('a31fb5c3-106e-11e5-b6bc-005056970072','219','Medicaid Managed Care Other','bae86261267111e1a17a78acc0b65c43'),('a31fba0e-106e-11e5-b6bc-005056970072','22','Medicaid (Non-managed Care Plan)','bae86261267111e1a17a78acc0b65c43'),('a31fbcc3-106e-11e5-b6bc-005056970072','23','Medicaid/SCHIP','bae86261267111e1a17a78acc0b65c43'),('a31fc04d-106e-11e5-b6bc-005056970072','24','Medicaid Applicant','bae86261267111e1a17a78acc0b65c43'),('a31fc2de-106e-11e5-b6bc-005056970072','25','Medicaid - Out of State','bae86261267111e1a17a78acc0b65c43'),('a31fc6e7-106e-11e5-b6bc-005056970072','29','Medicaid Other','bae86261267111e1a17a78acc0b65c43'),('a31fcba7-106e-11e5-b6bc-005056970072','3','OTHER GOVERNMENT (Federal/State/Local) (excluding Department of Corrections)','bae86261267111e1a17a78acc0b65c43'),('a31fce3d-106e-11e5-b6bc-005056970072','31','Department of Defense','bae86261267111e1a17a78acc0b65c43'),('a31fd0ab-106e-11e5-b6bc-005056970072','311','TRICARE (CHAMPUS)','bae86261267111e1a17a78acc0b65c43'),('a31fd315-106e-11e5-b6bc-005056970072','3111','TRICARE  Prime--HMO','bae86261267111e1a17a78acc0b65c43'),('a31fd585-106e-11e5-b6bc-005056970072','3112','TRICARE  Extra--PPO','bae86261267111e1a17a78acc0b65c43'),('a31fd974-106e-11e5-b6bc-005056970072','3113','TRICARE Standard - Fee For Service','bae86261267111e1a17a78acc0b65c43'),('a31fdc06-106e-11e5-b6bc-005056970072','3114','TRICARE For Life--Medicare Supplement','bae86261267111e1a17a78acc0b65c43'),('a31fde6a-106e-11e5-b6bc-005056970072','3115','TRICARE Reserve Select','bae86261267111e1a17a78acc0b65c43'),('a31fe0e7-106e-11e5-b6bc-005056970072','3116','Uniformed Services Family Health Plan (USFHP) -- HMO','bae86261267111e1a17a78acc0b65c43'),('a31fe368-106e-11e5-b6bc-005056970072','3119','Department of Defense -  (other)','bae86261267111e1a17a78acc0b65c43'),('a31fe6e1-106e-11e5-b6bc-005056970072','312','Military Treatment Facility','bae86261267111e1a17a78acc0b65c43'),('a31fe97f-106e-11e5-b6bc-005056970072','3121','Enrolled Prime--HMO','bae86261267111e1a17a78acc0b65c43'),('a31fec72-106e-11e5-b6bc-005056970072','3122','Non-enrolled Space Available','bae86261267111e1a17a78acc0b65c43'),('a31fef84-106e-11e5-b6bc-005056970072','3123','TRICARE For Life (TFL)','bae86261267111e1a17a78acc0b65c43'),('a31ff20c-106e-11e5-b6bc-005056970072','313','Dental --Stand Alone','bae86261267111e1a17a78acc0b65c43'),('a31ff47c-106e-11e5-b6bc-005056970072','32','Department of Veterans Affairs','bae86261267111e1a17a78acc0b65c43'),('a31ff6e0-106e-11e5-b6bc-005056970072','321','Veteran care--Care provided to Veterans','bae86261267111e1a17a78acc0b65c43'),('a31ffa75-106e-11e5-b6bc-005056970072','3211','Direct Care--Care provided in VA facilities','bae86261267111e1a17a78acc0b65c43'),('a31ffd18-106e-11e5-b6bc-005056970072','3212','Indirect Care--Care provided outside VA facilities','bae86261267111e1a17a78acc0b65c43'),('a31fff92-106e-11e5-b6bc-005056970072','32121','Fee Basis','bae86261267111e1a17a78acc0b65c43'),('a32001fb-106e-11e5-b6bc-005056970072','32122','Foreign Fee/Foreign Medical Program(FMP)','bae86261267111e1a17a78acc0b65c43'),('a320047a-106e-11e5-b6bc-005056970072','32123','Contract Nursing Home/Community Nursing Home','bae86261267111e1a17a78acc0b65c43'),('a32006f0-106e-11e5-b6bc-005056970072','32124','State Veterans Home','bae86261267111e1a17a78acc0b65c43'),('a3200961-106e-11e5-b6bc-005056970072','32125','Sharing Agreements','bae86261267111e1a17a78acc0b65c43'),('a32082d6-106e-11e5-b6bc-005056970072','32126','Other Federal Agency','bae86261267111e1a17a78acc0b65c43'),('a32086ea-106e-11e5-b6bc-005056970072','322','Non-veteran care','bae86261267111e1a17a78acc0b65c43'),('a32089ee-106e-11e5-b6bc-005056970072','3221','Civilian Health and Medical Program for the VA (CHAMPVA)','bae86261267111e1a17a78acc0b65c43'),('a3208f20-106e-11e5-b6bc-005056970072','3222','Spina Bifida Health Care Program (SB)','bae86261267111e1a17a78acc0b65c43'),('a32091d2-106e-11e5-b6bc-005056970072','3223','Children of Women Vietnam Veterans (CWVV)','bae86261267111e1a17a78acc0b65c43'),('a3209451-106e-11e5-b6bc-005056970072','3229','Other non-veteran care','bae86261267111e1a17a78acc0b65c43'),('a32096cb-106e-11e5-b6bc-005056970072','33','Indian Health Service or Tribe','bae86261267111e1a17a78acc0b65c43'),('a3209947-106e-11e5-b6bc-005056970072','331','Indian Health Service - Regular','bae86261267111e1a17a78acc0b65c43'),('a3209c2c-106e-11e5-b6bc-005056970072','332','Indian Health Service - Contract','bae86261267111e1a17a78acc0b65c43'),('a3209f53-106e-11e5-b6bc-005056970072','333','Indian Health Service - Managed Care','bae86261267111e1a17a78acc0b65c43'),('a320a260-106e-11e5-b6bc-005056970072','334','Indian Tribe - Sponsored Coverage','bae86261267111e1a17a78acc0b65c43'),('a320a4ec-106e-11e5-b6bc-005056970072','34','HRSA Program','bae86261267111e1a17a78acc0b65c43'),('a320a75c-106e-11e5-b6bc-005056970072','341','Title V (MCH Block Grant)','bae86261267111e1a17a78acc0b65c43'),('a320aad0-106e-11e5-b6bc-005056970072','342','Migrant Health Program','bae86261267111e1a17a78acc0b65c43'),('a320ad68-106e-11e5-b6bc-005056970072','343','Ryan White Act','bae86261267111e1a17a78acc0b65c43'),('a320b042-106e-11e5-b6bc-005056970072','349','Other','bae86261267111e1a17a78acc0b65c43'),('a320b2c1-106e-11e5-b6bc-005056970072','35','Black Lung','bae86261267111e1a17a78acc0b65c43'),('a320b521-106e-11e5-b6bc-005056970072','36','State Government','bae86261267111e1a17a78acc0b65c43'),('a320b78d-106e-11e5-b6bc-005056970072','361','State SCHIP program (codes for individual states)','bae86261267111e1a17a78acc0b65c43'),('a320ba00-106e-11e5-b6bc-005056970072','362','Specific state programs (list/ local code)','bae86261267111e1a17a78acc0b65c43'),('a320bc79-106e-11e5-b6bc-005056970072','369','State, not otherwise specified (other state)','bae86261267111e1a17a78acc0b65c43'),('a320bee8-106e-11e5-b6bc-005056970072','37','Local Government','bae86261267111e1a17a78acc0b65c43'),('a320c14f-106e-11e5-b6bc-005056970072','371','Local - Managed care','bae86261267111e1a17a78acc0b65c43'),('a320c422-106e-11e5-b6bc-005056970072','3711','HMO','bae86261267111e1a17a78acc0b65c43'),('a320c6ac-106e-11e5-b6bc-005056970072','3712','PPO','bae86261267111e1a17a78acc0b65c43'),('a320c926-106e-11e5-b6bc-005056970072','3713','POS','bae86261267111e1a17a78acc0b65c43'),('a320cebd-106e-11e5-b6bc-005056970072','372','FFS/Indemnity','bae86261267111e1a17a78acc0b65c43'),('a320d41b-106e-11e5-b6bc-005056970072','379','Local, not otherwise specified (other local, county)','bae86261267111e1a17a78acc0b65c43'),('a320d734-106e-11e5-b6bc-005056970072','38','Other Government (Federal, State, Local not specified)','bae86261267111e1a17a78acc0b65c43'),('a320d9ba-106e-11e5-b6bc-005056970072','381','Federal, State, Local not specified managed care','bae86261267111e1a17a78acc0b65c43'),('a320dcac-106e-11e5-b6bc-005056970072','3811','Federal, State, Local not specified - HMO','bae86261267111e1a17a78acc0b65c43'),('a320df90-106e-11e5-b6bc-005056970072','3812','Federal, State, Local not specified - PPO','bae86261267111e1a17a78acc0b65c43'),('a320e20f-106e-11e5-b6bc-005056970072','3813','Federal, State, Local not specified - POS','bae86261267111e1a17a78acc0b65c43'),('a320e4e5-106e-11e5-b6bc-005056970072','3819','Federal, State, Local not specified - not specified managed care','bae86261267111e1a17a78acc0b65c43'),('a320e772-106e-11e5-b6bc-005056970072','382','Federal, State, Local not specified - FFS','bae86261267111e1a17a78acc0b65c43'),('a320e9ea-106e-11e5-b6bc-005056970072','389','Federal, State, Local not specified - Other','bae86261267111e1a17a78acc0b65c43'),('a320ec5f-106e-11e5-b6bc-005056970072','39','Other Federal','bae86261267111e1a17a78acc0b65c43'),('a320eec5-106e-11e5-b6bc-005056970072','4','DEPARTMENTS OF CORRECTIONS','bae86261267111e1a17a78acc0b65c43'),('a320f13a-106e-11e5-b6bc-005056970072','41','Corrections Federal','bae86261267111e1a17a78acc0b65c43'),('a320f447-106e-11e5-b6bc-005056970072','42','Corrections State','bae86261267111e1a17a78acc0b65c43'),('a32146d0-106e-11e5-b6bc-005056970072','43','Corrections Local','bae86261267111e1a17a78acc0b65c43'),('a32149bf-106e-11e5-b6bc-005056970072','44','Corrections Unknown Level','bae86261267111e1a17a78acc0b65c43'),('a3214c42-106e-11e5-b6bc-005056970072','5','PRIVATE HEALTH INSURANCE','bae86261267111e1a17a78acc0b65c43'),('a3214ebe-106e-11e5-b6bc-005056970072','51','Managed Care (Private)','bae86261267111e1a17a78acc0b65c43'),('a3215130-106e-11e5-b6bc-005056970072','511','Commercial Managed Care - HMO','bae86261267111e1a17a78acc0b65c43'),('a3215393-106e-11e5-b6bc-005056970072','512','Commercial Managed Care - PPO','bae86261267111e1a17a78acc0b65c43'),('a32155f2-106e-11e5-b6bc-005056970072','513','Commercial Managed Care - POS','bae86261267111e1a17a78acc0b65c43'),('a32158f9-106e-11e5-b6bc-005056970072','514','Exclusive  Provider Organization','bae86261267111e1a17a78acc0b65c43'),('a3215b83-106e-11e5-b6bc-005056970072','515','Gatekeeper PPO (GPPO)','bae86261267111e1a17a78acc0b65c43'),('a3215dea-106e-11e5-b6bc-005056970072','519','Managed Care, Other (non HMO)','bae86261267111e1a17a78acc0b65c43'),('a3216071-106e-11e5-b6bc-005056970072','52','Private Health Insurance - Indemnity','bae86261267111e1a17a78acc0b65c43'),('a32165b6-106e-11e5-b6bc-005056970072','521','Commercial Indemnity','bae86261267111e1a17a78acc0b65c43'),('a321684a-106e-11e5-b6bc-005056970072','522','Self-insured (ERISA) Administrative Services Only (ASO) plan','bae86261267111e1a17a78acc0b65c43'),('a3216ac2-106e-11e5-b6bc-005056970072','523','Medicare supplemental policy (as second payer)','bae86261267111e1a17a78acc0b65c43'),('a3216fd4-106e-11e5-b6bc-005056970072','529','Private health insurance—other commercial Indemnity','bae86261267111e1a17a78acc0b65c43'),('a3217360-106e-11e5-b6bc-005056970072','53','Managed Care (private) or private health insurance (indemnity), not otherwise specified','bae86261267111e1a17a78acc0b65c43'),('a32175fe-106e-11e5-b6bc-005056970072','54','Organized Delivery System','bae86261267111e1a17a78acc0b65c43'),('a32178e5-106e-11e5-b6bc-005056970072','55','Small Employer Purchasing Group','bae86261267111e1a17a78acc0b65c43'),('a3217b76-106e-11e5-b6bc-005056970072','59','Other Private Insurance','bae86261267111e1a17a78acc0b65c43'),('a3217df8-106e-11e5-b6bc-005056970072','6','BLUE CROSS/BLUE SHIELD','bae86261267111e1a17a78acc0b65c43'),('a32180db-106e-11e5-b6bc-005056970072','61','BC Managed Care','bae86261267111e1a17a78acc0b65c43'),('a321834d-106e-11e5-b6bc-005056970072','611','BC Managed Care - HMO','bae86261267111e1a17a78acc0b65c43'),('a32185fe-106e-11e5-b6bc-005056970072','612','BC Managed Care - PPO','bae86261267111e1a17a78acc0b65c43'),('a321887c-106e-11e5-b6bc-005056970072','613','BC Managed Care - POS','bae86261267111e1a17a78acc0b65c43'),('a3218aee-106e-11e5-b6bc-005056970072','619','BC Managed Care - Other','bae86261267111e1a17a78acc0b65c43'),('a3218d5c-106e-11e5-b6bc-005056970072','62','BC Indemnity','bae86261267111e1a17a78acc0b65c43'),('a3219c24-106e-11e5-b6bc-005056970072','63','BC (Indemnity or Managed Care) - Out of State','bae86261267111e1a17a78acc0b65c43'),('a3219f56-106e-11e5-b6bc-005056970072','64','BC (Indemnity or Managed Care) - Unspecified','bae86261267111e1a17a78acc0b65c43'),('a321a1fb-106e-11e5-b6bc-005056970072','69','BC (Indemnity or Managed Care) - Other','bae86261267111e1a17a78acc0b65c43'),('a321a47c-106e-11e5-b6bc-005056970072','7','MANAGED CARE, UNSPECIFIED(to be used only if one can\'t distinguish public  from private)','bae86261267111e1a17a78acc0b65c43'),('a321a861-106e-11e5-b6bc-005056970072','71','HMO','bae86261267111e1a17a78acc0b65c43'),('a321ab55-106e-11e5-b6bc-005056970072','72','PPO','bae86261267111e1a17a78acc0b65c43'),('a321ae00-106e-11e5-b6bc-005056970072','73','POS','bae86261267111e1a17a78acc0b65c43'),('a321b08a-106e-11e5-b6bc-005056970072','79','Other Managed Care, Unknown if public or private','bae86261267111e1a17a78acc0b65c43'),('a321b311-106e-11e5-b6bc-005056970072','8','NO PAYMENT from an Organization/Agency/Program/Private Payer Listed','bae86261267111e1a17a78acc0b65c43'),('a321b60c-106e-11e5-b6bc-005056970072','81','Self-pay','bae86261267111e1a17a78acc0b65c43'),('a321b88b-106e-11e5-b6bc-005056970072','82','No Charge','bae86261267111e1a17a78acc0b65c43'),('a3220e67-106e-11e5-b6bc-005056970072','821','Charity','bae86261267111e1a17a78acc0b65c43'),('a322113e-106e-11e5-b6bc-005056970072','822','Professional Courtesy','bae86261267111e1a17a78acc0b65c43'),('a3221441-106e-11e5-b6bc-005056970072','823','Hispanic or Latino','bae86261267111e1a17a78acc0b65c43'),('a32216ce-106e-11e5-b6bc-005056970072','83','Refusal to Pay/Bad Debt','bae86261267111e1a17a78acc0b65c43'),('a322194b-106e-11e5-b6bc-005056970072','84','Hill Burton Free Care','bae86261267111e1a17a78acc0b65c43'),('a3221cd4-106e-11e5-b6bc-005056970072','85','Research/Donor','bae86261267111e1a17a78acc0b65c43'),('a3221f6b-106e-11e5-b6bc-005056970072','89','No Payment, Other','bae86261267111e1a17a78acc0b65c43'),('a32221ff-106e-11e5-b6bc-005056970072','9','MISCELLANEOUS/OTHER','bae86261267111e1a17a78acc0b65c43'),('a322247a-106e-11e5-b6bc-005056970072','91','Foreign National','bae86261267111e1a17a78acc0b65c43'),('a32226f7-106e-11e5-b6bc-005056970072','92','Other (Non-government)','bae86261267111e1a17a78acc0b65c43'),('a32229cd-106e-11e5-b6bc-005056970072','93','Disability Insurance','bae86261267111e1a17a78acc0b65c43'),('a3222c9e-106e-11e5-b6bc-005056970072','94','Long-term Care Insurance','bae86261267111e1a17a78acc0b65c43'),('a3223012-106e-11e5-b6bc-005056970072','95','Worker\'s Compensation','bae86261267111e1a17a78acc0b65c43'),('a32232c5-106e-11e5-b6bc-005056970072','951','Worker\'s Comp HMO','bae86261267111e1a17a78acc0b65c43'),('a322353d-106e-11e5-b6bc-005056970072','953','Worker\'s Comp Fee-for-Service','bae86261267111e1a17a78acc0b65c43'),('a3223830-106e-11e5-b6bc-005056970072','954','Worker\'s Comp Other Managed Care','bae86261267111e1a17a78acc0b65c43'),('a3223b1a-106e-11e5-b6bc-005056970072','959','Worker\'s Comp, Other unspecified','bae86261267111e1a17a78acc0b65c43'),('a3223daa-106e-11e5-b6bc-005056970072','96','Auto Insurance (no fault)','bae86261267111e1a17a78acc0b65c43'),('a322401d-106e-11e5-b6bc-005056970072','98','Other specified (includes Hospice - Unspecified plan)','bae86261267111e1a17a78acc0b65c43'),('a322429d-106e-11e5-b6bc-005056970072','99','No Typology Code available for payment source','bae86261267111e1a17a78acc0b65c43'),('a3224511-106e-11e5-b6bc-005056970072','9999','Unavailable / Unknown','bae86261267111e1a17a78acc0b65c43');
/*!40000 ALTER TABLE `CODE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CODE_LIST`
--

DROP TABLE IF EXISTS `CODE_LIST`;
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
-- Dumping data for table `CODE_LIST`
--

LOCK TABLES `CODE_LIST` WRITE;
/*!40000 ALTER TABLE `CODE_LIST` DISABLE KEYS */;
INSERT INTO `CODE_LIST` VALUES ('8ae452962e3a223a012e3a254b808889'),('8ae452962e3a223a012e3a254b808890'),('8ae452962e3a223a012e3a254b808891'),('8ae452962e3a223a012e3a254b808892'),('bae50f18267111e1a17a78acc0b65c43'),('bae85d87267111e1a17a78acc0b65c43'),('bae86046267111e1a17a78acc0b65c43'),('bae86261267111e1a17a78acc0b65c43');
/*!40000 ALTER TABLE `CODE_LIST` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CODE_LIST_AUDIT_LOG`
--

DROP TABLE IF EXISTS `CODE_LIST_AUDIT_LOG`;
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
-- Dumping data for table `CODE_LIST_AUDIT_LOG`
--

LOCK TABLES `CODE_LIST_AUDIT_LOG` WRITE;
/*!40000 ALTER TABLE `CODE_LIST_AUDIT_LOG` DISABLE KEYS */;
/*!40000 ALTER TABLE `CODE_LIST_AUDIT_LOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CODE_SYSTEM`
--

DROP TABLE IF EXISTS `CODE_SYSTEM`;
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
-- Dumping data for table `CODE_SYSTEM`
--

LOCK TABLES `CODE_SYSTEM` WRITE;
/*!40000 ALTER TABLE `CODE_SYSTEM` DISABLE KEYS */;
INSERT INTO `CODE_SYSTEM` VALUES ('1','SNOMED-CT','1','SNM'),('10','ICD-9','4','I9'),('100','ICD-10','14','I10'),('101','CVX','16','CVX'),('102','LOINC','17','LNC'),('103','LOINC','20','LNC'),('104','ASC X12','23','ASC'),('105','GMDN','23','GMD'),('106','ICF','23','ICF'),('107','ISO 639-2','23','ISO'),('108','PHIN-VADS','23','VAD'),('109','UCUM','23','UCM'),('11','ICD-10','4','I10'),('110','UMDNS','23','UMD'),('112','HCPCS','3','HCP'),('113','LOINC','3','LNC'),('114','GMDN','5','GMD'),('115','UMDNS','5','UMD'),('116','HL7','6','HL7'),('117','ICD-9','6','I9'),('118','ICD-10','6','I10'),('119','UCUM','6','UCM'),('12','SNOMED-CT','5','SNM'),('120','HL7','7','HL7'),('121','ICD-9','7','I9'),('122','ICD-10','7','I10'),('124','ICF','8','ICF'),('125','LOINC','8','LNC'),('126','ASC X12','9','ASC'),('127','ICD-9','9','I9'),('128','ICD-10','9','I10'),('129','ISO 639-2','9','ISO'),('13','ICD-9','5','I9'),('130','LOINC','9','LNC'),('131','PHIN-VADS','9','VAD'),('132','Administrative Sex','9','HL7'),('133','CDC','9','CDC'),('134','Source of Payment Typology','9','Source of Payment Typology'),('135','LOINC','6','LNC'),('136','CDT','7','CDT'),('137','CDT','16','CDT'),('138','CDT','23','CDT'),('14','ICD-10','5','I10'),('15','SNOMED-CT','6','SNM'),('17','CPT','6','CPT'),('18','HCPCS','6','HCP'),('19','SNOMED-CT','7','SNM'),('2','SNOMED-CT','2','SNM'),('21','CPT','7','CPT'),('22','HCPCS','7','HCP'),('23','SNOMED-CT','8','SNM'),('26','HL7','9','HL7'),('27','SNOMED-CT','10','SNM'),('28','LOINC','10','LNC'),('29','ICD-9','10','I9'),('30','ICD-10','10','I10'),('31','CPT','10','CPT'),('32','HCPCS','10','HCP'),('33','SNOMED-CT','11','SNM'),('34','LOINC','11','LNC'),('35','RxNorm','12','RxN'),('38','SNOMED-CT','14','SNM'),('39','LOINC','14','LNC'),('41','SNOMED-CT','16','SNM'),('42','ICD-9','16','I9'),('43','ICD-10','16','I10'),('44','CPT','16','CPT'),('45','HCPCS','16','HCP'),('46','SNOMED-CT','17','SNM'),('49','SNOMED-CT','18','SNM'),('5','SNOMED-CT','3','SNM'),('52','SNOMED-CT','19','SNM'),('53','ICD-9','19','I9'),('54','ICD-10','19','I10'),('55','SNOMED-CT','20','SNM'),('56','HL7','20','HL7'),('57','SNOMED-CT','21','SNM'),('6','ICD-9','3','I9'),('60','NQF','22','NQF'),('61','ICD-9','23','I9'),('62','ICD-10','23','I10'),('63','SNOMED-CT','23','SNM'),('64','CPT','23','CPT'),('65','LOINC','23','LNC'),('66','HCPCS','23','HCP'),('67','RxNorm','23','RxN'),('68','HL7','23','HL7'),('69','SNOMED-CT','9','SNM'),('7','ICD-10','3','I10'),('70','Grouping','3','GRP'),('71','Grouping','4','GRP'),('72','Grouping','5','GRP'),('73','Grouping','6','GRP'),('74','Grouping','7','GRP'),('75','Grouping','8','GRP'),('76','Grouping','9','GRP'),('77','Grouping','10','GRP'),('78','Grouping','11','GRP'),('79','Grouping','12','GRP'),('8','CPT','3','CPT'),('81','Grouping','14','GRP'),('83','Grouping','16','GRP'),('84','Grouping','17','GRP'),('85','Grouping','18','GRP'),('86','Grouping','19','GRP'),('87','Grouping','20','GRP'),('88','Grouping','21','GRP'),('89','Grouping','22','GRP'),('9','SNOMED-CT','4','SNM'),('90','Grouping','23','GRP'),('91','Grouping','1','GRP'),('92','Grouping','2','GRP'),('93','LOINC','1','LNC'),('94','CVX','10','CVX'),('95','CPT','11','CPT'),('96','HCPCS','11','HCP'),('97','UCUM','11','UCM'),('98','CVX','12','CVX'),('99','ICD-9','14','I9');
/*!40000 ALTER TABLE `CODE_SYSTEM` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COMPONENT_MEASURES`
--

DROP TABLE IF EXISTS `COMPONENT_MEASURES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `COMPONENT_MEASURES` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `COMPOSITE_MEASURE_ID` varchar(64) NOT NULL,
  `COMPONENT_MEASURE_ID` varchar(64) NOT NULL,
  `ALIAS` text,
  PRIMARY KEY (`ID`),
  KEY `COMPOSITE_MEASURE_ID_FK` (`COMPOSITE_MEASURE_ID`),
  KEY `COMPONENT_MEASURE_ID_FK` (`COMPONENT_MEASURE_ID`),
  CONSTRAINT `COMPONENT_MEASURE_ID_FK` FOREIGN KEY (`COMPONENT_MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `COMPOSITE_MEASURE_ID_FK` FOREIGN KEY (`COMPOSITE_MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COMPONENT_MEASURES`
--

LOCK TABLES `COMPONENT_MEASURES` WRITE;
/*!40000 ALTER TABLE `COMPONENT_MEASURES` DISABLE KEYS */;
/*!40000 ALTER TABLE `COMPONENT_MEASURES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CONTEXT`
--

DROP TABLE IF EXISTS `CONTEXT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CONTEXT` (
  `CONTEXT_ID` varchar(64) NOT NULL,
  `DESCRIPTION` varchar(100) NOT NULL,
  PRIMARY KEY (`CONTEXT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CONTEXT`
--

LOCK TABLES `CONTEXT` WRITE;
/*!40000 ALTER TABLE `CONTEXT` DISABLE KEYS */;
INSERT INTO `CONTEXT` VALUES ('1','Population'),('10','User-defined'),('11','Measure Phrase'),('2','Numerator'),('3','Numerator Exclusions'),('4','Denominator'),('5','Denominator Exclusions'),('6','Denominator Exceptions'),('7','Measure Population'),('8','Measure Observation'),('9','Stratification');
/*!40000 ALTER TABLE `CONTEXT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CQL_AUDIT_LOG`
--

DROP TABLE IF EXISTS `CQL_AUDIT_LOG`;
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
-- Dumping data for table `CQL_AUDIT_LOG`
--

LOCK TABLES `CQL_AUDIT_LOG` WRITE;
/*!40000 ALTER TABLE `CQL_AUDIT_LOG` DISABLE KEYS */;
/*!40000 ALTER TABLE `CQL_AUDIT_LOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CQL_DATA`
--

DROP TABLE IF EXISTS `CQL_DATA`;
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
-- Dumping data for table `CQL_DATA`
--

LOCK TABLES `CQL_DATA` WRITE;
/*!40000 ALTER TABLE `CQL_DATA` DISABLE KEYS */;
/*!40000 ALTER TABLE `CQL_DATA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CQL_LIBRARY`
--

DROP TABLE IF EXISTS `CQL_LIBRARY`;
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
  `QDM_VERSION` varchar(45) NOT NULL DEFAULT '5.0.2',
  `LAST_MODIFIED_ON` timestamp NULL DEFAULT NULL,
  `LAST_MODIFIED_BY` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `LOCKED_USER_ID_FK_idx` (`LOCKED_USER`),
  KEY `CQL_OWNER_ID_FK_idx` (`OWNER_ID`),
  KEY `fk_library_user` (`LAST_MODIFIED_BY`),
  KEY `idx_set_id` (`SET_ID`),
  KEY `idx_library_cql_name` (`CQL_NAME`),
  CONSTRAINT `CQL_OWNER_ID_FK` FOREIGN KEY (`OWNER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `LOCKED_USER_ID_FK` FOREIGN KEY (`LOCKED_USER`) REFERENCES `USER` (`USER_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_library_user` FOREIGN KEY (`LAST_MODIFIED_BY`) REFERENCES `USER` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CQL_LIBRARY`
--

LOCK TABLES `CQL_LIBRARY` WRITE;
/*!40000 ALTER TABLE `CQL_LIBRARY` DISABLE KEYS */;
/*!40000 ALTER TABLE `CQL_LIBRARY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CQL_LIBRARY_ASSOCIATION`
--

DROP TABLE IF EXISTS `CQL_LIBRARY_ASSOCIATION`;
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
-- Dumping data for table `CQL_LIBRARY_ASSOCIATION`
--

LOCK TABLES `CQL_LIBRARY_ASSOCIATION` WRITE;
/*!40000 ALTER TABLE `CQL_LIBRARY_ASSOCIATION` DISABLE KEYS */;
/*!40000 ALTER TABLE `CQL_LIBRARY_ASSOCIATION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CQL_LIBRARY_BACKUP`
--

DROP TABLE IF EXISTS `CQL_LIBRARY_BACKUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CQL_LIBRARY_BACKUP` (
  `ID` varchar(64) NOT NULL,
  `MEASURE_ID` varchar(64) DEFAULT NULL,
  `MEASURE_SET_ID` varchar(45) DEFAULT NULL,
  `CQL_SET_ID` varchar(45) DEFAULT NULL,
  `CQL_NAME` varchar(300) DEFAULT NULL,
  `DRAFT` tinyint(1) DEFAULT '1',
  `VERSION` decimal(6,3) DEFAULT '0.000',
  `FINALIZED_DATE` timestamp NULL DEFAULT NULL,
  `RELEASE_VERSION` varchar(45) DEFAULT NULL,
  `OWNER_ID` varchar(40) NOT NULL,
  `LOCKED_USER` varchar(40) DEFAULT NULL,
  `LOCKED_OUT_DATE` timestamp NULL DEFAULT NULL,
  `CQL_XML` longblob,
  `REVISION_NUMBER` int(3) unsigned zerofill DEFAULT '000',
  `QDM_VERSION` varchar(45) NOT NULL DEFAULT '5.0.2'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CQL_LIBRARY_BACKUP`
--

LOCK TABLES `CQL_LIBRARY_BACKUP` WRITE;
/*!40000 ALTER TABLE `CQL_LIBRARY_BACKUP` DISABLE KEYS */;
/*!40000 ALTER TABLE `CQL_LIBRARY_BACKUP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CQL_LIBRARY_EXPORT`
--

DROP TABLE IF EXISTS `CQL_LIBRARY_EXPORT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CQL_LIBRARY_EXPORT` (
  `ID` varchar(64) NOT NULL,
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
-- Dumping data for table `CQL_LIBRARY_EXPORT`
--

LOCK TABLES `CQL_LIBRARY_EXPORT` WRITE;
/*!40000 ALTER TABLE `CQL_LIBRARY_EXPORT` DISABLE KEYS */;
/*!40000 ALTER TABLE `CQL_LIBRARY_EXPORT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CQL_LIBRARY_HISTORY`
--

DROP TABLE IF EXISTS `CQL_LIBRARY_HISTORY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CQL_LIBRARY_HISTORY` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MEASURE_ID` varchar(64) DEFAULT NULL,
  `LIBRARY_ID` varchar(64) DEFAULT NULL,
  `LAST_MODIFIED_BY` varchar(40) NOT NULL,
  `CQL_LIBRARY` blob,
  `LAST_MODIFIED_ON` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `FREE_TEXT_EDITOR_USED` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`ID`),
  KEY `CQL_LIBRARY_HISTORY_USER_ID_FK` (`LAST_MODIFIED_BY`),
  KEY `CQL_LIBRARY_HISTORY_MEASURE_ID_FK` (`MEASURE_ID`),
  KEY `CQL_LIBRARY_HISTORY_LIBRARY_ID_FK` (`LIBRARY_ID`),
  CONSTRAINT `CQL_LIBRARY_HISTORY_LIBRARY_ID_FK` FOREIGN KEY (`LIBRARY_ID`) REFERENCES `CQL_LIBRARY` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `CQL_LIBRARY_HISTORY_MEASURE_ID_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `CQL_LIBRARY_HISTORY_USER_ID_FK` FOREIGN KEY (`LAST_MODIFIED_BY`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CQL_LIBRARY_HISTORY`
--

LOCK TABLES `CQL_LIBRARY_HISTORY` WRITE;
/*!40000 ALTER TABLE `CQL_LIBRARY_HISTORY` DISABLE KEYS */;
/*!40000 ALTER TABLE `CQL_LIBRARY_HISTORY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CQL_LIBRARY_SET`
--

DROP TABLE IF EXISTS `CQL_LIBRARY_SET`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CQL_LIBRARY_SET` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CQL_LIBRARY_SET`
--

LOCK TABLES `CQL_LIBRARY_SET` WRITE;
/*!40000 ALTER TABLE `CQL_LIBRARY_SET` DISABLE KEYS */;
/*!40000 ALTER TABLE `CQL_LIBRARY_SET` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CQL_LIBRARY_SHARE`
--

DROP TABLE IF EXISTS `CQL_LIBRARY_SHARE`;
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
-- Dumping data for table `CQL_LIBRARY_SHARE`
--

LOCK TABLES `CQL_LIBRARY_SHARE` WRITE;
/*!40000 ALTER TABLE `CQL_LIBRARY_SHARE` DISABLE KEYS */;
/*!40000 ALTER TABLE `CQL_LIBRARY_SHARE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATABASECHANGELOG`
--

DROP TABLE IF EXISTS `DATABASECHANGELOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DATABASECHANGELOG` (
  `ID` varchar(63) NOT NULL,
  `AUTHOR` varchar(63) NOT NULL,
  `FILENAME` varchar(200) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
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
-- Dumping data for table `DATABASECHANGELOG`
--

LOCK TABLES `DATABASECHANGELOG` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOG` DISABLE KEYS */;
/*!40000 ALTER TABLE `DATABASECHANGELOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATABASECHANGELOGLOCK`
--

DROP TABLE IF EXISTS `DATABASECHANGELOGLOCK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DATABASECHANGELOGLOCK` (
  `ID` int(11) NOT NULL,
  `LOCKED` tinyint(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOGLOCK`
--

LOCK TABLES `DATABASECHANGELOGLOCK` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` DISABLE KEYS */;
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATA_TYPE`
--

DROP TABLE IF EXISTS `DATA_TYPE`;
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
-- Dumping data for table `DATA_TYPE`
--

LOCK TABLES `DATA_TYPE` WRITE;
/*!40000 ALTER TABLE `DATA_TYPE` DISABLE KEYS */;
INSERT INTO `DATA_TYPE` VALUES ('1','Patient Care Experience','1'),('100','Patient Characteristic Payer','9'),('101','Patient Characteristic Sex','9'),('102','Patient Characteristic Ethnicity','9'),('103','Patient Characteristic Race','9'),('104','Medication, Discharge','12'),('105','Family History','4'),('106','Symptom','19'),('107','Immunization, Administered','24'),('108','Immunization, Order','24'),('111','Diagnosis','4'),('112','Assessment, Recommended','25'),('113','Assessment, Performed','25'),('114','Adverse Event','26'),('115','Allergy/Intolerance','27'),('116','Assessment, Not Performed','25'),('117','Assessment, Not Recommended','25'),('121','Device, Not Applied','5'),('122','Device, Not Ordered','5'),('123','Device, Not Recommended','5'),('124','Diagnostic Study, Not Ordered','6'),('125','Diagnostic Study, Not Performed','6'),('126','Diagnostic Study, Not Recommended','6'),('127','Encounter, Not Ordered','7'),('128','Encounter, Not Performed','7'),('129','Encounter, Not Recommended','7'),('13','Device, Applied','5'),('130','Immunization, Not Administered','24'),('131','Immunization, Not Ordered','24'),('132','Intervention, Not Ordered','10'),('133','Intervention, Not Performed','10'),('134','Intervention, Not Recommended','10'),('135','Laboratory Test, Not Ordered','11'),('136','Laboratory Test, Not Performed','11'),('137','Laboratory Test, Not Recommended','11'),('138','Medication, Not Administered','12'),('139','Medication, Not Discharged','12'),('140','Medication, Not Dispensed','12'),('141','Medication, Not Ordered','12'),('142','Physical Exam, Not Ordered','14'),('143','Physical Exam, Not Performed','14'),('144','Physical Exam, Not Recommended','14'),('145','Procedure, Not Ordered','16'),('146','Procedure, Not Performed','16'),('147','Procedure, Not Recommended','16'),('148','Substance, Not Administered','18'),('149','Substance, Not Ordered','18'),('15','Device, Order','5'),('150','Substance, Not Recommended','18'),('151','Participation','28'),('152','Assessment, Order','25'),('153','Assessment, Not Ordered','25'),('154','Communication, Performed','3'),('155','Communication, Not Performed','3'),('156','Related Person','29'),('18','Diagnostic Study, Order','6'),('19','Diagnostic Study, Performed','6'),('2','Provider Care Experience','1'),('21','Encounter, Order','7'),('22','Encounter, Performed','7'),('26','Patient Characteristic','9'),('3','Care Goal','2'),('30','Intervention, Order','10'),('31','Intervention, Performed','10'),('35','Laboratory Test, Order','11'),('36','Laboratory Test, Performed','11'),('38','Medication, Active','12'),('39','Medication, Administered','12'),('42','Medication, Dispensed','12'),('44','Medication, Order','12'),('56','Physical Exam, Order','14'),('57','Physical Exam, Performed','14'),('62','Procedure, Order','16'),('63','Procedure, Performed','16'),('66','Substance, Administered','18'),('70','Substance, Order','18'),('78','Device, Recommended','5'),('79','Encounter, Recommended','7'),('81','Intervention, Recommended','10'),('82','Laboratory Test, Recommended','11'),('87','Physical Exam, Recommended','14'),('88','Procedure, Recommended','16'),('89','Substance, Recommended','18'),('90','Diagnostic Study, Recommended','6'),('92','attribute','23'),('96','Timing Element','22'),('97','Patient Characteristic Birthdate','9'),('98','Patient Characteristic Expired','9'),('99','Patient Characteristic Clinical Trial Participant','9');
/*!40000 ALTER TABLE `DATA_TYPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATA_TYPE_BACKUP_AUG2015`
--

DROP TABLE IF EXISTS `DATA_TYPE_BACKUP_AUG2015`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DATA_TYPE_BACKUP_AUG2015` (
  `DATA_TYPE_ID` varchar(32) NOT NULL,
  `DESCRIPTION` varchar(50) NOT NULL,
  `CATEGORY_ID` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATA_TYPE_BACKUP_AUG2015`
--

LOCK TABLES `DATA_TYPE_BACKUP_AUG2015` WRITE;
/*!40000 ALTER TABLE `DATA_TYPE_BACKUP_AUG2015` DISABLE KEYS */;
INSERT INTO `DATA_TYPE_BACKUP_AUG2015` VALUES ('1','Patient Care Experience','1'),('10','Diagnosis, Resolved','4'),('100','Patient Characteristic Payer','9'),('101','Patient Characteristic Sex','9'),('102','Patient Characteristic Ethnicity','9'),('103','Patient Characteristic Race','9'),('104','Medication, Discharge','12'),('11','Device, Adverse Event','5'),('12','Device, Allergy','5'),('13','Device, Applied','5'),('14','Device, Intolerance','5'),('15','Device, Order','5'),('16','Diagnostic Study, Adverse Event','6'),('17','Diagnostic Study, Intolerance','6'),('18','Diagnostic Study, Order','6'),('19','Diagnostic Study, Performed','6'),('2','Provider Care Experience','1'),('21','Encounter, Order','7'),('22','Encounter, Performed','7'),('23','Functional Status, Order','8'),('24','Functional Status, Performed','8'),('26','Patient Characteristic','9'),('27','Provider Characteristic','9'),('28','Intervention, Adverse Event','10'),('29','Intervention, Intolerance','10'),('3','Care Goal','2'),('30','Intervention, Order','10'),('31','Intervention, Performed','10'),('33','Laboratory Test, Adverse Event','11'),('34','Laboratory Test, Intolerance','11'),('35','Laboratory Test, Order','11'),('36','Laboratory Test, Performed','11'),('38','Medication, Active','12'),('39','Medication, Administered','12'),('4','Communication: From Provider to Provider','3'),('40','Medication, Adverse Effects','12'),('41','Medication, Allergy','12'),('42','Medication, Dispensed','12'),('43','Medication, Intolerance','12'),('44','Medication, Order','12'),('5','Communication: From Patient to Provider','3'),('56','Physical Exam, Order','14'),('57','Physical Exam, Performed','14'),('6','Communication: From Provider to Patient','3'),('60','Procedure, Adverse Event','16'),('61','Procedure, Intolerance','16'),('62','Procedure, Order','16'),('63','Procedure, Performed','16'),('65','Risk Category Assessment','17'),('66','Substance, Administered','18'),('67','Substance, Adverse Event','18'),('68','Substance, Allergy','18'),('69','Substance, Intolerance','18'),('7','Diagnosis, Active','4'),('70','Substance, Order','18'),('71','Symptom, Active','19'),('72','Symptom, Assessed','19'),('73','Symptom, Inactive','19'),('74','Symptom, Resolved','19'),('76','Transfer From','21'),('77','Transfer To','21'),('78','Device, Recommended','5'),('79','Encounter, Recommended','7'),('8','Diagnosis, Family History','4'),('80','Functional Status, Recommended','8'),('81','Intervention, Recommended','10'),('82','Laboratory Test, Recommended','11'),('87','Physical Exam, Recommended','14'),('88','Procedure, Recommended','16'),('89','Substance, Recommended','18'),('9','Diagnosis, Inactive','4'),('90','Diagnostic Study, Recommended','6'),('92','attribute','23'),('95','Encounter, Active','7'),('96','Timing Element','22'),('97','Patient Characteristic Birthdate','9'),('98','Patient Characteristic Expired','9'),('99','Patient Characteristic Clinical Trial Participant','9');
/*!40000 ALTER TABLE `DATA_TYPE_BACKUP_AUG2015` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DECISION`
--

DROP TABLE IF EXISTS `DECISION`;
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
-- Dumping data for table `DECISION`
--

LOCK TABLES `DECISION` WRITE;
/*!40000 ALTER TABLE `DECISION` DISABLE KEYS */;
/*!40000 ALTER TABLE `DECISION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EMAIL_AUDIT_LOG`
--

DROP TABLE IF EXISTS `EMAIL_AUDIT_LOG`;
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
-- Dumping data for table `EMAIL_AUDIT_LOG`
--

LOCK TABLES `EMAIL_AUDIT_LOG` WRITE;
/*!40000 ALTER TABLE `EMAIL_AUDIT_LOG` DISABLE KEYS */;
/*!40000 ALTER TABLE `EMAIL_AUDIT_LOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GROUPED_CODE_LISTS`
--

DROP TABLE IF EXISTS `GROUPED_CODE_LISTS`;
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
-- Dumping data for table `GROUPED_CODE_LISTS`
--

LOCK TABLES `GROUPED_CODE_LISTS` WRITE;
/*!40000 ALTER TABLE `GROUPED_CODE_LISTS` DISABLE KEYS */;
/*!40000 ALTER TABLE `GROUPED_CODE_LISTS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LIST_OBJECT`
--

DROP TABLE IF EXISTS `LIST_OBJECT`;
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
-- Dumping data for table `LIST_OBJECT`
--

LOCK TABLES `LIST_OBJECT` WRITE;
/*!40000 ALTER TABLE `LIST_OBJECT` DISABLE KEYS */;
INSERT INTO `LIST_OBJECT` VALUES ('8a4d8c81309da15201309e46121d00d4','Measurement Period','80','2.16.840.1.113883.3.67.1.101.1.53','N/A',NULL,NULL,'22','1','60',NULL,NULL,NULL,'Default Measurement CodeList','1970-01-01 06:00:00',0),('8a4d8c81309da15201309e46124800e4','Measurement Start Date','80','2.16.840.1.113883.3.67.1.101.1.54','N/A',NULL,NULL,'22','1','60',NULL,NULL,NULL,'Default Measurement CodeList','1970-01-01 06:00:00',0),('8a4d8c81309da15201309e4612567F35','Dead','85','419099009','N/A',NULL,NULL,'9','2015','69',NULL,NULL,NULL,NULL,'2015-06-11 19:18:29',0),('8a4d8c81309da15201309e46126d00f4','Measurement End Date','80','2.16.840.1.113883.3.67.1.101.1.55','N/A',NULL,NULL,'22','1','60',NULL,NULL,NULL,'Default Measurement CodeList','1970-01-01 06:00:00',0),('8a4d8c81309da15201309e46126DA2E0','Birthdate','85','21112-8','N/A',NULL,NULL,'9','2015','69',NULL,NULL,NULL,NULL,'2015-06-11 19:18:29',0),('8ae452962e3a223a012e3a254b808889','Male','80','2.16.840.1.113883.3.560.100.1','N/A',NULL,NULL,'9','HL7 v2.5','132',NULL,NULL,'National Quality Forum','Default Gender CodeList','2011-07-27 15:47:00',0),('8ae452962e3a223a012e3a254b808890','Female','80','2.16.840.1.113883.3.560.100.2','N/A',NULL,NULL,'9','HL7 v2.5','132',NULL,NULL,'National Quality Forum','Default Gender CodeList','2011-07-27 15:47:00',0),('8ae452962e3a223a012e3a254b808891','Unknown Sex','80','2.16.840.1.113883.3.560.100.3','N/A',NULL,NULL,'9','HL7 v2.5','132',NULL,NULL,'National Quality Forum','Default Gender CodeList','2011-07-27 15:47:00',0),('8ae452962e3a223a012e3a254b808892','birth date','80','2.16.840.1.113883.3.560.100.4','N/A',NULL,NULL,'9','2.36','130',NULL,NULL,'National Quality Forum','Default Gender CodeList','2011-09-20 05:00:00',0),('bae50f18267111e1a17a78acc0b65c43','ONC Administrative Sex','86','2.16.840.1.113762.1.4.1','N/A',NULL,NULL,'9','HL7 v2.5','132',NULL,NULL,'National Library of Medicine','Supplimental CodeList','2011-07-27 15:47:00',1),('bae85d87267111e1a17a78acc0b65c43','Race','87','2.16.840.1.114222.4.11.836','N/A',NULL,NULL,'9','1.0','133',NULL,NULL,'CDC NCHS','Supplimental CodeList','2007-03-30 05:00:00',1),('bae86046267111e1a17a78acc0b65c43','Ethnicity','87','2.16.840.1.114222.4.11.837','N/A',NULL,NULL,'9','1.0','133',NULL,NULL,'CDC NCHS','Supplimental CodeList','2007-03-30 05:00:00',1),('bae86261267111e1a17a78acc0b65c43','Payer','88','2.16.840.1.114222.4.11.3591','N/A',NULL,NULL,'9','4.0','134',NULL,NULL,'PHDSC','Supplimental CodeList','2011-10-01 05:00:00',1);
/*!40000 ALTER TABLE `LIST_OBJECT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MAT_FLAG`
--

DROP TABLE IF EXISTS `MAT_FLAG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MAT_FLAG` (
  `ID` varchar(32) NOT NULL DEFAULT '1',
  `FLAG` varchar(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MAT_FLAG`
--

LOCK TABLES `MAT_FLAG` WRITE;
/*!40000 ALTER TABLE `MAT_FLAG` DISABLE KEYS */;
INSERT INTO `MAT_FLAG` VALUES ('1','1');
/*!40000 ALTER TABLE `MAT_FLAG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE`
--

DROP TABLE IF EXISTS `MEASURE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE` (
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
  `EMEASURE_ID` int(6) DEFAULT '0',
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
  `MEASURE_STEWARD_ID` int(11) DEFAULT NULL,
  `NQF_NUMBER` text,
  `MEASUREMENT_PERIOD_FROM` timestamp NULL DEFAULT NULL,
  `MEASUREMENT_PERIOD_TO` timestamp NULL DEFAULT NULL,
  `CQL_NAME` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `MEASURE_OWNER_FK` (`MEASURE_OWNER_ID`),
  KEY `MEASURE_LOCK_USER_FK` (`LOCKED_USER_ID`),
  KEY `MEASURE_SET_FK` (`MEASURE_SET_ID`),
  KEY `LAST_MODIFIED_BY` (`LAST_MODIFIED_BY`),
  KEY `MEASURE_STEWARD_ID` (`MEASURE_STEWARD_ID`),
  KEY `idx_measure_cql_name` (`CQL_NAME`),
  CONSTRAINT `MEASURE_LOCK_USER_FK` FOREIGN KEY (`LOCKED_USER_ID`) REFERENCES `USER` (`USER_ID`),
  CONSTRAINT `MEASURE_OWNER_FK` FOREIGN KEY (`MEASURE_OWNER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `MEASURE_SET_FK` FOREIGN KEY (`MEASURE_SET_ID`) REFERENCES `MEASURE_SET` (`ID`),
  CONSTRAINT `measure_ibfk_1` FOREIGN KEY (`LAST_MODIFIED_BY`) REFERENCES `USER` (`USER_ID`),
  CONSTRAINT `measure_ibfk_2` FOREIGN KEY (`MEASURE_STEWARD_ID`) REFERENCES `ORGANIZATION` (`ORG_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEASURE`
--

LOCK TABLES `MEASURE` WRITE;
/*!40000 ALTER TABLE `MEASURE` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASURE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASUREMENT_TERM`
--

DROP TABLE IF EXISTS `MEASUREMENT_TERM`;
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
-- Dumping data for table `MEASUREMENT_TERM`
--

LOCK TABLES `MEASUREMENT_TERM` WRITE;
/*!40000 ALTER TABLE `MEASUREMENT_TERM` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASUREMENT_TERM` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_AUDIT_LOG`
--

DROP TABLE IF EXISTS `MEASURE_AUDIT_LOG`;
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
-- Dumping data for table `MEASURE_AUDIT_LOG`
--

LOCK TABLES `MEASURE_AUDIT_LOG` WRITE;
/*!40000 ALTER TABLE `MEASURE_AUDIT_LOG` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASURE_AUDIT_LOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_AUDIT_LOG_BACKUP`
--

DROP TABLE IF EXISTS `MEASURE_AUDIT_LOG_BACKUP`;
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
-- Dumping data for table `MEASURE_AUDIT_LOG_BACKUP`
--

LOCK TABLES `MEASURE_AUDIT_LOG_BACKUP` WRITE;
/*!40000 ALTER TABLE `MEASURE_AUDIT_LOG_BACKUP` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASURE_AUDIT_LOG_BACKUP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_BACKUP_OCT2015`
--

DROP TABLE IF EXISTS `MEASURE_BACKUP_OCT2015`;
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
  `EMEASURE_ID` int(6) DEFAULT '0',
  `PRIVATE` tinyint(1) NOT NULL DEFAULT '0',
  `DELETED` varchar(20) DEFAULT NULL,
  `REVISION_NUMBER` int(3) unsigned zerofill DEFAULT '000'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEASURE_BACKUP_OCT2015`
--

LOCK TABLES `MEASURE_BACKUP_OCT2015` WRITE;
/*!40000 ALTER TABLE `MEASURE_BACKUP_OCT2015` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASURE_BACKUP_OCT2015` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_DETAILS`
--

DROP TABLE IF EXISTS `MEASURE_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_DETAILS` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
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
  CONSTRAINT `MEASURE_DETAILS_MEASURE_ID_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEASURE_DETAILS`
--

LOCK TABLES `MEASURE_DETAILS` WRITE;
/*!40000 ALTER TABLE `MEASURE_DETAILS` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASURE_DETAILS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_DETAILS_REFERENCE`
--

DROP TABLE IF EXISTS `MEASURE_DETAILS_REFERENCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_DETAILS_REFERENCE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MEASURE_DETAILS_ID` int(11) NOT NULL,
  `REFERENCE` longtext,
  `REFERENCE_NUMBER` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `MEASURE_DETAILS_REFERENCE_MEASURE_DETAILS_ID_FK` (`MEASURE_DETAILS_ID`),
  CONSTRAINT `MEASURE_DETAILS_REFERENCE_MEASURE_DETAILS_ID_FK` FOREIGN KEY (`MEASURE_DETAILS_ID`) REFERENCES `MEASURE_DETAILS` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEASURE_DETAILS_REFERENCE`
--

LOCK TABLES `MEASURE_DETAILS_REFERENCE` WRITE;
/*!40000 ALTER TABLE `MEASURE_DETAILS_REFERENCE` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASURE_DETAILS_REFERENCE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_DEVELOPER_ASSOCIATION`
--

DROP TABLE IF EXISTS `MEASURE_DEVELOPER_ASSOCIATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_DEVELOPER_ASSOCIATION` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MEASURE_ID` varchar(64) NOT NULL,
  `MEASURE_DEVELOPER_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `MEASURE_DEVELOPER_UNIQUE_CONSTRAINT` (`MEASURE_ID`,`MEASURE_DEVELOPER_ID`),
  KEY `MEASURE_DEVELOPER_ID_FK` (`MEASURE_DEVELOPER_ID`),
  CONSTRAINT `MEASURE_DEVELOPER_ID_FK` FOREIGN KEY (`MEASURE_DEVELOPER_ID`) REFERENCES `ORGANIZATION` (`ORG_ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `MEASURE_MEASURE_DEVELOPER_MEASURE_ID_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEASURE_DEVELOPER_ASSOCIATION`
--

LOCK TABLES `MEASURE_DEVELOPER_ASSOCIATION` WRITE;
/*!40000 ALTER TABLE `MEASURE_DEVELOPER_ASSOCIATION` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASURE_DEVELOPER_ASSOCIATION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_EXPORT`
--

DROP TABLE IF EXISTS `MEASURE_EXPORT`;
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
  PRIMARY KEY (`MEASURE_EXPORT_ID`),
  KEY `MEASURE_EXPORT_ID_MEASURE_FK` (`MEASURE_ID`),
  CONSTRAINT `MEASURE_EXPORT_ID_MEASURE_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEASURE_EXPORT`
--

LOCK TABLES `MEASURE_EXPORT` WRITE;
/*!40000 ALTER TABLE `MEASURE_EXPORT` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASURE_EXPORT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_NOTES_BACKUP`
--

DROP TABLE IF EXISTS `MEASURE_NOTES_BACKUP`;
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
-- Dumping data for table `MEASURE_NOTES_BACKUP`
--

LOCK TABLES `MEASURE_NOTES_BACKUP` WRITE;
/*!40000 ALTER TABLE `MEASURE_NOTES_BACKUP` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASURE_NOTES_BACKUP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_NOTES_BACKUP_SEP2017`
--

DROP TABLE IF EXISTS `MEASURE_NOTES_BACKUP_SEP2017`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_NOTES_BACKUP_SEP2017` (
  `ID` varchar(32) NOT NULL,
  `MEASURE_ID` varchar(32) NOT NULL,
  `TITLE` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(3000) DEFAULT NULL,
  `CREATE_USER_ID` varchar(40) NOT NULL,
  `MODIFY_USER_ID` varchar(40) DEFAULT NULL,
  `LAST_MODIFIED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEASURE_NOTES_BACKUP_SEP2017`
--

LOCK TABLES `MEASURE_NOTES_BACKUP_SEP2017` WRITE;
/*!40000 ALTER TABLE `MEASURE_NOTES_BACKUP_SEP2017` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASURE_NOTES_BACKUP_SEP2017` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_SCORE`
--

DROP TABLE IF EXISTS `MEASURE_SCORE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_SCORE` (
  `ID` varchar(32) NOT NULL,
  `SCORE` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEASURE_SCORE`
--

LOCK TABLES `MEASURE_SCORE` WRITE;
/*!40000 ALTER TABLE `MEASURE_SCORE` DISABLE KEYS */;
INSERT INTO `MEASURE_SCORE` VALUES ('1','Continuous Variable'),('2','Proportion'),('3','Ratio'),('4','Cohort');
/*!40000 ALTER TABLE `MEASURE_SCORE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_SET`
--

DROP TABLE IF EXISTS `MEASURE_SET`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_SET` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEASURE_SET`
--

LOCK TABLES `MEASURE_SET` WRITE;
/*!40000 ALTER TABLE `MEASURE_SET` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASURE_SET` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_SHARE`
--

DROP TABLE IF EXISTS `MEASURE_SHARE`;
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
-- Dumping data for table `MEASURE_SHARE`
--

LOCK TABLES `MEASURE_SHARE` WRITE;
/*!40000 ALTER TABLE `MEASURE_SHARE` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASURE_SHARE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_TYPES`
--

DROP TABLE IF EXISTS `MEASURE_TYPES`;
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
-- Dumping data for table `MEASURE_TYPES`
--

LOCK TABLES `MEASURE_TYPES` WRITE;
/*!40000 ALTER TABLE `MEASURE_TYPES` DISABLE KEYS */;
INSERT INTO `MEASURE_TYPES` VALUES ('1','Composite','COMPOSITE'),('10','Appropriate Use Process','APPROPRIATE'),('2','Cost/Resource Use','RESOURCE'),('3','Efficiency','EFFICIENCY'),('4','Outcome','OUTCOME'),('5','Patient Engagement/Experience','EXPERIENCE'),('6','Process','PROCESS'),('7','Structure','STRUCTURE'),('8','Patient Reported Outcome Performance','PRO-PM'),('9','Intermediate Clinical Outcome','INTERM-OM');
/*!40000 ALTER TABLE `MEASURE_TYPES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_TYPE_ASSOCIATION`
--

DROP TABLE IF EXISTS `MEASURE_TYPE_ASSOCIATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_TYPE_ASSOCIATION` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MEASURE_ID` varchar(64) NOT NULL,
  `MEASURE_TYPE_ID` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `MEASURE_TYPE_UNIQUE_CONSTRAINT` (`MEASURE_ID`,`MEASURE_TYPE_ID`),
  KEY `MEASURE_TYPE_ID_FK` (`MEASURE_TYPE_ID`),
  CONSTRAINT `MEASURE_MEASURE_TYPE_MEASURE_ID_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `MEASURE_TYPE_ID_FK` FOREIGN KEY (`MEASURE_TYPE_ID`) REFERENCES `MEASURE_TYPES` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEASURE_TYPE_ASSOCIATION`
--

LOCK TABLES `MEASURE_TYPE_ASSOCIATION` WRITE;
/*!40000 ALTER TABLE `MEASURE_TYPE_ASSOCIATION` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASURE_TYPE_ASSOCIATION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_VALIDATION_LOG`
--

DROP TABLE IF EXISTS `MEASURE_VALIDATION_LOG`;
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
-- Dumping data for table `MEASURE_VALIDATION_LOG`
--

LOCK TABLES `MEASURE_VALIDATION_LOG` WRITE;
/*!40000 ALTER TABLE `MEASURE_VALIDATION_LOG` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASURE_VALIDATION_LOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_XML`
--

DROP TABLE IF EXISTS `MEASURE_XML`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MEASURE_XML` (
  `ID` varchar(64) NOT NULL,
  `MEASURE_ID` varchar(64) NOT NULL,
  `MEASURE_XML` longblob,
  PRIMARY KEY (`ID`),
  KEY `MEASURE_XML_FK` (`MEASURE_ID`),
  CONSTRAINT `MEASURE_XML_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEASURE_XML`
--

LOCK TABLES `MEASURE_XML` WRITE;
/*!40000 ALTER TABLE `MEASURE_XML` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASURE_XML` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `METADATA`
--

DROP TABLE IF EXISTS `METADATA`;
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
-- Dumping data for table `METADATA`
--

LOCK TABLES `METADATA` WRITE;
/*!40000 ALTER TABLE `METADATA` DISABLE KEYS */;
/*!40000 ALTER TABLE `METADATA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MODES`
--

DROP TABLE IF EXISTS `MODES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MODES` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MODE_NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MODES`
--

LOCK TABLES `MODES` WRITE;
/*!40000 ALTER TABLE `MODES` DISABLE KEYS */;
INSERT INTO `MODES` VALUES (1,'Comparison'),(2,'Computative'),(3,'Nullable'),(4,'Value Sets');
/*!40000 ALTER TABLE `MODES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OBJECT_STATUS`
--

DROP TABLE IF EXISTS `OBJECT_STATUS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `OBJECT_STATUS` (
  `OBJECT_STATUS_ID` varchar(32) NOT NULL,
  `DESCRIPTION` varchar(50) NOT NULL,
  PRIMARY KEY (`OBJECT_STATUS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OBJECT_STATUS`
--

LOCK TABLES `OBJECT_STATUS` WRITE;
/*!40000 ALTER TABLE `OBJECT_STATUS` DISABLE KEYS */;
INSERT INTO `OBJECT_STATUS` VALUES ('1','In Progress'),('2','Complete');
/*!40000 ALTER TABLE `OBJECT_STATUS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OPERATOR`
--

DROP TABLE IF EXISTS `OPERATOR`;
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
-- Dumping data for table `OPERATOR`
--

LOCK TABLES `OPERATOR` WRITE;
/*!40000 ALTER TABLE `OPERATOR` DISABLE KEYS */;
INSERT INTO `OPERATOR` VALUES ('1','AND','AND','1'),('10','Ends During','EDU','2'),('11','Starts After End Of','SAE','2'),('12','Starts After Start Of','SAS','2'),('13','Starts Before Start Of','SBS','2'),('14','Starts Before End Of','SBE','2'),('15','Starts Concurrent With','SCW','2'),('16','Starts During','SDU','2'),('2','OR','OR','1'),('22','Avg','AVG','4'),('23','Count','COUNT','4'),('25','Max','MAX','4'),('26','Min','MIN','4'),('27','Median','MEDIAN','4'),('28','Sum','SUM','4'),('3','Concurrent With','CONCURRENT','2'),('30','First','FIRST','4'),('31','Second','SECOND','4'),('32','Third','THIRD','4'),('33','Fourth','FOURTH','4'),('34','Fifth','FIFTH','4'),('35','Most Recent','MOST RECENT','4'),('37','Less Than','<','5'),('38','Greater Than','>','5'),('39','Less Than or Equal To','<=','5'),('4','During','DURING','2'),('40','Greater Than or Equal To','>=','5'),('41','Equal To','=','5'),('42','Union','UNION','6'),('43','Intersection','INTERSECTION','6'),('44','AND NOT','AND NOT','1'),('45','OR NOT','OR NOT','1'),('46','Ends Concurrent With Start Of','ECWS','2'),('47','Starts Concurrent With End Of','SCWE','2'),('48','Overlaps','Overlap','2'),('49','Age At','AGE AT','4'),('5','Ends After End Of','EAE','2'),('50','Starts Before Or Concurrent With Start Of','SBSORSCW','2'),('51','Starts After Or Concurrent With Start Of','SASORSCW','2'),('52','Starts Before Or Concurrent With End Of','SBEORSCWE','2'),('53','Starts After Or Concurrent With End Of','SAEORSCWE','2'),('54','Ends Before Or Concurrent With End Of','EBEORECW','2'),('55','Ends After Or Concurrent With End Of','EAEORECW','2'),('56','Ends Before Or Concurrent With Start Of','EBSORECWS','2'),('57','Ends After Or Concurrent With Start Of','EASORECWS','2'),('58','Satisfies All','SATISFIES ALL','4'),('59','Satisfies Any','SATISFIES ANY','4'),('6','Ends After Start Of','EAS','2'),('60','Fulfills','FULFILLS','3'),('61','Datetimediff','DATETIMEDIFF','4'),('7','Ends Before End Of','EBE','2'),('8','Ends Before Start Of','EBS','2'),('9','Ends Concurrent With','ECW','2');
/*!40000 ALTER TABLE `OPERATOR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OPERATOR_BACKUP`
--

DROP TABLE IF EXISTS `OPERATOR_BACKUP`;
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
-- Dumping data for table `OPERATOR_BACKUP`
--

LOCK TABLES `OPERATOR_BACKUP` WRITE;
/*!40000 ALTER TABLE `OPERATOR_BACKUP` DISABLE KEYS */;
INSERT INTO `OPERATOR_BACKUP` VALUES ('1','AND','AND','1'),('10','Ends During','EDU','2'),('11','Starts After End Of','SAE','2'),('12','Starts After Start Of','SAS','2'),('13','Starts Before Start Of','SBS','2'),('14','Starts Before End Of','SBE','2'),('15','Starts Concurrent With','SCW','2'),('16','Starts During','SDU','2'),('2','OR','OR','1'),('22','AVG','AVG','4'),('23','COUNT','COUNT','4'),('25','MAX','MAX','4'),('26','MIN','MIN','4'),('27','MEDIAN','MEDIAN','4'),('28','SUM','SUM','4'),('3','Concurrent With','CONCURRENT','2'),('30','FIRST','FIRST','4'),('31','SECOND','SECOND','4'),('32','THIRD','THIRD','4'),('33','FOURTH','FOURTH','4'),('34','FIFTH','FIFTH','4'),('35','MOST RECENT','MOST RECENT','4'),('37','Less Than','<','5'),('38','Greater Than','>','5'),('39','Less Than or Equal To','<=','5'),('4','During','DURING','2'),('40','Greater Than or Equal To','>=','5'),('41','Equal To','=','5'),('42','UNION','UNION','6'),('43','INTERSECTION','INTERSECTION','6'),('44','AND NOT','AND NOT','1'),('45','OR NOT','OR NOT','1'),('46','Ends Concurrent With Start Of','ECWS','2'),('47','Starts Concurrent With End Of','SCWE','2'),('48','Overlaps','Overlap','2'),('49','AGE AT','AGE AT','4'),('5','Ends After End Of','EAE','2'),('50','Starts Before Or Concurrent With Start Of','SBSORSCW','2'),('51','Starts After Or Concurrent With Start Of','SASORSCW','2'),('52','Starts Before Or Concurrent With End Of','SBEORSCWE','2'),('53','Starts After Or Concurrent With End Of','SAEORSCWE','2'),('54','Ends Before Or Concurrent With End Of','EBEORECW','2'),('55','Ends After Or Concurrent With End Of','EAEORECW','2'),('56','Ends Before Or Concurrent With Start Of','EBSORECWS','2'),('57','Ends After Or Concurrent With Start Of','EASORECWS','2'),('58','SATISFIES ALL','SATISFIES ALL','4'),('59','SATISFIES ANY','SATISFIES ANY','4'),('6','Ends After Start Of','EAS','2'),('60','Fulfills','FULFILLS','3'),('61','DATETIMEDIFF','DATETIMEDIFF','4'),('7','Ends Before End Of','EBE','2'),('8','Ends Before Start Of','EBS','2'),('9','Ends Concurrent With','ECW','2');
/*!40000 ALTER TABLE `OPERATOR_BACKUP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OPERATOR_TYPE`
--

DROP TABLE IF EXISTS `OPERATOR_TYPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `OPERATOR_TYPE` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OPERATOR_TYPE`
--

LOCK TABLES `OPERATOR_TYPE` WRITE;
/*!40000 ALTER TABLE `OPERATOR_TYPE` DISABLE KEYS */;
INSERT INTO `OPERATOR_TYPE` VALUES ('1','Logical Operators'),('2','Relative Timings'),('3','Relative Associations'),('4','Functions'),('5','Comparison Operator'),('6','Set Operators');
/*!40000 ALTER TABLE `OPERATOR_TYPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ORGANIZATION`
--

DROP TABLE IF EXISTS `ORGANIZATION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ORGANIZATION` (
  `ORG_ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORG_NAME` varchar(150) DEFAULT NULL,
  `ORG_OID` varchar(50) NOT NULL,
  PRIMARY KEY (`ORG_ID`),
  UNIQUE KEY `ORG_OID_UNIQUE` (`ORG_OID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ORGANIZATION`
--

LOCK TABLES `ORGANIZATION` WRITE;
/*!40000 ALTER TABLE `ORGANIZATION` DISABLE KEYS */;
INSERT INTO `ORGANIZATION` VALUES (1,'Telligen','2.16.840.1.113883.3.67');
/*!40000 ALTER TABLE `ORGANIZATION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PACKAGER`
--

DROP TABLE IF EXISTS `PACKAGER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PACKAGER` (
  `PACKAGER_ID` varchar(32) NOT NULL,
  `MEASURE_ID` varchar(32) NOT NULL,
  `CLAUSE_ID` varchar(32) NOT NULL,
  `SEQUENCE` int(11) NOT NULL,
  PRIMARY KEY (`PACKAGER_ID`),
  KEY `PACKAGER_MEASURE_FK` (`MEASURE_ID`),
  KEY `PACKAGER_CLAUSE_FK` (`CLAUSE_ID`),
  CONSTRAINT `PACKAGER_CLAUSE_FK` FOREIGN KEY (`CLAUSE_ID`) REFERENCES `CLAUSE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PACKAGER_MEASURE_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PACKAGER`
--

LOCK TABLES `PACKAGER` WRITE;
/*!40000 ALTER TABLE `PACKAGER` DISABLE KEYS */;
/*!40000 ALTER TABLE `PACKAGER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QDM_ATTRIBUTES`
--

DROP TABLE IF EXISTS `QDM_ATTRIBUTES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QDM_ATTRIBUTES` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(32) NOT NULL,
  `DATA_TYPE_ID` varchar(32) NOT NULL,
  `QDM_ATTRIBUTE_TYPE` varchar(32) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=852 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QDM_ATTRIBUTES`
--

LOCK TABLES `QDM_ATTRIBUTES` WRITE;
/*!40000 ALTER TABLE `QDM_ATTRIBUTES` DISABLE KEYS */;
INSERT INTO `QDM_ATTRIBUTES` VALUES (1,'code','114','Data Type'),(3,'type','114','Data Type'),(4,'severity','114','Data Type'),(5,'facilityLocation','114','Data Type'),(6,'id','114','Data Type'),(7,'recorder','114','Data Type'),(9,'code','115','Data Type'),(10,'prevalencePeriod','115','Data Type'),(11,'type','115','Data Type'),(12,'severity','115','Data Type'),(13,'id','115','Data Type'),(14,'recorder','115','Data Type'),(16,'code','116','Data Type'),(17,'negationRationale','116','Data Type'),(18,'id','116','Data Type'),(21,'code','117','Data Type'),(22,'negationRationale','117','Data Type'),(23,'id','117','Data Type'),(26,'code','113','Data Type'),(27,'authorDatetime','113','Data Type'),(28,'method','113','Data Type'),(29,'reason','113','Data Type'),(30,'result','113','Data Type'),(31,'id','113','Data Type'),(34,'authorDatetime','112','Data Type'),(35,'code','112','Data Type'),(37,'reason','112','Data Type'),(38,'id','112','Data Type'),(41,'code','3','Data Type'),(42,'relatedTo','3','Data Type'),(43,'relevantPeriod','3','Data Type'),(44,'targetOutcome','3','Data Type'),(45,'id','3','Data Type'),(79,'anatomicalLocationSite','13','Data Type'),(80,'code','13','Data Type'),(81,'reason','13','Data Type'),(82,'relevantPeriod','13','Data Type'),(83,'id','13','Data Type'),(86,'code','121','Data Type'),(87,'negationRationale','121','Data Type'),(88,'id','121','Data Type'),(91,'code','122','Data Type'),(92,'negationRationale','122','Data Type'),(93,'id','122','Data Type'),(96,'code','123','Data Type'),(97,'negationRationale','123','Data Type'),(98,'id','123','Data Type'),(101,'authorDatetime','15','Data Type'),(102,'code','15','Data Type'),(103,'reason','15','Data Type'),(104,'id','15','Data Type'),(107,'authorDatetime','78','Data Type'),(108,'code','78','Data Type'),(109,'reason','78','Data Type'),(110,'id','78','Data Type'),(113,'anatomicalLocationSite','111','Data Type'),(114,'code','111','Data Type'),(115,'prevalencePeriod','111','Data Type'),(116,'severity','111','Data Type'),(117,'id','111','Data Type'),(118,'recorder','111','Data Type'),(120,'code','124','Data Type'),(121,'negationRationale','124','Data Type'),(122,'id','124','Data Type'),(125,'code','125','Data Type'),(126,'negationRationale','125','Data Type'),(127,'id','125','Data Type'),(130,'code','126','Data Type'),(131,'negationRationale','126','Data Type'),(132,'id','126','Data Type'),(135,'authorDatetime','18','Data Type'),(136,'code','18','Data Type'),(140,'reason','18','Data Type'),(141,'id','18','Data Type'),(144,'code','19','Data Type'),(145,'facilityLocation','19','Data Type'),(146,'method','19','Data Type'),(149,'reason','19','Data Type'),(150,'relevantPeriod','19','Data Type'),(151,'result','19','Data Type'),(152,'resultDatetime','19','Data Type'),(153,'status','19','Data Type'),(154,'id','19','Data Type'),(157,'code','90','Data Type'),(161,'authorDatetime','90','Data Type'),(162,'id','90','Data Type'),(174,'code','127','Data Type'),(175,'negationRationale','127','Data Type'),(176,'id','127','Data Type'),(179,'code','128','Data Type'),(180,'negationRationale','128','Data Type'),(181,'id','128','Data Type'),(184,'code','129','Data Type'),(185,'negationRationale','129','Data Type'),(186,'id','129','Data Type'),(189,'authorDatetime','21','Data Type'),(190,'code','21','Data Type'),(191,'facilityLocation','21','Data Type'),(192,'reason','21','Data Type'),(193,'id','21','Data Type'),(196,'admissionSource','22','Data Type'),(197,'code','22','Data Type'),(198,'diagnoses','22','Data Type'),(199,'dischargeDisposition','22','Data Type'),(201,'lengthOfStay','22','Data Type'),(205,'relevantPeriod','22','Data Type'),(206,'id','22','Data Type'),(209,'authorDatetime','79','Data Type'),(210,'code','79','Data Type'),(211,'facilityLocation','79','Data Type'),(212,'reason','79','Data Type'),(213,'id','79','Data Type'),(216,'authorDatetime','105','Data Type'),(217,'code','105','Data Type'),(218,'relationship','105','Data Type'),(219,'id','105','Data Type'),(220,'recorder','105','Data Type'),(222,'authorDatetime','107','Data Type'),(223,'code','107','Data Type'),(224,'dosage','107','Data Type'),(226,'reason','107','Data Type'),(227,'route','107','Data Type'),(228,'id','107','Data Type'),(231,'code','130','Data Type'),(232,'negationRationale','130','Data Type'),(233,'id','130','Data Type'),(236,'code','131','Data Type'),(237,'negationRationale','131','Data Type'),(238,'id','131','Data Type'),(241,'activeDatetime','108','Data Type'),(242,'authorDatetime','108','Data Type'),(243,'code','108','Data Type'),(244,'supply','108','Data Type'),(245,'reason','108','Data Type'),(246,'route','108','Data Type'),(247,'dosage','108','Data Type'),(248,'id','108','Data Type'),(251,'code','132','Data Type'),(252,'negationRationale','132','Data Type'),(253,'id','132','Data Type'),(256,'code','133','Data Type'),(257,'negationRationale','133','Data Type'),(258,'id','133','Data Type'),(261,'code','134','Data Type'),(262,'negationRationale','134','Data Type'),(263,'id','134','Data Type'),(266,'authorDatetime','30','Data Type'),(267,'code','30','Data Type'),(268,'reason','30','Data Type'),(269,'id','30','Data Type'),(272,'code','31','Data Type'),(273,'reason','31','Data Type'),(274,'relevantPeriod','31','Data Type'),(275,'result','31','Data Type'),(276,'status','31','Data Type'),(277,'id','31','Data Type'),(280,'authorDatetime','81','Data Type'),(281,'code','81','Data Type'),(282,'reason','81','Data Type'),(283,'id','81','Data Type'),(286,'code','135','Data Type'),(287,'negationRationale','135','Data Type'),(288,'id','135','Data Type'),(291,'code','136','Data Type'),(292,'negationRationale','136','Data Type'),(293,'id','136','Data Type'),(296,'code','137','Data Type'),(297,'negationRationale','137','Data Type'),(298,'id','137','Data Type'),(301,'authorDatetime','35','Data Type'),(302,'code','35','Data Type'),(304,'reason','35','Data Type'),(305,'id','35','Data Type'),(308,'code','36','Data Type'),(309,'method','36','Data Type'),(310,'referenceRange','36','Data Type'),(311,'relevantPeriod','36','Data Type'),(312,'result','36','Data Type'),(313,'resultDatetime','36','Data Type'),(314,'status','36','Data Type'),(315,'reason','36','Data Type'),(316,'id','36','Data Type'),(319,'authorDatetime','82','Data Type'),(320,'code','82','Data Type'),(322,'reason','82','Data Type'),(323,'id','82','Data Type'),(326,'code','38','Data Type'),(327,'dosage','38','Data Type'),(329,'frequency','38','Data Type'),(330,'relevantPeriod','38','Data Type'),(331,'route','38','Data Type'),(332,'id','38','Data Type'),(333,'recorder','38','Data Type'),(335,'code','39','Data Type'),(336,'dosage','39','Data Type'),(338,'frequency','39','Data Type'),(339,'relevantPeriod','39','Data Type'),(340,'route','39','Data Type'),(341,'reason','39','Data Type'),(342,'id','39','Data Type'),(345,'authorDatetime','104','Data Type'),(346,'code','104','Data Type'),(347,'dosage','104','Data Type'),(348,'supply','104','Data Type'),(349,'frequency','104','Data Type'),(350,'refills','104','Data Type'),(351,'route','104','Data Type'),(352,'id','104','Data Type'),(353,'recorder','104','Data Type'),(355,'authorDatetime','42','Data Type'),(356,'code','42','Data Type'),(357,'dosage','42','Data Type'),(358,'supply','42','Data Type'),(359,'frequency','42','Data Type'),(360,'refills','42','Data Type'),(361,'route','42','Data Type'),(362,'id','42','Data Type'),(365,'code','138','Data Type'),(366,'negationRationale','138','Data Type'),(367,'id','138','Data Type'),(370,'code','139','Data Type'),(371,'negationRationale','139','Data Type'),(372,'id','139','Data Type'),(375,'code','140','Data Type'),(376,'negationRationale','140','Data Type'),(377,'id','140','Data Type'),(380,'code','141','Data Type'),(381,'negationRationale','141','Data Type'),(382,'id','141','Data Type'),(386,'authorDatetime','44','Data Type'),(387,'code','44','Data Type'),(388,'dosage','44','Data Type'),(389,'supply','44','Data Type'),(390,'frequency','44','Data Type'),(392,'reason','44','Data Type'),(393,'refills','44','Data Type'),(394,'route','44','Data Type'),(395,'id','44','Data Type'),(398,'authorDatetime','1','Data Type'),(399,'code','1','Data Type'),(400,'id','1','Data Type'),(401,'recorder','1','Data Type'),(403,'authorDatetime','26','Data Type'),(404,'code','26','Data Type'),(405,'id','26','Data Type'),(408,'code','97','Data Type'),(409,'birthDatetime','97','Data Type'),(410,'id','97','Data Type'),(413,'code','99','Data Type'),(414,'reason','99','Data Type'),(415,'relevantPeriod','99','Data Type'),(416,'id','99','Data Type'),(419,'code','102','Data Type'),(420,'id','102','Data Type'),(423,'cause','98','Data Type'),(424,'code','98','Data Type'),(425,'expiredDatetime','98','Data Type'),(426,'id','98','Data Type'),(429,'code','100','Data Type'),(430,'relevantPeriod','100','Data Type'),(431,'id','100','Data Type'),(434,'code','103','Data Type'),(435,'id','103','Data Type'),(438,'code','101','Data Type'),(439,'id','101','Data Type'),(442,'code','142','Data Type'),(443,'negationRationale','142','Data Type'),(444,'id','142','Data Type'),(447,'code','143','Data Type'),(448,'negationRationale','143','Data Type'),(449,'id','143','Data Type'),(452,'code','144','Data Type'),(453,'negationRationale','144','Data Type'),(454,'id','144','Data Type'),(457,'anatomicalLocationSite','56','Data Type'),(458,'authorDatetime','56','Data Type'),(459,'code','56','Data Type'),(461,'reason','56','Data Type'),(462,'id','56','Data Type'),(465,'anatomicalLocationSite','57','Data Type'),(466,'code','57','Data Type'),(467,'method','57','Data Type'),(468,'reason','57','Data Type'),(469,'relevantPeriod','57','Data Type'),(470,'result','57','Data Type'),(471,'id','57','Data Type'),(474,'anatomicalLocationSite','87','Data Type'),(475,'authorDatetime','87','Data Type'),(476,'code','87','Data Type'),(478,'reason','87','Data Type'),(479,'id','87','Data Type'),(482,'code','145','Data Type'),(483,'negationRationale','145','Data Type'),(484,'id','145','Data Type'),(487,'negationRationale','146','Data Type'),(488,'code','146','Data Type'),(489,'id','146','Data Type'),(492,'code','147','Data Type'),(493,'negationRationale','147','Data Type'),(494,'id','147','Data Type'),(498,'anatomicalLocationSite','62','Data Type'),(499,'authorDatetime','62','Data Type'),(500,'code','62','Data Type'),(504,'reason','62','Data Type'),(505,'id','62','Data Type'),(509,'anatomicalLocationSite','63','Data Type'),(510,'code','63','Data Type'),(511,'incisionDatetime','63','Data Type'),(512,'method','63','Data Type'),(516,'reason','63','Data Type'),(517,'relevantPeriod','63','Data Type'),(518,'result','63','Data Type'),(519,'status','63','Data Type'),(520,'id','63','Data Type'),(524,'anatomicalLocationSite','88','Data Type'),(525,'authorDatetime','88','Data Type'),(526,'code','88','Data Type'),(529,'reason','88','Data Type'),(530,'id','88','Data Type'),(533,'authorDatetime','2','Data Type'),(534,'code','2','Data Type'),(535,'id','2','Data Type'),(536,'recorder','2','Data Type'),(543,'code','66','Data Type'),(544,'dosage','66','Data Type'),(545,'frequency','66','Data Type'),(546,'relevantPeriod','66','Data Type'),(547,'route','66','Data Type'),(548,'id','66','Data Type'),(551,'code','148','Data Type'),(552,'negationRationale','148','Data Type'),(553,'id','148','Data Type'),(556,'code','149','Data Type'),(557,'negationRationale','149','Data Type'),(558,'id','149','Data Type'),(561,'code','150','Data Type'),(562,'negationRationale','150','Data Type'),(563,'id','150','Data Type'),(566,'authorDatetime','70','Data Type'),(567,'code','70','Data Type'),(568,'dosage','70','Data Type'),(569,'frequency','70','Data Type'),(571,'reason','70','Data Type'),(572,'refills','70','Data Type'),(573,'route','70','Data Type'),(574,'supply','70','Data Type'),(575,'id','70','Data Type'),(578,'authorDatetime ','89','Data Type'),(579,'code','89','Data Type'),(580,'dosage','89','Data Type'),(581,'frequency','89','Data Type'),(583,'reason','89','Data Type'),(584,'refills','89','Data Type'),(585,'route','89','Data Type'),(586,'id','89','Data Type'),(589,'code','106','Data Type'),(590,'prevalencePeriod','106','Data Type'),(591,'severity','106','Data Type'),(592,'id','106','Data Type'),(593,'recorder','106','Data Type'),(595,'authorDatetime','36','Data Type'),(596,'authorDatetime','142','Data Type'),(597,'authorDatetime','116','Data Type'),(598,'authorDatetime','117','Data Type'),(602,'authorDatetime','13','Data Type'),(603,'authorDatetime','121','Data Type'),(604,'authorDatetime','122','Data Type'),(605,'authorDatetime','123','Data Type'),(606,'authorDatetime','124','Data Type'),(607,'authorDatetime','125','Data Type'),(608,'authorDatetime','126','Data Type'),(609,'authorDatetime','19','Data Type'),(610,'authorDatetime','127','Data Type'),(611,'authorDatetime','128','Data Type'),(612,'authorDatetime','129','Data Type'),(613,'authorDatetime','22','Data Type'),(614,'authorDatetime','130','Data Type'),(615,'authorDatetime','131','Data Type'),(616,'authorDatetime','132','Data Type'),(617,'authorDatetime','133','Data Type'),(618,'authorDatetime','134','Data Type'),(619,'authorDatetime','31','Data Type'),(620,'authorDatetime','135','Data Type'),(621,'authorDatetime','136','Data Type'),(622,'authorDatetime','137','Data Type'),(623,'authorDatetime','39','Data Type'),(624,'authorDatetime','138','Data Type'),(625,'authorDatetime','139','Data Type'),(626,'authorDatetime','140','Data Type'),(627,'authorDatetime','141','Data Type'),(628,'authorDatetime','143','Data Type'),(629,'authorDatetime','144','Data Type'),(630,'authorDatetime','57','Data Type'),(631,'authorDatetime','145','Data Type'),(632,'authorDatetime','146','Data Type'),(633,'authorDatetime','147','Data Type'),(634,'authorDatetime','63','Data Type'),(635,'authorDatetime','66','Data Type'),(636,'authorDatetime','148','Data Type'),(637,'authorDatetime','149','Data Type'),(638,'authorDatetime','150','Data Type'),(639,'components','113','Data Type'),(640,'components','19','Data Type'),(641,'components','36','Data Type'),(642,'components','57','Data Type'),(643,'components','63','Data Type'),(644,'authorDatetime','111','Data Type'),(645,'authorDatetime','114','Data Type'),(646,'authorDatetime','115','Data Type'),(647,'code','151','Data Type'),(648,'id','151','Data Type'),(649,'participationPeriod','151','Data Type'),(651,'recorder','151','Data Type'),(652,'facilityLocations','22','Data Type'),(656,'relatedTo','113','Data Type'),(659,'relevantPeriod','44','Data Type'),(660,'relevantPeriod','42','Data Type'),(661,'authorDatetime','152','Data Type'),(662,'reason','152','Data Type'),(663,'code','152','Data Type'),(664,'id','152','Data Type'),(667,'authorDatetime','153','Data Type'),(668,'negationRationale','153','Data Type'),(669,'code','153','Data Type'),(670,'id','153','Data Type'),(673,'setting','44','Data Type'),(674,'authorDatetime','154','Data Type'),(675,'category','154','Data Type'),(676,'medium','154','Data Type'),(677,'code','154','Data Type'),(678,'sender','154','Data Type'),(679,'recipient','154','Data Type'),(681,'relatedTo','154','Data Type'),(682,'id','154','Data Type'),(685,'authorDatetime','155','Data Type'),(686,'category','155','Data Type'),(687,'negationRationale','155','Data Type'),(688,'code','155','Data Type'),(689,'sender','155','Data Type'),(690,'recipient','155','Data Type'),(691,'id','155','Data Type'),(694,'daysSupplied','44','Data Type'),(695,'daysSupplied','104','Data Type'),(696,'daysSupplied','42','Data Type'),(700,'id','156','Data Type'),(702,'sentDatetime','154','Data Type'),(703,'receivedDatetime','154','Data Type'),(704,'relevantDatetime','114','Data Type'),(705,'relevantDatetime','113','Data Type'),(706,'relevantDatetime','13','Data Type'),(707,'relevantDatetime','19','Data Type'),(708,'relevantDatetime','107','Data Type'),(709,'relevantDatetime','31','Data Type'),(710,'relevantDatetime','36','Data Type'),(711,'relevantDatetime','38','Data Type'),(712,'relevantDatetime','39','Data Type'),(713,'relevantDatetime','42','Data Type'),(714,'relevantDatetime','57','Data Type'),(715,'relevantDatetime','63','Data Type'),(716,'relevantDatetime','66','Data Type'),(717,'priority','21','Data Type'),(718,'priority','22','Data Type'),(719,'priority','62','Data Type'),(720,'priority','63','Data Type'),(721,'performer','113','Data Type'),(722,'performer','3','Data Type'),(723,'performer','13','Data Type'),(724,'performer','19','Data Type'),(725,'performer','107','Data Type'),(726,'performer','31','Data Type'),(727,'performer','36','Data Type'),(728,'performer','39','Data Type'),(729,'performer','57','Data Type'),(730,'performer','63','Data Type'),(731,'performer','66','Data Type'),(732,'requester','152','Data Type'),(733,'requester','112','Data Type'),(734,'requester','15','Data Type'),(735,'requester','78','Data Type'),(736,'requester','18','Data Type'),(737,'requester','90','Data Type'),(738,'requester','21','Data Type'),(739,'requester','79','Data Type'),(740,'requester','108','Data Type'),(741,'requester','30','Data Type'),(742,'requester','81','Data Type'),(743,'requester','35','Data Type'),(744,'requester','82','Data Type'),(745,'requester','56','Data Type'),(746,'requester','87','Data Type'),(747,'requester','62','Data Type'),(748,'requester','88','Data Type'),(749,'requester','70','Data Type'),(750,'requester','89','Data Type'),(751,'relevantPeriod','70','Data Type'),(752,'statusDate','3','Data Type'),(753,'prescriber','104','Data Type'),(754,'prescriber','42','Data Type'),(755,'prescriber','44','Data Type'),(756,'relevantPeriod','113','Data Type'),(757,'participant','22','Data Type'),(758,'dispenser','42','Data Type'),(759,'patientId','114','Data Type'),(760,'patientId','115','Data Type'),(761,'patientId','153','Data Type'),(762,'patientId','116','Data Type'),(763,'patientId','117','Data Type'),(764,'patientId','152','Data Type'),(765,'patientId','113','Data Type'),(766,'patientId','112','Data Type'),(767,'patientId','3','Data Type'),(768,'patientId','154','Data Type'),(769,'patientId','155','Data Type'),(770,'patientId','13','Data Type'),(771,'patientId','121','Data Type'),(772,'patientId','122','Data Type'),(773,'patientId','123','Data Type'),(774,'patientId','78','Data Type'),(775,'patientId','15','Data Type'),(776,'patientId','111','Data Type'),(777,'patientId','124','Data Type'),(778,'patientId','125','Data Type'),(779,'patientId','126','Data Type'),(780,'patientId','18','Data Type'),(781,'patientId','19','Data Type'),(782,'patientId','90','Data Type'),(783,'patientId','127','Data Type'),(784,'patientId','128','Data Type'),(785,'patientId','129','Data Type'),(786,'patientId','21','Data Type'),(787,'patientId','22','Data Type'),(788,'patientId','79','Data Type'),(789,'patientId','105','Data Type'),(790,'patientId','107','Data Type'),(791,'patientId','130','Data Type'),(792,'patientId','131','Data Type'),(793,'patientId','108','Data Type'),(794,'patientId','132','Data Type'),(795,'patientId','133','Data Type'),(796,'patientId','134','Data Type'),(797,'patientId','30','Data Type'),(798,'patientId','31','Data Type'),(799,'patientId','81','Data Type'),(800,'patientId','135','Data Type'),(801,'patientId','136','Data Type'),(802,'patientId','137','Data Type'),(803,'patientId','35','Data Type'),(804,'patientId','36','Data Type'),(805,'patientId','82','Data Type'),(806,'patientId','38','Data Type'),(807,'patientId','39','Data Type'),(808,'patientId','104','Data Type'),(809,'patientId','42','Data Type'),(810,'patientId','138','Data Type'),(811,'patientId','139','Data Type'),(812,'patientId','140','Data Type'),(813,'patientId','141','Data Type'),(814,'patientId','44','Data Type'),(815,'patientId','151','Data Type'),(816,'patientId','1','Data Type'),(817,'patientId','26','Data Type'),(818,'patientId','97','Data Type'),(819,'patientId','99','Data Type'),(820,'patientId','102','Data Type'),(821,'patientId','98','Data Type'),(822,'patientId','100','Data Type'),(823,'patientId','103','Data Type'),(824,'patientId','101','Data Type'),(825,'patientId','142','Data Type'),(826,'patientId','143','Data Type'),(827,'patientId','144','Data Type'),(828,'patientId','56','Data Type'),(829,'patientId','57','Data Type'),(830,'patientId','87','Data Type'),(831,'patientId','145','Data Type'),(832,'patientId','146','Data Type'),(833,'patientId','147','Data Type'),(834,'patientId','62','Data Type'),(835,'patientId','63','Data Type'),(836,'patientId','88','Data Type'),(837,'patientId','2','Data Type'),(838,'patientId','156','Data Type'),(839,'patientId','66','Data Type'),(840,'patientId','148','Data Type'),(841,'patientId','149','Data Type'),(842,'patientId','150','Data Type'),(843,'patientId','70','Data Type'),(844,'patientId','89','Data Type'),(845,'patientId','106','Data Type'),(846,'rank','62','Data Type'),(847,'rank','63','Data Type'),(848,'rank','88','Data Type'),(849,'identifier','156','Data Type'),(850,'linkedPatientId','156','Data Type'),(851,'code','156','Data Type');
/*!40000 ALTER TABLE `QDM_ATTRIBUTES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QDM_ATTRIBUTES_BACKUP`
--

DROP TABLE IF EXISTS `QDM_ATTRIBUTES_BACKUP`;
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
-- Dumping data for table `QDM_ATTRIBUTES_BACKUP`
--

LOCK TABLES `QDM_ATTRIBUTES_BACKUP` WRITE;
/*!40000 ALTER TABLE `QDM_ATTRIBUTES_BACKUP` DISABLE KEYS */;
INSERT INTO `QDM_ATTRIBUTES_BACKUP` VALUES ('10','provider preference','1','Data Type'),('1001','facility location','19','Data Type'),('101','negation rationale','14','Data Type'),('1010','start datetime','97','Data Type'),('1011','stop datetime','97','Data Type'),('1012','date','98','Data Type'),('1013','time','98','Data Type'),('1014','cause','98','Data Type'),('1015','start datetime','99','Data Type'),('1016','stop datetime','99','Data Type'),('1017','reason','99','Data Type'),('1018','start datetime','100','Data Type'),('1019','stop datetime','100','Data Type'),('102','patient preference','14','Data Type'),('1020','start datetime','101','Data Type'),('1021','stop datetime','101','Data Type'),('1023','dose','104','Data Type'),('1024','frequency','104','Data Type'),('1026','refills','104','Data Type'),('1027','route','104','Data Type'),('1028','start datetime','104','Data Type'),('1029','stop datetime','104','Data Type'),('103','provider preference','14','Data Type'),('1030','negation rationale','104','Data Type'),('1031','anatomical location site','7','Data Type'),('1032','anatomical location site','10','Data Type'),('1033','anatomical location site','9','Data Type'),('1034','anatomical location site','13','Data Type'),('1038','anatomical location site','56','Data Type'),('1039','anatomical location site','57','Data Type'),('104','reaction','14','Data Type'),('1040','anatomical location site','87','Data Type'),('1041','anatomical location site','62','Data Type'),('1042','anatomical location site','63','Data Type'),('1043','anatomical approach site','13','Data Type'),('1047','anatomical approach site','56','Data Type'),('1048','anatomical approach site','87','Data Type'),('1049','anatomical approach site','57','Data Type'),('105','start datetime','14','Data Type'),('1050','anatomical approach site','63','Data Type'),('1051','anatomical approach site','62','Data Type'),('1052','result','19','Data Type'),('1053','result','36','Data Type'),('1054','result','57','Data Type'),('1055','result','24','Data Type'),('1056','result','31','Data Type'),('1057','status','19','Data Type'),('1058','status','31','Data Type'),('1059','status','36','Data Type'),('106','stop datetime','14','Data Type'),('1060','patient preference','104','Data Type'),('1061','provider preference','104','Data Type'),('1062','cumulative medication duration','39','Data Type'),('1063','method','61','Data Type'),('1064','ordinality','61','Data Type'),('1065','radiation duration','62','Data Type'),('1066','status','63','Data Type'),('1067','radiation duration','63','Data Type'),('1068','radiation dosage','63','Data Type'),('1069','anatomical approach site','88','Data Type'),('1070','anatomical location site','88','Data Type'),('1071','active datetime','44','Data Type'),('1072','signed datetime','44','Data Type'),('1073','target outcome','3','Data Type'),('108','negation rationale','15','Data Type'),('109','patient preference','15','Data Type'),('11','start datetime','1','Data Type'),('110','provider preference','15','Data Type'),('111','reason','15','Data Type'),('112','start datetime','15','Data Type'),('113','stop datetime','15','Data Type'),('115','negation rationale','78','Data Type'),('116','patient preference','78','Data Type'),('117','provider preference','78','Data Type'),('118','reason','78','Data Type'),('119','start datetime','78','Data Type'),('12','stop datetime','1','Data Type'),('120','stop datetime','78','Data Type'),('122','negation rationale','16','Data Type'),('123','patient preference','16','Data Type'),('124','provider preference','16','Data Type'),('125','radiation dosage','16','Data Type'),('126','radiation duration','16','Data Type'),('127','reaction','16','Data Type'),('128','start datetime','16','Data Type'),('129','stop datetime','16','Data Type'),('131','negation rationale','17','Data Type'),('132','patient preference','17','Data Type'),('133','provider preference','17','Data Type'),('134','radiation dosage','17','Data Type'),('135','radiation duration','17','Data Type'),('136','reaction','17','Data Type'),('137','start datetime','17','Data Type'),('138','stop datetime','17','Data Type'),('14','negation rationale','2','Data Type'),('140','method','18','Data Type'),('141','negation rationale','18','Data Type'),('142','patient preference','18','Data Type'),('143','provider preference','18','Data Type'),('144','radiation dosage','18','Data Type'),('145','radiation duration','18','Data Type'),('146','reason','18','Data Type'),('147','start datetime','18','Data Type'),('148','stop datetime','18','Data Type'),('15','patient preference','2','Data Type'),('150','method','19','Data Type'),('151','negation rationale','19','Data Type'),('152','patient preference','19','Data Type'),('153','provider preference','19','Data Type'),('154','radiation dosage','19','Data Type'),('155','radiation duration','19','Data Type'),('156','reason','19','Data Type'),('157','start datetime','19','Data Type'),('158','stop datetime','19','Data Type'),('16','provider preference','2','Data Type'),('160','method','90','Data Type'),('161','negation rationale','90','Data Type'),('162','patient preference','90','Data Type'),('163','provider preference','90','Data Type'),('164','radiation dosage','90','Data Type'),('165','radiation duration','90','Data Type'),('166','start datetime','90','Data Type'),('167','stop datetime','90','Data Type'),('17','start datetime','2','Data Type'),('179','length of stay','95','Data Type'),('18','stop datetime','2','Data Type'),('181','facility location','95','Data Type'),('182','negation rationale','95','Data Type'),('183','patient preference','95','Data Type'),('184','provider preference','95','Data Type'),('185','reason','95','Data Type'),('186','admission datetime','95','Data Type'),('187','discharge datetime','95','Data Type'),('198','facility location','21','Data Type'),('199','negation rationale','21','Data Type'),('20','negation rationale','3','Data Type'),('200','patient preference','21','Data Type'),('201','provider preference','21','Data Type'),('202','reason','21','Data Type'),('203','start datetime','21','Data Type'),('204','stop datetime','21','Data Type'),('205','length of stay','22','Data Type'),('207','facility location','22','Data Type'),('208','negation rationale','22','Data Type'),('209','patient preference','22','Data Type'),('21','patient preference','3','Data Type'),('210','provider preference','22','Data Type'),('211','reason','22','Data Type'),('212','admission datetime','22','Data Type'),('213','discharge datetime','22','Data Type'),('215','facility location','79','Data Type'),('216','negation rationale','79','Data Type'),('217','patient preference','79','Data Type'),('218','provider preference','79','Data Type'),('219','reason','79','Data Type'),('22','provider preference','3','Data Type'),('220','start datetime','79','Data Type'),('221','stop datetime','79','Data Type'),('223','method','23','Data Type'),('224','negation rationale','23','Data Type'),('225','patient preference','23','Data Type'),('226','provider preference','23','Data Type'),('227','reason','23','Data Type'),('228','start datetime','23','Data Type'),('229','stop datetime','23','Data Type'),('23','start datetime','3','Data Type'),('231','method','24','Data Type'),('232','negation rationale','24','Data Type'),('233','patient preference','24','Data Type'),('234','provider preference','24','Data Type'),('235','reason','24','Data Type'),('236','start datetime','24','Data Type'),('237','stop datetime','24','Data Type'),('239','method','80','Data Type'),('24','stop datetime','3','Data Type'),('240','negation rationale','80','Data Type'),('241','patient preference','80','Data Type'),('242','provider preference','80','Data Type'),('243','reason','80','Data Type'),('244','start datetime','80','Data Type'),('245','stop datetime','80','Data Type'),('256','start datetime','26','Data Type'),('257','stop datetime','26','Data Type'),('259','negation rationale','27','Data Type'),('26','negation rationale','5','Data Type'),('260','start datetime','27','Data Type'),('261','stop datetime','27','Data Type'),('263','negation rationale','28','Data Type'),('264','patient preference','28','Data Type'),('265','provider preference','28','Data Type'),('266','reaction','28','Data Type'),('267','start datetime','28','Data Type'),('268','stop datetime','28','Data Type'),('27','patient preference','5','Data Type'),('270','negation rationale','29','Data Type'),('271','patient preference','29','Data Type'),('272','provider preference','29','Data Type'),('273','reaction','29','Data Type'),('274','start datetime','29','Data Type'),('275','stop datetime','29','Data Type'),('278','negation rationale','30','Data Type'),('279','patient preference','30','Data Type'),('28','provider preference','5','Data Type'),('280','provider preference','30','Data Type'),('281','reason','30','Data Type'),('282','start datetime','30','Data Type'),('283','stop datetime','30','Data Type'),('286','negation rationale','31','Data Type'),('287','patient preference','31','Data Type'),('288','provider preference','31','Data Type'),('289','reason','31','Data Type'),('29','start datetime','5','Data Type'),('290','start datetime','31','Data Type'),('291','stop datetime','31','Data Type'),('294','negation rationale','81','Data Type'),('295','patient preference','81','Data Type'),('296','provider preference','81','Data Type'),('297','reason','81','Data Type'),('298','start datetime','81','Data Type'),('299','stop datetime','81','Data Type'),('30','stop datetime','5','Data Type'),('310','negation rationale','33','Data Type'),('311','patient preference','33','Data Type'),('312','provider preference','33','Data Type'),('313','reaction','33','Data Type'),('314','start datetime','33','Data Type'),('315','stop datetime','33','Data Type'),('317','negation rationale','34','Data Type'),('318','patient preference','34','Data Type'),('319','provider preference','34','Data Type'),('32','negation rationale','6','Data Type'),('320','reaction','34','Data Type'),('321','start datetime','34','Data Type'),('322','stop datetime','34','Data Type'),('324','method','35','Data Type'),('325','negation rationale','35','Data Type'),('326','patient preference','35','Data Type'),('327','provider preference','35','Data Type'),('328','reason','35','Data Type'),('329','start datetime','35','Data Type'),('33','patient preference','6','Data Type'),('330','stop datetime','35','Data Type'),('332','method','36','Data Type'),('333','negation rationale','36','Data Type'),('334','patient preference','36','Data Type'),('335','provider preference','36','Data Type'),('336','reason','36','Data Type'),('337','start datetime','36','Data Type'),('338','stop datetime','36','Data Type'),('34','provider preference','6','Data Type'),('340','method','82','Data Type'),('341','negation rationale','82','Data Type'),('342','patient preference','82','Data Type'),('343','provider preference','82','Data Type'),('344','reason','82','Data Type'),('345','start datetime','82','Data Type'),('346','stop datetime','82','Data Type'),('35','start datetime','6','Data Type'),('356','cumulative medication duration','38','Data Type'),('357','dose','38','Data Type'),('358','frequency','38','Data Type'),('36','stop datetime','6','Data Type'),('360','negation rationale','38','Data Type'),('362','patient preference','38','Data Type'),('363','provider preference','38','Data Type'),('365','route','38','Data Type'),('366','start datetime','38','Data Type'),('367','stop datetime','38','Data Type'),('368','dose','39','Data Type'),('369','frequency','39','Data Type'),('371','negation rationale','39','Data Type'),('373','patient preference','39','Data Type'),('374','provider preference','39','Data Type'),('376','route','39','Data Type'),('377','start datetime','39','Data Type'),('378','stop datetime','39','Data Type'),('38','negation rationale','4','Data Type'),('382','negation rationale','40','Data Type'),('384','patient preference','40','Data Type'),('385','provider preference','40','Data Type'),('386','reaction','40','Data Type'),('389','start datetime','40','Data Type'),('39','patient preference','4','Data Type'),('390','stop datetime','40','Data Type'),('394','negation rationale','41','Data Type'),('396','patient preference','41','Data Type'),('397','provider preference','41','Data Type'),('398','reaction','41','Data Type'),('40','provider preference','4','Data Type'),('401','start datetime','41','Data Type'),('402','stop datetime','41','Data Type'),('403','cumulative medication duration','42','Data Type'),('404','dose','42','Data Type'),('405','frequency','42','Data Type'),('407','negation rationale','42','Data Type'),('409','patient preference','42','Data Type'),('41','start datetime','4','Data Type'),('410','provider preference','42','Data Type'),('411','refills','42','Data Type'),('412','route','42','Data Type'),('413','start datetime','42','Data Type'),('414','stop datetime','42','Data Type'),('418','negation rationale','43','Data Type'),('42','stop datetime','4','Data Type'),('420','patient preference','43','Data Type'),('421','provider preference','43','Data Type'),('422','reaction','43','Data Type'),('425','start datetime','43','Data Type'),('426','stop datetime','43','Data Type'),('427','cumulative medication duration','44','Data Type'),('428','dose','44','Data Type'),('429','frequency','44','Data Type'),('431','method','44','Data Type'),('432','negation rationale','44','Data Type'),('434','patient preference','44','Data Type'),('435','provider preference','44','Data Type'),('436','reason','44','Data Type'),('437','refills','44','Data Type'),('438','route','44','Data Type'),('439','start datetime','44','Data Type'),('44','negation rationale','7','Data Type'),('440','stop datetime','44','Data Type'),('45','ordinality','7','Data Type'),('453','method','56','Data Type'),('454','negation rationale','56','Data Type'),('455','patient preference','56','Data Type'),('456','provider preference','56','Data Type'),('457','reason','56','Data Type'),('458','start datetime','56','Data Type'),('459','stop datetime','56','Data Type'),('46','patient preference','7','Data Type'),('462','method','57','Data Type'),('463','negation rationale','57','Data Type'),('464','patient preference','57','Data Type'),('465','provider preference','57','Data Type'),('466','reason','57','Data Type'),('467','start datetime','57','Data Type'),('468','stop datetime','57','Data Type'),('47','provider preference','7','Data Type'),('471','method','87','Data Type'),('472','negation rationale','87','Data Type'),('473','patient preference','87','Data Type'),('474','provider preference','87','Data Type'),('475','reason','87','Data Type'),('476','start datetime','87','Data Type'),('477','stop datetime','87','Data Type'),('479','negation rationale','60','Data Type'),('48','severity','7','Data Type'),('480','patient preference','60','Data Type'),('481','provider preference','60','Data Type'),('482','reaction','60','Data Type'),('483','start datetime','60','Data Type'),('484','stop datetime','60','Data Type'),('486','negation rationale','61','Data Type'),('487','patient preference','61','Data Type'),('488','provider preference','61','Data Type'),('489','reaction','61','Data Type'),('49','start datetime','7','Data Type'),('490','start datetime','61','Data Type'),('491','stop datetime','61','Data Type'),('493','method','62','Data Type'),('494','negation rationale','62','Data Type'),('495','patient preference','62','Data Type'),('496','provider preference','62','Data Type'),('497','reason','62','Data Type'),('498','start datetime','62','Data Type'),('499','stop datetime','62','Data Type'),('501','method','63','Data Type'),('502','negation rationale','63','Data Type'),('503','patient preference','63','Data Type'),('504','provider preference','63','Data Type'),('505','reason','63','Data Type'),('506','start datetime','63','Data Type'),('507','stop datetime','63','Data Type'),('509','method','88','Data Type'),('510','negation rationale','88','Data Type'),('511','patient preference','88','Data Type'),('512','provider preference','88','Data Type'),('513','reason','88','Data Type'),('514','start datetime','88','Data Type'),('515','stop datetime','88','Data Type'),('52','stop datetime','7','Data Type'),('526','negation rationale','65','Data Type'),('527','patient preference','65','Data Type'),('528','provider preference','65','Data Type'),('529','start datetime','65','Data Type'),('530','stop datetime','65','Data Type'),('531','dose','66','Data Type'),('533','frequency','66','Data Type'),('534','negation rationale','66','Data Type'),('536','patient preference','66','Data Type'),('537','provider preference','66','Data Type'),('539','route','66','Data Type'),('54','negation rationale','8','Data Type'),('540','start datetime','66','Data Type'),('541','stop datetime','66','Data Type'),('545','negation rationale','67','Data Type'),('547','patient preference','67','Data Type'),('548','provider preference','67','Data Type'),('549','reaction','67','Data Type'),('55','ordinality','8','Data Type'),('552','start datetime','67','Data Type'),('553','stop datetime','67','Data Type'),('557','negation rationale','68','Data Type'),('559','patient preference','68','Data Type'),('56','patient preference','8','Data Type'),('560','provider preference','68','Data Type'),('561','reaction','68','Data Type'),('564','start datetime','68','Data Type'),('565','stop datetime','68','Data Type'),('569','negation rationale','69','Data Type'),('57','provider preference','8','Data Type'),('571','patient preference','69','Data Type'),('572','provider preference','69','Data Type'),('573','reaction','69','Data Type'),('576','start datetime','69','Data Type'),('577','stop datetime','69','Data Type'),('578','dose','70','Data Type'),('58','severity','8','Data Type'),('580','frequency','70','Data Type'),('581','method','70','Data Type'),('582','negation rationale','70','Data Type'),('584','patient preference','70','Data Type'),('585','provider preference','70','Data Type'),('586','reason','70','Data Type'),('587','refills','70','Data Type'),('588','route','70','Data Type'),('589','start datetime','70','Data Type'),('59','start datetime','8','Data Type'),('590','stop datetime','70','Data Type'),('591','dose','89','Data Type'),('593','frequency','89','Data Type'),('594','method','89','Data Type'),('595','negation rationale','89','Data Type'),('597','patient preference','89','Data Type'),('598','provider preference','89','Data Type'),('599','reason','89','Data Type'),('60','status','8','Data Type'),('600','refills','89','Data Type'),('601','route','89','Data Type'),('602','start datetime','89','Data Type'),('603','stop datetime','89','Data Type'),('605','negation rationale','71','Data Type'),('606','ordinality','71','Data Type'),('607','patient preference','71','Data Type'),('608','provider preference','71','Data Type'),('609','severity','71','Data Type'),('61','stop datetime','8','Data Type'),('610','start datetime','71','Data Type'),('612','stop datetime','71','Data Type'),('614','negation rationale','72','Data Type'),('615','ordinality','72','Data Type'),('616','patient preference','72','Data Type'),('617','provider preference','72','Data Type'),('618','severity','72','Data Type'),('619','start datetime','72','Data Type'),('621','stop datetime','72','Data Type'),('623','negation rationale','73','Data Type'),('624','ordinality','73','Data Type'),('625','patient preference','73','Data Type'),('626','provider preference','73','Data Type'),('627','severity','73','Data Type'),('628','start datetime','73','Data Type'),('63','negation rationale','9','Data Type'),('630','stop datetime','73','Data Type'),('632','negation rationale','74','Data Type'),('633','ordinality','74','Data Type'),('634','patient preference','74','Data Type'),('635','provider preference','74','Data Type'),('636','severity','74','Data Type'),('637','start datetime','74','Data Type'),('639','stop datetime','74','Data Type'),('64','ordinality','9','Data Type'),('641','negation rationale','75','Data Type'),('642','start datetime','75','Data Type'),('643','stop datetime','75','Data Type'),('645','negation rationale','76','Data Type'),('646','patient preference','76','Data Type'),('647','provider preference','76','Data Type'),('648','start datetime','76','Data Type'),('649','stop datetime','76','Data Type'),('65','patient preference','9','Data Type'),('651','negation rationale','77','Data Type'),('652','patient preference','77','Data Type'),('653','provider preference','77','Data Type'),('654','start datetime','77','Data Type'),('655','stop datetime','77','Data Type'),('66','provider preference','9','Data Type'),('662','Health Record Field','','Data Flow'),('663','laterality','7','Data Type'),('664','reason','13','Data Type'),('665','discharge status','22','Data Type'),('669','facility location arrival datetime','95','Data Type'),('67','severity','9','Data Type'),('670','facility location departure datetime','95','Data Type'),('671','facility location arrival datetime','22','Data Type'),('672','facility location departure datetime','22','Data Type'),('673','reason','39','Data Type'),('676','ordinality','62','Data Type'),('677','ordinality','63','Data Type'),('678','result','63','Data Type'),('679','incision datetime','63','Data Type'),('68','start datetime','9','Data Type'),('680','ordinality','88','Data Type'),('685','result','65','Data Type'),('689','related to','3','Data Type'),('70','stop datetime','9','Data Type'),('701','Source','','Data Flow'),('702','Recorder','','Data Flow'),('72','negation rationale','10','Data Type'),('73','ordinality','10','Data Type'),('74','patient preference','10','Data Type'),('75','provider preference','10','Data Type'),('76','severity','10','Data Type'),('77','start datetime','10','Data Type'),('79','stop datetime','10','Data Type'),('8','negation rationale','1','Data Type'),('81','negation rationale','11','Data Type'),('82','patient preference','11','Data Type'),('83','provider preference','11','Data Type'),('84','reaction','11','Data Type'),('85','start datetime','11','Data Type'),('86','stop datetime','11','Data Type'),('88','negation rationale','12','Data Type'),('89','patient preference','12','Data Type'),('9','patient preference','1','Data Type'),('90','provider preference','12','Data Type'),('91','reaction','12','Data Type'),('92','start datetime','12','Data Type'),('93','stop datetime','12','Data Type'),('95','negation rationale','13','Data Type'),('96','patient preference','13','Data Type'),('97','provider preference','13','Data Type'),('98','start datetime','13','Data Type'),('99','removal datetime','13','Data Type');
/*!40000 ALTER TABLE `QDM_ATTRIBUTES_BACKUP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QDM_ATTRIBUTES_BACKUP_AUG2015`
--

DROP TABLE IF EXISTS `QDM_ATTRIBUTES_BACKUP_AUG2015`;
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
-- Dumping data for table `QDM_ATTRIBUTES_BACKUP_AUG2015`
--

LOCK TABLES `QDM_ATTRIBUTES_BACKUP_AUG2015` WRITE;
/*!40000 ALTER TABLE `QDM_ATTRIBUTES_BACKUP_AUG2015` DISABLE KEYS */;
INSERT INTO `QDM_ATTRIBUTES_BACKUP_AUG2015` VALUES ('10','provider preference','1','Data Type'),('1001','facility location','19','Data Type'),('101','negation rationale','14','Data Type'),('1010','start datetime','97','Data Type'),('1011','stop datetime','97','Data Type'),('1012','date','98','Data Type'),('1013','time','98','Data Type'),('1014','cause','98','Data Type'),('1015','start datetime','99','Data Type'),('1016','stop datetime','99','Data Type'),('1017','reason','99','Data Type'),('1018','start datetime','100','Data Type'),('1019','stop datetime','100','Data Type'),('102','patient preference','14','Data Type'),('1020','start datetime','101','Data Type'),('1021','stop datetime','101','Data Type'),('1023','dose','104','Data Type'),('1024','frequency','104','Data Type'),('1026','refills','104','Data Type'),('1027','route','104','Data Type'),('1028','start datetime','104','Data Type'),('1029','stop datetime','104','Data Type'),('103','provider preference','14','Data Type'),('1030','negation rationale','104','Data Type'),('1031','anatomical location site','7','Data Type'),('1032','anatomical location site','10','Data Type'),('1033','anatomical location site','9','Data Type'),('1034','anatomical location site','13','Data Type'),('1038','anatomical location site','56','Data Type'),('1039','anatomical location site','57','Data Type'),('104','reaction','14','Data Type'),('1040','anatomical location site','87','Data Type'),('1041','anatomical location site','62','Data Type'),('1042','anatomical location site','63','Data Type'),('1043','anatomical approach site','13','Data Type'),('105','start datetime','14','Data Type'),('1050','anatomical approach site','63','Data Type'),('1051','anatomical approach site','62','Data Type'),('1052','result','19','Data Type'),('1053','result','36','Data Type'),('1054','result','57','Data Type'),('1055','result','24','Data Type'),('1056','result','31','Data Type'),('1057','status','19','Data Type'),('1058','status','31','Data Type'),('1059','status','36','Data Type'),('106','stop datetime','14','Data Type'),('1060','patient preference','104','Data Type'),('1061','provider preference','104','Data Type'),('1062','cumulative medication duration','39','Data Type'),('1064','ordinality','61','Data Type'),('1065','radiation duration','62','Data Type'),('1066','status','63','Data Type'),('1067','radiation duration','63','Data Type'),('1068','radiation dosage','63','Data Type'),('1069','anatomical approach site','88','Data Type'),('1070','anatomical location site','88','Data Type'),('1071','active datetime','44','Data Type'),('1072','signed datetime','44','Data Type'),('1073','target outcome','3','Data Type'),('108','negation rationale','15','Data Type'),('109','patient preference','15','Data Type'),('11','start datetime','1','Data Type'),('110','provider preference','15','Data Type'),('111','reason','15','Data Type'),('112','start datetime','15','Data Type'),('113','stop datetime','15','Data Type'),('115','negation rationale','78','Data Type'),('116','patient preference','78','Data Type'),('117','provider preference','78','Data Type'),('118','reason','78','Data Type'),('119','start datetime','78','Data Type'),('12','stop datetime','1','Data Type'),('120','stop datetime','78','Data Type'),('122','negation rationale','16','Data Type'),('123','patient preference','16','Data Type'),('124','provider preference','16','Data Type'),('125','radiation dosage','16','Data Type'),('126','radiation duration','16','Data Type'),('127','reaction','16','Data Type'),('128','start datetime','16','Data Type'),('129','stop datetime','16','Data Type'),('131','negation rationale','17','Data Type'),('132','patient preference','17','Data Type'),('133','provider preference','17','Data Type'),('134','radiation dosage','17','Data Type'),('135','radiation duration','17','Data Type'),('136','reaction','17','Data Type'),('137','start datetime','17','Data Type'),('138','stop datetime','17','Data Type'),('14','negation rationale','2','Data Type'),('140','method','18','Data Type'),('141','negation rationale','18','Data Type'),('142','patient preference','18','Data Type'),('143','provider preference','18','Data Type'),('144','radiation dosage','18','Data Type'),('145','radiation duration','18','Data Type'),('146','reason','18','Data Type'),('147','start datetime','18','Data Type'),('148','stop datetime','18','Data Type'),('15','patient preference','2','Data Type'),('150','method','19','Data Type'),('151','negation rationale','19','Data Type'),('152','patient preference','19','Data Type'),('153','provider preference','19','Data Type'),('154','radiation dosage','19','Data Type'),('155','radiation duration','19','Data Type'),('156','reason','19','Data Type'),('157','start datetime','19','Data Type'),('158','stop datetime','19','Data Type'),('16','provider preference','2','Data Type'),('160','method','90','Data Type'),('161','negation rationale','90','Data Type'),('162','patient preference','90','Data Type'),('163','provider preference','90','Data Type'),('164','radiation dosage','90','Data Type'),('165','radiation duration','90','Data Type'),('166','start datetime','90','Data Type'),('167','stop datetime','90','Data Type'),('17','start datetime','2','Data Type'),('179','length of stay','95','Data Type'),('18','stop datetime','2','Data Type'),('181','facility location','95','Data Type'),('182','negation rationale','95','Data Type'),('183','patient preference','95','Data Type'),('184','provider preference','95','Data Type'),('185','reason','95','Data Type'),('186','admission datetime','95','Data Type'),('187','discharge datetime','95','Data Type'),('198','facility location','21','Data Type'),('199','negation rationale','21','Data Type'),('20','negation rationale','3','Data Type'),('200','patient preference','21','Data Type'),('201','provider preference','21','Data Type'),('202','reason','21','Data Type'),('203','start datetime','21','Data Type'),('204','stop datetime','21','Data Type'),('205','length of stay','22','Data Type'),('207','facility location','22','Data Type'),('208','negation rationale','22','Data Type'),('209','patient preference','22','Data Type'),('21','patient preference','3','Data Type'),('210','provider preference','22','Data Type'),('211','reason','22','Data Type'),('212','admission datetime','22','Data Type'),('213','discharge datetime','22','Data Type'),('215','facility location','79','Data Type'),('216','negation rationale','79','Data Type'),('217','patient preference','79','Data Type'),('218','provider preference','79','Data Type'),('219','reason','79','Data Type'),('22','provider preference','3','Data Type'),('220','start datetime','79','Data Type'),('221','stop datetime','79','Data Type'),('223','method','23','Data Type'),('224','negation rationale','23','Data Type'),('225','patient preference','23','Data Type'),('226','provider preference','23','Data Type'),('227','reason','23','Data Type'),('228','start datetime','23','Data Type'),('229','stop datetime','23','Data Type'),('23','start datetime','3','Data Type'),('231','method','24','Data Type'),('232','negation rationale','24','Data Type'),('233','patient preference','24','Data Type'),('234','provider preference','24','Data Type'),('235','reason','24','Data Type'),('236','start datetime','24','Data Type'),('237','stop datetime','24','Data Type'),('239','method','80','Data Type'),('24','stop datetime','3','Data Type'),('240','negation rationale','80','Data Type'),('241','patient preference','80','Data Type'),('242','provider preference','80','Data Type'),('243','reason','80','Data Type'),('244','start datetime','80','Data Type'),('245','stop datetime','80','Data Type'),('256','start datetime','26','Data Type'),('257','stop datetime','26','Data Type'),('259','negation rationale','27','Data Type'),('26','negation rationale','5','Data Type'),('260','start datetime','27','Data Type'),('261','stop datetime','27','Data Type'),('263','negation rationale','28','Data Type'),('264','patient preference','28','Data Type'),('265','provider preference','28','Data Type'),('266','reaction','28','Data Type'),('267','start datetime','28','Data Type'),('268','stop datetime','28','Data Type'),('27','patient preference','5','Data Type'),('270','negation rationale','29','Data Type'),('271','patient preference','29','Data Type'),('272','provider preference','29','Data Type'),('273','reaction','29','Data Type'),('274','start datetime','29','Data Type'),('275','stop datetime','29','Data Type'),('278','negation rationale','30','Data Type'),('279','patient preference','30','Data Type'),('28','provider preference','5','Data Type'),('280','provider preference','30','Data Type'),('281','reason','30','Data Type'),('282','start datetime','30','Data Type'),('283','stop datetime','30','Data Type'),('286','negation rationale','31','Data Type'),('287','patient preference','31','Data Type'),('288','provider preference','31','Data Type'),('289','reason','31','Data Type'),('29','start datetime','5','Data Type'),('290','start datetime','31','Data Type'),('291','stop datetime','31','Data Type'),('294','negation rationale','81','Data Type'),('295','patient preference','81','Data Type'),('296','provider preference','81','Data Type'),('297','reason','81','Data Type'),('298','start datetime','81','Data Type'),('299','stop datetime','81','Data Type'),('30','stop datetime','5','Data Type'),('310','negation rationale','33','Data Type'),('311','patient preference','33','Data Type'),('312','provider preference','33','Data Type'),('313','reaction','33','Data Type'),('314','start datetime','33','Data Type'),('315','stop datetime','33','Data Type'),('317','negation rationale','34','Data Type'),('318','patient preference','34','Data Type'),('319','provider preference','34','Data Type'),('32','negation rationale','6','Data Type'),('320','reaction','34','Data Type'),('321','start datetime','34','Data Type'),('322','stop datetime','34','Data Type'),('324','method','35','Data Type'),('325','negation rationale','35','Data Type'),('326','patient preference','35','Data Type'),('327','provider preference','35','Data Type'),('328','reason','35','Data Type'),('329','start datetime','35','Data Type'),('33','patient preference','6','Data Type'),('330','stop datetime','35','Data Type'),('332','method','36','Data Type'),('333','negation rationale','36','Data Type'),('334','patient preference','36','Data Type'),('335','provider preference','36','Data Type'),('336','reason','36','Data Type'),('337','start datetime','36','Data Type'),('338','stop datetime','36','Data Type'),('34','provider preference','6','Data Type'),('340','method','82','Data Type'),('341','negation rationale','82','Data Type'),('342','patient preference','82','Data Type'),('343','provider preference','82','Data Type'),('344','reason','82','Data Type'),('345','start datetime','82','Data Type'),('346','stop datetime','82','Data Type'),('35','start datetime','6','Data Type'),('356','cumulative medication duration','38','Data Type'),('357','dose','38','Data Type'),('358','frequency','38','Data Type'),('36','stop datetime','6','Data Type'),('360','negation rationale','38','Data Type'),('362','patient preference','38','Data Type'),('363','provider preference','38','Data Type'),('365','route','38','Data Type'),('366','start datetime','38','Data Type'),('367','stop datetime','38','Data Type'),('368','dose','39','Data Type'),('369','frequency','39','Data Type'),('371','negation rationale','39','Data Type'),('373','patient preference','39','Data Type'),('374','provider preference','39','Data Type'),('376','route','39','Data Type'),('377','start datetime','39','Data Type'),('378','stop datetime','39','Data Type'),('38','negation rationale','4','Data Type'),('382','negation rationale','40','Data Type'),('384','patient preference','40','Data Type'),('385','provider preference','40','Data Type'),('386','reaction','40','Data Type'),('389','start datetime','40','Data Type'),('39','patient preference','4','Data Type'),('390','stop datetime','40','Data Type'),('394','negation rationale','41','Data Type'),('396','patient preference','41','Data Type'),('397','provider preference','41','Data Type'),('398','reaction','41','Data Type'),('40','provider preference','4','Data Type'),('401','start datetime','41','Data Type'),('402','stop datetime','41','Data Type'),('403','cumulative medication duration','42','Data Type'),('404','dose','42','Data Type'),('405','frequency','42','Data Type'),('407','negation rationale','42','Data Type'),('409','patient preference','42','Data Type'),('41','start datetime','4','Data Type'),('410','provider preference','42','Data Type'),('411','refills','42','Data Type'),('412','route','42','Data Type'),('413','start datetime','42','Data Type'),('414','stop datetime','42','Data Type'),('418','negation rationale','43','Data Type'),('42','stop datetime','4','Data Type'),('420','patient preference','43','Data Type'),('421','provider preference','43','Data Type'),('422','reaction','43','Data Type'),('425','start datetime','43','Data Type'),('426','stop datetime','43','Data Type'),('427','cumulative medication duration','44','Data Type'),('428','dose','44','Data Type'),('429','frequency','44','Data Type'),('431','method','44','Data Type'),('432','negation rationale','44','Data Type'),('434','patient preference','44','Data Type'),('435','provider preference','44','Data Type'),('436','reason','44','Data Type'),('437','refills','44','Data Type'),('438','route','44','Data Type'),('439','start datetime','44','Data Type'),('44','negation rationale','7','Data Type'),('440','stop datetime','44','Data Type'),('45','ordinality','7','Data Type'),('453','method','56','Data Type'),('454','negation rationale','56','Data Type'),('455','patient preference','56','Data Type'),('456','provider preference','56','Data Type'),('457','reason','56','Data Type'),('458','start datetime','56','Data Type'),('459','stop datetime','56','Data Type'),('46','patient preference','7','Data Type'),('462','method','57','Data Type'),('463','negation rationale','57','Data Type'),('464','patient preference','57','Data Type'),('465','provider preference','57','Data Type'),('466','reason','57','Data Type'),('467','start datetime','57','Data Type'),('468','stop datetime','57','Data Type'),('47','provider preference','7','Data Type'),('471','method','87','Data Type'),('472','negation rationale','87','Data Type'),('473','patient preference','87','Data Type'),('474','provider preference','87','Data Type'),('475','reason','87','Data Type'),('476','start datetime','87','Data Type'),('477','stop datetime','87','Data Type'),('479','negation rationale','60','Data Type'),('48','severity','7','Data Type'),('480','patient preference','60','Data Type'),('481','provider preference','60','Data Type'),('482','reaction','60','Data Type'),('483','start datetime','60','Data Type'),('484','stop datetime','60','Data Type'),('486','negation rationale','61','Data Type'),('487','patient preference','61','Data Type'),('488','provider preference','61','Data Type'),('489','reaction','61','Data Type'),('49','start datetime','7','Data Type'),('490','start datetime','61','Data Type'),('491','stop datetime','61','Data Type'),('493','method','62','Data Type'),('494','negation rationale','62','Data Type'),('495','patient preference','62','Data Type'),('496','provider preference','62','Data Type'),('497','reason','62','Data Type'),('498','start datetime','62','Data Type'),('499','stop datetime','62','Data Type'),('501','method','63','Data Type'),('502','negation rationale','63','Data Type'),('503','patient preference','63','Data Type'),('504','provider preference','63','Data Type'),('505','reason','63','Data Type'),('506','start datetime','63','Data Type'),('507','stop datetime','63','Data Type'),('509','method','88','Data Type'),('510','negation rationale','88','Data Type'),('511','patient preference','88','Data Type'),('512','provider preference','88','Data Type'),('513','reason','88','Data Type'),('514','start datetime','88','Data Type'),('515','stop datetime','88','Data Type'),('52','stop datetime','7','Data Type'),('526','negation rationale','65','Data Type'),('527','patient preference','65','Data Type'),('528','provider preference','65','Data Type'),('529','start datetime','65','Data Type'),('530','stop datetime','65','Data Type'),('531','dose','66','Data Type'),('533','frequency','66','Data Type'),('534','negation rationale','66','Data Type'),('536','patient preference','66','Data Type'),('537','provider preference','66','Data Type'),('539','route','66','Data Type'),('54','negation rationale','8','Data Type'),('540','start datetime','66','Data Type'),('541','stop datetime','66','Data Type'),('545','negation rationale','67','Data Type'),('547','patient preference','67','Data Type'),('548','provider preference','67','Data Type'),('549','reaction','67','Data Type'),('55','ordinality','8','Data Type'),('552','start datetime','67','Data Type'),('553','stop datetime','67','Data Type'),('557','negation rationale','68','Data Type'),('559','patient preference','68','Data Type'),('56','patient preference','8','Data Type'),('560','provider preference','68','Data Type'),('561','reaction','68','Data Type'),('564','start datetime','68','Data Type'),('565','stop datetime','68','Data Type'),('569','negation rationale','69','Data Type'),('57','provider preference','8','Data Type'),('571','patient preference','69','Data Type'),('572','provider preference','69','Data Type'),('573','reaction','69','Data Type'),('576','start datetime','69','Data Type'),('577','stop datetime','69','Data Type'),('578','dose','70','Data Type'),('58','severity','8','Data Type'),('580','frequency','70','Data Type'),('581','method','70','Data Type'),('582','negation rationale','70','Data Type'),('584','patient preference','70','Data Type'),('585','provider preference','70','Data Type'),('586','reason','70','Data Type'),('587','refills','70','Data Type'),('588','route','70','Data Type'),('589','start datetime','70','Data Type'),('59','start datetime','8','Data Type'),('590','stop datetime','70','Data Type'),('591','dose','89','Data Type'),('593','frequency','89','Data Type'),('594','method','89','Data Type'),('595','negation rationale','89','Data Type'),('597','patient preference','89','Data Type'),('598','provider preference','89','Data Type'),('599','reason','89','Data Type'),('60','status','8','Data Type'),('600','refills','89','Data Type'),('601','route','89','Data Type'),('602','start datetime','89','Data Type'),('603','stop datetime','89','Data Type'),('605','negation rationale','71','Data Type'),('606','ordinality','71','Data Type'),('607','patient preference','71','Data Type'),('608','provider preference','71','Data Type'),('609','severity','71','Data Type'),('61','stop datetime','8','Data Type'),('610','start datetime','71','Data Type'),('612','stop datetime','71','Data Type'),('614','negation rationale','72','Data Type'),('615','ordinality','72','Data Type'),('616','patient preference','72','Data Type'),('617','provider preference','72','Data Type'),('618','severity','72','Data Type'),('619','start datetime','72','Data Type'),('621','stop datetime','72','Data Type'),('623','negation rationale','73','Data Type'),('624','ordinality','73','Data Type'),('625','patient preference','73','Data Type'),('626','provider preference','73','Data Type'),('627','severity','73','Data Type'),('628','start datetime','73','Data Type'),('63','negation rationale','9','Data Type'),('630','stop datetime','73','Data Type'),('632','negation rationale','74','Data Type'),('633','ordinality','74','Data Type'),('634','patient preference','74','Data Type'),('635','provider preference','74','Data Type'),('636','severity','74','Data Type'),('637','start datetime','74','Data Type'),('639','stop datetime','74','Data Type'),('64','ordinality','9','Data Type'),('641','negation rationale','75','Data Type'),('642','start datetime','75','Data Type'),('643','stop datetime','75','Data Type'),('645','negation rationale','76','Data Type'),('646','patient preference','76','Data Type'),('647','provider preference','76','Data Type'),('648','start datetime','76','Data Type'),('649','stop datetime','76','Data Type'),('65','patient preference','9','Data Type'),('651','negation rationale','77','Data Type'),('652','patient preference','77','Data Type'),('653','provider preference','77','Data Type'),('654','start datetime','77','Data Type'),('655','stop datetime','77','Data Type'),('66','provider preference','9','Data Type'),('662','Health Record Field','','Data Flow'),('663','laterality','7','Data Type'),('664','reason','13','Data Type'),('665','discharge status','22','Data Type'),('669','facility location arrival datetime','95','Data Type'),('67','severity','9','Data Type'),('670','facility location departure datetime','95','Data Type'),('671','facility location arrival datetime','22','Data Type'),('672','facility location departure datetime','22','Data Type'),('673','reason','39','Data Type'),('676','ordinality','62','Data Type'),('677','ordinality','63','Data Type'),('678','result','63','Data Type'),('679','incision datetime','63','Data Type'),('68','start datetime','9','Data Type'),('680','ordinality','88','Data Type'),('685','result','65','Data Type'),('689','related to','3','Data Type'),('70','stop datetime','9','Data Type'),('701','Source','','Data Flow'),('702','Recorder','','Data Flow'),('72','negation rationale','10','Data Type'),('73','ordinality','10','Data Type'),('74','patient preference','10','Data Type'),('75','provider preference','10','Data Type'),('76','severity','10','Data Type'),('77','start datetime','10','Data Type'),('79','stop datetime','10','Data Type'),('8','negation rationale','1','Data Type'),('81','negation rationale','11','Data Type'),('82','patient preference','11','Data Type'),('83','provider preference','11','Data Type'),('84','reaction','11','Data Type'),('85','start datetime','11','Data Type'),('86','stop datetime','11','Data Type'),('88','negation rationale','12','Data Type'),('89','patient preference','12','Data Type'),('9','patient preference','1','Data Type'),('90','provider preference','12','Data Type'),('91','reaction','12','Data Type'),('92','start datetime','12','Data Type'),('93','stop datetime','12','Data Type'),('95','negation rationale','13','Data Type'),('96','patient preference','13','Data Type'),('97','provider preference','13','Data Type'),('98','start datetime','13','Data Type'),('99','removal datetime','13','Data Type');
/*!40000 ALTER TABLE `QDM_ATTRIBUTES_BACKUP_AUG2015` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QDM_ATTRIBUTES_BACKUP_SEP2015`
--

DROP TABLE IF EXISTS `QDM_ATTRIBUTES_BACKUP_SEP2015`;
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
-- Dumping data for table `QDM_ATTRIBUTES_BACKUP_SEP2015`
--

LOCK TABLES `QDM_ATTRIBUTES_BACKUP_SEP2015` WRITE;
/*!40000 ALTER TABLE `QDM_ATTRIBUTES_BACKUP_SEP2015` DISABLE KEYS */;
INSERT INTO `QDM_ATTRIBUTES_BACKUP_SEP2015` VALUES ('1001','facility location','19','Data Type'),('1010','start datetime','97','Data Type'),('1011','stop datetime','97','Data Type'),('1012','date','98','Data Type'),('1013','time','98','Data Type'),('1014','cause','98','Data Type'),('1015','start datetime','99','Data Type'),('1016','stop datetime','99','Data Type'),('1017','reason','99','Data Type'),('1018','start datetime','100','Data Type'),('1019','stop datetime','100','Data Type'),('1020','start datetime','101','Data Type'),('1021','stop datetime','101','Data Type'),('1023','dose','104','Data Type'),('1024','frequency','104','Data Type'),('1026','refills','104','Data Type'),('1027','route','104','Data Type'),('1028','start datetime','104','Data Type'),('1029','stop datetime','104','Data Type'),('1030','negation rationale','104','Data Type'),('1031','anatomical location site','7','Data Type'),('1032','anatomical location site','10','Data Type'),('1033','anatomical location site','9','Data Type'),('1034','anatomical location site','13','Data Type'),('1038','anatomical location site','56','Data Type'),('1039','anatomical location site','57','Data Type'),('104','reaction','14','Data Type'),('1040','anatomical location site','87','Data Type'),('1041','anatomical location site','62','Data Type'),('1042','anatomical location site','63','Data Type'),('1043','anatomical approach site','13','Data Type'),('105','start datetime','14','Data Type'),('1050','anatomical approach site','63','Data Type'),('1051','anatomical approach site','62','Data Type'),('1052','result','19','Data Type'),('1053','result','36','Data Type'),('1054','result','57','Data Type'),('1055','result','24','Data Type'),('1056','result','31','Data Type'),('1057','status','19','Data Type'),('1058','status','31','Data Type'),('1059','status','36','Data Type'),('106','stop datetime','14','Data Type'),('1062','cumulative medication duration','39','Data Type'),('1064','ordinality','61','Data Type'),('1065','radiation duration','62','Data Type'),('1066','status','63','Data Type'),('1067','radiation duration','63','Data Type'),('1068','radiation dosage','63','Data Type'),('1069','anatomical approach site','88','Data Type'),('1070','anatomical location site','88','Data Type'),('1071','active datetime','44','Data Type'),('1072','signed datetime','44','Data Type'),('1073','target outcome','3','Data Type'),('1074','relationship','105','Data Type'),('1075','onset age','105','Data Type'),('1076','recorded datetime','105','Data Type'),('1077','severity','106','Data Type'),('1078','onset datetime','106','Data Type'),('1079','abatement datetime','106','Data Type'),('108','negation rationale','15','Data Type'),('1080','reference range low','36','Data Type'),('1081','reference range high','36','Data Type'),('1083','dose','107','Data Type'),('1084','start datetime','107','Data Type'),('1085','route','107','Data Type'),('1086','reason','107','Data Type'),('1087','stop datetime','107','Data Type'),('1088','negation rationale','107','Data Type'),('1089','route','108','Data Type'),('1090','dose','108','Data Type'),('1091','start datetime','108','Data Type'),('1092','stop datetime','108','Data Type'),('1093','active datetime','108','Data Type'),('1094','signed datetime','108','Data Type'),('1095','negation rationale','108','Data Type'),('1096','reason','108','Data Type'),('1097','reaction','109','Data Type'),('1098','start datetime','109','Data Type'),('1099','stop datetime','109','Data Type'),('11','start datetime','1','Data Type'),('1100','reaction','110','Data Type'),('1101','start datetime','110','Data Type'),('1102','stop datetime','110','Data Type'),('1103','diagnosis','22','Data Type'),('1104','principal diagnosis','22','Data Type'),('1105','onset datetime','111','Data Type'),('1106','abatement datetime','111','Data Type'),('1107','anatomical location site','111','Data Type'),('1108','severity','111','Data Type'),('111','reason','15','Data Type'),('112','start datetime','15','Data Type'),('113','stop datetime','15','Data Type'),('115','negation rationale','78','Data Type'),('118','reason','78','Data Type'),('119','start datetime','78','Data Type'),('12','stop datetime','1','Data Type'),('120','stop datetime','78','Data Type'),('125','radiation dosage','16','Data Type'),('126','radiation duration','16','Data Type'),('127','reaction','16','Data Type'),('128','start datetime','16','Data Type'),('129','stop datetime','16','Data Type'),('134','radiation dosage','17','Data Type'),('135','radiation duration','17','Data Type'),('136','reaction','17','Data Type'),('137','start datetime','17','Data Type'),('138','stop datetime','17','Data Type'),('140','method','18','Data Type'),('141','negation rationale','18','Data Type'),('144','radiation dosage','18','Data Type'),('145','radiation duration','18','Data Type'),('146','reason','18','Data Type'),('147','start datetime','18','Data Type'),('148','stop datetime','18','Data Type'),('150','method','19','Data Type'),('151','negation rationale','19','Data Type'),('154','radiation dosage','19','Data Type'),('155','radiation duration','19','Data Type'),('156','reason','19','Data Type'),('157','start datetime','19','Data Type'),('158','stop datetime','19','Data Type'),('160','method','90','Data Type'),('161','negation rationale','90','Data Type'),('164','radiation dosage','90','Data Type'),('165','radiation duration','90','Data Type'),('166','start datetime','90','Data Type'),('167','stop datetime','90','Data Type'),('17','start datetime','2','Data Type'),('179','length of stay','95','Data Type'),('18','stop datetime','2','Data Type'),('181','facility location','95','Data Type'),('185','reason','95','Data Type'),('186','admission datetime','95','Data Type'),('187','discharge datetime','95','Data Type'),('198','facility location','21','Data Type'),('199','negation rationale','21','Data Type'),('202','reason','21','Data Type'),('203','start datetime','21','Data Type'),('204','stop datetime','21','Data Type'),('205','length of stay','22','Data Type'),('207','facility location','22','Data Type'),('208','negation rationale','22','Data Type'),('211','reason','22','Data Type'),('212','admission datetime','22','Data Type'),('213','discharge datetime','22','Data Type'),('215','facility location','79','Data Type'),('216','negation rationale','79','Data Type'),('219','reason','79','Data Type'),('220','start datetime','79','Data Type'),('221','stop datetime','79','Data Type'),('223','method','23','Data Type'),('224','negation rationale','23','Data Type'),('227','reason','23','Data Type'),('228','start datetime','23','Data Type'),('229','stop datetime','23','Data Type'),('23','start datetime','3','Data Type'),('231','method','24','Data Type'),('232','negation rationale','24','Data Type'),('235','reason','24','Data Type'),('236','start datetime','24','Data Type'),('237','stop datetime','24','Data Type'),('239','method','80','Data Type'),('24','stop datetime','3','Data Type'),('240','negation rationale','80','Data Type'),('243','reason','80','Data Type'),('244','start datetime','80','Data Type'),('245','stop datetime','80','Data Type'),('256','start datetime','26','Data Type'),('257','stop datetime','26','Data Type'),('26','negation rationale','5','Data Type'),('260','start datetime','27','Data Type'),('261','stop datetime','27','Data Type'),('266','reaction','28','Data Type'),('267','start datetime','28','Data Type'),('268','stop datetime','28','Data Type'),('273','reaction','29','Data Type'),('274','start datetime','29','Data Type'),('275','stop datetime','29','Data Type'),('278','negation rationale','30','Data Type'),('281','reason','30','Data Type'),('282','start datetime','30','Data Type'),('283','stop datetime','30','Data Type'),('286','negation rationale','31','Data Type'),('289','reason','31','Data Type'),('29','start datetime','5','Data Type'),('290','start datetime','31','Data Type'),('291','stop datetime','31','Data Type'),('294','negation rationale','81','Data Type'),('297','reason','81','Data Type'),('298','start datetime','81','Data Type'),('299','stop datetime','81','Data Type'),('30','stop datetime','5','Data Type'),('313','reaction','33','Data Type'),('314','start datetime','33','Data Type'),('315','stop datetime','33','Data Type'),('32','negation rationale','6','Data Type'),('320','reaction','34','Data Type'),('321','start datetime','34','Data Type'),('322','stop datetime','34','Data Type'),('324','method','35','Data Type'),('325','negation rationale','35','Data Type'),('328','reason','35','Data Type'),('329','start datetime','35','Data Type'),('330','stop datetime','35','Data Type'),('332','method','36','Data Type'),('333','negation rationale','36','Data Type'),('336','reason','36','Data Type'),('337','start datetime','36','Data Type'),('338','stop datetime','36','Data Type'),('340','method','82','Data Type'),('341','negation rationale','82','Data Type'),('344','reason','82','Data Type'),('345','start datetime','82','Data Type'),('346','stop datetime','82','Data Type'),('35','start datetime','6','Data Type'),('356','cumulative medication duration','38','Data Type'),('357','dose','38','Data Type'),('358','frequency','38','Data Type'),('36','stop datetime','6','Data Type'),('365','route','38','Data Type'),('366','start datetime','38','Data Type'),('367','stop datetime','38','Data Type'),('368','dose','39','Data Type'),('369','frequency','39','Data Type'),('371','negation rationale','39','Data Type'),('376','route','39','Data Type'),('377','start datetime','39','Data Type'),('378','stop datetime','39','Data Type'),('38','negation rationale','4','Data Type'),('386','reaction','40','Data Type'),('389','start datetime','40','Data Type'),('390','stop datetime','40','Data Type'),('398','reaction','41','Data Type'),('401','start datetime','41','Data Type'),('402','stop datetime','41','Data Type'),('403','cumulative medication duration','42','Data Type'),('404','dose','42','Data Type'),('405','frequency','42','Data Type'),('407','negation rationale','42','Data Type'),('41','start datetime','4','Data Type'),('411','refills','42','Data Type'),('412','route','42','Data Type'),('413','start datetime','42','Data Type'),('414','stop datetime','42','Data Type'),('42','stop datetime','4','Data Type'),('422','reaction','43','Data Type'),('425','start datetime','43','Data Type'),('426','stop datetime','43','Data Type'),('427','cumulative medication duration','44','Data Type'),('428','dose','44','Data Type'),('429','frequency','44','Data Type'),('431','method','44','Data Type'),('432','negation rationale','44','Data Type'),('436','reason','44','Data Type'),('437','refills','44','Data Type'),('438','route','44','Data Type'),('439','start datetime','44','Data Type'),('440','stop datetime','44','Data Type'),('45','ordinality','7','Data Type'),('453','method','56','Data Type'),('454','negation rationale','56','Data Type'),('457','reason','56','Data Type'),('458','start datetime','56','Data Type'),('459','stop datetime','56','Data Type'),('462','method','57','Data Type'),('463','negation rationale','57','Data Type'),('466','reason','57','Data Type'),('467','start datetime','57','Data Type'),('468','stop datetime','57','Data Type'),('471','method','87','Data Type'),('472','negation rationale','87','Data Type'),('475','reason','87','Data Type'),('476','start datetime','87','Data Type'),('477','stop datetime','87','Data Type'),('48','severity','7','Data Type'),('482','reaction','60','Data Type'),('483','start datetime','60','Data Type'),('484','stop datetime','60','Data Type'),('489','reaction','61','Data Type'),('49','start datetime','7','Data Type'),('490','start datetime','61','Data Type'),('491','stop datetime','61','Data Type'),('493','method','62','Data Type'),('494','negation rationale','62','Data Type'),('497','reason','62','Data Type'),('498','start datetime','62','Data Type'),('499','stop datetime','62','Data Type'),('501','method','63','Data Type'),('502','negation rationale','63','Data Type'),('505','reason','63','Data Type'),('506','start datetime','63','Data Type'),('507','stop datetime','63','Data Type'),('509','method','88','Data Type'),('510','negation rationale','88','Data Type'),('513','reason','88','Data Type'),('514','start datetime','88','Data Type'),('515','stop datetime','88','Data Type'),('52','stop datetime','7','Data Type'),('526','negation rationale','65','Data Type'),('529','start datetime','65','Data Type'),('530','stop datetime','65','Data Type'),('531','dose','66','Data Type'),('533','frequency','66','Data Type'),('534','negation rationale','66','Data Type'),('539','route','66','Data Type'),('540','start datetime','66','Data Type'),('541','stop datetime','66','Data Type'),('549','reaction','67','Data Type'),('552','start datetime','67','Data Type'),('553','stop datetime','67','Data Type'),('561','reaction','68','Data Type'),('564','start datetime','68','Data Type'),('565','stop datetime','68','Data Type'),('573','reaction','69','Data Type'),('576','start datetime','69','Data Type'),('577','stop datetime','69','Data Type'),('578','dose','70','Data Type'),('580','frequency','70','Data Type'),('581','method','70','Data Type'),('582','negation rationale','70','Data Type'),('586','reason','70','Data Type'),('587','refills','70','Data Type'),('588','route','70','Data Type'),('589','start datetime','70','Data Type'),('590','stop datetime','70','Data Type'),('591','dose','89','Data Type'),('593','frequency','89','Data Type'),('594','method','89','Data Type'),('595','negation rationale','89','Data Type'),('599','reason','89','Data Type'),('600','refills','89','Data Type'),('601','route','89','Data Type'),('602','start datetime','89','Data Type'),('603','stop datetime','89','Data Type'),('64','ordinality','9','Data Type'),('641','negation rationale','75','Data Type'),('642','start datetime','75','Data Type'),('643','stop datetime','75','Data Type'),('645','negation rationale','76','Data Type'),('648','start datetime','76','Data Type'),('649','stop datetime','76','Data Type'),('651','negation rationale','77','Data Type'),('654','start datetime','77','Data Type'),('655','stop datetime','77','Data Type'),('662','Health Record Field','','Data Flow'),('663','laterality','7','Data Type'),('664','reason','13','Data Type'),('665','discharge status','22','Data Type'),('669','facility location arrival datetime','95','Data Type'),('67','severity','9','Data Type'),('670','facility location departure datetime','95','Data Type'),('671','facility location arrival datetime','22','Data Type'),('672','facility location departure datetime','22','Data Type'),('673','reason','39','Data Type'),('676','ordinality','62','Data Type'),('677','ordinality','63','Data Type'),('678','result','63','Data Type'),('679','incision datetime','63','Data Type'),('68','start datetime','9','Data Type'),('680','ordinality','88','Data Type'),('685','result','65','Data Type'),('689','related to','3','Data Type'),('70','stop datetime','9','Data Type'),('701','Source','','Data Flow'),('702','Recorder','','Data Flow'),('73','ordinality','10','Data Type'),('76','severity','10','Data Type'),('77','start datetime','10','Data Type'),('79','stop datetime','10','Data Type'),('84','reaction','11','Data Type'),('85','start datetime','11','Data Type'),('86','stop datetime','11','Data Type'),('91','reaction','12','Data Type'),('92','start datetime','12','Data Type'),('93','stop datetime','12','Data Type'),('95','negation rationale','13','Data Type'),('98','start datetime','13','Data Type'),('99','removal datetime','13','Data Type');
/*!40000 ALTER TABLE `QDM_ATTRIBUTES_BACKUP_SEP2015` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QDM_TERM`
--

DROP TABLE IF EXISTS `QDM_TERM`;
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
-- Dumping data for table `QDM_TERM`
--

LOCK TABLES `QDM_TERM` WRITE;
/*!40000 ALTER TABLE `QDM_TERM` DISABLE KEYS */;
/*!40000 ALTER TABLE `QDM_TERM` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QUALITY_DATA_MODEL`
--

DROP TABLE IF EXISTS `QUALITY_DATA_MODEL`;
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
-- Dumping data for table `QUALITY_DATA_MODEL`
--

LOCK TABLES `QUALITY_DATA_MODEL` WRITE;
/*!40000 ALTER TABLE `QUALITY_DATA_MODEL` DISABLE KEYS */;
/*!40000 ALTER TABLE `QUALITY_DATA_MODEL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QUALITY_DATA_MODEL_OID_GEN`
--

DROP TABLE IF EXISTS `QUALITY_DATA_MODEL_OID_GEN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QUALITY_DATA_MODEL_OID_GEN` (
  `OID_GEN_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`OID_GEN_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QUALITY_DATA_MODEL_OID_GEN`
--

LOCK TABLES `QUALITY_DATA_MODEL_OID_GEN` WRITE;
/*!40000 ALTER TABLE `QUALITY_DATA_MODEL_OID_GEN` DISABLE KEYS */;
/*!40000 ALTER TABLE `QUALITY_DATA_MODEL_OID_GEN` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RECENT_CQL_ACTIVITY_LOG`
--

DROP TABLE IF EXISTS `RECENT_CQL_ACTIVITY_LOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RECENT_CQL_ACTIVITY_LOG` (
  `ID` varchar(32) NOT NULL,
  `CQL_ID` varchar(32) NOT NULL,
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
-- Dumping data for table `RECENT_CQL_ACTIVITY_LOG`
--

LOCK TABLES `RECENT_CQL_ACTIVITY_LOG` WRITE;
/*!40000 ALTER TABLE `RECENT_CQL_ACTIVITY_LOG` DISABLE KEYS */;
/*!40000 ALTER TABLE `RECENT_CQL_ACTIVITY_LOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RECENT_MSR_ACTIVITY_LOG`
--

DROP TABLE IF EXISTS `RECENT_MSR_ACTIVITY_LOG`;
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
-- Dumping data for table `RECENT_MSR_ACTIVITY_LOG`
--

LOCK TABLES `RECENT_MSR_ACTIVITY_LOG` WRITE;
/*!40000 ALTER TABLE `RECENT_MSR_ACTIVITY_LOG` DISABLE KEYS */;
/*!40000 ALTER TABLE `RECENT_MSR_ACTIVITY_LOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SECURITY_QUESTIONS`
--

DROP TABLE IF EXISTS `SECURITY_QUESTIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SECURITY_QUESTIONS` (
  `QUESTION_ID` int(2) NOT NULL,
  `QUESTION` varchar(100) NOT NULL,
  PRIMARY KEY (`QUESTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SECURITY_QUESTIONS`
--

LOCK TABLES `SECURITY_QUESTIONS` WRITE;
/*!40000 ALTER TABLE `SECURITY_QUESTIONS` DISABLE KEYS */;
INSERT INTO `SECURITY_QUESTIONS` VALUES (1,'What was your dream job as a child?'),(2,'What is your preferred musical genre?'),(3,'What is the name of your favorite childhood friend?'),(4,'What was the make of your first car?'),(5,'In what city or town was your first job?'),(6,'What was the name of your elementary / primary school?'),(7,'What school did you attend for sixth grade?'),(8,'What was the first sport you ever played as a child?');
/*!40000 ALTER TABLE `SECURITY_QUESTIONS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SECURITY_ROLE`
--

DROP TABLE IF EXISTS `SECURITY_ROLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SECURITY_ROLE` (
  `SECURITY_ROLE_ID` varchar(32) NOT NULL,
  `DESCRIPTION` varchar(50) NOT NULL,
  PRIMARY KEY (`SECURITY_ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SECURITY_ROLE`
--

LOCK TABLES `SECURITY_ROLE` WRITE;
/*!40000 ALTER TABLE `SECURITY_ROLE` DISABLE KEYS */;
INSERT INTO `SECURITY_ROLE` VALUES ('1','Administrator'),('2','Top Level User'),('3','User');
/*!40000 ALTER TABLE `SECURITY_ROLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SHARE_LEVEL`
--

DROP TABLE IF EXISTS `SHARE_LEVEL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SHARE_LEVEL` (
  `SHARE_LEVEL_ID` varchar(32) NOT NULL,
  `DESCRIPTION` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`SHARE_LEVEL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SHARE_LEVEL`
--

LOCK TABLES `SHARE_LEVEL` WRITE;
/*!40000 ALTER TABLE `SHARE_LEVEL` DISABLE KEYS */;
INSERT INTO `SHARE_LEVEL` VALUES ('1','View Only'),('2','Modify');
/*!40000 ALTER TABLE `SHARE_LEVEL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `STATUS`
--

DROP TABLE IF EXISTS `STATUS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `STATUS` (
  `STATUS_ID` varchar(32) NOT NULL,
  `DESCRIPTION` varchar(50) NOT NULL,
  PRIMARY KEY (`STATUS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `STATUS`
--

LOCK TABLES `STATUS` WRITE;
/*!40000 ALTER TABLE `STATUS` DISABLE KEYS */;
INSERT INTO `STATUS` VALUES ('1','Active'),('2','Revoked');
/*!40000 ALTER TABLE `STATUS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `STEWARD_ORG`
--

DROP TABLE IF EXISTS `STEWARD_ORG`;
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
-- Dumping data for table `STEWARD_ORG`
--

LOCK TABLES `STEWARD_ORG` WRITE;
/*!40000 ALTER TABLE `STEWARD_ORG` DISABLE KEYS */;
INSERT INTO `STEWARD_ORG` VALUES ('14','American Medical Association-convened Physician Consortium for Performance Improvement(R) (AMA-PCPI)','2.16.840.1.113883.3.526'),('15','Centers for Medicare & Medicaid Services','2.16.840.1.113883.3.560.3.31'),('29','Cleveland Clinic','2.16.840.1.114222.4.1.213632'),('55','National Committee for Quality Assurance','2.16.840.1.113883.3.464'),('80','National Quality Forum','2.16.840.1.113883.3.560'),('81','Joint Commission','1.3.6.1.4.1.33895'),('82','Oklahoma Foundation for Medical Quality','2.16.840.1.113883'),('83','American Board of Internal Medicine','2.16.840.1.113883.3.797'),('84','Kaiser Permanente','1.3.6.1.4.1.26580'),('85','Other',NULL),('86','National Library of Medicine','2.16.840.1.113883.1.11.1'),('87','CDC NCHS','2.16.840.1.114222.4.11.836'),('88','PHDSC','2.16.840.1.113883.221.5');
/*!40000 ALTER TABLE `STEWARD_ORG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TRANSACTION_AUDIT_LOG`
--

DROP TABLE IF EXISTS `TRANSACTION_AUDIT_LOG`;
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
-- Dumping data for table `TRANSACTION_AUDIT_LOG`
--

LOCK TABLES `TRANSACTION_AUDIT_LOG` WRITE;
/*!40000 ALTER TABLE `TRANSACTION_AUDIT_LOG` DISABLE KEYS */;
INSERT INTO `TRANSACTION_AUDIT_LOG` VALUES ('8ae455fa4de40f05014de41120f00001',NULL,NULL,'ADMIN_ACCT_TAB_EVENT','Admin','2015-06-11 19:20:37','[Admin] adminAccountTab0'),('8ae455fa4de40f05014de41120f00002',NULL,NULL,'LOGIN_EVENT','Admin','2015-06-11 19:20:37','[Admin] null'),('8ae455fa4de40f05014de41121320003',NULL,NULL,'MAIN_TAB_EVENT','Admin','2015-06-11 19:20:37','[Admin] mainTab0'),('8ae455fa4de40f05014de41135260004',NULL,NULL,'SIGN_OUT_EVENT','Admin','2015-06-11 19:20:42','[Admin]'),('8ae455fa4de40f05014de41162d20005',NULL,NULL,'LOGIN_EVENT','Admin','2015-06-11 19:20:54','[Admin] null'),('8ae455fa4de40f05014de41162df0006',NULL,NULL,'ADMIN_ACCT_TAB_EVENT','Admin','2015-06-11 19:20:54','[Admin] adminAccountTab0'),('8ae455fa4de40f05014de41162f40007',NULL,NULL,'MAIN_TAB_EVENT','Admin','2015-06-11 19:20:54','[Admin] mainTab0'),('8ae455fa4de40f05014de4116d020008',NULL,NULL,'SIGN_OUT_EVENT','Admin','2015-06-11 19:20:56','[Admin]');
/*!40000 ALTER TABLE `TRANSACTION_AUDIT_LOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UNIT`
--

DROP TABLE IF EXISTS `UNIT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UNIT` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  `SORT_ORDER` int(4) NOT NULL DEFAULT '0',
  `CQL_UNIT` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UNIT`
--

LOCK TABLES `UNIT` WRITE;
/*!40000 ALTER TABLE `UNIT` DISABLE KEYS */;
INSERT INTO `UNIT` VALUES ('1','seconds',4,'seconds'),('10','bpm',19,'{beats}/min'),('11','cm',20,'cm'),('12','dL',22,'dL'),('13','eq',23,'eq'),('14','g',24,'g'),('15','kg',25,'kg'),('16','Liter',26,'L'),('17','mEq',27,'meq'),('18','mg',28,'mg'),('19','mg/dL',29,'mg/dL'),('2','minutes',6,'minutes'),('20','mL',30,'mL'),('21','mm',31,'mm'),('22','mmHg',32,'mm[Hg]'),('23','mmol/L',33,'mmol/L'),('24','ng/dL',34,'ng/dL'),('25','kg/m2',36,'kg/m2'),('26','RAD',38,'RAD'),('27','per mm3',37,'/mm3'),('28','copies/mL',21,'{copies}/mL'),('29','ng/mL',35,'ng/mL'),('3','hours',8,'hours'),('30','IU',39,'[iU]'),('31','IU/L',40,'[iU]/L'),('32','U/L',41,'U/L'),('33','AU',42,'[AU]'),('34','BAU',43,'[BAU]'),('35','millisecond',1,'millisecond'),('36','milliseconds',2,'milliseconds'),('37','second',3,'second'),('38','minute',5,'minute'),('39','hour',7,'hour'),('4','days',10,'days'),('40','day',9,'day'),('41','week',11,'week'),('42','month',13,'month'),('43','year',15,'year'),('5','weeks',12,'weeks'),('6','months',14,'months'),('7','years',16,'years'),('8','%',17,'%'),('9','celsius',18,'Cel');
/*!40000 ALTER TABLE `UNIT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UNIT_TYPE`
--

DROP TABLE IF EXISTS `UNIT_TYPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `UNIT_TYPE` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UNIT_TYPE`
--

LOCK TABLES `UNIT_TYPE` WRITE;
/*!40000 ALTER TABLE `UNIT_TYPE` DISABLE KEYS */;
INSERT INTO `UNIT_TYPE` VALUES ('1','Function'),('2','Comparison'),('3','TemporalComparison'),('4','Attribute');
/*!40000 ALTER TABLE `UNIT_TYPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UNIT_TYPE_MATRIX`
--

DROP TABLE IF EXISTS `UNIT_TYPE_MATRIX`;
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
-- Dumping data for table `UNIT_TYPE_MATRIX`
--

LOCK TABLES `UNIT_TYPE_MATRIX` WRITE;
/*!40000 ALTER TABLE `UNIT_TYPE_MATRIX` DISABLE KEYS */;
INSERT INTO `UNIT_TYPE_MATRIX` VALUES ('1','1','1'),('10','10','1'),('11','11','1'),('12','12','1'),('13','13','1'),('14','14','1'),('15','15','1'),('16','16','1'),('17','17','1'),('18','18','1'),('19','19','1'),('2','2','1'),('20','20','1'),('21','21','1'),('22','22','1'),('23','23','1'),('24','24','1'),('25','1','2'),('26','2','2'),('27','3','2'),('28','4','2'),('29','5','2'),('3','3','1'),('30','6','2'),('31','7','2'),('32','1','3'),('33','2','3'),('34','3','3'),('35','4','3'),('36','5','3'),('37','6','3'),('38','7','3'),('39','1','4'),('4','4','1'),('40','2','4'),('41','3','4'),('42','4','4'),('43','5','4'),('44','6','4'),('45','7','4'),('46','8','4'),('47','9','4'),('48','10','4'),('49','11','4'),('5','5','1'),('50','12','4'),('51','13','4'),('52','14','4'),('53','15','4'),('54','16','4'),('55','17','4'),('56','18','4'),('57','19','4'),('58','20','4'),('59','21','4'),('6','6','1'),('60','22','4'),('61','23','4'),('62','24','4'),('63','25','1'),('64','26','1'),('65','25','4'),('66','26','4'),('67','27','1'),('68','27','4'),('69','28','1'),('7','7','1'),('70','28','4'),('71','29','1'),('72','29','4'),('73','30','1'),('74','30','4'),('75','31','1'),('76','31','4'),('77','32','1'),('78','32','4'),('79','33','1'),('8','8','1'),('80','33','4'),('81','34','1'),('82','34','4'),('83','35','1'),('84','36','1'),('85','37','1'),('86','38','1'),('87','39','1'),('88','40','1'),('89','41','1'),('9','9','1'),('90','42','1'),('91','43','1');
/*!40000 ALTER TABLE `UNIT_TYPE_MATRIX` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER`
--

DROP TABLE IF EXISTS `USER`;
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
  `ORG_ID` int(11) NOT NULL,
  `SESSION_ID` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `LOGIN_ID_UNIQUE` (`LOGIN_ID`),
  KEY `USER_SECURITY_ROLE_FK` (`SECURITY_ROLE_ID`),
  KEY `USER_AUDIT_FK` (`AUDIT_ID`),
  KEY `USER_STATUS_FK` (`STATUS_ID`),
  KEY `ORG_ID_FK_idx` (`ORG_ID`),
  CONSTRAINT `ORG_ID_FK` FOREIGN KEY (`ORG_ID`) REFERENCES `ORGANIZATION` (`ORG_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `USER_AUDIT_FK` FOREIGN KEY (`AUDIT_ID`) REFERENCES `AUDIT_LOG` (`AUDIT_LOG_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `USER_SECURITY_ROLE_FK` FOREIGN KEY (`SECURITY_ROLE_ID`) REFERENCES `SECURITY_ROLE` (`SECURITY_ROLE_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `USER_STATUS_FK` FOREIGN KEY (`STATUS_ID`) REFERENCES `STATUS` (`STATUS_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER`
--

LOCK TABLES `USER` WRITE;
/*!40000 ALTER TABLE `USER` DISABLE KEYS */;
INSERT INTO `USER` VALUES ('Admin','Admin',NULL,'user','Admin','999-999-9999',NULL,NULL,'2015-06-11','2015-06-11 19:20:54','2015-06-11 19:20:56',NULL,'1','1','1','Aduser0001',1,NULL);
/*!40000 ALTER TABLE `USER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_AUDIT_LOG`
--

DROP TABLE IF EXISTS `USER_AUDIT_LOG`;
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
-- Dumping data for table `USER_AUDIT_LOG`
--

LOCK TABLES `USER_AUDIT_LOG` WRITE;
/*!40000 ALTER TABLE `USER_AUDIT_LOG` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_AUDIT_LOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_BACKUP`
--

DROP TABLE IF EXISTS `USER_BACKUP`;
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
-- Dumping data for table `USER_BACKUP`
--

LOCK TABLES `USER_BACKUP` WRITE;
/*!40000 ALTER TABLE `USER_BACKUP` DISABLE KEYS */;
INSERT INTO `USER_BACKUP` VALUES ('Admin','Admin',NULL,'user','Admin','999-999-9999',NULL,NULL,'2015-06-11','2010-10-28 20:08:27','2010-10-28 20:09:27',NULL,'1','1','1','Telligen','2.16.840.1.113883.3.67','2.16.840.1.113883.3.67.1.101.1');
/*!40000 ALTER TABLE `USER_BACKUP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_BACKUP_FOR_ORGANIZATION`
--

DROP TABLE IF EXISTS `USER_BACKUP_FOR_ORGANIZATION`;
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
-- Dumping data for table `USER_BACKUP_FOR_ORGANIZATION`
--

LOCK TABLES `USER_BACKUP_FOR_ORGANIZATION` WRITE;
/*!40000 ALTER TABLE `USER_BACKUP_FOR_ORGANIZATION` DISABLE KEYS */;
INSERT INTO `USER_BACKUP_FOR_ORGANIZATION` VALUES ('Admin','Admin',NULL,'user','Admin','999-999-9999',NULL,NULL,'2015-06-11','2010-10-28 20:08:27','2010-10-28 20:09:27',NULL,'1','1','1','Telligen','2.16.840.1.113883.3.67','Aduser0001');
/*!40000 ALTER TABLE `USER_BACKUP_FOR_ORGANIZATION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_BACKUP_FOR_ROOTID`
--

DROP TABLE IF EXISTS `USER_BACKUP_FOR_ROOTID`;
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
-- Dumping data for table `USER_BACKUP_FOR_ROOTID`
--

LOCK TABLES `USER_BACKUP_FOR_ROOTID` WRITE;
/*!40000 ALTER TABLE `USER_BACKUP_FOR_ROOTID` DISABLE KEYS */;
INSERT INTO `USER_BACKUP_FOR_ROOTID` VALUES ('Admin','Admin',NULL,'user','Admin','999-999-9999',NULL,NULL,'2015-06-11','2010-10-28 20:08:27','2010-10-28 20:09:27',NULL,'1','1','1','Telligen','2.16.840.1.113883.3.67','2.16.840.1.113883.3.67.1.101.1','Aduser0001');
/*!40000 ALTER TABLE `USER_BACKUP_FOR_ROOTID` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_BONNIE_ACCESS_INFO`
--

DROP TABLE IF EXISTS `USER_BONNIE_ACCESS_INFO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_BONNIE_ACCESS_INFO` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` varchar(40) NOT NULL,
  `REFRESH_TOKEN` varchar(250) NOT NULL,
  `ACCESS_TOKEN` varchar(250) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `USER_ID` (`USER_ID`),
  CONSTRAINT `user_bonnie_access_info_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_BONNIE_ACCESS_INFO`
--

LOCK TABLES `USER_BONNIE_ACCESS_INFO` WRITE;
/*!40000 ALTER TABLE `USER_BONNIE_ACCESS_INFO` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_BONNIE_ACCESS_INFO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_PASSWORD`
--

DROP TABLE IF EXISTS `USER_PASSWORD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_PASSWORD` (
  `USER_PASSWORD_ID` varchar(32) NOT NULL,
  `USER_ID` varchar(40) NOT NULL,
  `PWD_LOCK_COUNTER` int(11) DEFAULT NULL,
  `FORGOT_PWD_LOCK_COUNTER` int(11) DEFAULT NULL,
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
-- Dumping data for table `USER_PASSWORD`
--

LOCK TABLES `USER_PASSWORD` WRITE;
/*!40000 ALTER TABLE `USER_PASSWORD` DISABLE KEYS */;
INSERT INTO `USER_PASSWORD` VALUES ('1','Admin',0,0,'8a06ddf2a3da6e7d558c91951fb48d3f5787906904724b25d23ff161f92b1e70','d0dccfba-9178-4466-9a1a-981023b721a9',1,sysdate(),NULL,0);
/*!40000 ALTER TABLE `USER_PASSWORD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_PASSWORD_HISTORY`
--

DROP TABLE IF EXISTS `USER_PASSWORD_HISTORY`;
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
-- Dumping data for table `USER_PASSWORD_HISTORY`
--

LOCK TABLES `USER_PASSWORD_HISTORY` WRITE;
/*!40000 ALTER TABLE `USER_PASSWORD_HISTORY` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_PASSWORD_HISTORY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_PASSWORD_TEMP`
--

DROP TABLE IF EXISTS `USER_PASSWORD_TEMP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_PASSWORD_TEMP` (
  `USER_PASSWORD_ID` varchar(32) NOT NULL,
  `USER_ID` varchar(40) NOT NULL,
  `PWD_LOCK_COUNTER` int(11) DEFAULT NULL,
  `FORGOT_PWD_LOCK_COUNTER` int(11) DEFAULT NULL,
  `PASSWORD` varchar(100) NOT NULL,
  `SALT` varchar(100) NOT NULL,
  `INITIAL_PWD` tinyint(1) DEFAULT '0',
  `CREATE_DATE` date NOT NULL,
  `FIRST_FAILED_ATTEMPT_TIME` timestamp NULL DEFAULT NULL,
  `TEMP_PWD` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_PASSWORD_TEMP`
--

LOCK TABLES `USER_PASSWORD_TEMP` WRITE;
/*!40000 ALTER TABLE `USER_PASSWORD_TEMP` DISABLE KEYS */;
INSERT INTO `USER_PASSWORD_TEMP` VALUES ('1','Admin',0,0,'c5190eed6feded32643bd04be40660df09b8eb046887e283a5eb093f3ccf7499','63fde2e4-236f-4b24-bfed-1e0eea874d76',0,sysdate(),NULL,0);
/*!40000 ALTER TABLE `USER_PASSWORD_TEMP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_PREFERENCE`
--

DROP TABLE IF EXISTS `USER_PREFERENCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_PREFERENCE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` varchar(40) NOT NULL,
  `FREE_TEXT_EDITOR_ENABLED` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `USER_PREFERENCE_USER_ID_FK` (`USER_ID`),
  CONSTRAINT `USER_PREFERENCE_USER_ID_FK` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_PREFERENCE`
--

LOCK TABLES `USER_PREFERENCE` WRITE;
/*!40000 ALTER TABLE `USER_PREFERENCE` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_PREFERENCE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_REVOKE_ORG_CHANGE_BKP`
--

DROP TABLE IF EXISTS `USER_REVOKE_ORG_CHANGE_BKP`;
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
  `ORG_ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_REVOKE_ORG_CHANGE_BKP`
--

LOCK TABLES `USER_REVOKE_ORG_CHANGE_BKP` WRITE;
/*!40000 ALTER TABLE `USER_REVOKE_ORG_CHANGE_BKP` DISABLE KEYS */;
INSERT INTO `USER_REVOKE_ORG_CHANGE_BKP` VALUES ('Admin','Admin',NULL,'user','Admin','999-999-9999',NULL,NULL,'2015-06-11','2010-10-28 20:08:27','2010-10-28 20:09:27',NULL,'1','1','1','Aduser0001',1);
/*!40000 ALTER TABLE `USER_REVOKE_ORG_CHANGE_BKP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_SECURITY_QUESTIONS`
--

DROP TABLE IF EXISTS `USER_SECURITY_QUESTIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_SECURITY_QUESTIONS` (
  `USER_SECURITY_QUESTIONS_ID` int(5) NOT NULL AUTO_INCREMENT,
  `USER_ID` varchar(40) NOT NULL,
  `ROW_ID` int(11) NOT NULL,
  `ANSWER` varchar(100) DEFAULT NULL,
  `QUESTION_ID` int(2) DEFAULT NULL,
  `SALT` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`USER_SECURITY_QUESTIONS_ID`),
  KEY `SECURITY_QUES_USER_FK` (`USER_ID`),
  KEY `FK_SECURITY_QUESTIONS` (`QUESTION_ID`),
  CONSTRAINT `FK_SECURITY_QUESTIONS` FOREIGN KEY (`QUESTION_ID`) REFERENCES `SECURITY_QUESTIONS` (`QUESTION_ID`),
  CONSTRAINT `SECURITY_QUES_USER_FK` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_SECURITY_QUESTIONS`
--

LOCK TABLES `USER_SECURITY_QUESTIONS` WRITE;
/*!40000 ALTER TABLE `USER_SECURITY_QUESTIONS` DISABLE KEYS */;
INSERT INTO `USER_SECURITY_QUESTIONS` VALUES (1,'Admin',0,'child',1,NULL),(2,'Admin',1,'genre',2,NULL),(3,'Admin',2,'friend',3,NULL);
/*!40000 ALTER TABLE `USER_SECURITY_QUESTIONS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_SECURITY_QUESTIONS_BACKUP_SEPT2015`
--

DROP TABLE IF EXISTS `USER_SECURITY_QUESTIONS_BACKUP_SEPT2015`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_SECURITY_QUESTIONS_BACKUP_SEPT2015` (
  `USER_ID` varchar(40) NOT NULL,
  `ROW_ID` int(11) NOT NULL,
  `ANSWER` varchar(100) DEFAULT NULL,
  `QUESTION_ID` int(2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_SECURITY_QUESTIONS_BACKUP_SEPT2015`
--

LOCK TABLES `USER_SECURITY_QUESTIONS_BACKUP_SEPT2015` WRITE;
/*!40000 ALTER TABLE `USER_SECURITY_QUESTIONS_BACKUP_SEPT2015` DISABLE KEYS */;
INSERT INTO `USER_SECURITY_QUESTIONS_BACKUP_SEPT2015` VALUES ('Admin',0,'child',1),('Admin',1,'genre',2),('Admin',2,'friend',3);
/*!40000 ALTER TABLE `USER_SECURITY_QUESTIONS_BACKUP_SEPT2015` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_SECURITY_QUESTIONS_BCKUP`
--

DROP TABLE IF EXISTS `USER_SECURITY_QUESTIONS_BCKUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_SECURITY_QUESTIONS_BCKUP` (
  `USER_ID` varchar(40) NOT NULL,
  `ROW_ID` int(11) NOT NULL,
  `QUESTION` varchar(100) NOT NULL,
  `ANSWER` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_SECURITY_QUESTIONS_BCKUP`
--

LOCK TABLES `USER_SECURITY_QUESTIONS_BCKUP` WRITE;
/*!40000 ALTER TABLE `USER_SECURITY_QUESTIONS_BCKUP` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_SECURITY_QUESTIONS_BCKUP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_SECURITY_QUESTIONS_TEMP`
--

DROP TABLE IF EXISTS `USER_SECURITY_QUESTIONS_TEMP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `USER_SECURITY_QUESTIONS_TEMP` (
  `USER_ID` varchar(40) NOT NULL,
  `ROW_ID` int(11) NOT NULL,
  `QUESTION` varchar(100) NOT NULL,
  `ANSWER` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_SECURITY_QUESTIONS_TEMP`
--

LOCK TABLES `USER_SECURITY_QUESTIONS_TEMP` WRITE;
/*!40000 ALTER TABLE `USER_SECURITY_QUESTIONS_TEMP` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_SECURITY_QUESTIONS_TEMP` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-18 16:17:20
