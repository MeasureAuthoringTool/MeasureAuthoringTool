--- this script is now executed from ppc_1_5_patchrun          ------------------
--- Do not run this directly                                   ------------------
---------------------------------------------------------------------------------
-- PATCH:   CMS MAT release DB patch 1.0
-- PROJECT: CMS MAT
-- Created: 09/01/2013
-- RUN SCRIPT AS DBA.
-----------------------
-- Set up environment
-----------------------
-- set verify on;
-- set define ON;
-- SET TIMING ON;
-- set echo off;

-- alter session set nls_date_format = 'mm/dd/yyyy';

-- set serveroutput on size 1000000;  
-- WHENEVER SQLERROR CONTINUE;

-----------------
-- Spool output
-----------------
-- whenever oserror exit failure
-- column sdate new_value v_date
-- select to_char(sysdate , 'yyyymmdd_hh24miss') as sdate from dual;

-- spool patchrun_logs\&_connect_identifier._MAT_&v_date._run.log;

--------------------------- 
-- Record User information
--------------------------- 
-- select user,global_name from global_name;

SELECT TO_CHAR(SYSDATE,'MM/DD/YYYY HH24:MI:SS') "CMS-MAT release 1.0" FROM DUAL;

prompt ++++++ Create Login Id Column updates ++++++ 1_0
start LoginID_Script.sql

prompt ++++++ One Time Email Scheduler ++++++ 1_0
start OneTimeScheduler.sql

---------------------------
-- Create Patch Log Entry
---------------------------
SELECT TO_CHAR(SYSDATE,'MM/DD/YYYY HH24:MI:SS') "SCRIPT CMS-MAT 1.0" FROM DUAL;
INSERT INTO IFMC_COMMON.PATCH_LOG (PATCH_NAME,INSTALL_DT,SCHEMA_NAME,DESCRIPTION)
VALUES ('CMS-MAT DB 1.0',SYSDATE,user,'CMS-MAT 1.0 - Application release 1.0');

-------------
-- End Patch
-------------
SELECT TO_CHAR(SYSDATE,'MM/DD/YYYY HH24:MI:SS') "CMS-MAT 1.0 Ended" FROM DUAL;
commit work;

-- Prompt type "exit" to complete database updates;

--  spool off;
