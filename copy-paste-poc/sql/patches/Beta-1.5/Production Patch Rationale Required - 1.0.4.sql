-- updates all the codeList with 'N/A' where the RATIONALE field is NULL
UPDATE `MAT_APP`.`LIST_OBJECT`  SET RATIONALE = 'N/A' WHERE RATIONALE is NULL;

-- updates all the codeList with 'N/A' where the RATIONALE Field is having empty String or whiteSpace
UPDATE `MAT_APP`.`LIST_OBJECT`  SET RATIONALE = 'N/A' WHERE trim(RATIONALE)=''; 

-- Altering the table list_object to Modify Rationale field to be NOT NULL
ALTER TABLE `MAT_APP`.`LIST_OBJECT`  MODIFY `RATIONALE` VARCHAR(2000) NOT NULL DEFAULT 'N/A';



