INSERT INTO MAT_APP.STEWARD_ORG VALUES ('80', 'National Quality Forum','');
INSERT INTO MAT_APP.STEWARD_ORG VALUES ('81', 'Joint Commission','');
INSERT INTO MAT_APP.STEWARD_ORG VALUES ('82', 'Oklahoma Foundation for Medical Quality','');
INSERT INTO MAT_APP.STEWARD_ORG VALUES ('83', 'American Board of Internal Medicine','');
-- Add default Steward organizations.
INSERT INTO MAT_APP.AUTHOR VALUES ('81', 'National Quality Forum');

-- Add new Steward organizations.
INSERT INTO MAT_APP.AUTHOR VALUES ('82', 'Joint Commission');
INSERT INTO MAT_APP.AUTHOR VALUES ('83', 'Oklahoma Foundation for Medical Quality');
INSERT INTO MAT_APP.AUTHOR VALUES ('84', 'American Board of Internal Medicine');

-- Add associate all steward organizations that are removed to default NQF in Code list table.
UPDATE MAT_APP.LIST_OBJECT SET STEWARD = '80' WHERE STEWARD NOT IN ('14', '29', '55');

-- Add associate all steward organizations that are removed to default NQF in metadata table.
UPDATE MAT_APP.METADATA SET value = 'National Quality Forum' where name= 'MeasureSteward' and value NOT IN ('Cleveland Clinic', 'National Committee for Quality Assurance', 'American Medical Association - Physician Consortium for Performance Improvement');

-- Add associate all authors that are removed to default 'NQF' in metadata table.
UPDATE MAT_APP.METADATA SET value = 'National Quality Forum' where name= 'Author' and value NOT IN ('Cleveland Clinic', 'National Committee for Quality Assurance', 'American Medical Association - Physician Consortium for Performance Improvement');

-- Remove unnecessary Steward organizations and Authors entries.
DELETE FROM MAT_APP.STEWARD_ORG WHERE ID NOT IN ('14', '29', '55', '80', '81', '82', '83');
DELETE FROM MAT_APP.AUTHOR WHERE ID NOT IN ('14', '29', '55', '80', '81', '82', '83', '84');
