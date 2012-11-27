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
INSERT INTO `audit_log` VALUES ('1','2010-10-28 20:08:27','Admin',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('8ae452962e3a223a012e3a2547880002','2011-02-18 19:00:45','Admin',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('8ae452962e3a223a012e3a254ab50005','2011-02-18 19:00:45','Admin',NULL,NULL,'Insert',NULL,'8ae452962e3a223a012e3a254aa60004',NULL,NULL),('8ae452962e3a223a012e3a254b130007','2011-02-18 19:00:45','Admin',NULL,NULL,'Insert',NULL,'8ae452962e3a223a012e3a254b130006',NULL,NULL),('8ae452962e3a223a012e3a254b800009','2011-02-18 19:00:46','Admin',NULL,NULL,'Insert',NULL,'8ae452962e3a223a012e3a254b800008',NULL,NULL),('8ae452962e3a223a012e3a37cf63000b','2011-02-18 19:20:59','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert','8ae452962e3a223a012e3a37cf63000a',NULL,NULL,NULL),('8ae452962e3a3b8f012e3a3d39830002','2011-02-18 19:26:54','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,'8ae452962e3a3b8f012e3a3d39640001',NULL,NULL),('8ae452962e3a3b8f012e3a3ea0480005','2011-02-18 19:28:26','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,'8ae452962e3a3b8f012e3a3ea0480004',NULL,NULL),('8ae452962e3a3b8f012e3a3f62120008','2011-02-18 19:29:15','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,'8ae452962e3a3b8f012e3a3f62120007',NULL,NULL),('8ae452962e3a3b8f012e3a40268c000b','2011-02-18 19:30:06','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,'8ae452962e3a3b8f012e3a40268c000a',NULL,NULL),('8ae452962e3a3b8f012e3a41202f000e','2011-02-18 19:31:09','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,'8ae452962e3a3b8f012e3a41202f000d',NULL,NULL),('8ae452962e3a3b8f012e3a41b54b0011','2011-02-18 19:31:48','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,'8ae452962e3a3b8f012e3a41b54b0010',NULL,NULL),('8ae452962e3a3b8f012e3a424d750014','2011-02-18 19:32:27','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,'8ae452962e3a3b8f012e3a424d750013',NULL,NULL),('8ae452962e3a3b8f012e3a440d470017','2011-02-18 19:34:21','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a440d470016'),('8ae452962e3a3b8f012e3a442ce50019','2011-02-18 19:34:29','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a442ce50018'),('8ae452962e3a3b8f012e3a444cd1001b','2011-02-18 19:34:38','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a444cd1001a'),('8ae452962e3a3b8f012e3a447af5001d','2011-02-18 19:34:49','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a447af5001c'),('8ae452962e3a3b8f012e3a44893e001f','2011-02-18 19:34:53','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a44893e001e'),('8ae452962e3a3b8f012e3a449ae10021','2011-02-18 19:34:58','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a449ae10020'),('8ae452962e3a3b8f012e3a44bbc70023','2011-02-18 19:35:06','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a44bbc70022'),('8ae452962e3a3b8f012e3a44c9350025','2011-02-18 19:35:09','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a44c9350024'),('8ae452962e3a3b8f012e3a44e19f0027','2011-02-18 19:35:16','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a44e19f0026'),('8ae452962e3a3b8f012e3a44f8340029','2011-02-18 19:35:21','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a44f8340028'),('8ae452962e3a3b8f012e3a451949002b','2011-02-18 19:35:30','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a451949002a'),('8ae452962e3a3b8f012e3a452b2b002d','2011-02-18 19:35:34','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a452b2b002c'),('8ae452962e3a3b8f012e3a4541b1002f','2011-02-18 19:35:40','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a4541b1002e'),('8ae452962e3a3b8f012e3a4555780031','2011-02-18 19:35:45','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a4555780030'),('8ae452962e3a3b8f012e3a4560b30033','2011-02-18 19:35:48','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a4560b30032'),('8ae452962e3a3b8f012e3a4572560035','2011-02-18 19:35:53','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a4572560034'),('8ae452962e3a3b8f012e3a4582b20037','2011-02-18 19:35:57','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a4582b20036'),('8ae452962e3a3b8f012e3a458fb20039','2011-02-18 19:36:00','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,NULL,'8ae452962e3a3b8f012e3a458fb20038'),('8ae452962e4daa76012e4dac3d370002','2011-02-22 14:00:56','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4dac3d270001',NULL),('8ae452962e4daa76012e4dacbad40004','2011-02-22 14:01:29','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4dacbaa50003',NULL),('8ae452962e4daa76012e4dad71b10006','2011-02-22 14:02:15','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4dad71a10005',NULL),('8ae452962e4daa76012e4dae43790008','2011-02-22 14:03:09','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4dae434a0007',NULL),('8ae452962e4daa76012e4daed365000a','2011-02-22 14:03:46','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4daed3650009',NULL),('8ae452962e4daa76012e4daf6574000c','2011-02-22 14:04:23','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4daf6564000b',NULL),('8ae452962e4daa76012e4db01a6d000e','2011-02-22 14:05:10','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4db01a6d000d',NULL),('8ae452962e4daa76012e4db0801c0010','2011-02-22 14:05:36','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4db0800d000f',NULL),('8ae452962e4daa76012e4db10fda0012','2011-02-22 14:06:12','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4db10fda0011',NULL),('8ae452962e4daa76012e4db1c0ac0014','2011-02-22 14:06:58','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4db1c0ac0013',NULL),('8ae452962e4daa76012e4db344bd0016','2011-02-22 14:08:37','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4db344bd0015',NULL),('8ae452962e4daa76012e4db34fe80018','2011-02-22 14:08:40','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4db34fe80017',NULL),('8ae452962e4daa76012e4db35df2001a','2011-02-22 14:08:44','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4db35df20019',NULL),('8ae452962e4daa76012e4db36d05001c','2011-02-22 14:08:47','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4db36d05001b',NULL),('8ae452962e4daa76012e4db37dbf001e','2011-02-22 14:08:52','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4db37daf001d',NULL),('8ae452962e4daa76012e4db3bd1a001f','2011-02-22 14:09:08','8ae452962e3a223a012e3a2547880001','2011-02-22 14:09:08','8ae452962e3a223a012e3a2547880001','Update',NULL,NULL,'8ae452962e4daa76012e4db344bd0015',NULL),('8ae452962e4daa76012e4db4449a0020','2011-02-22 14:09:43','8ae452962e3a223a012e3a2547880001','2011-02-22 14:09:43','8ae452962e3a223a012e3a2547880001','Update',NULL,NULL,'8ae452962e4daa76012e4db34fe80017',NULL),('8ae452962e4daa76012e4db46a620021','2011-02-22 14:09:52','8ae452962e3a223a012e3a2547880001','2011-02-22 14:09:52','8ae452962e3a223a012e3a2547880001','Update',NULL,NULL,'8ae452962e4daa76012e4db35df20019',NULL),('8ae452962e4daa76012e4db4a0080022','2011-02-22 14:10:06','8ae452962e3a223a012e3a2547880001','2011-02-22 14:10:06','8ae452962e3a223a012e3a2547880001','Update',NULL,NULL,'8ae452962e4daa76012e4db36d05001b',NULL),('8ae452962e4daa76012e4db51ebe0023','2011-02-22 14:10:38','8ae452962e3a223a012e3a2547880001','2011-02-22 14:10:38','8ae452962e3a223a012e3a2547880001','Update',NULL,NULL,'8ae452962e4daa76012e4db37daf001d',NULL),('8ae452962e4daa76012e4db51fd70025','2011-02-22 14:10:39','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4db51fd70024',NULL),('8ae452962e4daa76012e4db567cd0026','2011-02-22 14:10:57','8ae452962e3a223a012e3a2547880001','2011-02-22 14:10:57','8ae452962e3a223a012e3a2547880001','Update',NULL,NULL,'8ae452962e4daa76012e4db36d05001b',NULL),('8ae452962e4daa76012e4db568990028','2011-02-22 14:10:57','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4db568990027',NULL),('8ae452962e4daa76012e4db5a66d0029','2011-02-22 14:11:13','8ae452962e3a223a012e3a2547880001','2011-02-22 14:11:13','8ae452962e3a223a012e3a2547880001','Update',NULL,NULL,'8ae452962e4daa76012e4db35df20019',NULL),('8ae452962e4daa76012e4db5a748002b','2011-02-22 14:11:13','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4db5a748002a',NULL),('8ae452962e4daa76012e4db5ed88002c','2011-02-22 14:11:31','8ae452962e3a223a012e3a2547880001','2011-02-22 14:11:31','8ae452962e3a223a012e3a2547880001','Update',NULL,NULL,'8ae452962e4daa76012e4db34fe80017',NULL),('8ae452962e4daa76012e4db5ee44002e','2011-02-22 14:11:32','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4db5ee44002d',NULL),('8ae452962e4daa76012e4db64889002f','2011-02-22 14:11:55','8ae452962e3a223a012e3a2547880001','2011-02-22 14:11:55','8ae452962e3a223a012e3a2547880001','Update',NULL,NULL,'8ae452962e4daa76012e4db344bd0015',NULL),('8ae452962e4daa76012e4db649830031','2011-02-22 14:11:55','8ae452962e3a223a012e3a2547880001',NULL,NULL,'Insert',NULL,NULL,'8ae452962e4daa76012e4db649830030',NULL);
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
INSERT INTO `author` VALUES ('1','3M Health Information Systems'),('10','American Academy of Family Physicians'),('11','American Academy of Orthopaedic Surgeons'),('12','American College of Rheumatology'),('13','American College of Surgeons'),('14','American Medical Association - Physician Consortium for Performance Improvement'),('15','American Nurses Association'),('16','American Podiatric Medical Association'),('17','American Speech-Language-Hearing Association'),('18','Asian Liver Center at Stanford University'),('19','Booz Allen Hamilton'),('2','ACC/AHA Task Force on Performance Measures'),('20','California Maternal Quality Care Collaborative'),('21','California Nursing Outcome Coalition'),('22','Center for Medicare & Medicaid Services'),('23','Center for Quality Assessment and Improvement in Mental Health'),('24','Centers for Disease Control and Prevention'),('25','Child Health Corporation of America'),('26','Children\'s Hospital of Philadelphia'),('27','Christiana Care Health System'),('28','City of New York Department of Health and Mental Hygiene'),('29','Cleveland Clinic'),('3','ActiveHealth Management'),('30','CREcare'),('31','Focus on Therapeutic Outcomes, Inc'),('32','General Electric - Healthcare'),('33','Harborview Medical Center'),('34','Health Benchmarks, Inc'),('35','HealthPartners'),('36','Henry Ford Hospital'),('37','Hospital Corporation of America'),('38','Ingenix'),('39','Institute for Clinical and Evaluative Sciences'),('4','Agency for Healthcare Research and Quality'),('40','Institute for Clinical Systems Improvement'),('41','Institute for Healthcare Improvement'),('42','Intermountain Healthcare'),('43','Intersocietal Accreditation Comission'),('44','IPRO'),('45','James Whitcomb Riley Hospital for Children'),('46','Joint Commission Resources, Inc.'),('47','Kidney Care Quality Alliance'),('48','Leapfrog Group'),('49','Lifescan, A Johnson & Johnson Company'),('5','Ambulatory Surgical Centers Quality Collaboration'),('50','Louisiana State University'),('51','Massachusetts General Hospital/Partners Health Care System'),('52','MN Community Measurement'),('53','National Association of Children\'s Hospitals and Related Institutions'),('54','National Cancer Institute'),('55','National Committee for Quality Assurance'),('56','National Hospice and Palliative Care Organization'),('57','National Initiative for Children\'s Healthcare Quality'),('58','National Perinatal Information Center'),('59','New York State Department of Health'),('6','American Academy of Dermatology'),('60','Office of Statewide Health Planning and Development'),('61','Oregon Health & Science University'),('62','Ortho-McNeil-Janssen Pharmaceuticals, Inc.'),('63','PacifiCare'),('64','Premier, Inc'),('65','PRETest Consult, LLC'),('66','Providence St. Vincent Medical Center'),('67','Providence Health and Services'),('68','RAND'),('69','Resolution Health, Inc.'),('7','American Academy of Pediatrics'),('70','Society for Vascular Surgery'),('71','Society of Thoracic Surgeons'),('72','The Joint Commission'),('73','United Health Group'),('74','University of Colorado Health Sciences Center'),('75','University of Minnesota Rural Health Research Center'),('76','Vermont Oxford Network'),('77','VHA, Inc.'),('78','Wisconsin Collaborative for Healthcare Quality'),('79','Wisconsin Department of Health and Family Services'),('8','American College of Cardiology'),('80','Other'),('9','American College of Emergency Physicians');
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
INSERT INTO `clause` VALUES ('8ae452962e4daa76012e4dac3d270001','phrase_pop1',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','7','e77f9d4b-bbb1-4809-8488-08dd92d716e3',NULL,NULL,NULL),('8ae452962e4daa76012e4dacbaa50003','phrase_pop2',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','7','da8e98f4-1475-48c3-9605-fdc59a596a5b',NULL,NULL,NULL),('8ae452962e4daa76012e4dad71a10005','phrase_num1',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','7','a1ae91a0-0e2e-4f73-9099-67599413cd58',NULL,NULL,NULL),('8ae452962e4daa76012e4dae434a0007','phrase_num2',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','7','246559cc-9453-4b2f-9c00-7dc201234529',NULL,NULL,NULL),('8ae452962e4daa76012e4daed3650009','phrase_denom1',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','7','5d84efe3-910c-4cdb-83eb-0eb345095a91',NULL,NULL,NULL),('8ae452962e4daa76012e4daf6564000b','phrase_denom2',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','7','2b23dd6c-d117-497b-8e70-15b127e8823b',NULL,NULL,NULL),('8ae452962e4daa76012e4db01a6d000d','phrase_excl1',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','7','012f578b-357b-495e-b2e0-785988ce4607',NULL,NULL,NULL),('8ae452962e4daa76012e4db0800d000f','phrase_excl2',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','7','ffc3182f-5d29-45d8-90ae-7175fac53378',NULL,NULL,NULL),('8ae452962e4daa76012e4db10fda0011','phrase_excep1',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','7','9f076d9b-de02-4b44-89a6-ee73937b2d54',NULL,NULL,NULL),('8ae452962e4daa76012e4db1c0ac0013','phrase_excep2',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','7','2120fb89-267d-4f5a-bd78-6661eceed4c8',NULL,NULL,NULL),('8ae452962e4daa76012e4db344bd0015','3rd Dream_Population1',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','1','6e13fc3c-4a56-49c5-9291-4fd142d114ab',NULL,NULL,NULL),('8ae452962e4daa76012e4db34fe80017','3rd Dream_Numerator1',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','2','b7e64652-afa0-420d-9f2e-54e024dfdf47',NULL,NULL,NULL),('8ae452962e4daa76012e4db35df20019','3rd Dream_Denominator1',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','3','e1bb4eec-2bda-4674-a69a-eff10f34a2ad',NULL,NULL,NULL),('8ae452962e4daa76012e4db36d05001b','3rd Dream_Exclusions1',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','4','409c5eec-d62d-44e5-bf61-b7693d4b9c11',NULL,NULL,NULL),('8ae452962e4daa76012e4db37daf001d','3rd Dream_Exceptions1',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','5','9a0b89a6-b531-4cfc-ad13-261272017d99',NULL,NULL,NULL),('8ae452962e4daa76012e4db51fd70024','3rd Dream_Exceptions2',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','5','f1607d5b-44d9-47b4-9da4-cefd4a002d5e',NULL,NULL,NULL),('8ae452962e4daa76012e4db568990027','3rd Dream_Exclusions2',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','4','b11359df-8b66-4193-9500-2b1711f3273b',NULL,NULL,NULL),('8ae452962e4daa76012e4db5a748002a','3rd Dream_Denominator2',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','3','909cfc36-d6e9-43ea-bb3d-1b784d5a997f',NULL,NULL,NULL),('8ae452962e4daa76012e4db5ee44002d','3rd Dream_Numerator2',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','2','74e30abf-a7fe-43e5-a2cc-59ce82c49759',NULL,NULL,NULL),('8ae452962e4daa76012e4db649830030','3rd Dream_Population2',NULL,NULL,'8ae452962e3a223a012e3a37cf63000a','1','f89ca73f-cfad-4001-9a26-dda85d33192c',NULL,NULL,NULL);
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
INSERT INTO `decision` VALUES ('01281538-a1dc-4958-8466-0ac2b637bf94','QDSTERM','f90d8108-7cfb-432a-820a-1f1ce39a1e30','1',NULL,NULL),('012f578b-357b-495e-b2e0-785988ce4607','CLAUSE',NULL,NULL,NULL,NULL),('2120fb89-267d-4f5a-bd78-6661eceed4c8','CLAUSE',NULL,NULL,NULL,NULL),('246559cc-9453-4b2f-9c00-7dc201234529','CLAUSE',NULL,NULL,NULL,NULL),('26309ff0-ff78-4233-97a8-74acfb078783','AND','f1607d5b-44d9-47b4-9da4-cefd4a002d5e','0',NULL,NULL),('26a5d468-0e57-4872-b426-13c0eb21e27f','ECW','806f4267-bb7c-4c23-90c4-0c083b487dd6','0',NULL,NULL),('2b23dd6c-d117-497b-8e70-15b127e8823b','CLAUSE',NULL,NULL,NULL,NULL),('33fc043c-e43f-476b-b6f1-df94e8c61d26','CLAUSE','6b4730b4-b6b4-4fea-a7a6-05a77320b830','0','8ae452962e4daa76012e4db01a6d000d',NULL),('350465cc-e4f4-45c5-b228-32a81e8164d0','SAS','012f578b-357b-495e-b2e0-785988ce4607','0',NULL,NULL),('353da23e-304e-4fb0-88c6-f08ab9be4bd4','QDSTERM','69c1be64-b76c-4b54-844a-f12454deb8ff','1',NULL,NULL),('37dbd6a2-07cc-40cd-adf3-b2a26c25a393','CLAUSE','26309ff0-ff78-4233-97a8-74acfb078783','0','8ae452962e4daa76012e4db1c0ac0013',NULL),('3bc14e91-30ff-40d8-b4d5-9a32a6c6a847','AND','6e13fc3c-4a56-49c5-9291-4fd142d114ab','0',NULL,NULL),('409c5eec-d62d-44e5-bf61-b7693d4b9c11','CLAUSE',NULL,NULL,NULL,NULL),('464da299-4adf-4506-a926-d704b824dad5','CLAUSE','3bc14e91-30ff-40d8-b4d5-9a32a6c6a847','0','8ae452962e4daa76012e4dac3d270001',NULL),('4a343ddc-e22d-4d12-9400-ff5ac45cf5c5','QDSTERM','4b34ae37-aed8-41c9-b6d6-0ca7dee5a0ae','0',NULL,NULL),('4a570dbc-37ac-4568-a5c4-cf416dd88034','CLAUSE','651619e5-e3fd-4ee1-852f-5035db469417','0','8ae452962e4daa76012e4daed3650009',NULL),('4b34ae37-aed8-41c9-b6d6-0ca7dee5a0ae','EBOD','5d84efe3-910c-4cdb-83eb-0eb345095a91','0',NULL,NULL),('4eadb472-c634-4a19-bdf3-6db3cf620e73','QDSTERM','4b34ae37-aed8-41c9-b6d6-0ca7dee5a0ae','1',NULL,NULL),('4f4cb8aa-26a2-4f61-bc66-fc1848b5039a','QDSTERM','69c1be64-b76c-4b54-844a-f12454deb8ff','0',NULL,NULL),('50b02f10-9ff6-4e81-9b9c-7b87b3ed0281','DURING','246559cc-9453-4b2f-9c00-7dc201234529','0',NULL,NULL),('59a911f6-dd0a-4de7-a48e-b73477643d65','CLAUSE','62be4544-be2c-474f-9768-5389f23aea0b','0','8ae452962e4daa76012e4dacbaa50003',NULL),('5d84efe3-910c-4cdb-83eb-0eb345095a91','CLAUSE',NULL,NULL,NULL,NULL),('601dd2e8-07c9-4487-a72e-b422ee320d47','CLAUSE','af944243-fd45-438f-bd54-b16747ae0a28','1','8ae452962e4daa76012e4daf6564000b',NULL),('62361ead-d672-4869-96eb-aa8b4317e57c','QDSTERM','26a5d468-0e57-4872-b426-13c0eb21e27f','0',NULL,NULL),('62be4544-be2c-474f-9768-5389f23aea0b','AND','f89ca73f-cfad-4001-9a26-dda85d33192c','0',NULL,NULL),('651619e5-e3fd-4ee1-852f-5035db469417','AND','e1bb4eec-2bda-4674-a69a-eff10f34a2ad','0',NULL,NULL),('6695e2c2-37a1-44ee-8ab7-c4170ec696de','AND','b7e64652-afa0-420d-9f2e-54e024dfdf47','0',NULL,NULL),('69c1be64-b76c-4b54-844a-f12454deb8ff','SBS','2b23dd6c-d117-497b-8e70-15b127e8823b','0',NULL,NULL),('6b4730b4-b6b4-4fea-a7a6-05a77320b830','AND','409c5eec-d62d-44e5-bf61-b7693d4b9c11','0',NULL,NULL),('6e13fc3c-4a56-49c5-9291-4fd142d114ab','CLAUSE',NULL,NULL,NULL,NULL),('72dea05b-fd48-4516-bd39-4baf10ff961c','QDSTERM','bcd8ff51-f279-4c38-a030-b21d3c9a2ac5','0',NULL,NULL),('74e30abf-a7fe-43e5-a2cc-59ce82c49759','CLAUSE',NULL,NULL,NULL,NULL),('74ff253c-4956-4866-b1d7-6dc29be09279','QDSTERM','cdc34bff-d88e-419a-a4fa-d553b81fe42c','0',NULL,NULL),('805daee3-1781-4d16-87f7-b34f709515e9','CLAUSE','b78a9638-05e8-4981-b9df-5b1abe6d7e7a','0','8ae452962e4daa76012e4db10fda0011',NULL),('8067fb12-3bce-4834-b5d6-797c8f717240','CLAUSE','8b941559-9541-4989-bd35-9bc82e5271ce','0','8ae452962e4daa76012e4dae434a0007',NULL),('806f4267-bb7c-4c23-90c4-0c083b487dd6','DAYOFYEAR','a1ae91a0-0e2e-4f73-9099-67599413cd58','0',NULL,NULL),('80fe3bfc-dc99-47b0-bad2-e55f1b987e64','QDSTERM','cdc34bff-d88e-419a-a4fa-d553b81fe42c','1',NULL,NULL),('8b941559-9541-4989-bd35-9bc82e5271ce','AND','74e30abf-a7fe-43e5-a2cc-59ce82c49759','0',NULL,NULL),('9071c4e5-09c9-41bc-b2ab-670cd5d1b1f3','QDSTERM','9bc83b4e-fce2-4e64-afeb-d087df37a2f5','0',NULL,NULL),('909cfc36-d6e9-43ea-bb3d-1b784d5a997f','CLAUSE',NULL,NULL,NULL,NULL),('919c4ebf-02c4-4a74-89b2-e8570a0ab827','QDSTERM','f90d8108-7cfb-432a-820a-1f1ce39a1e30','0',NULL,NULL),('94479e69-0c59-48e0-a579-53ff6d439750','QDSTERM','50b02f10-9ff6-4e81-9b9c-7b87b3ed0281','0',NULL,NULL),('9a0b89a6-b531-4cfc-ad13-261272017d99','CLAUSE',NULL,NULL,NULL,NULL),('9bc83b4e-fce2-4e64-afeb-d087df37a2f5','EBS','da8e98f4-1475-48c3-9605-fdc59a596a5b','0',NULL,NULL),('9f076d9b-de02-4b44-89a6-ee73937b2d54','CLAUSE',NULL,NULL,NULL,NULL),('a1ae91a0-0e2e-4f73-9099-67599413cd58','CLAUSE',NULL,NULL,NULL,NULL),('a8d5a5f2-19fc-4320-bf65-e7c761b3de48','QDSTERM','26a5d468-0e57-4872-b426-13c0eb21e27f','1',NULL,NULL),('a9a4938a-9882-4891-ab9c-1d354a06d18d','CLAUSE','c8ac7c8d-0a9e-4f47-9dde-31b373fafff0','0','8ae452962e4daa76012e4daf6564000b',NULL),('ab06f9b8-5720-4304-a15f-b14083e7a1ba','AND','b11359df-8b66-4193-9500-2b1711f3273b','0',NULL,NULL),('af36d064-7f76-451b-ac9e-b0bb7a44a8e1','CLAUSE','6695e2c2-37a1-44ee-8ab7-c4170ec696de','0','8ae452962e4daa76012e4dad71a10005',NULL),('af944243-fd45-438f-bd54-b16747ae0a28','EAE','2120fb89-267d-4f5a-bd78-6661eceed4c8','0',NULL,NULL),('b11359df-8b66-4193-9500-2b1711f3273b','CLAUSE',NULL,NULL,NULL,NULL),('b78a9638-05e8-4981-b9df-5b1abe6d7e7a','AND','9a0b89a6-b531-4cfc-ad13-261272017d99','0',NULL,NULL),('b7e64652-afa0-420d-9f2e-54e024dfdf47','CLAUSE',NULL,NULL,NULL,NULL),('bcd8ff51-f279-4c38-a030-b21d3c9a2ac5','DURING','9f076d9b-de02-4b44-89a6-ee73937b2d54','0',NULL,NULL),('c332b2d4-0533-4dd5-8fd1-de2fa82c8a96','QDSTERM','50b02f10-9ff6-4e81-9b9c-7b87b3ed0281','1',NULL,NULL),('c6e9ae93-d7d5-4c3d-931f-2638456d68e9','QDSTERM','350465cc-e4f4-45c5-b228-32a81e8164d0','1',NULL,NULL),('c8ac7c8d-0a9e-4f47-9dde-31b373fafff0','AND','909cfc36-d6e9-43ea-bb3d-1b784d5a997f','0',NULL,NULL),('cdc34bff-d88e-419a-a4fa-d553b81fe42c','EAS','e77f9d4b-bbb1-4809-8488-08dd92d716e3','0',NULL,NULL),('d1aabe30-99f8-4f61-bb22-8ce712edbefa','CLAUSE','bcd8ff51-f279-4c38-a030-b21d3c9a2ac5','1','8ae452962e4daa76012e4daed3650009',NULL),('d39d330e-a843-49de-9c5d-3f056ca7d77a','QDSTERM','9bc83b4e-fce2-4e64-afeb-d087df37a2f5','1',NULL,NULL),('da8e98f4-1475-48c3-9605-fdc59a596a5b','CLAUSE',NULL,NULL,NULL,NULL),('e1bb4eec-2bda-4674-a69a-eff10f34a2ad','CLAUSE',NULL,NULL,NULL,NULL),('e77f9d4b-bbb1-4809-8488-08dd92d716e3','CLAUSE',NULL,NULL,NULL,NULL),('ee85da59-7435-4a30-8991-3e6f3d889822','CLAUSE','ab06f9b8-5720-4304-a15f-b14083e7a1ba','0','8ae452962e4daa76012e4db0800d000f',NULL),('f1607d5b-44d9-47b4-9da4-cefd4a002d5e','CLAUSE',NULL,NULL,NULL,NULL),('f5ab7a83-9a08-4544-a1a2-fcc4428b73d5','QDSTERM','af944243-fd45-438f-bd54-b16747ae0a28','0',NULL,NULL),('f89ca73f-cfad-4001-9a26-dda85d33192c','CLAUSE',NULL,NULL,NULL,NULL),('f90d8108-7cfb-432a-820a-1f1ce39a1e30','SBOD','ffc3182f-5d29-45d8-90ae-7175fac53378','0',NULL,NULL),('f9bf177f-4995-4807-97b6-09db1a4503f9','QDSTERM','350465cc-e4f4-45c5-b228-32a81e8164d0','0',NULL,NULL),('ffc3182f-5d29-45d8-90ae-7175fac53378','CLAUSE',NULL,NULL,NULL,NULL);
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
  `RATIONALE` varchar(2000) DEFAULT NULL,
  `COMMENT` varchar(2000) DEFAULT NULL,
  `OBJECT_STATUS_ID` varchar(32) NOT NULL,
  `OBJECT_OWNER` varchar(32) NOT NULL,
  `CATEGORY_ID` varchar(32) NOT NULL,
  `CODE_SYS_VERSION` varchar(255) NOT NULL,
  `CODE_SYSTEM_ID` varchar(32) NOT NULL,
  `MEASURE_ID` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`LIST_OBJECT_ID`),
  KEY `LIST_OBJECT_STATUS_FK` (`OBJECT_STATUS_ID`),
  KEY `LIST_OBJECT_USER_FK` (`OBJECT_OWNER`),
  KEY `LIST_OBJECT_CAT_FK` (`CATEGORY_ID`),
  KEY `LIST_OBJECT_CODE_SYSTEM_FK` (`CODE_SYSTEM_ID`),
  KEY `LIST_OBJECT_STEWARD_FK` (`STEWARD`),
  KEY `LIST_OBJECT_MEASURE_FK` (`MEASURE_ID`),
  CONSTRAINT `LIST_OBJECT_CODE_SYSTEM_FK` FOREIGN KEY (`CODE_SYSTEM_ID`) REFERENCES `code_system` (`CODE_SYSTEM_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `LIST_OBJECT_STEWARD_FK` FOREIGN KEY (`STEWARD`) REFERENCES `steward_org` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `LIST_OBJECT_STATUS_FK` FOREIGN KEY (`OBJECT_STATUS_ID`) REFERENCES `object_status` (`OBJECT_STATUS_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `LIST_OBJECT_USER_FK` FOREIGN KEY (`OBJECT_OWNER`) REFERENCES `user` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `LIST_OBJECT_CAT_FK` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `category` (`CATEGORY_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `LIST_OBJECT_MEASURE_FK` FOREIGN KEY (`MEASURE_ID`) REFERENCES `measure` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `list_object`
--

LOCK TABLES `list_object` WRITE;
/*!40000 ALTER TABLE `list_object` DISABLE KEYS */;
INSERT INTO `list_object` VALUES ('8ae452962e3a223a012e3a254aa60004','1','Measurement Period','1','1',NULL,NULL,'2','8ae452962e3a223a012e3a2547880001','22','1','60',NULL),('8ae452962e3a223a012e3a254b130006','1','Measurement Start Date','1','2',NULL,NULL,'2','8ae452962e3a223a012e3a2547880001','22','1','60',NULL),('8ae452962e3a223a012e3a254b800008','1','Measurement End Date','1','3',NULL,NULL,'2','8ae452962e3a223a012e3a2547880001','22','1','60',NULL),('8ae452962e3a3b8f012e3a3d39640001','1','encounterCodeList','46','4','','','1','8ae452962e3a223a012e3a2547880001','7','1','21','8ae452962e3a223a012e3a37cf63000a'),('8ae452962e3a3b8f012e3a3ea0480004','1','medicationCodeList','78','5','','','1','8ae452962e3a223a012e3a2547880001','12','2','35','8ae452962e3a223a012e3a37cf63000a'),('8ae452962e3a3b8f012e3a3f62120007','1','diagnosticStudyCodeList','54','6','','','1','8ae452962e3a223a012e3a2547880001','6','3','16','8ae452962e3a223a012e3a37cf63000a'),('8ae452962e3a3b8f012e3a40268c000a','1','substanceCodeList1','14','7','','','1','8ae452962e3a223a012e3a2547880001','18','1','50','8ae452962e3a223a012e3a37cf63000a'),('8ae452962e3a3b8f012e3a41202f000d','1','attribute1','63','8','','','1','8ae452962e3a223a012e3a2547880001','23','1','64','8ae452962e3a223a012e3a37cf63000a'),('8ae452962e3a3b8f012e3a41b54b0010','1','attribute2','64','9','','','1','8ae452962e3a223a012e3a2547880001','23','2','63','8ae452962e3a223a012e3a37cf63000a'),('8ae452962e3a3b8f012e3a424d750013','1','attribute3','55','10','','','1','8ae452962e3a223a012e3a2547880001','23','3','65','8ae452962e3a223a012e3a37cf63000a');
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
INSERT INTO `measure` VALUES ('8ae452962e3a223a012e3a37cf63000a','8ae452962e3a223a012e3a2547880001','3rd Dream','Andrew Schmidt\'s 3rd Dream',NULL,'In Progress',NULL);
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
INSERT INTO `qds_term` VALUES ('1084466d-a43c-4f55-b1fe-6a1f5a56f872','8ae452962e3a3b8f012e3a444cd1001a','f9bf177f-4995-4807-97b6-09db1a4503f9'),('109f6fdd-c689-436d-8f05-b076a8fb85da','8ae452962e3a3b8f012e3a44f8340028','f5ab7a83-9a08-4544-a1a2-fcc4428b73d5'),('2f14f0a9-53ea-4d26-b67a-ad441b46a1f7','8ae452962e3a3b8f012e3a44893e001e','80fe3bfc-dc99-47b0-bad2-e55f1b987e64'),('353cfa28-89e0-4c35-9622-c6e14abf16c8','8ae452962e3a3b8f012e3a440d470016','74ff253c-4956-4866-b1d7-6dc29be09279'),('3ea408c8-c9af-4d59-8d00-8e7aa2c53dde','8ae452962e3a3b8f012e3a452b2b002c','4f4cb8aa-26a2-4f61-bc66-fc1848b5039a'),('45aa18ec-20f4-498d-bd41-ae80b1b65baf','8ae452962e3a3b8f012e3a451949002a','a8d5a5f2-19fc-4320-bf65-e7c761b3de48'),('68321bc5-9fe7-4cb3-bd56-1d9b19b1524e','8ae452962e3a3b8f012e3a449ae10020','94479e69-0c59-48e0-a579-53ff6d439750'),('70336555-2fe1-46ae-8720-38df3c5ff035','8ae452962e3a3b8f012e3a4560b30032','4eadb472-c634-4a19-bdf3-6db3cf620e73'),('706d758c-b9aa-41a4-8d66-92483c93c6a8','8ae452962e3a3b8f012e3a440d470016','01281538-a1dc-4958-8466-0ac2b637bf94'),('71d42610-bce5-4015-9356-9d20b75f2d97','8ae452962e3a3b8f012e3a44893e001e','72dea05b-fd48-4516-bd39-4baf10ff961c'),('78c724fa-b6fe-4d9a-b039-7738bb3d9694','8ae452962e3a3b8f012e3a44e19f0026','919c4ebf-02c4-4a74-89b2-e8570a0ab827'),('855e6a08-9454-4a35-a998-619b9af9712b','8ae452962e3a3b8f012e3a442ce50018','62361ead-d672-4869-96eb-aa8b4317e57c'),('8ef4feb6-5bd4-4e5e-a012-836d79ee78eb','8ae452962e3a3b8f012e3a44bbc70022','d39d330e-a843-49de-9c5d-3f056ca7d77a'),('a591b179-8a58-4424-b169-e2b4041525b5','8ae452962e3a3b8f012e3a4555780030','c332b2d4-0533-4dd5-8fd1-de2fa82c8a96'),('aa0e7224-4655-4c67-b3b7-1cba33061604','8ae452962e3a3b8f012e3a44f8340028','9071c4e5-09c9-41bc-b2ab-670cd5d1b1f3'),('b7ede136-e2de-45a0-b6c7-52dbb3967ffe','8ae452962e3a3b8f012e3a4541b1002e','353da23e-304e-4fb0-88c6-f08ab9be4bd4'),('bd149e28-ff84-46cf-abc7-ad42a264ed50','8ae452962e3a3b8f012e3a447af5001c','4a343ddc-e22d-4d12-9400-ff5ac45cf5c5'),('def04241-da81-415d-ac49-c2709564af4a','8ae452962e3a3b8f012e3a44c9350024','c6e9ae93-d7d5-4c3d-931f-2638456d68e9');
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
INSERT INTO `steward_org` VALUES ('1','3M Health Information Systems',''),('10','American Academy of Family Physicians',''),('11','American Academy of Orthopaedic Surgeons',''),('12','American College of Rheumatology',''),('13','American College of Surgeons',''),('14','American Medical Association - Physician Consortium for Performance Improvement',''),('15','American Nurses Association',''),('16','American Podiatric Medical Association',''),('17','American Speech-Language-Hearing Association',''),('18','Asian Liver Center at Stanford University',''),('19','Booz Allen Hamilton',''),('2','ACC/AHA Task Force on Performance Measures',''),('20','California Maternal Quality Care Collaborative',''),('21','California Nursing Outcome Coalition',''),('22','Center for Medicare & Medicaid Services',''),('23','Center for Quality Assessment and Improvement in Mental Health',''),('24','Centers for Disease Control and Prevention',''),('25','Child Health Corporation of America',''),('26','Children\'s Hospital of Philadelphia',''),('27','Christiana Care Health System',''),('28','City of New York Department of Health and Mental Hygiene',''),('29','Cleveland Clinic',''),('3','ActiveHealth Management',''),('30','CREcare',''),('31','Focus on Therapeutic Outcomes, Inc',''),('32','General Electric - Healthcare',''),('33','Harborview Medical Center',''),('34','Health Benchmarks, Inc',''),('35','HealthPartners',''),('36','Henry Ford Hospital',''),('37','Hospital Corporation of America',''),('38','Ingenix',''),('39','Institute for Clinical and Evaluative Sciences',''),('4','Agency for Healthcare Research and Quality',''),('40','Institute for Clinical Systems Improvement',''),('41','Institute for Healthcare Improvement',''),('42','Intermountain Healthcare',''),('43','Intersocietal Accreditation Comission',''),('44','IPRO',''),('45','James Whitcomb Riley Hospital for Children',''),('46','Joint Commission Resources, Inc.',''),('47','Kidney Care Quality Alliance',''),('48','Leapfrog Group',''),('49','Lifescan, A Johnson & Johnson Company',''),('5','Ambulatory Surgical Centers Quality Collaboration',''),('50','Louisiana State University',''),('51','Massachusetts General Hospital/Partners Health Care System',''),('52','MN Community Measurement',''),('53','National Association of Children\'s Hospitals and Related Institutions',''),('54','National Cancer Institute',''),('55','National Committee for Quality Assurance',''),('56','National Hospice and Palliative Care Organization',''),('57','National Initiative for Children\'s Healthcare Quality',''),('58','National Perinatal Information Center',''),('59','New York State Department of Health',''),('6','American Academy of Dermatology',''),('60','Office of Statewide Health Planning and Development',''),('61','Oregon Health & Science University',''),('62','Ortho-McNeil-Janssen Pharmaceuticals, Inc.',''),('63','PacifiCare',''),('64','Premier, Inc',''),('65','PRETest Consult, LLC',''),('66','Providence St. Vincent Medical Center',''),('67','Providence Health and Services',''),('68','RAND',''),('69','Resolution Health, Inc.',''),('7','American Academy of Pediatrics',''),('70','Society for Vascular Surgery',''),('71','Society of Thoracic Surgeons',''),('72','The Joint Commission',''),('73','United Health Group',''),('74','University of Colorado Health Sciences Center',''),('75','University of Minnesota Rural Health Research Center',''),('76','Vermont Oxford Network',''),('77','VHA, Inc.',''),('78','Wisconsin Collaborative for Healthcare Quality',''),('79','Wisconsin Department of Health and Family Services',''),('8','American College of Cardiology',''),('9','American College of Emergency Physicians','');
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
INSERT INTO `user` VALUES ('8ae452962e3a223a012e3a2547880001','Andrew','','Schmidt','aschmidt@ifmc.org','123-456-7890','',NULL,'2011-02-18','2011-02-22 14:13:00',NULL,NULL,'1','8ae452962e3a223a012e3a2547880002','2','IFMC','1234567890','1234567890'),('Admin','Admin',NULL,'user','Admin','515-453-8083',NULL,NULL,'2010-10-28','2011-02-18 18:59:20',NULL,NULL,'1','1','1','IFMC','2.16.840.1.113883.3.67','2.16.840.1.113883.3.67.1.101.1');
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
INSERT INTO `user_password` VALUES ('1','Admin',0,0,'5d205275436221221185ee9ae8850c91','ebed4630-deed-4df9-b939-5bd56ab6d3e3',0,'2011-02-18',NULL,0),('8ae452962e3a223a012e3a2547980003','8ae452962e3a223a012e3a2547880001',1,0,'c66aae166c7ab8dc16e67703e864bd53','8600f5bc-5bf0-4755-8e51-b7c035638143',0,'2011-02-18','2011-02-22 14:12:57',0);
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

-- Dump completed on 2011-02-22  8:13:53
