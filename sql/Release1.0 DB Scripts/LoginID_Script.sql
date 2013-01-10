CREATE TABLE USER_BACKUP AS (SELECT * FROM USER);
				  ALTER TABLE USER ADD COLUMN `LOGIN_ID` VARCHAR(45) NULL  AFTER `ROOT_OID`, ADD UNIQUE INDEX `LOGIN_ID_UNIQUE` (`LOGIN_ID` ASC) ;
				  ALTER TABLE USER ADD COLUMN `TEMP_AUTO_INCR` INT(4) ZEROFILL NOT NULL AUTO_INCREMENT  AFTER `LOGIN_ID`, ADD UNIQUE INDEX `TEMP_AUTO_INCR_UNIQUE` (`TEMP_AUTO_INCR` ASC) ;
				  UPDATE USER SET LOGIN_ID =CONCAT(SUBSTRING(FIRST_NAME,1,2),SUBSTRING(LAST_NAME,1,6),TEMP_AUTO_INCR) WHERE LOGIN_ID IS NULL;
				  ALTER TABLE USER DROP COLUMN `TEMP_AUTO_INCR`, DROP INDEX `TEMP_AUTO_INCR_UNIQUE` ;