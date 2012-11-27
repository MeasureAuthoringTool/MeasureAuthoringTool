SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS MAT_APP;
CREATE SCHEMA IF NOT EXISTS MAT_APP DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE MAT_APP ;

-- -----------------------------------------------------
-- Table AUDIT_LOG
-- -----------------------------------------------------
DROP TABLE IF EXISTS AUDIT_LOG ;

CREATE  TABLE IF NOT EXISTS AUDIT_LOG (
  AUDIT_LOG_ID VARCHAR(32) NOT NULL ,
  CREATE_DATE TIMESTAMP NOT NULL ,
  CREATE_USER VARCHAR(40) NOT NULL ,
  UPDATE_DATE TIMESTAMP NULL ,
  UPDATE_USER VARCHAR(40) NULL ,
  ACTIVITY_TYPE VARCHAR(32) NULL ,
  MEASURE_ID VARCHAR(32) NULL ,
  LIST_OBJECT_ID VARCHAR(32) NULL ,
  CLAUSE_ID VARCHAR(32) NULL ,
  QDS_ID VARCHAR(32) NULL ,
  PRIMARY KEY (AUDIT_LOG_ID) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table SECURITY_ROLE
-- -----------------------------------------------------
DROP TABLE IF EXISTS SECURITY_ROLE ;

CREATE  TABLE IF NOT EXISTS SECURITY_ROLE (
  SECURITY_ROLE_ID VARCHAR(32) NOT NULL ,
  DESCRIPTION VARCHAR(50) NOT NULL ,
  PRIMARY KEY (SECURITY_ROLE_ID) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table STATUS
-- -----------------------------------------------------
DROP TABLE IF EXISTS STATUS ;

CREATE  TABLE IF NOT EXISTS STATUS (
  STATUS_ID VARCHAR(32) NOT NULL ,
  DESCRIPTION VARCHAR(50) NOT NULL ,
  PRIMARY KEY (STATUS_ID) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table USER
-- -----------------------------------------------------
DROP TABLE IF EXISTS USER ;

CREATE  TABLE IF NOT EXISTS USER (
  USER_ID VARCHAR(40) NOT NULL ,
  FIRST_NAME VARCHAR(100) NOT NULL ,
  MIDDLE_INITIAL VARCHAR(45) NULL ,
  LAST_NAME VARCHAR(100) NOT NULL ,
  EMAIL_ADDRESS VARCHAR(254) NOT NULL ,
  PHONE_NO VARCHAR(45) NOT NULL ,
  TITLE VARCHAR(45) NULL ,
  TERMINATION_DATE DATE NULL ,
  ACTIVATION_DATE DATE NOT NULL ,
  SIGN_IN_DATE TIMESTAMP NULL ,
  SIGN_OUT_DATE TIMESTAMP NULL ,
  LOCKED_OUT_DATE TIMESTAMP NULL ,
  STATUS_ID VARCHAR(32) NOT NULL ,
  AUDIT_ID VARCHAR(32) NOT NULL ,
  SECURITY_ROLE_ID VARCHAR(32) NOT NULL ,
  ORGANIZATION_NAME VARCHAR(80) NOT NULL ,
  ORG_OID VARCHAR(50) NOT NULL ,
  ROOT_OID VARCHAR(50) NOT NULL ,
  PRIMARY KEY (USER_ID) ,
  INDEX USER_SECURITY_ROLE_FK (SECURITY_ROLE_ID ASC) ,
  INDEX USER_AUDIT_FK (AUDIT_ID ASC) ,
  INDEX USER_STATUS_FK (STATUS_ID ASC) ,
  CONSTRAINT USER_SECURITY_ROLE_FK
    FOREIGN KEY (SECURITY_ROLE_ID )
    REFERENCES SECURITY_ROLE (SECURITY_ROLE_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT USER_AUDIT_FK
    FOREIGN KEY (AUDIT_ID )
    REFERENCES AUDIT_LOG (AUDIT_LOG_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT USER_STATUS_FK
    FOREIGN KEY (STATUS_ID )
    REFERENCES STATUS (STATUS_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table USER_PASSWORD
-- -----------------------------------------------------
DROP TABLE IF EXISTS USER_PASSWORD ;

CREATE  TABLE IF NOT EXISTS USER_PASSWORD (
  USER_PASSWORD_ID VARCHAR(32) NOT NULL ,
  USER_ID VARCHAR(40) NOT NULL ,
  PWD_LOCK_COUNTER INT(11)  NULL ,
  FORGOT_PWD_LOCK_COUNTER INT(11)  NULL ,
  PASSWORD VARCHAR(100) NOT NULL ,
  SALT VARCHAR(100) NOT NULL ,
  INITIAL_PWD TINYINT(1)  NULL DEFAULT false ,
  CREATE_DATE DATE NOT NULL ,
  FIRST_FAILED_ATTEMPT_TIME TIMESTAMP NULL ,
  TEMP_PWD TINYINT(1)  NULL DEFAULT false ,
  PRIMARY KEY (USER_PASSWORD_ID) ,
  INDEX PASSWORD_USER_FK (USER_ID ASC) ,
  UNIQUE INDEX USER_ID_UNIQUE (USER_ID ASC) ,
  CONSTRAINT PASSWORD_USER_FK
    FOREIGN KEY (USER_ID )
    REFERENCES USER (USER_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table USER_SECURITY_QUESTIONS
-- -----------------------------------------------------
DROP TABLE IF EXISTS USER_SECURITY_QUESTIONS ;

CREATE  TABLE IF NOT EXISTS USER_SECURITY_QUESTIONS (
  USER_ID VARCHAR(40) NOT NULL ,
  ROW_ID INT(11)  NOT NULL ,
  QUESTION VARCHAR(100) NOT NULL ,
  ANSWER VARCHAR(100) NOT NULL ,
  PRIMARY KEY (USER_ID, ROW_ID) ,
  INDEX SECURITY_QUES_USER_FK (USER_ID ASC) ,
  CONSTRAINT SECURITY_QUES_USER_FK
    FOREIGN KEY (USER_ID )
    REFERENCES USER (USER_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table OBJECT_STATUS
-- -----------------------------------------------------
DROP TABLE IF EXISTS OBJECT_STATUS ;

CREATE  TABLE IF NOT EXISTS OBJECT_STATUS (
  OBJECT_STATUS_ID VARCHAR(32) NOT NULL ,
  DESCRIPTION VARCHAR(50) NOT NULL ,
  PRIMARY KEY (OBJECT_STATUS_ID) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table CATEGORY
-- -----------------------------------------------------
DROP TABLE IF EXISTS CATEGORY ;

CREATE  TABLE IF NOT EXISTS CATEGORY (
  CATEGORY_ID VARCHAR(32) NOT NULL ,
  DESCRIPTION VARCHAR(50) NOT NULL ,
  ABBREVIATION VARCHAR(50) NOT NULL ,
  PRIMARY KEY (CATEGORY_ID) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table MEASURE_TYPES
-- -----------------------------------------------------
DROP TABLE IF EXISTS MEASURE_TYPES ;

CREATE  TABLE IF NOT EXISTS MEASURE_TYPES (
  ID VARCHAR(32) NOT NULL ,
  NAME VARCHAR(50) NOT NULL ,
  PRIMARY KEY (ID) )
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table STEWARD_ORG
-- -----------------------------------------------------
DROP TABLE IF EXISTS STEWARD_ORG ;

CREATE  TABLE IF NOT EXISTS STEWARD_ORG (
  ID VARCHAR(32) NOT NULL ,
  ORG_NAME VARCHAR(200) NOT NULL ,
  ORG_OID VARCHAR(100) NULL ,
  PRIMARY KEY (ID) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table AUTHOR
-- -----------------------------------------------------
DROP TABLE IF EXISTS AUTHOR ;

CREATE  TABLE IF NOT EXISTS AUTHOR (
  ID VARCHAR(32) NOT NULL ,
  AUTHOR_NAME VARCHAR(200) NOT NULL ,
  PRIMARY KEY (ID) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table DATA_TYPE
-- -----------------------------------------------------
DROP TABLE IF EXISTS DATA_TYPE ;

CREATE  TABLE IF NOT EXISTS DATA_TYPE (
  DATA_TYPE_ID VARCHAR(32) NOT NULL ,
  DESCRIPTION VARCHAR(50) NOT NULL ,
  CATEGORY_ID VARCHAR(32) NOT NULL ,
  PRIMARY KEY (DATA_TYPE_ID) ,
  INDEX DATA_TYPE_CAT_FK (CATEGORY_ID ASC) ,
  CONSTRAINT DATA_TYPE_CAT_FK
    FOREIGN KEY (CATEGORY_ID )
    REFERENCES CATEGORY (CATEGORY_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table CODE_SYSTEM
-- -----------------------------------------------------
DROP TABLE IF EXISTS CODE_SYSTEM ;

CREATE  TABLE IF NOT EXISTS CODE_SYSTEM (
  CODE_SYSTEM_ID VARCHAR(32) NOT NULL ,
  DESCRIPTION VARCHAR(50) NOT NULL ,
  CATEGORY_ID VARCHAR(32) NOT NULL ,
  PRIMARY KEY (CODE_SYSTEM_ID) ,
  INDEX CODE_SYS_CAT_FK (CATEGORY_ID ASC) ,
  CONSTRAINT CODE_SYS_CAT_FK
    FOREIGN KEY (CATEGORY_ID )
    REFERENCES CATEGORY (CATEGORY_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table LIST_OBJECT
-- -----------------------------------------------------
DROP TABLE IF EXISTS LIST_OBJECT ;

CREATE  TABLE IF NOT EXISTS LIST_OBJECT (
  LIST_OBJECT_ID VARCHAR(32) NOT NULL ,
  VERSION VARCHAR(255) NOT NULL DEFAULT '1.0' ,
  NAME VARCHAR(255) NOT NULL ,
  STEWARD VARCHAR(255) NULL ,
  OID VARCHAR(255) NOT NULL ,
  RATIONALE VARCHAR(2000) NULL ,
  COMMENT VARCHAR(2000) NULL ,
  OBJECT_STATUS_ID VARCHAR(32) NOT NULL ,
  OBJECT_OWNER VARCHAR(32) NOT NULL ,
  CATEGORY_ID VARCHAR(32) NOT NULL ,
  CODE_SYS_VERSION VARCHAR(255) NOT NULL ,
  CODE_SYSTEM_ID VARCHAR(32) NOT NULL ,
  MEASURE_ID VARCHAR(32) NULL ,
  PRIMARY KEY (LIST_OBJECT_ID) ,
  INDEX LIST_OBJECT_STATUS_FK (OBJECT_STATUS_ID ASC) ,
  INDEX LIST_OBJECT_USER_FK (OBJECT_OWNER ASC) ,
  INDEX LIST_OBJECT_CAT_FK (CATEGORY_ID ASC) ,
  INDEX LIST_OBJECT_CODE_SYSTEM_FK (CODE_SYSTEM_ID ASC) ,
  CONSTRAINT LIST_OBJECT_CODE_SYSTEM_FK
    FOREIGN KEY (CODE_SYSTEM_ID )
    REFERENCES CODE_SYSTEM (CODE_SYSTEM_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT LIST_OBJECT_STEWARD_FK
    FOREIGN KEY (STEWARD )
    REFERENCES STEWARD_ORG (ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT LIST_OBJECT_STATUS_FK
    FOREIGN KEY (OBJECT_STATUS_ID )
    REFERENCES OBJECT_STATUS (OBJECT_STATUS_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT LIST_OBJECT_USER_FK
    FOREIGN KEY (OBJECT_OWNER )
    REFERENCES USER (USER_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT LIST_OBJECT_CAT_FK
    FOREIGN KEY (CATEGORY_ID )
    REFERENCES CATEGORY (CATEGORY_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT LIST_OBJECT_MEASURE_FK
    FOREIGN KEY (MEASURE_ID )
    REFERENCES MEASURE (ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE)    
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table LIST_OBJECT_OID_GEN
-- -----------------------------------------------------
DROP TABLE IF EXISTS LIST_OBJECT_OID_GEN ;

CREATE  TABLE IF NOT EXISTS LIST_OBJECT_OID_GEN (
  LIST_OBJECT_OID_GEN_ID BIGINT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY(LIST_OBJECT_OID_GEN_ID)
)
ENGINE = InnoDB;
-- -----------------------------------------------------
-- Table CODE_LIST
-- -----------------------------------------------------
DROP TABLE IF EXISTS CODE_LIST ;

CREATE  TABLE IF NOT EXISTS CODE_LIST (
  CODE_LIST_ID VARCHAR(32) NOT NULL ,
  PRIMARY KEY (CODE_LIST_ID),
  INDEX CODE_LIST_OBJECT_FK (CODE_LIST_ID ASC) ,
  CONSTRAINT CODE_LIST_OBJECT_FK
    FOREIGN KEY (CODE_LIST_ID )
    REFERENCES LIST_OBJECT (LIST_OBJECT_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table CODE
-- -----------------------------------------------------
DROP TABLE IF EXISTS CODE ;

CREATE  TABLE IF NOT EXISTS CODE (
  CODE_ID VARCHAR(32) NOT NULL ,
  CODE VARCHAR(32) NOT NULL ,
  DESCRIPTION VARCHAR(1400) NOT NULL ,
  CODE_LIST_ID VARCHAR(32) NOT NULL ,
  PRIMARY KEY (CODE_ID) ,
  INDEX CODE_LIST_FK (CODE_LIST_ID ASC) ,
  CONSTRAINT CODE_LIST_FK
    FOREIGN KEY (CODE_LIST_ID )
    REFERENCES CODE_LIST (CODE_LIST_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;




-- -----------------------------------------------------
-- Table GROUPED_CODE_LISTS
-- -----------------------------------------------------
DROP TABLE IF EXISTS GROUPED_CODE_LISTS ;
CREATE  TABLE IF NOT EXISTS GROUPED_CODE_LISTS (
  GROUPED_CODE_LISTS_ID VARCHAR(32) NOT NULL ,
  GROUP_LIST_ID VARCHAR(32) NOT NULL ,
  CODE_LIST_ID VARCHAR(32) NOT NULL ,
  DESCRIPTION VARCHAR(1000) NOT NULL ,
  PRIMARY KEY (GROUPED_CODE_LISTS_ID) ,
  INDEX GR_CODE_LIST_FK (CODE_LIST_ID ASC) ,
  INDEX GR_LIST_OBJ_FK (GROUP_LIST_ID ASC) ,
  CONSTRAINT GR_CODE_LIST_FK
    FOREIGN KEY (CODE_LIST_ID )
    REFERENCES CODE_LIST (CODE_LIST_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT GR_LIST_OBJ_FK
    FOREIGN KEY (GROUP_LIST_ID )
    REFERENCES LIST_OBJECT (LIST_OBJECT_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- ----------- BEGIN MEASURE ---------------------------



-- -----------------------------------------------------
-- Table CONTEXT
-- -----------------------------------------------------
DROP TABLE IF EXISTS CONTEXT ;

CREATE  TABLE IF NOT EXISTS CONTEXT (
  CONTEXT_ID VARCHAR(64) NOT NULL ,
  DESCRIPTION VARCHAR(100) NOT NULL ,
  PRIMARY KEY (CONTEXT_ID))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table OPERATOR
-- -----------------------------------------------------
DROP TABLE IF EXISTS OPERATOR ;

CREATE  TABLE IF NOT EXISTS OPERATOR (
  OPERATOR_ID VARCHAR(64) NOT NULL ,
  NAME VARCHAR(45) NOT NULL ,
  TYPE VARCHAR(45) NOT NULL ,
  PRIMARY KEY (OPERATOR_ID))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table STATIC_ELEMENT
-- -----------------------------------------------------
DROP TABLE IF EXISTS STATIC_ELEMENT ;

CREATE  TABLE IF NOT EXISTS STATIC_ELEMENT (
  STATIC_ELEMENT_ID VARCHAR(64) NOT NULL ,
  UNIT VARCHAR(45) NOT NULL ,
  QUANTITY VARCHAR(100) NOT NULL ,
  PRIMARY KEY (STATIC_ELEMENT_ID))
ENGINE = InnoDB;



-- -----------------------------------------------------
-- Table TERM
-- -----------------------------------------------------
DROP TABLE IF EXISTS TERM ;

CREATE  TABLE IF NOT EXISTS TERM (
  TERM_ID VARCHAR(64) NOT NULL ,
  `ORDER` VARCHAR(45) NOT NULL ,
  OPERATOR_ID VARCHAR(64) NOT NULL ,
  NEXT_TERM_ID VARCHAR(64) NOT NULL ,
  STATIC_ELEMENT_ID VARCHAR(64) NOT NULL ,
  QDS_ELEMENT_ID VARCHAR(64) NOT NULL,
  PRIMARY KEY (TERM_ID),
  INDEX OPERATOR_ID_FK (OPERATOR_ID ASC) ,
  INDEX NEXT_TERM_ID_FK (NEXT_TERM_ID ASC) ,
  INDEX STATIC_ELEMENT_ID_FK (STATIC_ELEMENT_ID ASC) ,
  INDEX QDS_ELEMENT_ID_FK (QDS_ELEMENT_ID ASC),
  CONSTRAINT OPERATOR_ID_FK
    FOREIGN KEY (OPERATOR_ID )
    REFERENCES OPERATOR (OPERATOR_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT NEXT_TERM_ID_FK
    FOREIGN KEY (NEXT_TERM_ID )
    REFERENCES TERM (TERM_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT STATIC_ELEMENT_ID_FK
    FOREIGN KEY (STATIC_ELEMENT_ID )
    REFERENCES STATIC_ELEMENT (STATIC_ELEMENT_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT QDS_ELEMENT_ID_FK
    FOREIGN KEY (QDS_ELEMENT_ID )
    REFERENCES QDS_ELEMENT (QDS_ELEMENT_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table QDS_ELEMENT
-- -----------------------------------------------------
DROP TABLE IF EXISTS QDS_ELEMENT ;

CREATE  TABLE IF NOT EXISTS QDS_ELEMENT (
  QDS_ELEMENT_ID VARCHAR(64) NOT NULL ,
  MEASURE_PACKAGE_ID VARCHAR(64) NOT NULL ,
  PRIMARY KEY (QDS_ELEMENT_ID) ,
  INDEX MEASURE_PACKAGE_ID_QDS_ELEMENT_FK (MEASURE_PACKAGE_ID ASC) ,
  CONSTRAINT MEASURE_PACKAGE_ID_QDS_ELEMENT_FK
    FOREIGN KEY (MEASURE_PACKAGE_ID )
    REFERENCES MEASURE_PACKAGE (MEASURE_PACKAGE_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table QDS_PROPERTY
-- -----------------------------------------------------
DROP TABLE IF EXISTS QDS_PROPERTY ;

CREATE  TABLE IF NOT EXISTS QDS_PROPERTY (
  QDS_PROPERTY_ID VARCHAR(64) NOT NULL ,
  NAME VARCHAR(45) NOT NULL ,
  VALUE VARCHAR(200) NOT NULL ,
  TYPE VARCHAR(45) NOT NULL ,
  QDS_ELEMENT_ID VARCHAR(64) NOT NULL ,
  PRIMARY KEY (QDS_PROPERTY_ID),
  INDEX QDS_ELEMENT_ID_QDS_PROPERTY_FK (QDS_ELEMENT_ID ASC) ,
  CONSTRAINT QDS_ELEMENT_ID_QDS_PROPERTY_FK
    FOREIGN KEY (QDS_ELEMENT_ID )
    REFERENCES QDS_ELEMENT (QDS_ELEMENT_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE  TABLE IF NOT EXISTS ATTRIBUTE_DETAILS (
  ATTRIBUTE_DETAILS_ID VARCHAR(64) NOT NULL ,
  ATTR_NAME VARCHAR(200) NOT NULL ,
  CODE VARCHAR(200) NOT NULL ,
  CODE_SYSTEM VARCHAR(200) NOT NULL ,
  CODE_SYSTEM_NAME VARCHAR(200) NOT NULL ,
  MODE VARCHAR(200) NOT NULL ,
  TYPE_CODE VARCHAR(200) NOT NULL ,
  PRIMARY KEY (ATTRIBUTE_DETAILS_ID))
ENGINE = InnoDB;

-- ------------- END MEASURE ---------------------------
-- -----------------------------------------------------
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


-- -----------------------------------------------------
-- Table DECISION
-- -----------------------------------------------------
DROP TABLE IF EXISTS DECISION ;

CREATE  TABLE IF NOT EXISTS DECISION (
  ID VARCHAR(64) NOT NULL ,
  OPERATOR VARCHAR(45) NOT NULL ,
  PARENT_ID VARCHAR(64) ,
  ORDER_NUM VARCHAR(32) ,
  CLAUSE_ID VARCHAR(64) ,
  ATTRIBUTE_ID VARCHAR(64) ,
PRIMARY KEY (ID) )
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table QDSTERM
-- -----------------------------------------------------
DROP TABLE IF EXISTS QDS_TERM ;

CREATE  TABLE IF NOT EXISTS QDS_TERM (
  ID VARCHAR(64) NOT NULL ,
  QDS_ELEMENT_ID VARCHAR(64) NOT NULL ,
  DECISION_ID VARCHAR(64) NOT NULL ,
  PRIMARY KEY (ID))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table MEASUREMENT_TERM
-- -----------------------------------------------------
DROP TABLE IF EXISTS MEASUREMENT_TERM ;

CREATE  TABLE IF NOT EXISTS MEASUREMENT_TERM (
  ID VARCHAR(64) NOT NULL ,
  UNIT VARCHAR(45),
  QUANTITY VARCHAR(100) NOT NULL ,
  DECISION_ID VARCHAR(64) NOT NULL ,
  PRIMARY KEY (ID))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table OPERATORS
-- -----------------------------------------------------
DROP TABLE IF EXISTS OPERATORS ;

-- -----------------------------------------------------
-- Table CLAUSE
-- -----------------------------------------------------
DROP TABLE IF EXISTS CLAUSE ;

CREATE  TABLE IF NOT EXISTS CLAUSE (
  ID VARCHAR(32) NOT NULL ,
  NAME VARCHAR(100),
  DESCRIPTION VARCHAR(2000),
  MEASURE_ID VARCHAR(64),
  CONTEXT_ID VARCHAR(32),
  DECISION_ID VARCHAR(64),
  CLAUSE_TYPE_ID VARCHAR(32),
  STATUS_ID VARCHAR(32),
  VERSION VARCHAR(32),
  PRIMARY KEY (ID))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table MEASURE
-- -----------------------------------------------------
DROP TABLE IF EXISTS MEASURE ;

CREATE  TABLE IF NOT EXISTS MEASURE (
  ID VARCHAR(64) NOT NULL ,
  MEASURE_OWNER_ID VARCHAR(40) NOT NULL,
  ABBR_NAME VARCHAR(45),
  DESCRIPTION VARCHAR(2000),
  VERSION VARCHAR(32),
  MEASURE_STATUS VARCHAR(32),
  EXPORT_TS TIMESTAMP NULL ,
  PRIMARY KEY (ID),
  INDEX MEASURE_OWNER_FK (MEASURE_OWNER_ID ASC),
  CONSTRAINT MEASURE_OWNER_FK
    FOREIGN KEY (MEASURE_OWNER_ID )
    REFERENCES USER (USER_ID )
    ON UPDATE CASCADE
    ON DELETE CASCADE
    
)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table METADATA
-- -----------------------------------------------------
DROP TABLE IF EXISTS METADATA ;

CREATE  TABLE IF NOT EXISTS METADATA (
  METADATA_ID VARCHAR(64) NOT NULL ,
  NAME VARCHAR(100) NOT NULL ,
  VALUE VARCHAR(30000) NOT NULL ,
  MEASURE_ID VARCHAR(64) NOT NULL ,
  PRIMARY KEY (METADATA_ID) ,
  INDEX MEASURE_ID_METADATA_FK (MEASURE_ID ASC) ,
  CONSTRAINT MEASURE_ID_METADATA_FK
    FOREIGN KEY (MEASURE_ID )
    REFERENCES MEASURE (ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table QUALITY_DATA_SET
-- -----------------------------------------------------
DROP TABLE IF EXISTS QUALITY_DATA_SET ;

CREATE  TABLE IF NOT EXISTS QUALITY_DATA_SET (
  QUALITY_DATA_SET_ID VARCHAR(32) NOT NULL ,
  DATA_TYPE_ID VARCHAR(32) NOT NULL ,
  LIST_OBJECT_ID VARCHAR(32) NOT NULL ,
  MEASURE_ID VARCHAR(32) NOT NULL,
  VERSION VARCHAR(32) NOT NULL,
  OID VARCHAR(255) NOT NULL,
  PRIMARY KEY (QUALITY_DATA_SET_ID) ,
  INDEX QDS_DATA_TYPE_FK (DATA_TYPE_ID ASC) ,
  INDEX QDS_CODE_LIST_FK (LIST_OBJECT_ID ASC) ,
  CONSTRAINT QDS_DATA_TYPE_FK
    FOREIGN KEY (DATA_TYPE_ID )
    REFERENCES DATA_TYPE (DATA_TYPE_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT QDS_CODE_LIST_FK
    FOREIGN KEY (LIST_OBJECT_ID )
    REFERENCES LIST_OBJECT (LIST_OBJECT_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT QDS_MEASURE_ID_FK
    FOREIGN KEY (MEASURE_ID)
    REFERENCES MEASURE  (ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table QUALITY_DATA_SET_OID_GEN
-- -----------------------------------------------------
DROP TABLE IF EXISTS QUALITY_DATA_SET_OID_GEN ;

CREATE  TABLE IF NOT EXISTS QUALITY_DATA_SET_OID_GEN (
  OID_GEN_ID BIGINT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY(OID_GEN_ID)
)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table SHARE_LEVEL
-- -----------------------------------------------------
DROP TABLE IF EXISTS SHARE_LEVEL ;

CREATE  TABLE IF NOT EXISTS SHARE_LEVEL (
  SHARE_LEVEL_ID VARCHAR(32) NOT NULL,
  DESCRIPTION VARCHAR(50),
  PRIMARY KEY (SHARE_LEVEL_ID))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table MEASURE_SHARE
-- -----------------------------------------------------
DROP TABLE IF EXISTS MEASURE_SHARE ;

CREATE  TABLE IF NOT EXISTS MEASURE_SHARE (
  MEASURE_SHARE_ID VARCHAR(32) NOT NULL ,
  MEASURE_ID VARCHAR(32) NOT NULL ,
  MEASURE_OWNER_USER_ID VARCHAR(40) NOT NULL,
  SHARE_USER_ID VARCHAR(40) NOT NULL ,
  SHARE_LEVEL_ID VARCHAR(32) NOT NULL,
  PRIMARY KEY (MEASURE_SHARE_ID),
  INDEX MEASURE_SHARE_MEASURE_FK (MEASURE_ID ASC),
  INDEX MEASURE_SHARE_OWNER_FK (MEASURE_OWNER_USER_ID ASC),
  INDEX MEASURE_SHARE_SHARE_FK (SHARE_USER_ID ASC),
  CONSTRAINT MEASURE_SHARE_MEASURE_FK
    FOREIGN KEY (MEASURE_ID )
    REFERENCES MEASURE (ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT MEASURE_SHARE_OWNER_FK
    FOREIGN KEY (MEASURE_OWNER_USER_ID )
    REFERENCES USER (USER_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE,    
  CONSTRAINT MEASURE_SHARE_SHARE_FK
    FOREIGN KEY (SHARE_USER_ID )
    REFERENCES USER (USER_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE, 
  CONSTRAINT MEASURE_SHARE_LEVEL_ID
    FOREIGN KEY (SHARE_LEVEL_ID )
    REFERENCES SHARE_LEVEL (SHARE_LEVEL_ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE   
)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table PACKAGER
-- -----------------------------------------------------
DROP TABLE IF EXISTS PACKAGER ;

CREATE  TABLE IF NOT EXISTS PACKAGER (
  PACKAGER_ID VARCHAR(32) NOT NULL ,
  MEASURE_ID VARCHAR(32) NOT NULL ,
  CLAUSE_ID VARCHAR(32) NOT NULL,
  SEQUENCE INT NOT NULL,
  PRIMARY KEY (PACKAGER_ID),
  INDEX PACKAGER_MEASURE_FK (MEASURE_ID ASC),
  INDEX PACKAGER_CLAUSE_FK (CLAUSE_ID ASC),
  CONSTRAINT PACKAGER_MEASURE_FK
    FOREIGN KEY (MEASURE_ID )
    REFERENCES MEASURE (ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT PACKAGER_CLAUSE_FK
    FOREIGN KEY (CLAUSE_ID )
    REFERENCES CLAUSE (ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE
)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table QDS_ATTRIBUTES
-- -----------------------------------------------------
DROP TABLE IF EXISTS QDS_ATTRIBUTES ;

CREATE  TABLE IF NOT EXISTS QDS_ATTRIBUTES (
  ID VARCHAR(64) NOT NULL,
  NAME VARCHAR(100),
  DATA_TYPE_ID VARCHAR(32),
  QDS_ATTRIBUTE_TYPE VARCHAR(32),
  PRIMARY KEY (ID))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table QDS_ATTRIBUTE_DETAILS
-- -----------------------------------------------------
DROP TABLE IF EXISTS QDS_ATTRIBUTE_DETAILS ;

CREATE  TABLE IF NOT EXISTS QDS_ATTRIBUTE_DETAILS (
  ID VARCHAR(64) NOT NULL,
  DECISION_ID VARCHAR(64),
  QDS_ATTRIBUTE_ID VARCHAR(64),
  PRIMARY KEY (ID))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table MEASURE_EXPORT
-- -----------------------------------------------------
DROP TABLE IF EXISTS MEASURE_EXPORT ;

CREATE  TABLE IF NOT EXISTS MEASURE_EXPORT (
  MEASURE_EXPORT_ID VARCHAR(64) NOT NULL ,
  MEASURE_ID VARCHAR(64) NOT NULL ,
  SIMPLE_XML LONGTEXT NOT NULL ,
  PRIMARY KEY (MEASURE_EXPORT_ID),
  INDEX MEASURE_EXPORT_ID_MEASURE_FK (MEASURE_ID ASC) ,
  CONSTRAINT MEASURE_EXPORT_ID_MEASURE_FK
    FOREIGN KEY (MEASURE_ID )
    REFERENCES MEASURE (ID )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;
-- -----------------------------------------------------
-- -----------------------------------------------------

INSERT INTO SECURITY_ROLE (SECURITY_ROLE_ID,DESCRIPTION) VALUES
 ('1','Administrator'),
 ('2','Super user'),
 ('3','User');

INSERT INTO STATUS (STATUS_ID,DESCRIPTION) VALUES
 ('1','Active'),
 ('2','Revoked');

INSERT INTO AUDIT_LOG (AUDIT_LOG_ID,CREATE_DATE,CREATE_USER,UPDATE_DATE,UPDATE_USER) VALUES
 ('1','2010-10-28 15:08:27','Admin',NULL,NULL);

INSERT INTO USER (USER_ID,FIRST_NAME,MIDDLE_INITIAL,LAST_NAME,EMAIL_ADDRESS,PHONE_NO,
	TITLE,TERMINATION_DATE,ACTIVATION_DATE,SIGN_IN_DATE,SIGN_OUT_DATE,LOCKED_OUT_DATE,
	STATUS_ID,AUDIT_ID,SECURITY_ROLE_ID,ORGANIZATION_NAME,ORG_OID,ROOT_OID) VALUES 
 	('Admin','Admin',NULL,'user','Admin','515-453-8083',NULL,NULL,'2010-10-28',
	'2010-10-28 15:08:27',NULL,NULL,'1','1','1','IFMC','2.16.840.1.113883.3.67','2.16.840.1.113883.3.67.1.101.1');

INSERT INTO USER_PASSWORD (USER_PASSWORD_ID,USER_ID,PWD_LOCK_COUNTER,FORGOT_PWD_LOCK_COUNTER,PASSWORD,
	SALT, INITIAL_PWD,CREATE_DATE,FIRST_FAILED_ATTEMPT_TIME,TEMP_PWD) VALUES 
 	('1','Admin',0,0,'e47b456be30459c185ddcebb92491767','bbebea51-797b-4734-b71f-646e14843bda',1,'2010-10-28',NULL,0);

INSERT INTO OBJECT_STATUS (OBJECT_STATUS_ID,DESCRIPTION) VALUES 
 ('1','InProgress'),
 ('2','Complete');


INSERT INTO CATEGORY VALUES ('1', 'Care Experience','EXP');
INSERT INTO CATEGORY VALUES ('2', 'Care Plan','PLN'); 
INSERT INTO CATEGORY VALUES ('3', 'Communication','COM');
INSERT INTO CATEGORY VALUES ('4', 'Condition/Diagnosis/Problem','CDP');
INSERT INTO CATEGORY VALUES ('5', 'Device','DEV');
INSERT INTO CATEGORY VALUES ('6', 'Diagnostic Study','DXS');
INSERT INTO CATEGORY VALUES ('7', 'Encounter','ENC');
INSERT INTO CATEGORY VALUES ('8', 'Functional Status','FXS');
INSERT INTO CATEGORY VALUES ('9', 'Individual Characteristic','IND');
INSERT INTO CATEGORY VALUES ('10', 'Intervention','INT');
INSERT INTO CATEGORY VALUES ('11', 'Laboratory Test','LAB');
INSERT INTO CATEGORY VALUES ('12', 'Medication','MED');
INSERT INTO CATEGORY VALUES ('14', 'Physical Exam','PE');
INSERT INTO CATEGORY VALUES ('16', 'Procedure','PRC');
INSERT INTO CATEGORY VALUES ('17', 'Risk Category/Assessment','RSK');
INSERT INTO CATEGORY VALUES ('18', 'Substance','SUB');
INSERT INTO CATEGORY VALUES ('19', 'Symptom','SX');
INSERT INTO CATEGORY VALUES ('20', 'System Characteristic','SYS');
INSERT INTO CATEGORY VALUES ('21', 'Transfer of Care','TRN');
INSERT INTO CATEGORY VALUES ('22', 'Measure Timing', 'TMG');
INSERT INTO CATEGORY VALUES ('23', 'Attribute', 'ATR');

insert into CODE_SYSTEM values ('1', 'SNOMED', '1');
insert into CODE_SYSTEM values ('2', 'SNOMED', '2'); 
insert into CODE_SYSTEM values ('3', 'I9', '2');
insert into CODE_SYSTEM values ('4', 'I10', '2');
insert into CODE_SYSTEM values ('5', 'SNOMED', '3');
insert into CODE_SYSTEM values ('6', 'I9', '3');
insert into CODE_SYSTEM values ('7', 'I10', '3');
insert into CODE_SYSTEM values ('8', 'CPT', '3');
insert into CODE_SYSTEM values ('9', 'SNOMED', '4');
insert into CODE_SYSTEM values ('10', 'I9', '4');
insert into CODE_SYSTEM values ('11', 'I10', '4');
insert into CODE_SYSTEM values ('12', 'SNOMED', '5');
insert into CODE_SYSTEM values ('13', 'I9', '5');
insert into CODE_SYSTEM values ('14', 'I10', '5');
insert into CODE_SYSTEM values ('15', 'SNOMED', '6');
insert into CODE_SYSTEM values ('16', 'LOINC', '6');
insert into CODE_SYSTEM values ('17', 'CPT', '6');
insert into CODE_SYSTEM values ('18', 'HCPCS', '6');
insert into CODE_SYSTEM values ('19', 'SNOMED', '7');
insert into CODE_SYSTEM values ('20', 'LOINC', '7');
insert into CODE_SYSTEM values ('21', 'CPT', '7');
insert into CODE_SYSTEM values ('22', 'HCPCS', '7');
insert into CODE_SYSTEM values ('23', 'SNOMED', '8');
insert into CODE_SYSTEM values ('24', 'I9', '8');
insert into CODE_SYSTEM values ('25', 'I10', '8');
insert into CODE_SYSTEM values ('26', 'HL7', '9');
insert into CODE_SYSTEM values ('27', 'SNOMED', '10');
insert into CODE_SYSTEM values ('28', 'LOINC', '10');
insert into CODE_SYSTEM values ('29', 'I9', '10');
insert into CODE_SYSTEM values ('30', 'I10', '10');
insert into CODE_SYSTEM values ('31', 'CPT', '10');
insert into CODE_SYSTEM values ('32', 'HCPCS', '10');
insert into CODE_SYSTEM values ('33', 'SNOMED', '11');
insert into CODE_SYSTEM values ('34', 'LOINC', '11');
insert into CODE_SYSTEM values ('35', 'RxNorm', '12');
insert into CODE_SYSTEM values ('38', 'SNOMED', '14');
insert into CODE_SYSTEM values ('39', 'LOINC', '14');
insert into CODE_SYSTEM values ('41', 'SNOMED', '16');
insert into CODE_SYSTEM values ('42', 'I9', '16');
insert into CODE_SYSTEM values ('43', 'I10', '16');
insert into CODE_SYSTEM values ('44', 'CPT', '16');
insert into CODE_SYSTEM values ('45', 'HCPCS', '16');
insert into CODE_SYSTEM values ('46', 'SNOMED', '17');
insert into CODE_SYSTEM values ('47', 'I9', '17');
insert into CODE_SYSTEM values ('48', 'I10', '17');
insert into CODE_SYSTEM values ('49', 'SNOMED', '18');
insert into CODE_SYSTEM values ('50', 'I9', '18');
insert into CODE_SYSTEM values ('51', 'I10', '18');
insert into CODE_SYSTEM values ('52', 'SNOMED', '19');
insert into CODE_SYSTEM values ('53', 'I9', '19');
insert into CODE_SYSTEM values ('54', 'I10', '19');
insert into CODE_SYSTEM values ('55', 'SNOMED', '20');
insert into CODE_SYSTEM values ('56', 'HL7', '20');
insert into CODE_SYSTEM values ('57', 'SNOMED', '21');
insert into CODE_SYSTEM values ('58', 'I9', '21');
insert into CODE_SYSTEM values ('59', 'I10', '21');
insert into CODE_SYSTEM values ('60', 'NQF', '22');
insert into CODE_SYSTEM values ('61', 'I9', '23');
insert into CODE_SYSTEM values ('62', 'I10', '23');
insert into CODE_SYSTEM values ('63', 'SNOMED', '23');
insert into CODE_SYSTEM values ('64', 'CPT', '23');
insert into CODE_SYSTEM values ('65', 'LOINC', '23');
insert into CODE_SYSTEM values ('66', 'HCPCS', '23');
insert into CODE_SYSTEM values ('67', 'RXNORM', '23');
insert into CODE_SYSTEM values ('68', 'HL7', '23');
insert into CODE_SYSTEM values ('69', 'SNOMED', '9');

insert into CODE_SYSTEM values ('91', 'Grouping', '1');
insert into CODE_SYSTEM values ('92', 'Grouping', '2');
insert into CODE_SYSTEM values ('70', 'Grouping', '3');
insert into CODE_SYSTEM values ('71', 'Grouping', '4');
insert into CODE_SYSTEM values ('72', 'Grouping', '5');
insert into CODE_SYSTEM values ('73', 'Grouping', '6');
insert into CODE_SYSTEM values ('74', 'Grouping', '7');
insert into CODE_SYSTEM values ('75', 'Grouping', '8');
insert into CODE_SYSTEM values ('76', 'Grouping', '9');
insert into CODE_SYSTEM values ('77', 'Grouping', '10');
insert into CODE_SYSTEM values ('78', 'Grouping', '11');
insert into CODE_SYSTEM values ('79', 'Grouping', '12');
insert into CODE_SYSTEM values ('81', 'Grouping', '14');
insert into CODE_SYSTEM values ('83', 'Grouping', '16');
insert into CODE_SYSTEM values ('84', 'Grouping', '17');
insert into CODE_SYSTEM values ('85', 'Grouping', '18');
insert into CODE_SYSTEM values ('86', 'Grouping', '19');
insert into CODE_SYSTEM values ('87', 'Grouping', '20');
insert into CODE_SYSTEM values ('88', 'Grouping', '21');
insert into CODE_SYSTEM values ('89', 'Grouping', '22');
insert into CODE_SYSTEM values ('90', 'Grouping', '23');



insert into DATA_TYPE values('1', 'Patient Care Experience', '1');
insert into DATA_TYPE values('2', 'Provider Care Experience', '1');
insert into DATA_TYPE values('3', 'Care Plan', '2');
insert into DATA_TYPE values('4', 'Communication: From Provider to Provider', '3');
insert into DATA_TYPE values('5', 'Communication: From Patient to Provider', '3');
insert into DATA_TYPE values('6', 'Communication: From Provider to Patient', '3');
insert into DATA_TYPE values('7', 'Diagnosis, Active', '4');
insert into DATA_TYPE values('8', 'Diagnosis, Family History', '4');
insert into DATA_TYPE values('9', 'Diagnosis, Inactive', '4');
insert into DATA_TYPE values('10', 'Diagnosis, Resolved', '4');
insert into DATA_TYPE values('11', 'Device, Adverse Event', '5');
insert into DATA_TYPE values('12', 'Device, Allergy', '5');
insert into DATA_TYPE values('13', 'Device, Applied', '5');
insert into DATA_TYPE values('14', 'Device, Intolerance', '5');
insert into DATA_TYPE values('15', 'Device, Order', '5');
insert into DATA_TYPE values('16', 'Diagnostic Study, Adverse Event', '6');
insert into DATA_TYPE values('17', 'Diagnostic Study, Intolerance', '6');
insert into DATA_TYPE values('18', 'Diagnostic Study, Order', '6');
insert into DATA_TYPE values('19', 'Diagnostic Study, Performed', '6');
insert into DATA_TYPE values('20', 'Diagnostic Study, Result', '6');
insert into DATA_TYPE values('21', 'Encounter, Order', '7');
insert into DATA_TYPE values('22', 'Encounter, Performed', '7');
insert into DATA_TYPE values('23', 'Functional Status, Order', '8');
insert into DATA_TYPE values('24', 'Functional Status, Performed', '8');
insert into DATA_TYPE values('25', 'Functional Status, Result', '8');
insert into DATA_TYPE values('26', 'Patient Characteristic', '9');
insert into DATA_TYPE values('27', 'Provider Characteristic', '9');
insert into DATA_TYPE values('28', 'Intervention, Adverse Event', '10');
insert into DATA_TYPE values('29', 'Intervention, Intolerance', '10');
insert into DATA_TYPE values('30', 'Intervention, Order', '10');
insert into DATA_TYPE values('31', 'Intervention, Performed', '10');
insert into DATA_TYPE values('32', 'Intervention, Result', '10');
insert into DATA_TYPE values('33', 'Laboratory Test, Adverse Event', '11');
insert into DATA_TYPE values('34', 'Laboratory Test, Intolerance', '11');
insert into DATA_TYPE values('35', 'Laboratory Test, Order', '11');
insert into DATA_TYPE values('36', 'Laboratory Test, Performed', '11');
insert into DATA_TYPE values('37', 'Laboratory Test, Result', '11');
insert into DATA_TYPE values('38', 'Medication, Active', '12');
insert into DATA_TYPE values('39', 'Medication, Administered', '12');
insert into DATA_TYPE values('40', 'Medication, Adverse Effects', '12');
insert into DATA_TYPE values('41', 'Medication, Allergy', '12');
insert into DATA_TYPE values('42', 'Medication, Dispensed', '12');
insert into DATA_TYPE values('43', 'Medication, Intolerance', '12');
insert into DATA_TYPE values('44', 'Medication, Order', '12');
insert into DATA_TYPE values('55', 'Physical Exam, Finding', '14');
insert into DATA_TYPE values('56', 'Physical Exam, Order', '14');
insert into DATA_TYPE values('57', 'Physical Exam, Performed', '14');
insert into DATA_TYPE values('60', 'Procedure, Adverse Event', '16');
insert into DATA_TYPE values('61', 'Procedure, Intolerance', '16');
insert into DATA_TYPE values('62', 'Procedure, Order', '16');
insert into DATA_TYPE values('63', 'Procedure, Performed', '16');
insert into DATA_TYPE values('64', 'Procedure, Result', '16');
insert into DATA_TYPE values('65', 'Risk Category Assessment', '17');
insert into DATA_TYPE values('66', 'Substance, Administered', '18');
insert into DATA_TYPE values('67', 'Substance, Adverse Event', '18');
insert into DATA_TYPE values('68', 'Substance, Allergy', '18');
insert into DATA_TYPE values('69', 'Substance, Intolerance', '18');
insert into DATA_TYPE values('70', 'Substance, Order', '18');
insert into DATA_TYPE values('71', 'Symptom, Active', '19');
insert into DATA_TYPE values('72', 'Symptom, Assessed', '19');
insert into DATA_TYPE values('73', 'Symptom, Inactive', '19');
insert into DATA_TYPE values('74', 'Symptom, Resolved', '19');
insert into DATA_TYPE values('75', 'System Characteristic', '20');
insert into DATA_TYPE values('76', 'Transfer From', '21');
insert into DATA_TYPE values('77', 'Transfer To', '21');
insert into DATA_TYPE values('78', 'Device, Recommended', '5');
insert into DATA_TYPE values('79', 'Encounter, Recommended', '7');
insert into DATA_TYPE values('80', 'Functional Status, Recommended', '8');
insert into DATA_TYPE values('81', 'Intervention, Recommended', '10');
insert into DATA_TYPE values('82', 'Laboratory Test, Recommended', '11');
insert into DATA_TYPE values('87', 'Physical Exam, Recommended', '14');
insert into DATA_TYPE values('88', 'Procedure, Recommended', '16');
insert into DATA_TYPE values('89', 'Substance, Recommended', '18');
insert into DATA_TYPE values('90', 'Diagnostic Study, Recommended', '6');
insert into DATA_TYPE values('91', 'start of timing window', '22');
insert into DATA_TYPE values('92', 'attribute', '23');
insert into DATA_TYPE values('93', 'end of timing window', '22');
insert into DATA_TYPE values('94', 'Encounter', '7');
insert into DATA_TYPE values('95', 'Encounter, Active', '7');

INSERT INTO SHARE_LEVEL VALUES ('1', 'View Only');
INSERT INTO SHARE_LEVEL VALUES ('2', 'Modify');

INSERT INTO MEASURE_TYPES VALUES ('1', 'Composite');
INSERT INTO MEASURE_TYPES VALUES ('2', 'Cost/Resource Use');
INSERT INTO MEASURE_TYPES VALUES ('3', 'Efficiency');
INSERT INTO MEASURE_TYPES VALUES ('4', 'Outcome');
INSERT INTO MEASURE_TYPES VALUES ('5', 'Patient Engagement/Experience');
INSERT INTO MEASURE_TYPES VALUES ('6', 'Process');
INSERT INTO MEASURE_TYPES VALUES ('7', 'Structure');


INSERT INTO STEWARD_ORG VALUES ('1', '3M Health Information Systems','');
INSERT INTO STEWARD_ORG VALUES ('2', 'ACC/AHA Task Force on Performance Measures','');
INSERT INTO STEWARD_ORG VALUES ('3', 'ActiveHealth Management','');
INSERT INTO STEWARD_ORG VALUES ('4', 'Agency for Healthcare Research and Quality','');
INSERT INTO STEWARD_ORG VALUES ('5', 'Ambulatory Surgical Centers Quality Collaboration','');
INSERT INTO STEWARD_ORG VALUES ('6', 'American Academy of Dermatology','');
INSERT INTO STEWARD_ORG VALUES ('7', 'American Academy of Pediatrics','');
INSERT INTO STEWARD_ORG VALUES ('8', 'American College of Cardiology','');
INSERT INTO STEWARD_ORG VALUES ('9', 'American College of Emergency Physicians','');
INSERT INTO STEWARD_ORG VALUES ('10', 'American Academy of Family Physicians','');
INSERT INTO STEWARD_ORG VALUES ('11', 'American Academy of Orthopaedic Surgeons','');
INSERT INTO STEWARD_ORG VALUES ('12', 'American College of Rheumatology','');
INSERT INTO STEWARD_ORG VALUES ('13', 'American College of Surgeons','');
INSERT INTO STEWARD_ORG VALUES ('14', 'American Medical Association - Physician Consortium for Performance Improvement','');
INSERT INTO STEWARD_ORG VALUES ('15', 'American Nurses Association','');
INSERT INTO STEWARD_ORG VALUES ('16', 'American Podiatric Medical Association','');
INSERT INTO STEWARD_ORG VALUES ('17', 'American Speech-Language-Hearing Association','');
INSERT INTO STEWARD_ORG VALUES ('18', 'Asian Liver Center at Stanford University','');
INSERT INTO STEWARD_ORG VALUES ('19', 'Booz Allen Hamilton','');
INSERT INTO STEWARD_ORG VALUES ('20', 'California Maternal Quality Care Collaborative','');
INSERT INTO STEWARD_ORG VALUES ('21', 'California Nursing Outcome Coalition','');
INSERT INTO STEWARD_ORG VALUES ('22', 'Center for Medicare & Medicaid Services','');
INSERT INTO STEWARD_ORG VALUES ('23', 'Center for Quality Assessment and Improvement in Mental Health','');
INSERT INTO STEWARD_ORG VALUES ('24', 'Centers for Disease Control and Prevention','');
INSERT INTO STEWARD_ORG VALUES ('25', 'Child Health Corporation of America','');
INSERT INTO STEWARD_ORG VALUES ('26', 'Children''s Hospital of Philadelphia','');
INSERT INTO STEWARD_ORG VALUES ('27', 'Christiana Care Health System','');
INSERT INTO STEWARD_ORG VALUES ('28', 'City of New York Department of Health and Mental Hygiene','');
INSERT INTO STEWARD_ORG VALUES ('29', 'Cleveland Clinic','');
INSERT INTO STEWARD_ORG VALUES ('30', 'CREcare','');

INSERT INTO STEWARD_ORG VALUES ('31', 'Focus on Therapeutic Outcomes, Inc','');
INSERT INTO STEWARD_ORG VALUES ('32', 'General Electric - Healthcare','');
INSERT INTO STEWARD_ORG VALUES ('33', 'Harborview Medical Center','');
INSERT INTO STEWARD_ORG VALUES ('34', 'Health Benchmarks, Inc','');
INSERT INTO STEWARD_ORG VALUES ('35', 'HealthPartners','');
INSERT INTO STEWARD_ORG VALUES ('36', 'Henry Ford Hospital','');
INSERT INTO STEWARD_ORG VALUES ('37', 'Hospital Corporation of America','');
INSERT INTO STEWARD_ORG VALUES ('38', 'Ingenix','');

INSERT INTO STEWARD_ORG VALUES ('39', 'Institute for Clinical and Evaluative Sciences','');
INSERT INTO STEWARD_ORG VALUES ('40', 'Institute for Clinical Systems Improvement','');
INSERT INTO STEWARD_ORG VALUES ('41', 'Institute for Healthcare Improvement','');
INSERT INTO STEWARD_ORG VALUES ('42', 'Intermountain Healthcare','');
INSERT INTO STEWARD_ORG VALUES ('43', 'Intersocietal Accreditation Comission','');
INSERT INTO STEWARD_ORG VALUES ('44', 'IPRO','');
INSERT INTO STEWARD_ORG VALUES ('45', 'James Whitcomb Riley Hospital for Children','');
INSERT INTO STEWARD_ORG VALUES ('46', 'Joint Commission Resources, Inc.','');
INSERT INTO STEWARD_ORG VALUES ('47', 'Kidney Care Quality Alliance','');
INSERT INTO STEWARD_ORG VALUES ('48', 'Leapfrog Group','');
INSERT INTO STEWARD_ORG VALUES ('49', 'Lifescan, A Johnson & Johnson Company','');
INSERT INTO STEWARD_ORG VALUES ('50', 'Louisiana State University','');
INSERT INTO STEWARD_ORG VALUES ('51', 'Massachusetts General Hospital/Partners Health Care System','');
INSERT INTO STEWARD_ORG VALUES ('52', 'MN Community Measurement','');
INSERT INTO STEWARD_ORG VALUES ('53', 'National Association of Children''s Hospitals and Related Institutions','');
INSERT INTO STEWARD_ORG VALUES ('54', 'National Cancer Institute','');
INSERT INTO STEWARD_ORG VALUES ('55', 'National Committee for Quality Assurance','');
INSERT INTO STEWARD_ORG VALUES ('56', 'National Hospice and Palliative Care Organization','');
INSERT INTO STEWARD_ORG VALUES ('57', 'National Initiative for Children''s Healthcare Quality','');
INSERT INTO STEWARD_ORG VALUES ('58', 'National Perinatal Information Center','');
INSERT INTO STEWARD_ORG VALUES ('59', 'New York State Department of Health','');
INSERT INTO STEWARD_ORG VALUES ('60', 'Office of Statewide Health Planning and Development','');
INSERT INTO STEWARD_ORG VALUES ('61', 'Oregon Health & Science University','');
INSERT INTO STEWARD_ORG VALUES ('62', 'Ortho-McNeil-Janssen Pharmaceuticals, Inc.','');
INSERT INTO STEWARD_ORG VALUES ('63', 'PacifiCare','');
INSERT INTO STEWARD_ORG VALUES ('64', 'Premier, Inc','');
INSERT INTO STEWARD_ORG VALUES ('65', 'PRETest Consult, LLC','');
INSERT INTO STEWARD_ORG VALUES ('66', 'Providence St. Vincent Medical Center','');
INSERT INTO STEWARD_ORG VALUES ('67', 'Providence Health and Services','');
INSERT INTO STEWARD_ORG VALUES ('68', 'RAND','');
INSERT INTO STEWARD_ORG VALUES ('69', 'Resolution Health, Inc.','');
INSERT INTO STEWARD_ORG VALUES ('70', 'Society for Vascular Surgery','');
INSERT INTO STEWARD_ORG VALUES ('71', 'Society of Thoracic Surgeons','');
INSERT INTO STEWARD_ORG VALUES ('72', 'The Joint Commission','');
INSERT INTO STEWARD_ORG VALUES ('73', 'United Health Group','');
INSERT INTO STEWARD_ORG VALUES ('74', 'University of Colorado Health Sciences Center','');
INSERT INTO STEWARD_ORG VALUES ('75', 'University of Minnesota Rural Health Research Center','');
INSERT INTO STEWARD_ORG VALUES ('76', 'Vermont Oxford Network','');
INSERT INTO STEWARD_ORG VALUES ('77', 'VHA, Inc.','');
INSERT INTO STEWARD_ORG VALUES ('78', 'Wisconsin Collaborative for Healthcare Quality','');
INSERT INTO STEWARD_ORG VALUES ('79', 'Wisconsin Department of Health and Family Services','');



INSERT INTO AUTHOR VALUES ('1', '3M Health Information Systems');
INSERT INTO AUTHOR VALUES ('2', 'ACC/AHA Task Force on Performance Measures');
INSERT INTO AUTHOR VALUES ('3', 'ActiveHealth Management');
INSERT INTO AUTHOR VALUES ('4', 'Agency for Healthcare Research and Quality');
INSERT INTO AUTHOR VALUES ('5', 'Ambulatory Surgical Centers Quality Collaboration');
INSERT INTO AUTHOR VALUES ('6', 'American Academy of Dermatology');
INSERT INTO AUTHOR VALUES ('7', 'American Academy of Pediatrics');
INSERT INTO AUTHOR VALUES ('8', 'American College of Cardiology');
INSERT INTO AUTHOR VALUES ('9', 'American College of Emergency Physicians');
INSERT INTO AUTHOR VALUES ('10', 'American Academy of Family Physicians');
INSERT INTO AUTHOR VALUES ('11', 'American Academy of Orthopaedic Surgeons');
INSERT INTO AUTHOR VALUES ('12', 'American College of Rheumatology');
INSERT INTO AUTHOR VALUES ('13', 'American College of Surgeons');
INSERT INTO AUTHOR VALUES ('14', 'American Medical Association - Physician Consortium for Performance Improvement');
INSERT INTO AUTHOR VALUES ('15', 'American Nurses Association');
INSERT INTO AUTHOR VALUES ('16', 'American Podiatric Medical Association');
INSERT INTO AUTHOR VALUES ('17', 'American Speech-Language-Hearing Association');
INSERT INTO AUTHOR VALUES ('18', 'Asian Liver Center at Stanford University');
INSERT INTO AUTHOR VALUES ('19', 'Booz Allen Hamilton');
INSERT INTO AUTHOR VALUES ('20', 'California Maternal Quality Care Collaborative');
INSERT INTO AUTHOR VALUES ('21', 'California Nursing Outcome Coalition');
INSERT INTO AUTHOR VALUES ('22', 'Center for Medicare & Medicaid Services');
INSERT INTO AUTHOR VALUES ('23', 'Center for Quality Assessment and Improvement in Mental Health');
INSERT INTO AUTHOR VALUES ('24', 'Centers for Disease Control and Prevention');
INSERT INTO AUTHOR VALUES ('25', 'Child Health Corporation of America');
INSERT INTO AUTHOR VALUES ('26', 'Children''s Hospital of Philadelphia');
INSERT INTO AUTHOR VALUES ('27', 'Christiana Care Health System');
INSERT INTO AUTHOR VALUES ('28', 'City of New York Department of Health and Mental Hygiene');
INSERT INTO AUTHOR VALUES ('29', 'Cleveland Clinic');
INSERT INTO AUTHOR VALUES ('30', 'CREcare');

INSERT INTO AUTHOR VALUES ('31', 'Focus on Therapeutic Outcomes, Inc');
INSERT INTO AUTHOR VALUES ('32', 'General Electric - Healthcare');
INSERT INTO AUTHOR VALUES ('33', 'Harborview Medical Center');
INSERT INTO AUTHOR VALUES ('34', 'Health Benchmarks, Inc');
INSERT INTO AUTHOR VALUES ('35', 'HealthPartners');
INSERT INTO AUTHOR VALUES ('36', 'Henry Ford Hospital');
INSERT INTO AUTHOR VALUES ('37', 'Hospital Corporation of America');
INSERT INTO AUTHOR VALUES ('38', 'Ingenix');

INSERT INTO AUTHOR VALUES ('39', 'Institute for Clinical and Evaluative Sciences');
INSERT INTO AUTHOR VALUES ('40', 'Institute for Clinical Systems Improvement');
INSERT INTO AUTHOR VALUES ('41', 'Institute for Healthcare Improvement');
INSERT INTO AUTHOR VALUES ('42', 'Intermountain Healthcare');
INSERT INTO AUTHOR VALUES ('43', 'Intersocietal Accreditation Comission');
INSERT INTO AUTHOR VALUES ('44', 'IPRO');
INSERT INTO AUTHOR VALUES ('45', 'James Whitcomb Riley Hospital for Children');
INSERT INTO AUTHOR VALUES ('46', 'Joint Commission Resources, Inc.');
INSERT INTO AUTHOR VALUES ('47', 'Kidney Care Quality Alliance');
INSERT INTO AUTHOR VALUES ('48', 'Leapfrog Group');
INSERT INTO AUTHOR VALUES ('49', 'Lifescan, A Johnson & Johnson Company');
INSERT INTO AUTHOR VALUES ('50', 'Louisiana State University');
INSERT INTO AUTHOR VALUES ('51', 'Massachusetts General Hospital/Partners Health Care System');
INSERT INTO AUTHOR VALUES ('52', 'MN Community Measurement');
INSERT INTO AUTHOR VALUES ('53', 'National Association of Children''s Hospitals and Related Institutions');
INSERT INTO AUTHOR VALUES ('54', 'National Cancer Institute');
INSERT INTO AUTHOR VALUES ('55', 'National Committee for Quality Assurance');
INSERT INTO AUTHOR VALUES ('56', 'National Hospice and Palliative Care Organization');
INSERT INTO AUTHOR VALUES ('57', 'National Initiative for Children''s Healthcare Quality');
INSERT INTO AUTHOR VALUES ('58', 'National Perinatal Information Center');
INSERT INTO AUTHOR VALUES ('59', 'New York State Department of Health');
INSERT INTO AUTHOR VALUES ('60', 'Office of Statewide Health Planning and Development');
INSERT INTO AUTHOR VALUES ('61', 'Oregon Health & Science University');
INSERT INTO AUTHOR VALUES ('62', 'Ortho-McNeil-Janssen Pharmaceuticals, Inc.');
INSERT INTO AUTHOR VALUES ('63', 'PacifiCare');
INSERT INTO AUTHOR VALUES ('64', 'Premier, Inc');
INSERT INTO AUTHOR VALUES ('65', 'PRETest Consult, LLC');
INSERT INTO AUTHOR VALUES ('66', 'Providence St. Vincent Medical Center');
INSERT INTO AUTHOR VALUES ('67', 'Providence Health and Services');
INSERT INTO AUTHOR VALUES ('68', 'RAND');
INSERT INTO AUTHOR VALUES ('69', 'Resolution Health, Inc.');
INSERT INTO AUTHOR VALUES ('70', 'Society for Vascular Surgery');
INSERT INTO AUTHOR VALUES ('71', 'Society of Thoracic Surgeons');
INSERT INTO AUTHOR VALUES ('72', 'The Joint Commission');
INSERT INTO AUTHOR VALUES ('73', 'United Health Group');
INSERT INTO AUTHOR VALUES ('74', 'University of Colorado Health Sciences Center');
INSERT INTO AUTHOR VALUES ('75', 'University of Minnesota Rural Health Research Center');
INSERT INTO AUTHOR VALUES ('76', 'Vermont Oxford Network');
INSERT INTO AUTHOR VALUES ('77', 'VHA, Inc.');
INSERT INTO AUTHOR VALUES ('78', 'Wisconsin Collaborative for Healthcare Quality');
INSERT INTO AUTHOR VALUES ('79', 'Wisconsin Department of Health and Family Services');
INSERT INTO AUTHOR VALUES ('80', 'Other');


INSERT INTO CONTEXT VALUES ('1', 'Population');
INSERT INTO CONTEXT VALUES ('2', 'Numerator');
INSERT INTO CONTEXT VALUES ('3', 'Denominator');
INSERT INTO CONTEXT VALUES ('4', 'Exclusions');
INSERT INTO CONTEXT VALUES ('5', 'Exceptions');
INSERT INTO CONTEXT VALUES ('6', 'User-defined');
INSERT INTO CONTEXT VALUES ('7', 'Measure Phrase');

/* inserts for QDS_ATTRIBUTES */
insert into QDS_ATTRIBUTES values('7', 'duration', '1', 'Data Type');
insert into QDS_ATTRIBUTES values('8', 'negation rationale', '1', 'Data Type');
insert into QDS_ATTRIBUTES values('9', 'patient preference', '1', 'Data Type');
insert into QDS_ATTRIBUTES values('10', 'provider preference', '1', 'Data Type');
insert into QDS_ATTRIBUTES values('11', 'start datetime', '1', 'Data Type');
insert into QDS_ATTRIBUTES values('12', 'stop datetime', '1', 'Data Type');
insert into QDS_ATTRIBUTES values('13', 'duration', '2', 'Data Type');
insert into QDS_ATTRIBUTES values('14', 'negation rationale', '2', 'Data Type');
insert into QDS_ATTRIBUTES values('15', 'patient preference', '2', 'Data Type');
insert into QDS_ATTRIBUTES values('16', 'provider preference', '2', 'Data Type');
insert into QDS_ATTRIBUTES values('17', 'start datetime', '2', 'Data Type');
insert into QDS_ATTRIBUTES values('18', 'stop datetime', '2', 'Data Type');
insert into QDS_ATTRIBUTES values('19', 'duration', '3', 'Data Type');
insert into QDS_ATTRIBUTES values('20', 'negation rationale', '3', 'Data Type');
insert into QDS_ATTRIBUTES values('21', 'patient preference', '3', 'Data Type');
insert into QDS_ATTRIBUTES values('22', 'provider preference', '3', 'Data Type');
insert into QDS_ATTRIBUTES values('23', 'start datetime', '3', 'Data Type');
insert into QDS_ATTRIBUTES values('24', 'stop datetime', '3', 'Data Type');
insert into QDS_ATTRIBUTES values('25', 'duration', '5', 'Data Type');
insert into QDS_ATTRIBUTES values('26', 'negation rationale', '5', 'Data Type');
insert into QDS_ATTRIBUTES values('27', 'patient preference', '5', 'Data Type');
insert into QDS_ATTRIBUTES values('28', 'provider preference', '5', 'Data Type');
insert into QDS_ATTRIBUTES values('29', 'start datetime', '5', 'Data Type');
insert into QDS_ATTRIBUTES values('30', 'stop datetime', '5', 'Data Type');
insert into QDS_ATTRIBUTES values('31', 'duration', '6', 'Data Type');
insert into QDS_ATTRIBUTES values('32', 'negation rationale', '6', 'Data Type');
insert into QDS_ATTRIBUTES values('33', 'patient preference', '6', 'Data Type');
insert into QDS_ATTRIBUTES values('34', 'provider preference', '6', 'Data Type');
insert into QDS_ATTRIBUTES values('35', 'start datetime', '6', 'Data Type');
insert into QDS_ATTRIBUTES values('36', 'stop datetime', '6', 'Data Type');
insert into QDS_ATTRIBUTES values('37', 'duration', '4', 'Data Type');
insert into QDS_ATTRIBUTES values('38', 'negation rationale', '4', 'Data Type');
insert into QDS_ATTRIBUTES values('39', 'patient preference', '4', 'Data Type');
insert into QDS_ATTRIBUTES values('40', 'provider preference', '4', 'Data Type');
insert into QDS_ATTRIBUTES values('41', 'start datetime', '4', 'Data Type');
insert into QDS_ATTRIBUTES values('42', 'stop datetime', '4', 'Data Type');
insert into QDS_ATTRIBUTES values('43', 'duration', '7', 'Data Type');
insert into QDS_ATTRIBUTES values('44', 'negation rationale', '7', 'Data Type');
insert into QDS_ATTRIBUTES values('45', 'ordinality', '7', 'Data Type');
insert into QDS_ATTRIBUTES values('46', 'patient preference', '7', 'Data Type');
insert into QDS_ATTRIBUTES values('47', 'provider preference', '7', 'Data Type');
insert into QDS_ATTRIBUTES values('48', 'severity', '7', 'Data Type');
insert into QDS_ATTRIBUTES values('49', 'start datetime', '7', 'Data Type');
insert into QDS_ATTRIBUTES values('50', 'status', '7', 'Data Type');
insert into QDS_ATTRIBUTES values('52', 'stop datetime', '7', 'Data Type');
insert into QDS_ATTRIBUTES values('53', 'duration', '8', 'Data Type');
insert into QDS_ATTRIBUTES values('54', 'negation rationale', '8', 'Data Type');
insert into QDS_ATTRIBUTES values('55', 'ordinality', '8', 'Data Type');
insert into QDS_ATTRIBUTES values('56', 'patient preference', '8', 'Data Type');
insert into QDS_ATTRIBUTES values('57', 'provider preference', '8', 'Data Type');
insert into QDS_ATTRIBUTES values('58', 'severity', '8', 'Data Type');
insert into QDS_ATTRIBUTES values('59', 'start datetime', '8', 'Data Type');
insert into QDS_ATTRIBUTES values('60', 'status', '8', 'Data Type');
insert into QDS_ATTRIBUTES values('61', 'stop datetime', '8', 'Data Type');
insert into QDS_ATTRIBUTES values('62', 'duration', '9', 'Data Type');
insert into QDS_ATTRIBUTES values('63', 'negation rationale', '9', 'Data Type');
insert into QDS_ATTRIBUTES values('64', 'ordinality', '9', 'Data Type');
insert into QDS_ATTRIBUTES values('65', 'patient preference', '9', 'Data Type');
insert into QDS_ATTRIBUTES values('66', 'provider preference', '9', 'Data Type');
insert into QDS_ATTRIBUTES values('67', 'severity', '9', 'Data Type');
insert into QDS_ATTRIBUTES values('68', 'start datetime', '9', 'Data Type');
insert into QDS_ATTRIBUTES values('69', 'status', '9', 'Data Type');
insert into QDS_ATTRIBUTES values('70', 'stop datetime', '9', 'Data Type');
insert into QDS_ATTRIBUTES values('71', 'duration', '10', 'Data Type');
insert into QDS_ATTRIBUTES values('72', 'negation rationale', '10', 'Data Type');
insert into QDS_ATTRIBUTES values('73', 'ordinality', '10', 'Data Type');
insert into QDS_ATTRIBUTES values('74', 'patient preference', '10', 'Data Type');
insert into QDS_ATTRIBUTES values('75', 'provider preference', '10', 'Data Type');
insert into QDS_ATTRIBUTES values('76', 'severity', '10', 'Data Type');
insert into QDS_ATTRIBUTES values('77', 'start datetime', '10', 'Data Type');
insert into QDS_ATTRIBUTES values('78', 'status', '10', 'Data Type');
insert into QDS_ATTRIBUTES values('79', 'stop datetime', '10', 'Data Type');
insert into QDS_ATTRIBUTES values('80', 'duration', '11', 'Data Type');
insert into QDS_ATTRIBUTES values('81', 'negation rationale', '11', 'Data Type');
insert into QDS_ATTRIBUTES values('82', 'patient preference', '11', 'Data Type');
insert into QDS_ATTRIBUTES values('83', 'provider preference', '11', 'Data Type');
insert into QDS_ATTRIBUTES values('84', 'reaction', '11', 'Data Type');
insert into QDS_ATTRIBUTES values('85', 'start datetime', '11', 'Data Type');
insert into QDS_ATTRIBUTES values('86', 'stop datetime', '11', 'Data Type');
insert into QDS_ATTRIBUTES values('87', 'duration', '12', 'Data Type');
insert into QDS_ATTRIBUTES values('88', 'negation rationale', '12', 'Data Type');
insert into QDS_ATTRIBUTES values('89', 'patient preference', '12', 'Data Type');
insert into QDS_ATTRIBUTES values('90', 'provider preference', '12', 'Data Type');
insert into QDS_ATTRIBUTES values('91', 'reaction', '12', 'Data Type');
insert into QDS_ATTRIBUTES values('92', 'start datetime', '12', 'Data Type');
insert into QDS_ATTRIBUTES values('93', 'stop datetime', '12', 'Data Type');
insert into QDS_ATTRIBUTES values('94', 'duration', '13', 'Data Type');
insert into QDS_ATTRIBUTES values('95', 'negation rationale', '13', 'Data Type');
insert into QDS_ATTRIBUTES values('96', 'patient preference', '13', 'Data Type');
insert into QDS_ATTRIBUTES values('97', 'provider preference', '13', 'Data Type');
insert into QDS_ATTRIBUTES values('98', 'start datetime', '13', 'Data Type');
insert into QDS_ATTRIBUTES values('99', 'stop datetime', '13', 'Data Type');
insert into QDS_ATTRIBUTES values('100', 'duration', '14', 'Data Type');
insert into QDS_ATTRIBUTES values('101', 'negation rationale', '14', 'Data Type');
insert into QDS_ATTRIBUTES values('102', 'patient preference', '14', 'Data Type');
insert into QDS_ATTRIBUTES values('103', 'provider preference', '14', 'Data Type');
insert into QDS_ATTRIBUTES values('104', 'reaction', '14', 'Data Type');
insert into QDS_ATTRIBUTES values('105', 'start datetime', '14', 'Data Type');
insert into QDS_ATTRIBUTES values('106', 'stop datetime', '14', 'Data Type');
insert into QDS_ATTRIBUTES values('107', 'duration', '15', 'Data Type');
insert into QDS_ATTRIBUTES values('108', 'negation rationale', '15', 'Data Type');
insert into QDS_ATTRIBUTES values('109', 'patient preference', '15', 'Data Type');
insert into QDS_ATTRIBUTES values('110', 'provider preference', '15', 'Data Type');
insert into QDS_ATTRIBUTES values('111', 'reason', '15', 'Data Type');
insert into QDS_ATTRIBUTES values('112', 'start datetime', '15', 'Data Type');
insert into QDS_ATTRIBUTES values('113', 'stop datetime', '15', 'Data Type');
insert into QDS_ATTRIBUTES values('114', 'duration', '78', 'Data Type');
insert into QDS_ATTRIBUTES values('115', 'negation rationale', '78', 'Data Type');
insert into QDS_ATTRIBUTES values('116', 'patient preference', '78', 'Data Type');
insert into QDS_ATTRIBUTES values('117', 'provider preference', '78', 'Data Type');
insert into QDS_ATTRIBUTES values('118', 'reason', '78', 'Data Type');
insert into QDS_ATTRIBUTES values('119', 'start datetime', '78', 'Data Type');
insert into QDS_ATTRIBUTES values('120', 'stop datetime', '78', 'Data Type');
insert into QDS_ATTRIBUTES values('121', 'duration', '16', 'Data Type');
insert into QDS_ATTRIBUTES values('122', 'negation rationale', '16', 'Data Type');
insert into QDS_ATTRIBUTES values('123', 'patient preference', '16', 'Data Type');
insert into QDS_ATTRIBUTES values('124', 'provider preference', '16', 'Data Type');
insert into QDS_ATTRIBUTES values('125', 'radiationdosage', '16', 'Data Type');
insert into QDS_ATTRIBUTES values('126', 'radiationduration', '16', 'Data Type');
insert into QDS_ATTRIBUTES values('127', 'reaction', '16', 'Data Type');
insert into QDS_ATTRIBUTES values('128', 'start datetime', '16', 'Data Type');
insert into QDS_ATTRIBUTES values('129', 'stop datetime', '16', 'Data Type');
insert into QDS_ATTRIBUTES values('130', 'duration', '17', 'Data Type');
insert into QDS_ATTRIBUTES values('131', 'negation rationale', '17', 'Data Type');
insert into QDS_ATTRIBUTES values('132', 'patient preference', '17', 'Data Type');
insert into QDS_ATTRIBUTES values('133', 'provider preference', '17', 'Data Type');
insert into QDS_ATTRIBUTES values('134', 'radiationdosage', '17', 'Data Type');
insert into QDS_ATTRIBUTES values('135', 'radiationduration', '17', 'Data Type');
insert into QDS_ATTRIBUTES values('136', 'reaction', '17', 'Data Type');
insert into QDS_ATTRIBUTES values('137', 'start datetime', '17', 'Data Type');
insert into QDS_ATTRIBUTES values('138', 'stop datetime', '17', 'Data Type');
insert into QDS_ATTRIBUTES values('139', 'duration', '18', 'Data Type');
insert into QDS_ATTRIBUTES values('140', 'method', '18', 'Data Type');
insert into QDS_ATTRIBUTES values('141', 'negation rationale', '18', 'Data Type');
insert into QDS_ATTRIBUTES values('142', 'patient preference', '18', 'Data Type');
insert into QDS_ATTRIBUTES values('143', 'provider preference', '18', 'Data Type');
insert into QDS_ATTRIBUTES values('144', 'radiationdosage', '18', 'Data Type');
insert into QDS_ATTRIBUTES values('145', 'radiationduration', '18', 'Data Type');
insert into QDS_ATTRIBUTES values('146', 'reason', '18', 'Data Type');
insert into QDS_ATTRIBUTES values('147', 'start datetime', '18', 'Data Type');
insert into QDS_ATTRIBUTES values('148', 'stop datetime', '18', 'Data Type');
insert into QDS_ATTRIBUTES values('149', 'duration', '19', 'Data Type');
insert into QDS_ATTRIBUTES values('150', 'method', '19', 'Data Type');
insert into QDS_ATTRIBUTES values('151', 'negation rationale', '19', 'Data Type');
insert into QDS_ATTRIBUTES values('152', 'patient preference', '19', 'Data Type');
insert into QDS_ATTRIBUTES values('153', 'provider preference', '19', 'Data Type');
insert into QDS_ATTRIBUTES values('154', 'radiationdosage', '19', 'Data Type');
insert into QDS_ATTRIBUTES values('155', 'radiationduration', '19', 'Data Type');
insert into QDS_ATTRIBUTES values('156', 'reason', '19', 'Data Type');
insert into QDS_ATTRIBUTES values('157', 'start datetime', '19', 'Data Type');
insert into QDS_ATTRIBUTES values('158', 'stop datetime', '19', 'Data Type');
insert into QDS_ATTRIBUTES values('159', 'duration', '90', 'Data Type');
insert into QDS_ATTRIBUTES values('160', 'method', '90', 'Data Type');
insert into QDS_ATTRIBUTES values('161', 'negation rationale', '90', 'Data Type');
insert into QDS_ATTRIBUTES values('162', 'patient preference', '90', 'Data Type');
insert into QDS_ATTRIBUTES values('163', 'provider preference', '90', 'Data Type');
insert into QDS_ATTRIBUTES values('164', 'radiationdosage', '90', 'Data Type');
insert into QDS_ATTRIBUTES values('165', 'radiationduration', '90', 'Data Type');
insert into QDS_ATTRIBUTES values('166', 'start datetime', '90', 'Data Type');
insert into QDS_ATTRIBUTES values('167', 'stop datetime', '90', 'Data Type');
insert into QDS_ATTRIBUTES values('168', 'duration', '20', 'Data Type');
insert into QDS_ATTRIBUTES values('169', 'method', '20', 'Data Type');
insert into QDS_ATTRIBUTES values('170', 'negation rationale', '20', 'Data Type');
insert into QDS_ATTRIBUTES values('171', 'patient preference', '20', 'Data Type');
insert into QDS_ATTRIBUTES values('172', 'provider preference', '20', 'Data Type');
insert into QDS_ATTRIBUTES values('173', 'radiationdosage', '20', 'Data Type');
insert into QDS_ATTRIBUTES values('174', 'radiationduration', '20', 'Data Type');
insert into QDS_ATTRIBUTES values('175', 'reason', '20', 'Data Type');
insert into QDS_ATTRIBUTES values('176', 'result', '20', 'Data Type');
insert into QDS_ATTRIBUTES values('177', 'start datetime', '20', 'Data Type');
insert into QDS_ATTRIBUTES values('178', 'stop datetime', '20', 'Data Type');
insert into QDS_ATTRIBUTES values('179', 'duration', '95', 'Data Type');
insert into QDS_ATTRIBUTES values('180', 'durationfromarrival', '95', 'Data Type');
insert into QDS_ATTRIBUTES values('181', 'hospital location', '95', 'Data Type');
insert into QDS_ATTRIBUTES values('182', 'negation rationale', '95', 'Data Type');
insert into QDS_ATTRIBUTES values('183', 'patient preference', '95', 'Data Type');
insert into QDS_ATTRIBUTES values('184', 'provider preference', '95', 'Data Type');
insert into QDS_ATTRIBUTES values('185', 'reason', '95', 'Data Type');
insert into QDS_ATTRIBUTES values('186', 'start datetime', '95', 'Data Type');
insert into QDS_ATTRIBUTES values('187', 'stop datetime', '95', 'Data Type');
insert into QDS_ATTRIBUTES values('188', 'duration', '94', 'Data Type');
insert into QDS_ATTRIBUTES values('189', 'durationfromarrival', '94', 'Data Type');
insert into QDS_ATTRIBUTES values('190', 'hospital location', '94', 'Data Type');
insert into QDS_ATTRIBUTES values('191', 'negation rationale', '94', 'Data Type');
insert into QDS_ATTRIBUTES values('192', 'patient preference', '94', 'Data Type');
insert into QDS_ATTRIBUTES values('193', 'provider preference', '94', 'Data Type');
insert into QDS_ATTRIBUTES values('194', 'reason', '94', 'Data Type');
insert into QDS_ATTRIBUTES values('195', 'start datetime', '94', 'Data Type');
insert into QDS_ATTRIBUTES values('196', 'stop datetime', '94', 'Data Type');
insert into QDS_ATTRIBUTES values('197', 'duration', '21', 'Data Type');
insert into QDS_ATTRIBUTES values('198', 'hospital location', '21', 'Data Type');
insert into QDS_ATTRIBUTES values('199', 'negation rationale', '21', 'Data Type');
insert into QDS_ATTRIBUTES values('200', 'patient preference', '21', 'Data Type');
insert into QDS_ATTRIBUTES values('201', 'provider preference', '21', 'Data Type');
insert into QDS_ATTRIBUTES values('202', 'reason', '21', 'Data Type');
insert into QDS_ATTRIBUTES values('203', 'start datetime', '21', 'Data Type');
insert into QDS_ATTRIBUTES values('204', 'stop datetime', '21', 'Data Type');
insert into QDS_ATTRIBUTES values('205', 'duration', '22', 'Data Type');
insert into QDS_ATTRIBUTES values('206', 'durationfromarrival', '22', 'Data Type');
insert into QDS_ATTRIBUTES values('207', 'hospital location', '22', 'Data Type');
insert into QDS_ATTRIBUTES values('208', 'negation rationale', '22', 'Data Type');
insert into QDS_ATTRIBUTES values('209', 'patient preference', '22', 'Data Type');
insert into QDS_ATTRIBUTES values('210', 'provider preference', '22', 'Data Type');
insert into QDS_ATTRIBUTES values('211', 'reason', '22', 'Data Type');
insert into QDS_ATTRIBUTES values('212', 'start datetime', '22', 'Data Type');
insert into QDS_ATTRIBUTES values('213', 'stop datetime', '22', 'Data Type');
insert into QDS_ATTRIBUTES values('214', 'duration', '79', 'Data Type');
insert into QDS_ATTRIBUTES values('215', 'hospital location', '79', 'Data Type');
insert into QDS_ATTRIBUTES values('216', 'negation rationale', '79', 'Data Type');
insert into QDS_ATTRIBUTES values('217', 'patient preference', '79', 'Data Type');
insert into QDS_ATTRIBUTES values('218', 'provider preference', '79', 'Data Type');
insert into QDS_ATTRIBUTES values('219', 'reason', '79', 'Data Type');
insert into QDS_ATTRIBUTES values('220', 'start datetime', '79', 'Data Type');
insert into QDS_ATTRIBUTES values('221', 'stop datetime', '79', 'Data Type');
insert into QDS_ATTRIBUTES values('222', 'duration', '23', 'Data Type');
insert into QDS_ATTRIBUTES values('223', 'method', '23', 'Data Type');
insert into QDS_ATTRIBUTES values('224', 'negation rationale', '23', 'Data Type');
insert into QDS_ATTRIBUTES values('225', 'patient preference', '23', 'Data Type');
insert into QDS_ATTRIBUTES values('226', 'provider preference', '23', 'Data Type');
insert into QDS_ATTRIBUTES values('227', 'reason', '23', 'Data Type');
insert into QDS_ATTRIBUTES values('228', 'start datetime', '23', 'Data Type');
insert into QDS_ATTRIBUTES values('229', 'stop datetime', '23', 'Data Type');
insert into QDS_ATTRIBUTES values('230', 'duration', '24', 'Data Type');
insert into QDS_ATTRIBUTES values('231', 'method', '24', 'Data Type');
insert into QDS_ATTRIBUTES values('232', 'negation rationale', '24', 'Data Type');
insert into QDS_ATTRIBUTES values('233', 'patient preference', '24', 'Data Type');
insert into QDS_ATTRIBUTES values('234', 'provider preference', '24', 'Data Type');
insert into QDS_ATTRIBUTES values('235', 'reason', '24', 'Data Type');
insert into QDS_ATTRIBUTES values('236', 'start datetime', '24', 'Data Type');
insert into QDS_ATTRIBUTES values('237', 'stop datetime', '24', 'Data Type');
insert into QDS_ATTRIBUTES values('238', 'duration', '80', 'Data Type');
insert into QDS_ATTRIBUTES values('239', 'method', '80', 'Data Type');
insert into QDS_ATTRIBUTES values('240', 'negation rationale', '80', 'Data Type');
insert into QDS_ATTRIBUTES values('241', 'patient preference', '80', 'Data Type');
insert into QDS_ATTRIBUTES values('242', 'provider preference', '80', 'Data Type');
insert into QDS_ATTRIBUTES values('243', 'reason', '80', 'Data Type');
insert into QDS_ATTRIBUTES values('244', 'start datetime', '80', 'Data Type');
insert into QDS_ATTRIBUTES values('245', 'stop datetime', '80', 'Data Type');
insert into QDS_ATTRIBUTES values('246', 'duration', '25', 'Data Type');
insert into QDS_ATTRIBUTES values('247', 'method', '25', 'Data Type');
insert into QDS_ATTRIBUTES values('248', 'negation rationale', '25', 'Data Type');
insert into QDS_ATTRIBUTES values('249', 'patient preference', '25', 'Data Type');
insert into QDS_ATTRIBUTES values('250', 'provider preference', '25', 'Data Type');
insert into QDS_ATTRIBUTES values('251', 'reason', '25', 'Data Type');
insert into QDS_ATTRIBUTES values('252', 'result', '25', 'Data Type');
insert into QDS_ATTRIBUTES values('253', 'start datetime', '25', 'Data Type');
insert into QDS_ATTRIBUTES values('254', 'stop datetime', '25', 'Data Type');
insert into QDS_ATTRIBUTES values('255', 'duration', '26', 'Data Type');
insert into QDS_ATTRIBUTES values('256', 'start datetime', '26', 'Data Type');
insert into QDS_ATTRIBUTES values('257', 'stop datetime', '26', 'Data Type');
insert into QDS_ATTRIBUTES values('258', 'duration', '27', 'Data Type');
insert into QDS_ATTRIBUTES values('259', 'negation rationale', '27', 'Data Type');
insert into QDS_ATTRIBUTES values('260', 'start datetime', '27', 'Data Type');
insert into QDS_ATTRIBUTES values('261', 'stop datetime', '27', 'Data Type');
insert into QDS_ATTRIBUTES values('262', 'duration', '28', 'Data Type');
insert into QDS_ATTRIBUTES values('263', 'negation rationale', '28', 'Data Type');
insert into QDS_ATTRIBUTES values('264', 'patient preference', '28', 'Data Type');
insert into QDS_ATTRIBUTES values('265', 'provider preference', '28', 'Data Type');
insert into QDS_ATTRIBUTES values('266', 'reaction', '28', 'Data Type');
insert into QDS_ATTRIBUTES values('267', 'start datetime', '28', 'Data Type');
insert into QDS_ATTRIBUTES values('268', 'stop datetime', '28', 'Data Type');
insert into QDS_ATTRIBUTES values('269', 'duration', '29', 'Data Type');
insert into QDS_ATTRIBUTES values('270', 'negation rationale', '29', 'Data Type');
insert into QDS_ATTRIBUTES values('271', 'patient preference', '29', 'Data Type');
insert into QDS_ATTRIBUTES values('272', 'provider preference', '29', 'Data Type');
insert into QDS_ATTRIBUTES values('273', 'reaction', '29', 'Data Type');
insert into QDS_ATTRIBUTES values('274', 'start datetime', '29', 'Data Type');
insert into QDS_ATTRIBUTES values('275', 'stop datetime', '29', 'Data Type');
insert into QDS_ATTRIBUTES values('276', 'duration', '30', 'Data Type');
insert into QDS_ATTRIBUTES values('277', 'method', '30', 'Data Type');
insert into QDS_ATTRIBUTES values('278', 'negation rationale', '30', 'Data Type');
insert into QDS_ATTRIBUTES values('279', 'patient preference', '30', 'Data Type');
insert into QDS_ATTRIBUTES values('280', 'provider preference', '30', 'Data Type');
insert into QDS_ATTRIBUTES values('281', 'reason', '30', 'Data Type');
insert into QDS_ATTRIBUTES values('282', 'start datetime', '30', 'Data Type');
insert into QDS_ATTRIBUTES values('283', 'stop datetime', '30', 'Data Type');
insert into QDS_ATTRIBUTES values('284', 'duration', '31', 'Data Type');
insert into QDS_ATTRIBUTES values('285', 'method', '31', 'Data Type');
insert into QDS_ATTRIBUTES values('286', 'negation rationale', '31', 'Data Type');
insert into QDS_ATTRIBUTES values('287', 'patient preference', '31', 'Data Type');
insert into QDS_ATTRIBUTES values('288', 'provider preference', '31', 'Data Type');
insert into QDS_ATTRIBUTES values('289', 'reason', '31', 'Data Type');
insert into QDS_ATTRIBUTES values('290', 'start datetime', '31', 'Data Type');
insert into QDS_ATTRIBUTES values('291', 'stop datetime', '31', 'Data Type');
insert into QDS_ATTRIBUTES values('292', 'duration', '81', 'Data Type');
insert into QDS_ATTRIBUTES values('293', 'method', '81', 'Data Type');
insert into QDS_ATTRIBUTES values('294', 'negation rationale', '81', 'Data Type');
insert into QDS_ATTRIBUTES values('295', 'patient preference', '81', 'Data Type');
insert into QDS_ATTRIBUTES values('296', 'provider preference', '81', 'Data Type');
insert into QDS_ATTRIBUTES values('297', 'reason', '81', 'Data Type');
insert into QDS_ATTRIBUTES values('298', 'start datetime', '81', 'Data Type');
insert into QDS_ATTRIBUTES values('299', 'stop datetime', '81', 'Data Type');
insert into QDS_ATTRIBUTES values('300', 'duration', '32', 'Data Type');
insert into QDS_ATTRIBUTES values('301', 'method', '32', 'Data Type');
insert into QDS_ATTRIBUTES values('302', 'negation rationale', '32', 'Data Type');
insert into QDS_ATTRIBUTES values('303', 'patient preference', '32', 'Data Type');
insert into QDS_ATTRIBUTES values('304', 'provider preference', '32', 'Data Type');
insert into QDS_ATTRIBUTES values('305', 'reason', '32', 'Data Type');
insert into QDS_ATTRIBUTES values('306', 'result', '32', 'Data Type');
insert into QDS_ATTRIBUTES values('307', 'start datetime', '32', 'Data Type');
insert into QDS_ATTRIBUTES values('308', 'stop datetime', '32', 'Data Type');
insert into QDS_ATTRIBUTES values('309', 'duration', '33', 'Data Type');
insert into QDS_ATTRIBUTES values('310', 'negation rationale', '33', 'Data Type');
insert into QDS_ATTRIBUTES values('311', 'patient preference', '33', 'Data Type');
insert into QDS_ATTRIBUTES values('312', 'provider preference', '33', 'Data Type');
insert into QDS_ATTRIBUTES values('313', 'reaction', '33', 'Data Type');
insert into QDS_ATTRIBUTES values('314', 'start datetime', '33', 'Data Type');
insert into QDS_ATTRIBUTES values('315', 'stop datetime', '33', 'Data Type');
insert into QDS_ATTRIBUTES values('316', 'duration', '34', 'Data Type');
insert into QDS_ATTRIBUTES values('317', 'negation rationale', '34', 'Data Type');
insert into QDS_ATTRIBUTES values('318', 'patient preference', '34', 'Data Type');
insert into QDS_ATTRIBUTES values('319', 'provider preference', '34', 'Data Type');
insert into QDS_ATTRIBUTES values('320', 'reaction', '34', 'Data Type');
insert into QDS_ATTRIBUTES values('321', 'start datetime', '34', 'Data Type');
insert into QDS_ATTRIBUTES values('322', 'stop datetime', '34', 'Data Type');
insert into QDS_ATTRIBUTES values('323', 'duration', '35', 'Data Type');
insert into QDS_ATTRIBUTES values('324', 'method', '35', 'Data Type');
insert into QDS_ATTRIBUTES values('325', 'negation rationale', '35', 'Data Type');
insert into QDS_ATTRIBUTES values('326', 'patient preference', '35', 'Data Type');
insert into QDS_ATTRIBUTES values('327', 'provider preference', '35', 'Data Type');
insert into QDS_ATTRIBUTES values('328', 'reason', '35', 'Data Type');
insert into QDS_ATTRIBUTES values('329', 'start datetime', '35', 'Data Type');
insert into QDS_ATTRIBUTES values('330', 'stop datetime', '35', 'Data Type');
insert into QDS_ATTRIBUTES values('331', 'duration', '36', 'Data Type');
insert into QDS_ATTRIBUTES values('332', 'method', '36', 'Data Type');
insert into QDS_ATTRIBUTES values('333', 'negation rationale', '36', 'Data Type');
insert into QDS_ATTRIBUTES values('334', 'patient preference', '36', 'Data Type');
insert into QDS_ATTRIBUTES values('335', 'provider preference', '36', 'Data Type');
insert into QDS_ATTRIBUTES values('336', 'reason', '36', 'Data Type');
insert into QDS_ATTRIBUTES values('337', 'start datetime', '36', 'Data Type');
insert into QDS_ATTRIBUTES values('338', 'stop datetime', '36', 'Data Type');
insert into QDS_ATTRIBUTES values('339', 'duration', '82', 'Data Type');
insert into QDS_ATTRIBUTES values('340', 'method', '82', 'Data Type');
insert into QDS_ATTRIBUTES values('341', 'negation rationale', '82', 'Data Type');
insert into QDS_ATTRIBUTES values('342', 'patient preference', '82', 'Data Type');
insert into QDS_ATTRIBUTES values('343', 'provider preference', '82', 'Data Type');
insert into QDS_ATTRIBUTES values('344', 'reason', '82', 'Data Type');
insert into QDS_ATTRIBUTES values('345', 'start datetime', '82', 'Data Type');
insert into QDS_ATTRIBUTES values('346', 'stop datetime', '82', 'Data Type');
insert into QDS_ATTRIBUTES values('347', 'duration', '37', 'Data Type');
insert into QDS_ATTRIBUTES values('348', 'method', '37', 'Data Type');
insert into QDS_ATTRIBUTES values('349', 'negation rationale', '37', 'Data Type');
insert into QDS_ATTRIBUTES values('350', 'patient preference', '37', 'Data Type');
insert into QDS_ATTRIBUTES values('351', 'provider preference', '37', 'Data Type');
insert into QDS_ATTRIBUTES values('352', 'reason', '37', 'Data Type');
insert into QDS_ATTRIBUTES values('353', 'result', '37', 'Data Type');
insert into QDS_ATTRIBUTES values('354', 'start datetime', '37', 'Data Type');
insert into QDS_ATTRIBUTES values('355', 'stop datetime', '37', 'Data Type');
insert into QDS_ATTRIBUTES values('356', 'cumulativeMedicationDuration', '38', 'Data Type');
insert into QDS_ATTRIBUTES values('357', 'dose', '38', 'Data Type');
insert into QDS_ATTRIBUTES values('358', 'frequency', '38', 'Data Type');
insert into QDS_ATTRIBUTES values('359', 'infusionDuration', '38', 'Data Type');
insert into QDS_ATTRIBUTES values('360', 'negation rationale', '38', 'Data Type');
insert into QDS_ATTRIBUTES values('361', 'number', '38', 'Data Type');
insert into QDS_ATTRIBUTES values('362', 'patient preference', '38', 'Data Type');
insert into QDS_ATTRIBUTES values('363', 'provider preference', '38', 'Data Type');
insert into QDS_ATTRIBUTES values('364', 'refills', '38', 'Data Type');
insert into QDS_ATTRIBUTES values('365', 'route', '38', 'Data Type');
insert into QDS_ATTRIBUTES values('366', 'start datetime', '38', 'Data Type');
insert into QDS_ATTRIBUTES values('367', 'stop datetime', '38', 'Data Type');
insert into QDS_ATTRIBUTES values('368', 'dose', '39', 'Data Type');
insert into QDS_ATTRIBUTES values('369', 'frequency', '39', 'Data Type');
insert into QDS_ATTRIBUTES values('370', 'infusionDuration', '39', 'Data Type');
insert into QDS_ATTRIBUTES values('371', 'negation rationale', '39', 'Data Type');
insert into QDS_ATTRIBUTES values('372', 'number', '39', 'Data Type');
insert into QDS_ATTRIBUTES values('373', 'patient preference', '39', 'Data Type');
insert into QDS_ATTRIBUTES values('374', 'provider preference', '39', 'Data Type');
insert into QDS_ATTRIBUTES values('375', 'refills', '39', 'Data Type');
insert into QDS_ATTRIBUTES values('376', 'route', '39', 'Data Type');
insert into QDS_ATTRIBUTES values('377', 'start datetime', '39', 'Data Type');
insert into QDS_ATTRIBUTES values('378', 'stop datetime', '39', 'Data Type');
insert into QDS_ATTRIBUTES values('379', 'dose', '40', 'Data Type');
insert into QDS_ATTRIBUTES values('380', 'frequency', '40', 'Data Type');
insert into QDS_ATTRIBUTES values('381', 'infusionDuration', '40', 'Data Type');
insert into QDS_ATTRIBUTES values('382', 'negation rationale', '40', 'Data Type');
insert into QDS_ATTRIBUTES values('383', 'number', '40', 'Data Type');
insert into QDS_ATTRIBUTES values('384', 'patient preference', '40', 'Data Type');
insert into QDS_ATTRIBUTES values('385', 'provider preference', '40', 'Data Type');
insert into QDS_ATTRIBUTES values('386', 'reaction', '40', 'Data Type');
insert into QDS_ATTRIBUTES values('387', 'refills', '40', 'Data Type');
insert into QDS_ATTRIBUTES values('388', 'route', '40', 'Data Type');
insert into QDS_ATTRIBUTES values('389', 'start datetime', '40', 'Data Type');
insert into QDS_ATTRIBUTES values('390', 'stop datetime', '40', 'Data Type');
insert into QDS_ATTRIBUTES values('391', 'dose', '41', 'Data Type');
insert into QDS_ATTRIBUTES values('392', 'frequency', '41', 'Data Type');
insert into QDS_ATTRIBUTES values('393', 'infusionDuration', '41', 'Data Type');
insert into QDS_ATTRIBUTES values('394', 'negation rationale', '41', 'Data Type');
insert into QDS_ATTRIBUTES values('395', 'number', '41', 'Data Type');
insert into QDS_ATTRIBUTES values('396', 'patient preference', '41', 'Data Type');
insert into QDS_ATTRIBUTES values('397', 'provider preference', '41', 'Data Type');
insert into QDS_ATTRIBUTES values('398', 'reaction', '41', 'Data Type');
insert into QDS_ATTRIBUTES values('399', 'refills', '41', 'Data Type');
insert into QDS_ATTRIBUTES values('400', 'route', '41', 'Data Type');
insert into QDS_ATTRIBUTES values('401', 'start datetime', '41', 'Data Type');
insert into QDS_ATTRIBUTES values('402', 'stop datetime', '41', 'Data Type');
insert into QDS_ATTRIBUTES values('403', 'cumulativeMedicationDuration', '42', 'Data Type');
insert into QDS_ATTRIBUTES values('404', 'dose', '42', 'Data Type');
insert into QDS_ATTRIBUTES values('405', 'frequency', '42', 'Data Type');
insert into QDS_ATTRIBUTES values('406', 'infusionDuration', '42', 'Data Type');
insert into QDS_ATTRIBUTES values('407', 'negation rationale', '42', 'Data Type');
insert into QDS_ATTRIBUTES values('408', 'number', '42', 'Data Type');
insert into QDS_ATTRIBUTES values('409', 'patient preference', '42', 'Data Type');
insert into QDS_ATTRIBUTES values('410', 'provider preference', '42', 'Data Type');
insert into QDS_ATTRIBUTES values('411', 'refills', '42', 'Data Type');
insert into QDS_ATTRIBUTES values('412', 'route', '42', 'Data Type');
insert into QDS_ATTRIBUTES values('413', 'start datetime', '42', 'Data Type');
insert into QDS_ATTRIBUTES values('414', 'stop datetime', '42', 'Data Type');
insert into QDS_ATTRIBUTES values('415', 'dose', '43', 'Data Type');
insert into QDS_ATTRIBUTES values('416', 'frequency', '43', 'Data Type');
insert into QDS_ATTRIBUTES values('417', 'infusionDuration', '43', 'Data Type');
insert into QDS_ATTRIBUTES values('418', 'negation rationale', '43', 'Data Type');
insert into QDS_ATTRIBUTES values('419', 'number', '43', 'Data Type');
insert into QDS_ATTRIBUTES values('420', 'patient preference', '43', 'Data Type');
insert into QDS_ATTRIBUTES values('421', 'provider preference', '43', 'Data Type');
insert into QDS_ATTRIBUTES values('422', 'reaction', '43', 'Data Type');
insert into QDS_ATTRIBUTES values('423', 'refills', '43', 'Data Type');
insert into QDS_ATTRIBUTES values('424', 'route', '43', 'Data Type');
insert into QDS_ATTRIBUTES values('425', 'start datetime', '43', 'Data Type');
insert into QDS_ATTRIBUTES values('426', 'stop datetime', '43', 'Data Type');
insert into QDS_ATTRIBUTES values('427', 'cumulativeMedicationDuration', '44', 'Data Type');
insert into QDS_ATTRIBUTES values('428', 'dose', '44', 'Data Type');
insert into QDS_ATTRIBUTES values('429', 'frequency', '44', 'Data Type');
insert into QDS_ATTRIBUTES values('430', 'infusionDuration', '44', 'Data Type');
insert into QDS_ATTRIBUTES values('431', 'method', '44', 'Data Type');
insert into QDS_ATTRIBUTES values('432', 'negation rationale', '44', 'Data Type');
insert into QDS_ATTRIBUTES values('433', 'number', '44', 'Data Type');
insert into QDS_ATTRIBUTES values('434', 'patient preference', '44', 'Data Type');
insert into QDS_ATTRIBUTES values('435', 'provider preference', '44', 'Data Type');
insert into QDS_ATTRIBUTES values('436', 'reason', '44', 'Data Type');
insert into QDS_ATTRIBUTES values('437', 'refills', '44', 'Data Type');
insert into QDS_ATTRIBUTES values('438', 'route', '44', 'Data Type');
insert into QDS_ATTRIBUTES values('439', 'start datetime', '44', 'Data Type');
insert into QDS_ATTRIBUTES values('440', 'stop datetime', '44', 'Data Type');
insert into QDS_ATTRIBUTES values('441', 'anatomical location', '55', 'Data Type');
insert into QDS_ATTRIBUTES values('442', 'duration', '55', 'Data Type');
insert into QDS_ATTRIBUTES values('443', 'method', '55', 'Data Type');
insert into QDS_ATTRIBUTES values('444', 'negation rationale', '55', 'Data Type');
insert into QDS_ATTRIBUTES values('445', 'patient preference', '55', 'Data Type');
insert into QDS_ATTRIBUTES values('446', 'provider preference', '55', 'Data Type');
insert into QDS_ATTRIBUTES values('447', 'reason', '55', 'Data Type');
insert into QDS_ATTRIBUTES values('448', 'result', '55', 'Data Type');
insert into QDS_ATTRIBUTES values('449', 'start datetime', '55', 'Data Type');
insert into QDS_ATTRIBUTES values('450', 'stop datetime', '55', 'Data Type');
insert into QDS_ATTRIBUTES values('451', 'anatomical location', '56', 'Data Type');
insert into QDS_ATTRIBUTES values('452', 'duration', '56', 'Data Type');
insert into QDS_ATTRIBUTES values('453', 'method', '56', 'Data Type');
insert into QDS_ATTRIBUTES values('454', 'negation rationale', '56', 'Data Type');
insert into QDS_ATTRIBUTES values('455', 'patient preference', '56', 'Data Type');
insert into QDS_ATTRIBUTES values('456', 'provider preference', '56', 'Data Type');
insert into QDS_ATTRIBUTES values('457', 'reason', '56', 'Data Type');
insert into QDS_ATTRIBUTES values('458', 'start datetime', '56', 'Data Type');
insert into QDS_ATTRIBUTES values('459', 'stop datetime', '56', 'Data Type');
insert into QDS_ATTRIBUTES values('460', 'anatomical location', '57', 'Data Type');
insert into QDS_ATTRIBUTES values('461', 'duration', '57', 'Data Type');
insert into QDS_ATTRIBUTES values('462', 'method', '57', 'Data Type');
insert into QDS_ATTRIBUTES values('463', 'negation rationale', '57', 'Data Type');
insert into QDS_ATTRIBUTES values('464', 'patient preference', '57', 'Data Type');
insert into QDS_ATTRIBUTES values('465', 'provider preference', '57', 'Data Type');
insert into QDS_ATTRIBUTES values('466', 'reason', '57', 'Data Type');
insert into QDS_ATTRIBUTES values('467', 'start datetime', '57', 'Data Type');
insert into QDS_ATTRIBUTES values('468', 'stop datetime', '57', 'Data Type');
insert into QDS_ATTRIBUTES values('469', 'anatomical location', '87', 'Data Type');
insert into QDS_ATTRIBUTES values('470', 'duration', '87', 'Data Type');
insert into QDS_ATTRIBUTES values('471', 'method', '87', 'Data Type');
insert into QDS_ATTRIBUTES values('472', 'negation rationale', '87', 'Data Type');
insert into QDS_ATTRIBUTES values('473', 'patient preference', '87', 'Data Type');
insert into QDS_ATTRIBUTES values('474', 'provider preference', '87', 'Data Type');
insert into QDS_ATTRIBUTES values('475', 'reason', '87', 'Data Type');
insert into QDS_ATTRIBUTES values('476', 'start datetime', '87', 'Data Type');
insert into QDS_ATTRIBUTES values('477', 'stop datetime', '87', 'Data Type');
insert into QDS_ATTRIBUTES values('478', 'duration', '60', 'Data Type');
insert into QDS_ATTRIBUTES values('479', 'negation rationale', '60', 'Data Type');
insert into QDS_ATTRIBUTES values('480', 'patient preference', '60', 'Data Type');
insert into QDS_ATTRIBUTES values('481', 'provider preference', '60', 'Data Type');
insert into QDS_ATTRIBUTES values('482', 'reaction', '60', 'Data Type');
insert into QDS_ATTRIBUTES values('483', 'start datetime', '60', 'Data Type');
insert into QDS_ATTRIBUTES values('484', 'stop datetime', '60', 'Data Type');
insert into QDS_ATTRIBUTES values('485', 'duration', '61', 'Data Type');
insert into QDS_ATTRIBUTES values('486', 'negation rationale', '61', 'Data Type');
insert into QDS_ATTRIBUTES values('487', 'patient preference', '61', 'Data Type');
insert into QDS_ATTRIBUTES values('488', 'provider preference', '61', 'Data Type');
insert into QDS_ATTRIBUTES values('489', 'reaction', '61', 'Data Type');
insert into QDS_ATTRIBUTES values('490', 'start datetime', '61', 'Data Type');
insert into QDS_ATTRIBUTES values('491', 'stop datetime', '61', 'Data Type');
insert into QDS_ATTRIBUTES values('492', 'duration', '62', 'Data Type');
insert into QDS_ATTRIBUTES values('493', 'method', '62', 'Data Type');
insert into QDS_ATTRIBUTES values('494', 'negation rationale', '62', 'Data Type');
insert into QDS_ATTRIBUTES values('495', 'patient preference', '62', 'Data Type');
insert into QDS_ATTRIBUTES values('496', 'provider preference', '62', 'Data Type');
insert into QDS_ATTRIBUTES values('497', 'reason', '62', 'Data Type');
insert into QDS_ATTRIBUTES values('498', 'start datetime', '62', 'Data Type');
insert into QDS_ATTRIBUTES values('499', 'stop datetime', '62', 'Data Type');
insert into QDS_ATTRIBUTES values('500', 'duration', '63', 'Data Type');
insert into QDS_ATTRIBUTES values('501', 'method', '63', 'Data Type');
insert into QDS_ATTRIBUTES values('502', 'negation rationale', '63', 'Data Type');
insert into QDS_ATTRIBUTES values('503', 'patient preference', '63', 'Data Type');
insert into QDS_ATTRIBUTES values('504', 'provider preference', '63', 'Data Type');
insert into QDS_ATTRIBUTES values('505', 'reason', '63', 'Data Type');
insert into QDS_ATTRIBUTES values('506', 'start datetime', '63', 'Data Type');
insert into QDS_ATTRIBUTES values('507', 'stop datetime', '63', 'Data Type');
insert into QDS_ATTRIBUTES values('508', 'duration', '88', 'Data Type');
insert into QDS_ATTRIBUTES values('509', 'method', '88', 'Data Type');
insert into QDS_ATTRIBUTES values('510', 'negation rationale', '88', 'Data Type');
insert into QDS_ATTRIBUTES values('511', 'patient preference', '88', 'Data Type');
insert into QDS_ATTRIBUTES values('512', 'provider preference', '88', 'Data Type');
insert into QDS_ATTRIBUTES values('513', 'reason', '88', 'Data Type');
insert into QDS_ATTRIBUTES values('514', 'start datetime', '88', 'Data Type');
insert into QDS_ATTRIBUTES values('515', 'stop datetime', '88', 'Data Type');
insert into QDS_ATTRIBUTES values('516', 'duration', '64', 'Data Type');
insert into QDS_ATTRIBUTES values('517', 'method', '64', 'Data Type');
insert into QDS_ATTRIBUTES values('518', 'negation rationale', '64', 'Data Type');
insert into QDS_ATTRIBUTES values('519', 'patient preference', '64', 'Data Type');
insert into QDS_ATTRIBUTES values('520', 'provider preference', '64', 'Data Type');
insert into QDS_ATTRIBUTES values('521', 'reason', '64', 'Data Type');
insert into QDS_ATTRIBUTES values('522', 'result', '64', 'Data Type');
insert into QDS_ATTRIBUTES values('523', 'start datetime', '64', 'Data Type');
insert into QDS_ATTRIBUTES values('524', 'stop datetime', '64', 'Data Type');
insert into QDS_ATTRIBUTES values('525', 'duration', '65', 'Data Type');
insert into QDS_ATTRIBUTES values('526', 'negation rationale', '65', 'Data Type');
insert into QDS_ATTRIBUTES values('527', 'patient preference', '65', 'Data Type');
insert into QDS_ATTRIBUTES values('528', 'provider preference', '65', 'Data Type');
insert into QDS_ATTRIBUTES values('529', 'start datetime', '65', 'Data Type');
insert into QDS_ATTRIBUTES values('530', 'stop datetime', '65', 'Data Type');
insert into QDS_ATTRIBUTES values('531', 'dose', '66', 'Data Type');
insert into QDS_ATTRIBUTES values('532', 'duration', '66', 'Data Type');
insert into QDS_ATTRIBUTES values('533', 'frequency', '66', 'Data Type');
insert into QDS_ATTRIBUTES values('534', 'negation rationale', '66', 'Data Type');
insert into QDS_ATTRIBUTES values('535', 'number', '66', 'Data Type');
insert into QDS_ATTRIBUTES values('536', 'patient preference', '66', 'Data Type');
insert into QDS_ATTRIBUTES values('537', 'provider preference', '66', 'Data Type');
insert into QDS_ATTRIBUTES values('538', 'refills', '66', 'Data Type');
insert into QDS_ATTRIBUTES values('539', 'route', '66', 'Data Type');
insert into QDS_ATTRIBUTES values('540', 'start datetime', '66', 'Data Type');
insert into QDS_ATTRIBUTES values('541', 'stop datetime', '66', 'Data Type');
insert into QDS_ATTRIBUTES values('542', 'dose', '67', 'Data Type');
insert into QDS_ATTRIBUTES values('543', 'duration', '67', 'Data Type');
insert into QDS_ATTRIBUTES values('544', 'frequency', '67', 'Data Type');
insert into QDS_ATTRIBUTES values('545', 'negation rationale', '67', 'Data Type');
insert into QDS_ATTRIBUTES values('546', 'number', '67', 'Data Type');
insert into QDS_ATTRIBUTES values('547', 'patient preference', '67', 'Data Type');
insert into QDS_ATTRIBUTES values('548', 'provider preference', '67', 'Data Type');
insert into QDS_ATTRIBUTES values('549', 'reaction', '67', 'Data Type');
insert into QDS_ATTRIBUTES values('550', 'refills', '67', 'Data Type');
insert into QDS_ATTRIBUTES values('551', 'route', '67', 'Data Type');
insert into QDS_ATTRIBUTES values('552', 'start datetime', '67', 'Data Type');
insert into QDS_ATTRIBUTES values('553', 'stop datetime', '67', 'Data Type');
insert into QDS_ATTRIBUTES values('554', 'dose', '68', 'Data Type');
insert into QDS_ATTRIBUTES values('555', 'duration', '68', 'Data Type');
insert into QDS_ATTRIBUTES values('556', 'frequency', '68', 'Data Type');
insert into QDS_ATTRIBUTES values('557', 'negation rationale', '68', 'Data Type');
insert into QDS_ATTRIBUTES values('558', 'number', '68', 'Data Type');
insert into QDS_ATTRIBUTES values('559', 'patient preference', '68', 'Data Type');
insert into QDS_ATTRIBUTES values('560', 'provider preference', '68', 'Data Type');
insert into QDS_ATTRIBUTES values('561', 'reaction', '68', 'Data Type');
insert into QDS_ATTRIBUTES values('562', 'refills', '68', 'Data Type');
insert into QDS_ATTRIBUTES values('563', 'route', '68', 'Data Type');
insert into QDS_ATTRIBUTES values('564', 'start datetime', '68', 'Data Type');
insert into QDS_ATTRIBUTES values('565', 'stop datetime', '68', 'Data Type');
insert into QDS_ATTRIBUTES values('566', 'dose', '69', 'Data Type');
insert into QDS_ATTRIBUTES values('567', 'duration', '69', 'Data Type');
insert into QDS_ATTRIBUTES values('568', 'frequency', '69', 'Data Type');
insert into QDS_ATTRIBUTES values('569', 'negation rationale', '69', 'Data Type');
insert into QDS_ATTRIBUTES values('570', 'number', '69', 'Data Type');
insert into QDS_ATTRIBUTES values('571', 'patient preference', '69', 'Data Type');
insert into QDS_ATTRIBUTES values('572', 'provider preference', '69', 'Data Type');
insert into QDS_ATTRIBUTES values('573', 'reaction', '69', 'Data Type');
insert into QDS_ATTRIBUTES values('574', 'refills', '69', 'Data Type');
insert into QDS_ATTRIBUTES values('575', 'route', '69', 'Data Type');
insert into QDS_ATTRIBUTES values('576', 'start datetime', '69', 'Data Type');
insert into QDS_ATTRIBUTES values('577', 'stop datetime', '69', 'Data Type');
insert into QDS_ATTRIBUTES values('578', 'dose', '70', 'Data Type');
insert into QDS_ATTRIBUTES values('579', 'duration', '70', 'Data Type');
insert into QDS_ATTRIBUTES values('580', 'frequency', '70', 'Data Type');
insert into QDS_ATTRIBUTES values('581', 'method', '70', 'Data Type');
insert into QDS_ATTRIBUTES values('582', 'negation rationale', '70', 'Data Type');
insert into QDS_ATTRIBUTES values('583', 'number', '70', 'Data Type');
insert into QDS_ATTRIBUTES values('584', 'patient preference', '70', 'Data Type');
insert into QDS_ATTRIBUTES values('585', 'provider preference', '70', 'Data Type');
insert into QDS_ATTRIBUTES values('586', 'reason', '70', 'Data Type');
insert into QDS_ATTRIBUTES values('587', 'refills', '70', 'Data Type');
insert into QDS_ATTRIBUTES values('588', 'route', '70', 'Data Type');
insert into QDS_ATTRIBUTES values('589', 'start datetime', '70', 'Data Type');
insert into QDS_ATTRIBUTES values('590', 'stop datetime', '70', 'Data Type');
insert into QDS_ATTRIBUTES values('591', 'dose', '89', 'Data Type');
insert into QDS_ATTRIBUTES values('592', 'duration', '89', 'Data Type');
insert into QDS_ATTRIBUTES values('593', 'frequency', '89', 'Data Type');
insert into QDS_ATTRIBUTES values('594', 'method', '89', 'Data Type');
insert into QDS_ATTRIBUTES values('595', 'negation rationale', '89', 'Data Type');
insert into QDS_ATTRIBUTES values('596', 'number', '89', 'Data Type');
insert into QDS_ATTRIBUTES values('597', 'patient preference', '89', 'Data Type');
insert into QDS_ATTRIBUTES values('598', 'provider preference', '89', 'Data Type');
insert into QDS_ATTRIBUTES values('599', 'reason', '89', 'Data Type');
insert into QDS_ATTRIBUTES values('600', 'refills', '89', 'Data Type');
insert into QDS_ATTRIBUTES values('601', 'route', '89', 'Data Type');
insert into QDS_ATTRIBUTES values('602', 'start datetime', '89', 'Data Type');
insert into QDS_ATTRIBUTES values('603', 'stop datetime', '89', 'Data Type');
insert into QDS_ATTRIBUTES values('604', 'duration', '71', 'Data Type');
insert into QDS_ATTRIBUTES values('605', 'negation rationale', '71', 'Data Type');
insert into QDS_ATTRIBUTES values('606', 'ordinality', '71', 'Data Type');
insert into QDS_ATTRIBUTES values('607', 'patient preference', '71', 'Data Type');
insert into QDS_ATTRIBUTES values('608', 'provider preference', '71', 'Data Type');
insert into QDS_ATTRIBUTES values('609', 'severity', '71', 'Data Type');
insert into QDS_ATTRIBUTES values('610', 'start datetime', '71', 'Data Type');
insert into QDS_ATTRIBUTES values('611', 'status', '71', 'Data Type');
insert into QDS_ATTRIBUTES values('612', 'stop datetime', '71', 'Data Type');
insert into QDS_ATTRIBUTES values('613', 'duration', '72', 'Data Type');
insert into QDS_ATTRIBUTES values('614', 'negation rationale', '72', 'Data Type');
insert into QDS_ATTRIBUTES values('615', 'ordinality', '72', 'Data Type');
insert into QDS_ATTRIBUTES values('616', 'patient preference', '72', 'Data Type');
insert into QDS_ATTRIBUTES values('617', 'provider preference', '72', 'Data Type');
insert into QDS_ATTRIBUTES values('618', 'severity', '72', 'Data Type');
insert into QDS_ATTRIBUTES values('619', 'start datetime', '72', 'Data Type');
insert into QDS_ATTRIBUTES values('620', 'status', '72', 'Data Type');
insert into QDS_ATTRIBUTES values('621', 'stop datetime', '72', 'Data Type');
insert into QDS_ATTRIBUTES values('622', 'duration', '73', 'Data Type');
insert into QDS_ATTRIBUTES values('623', 'negation rationale', '73', 'Data Type');
insert into QDS_ATTRIBUTES values('624', 'ordinality', '73', 'Data Type');
insert into QDS_ATTRIBUTES values('625', 'patient preference', '73', 'Data Type');
insert into QDS_ATTRIBUTES values('626', 'provider preference', '73', 'Data Type');
insert into QDS_ATTRIBUTES values('627', 'severity', '73', 'Data Type');
insert into QDS_ATTRIBUTES values('628', 'start datetime', '73', 'Data Type');
insert into QDS_ATTRIBUTES values('629', 'status', '73', 'Data Type');
insert into QDS_ATTRIBUTES values('630', 'stop datetime', '73', 'Data Type');
insert into QDS_ATTRIBUTES values('631', 'duration', '74', 'Data Type');
insert into QDS_ATTRIBUTES values('632', 'negation rationale', '74', 'Data Type');
insert into QDS_ATTRIBUTES values('633', 'ordinality', '74', 'Data Type');
insert into QDS_ATTRIBUTES values('634', 'patient preference', '74', 'Data Type');
insert into QDS_ATTRIBUTES values('635', 'provider preference', '74', 'Data Type');
insert into QDS_ATTRIBUTES values('636', 'severity', '74', 'Data Type');
insert into QDS_ATTRIBUTES values('637', 'start datetime', '74', 'Data Type');
insert into QDS_ATTRIBUTES values('638', 'status', '74', 'Data Type');
insert into QDS_ATTRIBUTES values('639', 'stop datetime', '74', 'Data Type');
insert into QDS_ATTRIBUTES values('640', 'duration', '75', 'Data Type');
insert into QDS_ATTRIBUTES values('641', 'negation rationale', '75', 'Data Type');
insert into QDS_ATTRIBUTES values('642', 'start datetime', '75', 'Data Type');
insert into QDS_ATTRIBUTES values('643', 'stop datetime', '75', 'Data Type');
insert into QDS_ATTRIBUTES values('644', 'duration', '76', 'Data Type');
insert into QDS_ATTRIBUTES values('645', 'negation rationale', '76', 'Data Type');
insert into QDS_ATTRIBUTES values('646', 'patient preference', '76', 'Data Type');
insert into QDS_ATTRIBUTES values('647', 'provider preference', '76', 'Data Type');
insert into QDS_ATTRIBUTES values('648', 'start datetime', '76', 'Data Type');
insert into QDS_ATTRIBUTES values('649', 'stop datetime', '76', 'Data Type');
insert into QDS_ATTRIBUTES values('650', 'duration', '77', 'Data Type');
insert into QDS_ATTRIBUTES values('651', 'negation rationale', '77', 'Data Type');
insert into QDS_ATTRIBUTES values('652', 'patient preference', '77', 'Data Type');
insert into QDS_ATTRIBUTES values('653', 'provider preference', '77', 'Data Type');
insert into QDS_ATTRIBUTES values('654', 'start datetime', '77', 'Data Type');
insert into QDS_ATTRIBUTES values('655', 'stop datetime', '77', 'Data Type');
insert into QDS_ATTRIBUTES values('656', 'Source - Device', '', 'Data Flow');
insert into QDS_ATTRIBUTES values('657', 'Source - Informant', '', 'Data Flow');
insert into QDS_ATTRIBUTES values('659', 'Recorder - Informant', '', 'Data Flow');
insert into QDS_ATTRIBUTES values('660', 'Recorder - Device', '', 'Data Flow');
insert into QDS_ATTRIBUTES values('661', 'Setting', '', 'Data Flow');
insert into QDS_ATTRIBUTES values('662', 'Health Record Field', '', 'Data Flow');

/* INSERT ATTRIBUTE DETAILS */
INSERT INTO ATTRIBUTE_DETAILS VALUES ('1','anatomical location','91723000','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('2','cumulativeMedicationDuration','363819003','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('3','dose','398232005','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('4','duration','','','','S(I)/S(I)','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('5','durationfromarrival','','','','S(I)/P(I)','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('6','frequency','260864003','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('7','hospital location','','','','P','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('8','infusionDuration','36576007','2.16.840.1.113883.6.96','SNOMED-CT','S(I)/S(I)','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('9','method','414679000','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('10','negation rationale','','','','','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('11','number','107651007','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('12','ordinality','117363000','2.16.840.1.113883.6.96','SNOMED-CT','S','SUBJ');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('13','patient preference','PAT','2.16.840.1.113883.5.8','HL7 ActReason','S','RSON');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('14','provider preference','103323008','2.16.840.1.113883.6.96','SNOMED-CT','S','RSON');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('15','radiationdosage','218190002','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('16','radiationduration','218190002','2.16.840.1.113883.6.96','SNOMED-CT','S(I)/S(I)','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('17','reaction','ASSERTION','2.16.840.1.113883.5.4','Clinical Observation Assertion','S','MFST');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('18','reason','410666004','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('19','refills','','','','S','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('20','result','385676005','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('21','route','263513008','2.16.840.1.113883.6.96','SNOMED-CT','S','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('22','severity','SEV','2.16.840.1.113883.5.4','HL7 Severity observation','S','SUBJ');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('23','start datetime','','','','I','');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('24','status','33999-4','2.16.840.1.113883.6.1','LOINC','S','REFR');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('25','stop datetime','','','','I','');

INSERT INTO ATTRIBUTE_DETAILS VALUES ('26','Source - Device','','','','P','');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('27','Source - Informant','','','','P','');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('28','Recorder - Informant','','','','P','');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('29','Recorder - Device','','','','P','');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('30','Setting','','','','P','');
INSERT INTO ATTRIBUTE_DETAILS VALUES ('31', 'Health Record Field','','','','AC','');

				
				/*update to a category table */

				UPDATE CATEGORY SET ABBREVIATION = 'ATT' WHERE DESCRIPTION = 'Attribute';

				/* UPDATE to a datatype row */

				UPDATE DATA_TYPE SET DESCRIPTION = 'Procedure,Result' WHERE DESCRIPTION = 'procedure result';

				/* Adding a column to the code_system table called Abbreviation */

				ALTER TABLE CODE_SYSTEM ADD COLUMN ABBREVIATION VARCHAR(32) NULL ;
				
				/* UPDATE to codeSystem Table rows */

				UPDATE CODE_SYSTEM SET ABBREVIATION =  'I9' WHERE DESCRIPTION = 'I9';

				UPDATE CODE_SYSTEM SET ABBREVIATION =  'I10' WHERE DESCRIPTION = 'I10';

				UPDATE CODE_SYSTEM SET ABBREVIATION =  'CPT' WHERE DESCRIPTION = 'CPT';

				UPDATE CODE_SYSTEM SET ABBREVIATION =  'LNC' WHERE DESCRIPTION = 'Loinc';

				UPDATE CODE_SYSTEM SET ABBREVIATION =  'HCP' WHERE DESCRIPTION = 'HCPCS';

				UPDATE CODE_SYSTEM SET ABBREVIATION =  'HL7' WHERE DESCRIPTION = 'HL7';

				UPDATE CODE_SYSTEM SET ABBREVIATION =  'RxN' WHERE DESCRIPTION = 'RxNorm';

				UPDATE CODE_SYSTEM SET ABBREVIATION =  'CVX' WHERE DESCRIPTION = 'CVX';

				UPDATE CODE_SYSTEM SET ABBREVIATION =  'SNM' WHERE DESCRIPTION = 'SNOMED';

				UPDATE CODE_SYSTEM SET ABBREVIATION =  'GRP' WHERE DESCRIPTION = 'Grouping';

				UPDATE CODE_SYSTEM SET ABBREVIATION =  'NQF' WHERE DESCRIPTION = 'NQF';
			
				/* Add a column for holding a custom name. */
				ALTER TABLE CLAUSE ADD COLUMN CUSTOM_NAME VARCHAR(100) NULL DEFAULT NULL  AFTER NAME;
				
				/* Add the default steward organization. */
	            INSERT INTO STEWARD_ORG VALUES ('80', 'National Quality Forum','');

				/* Add the new Steward organizations for beta testing */
				INSERT INTO STEWARD_ORG VALUES ('81', 'Joint Commission','');
				INSERT INTO STEWARD_ORG VALUES ('82', 'Oklahoma Foundation for Medical Quality','');
				INSERT INTO STEWARD_ORG VALUES ('83', 'American Board of Internal Medicine','');

				/* Add the default Author. */
				INSERT INTO AUTHOR VALUES ('81', 'National Quality Forum');

				/* Add new Author organizations for beta testing. */
				INSERT INTO AUTHOR VALUES ('82', 'Joint Commission');
				INSERT INTO AUTHOR VALUES ('83', 'Oklahoma Foundation for Medical Quality');
				INSERT INTO AUTHOR VALUES ('84', 'American Board of Internal Medicine');

				/* Add associate all steward organizations that are being removed to the default organization in Code list table.  */
				UPDATE LIST_OBJECT SET STEWARD = '80' WHERE STEWARD NOT IN ('14', '29', '55');

				/* Add associate all steward organizations that are removed to default NQF in the metadata table.  */
				UPDATE METADATA SET value = 'National Quality Forum' where name= 'MeasureSteward' and value NOT IN ('Cleveland Clinic', 'National Committee for Quality Assurance', 'American Medical Association - Physician Consortium for Performance Improvement');

				/* Add associate all authors that are being removed to the default author in the metadata table.  */
				UPDATE METADATA SET value = 'National Quality Forum' where name= 'Author' and value NOT IN ('Cleveland Clinic', 'National Committee for Quality Assurance', 'American Medical Association - Physician Consortium for Performance Improvement');

				/* Remove unnecessary Steward organizations and Authors entries. */
				DELETE FROM STEWARD_ORG WHERE ID NOT IN ('14', '29', '55', '80', '81', '82', '83');
				DELETE FROM AUTHOR WHERE ID NOT IN ('14', '29', '55', '80', '81', '82', '83', '84');
				
				/* Add Kaiser Permanente to the beta users list */
	            INSERT INTO STEWARD_ORG VALUES ('84', 'Kaiser Permanente','');
				INSERT INTO AUTHOR VALUES ('85', 'Kaiser Permanente');
				
COMMIT;