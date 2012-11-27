-- MySQL dump 10.13  Distrib 5.5.8, for Win32 (x86)
--
-- Host: localhost    Database: mat_app
-- ------------------------------------------------------
-- Server version	5.5.8

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `attribute_details`
--

DROP TABLE IF EXISTS `attribute_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attribute_details` (
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
-- Dumping data for table `attribute_details`
--

LOCK TABLES `attribute_details` WRITE;
/*!40000 ALTER TABLE `attribute_details` DISABLE KEYS */;
INSERT INTO `attribute_details` VALUES ('1','anatomical location','91723000','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('10','negation rationale','','','','','REFR'),('11','number','107651007','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('12','ordinality','117363000','2.16.840.1.113883.6.96','SNOMED-CT','S','SUBJ'),('13','patient preference','PAT','2.16.840.1.113883.5.8','HL7 ActReason','S','RSON'),('14','provider preference','103323008','2.16.840.1.113883.6.96','SNOMED-CT','S','RSON'),('15','radiationdosage','218190002','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('16','radiationduration','218190002','2.16.840.1.113883.6.96','SNOMED-CT','S(I)/S(I)','REFR'),('17','reaction','ASSERTION','2.16.840.1.113883.5.4','Clinical Observation Assertion','S','MFST'),('18','reason','410666004','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('19','refills','','','','S','REFR'),('2','cumulativeMedicationDuration','363819003','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('20','result','385676005','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('21','route','263513008','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('22','severity','SEV','2.16.840.1.113883.5.4','HL7 Severity observation','S','SUBJ'),('23','start datetime','','','','I',''),('24','status','33999-4','2.16.840.1.113883.6.1','LOINC','S','REFR'),('25','stop datetime','','','','I',''),('26','Source - Device','','','','P',''),('27','Source - Informant','','','','P',''),('28','Recorder - Informant','','','','P',''),('29','Recorder - Device','','','','P',''),('3','dose','398232005','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('30','Setting','','','','P',''),('31','Health Record Field','','','','AC',''),('4','duration','','','','S(I)/S(I)','REFR'),('5','durationfromarrival','','','','S(I)/P(I)','REFR'),('6','frequency','260864003','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR'),('7','hospital location','','','','P','REFR'),('8','infusionDuration','36576007','2.16.840.1.113883.6.96','SNOMED-CT','S(I)/S(I)','REFR'),('9','method','414679000','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR');
/*!40000 ALTER TABLE `attribute_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_log`
--

DROP TABLE IF EXISTS `audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_log` (
  `AUDIT_LOG_ID` varchar(32) NOT NULL,
  `CREATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `CREATE_USER` varchar(40) NOT NULL,
  `UPDATE_DATE` timestamp NULL DEFAULT NULL,
  `UPDATE_USER` varchar(40) DEFAULT NULL,
  `ACTIVITY_TYPE` varchar(32) DEFAULT NULL,
  `MEASURE_ID` varchar(32) DEFAULT NULL,
  `LIST_OBJECT_ID` varchar(32) DEFAULT NULL,
  `CLAUSE_ID` varchar(32) DEFAULT NULL,
  `QDS_ID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`AUDIT_LOG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_log`
--

LOCK TABLES `audit_log` WRITE;
/*!40000 ALTER TABLE `audit_log` DISABLE KEYS */;
INSERT INTO `audit_log` VALUES ('1','2010-10-28 20:08:27','Admin',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('8ae452962e3a223a012e3a2547880002','2011-02-18 19:00:45','Admin',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('8ae452962e3a223a012e3a254ab50005','2011-02-18 19:00:45','Admin',NULL,NULL,'Insert',NULL,'8ae452962e3a223a012e3a254aa60004',NULL,NULL),('8ae452962e3a223a012e3a254b130007','2011-02-18 19:00:45','Admin',NULL,NULL,'Insert',NULL,'8ae452962e3a223a012e3a254b130006',NULL,NULL),('8ae452962e3a223a012e3a254b800009','2011-02-18 19:00:46','Admin',NULL,NULL,'Insert',NULL,'8ae452962e3a223a012e3a254b800008',NULL,NULL),('8ae452962e3a223a012e3a37cf63000b','2011-02-18 19:20:59','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert','8ae452962e3a223a012e3a37cf63000a',NULL,NULL,NULL),('8ae452962e3a3b8f012e3a3d39830002','2011-02-18 19:26:54','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,'8ae452962e3a3b8f012e3a3d39640001',NULL,NULL),('8ae452962e3a3b8f012e3a3ea0480005','2011-02-18 19:28:26','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,'8ae452962e3a3b8f012e3a3ea0480004',NULL,NULL),('8ae452962e3a3b8f012e3a3f62120008','2011-02-18 19:29:15','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,'8ae452962e3a3b8f012e3a3f62120007',NULL,NULL),('8ae452962e3a3b8f012e3a40268c000b','2011-02-18 19:30:06','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,'8ae452962e3a3b8f012e3a40268c000a',NULL,NULL),('8ae452962e3a3b8f012e3a41202f000e','2011-02-18 19:31:09','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,'8ae452962e3a3b8f012e3a41202f000d',NULL,NULL),('8ae452962e3a3b8f012e3a41b54b0011','2011-02-18 19:31:48','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,'8ae452962e3a3b8f012e3a41b54b0010',NULL,NULL),('8ae452962e3a3b8f012e3a424d750014','2011-02-18 19:32:27','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,'8ae452962e3a3b8f012e3a424d750013',NULL,NULL),('8ae452962e3a3b8f012e3a440d470017','2011-02-18 19:34:21','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a440d470016'),('8ae452962e3a3b8f012e3a442ce50019','2011-02-18 19:34:29','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a442ce50018'),('8ae452962e3a3b8f012e3a444cd1001b','2011-02-18 19:34:38','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a444cd1001a'),('8ae452962e3a3b8f012e3a447af5001d','2011-02-18 19:34:49','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a447af5001c'),('8ae452962e3a3b8f012e3a44893e001f','2011-02-18 19:34:53','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a44893e001e'),('8ae452962e3a3b8f012e3a449ae10021','2011-02-18 19:34:58','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a449ae10020'),('8ae452962e3a3b8f012e3a44bbc70023','2011-02-18 19:35:06','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a44bbc70022'),('8ae452962e3a3b8f012e3a44c9350025','2011-02-18 19:35:09','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a44c9350024'),('8ae452962e3a3b8f012e3a44e19f0027','2011-02-18 19:35:16','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a44e19f0026'),('8ae452962e3a3b8f012e3a44f8340029','2011-02-18 19:35:21','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a44f8340028'),('8ae452962e3a3b8f012e3a451949002b','2011-02-18 19:35:30','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a451949002a'),('8ae452962e3a3b8f012e3a452b2b002d','2011-02-18 19:35:34','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a452b2b002c'),('8ae452962e3a3b8f012e3a4541b1002f','2011-02-18 19:35:40','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a4541b1002e'),('8ae452962e3a3b8f012e3a4555780031','2011-02-18 19:35:45','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a4555780030'),('8ae452962e3a3b8f012e3a4560b30033','2011-02-18 19:35:48','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a4560b30032'),('8ae452962e3a3b8f012e3a4572560035','2011-02-18 19:35:53','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a4572560034'),('8ae452962e3a3b8f012e3a4582b20037','2011-02-18 19:35:57','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a4582b20036'),('8ae452962e3a3b8f012e3a458fb20039','2011-02-18 19:36:00','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a458fb20038'),('8ae452962e59a64f012e59a8af31000c','2011-02-24 21:52:30','8ae452962e3a223a012e3a2547880001','2011-02-24 21:52:30','8ae452962e3a223a012e3a2547880001','Update','8ae452962e3a223a012e3a37cf63000a',NULL,NULL,NULL),('8ae452962eb48426012eb486c4db0013','2011-03-14 13:20:54','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962eb48426012eb486c4cc0012',NULL),('8ae452962eb48426012eb486c51a0015','2011-03-14 13:20:54','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962eb48426012eb486c51a0014',NULL),('8ae452962eb48426012eb486c5390017','2011-03-14 13:20:54','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962eb48426012eb486c5390016',NULL),('8ae452962eb48426012eb486c5580019','2011-03-14 13:20:54','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962eb48426012eb486c5580018',NULL),('8ae452962eb48426012eb486c587001b','2011-03-14 13:20:54','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962eb48426012eb486c587001a',NULL),('8ae452962eb48426012eb486c604001d','2011-03-14 13:20:54','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962eb48426012eb486c5e5001c',NULL);
/*!40000 ALTER TABLE `audit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `author`
--

DROP TABLE IF EXISTS `author`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `author` (
  `ID` varchar(32) NOT NULL,
  `AUTHOR_NAME` varchar(200) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `author`
--

LOCK TABLES `author` WRITE;
/*!40000 ALTER TABLE `author` DISABLE KEYS */;
INSERT INTO `author` VALUES ('14','American Medical Association - Physician Consortium for Performance Improvement'),('29','Cleveland Clinic'),('55','National Committee for Quality Assurance'),('80','Other'),('81','National Quality Forum'),('82','Joint Commission'),('83','Oklahoma Foundation for Medical Quality'),('84','American Board of Internal Medicine');
/*!40000 ALTER TABLE `author` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `CATEGORY_ID` varchar(32) NOT NULL,
  `DESCRIPTION` varchar(50) NOT NULL,
  `ABBREVIATION` varchar(50) NOT NULL,
  PRIMARY KEY (`CATEGORY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES ('1','Care Experience','EXP'),('10','Intervention','INT'),('11','Laboratory Test','LAB'),('12','Medication','MED'),('14','Physical Exam','PE'),('16','Procedure','PRC'),('17','Risk Category/Assessment','RSK'),('18','Substance','SUB'),('19','Symptom','SX'),('2','Care Plan','PLN'),('20','System Characteristic','SYS'),('21','Transfer of Care','TRN'),('22','Measure Timing','TMG'),('23','Attribute','ATT'),('3','Communication','COM'),('4','Condition/Diagnosis/Problem','CDP'),('5','Device','DEV'),('6','Diagnostic Study','DXS'),('7','Encounter','ENC'),('8','Functional Status','FXS'),('9','Individual Characteristic','IND');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clause`
--

DROP TABLE IF EXISTS `clause`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clause` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `CUSTOM_NAME` varchar(100) DEFAULT NULL,
  `DESCRIPTION` varchar(2000) DEFAULT NULL,
  `MEASURE_ID` varchar(64) DEFAULT NULL,
  `CONTEXT_ID` varchar(32) DEFAULT NULL,
  `DECISION_ID` varchar(64) DEFAULT NULL,
  `CLAUSE_TYPE_ID` varchar(32) DEFAULT NULL,
  `STATUS_ID` varchar(32) DEFAULT NULL,
  `VERSION` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clause`
--

LOCK TABLES `clause` WRITE;
/*!40000 ALTER TABLE `clause` DISABLE KEYS */;
INSERT INTO `clause` VALUES ('8ae452962eb48426012eb486c4cc0012','3rd Dream_Population1',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','1','0b824bdd-9f37-4155-8e46-11ae5420fdfe',NULL,NULL,NULL),('8ae452962eb48426012eb486c51a0014','3rd Dream_Numerator1',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','2','60a94c23-0121-4824-8b71-abf3f38d378b',NULL,NULL,NULL),('8ae452962eb48426012eb486c5390016','3rd Dream_Denominator1',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','3','269d28d9-22b4-43b3-869b-7e5ae15ca33f',NULL,NULL,NULL),('8ae452962eb48426012eb486c5580018','3rd Dream_Exclusions1',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','4','8382ea5b-7903-4e4a-9d9c-90c23d293a71',NULL,NULL,NULL),('8ae452962eb48426012eb486c587001a','3rd Dream_Exceptions1',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','5','314c5364-d46d-412f-9583-8b33b9c05be9',NULL,NULL,NULL),('8ae452962eb48426012eb486c5e5001c','3rd Dream_User-defined1',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','6','05917b30-3a66-44d4-b292-9a8ed8da5e9f',NULL,NULL,NULL);
/*!40000 ALTER TABLE `clause` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `code`
--

DROP TABLE IF EXISTS `code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `code` (
  `CODE_ID` varchar(32) NOT NULL,
  `CODE` varchar(32) NOT NULL,
  `DESCRIPTION` varchar(1400) NOT NULL,
  `CODE_LIST_ID` varchar(32) NOT NULL,
  PRIMARY KEY (`CODE_ID`),
  KEY `CODE_LIST_FK` (`CODE_LIST_ID`),
  CONSTRAINT `CODE_LIST_FK` FOREIGN KEY (`CODE_LIST_ID`) REFERENCES `code_list` (`CODE_LIST_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `code`
--

LOCK TABLES `code` WRITE;
/*!40000 ALTER TABLE `code` DISABLE KEYS */;
INSERT INTO `code` VALUES ('8ae452962e3a3b8f012e3a3db22e0003','00000041','encounterCode1','8ae452962e3a3b8f012e3a3d39640001'),('8ae452962e3a3b8f012e3a3ec3320006','00000042','medicationCode1','8ae452962e3a3b8f012e3a3ea0480004'),('8ae452962e3a3b8f012e3a3f9da40009','00000043','diagnosticStudyCode','8ae452962e3a3b8f012e3a3f62120007'),('8ae452962e3a3b8f012e3a405d7a000c','00000044','substanceCode1','8ae452962e3a3b8f012e3a40268c000a'),('8ae452962e3a3b8f012e3a415307000f','00000045','attributeCode1','8ae452962e3a3b8f012e3a41202f000d'),('8ae452962e3a3b8f012e3a41f6aa0012','00000046','attributeCode2','8ae452962e3a3b8f012e3a41b54b0010'),('8ae452962e3a3b8f012e3a42761b0015','00000047','attributeCode3','8ae452962e3a3b8f012e3a424d750013');
/*!40000 ALTER TABLE `code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `code_list`
--

DROP TABLE IF EXISTS `code_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `code_list` (
  `CODE_LIST_ID` varchar(32) NOT NULL,
  PRIMARY KEY (`CODE_LIST_ID`),
  KEY `CODE_LIST_OBJECT_FK` (`CODE_LIST_ID`),
  CONSTRAINT `CODE_LIST_OBJECT_FK` FOREIGN KEY (`CODE_LIST_ID`) REFERENCES `list_object` (`LIST_OBJECT_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `code_list`
--

LOCK TABLES `code_list` WRITE;
/*!40000 ALTER TABLE `code_list` DISABLE KEYS */;
INSERT INTO `code_list` VALUES ('8ae452962e3a223a012e3a254aa60004'),('8ae452962e3a223a012e3a254b130006'),('8ae452962e3a223a012e3a254b800008'),('8ae452962e3a3b8f012e3a3d39640001'),('8ae452962e3a3b8f012e3a3ea0480004'),('8ae452962e3a3b8f012e3a3f62120007'),('8ae452962e3a3b8f012e3a40268c000a'),('8ae452962e3a3b8f012e3a41202f000d'),('8ae452962e3a3b8f012e3a41b54b0010'),('8ae452962e3a3b8f012e3a424d750013');
/*!40000 ALTER TABLE `code_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `code_system`
--

DROP TABLE IF EXISTS `code_system`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `code_system` (
  `CODE_SYSTEM_ID` varchar(32) NOT NULL,
  `DESCRIPTION` varchar(50) NOT NULL,
  `CATEGORY_ID` varchar(32) NOT NULL,
  `ABBREVIATION` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`CODE_SYSTEM_ID`),
  KEY `CODE_SYS_CAT_FK` (`CATEGORY_ID`),
  CONSTRAINT `CODE_SYS_CAT_FK` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `category` (`CATEGORY_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `code_system`
--

LOCK TABLES `code_system` WRITE;
/*!40000 ALTER TABLE `code_system` DISABLE KEYS */;
INSERT INTO `code_system` VALUES ('1','SNOMED','1','SNM'),('10','I9','4','I9'),('11','I10','4','I10'),('12','SNOMED','5','SNM'),('13','I9','5','I9'),('14','I10','5','I10'),('15','SNOMED','6','SNM'),('16','LOINC','6','LNC'),('17','CPT','6','CPT'),('18','HCPCS','6','HCP'),('19','SNOMED','7','SNM'),('2','SNOMED','2','SNM'),('20','LOINC','7','LNC'),('21','CPT','7','CPT'),('22','HCPCS','7','HCP'),('23','SNOMED','8','SNM'),('24','I9','8','I9'),('25','I10','8','I10'),('26','HL7','9','HL7'),('27','SNOMED','10','SNM'),('28','LOINC','10','LNC'),('29','I9','10','I9'),('3','I9','2','I9'),('30','I10','10','I10'),('31','CPT','10','CPT'),('32','HCPCS','10','HCP'),('33','SNOMED','11','SNM'),('34','LOINC','11','LNC'),('35','RxNorm','12','RxN'),('38','SNOMED','14','SNM'),('39','LOINC','14','LNC'),('4','I10','2','I10'),('41','SNOMED','16','SNM'),('42','I9','16','I9'),('43','I10','16','I10'),('44','CPT','16','CPT'),('45','HCPCS','16','HCP'),('46','SNOMED','17','SNM'),('47','I9','17','I9'),('48','I10','17','I10'),('49','SNOMED','18','SNM'),('5','SNOMED','3','SNM'),('50','I9','18','I9'),('51','I10','18','I10'),('52','SNOMED','19','SNM'),('53','I9','19','I9'),('54','I10','19','I10'),('55','SNOMED','20','SNM'),('56','HL7','20','HL7'),('57','SNOMED','21','SNM'),('58','I9','21','I9'),('59','I10','21','I10'),('6','I9','3','I9'),('60','NQF','22','NQF'),('61','I9','23','I9'),('62','I10','23','I10'),('63','SNOMED','23','SNM'),('64','CPT','23','CPT'),('65','LOINC','23','LNC'),('66','HCPCS','23','HCP'),('67','RXNORM','23','RxN'),('68','HL7','23','HL7'),('69','SNOMED','9','SNM'),('7','I10','3','I10'),('70','Grouping','3','GRP'),('71','Grouping','4','GRP'),('72','Grouping','5','GRP'),('73','Grouping','6','GRP'),('74','Grouping','7','GRP'),('75','Grouping','8','GRP'),('76','Grouping','9','GRP'),('77','Grouping','10','GRP'),('78','Grouping','11','GRP'),('79','Grouping','12','GRP'),('8','CPT','3','CPT'),('81','Grouping','14','GRP'),('83','Grouping','16','GRP'),('84','Grouping','17','GRP'),('85','Grouping','18','GRP'),('86','Grouping','19','GRP'),('87','Grouping','20','GRP'),('88','Grouping','21','GRP'),('89','Grouping','22','GRP'),('9','SNOMED','4','SNM'),('90','Grouping','23','GRP'),('91','Grouping','1','GRP'),('92','Grouping','2','GRP');
/*!40000 ALTER TABLE `code_system` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `context`
--

DROP TABLE IF EXISTS `context`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `context` (
  `CONTEXT_ID` varchar(64) NOT NULL,
  `DESCRIPTION` varchar(100) NOT NULL,
  PRIMARY KEY (`CONTEXT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `context`
--

LOCK TABLES `context` WRITE;
/*!40000 ALTER TABLE `context` DISABLE KEYS */;
INSERT INTO `context` VALUES ('1','Population'),('2','Numerator'),('3','Denominator'),('4','Exclusions'),('5','Exceptions'),('6','User-defined'),('7','Measure Phrase');
/*!40000 ALTER TABLE `context` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `data_type`
--

DROP TABLE IF EXISTS `data_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `data_type` (
  `DATA_TYPE_ID` varchar(32) NOT NULL,
  `DESCRIPTION` varchar(50) NOT NULL,
  `CATEGORY_ID` varchar(32) NOT NULL,
  PRIMARY KEY (`DATA_TYPE_ID`),
  KEY `DATA_TYPE_CAT_FK` (`CATEGORY_ID`),
  CONSTRAINT `DATA_TYPE_CAT_FK` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `category` (`CATEGORY_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `data_type`
--

LOCK TABLES `data_type` WRITE;
/*!40000 ALTER TABLE `data_type` DISABLE KEYS */;
INSERT INTO `data_type` VALUES ('1','Patient Care Experience','1'),('10','Diagnosis, Resolved','4'),('11','Device, Adverse Event','5'),('12','Device, Allergy','5'),('13','Device, Applied','5'),('14','Device, Intolerance','5'),('15','Device, Order','5'),('16','Diagnostic Study, Adverse Event','6'),('17','Diagnostic Study, Intolerance','6'),('18','Diagnostic Study, Order','6'),('19','Diagnostic Study, Performed','6'),('2','Provider Care Experience','1'),('20','Diagnostic Study, Result','6'),('21','Encounter, Order','7'),('22','Encounter, Performed','7'),('23','Functional Status, Order','8'),('24','Functional Status, Performed','8'),('25','Functional Status, Result','8'),('26','Patient Characteristic','9'),('27','Provider Characteristic','9'),('28','Intervention, Adverse Event','10'),('29','Intervention, Intolerance','10'),('3','Care Plan','2'),('30','Intervention, Order','10'),('31','Intervention, Performed','10'),('32','Intervention, Result','10'),('33','Laboratory Test, Adverse Event','11'),('34','Laboratory Test, Intolerance','11'),('35','Laboratory Test, Order','11'),('36','Laboratory Test, Performed','11'),('37','Laboratory Test, Result','11'),('38','Medication, Active','12'),('39','Medication, Administered','12'),('4','Communication: From Provider to Provider','3'),('40','Medication, Adverse Effects','12'),('41','Medication, Allergy','12'),('42','Medication, Dispensed','12'),('43','Medication, Intolerance','12'),('44','Medication, Order','12'),('5','Communication: From Patient to Provider','3'),('55','Physical Exam, Finding','14'),('56','Physical Exam, Order','14'),('57','Physical Exam, Performed','14'),('6','Communication: From Provider to Patient','3'),('60','Procedure, Adverse Event','16'),('61','Procedure, Intolerance','16'),('62','Procedure, Order','16'),('63','Procedure, Performed','16'),('64','Procedure, Result','16'),('65','Risk Category Assessment','17'),('66','Substance, Administered','18'),('67','Substance, Adverse Event','18'),('68','Substance, Allergy','18'),('69','Substance, Intolerance','18'),('7','Diagnosis, Active','4'),('70','Substance, Order','18'),('71','Symptom, Active','19'),('72','Symptom, Assessed','19'),('73','Symptom, Inactive','19'),('74','Symptom, Resolved','19'),('75','System Characteristic','20'),('76','Transfer From','21'),('77','Transfer To','21'),('78','Device, Recommended','5'),('79','Encounter, Recommended','7'),('8','Diagnosis, Family History','4'),('80','Functional Status, Recommended','8'),('81','Intervention, Recommended','10'),('82','Laboratory Test, Recommended','11'),('87','Physical Exam, Recommended','14'),('88','Procedure, Recommended','16'),('89','Substance, Recommended','18'),('9','Diagnosis, Inactive','4'),('90','Diagnostic Study, Recommended','6'),('91','start of timing window','22'),('92','attribute','23'),('93','end of timing window','22'),('94','Encounter','7'),('95','Encounter, Active','7');
/*!40000 ALTER TABLE `data_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `databasechangelog`
--

DROP TABLE IF EXISTS `databasechangelog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `databasechangelog` (
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
  PRIMARY KEY (`ID`,`AUTHOR`,`FILENAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `databasechangelog`
--

LOCK TABLES `databasechangelog` WRITE;
/*!40000 ALTER TABLE `databasechangelog` DISABLE KEYS */;
INSERT INTO `databasechangelog` VALUES ('1','mat_dev_user','classpath:/liquibase/deploy_01.00.05_20110303.xml','2011-03-09 13:53:21',1,'EXECUTED','3:626ecb5e80ec84f99b180ba89b641f08','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.06_20110308.xml','2011-03-09 13:53:21',2,'EXECUTED','3:711ff533c7b970ed32fdbccc9f26f1ae','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.07_20110308.xml','2011-03-09 13:53:21',3,'EXECUTED','3:9d0b71411447374472c4d76cf309d5ca','Custom SQL','',NULL,'2.0.1');
/*!40000 ALTER TABLE `databasechangelog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `databasechangeloglock`
--

DROP TABLE IF EXISTS `databasechangeloglock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `databasechangeloglock` (
  `ID` int(11) NOT NULL,
  `LOCKED` tinyint(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `databasechangeloglock`
--

LOCK TABLES `databasechangeloglock` WRITE;
/*!40000 ALTER TABLE `databasechangeloglock` DISABLE KEYS */;
INSERT INTO `databasechangeloglock` VALUES (1,0,NULL,NULL);
/*!40000 ALTER TABLE `databasechangeloglock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `decision`
--

DROP TABLE IF EXISTS `decision`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `decision` (
  `ID` varchar(64) NOT NULL,
  `OPERATOR` varchar(45) NOT NULL,
  `PARENT_ID` varchar(64) DEFAULT NULL,
  `ORDER_NUM` varchar(32) DEFAULT NULL,
  `CLAUSE_ID` varchar(64) DEFAULT NULL,
  `ATTRIBUTE_ID` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `decision`
--

LOCK TABLES `decision` WRITE;
/*!40000 ALTER TABLE `decision` DISABLE KEYS */;
INSERT INTO `decision` VALUES ('05917b30-3a66-44d4-b292-9a8ed8da5e9f','CLAUSE',NULL,NULL,NULL,NULL),('0b824bdd-9f37-4155-8e46-11ae5420fdfe','CLAUSE',NULL,NULL,NULL,NULL),('0e31fadb-6b86-406f-8233-b3c3a2b712ed','AND','8382ea5b-7903-4e4a-9d9c-90c23d293a71','0',NULL,NULL),('1940d996-6ef5-4d74-85b1-e6c0cc425707','AND','60a94c23-0121-4824-8b71-abf3f38d378b','0',NULL,NULL),('269d28d9-22b4-43b3-869b-7e5ae15ca33f','CLAUSE',NULL,NULL,NULL,NULL),('314c5364-d46d-412f-9583-8b33b9c05be9','CLAUSE',NULL,NULL,NULL,NULL),('60a94c23-0121-4824-8b71-abf3f38d378b','CLAUSE',NULL,NULL,NULL,NULL),('779af57c-7c22-481a-9ac3-cd9649c178f5','AND','0b824bdd-9f37-4155-8e46-11ae5420fdfe','0',NULL,NULL),('8382ea5b-7903-4e4a-9d9c-90c23d293a71','CLAUSE',NULL,NULL,NULL,NULL),('9a232b3e-c580-4cf6-8e05-836c4f7abd4e','AND','05917b30-3a66-44d4-b292-9a8ed8da5e9f','0',NULL,NULL),('b25269a2-695a-4a43-ad56-c0ff55e0e283','AND','269d28d9-22b4-43b3-869b-7e5ae15ca33f','0',NULL,NULL),('b5f10192-f902-4578-8e28-9cc2e534daa8','AND','314c5364-d46d-412f-9583-8b33b9c05be9','0',NULL,NULL);
/*!40000 ALTER TABLE `decision` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grouped_code_lists`
--

DROP TABLE IF EXISTS `grouped_code_lists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grouped_code_lists` (
  `GROUPED_CODE_LISTS_ID` varchar(32) NOT NULL,
  `GROUP_LIST_ID` varchar(32) NOT NULL,
  `CODE_LIST_ID` varchar(32) NOT NULL,
  `DESCRIPTION` varchar(1000) NOT NULL,
  PRIMARY KEY (`GROUPED_CODE_LISTS_ID`),
  KEY `GR_CODE_LIST_FK` (`CODE_LIST_ID`),
  KEY `GR_LIST_OBJ_FK` (`GROUP_LIST_ID`),
  CONSTRAINT `GR_CODE_LIST_FK` FOREIGN KEY (`CODE_LIST_ID`) REFERENCES `code_list` (`CODE_LIST_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `GR_LIST_OBJ_FK` FOREIGN KEY (`GROUP_LIST_ID`) REFERENCES `list_object` (`LIST_OBJECT_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grouped_code_lists`
--

LOCK TABLES `grouped_code_lists` WRITE;
/*!40000 ALTER TABLE `grouped_code_lists` DISABLE KEYS */;
/*!40000 ALTER TABLE `grouped_code_lists` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `list_object`
--

DROP TABLE IF EXISTS `list_object`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `list_object` (
  `LIST_OBJECT_ID` varchar(32) NOT NULL,
  `VERSION` varchar(255) NOT NULL DEFAULT '0.1',
  `NAME` varchar(255) NOT NULL,
  `STEWARD` varchar(255) DEFAULT NULL,
  `OID` varchar(255) NOT NULL,
  `RATIONALE` varchar(2000) NOT NULL DEFAULT 'N/A',
  `COMMENT` varchar(2000) DEFAULT NULL,
  `OBJECT_STATUS_ID` varchar(32) NOT NULL,
  `OBJECT_OWNER` varchar(32) NOT NULL,
  `CATEGORY_ID` varchar(32) NOT NULL,
  `CODE_SYS_VERSION` varchar(255) NOT NULL,
  `CODE_SYSTEM_ID` varchar(32) NOT NULL,
  `MEASURE_ID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`LIST_OBJECT_ID`),
  UNIQUE KEY `OID_UC` (`OID`),
  KEY `LIST_OBJECT_STATUS_FK` (`OBJECT_STATUS_ID`),
  KEY `LIST_OBJECT_USER_FK` (`OBJECT_OWNER`),
  KEY `LIST_OBJECT_CAT_FK` (`CATEGORY_ID`),
  KEY `LIST_OBJECT_CODE_SYSTEM_FK` (`CODE_SYSTEM_ID`),
  KEY `LIST_OBJECT_STEWARD_FK` (`STEWARD`),
  KEY `LIST_OBJECT_MEASURE_FK` (`MEASURE_ID`),
  CONSTRAINT `LIST_OBJECT_CAT_FK` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `category` (`CATEGORY_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `LIST_OBJECT_CODE_SYSTEM_FK` FOREIGN KEY (`CODE_SYSTEM_ID`) REFERENCES `code_system` (`CODE_SYSTEM_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `LIST_OBJECT_MEASURE_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `measure` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `LIST_OBJECT_STATUS_FK` FOREIGN KEY (`OBJECT_STATUS_ID`) REFERENCES `object_status` (`OBJECT_STATUS_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `LIST_OBJECT_STEWARD_FK` FOREIGN KEY (`STEWARD`) REFERENCES `steward_org` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `LIST_OBJECT_USER_FK` FOREIGN KEY (`OBJECT_OWNER`) REFERENCES `user` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `list_object`
--

LOCK TABLES `list_object` WRITE;
/*!40000 ALTER TABLE `list_object` DISABLE KEYS */;
INSERT INTO `list_object` VALUES ('8ae452962e3a223a012e3a254aa60004','1','Measurement Period','80','1234567890.1','N/A',NULL,'2','8ae452962e3a223a012e3a2547880001','22','1','60',NULL),('8ae452962e3a223a012e3a254b130006','1','Measurement Start Date','80','1234567890.2','N/A',NULL,'2','8ae452962e3a223a012e3a2547880001','22','1','60',NULL),('8ae452962e3a223a012e3a254b800008','1','Measurement End Date','80','1234567890.3','N/A',NULL,'2','8ae452962e3a223a012e3a2547880001','22','1','60',NULL),('8ae452962e3a3b8f012e3a3d39640001','1','encounterCodeList','80','1234567890.4','N/A','','1','8ae452962e3a223a012e3a2547880001','7','1','21','8ae452962e3a223a012e3a37cf63000a'),('8ae452962e3a3b8f012e3a3ea0480004','1','medicationCodeList','80','1234567890.5','N/A','','1','8ae452962e3a223a012e3a2547880001','12','2','35','8ae452962e3a223a012e3a37cf63000a'),('8ae452962e3a3b8f012e3a3f62120007','1','diagnosticStudyCodeList','80','1234567890.6','N/A','','1','8ae452962e3a223a012e3a2547880001','6','3','16','8ae452962e3a223a012e3a37cf63000a'),('8ae452962e3a3b8f012e3a40268c000a','1','substanceCodeList1','14','1234567890.7','N/A','','1','8ae452962e3a223a012e3a2547880001','18','1','50','8ae452962e3a223a012e3a37cf63000a'),('8ae452962e3a3b8f012e3a41202f000d','1','attribute1','80','1234567890.8','N/A','','1','8ae452962e3a223a012e3a2547880001','23','1','64','8ae452962e3a223a012e3a37cf63000a'),('8ae452962e3a3b8f012e3a41b54b0010','1','attribute2','80','1234567890.9','N/A','','1','8ae452962e3a223a012e3a2547880001','23','2','63','8ae452962e3a223a012e3a37cf63000a'),('8ae452962e3a3b8f012e3a424d750013','1','attribute3','55','1234567890.10','N/A','','1','8ae452962e3a223a012e3a2547880001','23','3','65','8ae452962e3a223a012e3a37cf63000a');
/*!40000 ALTER TABLE `list_object` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `list_object_oid_gen`
--

DROP TABLE IF EXISTS `list_object_oid_gen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `list_object_oid_gen` (
  `LIST_OBJECT_OID_GEN_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`LIST_OBJECT_OID_GEN_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `list_object_oid_gen`
--

LOCK TABLES `list_object_oid_gen` WRITE;
/*!40000 ALTER TABLE `list_object_oid_gen` DISABLE KEYS */;
INSERT INTO `list_object_oid_gen` VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10);
/*!40000 ALTER TABLE `list_object_oid_gen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `measure`
--

DROP TABLE IF EXISTS `measure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `measure` (
  `ID` varchar(64) NOT NULL,
  `MEASURE_OWNER_ID` varchar(40) NOT NULL,
  `ABBR_NAME` varchar(45) DEFAULT NULL,
  `DESCRIPTION` varchar(2000) DEFAULT NULL,
  `VERSION` varchar(32) DEFAULT NULL,
  `MEASURE_STATUS` varchar(32) DEFAULT NULL,
  `EXPORT_TS` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `MEASURE_OWNER_FK` (`MEASURE_OWNER_ID`),
  CONSTRAINT `MEASURE_OWNER_FK` FOREIGN KEY (`MEASURE_OWNER_ID`) REFERENCES `user` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `measure`
--

LOCK TABLES `measure` WRITE;
/*!40000 ALTER TABLE `measure` DISABLE KEYS */;
INSERT INTO `measure` VALUES ('8ae452962e3a223a012e3a37cf63000a','8ae452962e3a223a012e3a2547880001','3rd Dream','Andrew Schmidt\'s 3rd Dream',NULL,'Complete',NULL);
/*!40000 ALTER TABLE `measure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `measure_export`
--

DROP TABLE IF EXISTS `measure_export`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `measure_export` (
  `MEASURE_EXPORT_ID` varchar(64) NOT NULL,
  `MEASURE_ID` varchar(64) NOT NULL,
  `SIMPLE_XML` longtext NOT NULL,
  PRIMARY KEY (`MEASURE_EXPORT_ID`),
  KEY `MEASURE_EXPORT_ID_MEASURE_FK` (`MEASURE_ID`),
  CONSTRAINT `MEASURE_EXPORT_ID_MEASURE_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `measure` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `measure_export`
--

LOCK TABLES `measure_export` WRITE;
/*!40000 ALTER TABLE `measure_export` DISABLE KEYS */;
/*!40000 ALTER TABLE `measure_export` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `measure_share`
--

DROP TABLE IF EXISTS `measure_share`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `measure_share` (
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
  CONSTRAINT `MEASURE_SHARE_MEASURE_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `measure` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `MEASURE_SHARE_OWNER_FK` FOREIGN KEY (`MEASURE_OWNER_USER_ID`) REFERENCES `user` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `MEASURE_SHARE_SHARE_FK` FOREIGN KEY (`SHARE_USER_ID`) REFERENCES `user` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `MEASURE_SHARE_LEVEL_ID` FOREIGN KEY (`SHARE_LEVEL_ID`) REFERENCES `share_level` (`SHARE_LEVEL_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `measure_share`
--

LOCK TABLES `measure_share` WRITE;
/*!40000 ALTER TABLE `measure_share` DISABLE KEYS */;
/*!40000 ALTER TABLE `measure_share` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `measure_types`
--

DROP TABLE IF EXISTS `measure_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `measure_types` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `measure_types`
--

LOCK TABLES `measure_types` WRITE;
/*!40000 ALTER TABLE `measure_types` DISABLE KEYS */;
INSERT INTO `measure_types` VALUES ('1','Composite'),('2','Cost/Resource Use'),('3','Efficiency'),('4','Outcome'),('5','Patient Engagement/Experience'),('6','Process'),('7','Structure');
/*!40000 ALTER TABLE `measure_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `measurement_term`
--

DROP TABLE IF EXISTS `measurement_term`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `measurement_term` (
  `ID` varchar(64) NOT NULL,
  `UNIT` varchar(45) DEFAULT NULL,
  `QUANTITY` varchar(100) NOT NULL,
  `DECISION_ID` varchar(64) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `measurement_term`
--

LOCK TABLES `measurement_term` WRITE;
/*!40000 ALTER TABLE `measurement_term` DISABLE KEYS */;
/*!40000 ALTER TABLE `measurement_term` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `metadata`
--

DROP TABLE IF EXISTS `metadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `metadata` (
  `METADATA_ID` varchar(64) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `VALUE` varchar(30000) NOT NULL,
  `MEASURE_ID` varchar(64) NOT NULL,
  PRIMARY KEY (`METADATA_ID`),
  KEY `MEASURE_ID_METADATA_FK` (`MEASURE_ID`),
  CONSTRAINT `MEASURE_ID_METADATA_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `measure` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `metadata`
--

LOCK TABLES `metadata` WRITE;
/*!40000 ALTER TABLE `metadata` DISABLE KEYS */;
INSERT INTO `metadata` VALUES ('8ae452962eb48426012eb486b50d0001','MeasureId','DF19F049-0578-4EA1-B6DA-ACD4F8F9BB34','8ae452962e3a223a012e3a37cf63000a'),('8ae452962eb48426012eb486b50d0002','Description','blah','8ae452962e3a223a012e3a37cf63000a'),('8ae452962eb48426012eb486b50d0003','MeasureToPeriod','12/31/2012','8ae452962e3a223a012e3a37cf63000a'),('8ae452962eb48426012eb486b50d0004','MeasureDuration','12Month(s)','8ae452962e3a223a012e3a37cf63000a'),('8ae452962eb48426012eb486b50d0005','AvailableDate','02/25/2011','8ae452962e3a223a012e3a37cf63000a'),('8ae452962eb48426012eb486b50d0006','MeasureSteward','American Board of Internal Medicine','8ae452962e3a223a012e3a37cf63000a'),('8ae452962eb48426012eb486b50d0007','EndorseByNQF','true','8ae452962e3a223a012e3a37cf63000a'),('8ae452962eb48426012eb486b50d0008','Stratification','blah','8ae452962e3a223a012e3a37cf63000a'),('8ae452962eb48426012eb486b50d0009','MeasureScoring','Proportion','8ae452962e3a223a012e3a37cf63000a'),('8ae452962eb48426012eb486b50d000a','MeasureStatus','Complete','8ae452962e3a223a012e3a37cf63000a'),('8ae452962eb48426012eb486b50d000b','MeasureFromPeriod','01/01/2012','8ae452962e3a223a012e3a37cf63000a'),('8ae452962eb48426012eb486b50d000c','Name','Andrew Schmidt\'s 3rd Dream','8ae452962e3a223a012e3a37cf63000a'),('8ae452962eb48426012eb486b50d000d','ShortName','3rd Dream','8ae452962e3a223a012e3a37cf63000a'),('8ae452962eb48426012eb486b50d000e','Version','1','8ae452962e3a223a012e3a37cf63000a'),('8ae452962eb48426012eb486b51c000f','Rationale','blah','8ae452962e3a223a012e3a37cf63000a'),('8ae452962eb48426012eb486b51c0010','Author','Joint Commission','8ae452962e3a223a012e3a37cf63000a'),('8ae452962eb48426012eb486b51c0011','MeasureType','Outcome','8ae452962e3a223a012e3a37cf63000a');
/*!40000 ALTER TABLE `metadata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `object_status`
--

DROP TABLE IF EXISTS `object_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `object_status` (
  `OBJECT_STATUS_ID` varchar(32) NOT NULL,
  `DESCRIPTION` varchar(50) NOT NULL,
  PRIMARY KEY (`OBJECT_STATUS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `object_status`
--

LOCK TABLES `object_status` WRITE;
/*!40000 ALTER TABLE `object_status` DISABLE KEYS */;
INSERT INTO `object_status` VALUES ('1','InProgress'),('2','Complete');
/*!40000 ALTER TABLE `object_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operator`
--

DROP TABLE IF EXISTS `operator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operator` (
  `OPERATOR_ID` varchar(64) NOT NULL,
  `NAME` varchar(45) NOT NULL,
  `TYPE` varchar(45) NOT NULL,
  PRIMARY KEY (`OPERATOR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operator`
--

LOCK TABLES `operator` WRITE;
/*!40000 ALTER TABLE `operator` DISABLE KEYS */;
/*!40000 ALTER TABLE `operator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `packager`
--

DROP TABLE IF EXISTS `packager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `packager` (
  `PACKAGER_ID` varchar(32) NOT NULL,
  `MEASURE_ID` varchar(32) NOT NULL,
  `CLAUSE_ID` varchar(32) NOT NULL,
  `SEQUENCE` int(11) NOT NULL,
  PRIMARY KEY (`PACKAGER_ID`),
  KEY `PACKAGER_MEASURE_FK` (`MEASURE_ID`),
  KEY `PACKAGER_CLAUSE_FK` (`CLAUSE_ID`),
  CONSTRAINT `PACKAGER_MEASURE_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `measure` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `PACKAGER_CLAUSE_FK` FOREIGN KEY (`CLAUSE_ID`) REFERENCES `clause` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `packager`
--

LOCK TABLES `packager` WRITE;
/*!40000 ALTER TABLE `packager` DISABLE KEYS */;
/*!40000 ALTER TABLE `packager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qds_attribute_details`
--

DROP TABLE IF EXISTS `qds_attribute_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qds_attribute_details` (
  `ID` varchar(64) NOT NULL,
  `DECISION_ID` varchar(64) DEFAULT NULL,
  `QDS_ATTRIBUTE_ID` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qds_attribute_details`
--

LOCK TABLES `qds_attribute_details` WRITE;
/*!40000 ALTER TABLE `qds_attribute_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `qds_attribute_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qds_attributes`
--

DROP TABLE IF EXISTS `qds_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qds_attributes` (
  `ID` varchar(64) NOT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `DATA_TYPE_ID` varchar(32) DEFAULT NULL,
  `QDS_ATTRIBUTE_TYPE` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qds_attributes`
--

LOCK TABLES `qds_attributes` WRITE;
/*!40000 ALTER TABLE `qds_attributes` DISABLE KEYS */;
INSERT INTO `qds_attributes` VALUES ('10','provider preference','1','Data Type'),('100','duration','14','Data Type'),('101','negation rationale','14','Data Type'),('102','patient preference','14','Data Type'),('103','provider preference','14','Data Type'),('104','reaction','14','Data Type'),('105','start datetime','14','Data Type'),('106','stop datetime','14','Data Type'),('107','duration','15','Data Type'),('108','negation rationale','15','Data Type'),('109','patient preference','15','Data Type'),('11','start datetime','1','Data Type'),('110','provider preference','15','Data Type'),('111','reason','15','Data Type'),('112','start datetime','15','Data Type'),('113','stop datetime','15','Data Type'),('114','duration','78','Data Type'),('115','negation rationale','78','Data Type'),('116','patient preference','78','Data Type'),('117','provider preference','78','Data Type'),('118','reason','78','Data Type'),('119','start datetime','78','Data Type'),('12','stop datetime','1','Data Type'),('120','stop datetime','78','Data Type'),('121','duration','16','Data Type'),('122','negation rationale','16','Data Type'),('123','patient preference','16','Data Type'),('124','provider preference','16','Data Type'),('125','radiationdosage','16','Data Type'),('126','radiationduration','16','Data Type'),('127','reaction','16','Data Type'),('128','start datetime','16','Data Type'),('129','stop datetime','16','Data Type'),('13','duration','2','Data Type'),('130','duration','17','Data Type'),('131','negation rationale','17','Data Type'),('132','patient preference','17','Data Type'),('133','provider preference','17','Data Type'),('134','radiationdosage','17','Data Type'),('135','radiationduration','17','Data Type'),('136','reaction','17','Data Type'),('137','start datetime','17','Data Type'),('138','stop datetime','17','Data Type'),('139','duration','18','Data Type'),('14','negation rationale','2','Data Type'),('140','method','18','Data Type'),('141','negation rationale','18','Data Type'),('142','patient preference','18','Data Type'),('143','provider preference','18','Data Type'),('144','radiationdosage','18','Data Type'),('145','radiationduration','18','Data Type'),('146','reason','18','Data Type'),('147','start datetime','18','Data Type'),('148','stop datetime','18','Data Type'),('149','duration','19','Data Type'),('15','patient preference','2','Data Type'),('150','method','19','Data Type'),('151','negation rationale','19','Data Type'),('152','patient preference','19','Data Type'),('153','provider preference','19','Data Type'),('154','radiationdosage','19','Data Type'),('155','radiationduration','19','Data Type'),('156','reason','19','Data Type'),('157','start datetime','19','Data Type'),('158','stop datetime','19','Data Type'),('159','duration','90','Data Type'),('16','provider preference','2','Data Type'),('160','method','90','Data Type'),('161','negation rationale','90','Data Type'),('162','patient preference','90','Data Type'),('163','provider preference','90','Data Type'),('164','radiationdosage','90','Data Type'),('165','radiationduration','90','Data Type'),('166','start datetime','90','Data Type'),('167','stop datetime','90','Data Type'),('168','duration','20','Data Type'),('169','method','20','Data Type'),('17','start datetime','2','Data Type'),('170','negation rationale','20','Data Type'),('171','patient preference','20','Data Type'),('172','provider preference','20','Data Type'),('173','radiationdosage','20','Data Type'),('174','radiationduration','20','Data Type'),('175','reason','20','Data Type'),('176','result','20','Data Type'),('177','start datetime','20','Data Type'),('178','stop datetime','20','Data Type'),('179','duration','95','Data Type'),('18','stop datetime','2','Data Type'),('180','durationfromarrival','95','Data Type'),('181','hospital location','95','Data Type'),('182','negation rationale','95','Data Type'),('183','patient preference','95','Data Type'),('184','provider preference','95','Data Type'),('185','reason','95','Data Type'),('186','start datetime','95','Data Type'),('187','stop datetime','95','Data Type'),('188','duration','94','Data Type'),('189','durationfromarrival','94','Data Type'),('19','duration','3','Data Type'),('190','hospital location','94','Data Type'),('191','negation rationale','94','Data Type'),('192','patient preference','94','Data Type'),('193','provider preference','94','Data Type'),('194','reason','94','Data Type'),('195','start datetime','94','Data Type'),('196','stop datetime','94','Data Type'),('197','duration','21','Data Type'),('198','hospital location','21','Data Type'),('199','negation rationale','21','Data Type'),('20','negation rationale','3','Data Type'),('200','patient preference','21','Data Type'),('201','provider preference','21','Data Type'),('202','reason','21','Data Type'),('203','start datetime','21','Data Type'),('204','stop datetime','21','Data Type'),('205','duration','22','Data Type'),('206','durationfromarrival','22','Data Type'),('207','hospital location','22','Data Type'),('208','negation rationale','22','Data Type'),('209','patient preference','22','Data Type'),('21','patient preference','3','Data Type'),('210','provider preference','22','Data Type'),('211','reason','22','Data Type'),('212','start datetime','22','Data Type'),('213','stop datetime','22','Data Type'),('214','duration','79','Data Type'),('215','hospital location','79','Data Type'),('216','negation rationale','79','Data Type'),('217','patient preference','79','Data Type'),('218','provider preference','79','Data Type'),('219','reason','79','Data Type'),('22','provider preference','3','Data Type'),('220','start datetime','79','Data Type'),('221','stop datetime','79','Data Type'),('222','duration','23','Data Type'),('223','method','23','Data Type'),('224','negation rationale','23','Data Type'),('225','patient preference','23','Data Type'),('226','provider preference','23','Data Type'),('227','reason','23','Data Type'),('228','start datetime','23','Data Type'),('229','stop datetime','23','Data Type'),('23','start datetime','3','Data Type'),('230','duration','24','Data Type'),('231','method','24','Data Type'),('232','negation rationale','24','Data Type'),('233','patient preference','24','Data Type'),('234','provider preference','24','Data Type'),('235','reason','24','Data Type'),('236','start datetime','24','Data Type'),('237','stop datetime','24','Data Type'),('238','duration','80','Data Type'),('239','method','80','Data Type'),('24','stop datetime','3','Data Type'),('240','negation rationale','80','Data Type'),('241','patient preference','80','Data Type'),('242','provider preference','80','Data Type'),('243','reason','80','Data Type'),('244','start datetime','80','Data Type'),('245','stop datetime','80','Data Type'),('246','duration','25','Data Type'),('247','method','25','Data Type'),('248','negation rationale','25','Data Type'),('249','patient preference','25','Data Type'),('25','duration','5','Data Type'),('250','provider preference','25','Data Type'),('251','reason','25','Data Type'),('252','result','25','Data Type'),('253','start datetime','25','Data Type'),('254','stop datetime','25','Data Type'),('255','duration','26','Data Type'),('256','start datetime','26','Data Type'),('257','stop datetime','26','Data Type'),('258','duration','27','Data Type'),('259','negation rationale','27','Data Type'),('26','negation rationale','5','Data Type'),('260','start datetime','27','Data Type'),('261','stop datetime','27','Data Type'),('262','duration','28','Data Type'),('263','negation rationale','28','Data Type'),('264','patient preference','28','Data Type'),('265','provider preference','28','Data Type'),('266','reaction','28','Data Type'),('267','start datetime','28','Data Type'),('268','stop datetime','28','Data Type'),('269','duration','29','Data Type'),('27','patient preference','5','Data Type'),('270','negation rationale','29','Data Type'),('271','patient preference','29','Data Type'),('272','provider preference','29','Data Type'),('273','reaction','29','Data Type'),('274','start datetime','29','Data Type'),('275','stop datetime','29','Data Type'),('276','duration','30','Data Type'),('277','method','30','Data Type'),('278','negation rationale','30','Data Type'),('279','patient preference','30','Data Type'),('28','provider preference','5','Data Type'),('280','provider preference','30','Data Type'),('281','reason','30','Data Type'),('282','start datetime','30','Data Type'),('283','stop datetime','30','Data Type'),('284','duration','31','Data Type'),('285','method','31','Data Type'),('286','negation rationale','31','Data Type'),('287','patient preference','31','Data Type'),('288','provider preference','31','Data Type'),('289','reason','31','Data Type'),('29','start datetime','5','Data Type'),('290','start datetime','31','Data Type'),('291','stop datetime','31','Data Type'),('292','duration','81','Data Type'),('293','method','81','Data Type'),('294','negation rationale','81','Data Type'),('295','patient preference','81','Data Type'),('296','provider preference','81','Data Type'),('297','reason','81','Data Type'),('298','start datetime','81','Data Type'),('299','stop datetime','81','Data Type'),('30','stop datetime','5','Data Type'),('300','duration','32','Data Type'),('301','method','32','Data Type'),('302','negation rationale','32','Data Type'),('303','patient preference','32','Data Type'),('304','provider preference','32','Data Type'),('305','reason','32','Data Type'),('306','result','32','Data Type'),('307','start datetime','32','Data Type'),('308','stop datetime','32','Data Type'),('309','duration','33','Data Type'),('31','duration','6','Data Type'),('310','negation rationale','33','Data Type'),('311','patient preference','33','Data Type'),('312','provider preference','33','Data Type'),('313','reaction','33','Data Type'),('314','start datetime','33','Data Type'),('315','stop datetime','33','Data Type'),('316','duration','34','Data Type'),('317','negation rationale','34','Data Type'),('318','patient preference','34','Data Type'),('319','provider preference','34','Data Type'),('32','negation rationale','6','Data Type'),('320','reaction','34','Data Type'),('321','start datetime','34','Data Type'),('322','stop datetime','34','Data Type'),('323','duration','35','Data Type'),('324','method','35','Data Type'),('325','negation rationale','35','Data Type'),('326','patient preference','35','Data Type'),('327','provider preference','35','Data Type'),('328','reason','35','Data Type'),('329','start datetime','35','Data Type'),('33','patient preference','6','Data Type'),('330','stop datetime','35','Data Type'),('331','duration','36','Data Type'),('332','method','36','Data Type'),('333','negation rationale','36','Data Type'),('334','patient preference','36','Data Type'),('335','provider preference','36','Data Type'),('336','reason','36','Data Type'),('337','start datetime','36','Data Type'),('338','stop datetime','36','Data Type'),('339','duration','82','Data Type'),('34','provider preference','6','Data Type'),('340','method','82','Data Type'),('341','negation rationale','82','Data Type'),('342','patient preference','82','Data Type'),('343','provider preference','82','Data Type'),('344','reason','82','Data Type'),('345','start datetime','82','Data Type'),('346','stop datetime','82','Data Type'),('347','duration','37','Data Type'),('348','method','37','Data Type'),('349','negation rationale','37','Data Type'),('35','start datetime','6','Data Type'),('350','patient preference','37','Data Type'),('351','provider preference','37','Data Type'),('352','reason','37','Data Type'),('353','result','37','Data Type'),('354','start datetime','37','Data Type'),('355','stop datetime','37','Data Type'),('356','cumulativeMedicationDuration','38','Data Type'),('357','dose','38','Data Type'),('358','frequency','38','Data Type'),('359','infusionDuration','38','Data Type'),('36','stop datetime','6','Data Type'),('360','negation rationale','38','Data Type'),('361','number','38','Data Type'),('362','patient preference','38','Data Type'),('363','provider preference','38','Data Type'),('364','refills','38','Data Type'),('365','route','38','Data Type'),('366','start datetime','38','Data Type'),('367','stop datetime','38','Data Type'),('368','dose','39','Data Type'),('369','frequency','39','Data Type'),('37','duration','4','Data Type'),('370','infusionDuration','39','Data Type'),('371','negation rationale','39','Data Type'),('372','number','39','Data Type'),('373','patient preference','39','Data Type'),('374','provider preference','39','Data Type'),('375','refills','39','Data Type'),('376','route','39','Data Type'),('377','start datetime','39','Data Type'),('378','stop datetime','39','Data Type'),('379','dose','40','Data Type'),('38','negation rationale','4','Data Type'),('380','frequency','40','Data Type'),('381','infusionDuration','40','Data Type'),('382','negation rationale','40','Data Type'),('383','number','40','Data Type'),('384','patient preference','40','Data Type'),('385','provider preference','40','Data Type'),('386','reaction','40','Data Type'),('387','refills','40','Data Type'),('388','route','40','Data Type'),('389','start datetime','40','Data Type'),('39','patient preference','4','Data Type'),('390','stop datetime','40','Data Type'),('391','dose','41','Data Type'),('392','frequency','41','Data Type'),('393','infusionDuration','41','Data Type'),('394','negation rationale','41','Data Type'),('395','number','41','Data Type'),('396','patient preference','41','Data Type'),('397','provider preference','41','Data Type'),('398','reaction','41','Data Type'),('399','refills','41','Data Type'),('40','provider preference','4','Data Type'),('400','route','41','Data Type'),('401','start datetime','41','Data Type'),('402','stop datetime','41','Data Type'),('403','cumulativeMedicationDuration','42','Data Type'),('404','dose','42','Data Type'),('405','frequency','42','Data Type'),('406','infusionDuration','42','Data Type'),('407','negation rationale','42','Data Type'),('408','number','42','Data Type'),('409','patient preference','42','Data Type'),('41','start datetime','4','Data Type'),('410','provider preference','42','Data Type'),('411','refills','42','Data Type'),('412','route','42','Data Type'),('413','start datetime','42','Data Type'),('414','stop datetime','42','Data Type'),('415','dose','43','Data Type'),('416','frequency','43','Data Type'),('417','infusionDuration','43','Data Type'),('418','negation rationale','43','Data Type'),('419','number','43','Data Type'),('42','stop datetime','4','Data Type'),('420','patient preference','43','Data Type'),('421','provider preference','43','Data Type'),('422','reaction','43','Data Type'),('423','refills','43','Data Type'),('424','route','43','Data Type'),('425','start datetime','43','Data Type'),('426','stop datetime','43','Data Type'),('427','cumulativeMedicationDuration','44','Data Type'),('428','dose','44','Data Type'),('429','frequency','44','Data Type'),('43','duration','7','Data Type'),('430','infusionDuration','44','Data Type'),('431','method','44','Data Type'),('432','negation rationale','44','Data Type'),('433','number','44','Data Type'),('434','patient preference','44','Data Type'),('435','provider preference','44','Data Type'),('436','reason','44','Data Type'),('437','refills','44','Data Type'),('438','route','44','Data Type'),('439','start datetime','44','Data Type'),('44','negation rationale','7','Data Type'),('440','stop datetime','44','Data Type'),('441','anatomical location','55','Data Type'),('442','duration','55','Data Type'),('443','method','55','Data Type'),('444','negation rationale','55','Data Type'),('445','patient preference','55','Data Type'),('446','provider preference','55','Data Type'),('447','reason','55','Data Type'),('448','result','55','Data Type'),('449','start datetime','55','Data Type'),('45','ordinality','7','Data Type'),('450','stop datetime','55','Data Type'),('451','anatomical location','56','Data Type'),('452','duration','56','Data Type'),('453','method','56','Data Type'),('454','negation rationale','56','Data Type'),('455','patient preference','56','Data Type'),('456','provider preference','56','Data Type'),('457','reason','56','Data Type'),('458','start datetime','56','Data Type'),('459','stop datetime','56','Data Type'),('46','patient preference','7','Data Type'),('460','anatomical location','57','Data Type'),('461','duration','57','Data Type'),('462','method','57','Data Type'),('463','negation rationale','57','Data Type'),('464','patient preference','57','Data Type'),('465','provider preference','57','Data Type'),('466','reason','57','Data Type'),('467','start datetime','57','Data Type'),('468','stop datetime','57','Data Type'),('469','anatomical location','87','Data Type'),('47','provider preference','7','Data Type'),('470','duration','87','Data Type'),('471','method','87','Data Type'),('472','negation rationale','87','Data Type'),('473','patient preference','87','Data Type'),('474','provider preference','87','Data Type'),('475','reason','87','Data Type'),('476','start datetime','87','Data Type'),('477','stop datetime','87','Data Type'),('478','duration','60','Data Type'),('479','negation rationale','60','Data Type'),('48','severity','7','Data Type'),('480','patient preference','60','Data Type'),('481','provider preference','60','Data Type'),('482','reaction','60','Data Type'),('483','start datetime','60','Data Type'),('484','stop datetime','60','Data Type'),('485','duration','61','Data Type'),('486','negation rationale','61','Data Type'),('487','patient preference','61','Data Type'),('488','provider preference','61','Data Type'),('489','reaction','61','Data Type'),('49','start datetime','7','Data Type'),('490','start datetime','61','Data Type'),('491','stop datetime','61','Data Type'),('492','duration','62','Data Type'),('493','method','62','Data Type'),('494','negation rationale','62','Data Type'),('495','patient preference','62','Data Type'),('496','provider preference','62','Data Type'),('497','reason','62','Data Type'),('498','start datetime','62','Data Type'),('499','stop datetime','62','Data Type'),('50','status','7','Data Type'),('500','duration','63','Data Type'),('501','method','63','Data Type'),('502','negation rationale','63','Data Type'),('503','patient preference','63','Data Type'),('504','provider preference','63','Data Type'),('505','reason','63','Data Type'),('506','start datetime','63','Data Type'),('507','stop datetime','63','Data Type'),('508','duration','88','Data Type'),('509','method','88','Data Type'),('510','negation rationale','88','Data Type'),('511','patient preference','88','Data Type'),('512','provider preference','88','Data Type'),('513','reason','88','Data Type'),('514','start datetime','88','Data Type'),('515','stop datetime','88','Data Type'),('516','duration','64','Data Type'),('517','method','64','Data Type'),('518','negation rationale','64','Data Type'),('519','patient preference','64','Data Type'),('52','stop datetime','7','Data Type'),('520','provider preference','64','Data Type'),('521','reason','64','Data Type'),('522','result','64','Data Type'),('523','start datetime','64','Data Type'),('524','stop datetime','64','Data Type'),('525','duration','65','Data Type'),('526','negation rationale','65','Data Type'),('527','patient preference','65','Data Type'),('528','provider preference','65','Data Type'),('529','start datetime','65','Data Type'),('53','duration','8','Data Type'),('530','stop datetime','65','Data Type'),('531','dose','66','Data Type'),('532','duration','66','Data Type'),('533','frequency','66','Data Type'),('534','negation rationale','66','Data Type'),('535','number','66','Data Type'),('536','patient preference','66','Data Type'),('537','provider preference','66','Data Type'),('538','refills','66','Data Type'),('539','route','66','Data Type'),('54','negation rationale','8','Data Type'),('540','start datetime','66','Data Type'),('541','stop datetime','66','Data Type'),('542','dose','67','Data Type'),('543','duration','67','Data Type'),('544','frequency','67','Data Type'),('545','negation rationale','67','Data Type'),('546','number','67','Data Type'),('547','patient preference','67','Data Type'),('548','provider preference','67','Data Type'),('549','reaction','67','Data Type'),('55','ordinality','8','Data Type'),('550','refills','67','Data Type'),('551','route','67','Data Type'),('552','start datetime','67','Data Type'),('553','stop datetime','67','Data Type'),('554','dose','68','Data Type'),('555','duration','68','Data Type'),('556','frequency','68','Data Type'),('557','negation rationale','68','Data Type'),('558','number','68','Data Type'),('559','patient preference','68','Data Type'),('56','patient preference','8','Data Type'),('560','provider preference','68','Data Type'),('561','reaction','68','Data Type'),('562','refills','68','Data Type'),('563','route','68','Data Type'),('564','start datetime','68','Data Type'),('565','stop datetime','68','Data Type'),('566','dose','69','Data Type'),('567','duration','69','Data Type'),('568','frequency','69','Data Type'),('569','negation rationale','69','Data Type'),('57','provider preference','8','Data Type'),('570','number','69','Data Type'),('571','patient preference','69','Data Type'),('572','provider preference','69','Data Type'),('573','reaction','69','Data Type'),('574','refills','69','Data Type'),('575','route','69','Data Type'),('576','start datetime','69','Data Type'),('577','stop datetime','69','Data Type'),('578','dose','70','Data Type'),('579','duration','70','Data Type'),('58','severity','8','Data Type'),('580','frequency','70','Data Type'),('581','method','70','Data Type'),('582','negation rationale','70','Data Type'),('583','number','70','Data Type'),('584','patient preference','70','Data Type'),('585','provider preference','70','Data Type'),('586','reason','70','Data Type'),('587','refills','70','Data Type'),('588','route','70','Data Type'),('589','start datetime','70','Data Type'),('59','start datetime','8','Data Type'),('590','stop datetime','70','Data Type'),('591','dose','89','Data Type'),('592','duration','89','Data Type'),('593','frequency','89','Data Type'),('594','method','89','Data Type'),('595','negation rationale','89','Data Type'),('596','number','89','Data Type'),('597','patient preference','89','Data Type'),('598','provider preference','89','Data Type'),('599','reason','89','Data Type'),('60','status','8','Data Type'),('600','refills','89','Data Type'),('601','route','89','Data Type'),('602','start datetime','89','Data Type'),('603','stop datetime','89','Data Type'),('604','duration','71','Data Type'),('605','negation rationale','71','Data Type'),('606','ordinality','71','Data Type'),('607','patient preference','71','Data Type'),('608','provider preference','71','Data Type'),('609','severity','71','Data Type'),('61','stop datetime','8','Data Type'),('610','start datetime','71','Data Type'),('611','status','71','Data Type'),('612','stop datetime','71','Data Type'),('613','duration','72','Data Type'),('614','negation rationale','72','Data Type'),('615','ordinality','72','Data Type'),('616','patient preference','72','Data Type'),('617','provider preference','72','Data Type'),('618','severity','72','Data Type'),('619','start datetime','72','Data Type'),('62','duration','9','Data Type'),('620','status','72','Data Type'),('621','stop datetime','72','Data Type'),('622','duration','73','Data Type'),('623','negation rationale','73','Data Type'),('624','ordinality','73','Data Type'),('625','patient preference','73','Data Type'),('626','provider preference','73','Data Type'),('627','severity','73','Data Type'),('628','start datetime','73','Data Type'),('629','status','73','Data Type'),('63','negation rationale','9','Data Type'),('630','stop datetime','73','Data Type'),('631','duration','74','Data Type'),('632','negation rationale','74','Data Type'),('633','ordinality','74','Data Type'),('634','patient preference','74','Data Type'),('635','provider preference','74','Data Type'),('636','severity','74','Data Type'),('637','start datetime','74','Data Type'),('638','status','74','Data Type'),('639','stop datetime','74','Data Type'),('64','ordinality','9','Data Type'),('640','duration','75','Data Type'),('641','negation rationale','75','Data Type'),('642','start datetime','75','Data Type'),('643','stop datetime','75','Data Type'),('644','duration','76','Data Type'),('645','negation rationale','76','Data Type'),('646','patient preference','76','Data Type'),('647','provider preference','76','Data Type'),('648','start datetime','76','Data Type'),('649','stop datetime','76','Data Type'),('65','patient preference','9','Data Type'),('650','duration','77','Data Type'),('651','negation rationale','77','Data Type'),('652','patient preference','77','Data Type'),('653','provider preference','77','Data Type'),('654','start datetime','77','Data Type'),('655','stop datetime','77','Data Type'),('656','Source - Device','','Data Flow'),('657','Source - Informant','','Data Flow'),('659','Recorder - Informant','','Data Flow'),('66','provider preference','9','Data Type'),('660','Recorder - Device','','Data Flow'),('661','Setting','','Data Flow'),('662','Health Record Field','','Data Flow'),('67','severity','9','Data Type'),('68','start datetime','9','Data Type'),('69','status','9','Data Type'),('7','duration','1','Data Type'),('70','stop datetime','9','Data Type'),('71','duration','10','Data Type'),('72','negation rationale','10','Data Type'),('73','ordinality','10','Data Type'),('74','patient preference','10','Data Type'),('75','provider preference','10','Data Type'),('76','severity','10','Data Type'),('77','start datetime','10','Data Type'),('78','status','10','Data Type'),('79','stop datetime','10','Data Type'),('8','negation rationale','1','Data Type'),('80','duration','11','Data Type'),('81','negation rationale','11','Data Type'),('82','patient preference','11','Data Type'),('83','provider preference','11','Data Type'),('84','reaction','11','Data Type'),('85','start datetime','11','Data Type'),('86','stop datetime','11','Data Type'),('87','duration','12','Data Type'),('88','negation rationale','12','Data Type'),('89','patient preference','12','Data Type'),('9','patient preference','1','Data Type'),('90','provider preference','12','Data Type'),('91','reaction','12','Data Type'),('92','start datetime','12','Data Type'),('93','stop datetime','12','Data Type'),('94','duration','13','Data Type'),('95','negation rationale','13','Data Type'),('96','patient preference','13','Data Type'),('97','provider preference','13','Data Type'),('98','start datetime','13','Data Type'),('99','stop datetime','13','Data Type');
/*!40000 ALTER TABLE `qds_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qds_element`
--

DROP TABLE IF EXISTS `qds_element`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qds_element` (
  `QDS_ELEMENT_ID` varchar(64) NOT NULL,
  `MEASURE_PACKAGE_ID` varchar(64) NOT NULL,
  PRIMARY KEY (`QDS_ELEMENT_ID`),
  KEY `MEASURE_PACKAGE_ID_QDS_ELEMENT_FK` (`MEASURE_PACKAGE_ID`),
  CONSTRAINT `MEASURE_PACKAGE_ID_QDS_ELEMENT_FK` FOREIGN KEY (`MEASURE_PACKAGE_ID`) REFERENCES `measure_package` (`MEASURE_PACKAGE_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qds_element`
--

LOCK TABLES `qds_element` WRITE;
/*!40000 ALTER TABLE `qds_element` DISABLE KEYS */;
/*!40000 ALTER TABLE `qds_element` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qds_property`
--

DROP TABLE IF EXISTS `qds_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qds_property` (
  `QDS_PROPERTY_ID` varchar(64) NOT NULL,
  `NAME` varchar(45) NOT NULL,
  `VALUE` varchar(200) NOT NULL,
  `TYPE` varchar(45) NOT NULL,
  `QDS_ELEMENT_ID` varchar(64) NOT NULL,
  PRIMARY KEY (`QDS_PROPERTY_ID`),
  KEY `QDS_ELEMENT_ID_QDS_PROPERTY_FK` (`QDS_ELEMENT_ID`),
  CONSTRAINT `QDS_ELEMENT_ID_QDS_PROPERTY_FK` FOREIGN KEY (`QDS_ELEMENT_ID`) REFERENCES `qds_element` (`QDS_ELEMENT_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qds_property`
--

LOCK TABLES `qds_property` WRITE;
/*!40000 ALTER TABLE `qds_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `qds_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qds_term`
--

DROP TABLE IF EXISTS `qds_term`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qds_term` (
  `ID` varchar(64) NOT NULL,
  `QDS_ELEMENT_ID` varchar(64) NOT NULL,
  `DECISION_ID` varchar(64) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qds_term`
--

LOCK TABLES `qds_term` WRITE;
/*!40000 ALTER TABLE `qds_term` DISABLE KEYS */;
/*!40000 ALTER TABLE `qds_term` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quality_data_set`
--

DROP TABLE IF EXISTS `quality_data_set`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quality_data_set` (
  `QUALITY_DATA_SET_ID` varchar(32) NOT NULL,
  `DATA_TYPE_ID` varchar(32) NOT NULL,
  `LIST_OBJECT_ID` varchar(32) NOT NULL,
  `MEASURE_ID` varchar(32) NOT NULL,
  `VERSION` varchar(32) NOT NULL,
  `OID` varchar(255) NOT NULL,
  PRIMARY KEY (`QUALITY_DATA_SET_ID`),
  KEY `QDS_DATA_TYPE_FK` (`DATA_TYPE_ID`),
  KEY `QDS_CODE_LIST_FK` (`LIST_OBJECT_ID`),
  KEY `QDS_MEASURE_ID_FK` (`MEASURE_ID`),
  CONSTRAINT `QDS_DATA_TYPE_FK` FOREIGN KEY (`DATA_TYPE_ID`) REFERENCES `data_type` (`DATA_TYPE_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `QDS_CODE_LIST_FK` FOREIGN KEY (`LIST_OBJECT_ID`) REFERENCES `list_object` (`LIST_OBJECT_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `QDS_MEASURE_ID_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `measure` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quality_data_set`
--

LOCK TABLES `quality_data_set` WRITE;
/*!40000 ALTER TABLE `quality_data_set` DISABLE KEYS */;
INSERT INTO `quality_data_set` VALUES ('8ae452962e3a3b8f012e3a440d470016','16','8ae452962e3a3b8f012e3a3f62120007','8ae452962e3a223a012e3a37cf63000a','1','1'),('8ae452962e3a3b8f012e3a442ce50018','17','8ae452962e3a3b8f012e3a3f62120007','8ae452962e3a223a012e3a37cf63000a','1','2'),('8ae452962e3a3b8f012e3a444cd1001a','18','8ae452962e3a3b8f012e3a3f62120007','8ae452962e3a223a012e3a37cf63000a','1','3'),('8ae452962e3a3b8f012e3a447af5001c','95','8ae452962e3a3b8f012e3a3d39640001','8ae452962e3a223a012e3a37cf63000a','1','4'),('8ae452962e3a3b8f012e3a44893e001e','21','8ae452962e3a3b8f012e3a3d39640001','8ae452962e3a223a012e3a37cf63000a','1','5'),('8ae452962e3a3b8f012e3a449ae10020','22','8ae452962e3a3b8f012e3a3d39640001','8ae452962e3a223a012e3a37cf63000a','1','6'),('8ae452962e3a3b8f012e3a44bbc70022','38','8ae452962e3a3b8f012e3a3ea0480004','8ae452962e3a223a012e3a37cf63000a','1','7'),('8ae452962e3a3b8f012e3a44c9350024','39','8ae452962e3a3b8f012e3a3ea0480004','8ae452962e3a223a012e3a37cf63000a','1','8'),('8ae452962e3a3b8f012e3a44e19f0026','41','8ae452962e3a3b8f012e3a3ea0480004','8ae452962e3a223a012e3a37cf63000a','1','9'),('8ae452962e3a3b8f012e3a44f8340028','66','8ae452962e3a3b8f012e3a40268c000a','8ae452962e3a223a012e3a37cf63000a','1','10'),('8ae452962e3a3b8f012e3a451949002a','69','8ae452962e3a3b8f012e3a40268c000a','8ae452962e3a223a012e3a37cf63000a','1','11'),('8ae452962e3a3b8f012e3a452b2b002c','89','8ae452962e3a3b8f012e3a40268c000a','8ae452962e3a223a012e3a37cf63000a','1','12'),('8ae452962e3a3b8f012e3a4541b1002e','93','8ae452962e3a223a012e3a254b800008','8ae452962e3a223a012e3a37cf63000a','1','13'),('8ae452962e3a3b8f012e3a4555780030','91','8ae452962e3a223a012e3a254aa60004','8ae452962e3a223a012e3a37cf63000a','1','14'),('8ae452962e3a3b8f012e3a4560b30032','91','8ae452962e3a223a012e3a254b130006','8ae452962e3a223a012e3a37cf63000a','1','15'),('8ae452962e3a3b8f012e3a4572560034','92','8ae452962e3a3b8f012e3a41202f000d','8ae452962e3a223a012e3a37cf63000a','1','16'),('8ae452962e3a3b8f012e3a4582b20036','92','8ae452962e3a3b8f012e3a41b54b0010','8ae452962e3a223a012e3a37cf63000a','1','17'),('8ae452962e3a3b8f012e3a458fb20038','92','8ae452962e3a3b8f012e3a424d750013','8ae452962e3a223a012e3a37cf63000a','1','18');
/*!40000 ALTER TABLE `quality_data_set` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `quality_data_set_oid_gen`
--

DROP TABLE IF EXISTS `quality_data_set_oid_gen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quality_data_set_oid_gen` (
  `OID_GEN_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`OID_GEN_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `quality_data_set_oid_gen`
--

LOCK TABLES `quality_data_set_oid_gen` WRITE;
/*!40000 ALTER TABLE `quality_data_set_oid_gen` DISABLE KEYS */;
INSERT INTO `quality_data_set_oid_gen` VALUES (1),(2),(3),(4),(5),(6),(7),(8),(9),(10),(11),(12),(13),(14),(15),(16),(17),(18);
/*!40000 ALTER TABLE `quality_data_set_oid_gen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `security_role`
--

DROP TABLE IF EXISTS `security_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `security_role` (
  `SECURITY_ROLE_ID` varchar(32) NOT NULL,
  `DESCRIPTION` varchar(50) NOT NULL,
  PRIMARY KEY (`SECURITY_ROLE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `security_role`
--

LOCK TABLES `security_role` WRITE;
/*!40000 ALTER TABLE `security_role` DISABLE KEYS */;
INSERT INTO `security_role` VALUES ('1','Administrator'),('2','Super user'),('3','User');
/*!40000 ALTER TABLE `security_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `share_level`
--

DROP TABLE IF EXISTS `share_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `share_level` (
  `SHARE_LEVEL_ID` varchar(32) NOT NULL,
  `DESCRIPTION` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`SHARE_LEVEL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `share_level`
--

LOCK TABLES `share_level` WRITE;
/*!40000 ALTER TABLE `share_level` DISABLE KEYS */;
INSERT INTO `share_level` VALUES ('1','View Only'),('2','Modify');
/*!40000 ALTER TABLE `share_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `static_element`
--

DROP TABLE IF EXISTS `static_element`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `static_element` (
  `STATIC_ELEMENT_ID` varchar(64) NOT NULL,
  `UNIT` varchar(45) NOT NULL,
  `QUANTITY` varchar(100) NOT NULL,
  PRIMARY KEY (`STATIC_ELEMENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `static_element`
--

LOCK TABLES `static_element` WRITE;
/*!40000 ALTER TABLE `static_element` DISABLE KEYS */;
/*!40000 ALTER TABLE `static_element` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status` (
  `STATUS_ID` varchar(32) NOT NULL,
  `DESCRIPTION` varchar(50) NOT NULL,
  PRIMARY KEY (`STATUS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES ('1','Active'),('2','Revoked');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `steward_org`
--

DROP TABLE IF EXISTS `steward_org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `steward_org` (
  `ID` varchar(32) NOT NULL,
  `ORG_NAME` varchar(200) NOT NULL,
  `ORG_OID` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `steward_org`
--

LOCK TABLES `steward_org` WRITE;
/*!40000 ALTER TABLE `steward_org` DISABLE KEYS */;
INSERT INTO `steward_org` VALUES ('14','American Medical Association - Physician Consortium for Performance Improvement',''),('29','Cleveland Clinic',''),('55','National Committee for Quality Assurance',''),('80','National Quality Forum',''),('81','Joint Commission',''),('82','Oklahoma Foundation for Medical Quality',''),('83','American Board of Internal Medicine','');
/*!40000 ALTER TABLE `steward_org` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `term`
--

DROP TABLE IF EXISTS `term`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `term` (
  `TERM_ID` varchar(64) NOT NULL,
  `ORDER` varchar(45) NOT NULL,
  `OPERATOR_ID` varchar(64) NOT NULL,
  `NEXT_TERM_ID` varchar(64) NOT NULL,
  `STATIC_ELEMENT_ID` varchar(64) NOT NULL,
  `QDS_ELEMENT_ID` varchar(64) NOT NULL,
  PRIMARY KEY (`TERM_ID`),
  KEY `OPERATOR_ID_FK` (`OPERATOR_ID`),
  KEY `NEXT_TERM_ID_FK` (`NEXT_TERM_ID`),
  KEY `STATIC_ELEMENT_ID_FK` (`STATIC_ELEMENT_ID`),
  KEY `QDS_ELEMENT_ID_FK` (`QDS_ELEMENT_ID`),
  CONSTRAINT `OPERATOR_ID_FK` FOREIGN KEY (`OPERATOR_ID`) REFERENCES `operator` (`OPERATOR_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `NEXT_TERM_ID_FK` FOREIGN KEY (`NEXT_TERM_ID`) REFERENCES `term` (`TERM_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `STATIC_ELEMENT_ID_FK` FOREIGN KEY (`STATIC_ELEMENT_ID`) REFERENCES `static_element` (`STATIC_ELEMENT_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `QDS_ELEMENT_ID_FK` FOREIGN KEY (`QDS_ELEMENT_ID`) REFERENCES `qds_element` (`QDS_ELEMENT_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `term`
--

LOCK TABLES `term` WRITE;
/*!40000 ALTER TABLE `term` DISABLE KEYS */;
/*!40000 ALTER TABLE `term` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
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
  PRIMARY KEY (`USER_ID`),
  KEY `USER_SECURITY_ROLE_FK` (`SECURITY_ROLE_ID`),
  KEY `USER_AUDIT_FK` (`AUDIT_ID`),
  KEY `USER_STATUS_FK` (`STATUS_ID`),
  CONSTRAINT `USER_SECURITY_ROLE_FK` FOREIGN KEY (`SECURITY_ROLE_ID`) REFERENCES `security_role` (`SECURITY_ROLE_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `USER_AUDIT_FK` FOREIGN KEY (`AUDIT_ID`) REFERENCES `audit_log` (`AUDIT_LOG_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `USER_STATUS_FK` FOREIGN KEY (`STATUS_ID`) REFERENCES `status` (`STATUS_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('8ae452962e3a223a012e3a2547880001','Andrew','','Schmidt','aschmidt@ifmc.org','123-456-7890','',NULL,'2011-02-18','2011-03-14 13:19:36',NULL,NULL,'1','8ae452962e3a223a012e3a2547880002','2','IFMC','1234567890','1234567890'),('Admin','Admin',NULL,'user','Admin','515-453-8083',NULL,NULL,'2010-10-28','2011-02-18 18:59:20',NULL,NULL,'1','1','1','IFMC','2.16.840.1.113883.3.67','2.16.840.1.113883.3.67.1.101.1');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_password`
--

DROP TABLE IF EXISTS `user_password`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_password` (
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
  CONSTRAINT `PASSWORD_USER_FK` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_password`
--

LOCK TABLES `user_password` WRITE;
/*!40000 ALTER TABLE `user_password` DISABLE KEYS */;
INSERT INTO `user_password` VALUES ('1','Admin',0,0,'5d205275436221221185ee9ae8850c91','ebed4630-deed-4df9-b939-5bd56ab6d3e3',0,'2011-02-18',NULL,0),('8ae452962e3a223a012e3a2547980003','8ae452962e3a223a012e3a2547880001',0,0,'c66aae166c7ab8dc16e67703e864bd53','8600f5bc-5bf0-4755-8e51-b7c035638143',0,'2011-02-18',NULL,0);
/*!40000 ALTER TABLE `user_password` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_security_questions`
--

DROP TABLE IF EXISTS `user_security_questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_security_questions` (
  `USER_ID` varchar(40) NOT NULL,
  `ROW_ID` int(11) NOT NULL,
  `QUESTION` varchar(100) NOT NULL,
  `ANSWER` varchar(100) NOT NULL,
  PRIMARY KEY (`USER_ID`,`ROW_ID`),
  KEY `SECURITY_QUES_USER_FK` (`USER_ID`),
  CONSTRAINT `SECURITY_QUES_USER_FK` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_security_questions`
--

LOCK TABLES `user_security_questions` WRITE;
/*!40000 ALTER TABLE `user_security_questions` DISABLE KEYS */;
INSERT INTO `user_security_questions` VALUES ('8ae452962e3a223a012e3a2547880001',0,'What is your father\'s middle name?','blah1'),('8ae452962e3a223a012e3a2547880001',1,'What was the name of your first pet?','blah2'),('8ae452962e3a223a012e3a2547880001',2,'What was the name of your first school?','blah3'),('Admin',0,'What is your father\'s middle name?','blah1'),('Admin',1,'What was the name of your first pet?','blah2'),('Admin',2,'What was the name of your first school?','blah3');
/*!40000 ALTER TABLE `user_security_questions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-03-14  8:21:16
