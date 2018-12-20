-- MySQL dump 10.13  Distrib 5.6.29, for Linux (x86_64)
--
-- ------------------------------------------------------
-- Server version	5.6.29

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

DROP SCHEMA IF EXISTS `MAT_APP_BLANK` ;
CREATE SCHEMA IF NOT EXISTS `MAT_APP_BLANK` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `MAT_APP_BLANK` ;

--
-- Table structure for table `ATTRIBUTE_DETAILS`
--

DROP TABLE IF EXISTS `ATTRIBUTE_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
INSERT INTO `CATEGORY` VALUES ('1','Care Experience','EXP'),('10','Intervention','INT'),('11','Laboratory Test','LAB'),('12','Medication','MED'),('14','Physical Exam','PE'),('16','Procedure','PRC'),('17','Risk Category/Assessment','RSK'),('18','Substance','SUB'),('19','Symptom','SX'),('2','Care Goal','GOL'),('20','System Characteristic','SYS'),('21','Transfer of Care','TRN'),('22','Measure Timing','TMG'),('23','Attribute','ATT'),('3','Communication','COM'),('4','Condition/Diagnosis/Problem','CDP'),('5','Device','DEV'),('6','Diagnostic Study','DXS'),('7','Encounter','ENC'),('8','Functional Status','FXS'),('9','Individual Characteristic','IND');
/*!40000 ALTER TABLE `CATEGORY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLAUSE`
--

DROP TABLE IF EXISTS `CLAUSE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
-- Table structure for table `CONTEXT`
--

DROP TABLE IF EXISTS `CONTEXT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
-- Table structure for table `DATABASECHANGELOG`
--

DROP TABLE IF EXISTS `DATABASECHANGELOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  PRIMARY KEY (`ID`,`AUTHOR`,`FILENAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOG`
--

LOCK TABLES `DATABASECHANGELOG` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOG` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOG` VALUES ('1','mat_dev_user','classpath:/liquibase/deploy_01.00.05_20110303.xml','2015-06-11 14:18:25',1,'EXECUTED','3:df5d494ac46a1e8edbe88c7e37501007','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.06_20110308.xml','2015-06-11 14:18:25',2,'EXECUTED','3:aa7427ca1b29b87ea94d0f36fcdfbcbb','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.07_20110308.xml','2015-06-11 14:18:25',3,'EXECUTED','3:db48d204c4119697ad3149a258e08267','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.08_20110315.xml','2015-06-11 14:18:25',4,'EXECUTED','3:4df623114c3fc40fc0e2c92e6ccc97aa','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.09_20110322.xml','2015-06-11 14:18:25',5,'EXECUTED','3:8814ce4c9a828e73639897b5703f95ff','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.10_20110414.xml','2015-06-11 14:18:25',6,'EXECUTED','3:f7918819f1e7b3c2f110d146e293caeb','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.11_20110426.xml','2015-06-11 14:18:25',7,'EXECUTED','3:17b7cb50c4bd1a2e30f37a341490ec54','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.12_20110513.xml','2015-06-11 14:18:25',8,'EXECUTED','3:6e87d921c12c3485dfa1840289abae8a','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.13_20110503.xml','2015-06-11 14:18:25',9,'EXECUTED','3:05b86029c7554dc683c697d528fd103d','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.14_20110621.xml','2015-06-11 14:18:25',10,'EXECUTED','3:29077851c1516702acea548853b7e83e','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.15_20110624.xml','2015-06-11 14:18:25',11,'EXECUTED','3:aad73700b1c0cb28ada93d5cae10d180','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.16_20110628.xml','2015-06-11 14:18:25',12,'EXECUTED','3:603a3122b0f8fcf452c8a7727b2be43b','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.17_20110701.xml','2015-06-11 14:18:26',13,'EXECUTED','3:ad4e572061a467449ffc766c0063469c','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.18_20110803.xml','2015-06-11 14:18:26',14,'EXECUTED','3:47bcaca1876d526f7e319fcf5495f4e2','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.19_20110805.xml','2015-06-11 14:18:26',15,'EXECUTED','3:4c87814637953fd9ee6c21fef966e299','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.20_20110802.xml','2015-06-11 14:18:26',16,'EXECUTED','3:8caa10527dd54b458f0aec6c7a4e834d','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.21_20110816.xml','2015-06-11 14:18:26',17,'EXECUTED','3:f1f1e073a45ad6803c0435df3fe61a7b','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.22_20110817.xml','2015-06-11 14:18:26',18,'EXECUTED','3:4332583f7296af6cc7d22e8ea17158f0','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.23_20110818.xml','2015-06-11 14:18:26',19,'EXECUTED','3:545274fcf95081bbee3a0b03bd373404','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.24_20110819.xml','2015-06-11 14:18:26',20,'EXECUTED','3:047069609780ffe2eca9ed1cf1e13731','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.25_20110819.xml','2015-06-11 14:18:26',21,'EXECUTED','3:c8147521d88c6ec052299d25fe7b6b21','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.26_20110825.xml','2015-06-11 14:18:26',22,'EXECUTED','3:587b4022014ad8a5a00f032bd373ab9b','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.27_20110826.xml','2015-06-11 14:18:26',23,'EXECUTED','3:8c0f0f50f4e3db62cf4ae7c920f5aa47','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.28_20110831.xml','2015-06-11 14:18:26',24,'EXECUTED','3:70d5034e5b121201cc000dc2ac90b3ec','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.29_20110831.xml','2015-06-11 14:18:26',25,'EXECUTED','3:a2c0136415f43c40b84290e8d6937105','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.30_20110831.xml','2015-06-11 14:18:26',26,'EXECUTED','3:aca2f8010670bce9d278c64cfe395887','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.31_20110831.xml','2015-06-11 14:18:26',27,'EXECUTED','3:b41005b5b4a5807144459bbca027c80f','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.32_20110831.xml','2015-06-11 14:18:26',30,'EXECUTED','3:d70f67d8963f4bf2218bea2bc58a62aa','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.33_20110905.xml','2015-06-11 14:18:26',32,'EXECUTED','3:8eb55fe952c78e8e2e5fc62137888342','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.34_20110906.xml','2015-06-11 14:18:26',28,'EXECUTED','3:a2cb2d372434e0174616d637ac967e4d','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.35_20110906.xml','2015-06-11 14:18:26',31,'EXECUTED','3:a617e028a7991b056b34d466e50247d7','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.36_20110908.xml','2015-06-11 14:18:26',29,'EXECUTED','3:9f0b08ab28b09c3cd5559ccf05efce51','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.37_20110909.xml','2015-06-11 14:18:26',33,'EXECUTED','3:8ea4e548c688777daf5a010686a69abb','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.38_20110909.xml','2015-06-11 14:18:26',34,'EXECUTED','3:dbd08fd7af11637f3ec18c4e0cdaaa08','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.39_20110910.xml','2015-06-11 14:18:26',35,'EXECUTED','3:e75a847cd7e41a2aa984911985824603','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.40_20110914.xml','2015-06-11 14:18:26',36,'EXECUTED','3:020d2e669d42a85da2be6d418e656cac','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.41_20110914.xml','2015-06-11 14:18:26',37,'EXECUTED','3:21f51548b2b12b33232e720e5284130c','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.42_20110928.xml','2015-06-11 14:18:26',38,'EXECUTED','3:f8a9f75a42a6a35e83e6e765204bf94b','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.43_20111002.xml','2015-06-11 14:18:26',39,'EXECUTED','3:ecf092992cd7e7f5b93a610f65448a79','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.44_20111031.xml','2015-06-11 14:18:26',40,'EXECUTED','3:f800a41884a55c6d5c6eabff3e0ae22a','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.45_20111031.xml','2015-06-11 14:18:26',41,'EXECUTED','3:b248a10f716a62db98233ac316fff112','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.46_20111101.xml','2015-06-11 14:18:26',42,'EXECUTED','3:79936ac9d9b6c400963e7f7293406817','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.47_20111103.xml','2015-06-11 14:18:26',43,'EXECUTED','3:85ecbd2592182c46da9c0b122b11ec3a','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.48_20111109.xml','2015-06-11 14:18:27',44,'EXECUTED','3:821f5113dfbb7d740eb0d8f1fdd8c274','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.49_20111129.xml','2015-06-11 14:18:27',45,'EXECUTED','3:0a58d7a42ccf234054b4bbe124368825','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.50_20111201.xml','2015-06-11 14:18:27',46,'EXECUTED','3:6abda6237ca8c1540dc54c18cbec078e','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.51_20111202.xml','2015-06-11 14:18:27',47,'EXECUTED','3:a7e0b21ec76aec853ed94886d7cbeca9','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.52_20111202.xml','2015-06-11 14:18:27',48,'EXECUTED','3:564060c5de71b8800bed3fe496aa06f7','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.53_20111207.xml','2015-06-11 14:18:27',49,'EXECUTED','3:8863e444d6293e02f19e80de8bebe4f1','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.54_20111213.xml','2015-06-11 14:18:27',50,'EXECUTED','3:33ed6d08eba87ebb2c5275938f2151f3','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.55_20111213.xml','2015-06-11 14:18:27',51,'EXECUTED','3:63b129b8b8165180b809d5844ea4672e','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.56_20111214.xml','2015-06-11 14:18:27',52,'EXECUTED','3:8c2f92dfd9ef741596b832321ec9209e','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.57_20111214.xml','2015-06-11 14:18:27',53,'EXECUTED','3:e35e8b1ad2d58956adc13085423ef403','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.58_20111216.xml','2015-06-11 14:18:27',54,'EXECUTED','3:94b3e62c3c68b2826b07e665923056c4','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.59_20111216.xml','2015-06-11 14:18:27',55,'EXECUTED','3:aba863df433fdd6bfbd70920739417d7','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.60_20111220.xml','2015-06-11 14:18:27',56,'EXECUTED','3:3f25eb9a59be2fc384d497126a6da093','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.61_20111223.xml','2015-06-11 14:18:27',57,'EXECUTED','3:91e48e654accdb672a5457d59c0c0b89','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.65_20120124.xml','2015-06-11 14:18:27',58,'EXECUTED','3:6d5b1c91d5fcb178d2483be2b432bf4e','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.66_20120125.xml','2015-06-11 14:18:27',59,'EXECUTED','3:3d9557dfad9307d3ba15147c6982b756','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.67_20120126.xml','2015-06-11 14:18:27',60,'EXECUTED','3:39f8d9be221294846cbdcdcd062fc5fd','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.68_20120130.xml','2015-06-11 14:18:27',61,'EXECUTED','3:bde6b189200c878e2dc9e18b6ebe35ce','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.00.69_20120130.xml','2015-06-11 14:18:27',62,'EXECUTED','3:59754ed0155f616fef8203514c496b1a','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.02.01_Sprint-20_US4.xml','2015-06-11 14:18:27',63,'EXECUTED','3:9d07468e305512fe51ae2fb14d8f27b6','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.02.02_Sprint-20_US6.xml','2015-06-11 14:18:27',64,'EXECUTED','3:8e1fd900901f3bea530f407d2909ade5','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.02.03_Sprint-20_US6.xml','2015-06-11 14:18:27',65,'EXECUTED','3:11e283f64b41b8a59b9ed8652eb8768c','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.02.04_Sprint-20_US6.xml','2015-06-11 14:18:27',66,'EXECUTED','3:dc347d9024dfdf82703f72a50d0ca9ff','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.02.05_Sprint-20_US141.xml','2015-06-11 14:18:27',67,'EXECUTED','3:41c9c34915d3cf6701accbd4523f246c','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.02.06_Sprint-20_US5.xml','2015-06-11 14:18:27',68,'EXECUTED','3:90bac7c69ce216d5a40c9682742d2b1d','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.02.07_Sprint-20_US5.xml','2015-06-11 14:18:27',69,'EXECUTED','3:1fe411e376e8f66ccf4b4b8dc66d1c07','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.02.08_Sprint-20_US5.xml','2015-06-11 14:18:27',70,'EXECUTED','3:460fcc71a226c6ed155acf0b5286e59e','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.02.09_Sprint-20_US5.xml','2015-06-11 14:18:27',71,'EXECUTED','3:48f429d09fbbe954cc3c98229c04e661','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_01.02.10_Sprint-21_US137.xml','2015-06-11 14:18:27',72,'EXECUTED','3:c5e4a42f73ba0179b46803bae3bfcc84','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-21_US138_US139.xml','2015-06-11 14:18:27',76,'EXECUTED','3:224d6776564995297474369f6f8f246c','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-21_US144.xml','2015-06-11 14:18:27',77,'EXECUTED','3:bae7676a15265f141a37f204b79a7c36','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-21_US147.xml','2015-06-11 14:18:27',73,'EXECUTED','3:754e36a649936619d250312f8602f9b1','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-21_US148.xml','2015-06-11 14:18:27',74,'EXECUTED','3:bee957e1a34dab8ac4f333ff3d2d06b0','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-21_US151.xml','2015-06-11 14:18:27',75,'EXECUTED','3:644448821f33cce94b3f262414f9d3bf','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-21_US154.xml','2015-06-11 14:18:27',78,'EXECUTED','3:987c8f568e7f5a0291b6702714912cd1','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-22_US156.xml','2015-06-11 14:18:27',79,'EXECUTED','3:d357503e4124f55c78243a17cdcaa96d','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-22_US161.xml','2015-06-11 14:18:27',81,'EXECUTED','3:26d234f1cbc46ebd28be91d22a20f970','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-22_US171.xml','2015-06-11 14:18:27',80,'EXECUTED','3:eacdf7154c33df6ab70cdea0f40d067f','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-23_US182.xml','2015-06-11 14:18:27',83,'EXECUTED','3:7955d3c2912253162349688aea47c339','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-23_US185.xml','2015-06-11 14:18:27',82,'EXECUTED','3:fa14e842a668358d06bcfedb19160030','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-23_US185_b.xml','2015-06-11 14:18:27',84,'EXECUTED','3:d0d06c6ee7108743095ac6d42bf4d29d','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-23_US185_c.xml','2015-06-11 14:18:27',87,'EXECUTED','3:b1f221bac6e65c9bd8164776e0bde1bf','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-23_US203.xml','2015-06-11 14:18:27',85,'EXECUTED','3:ff56ccb8654929e8f12fa1870526bd11','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-23_US203_b.xml','2015-06-11 14:18:27',86,'EXECUTED','3:ce30c1c2800aeb30c8d0b4759e30a5d1','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-23_US203_c.xml','2015-06-11 14:18:28',88,'EXECUTED','3:d1ce3f0f8c5486a90950f2d5beeb8621','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-23_US204.xml','2015-06-11 14:18:28',90,'EXECUTED','3:3579e2cd433d3b1f035a2d2d8036c7d9','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-23_US214.xml','2015-06-11 14:18:28',89,'EXECUTED','3:9f4a436b239bfdad46bd16cc3fd67ea6','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-24_US2551.xml','2015-06-11 14:18:28',114,'EXECUTED','3:d38dfff4f8296be290e128aeeff5581d','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-27_US321.xml','2015-06-11 14:18:28',115,'EXECUTED','3:4b6f4cbf9e96abe31694827024fb66b2','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-29_US2895.xml','2015-06-11 14:18:28',116,'EXECUTED','3:6a38e235b819cb21340b147aeecc69fa','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-30_US2980.xml','2015-06-11 14:18:28',117,'EXECUTED','3:353cecee9c1f3ee1dceb0c67eb54d3e7','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-31_US_3096.xml','2015-06-11 14:18:28',118,'EXECUTED','3:c2065930beca1bfdca409628f0b0edd4','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-33_US_3187_3197.xml','2015-06-11 14:18:28',119,'EXECUTED','3:bcc0f38d037efa3fddcbe7a9ed74ce9a','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-34_US_3304.xml','2015-06-11 14:18:28',120,'EXECUTED','3:ce251a3c284c87c8c61c4d8247f14c21','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-35_add_other_attribs.xml','2015-06-11 14:18:28',123,'EXECUTED','3:20ee825f38dd2d74f2e9f8e895de4dd3','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-35_anatomical_approach_site_attrib.xml','2015-06-11 14:18:28',122,'EXECUTED','3:53c4c3d4bb4dd90bf0cc83b06fd1b2d8','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-35_anatomical_location_site_attrib.xml','2015-06-11 14:18:28',121,'EXECUTED','3:6f616c2999df3af36f809a4004c43b6a','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-35_delete_anatomical_structure_attrib.xml','2015-06-11 14:18:28',124,'EXECUTED','3:55cf09fdcfbe60af9a9d81ddeff08683','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-35_delete_data_types_and_assoc_attribs.xml','2015-06-11 14:18:28',125,'EXECUTED','3:fe790609f946d488be768e5b58208c73','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-36_add_new_attribs.xml','2015-06-11 14:18:28',126,'EXECUTED','3:f3376832229ef8689f140979341cf9cf','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-36_delete_Procedure_Result_datatype.xml','2015-06-11 14:18:28',127,'EXECUTED','3:dace96f5abf554287fd3e6aa7abf531f','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-37_add_new_timing_relationsships.xml','2015-06-11 14:18:28',129,'EXECUTED','3:91386f2a2786e092ab662b9f4bd3ee94','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-37_US3755_US3760_AddRemove_Functions.xml','2015-06-11 14:18:28',128,'EXECUTED','3:09adb147f3febeb729b62ed0df90a416','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-39_US_3872_Add AGE AT Function.xml','2015-06-11 14:18:28',130,'EXECUTED','3:a9aaa50aedfc520ad827cfe7b9cce273','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-40_US3962_edit_Slightly_Alter_Timing_Names.xml','2015-06-11 14:18:29',133,'EXECUTED','3:2b8d544035c5c5378782a3f71b8190ca','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-40_US3962_Slightly_Alter_Timing_Names.xml','2015-06-11 14:18:28',132,'EXECUTED','3:36fdabd51d29f23b1f848e680cac80e2','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-40_US3982_Add_Aditional_Timing_Operators.xml','2015-06-11 14:18:28',131,'EXECUTED','3:aa22e8efd89eff19c315265f6583e211','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-41_US3957_Add_SATISFIES_ALL_SATISFIES_ANY_ Functions.xml','2015-06-11 14:18:29',136,'EXECUTED','3:4a93f46105e42d370d47ccce3adede1e','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-41_US4053_Add_Two_Attributes_To_Medication_Order.xml','2015-06-11 14:18:29',134,'EXECUTED','3:812bb08162fb084387825da4d6677815','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-41_US4057_Add_TargetOutcome_Attribute_To_CareGoal.xml','2015-06-11 14:18:29',135,'EXECUTED','3:b1e8136a024c4cc9fc1c7122a3f66754','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-41_US4117_Rename_Reason_Attribute_To_Cause_For_Patient_Characteristic_Expired.xml','2015-06-11 14:18:29',139,'EXECUTED','3:109ea16518056b9e79835042a4317331','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-41_US4122_Remove_SystemCharacteristic_From_DataType.xml','2015-06-11 14:18:29',137,'EXECUTED','3:b167ff3ef1323de8f00112c8c11af4dd','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-41_US4127_US4132_US4137_Remove_Status_Date_Time_Attributes.xml','2015-06-11 14:18:29',138,'EXECUTED','3:6c2b93747e94cc99edd6ee1cacd4a8b6','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-42_delete_attributes_add_fulfills_relation.xml','2015-06-11 14:18:29',140,'EXECUTED','3:915c9cee5ece46e1cbee6c784ca50bc9','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-42_US4237_US4242_US4247_delete_attributes.xml','2015-06-11 14:18:29',141,'EXECUTED','3:7eee2b4529dfd5ce3ad041b1bc4d8583','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-43_US4301_Rename_4_Timing_Relationships.xml','2015-06-11 14:18:29',142,'EXECUTED','3:b1d86f40a35e296e38baa2639b443526','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-43_US4371_Adding_Birthdate_and_Expired.xml','2015-06-11 14:18:29',143,'EXECUTED','3:bc0c53da843937ae30a04a9d772d3ff5','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-44_US4558_New.xml','2015-06-11 14:18:29',144,'EXECUTED','3:42e8c3385f4a927816d602b2ee84cf75','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-44_US4573.xml','2015-06-11 14:18:29',145,'EXECUTED','3:f385665d42f2229c5784487895caf0f4','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_sprint-46_US4697_US4752_US4757.xml','2015-06-11 14:18:29',146,'EXECUTED','3:7bcfbc12602da24429c66932cd517f8c','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-50_delete_anatomical_approach_site_attr_Physical_Exams_dataType.xml','2015-06-11 14:18:29',147,'EXECUTED','3:4e5d261ac7b2cfcf45bea0850500a372','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-50_delete_method_attr_Prodecure_Intolerance_dataType.xml','2015-06-11 14:18:29',148,'EXECUTED','3:050c4162cbd33deee8ea42ec67143c9d','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-54_US5194_20141124.xml','2015-06-11 14:18:29',149,'EXECUTED','3:202ae947d641f3434c27f772f496a830','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-58_rename_ECWSO_SCWEO.xml','2015-06-11 14:18:29',151,'EXECUTED','3:1439d14ae8151dcddb711867052426d7','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-63_US-5568_AlterCaseOfSetOpsAndFuncOps.xml','2015-06-11 14:18:29',153,'EXECUTED','3:e253e404686eeeea8296bb33218fad84','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint-65_USMAT-5666.xml','2015-06-11 14:18:29',154,'EXECUTED','3:d5c24c6f61f699685c398bccc55154c2','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint16_US381.xml','2015-06-11 14:18:28',105,'EXECUTED','3:331ef604c683421e3e8220659a4a34e2','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_SprintOandM_US230.xml','2015-06-11 14:18:28',91,'EXECUTED','3:1c6bb16d2138562a2e395a06b43eb28a','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_SprintOandM_US231.xml','2015-06-11 14:18:28',92,'EXECUTED','3:9b65a4ca428b9e1d0bda6459a343ef06','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_SprintOandM_US233.xml','2015-06-11 14:18:28',93,'EXECUTED','3:129a88e43c90fd425d517e0f32e8d2b6','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_SprintOandM_US236_US237.xml','2015-06-11 14:18:28',94,'EXECUTED','3:e0cb4ffd8995dfc95a5ecfaab3924fb4','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_03_US_MAT110.xml','2015-06-11 14:18:28',95,'EXECUTED','3:8216b98d70100fd3266e3daf7b5363f0','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_04_Task_MAT630.xml','2015-06-11 14:18:28',96,'EXECUTED','3:72caf4d34f64711a14199c823bd99737','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_05_Task_MAT286.xml','2015-06-11 14:18:28',98,'EXECUTED','3:c28375165fcbcd2692376790b81c7808','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_06_US_MAT786.xml','2015-06-11 14:18:28',97,'EXECUTED','3:9bb785c143840f48fe6b2b6e2b36cd03','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_08_Task_MAT996.xml','2015-06-11 14:18:28',99,'EXECUTED','3:8bb80bf0012e7718e7368599970d4b74','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_09_US_MAT1155.xml','2015-06-11 14:18:28',100,'EXECUTED','3:f1cedfe5e2629e92a1125979f59e46c7','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_09_US_MAT1155_UPDATE.xml','2015-06-11 14:18:28',101,'EXECUTED','3:7645b3796d2dc57aa18d8c0e791542ff','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_11_US_MAT1053.xml','2015-06-11 14:18:28',102,'EXECUTED','3:b14125eb62e1cad9c9ac65577fc9eaca','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_12_Task_MAT1427.xml','2015-06-11 14:18:28',103,'EXECUTED','3:177b6126acd85748d6276f41eeb5251e','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_14_Task_MAT1804.xml','2015-06-11 14:18:28',104,'EXECUTED','3:3094c9a1e81f2219feb6dfc0ee43787f','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_17_US_MAT1641.xml','2015-06-11 14:18:28',106,'EXECUTED','3:d1c995cf014eacd61985fade819fa0d4','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_18_Task_MAT2074.xml','2015-06-11 14:18:28',107,'EXECUTED','3:28717777bbbd31ee21c456d7f588f8f8','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_18_US2080.xml','2015-06-11 14:18:28',110,'EXECUTED','3:51f42771017c355aaa42493df84e3385','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_18_US_MAT2123.xml','2015-06-11 14:18:28',108,'EXECUTED','3:a0f487d4c83a3d44414967bb34831a0a','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_18_US_MAT2123_1.xml','2015-06-11 14:18:28',109,'EXECUTED','3:e09b23916ba413879f0776894602a3bc','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_20_US2136.xml','2015-06-11 14:18:28',111,'EXECUTED','3:4747603246104ce00b6acf3c4f23b02a','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_20_US_MAT_1337.xml','2015-06-11 14:18:28',112,'EXECUTED','3:4fecd4836ce2f1a48ca1bd6a69b9aed9','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_20_US_MAT_2270_Modified.xml','2015-06-11 14:18:28',113,'EXECUTED','3:d624fd9d02240bc21af875b19b03c250','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_55_Change_Timing_ShortNames.xml','2015-06-11 14:18:29',150,'EXECUTED','3:a2996ba755fc0b37ce4536644edcf915','Custom SQL','',NULL,'2.0.1'),('1','mat_dev_user','classpath:/liquibase/deploy_Sprint_59_Revoked_User_Org_Change.xml','2015-06-11 14:18:29',152,'EXECUTED','3:f1fd37957b28488d82639b54386004d2','Custom SQL','',NULL,'2.0.1');
/*!40000 ALTER TABLE `DATABASECHANGELOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATABASECHANGELOGLOCK`
--

DROP TABLE IF EXISTS `DATABASECHANGELOGLOCK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
INSERT INTO `DATABASECHANGELOGLOCK` VALUES (1,0,NULL,NULL);
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATA_TYPE`
--

DROP TABLE IF EXISTS `DATA_TYPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
INSERT INTO `DATA_TYPE` VALUES ('1','Patient Care Experience','1'),('10','Diagnosis, Resolved','4'),('100','Patient Characteristic Payer','9'),('101','Patient Characteristic Sex','9'),('102','Patient Characteristic Ethnicity','9'),('103','Patient Characteristic Race','9'),('104','Medication, Discharge','12'),('11','Device, Adverse Event','5'),('12','Device, Allergy','5'),('13','Device, Applied','5'),('14','Device, Intolerance','5'),('15','Device, Order','5'),('16','Diagnostic Study, Adverse Event','6'),('17','Diagnostic Study, Intolerance','6'),('18','Diagnostic Study, Order','6'),('19','Diagnostic Study, Performed','6'),('2','Provider Care Experience','1'),('21','Encounter, Order','7'),('22','Encounter, Performed','7'),('23','Functional Status, Order','8'),('24','Functional Status, Performed','8'),('26','Patient Characteristic','9'),('27','Provider Characteristic','9'),('28','Intervention, Adverse Event','10'),('29','Intervention, Intolerance','10'),('3','Care Goal','2'),('30','Intervention, Order','10'),('31','Intervention, Performed','10'),('33','Laboratory Test, Adverse Event','11'),('34','Laboratory Test, Intolerance','11'),('35','Laboratory Test, Order','11'),('36','Laboratory Test, Performed','11'),('38','Medication, Active','12'),('39','Medication, Administered','12'),('4','Communication: From Provider to Provider','3'),('40','Medication, Adverse Effects','12'),('41','Medication, Allergy','12'),('42','Medication, Dispensed','12'),('43','Medication, Intolerance','12'),('44','Medication, Order','12'),('5','Communication: From Patient to Provider','3'),('56','Physical Exam, Order','14'),('57','Physical Exam, Performed','14'),('6','Communication: From Provider to Patient','3'),('60','Procedure, Adverse Event','16'),('61','Procedure, Intolerance','16'),('62','Procedure, Order','16'),('63','Procedure, Performed','16'),('65','Risk Category Assessment','17'),('66','Substance, Administered','18'),('67','Substance, Adverse Event','18'),('68','Substance, Allergy','18'),('69','Substance, Intolerance','18'),('7','Diagnosis, Active','4'),('70','Substance, Order','18'),('71','Symptom, Active','19'),('72','Symptom, Assessed','19'),('73','Symptom, Inactive','19'),('74','Symptom, Resolved','19'),('76','Transfer From','21'),('77','Transfer To','21'),('78','Device, Recommended','5'),('79','Encounter, Recommended','7'),('8','Diagnosis, Family History','4'),('80','Functional Status, Recommended','8'),('81','Intervention, Recommended','10'),('82','Laboratory Test, Recommended','11'),('87','Physical Exam, Recommended','14'),('88','Procedure, Recommended','16'),('89','Substance, Recommended','18'),('9','Diagnosis, Inactive','4'),('90','Diagnostic Study, Recommended','6'),('92','attribute','23'),('95','Encounter, Active','7'),('96','Timing Element','22'),('97','Patient Characteristic Birthdate','9'),('98','Patient Characteristic Expired','9'),('99','Patient Characteristic Clinical Trial Participant','9');
/*!40000 ALTER TABLE `DATA_TYPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DECISION`
--

DROP TABLE IF EXISTS `DECISION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
-- Table structure for table `GROUPED_CODE_LISTS`
--

DROP TABLE IF EXISTS `GROUPED_CODE_LISTS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
INSERT INTO `LIST_OBJECT` VALUES ('8a4d8c81309da15201309e46121d00d4','Measurement Period','80','2.16.840.1.113883.3.67.1.101.1.53','N/A',NULL,NULL,'22','1','60',NULL,NULL,NULL,'Default Measurement CodeList','1970-01-01 06:00:00',0),('8a4d8c81309da15201309e46124800e4','Measurement Start Date','80','2.16.840.1.113883.3.67.1.101.1.54','N/A',NULL,NULL,'22','1','60',NULL,NULL,NULL,'Default Measurement CodeList','1970-01-01 06:00:00',0),('8a4d8c81309da15201309e4612567F35','Expired','85','419099009','N/A',NULL,NULL,'9','2015','69',NULL,NULL,NULL,NULL,'2015-06-11 19:18:29',0),('8a4d8c81309da15201309e46126d00f4','Measurement End Date','80','2.16.840.1.113883.3.67.1.101.1.55','N/A',NULL,NULL,'22','1','60',NULL,NULL,NULL,'Default Measurement CodeList','1970-01-01 06:00:00',0),('8a4d8c81309da15201309e46126DA2E0','Birthdate','85','21112-8','N/A',NULL,NULL,'9','2015','69',NULL,NULL,NULL,NULL,'2015-06-11 19:18:29',0),('8ae452962e3a223a012e3a254b808889','Male','80','2.16.840.1.113883.3.560.100.1','N/A',NULL,NULL,'9','HL7 v2.5','132',NULL,NULL,'National Quality Forum','Default Gender CodeList','2011-07-27 15:47:00',0),('8ae452962e3a223a012e3a254b808890','Female','80','2.16.840.1.113883.3.560.100.2','N/A',NULL,NULL,'9','HL7 v2.5','132',NULL,NULL,'National Quality Forum','Default Gender CodeList','2011-07-27 15:47:00',0),('8ae452962e3a223a012e3a254b808891','Unknown Sex','80','2.16.840.1.113883.3.560.100.3','N/A',NULL,NULL,'9','HL7 v2.5','132',NULL,NULL,'National Quality Forum','Default Gender CodeList','2011-07-27 15:47:00',0),('8ae452962e3a223a012e3a254b808892','birth date','80','2.16.840.1.113883.3.560.100.4','N/A',NULL,NULL,'9','2.36','130',NULL,NULL,'National Quality Forum','Default Gender CodeList','2011-09-20 05:00:00',0),('bae50f18267111e1a17a78acc0b65c43','ONC Administrative Sex','86','2.16.840.1.113762.1.4.1','N/A',NULL,NULL,'9','HL7 v2.5','132',NULL,NULL,'National Library of Medicine','Supplimental CodeList','2011-07-27 15:47:00',1),('bae85d87267111e1a17a78acc0b65c43','Race','87','2.16.840.1.114222.4.11.836','N/A',NULL,NULL,'9','1.0','133',NULL,NULL,'CDC NCHS','Supplimental CodeList','2007-03-30 05:00:00',1),('bae86046267111e1a17a78acc0b65c43','Ethnicity','87','2.16.840.1.114222.4.11.837','N/A',NULL,NULL,'9','1.0','133',NULL,NULL,'CDC NCHS','Supplimental CodeList','2007-03-30 05:00:00',1),('bae86261267111e1a17a78acc0b65c43','Payer','88','2.16.840.1.114222.4.11.3591','N/A',NULL,NULL,'9','4.0','134',NULL,NULL,'PHDSC','Supplimental CodeList','2011-10-01 05:00:00',1);
/*!40000 ALTER TABLE `LIST_OBJECT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MAT_FLAG`
--

DROP TABLE IF EXISTS `MAT_FLAG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
  PRIMARY KEY (`ID`),
  KEY `MEASURE_OWNER_FK` (`MEASURE_OWNER_ID`),
  KEY `MEASURE_LOCK_USER_FK` (`LOCKED_USER_ID`),
  KEY `MEASURE_SET_FK` (`MEASURE_SET_ID`),
  CONSTRAINT `MEASURE_LOCK_USER_FK` FOREIGN KEY (`LOCKED_USER_ID`) REFERENCES `USER` (`USER_ID`),
  CONSTRAINT `MEASURE_OWNER_FK` FOREIGN KEY (`MEASURE_OWNER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `MEASURE_SET_FK` FOREIGN KEY (`MEASURE_SET_ID`) REFERENCES `MEASURE_SET` (`ID`)
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
-- Table structure for table `MEASURE_EXPORT`
--

DROP TABLE IF EXISTS `MEASURE_EXPORT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MEASURE_EXPORT` (
  `MEASURE_EXPORT_ID` varchar(64) NOT NULL,
  `MEASURE_ID` varchar(64) NOT NULL,
  `SIMPLE_XML` longtext NOT NULL,
  `CODE_LIST` longblob,
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
-- Table structure for table `MEASURE_NOTES`
--

DROP TABLE IF EXISTS `MEASURE_NOTES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MEASURE_NOTES` (
  `ID` varchar(32) NOT NULL,
  `MEASURE_ID` varchar(32) NOT NULL,
  `TITLE` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(3000) DEFAULT NULL,
  `CREATE_USER_ID` varchar(40) NOT NULL,
  `MODIFY_USER_ID` varchar(40) DEFAULT NULL,
  `LAST_MODIFIED_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  KEY `MEASURE_MEASURE_ID_FK` (`MEASURE_ID`),
  KEY `USER_CREATE_USER_ID_FK` (`CREATE_USER_ID`),
  KEY `USER_MODIFY_USER_ID_FK` (`MODIFY_USER_ID`),
  CONSTRAINT `MEASURE_NOTES_ibfk_1` FOREIGN KEY (`MEASURE_ID`) REFERENCES `MEASURE` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `MEASURE_NOTES_ibfk_2` FOREIGN KEY (`CREATE_USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE,
  CONSTRAINT `MEASURE_NOTES_ibfk_3` FOREIGN KEY (`MODIFY_USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEASURE_NOTES`
--

LOCK TABLES `MEASURE_NOTES` WRITE;
/*!40000 ALTER TABLE `MEASURE_NOTES` DISABLE KEYS */;
/*!40000 ALTER TABLE `MEASURE_NOTES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_NOTES_BACKUP`
--

DROP TABLE IF EXISTS `MEASURE_NOTES_BACKUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
-- Table structure for table `MEASURE_SCORE`
--

DROP TABLE IF EXISTS `MEASURE_SCORE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MEASURE_TYPES` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MEASURE_TYPES`
--

LOCK TABLES `MEASURE_TYPES` WRITE;
/*!40000 ALTER TABLE `MEASURE_TYPES` DISABLE KEYS */;
INSERT INTO `MEASURE_TYPES` VALUES ('1','Composite'),('2','Cost/Resource Use'),('3','Efficiency'),('4','Outcome'),('5','Patient Engagement/Experience'),('6','Process'),('7','Structure');
/*!40000 ALTER TABLE `MEASURE_TYPES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MEASURE_VALIDATION_LOG`
--

DROP TABLE IF EXISTS `MEASURE_VALIDATION_LOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
-- Table structure for table `OBJECT_STATUS`
--

DROP TABLE IF EXISTS `OBJECT_STATUS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QDM_ATTRIBUTES` (
  `ID` varchar(64) NOT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `DATA_TYPE_ID` varchar(32) DEFAULT NULL,
  `QDM_ATTRIBUTE_TYPE` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QDM_ATTRIBUTES`
--

LOCK TABLES `QDM_ATTRIBUTES` WRITE;
/*!40000 ALTER TABLE `QDM_ATTRIBUTES` DISABLE KEYS */;
INSERT INTO `QDM_ATTRIBUTES` VALUES ('10','provider preference','1','Data Type'),('1001','facility location','19','Data Type'),('101','negation rationale','14','Data Type'),('1010','start datetime','97','Data Type'),('1011','stop datetime','97','Data Type'),('1012','date','98','Data Type'),('1013','time','98','Data Type'),('1014','cause','98','Data Type'),('1015','start datetime','99','Data Type'),('1016','stop datetime','99','Data Type'),('1017','reason','99','Data Type'),('1018','start datetime','100','Data Type'),('1019','stop datetime','100','Data Type'),('102','patient preference','14','Data Type'),('1020','start datetime','101','Data Type'),('1021','stop datetime','101','Data Type'),('1023','dose','104','Data Type'),('1024','frequency','104','Data Type'),('1026','refills','104','Data Type'),('1027','route','104','Data Type'),('1028','start datetime','104','Data Type'),('1029','stop datetime','104','Data Type'),('103','provider preference','14','Data Type'),('1030','negation rationale','104','Data Type'),('1031','anatomical location site','7','Data Type'),('1032','anatomical location site','10','Data Type'),('1033','anatomical location site','9','Data Type'),('1034','anatomical location site','13','Data Type'),('1038','anatomical location site','56','Data Type'),('1039','anatomical location site','57','Data Type'),('104','reaction','14','Data Type'),('1040','anatomical location site','87','Data Type'),('1041','anatomical location site','62','Data Type'),('1042','anatomical location site','63','Data Type'),('1043','anatomical approach site','13','Data Type'),('105','start datetime','14','Data Type'),('1050','anatomical approach site','63','Data Type'),('1051','anatomical approach site','62','Data Type'),('1052','result','19','Data Type'),('1053','result','36','Data Type'),('1054','result','57','Data Type'),('1055','result','24','Data Type'),('1056','result','31','Data Type'),('1057','status','19','Data Type'),('1058','status','31','Data Type'),('1059','status','36','Data Type'),('106','stop datetime','14','Data Type'),('1060','patient preference','104','Data Type'),('1061','provider preference','104','Data Type'),('1062','cumulative medication duration','39','Data Type'),('1064','ordinality','61','Data Type'),('1065','radiation duration','62','Data Type'),('1066','status','63','Data Type'),('1067','radiation duration','63','Data Type'),('1068','radiation dosage','63','Data Type'),('1069','anatomical approach site','88','Data Type'),('1070','anatomical location site','88','Data Type'),('1071','active datetime','44','Data Type'),('1072','signed datetime','44','Data Type'),('1073','target outcome','3','Data Type'),('108','negation rationale','15','Data Type'),('109','patient preference','15','Data Type'),('11','start datetime','1','Data Type'),('110','provider preference','15','Data Type'),('111','reason','15','Data Type'),('112','start datetime','15','Data Type'),('113','stop datetime','15','Data Type'),('115','negation rationale','78','Data Type'),('116','patient preference','78','Data Type'),('117','provider preference','78','Data Type'),('118','reason','78','Data Type'),('119','start datetime','78','Data Type'),('12','stop datetime','1','Data Type'),('120','stop datetime','78','Data Type'),('122','negation rationale','16','Data Type'),('123','patient preference','16','Data Type'),('124','provider preference','16','Data Type'),('125','radiation dosage','16','Data Type'),('126','radiation duration','16','Data Type'),('127','reaction','16','Data Type'),('128','start datetime','16','Data Type'),('129','stop datetime','16','Data Type'),('131','negation rationale','17','Data Type'),('132','patient preference','17','Data Type'),('133','provider preference','17','Data Type'),('134','radiation dosage','17','Data Type'),('135','radiation duration','17','Data Type'),('136','reaction','17','Data Type'),('137','start datetime','17','Data Type'),('138','stop datetime','17','Data Type'),('14','negation rationale','2','Data Type'),('140','method','18','Data Type'),('141','negation rationale','18','Data Type'),('142','patient preference','18','Data Type'),('143','provider preference','18','Data Type'),('144','radiation dosage','18','Data Type'),('145','radiation duration','18','Data Type'),('146','reason','18','Data Type'),('147','start datetime','18','Data Type'),('148','stop datetime','18','Data Type'),('15','patient preference','2','Data Type'),('150','method','19','Data Type'),('151','negation rationale','19','Data Type'),('152','patient preference','19','Data Type'),('153','provider preference','19','Data Type'),('154','radiation dosage','19','Data Type'),('155','radiation duration','19','Data Type'),('156','reason','19','Data Type'),('157','start datetime','19','Data Type'),('158','stop datetime','19','Data Type'),('16','provider preference','2','Data Type'),('160','method','90','Data Type'),('161','negation rationale','90','Data Type'),('162','patient preference','90','Data Type'),('163','provider preference','90','Data Type'),('164','radiation dosage','90','Data Type'),('165','radiation duration','90','Data Type'),('166','start datetime','90','Data Type'),('167','stop datetime','90','Data Type'),('17','start datetime','2','Data Type'),('179','length of stay','95','Data Type'),('18','stop datetime','2','Data Type'),('181','facility location','95','Data Type'),('182','negation rationale','95','Data Type'),('183','patient preference','95','Data Type'),('184','provider preference','95','Data Type'),('185','reason','95','Data Type'),('186','admission datetime','95','Data Type'),('187','discharge datetime','95','Data Type'),('198','facility location','21','Data Type'),('199','negation rationale','21','Data Type'),('20','negation rationale','3','Data Type'),('200','patient preference','21','Data Type'),('201','provider preference','21','Data Type'),('202','reason','21','Data Type'),('203','start datetime','21','Data Type'),('204','stop datetime','21','Data Type'),('205','length of stay','22','Data Type'),('207','facility location','22','Data Type'),('208','negation rationale','22','Data Type'),('209','patient preference','22','Data Type'),('21','patient preference','3','Data Type'),('210','provider preference','22','Data Type'),('211','reason','22','Data Type'),('212','admission datetime','22','Data Type'),('213','discharge datetime','22','Data Type'),('215','facility location','79','Data Type'),('216','negation rationale','79','Data Type'),('217','patient preference','79','Data Type'),('218','provider preference','79','Data Type'),('219','reason','79','Data Type'),('22','provider preference','3','Data Type'),('220','start datetime','79','Data Type'),('221','stop datetime','79','Data Type'),('223','method','23','Data Type'),('224','negation rationale','23','Data Type'),('225','patient preference','23','Data Type'),('226','provider preference','23','Data Type'),('227','reason','23','Data Type'),('228','start datetime','23','Data Type'),('229','stop datetime','23','Data Type'),('23','start datetime','3','Data Type'),('231','method','24','Data Type'),('232','negation rationale','24','Data Type'),('233','patient preference','24','Data Type'),('234','provider preference','24','Data Type'),('235','reason','24','Data Type'),('236','start datetime','24','Data Type'),('237','stop datetime','24','Data Type'),('239','method','80','Data Type'),('24','stop datetime','3','Data Type'),('240','negation rationale','80','Data Type'),('241','patient preference','80','Data Type'),('242','provider preference','80','Data Type'),('243','reason','80','Data Type'),('244','start datetime','80','Data Type'),('245','stop datetime','80','Data Type'),('256','start datetime','26','Data Type'),('257','stop datetime','26','Data Type'),('259','negation rationale','27','Data Type'),('26','negation rationale','5','Data Type'),('260','start datetime','27','Data Type'),('261','stop datetime','27','Data Type'),('263','negation rationale','28','Data Type'),('264','patient preference','28','Data Type'),('265','provider preference','28','Data Type'),('266','reaction','28','Data Type'),('267','start datetime','28','Data Type'),('268','stop datetime','28','Data Type'),('27','patient preference','5','Data Type'),('270','negation rationale','29','Data Type'),('271','patient preference','29','Data Type'),('272','provider preference','29','Data Type'),('273','reaction','29','Data Type'),('274','start datetime','29','Data Type'),('275','stop datetime','29','Data Type'),('278','negation rationale','30','Data Type'),('279','patient preference','30','Data Type'),('28','provider preference','5','Data Type'),('280','provider preference','30','Data Type'),('281','reason','30','Data Type'),('282','start datetime','30','Data Type'),('283','stop datetime','30','Data Type'),('286','negation rationale','31','Data Type'),('287','patient preference','31','Data Type'),('288','provider preference','31','Data Type'),('289','reason','31','Data Type'),('29','start datetime','5','Data Type'),('290','start datetime','31','Data Type'),('291','stop datetime','31','Data Type'),('294','negation rationale','81','Data Type'),('295','patient preference','81','Data Type'),('296','provider preference','81','Data Type'),('297','reason','81','Data Type'),('298','start datetime','81','Data Type'),('299','stop datetime','81','Data Type'),('30','stop datetime','5','Data Type'),('310','negation rationale','33','Data Type'),('311','patient preference','33','Data Type'),('312','provider preference','33','Data Type'),('313','reaction','33','Data Type'),('314','start datetime','33','Data Type'),('315','stop datetime','33','Data Type'),('317','negation rationale','34','Data Type'),('318','patient preference','34','Data Type'),('319','provider preference','34','Data Type'),('32','negation rationale','6','Data Type'),('320','reaction','34','Data Type'),('321','start datetime','34','Data Type'),('322','stop datetime','34','Data Type'),('324','method','35','Data Type'),('325','negation rationale','35','Data Type'),('326','patient preference','35','Data Type'),('327','provider preference','35','Data Type'),('328','reason','35','Data Type'),('329','start datetime','35','Data Type'),('33','patient preference','6','Data Type'),('330','stop datetime','35','Data Type'),('332','method','36','Data Type'),('333','negation rationale','36','Data Type'),('334','patient preference','36','Data Type'),('335','provider preference','36','Data Type'),('336','reason','36','Data Type'),('337','start datetime','36','Data Type'),('338','stop datetime','36','Data Type'),('34','provider preference','6','Data Type'),('340','method','82','Data Type'),('341','negation rationale','82','Data Type'),('342','patient preference','82','Data Type'),('343','provider preference','82','Data Type'),('344','reason','82','Data Type'),('345','start datetime','82','Data Type'),('346','stop datetime','82','Data Type'),('35','start datetime','6','Data Type'),('356','cumulative medication duration','38','Data Type'),('357','dose','38','Data Type'),('358','frequency','38','Data Type'),('36','stop datetime','6','Data Type'),('360','negation rationale','38','Data Type'),('362','patient preference','38','Data Type'),('363','provider preference','38','Data Type'),('365','route','38','Data Type'),('366','start datetime','38','Data Type'),('367','stop datetime','38','Data Type'),('368','dose','39','Data Type'),('369','frequency','39','Data Type'),('371','negation rationale','39','Data Type'),('373','patient preference','39','Data Type'),('374','provider preference','39','Data Type'),('376','route','39','Data Type'),('377','start datetime','39','Data Type'),('378','stop datetime','39','Data Type'),('38','negation rationale','4','Data Type'),('382','negation rationale','40','Data Type'),('384','patient preference','40','Data Type'),('385','provider preference','40','Data Type'),('386','reaction','40','Data Type'),('389','start datetime','40','Data Type'),('39','patient preference','4','Data Type'),('390','stop datetime','40','Data Type'),('394','negation rationale','41','Data Type'),('396','patient preference','41','Data Type'),('397','provider preference','41','Data Type'),('398','reaction','41','Data Type'),('40','provider preference','4','Data Type'),('401','start datetime','41','Data Type'),('402','stop datetime','41','Data Type'),('403','cumulative medication duration','42','Data Type'),('404','dose','42','Data Type'),('405','frequency','42','Data Type'),('407','negation rationale','42','Data Type'),('409','patient preference','42','Data Type'),('41','start datetime','4','Data Type'),('410','provider preference','42','Data Type'),('411','refills','42','Data Type'),('412','route','42','Data Type'),('413','start datetime','42','Data Type'),('414','stop datetime','42','Data Type'),('418','negation rationale','43','Data Type'),('42','stop datetime','4','Data Type'),('420','patient preference','43','Data Type'),('421','provider preference','43','Data Type'),('422','reaction','43','Data Type'),('425','start datetime','43','Data Type'),('426','stop datetime','43','Data Type'),('427','cumulative medication duration','44','Data Type'),('428','dose','44','Data Type'),('429','frequency','44','Data Type'),('431','method','44','Data Type'),('432','negation rationale','44','Data Type'),('434','patient preference','44','Data Type'),('435','provider preference','44','Data Type'),('436','reason','44','Data Type'),('437','refills','44','Data Type'),('438','route','44','Data Type'),('439','start datetime','44','Data Type'),('44','negation rationale','7','Data Type'),('440','stop datetime','44','Data Type'),('45','ordinality','7','Data Type'),('453','method','56','Data Type'),('454','negation rationale','56','Data Type'),('455','patient preference','56','Data Type'),('456','provider preference','56','Data Type'),('457','reason','56','Data Type'),('458','start datetime','56','Data Type'),('459','stop datetime','56','Data Type'),('46','patient preference','7','Data Type'),('462','method','57','Data Type'),('463','negation rationale','57','Data Type'),('464','patient preference','57','Data Type'),('465','provider preference','57','Data Type'),('466','reason','57','Data Type'),('467','start datetime','57','Data Type'),('468','stop datetime','57','Data Type'),('47','provider preference','7','Data Type'),('471','method','87','Data Type'),('472','negation rationale','87','Data Type'),('473','patient preference','87','Data Type'),('474','provider preference','87','Data Type'),('475','reason','87','Data Type'),('476','start datetime','87','Data Type'),('477','stop datetime','87','Data Type'),('479','negation rationale','60','Data Type'),('48','severity','7','Data Type'),('480','patient preference','60','Data Type'),('481','provider preference','60','Data Type'),('482','reaction','60','Data Type'),('483','start datetime','60','Data Type'),('484','stop datetime','60','Data Type'),('486','negation rationale','61','Data Type'),('487','patient preference','61','Data Type'),('488','provider preference','61','Data Type'),('489','reaction','61','Data Type'),('49','start datetime','7','Data Type'),('490','start datetime','61','Data Type'),('491','stop datetime','61','Data Type'),('493','method','62','Data Type'),('494','negation rationale','62','Data Type'),('495','patient preference','62','Data Type'),('496','provider preference','62','Data Type'),('497','reason','62','Data Type'),('498','start datetime','62','Data Type'),('499','stop datetime','62','Data Type'),('501','method','63','Data Type'),('502','negation rationale','63','Data Type'),('503','patient preference','63','Data Type'),('504','provider preference','63','Data Type'),('505','reason','63','Data Type'),('506','start datetime','63','Data Type'),('507','stop datetime','63','Data Type'),('509','method','88','Data Type'),('510','negation rationale','88','Data Type'),('511','patient preference','88','Data Type'),('512','provider preference','88','Data Type'),('513','reason','88','Data Type'),('514','start datetime','88','Data Type'),('515','stop datetime','88','Data Type'),('52','stop datetime','7','Data Type'),('526','negation rationale','65','Data Type'),('527','patient preference','65','Data Type'),('528','provider preference','65','Data Type'),('529','start datetime','65','Data Type'),('530','stop datetime','65','Data Type'),('531','dose','66','Data Type'),('533','frequency','66','Data Type'),('534','negation rationale','66','Data Type'),('536','patient preference','66','Data Type'),('537','provider preference','66','Data Type'),('539','route','66','Data Type'),('54','negation rationale','8','Data Type'),('540','start datetime','66','Data Type'),('541','stop datetime','66','Data Type'),('545','negation rationale','67','Data Type'),('547','patient preference','67','Data Type'),('548','provider preference','67','Data Type'),('549','reaction','67','Data Type'),('55','ordinality','8','Data Type'),('552','start datetime','67','Data Type'),('553','stop datetime','67','Data Type'),('557','negation rationale','68','Data Type'),('559','patient preference','68','Data Type'),('56','patient preference','8','Data Type'),('560','provider preference','68','Data Type'),('561','reaction','68','Data Type'),('564','start datetime','68','Data Type'),('565','stop datetime','68','Data Type'),('569','negation rationale','69','Data Type'),('57','provider preference','8','Data Type'),('571','patient preference','69','Data Type'),('572','provider preference','69','Data Type'),('573','reaction','69','Data Type'),('576','start datetime','69','Data Type'),('577','stop datetime','69','Data Type'),('578','dose','70','Data Type'),('58','severity','8','Data Type'),('580','frequency','70','Data Type'),('581','method','70','Data Type'),('582','negation rationale','70','Data Type'),('584','patient preference','70','Data Type'),('585','provider preference','70','Data Type'),('586','reason','70','Data Type'),('587','refills','70','Data Type'),('588','route','70','Data Type'),('589','start datetime','70','Data Type'),('59','start datetime','8','Data Type'),('590','stop datetime','70','Data Type'),('591','dose','89','Data Type'),('593','frequency','89','Data Type'),('594','method','89','Data Type'),('595','negation rationale','89','Data Type'),('597','patient preference','89','Data Type'),('598','provider preference','89','Data Type'),('599','reason','89','Data Type'),('60','status','8','Data Type'),('600','refills','89','Data Type'),('601','route','89','Data Type'),('602','start datetime','89','Data Type'),('603','stop datetime','89','Data Type'),('605','negation rationale','71','Data Type'),('606','ordinality','71','Data Type'),('607','patient preference','71','Data Type'),('608','provider preference','71','Data Type'),('609','severity','71','Data Type'),('61','stop datetime','8','Data Type'),('610','start datetime','71','Data Type'),('612','stop datetime','71','Data Type'),('614','negation rationale','72','Data Type'),('615','ordinality','72','Data Type'),('616','patient preference','72','Data Type'),('617','provider preference','72','Data Type'),('618','severity','72','Data Type'),('619','start datetime','72','Data Type'),('621','stop datetime','72','Data Type'),('623','negation rationale','73','Data Type'),('624','ordinality','73','Data Type'),('625','patient preference','73','Data Type'),('626','provider preference','73','Data Type'),('627','severity','73','Data Type'),('628','start datetime','73','Data Type'),('63','negation rationale','9','Data Type'),('630','stop datetime','73','Data Type'),('632','negation rationale','74','Data Type'),('633','ordinality','74','Data Type'),('634','patient preference','74','Data Type'),('635','provider preference','74','Data Type'),('636','severity','74','Data Type'),('637','start datetime','74','Data Type'),('639','stop datetime','74','Data Type'),('64','ordinality','9','Data Type'),('641','negation rationale','75','Data Type'),('642','start datetime','75','Data Type'),('643','stop datetime','75','Data Type'),('645','negation rationale','76','Data Type'),('646','patient preference','76','Data Type'),('647','provider preference','76','Data Type'),('648','start datetime','76','Data Type'),('649','stop datetime','76','Data Type'),('65','patient preference','9','Data Type'),('651','negation rationale','77','Data Type'),('652','patient preference','77','Data Type'),('653','provider preference','77','Data Type'),('654','start datetime','77','Data Type'),('655','stop datetime','77','Data Type'),('66','provider preference','9','Data Type'),('662','Health Record Field','','Data Flow'),('663','laterality','7','Data Type'),('664','reason','13','Data Type'),('665','discharge status','22','Data Type'),('669','facility location arrival datetime','95','Data Type'),('67','severity','9','Data Type'),('670','facility location departure datetime','95','Data Type'),('671','facility location arrival datetime','22','Data Type'),('672','facility location departure datetime','22','Data Type'),('673','reason','39','Data Type'),('676','ordinality','62','Data Type'),('677','ordinality','63','Data Type'),('678','result','63','Data Type'),('679','incision datetime','63','Data Type'),('68','start datetime','9','Data Type'),('680','ordinality','88','Data Type'),('685','result','65','Data Type'),('689','related to','3','Data Type'),('70','stop datetime','9','Data Type'),('701','Source','','Data Flow'),('702','Recorder','','Data Flow'),('72','negation rationale','10','Data Type'),('73','ordinality','10','Data Type'),('74','patient preference','10','Data Type'),('75','provider preference','10','Data Type'),('76','severity','10','Data Type'),('77','start datetime','10','Data Type'),('79','stop datetime','10','Data Type'),('8','negation rationale','1','Data Type'),('81','negation rationale','11','Data Type'),('82','patient preference','11','Data Type'),('83','provider preference','11','Data Type'),('84','reaction','11','Data Type'),('85','start datetime','11','Data Type'),('86','stop datetime','11','Data Type'),('88','negation rationale','12','Data Type'),('89','patient preference','12','Data Type'),('9','patient preference','1','Data Type'),('90','provider preference','12','Data Type'),('91','reaction','12','Data Type'),('92','start datetime','12','Data Type'),('93','stop datetime','12','Data Type'),('95','negation rationale','13','Data Type'),('96','patient preference','13','Data Type'),('97','provider preference','13','Data Type'),('98','start datetime','13','Data Type'),('99','removal datetime','13','Data Type');
/*!40000 ALTER TABLE `QDM_ATTRIBUTES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QDM_ATTRIBUTES_BACKUP`
--

DROP TABLE IF EXISTS `QDM_ATTRIBUTES_BACKUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
-- Table structure for table `QDM_TERM`
--

DROP TABLE IF EXISTS `QDM_TERM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `QUALITY_DATA_MODEL` (
  `QUALITY_DATA_MODEL_ID` varchar(36) NOT NULL,
  `DATA_TYPE_ID` varchar(32) NOT NULL,
  `LIST_OBJECT_ID` varchar(32) NOT NULL,
  `MEASURE_ID` varchar(32) NOT NULL,
  `VERSION` varchar(32) NOT NULL,
  `OID` varchar(255) NOT NULL,
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
/*!40101 SET character_set_client = utf8 */;
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
-- Table structure for table `RECENT_MSR_ACTIVITY_LOG`
--

DROP TABLE IF EXISTS `RECENT_MSR_ACTIVITY_LOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UNIT` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  `SORT_ORDER` int(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UNIT`
--

LOCK TABLES `UNIT` WRITE;
/*!40000 ALTER TABLE `UNIT` DISABLE KEYS */;
INSERT INTO `UNIT` VALUES ('1','seconds',1),('10','bpm',10),('11','cm',11),('12','dL',13),('13','eq',14),('14','g',15),('15','kg',16),('16','L',17),('17','mEq',18),('18','mg',19),('19','mg/dL',20),('2','minutes',2),('20','mL',21),('21','mm',22),('22','mmHg',23),('23','mmol/L',24),('24','ng/dL',25),('25','kg/m2',27),('26','RAD',29),('27','per mm3',28),('28','copies/mL',12),('29','ng/mL',26),('3','hours',3),('4','days',4),('5','weeks',5),('6','months',6),('7','years',7),('8','%',8),('9','celsius',9);
/*!40000 ALTER TABLE `UNIT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UNIT_TYPE`
--

DROP TABLE IF EXISTS `UNIT_TYPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
INSERT INTO `UNIT_TYPE_MATRIX` VALUES ('1','1','1'),('10','10','1'),('11','11','1'),('12','12','1'),('13','13','1'),('14','14','1'),('15','15','1'),('16','16','1'),('17','17','1'),('18','18','1'),('19','19','1'),('2','2','1'),('20','20','1'),('21','21','1'),('22','22','1'),('23','23','1'),('24','24','1'),('25','1','2'),('26','2','2'),('27','3','2'),('28','4','2'),('29','5','2'),('3','3','1'),('30','6','2'),('31','7','2'),('32','1','3'),('33','2','3'),('34','3','3'),('35','4','3'),('36','5','3'),('37','6','3'),('38','7','3'),('39','1','4'),('4','4','1'),('40','2','4'),('41','3','4'),('42','4','4'),('43','5','4'),('44','6','4'),('45','7','4'),('46','8','4'),('47','9','4'),('48','10','4'),('49','11','4'),('5','5','1'),('50','12','4'),('51','13','4'),('52','14','4'),('53','15','4'),('54','16','4'),('55','17','4'),('56','18','4'),('57','19','4'),('58','20','4'),('59','21','4'),('6','6','1'),('60','22','4'),('61','23','4'),('62','24','4'),('63','25','1'),('64','26','1'),('65','25','4'),('66','26','4'),('67','27','1'),('68','27','4'),('69','28','1'),('7','7','1'),('70','28','4'),('71','29','1'),('72','29','4'),('8','8','1'),('9','9','1');
/*!40000 ALTER TABLE `UNIT_TYPE_MATRIX` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER`
--

DROP TABLE IF EXISTS `USER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
INSERT INTO `USER` VALUES ('Admin','Admin',NULL,'user','Admin','999-999-9999',NULL,NULL,'2015-06-11','2015-06-11 19:20:54','2015-06-11 19:20:56',NULL,'1','1','1','Aduser0001',1);
/*!40000 ALTER TABLE `USER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_BACKUP`
--

DROP TABLE IF EXISTS `USER_BACKUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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
-- Table structure for table `USER_PASSWORD`
--

DROP TABLE IF EXISTS `USER_PASSWORD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
-- Table structure for table `USER_PASSWORD_TEMP`
--

DROP TABLE IF EXISTS `USER_PASSWORD_TEMP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
INSERT INTO `USER_PASSWORD_TEMP` VALUES ('1','Admin',0,0,'c5190eed6feded32643bd04be40660df09b8eb046887e283a5eb093f3ccf7499','63fde2e4-236f-4b24-bfed-1e0eea874d76',0,'2015-06-11',NULL,0);
/*!40000 ALTER TABLE `USER_PASSWORD_TEMP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_REVOKE_ORG_CHANGE_BKP`
--

DROP TABLE IF EXISTS `USER_REVOKE_ORG_CHANGE_BKP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_SECURITY_QUESTIONS` (
  `USER_ID` varchar(40) NOT NULL,
  `ROW_ID` int(11) NOT NULL,
  `ANSWER` varchar(100) DEFAULT NULL,
  `QUESTION_ID` int(2) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`,`ROW_ID`),
  KEY `SECURITY_QUES_USER_FK` (`USER_ID`),
  KEY `FK_SECURITY_QUESTIONS` (`QUESTION_ID`),
  CONSTRAINT `FK_SECURITY_QUESTIONS` FOREIGN KEY (`QUESTION_ID`) REFERENCES `SECURITY_QUESTIONS` (`QUESTION_ID`),
  CONSTRAINT `SECURITY_QUES_USER_FK` FOREIGN KEY (`USER_ID`) REFERENCES `USER` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_SECURITY_QUESTIONS`
--

LOCK TABLES `USER_SECURITY_QUESTIONS` WRITE;
/*!40000 ALTER TABLE `USER_SECURITY_QUESTIONS` DISABLE KEYS */;
INSERT INTO `USER_SECURITY_QUESTIONS` VALUES ('Admin',0,'child',1),('Admin',1,'genre',2),('Admin',2,'friend',3);
/*!40000 ALTER TABLE `USER_SECURITY_QUESTIONS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_SECURITY_QUESTIONS_BCKUP`
--

DROP TABLE IF EXISTS `USER_SECURITY_QUESTIONS_BCKUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
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


